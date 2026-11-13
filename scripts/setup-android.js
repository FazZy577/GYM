// Se ejecuta despues de "cap add android": copia el codigo nativo y ajusta el manifiesto.
const fs = require('fs'), path = require('path');
const pkg = 'com.diariogym.app', base = 'android/app/src/main';
const jd = path.join(base, 'java', ...pkg.split('.'));
fs.mkdirSync(jd, { recursive: true });
['MainActivity', 'WorkoutService', 'WorkoutServicePlugin'].forEach(f =>
  fs.copyFileSync('native/' + f + '.java', path.join(jd, f + '.java')));
fs.mkdirSync(path.join(base, 'res/drawable'), { recursive: true });
fs.copyFileSync('native/ic_stat_gym.xml', path.join(base, 'res/drawable/ic_stat_gym.xml'));
const mp = path.join(base, 'AndroidManifest.xml');
let m = fs.readFileSync(mp, 'utf8');
if (!m.includes('WorkoutService')) {
  m = m.replace('</application>',
    '<service android:name=".WorkoutService" android:exported="false" android:foregroundServiceType="specialUse">' +
    '<property android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE" android:value="Cronometro de entreno en curso"/>' +
    '</service></application>');
  m = m.replace('</manifest>',
    '<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>' +
    '<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE"/>' +
    '<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/></manifest>');
  fs.writeFileSync(mp, m);
}
console.log('Android listo');
