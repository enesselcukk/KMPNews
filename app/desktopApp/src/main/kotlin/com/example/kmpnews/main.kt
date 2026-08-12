package com.example.kmpnews

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.kmpnews.shared.KmpNewsApp
import com.example.kmpnews.shared.di.initAppKoin

fun main() = application {
    initAppKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPNews",
    ) {
        KmpNewsApp()
    }
}
