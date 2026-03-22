# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`ratingbar-cmp` is a Kotlin Multiplatform Compose library providing a `RatingBar` composable for Android, Desktop (JVM), iOS, and Web (JS/IR). All library logic lives in `commonMain` — there are no platform-specific source sets in the library module.

## Build & Test Commands

```bash
# Full build (all platforms)
./gradlew build

# Platform-specific compilation
./gradlew compileAndroidMain
./gradlew compileKotlinDesktop
./gradlew compileKotlinJs

# Run tests (all shared tests run on desktop JVM)
./gradlew desktopTest
./gradlew jsTest
./gradlew assembleUnitTest   # Android compile check (no runtime tests)

# Lint & API compatibility
./gradlew detekt
./gradlew apiCheck

# Build artifacts
./gradlew bundleAndroidMainAar
./gradlew desktopJar
./gradlew publishToMavenLocal

# Pre-release validation
./scripts/release-check.sh
./scripts/release-check.sh --skip-ios        # On non-macOS machines
./scripts/release-check.sh --skip-samples
```

## Running Sample Apps

```bash
./gradlew -p samples :desktop:run
./gradlew -p samples :android:installDebug
./gradlew -p samples :web:jsBrowserDevelopmentRun   # Dev server at http://localhost:8080
```

iOS sample requires Xcode — open `samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj`.

## Architecture

### Module Layout
- **Root project** — Library module (KMP, all logic in `commonMain`, published from root for clean JitPack coordinate)
- **`samples/`** — Standalone Gradle composite build; `includeBuild("..")` substitutes the local library when `useLocalLibrary=true` (the default). Pass `-PuseLocalLibrary=false` to resolve from JitPack instead.
- **`samples/common/`** — Shared sample UI composable used by all platform launchers
- **`samples/{android,desktop,ios,web}/`** — Thin platform launchers
- **`samples/ios-app-host/`** — Xcode project wrapping the iOS Kotlin/Native framework

### Library Source Structure (`src/commonMain/`)
- `RatingBar.kt` — Public composables and gesture/interaction logic
- `core/RatingBarState.kt` — `RatingBarConfig` and `RatingBarState` (immutable value type, ViewModel-friendly)
- `RatingBarDefaults.kt` — Size/spacing/animation presets
- `RatingBarIcons.kt` — Built-in star vector painters
- `FractionalClipShape.kt` — Clip shape for partial star fills
- `RatingBarStateHelpers.kt` — State computation helpers

### Tests (`src/commonTest/`)
- `RatingBarStateTest.kt`, `RatingBarInteractionTest.kt`, `FractionalClipShapeTest.kt`
- Run on desktop JVM via `desktopTest`; no Compose UI tests (pure logic)

## AGP 9.x / Build System Notes

- **AGP plugin**: `com.android.kotlin.multiplatform.library` — do NOT add `kotlin-android`
- **No `testDebugUnitTest`** — use `assembleUnitTest` (compile check) and `desktopTest` (runtime)
- **Android compile task**: `compileAndroidMain` (not `compileDebugKotlinAndroid`)
- **AAR task**: `bundleAndroidMainAar` (not `assembleRelease`)
- **JVM target 17** — configured via `kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_17 } }`

## Key Configuration

- `gradle/libs.versions.toml` — All dependency versions
- `gradle.properties` — `org.gradle.jvmargs=-Xmx8192m` and `workers.max=2` are required to prevent OOM during iOS Kotlin/Native linking
- `detekt.yml` — Zero-tolerance linting (`maxIssues: 0`); Compose wildcard imports are excluded
- `useLocalLibrary` Gradle property — controls whether `samples/` resolve from local source (`true`, default) or JitPack (`false`). Pass via `-PuseLocalLibrary=false` to the samples build, or set in `samples/gradle.properties`. Run samples with `-p samples` flag from the repo root.
- Binary Compatibility Validator is applied; run `apiDump` to update `.api` files after public API changes

## Platform-Specific Behavior (all implemented in `commonMain`)

- Hover preview: Desktop/Web only
- Scroll wheel input: Desktop only
- Haptic feedback: Android only (conditional via `expect/actual`-free platform checks via Compose APIs)
- Keyboard input (arrows, digits, Home/End): All platforms
