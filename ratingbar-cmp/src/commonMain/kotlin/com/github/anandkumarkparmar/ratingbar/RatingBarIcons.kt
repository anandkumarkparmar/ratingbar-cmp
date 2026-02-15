package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

/**
 * Default vector icons for RatingBar to avoid dependency on unlimited material-icons-extended.
 */
object RatingBarIcons {
    // Copied from Material Icons (Star) to keep bundle small
    val StarFilled: ImageVector = ImageVector.Builder(
        name = "StarFilled",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f
    ) {
        moveTo(12f, 17.27f)
        lineTo(18.18f, 21f)
        lineTo(16.54f, 13.97f)
        lineTo(22f, 9.24f)
        lineTo(14.81f, 8.62f)
        lineTo(12f, 2f)
        lineTo(9.19f, 8.62f)
        lineTo(2f, 9.24f)
        lineTo(7.45f, 13.97f)
        lineTo(5.82f, 21f)
        close()
    }.build()

    val StarOutline: ImageVector = ImageVector.Builder(
        name = "StarOutline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f
    ) {
        moveTo(22f, 9.24f)
        lineTo(14.81f, 8.62f)
        lineTo(12f, 2f)
        lineTo(9.19f, 8.62f)
        lineTo(2f, 9.24f)
        lineTo(7.45f, 13.97f)
        lineTo(5.82f, 21f)
        lineTo(12f, 17.27f)
        lineTo(18.18f, 21f)
        lineTo(16.54f, 13.97f)
        lineTo(22f, 9.24f)
        close()
        moveTo(12f, 15.4f)
        lineTo(8.24f, 17.67f)
        lineTo(9.24f, 13.39f)
        lineTo(5.92f, 10.51f)
        lineTo(10.3f, 10.13f)
        lineTo(12f, 6.1f)
        lineTo(13.71f, 10.14f)
        lineTo(18.09f, 10.52f)
        lineTo(14.77f, 13.4f)
        lineTo(15.77f, 17.68f)
        lineTo(12f, 15.4f)
        close()
    }.build()
}
