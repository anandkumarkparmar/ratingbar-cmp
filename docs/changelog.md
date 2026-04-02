# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.4.0] - 2026-04-02

### Added

#### Accessibility (F1, F2)
- **TalkBack / VoiceOver slider semantics** — `role = Role.ValuePicker`, `progressBarRangeInfo`,
  and `setProgress` added to the semantics block on the slot overload. TalkBack announces the bar
  as a seekbar; VoiceOver marks it as adjustable. Swipe-up/down value changes work without any
  code change from callers.
- **`itemLabels: List<String>?`** — Optional per-item semantic labels (e.g., `listOf("Terrible",
  "Bad", "Okay", "Good", "Excellent")`). When provided, the bar's `stateDescription` reflects the
  active label: `"Good (4.0 out of 5)"`. Available on both overloads.

#### State (F3)
- **`rememberSaveableRatingBarState()`** — New helper that survives Android configuration changes
  and Compose Navigation back-stack restoration. Uses `rememberSaveable` under the hood — `Float`
  is natively saveable so no custom `Saver` is required.

#### Animations (F4)
- **Reduced-motion support** — `RatingBarAnimations(reducedMotion = true)` forces `snap()` for
  fill transitions and suppresses scale animation regardless of `enabled`. Callers read the OS
  preference and pass it in.

#### Loading State (F5)
- **`RatingBarPlaceholder`** — New shimmer composable for skeleton screens. Displays a row of
  rounded rectangles with an animated sweep. Configurable via `max`, `itemSize`, `itemSpacing`,
  `shimmerBaseColor`, `shimmerHighlightColor`, `animationDurationMillis`, `reducedMotion`.

#### Shape Presets (F6)
- **`RatingBarIcons`** — Six new built-in `ImageVector` entries: `Heart`, `HeartOutline`,
  `ThumbUp`, `ThumbUpOutline`, `Circle`, `CircleOutline`. All 24×24dp Material-style; no external
  dependency required.

#### Interactions (F7, F10)
- **Long-press to reset** — `RatingBarBehavior(enableLongPressReset = true)` resets the rating
  to `config.effectiveMin` on a long-press anywhere on the bar. Fires `onValueChangeFinished`.
  No-op when `readOnly = true`.
- **Interaction source callback** — `onInteraction: ((RatingInteractionSource) -> Unit)?` fires
  on each value-changing interaction with the source: `Tap`, `Drag`, `Keyboard`, or `Scroll`.

#### Visual (F8, F9)
- **Gradient fill** — `RatingBarDefaults.colors(fillBrush = Brush.linearGradient(...))` applies
  a gradient to the filled layer via `BlendMode.SrcIn`, clipped to the exact star silhouette
  including fractional fills.
- **Leading / trailing content slots** — `leadingContent` and `trailingContent` composable slots
  on both overloads. Wrap the bar in an outer `Row` with the provided composables on either side.
  Useful for numeric labels, icons, or badges.

### Changed

#### API — Parameter Grouping (breaking, minor version)
The `RatingBar` star overload shrinks from 22 to 13 parameters; the slot overload from 16 to 15.
Four semantic groups replace individual flags:

| Old flat params | New group |
|---|---|
| `max`, `step`, `allowZero`, `minValue` | `config: RatingBarConfig` |
| `itemSize`, `itemSpacing`, painters, colors | `style: RatingBarStyle` (star only) |
| `animateRating`, `ratingAnimationSpec`, `animateScale` | `animations: RatingBarAnimations` |
| `showHoverPreview`, `enableScrollInput`, `hapticFeedback` | `behavior: RatingBarBehavior` |

All groups ship with sensible defaults — existing call sites that rely on defaults require no changes.
Call sites that passed individual params must migrate to the group objects.

**Migration example:**
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

#### New value types
- `RatingBarColors` — `filled`, `unfilled`, `hover`, `fillBrush?`
- `RatingBarStyle` — `itemSize`, `itemSpacing`, `filledPainter`, `unfilledPainter`, `colors`
- `RatingBarAnimations` — `enabled`, `spec`, `animateScale`, `reducedMotion`
- `RatingBarBehavior` — `showHoverPreview`, `enableScrollInput`, `hapticFeedback`, `enableLongPressReset`
- `RatingInteractionSource` — enum: `Tap`, `Drag`, `Keyboard`, `Scroll`

`RatingBarConfig` gains two optional fields: `allowZero: Boolean = true`, `minValue: Float = 0f`
(moved from top-level params).

---

## [0.3.0] - 2026-03-19

### Changed

#### Build & Infrastructure (no API changes)
- Library source moved to root project for cleaner JitPack coordinates (from `ratingbar-cmp/src/` to `src/`)
- Samples converted to standalone Gradle composite build — `samples/` now uses `includeBuild("..")` with automatic dependency substitution, replacing the `useLocalLibrary` Gradle property
- API baseline files moved to root `api/` directory
- CI/CD change detection rules updated to match new `src/**` path structure
- Build task paths simplified: `:ratingbar-cmp:` prefix removed from all root-level tasks
- CLAUDE.md project guidance document added

#### Sample Updates
- Sample app now features three screens: Standard, Playground, and Behaviors (was: Standard and Customization)
- Playground screen: live interactive customization panel with real-time sliders and toggles
- Behaviors screen: individual feature toggle demos with explanatory notes

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
