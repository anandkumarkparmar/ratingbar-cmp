package com.github.anandkumarkparmar.ratingbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingBarState
import kotlinx.coroutines.delay


/**
 * A generalized rating bar that allows fully custom item rendering via the [itemContent] slot.
 *
 * This is the low-level overload. Use the default star overload when you only need to customize
 * colors, sizes, and painters. Use this overload when you need to render completely custom items
 * (e.g., heart icons, emoji, colored circles).
 *
 * This component is state-hoisted — the caller manages [value] and reacts to [onValueChange].
 *
 * **Interaction support (all platforms):**
 * - Tap and drag to set rating
 * - Keyboard: Arrow keys, +/-, Home (min), End (max), digit keys 1–9
 *
 * **Desktop / Web extras:**
 * - Hover preview via [showHoverPreview] — cursor position shows a live fill preview
 * - Mouse wheel scroll via [enableScrollInput] — increments/decrements by [step]
 *
 * **Android extras:**
 * - Haptic pulse on value change via [hapticFeedback]
 *
 * @param value The current rating value. Clamped to `[0, max]` and rounded to the nearest [step].
 * @param onValueChange Callback invoked when the rating value changes. Receives the stepped value.
 * @param onValueChangeFinished Optional callback invoked once when a tap or drag gesture ends.
 * @param modifier Modifier applied to the rating bar row.
 * @param max Maximum rating value (e.g., 5 for a 5-star bar). Must be greater than 0.
 * @param step Step increment for selectable values (e.g., `0.5f` for half-star). Must be > 0 and ≤ max.
 * @param readOnly If `true`, all interaction is disabled. Hover preview is also suppressed.
 * @param itemSpacing Spacing between adjacent items.
 * @param animateRating If `true`, fill fraction transitions are animated with [ratingAnimationSpec].
 *   Pass `false` for instant (snap) transitions, e.g., to respect system reduced-motion preferences.
 * @param ratingAnimationSpec Animation spec used for fill transitions when [animateRating] is `true`.
 *   Defaults to [RatingBarDefaults.RatingAnimationSpec] (200ms ease-out tween).
 * @param allowZero If `false`, the minimum selectable value is clamped to at least one [step].
 *   Drag-to-zero and keyboard Home will stop at [step] rather than 0.
 * @param minValue Explicit minimum selectable value. Ignored when [allowZero] is `false` and
 *   [step] is larger. Effective minimum = `if (!allowZero) maxOf(step, minValue) else minValue`.
 * @param showHoverPreview If `true` (default), hovering on Desktop/Web shows a live fill preview
 *   at the cursor position. No-op on Android/iOS where hover events don't fire.
 * @param enableScrollInput If `true` (default), mouse wheel scroll on Desktop adjusts the rating
 *   by one [step] per scroll tick. No-op on Android/iOS/Web.
 * @param hapticFeedback If `true` (default `false`), a haptic pulse fires on Android each time the
 *   value changes during tap or drag. Uses [HapticFeedbackType.LongPress] — the strongest
 *   standard feedback constant, ensuring the pulse is felt across devices. No-op on Desktop, iOS, and Web.
 *   Requires the device to have "Vibration & haptics" enabled in Android Settings.
 *   On Android 13+, haptics are suppressed by the OS in Silent mode.
 * @param onHoverValueChange Optional callback invoked whenever the hover preview value changes.
 *   Receives the current hover position as a raw (unstepped) float, or `null` when the cursor exits.
 *   Useful when the star overload or custom slot callers need to react to hover state.
 * @param itemContent Composable slot invoked for each item. Receives `(index, fillFraction)` where
 *   `index` is zero-based and `fillFraction` is in `[0f, 1f]`. When [showHoverPreview] is active,
 *   `fillFraction` reflects the hover position rather than the committed value.
 */
