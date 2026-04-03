# API Reference

Full API reference for **ratingbar-cmp** v0.4.0.

> **Note:** This document is maintained alongside the source code. For the most precise and
> up-to-date reference (including parameter types, defaults, and KDoc), see the
> [hosted API documentation](https://anandkumarkparmar.github.io/ratingbar-cmp/).

---

## RatingBar (Star Overload)

A rating bar composable that displays a row of star items representing a rating value.

This is the default overload — use it whenever built-in icon rendering with color/size control is sufficient. For fully custom item rendering (hearts, emoji, etc.), use the [Slot Overload](#ratingbar-slot-overload) below.

This component is state-hoisted — the caller manages `value` and reacts to `onValueChange`.

```kotlin
@Composable
fun RatingBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: (() -> Unit)? = null,
    readOnly: Boolean = false,
    config: RatingBarConfig = RatingBarConfig(),
    style: RatingBarStyle = RatingBarDefaults.style(),
    animations: RatingBarAnimations = RatingBarDefaults.animations(),
    behavior: RatingBarBehavior = RatingBarDefaults.behavior(),
    itemLabels: List<String>? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onInteraction: ((RatingInteractionSource) -> Unit)? = null,
)
```

> **Note:** `style` and `RatingBarDefaults.style()` are `@Composable` — they must be called inside a composable context. `RatingBarDefaults.style()` wraps the result in `remember { }` to prevent unnecessary recomposition.

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | — | Current rating value. Clamped to `[effectiveMin, max]` and snapped to `step`. |
| `onValueChange` | `(Float) -> Unit` | — | Callback invoked with the stepped value when the rating changes. |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the outermost layout element. |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Called once when a tap or drag gesture ends. |
| `readOnly` | `Boolean` | `false` | If `true`, all interaction is disabled including hover preview. |
| `config` | `RatingBarConfig` | `RatingBarConfig()` | Rating constraints: `max`, `step`, `allowZero`, `minValue`. |
| `style` | `RatingBarStyle` | `RatingBarDefaults.style()` | Visual settings: item size, spacing, painters, colors/gradient. |
| `animations` | `RatingBarAnimations` | `RatingBarDefaults.animations()` | Animation settings: fill, scale, reduced-motion. All default to off. |
| `behavior` | `RatingBarBehavior` | `RatingBarDefaults.behavior()` | Platform and interaction flags: hover, scroll, haptic, long-press reset. All default to off. |
| `itemLabels` | `List<String>?` | `null` | Per-item semantic labels for TalkBack/VoiceOver (e.g., `["Terrible", "Bad", "Okay", "Good", "Excellent"]`). Must have ≥ `max` entries; shorter lists are ignored. |
| `leadingContent` | `(@Composable () -> Unit)?` | `null` | Optional composable rendered before the bar (e.g., a label). |
| `trailingContent` | `(@Composable () -> Unit)?` | `null` | Optional composable rendered after the bar (e.g., a numeric display). |
| `onInteraction` | `((RatingInteractionSource) -> Unit)?` | `null` | Fires on each value-changing interaction with the interaction source type. |

### Example

```kotlin
// Minimal — all defaults
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(value = rating, onValueChange = { rating = it })

// Customised
var rating by rememberSaveableRatingBarState(initialValue = 3f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    config = RatingBarConfig(max = 5, step = 0.5f),
    style = RatingBarDefaults.style(
        itemSize = RatingBarDefaults.SizeLarge,
        colors = RatingBarDefaults.colors(filled = Color(0xFFFFB300))
    ),
    animations = RatingBarDefaults.animations(enabled = true, animateScale = true),
    behavior = RatingBarDefaults.behavior(showHoverPreview = true, hapticFeedback = true),
    trailingContent = { Text("$rating / 5") }
)
```

---

## RatingBar (Slot Overload)

A generalized rating bar with a fully custom `itemContent` slot. Use this overload when you need to render hearts, circles, emoji, or any composable as a rating item.

```kotlin
@Composable
fun RatingBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: (() -> Unit)? = null,
    readOnly: Boolean = false,
    itemSpacing: Dp = RatingBarDefaults.ItemSpacing,
    config: RatingBarConfig = RatingBarConfig(),
    animations: RatingBarAnimations = RatingBarDefaults.animations(),
    behavior: RatingBarBehavior = RatingBarDefaults.behavior(),
    itemLabels: List<String>? = null,
    onHoverValueChange: ((Float?) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onInteraction: ((RatingInteractionSource) -> Unit)? = null,
    itemContent: @Composable (index: Int, fillFraction: Float) -> Unit,
)
```

### Parameters

Parameters shared with the star overload behave identically. Slot-specific parameters:

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | — | Current rating value. Clamped to `[effectiveMin, max]` and snapped to `step`. |
| `onValueChange` | `(Float) -> Unit` | — | Callback invoked with the stepped value when the rating changes. |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the outermost layout element. |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Called once when a tap or drag gesture ends. |
| `readOnly` | `Boolean` | `false` | If `true`, all interaction is disabled. |
| `itemSpacing` | `Dp` | `ItemSpacing` (4.dp) | Spacing between adjacent items. |
| `config` | `RatingBarConfig` | `RatingBarConfig()` | Rating constraints: `max`, `step`, `allowZero`, `minValue`. |
| `animations` | `RatingBarAnimations` | `RatingBarDefaults.animations()` | Animation settings. All default to off. |
| `behavior` | `RatingBarBehavior` | `RatingBarDefaults.behavior()` | Platform and interaction flags. All default to off. |
| `itemLabels` | `List<String>?` | `null` | Per-item semantic labels for screen readers. |
| `onHoverValueChange` | `((Float?) -> Unit)?` | `null` | Called when hover position changes on Desktop/Web. Receives `null` on cursor exit. Useful when the slot caller needs to react to hover state independently. |
| `leadingContent` | `(@Composable () -> Unit)?` | `null` | Optional composable rendered before the bar. |
| `trailingContent` | `(@Composable () -> Unit)?` | `null` | Optional composable rendered after the bar. |
| `onInteraction` | `((RatingInteractionSource) -> Unit)?` | `null` | Fires on each value-changing interaction with the interaction source type. |
| `itemContent` | `@Composable (Int, Float) -> Unit` | — | Slot for each item. Receives `(index, fillFraction)` where `fillFraction` is in `[0f, 1f]`. |

### Example

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    itemContent = { _, fillFraction ->
        Icon(
            painter = rememberVectorPainter(
                if (fillFraction > 0f) RatingBarIcons.Heart else RatingBarIcons.HeartOutline
            ),
            contentDescription = null,
            tint = if (fillFraction > 0f) Color(0xFFE91E63) else Color.Gray,
            modifier = Modifier.size(32.dp)
        )
    }
)
```

---

## rememberRatingBarState

Creates and remembers a `MutableState<Float>` holding a rating value.

```kotlin
@Composable
fun rememberRatingBarState(initialValue: Float = 0f): MutableState<Float>
```

| Parameter | Default | Description |
|---|---|---|
| `initialValue` | `0f` | Initial rating value. |

> **Note:** Uses `remember` — the value is **lost** on Android configuration changes (rotation, locale change) and Compose Navigation back-stack restoration. Use [`rememberSaveableRatingBarState`](#remembersaveableratingbarstate) when state survival is needed.

```kotlin
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(value = rating, onValueChange = { rating = it })
```

---

## rememberSaveableRatingBarState

Creates and remembers a `MutableState<Float>` that survives Android configuration changes and Compose Navigation back-stack restoration.

```kotlin
@Composable
fun rememberSaveableRatingBarState(initialValue: Float = 0f): MutableState<Float>
```

| Parameter | Default | Description |
|---|---|---|
| `initialValue` | `0f` | Initial rating value. |

Uses `rememberSaveable` under the hood — `Float` is natively saveable so no custom `Saver` is required. Use this instead of `rememberRatingBarState` for forms or screens that must survive rotation or process death.

```kotlin
var rating by rememberSaveableRatingBarState(initialValue = 3f)
RatingBar(value = rating, onValueChange = { rating = it })
```

---

## RatingBarConfig

Configuration data class for rating constraints. Package: `com.github.anandkumarkparmar.ratingbar.core`.

```kotlin
data class RatingBarConfig(
    val max: Int = 5,
    val step: Float = 1f,
    val allowZero: Boolean = true,
    val minValue: Float = 0f,
)
```

### Properties

| Property | Type | Default | Description |
|---|---|---|---|
| `max` | `Int` | `5` | Maximum rating value. Must be > 0. |
| `step` | `Float` | `1f` | Step increment. Must be > 0 and ≤ `max`. E.g. `0.5f` for half-star. |
| `allowZero` | `Boolean` | `true` | If `false`, minimum selectable value is at least one `step`. |
| `minValue` | `Float` | `0f` | Explicit minimum selectable value. Must be ≥ 0. |

### Computed Property

| Property | Type | Description |
|---|---|---|
| `effectiveMin` | `Float` | Derived minimum: equals `minValue` when `allowZero = true`; `maxOf(step, minValue)` when `allowZero = false`. |

### Validation

Throws `IllegalArgumentException` if:
- `max ≤ 0`
- `step ≤ 0`
- `step > max`
- `minValue < 0`

---

## RatingBarStyle

Visual style configuration for the default star overload. Bundles sizing, painter, and colour settings into a single object.

> **Note:** `RatingBarStyle` is a `@Stable class`, not a `data class`. Create instances via `RatingBarDefaults.style()` — it wraps the result in `remember { }` to prevent unnecessary recomposition.

```kotlin
@Stable
class RatingBarStyle(
    val itemSize: Dp,
    val itemSpacing: Dp,
    val filledPainter: Painter,
    val unfilledPainter: Painter,
    val colors: RatingBarColors,
)
```

### Properties

| Property | Type | Description |
|---|---|---|
| `itemSize` | `Dp` | Size of each rating item. |
| `itemSpacing` | `Dp` | Spacing between adjacent items. |
| `filledPainter` | `Painter` | Painter for the filled (selected) state. |
| `unfilledPainter` | `Painter` | Painter for the unfilled (empty) state. |
| `colors` | `RatingBarColors` | Color and fill configuration. |

### Factory — `RatingBarDefaults.style()` (`@Composable`)

```kotlin
@Composable
fun RatingBarDefaults.style(
    itemSize: Dp = SizeMedium,
    itemSpacing: Dp = ItemSpacing,
    filledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarFilled),
    unfilledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarOutline),
    colors: RatingBarColors = colors(),
): RatingBarStyle
```

---

## RatingBarColors

Color and fill configuration for the default star overload.

```kotlin
@Immutable
data class RatingBarColors(
    val filled: Color,
    val unfilled: Color,
    val hover: Color,
    val fillBrush: Brush? = null,
)
```

### Properties

| Property | Type | Default | Description |
|---|---|---|---|
| `filled` | `Color` | — | Color for filled (selected) items. |
| `unfilled` | `Color` | — | Color for unfilled (empty) items. |
| `hover` | `Color` | — | Color for items during hover preview on Desktop/Web. |
| `fillBrush` | `Brush?` | `null` | Optional gradient brush applied to the filled layer instead of `filled`. Overrides `filled` when non-null. Clipped to the item silhouette via `BlendMode.SrcIn`. |

### Factory — `RatingBarDefaults.colors()` (`@Composable`)

```kotlin
@Composable
fun RatingBarDefaults.colors(
    filled: Color = MaterialTheme.colorScheme.primary,
    unfilled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    hover: Color = filled.copy(alpha = 0.6f),
    fillBrush: Brush? = null,
): RatingBarColors
```

### Gradient Fill Example

```kotlin
style = RatingBarDefaults.style(
    colors = RatingBarDefaults.colors(
        fillBrush = Brush.linearGradient(
            listOf(Color(0xFFF44336), Color(0xFFFFEB3B), Color(0xFF4CAF50))
        )
    )
)
```

---

## RatingBarAnimations

Animation configuration for `RatingBar`. All flags default to `false` — animations are opt-in.

```kotlin
@Immutable
data class RatingBarAnimations(
    val enabled: Boolean = false,
    val spec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    val animateScale: Boolean = false,
    val reducedMotion: Boolean = false,
)
```

### Properties

| Property | Type | Default | Description |
|---|---|---|---|
| `enabled` | `Boolean` | `false` | If `true`, fill fraction transitions animate using `spec`. |
| `spec` | `AnimationSpec<Float>` | `RatingAnimationSpec` | Animation spec for fill transitions. Ignored when `enabled = false` or `reducedMotion = true`. |
| `animateScale` | `Boolean` | `false` | If `true`, the selected star briefly scales up with a spring bounce on commit. Applies to the star overload only; no-op in `readOnly` mode. |
| `reducedMotion` | `Boolean` | `false` | If `true`, overrides `enabled` and `animateScale` — all transitions snap instantly. Pass the OS reduced-motion preference here to respect the user's accessibility setting. |

### Factory — `RatingBarDefaults.animations()` (not `@Composable`)

```kotlin
fun RatingBarDefaults.animations(
    enabled: Boolean = false,
    spec: AnimationSpec<Float> = RatingAnimationSpec,
    animateScale: Boolean = false,
    reducedMotion: Boolean = false,
): RatingBarAnimations
```

---

## RatingBarBehavior

Platform and interaction flags for `RatingBar`. All flags default to `false` — platform behaviors are opt-in.

```kotlin
@Immutable
data class RatingBarBehavior(
    val showHoverPreview: Boolean = false,
    val enableScrollInput: Boolean = false,
    val hapticFeedback: Boolean = false,
    val enableLongPressReset: Boolean = false,
)
```

### Properties

| Property | Type | Default | Description |
|---|---|---|---|
| `showHoverPreview` | `Boolean` | `false` | Enables hover fill preview on Desktop/Web. No-op on Android/iOS. |
| `enableScrollInput` | `Boolean` | `false` | Enables mouse wheel scroll input on Desktop. No-op on other platforms. |
| `hapticFeedback` | `Boolean` | `false` | Enables haptic pulse on Android when the stepped value changes. No-op on Desktop, iOS, and Web. |
| `enableLongPressReset` | `Boolean` | `false` | If `true`, a long-press anywhere on the bar resets the rating to `config.effectiveMin`. Fires `onValueChangeFinished` after reset. No-op when `readOnly = true`. |

### Factory — `RatingBarDefaults.behavior()` (not `@Composable`)

```kotlin
fun RatingBarDefaults.behavior(
    showHoverPreview: Boolean = false,
    enableScrollInput: Boolean = false,
    hapticFeedback: Boolean = false,
    enableLongPressReset: Boolean = false,
): RatingBarBehavior
```

---

## RatingBarDefaults

Default values and factory functions for `RatingBar` components.

```kotlin
object RatingBarDefaults {
    val SizeSmall: Dp = 16.dp
    val SizeMedium: Dp = 32.dp
    val SizeLarge: Dp = 48.dp
    val ItemSpacing: Dp = 4.dp
    val RatingAnimationSpec: TweenSpec<Float>  // 200ms FastOutSlowIn
    const val ShimmerDurationMillis: Int = 1000
}
```

### Constants

| Constant | Type | Value | Description |
|---|---|---|---|
| `SizeSmall` | `Dp` | `16.dp` | Small item size preset. |
| `SizeMedium` | `Dp` | `32.dp` | Medium item size preset (default). |
| `SizeLarge` | `Dp` | `48.dp` | Large item size preset. |
| `ItemSpacing` | `Dp` | `4.dp` | Default spacing between items. |
| `RatingAnimationSpec` | `TweenSpec<Float>` | 200ms tween | Default fill animation spec (FastOutSlowIn easing). |
| `ShimmerDurationMillis` | `Int` | `1000` | Default shimmer cycle duration for `RatingBarPlaceholder`. |

### Factory Functions

| Function | `@Composable` | Returns | Description |
|---|---|---|---|
| `style(...)` | Yes | `RatingBarStyle` | Creates a remembered `RatingBarStyle`. Calls `rememberVectorPainter` and `MaterialTheme` internally. |
| `colors(...)` | Yes | `RatingBarColors` | Creates `RatingBarColors` with Material-themed defaults. |
| `animations(...)` | No | `RatingBarAnimations` | Creates `RatingBarAnimations`. Safe to call outside composition. |
| `behavior(...)` | No | `RatingBarBehavior` | Creates `RatingBarBehavior`. Safe to call outside composition. |

---

## RatingBarIcons

Built-in vector icons for rating items. Bundled in the library to avoid a dependency on `material-icons-extended`.

```kotlin
object RatingBarIcons {
    val StarFilled: ImageVector
    val StarOutline: ImageVector
    val Heart: ImageVector
    val HeartOutline: ImageVector
    val ThumbUp: ImageVector
    val ThumbUpOutline: ImageVector
    val Circle: ImageVector
    val CircleOutline: ImageVector
}
```

### Icon Pairs

| Pair | Icons | Use case |
|---|---|---|
| Star | `StarFilled` / `StarOutline` | Default star rating (used by `RatingBarDefaults.style()`). |
| Heart | `Heart` / `HeartOutline` | Wishlist or favorites-style bars. |
| ThumbUp | `ThumbUp` / `ThumbUpOutline` | Sentiment or binary-style bars. |
| Circle | `Circle` / `CircleOutline` | Progress or step indicator bars. |

All icons are 24×24dp `ImageVector` instances. Pass them as painters:

```kotlin
style = RatingBarDefaults.style(
    filledPainter = rememberVectorPainter(RatingBarIcons.Heart),
    unfilledPainter = rememberVectorPainter(RatingBarIcons.HeartOutline),
    colors = RatingBarDefaults.colors(filled = Color(0xFFE91E63))
)
```

---

## RatingInteractionSource

Identifies the input method that triggered a rating value change. Package: `com.github.anandkumarkparmar.ratingbar.core`.

```kotlin
enum class RatingInteractionSource {
    Tap,
    Drag,
    Keyboard,
    Scroll,
}
```

| Case | Description |
|---|---|
| `Tap` | Value changed via a single tap on an item. |
| `Drag` | Value changed by dragging across the bar. |
| `Keyboard` | Value changed via keyboard input (arrow keys, +/-, Home/End, digit keys). |
| `Scroll` | Value changed via mouse wheel scroll (Desktop only). |

Passed to the `onInteraction` callback for analytics, source-specific behavior, or logging:

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    onInteraction = { source ->
        when (source) {
            RatingInteractionSource.Tap -> analytics.track("rating_tap")
            RatingInteractionSource.Keyboard -> analytics.track("rating_keyboard")
            else -> {}
        }
    }
)
```

