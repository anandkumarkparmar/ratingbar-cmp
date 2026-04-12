# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.5.0] - 2026-04-11

Internal restructure release in preparation for 1.0.0. The Maven coordinate is unchanged — consumers update one import line and bump the version.

### Added

- Added unit tests for `RatingBarAnimations`, `RatingBarBehavior`, `RatingBarColors`, and `RatingBarStyle`.

### Changed

- **BREAKING:** Flattened the package layout — removed the `com.github.anandkumarkparmar.ratingbar.core` subpackage. Drop `.core` from imports of `RatingBarConfig`, `RatingBarState`, and `RatingInteractionSource`. No signature or behavior changes.
- Moved library code into the `:ratingbar-cmp` subproject and flattened samples into `:samples:*` peer subprojects. A single `./gradlew build` from the repo root now builds everything.
- Consolidated Detekt configuration at the repo root and applied it to the library and all sample modules.
- Moved `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, and `CHANGELOG.md` from `docs/` to the repo root.

### Removed

- Removed the `useLocalLibrary` Gradle property and the samples composite build.
- Removed stale duplicate demo GIFs and a CI backup file (~21 MB saved).

---

## [0.4.0] - 2026-04-02

Major accessibility and customization release — the largest feature expansion of the 0.x series, introducing screen-reader support, loading placeholders, icon presets, gradient fills, and a grouped-parameter API.

### Added

- Added TalkBack and VoiceOver slider semantics on the slot overload — the semantics block now exposes `role = Role.ValuePicker`, `progressBarRangeInfo`, and `setProgress`. TalkBack announces the bar as a seekbar; VoiceOver marks it as adjustable. Swipe-up/down value changes work without any code change from callers.
- Added `itemLabels: List<String>?` for optional per-item semantic labels (e.g., `listOf("Terrible", "Bad", "Okay", "Good", "Excellent")`). When provided, `stateDescription` reflects the active label — for example, `"Good (4.0 out of 5)"`. Available on both overloads.
- Added `rememberSaveableRatingBarState()` — a state helper that survives Android configuration changes and Compose Navigation back-stack restoration. Uses `rememberSaveable` under the hood; no custom `Saver` is required because `Float` is natively saveable.
- Added reduced-motion support — `RatingBarAnimations(reducedMotion = true)` forces `snap()` for fill transitions and suppresses scale animation regardless of `enabled`. Callers read the OS preference and pass it in.
- Added `RatingBarPlaceholder`, a shimmer composable for skeleton screens. Displays a row of rounded rectangles with an animated sweep. Configurable via `max`, `itemSize`, `itemSpacing`, `shimmerBaseColor`, `shimmerHighlightColor`, `animationDurationMillis`, and `reducedMotion`.
- Added six new built-in `ImageVector` entries to `RatingBarIcons`: `Heart`, `HeartOutline`, `ThumbUp`, `ThumbUpOutline`, `Circle`, and `CircleOutline`. All 24×24 dp, Material-style, with no external dependency required.
- Added long-press to reset — `RatingBarBehavior(enableLongPressReset = true)` resets the rating to `config.effectiveMin` on a long-press anywhere on the bar. Fires `onValueChangeFinished`. No-op when `readOnly = true`.
- Added an interaction-source callback — `onInteraction: ((RatingInteractionSource) -> Unit)?` fires on each value-changing interaction with its source: `Tap`, `Drag`, `Keyboard`, or `Scroll`.
- Added gradient fill support — `RatingBarDefaults.colors(fillBrush = Brush.linearGradient(...))` applies a gradient to the filled layer via `BlendMode.SrcIn`, clipped to the exact star silhouette including fractional fills.
- Added leading and trailing content slots — `leadingContent` and `trailingContent` composable slots on both overloads. Wrap the bar in an outer `Row` with the provided composables on either side. Useful for numeric labels, icons, or badges.
- Added four new value-type classes: `RatingBarColors` (`filled`, `unfilled`, `hover`, `fillBrush?`), `RatingBarStyle` (`itemSize`, `itemSpacing`, `filledPainter`, `unfilledPainter`, `colors`), `RatingBarAnimations` (`enabled`, `spec`, `animateScale`, `reducedMotion`), and `RatingBarBehavior` (`showHoverPreview`, `enableScrollInput`, `hapticFeedback`, `enableLongPressReset`).
- Added the `RatingInteractionSource` enum — `Tap`, `Drag`, `Keyboard`, `Scroll`.
- Added `allowZero: Boolean = true` and `minValue: Float = 0f` to `RatingBarConfig` (previously top-level parameters).

### Changed

- **BREAKING:** Regrouped `RatingBar` parameters into semantic value-type objects. The star overload shrinks from 22 to 13 parameters; the slot overload from 16 to 15. Call sites that rely on defaults require no changes; call sites that passed individual parameters must migrate to the group objects.

**Migration:**

| Old flat parameters | New group |
|---|---|
| `max`, `step`, `allowZero`, `minValue` | `config: RatingBarConfig` |
| `itemSize`, `itemSpacing`, painters, colors | `style: RatingBarStyle` (star overload only) |
| `animateRating`, `ratingAnimationSpec`, `animateScale` | `animations: RatingBarAnimations` |
| `showHoverPreview`, `enableScrollInput`, `hapticFeedback` | `behavior: RatingBarBehavior` |

```kotlin
// Before (v0.3.0)
RatingBar(value, onValueChange, max = 5, step = 0.5f, filledColor = Color.Yellow,
    animateRating = true, showHoverPreview = true)

