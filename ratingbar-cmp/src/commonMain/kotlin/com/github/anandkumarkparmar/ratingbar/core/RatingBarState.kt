package com.github.anandkumarkparmar.ratingbar.core

/**
 * Configuration for a rating bar.
 *
 * @property max The maximum rating value (e.g., 5 for 5 stars)
 * @property step The step increment for rating values (e.g., 0.5 for half-star increments)
 */
data class RatingBarConfig(
    val max: Int = 5,
    val step: Float = 1f
) {
    init {
        require(max > 0) { "max must be greater than 0" }
        require(step > 0) { "step must be greater than 0" }
    }
}

/**
 * State holder for rating bar value with clamping and step logic.
 *
 * @property value The current rating value
 * @property config Configuration for the rating bar
 */
data class RatingBarState(
    val value: Float,
    val config: RatingBarConfig = RatingBarConfig()
) {
    /**
     * The clamped value within [0, config.max]
     */
    val clampedValue: Float = value.coerceIn(0f, config.max.toFloat())
    
    /**
     * The value rounded to the nearest step
     */
    val steppedValue: Float = roundToStep(clampedValue, config.step)
    
    /**
     * Returns a new state with the given value, applying clamping and stepping.
     */
    fun withValue(newValue: Float): RatingBarState {
        return copy(value = newValue)
    }
    
    companion object {
        /**
         * Round a value to the nearest step.
         */
        fun roundToStep(value: Float, step: Float): Float {
            if (step == 0f) return value
            return (value / step).toInt() * step + 
                   if ((value % step) >= step / 2) step else 0f
        }
        
        /**
         * Calculate the fractional fill for a given item index.
         *
         * @param itemIndex The zero-based index of the item
         * @param value The current rating value
         * @return A value between 0f and 1f representing how full the item should be
         */
        fun fillFraction(itemIndex: Int, value: Float): Float {
            val itemValue = itemIndex + 1
            return when {
                value >= itemValue -> 1f
                value > itemIndex -> value - itemIndex
                else -> 0f
            }
        }
    }
}

/**
 * Event representing a rating bar interaction.
 *
 * @property value The new rating value
 * @property source The source of the interaction
 */
data class RatingBarEvent(
    val value: Float,
    val source: InteractionSource
)

/**
 * The source of a rating bar interaction.
 */
enum class InteractionSource {
    MOUSE,
    TOUCH,
    KEYBOARD,
    PROGRAMMATIC
}
