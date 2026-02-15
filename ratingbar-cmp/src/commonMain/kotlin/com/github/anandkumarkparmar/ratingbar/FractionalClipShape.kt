package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * A shape that clips based on a fractional progress and layout direction.
 * Useful for partial filling of rating items (e.g. half-star).
 */
class FractionalClipShape(
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
