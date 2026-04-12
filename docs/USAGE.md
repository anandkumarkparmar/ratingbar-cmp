# Usage Examples

Practical examples for common `ratingbar-cmp` use cases. Each example is self-contained and focuses on one concept at a time — copy the snippet, drop it into a `@Composable`, and it should just work.

- For the **full API reference** — every parameter, every default, every type signature — see the [live API documentation](https://anandkumarkparmar.github.io/ratingbar-cmp/). It's auto-generated from KDoc on every release, so it cannot drift.
- To **try the library interactively** without installing anything, open the [live web demo](https://anandkumarkparmar.github.io/ratingbar-cmp/demo/).
- To **run these examples locally**, clone the repo and follow [SETUP.md](../SETUP.md) — the `:samples:common` sample app shows most of these patterns on every platform.

The first example below includes the full imports. Subsequent examples omit imports for brevity — assume `androidx.compose.*` and `com.github.anandkumarkparmar.ratingbar.*` are in scope.

---

## Table of Contents

- [Basic rating](#basic-rating)
- [Half-star and fractional values](#half-star-and-fractional-values)
- [Custom size and color](#custom-size-and-color)
- [Read-only display](#read-only-display)
- [Gradient fill](#gradient-fill)
- [Custom icons (Heart, ThumbUp, Circle)](#custom-icons-heart-thumbup-circle)
- [Fully custom rendering (slot API)](#fully-custom-rendering-slot-api)
- [Animations](#animations)
- [Saveable state (survives rotation)](#saveable-state-survives-rotation)
- [Loading placeholder](#loading-placeholder)
- [Long-press to reset](#long-press-to-reset)
- [Accessibility: per-item labels and reduced motion](#accessibility-per-item-labels-and-reduced-motion)
- [Leading and trailing content](#leading-and-trailing-content)
- [Platform-specific interactions](#platform-specific-interactions)
- [Tracking the interaction source](#tracking-the-interaction-source)

---

## Basic rating

A minimal 5-star rating with integer steps, default Material-themed colors, and no animations or platform features enabled.

```kotlin
import androidx.compose.runtime.*
import com.github.anandkumarkparmar.ratingbar.*

@Composable
fun BasicRating() {
    var rating by rememberRatingBarState(initialValue = 3f)
    RatingBar(
        value = rating,
        onValueChange = { rating = it },
    )
}
```

`rememberRatingBarState` is a convenience for `remember { mutableStateOf(3f) }`. The state is caller-managed — `RatingBar` never owns the value, so you can hoist it into a ViewModel or persist it any way you like.

---

## Half-star and fractional values

Set `step` to any value smaller than `max` to enable fractional ratings. Common choices: `0.5f` for half-star, `0.25f` for quarter-star, `0.1f` for percentage-style precision.

```kotlin
var rating by rememberRatingBarState(initialValue = 3.5f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    config = RatingBarConfig(max = 5, step = 0.5f),
)
```

Tap and drag positions are automatically rounded to the nearest step. The value passed to `onValueChange` is always a multiple of `step`.

---

## Custom size and color

Use `RatingBarDefaults.style()` to bundle item size, spacing, and colors into a single object.

```kotlin
var rating by rememberRatingBarState(initialValue = 4f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    style = RatingBarDefaults.style(
        itemSize = RatingBarDefaults.SizeLarge,
        colors = RatingBarDefaults.colors(
            filled = Color(0xFFFFB300),
            unfilled = Color.LightGray,
        ),
    ),
)
```

Size presets: `SizeSmall` (16 dp), `SizeMedium` (32 dp, default), `SizeLarge` (48 dp). Pass any `Dp` value for custom sizes.

`RatingBarDefaults.style()` is a `@Composable` factory — it calls `rememberVectorPainter` and `MaterialTheme` internally, so you can only invoke it inside a composable context. It wraps its result in `remember { }` to prevent unnecessary recomposition.

---

## Read-only display

For showing a fixed rating — a product's average score, a review that's already been submitted, a badge — set `readOnly = true`. All interaction (tap, drag, keyboard, hover, scroll) is disabled.

```kotlin
RatingBar(
    value = 4.2f,
    onValueChange = {},              // never invoked when readOnly
    readOnly = true,
    config = RatingBarConfig(step = 0.1f),
)
```

Read-only bars still participate in accessibility semantics, so screen readers announce them correctly ("Rating: 4.2 out of 5").

---

## Gradient fill

Pass a `Brush` to `RatingBarDefaults.colors()` to apply a gradient to the filled portion. The brush is clipped to the star silhouette including fractional fills, so the gradient follows the shape exactly — it doesn't leak outside the icon.

```kotlin
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    style = RatingBarDefaults.style(
        colors = RatingBarDefaults.colors(
            fillBrush = Brush.linearGradient(
                listOf(
                    Color(0xFFF44336),   // red
                    Color(0xFFFFEB3B),   // yellow
                    Color(0xFF4CAF50),   // green
                )
            )
        )
    ),
)
```

When `fillBrush` is non-null, it overrides the solid `filled` color. The `unfilled` color still applies to empty stars. Under the hood, this uses `CompositingStrategy.Offscreen` with `BlendMode.SrcIn` to mask the brush to the icon shape.

---

## Custom icons (Heart, ThumbUp, Circle)

`RatingBarIcons` bundles four icon pairs — Star, Heart, ThumbUp, Circle — so you don't need `material-icons-extended` as a dependency.

```kotlin
var favorite by rememberRatingBarState(initialValue = 4f)
RatingBar(
    value = favorite,
    onValueChange = { favorite = it },
    style = RatingBarDefaults.style(
        filledPainter = rememberVectorPainter(RatingBarIcons.Heart),
        unfilledPainter = rememberVectorPainter(RatingBarIcons.HeartOutline),
        colors = RatingBarDefaults.colors(filled = Color(0xFFE91E63)),
    ),
)
```

All available pairs: `StarFilled`/`StarOutline`, `Heart`/`HeartOutline`, `ThumbUp`/`ThumbUpOutline`, `Circle`/`CircleOutline`. Each icon is a 24×24 dp `ImageVector`.

---

## Fully custom rendering (slot API)

When the built-in star overload isn't enough — emoji, animated items, custom composables — use the slot overload. Supply an `itemContent` lambda that receives each item's index and fill fraction, and render whatever you want.

```kotlin
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    itemContent = { _, fillFraction ->
        val emoji = when {
            fillFraction >= 1f -> "😍"
            fillFraction > 0f  -> "😐"
            else               -> "😶"
        }
        Text(emoji, fontSize = 32.sp)
    },
)
```

The slot overload handles all gesture logic — tap, drag, keyboard, RTL — for you. You only supply the per-item rendering. `fillFraction` is always in `[0f, 1f]`: `0f` for empty, `1f` for full, and anything between for fractional fills.

---

## Animations

Animations are opt-in. Pass `RatingBarDefaults.animations(enabled = true)` to enable fill animation, and add `animateScale = true` for a spring bounce on the newly selected star.

```kotlin
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    animations = RatingBarDefaults.animations(
        enabled = true,
        animateScale = true,
    ),
)
```

### Respecting reduced-motion preferences

To respect the OS accessibility setting, read the preference from the platform and pass it through. When `reducedMotion = true`, all transitions snap instantly regardless of `enabled` and `animateScale` — this overrides the other flags, so you can keep them `true` in the rest of your app.

```kotlin
animations = RatingBarDefaults.animations(
    enabled = true,
    animateScale = true,
    reducedMotion = userPrefersReducedMotion,
)
```

---

## Saveable state (survives rotation)

`rememberRatingBarState` uses `remember` under the hood, which does **not** survive Android configuration changes (rotation, locale change) or Compose Navigation back-stack restoration. For forms and long-lived screens, use `rememberSaveableRatingBarState` instead.

```kotlin
@Composable
fun RatingForm() {
    var rating by rememberSaveableRatingBarState(initialValue = 0f)
    RatingBar(value = rating, onValueChange = { rating = it })
}
```

Because `Float` is natively saveable in Compose, no custom `Saver` is required — the helper is a direct drop-in replacement for `rememberRatingBarState` wherever you need persistence.

---

## Loading placeholder

While you're fetching a rating value from the network, show `RatingBarPlaceholder` as a skeleton. It renders an animated shimmer that mimics the layout of a real `RatingBar`.

```kotlin
@Composable
fun ProductRating(product: Product?) {
    if (product == null) {
        RatingBarPlaceholder(
            max = 5,
            itemPainter = rememberVectorPainter(RatingBarIcons.StarFilled),
        )
    } else {
        RatingBar(
            value = product.rating,
            onValueChange = {},
            readOnly = true,
        )
    }
}
```

Pass the same `itemPainter` you'll use in the live bar so the shimmer is clipped to the star silhouette — this makes the skeleton visually consistent with the eventual rendered state. When `itemPainter` is `null`, items render as rounded rectangles instead.

The shimmer animation respects `reducedMotion = true` the same way `RatingBarAnimations` does — when set, items render in a static base color with no sweep.

---

## Long-press to reset

Enable `enableLongPressReset = true` to let users clear the rating with a long-press anywhere on the bar. The rating resets to `config.effectiveMin` (which is `0f` when `allowZero = true`, or `step` when `allowZero = false`) and fires `onValueChangeFinished`.

```kotlin
var rating by rememberRatingBarState(initialValue = 3f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    onValueChangeFinished = { analytics.track("rating_finalized") },
    behavior = RatingBarDefaults.behavior(enableLongPressReset = true),
)
```

No-op when `readOnly = true`.

---

## Accessibility: per-item labels and reduced motion

Pass `itemLabels` to provide richer screen-reader announcements. When set, `stateDescription` reflects the active label ("Good (4.0 out of 5)") instead of just "4.0 out of 5".

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    itemLabels = listOf("Terrible", "Bad", "Okay", "Good", "Excellent"),
)
```

**Important:** the list must have at least `config.max` entries. Shorter lists are silently ignored — pass exactly `max` labels to be safe.

The rating bar also exposes `Role.ValuePicker`, `progressBarRangeInfo`, and the `setProgress` semantic action on all platforms automatically. TalkBack announces it as a seekbar; VoiceOver marks it as adjustable. Swipe-up/down (TalkBack) or flick-up/down (VoiceOver) to change the value works without any extra code from you.

For reduced-motion handling, see [Animations](#animations) above.

---

## Leading and trailing content

Wrap the bar with optional composables rendered inline — useful for numeric displays, icons, category labels, or compact badges.

```kotlin
var rating by rememberRatingBarState(initialValue = 3.5f)
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    config = RatingBarConfig(step = 0.5f),
    trailingContent = {
        Text(
            "$rating / 5",
            modifier = Modifier.padding(start = 8.dp),
        )
    },
)
```

`leadingContent` and `trailingContent` both wrap the bar in an outer `Row`, so they sit on either side at the same baseline. Either or both can be non-null.

---

## Platform-specific interactions

Three platform features are gated behind `RatingBarBehavior` flags. All default to `false` — enable only what you need, and they degrade gracefully on platforms that don't support them.

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    behavior = RatingBarDefaults.behavior(
        showHoverPreview  = true,    // Desktop + Web
        enableScrollInput = true,    // Desktop (mouse wheel)
        hapticFeedback    = true,    // Android
    ),
)
```

| Flag | Platform | What it does |
|---|---|---|
| `showHoverPreview` | Desktop + Web | Hovering the cursor shows a live fill preview tinted with `colors.hover`. No-op on Android and iOS. |
| `enableScrollInput` | Desktop | Scrolling the mouse wheel changes the rating by one step. No-op elsewhere. |
| `hapticFeedback` | Android | A short haptic pulse fires each time the stepped value changes. No-op elsewhere. |

All three are no-ops on unsupported platforms — no crash, no warning, no runtime check required on your side.

---

## Tracking the interaction source

Use `onInteraction` for analytics, source-specific behavior, or logging. The callback fires on every value-changing interaction with a `RatingInteractionSource` identifying *how* the user changed the value.

```kotlin
RatingBar(
    value = rating,
    onValueChange = { rating = it },
    onInteraction = { source ->
        when (source) {
            RatingInteractionSource.Tap      -> analytics.track("rating_tap")
            RatingInteractionSource.Drag     -> analytics.track("rating_drag")
            RatingInteractionSource.Keyboard -> analytics.track("rating_keyboard")
            RatingInteractionSource.Scroll   -> analytics.track("rating_scroll")
        }
    },
)
```

This is distinct from `onValueChange` — the callback only fires when user input actually produced a value change, and it tells you the input method. Useful for measuring which input channels users actually reach for.
