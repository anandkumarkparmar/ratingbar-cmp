# Running Samples

This guide explains how to build and run the sample apps for each platform.

All platform launchers host the same shared `SampleApp()` implementation from `samples:common`.

---

## Prerequisites

| Tool | Minimum Version | Notes |
|---|---|---|
| JDK | 17 | Temurin recommended |
| Android Studio | Latest stable | For Android sample |
| Xcode | Latest stable | Required for iOS sample (macOS only) |
| Gradle | Wrapper included | Run via `./gradlew` |

---

## Android

```bash
./gradlew :samples:android:installDebug
```

This builds and installs the debug APK on a connected device or running emulator. Ensure `adb` is available and a device is connected.

---

## Desktop (JVM)

```bash
./gradlew :samples:desktop:run
```

Launches the desktop application window directly from Gradle.

---

## iOS

iOS requires macOS with Xcode installed.

### Option 1: Xcode (recommended)

1. Build the shared Compose framework:
   ```bash
   ./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
   ```
2. Open the Xcode project:
   ```
   samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj
   ```
3. Select an iOS Simulator from the scheme selector.
4. Press **Build & Run** (⌘R).

### Option 2: Framework only

If you only need to verify the Compose framework compiles without opening Xcode:

```bash
./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
```

---

## Web

```bash
./gradlew :samples:web:jsBrowserDevelopmentRun
```

Starts a local development server. Open the printed URL in your browser (typically `http://localhost:8080`).

---

## Sample App Content

The sample app contains two tabs:

- **Standard** — Default star rating bar with half-step increments, read-only display, and value feedback
- **Customization** — Custom item slot API demonstrating icon substitution and color customization

---

## Troubleshooting

### Android: "No devices" error

- Check `adb devices` to confirm your device or emulator is connected.
- Ensure USB debugging is enabled on physical devices.

### iOS: Framework build fails

- Confirm Xcode command-line tools are installed: `xcode-select --install`
- Verify you are on macOS and Xcode is up to date.

### Web: Port already in use

The development server defaults to port 8080. If that port is busy, Gradle will report the actual URL in the output.

### Desktop: Window doesn't open

- Ensure you are not running in a headless environment.
- On Linux, confirm a display is available (e.g., `DISPLAY` is set).
