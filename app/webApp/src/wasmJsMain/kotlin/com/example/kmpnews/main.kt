package com.example.kmpnews

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.kmpnews.shared.KmpNewsApp
import com.example.kmpnews.shared.di.initAppKoin
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initAppKoin()
    ComposeViewport(viewportContainerId = "webApp") {
        KmpNewsApp()
    }
    document.title = "KMPNews"
}
