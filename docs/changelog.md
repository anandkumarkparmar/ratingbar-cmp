# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.1.0] - 2026-03-06

### Added

- State-hoisted `RatingBar` composable API with `value` / `onValueChange` pattern
- Fractional rating support via configurable `step` parameter (e.g., `0.5f`, `0.1f`)
- Touch, tap, and drag interaction support on all platforms
- Keyboard interaction support (Arrow keys, +/-, Home/End, digit keys 1-9)
- RTL (right-to-left) behavior for visuals, touch, drag, and keyboard
- Read-only mode via `readOnly` parameter
- Slot API (`itemContent` lambda) for fully custom item rendering
- Default star overload with `filledPainter`, `unfilledPainter`, `filledColor`, `unfilledColor` parameters
- `onValueChangeFinished` callback for gesture completion
- `RatingBarDefaults` object with `SizeSmall` (16.dp), `SizeMedium` (32.dp), `SizeLarge` (48.dp), `ItemSpacing` (4.dp)
- `RatingBarIcons` object with built-in `StarFilled` and `StarOutline` vector icons (no `material-icons-extended` dependency)
- `RatingBarConfig` data class with validation (`max > 0`, `step > 0`, `step <= max`)
- `RatingBarState` data class with clamping, stepping, and `fillFraction` calculation
- `FractionalClipShape` for partial item fill with RTL support
- Shared multiplatform sample app with Standard and Customization tabs
- Platform launchers for Android, Desktop (JVM), iOS, and Web (JS)
- CI pipeline with intelligent change detection and cost-optimized iOS builds
- Release workflow with tag-based validation and artifact size enforcement
- JitPack-compatible Maven publishing
- Artifact size budgets and badge generation
- Accessibility semantics (`contentDescription`, `stateDescription`) for screen readers

### Platform Support

- Android (minSdk 24, compileSdk 36)
- Desktop (JVM)
- iOS (arm64, x64, simulatorArm64)
- Web (JS/IR, browser)
