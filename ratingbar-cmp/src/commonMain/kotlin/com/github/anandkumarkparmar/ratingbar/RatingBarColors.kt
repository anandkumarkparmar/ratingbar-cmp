package com.github.anandkumarkparmar.ratingbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Colour and fill configuration for the default star [RatingBar] overload.
 *
 * Create an instance via [RatingBarDefaults.colors]:
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     style = RatingBarDefaults.style(
 *         colors = RatingBarDefaults.colors(filled = Color(0xFFFFB300))
 *     )
 * )
 * ```
 *
 * @property filled Color applied to filled (selected) items.
 * @property unfilled Color applied to unfilled (empty) items.
 * @property hover Color applied to items during a hover-preview on Desktop/Web.
 * @property fillBrush Optional [Brush] applied to the filled layer instead of [filled].
 *   When non-null, overrides [filled] with a gradient or other brush sweep across the
 *   filled portion. Useful for red→yellow→green sentiment progressions. Set to `null`
 *   (the default) to use [filled] as a solid color.
 */
@Immutable
public data class RatingBarColors(
    val filled: Color,
    val unfilled: Color,
    val hover: Color,
    val fillBrush: Brush? = null,
)

/**
 * Creates a [RatingBarColors] with sensible Material-themed defaults.
 *
 * @param filled Color applied to filled items. Defaults to `MaterialTheme.colorScheme.primary`.
 * @param unfilled Color applied to unfilled items. Defaults to `onSurface` at 30% alpha.
 * @param hover Color applied during hover preview. Defaults to [filled] at 60% alpha.
 * @param fillBrush Optional gradient [Brush] for the filled layer. Overrides [filled] when set.
 */
@Composable
public fun RatingBarDefaults.colors(
    filled: Color = MaterialTheme.colorScheme.primary,
    unfilled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    hover: Color = filled.copy(alpha = 0.6f),
    fillBrush: Brush? = null,
): RatingBarColors = RatingBarColors(
    filled = filled,
    unfilled = unfilled,
    hover = hover,
    fillBrush = fillBrush,
)
