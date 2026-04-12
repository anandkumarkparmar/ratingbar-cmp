# Setup Guide

This guide gets you from `git clone` to running a sample app on your machine. It's written for anyone who wants to explore the repository — evaluating the library, reading the source, running the samples, or getting ready to contribute.

If you're here because you want to contribute, start with this guide to get your environment working, then move on to [CONTRIBUTING.md](CONTRIBUTING.md) for the workflow (branches, commits, PRs).

---

## Prerequisites

You'll need the following installed before you can build the project.

| Tool | Required version | How to install |
|---|---|---|
| JDK | 17 (Temurin recommended) | [Temurin](https://adoptium.net/temurin/releases/?version=17), [sdkman](https://sdkman.io/) (`sdk install java 17-tem`), or Homebrew (`brew install temurin@17`) |
| Android SDK | compileSdk 36, minSdk 24 | Install [Android Studio](https://developer.android.com/studio) and open this project once — Studio will auto-install the required SDK and create a `local.properties` file |
| Xcode | Latest stable (macOS only) | Mac App Store — **only needed if you want to run the iOS sample** |
| Git | Any recent version | [git-scm.com](https://git-scm.com/) or your OS package manager |

Kotlin and Gradle are managed by the Gradle wrapper included in the repo — no separate install needed.

If you're not touching iOS or Android, you can skip Xcode and the Android SDK respectively. For Desktop and Web samples, JDK 17 and Git are enough.

---

## Clone and First Build

```bash
git clone https://github.com/anandkumarkparmar/ratingbar-cmp.git
cd ratingbar-cmp
./gradlew :ratingbar-cmp:desktopTest
```

This compiles the library's shared code and runs all unit tests on the desktop JVM. It's the fastest cross-platform verification that your JDK, Gradle wrapper, and dependency resolution all work — no Android SDK or Xcode required at this stage.

The first run downloads Kotlin, Compose Multiplatform, and test dependencies, and may take several minutes on a cold machine. Subsequent runs are incremental and fast.

If it fails, see [Troubleshooting](#troubleshooting) — most first-build failures are JDK or Android SDK related and have a one-line fix.

---

## Running the Samples

All four sample launchers host the same `SampleApp()` composable from `:samples:common`, which demonstrates every feature across three tabs: **Standard**, **Behaviors**, and **Playground**.

### Android

```bash
./gradlew :samples:android:installDebug
```

Builds and installs the debug APK on a connected device or running emulator. Confirm a device is available with `adb devices` before running.

### Desktop (JVM)

```bash
./gradlew :samples:desktop:run
```

Launches the desktop sample directly from Gradle. No packaging step — the window opens as soon as the build finishes.

### Web

```bash
./gradlew :samples:web:jsBrowserDevelopmentRun
```

Starts a Kotlin/JS development server with hot reload. Open the URL printed in the terminal (typically `http://localhost:8080`) in any modern browser. Edits to the sample source trigger a rebuild automatically.

### iOS

iOS requires macOS with Xcode installed. You have two options:

**Option 1 — Xcode (recommended for running the app):**

1. Build the shared Compose framework:

   ```bash
   ./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
   ```

2. Open the Xcode project:

   ```
   samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj
   ```

3. Pick an iOS Simulator from the scheme selector.
4. Press **Build & Run** (⌘R).

**Option 2 — Framework only (compile check without running):**

```bash
./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
```

Useful if you just want to confirm the framework compiles — no simulator launches.

---

## What's in the Sample App

Three tabs live inside `SampleApp()`:

- **Standard** — Default star patterns: basic rating, half-step increments, read-only display, RTL layout, and a custom slot example with hearts.
- **Behaviors** — Individual feature toggles for hover preview, scroll input, haptic feedback, animations, long-press reset, and reduced-motion — each with explanatory notes.
- **Playground** — Live interactive customization panel with real-time sliders and toggles for max rating, step size, item size, spacing, colors, and all behavioral flags.

The shared sample code lives at `samples/common/src/commonMain/kotlin/com/github/anandkumarkparmar/ratingbar/sample/SampleApp.kt` — edit it to try your own ideas, then re-run any platform launcher above.

---

## Exploring the Source

The library itself is at:

```
ratingbar-cmp/src/commonMain/kotlin/com/github/anandkumarkparmar/ratingbar/
```

All library code lives in this single package — there are no platform-specific source sets. Platform-gated behavior (hover preview, scroll wheel, haptics) is handled through Compose APIs rather than `expect`/`actual` declarations, which keeps the codebase flat and easy to read.

For the full project tree and where samples fit, see [CONTRIBUTING.md § Project Structure](CONTRIBUTING.md#project-structure).

---

## Where to Go Next

| If you want to... | Read this |
|---|---|
| See usage examples for common patterns | [`docs/USAGE.md`](docs/USAGE.md) |
| Browse the full API reference (every type, every parameter) | [Live API docs](https://anandkumarkparmar.github.io/ratingbar-cmp/) — auto-generated from KDoc |
| Try the library in a browser without cloning | [Live web demo](https://anandkumarkparmar.github.io/ratingbar-cmp/demo/) |
| Contribute a fix or feature | [`CONTRIBUTING.md`](CONTRIBUTING.md) |
| See what's planned next | [`docs/ROADMAP.md`](docs/ROADMAP.md) |
| See what shipped in past releases | [`CHANGELOG.md`](CHANGELOG.md) |

---

## Troubleshooting

### "JDK 17 is required" or "Unsupported class file major version"

Your local JDK doesn't match. Check the active version:

```bash
java -version
```

You need **JDK 17**. If you have multiple JDKs installed, tell Gradle which one to use by adding this to `~/.gradle/gradle.properties`:

```properties
org.gradle.java.home=/path/to/jdk-17
```

Or with sdkman:

```bash
sdk install java 17-tem
sdk use java 17-tem
```

### "SDK location not found" when building Android

You don't have the Android SDK installed, or Gradle can't find it. The simplest fix is to install [Android Studio](https://developer.android.com/studio), open this project once, and let Studio auto-install the SDK and create a `local.properties` file for you.

### Gradle runs out of memory during iOS linking

This shouldn't happen — `gradle.properties` already sets `org.gradle.jvmargs=-Xmx8192m` to prevent Kotlin/Native OOMs. If it still does on your machine, raise the heap further:

```properties
org.gradle.jvmargs=-Xmx12288m
```

### "Xcode not found" when building the iOS sample

iOS samples require Xcode and therefore macOS. On Linux or Windows, skip iOS entirely — the other three platforms (Android, Desktop, Web) still work.

### "Permission denied" when running `./gradlew`

Give the wrapper execute permission:

```bash
chmod +x gradlew
```

### Android: "No devices" when running `installDebug`

Check that your device or emulator is connected:

```bash
adb devices
```

Ensure USB debugging is enabled on physical devices, or that an Android Virtual Device is running if you're using an emulator.

### Web: "Port 8080 already in use"

The development server defaults to port 8080. If that port is busy, Gradle falls back to another port — check the actual URL printed in the terminal output.

### Desktop: window doesn't open

Make sure you're not running in a headless environment. On Linux, confirm a display is available:

```bash
echo $DISPLAY
```

If this prints nothing, set up X forwarding or run on a machine with a graphical session.

### Detekt failures you don't understand

Generate the HTML report and open it:

```bash
./gradlew :ratingbar-cmp:detekt
open ratingbar-cmp/build/reports/detekt/detekt.html
```

The HTML report shows the exact rule, line, and suggested fix for each issue.

### First build hangs or feels stuck

The first build downloads Kotlin, Compose Multiplatform, and Android dependencies — this can legitimately take several minutes on a slow connection. If it's been more than 15 minutes with no progress, cancel with `Ctrl+C` and retry with `--info` to see what's happening:

```bash
./gradlew :ratingbar-cmp:desktopTest --info
```

### `apiCheck` fails on CI but passes locally

You touched the public API without updating the binary-compatibility baseline. Regenerate it:

```bash
./gradlew :ratingbar-cmp:apiDump
git add ratingbar-cmp/api/
```

Commit the updated baseline with your change.
