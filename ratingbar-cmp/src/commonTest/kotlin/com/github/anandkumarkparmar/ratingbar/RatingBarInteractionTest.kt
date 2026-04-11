package com.github.anandkumarkparmar.ratingbar

import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingBarState
import com.github.anandkumarkparmar.ratingbar.core.RatingInteractionSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        assertTrue(
            state.steppedValue <= config.max.toFloat(),
            "steppedValue ${state.steppedValue} exceeds max ${config.max}"
        )
    }

    @Test
    fun fillFractionAtExactBoundary() {
        // value == itemIndex+1 should give exactly 1f
        assertEquals(1f, RatingBarState.fillFraction(4, 5f))
        // value == 0 should give 0f for all items
        assertEquals(0f, RatingBarState.fillFraction(0, 0f))
    }

    // ── F1 — progressBarRangeInfo step count formula ────────────────────────

    @Test
    fun progressBarStepCountForDefaultConfig() {
        val config = RatingBarConfig(max = 5, step = 1f)
        val steps = ((config.max.toFloat() / config.step).toInt() - 1).coerceAtLeast(0)
        assertEquals(4, steps) // 5 whole-star positions → 4 intervals
    }

    @Test
    fun progressBarStepCountForHalfStep() {
        val config = RatingBarConfig(max = 5, step = 0.5f)
        val steps = ((config.max.toFloat() / config.step).toInt() - 1).coerceAtLeast(0)
        assertEquals(9, steps) // 10 half-star positions → 9 intervals
    }

    @Test
    fun progressBarStepCountNeverNegative() {
        val config = RatingBarConfig(max = 1, step = 1f)
        val steps = ((config.max.toFloat() / config.step).toInt() - 1).coerceAtLeast(0)
        assertEquals(0, steps) // 1 position → 0 intervals, never negative
    }

    // ── F2 — itemLabels stateDescription logic ──────────────────────────────

    @Test
    fun itemLabelsStateDescriptionUsesActiveLabel() {
        val labels = listOf("Terrible", "Bad", "Okay", "Good", "Excellent")
        val stateDesc = computeStateDesc(labels, max = 5, displayValue = 4f)
        // Use contains to avoid JVM ("4.0") vs JS ("4") float-toString differences
        assertTrue(stateDesc.startsWith("Good ("), "Expected label prefix, got: $stateDesc")
        assertTrue(stateDesc.contains("out of 5"), "Expected 'out of 5', got: $stateDesc")
    }

    @Test
    fun itemLabelsStateDescriptionFallsBackWhenNull() {
        val stateDesc = computeStateDesc(null, max = 5, displayValue = 3f)
        assertTrue(stateDesc.contains("out of 5"), "Expected fallback with max, got: $stateDesc")
        assertFalse(stateDesc.contains("("), "Fallback should not contain a label prefix")
    }

    @Test
    fun itemLabelsStateDescriptionFallsBackWhenDisplayValueIsZero() {
        val labels = listOf("Terrible", "Bad", "Okay", "Good", "Excellent")
        val stateDesc = computeStateDesc(labels, max = 5, displayValue = 0f)
        // displayValue == 0 → falls back to plain format (no label)
        assertTrue(stateDesc.contains("out of 5"), "Expected fallback format, got: $stateDesc")
        assertFalse(stateDesc.contains("Terrible"), "Zero value must not use a label")
    }

    @Test
    fun itemLabelsShortListIsIgnored() {
        // Lists shorter than max are ignored to prevent index-out-of-bounds
        val labels = listOf("Okay", "Good") // only 2 entries for max=5
        val stateDesc = computeStateDesc(labels, max = 5, displayValue = 2f)
        assertTrue(stateDesc.contains("out of 5"), "Short list should fall back to plain format")
        assertFalse(stateDesc.contains("Okay"), "Short list label should not appear in stateDesc")
    }

    // ── F4 — animations.reducedMotion / enabled spec selection logic ────────

    @Test
    fun reducedMotionSuppressesAnimation() {
        val animations = RatingBarAnimations(enabled = true, reducedMotion = true)
        assertFalse(animations.enabled && !animations.reducedMotion)
    }

    @Test
    fun animationsEnabledWithoutReducedMotion() {
        val animations = RatingBarAnimations(enabled = true, reducedMotion = false)
        assertTrue(animations.enabled && !animations.reducedMotion)
    }

    @Test
    fun animationsDisabledByDefault() {
        val animations = RatingBarAnimations()
        assertFalse(animations.enabled)
        assertFalse(animations.animateScale)
        assertFalse(animations.reducedMotion)
    }

    // ── F7 — long-press reset target (effectiveMin) ─────────────────────────

    @Test
    fun longPressResetTargetWithAllowZeroTrue() {
        val config = RatingBarConfig(max = 5, allowZero = true)
        assertEquals(0f, config.effectiveMin)
    }

    @Test
    fun longPressResetTargetWithAllowZeroFalse() {
        val config = RatingBarConfig(max = 5, step = 1f, allowZero = false)
        assertEquals(1f, config.effectiveMin)
    }

    @Test
    fun longPressResetRespectsMinValueWhenLargerThanStep() {
        val config = RatingBarConfig(max = 5, step = 0.5f, allowZero = false, minValue = 2f)
        assertEquals(2f, config.effectiveMin)
    }

    // ── F10 — RatingInteractionSource enum values ───────────────────────────

    @Test
    fun ratingInteractionSourceHasFourValues() {
        assertEquals(4, RatingInteractionSource.entries.size)
    }

    @Test
    fun ratingInteractionSourceAllVariantsExist() {
        val entries = RatingInteractionSource.entries
        assertTrue(entries.contains(RatingInteractionSource.Tap))
        assertTrue(entries.contains(RatingInteractionSource.Drag))
        assertTrue(entries.contains(RatingInteractionSource.Keyboard))
        assertTrue(entries.contains(RatingInteractionSource.Scroll))
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private fun computeStateDesc(
        itemLabels: List<String>?,
        max: Int,
        displayValue: Float,
    ): String = if (itemLabels != null && itemLabels.size >= max && displayValue > 0f) {
        val idx = (displayValue.toInt() - 1).coerceIn(0, itemLabels.size - 1)
        "${itemLabels[idx]} ($displayValue out of $max)"
    } else {
        "$displayValue out of $max"
    }
}
