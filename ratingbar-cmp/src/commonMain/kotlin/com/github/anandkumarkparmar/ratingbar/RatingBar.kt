package com.github.anandkumarkparmar.ratingbar

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingBarState


/**
 * A generalized rating bar that allows custom content via the [itemContent] slot.
 * 
 * @param value The current rating value.
 * @param onValueChange Callback invoked when value changes.
 * @param onValueChangeFinished Callback invoked when value change gesture is finished (optional).
 * @param modifier Modifier for the container.
 * @param max Max rating.
 * @param step Step size.
 * @param readOnly Whether it's read-only.
 * @param itemSpacing Spacing between items.
 * @param itemContent composable lambda for each item. Received (index, fillFraction).
 */
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
) {
    val config = remember(max, step) { RatingBarConfig(max = max, step = step) }
    val state = remember(value, config) { RatingBarState(value = value, config = config) }
    val displayValue = state.steppedValue
    
    var widthPx by remember { mutableStateOf(0f) }
    val layoutDirection = LocalLayoutDirection.current
    val focusRequester = remember { FocusRequester() }
    
    val handleValueChange: (Float) -> Unit = { newValue ->
        if (!readOnly) {
            val newState = state.withValue(newValue)
            onValueChange(newState.steppedValue)
        }
    }

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
                                        val x = if (layoutDirection == LayoutDirection.Rtl) {
                                            widthPx - offset.x
                                        } else {
                                            offset.x
                                        }
                                        val clickRatio = x / widthPx
                                        val newValue = (clickRatio * max).coerceIn(0f, max.toFloat())
                                        handleValueChange(newValue)
                                        onValueChangeFinished?.invoke()
                                    }
                                }
                            )
                        }
                        .pointerInput(layoutDirection, widthPx, max) {
                            detectDragGestures(
                                onDragEnd = { onValueChangeFinished?.invoke() },
                                onDragCancel = { onValueChangeFinished?.invoke() }
                            ) { change, _ ->
                                change.consume()
                                if (widthPx > 0) {
                                    val x = if (layoutDirection == LayoutDirection.Rtl) {
                                        widthPx - change.position.x
                                    } else {
                                        change.position.x
                                    }
                                    val dragRatio = x / widthPx
                                    val newValue = (dragRatio * max).coerceIn(0f, max.toFloat())
                                    handleValueChange(newValue)
                                }
                            }
                        }
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                val isRtl = layoutDirection == LayoutDirection.Rtl
                                when (event.key) {
                                    Key.DirectionRight -> {
                                        val change = if (isRtl) -step else step
                                        handleValueChange((displayValue + change).coerceIn(0f, max.toFloat()))
                                        true
                                    }
                                    Key.DirectionLeft -> {
                                        val change = if (isRtl) step else -step
                                        handleValueChange((displayValue + change).coerceIn(0f, max.toFloat()))
                                        true
                                    }
                                    Key.Plus -> {
                                        handleValueChange((displayValue + step).coerceAtMost(max.toFloat()))
                                        true
                                    }
                                    Key.Minus -> {
                                        handleValueChange((displayValue - step).coerceAtLeast(0f))
                                        true
                                    }
                                    Key.Home -> {
                                        handleValueChange(0f)
                                        true
                                    }
                                    Key.MoveEnd -> {
                                        handleValueChange(max.toFloat())
                                        true
                                    }
                                    else -> {
                                        val digit = event.key.keyCode - Key.One.keyCode + 1
                                        if (digit in 1..9 && digit <= max) {
                                            handleValueChange(digit.toFloat())
                                            true
                                        } else false
                                    }
                                }
                            } else false
                        }
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        repeat(max) { index ->
            val fillFraction = RatingBarState.fillFraction(index, displayValue)
            itemContent(index, fillFraction)
        }
    }
}

/**
 * A rating bar composable that displays a row of items (stars by default) representing a rating value.
 *
 * This component is state-hoisted—the caller must manage the rating [value] via [onValueChange].
 *
 * @param value The current rating value (will be clamped to [0, max])
 * @param onValueChange Callback invoked when the rating changes
 * @param onValueChangeFinished Callback invoked when the rating change gesture is finished (optional)
 * @param modifier Modifier to be applied to the rating bar
 * @param max The maximum rating value (e.g., 5 for 5 stars)
 * @param step The step increment for rating values (e.g., 0.5 for half-star)
 * @param readOnly If true, the rating bar is display-only and cannot be interacted with
 * @param itemSize The size of each rating item
 * @param itemSpacing The spacing between rating items
 * @param filledPainter Painter for filled items (default: filled star icon)
 * @param unfilledPainter Painter for unfilled items (default: outlined star icon)
 * @param filledColor Color for filled items
 * @param unfilledColor Color for unfilled items
 */
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
) {
    RatingBar(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        max = max,
        step = step,
        readOnly = readOnly,
        itemSpacing = itemSpacing,
        itemContent = { index, fillFraction ->
             RatingBarItem(
                fillFraction = fillFraction,
                size = itemSize,
                filledPainter = filledPainter,
                unfilledPainter = unfilledPainter,
                filledColor = filledColor,
                unfilledColor = unfilledColor
            )
        }
    )
}


@Composable
private fun RatingBarItem(
    fillFraction: Float,
    size: Dp,
    filledPainter: Painter,
    unfilledPainter: Painter,
    filledColor: Color,
    unfilledColor: Color
) {
    Box(modifier = Modifier.size(size)) {
        Icon(
            painter = unfilledPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = unfilledColor
        )

        if (fillFraction > 0f) {
            Icon(
                painter = filledPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (fillFraction < 1f) {
                            Modifier.clip(FractionalClipShape(fillFraction))
                        } else {
                            Modifier
                        }
                    ),
                tint = filledColor
            )
        }
    }
}
