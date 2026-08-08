package com.example.kmpnews.iosApp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmpnews.iosApp.di.initAppKoin
import com.example.kmpnews.shared.App

fun MainViewController() = ComposeUIViewController {
    initAppKoin()
    App()
}
