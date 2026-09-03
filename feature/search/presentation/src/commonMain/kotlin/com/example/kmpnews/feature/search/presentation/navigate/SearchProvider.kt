package com.example.kmpnews.feature.search.presentation.navigate

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.feature.search.contract.SearchScreenDestination
import com.example.kmpnews.feature.search.presentation.ui.SearchScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal class SearchProvider : NavEntryProvider {
    override val navKeySerializers: SerializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(SearchScreenDestination::class, SearchScreenDestination.serializer())
        }
    }

    override fun EntryProviderScope<NavKey>.registerEntries() {
        entry<SearchScreenDestination> {
            SearchScreen()
        }
    }
}
