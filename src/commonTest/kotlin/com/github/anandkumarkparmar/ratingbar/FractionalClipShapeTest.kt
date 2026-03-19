package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals

class FractionalClipShapeTest {

    private val size = Size(100f, 50f)
    private val density = Density(1f)

    // ── LTR ──────────────────────────────────────────────────────────────────

    @Test
    fun ltr_emptyFraction_clipCollapses() {
        val rect = outline(0f, LayoutDirection.Ltr)
        assertEquals(0f, rect.left)
        assertEquals(0f, rect.right)
    }

    @Test
    fun ltr_halfFraction_clipsFromLeft() {
        val rect = outline(0.5f, LayoutDirection.Ltr)
        assertEquals(0f, rect.left)
        assertEquals(50f, rect.right)
    }

    @Test
    fun ltr_fullFraction_noClip() {
        val rect = outline(1f, LayoutDirection.Ltr)
        assertEquals(0f, rect.left)
        assertEquals(100f, rect.right)
    }

    @Test
    fun ltr_quarterFraction() {
        val rect = outline(0.25f, LayoutDirection.Ltr)
        assertEquals(0f, rect.left)
        assertEquals(25f, rect.right)
    }

    // ── RTL ──────────────────────────────────────────────────────────────────

    @Test
    fun rtl_emptyFraction_clipCollapses() {
        val rect = outline(0f, LayoutDirection.Rtl)
        assertEquals(100f, rect.left)
        assertEquals(100f, rect.right)
    }

    @Test
    fun rtl_halfFraction_clipsFromRight() {
        val rect = outline(0.5f, LayoutDirection.Rtl)
        assertEquals(50f, rect.left)
        assertEquals(100f, rect.right)
    }

    @Test
    fun rtl_fullFraction_noClip() {
        val rect = outline(1f, LayoutDirection.Rtl)
        assertEquals(0f, rect.left)
        assertEquals(100f, rect.right)
    }

    @Test
    fun rtl_quarterFraction() {
        val rect = outline(0.25f, LayoutDirection.Rtl)
        assertEquals(75f, rect.left)
        assertEquals(100f, rect.right)
    }

    // ── Height preserved in both directions ───────────────────────────────────

    @Test
    fun heightIsAlwaysPreserved() {
        listOf(LayoutDirection.Ltr, LayoutDirection.Rtl).forEach { dir ->
            val rect = outline(0.5f, dir)
            assertEquals(0f, rect.top, "top should be 0 for $dir")
            assertEquals(50f, rect.bottom, "bottom should match size height for $dir")
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun outline(fraction: Float, direction: LayoutDirection): androidx.compose.ui.geometry.Rect {
        val o = FractionalClipShape(fraction).createOutline(size, direction, density) as Outline.Rectangle
        return o.rect
    }
}
