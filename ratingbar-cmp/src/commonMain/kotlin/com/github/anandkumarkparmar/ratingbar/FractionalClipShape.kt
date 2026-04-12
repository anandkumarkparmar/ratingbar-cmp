package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * A [Shape] that clips a composable to show only a fractional portion of its width.
 *
 * Used internally by [RatingBar] to render partial fills (e.g., a half-star at `fillFraction = 0.5f`).
 * The clip respects the current [androidx.compose.ui.unit.LayoutDirection]:
 * - **LTR**: clips from the left edge up to `fraction * width`.
 * - **RTL**: clips from the right edge down to `(1 - fraction) * width`.
 *
 * You can also use this shape directly in your own composables:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .size(48.dp)
 *         .clip(FractionalClipShape(0.75f))
 *         .background(Color.Yellow)
 * )
 * ```
 *
 * @param fraction Fill fraction in `[0f, 1f]`. `0f` produces an empty (invisible) clip;
 *   `1f` produces a full-width (unclipped) result.
 */
public class FractionalClipShape(
    private val fraction: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width

        val left = if (layoutDirection == LayoutDirection.Ltr) {
            0f
        } else {
            width * (1f - fraction)
        }
        
        val right = if (layoutDirection == LayoutDirection.Ltr) {
            width * fraction
        } else {
            width
        }

        return Outline.Rectangle(
            Rect(
                left = left,
                top = 0f,
                right = right,
                bottom = size.height
            )
        )
    }
}
