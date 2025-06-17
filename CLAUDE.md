# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

StreamPlayerApp is an open-source Netflix clone Android application built as a learning platform for the CodandoTV community. The app implements modern Android development practices with clean architecture, Jetpack Compose, and KARTE SDK integration for analytics.

## Architecture

The project follows a multi-module clean architecture pattern:

### Module Structure
- **app**: Main application module containing Application class and navigation setup
- **feature-***: Feature modules (list-streams, profile, favorites) following MVVM + Clean Architecture
- **core-***: Shared modules (navigation, networking, shared-ui, local-storage, shared)
- **build-logic**: Custom Gradle plugins and build configuration

### Key Technologies
- **UI**: Jetpack Compose with Navigation Compose
- **DI**: Koin for dependency injection with annotations
- **Networking**: Retrofit + Moshi + OkHttp
- **Database**: Room for local storage
- **Testing**: JUnit4, MockK, Coroutines Test
- **Analytics**: KARTE SDK (already integrated)
- **Build**: Custom Gradle plugins with version catalogs
- **CI/CD**: Fastlane for automation

## Common Development Commands

### Build Commands
```bash
# Clean and build debug
./gradlew clean assembleDebug

# Clean and build release
./gradlew clean assembleRelease

# Run tests
./gradlew test

# Run lint/static analysis (Detekt)
./gradlew detekt

# Generate coverage report
./gradlew koverHtmlReport

# Run specific module tests
./gradlew :feature-list-streams:test

# KSP compilation for Koin annotations
./gradlew kspDebugKotlin
```

### Fastlane Commands (if available)
```bash
# Run all CI checks (lint + test + debug build)
bundle exec fastlane ci

# Run tests only
bundle exec fastlane test

# Run lint only
bundle exec fastlane lint

# Build debug
bundle exec fastlane debug

# Build release
bundle exec fastlane release
```

## Development Guidelines

### Module Dependencies
- Feature modules should only depend on core modules, never other features
- Use `projects.featureName` syntax for module dependencies in build.gradle.kts (e.g., `projects.coreNavigation`)
- Core modules should be framework-agnostic where possible
- All modules use type-safe project accessors enabled in settings.gradle.kts

### Code Style
- Kotlin-first approach with extensive use of extension functions
- Follow existing naming conventions (e.g., `StreamPlayerApp`, `feature_module_name`)
- Use Compose for all new UI development
- Repository pattern for data access with Use Cases for business logic
- Package structure: `data/domain/presentation` layers within each feature
- UI State management with sealed classes ending in `UIState`

### Testing
- Unit tests for ViewModels, Use Cases, and Repositories
- Use MockK for mocking dependencies
- Coroutines testing utilities for async operations
- Test files follow naming convention: `ClassNameTest.kt`
- Test utilities in shared test packages (e.g., `InstantTaskCoroutinesExecutorRule`)

## Project Structure Notes

- **Navigation**: Centralized in `core-navigation` with type-safe route definitions and bottom navigation
- **Theming**: Material3 theme in `core-shared-ui` with custom colors and components
- **DI Setup**: Koin modules defined per feature with aggregation in main app module
- **Build Logic**: Custom Gradle plugins in `build-logic` for consistent module configuration
- **API Configuration**: Build config fields for different environments in `Config.kt`
- **External APIs**: TMDB for movie data, mockable.io for profile data
- **Version Management**: Centralized in `gradle/libs.versions.toml` with version catalogs

## Environment Setup

### Required Configuration
- **KARTE_APP_KEY**: Add to `gradle.properties` for KARTE SDK
- **TMDB_BEARER_TOKEN_DEBUG/RELEASE**: Environment variables for TMDB API (fallback token included)

### API Endpoints
- **Movies**: The Movie Database (TMDB) API v3
- **Profiles**: Demo mockable.io endpoint


## KARTE for App (Android SDK v2) ― Integration Guide

> **目的**: Gradle プラグインなしで KARTE Core を導入し、`view` / `cart` / `buy` イベントを Jetpack Compose 及び XML (Activity / Fragment) 画面で実装するための最小手順。Claude-code 等の AI ツールで自動生成するテンプレとしても利用できます。

---

## Quick Integration Checklist

1. **依存関係を追加** – `io.karte.android:core:<latest>` のみで OK。
2. **SDK 初期化** – `Application` で `KarteApp.setup()` を一度呼ぶ。
3. **イベント送信 API** – `Tracker.view()` / `Tracker.track("cart")` / `Tracker.track("buy")`。
4. **Jetpack Compose** – 画面遷移は `navigation-compose`、`LaunchedEffect(Unit)` で一度だけ送信。
5. **XML (Activity/Fragment)** – `onResume()` で `view` を 1 回送信。
6. **ベストプラクティス** – `view_name` を一意に、重複送信を防止、WebView は連携機能を活用。

