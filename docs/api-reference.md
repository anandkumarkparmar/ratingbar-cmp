# API Reference

Full API reference for **ratingbar-cmp**.

---

## RatingBar (Default Stars)

A rating bar composable that displays a row of star items representing a rating value.

This component is state-hoisted -- the caller manages the rating `value` via `onValueChange`.

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
    unfilledColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | -- | Current rating value (clamped to `[0, max]`) |
| `onValueChange` | `(Float) -> Unit` | -- | Callback invoked when the rating changes |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the rating bar |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Callback invoked when the gesture ends |
| `max` | `Int` | `5` | Maximum rating value (e.g., 5 for 5 stars) |
| `step` | `Float` | `1f` | Step increment (e.g., `0.5f` for half-star) |
| `readOnly` | `Boolean` | `false` | If `true`, the rating bar is display-only |
| `itemSize` | `Dp` | `SizeMedium` (32.dp) | Size of each rating item |
| `itemSpacing` | `Dp` | `ItemSpacing` (4.dp) | Spacing between rating items |
| `filledPainter` | `Painter` | `StarFilled` | Painter for filled items |
| `unfilledPainter` | `Painter` | `StarOutline` | Painter for unfilled items |
| `filledColor` | `Color` | `primary` | Color for filled items |
| `unfilledColor` | `Color` | `onSurface (0.3 alpha)` | Color for unfilled items |

---

## RatingBar (Custom Item Slot)

A generalized rating bar that allows custom content via the `itemContent` slot.

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
    itemContent: @Composable (index: Int, fillFraction: Float) -> Unit
)
```

### Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `value` | `Float` | -- | Current rating value |
| `onValueChange` | `(Float) -> Unit` | -- | Callback invoked when the rating changes |
| `onValueChangeFinished` | `(() -> Unit)?` | `null` | Callback invoked when the gesture ends |
| `modifier` | `Modifier` | `Modifier` | Modifier applied to the rating bar |
| `max` | `Int` | `5` | Maximum rating value |
| `step` | `Float` | `1f` | Step increment |
| `readOnly` | `Boolean` | `false` | If `true`, the rating bar is display-only |
| `itemSpacing` | `Dp` | `4.dp` | Spacing between items |
| `itemContent` | `@Composable (Int, Float) -> Unit` | -- | Composable lambda for each item. Receives `(index, fillFraction)` where `fillFraction` is between `0f` (empty) and `1f` (full). |

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

## RatingBarDefaults

Default values for RatingBar components.

```kotlin
object RatingBarDefaults {
    val SizeSmall: Dp   = 16.dp
    val SizeMedium: Dp  = 32.dp
    val SizeLarge: Dp   = 48.dp
    val ItemSpacing: Dp = 4.dp
}
```

| Constant | Value | Description |
|---|---|---|
| `SizeSmall` | `16.dp` | Small item size |
| `SizeMedium` | `32.dp` | Medium item size (default) |
| `SizeLarge` | `48.dp` | Large item size |
| `ItemSpacing` | `4.dp` | Default spacing between items |

---

## RatingBarIcons

Built-in vector icons for rating items. These are included in the library to avoid depending on `material-icons-extended`.

```kotlin
object RatingBarIcons {
    val StarFilled: ImageVector
    val StarOutline: ImageVector
}
```

| Icon | Description |
|---|---|
| `StarFilled` | Filled 24dp star (Material Icons Star) |
| `StarOutline` | Outlined 24dp star (Material Icons StarBorder) |

---

## RatingBarConfig

Configuration for a rating bar's constraints.

```kotlin
data class RatingBarConfig(
    val max: Int = 5,
    val step: Float = 1f
)
```

### Validation

- `max` must be greater than `0`
- `step` must be greater than `0`
- `step` must be less than or equal to `max`

---

## RatingBarState

State holder for rating bar value with clamping and step logic.

```kotlin
data class RatingBarState(
    val value: Float,
    val config: RatingBarConfig = RatingBarConfig()
)
```

### Properties

| Property | Type | Description |
|---|---|---|
| `value` | `Float` | Raw rating value |
| `clampedValue` | `Float` | Value clamped to `[0, config.max]` |
| `steppedValue` | `Float` | Value rounded to nearest step, clamped to `[0, config.max]` |

### Methods

| Method | Description |
|---|---|
| `withValue(newValue: Float): RatingBarState` | Returns a new state with the given value |

### Companion

| Method | Description |
|---|---|
| `roundToStep(value, step)` | Round a value to the nearest step |
| `fillFraction(itemIndex, value)` | Calculate the fractional fill (0f..1f) for a given item index |

---

## FractionalClipShape

A `Shape` that clips based on a fractional progress, respecting layout direction. Used internally for partial filling of rating items (e.g., half-star).

```kotlin
class FractionalClipShape(fraction: Float) : Shape
```

| Parameter | Type | Description |
|---|---|---|
| `fraction` | `Float` | Fill fraction between `0f` and `1f` |

RTL-aware: in RTL layout direction, the clip is applied from the right edge.

---

## Interaction Support

### Touch and Drag

Both tap and drag gestures are supported on all platforms. Tapping anywhere on the rating bar sets the value proportionally. Dragging adjusts the value in real-time.

### Keyboard

The following keyboard shortcuts are supported when the rating bar has focus:

| Key | Action |
|---|---|
| Arrow Right | Increase by step (decrease in RTL) |
| Arrow Left | Decrease by step (increase in RTL) |
| `+` | Increase by step |
| `-` | Decrease by step |
| Home | Set to 0 |
| End | Set to max |
| `1`-`9` | Set to that value directly |

### Accessibility

The rating bar exposes semantics for screen readers:

- `contentDescription`: "Rating: {value} out of {max}"
- `stateDescription`: "{value} out of {max}"
