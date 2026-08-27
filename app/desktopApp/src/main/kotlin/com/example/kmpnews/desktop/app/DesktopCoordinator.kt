package com.example.kmpnews.desktop.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.kmpnews.desktop.navigation.DesktopNavigationBridge

class DesktopCoordinator {
    private val navigationBridge = DesktopNavigationBridge()
    val detailStack = mutableStateListOf<String>()

    var isStarted by mutableStateOf(false)
        private set

    fun start() {
        if (isStarted) return

        navigationBridge.onNavigateToDetail = { articleUrl ->
            detailStack.add(articleUrl)
        }
        navigationBridge.onNavigateUp = {
            if (detailStack.isNotEmpty()) {
                detailStack.removeAt(detailStack.lastIndex)
            }
        }
        navigationBridge.start()
        isStarted = true
    }

    fun close() {
        navigationBridge.close()
    }
}
