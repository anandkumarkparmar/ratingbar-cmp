package com.github.anandkumarkparmar.ratingbar.sample.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.github.anandkumarkparmar.ratingbar.sample.SampleApp
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val root = document.getElementById("root") as HTMLElement
    ComposeViewport(root) {
        SampleApp()
    }
}
