# Contributing to ratingbar-cmp

Thanks for your interest in contributing to **ratingbar-cmp**. This guide covers everything you need to get started.

---

## Development Environment

### Requirements

| Tool | Minimum Version | Notes |
|---|---|---|
| JDK | 17 | Temurin recommended |
| Android SDK | compileSdk 36, minSdk 24 | Via Android Studio or `sdkmanager` |
| Kotlin | 2.3.10 | Managed by Gradle |
| Xcode | Latest stable | Required for iOS targets |
| Gradle | 9.3.1 | Wrapper included in repo |

### Setup

```bash
git clone https://github.com/anandkumarkparmar/ratingbar-cmp.git
cd ratingbar-cmp
./gradlew build
```

---

## Project Structure

```
ratingbar-cmp/
├── src/
│   ├── commonMain/         # Shared Kotlin/Compose code (all platforms) — library source
│   └── commonTest/         # Shared unit tests
├── api/                    # Binary compatibility baseline
├── samples/                # Standalone Gradle composite build (includeBuild(".."))
│   ├── common/             # Shared sample UI (SampleApp)
│   ├── android/            # Android launcher
│   ├── desktop/            # Desktop launcher
│   ├── ios/                # iOS Compose framework
│   ├── ios-app-host/       # Xcode project for iOS app
│   └── web/                # Web/JS launcher
├── scripts/                # CI and release scripts
├── docs/                   # Documentation
└── .github/                # Workflows, templates, badges
```

### Architecture

The library is a single `commonMain` module with no platform-specific source sets. All logic -- composables, state management, icons, shapes -- lives in shared Kotlin code.

```
com.github.anandkumarkparmar.ratingbar
├── RatingBar.kt              # Main composables and interaction logic
├── RatingBarDefaults.kt      # Size and spacing presets
├── RatingBarIcons.kt         # Built-in star vector icons
├── FractionalClipShape.kt    # Shape for partial fill (e.g., half-star)
└── core/
    └── RatingBarState.kt     # Config, state, stepping, fill fraction
```

---

## Ways to Contribute

### Bug Reports

Open an issue using the **Bug Report** template with:

- Platform (Android / Desktop / iOS / Web)
- Kotlin and Compose versions
- Minimal reproduction
- Expected vs actual behavior

### Feature Suggestions

Open an issue using the **Feature Request** template with:

- Clear use case
- Proposed API shape
- Non-breaking migration approach (if applicable)

### Code Contributions

1. Fork and clone the repository
2. Create a branch: `fix/<topic>`, `feat/<topic>`, or `docs/<topic>`
3. Make your changes
4. Validate locally (see below)
5. Open a PR targeting `main`

### Shoutout Contributions (Adopters)

Using this library in your app? Open an issue titled **"Shoutout: \<Your App Name\>"** with:

- App or project name
- Platform(s)
- Short usage note
- App/repo link (optional)
- Logo/screenshot (optional)

---

## Running Tests

### All platforms (except iOS)

```bash
./gradlew build -x iosX64Test -x iosArm64Test -x iosSimulatorArm64Test
```

### Per platform

```bash
# Desktop (includes commonTest)
./gradlew desktopTest

# Android unit tests
./gradlew assembleUnitTest

# Web/JS
./gradlew jsTest

# iOS (requires macOS + Xcode)
./gradlew iosSimulatorArm64Test
```

---

## Pre-PR Validation

Before opening a PR, run the full compile check:

```bash
./gradlew :samples:common:compileKotlinMetadata \
  :samples:android:compileDebugKotlinAndroid \
  :samples:desktop:compileKotlinDesktop \
  :samples:web:compileKotlinJs \
  :samples:ios:compileKotlinIosSimulatorArm64
```

### PR Checklist

- [ ] Change is scoped and focused
- [ ] No unrelated refactoring
- [ ] Docs updated if behavior changed
- [ ] Builds pass locally
- [ ] Tests added for new logic

---

## Coding Guidelines

- Keep the public API state-hoisted and predictable
- Prefer backward-compatible changes
- Avoid adding heavy dependencies
- Keep multiplatform parity where feasible
- Remove stale comments and dead code when touching files
- No `TODO` or `FIXME` comments in PR-ready code

### Code Style

- Kotlin official code style (`kotlin.code.style=official` in `gradle.properties`)
- KDoc for all public API elements
- Descriptive parameter names

---

## Branching

- **Target branch**: `main`
- **Branch naming**: `fix/<topic>`, `feat/<topic>`, `docs/<topic>`

---

## Release Notes Support

If your PR changes user-facing behavior, include in the PR description:

- Short summary of the change
- Before/after behavior
- Any migration notes

---

## Related Documentation

- [API Reference](api-reference.md)
- [Running Samples](running-samples.md)
- [CI Guide](ci-guide.md)
- [Publishing Checklist](publishing-checklist.md)
- [Code of Conduct](code-of-conduct.md)
