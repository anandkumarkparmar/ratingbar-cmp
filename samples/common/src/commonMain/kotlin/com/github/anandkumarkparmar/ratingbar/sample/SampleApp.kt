package com.github.anandkumarkparmar.ratingbar.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.github.anandkumarkparmar.ratingbar.*
import com.github.anandkumarkparmar.ratingbar.core.RatingBarConfig
import com.github.anandkumarkparmar.ratingbar.core.RatingInteractionSource

private enum class SampleTab { Standard, Behaviour, Playground }
private enum class Platform { All, Desktop, Android }
private enum class IconSet { Star, Heart, Thumb, Circle }

// ── Theme ─────────────────────────────────────────────────────────────────────

@Composable
private fun sampleColorScheme() = lightColorScheme(
    primary              = Color(0xFFE8A100),
    onPrimary            = Color(0xFF4A2F00),
    primaryContainer     = Color(0xFFFFDEA0),
    onPrimaryContainer   = Color(0xFF2C1B00),
    secondary            = Color(0xFFA06800),
    onSecondary          = Color(0xFFFFFFFF),
    secondaryContainer   = Color(0xFFFFDEA0),
    onSecondaryContainer = Color(0xFF321F00),
    surface              = Color(0xFFFFFBF0),
    onSurface            = Color(0xFF1E1B10),
    surfaceVariant       = Color(0xFFF0E0C0),
    onSurfaceVariant     = Color(0xFF5A4A28),
    background           = Color(0xFFFFFBF0),
)

// ── Entry point ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp() {
    var currentTab by remember { mutableStateOf(SampleTab.Standard) }

    MaterialTheme(colorScheme = sampleColorScheme()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = { Text("RatingBar", fontWeight = FontWeight.SemiBold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                PrimaryTabRow(selectedTabIndex = currentTab.ordinal) {
                    Tab(
                        selected = currentTab == SampleTab.Standard,
                        onClick = { currentTab = SampleTab.Standard },
                        text = { Text("Standard") },
                    )
                    Tab(
                        selected = currentTab == SampleTab.Behaviour,
                        onClick = { currentTab = SampleTab.Behaviour },
                        text = { Text("Behaviour") },
                    )
                    Tab(
                        selected = currentTab == SampleTab.Playground,
                        onClick = { currentTab = SampleTab.Playground },
                        text = { Text("Playground") },
                    )
                }
                when (currentTab) {
                    SampleTab.Standard   -> StandardScreen()
                    SampleTab.Behaviour  -> BehaviourScreen()
                    SampleTab.Playground -> PlaygroundScreen()
                }
            }
        }
    }
}

// ── Tab 1: Standard ───────────────────────────────────────────────────────────

