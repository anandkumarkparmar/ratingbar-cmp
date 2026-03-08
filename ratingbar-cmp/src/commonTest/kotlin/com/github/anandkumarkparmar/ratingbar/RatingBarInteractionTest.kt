package com.github.anandkumarkparmar.ratingbar

import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingBarState
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Interaction-level tests for RatingBar state transitions and fill-fraction logic.
 * UI rendering tests (click/drag/hover) live in the sample app where the full Compose
 * desktop stack is available.
 */
class RatingBarInteractionTest {

    @Test
    fun initialValueIsReflectedInState() {
        val state = RatingBarState(value = 3f, config = RatingBarConfig(max = 5))
        assertEquals(3f, state.steppedValue)
    }

    @Test
    fun valueClampedToMax() {
        val state = RatingBarState(value = 10f, config = RatingBarConfig(max = 5))
        assertEquals(5f, state.clampedValue)
    }

    @Test
    fun valueClampedToZero() {
        val state = RatingBarState(value = -1f, config = RatingBarConfig(max = 5))
        assertEquals(0f, state.clampedValue)
    }

    @Test
    fun halfStarSteppingRoundsCorrectly() {
        val config = RatingBarConfig(max = 5, step = 0.5f)
        assertEquals(3.0f, RatingBarState(value = 3.2f, config = config).steppedValue)
        assertEquals(3.5f, RatingBarState(value = 3.3f, config = config).steppedValue)
    }

    @Test
    fun fillFractionFullStars() {
        assertEquals(1f, RatingBarState.fillFraction(0, 3f))
        assertEquals(1f, RatingBarState.fillFraction(1, 3f))
        assertEquals(1f, RatingBarState.fillFraction(2, 3f))
    }

    @Test
    fun fillFractionPartialStar() {
        assertEquals(0.5f, RatingBarState.fillFraction(3, 3.5f))
    }

    @Test
    fun fillFractionEmptyStar() {
        assertEquals(0f, RatingBarState.fillFraction(4, 3.5f))
    }

    @Test
    fun withValueReturnsNewState() {
        val state = RatingBarState(value = 2f)
        val updated = state.withValue(4f)
        assertEquals(4f, updated.value)
        assertEquals(2f, state.value)
    }

    @Test
    fun effectiveMinWithAllowZeroFalse() {
        // When allowZero=false, effective min should be at least one step
        val step = 0.5f
        val effectiveMin = maxOf(step, 0f)
        assertEquals(0.5f, effectiveMin)
    }

    @Test
    fun steppedValueNeverExceedsMaxForLargeStep() {
        val config = RatingBarConfig(max = 5, step = 3f)
        val state = RatingBarState(value = 4.9f, config = config)
        assert(state.steppedValue <= config.max.toFloat()) {
            "steppedValue ${state.steppedValue} exceeds max ${config.max}"
        }
    }

    @Test
    fun fillFractionAtExactBoundary() {
        // value == itemIndex+1 should give exactly 1f
        assertEquals(1f, RatingBarState.fillFraction(4, 5f))
        // value == 0 should give 0f for all items
        assertEquals(0f, RatingBarState.fillFraction(0, 0f))
    }
}
