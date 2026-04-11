package com.github.anandkumarkparmar.ratingbar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Locks the [RatingBarBehavior] data-class contract at the 0.5 boundary.
 *
 * Tests cover default construction, factory defaults, equals/hashCode, and copy semantics
 * so platform-flag combinations remain consistent across releases.
 */
class RatingBarBehaviorTest {

    @Test
    fun `default construction has all behaviour flags disabled`() {
        val behavior = RatingBarBehavior()

        assertFalse(behavior.showHoverPreview, "showHoverPreview should default to false")
        assertFalse(behavior.enableScrollInput, "enableScrollInput should default to false")
        assertFalse(behavior.hapticFeedback, "hapticFeedback should default to false")
        assertFalse(behavior.enableLongPressReset, "enableLongPressReset should default to false")
    }

    @Test
    fun `factory with default arguments matches default constructor`() {
        val fromFactory = RatingBarDefaults.behavior()
        val fromConstructor = RatingBarBehavior()

        assertEquals(fromConstructor, fromFactory)
    }

    @Test
    fun `factory propagates all flags`() {
        val behavior = RatingBarDefaults.behavior(
            showHoverPreview = true,
            enableScrollInput = true,
            hapticFeedback = true,
            enableLongPressReset = true,
        )

        assertTrue(behavior.showHoverPreview)
        assertTrue(behavior.enableScrollInput)
        assertTrue(behavior.hapticFeedback)
        assertTrue(behavior.enableLongPressReset)
    }

    @Test
    fun `equals returns true for instances with identical flags`() {
        val a = RatingBarBehavior(showHoverPreview = true, hapticFeedback = true)
        val b = RatingBarBehavior(showHoverPreview = true, hapticFeedback = true)

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `equals returns false when any flag differs`() {
        val baseline = RatingBarBehavior()

        assertNotEquals(baseline, baseline.copy(showHoverPreview = true))
        assertNotEquals(baseline, baseline.copy(enableScrollInput = true))
        assertNotEquals(baseline, baseline.copy(hapticFeedback = true))
        assertNotEquals(baseline, baseline.copy(enableLongPressReset = true))
    }

    @Test
    fun `copy preserves untouched flags`() {
        val original = RatingBarBehavior(
            showHoverPreview = true,
            enableScrollInput = true,
            hapticFeedback = false,
            enableLongPressReset = true,
        )
        val modified = original.copy(hapticFeedback = true)

        assertTrue(modified.showHoverPreview)
        assertTrue(modified.enableScrollInput)
        assertTrue(modified.hapticFeedback)
        assertTrue(modified.enableLongPressReset)
    }
}
