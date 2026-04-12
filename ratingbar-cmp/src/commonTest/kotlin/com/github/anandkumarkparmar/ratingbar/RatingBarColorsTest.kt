package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Locks the [RatingBarColors] data-class contract at the 0.5 boundary.
 *
 * Note: the factory function `RatingBarDefaults.colors(...)` is @Composable (it reads
 * MaterialTheme defaults), so it can't be invoked from a plain unit test. This file
 * exercises the data class directly with explicit color values.
 */
class RatingBarColorsTest {

    @Test
    fun `construction with explicit colors exposes each field`() {
        val colors = RatingBarColors(
            filled = Color.Red,
            unfilled = Color.Gray,
            hover = Color.Yellow,
        )

        assertEquals(Color.Red, colors.filled)
        assertEquals(Color.Gray, colors.unfilled)
        assertEquals(Color.Yellow, colors.hover)
        assertNull(colors.fillBrush, "fillBrush should default to null")
    }

    @Test
    fun `construction with fillBrush exposes the brush`() {
        val brush: Brush = SolidColor(Color.Blue)
        val colors = RatingBarColors(
            filled = Color.Red,
            unfilled = Color.Gray,
            hover = Color.Yellow,
            fillBrush = brush,
        )

        assertNotNull(colors.fillBrush)
        assertEquals(brush, colors.fillBrush)
    }

    @Test
    fun `equals returns true for instances with identical fields`() {
        val a = RatingBarColors(filled = Color.Red, unfilled = Color.Gray, hover = Color.Yellow)
        val b = RatingBarColors(filled = Color.Red, unfilled = Color.Gray, hover = Color.Yellow)

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `equals returns false when any color differs`() {
        val baseline = RatingBarColors(
            filled = Color.Red,
            unfilled = Color.Gray,
            hover = Color.Yellow,
        )

        assertNotEquals(baseline, baseline.copy(filled = Color.Blue))
        assertNotEquals(baseline, baseline.copy(unfilled = Color.White))
        assertNotEquals(baseline, baseline.copy(hover = Color.Green))
    }

    @Test
    fun `equals distinguishes null fillBrush from non-null fillBrush`() {
        val withoutBrush = RatingBarColors(
            filled = Color.Red,
            unfilled = Color.Gray,
            hover = Color.Yellow,
            fillBrush = null,
        )
        val withBrush = withoutBrush.copy(fillBrush = SolidColor(Color.Blue))

        assertNotEquals(withoutBrush, withBrush)
    }

    @Test
    fun `copy preserves untouched fields`() {
        val original = RatingBarColors(
            filled = Color.Red,
            unfilled = Color.Gray,
            hover = Color.Yellow,
            fillBrush = SolidColor(Color.Blue),
        )
        val modified = original.copy(filled = Color.Magenta)

        assertEquals(Color.Magenta, modified.filled, "filled should be overridden")
        assertEquals(Color.Gray, modified.unfilled, "unfilled should be preserved")
        assertEquals(Color.Yellow, modified.hover, "hover should be preserved")
        assertEquals(original.fillBrush, modified.fillBrush, "fillBrush should be preserved")
    }
}
