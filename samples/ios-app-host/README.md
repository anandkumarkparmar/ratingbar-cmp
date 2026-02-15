## Run from Xcode

1. Open `samples/ios-app-host/sample-ratingbar-cmp/sample-ratingbar-cmp.xcodeproj`.
2. Select an iOS Simulator (Apple Silicon recommended: iPhone 16 / iOS Simulator Arm64).
3. Build & Run.

On each build, Xcode will trigger Gradle task(s) to produce framework binaries and copy them into the app build folder.

## Why this exists

The Gradle command below only builds/links the Kotlin framework, it does not create a runnable iOS app shell:

```bash
./gradlew :samples:ios:linkDebugFrameworkIosSimulatorArm64
```

## Current status

This host project now contains wiring for the Kotlin framework:

- SwiftUI `ContentView` renders Kotlin Compose UI via `MainKt.MainViewController()`
- Xcode target has a build phase that runs Gradle and copies `RatingBarSample.framework`
- Linker/search paths are configured to link `RatingBarSample`


