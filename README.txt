HARDIK TV LAUNCHER — Android project source

This is an Android Studio/AndroidIDE project source, not a compiled APK.
It registers the app as a HOME candidate and Android TV Leanback app. After building/installing, choose HARDIK TV Launcher when Android asks which Home app to use.

BUILD USING AN ANDROID PHONE (AndroidIDE)
1. Install AndroidIDE from its official project/release page. Do not download random APK build sites.
2. Extract this ZIP on your phone.
3. In AndroidIDE, open/import the extracted HARDIK_TV_Android_Project folder as a Gradle project.
4. Allow AndroidIDE to install its required SDK/build tools and Gradle dependencies.
5. Build > Assemble/Build APK (debug APK).
6. Install the generated app-debug.apk on the TV or transfer it to the TV and install, allowing installs from that file manager if Android prompts.
7. Press Home on the TV. If a chooser appears, select HARDIK TV Launcher. Select Always only if you want to make it default. If no chooser appears, use Settings > Apps > Default apps/Home app if your TV exposes that setting.

NOTES / LIMITATIONS
- This project has not been compiled or tested here; this workspace has no Android SDK/Gradle build tools, so no APK is included.
- Current interface is a WebView wrapper around the existing blue HTML prototype. App tiles and music links may be illustrative; this is not yet a fully native installed-app catalogue.
- Google TV firmware may restrict third-party HOME selection. Do not disable or uninstall the stock launcher; keep it as recovery option.
