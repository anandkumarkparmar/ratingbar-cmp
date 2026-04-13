package com.github.anandkumarkparmar.ratingbar.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font as SkikoFont
import kotlin.js.Promise
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array

/**
 * Google Fonts CSS URL requesting a subset of Noto Color Emoji containing only
 * the five emoji characters used in the sample. The subset keeps the download
 * small (~10-50 KB instead of the full ~24 MB font).
 */
private const val EMOJI_FONT_CSS_URL =
    "https://fonts.googleapis.com/css2?family=Noto+Color+Emoji" +
        "&text=%F0%9F%98%9E%F0%9F%98%95%F0%9F%98%90%F0%9F%99%82%F0%9F%98%84"

@Composable
actual fun rememberEmojiFontFamily(): FontFamily {
    var fontFamily: FontFamily by remember { mutableStateOf<FontFamily>(FontFamily.Default) }

    LaunchedEffect(Unit) {
        fontFamily = try {
            val bytes = fetchEmojiFontBytes()
            FontFamily(SkikoFont("NotoColorEmoji", bytes, FontWeight.Normal, FontStyle.Normal))
        } catch (_: Throwable) {
            FontFamily.Default
        }
    }

    return fontFamily
}

/**
 * Fetches the Noto Color Emoji subset font from Google Fonts.
 *
 * 1. Fetch the CSS stylesheet which contains the `@font-face` `src` URL.
 * 2. Parse the WOFF2 font file URL from the CSS.
 * 3. Fetch the font binary data and return it as a [ByteArray].
 */
private suspend fun fetchEmojiFontBytes(): ByteArray {
    val css = fetchText(EMOJI_FONT_CSS_URL)

    val fontUrl = Regex("""url\((https://[^)]+)\)""")
        .find(css)?.groupValues?.get(1)
        ?: error("Could not parse font URL from Google Fonts CSS response")

    return fetchBytes(fontUrl)
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UnsafeCastFromDynamic")
private suspend fun fetchText(url: String): String {
    val response = (window.asDynamic().fetch(url) as Promise<dynamic>).await()
    return (response.text() as Promise<String>).await()
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UnsafeCastFromDynamic")
private suspend fun fetchBytes(url: String): ByteArray {
    val response = (window.asDynamic().fetch(url) as Promise<dynamic>).await()
    val buffer: ArrayBuffer = (response.arrayBuffer() as Promise<ArrayBuffer>).await()
    // In Kotlin/JS, ByteArray and Int8Array are the same underlying JS type.
    return Int8Array(buffer).unsafeCast<ByteArray>()
}
