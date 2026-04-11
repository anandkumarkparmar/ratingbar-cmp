package com.github.anandkumarkparmar.ratingbar

import kotlin.math.ceil
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingBarState
import com.github.anandkumarkparmar.ratingbar.core.RatingInteractionSource
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
 * - Hover preview via [behavior].showHoverPreview — cursor position shows a live fill preview
 * - Mouse wheel scroll via [behavior].enableScrollInput — increments/decrements by step
 *
 * **Android extras:**
 * - Haptic pulse on value change via [behavior].hapticFeedback
 *
 * @param value The current rating value. Clamped to `[effectiveMin, max]` and snapped to step.
 * @param onValueChange Callback invoked when the rating value changes. Receives the stepped value.
 * @param modifier Modifier applied to the outermost element (outer Row when slots are present,
 *   inner bar Row otherwise).
 * @param onValueChangeFinished Optional callback invoked once when a tap or drag gesture ends.
 * @param readOnly If `true`, all interaction is disabled.
 * @param itemSpacing Spacing between adjacent items.
 * @param config Rating configuration: max, step, allowZero, minValue.
 * @param animations Animation configuration: enabled, spec, animateScale, reducedMotion.
 * @param behavior Platform and interaction flags: hover, scroll, haptic, long-press reset.
 * @param itemLabels Optional per-item semantic labels (e.g., "Terrible", "Bad", "Okay", "Good",
 *   "Excellent"). When provided, the bar's `stateDescription` includes the active label.
 *   Must have at least [RatingBarConfig.max] entries to be used; shorter lists are ignored.
 * @param onHoverValueChange Optional callback for hover preview position changes.
 * @param leadingContent Optional composable rendered before the bar.
 * @param trailingContent Optional composable rendered after the bar.
 * @param onInteraction Optional callback fired on each interaction, indicating source type.
 * @param itemContent Composable slot invoked for each item. Receives `(index, fillFraction)`
 *   where `index` is zero-based and `fillFraction` is in `[0f, 1f]`.
 */