// After (v0.4.0)
RatingBar(value, onValueChange,
    config = RatingBarConfig(step = 0.5f),
    style = RatingBarDefaults.style(colors = RatingBarDefaults.colors(filled = Color.Yellow)),
    animations = RatingBarDefaults.animations(enabled = true),
    behavior = RatingBarDefaults.behavior(showHoverPreview = true))
```

---

## [0.3.0] - 2026-03-19

Build and infrastructure release — restructured the repository layout for cleaner JitPack coordinates and reworked the sample app with interactive demos. No public API changes.

### Added

- Added a `CLAUDE.md` project guidance document.
- Added a live interactive customization panel to the Playground sample screen with real-time sliders and toggles.
- Added individual feature toggle demos with explanatory notes to the Behaviors sample screen.

### Changed

- Moved library source to the root project for cleaner JitPack coordinates (from `ratingbar-cmp/src/` to `src/`).
- Converted samples to a standalone Gradle composite build — `samples/` now uses `includeBuild("..")` with automatic dependency substitution, replacing the `useLocalLibrary` Gradle property.
- Moved API baseline files to the root `api/` directory.
- Updated CI/CD change detection rules to match the new `src/**` path structure.
- Simplified build task paths — removed the `:ratingbar-cmp:` prefix from all root-level tasks.
- Reworked the sample app to feature three screens: Standard, Playground, and Behaviors (previously Standard and Customization).

---

## [0.2.0] - 2026-03-08

First major feature release — fill and scale animations, minimum-value constraints, desktop hover and scroll input, Android haptics, and tooling for API stability.

### Added

- Added fill animation — fill fractions now animate smoothly when the rating value changes. New `animateRating: Boolean = true` and `ratingAnimationSpec: AnimationSpec<Float>` parameters on both `RatingBar` overloads. Set `animateRating = false` for instant (snap) transitions.
- Added scale-on-select animation — the newly selected star briefly scales up with a spring bounce when a value is committed via tap or drag. Controlled by `animateScale: Boolean = true` on the default star overload. Disabled automatically in `readOnly` mode.
- Added `rememberRatingBarState()` — a convenience composable function: `fun rememberRatingBarState(initialValue: Float = 0f): MutableState<Float>`. Replaces the boilerplate `remember { mutableStateOf(x) }` pattern at the call site.
- Added minimum-value constraints — new `allowZero: Boolean = true` and `minValue: Float = 0f` parameters on both overloads. `allowZero = false` prevents the rating being cleared to zero (minimum becomes one step). `minValue` enforces an explicit lower bound. Both apply to tap, drag, scroll, and keyboard input.
- Added hover preview on Desktop and Web — hovering the cursor over the bar shows a live fill preview at the cursor position. New `showHoverPreview: Boolean = true` parameter on both overloads. New `hoverColor: Color` parameter on the star overload (defaults to `filledColor` at 60% alpha) to visually distinguish preview fill from committed fill. New `onHoverValueChange` callback on the slot overload for custom slot callers that need to react to hover state.
- Added mouse wheel input on Desktop — scrolling the mouse wheel up/down increments or decrements the rating by one step. New `enableScrollInput: Boolean = true` parameter on both overloads. Respects `allowZero` and `minValue` constraints.
- Added haptic feedback on Android — a short haptic pulse fires each time the stepped value changes during interaction. New `hapticFeedback: Boolean = true` parameter on both overloads. No-op on other platforms. Does not fire for redundant same-value events.
- Added `RatingBarDefaults.RatingAnimationSpec` — a new `TweenSpec<Float>` constant (200 ms, `FastOutSlowInEasing`) exposed as the default fill animation spec.
- Enabled `explicitApi()` mode — all public declarations now carry an explicit `public` modifier. Accidental API surface expansion is a compile error.
- Added Detekt static analysis — runs on every build (`./gradlew :ratingbar-cmp:detekt`). Wired into the CI `validate` job. Configuration lives at `detekt.yml` in the project root.
- Added Dokka API documentation — `./gradlew :ratingbar-cmp:dokkaHtml` generates full HTML API docs from KDoc. Output lands at `ratingbar-cmp/build/dokka/html/`.
- Added Binary Compatibility Validator — the API surface is tracked in `ratingbar-cmp/api/`. `./gradlew :ratingbar-cmp:apiCheck` validates the surface against the committed baseline. Wired into CI. Run `apiDump` when intentionally adding public API.
- Added `FractionalClipShape` unit tests — nine tests covering LTR/RTL geometry at 0%, 25%, 50%, and 100% fill fractions, plus height preservation.
- Added `RatingBarInteractionTest` — 11 state and boundary tests covering clamping, stepping, fill fractions, `withValue`, `effectiveMin`, and edge cases.

### Changed

- Updated KDoc across all public classes and functions to document new parameters, behavior details, and platform-specific notes.

### Fixed

- Fixed a `roundToStep` precision bug — replaced integer truncation with `kotlin.math.roundToInt` to avoid floating-point boundary errors (e.g., `2.5f % 0.5f ≠ 0f` on some platforms). Added eight boundary-value tests to `RatingBarStateTest`.

---

## [0.1.0] - 2026-03-06

Initial release — a fractional, state-hoisted `RatingBar` composable with full multiplatform support, interaction coverage, and publishing infrastructure.

### Added

- Added a state-hoisted `RatingBar` composable API with the `value` / `onValueChange` pattern.
- Added fractional rating support via a configurable `step` parameter (e.g., `0.5f`, `0.1f`).
- Added touch, tap, and drag interaction support on all platforms.
- Added keyboard interaction support — arrow keys, `+`/`-`, Home/End, and digit keys 1–9.
- Added RTL (right-to-left) behavior for visuals, touch, drag, and keyboard interactions.
- Added read-only mode via the `readOnly` parameter.
- Added a slot API (`itemContent` lambda) for fully custom item rendering.
- Added a default star overload with `filledPainter`, `unfilledPainter`, `filledColor`, and `unfilledColor` parameters.
- Added an `onValueChangeFinished` callback for gesture completion.
- Added the `RatingBarDefaults` object with `SizeSmall` (16 dp), `SizeMedium` (32 dp), `SizeLarge` (48 dp), and `ItemSpacing` (4 dp).
- Added the `RatingBarIcons` object with built-in `StarFilled` and `StarOutline` vector icons (no `material-icons-extended` dependency).
- Added `RatingBarConfig` data class with validation (`max > 0`, `step > 0`, `step <= max`).
- Added `RatingBarState` data class with clamping, stepping, and `fillFraction` calculation.
- Added `FractionalClipShape` for partial item fill with RTL support.
- Added a shared multiplatform sample app with Standard and Customization tabs.
- Added platform launchers for Android, Desktop (JVM), iOS, and Web (JS).
- Added a CI pipeline with intelligent change detection and cost-optimized iOS builds.
- Added a release workflow with tag-based validation and artifact size enforcement.
- Added JitPack-compatible Maven publishing.
- Added artifact size budgets and badge generation.
- Added accessibility semantics (`contentDescription`, `stateDescription`) for screen readers.
- Added platform support for Android (minSdk 24, compileSdk 36), Desktop (JVM), iOS (arm64, x64, simulatorArm64), and Web (JS/IR, browser).

---

[0.5.0]: https://github.com/anandkumarkparmar/ratingbar-cmp/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/anandkumarkparmar/ratingbar-cmp/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/anandkumarkparmar/ratingbar-cmp/compare/v0.2.0...0.3.0
[0.2.0]: https://github.com/anandkumarkparmar/ratingbar-cmp/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/anandkumarkparmar/ratingbar-cmp/releases/tag/v0.1.0
