package com.github.anandkumarkparmar.ratingbar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp

/**
 * A placeholder composable that mimics the layout of a [RatingBar] while content is loading.
 *
 * Displays a row of rounded rectangles with an animated shimmer sweep. Useful for skeleton
 * screens when the rating value hasn't been fetched yet.
 *
 * ```kotlin
 * if (isLoading) {
 *     RatingBarPlaceholder()
 * } else {
 *     RatingBar(value = rating, onValueChange = { rating = it })
 * }
 * ```
 *
 * @param modifier Modifier applied to the outer [Row].
 * @param max Number of placeholder items to display. Must be > 0.
 * @param itemSize Size of each placeholder item. Defaults to [RatingBarDefaults.SizeMedium].
 * @param itemSpacing Spacing between adjacent items. Defaults to [RatingBarDefaults.ItemSpacing].
 * @param shimmerBaseColor Base (dark) colour of the shimmer. Defaults to
 *   `MaterialTheme.colorScheme.surfaceVariant`.
 * @param shimmerHighlightColor Highlight (light) colour of the shimmer sweep. Defaults to
 *   `MaterialTheme.colorScheme.surface`.
 * @param animationDurationMillis Duration of one shimmer sweep cycle in milliseconds.
 *   Defaults to [RatingBarDefaults.ShimmerDurationMillis] (1000ms).
 * @param reducedMotion If `true`, the shimmer animation is suppressed and items are rendered
 *   in a static [shimmerBaseColor]. Pass `true` when the user has enabled the OS reduced-motion
 *   accessibility setting.
 */
@Composable
public fun RatingBarPlaceholder(
    modifier: Modifier = Modifier,
    max: Int = 5,
    itemSize: Dp = RatingBarDefaults.SizeMedium,
    itemSpacing: Dp = RatingBarDefaults.ItemSpacing,
    shimmerBaseColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    shimmerHighlightColor: Color = MaterialTheme.colorScheme.surface,
    animationDurationMillis: Int = RatingBarDefaults.ShimmerDurationMillis,
    reducedMotion: Boolean = false,
) {
    require(max > 0) { "max must be greater than 0, but was $max" }
    require(animationDurationMillis > 0) {
        "animationDurationMillis must be greater than 0, but was $animationDurationMillis"
    }

    val brush = if (reducedMotion) {
        Brush.linearGradient(listOf(shimmerBaseColor, shimmerBaseColor))
    } else {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val offset by transition.animateFloat(
            initialValue = -1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = animationDurationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "shimmerOffset",
        )
        // Sweep a narrow highlight band across the item width. The offset drives the
        // gradient's x-positions, keeping the highlight sharp and evenly paced.
        val itemWidthPx = itemSize.value
        Brush.linearGradient(
            colors = listOf(shimmerBaseColor, shimmerHighlightColor, shimmerBaseColor),
            start = androidx.compose.ui.geometry.Offset(offset * itemWidthPx, 0f),
            end = androidx.compose.ui.geometry.Offset((offset + 1f) * itemWidthPx, 0f),
            tileMode = TileMode.Clamp,
        )
    }

    val cornerRadius = itemSize / 4
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        repeat(max) {
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(brush),
            )
        }
    }
}