@Composable
public fun RatingBar(
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
) {
    val state = remember(value, config) { RatingBarState(value = value, config = config) }
    val displayValue = state.steppedValue.coerceAtLeast(config.effectiveMin)

    // Sync parent hoisted state when effectiveMin rises above the current value
    LaunchedEffect(config.effectiveMin) {
        if (value < config.effectiveMin) onValueChange(config.effectiveMin)
    }

    var widthPx by remember { mutableStateOf(0f) }
    var hoverValue by remember { mutableStateOf<Float?>(null) }
    val layoutDirection = LocalLayoutDirection.current
    val focusRequester = remember { FocusRequester() }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(readOnly) { if (readOnly) hoverValue = null }
    LaunchedEffect(hoverValue) { onHoverValueChange?.invoke(hoverValue) }

    val handleValueChange: (Float, RatingInteractionSource) -> Unit = { newValue, source ->
        if (!readOnly) {
            val clamped = newValue.coerceIn(config.effectiveMin, config.max.toFloat())
            val stepped = state.withValue(clamped).steppedValue
            if (stepped != displayValue && behavior.hapticFeedback) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            onValueChange(stepped)
            onInteraction?.invoke(source)
        }
    }
    // rememberUpdatedState ensures pointerInput coroutines always call the latest lambda
    // without needing to restart (which would disrupt in-progress gestures).
    val latestHandleValueChange = rememberUpdatedState(handleValueChange)
    val latestOnValueChangeFinished = rememberUpdatedState(onValueChangeFinished)

    val stateDesc = if (itemLabels != null && itemLabels.size >= config.max && displayValue > 0f) {
        val idx = (displayValue.toInt() - 1).coerceIn(0, itemLabels.size - 1)
        "${itemLabels[idx]} ($displayValue out of ${config.max})"
    } else {
        "$displayValue out of ${config.max}"
    }

    val barModifier = Modifier
        .semantics(mergeDescendants = true) {
            role = Role.ValuePicker
            stateDescription = stateDesc
            contentDescription = "Rating: $displayValue out of ${config.max}"
            progressBarRangeInfo = ProgressBarRangeInfo(
                current = displayValue,
                range = config.effectiveMin..config.max.toFloat(),
                steps = ((config.max.toFloat() / config.step).toInt() - 1).coerceAtLeast(0)
            )
            if (!readOnly) {
                setProgress { targetValue ->
                    val clamped = targetValue.coerceIn(config.effectiveMin, config.max.toFloat())
                    val stepped = RatingBarState(value = clamped, config = config).steppedValue
                    onValueChange(stepped)
                    true
                }
            }
        }
        .focusRequester(focusRequester)
        .onGloballyPositioned { coordinates ->
            widthPx = coordinates.size.width.toFloat()
        }
        .then(
            if (!readOnly) {
                Modifier
                    .pointerInput(layoutDirection, widthPx, config.max) {
                        detectTapGestures(
                            onTap = { offset ->
                                if (widthPx > 0) {
                                    val x = adjustForRtl(offset.x, widthPx, layoutDirection)
                                    // ceil maps the tap to the star bin that was clicked:
                                    // tapping anywhere inside star N selects value N*step,
                                    // regardless of which side of the star was touched.
                                    val raw = x / widthPx * config.max
                                    val newValue = (ceil(raw / config.step) * config.step)
                                        .coerceIn(0f, config.max.toFloat())
                                    latestHandleValueChange.value(newValue, RatingInteractionSource.Tap)
                                    latestOnValueChangeFinished.value?.invoke()
                                }
                            },
                            onLongPress = {
                                if (behavior.enableLongPressReset) {
                                    latestHandleValueChange.value(
                                        config.effectiveMin,
                                        RatingInteractionSource.Tap,
                                    )
                                    latestOnValueChangeFinished.value?.invoke()
                                }
                            },
                        )
                    }
                    .pointerInput(layoutDirection, widthPx, config.max) {
                        detectDragGestures(
                            onDragStart = { hoverValue = null },
                            onDragEnd = { latestOnValueChangeFinished.value?.invoke() },
                            onDragCancel = { latestOnValueChangeFinished.value?.invoke() },
                        ) { change, _ ->
                            change.consume()
                            if (widthPx > 0) {
                                val x = adjustForRtl(change.position.x, widthPx, layoutDirection)
                                val newValue = (x / widthPx * config.max)
                                    .coerceIn(0f, config.max.toFloat())
                                latestHandleValueChange.value(newValue, RatingInteractionSource.Drag)
                            }
                        }
                    }
                    .onKeyEvent { event ->
                        handleRatingKeyEvent(
                            event = event,
                            displayValue = displayValue,
                            config = config,
                            isRtl = layoutDirection == LayoutDirection.Rtl,
                            onValue = { v ->
                                handleValueChange(v, RatingInteractionSource.Keyboard)
                            },
                        )
                    }
                    .pointerInput(
                        behavior.showHoverPreview, behavior.enableScrollInput,
                        widthPx, config.max, layoutDirection,
                        config.step, config.effectiveMin,
                    ) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    PointerEventType.Move -> {
                                        val noPress = !event.changes.any { it.pressed }
                                        if (behavior.showHoverPreview && widthPx > 0 && noPress) {
                                            event.changes.firstOrNull()?.position?.x?.let { x ->
                                                val adjustedX = adjustForRtl(x, widthPx, layoutDirection)
                                                hoverValue = (adjustedX / widthPx * config.max)
                                                    .coerceIn(0f, config.max.toFloat())
                                            }
                                        }
                                    }
                                    PointerEventType.Exit -> {
                                        hoverValue = null
                                    }
                                    PointerEventType.Scroll -> {
                                        if (behavior.enableScrollInput) {
                                            val delta =
                                                event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                                            when {
                                                delta < 0f -> latestHandleValueChange.value(
                                                    (displayValue + config.step)
                                                        .coerceAtMost(config.max.toFloat()),
                                                    RatingInteractionSource.Scroll,
                                                )
                                                delta > 0f -> latestHandleValueChange.value(
                                                    (displayValue - config.step)
                                                        .coerceAtLeast(config.effectiveMin),
                                                    RatingInteractionSource.Scroll,
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
        )

    val barContent: @Composable RowScope.() -> Unit = {
        repeat(config.max) { index ->
            val rawFill = RatingBarState.fillFraction(index, hoverValue ?: displayValue)
            val fillFraction by animateFloatAsState(
                targetValue = rawFill,
                animationSpec = if (animations.enabled && !animations.reducedMotion && hoverValue == null) {
                    animations.spec
                } else {
                    snap()
                },
                label = "ratingFill_$index",
            )
            itemContent(index, fillFraction)
        }
    }

    if (leadingContent != null || trailingContent != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent?.invoke()
            Row(
                modifier = barModifier,
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                content = barContent,
            )
            trailingContent?.invoke()
        }
    } else {
        Row(
            modifier = modifier.then(barModifier),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            content = barContent,
        )
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
 * - Hover preview via [behavior].showHoverPreview — cursor position shows a live fill preview
 *   tinted with [RatingBarColors.hover]
 * - Mouse wheel scroll via [behavior].enableScrollInput — increments/decrements by step
 *
 * **Android extras:**
 * - Haptic pulse on value change via [behavior].hapticFeedback
 *
 * @param value The current rating value. Clamped to `[effectiveMin, max]` and snapped to step.
 * @param onValueChange Callback invoked when the rating value changes. Receives the stepped value.
 * @param modifier Modifier applied to the outermost element.
 * @param onValueChangeFinished Optional callback invoked once when a tap or drag gesture ends.
 * @param readOnly If `true`, all interaction is disabled.
 * @param config Rating configuration: max, step, allowZero, minValue.
 * @param style Visual style: itemSize, itemSpacing, painters, colors (including gradient brush).
 * @param animations Animation configuration: enabled, spec, animateScale, reducedMotion.
 * @param behavior Platform and interaction flags: hover, scroll, haptic, long-press reset.
 * @param itemLabels Optional per-item semantic labels. Used in the bar's `stateDescription`
 *   and as each star's `contentDescription`. Must have at least [RatingBarConfig.max] entries.
 * @param leadingContent Optional composable rendered before the bar.
 * @param trailingContent Optional composable rendered after the bar.
 * @param onInteraction Optional callback fired on each interaction, indicating source type.
 */
@Composable
public fun RatingBar(
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
) {
    var activeItemIndex by remember { mutableStateOf<Int?>(null) }
    var isHoverActive by remember { mutableStateOf(false) }
    val committedSteppedValue = remember(value, config) {
        RatingBarState(value = value, config = config).steppedValue
    }

    LaunchedEffect(activeItemIndex) {
        if (activeItemIndex != null) {
            delay(150)
            activeItemIndex = null
        }
    }

    val wrappedOnValueChange: (Float) -> Unit = { newValue ->
        onValueChange(newValue)
        if (animations.animateScale && !readOnly) {
            activeItemIndex = if (newValue > 0f) {
                (kotlin.math.ceil(newValue.toDouble()).toInt() - 1).coerceIn(0, config.max - 1)
            } else {
                null
            }
        }
    }

    RatingBar(
        value = value,
        onValueChange = wrappedOnValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        readOnly = readOnly,
        itemSpacing = style.itemSpacing,
        config = config,
        animations = animations,
        behavior = behavior,
        itemLabels = itemLabels,
        onHoverValueChange = { isHoverActive = it != null },
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        onInteraction = onInteraction,
        itemContent = { index, fillFraction ->
            val committedFill = RatingBarState.fillFraction(index, committedSteppedValue)
            RatingBarItem(
                fillFraction = fillFraction,
                committedFillFraction = committedFill,
                isHoverActive = isHoverActive,
                size = style.itemSize,
                filledPainter = style.filledPainter,
                unfilledPainter = style.unfilledPainter,
                filledColor = style.colors.filled,
                unfilledColor = style.colors.unfilled,
                hoverColor = style.colors.hover,
                fillBrush = style.colors.fillBrush,
                isActive = animations.animateScale && index == activeItemIndex,
                contentDescription = itemLabels?.getOrNull(index),
            )
        },
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
    fillBrush: Brush? = null,
    isActive: Boolean = false,
    contentDescription: String? = null,
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "ratingScale",
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
            tint = unfilledColor,
        )

        // Layer 2 — hover preview extension (only for items beyond the committed fill)
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
                tint = hoverColor,
            )
        }

        // Layer 3 — committed fill (always preserves the confirmed rating colour / brush)
        if (committedDisplayFraction > 0f) {
            val clipMod = if (committedDisplayFraction < 1f)
                Modifier.clip(FractionalClipShape(committedDisplayFraction))
            else
                Modifier

            if (fillBrush != null) {
                // Render icon as a white shape mask, then overlay the brush via SrcIn blend mode.
                // graphicsLayer offscreen compositing ensures the blend operates on isolated pixels.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(clipMod)
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = fillBrush, blendMode = BlendMode.SrcIn)
                        },
                ) {
                    Icon(
                        painter = filledPainter,
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White,
                    )
                }
            } else {
                Icon(
                    painter = filledPainter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize().then(clipMod),
                    tint = filledColor,
                )
            }
        }
    }
}

