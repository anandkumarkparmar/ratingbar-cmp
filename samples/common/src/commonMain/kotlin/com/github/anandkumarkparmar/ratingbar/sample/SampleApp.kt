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

private enum class SampleScreen {
    Standard,
    Customization
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
                        selected = currentScreen == SampleScreen.Customization,
                        onClick = { currentScreen = SampleScreen.Customization },
                        text = { Text("Customization") }
                    )
                }

                when (currentScreen) {
                    SampleScreen.Standard -> StandardScreen()
                    SampleScreen.Customization -> CustomizationScreen()
                }
            }
        }
    }
}

@Composable
private fun StandardScreen() {
    var basicRating by remember { mutableStateOf(3f) }
    var halfRating by remember { mutableStateOf(3.5f) }
    var preciseRating by remember { mutableStateOf(2.7f) }
    val readOnlyRating = 4.5f
    var rtlRating by remember { mutableStateOf(2f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Predefined Usage",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "These are production-ready patterns for common rating scenarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        StandardCard(title = "1) Basic Interactive") {
            RatingBar(
                value = basicRating,
                onValueChange = { basicRating = it }
            )
            ValueText(basicRating)
        }

        StandardCard(title = "2) Half Step (0.5)") {
            RatingBar(
                value = halfRating,
                onValueChange = { halfRating = it },
                step = 0.5f,
                itemSize = RatingBarDefaults.SizeLarge
            )
            ValueText(halfRating)
        }

        StandardCard(title = "3) Precise Step (0.1)") {
            RatingBar(
                value = preciseRating,
                onValueChange = { preciseRating = it },
                max = 10,
                step = 0.1f,
                itemSize = RatingBarDefaults.SizeSmall
            )
            ValueText(preciseRating)
        }

        StandardCard(title = "4) Read-Only Display") {
            RatingBar(
                value = readOnlyRating,
                onValueChange = {},
                readOnly = true
            )
            ValueText(readOnlyRating)
        }

        StandardCard(title = "5) RTL") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                RatingBar(
                    value = rtlRating,
                    onValueChange = { rtlRating = it },
                    step = 0.5f
                )
            }
            ValueText(rtlRating)
        }

        StandardCard(title = "6) Custom Slot (Dots)") {
            var customRating by remember { mutableStateOf(3f) }
            RatingBar(
                value = customRating,
                onValueChange = { customRating = it },
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
            ValueText(customRating)
        }
    }
}

@Composable
private fun StandardCard(
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CustomizationScreen() {
    var rating by remember { mutableStateOf(3.5f) }
    var max by remember { mutableStateOf(5) }
    var step by remember { mutableStateOf(0.5f) }
    var itemSize by remember { mutableStateOf(RatingBarDefaults.SizeMedium) }
    var itemSpacing by remember { mutableStateOf(RatingBarDefaults.ItemSpacing) }
    var readOnly by remember { mutableStateOf(false) }
    var rtl by remember { mutableStateOf(false) }

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
                        filledColor = MaterialTheme.colorScheme.primary,
                        unfilledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    )
                    ValueText(rating)
                }
            }

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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Read-only")
                        Spacer(Modifier.width(12.dp))
                        Switch(checked = readOnly, onCheckedChange = { readOnly = it })
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("RTL")
                        Spacer(Modifier.width(12.dp))
                        Switch(checked = rtl, onCheckedChange = { rtl = it })
                    }
                }
            }
        }
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
