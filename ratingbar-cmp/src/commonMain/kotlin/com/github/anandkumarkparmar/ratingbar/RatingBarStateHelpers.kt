package com.github.anandkumarkparmar.ratingbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Creates and remembers a [MutableState] holding a rating value.
 *
 * Convenience alternative to `remember { mutableStateOf(initialValue) }`.
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
