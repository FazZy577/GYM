# Diario Gym (APK)

App Android (Capacitor) con tu diario de gimnasio y notificacion fija "Entreno en curso" con cronometro.

## Opcion A: sin instalar nada (GitHub Actions)
1. Crea un repositorio en github.com (privado vale) y sube TODO el contenido de esta carpeta, incluida la carpeta oculta `.github`.
2. Ve a la pestana **Actions** -> **Build APK** -> **Run workflow** (tambien se lanza solo al subir a `main`).
3. Cuando termine (5-8 min), entra en la ejecucion y descarga el artefacto **diario-gym-apk** (zip con `app-debug.apk`).
4. Pasa el APK al movil e instalalo (permite "instalar apps desconocidas" cuando te lo pida).

## Opcion B: en tu PC
Necesitas Node 20, JDK 17 y el Android SDK (o Android Studio).
```
npm install
npm run setup
cd android && ./gradlew assembleDebug
```
APK: `android/app/build/outputs/apk/debug/app-debug.apk`

Si cambias `www/index.html`: `npm run sync` y vuelve a compilar.

## Notas
- Al abrir la app por primera vez, acepta el permiso de notificaciones (Android 13+).
- La notificacion se mantiene aunque cierres la app y solo desaparece al pulsar "Terminar entreno" (guardar) o "Descartar".
- Si tu movil mata apps en segundo plano (Xiaomi, Huawei, Samsung...), pon la app en "Sin restricciones" en Ajustes > Bateria.
- Actualiza siempre instalando el nuevo APK encima (misma firma debug en el mismo equipo/runner puede variar: si Android rechaza la actualizacion, exporta tus datos antes y reinstala).
