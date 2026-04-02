package com.github.anandkumarkparmar.ratingbar

import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

/**
 * Built-in vector icons for [RatingBar] items.
 *
 * These icons are bundled directly in the library so you do not need to add a dependency on
 * `material-icons-extended`. Both icons are 24×24dp and match Material Design's Star iconography.
 *
 * You can replace them by passing a custom [androidx.compose.ui.graphics.painter.Painter] to the
 * `filledPainter` or `unfilledPainter` parameters of [RatingBar].
 */
public object RatingBarIcons {
    /**
     * Filled star icon (Material Icons: Star).
     *
     * Used as the default `filledPainter` in [RatingBar].
     */
    public val StarFilled: ImageVector = ImageVector.Builder(
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

    // ── Heart ──────────────────────────────────────────────────────────────

    /**
     * Filled heart icon (Material Icons: Favorite).
     *
     * Pair with [HeartOutline] as `unfilledPainter` for wishlist-style rating bars.
     */
    public val Heart: ImageVector = ImageVector.Builder(
        name = "Heart",
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
        moveTo(12f, 21.35f)
        lineTo(10.55f, 20.03f)
        curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f)
        curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f)
        curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f)
        curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f)
        curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f)
        curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.03f)
        lineTo(12f, 21.35f)
        close()
    }.build()

    /**
     * Outlined heart icon (Material Icons: FavoriteBorder).
     *
     * Use as `unfilledPainter` alongside [Heart] as `filledPainter`.
     */
    public val HeartOutline: ImageVector = ImageVector.Builder(
        name = "HeartOutline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f,
        pathFillType = PathFillType.EvenOdd
    ) {
        // Outer heart shape
        moveTo(16.5f, 3f)
        curveTo(14.76f, 3f, 13.09f, 3.81f, 12f, 5.09f)
        curveTo(10.91f, 3.81f, 9.24f, 3f, 7.5f, 3f)
        curveTo(4.42f, 3f, 2f, 5.42f, 2f, 8.5f)
        curveTo(2f, 12.28f, 5.4f, 15.36f, 10.55f, 20.03f)
        lineTo(12f, 21.35f)
        lineTo(13.45f, 20.03f)
        curveTo(18.6f, 15.36f, 22f, 12.28f, 22f, 8.5f)
        curveTo(22f, 5.42f, 19.58f, 3f, 16.5f, 3f)
        close()
        // Inner cutout — creates the outline ring via EvenOdd fill
        moveTo(12.1f, 18.55f)
        lineTo(12f, 18.65f)
        lineTo(11.9f, 18.55f)
        curveTo(7.14f, 14.24f, 4f, 11.39f, 4f, 8.5f)
        curveTo(4f, 6.5f, 5.5f, 5f, 7.5f, 5f)
        curveTo(9.04f, 5f, 10.54f, 5.99f, 11.07f, 7.36f)
        lineTo(12.94f, 7.36f)
        curveTo(13.46f, 5.99f, 14.96f, 5f, 16.5f, 5f)
        curveTo(18.5f, 5f, 20f, 6.5f, 20f, 8.5f)
        curveTo(20f, 11.39f, 16.86f, 14.24f, 12.1f, 18.55f)
        close()
    }.build()

    // ── ThumbUp ────────────────────────────────────────────────────────────

    /**
     * Filled thumb-up icon (Material Icons: ThumbUp).
     *
     * Pair with [ThumbUpOutline] as `unfilledPainter` for YouTube-style rating bars.
     */
    public val ThumbUp: ImageVector = ImageVector.Builder(
        name = "ThumbUp",
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
        // Left bar
        moveTo(1f, 21f)
        lineTo(5f, 21f)
        lineTo(5f, 9f)
        lineTo(1f, 9f)
        close()
        // Main thumb body
        moveTo(23f, 10f)
        curveTo(23f, 8.9f, 22.1f, 8f, 21f, 8f)
        lineTo(14.69f, 8f)
        lineTo(15.64f, 3.43f)
        lineTo(15.67f, 3.11f)
        curveTo(15.67f, 2.7f, 15.5f, 2.32f, 15.23f, 2.05f)
        lineTo(14.17f, 1f)
        lineTo(7.58f, 7.59f)
        curveTo(7.22f, 7.95f, 7f, 8.45f, 7f, 9f)
        lineTo(7f, 19f)
        curveTo(7f, 20.1f, 7.9f, 21f, 9f, 21f)
        lineTo(18f, 21f)
        curveTo(18.83f, 21f, 19.54f, 20.5f, 19.84f, 19.78f)
        lineTo(22.86f, 12.73f)
        curveTo(22.95f, 12.5f, 23f, 12.26f, 23f, 12f)
        lineTo(23f, 10f)
        close()
    }.build()

    /**
     * Outlined thumb-up icon (Material Icons: ThumbUpOffAlt / ThumbUpOutlined).
     *
     * Use as `unfilledPainter` alongside [ThumbUp] as `filledPainter`.
     */
    public val ThumbUpOutline: ImageVector = ImageVector.Builder(
        name = "ThumbUpOutline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f,
        pathFillType = PathFillType.EvenOdd
    ) {
        // Outer thumb shape
        moveTo(9f, 21f)
        lineTo(18f, 21f)
        curveTo(18.83f, 21f, 19.54f, 20.5f, 19.84f, 19.78f)
        lineTo(22.86f, 12.73f)
        curveTo(22.95f, 12.5f, 23f, 12.26f, 23f, 12f)
        lineTo(23f, 10f)
        curveTo(23f, 8.9f, 22.1f, 8f, 21f, 8f)
        lineTo(14.69f, 8f)
        lineTo(15.64f, 3.43f)
        lineTo(15.67f, 3.11f)
        curveTo(15.67f, 2.7f, 15.5f, 2.32f, 15.23f, 2.05f)
        lineTo(14.17f, 1f)
        lineTo(7.58f, 7.59f)
        curveTo(7.22f, 7.95f, 7f, 8.45f, 7f, 9f)
        lineTo(7f, 19f)
        curveTo(7f, 20.1f, 7.9f, 21f, 9f, 21f)
        close()
        // Inner cutout — thumb interior (EvenOdd creates the outline ring)
        moveTo(9f, 9f)
        lineTo(13.34f, 4.66f)
        lineTo(12.23f, 10f)
        lineTo(21f, 10f)
        lineTo(21f, 12f)
        lineTo(18f, 17f) // this vertex completes the inner thumb area
        lineTo(9f, 17f)
        lineTo(9f, 9f)
        close()
        // Left bar (outer)
        moveTo(5f, 5f)
        lineTo(1f, 5f)
        lineTo(1f, 17f)
        lineTo(5f, 17f)
        lineTo(5f, 5f)
        close()
        // Left bar (inner cutout)
        moveTo(3f, 7f)
        lineTo(3f, 15f)
        lineTo(3f, 15f)
        lineTo(3f, 7f)
        close()
    }.build()

    // ── Circle ─────────────────────────────────────────────────────────────

    /**
     * Filled circle icon (Material Icons: Circle / FiberManualRecord).
     *
     * Pair with [CircleOutline] as `unfilledPainter` for dot-style or compact rating bars.
     */
    public val Circle: ImageVector = ImageVector.Builder(
        name = "Circle",
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
        moveTo(12f, 2f)
        curveTo(6.47f, 2f, 2f, 6.47f, 2f, 12f)
        curveTo(2f, 17.52f, 6.47f, 22f, 12f, 22f)
        curveTo(17.53f, 22f, 22f, 17.52f, 22f, 12f)
        curveTo(22f, 6.47f, 17.53f, 2f, 12f, 2f)
        close()
    }.build()

    /**
     * Outlined circle icon (Material Icons: RadioButtonUnchecked).
     *
     * Use as `unfilledPainter` alongside [Circle] as `filledPainter`.
     */
    public val CircleOutline: ImageVector = ImageVector.Builder(
        name = "CircleOutline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f,
        pathFillType = PathFillType.EvenOdd
    ) {
        // Outer circle
        moveTo(12f, 2f)
        curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
        curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
        curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
        curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
        close()
        // Inner circle cutout — creates the ring shape via EvenOdd
        moveTo(12f, 20f)
        curveTo(7.58f, 20f, 4f, 16.42f, 4f, 12f)
        curveTo(4f, 7.58f, 7.58f, 4f, 12f, 4f)
        curveTo(16.42f, 4f, 20f, 7.58f, 20f, 12f)
        curveTo(20f, 16.42f, 16.42f, 20f, 12f, 20f)
        close()
    }.build()

    // ── Star (existing) ────────────────────────────────────────────────────

    /**
     * Outlined star icon (Material Icons: StarBorder).
     *
     * Used as the default `unfilledPainter` in [RatingBar].
     */
    public val StarOutline: ImageVector = ImageVector.Builder(
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
