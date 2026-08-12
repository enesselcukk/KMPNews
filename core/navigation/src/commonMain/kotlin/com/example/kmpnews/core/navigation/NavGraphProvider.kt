package com.example.kmpnews.core.navigation

import androidx.navigation.NavGraphBuilder

fun interface NavGraphProvider {
    fun registerGraph(provider: NavGraphBuilder)
}
