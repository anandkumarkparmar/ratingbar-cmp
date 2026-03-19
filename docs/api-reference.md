# API Reference

Full API reference for **ratingbar-cmp** v0.3.0.

> **Note:** This document is maintained alongside the source code. For the most precise and
> up-to-date reference (including parameter types, defaults, and KDoc), generate the Dokka HTML
> docs locally:
> ```
> ./gradlew dokkaHtml
> open build/dokka/html/index.html
> ```

---

## RatingBar (Default Stars)

A rating bar composable that displays a row of star items representing a rating value.

This component is state-hoisted — the caller manages the rating `value` via `onValueChange`.

```kotlin
@Composable
fun RatingBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: (() -> Unit)? = null,
    max: Int = 5,
    step: Float = 1f,
    readOnly: Boolean = false,
    itemSize: Dp = RatingBarDefaults.SizeMedium,
    itemSpacing: Dp = RatingBarDefaults.ItemSpacing,
    filledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarFilled),
    unfilledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarOutline),
    filledColor: Color = MaterialTheme.colorScheme.primary,
    unfilledColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    hoverColor: Color = filledColor.copy(alpha = 0.6f),
    animateRating: Boolean = true,
    ratingAnimationSpec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    animateScale: Boolean = true,
    allowZero: Boolean = true,
    minValue: Float = 0f,
    showHoverPreview: Boolean = true,
    enableScrollInput: Boolean = true,
    hapticFeedback: Boolean = true,
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | — | Current rating value. Clamped to `[0, max]`, rounded to nearest `step`. |
| `onValueChange` | `(Float) -> Unit` | — | Callback invoked with the stepped value when it changes. |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the rating bar. |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Called once when a tap or drag gesture ends. |
| `max` | `Int` | `5` | Maximum rating value. Must be > 0. |
| `step` | `Float` | `1f` | Step increment. Must be > 0 and ≤ max. E.g. `0.5f` for half-star. |
| `readOnly` | `Boolean` | `false` | If `true`, all interaction is disabled including hover preview. |
| `itemSize` | `Dp` | `SizeMedium` (32.dp) | Size of each rating item. |
| `itemSpacing` | `Dp` | `ItemSpacing` (4.dp) | Spacing between adjacent items. |
| `filledPainter` | `Painter` | `StarFilled` | Painter for the filled (selected) state. |
| `unfilledPainter` | `Painter` | `StarOutline` | Painter for the unfilled (empty) state. |
| `filledColor` | `Color` | `primary` | Color for filled items. |
| `unfilledColor` | `Color` | `onSurface (30% alpha)` | Color for unfilled items. |
| `hoverColor` | `Color` | `filledColor (60% alpha)` | Color for filled items while hover preview is active on Desktop/Web. |
| `animateRating` | `Boolean` | `true` | If `true`, fill transitions are animated with `ratingAnimationSpec`. |
| `ratingAnimationSpec` | `AnimationSpec<Float>` | `RatingAnimationSpec` | Animation spec for fill transitions. |
| `animateScale` | `Boolean` | `true` | If `true`, the newly selected item briefly scales up on value commit. |
| `allowZero` | `Boolean` | `true` | If `false`, minimum selectable value is at least one `step`. |
| `minValue` | `Float` | `0f` | Explicit minimum selectable value. |
| `showHoverPreview` | `Boolean` | `true` | If `true`, hovering on Desktop/Web shows a live fill preview. |
| `enableScrollInput` | `Boolean` | `true` | If `true`, mouse wheel scroll adjusts rating on Desktop. |
| `hapticFeedback` | `Boolean` | `true` | If `true`, haptic pulse fires on Android when value changes. |

---

## RatingBar (Custom Item Slot)

A generalized rating bar that allows custom item rendering via the `itemContent` slot.

Use this overload when you need to render completely custom items (hearts, emoji, circles, etc.).
For standard star-style rating bars, use the default star overload above.

```kotlin
@Composable
fun RatingBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    max: Int = 5,
    step: Float = 1f,
    readOnly: Boolean = false,
    itemSpacing: Dp = 4.dp,
    animateRating: Boolean = true,
    ratingAnimationSpec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    allowZero: Boolean = true,
    minValue: Float = 0f,
    showHoverPreview: Boolean = true,
    enableScrollInput: Boolean = true,
    hapticFeedback: Boolean = true,
    onHoverValueChange: ((Float?) -> Unit)? = null,
    itemContent: @Composable (index: Int, fillFraction: Float) -> Unit
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | — | Current rating value. |
| `onValueChange` | `(Float) -> Unit` | — | Callback invoked with the stepped value when it changes. |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Called once when a tap or drag gesture ends. |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the rating bar. |
| `max` | `Int` | `5` | Maximum rating value. Must be > 0. |
| `step` | `Float` | `1f` | Step increment. Must be > 0 and ≤ max. |
| `readOnly` | `Boolean` | `false` | If `true`, all interaction is disabled. |
| `itemSpacing` | `Dp` | `4.dp` | Spacing between adjacent items. |
| `animateRating` | `Boolean` | `true` | If `true`, fill transitions are animated. |
| `ratingAnimationSpec` | `AnimationSpec<Float>` | `RatingAnimationSpec` | Animation spec for fill transitions. |
| `allowZero` | `Boolean` | `true` | If `false`, minimum selectable value is at least one `step`. |
| `minValue` | `Float` | `0f` | Explicit minimum selectable value. |
| `showHoverPreview` | `Boolean` | `true` | If `true`, hover position drives `fillFraction` on Desktop/Web. |
| `enableScrollInput` | `Boolean` | `true` | If `true`, mouse wheel adjusts rating on Desktop. |
| `hapticFeedback` | `Boolean` | `true` | If `true`, haptic pulse fires on Android when value changes. |
| `onHoverValueChange` | `((Float?) -> Unit)?` | `null` | Called when hover preview value changes. `null` on cursor exit. |
| `itemContent` | `@Composable (Int, Float) -> Unit` | — | Slot for each item. Receives `(index, fillFraction)` in `[0f, 1f]`. |

