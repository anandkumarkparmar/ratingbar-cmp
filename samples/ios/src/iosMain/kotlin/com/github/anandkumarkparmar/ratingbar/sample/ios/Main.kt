package com.github.anandkumarkparmar.ratingbar.sample.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.github.anandkumarkparmar.ratingbar.sample.SampleApp
import platform.UIKit.UIViewController

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    }
) {
    SampleApp()
}
