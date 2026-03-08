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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.github.anandkumarkparmar.ratingbar.RatingBar
import com.github.anandkumarkparmar.ratingbar.RatingBarDefaults
import com.github.anandkumarkparmar.ratingbar.rememberRatingBarState

private enum class SampleScreen {
    Standard,
    Playground,
    Behaviors
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp() {
    var currentScreen by remember { mutableStateOf(SampleScreen.Standard) }

    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = { Text("RatingBar Sample") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                PrimaryTabRow(selectedTabIndex = currentScreen.ordinal) {
                    Tab(
                        selected = currentScreen == SampleScreen.Standard,
                        onClick = { currentScreen = SampleScreen.Standard },
                        text = { Text("Standard") }
                    )
                    Tab(
                        selected = currentScreen == SampleScreen.Playground,
                        onClick = { currentScreen = SampleScreen.Playground },
                        text = { Text("Playground") }
                    )
                    Tab(
                        selected = currentScreen == SampleScreen.Behaviors,
                        onClick = { currentScreen = SampleScreen.Behaviors },
                        text = { Text("Behaviors") }
                    )
                }

                when (currentScreen) {
                    SampleScreen.Standard -> StandardScreen()
                    SampleScreen.Playground -> PlaygroundScreen()
                    SampleScreen.Behaviors -> BehaviorsScreen()
                }
            }
        }
    }
}

// ── Standard ─────────────────────────────────────────────────────────────────

@Composable
private fun StandardScreen() {
    var basicRating by rememberRatingBarState(3f)
    var halfRating by rememberRatingBarState(3.5f)
    var preciseRating by rememberRatingBarState(2.7f)
    val readOnlyRating = 4.5f
    var rtlRating by rememberRatingBarState(2f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Common Patterns",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Production-ready usage patterns for the most common rating scenarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SampleCard(title = "Basic") {
            RatingBar(
                value = basicRating,
                onValueChange = { basicRating = it }
            )
            ValueText(basicRating)
        }

        SampleCard(title = "Half Star") {
            RatingBar(
                value = halfRating,
                onValueChange = { halfRating = it },
                step = 0.5f,
                itemSize = RatingBarDefaults.SizeLarge
            )
            ValueText(halfRating)
        }

        SampleCard(title = "Precise (0.1 step, 10 items)") {
            RatingBar(
                value = preciseRating,
                onValueChange = { preciseRating = it },
                max = 10,
                step = 0.1f,
                itemSize = RatingBarDefaults.SizeSmall
            )
            ValueText(preciseRating)
        }

        SampleCard(title = "Read-Only") {
            RatingBar(
                value = readOnlyRating,
                onValueChange = {},
                readOnly = true
            )
            ValueText(readOnlyRating)
        }

        SampleCard(title = "RTL Layout") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                RatingBar(
                    value = rtlRating,
                    onValueChange = { rtlRating = it },
                    step = 0.5f
                )
            }
            ValueText(rtlRating)
        }

        SampleCard(title = "Custom Slot — Dots") {
            var dotRating by rememberRatingBarState(3f)
            RatingBar(
                value = dotRating,
                onValueChange = { dotRating = it },
                max = 5,
                step = 1f,
                itemSpacing = 8.dp,
                itemContent = { _, fillFraction ->
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(
                                color = if (fillFraction > 0f) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                },
                                shape = CircleShape
                            )
                    )
                }
            )
            ValueText(dotRating)
        }
    }
}

// ── Playground ───────────────────────────────────────────────────────────────

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

    val direction = if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Customization Playground",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Live preview
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RatingBar(
                        value = rating,
                        onValueChange = { rating = it },
                        max = max,
                        step = step,
                        itemSize = itemSize,
                        itemSpacing = itemSpacing,
                        readOnly = readOnly,
                        animateRating = animateRating,
                        animateScale = animateScale,
                        allowZero = allowZero,
                        showHoverPreview = showHoverPreview,
                        enableScrollInput = enableScrollInput,
                        hapticFeedback = hapticFeedback,
                        filledColor = MaterialTheme.colorScheme.primary,
                        unfilledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    )
                    ValueText(rating)
                }
            }

            // Sizing controls
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Max: $max")
                    Slider(
                        value = max.toFloat(),
                        onValueChange = { max = it.toInt().coerceIn(3, 10) },
                        valueRange = 3f..10f,
                        steps = 6
                    )

                    HorizontalDivider()

                    Text("Item size: ${itemSize.value.toInt()}dp")
                    Slider(
                        value = itemSize.value,
                        onValueChange = { itemSize = it.dp },
                        valueRange = 18f..64f
                    )

                    Text("Item spacing: ${itemSpacing.value.toInt()}dp")
                    Slider(
                        value = itemSpacing.value,
                        onValueChange = { itemSpacing = it.dp },
                        valueRange = 0f..16f
                    )
                }
            }

            // Toggle controls
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Step")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StepChip(text = "0.1", selected = step == 0.1f) { step = 0.1f }
                        StepChip(text = "0.5", selected = step == 0.5f) { step = 0.5f }
                        StepChip(text = "1.0", selected = step == 1f) { step = 1f }
                    }

                    HorizontalDivider()

                    LabeledSwitch("Read-only", readOnly) { readOnly = it }
                    LabeledSwitch("RTL", rtl) { rtl = it }
                    LabeledSwitch("Allow zero", allowZero) { allowZero = it }

                    HorizontalDivider()

                    LabeledSwitch("Fill animation", animateRating) { animateRating = it }
                    LabeledSwitch("Scale on select", animateScale) { animateScale = it }
                    LabeledSwitch("Hover preview  (Desktop / Web)", showHoverPreview) {
                        showHoverPreview = it
                    }
                    LabeledSwitch("Mouse wheel scroll  (Desktop)", enableScrollInput) {
                        enableScrollInput = it
                    }
                    LabeledSwitch("Haptic feedback  (Android)", hapticFeedback) {
                        hapticFeedback = it
                    }
                }
            }
        }
    }
}