---

## 1. Add KARTE Core Dependency

```groovy
// app/build.gradle.kts
dependencies {
    implementation("io.karte.android:core:2.30.0") // 最新を確認
}
```

> Visual Tracking を使う場合のみプラグインを追加。通常のイベント計測には不要。

---

## 2. SDK Initialization
アプリ固有のApplicationの

例
```kotlin
// MainApplication.kt
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KarteApp.setup(this, "YOUR_PROJECT_KEY") // 管理画面で発行
    }
}
```

`AndroidManifest.xml` の `<application>` に `android:name=".MainApplication"` を忘れずに設定。

---

## 3. Event APIs (Kotlin)

| 種別        | メソッド                    | 必須パラメータ                     | 例                                                                       |
| --------- | ----------------------- | --------------------------- | ----------------------------------------------------------------------- |
| **画面表示**  | `Tracker.view()`        | `viewName`, `title`         | `Tracker.view("home", "Home")`                                          |
| **カート追加** | `Tracker.track("cart")` | `product_id`, `quantity`    | `Tracker.track("cart", mapOf("product_id" to id, "quantity" to 1))`     |
| **購入完了**  | `Tracker.track("buy")`  | `transaction_id`, `revenue` | `Tracker.track("buy", mapOf("transaction_id" to tx, "revenue" to amt))` |

`view` / `cart` / `buy` は KARTE の **定義済みイベント**。名前を変えずに送信すること。

---

## 4. Jetpack Compose Implementation Guide

### 4‑1. Navigation との合わせ技

`navigation-compose` の `NavHost` で `route` が変わったタイミングで `view` を送ります。

```kotlin
@Composable
fun AppNavGraph(start: String = "home") {
    val navController = rememberNavController()

    NavHost(navController, startDestination = start) {
        composable("home")        { HomeScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            DetailScreen(id = backStackEntry.arguments?.getString("id")!!)
        }
    }
}
```

### 4‑2. `LaunchedEffect` で一度だけ送信

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Tracker.view("home", "Home")
    }
}
```

### 4‑3. カート／購入イベントは ViewModel で集約

ViewModel 経由で `Tracker.track()` を呼ぶとテストが容易。

### 4‑4. Legacy View Implementation Guide (Fragments / Activities)

#### One‑Activity Multi‑Fragment

`Tracker.view()` は各 Fragment の **`onResume()`** で呼びます。

```kotlin
class CatalogFragment : Fragment(R.layout.fragment_catalog) {
    override fun onResume() {
        super.onResume()
        Tracker.view("catalog", "Catalog")
    }

    private fun onAddToCart(sku: String) {
        Tracker.track("cart", mapOf("product_id" to sku, "quantity" to 1))
    }
}
```

> **Back‑stack Duplication**: バックスタック復帰時に二重送信しないよう、最後に送った `viewName` を保持して比較するガードを入れると安全です。

#### Activity‑only Pattern (1 screen = 1 Activity)

```kotlin
class CheckoutActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        Tracker.view("checkout", "Checkout")
    }

    private fun completePurchase(tx: String, amount: Int) {
        Tracker.track(
            "buy",
            mapOf(
                "transaction_id" to tx,
                "revenue"        to amount
            )
        )
    }
}
```

> **TabLayout + ViewPager2** などのタブ UI では、タブ選択時に `view` を送って “画面” と一致させます。

---

## 5. Best Practices & Pitfalls

| シーン                   | 推奨パターン                                      | 理由                   |
| --------------------- | ------------------------------------------- | -------------------- |
| **`view` 多重発火**       | `LaunchedEffect(Unit)` / `onResume()` で 1 回 | イベント数上限・ポップアップ誤制御を防止 |
| **WebView 内遷移**       | SDK の WebView 連携で `view` を補完                | ネイティブと Web のユーザー統合   |
| **場所**       | 一旦おけるところ全てにおくこと               | あとで書き直しが面倒   |
| **`cart` / `buy` 重複** | `product_id` / `transaction_id` をキーに排重      | 売上計測の二重カウント防止        |
| **Visual Tracking**   | 必要な場合のみプラグインを追加                             | ビルド時間・設定漏れを防ぐ        |

---

## 6. Testing Tips

1. KARTE 管理画面 > **リアルタイムユーザー** でイベント到達を確認。
2. デバッグビルドでは `KarteApp.setLogLevel(DEBUG)` で送信内容を Logcat 出力。

---

