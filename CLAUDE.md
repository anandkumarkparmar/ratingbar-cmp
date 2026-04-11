# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`ratingbar-cmp` is a Kotlin Multiplatform Compose library providing a `RatingBar` composable for Android, Desktop (JVM), iOS, and Web (JS/IR). All library logic lives in `commonMain` — there are no platform-specific source sets in the library module.

## Build & Test Commands

All library tasks are scoped to the `:ratingbar-cmp` subproject.

```bash
# Full library build (all platforms)
./gradlew :ratingbar-cmp:build

# Platform-specific compilation
./gradlew :ratingbar-cmp:compileAndroidMain
./gradlew :ratingbar-cmp:compileKotlinDesktop
./gradlew :ratingbar-cmp:compileKotlinJs

# Run tests (all shared tests run on desktop JVM)
./gradlew :ratingbar-cmp:desktopTest
./gradlew :ratingbar-cmp:jsTest
./gradlew :ratingbar-cmp:assembleUnitTest   # Android compile check (no runtime tests)

# Lint & API compatibility
./gradlew :ratingbar-cmp:detekt
./gradlew :ratingbar-cmp:apiCheck
./gradlew :ratingbar-cmp:apiDump            # update .api golden after public API changes

# Build artifacts
./gradlew :ratingbar-cmp:bundleAndroidMainAar
./gradlew :ratingbar-cmp:desktopJar
./gradlew :ratingbar-cmp:publishToMavenLocal

# Pre-release validation
./scripts/release-check.sh
./scripts/release-check.sh --skip-ios        # On non-macOS machines
./scripts/release-check.sh --skip-samples
```

## Running Sample Apps

```bash
./gradlew :samples:desktop:run
./gradlew :samples:android:installDebug
./gradlew :samples:web:jsBrowserDevelopmentRun   # Dev server at http://localhost:8080
```

iOS sample requires Xcode — open `samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj`.

## Architecture

### Module Layout
- **Root project** — `ratingbar-cmp-parent`, a pure aggregator hosting repo-level files (docs, scripts, workflows, LICENSE) and a minimal `build.gradle.kts` for shared quality plugins.
- **`:ratingbar-cmp`** (folder `ratingbar-cmp/`) — the KMP library subproject. All library source, tests, API golden file, and publishing config live here. This is the module JitPack publishes under coordinate `com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:<version>`.
- **`:samples:common`** (folder `samples/common/`) — Shared sample UI composable used by all platform launchers. Declares `implementation(project(":ratingbar-cmp"))` directly.
- **`:samples:{android,desktop,ios,web}`** — Thin platform launchers depending on `:samples:common` and `:ratingbar-cmp`.
- **`samples/ios-app-host/`** — Xcode project wrapping the iOS Kotlin/Native framework (not a Gradle module).

All modules live in a **single Gradle build**. Samples are no longer a composite build — they're flat subprojects alongside `:ratingbar-cmp`. There is one `gradlew` wrapper (at repo root), one `gradle.properties`, and one `gradle/libs.versions.toml` shared across all modules.

### Library Source Structure (`ratingbar-cmp/src/commonMain/kotlin/com/github/anandkumarkparmar/ratingbar/`)
- `RatingBar.kt` — Public composables and gesture/interaction logic
- `core/RatingBarState.kt` — `RatingBarConfig` and `RatingBarState` (immutable value type, ViewModel-friendly)
- `core/RatingInteractionSource.kt` — `RatingInteractionSource` enum (Tap, Drag, Keyboard, Scroll)
- `RatingBarDefaults.kt` — Size/spacing/animation/shimmer presets
- `RatingBarColors.kt` — `RatingBarColors` + `RatingBarDefaults.colors()` factory
- `RatingBarStyle.kt` — `RatingBarStyle` + `RatingBarDefaults.style()` factory
- `RatingBarAnimations.kt` — `RatingBarAnimations` + `RatingBarDefaults.animations()` factory
- `RatingBarBehavior.kt` — `RatingBarBehavior` + `RatingBarDefaults.behavior()` factory
- `RatingBarIcons.kt` — Built-in vector painters: Star, Heart, ThumbUp, Circle (filled + outline)
- `RatingBarPlaceholder.kt` — Shimmer loading skeleton composable
- `FractionalClipShape.kt` — Clip shape for partial star fills
- `RatingBarStateHelpers.kt` — `rememberRatingBarState()` and `rememberSaveableRatingBarState()`

### Tests (`ratingbar-cmp/src/commonTest/kotlin/com/github/anandkumarkparmar/ratingbar/`)
- `RatingBarStateTest.kt`, `RatingBarInteractionTest.kt`, `FractionalClipShapeTest.kt`, `RatingBarPlaceholderTest.kt`
- Run on desktop JVM via `:ratingbar-cmp:desktopTest`; no Compose UI tests (pure logic)

## AGP 9.x / Build System Notes

- **AGP plugin**: `com.android.kotlin.multiplatform.library` — do NOT add `kotlin-android`
- **No `testDebugUnitTest`** — use `assembleUnitTest` (compile check) and `desktopTest` (runtime)
- **Android compile task**: `compileAndroidMain` (not `compileDebugKotlinAndroid`)
- **AAR task**: `bundleAndroidMainAar` (not `assembleRelease`)
- **JVM target 17** — configured via `kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_17 } }`

## Key Configuration

- `gradle/libs.versions.toml` — All dependency versions (repo root, shared with samples composite)
- `gradle.properties` — `org.gradle.jvmargs=-Xmx8192m` and `workers.max=2` are required to prevent OOM during iOS Kotlin/Native linking
- `ratingbar-cmp/detekt.yml` — Zero-tolerance linting (`maxIssues: 0`); Compose wildcard imports are excluded
- `ratingbar-cmp/build.gradle.kts` — library build config, version, publishing metadata
- `ratingbar-cmp/api/desktop/ratingbar-cmp.api` — binary-compatibility-validator golden file
- `settings.gradle.kts` (repo root) — `rootProject.name = "ratingbar-cmp-parent"`, `include(":ratingbar-cmp")`
- Binary Compatibility Validator is applied to `:ratingbar-cmp`; run `./gradlew :ratingbar-cmp:apiDump` to update the `.api` file after public API changes
- Samples consume the library via direct `implementation(project(":ratingbar-cmp"))` — changes to library code are immediately visible to samples with no republishing step. The old `useLocalLibrary` property and composite-build substitution have been removed.

## JitPack Coordinate Invariant

The publishing coordinate `com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:<version>` **must not change**. It depends on three things staying stable:
1. GitHub repo name `ratingbar-cmp` (drives the group)
2. Gradle subproject name `:ratingbar-cmp` (drives the artifact ID)
3. `maven-publish` plugin applied to the `:ratingbar-cmp` subproject (not the root)

The root project is named `ratingbar-cmp-parent` — this is internal Gradle naming only; JitPack never sees it.

## Platform-Specific Behavior (all implemented in `commonMain`)

- Hover preview: Desktop/Web only
- Scroll wheel input: Desktop only
- Haptic feedback: Android only (conditional via `expect/actual`-free platform checks via Compose APIs)
- Keyboard input (arrows, digits, Home/End): All platforms
