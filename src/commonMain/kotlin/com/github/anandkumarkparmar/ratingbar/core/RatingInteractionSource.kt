package com.github.anandkumarkparmar.ratingbar.core

/**
 * Identifies the input method that triggered a rating value change.
 *
 * Passed to the `onInteraction` callback on [com.github.anandkumarkparmar.ratingbar.RatingBar]
 * alongside each `onValueChange` call, so callers can distinguish how the user changed the value.
 *
 * Useful for analytics, source-specific haptic patterns, accessibility announcements, or
 * logging interaction patterns.
 *
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     onInteraction = { source ->
 *         analytics.track("rating_changed", mapOf("source" to source.name))
 *     }
 * )
 * ```
 */
public enum class RatingInteractionSource {
    /** Value changed via a single tap on an item. */
    Tap,

    /** Value changed by dragging across the bar. */
    Drag,

    /** Value changed via keyboard input (arrow keys, +/-, Home/End, digit keys). */
    Keyboard,

    /** Value changed via mouse wheel scroll (Desktop only). */
    Scroll,
}
