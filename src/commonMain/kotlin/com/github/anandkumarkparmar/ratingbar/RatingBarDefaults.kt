package com.github.anandkumarkparmar.ratingbar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values for [RatingBar] components.
 *
 * Use these constants when configuring a [RatingBar] to ensure consistent sizing and spacing
 * across your UI, or override them with custom values.
 *
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     itemSize = RatingBarDefaults.SizeLarge,
 *     itemSpacing = RatingBarDefaults.ItemSpacing,
 * )
 * ```
 */
public object RatingBarDefaults {
    /**
     * Small item size preset: 16.dp.
     */
    public val SizeSmall: Dp = 16.dp

    /**
     * Medium item size preset: 32.dp. This is the default [RatingBar] item size.
     */
    public val SizeMedium: Dp = 32.dp

    /**
     * Large item size preset: 48.dp.
     */
    public val SizeLarge: Dp = 48.dp

    /**
     * Default spacing between adjacent rating items: 4.dp.
     */
    public val ItemSpacing: Dp = 4.dp

    /**
     * Default animation spec for fill fraction transitions: 200ms ease-out tween.
     *
     * Used as the default [RatingBarAnimations.spec]. Pass a custom [AnimationSpec] via
     * [animations] to override.
     */
    public val RatingAnimationSpec: TweenSpec<Float> = tween(durationMillis = 200, easing = FastOutSlowInEasing)

    /**
     * Default shimmer animation duration for [RatingBarPlaceholder]: 1000ms.
     */
    public const val ShimmerDurationMillis: Int = 1000
}
