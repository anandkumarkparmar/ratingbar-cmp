package com.github.anandkumarkparmar.ratingbar

import androidx.compose.animation.core.tween
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Locks the [RatingBarAnimations] data-class contract at the 0.5 boundary.
 *
 * Tests cover default construction, factory defaults, equals/hashCode, and copy semantics
 * so future minor releases can't silently drift a field default or break structural equality.
 */
class RatingBarAnimationsTest {

    @Test
    fun `default construction has all animations disabled`() {
        val animations = RatingBarAnimations()

        assertFalse(animations.enabled, "enabled should default to false")
        assertFalse(animations.animateScale, "animateScale should default to false")
        assertFalse(animations.reducedMotion, "reducedMotion should default to false")
        assertSame(
            RatingBarDefaults.RatingAnimationSpec,
            animations.spec,
            "spec should default to RatingBarDefaults.RatingAnimationSpec"
        )
    }

    @Test
    fun `factory with default arguments matches default constructor`() {
        val fromFactory = RatingBarDefaults.animations()
        val fromConstructor = RatingBarAnimations()

        assertEquals(fromConstructor, fromFactory)
    }

    @Test
    fun `factory propagates non-default arguments`() {
        val customSpec = tween<Float>(durationMillis = 500)
        val animations = RatingBarDefaults.animations(
            enabled = true,
            spec = customSpec,
            animateScale = true,
            reducedMotion = true,
        )

        assertTrue(animations.enabled)
        assertTrue(animations.animateScale)
        assertTrue(animations.reducedMotion)
        assertSame(customSpec, animations.spec)
    }

    @Test
    fun `equals returns true for instances with identical fields`() {
        val a = RatingBarAnimations(enabled = true, animateScale = true)
        val b = RatingBarAnimations(enabled = true, animateScale = true)

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `equals returns false when any field differs`() {
        val baseline = RatingBarAnimations(enabled = true)

        assertNotEquals(baseline, baseline.copy(enabled = false))
        assertNotEquals(baseline, baseline.copy(animateScale = true))
        assertNotEquals(baseline, baseline.copy(reducedMotion = true))
    }

    @Test
    fun `copy preserves untouched fields`() {
        val original = RatingBarAnimations(
            enabled = true,
            animateScale = true,
            reducedMotion = false,
        )
        val modified = original.copy(reducedMotion = true)

        assertTrue(modified.enabled, "enabled should be preserved")
        assertTrue(modified.animateScale, "animateScale should be preserved")
        assertTrue(modified.reducedMotion, "reducedMotion should be overridden")
        assertSame(original.spec, modified.spec, "spec should be preserved")
    }
}
