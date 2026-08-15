package com.example.kmpnews.feature.detail.presentation.navigate

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import com.example.kmpnews.feature.detail.presentation.DetailScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal class DetailProvider : NavEntryProvider {
    override val navKeySerializers: SerializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(DetailScreenDestination::class, DetailScreenDestination.serializer())
        }
    }

    override fun EntryProviderScope<NavKey>.registerEntries() {
        entry<DetailScreenDestination> { destination ->
            DetailScreen(newsId = destination.newsId)
        }
    }
}