private fun adjustForRtl(rawX: Float, widthPx: Float, layoutDirection: LayoutDirection): Float =
    if (layoutDirection == LayoutDirection.Rtl) widthPx - rawX else rawX

private fun handleRatingKeyEvent(
    event: KeyEvent,
    displayValue: Float,
    config: RatingBarConfig,
    isRtl: Boolean,
    onValue: (Float) -> Unit,
): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    return when (event.key) {
        Key.DirectionRight -> {
            onValue(
                (displayValue + if (isRtl) -config.step else config.step)
                    .coerceIn(config.effectiveMin, config.max.toFloat())
            )
            true
        }
        Key.DirectionLeft -> {
            onValue(
                (displayValue + if (isRtl) config.step else -config.step)
                    .coerceIn(config.effectiveMin, config.max.toFloat())
            )
            true
        }
        Key.Plus -> {
            onValue((displayValue + config.step).coerceAtMost(config.max.toFloat()))
            true
        }
        Key.Minus -> {
            onValue((displayValue - config.step).coerceAtLeast(config.effectiveMin))
            true
        }
        Key.Home -> { onValue(config.effectiveMin); true }
        Key.MoveEnd -> { onValue(config.max.toFloat()); true }
        else -> {
            val digit = event.key.keyCode - Key.One.keyCode + 1
            if (digit in 1..9 && digit <= config.max) { onValue(digit.toFloat()); true } else false
        }
    }
}
