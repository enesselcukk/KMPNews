# KMPNews

A **Kotlin Multiplatform** news app for **SDH — Son Dakika Haber**, targeting **Android**, **iOS**, and **Desktop (JVM)** from a single shared codebase.

Live news data is fetched from [News API](https://newsapi.org/); the UI is built with **Jetpack Compose Multiplatform**.

---

## Screenshots

### Android

![Android Home Screen](docs/screenshots/android/home.png)

### iOS

![iOS Home Screen](docs/screenshots/ios/home.png)

### Desktop

![Desktop Home Screen](docs/screenshots/desktop/home.png)

> Add your images under `docs/screenshots/android/`, `docs/screenshots/ios/`, and `docs/screenshots/desktop/`. File names should match the paths above (`home.png`).

---

## Features

- **Multiplatform** — shared UI and business logic for Android, iOS, and Desktop
- **Live news feed** — Turkish sports news via News API
- **Headline carousel** — horizontal pager for featured articles
- **Image loading** — remote article images with Coil 3
- **Material 3 theme** — SDH brand colors (orange / navy), light & dark mode
- **Localization** — TR / EN string resources via Compose Multiplatform Resources
- **Modular architecture** — feature-based layers (contract, domain, data, presentation)
- **Dependency injection** — Koin for platform and feature module registration
- **Type-safe navigation** — Navigation Compose with contract modules

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin **2.4.10** |
| UI | Compose Multiplatform **1.11.1**, Material 3 |
| Architecture | Clean / layered architecture, MVVM |
| DI | Koin **4.2.2** |
| Networking | Ktor Client **3.5.2**, kotlinx.serialization |
| Images | Coil **3.5.0** (Ktor network fetcher) |
| Navigation | Navigation Compose **2.9.2** |
| Lifecycle | AndroidX Lifecycle **2.9.6** (ViewModel, runtime-compose) |
| Data | Room 3, SQLite, Multiplatform Settings |
| Localization | Compose Resources (`composeResources`) |
| Build | Gradle **9.x**, AGP **9.0.1**, Convention Plugins (`build-logic`) |
| Targets | Android (minSdk 24), iOS (Arm64 / Simulator), JVM Desktop |

---

## Architecture

The app uses a modular structure that separates responsibilities per feature:

```
┌─────────────────────────────────────────────────────────┐
│  app/androidApp · app/iosApp · app/desktopApp           │
│                    (platform entry)                      │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│  app/shared          KmpNewsApp, Koin, NavHost           │
│  app/ui-components   KmpNewsTheme, design system         │
└──────────────────────────┬──────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────────┐
│ feature/home  │  │ feature/detail│  │ core/*            │
│ contract      │  │ ...           │  │ network, domain,  │
│ domain        │  │               │  │ navigation, db... │
│ data          │  │               │  │                   │
│ presentation  │  │               │  │                   │
└───────────────┘  └───────────────┘  └───────────────────┘
```

**Data flow (Home):**

`HomeScreen` → `HomeViewModel` → `HomeNewsUseCase` → `HomeRepository` → `HomeApi` → News API

---

## Project Structure

```
KMPNews/
├── app/
│   ├── androidApp/       # Android application module
│   ├── desktopApp/       # Desktop (JVM) application module
│   ├── iosApp/           # iOS Xcode project + Kotlin bridge
│   ├── shared/           # Shared entry point (KmpNewsApp, Koin)
│   └── ui-components/    # Theme, typography, color palette
├── core/
│   ├── model/            # Shared models
│   ├── domain/           # Result types, use case base
│   ├── network/          # Ktor HttpClient, News API config
│   ├── database/         # Room / SQLite
│   ├── datastore/        # Settings (Multiplatform Settings)
│   ├── navigation/       # NavigationManager, NavGraphProvider
│   └── presentation/     # CoreViewModel, shared presentation helpers
├── feature/
│   ├── home/             # Home screen (contract · domain · data · presentation)
│   └── detail/           # Article detail (work in progress)
├── build-logic/          # Gradle convention plugins
└── docs/screenshots/     # README screenshots
```

---

## Requirements

- **JDK 11+**
- **Android Studio** Ladybug or newer (for Android)
- **Xcode 15+** (for iOS, macOS only)
- **News API key** — free registration at [newsapi.org](https://newsapi.org/register)

---

## Setup

1. Clone the repository:

```bash
git clone https://github.com/<username>/KMPNews.git
cd KMPNews
```

2. Configure your News API key:

Update the `API_KEY` value in `core/network/src/commonMain/kotlin/com/example/kmpnews/core/network/config/NewsApiConfig.kt`.

3. Sync the project:

```bash
./gradlew tasks
```

---

## Running the App

### Android

```bash
./gradlew :app:androidApp:installDebug
```

Or run the `app/androidApp` module from Android Studio.

### Desktop

```bash
./gradlew :app:desktopApp:run
```

Hot reload (when supported):

```bash
./gradlew :app:desktopApp:hotRun --auto
```

### iOS

1. Open `app/iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or physical device
3. Run

Build via Gradle:

```bash
./gradlew :app:shared:embedAndSignAppleFrameworkForXcode
```

---

## Testing

```bash
# Android unit tests
./gradlew :app:shared:testAndroidHostTest

# Desktop (JVM) tests
./gradlew :app:shared:jvmTest

# iOS simulator tests
./gradlew :app:shared:iosSimulatorArm64Test
```

---

## Localization

String resources are managed with Compose Multiplatform Resources:

```
feature/home/presentation/src/commonMain/composeResources/
├── values/strings.xml       # Default (EN)
└── values-tr/strings.xml    # Turkish
```

Usage in code:

```kotlin
Text(stringResource(Res.string.home_headlines))
```

The system locale automatically selects the matching `values-*` folder.

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is for educational / portfolio purposes. License to be added.

---

## Contact

**Enes** — project author

Repository: `https://github.com/<username>/KMPNews`