### Example

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    itemContent = { index, fillFraction ->
        val color = if (fillFraction > 0f) Color.Red else Color.Gray
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, CircleShape)
        )
    }
)
```

---

## rememberRatingBarState

Convenience composable function for creating and remembering a rating state.

```kotlin
@Composable
fun rememberRatingBarState(initialValue: Float = 0f): MutableState<Float>
```

### Usage

```kotlin
// Before (verbose):
var rating by remember { mutableStateOf(3f) }
RatingBar(value = rating, onValueChange = { rating = it })

// After (concise):
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(value = rating, onValueChange = { rating = it })
```

---

## RatingBarDefaults

Default values for `RatingBar` components.

```kotlin
object RatingBarDefaults {
    val SizeSmall: Dp                          = 16.dp
    val SizeMedium: Dp                         = 32.dp
    val SizeLarge: Dp                          = 48.dp
    val ItemSpacing: Dp                        = 4.dp
    val RatingAnimationSpec: TweenSpec<Float>  // 200ms FastOutSlowIn
}
```

| Constant | Value | Description |
|---|---|---|
| `SizeSmall` | `16.dp` | Small item size preset |
| `SizeMedium` | `32.dp` | Medium item size preset (default) |
| `SizeLarge` | `48.dp` | Large item size preset |
| `ItemSpacing` | `4.dp` | Default spacing between items |
| `RatingAnimationSpec` | 200ms tween | Default fill animation spec |

---

## RatingBarIcons

Built-in vector icons for rating items. Bundled in the library to avoid the `material-icons-extended` dependency.

```kotlin
object RatingBarIcons {
    val StarFilled: ImageVector   // Material Icons: Star
    val StarOutline: ImageVector  // Material Icons: StarBorder
}
```

| Icon | Description |
|---|---|
| `StarFilled` | Filled 24dp star (used as default `filledPainter`) |
| `StarOutline` | Outlined 24dp star (used as default `unfilledPainter`) |

---

## RatingBarConfig

Configuration data class for rating bar constraints.

```kotlin
data class RatingBarConfig(
    val max: Int = 5,
    val step: Float = 1f
)
```

### Validation

- `max` must be > 0
- `step` must be > 0
- `step` must be ≤ `max`

Throws `IllegalArgumentException` on violation.

---

## RatingBarState

Immutable state holder with clamping and step-rounding logic.

```kotlin
data class RatingBarState(
    val value: Float,
    val config: RatingBarConfig = RatingBarConfig()
)
```

### Properties

| Property | Type | Description |
|---|---|---|
| `value` | `Float` | Raw rating value as provided by the caller |
| `clampedValue` | `Float` | `value` clamped to `[0, config.max]` |
| `steppedValue` | `Float` | `clampedValue` rounded to the nearest step, then re-clamped |

### Methods

| Method | Description |
|---|---|
| `withValue(newValue: Float): RatingBarState` | Returns a new state with updated value |

### Companion

| Method | Description |
|---|---|
| `roundToStep(value, step)` | Round value to the nearest step using `roundToInt` (IEEE 754 safe) |
| `fillFraction(itemIndex, value)` | Compute fractional fill `[0f..1f]` for a given item index |

---

## FractionalClipShape

A `Shape` that clips a composable to show only a fractional portion of its width.
Used internally for partial star fill. RTL-aware.

```kotlin
class FractionalClipShape(fraction: Float) : Shape
```

| Parameter | Type | Description |
|---|---|---|
| `fraction` | `Float` | Fill fraction in `[0f, 1f]`. `0f` = empty, `1f` = full. |

**RTL behaviour:** In RTL layout direction, the clip is applied from the right edge
(`right = width`, `left = width * (1 - fraction)`).

---

## Interaction Support

### Touch and Drag

Tap and drag gestures are supported on all platforms. Tapping sets the value proportionally
to the tap position. Dragging adjusts the value in real-time.

### Keyboard

| Key | Action |
|---|---|
| Arrow Right | Increase by step (decrease in RTL) |
| Arrow Left | Decrease by step (increase in RTL) |
| `+` | Increase by step |
| `-` | Decrease by step |
| Home | Set to minimum (`0f` or `effectiveMin` when `allowZero = false`) |
| End | Set to `max` |
| `1`–`9` | Set to that integer value directly |

### Desktop / Web

| Feature | Parameter | Default |
|---|---|---|
| Hover preview (fill preview at cursor) | `showHoverPreview` | `true` |
| Hover tint color | `hoverColor` (star overload) | `filledColor` at 60% alpha |
| Mouse wheel scroll | `enableScrollInput` | `true` |

### Android

| Feature | Parameter | Default |
|---|---|---|
| Haptic feedback on value change | `hapticFeedback` | `true` |

### Accessibility

The rating bar exposes semantics for screen readers:

- `contentDescription`: `"Rating: {value} out of {max}"`
- `stateDescription`: `"{value} out of {max}"`
