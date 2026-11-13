package com.diariogym.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

/** Servicio en primer plano: mantiene fija la notificacion "Entreno en curso" con cronometro. */
public class WorkoutService extends Service {
    static final String CH = "entreno";

    @Override public IBinder onBind(Intent i) { return null; }

    @Override public int onStartCommand(Intent i, int flags, int startId) {
        SharedPreferences p = getSharedPreferences("w", MODE_PRIVATE);
        long t; String n;
        if (i != null && i.hasExtra("t")) {
            t = i.getLongExtra("t", 0);
            n = i.getStringExtra("n");
            p.edit().putLong("t", t).putString("n", n).apply();
        } else { // reinicio por parte del sistema: recupera el entreno guardado
            t = p.getLong("t", 0);
            n = p.getString("n", "");
        }
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel c = new NotificationChannel(CH, "Entreno en curso", NotificationManager.IMPORTANCE_LOW);
            c.setShowBadge(false);
            nm.createNotificationChannel(c);
        }
        Intent o = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, o, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        Notification nt = new NotificationCompat.Builder(this, CH)
            .setSmallIcon(R.drawable.ic_stat_gym)
            .setColor(0xFFC98A1B)
            .setContentTitle("Entreno en curso")
            .setContentText((n == null || n.isEmpty() ? "" : n + " · ") + "Toca para volver y finalizar")
            .setUsesChronometer(true)
            .setWhen(t == 0 ? System.currentTimeMillis() : t)
            .setShowWhen(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(pi)
            .build();
        if (Build.VERSION.SDK_INT >= 34) startForeground(1, nt, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        else startForeground(1, nt);
        if (t == 0) { stopSelf(); return START_NOT_STICKY; }
        return START_STICKY;
    }
}
