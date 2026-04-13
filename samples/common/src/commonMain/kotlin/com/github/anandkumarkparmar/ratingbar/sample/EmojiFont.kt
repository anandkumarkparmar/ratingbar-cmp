package com.github.anandkumarkparmar.ratingbar.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

/**
 * Returns an emoji-capable [FontFamily].
 *
 * On Android, Desktop, and iOS the system already provides emoji fonts, so
 * [FontFamily.Default] works. On Web (Kotlin/JS with Skiko) there is no
 * system emoji font, so the actual implementation fetches one at runtime.
 */
@Composable
expect fun rememberEmojiFontFamily(): FontFamily
