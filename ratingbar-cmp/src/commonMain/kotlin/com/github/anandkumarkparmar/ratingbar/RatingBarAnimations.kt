package com.github.anandkumarkparmar.ratingbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Immutable

/**
 * Animation configuration for [RatingBar].
 *
 * Create an instance via [RatingBarDefaults.animations]:
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     animations = RatingBarDefaults.animations(enabled = true, animateScale = true)
 * )
 * ```
 *
 * @property enabled If `true`, fill fraction transitions animate using [spec] when the rating
 *   value changes. Set to `false` for instant (snap) transitions.
 * @property spec [AnimationSpec] used for fill transitions when [enabled] is `true`.
 *   Defaults to [RatingBarDefaults.RatingAnimationSpec] (200 ms ease-out tween).
 * @property animateScale If `true`, the newly selected star briefly scales up with a spring
 *   bounce when a value is committed via tap or drag. Applies to the default star overload only;
 *   ignored by the slot overload. No-op in `readOnly` mode.
 * @property reducedMotion If `true`, overrides [enabled] and [animateScale] — all animations
 *   snap instantly regardless of their individual settings. Pass the system's reduced-motion
 *   preference here to respect the user's accessibility setting.
 */
@Immutable
public data class RatingBarAnimations(
    val enabled: Boolean = false,
    val spec: AnimationSpec<Float> = RatingBarDefaults.RatingAnimationSpec,
    val animateScale: Boolean = false,
    val reducedMotion: Boolean = false,
)

/**
 * Creates a [RatingBarAnimations] with the given settings.
 *
 * @param enabled Whether fill transitions are animated.
 * @param spec Custom [AnimationSpec] for fill transitions. Defaults to [RatingBarDefaults.RatingAnimationSpec].
 * @param animateScale Whether to apply a spring-bounce scale to the selected star on commit.
 * @param reducedMotion When `true`, disables all animations regardless of [enabled]/[animateScale].
 */
public fun RatingBarDefaults.animations(
    enabled: Boolean = false,
    spec: AnimationSpec<Float> = RatingAnimationSpec,
    animateScale: Boolean = false,
    reducedMotion: Boolean = false,
): RatingBarAnimations = RatingBarAnimations(
    enabled = enabled,
    spec = spec,
    animateScale = animateScale,
    reducedMotion = reducedMotion,
)