---

## RatingBarPlaceholder

A placeholder composable that mimics the layout of a `RatingBar` with an animated shimmer sweep. Use this for skeleton screens while a rating value is being fetched.

```kotlin
@Composable
fun RatingBarPlaceholder(
    modifier: Modifier = Modifier,
    max: Int = 5,
    itemSize: Dp = RatingBarDefaults.SizeMedium,
    itemSpacing: Dp = RatingBarDefaults.ItemSpacing,
    shimmerBaseColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    shimmerHighlightColor: Color = MaterialTheme.colorScheme.surface,
    animationDurationMillis: Int = RatingBarDefaults.ShimmerDurationMillis,
    reducedMotion: Boolean = false,
    itemPainter: Painter? = null,
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the outer Row. |
| `max` | `Int` | `5` | Number of placeholder items to display. Must be > 0. |
| `itemSize` | `Dp` | `SizeMedium` (32.dp) | Size of each placeholder item. |
| `itemSpacing` | `Dp` | `ItemSpacing` (4.dp) | Spacing between adjacent items. |
| `shimmerBaseColor` | `Color` | `surfaceVariant` | Base (dark) color of the shimmer. |
| `shimmerHighlightColor` | `Color` | `surface` | Highlight (light) color of the shimmer sweep. |
| `animationDurationMillis` | `Int` | `1000` | Duration of one shimmer sweep cycle in ms. Must be > 0. |
| `reducedMotion` | `Boolean` | `false` | If `true`, suppresses animation; items render in a static `shimmerBaseColor`. |
| `itemPainter` | `Painter?` | `null` | When provided, shimmer is clipped to the icon silhouette. When `null`, items render as rounded rectangles. |

Pass the same painter you use in your live `RatingBar` to keep the skeleton visually consistent:

```kotlin
if (isLoading) {
    RatingBarPlaceholder(
        itemPainter = rememberVectorPainter(RatingBarIcons.StarFilled),
    )
} else {
    RatingBar(value = rating, onValueChange = { rating = it })
}
```

---

## RatingBarState

Immutable value type with clamping and step-rounding logic.

> **Note:** `RatingBarState` is used internally by `RatingBar`. You rarely need to construct it directly — use it when you need to validate or round rating values outside of composition.

```kotlin
data class RatingBarState(
    val value: Float,
    val config: RatingBarConfig = RatingBarConfig()
)
```

### Properties

| Property | Type | Description |
|---|---|---|
| `value` | `Float` | Raw rating value as provided by the caller. |
| `clampedValue` | `Float` | `value` clamped to `[0, config.max]`. |
| `steppedValue` | `Float` | `clampedValue` rounded to the nearest step, then re-clamped. |

### Methods

| Method | Description |
|---|---|
| `withValue(newValue: Float): RatingBarState` | Returns a new state with updated value; same config. |

### Companion

| Method | Description |
|---|---|
| `roundToStep(value, step)` | Rounds value to the nearest multiple of step (IEEE 754-safe via `roundToInt`). |
| `fillFraction(itemIndex, value)` | Computes fractional fill `[0f, 1f]` for a given zero-based item index. |

---

## FractionalClipShape

A `Shape` that clips a composable to show only a fractional portion of its width. Used internally for partial star fill; public for custom slot callers.

```kotlin
class FractionalClipShape(fraction: Float) : Shape
```

| Parameter | Type | Description |
|---|---|---|
| `fraction` | `Float` | Fill fraction in `[0f, 1f]`. `0f` = fully clipped (empty), `1f` = unclipped (full). |

**RTL behavior:** In RTL layout direction, the clip is applied from the right edge (`right = width`, `left = width * (1 - fraction)`).

---

## Interaction Support

### Touch and Drag

Tap and drag gestures are supported on all platforms. Tapping sets the value proportionally to the tap position (maps to the star bin that was clicked). Dragging adjusts the value in real-time.

### Keyboard

| Key | Action |
|---|---|
| Arrow Right | Increase by step (decrease in RTL) |
| Arrow Left | Decrease by step (increase in RTL) |
| `+` | Increase by step |
| `-` | Decrease by step |
| Home | Set to `effectiveMin` |
| End | Set to `max` |
| `1`–`9` | Set to that integer value directly |

### Desktop / Web

| Feature | Config location | Default |
|---|---|---|
| Hover fill preview | `behavior.showHoverPreview` | `false` |
| Hover tint color | `style.colors.hover` | `filledColor` at 60% alpha |
| Mouse wheel scroll | `behavior.enableScrollInput` | `false` |

### Android

| Feature | Config location | Default |
|---|---|---|
| Haptic feedback on value change | `behavior.hapticFeedback` | `false` |

### Long-press Reset

| Feature | Config location | Default |
|---|---|---|
| Long-press to reset to `effectiveMin` | `behavior.enableLongPressReset` | `false` |

No-op when `readOnly = true`. Fires `onValueChangeFinished` after reset.

### Accessibility

The rating bar exposes semantics for screen readers on all platforms:

| Feature | Description |
|---|---|
| Screen reader role | `Role.ValuePicker` — announced as a seekbar by TalkBack and as adjustable by VoiceOver. |
| `contentDescription` | `"Rating: {value} out of {max}"` |
| `stateDescription` | `"{value} out of {max}"`, or `"{label} ({value} out of {max})"` when `itemLabels` is set and active. |
| `progressBarRangeInfo` | Exposes the range and step count for assistive technology. |
| `setProgress` semantic action | Allows TalkBack swipe-up/down and VoiceOver flick to change value without custom code. |
| Reduced-motion | Pass the OS preference to `animations = RatingBarDefaults.animations(reducedMotion = true)` to force snap transitions. |
| Per-item labels | Pass `itemLabels = listOf("Terrible", "Bad", "Okay", "Good", "Excellent")` for richer screen reader announcements. |

---

## Migrating from v0.3.0

v0.4.0 groups the individual flat parameters into four semantic objects. Call sites that relied on all defaults require **no changes**. Call sites that passed explicit values must migrate to the group objects.

> **Important behavioral change:** In v0.3.0, `animateRating`, `showHoverPreview`, `enableScrollInput`, and `hapticFeedback` all defaulted to `true` (opt-out). In v0.4.0, their equivalents in `RatingBarAnimations` and `RatingBarBehavior` all default to `false` (opt-in). If you previously relied on these being enabled by default, you must now pass them explicitly.

```kotlin
// v0.3.0 — individual flat params (opt-out defaults)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    max = 5,
    step = 0.5f,
    filledColor = Color.Yellow,
    animateRating = true,
    animateScale = true,
    showHoverPreview = true,
    hapticFeedback = true,
)

