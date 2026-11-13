package com.diariogym.app;

import android.content.Intent;
import androidx.core.content.ContextCompat;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "WorkoutService")
public class WorkoutServicePlugin extends Plugin {
    @PluginMethod
    public void start(PluginCall call) {
        Long t = call.getLong("startTime", System.currentTimeMillis());
        String n = call.getString("title", "");
        Intent i = new Intent(getContext(), WorkoutService.class);
        i.putExtra("t", t.longValue());
        i.putExtra("n", n);
        ContextCompat.startForegroundService(getContext(), i);
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        getContext().getSharedPreferences("w", 0).edit().clear().apply();
        getContext().stopService(new Intent(getContext(), WorkoutService.class));
        call.resolve();
    }
}