@Composable
private fun StandardScreen() {
    var basicRating by rememberRatingBarState(3f)
    var halfRating by rememberRatingBarState(3.5f)
    val readOnlyRating = 3.6f
    var heartRating by rememberRatingBarState(4f)
    var circleRating by rememberRatingBarState(3f)
    var slotRating by rememberRatingBarState(4f)
    var isLoading by remember { mutableStateOf(true) }
    var placeholderRating by rememberRatingBarState(3f)
    var rtlRating by rememberRatingBarState(2f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Standard Patterns",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Production-ready usage patterns for the most common rating scenarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        HeroCard()

        // ── Section 1: Real-World Showcase ────────────────────────────────────
        SectionHeader("Real-World Showcase", Color(0xFFE8A100))

        SampleCard("Read-Only") {
            RatingBar(
                value = readOnlyRating,
                onValueChange = {},
                config = RatingBarConfig(step = 0.1f),
                readOnly = true,
            )
            RatingValueChip(readOnlyRating)
            NoteText("Suitable for displaying aggregate scores in product listings.")
        }

        SampleCard("Label Slots") {
            RatingBar(
                value = slotRating,
                onValueChange = { slotRating = it },
                leadingContent = {
                    Text(
                        "Rate:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                },
                trailingContent = {
                    Text(
                        " ${(slotRating * 10).toInt() / 10f} / 5",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                },
            )
            NoteText("leadingContent and trailingContent slots wrap the bar in a Row.")
        }

        SampleCard("Custom Shape — Hearts") {
            RatingBar(
                value = heartRating,
                onValueChange = { heartRating = it },
                style = RatingBarDefaults.style(
                    filledPainter = rememberVectorPainter(RatingBarIcons.Heart),
                    unfilledPainter = rememberVectorPainter(RatingBarIcons.HeartOutline),
                    colors = RatingBarDefaults.colors(
                        filled = Color(0xFFE91E63),
                        unfilled = Color(0xFFE91E63).copy(alpha = 0.3f),
                        hover = Color(0xFFE91E63).copy(alpha = 0.6f),
                    ),
                ),
            )
            RatingValueChip(heartRating)
        }

        SampleCard("Loading Skeleton") {
            LabeledSwitch("Show loading skeleton", isLoading) { isLoading = it }
            if (isLoading) {
                RatingBarPlaceholder(
                    max = 5,
                    itemSize = RatingBarDefaults.SizeMedium,
                    itemPainter = rememberVectorPainter(RatingBarIcons.StarFilled),
                )
            } else {
                RatingBar(value = placeholderRating, onValueChange = { placeholderRating = it })
                RatingValueChip(placeholderRating)
            }
            NoteText("Toggle to switch between shimmer skeleton and the live rating bar.")
        }

        // ── Section 2: Basic Usage ────────────────────────────────────────────
        SectionHeader("Basic Usage", Color(0xFF1976D2))

        SampleCard("Basic") {
            RatingBar(value = basicRating, onValueChange = { basicRating = it })
            RatingValueChip(basicRating)
        }

        SampleCard("Half Stars") {
            RatingBar(
                value = halfRating,
                onValueChange = { halfRating = it },
                config = RatingBarConfig(step = 0.5f),
                style = RatingBarDefaults.style(itemSize = RatingBarDefaults.SizeLarge),
            )
            RatingValueChip(halfRating)
        }

        SampleCard("Custom Shape — Circles") {
            RatingBar(
                value = circleRating,
                onValueChange = { circleRating = it },
                style = RatingBarDefaults.style(
                    filledPainter = rememberVectorPainter(RatingBarIcons.Circle),
                    unfilledPainter = rememberVectorPainter(RatingBarIcons.CircleOutline),
                    itemSize = RatingBarDefaults.SizeSmall,
                ),
            )
            RatingValueChip(circleRating)
        }

        SampleCard("RTL Layout") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                RatingBar(
                    value = rtlRating,
                    onValueChange = { rtlRating = it },
                    config = RatingBarConfig(step = 0.5f),
                )
            }
            RatingValueChip(rtlRating)
        }

        SampleCard("Custom Slot — Emoji") {
            var emojiRating by rememberRatingBarState(3f)
            val emojis = listOf("😞", "😕", "😐", "🙂", "😄")
            RatingBar(
                value = emojiRating,
                onValueChange = { emojiRating = it },
                config = RatingBarConfig(max = 5, step = 1f),
                itemSpacing = 8.dp,
                itemContent = { index, fillFraction ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (fillFraction > 0f) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = emojis[index],
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
            )
            RatingValueChip(emojiRating)
            NoteText(
                "The slot overload accepts any Composable as a rating item — text, emoji, images, " +
                "or custom drawings. Unlike the style overload (which only accepts Painters), " +
                "slots give you full rendering control."
            )
        }
    }
}

@Composable
private fun HeroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Stellar Odyssey",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "2024 · Sci-Fi · Drama",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "4.3",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
            RatingBar(
                value = 4.3f,
                onValueChange = {},
                readOnly = true,
                config = RatingBarConfig(step = 0.1f),
                style = RatingBarDefaults.style(
                    itemSize = RatingBarDefaults.SizeLarge,
                    colors = RatingBarDefaults.colors(
                        fillBrush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B35),
                                Color(0xFFFFD700),
                                Color(0xFFE8A100),
                            ),
                        ),
                        unfilled = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                    ),
                ),
            )
            Text(
                text = "Based on 12,847 ratings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
            )
        }
    }
}