// ── Behaviors ────────────────────────────────────────────────────────────────

@Composable
private fun BehaviorsScreen() {
    // State hoisted to function level — predictable lifecycle, no composition-position ambiguity
    var fillAnimRating by rememberRatingBarState(3f)
    var fillAnimEnabled by remember { mutableStateOf(false) }

    var scaleRating by rememberRatingBarState(3f)
    var scaleEnabled by remember { mutableStateOf(false) }

    var minRating by rememberRatingBarState(1f)
    var allowZero by remember { mutableStateOf(false) }

    var hoverRating by rememberRatingBarState(3f)
    var hoverEnabled by remember { mutableStateOf(false) }

    var scrollRating by rememberRatingBarState(3f)
    var scrollEnabled by remember { mutableStateOf(false) }

    var hapticRating by rememberRatingBarState(3f)
    var hapticEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Behavior Controls",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Toggle each behavior on and off to see how it affects the rating bar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SampleCard(title = "Fill Animation") {
            RatingBar(
                value = fillAnimRating,
                onValueChange = { fillAnimRating = it },
                animateRating = fillAnimEnabled
            )
            ValueText(fillAnimRating)
            LabeledSwitch("Animate fill", fillAnimEnabled) { fillAnimEnabled = it }
            NoteText("When on, the fill transitions smoothly. When off, it jumps instantly.")
        }

        SampleCard(title = "Scale on Select") {
            RatingBar(
                value = scaleRating,
                onValueChange = { scaleRating = it },
                animateScale = scaleEnabled
            )
            ValueText(scaleRating)
            LabeledSwitch("Scale on select", scaleEnabled) { scaleEnabled = it }
            NoteText("Toggle on, then tap a star — the selected item briefly bounces.")
        }

        SampleCard(title = "Minimum Rating") {
            RatingBar(
                value = minRating,
                onValueChange = { minRating = it },
                allowZero = allowZero,
                minValue = if (allowZero) 0f else 1f
            )
            ValueText(minRating)
            LabeledSwitch("Allow zero", allowZero) { allowZero = it }
            NoteText(
                if (allowZero) "Tapping the active star clears the rating to 0."
                else "Rating cannot go below 1 — the minimum is enforced on tap and drag."
            )
        }

        SampleCard(title = "Hover Preview  (Desktop / Web)") {
            RatingBar(
                value = hoverRating,
                onValueChange = { hoverRating = it },
                showHoverPreview = hoverEnabled,
                hoverColor = MaterialTheme.colorScheme.tertiary
            )
            ValueText(hoverRating)
            LabeledSwitch("Show hover preview", hoverEnabled) { hoverEnabled = it }
            NoteText(
                "Move the cursor over the stars to preview a value before clicking. " +
                    "Only unfilled stars are tinted — your current rating stays visible. No-op on touch platforms."
            )
        }

        SampleCard(title = "Mouse Wheel Scroll  (Desktop)") {
            RatingBar(
                value = scrollRating,
                onValueChange = { scrollRating = it },
                step = 0.5f,
                enableScrollInput = scrollEnabled
            )
            ValueText(scrollRating)
            LabeledSwitch("Enable scroll input", scrollEnabled) { scrollEnabled = it }
            NoteText(
                "Scroll up/down over the bar to nudge the rating by one step (0.5 here). " +
                    "No-op on Android, iOS, and Web."
            )
        }

        SampleCard(title = "Haptic Feedback  (Android)") {
            RatingBar(
                value = hapticRating,
                onValueChange = { hapticRating = it },
                hapticFeedback = hapticEnabled
            )
            ValueText(hapticRating)
            LabeledSwitch("Haptic feedback", hapticEnabled) { hapticEnabled = it }
            NoteText(
                "Tap a different star to change the rating — a haptic tick fires on Android " +
                    "each time the value changes. Requires 'Vibration & haptics' to be enabled " +
                    "in device Settings. No-op on other platforms."
            )
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
private fun SampleCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            content()
        }
    }
}

@Composable
private fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun StepChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(onClick = onClick) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun ValueText(value: Float) {
    val oneDecimal = (value * 10f).toInt() / 10f
    Text(
        text = "Value: $oneDecimal",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun NoteText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
