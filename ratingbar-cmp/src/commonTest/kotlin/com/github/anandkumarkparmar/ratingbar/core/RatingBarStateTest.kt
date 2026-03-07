package com.github.anandkumarkparmar.ratingbar.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RatingBarStateTest {
    
    @Test
    fun testClampingValue() {
        val state = RatingBarState(value = 7f, config = RatingBarConfig(max = 5))
        assertEquals(5f, state.clampedValue, "Value should be clamped to max")
        
        val negativeState = RatingBarState(value = -2f, config = RatingBarConfig(max = 5))
        assertEquals(0f, negativeState.clampedValue, "Negative value should be clamped to 0")
    }
    
    @Test
    fun testSteppingValue() {
        val config = RatingBarConfig(max = 5, step = 0.5f)
        
        val state1 = RatingBarState(value = 3.2f, config = config)
        assertEquals(3.0f, state1.steppedValue, "3.2 should round to 3.0 with step 0.5")
        
        val state2 = RatingBarState(value = 3.3f, config = config)
        assertEquals(3.5f, state2.steppedValue, "3.3 should round to 3.5 with step 0.5")
        
        val state3 = RatingBarState(value = 2.75f, config = config)
        assertEquals(3.0f, state3.steppedValue, "2.75 should round to 3.0 with step 0.5")
    }
    
    @Test
    fun testFillFraction() {
        // Full stars
        assertEquals(1f, RatingBarState.fillFraction(0, 3.5f))
        assertEquals(1f, RatingBarState.fillFraction(1, 3.5f))
        assertEquals(1f, RatingBarState.fillFraction(2, 3.5f))
        
        // Half star
        assertEquals(0.5f, RatingBarState.fillFraction(3, 3.5f))
        
        // Empty stars
        assertEquals(0f, RatingBarState.fillFraction(4, 3.5f))
        assertEquals(0f, RatingBarState.fillFraction(5, 3.5f))
    }
    
    @Test
    fun testConfigValidation() {
        assertFailsWith<IllegalArgumentException> {
            RatingBarConfig(max = 0)
        }
        
        assertFailsWith<IllegalArgumentException> {
            RatingBarConfig(max = -5)
        }
        
        assertFailsWith<IllegalArgumentException> {
            RatingBarConfig(max = 5, step = 0f)
        }
        
        assertFailsWith<IllegalArgumentException> {
            RatingBarConfig(max = 5, step = -0.5f)
        }
    }
    
    @Test
    fun testWithValue() {
        val state = RatingBarState(value = 3f, config = RatingBarConfig(max = 5))
        val newState = state.withValue(4.5f)
        
        assertEquals(4.5f, newState.value)
        assertEquals(3f, state.value, "Original state should be unchanged")
    }
    
    @Test
    fun testRoundToStep() {
        assertEquals(3.0f, RatingBarState.roundToStep(3.2f, 0.5f))
        assertEquals(3.5f, RatingBarState.roundToStep(3.3f, 0.5f))
        assertEquals(4.0f, RatingBarState.roundToStep(4.1f, 1f))
        assertEquals(2.5f, RatingBarState.roundToStep(2.6f, 0.5f))
    }
    
    @Test
    fun testSteppedValueNeverExceedsMax() {
        val config = RatingBarConfig(max = 5, step = 2f)
        val state = RatingBarState(value = 5f, config = config)
        assertTrue(
            state.steppedValue <= config.max,
            "steppedValue (${state.steppedValue}) must not exceed max (${config.max})"
        )
        
        val state2 = RatingBarState(value = 4.5f, config = RatingBarConfig(max = 5, step = 3f))
        assertTrue(
            state2.steppedValue <= 5f,
            "steppedValue (${state2.steppedValue}) must not exceed max (5)"
        )
    }
    
    @Test
    fun testStepGreaterThanMaxValidation() {
        assertFailsWith<IllegalArgumentException>("step > max should be rejected") {
            RatingBarConfig(max = 3, step = 5f)
        }
        
        assertFailsWith<IllegalArgumentException>("step > max should be rejected") {
            RatingBarConfig(max = 1, step = 1.5f)
        }
    }
    
    @Test
    fun testStepEqualToMaxIsValid() {
        val config = RatingBarConfig(max = 5, step = 5f)
        val state = RatingBarState(value = 3f, config = config)
        assertTrue(state.steppedValue <= config.max)
    }
    
    @Test
    fun testFillFractionEdgeCases() {
        assertEquals(0f, RatingBarState.fillFraction(0, 0f), "Zero value should give empty fill")
        assertEquals(1f, RatingBarState.fillFraction(0, 1f), "Exact item boundary should be full")
        assertEquals(0f, RatingBarState.fillFraction(0, -1f), "Negative value should give empty fill")
        assertEquals(0.1f, RatingBarState.fillFraction(0, 0.1f), "Small fractional value")
        assertEquals(0.9f, RatingBarState.fillFraction(0, 0.9f), "Large fractional value below 1")
    }
    
    @Test
    fun testRoundToStepWithZeroStep() {
        assertEquals(3.7f, RatingBarState.roundToStep(3.7f, 0f), "Zero step should return value as-is")
    }
    
    @Test
    fun testRoundToStepBoundaryValues() {
        assertEquals(0f, RatingBarState.roundToStep(0f, 0.5f), "Zero value rounds to zero")
        assertEquals(0.5f, RatingBarState.roundToStep(0.25f, 0.5f), "0.25 rounds to 0.5 with step 0.5")
        assertEquals(0f, RatingBarState.roundToStep(0.2f, 0.5f), "0.2 rounds to 0.0 with step 0.5")
        assertEquals(1.0f, RatingBarState.roundToStep(1.0f, 0.5f), "Exact step boundary stays")
    }
}
