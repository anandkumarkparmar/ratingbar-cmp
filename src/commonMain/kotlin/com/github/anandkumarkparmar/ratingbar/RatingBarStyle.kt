package com.github.anandkumarkparmar.ratingbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp

/**
 * Visual style configuration for the default star [RatingBar] overload.
 *
 * Bundles sizing, painter, and colour settings into a single object, keeping the
 * `RatingBar` call-site concise. Create an instance via [RatingBarDefaults.style]:
 *
 * ```kotlin
 * RatingBar(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     style = RatingBarDefaults.style(
 *         itemSize = RatingBarDefaults.SizeLarge,
 *         colors = RatingBarDefaults.colors(filled = Color(0xFFFFB300))
 *     )
 * )
 * ```
 *
 * @property itemSize Size of each rating item.
 * @property itemSpacing Spacing between adjacent items.
 * @property filledPainter Painter for the filled (selected) state.
 * @property unfilledPainter Painter for the unfilled (empty) state.
 * @property colors Colour and fill configuration. See [RatingBarColors].
 */
@Stable
public class RatingBarStyle(
    public val itemSize: Dp,
    public val itemSpacing: Dp,
    public val filledPainter: Painter,
    public val unfilledPainter: Painter,
    public val colors: RatingBarColors,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RatingBarStyle) return false
        return itemSize == other.itemSize &&
            itemSpacing == other.itemSpacing &&
            filledPainter == other.filledPainter &&
            unfilledPainter == other.unfilledPainter &&
            colors == other.colors
    }

    override fun hashCode(): Int {
        var result = itemSize.hashCode()
        result = 31 * result + itemSpacing.hashCode()
        result = 31 * result + filledPainter.hashCode()
        result = 31 * result + unfilledPainter.hashCode()
        result = 31 * result + colors.hashCode()
        return result
    }
}

/**
 * Creates and remembers a [RatingBarStyle] with sensible defaults.
 *
 * The returned instance is `remember`ed — it is only recreated when one of the inputs
 * changes, preventing unnecessary recompositions.
 *
 * @param itemSize Size of each rating item. Defaults to [RatingBarDefaults.SizeMedium].
 * @param itemSpacing Spacing between adjacent items. Defaults to [RatingBarDefaults.ItemSpacing].
 * @param filledPainter Painter for filled items. Defaults to [RatingBarIcons.StarFilled].
 * @param unfilledPainter Painter for unfilled items. Defaults to [RatingBarIcons.StarOutline].
 * @param colors Colour and fill settings. Defaults to [RatingBarDefaults.colors].
 */
@Composable
public fun RatingBarDefaults.style(
    itemSize: Dp = SizeMedium,
    itemSpacing: Dp = ItemSpacing,
    filledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarFilled),
    unfilledPainter: Painter = rememberVectorPainter(RatingBarIcons.StarOutline),
    colors: RatingBarColors = colors(),
): RatingBarStyle = remember(itemSize, itemSpacing, filledPainter, unfilledPainter, colors) {
    RatingBarStyle(
        itemSize = itemSize,
        itemSpacing = itemSpacing,
        filledPainter = filledPainter,
        unfilledPainter = unfilledPainter,
        colors = colors,
    )
}