@Composable
public fun  RatingBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    max: Int = 5,
    step: Float = 1f,
    readOnly: Boolean = false,
    itemSpacing: Dp = 4.dp,
    animateRating: Boolean = false,
    ratingAnimationSpec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    allowZero: Boolean = true,
    minValue: Float = 0f,
    showHoverPreview: Boolean = false,
    enableScrollInput: Boolean = false,
    hapticFeedback: Boolean = false,
    onHoverValueChange: ((Float?) -> Unit)? = null,
    itemContent: @Composable (index: Int, fillFraction: Float) -> Unit
) {
    val config = remember(max, step) { RatingBarConfig(max = max, step = step) }
    val state = remember(value, config) { RatingBarState(value = value, config = config) }

    val effectiveMin = remember(allowZero, minValue, step) {
        if (!allowZero) maxOf(step, minValue) else minValue
    }
    // Clamp for immediate visual update when effectiveMin changes
    val displayValue = state.steppedValue.coerceAtLeast(effectiveMin)

    // Sync parent hoisted state when effectiveMin rises above the current value
    LaunchedEffect(effectiveMin) {
        if (value < effectiveMin) onValueChange(effectiveMin)
    }

    var widthPx by remember { mutableStateOf(0f) }
    var hoverValue by remember { mutableStateOf<Float?>(null) }
    val layoutDirection = LocalLayoutDirection.current
    val focusRequester = remember { FocusRequester() }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(readOnly) { if (readOnly) hoverValue = null }
    LaunchedEffect(hoverValue) { onHoverValueChange?.invoke(hoverValue) }

    val handleValueChange: (Float) -> Unit = { newValue ->
        if (!readOnly) {
            val clamped = newValue.coerceIn(effectiveMin, max.toFloat())
            val newState = state.withValue(clamped)
            val stepped = newState.steppedValue
            if (stepped != displayValue && hapticFeedback) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            onValueChange(stepped)
        }
    }
    // rememberUpdatedState ensures pointerInput coroutines always call the latest lambda
    // without needing to restart (which would disrupt in-progress gestures).
    val latestHandleValueChange = rememberUpdatedState(handleValueChange)
    val latestOnValueChangeFinished = rememberUpdatedState(onValueChangeFinished)

    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                stateDescription = "$displayValue out of $max"
                contentDescription = "Rating: $displayValue out of $max"
            }
            .focusRequester(focusRequester)
            .onGloballyPositioned { coordinates ->
                widthPx = coordinates.size.width.toFloat()
            }
            .then(
                if (!readOnly) {
                    Modifier
                        .pointerInput(layoutDirection, widthPx, max) {
                            detectTapGestures(
                                onTap = { offset ->
                                    if (widthPx > 0) {
                                        val x = adjustForRtl(offset.x, widthPx, layoutDirection)
                                        val newValue = (x / widthPx * max).coerceIn(0f, max.toFloat())
                                        latestHandleValueChange.value(newValue)
                                        latestOnValueChangeFinished.value?.invoke()
                                    }
                                }
                            )
                        }
                        .pointerInput(layoutDirection, widthPx, max) {
                            detectDragGestures(
                                onDragStart = { hoverValue = null },
                                onDragEnd = { latestOnValueChangeFinished.value?.invoke() },
                                onDragCancel = { latestOnValueChangeFinished.value?.invoke() }
                            ) { change, _ ->
                                change.consume()
                                if (widthPx > 0) {
                                    val x = adjustForRtl(change.position.x, widthPx, layoutDirection)
                                    val newValue = (x / widthPx * max).coerceIn(0f, max.toFloat())
                                    latestHandleValueChange.value(newValue)
                                }
                            }
                        }
                        .onKeyEvent { event ->
                            handleRatingKeyEvent(
                                event = event,
                                displayValue = displayValue,
                                step = step,
                                effectiveMin = effectiveMin,
                                max = max,
                                isRtl = layoutDirection == LayoutDirection.Rtl,
                                onValue = handleValueChange,
                            )
                        }
                        .pointerInput(
                            showHoverPreview, enableScrollInput, widthPx,
                            max, layoutDirection, step, effectiveMin
                        ) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    when (event.type) {
                                        PointerEventType.Move -> {
                                            val noPress = !event.changes.any { it.pressed }
                                            if (showHoverPreview && widthPx > 0 && noPress) {
                                                event.changes.firstOrNull()?.position?.x?.let { x ->
                                                    val adjustedX = adjustForRtl(x, widthPx, layoutDirection)
                                                    hoverValue = (adjustedX / widthPx * max)
                                                        .coerceIn(0f, max.toFloat())
                                                }
                                            }
                                        }
                                        PointerEventType.Exit -> {
                                            hoverValue = null
                                        }
                                        PointerEventType.Scroll -> {
                                            if (enableScrollInput) {
                                                val delta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                                                when {
                                                    delta < 0f -> latestHandleValueChange.value(
                                                        (displayValue + step).coerceAtMost(max.toFloat())
                                                    )
                                                    delta > 0f -> latestHandleValueChange.value(
                                                        (displayValue - step).coerceAtLeast(effectiveMin)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        repeat(max) { index ->
            val rawFill = RatingBarState.fillFraction(index, hoverValue ?: displayValue)
            val fillFraction by animateFloatAsState(
                targetValue = rawFill,
                animationSpec = if (animateRating && hoverValue == null) ratingAnimationSpec else snap(),
                label = "ratingFill_$index"
            )
            itemContent(index, fillFraction)
        }
    }
}

/**
 * A rating bar composable that displays a row of star items representing a rating value.
 *
 * This is the default overload with built-in star rendering. For fully custom item rendering
 * use the slot overload that accepts an [itemContent] lambda.
 *
 * This component is state-hoisted — the caller manages [value] and reacts to [onValueChange].
 *
 * **Interaction support (all platforms):**
 * - Tap and drag to set rating
 * - Keyboard: Arrow keys, +/-, Home (min), End (max), digit keys 1–9
 *
 * **Desktop / Web extras:**
 * - Hover preview via [showHoverPreview] — cursor position shows a live fill preview tinted
 *   with [hoverColor]
 * - Mouse wheel scroll via [enableScrollInput] — increments/decrements by [step]
 *
 * **Android extras:**
 * - Haptic pulse on value change via [hapticFeedback]
 *
 * @param value The current rating value. Clamped to `[0, max]` and rounded to the nearest [step].
 * @param onValueChange Callback invoked when the rating value changes. Receives the stepped value.
 * @param modifier Modifier applied to the rating bar row.
 * @param onValueChangeFinished Optional callback invoked once when a tap or drag gesture ends.
 * @param max Maximum rating value (e.g., 5 for a 5-star bar). Must be greater than 0.
 * @param step Step increment for selectable values (e.g., `0.5f` for half-star). Must be > 0 and ≤ max.
 * @param readOnly If `true`, all interaction is disabled. Hover preview is also suppressed.
 * @param itemSize Size of each rating item. Use [RatingBarDefaults] for presets.
 * @param itemSpacing Spacing between adjacent items.
 * @param filledPainter Painter for the filled (selected) state. Defaults to [RatingBarIcons.StarFilled].
 * @param unfilledPainter Painter for the unfilled (empty) state. Defaults to [RatingBarIcons.StarOutline].
 * @param filledColor Color applied to filled items. Defaults to `MaterialTheme.colorScheme.primary`.
 * @param unfilledColor Color applied to unfilled items. Defaults to `onSurface` at 30% alpha.
 * @param hoverColor Color applied to filled items while a hover preview is active on Desktop/Web.
 *   Defaults to [filledColor] at 60% alpha, giving a semi-transparent preview tint.
 * @param animateRating If `true`, fill fraction transitions are animated with [ratingAnimationSpec].
 *   Pass `false` for instant (snap) transitions, e.g., to respect system reduced-motion preferences.
 * @param ratingAnimationSpec Animation spec used for fill transitions when [animateRating] is `true`.
 *   Defaults to [RatingBarDefaults.RatingAnimationSpec] (200ms ease-out tween).
 * @param animateScale If `true`, the newly selected item briefly scales up (spring bounce) when
 *   a value change is committed via tap or drag. No-op in [readOnly] mode.
 * @param allowZero If `false`, the minimum selectable value is clamped to at least one [step].
 *   Drag-to-zero and keyboard Home will stop at [step] rather than 0.
 * @param minValue Explicit minimum selectable value. Effective minimum =
 *   `if (!allowZero) maxOf(step, minValue) else minValue`.
 * @param showHoverPreview If `true` (default), hovering on Desktop/Web shows a live fill preview
 *   tinted with [hoverColor]. No-op on Android/iOS.
 * @param enableScrollInput If `true` (default), mouse wheel scroll on Desktop adjusts the rating
 *   by one [step] per scroll tick. No-op on Android/iOS/Web.
 * @param hapticFeedback If `true` (default `false`), a haptic pulse fires on Android each time the
 *   value changes during tap or drag. Uses [HapticFeedbackType.LongPress] — the strongest
 *   standard feedback constant, ensuring the pulse is felt across devices. No-op on Desktop, iOS, and Web.
 *   Requires the device to have "Vibration & haptics" enabled in Android Settings.
 *   On Android 13+, haptics are suppressed by the OS in Silent mode.
 */
@Composable
public fun RatingBar(
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
    animateRating: Boolean = false,
    ratingAnimationSpec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    animateScale: Boolean = false,
    allowZero: Boolean = true,
    minValue: Float = 0f,
    showHoverPreview: Boolean = false,
    enableScrollInput: Boolean = false,
    hapticFeedback: Boolean = false,
) {
    var activeItemIndex by remember { mutableStateOf<Int?>(null) }
    var isHoverActive by remember { mutableStateOf(false) }
    val committedSteppedValue = remember(value, max, step) {
        RatingBarState(value = value, config = RatingBarConfig(max = max, step = step)).steppedValue
    }

    LaunchedEffect(activeItemIndex) {
        if (activeItemIndex != null) {
            delay(150)
            activeItemIndex = null
        }
    }

    val wrappedOnValueChange: (Float) -> Unit = { newValue ->
        onValueChange(newValue)
        if (animateScale && !readOnly) {
            activeItemIndex = if (newValue > 0f) {
                (kotlin.math.ceil(newValue.toDouble()).toInt() - 1).coerceIn(0, max - 1)
            } else null
        }
    }

    RatingBar(
        value = value,
        onValueChange = wrappedOnValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        max = max,
        step = step,
        readOnly = readOnly,
        itemSpacing = itemSpacing,
        animateRating = animateRating,
        ratingAnimationSpec = ratingAnimationSpec,
        allowZero = allowZero,
        minValue = minValue,
        showHoverPreview = showHoverPreview,
        enableScrollInput = enableScrollInput,
        hapticFeedback = hapticFeedback,
        onHoverValueChange = { isHoverActive = it != null },
        itemContent = { index, fillFraction ->
            val committedFill = RatingBarState.fillFraction(index, committedSteppedValue)
            RatingBarItem(
                fillFraction = fillFraction,
                committedFillFraction = committedFill,
                isHoverActive = isHoverActive,
                size = itemSize,
                filledPainter = filledPainter,
                unfilledPainter = unfilledPainter,
                filledColor = filledColor,
                unfilledColor = unfilledColor,
                hoverColor = hoverColor,
                isActive = animateScale && index == activeItemIndex,
            )
        }
    )
}


@Composable
private fun RatingBarItem(
    fillFraction: Float,
    committedFillFraction: Float,
    isHoverActive: Boolean,
    size: Dp,
    filledPainter: Painter,
    unfilledPainter: Painter,
    filledColor: Color,
    unfilledColor: Color,
    hoverColor: Color,
    isActive: Boolean = false,
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ratingScale"
    )

    // When hovering, use the instant committed fill for the filled layer so animation
    // doesn't fight the hover; when not hovering, use the animated fillFraction.
    val committedDisplayFraction = if (isHoverActive) committedFillFraction else fillFraction

    Box(modifier = Modifier.size(size).scale(scale)) {
        // Layer 1 — unfilled background
        Icon(
            painter = unfilledPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = unfilledColor
        )

        // Layer 2 — hover preview extension (only for stars beyond the committed fill)
        if (isHoverActive && fillFraction > committedFillFraction) {
            Icon(
                painter = filledPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (fillFraction < 1f) Modifier.clip(FractionalClipShape(fillFraction))
                        else Modifier
                    ),
                tint = hoverColor
            )
        }

        // Layer 3 — committed fill (on top, always preserves the confirmed rating colour)
        if (committedDisplayFraction > 0f) {
            Icon(
                painter = filledPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (committedDisplayFraction < 1f) {
                            Modifier.clip(FractionalClipShape(committedDisplayFraction))
                        } else {
                            Modifier
                        }
                    ),
                tint = filledColor
            )
        }
    }
}

private fun adjustForRtl(rawX: Float, widthPx: Float, layoutDirection: LayoutDirection): Float =
    if (layoutDirection == LayoutDirection.Rtl) widthPx - rawX else rawX

private fun handleRatingKeyEvent(
    event: KeyEvent,
    displayValue: Float,
    step: Float,
    effectiveMin: Float,
    max: Int,
    isRtl: Boolean,
    onValue: (Float) -> Unit,
): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    return when (event.key) {
        Key.DirectionRight -> {
            onValue((displayValue + if (isRtl) -step else step).coerceIn(effectiveMin, max.toFloat()))
            true
        }
        Key.DirectionLeft -> {
            onValue((displayValue + if (isRtl) step else -step).coerceIn(effectiveMin, max.toFloat()))
            true
        }
        Key.Plus -> { onValue((displayValue + step).coerceAtMost(max.toFloat())); true }
        Key.Minus -> { onValue((displayValue - step).coerceAtLeast(effectiveMin)); true }
        Key.Home -> { onValue(effectiveMin); true }
        Key.MoveEnd -> { onValue(max.toFloat()); true }
        else -> {
            val digit = event.key.keyCode - Key.One.keyCode + 1
            if (digit in 1..9 && digit <= max) { onValue(digit.toFloat()); true } else false
        }
    }
}
