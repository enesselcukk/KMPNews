package com.example.kmpnews.feature.search.presentation.di

import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.feature.search.presentation.navigate.SearchProvider
import com.example.kmpnews.feature.search.presentation.ui.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchPresentationModule = module {
    single<NavEntryProvider>(named("SearchProvider")) {
        SearchProvider()
    }

    viewModelOf(::SearchViewModel)
}