// v0.4.0 — grouped params (opt-in defaults)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    config = RatingBarConfig(step = 0.5f),
    style = RatingBarDefaults.style(
        colors = RatingBarDefaults.colors(filled = Color.Yellow)
    ),
    animations = RatingBarDefaults.animations(enabled = true, animateScale = true),
    behavior = RatingBarDefaults.behavior(showHoverPreview = true, hapticFeedback = true),
)
```

| v0.3.0 parameter | v0.4.0 location |
|---|---|
| `max` | `config.max` |
| `step` | `config.step` |
| `allowZero` | `config.allowZero` |
| `minValue` | `config.minValue` |
| `itemSize` | `style.itemSize` |
| `itemSpacing` | `style.itemSpacing` |
| `filledPainter` | `style.filledPainter` |
| `unfilledPainter` | `style.unfilledPainter` |
| `filledColor` | `style.colors.filled` |
| `unfilledColor` | `style.colors.unfilled` |
| `hoverColor` | `style.colors.hover` |
| `animateRating` | `animations.enabled` |
| `ratingAnimationSpec` | `animations.spec` |
| `animateScale` | `animations.animateScale` |
| `showHoverPreview` | `behavior.showHoverPreview` |
| `enableScrollInput` | `behavior.enableScrollInput` |
| `hapticFeedback` | `behavior.hapticFeedback` |

See the [changelog](changelog.md) for the full v0.4.0 change log.
