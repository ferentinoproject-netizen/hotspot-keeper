# Hotspot Keeper — Setup Guide

## Online widihට APK eka build karanna (Android Studio oni na!)
1. https://github.com yanna, account ekක් නෑ nam free ekක් hදaganna.
2. Aluත් **repository** ekක් hදaganna (e.g. name: `hotspot-keeper`), **Public** widihට, README add karanna epa (blank).
3. Meම zip eke files ම (folders inclusive — `app/`, `.github/`, `build.gradle`, `settings.gradle`, `gradle.properties`) e repo ekට upload karanna:
   - Repo eke "Add file > Upload files" eken, zip eka **extract karapු passe** ehidhi files/folders okkoma drag karanna (zip file eka ම upload karanna epa, extract karapu files witharai).
4. Upload wela ithuru unoth, repo eke uda **"Actions"** tab eka click karanna.
5. "Build APK" workflow eka pennanawa nam, e uda click karala **"Run workflow"** button eka press karanna (green button, right side).
6. ~3-5 minutes ithule build eka run wenawa (green tick ekක් pennanwa passe eka success). Click karala e run eka open karanna.
7. Pahalට **"Artifacts"** kiyala section ekක් thiyenawa — `HotspotKeeper-debug-apk` kiyala zip ekක් download karaganna.
8. E zip eke ithule `app-debug.apk` file eka thiyenawa — eka phone ekට transfer karala (WhatsApp/Google Drive/email/USB), open karala install karanna ("Unknown sources" / "Install unknown apps" allow karanna oni ehidhi phone eken අහයි).

Meka completely free, GitHub eke build eka run karanawa cloud eke (Ubuntu machine ekක් uda), oyage phone/PC ekට kisima install karanna oni නෑ.

## Meka mokakda karanne
Android eke security restrictions nisa, apps ta silently hotspot on karanna official API ekක් නෑ (Android 8+ ඉඳන්). Meya solve karanne **Accessibility Service** ekක් use karala — hotspot off unoth, Settings screen eka automatic open karala, toggle eka click karala, ආපහු app ekට යනවා. Meka Play Store ekට upload karanna බැ (accessibility misuse policy), ehet oyage phone ekට directly install (sideload) karaganna kisima prashnayක් නෑ.

## Android Studio eke set up karanna
1. Android Studio open karala **New Project > Empty Views Activity** hදන්න.
   - Package name: `com.hotspot.keeper` (mekම use karanna, files walata match wenna oni)
   - Minimum SDK: API 26 (Android 8.0)
   - Language: Kotlin
2. Auto-generated `MainActivity.kt` eka delete karala, mama දුන් files copy karanna:
   - `java/com/hotspot/keeper/*.kt` → oyage `app/src/main/java/com/hotspot/keeper/` folder ekට
   - `res/layout/activity_main.xml` → replace karanna existing eka
   - `res/xml/accessibility_service_config.xml` → `res/xml/` folder ekක් hදන්න (nathnam), meka copy karanna
   - `res/values/strings.xml` → merge karanna (app_name tiyena line eka replace karanna)
   - `AndroidManifest.xml` → mulinම replace karanna, ehet `android:icon` line eka gata bariwenawa nam ain karanna
3. `app/build.gradle` eke mekK check karanna (dependencies block eke thiyanna oni):
   ```
   implementation("androidx.appcompat:appcompat:1.6.1")
   implementation("androidx.core:core-ktx:1.12.0")
   ```
4. Build > Run (USB debugging on karala phone ekට connect karala, hරි APK build karala install karanna).

## Phone eke setup (app open karapu passe)
App eke pennana 3 buttons tikaම අනුපිළිවෙළට press karanna:
1. **Accessibility permission on karanna** — list eke "Hotspot Keeper" hoyaganna, on karanna.
2. **Battery optimization ignore karanna** — meka important, nathnam Android eka background service eka kill karayi.
3. **Start Always-On Hotspot** — meken monitoring eka start wenawa.

Ithin phone eka hotspot off unoth (someone turns it off, or after reboot), 3 minutes ithule automatically ආපහු on wෙනවා.

## Limitations (ඇත්තටම දැනගන්න ඕන දේවල්)
- Meka OEM-specific UI walata (Samsung One UI, Xiaomi MIUI wagේ) sensitive - switch eka hoyaganna node structure eka wenas wenna puluwan. Samsung/Xiaomi නම්, test කරලා switch එක හරියටම click වෙන්නේ නැත්නම් කියන්න, adjust කරන්න පුළුවන්.
- "Test" button eka press karala test karanna, hotspot eka already on nam short flash ekක් witharai wenne (settings open wela ආපහු yanawa).
- Battery optimization aggressive OEMs (MIUI, ColorOS) walata, service eka kill wenna puluwan real setup eke — "no restrictions" widihata app eka manually set karanna oni (battery settings walin).
