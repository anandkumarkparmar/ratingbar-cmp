package com.github.anandkumarkparmar.ratingbar.core

import kotlin.math.roundToInt

/**
 * Configuration for a rating bar's constraints.
 *
 * Pass a [RatingBarConfig] to [RatingBar] or [RatingBarState] to control the rating range,
 * step granularity, and minimum value behaviour.
 *
 * @property max The maximum rating value (e.g., `5` for a 5-star bar). Must be > 0.
 * @property step The step increment for rating values (e.g., `0.5f` for half-star). Must be > 0 and ≤ max.
 * @property allowZero If `false`, the minimum selectable value is at least one [step]. Prevents
 *   the rating from being cleared to zero via tap, drag, scroll, or keyboard.
 * @property minValue Explicit minimum selectable value. When [allowZero] is `false` and [step]
 *   is larger than [minValue], the effective minimum is [step].
 * @throws IllegalArgumentException if `max ≤ 0`, `step ≤ 0`, `step > max`, or `minValue < 0`.
 */
public data class RatingBarConfig(
    val max: Int = 5,
    val step: Float = 1f,
    val allowZero: Boolean = true,
    val minValue: Float = 0f,
) {
    init {
        require(max > 0) { "max must be greater than 0" }
        require(step > 0) { "step must be greater than 0" }
        require(step <= max) { "step must be less than or equal to max" }
        require(minValue >= 0f) { "minValue must be >= 0" }
    }

    /**
     * The effective minimum selectable value, derived from [allowZero] and [minValue].
     *
     * - When [allowZero] is `true`: equals [minValue].
     * - When [allowZero] is `false`: `maxOf(step, minValue)`.
     */
    val effectiveMin: Float = if (!allowZero) maxOf(step, minValue) else minValue
}

/**
 * Immutable state holder for a rating bar value with clamping and step-rounding logic.
 *
 * [RatingBarState] is a pure value type — it does not hold any Compose state. It is used
 * internally by [RatingBar] to compute [clampedValue] and [steppedValue] from a raw [value].
 * You can also use it directly to validate or round rating values outside of composition.
 *
 * @property value The raw rating value as provided by the caller.
 * @property config Configuration constraints for the rating bar.
 */
public data class RatingBarState(
    val value: Float,
    val config: RatingBarConfig = RatingBarConfig()
) {
    /**
     * [value] clamped to the range `[0, config.max]`.
     */
    val clampedValue: Float = value.coerceIn(0f, config.max.toFloat())

    /**
     * [clampedValue] rounded to the nearest multiple of [RatingBarConfig.step] and then
     * re-clamped to `[0, config.max]`.
     *
     * This is the value that [RatingBar] uses for rendering and exposes via `onValueChange`.
     */
    val steppedValue: Float = roundToStep(clampedValue, config.step)
        .coerceIn(0f, config.max.toFloat())

    /**
     * Returns a new [RatingBarState] with [value] replaced by [newValue], keeping the
     * same [config]. Clamping and stepping are applied automatically via [steppedValue].
     *
     * @param newValue The new raw rating value.
     */
    public fun withValue(newValue: Float): RatingBarState {
        return copy(value = newValue)
    }

    public companion object {
        /**
         * Rounds [value] to the nearest multiple of [step].
         *
         * Uses [kotlin.math.roundToInt] to avoid the floating-point precision errors that arise
         * from integer truncation (`(value / step).toInt() * step`) or the modulo approach
         * (`value % step`), both of which can produce incorrect results at exact boundaries
         * (e.g., `2.5f % 0.5f` is not exactly `0f` on some platforms).
         *
         * Returns [value] unchanged when [step] is `0f`.
         *
         * @param value The value to round.
         * @param step The step size. Must be ≥ 0.
         * @return The nearest multiple of [step], or [value] if [step] is zero.
         */
        public fun roundToStep(value: Float, step: Float): Float {
            if (step == 0f) return value
            return (value / step).roundToInt() * step
        }

        /**
         * Computes the fractional fill amount for a single rating item at [itemIndex].
         *
         * @param itemIndex Zero-based index of the item (e.g., `0` for the first star).
         * @param value The current rating value (stepped, not raw).
         * @return A value in `[0f, 1f]`:
         *   - `1f` when the item is fully filled (`value >= itemIndex + 1`)
         *   - `0f` when the item is empty (`value <= itemIndex`)
         *   - A partial fraction (`value - itemIndex`) for the partially filled item
         */
        public fun fillFraction(itemIndex: Int, value: Float): Float {
            val itemValue = itemIndex + 1
            return when {
                value >= itemValue -> 1f
                value > itemIndex -> value - itemIndex
                else -> 0f
            }
        }
    }
}
