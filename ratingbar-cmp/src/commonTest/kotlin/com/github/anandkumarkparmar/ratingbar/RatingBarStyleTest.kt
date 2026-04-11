package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Locks the [RatingBarStyle] contract at the 0.5 boundary.
 *
 * [RatingBarStyle] is a manual (non-data) class because [androidx.compose.ui.graphics.painter.Painter]
 * doesn't compose cleanly into Kotlin's generated equals. These tests exercise the hand-rolled
 * equals/hashCode implementation and construction.
 *
 * Note: the factory function `RatingBarDefaults.style(...)` is @Composable, so it can't be
 * invoked from a plain unit test. This file exercises the class directly with concrete
 * [ColorPainter] instances.
 */
class RatingBarStyleTest {

    private val filledPainter = ColorPainter(Color.Red)
    private val unfilledPainter = ColorPainter(Color.Gray)
    private val colors = RatingBarColors(
        filled = Color.Red,
        unfilled = Color.Gray,
        hover = Color.Yellow,
    )

    private fun style(
        itemSize: Int = 24,
        itemSpacing: Int = 4,
        filled: ColorPainter = filledPainter,
        unfilled: ColorPainter = unfilledPainter,
        ratingBarColors: RatingBarColors = colors,
    ) = RatingBarStyle(
        itemSize = itemSize.dp,
        itemSpacing = itemSpacing.dp,
        filledPainter = filled,
        unfilledPainter = unfilled,
        colors = ratingBarColors,
    )

    @Test
    fun `construction exposes each field`() {
        val subject = style(itemSize = 32, itemSpacing = 8)

        assertEquals(32.dp, subject.itemSize)
        assertEquals(8.dp, subject.itemSpacing)
        assertEquals(filledPainter, subject.filledPainter)
        assertEquals(unfilledPainter, subject.unfilledPainter)
        assertEquals(colors, subject.colors)
    }

    @Test
    fun `equals returns true for instances with identical fields`() {
        val a = style()
        val b = style()

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `equals returns false when itemSize differs`() {
        assertNotEquals(style(itemSize = 24), style(itemSize = 32))
    }

    @Test
    fun `equals returns false when itemSpacing differs`() {
        assertNotEquals(style(itemSpacing = 4), style(itemSpacing = 8))
    }

    @Test
    fun `equals returns false when filledPainter differs`() {
        val other = ColorPainter(Color.Green)
        assertNotEquals(style(filled = filledPainter), style(filled = other))
    }

    @Test
    fun `equals returns false when unfilledPainter differs`() {
        val other = ColorPainter(Color.White)
        assertNotEquals(style(unfilled = unfilledPainter), style(unfilled = other))
    }

    @Test
    fun `equals returns false when colors differ`() {
        val otherColors = colors.copy(filled = Color.Blue)
        assertNotEquals(style(ratingBarColors = colors), style(ratingBarColors = otherColors))
    }

    @Test
    fun `equals is reflexive symmetric and consistent`() {
        val subject = style()

        // reflexive
        assertEquals(subject, subject)

        // symmetric
        val twin = style()
        assertEquals(subject, twin)
        assertEquals(twin, subject)

        // consistent
        assertEquals(subject.hashCode(), twin.hashCode())
        assertEquals(subject.hashCode(), subject.hashCode())
    }
}