// ── Tab 2: Behaviour ──────────────────────────────────────────────────────────

@Composable
private fun BehaviourScreen() {
    // Animation
    var fillAnimRating by rememberRatingBarState(3f)
    var fillAnimEnabled by remember { mutableStateOf(false) }
    var scaleRating by rememberRatingBarState(3f)
    var scaleEnabled by remember { mutableStateOf(false) }
    var reducedMotion by remember { mutableStateOf(false) }
    var rmRating by rememberRatingBarState(2f)

    // Interaction
    var minRating by rememberRatingBarState(1f)
    var allowZero by remember { mutableStateOf(false) }
    var hoverRating by rememberRatingBarState(3f)
    var hoverEnabled by remember { mutableStateOf(false) }
    var scrollRating by rememberRatingBarState(3f)
    var scrollEnabled by remember { mutableStateOf(false) }
    var hapticRating by rememberRatingBarState(3f)
    var hapticEnabled by remember { mutableStateOf(false) }
    var longPressRating by rememberRatingBarState(4f)
    var sourceRating by rememberRatingBarState(3f)
    var lastSource by remember { mutableStateOf<RatingInteractionSource?>(null) }

    // Accessibility
    var labelRating by rememberSaveableRatingBarState(3f)
    var saveableRating by rememberSaveableRatingBarState(2f)

    // Visual
    var gradientRating by rememberRatingBarState(3f)
    var isLoading by remember { mutableStateOf(true) }
    var placeholderRating by rememberRatingBarState(3f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Behaviour",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Toggle each feature to see it in action.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // ── Animation ──────────────────────────────────────────────────────
        SectionHeader("Animation", Color(0xFFE8A100))

        BehaviourCard("Fill Animation") {
            RatingBar(
                value = fillAnimRating,
                onValueChange = { fillAnimRating = it },
                animations = RatingBarDefaults.animations(enabled = fillAnimEnabled),
            )
            RatingValueChip(fillAnimRating)
            LabeledSwitch("Animate fill", fillAnimEnabled) { fillAnimEnabled = it }
            NoteText("When enabled, the fill transitions smoothly. When off, it snaps instantly.")
        }

        BehaviourCard("Scale on Select") {
            RatingBar(
                value = scaleRating,
                onValueChange = { scaleRating = it },
                animations = RatingBarDefaults.animations(animateScale = scaleEnabled),
            )
            RatingValueChip(scaleRating)
            LabeledSwitch("Scale on select", scaleEnabled) { scaleEnabled = it }
            NoteText("Toggle on, then tap a star — the selected item briefly bounces.")
        }

        BehaviourCard("Reduced Motion") {
            RatingBar(
                value = rmRating,
                onValueChange = { rmRating = it },
                animations = RatingBarDefaults.animations(
                    enabled = true,
                    animateScale = true,
                    reducedMotion = reducedMotion,
                ),
            )
            RatingValueChip(rmRating)
            LabeledSwitch("Reduced motion", reducedMotion) { reducedMotion = it }
            NoteText(
                "When enabled, fill animations snap instantly and scale is suppressed. " +
                "Read the OS accessibility preference and pass it here."
            )
        }

        // ── Interaction ────────────────────────────────────────────────────
        SectionHeader("Interaction", Color(0xFF1976D2))

        BehaviourCard("Minimum Rating") {
            RatingBar(
                value = minRating,
                onValueChange = { minRating = it },
                config = RatingBarConfig(
                    allowZero = allowZero,
                    minValue = if (allowZero) 0f else 1f,
                ),
            )
            RatingValueChip(minRating)
            LabeledSwitch("Allow zero", allowZero) { allowZero = it }
            NoteText(
                if (allowZero) "Tapping the active star clears the rating to 0."
                else "Rating cannot drop below 1 — enforced on tap and drag."
            )
        }

        BehaviourCard("Hover Preview", Platform.Desktop) {
            RatingBar(
                value = hoverRating,
                onValueChange = { hoverRating = it },
                config = RatingBarConfig(step = 0.1f),
                style = RatingBarDefaults.style(
                    colors = RatingBarDefaults.colors(
                        hover = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    ),
                ),
                behavior = RatingBarDefaults.behavior(showHoverPreview = hoverEnabled),
            )
            RatingValueChip(hoverRating)
            LabeledSwitch("Show hover preview", hoverEnabled) { hoverEnabled = it }
            NoteText(
                "Move the cursor over the stars to preview a value before clicking. " +
                "Only unfilled stars are tinted — your current rating stays visible."
            )
        }

        BehaviourCard("Mouse Wheel Scroll", Platform.Desktop) {
            RatingBar(
                value = scrollRating,
                onValueChange = { scrollRating = it },
                config = RatingBarConfig(step = 0.5f),
                behavior = RatingBarDefaults.behavior(enableScrollInput = scrollEnabled),
            )
            RatingValueChip(scrollRating)
            LabeledSwitch("Enable scroll input", scrollEnabled) { scrollEnabled = it }
            NoteText("Scroll up or down over the bar to nudge the rating by one step.")
        }

        BehaviourCard("Haptic Feedback", Platform.Android) {
            RatingBar(
                value = hapticRating,
                onValueChange = { hapticRating = it },
                behavior = RatingBarDefaults.behavior(hapticFeedback = hapticEnabled),
            )
            RatingValueChip(hapticRating)
            LabeledSwitch("Haptic feedback", hapticEnabled) { hapticEnabled = it }
            NoteText(
                "A haptic pulse fires each time the value changes. " +
                "Requires vibration to be enabled in device settings."
            )
        }

        BehaviourCard("Long-Press to Reset") {
            RatingBar(
                value = longPressRating,
                onValueChange = { longPressRating = it },
                behavior = RatingBarDefaults.behavior(enableLongPressReset = true),
            )
            RatingValueChip(longPressRating)
            NoteText("Long-press anywhere on the bar to reset the rating to its minimum value.")
        }

        BehaviourCard("Interaction Callbacks") {
            RatingBar(
                value = sourceRating,
                onValueChange = { sourceRating = it },
                config = RatingBarConfig(step = 0.5f),
                behavior = RatingBarDefaults.behavior(
                    showHoverPreview = true,
                    enableScrollInput = true,
                ),
                onInteraction = { source -> lastSource = source },
            )
            RatingValueChip(sourceRating)
            Text(
                text = "Last input: ${lastSource?.name ?: "—"}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            NoteText("Tap, drag, use keyboard arrows, or scroll. Each interaction fires a callback with a source enum.")
        }

        // ── Accessibility ──────────────────────────────────────────────────
        SectionHeader("Accessibility", Color(0xFF388E3C))

        BehaviourCard("Per-Item Labels") {
            RatingBar(
                value = labelRating,
                onValueChange = { labelRating = it },
                itemLabels = listOf("Terrible", "Bad", "Okay", "Good", "Excellent"),
            )
            RatingValueChip(labelRating)
            NoteText(
                "TalkBack / VoiceOver: swipe up or down to change the value. " +
                "The state description includes the active label, e.g. \"Good (4.0 out of 5)\"."
            )
        }

        BehaviourCard("Saveable State") {
            RatingBar(
                value = saveableRating,
                onValueChange = { saveableRating = it },
            )
            RatingValueChip(saveableRating)
            NoteText(
                "Value survives Android configuration changes (rotation) and " +
                "Compose Navigation back-stack restoration."
            )
        }

        // ── Visual ─────────────────────────────────────────────────────────
        SectionHeader("Visual", Color(0xFF7B1FA2))

        BehaviourCard("Gradient Fill") {
            RatingBar(
                value = gradientRating,
                onValueChange = { gradientRating = it },
                style = RatingBarDefaults.style(
                    itemSize = RatingBarDefaults.SizeLarge,
                    colors = RatingBarDefaults.colors(
                        fillBrush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFF44336),
                                Color(0xFFFFEB3B),
                                Color(0xFF4CAF50),
                            ),
                        ),
                        unfilled = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    ),
                ),
            )
            RatingValueChip(gradientRating)
            NoteText("A linear gradient clipped to the exact star silhouette, including fractional fills.")
        }

        BehaviourCard("Loading Skeleton") {
            LabeledSwitch("Show loading skeleton", isLoading) { isLoading = it }
            if (isLoading) {
                RatingBarPlaceholder(
                    max = 5,
                    itemSize = RatingBarDefaults.SizeMedium,
                    itemPainter = rememberVectorPainter(RatingBarIcons.StarFilled),
                )
            } else {
                RatingBar(value = placeholderRating, onValueChange = { placeholderRating = it })
                RatingValueChip(placeholderRating)
            }
            NoteText("Show a shimmer placeholder while content loads, then swap to the live bar.")
        }
    }
}

