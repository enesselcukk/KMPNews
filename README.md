This is a Kotlin Multiplatform project targeting Android, iOS, and Desktop (JVM).

### Project structure

```
app/
  androidApp/     Android entry point
  desktopApp/     Desktop (JVM) entry point
  iosApp/         iOS entry point (Xcode + Kotlin bridge)
  shared/         Shared Compose UI + Koin DI (Android, Desktop, common)
  ui-components/  Design system (theme, components)
  screenshot/     App screenshots
core/             Data, network, database, navigation
feature/          Feature screens and ViewModels
```

### Running the apps

- Android: `./gradlew :app:androidApp:assembleDebug`
- Desktop:
  - Hot reload: `./gradlew :app:desktopApp:hotRun --auto`
  - Standard run: `./gradlew :app:desktopApp:run`
- iOS: open `app/iosApp` in Xcode and run from there.

### Running tests

- Android tests: `./gradlew :app:shared:testAndroidHostTest`
- Desktop tests: `./gradlew :app:shared:jvmTest`
- iOS tests: `./gradlew :app:shared:iosSimulatorArm64Test`
