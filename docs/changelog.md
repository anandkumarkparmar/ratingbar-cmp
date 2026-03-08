# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.2.0] - 2026-03-08

### Added

#### Features
- **F1 Fill animation** — Fill fractions now animate smoothly when the rating value changes.
  New `animateRating: Boolean = true` and `ratingAnimationSpec: AnimationSpec<Float>` parameters
  on both `RatingBar` overloads. Set `animateRating = false` for instant (snap) transitions.
- **F2 Scale-on-select animation** — The newly selected star briefly scales up with a spring
  bounce when a value is committed via tap or drag. Controlled by `animateScale: Boolean = true`
  on the default star overload. Disabled automatically in `readOnly` mode.
- **F3 `rememberRatingBarState()`** — New convenience composable function:
  `fun rememberRatingBarState(initialValue: Float = 0f): MutableState<Float>`. Replaces the
  boilerplate `remember { mutableStateOf(x) }` pattern at the call site.
- **F4 Minimum value constraints** — New `allowZero: Boolean = true` and `minValue: Float = 0f`
  parameters on both overloads. `allowZero = false` prevents the rating being cleared to zero
  (minimum becomes one step). `minValue` enforces an explicit lower bound. Both apply to tap,
  drag, scroll, and keyboard input.
- **F5 Hover preview** — On Desktop/Web, hovering the cursor over the bar shows a live fill
  preview at the cursor position. New `showHoverPreview: Boolean = true` parameter on both
  overloads. New `hoverColor: Color` parameter on the star overload (defaults to `filledColor`
  at 60% alpha) to visually distinguish preview fill from committed fill. New `onHoverValueChange`
  callback on the slot overload for custom slot callers that need to react to hover state.
- **F6 Mouse wheel input** — On Desktop, scrolling the mouse wheel up/down increments/decrements
  the rating by one step. New `enableScrollInput: Boolean = true` parameter on both overloads.
  Respects `allowZero` and `minValue` constraints.
- **F7 Haptic feedback** — On Android, a short haptic pulse fires each time the stepped value
  changes during interaction. New `hapticFeedback: Boolean = true` parameter on both overloads.
  No-op on other platforms. Does not fire for redundant same-value events.
- **`RatingBarDefaults.RatingAnimationSpec`** — New `TweenSpec<Float>` constant (200ms,
  `FastOutSlowInEasing`) exposed as the default fill animation spec.

#### Tooling and Quality
- **`explicitApi()` mode** — All public declarations now carry an explicit `public` modifier.
  Accidental API surface expansion is a compile error.
- **Detekt static analysis** — Detekt runs on every build (`./gradlew :ratingbar-cmp:detekt`).
  Wired into the CI `validate` job. Configuration at `detekt.yml` in the project root.
- **Dokka API documentation** — `./gradlew :ratingbar-cmp:dokkaHtml` generates full HTML
  API docs from KDoc. Output at `ratingbar-cmp/build/dokka/html/`.
- **Binary Compatibility Validator** — API surface is tracked in `ratingbar-cmp/api/`.
  `./gradlew :ratingbar-cmp:apiCheck` validates the surface matches the committed baseline.
  Wired into CI. Run `apiDump` when intentionally adding public API.
- **`FractionalClipShape` unit tests** — 9 tests covering LTR/RTL geometry at 0%, 25%, 50%,
  and 100% fill fractions, plus height preservation.
- **`RatingBarInteractionTest`** — 11 state and boundary tests covering clamping, stepping,
  fill fractions, `withValue`, `effectiveMin`, and edge cases.
- **`roundToStep` precision fix** — Replaced integer truncation with `kotlin.math.roundToInt`
  to avoid floating-point boundary errors (e.g., `2.5f % 0.5f ≠ 0f` on some platforms).
  8 new boundary-value tests added to `RatingBarStateTest`.

### Changed
- KDoc updated across all public classes and functions to document all new parameters,
  behavior details, and platform-specific notes.

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