// ── Tab 3: Playground ─────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlaygroundScreen() {
    var rating by rememberRatingBarState(3.5f)
    var max by remember { mutableStateOf(5) }
    var step by remember { mutableStateOf(0.5f) }
    var itemSize by remember { mutableStateOf(RatingBarDefaults.SizeMedium) }
    var itemSpacing by remember { mutableStateOf(RatingBarDefaults.ItemSpacing) }
    var readOnly by remember { mutableStateOf(false) }
    var rtl by remember { mutableStateOf(false) }
    var animateRating by remember { mutableStateOf(false) }
    var animateScale by remember { mutableStateOf(false) }
    var allowZero by remember { mutableStateOf(true) }
    var showHoverPreview by remember { mutableStateOf(false) }
    var enableScrollInput by remember { mutableStateOf(false) }
    var hapticFeedback by remember { mutableStateOf(false) }
    var useGradient by remember { mutableStateOf(false) }
    var selectedIconSet by remember { mutableStateOf(IconSet.Star) }

    val direction = if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    // Pre-compute all painters unconditionally (Compose rules: no remember in when branches)
    val starFilledPainter    = rememberVectorPainter(RatingBarIcons.StarFilled)
    val starOutlinePainter   = rememberVectorPainter(RatingBarIcons.StarOutline)
    val heartPainter         = rememberVectorPainter(RatingBarIcons.Heart)
    val heartOutlinePainter  = rememberVectorPainter(RatingBarIcons.HeartOutline)
    val thumbPainter         = rememberVectorPainter(RatingBarIcons.ThumbUp)
    val thumbOutlinePainter  = rememberVectorPainter(RatingBarIcons.ThumbUpOutline)
    val circlePainter        = rememberVectorPainter(RatingBarIcons.Circle)
    val circleOutlinePainter = rememberVectorPainter(RatingBarIcons.CircleOutline)

    val filledPainter = when (selectedIconSet) {
        IconSet.Star   -> starFilledPainter
        IconSet.Heart  -> heartPainter
        IconSet.Thumb  -> thumbPainter
        IconSet.Circle -> circlePainter
    }
    val unfilledPainter = when (selectedIconSet) {
        IconSet.Star   -> starOutlinePainter
        IconSet.Heart  -> heartOutlinePainter
        IconSet.Thumb  -> thumbOutlinePainter
        IconSet.Circle -> circleOutlinePainter
    }

    val resolvedStyle = RatingBarDefaults.style(
        itemSize = itemSize,
        itemSpacing = itemSpacing,
        filledPainter = filledPainter,
        unfilledPainter = unfilledPainter,
        colors = if (useGradient) {
            RatingBarDefaults.colors(
                fillBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF44336), Color(0xFFFFEB3B), Color(0xFF4CAF50)),
                ),
                unfilled = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            )
        } else {
            RatingBarDefaults.colors(
                filled = MaterialTheme.colorScheme.primary,
                unfilled = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            )
        },
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // Zone 1 — sticky live preview (outside scroll)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "Live Preview",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                // RTL wraps only the RatingBar — not the surrounding controls
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    RatingBar(
                        value = rating,
                        onValueChange = { rating = it },
                        readOnly = readOnly,
                        config = RatingBarConfig(max = max, step = step, allowZero = allowZero),
                        style = resolvedStyle,
                        animations = RatingBarDefaults.animations(
                            enabled = animateRating,
                            animateScale = animateScale,
                        ),
                        behavior = RatingBarDefaults.behavior(
                            showHoverPreview = showHoverPreview,
                            enableScrollInput = enableScrollInput,
                            hapticFeedback = hapticFeedback,
                        ),
                    )
                }
                RatingValueChip(rating, max = max)
            }
        }

        // Zone 2 — scrollable controls
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Sizing
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "Sizing",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Text("Max: $max", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = max.toFloat(),
                        onValueChange = { max = it.toInt().coerceIn(3, 10) },
                        valueRange = 3f..10f,
                        steps = 6,
                    )
                    Text("Item size: ${itemSize.value.toInt()} dp", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = itemSize.value,
                        onValueChange = { itemSize = it.dp },
                        valueRange = 18f..64f,
                    )
                    Text("Item spacing: ${itemSpacing.value.toInt()} dp", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = itemSpacing.value,
                        onValueChange = { itemSpacing = it.dp },
                        valueRange = 0f..16f,
                    )
                }
            }

            // Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        "Step",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StepChip("0.1", step == 0.1f) { step = 0.1f }
                        StepChip("0.5", step == 0.5f) { step = 0.5f }
                        StepChip("1.0", step == 1f)   { step = 1f }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text(
                        "Shape",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StepChip("Stars",   selectedIconSet == IconSet.Star)   { selectedIconSet = IconSet.Star }
                        StepChip("Hearts",  selectedIconSet == IconSet.Heart)  { selectedIconSet = IconSet.Heart }
                        StepChip("Thumbs",  selectedIconSet == IconSet.Thumb)  { selectedIconSet = IconSet.Thumb }
                        StepChip("Circles", selectedIconSet == IconSet.Circle) { selectedIconSet = IconSet.Circle }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LabeledSwitch("Read-only", readOnly) { readOnly = it }
                    LabeledSwitch("RTL layout", rtl) { rtl = it }
                    LabeledSwitch("Allow zero", allowZero) { allowZero = it }
                    LabeledSwitch("Gradient fill", useGradient) { useGradient = it }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    LabeledSwitch("Fill animation", animateRating) { animateRating = it }
                    LabeledSwitch("Scale on select", animateScale) { animateScale = it }
                    LabeledSwitch("Hover preview  (Desktop)", showHoverPreview) { showHoverPreview = it }
                    LabeledSwitch("Mouse wheel scroll  (Desktop)", enableScrollInput) { enableScrollInput = it }
                    LabeledSwitch("Haptic feedback  (Android)", hapticFeedback) { hapticFeedback = it }
                }
            }
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
private fun SampleCard(
    title: String,
    badge: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                badge?.invoke()
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = content,
            )
        }
    }
}

@Composable
private fun BehaviourCard(
    title: String,
    platform: Platform = Platform.All,
    content: @Composable ColumnScope.() -> Unit,
) {
    SampleCard(title = title, badge = { PlatformBadge(platform) }, content = content)
}

@Composable
private fun SectionHeader(title: String, accentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(accentColor, shape = RoundedCornerShape(2.dp)),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = accentColor,
        )
    }
}

@Composable
private fun PlatformBadge(platform: Platform) {
    if (platform == Platform.All) return
    val label = if (platform == Platform.Desktop) "Desktop" else "Android"
    val bg    = if (platform == Platform.Desktop) Color(0xFFE3F2FD) else Color(0xFFE8F5E9)
    val fg    = if (platform == Platform.Desktop) Color(0xFF0D47A1) else Color(0xFF1B5E20)
    Box(
        modifier = Modifier
            .background(bg, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = fg,
        )
    }
}

@Composable
private fun RatingValueChip(value: Float, max: Int = 5) {
    val display = (value * 10f).toInt() / 10f
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(50),
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = "$display / $max",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun StepChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
                             else MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NoteText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
