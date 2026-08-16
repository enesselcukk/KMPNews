# KMPNews

A **Kotlin Multiplatform** news app for **SDH — Son Dakika Haber**, targeting **Android**, **iOS**, and **Desktop (JVM)** from a single shared codebase.

---

## Screenshots

### Android

<img width="120" height="262" alt="Screenshot_20260813_133010" src="https://github.com/user-attachments/assets/d7fadfbb-edd0-4942-8433-083ad97c2b5a" />


### iOS

<img width="120" height="262" alt="Simulator Screenshot - iPhone 17 - 2026-08-13 at 13 14 30" src="https://github.com/user-attachments/assets/733d428b-ade6-4092-9728-9060795a9153" />


### Desktop

<img width="1188" height="748" alt="Ekran Resmi 2026-08-13 13 15 51" src="https://github.com/user-attachments/assets/6684240e-e13a-4227-8ff6-6d13fd439328" />

---

## Features

- **Multiplatform** — shared UI and business logic for Android, iOS, and Desktop
- **Category tabs** — `general`, `business`, `entertainment`, `health`, `science`, `sports`, `technology`
- **Article detail** — hero image, metadata, summary, and full content with back navigation
- **Headline carousel** — horizontal pager for featured articles on the home screen
- **Image loading** — remote article images with Coil 3
- **Material 3 theme** — SDH brand colors (orange / navy), light & dark mode
- **Localization** — TR / EN string resources via Compose Multiplatform Resources
- **Modular architecture** — feature-based layers (contract, domain, data, presentation)
- **Dependency injection** — Koin for platform and feature module registration
- **Navigation 3** — user-owned back stack, modular `NavEntryProvider`, and ViewModel scoping per destination

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
| Navigation | Navigation 3 **1.1.1** (`NavDisplay`, `NavKey`, `rememberNavBackStack`) |
| Lifecycle | AndroidX Lifecycle **2.9.6** (ViewModel, runtime-compose, viewmodel-navigation3) |
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
│  app/shared          KmpNewsApp, Koin, KmpNewsNavHost     │
│  app/ui-components   KmpNewsTheme, design system         │
└──────────────────────────┬──────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────────┐
│ feature/home  │  │ feature/detail│  │ core/*            │
│ contract      │  │ contract      │  │ network, domain,  │
│ domain        │  │ domain        │  │ navigation, db... │
│ data          │  │ data          │  │                   │
│ presentation  │  │ presentation  │  │                   │
└───────────────┘  └───────────────┘  └───────────────────┘
```

**Data flow (Home):**

`HomeScreen` → `HomeViewModel` → `HomeNewsUseCase` → `HomeRepository` → `HomeApi` → News API `/v2/top-headlines`

**Data flow (Detail):**

`DetailScreen` → `DetailViewModel` → `GetArticleDetailUseCase` → `DetailRepository` → cached home feed / News API

**Navigation flow:**

`ViewModel` → `NavigationManager` → `NavBackStack` → `NavDisplay` → feature `NavEntryProvider`

Each feature registers its own `@Serializable` destinations and screen entries through Koin. Navigation state is saveable across configuration changes and process death on all KMP targets.

---

## Project Structure

```
KMPNews/
├── app/
│   ├── androidApp/       # Android application module
│   ├── desktopApp/       # Desktop (JVM) application module
│   ├── iosApp/           # iOS Xcode project + Kotlin bridge
│   ├── shared/           # Shared entry point (KmpNewsApp, Koin)
│   └── ui-components/    # Theme, typography, shared UI components
├── core/
│   ├── model/            # Shared models
│   ├── domain/           # Result types, use case base
│   ├── network/          # Ktor HttpClient, News API config
│   ├── database/         # Room / SQLite
│   ├── datastore/        # Settings (Multiplatform Settings)
│   ├── navigation/       # NavigationManager, KmpNewsNavHost, NavEntryProvider
│   └── presentation/     # CoreViewModel, shared presentation helpers
├── feature/
│   ├── home/             # Home feed, categories, headlines
│   └── detail/           # Article detail screen
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

## Localization

String resources are managed with Compose Multiplatform Resources:

```
feature/home/presentation/src/commonMain/composeResources/
├── values/strings.xml       # Default (EN)
└── values-tr/strings.xml    # Turkish

feature/detail/presentation/src/commonMain/composeResources/
├── values/strings.xml
└── values-tr/strings.xml
```

Usage in code:

```kotlin
Text(stringResource(Res.string.home_headlines))
```

The system locale automatically selects the matching `values-*` folder.

---

## Navigation

Navigation is built on **Navigation 3** with a modular provider pattern:

- Destinations implement `NavigationCommand.Destination` and `NavKey`
- Each feature exposes a `NavEntryProvider` (serializer registration + `entry<>` DSL)
- `KmpNewsNavHost` merges providers, restores the back stack, and wires `NavigationManager`
- ViewModels are scoped to navigation entries via `rememberViewModelStoreNavEntryDecorator()`

To add a new screen:

1. Define a `@Serializable` destination in the feature `contract` module
2. Register the route in a feature `NavEntryProvider`
3. Bind the provider in the feature Koin module

---
