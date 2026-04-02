package com.github.anandkumarkparmar.ratingbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Creates and remembers a [MutableState] holding a rating value.
 *
 * Convenience alternative to `remember { mutableStateOf(initialValue) }`.
 *
 * **Note:** This uses `remember`, so the value is lost on Android configuration changes
 * (rotation, locale change) and Compose Navigation back-stack. Use
 * [rememberSaveableRatingBarState] when state survival is required.
 *
 * Usage:
 * ```kotlin
 * var rating by rememberRatingBarState(initialValue = 3f)
 * RatingBar(value = rating, onValueChange = { rating = it })
 * ```
 *
 * @param initialValue The initial rating value. Defaults to 0f.
 */
@Composable
public fun rememberRatingBarState(initialValue: Float = 0f): MutableState<Float> =
    remember { mutableStateOf(initialValue) }

/**
 * Creates and remembers a [MutableState] holding a rating value that survives
 * Android configuration changes and Compose Navigation back-stack restoration.
 *
 * Uses [rememberSaveable] under the hood — `Float` is natively saveable so no
 * custom [androidx.compose.runtime.saveable.Saver] is required.
 *
 * Use this instead of [rememberRatingBarState] whenever the rating is part of a
 * form or screen that must survive rotation or process death.
 *
 * Usage:
 * ```kotlin
 * var rating by rememberSaveableRatingBarState(initialValue = 3f)
 * RatingBar(value = rating, onValueChange = { rating = it })
 * ```
 *
 * @param initialValue The initial rating value. Defaults to 0f.
 */
@Composable
public fun rememberSaveableRatingBarState(initialValue: Float = 0f): MutableState<Float> =
    rememberSaveable { mutableStateOf(initialValue) }
