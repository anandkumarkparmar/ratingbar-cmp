## Project Overview

`ratingbar-cmp` is a Kotlin Multiplatform Compose library providing a `RatingBar` composable for Android, Desktop (JVM), iOS, and Web (JS/IR). All library logic lives in `commonMain` — there are no platform-specific source sets in the library module. Platform behavior (hover, scroll, haptics) is gated via Compose APIs, not `expect`/`actual`.

## Build & Test Commands

All library tasks are scoped to the `:ratingbar-cmp` subproject.

```bash
# Compile + test all non-iOS targets (fastest cross-platform validation)
./gradlew :ratingbar-cmp:compileAndroidMain :ratingbar-cmp:desktopTest :ratingbar-cmp:jsTest

# Platform-specific
./gradlew :ratingbar-cmp:desktopTest          # Runs all commonTest on desktop JVM
./gradlew :ratingbar-cmp:jsTest               # JS tests
./gradlew :ratingbar-cmp:assembleUnitTest     # Android compile check (no runtime tests — AGP 9.x)
./gradlew :ratingbar-cmp:iosSimulatorArm64Test # iOS (requires macOS + Xcode)

# Quality gates
./gradlew :ratingbar-cmp:detekt               # Static analysis (maxIssues: 0)
./gradlew :ratingbar-cmp:apiCheck             # Binary compatibility check
./gradlew :ratingbar-cmp:apiDump              # Regenerate .api golden after public API changes

# Build artifacts
./gradlew :ratingbar-cmp:bundleAndroidMainAar
./gradlew :ratingbar-cmp:desktopJar
./gradlew :ratingbar-cmp:publishToMavenLocal

# Pre-release validation (comprehensive — wraps all the above)
./scripts/release-check.sh
./scripts/release-check.sh --skip-ios         # On non-macOS machines
./scripts/release-check.sh --skip-samples
```

**Do NOT use** `./gradlew :ratingbar-cmp:build` — it includes iOS link tasks that fail on non-macOS and is slower than targeted commands.

## Running Sample Apps

```bash
./gradlew :samples:desktop:run                       # Desktop window
./gradlew :samples:android:installDebug              # Android APK on connected device
./gradlew :samples:web:jsBrowserDevelopmentRun       # Dev server at http://localhost:8080
```

iOS: build the framework with `./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64`, then open `samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj` in Xcode and Run.

## Architecture

### Module Layout

- **Root project** (`ratingbar-cmp-parent`) — pure aggregator; hosts repo-level files and applies shared detekt config to all subprojects.
- **`:ratingbar-cmp`** — the publishable KMP library. All source, tests, API golden file, and publishing config.
- **`:samples:common`** — shared `SampleApp()` composable. Declares `implementation(project(":ratingbar-cmp"))` — this is the **only** module that depends on the library directly.
- **`:samples:{android,desktop,ios,web}`** — thin platform launchers depending only on `:samples:common` (plus platform-specific runtime deps like `compose.desktop.currentOs` or `compose.mpp.html.core`).
- **`samples/ios-app-host/`** — Xcode project wrapping the Kotlin/Native framework (not a Gradle module).

Single Gradle build, one `gradlew` wrapper, one `gradle.properties`, one `gradle/libs.versions.toml`.

### Library Source

All library code is in one flat package: `ratingbar-cmp/src/commonMain/kotlin/com/github/anandkumarkparmar/ratingbar/`. No subpackages. Tests are pure logic in `commonTest`, run on desktop JVM via `desktopTest`.

## AGP 9.x Notes

- **Plugin**: `com.android.kotlin.multiplatform.library` — do NOT add `kotlin-android`
- **No `testDebugUnitTest`** — use `assembleUnitTest` (compile check) and `desktopTest` (runtime)
- **Android compile task**: `compileAndroidMain` (not `compileDebugKotlinAndroid`)
- **AAR task**: `bundleAndroidMainAar` (not `assembleRelease`)
- **JVM target 17**

## Key Configuration

- `gradle.properties` — `libraryVersion=0.5.0` is the **single source of truth** for the published version. `build.gradle.kts` reads it via `property("libraryVersion")`. Also sets `-Xmx8192m` and `workers.max=2` (required for iOS Kotlin/Native linking).
- `gradle/libs.versions.toml` — all dependency versions
- `detekt.yml` (repo root) — zero-tolerance linting (`maxIssues: 0`); applied to all subprojects from root `build.gradle.kts`
- `ratingbar-cmp/api/desktop/ratingbar-cmp.api` — binary-compatibility-validator golden file
- `settings.gradle.kts` — declares `:ratingbar-cmp` and all `:samples:*` subprojects

## JitPack Coordinate

`com.github.anandkumarkparmar.ratingbar-cmp:ratingbar-cmp:<version>` — depends on: GitHub repo name `ratingbar-cmp`, Gradle subproject name `:ratingbar-cmp`, `maven-publish` plugin on that subproject. Do not change any of these.

## Release Conventions

- **Tag format**: `0.x.y` (no `v` prefix). Enforced by `release.yml` regex `^0\.[0-9]+\.[0-9]+$`.
- **Version bump**: edit `libraryVersion` in `gradle.properties`, update `README.md` install snippet and `CHANGELOG.md`.
- **Full process**: see `docs/PUBLISHING_CHECKLIST.md`.
