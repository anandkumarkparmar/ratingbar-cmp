package com.github.anandkumarkparmar.ratingbar

import androidx.compose.runtime.Immutable

/**
 * Platform and interaction behaviour flags for [RatingBar].
 *
 * Create an instance via [RatingBarDefaults.behavior]:
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     behavior = RatingBarDefaults.behavior(hapticFeedback = true, showHoverPreview = true)
 * )
 * ```
 *
 * @property showHoverPreview If `true`, hovering the cursor on Desktop/Web shows a live fill
 *   preview at the cursor position. No-op on Android/iOS where hover events don't fire.
 * @property enableScrollInput If `true`, mouse wheel scroll on Desktop increments/decrements
 *   the rating by one step per tick. No-op on Android/iOS/Web.
 * @property hapticFeedback If `true`, a haptic pulse fires on Android each time the stepped
 *   value changes during tap or drag. Uses [androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress].
 *   No-op on Desktop, iOS, and Web. Requires the device to have vibration enabled; suppressed
 *   by the OS in Silent mode on Android 13+.
 * @property enableLongPressReset If `true`, a long-press anywhere on the bar resets the rating
 *   to the effective minimum value (respects `config.allowZero` and `config.minValue`).
 *   `onValueChangeFinished` is fired after the reset. No-op when `readOnly = true`.
 */
@Immutable
public data class RatingBarBehavior(
    val showHoverPreview: Boolean = false,
    val enableScrollInput: Boolean = false,
    val hapticFeedback: Boolean = false,
    val enableLongPressReset: Boolean = false,
)

/**
 * Creates a [RatingBarBehavior] with the given platform and interaction flags.
 *
 * @param showHoverPreview Enable hover fill preview on Desktop/Web.
 * @param enableScrollInput Enable mouse wheel input on Desktop.
 * @param hapticFeedback Enable haptic pulse on Android.
 * @param enableLongPressReset Enable long-press to reset to effective minimum.
 */
public fun RatingBarDefaults.behavior(
    showHoverPreview: Boolean = false,
    enableScrollInput: Boolean = false,
    hapticFeedback: Boolean = false,
    enableLongPressReset: Boolean = false,
): RatingBarBehavior = RatingBarBehavior(
    showHoverPreview = showHoverPreview,
    enableScrollInput = enableScrollInput,
    hapticFeedback = hapticFeedback,
    enableLongPressReset = enableLongPressReset,
)
