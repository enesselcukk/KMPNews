package com.example.kmpnews.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.modules.SerializersModule

interface NavEntryProvider {
    val navKeySerializers: SerializersModule

    fun EntryProviderScope<NavKey>.registerEntries()
}
