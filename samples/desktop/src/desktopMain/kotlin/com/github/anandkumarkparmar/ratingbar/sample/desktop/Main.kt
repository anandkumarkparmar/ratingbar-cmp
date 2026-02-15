package com.github.anandkumarkparmar.ratingbar.sample.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.anandkumarkparmar.ratingbar.sample.SampleApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RatingBar Sample - Desktop",
        state = rememberWindowState(width = 600.dp, height = 700.dp)
    ) {
        SampleApp()
    }
}
