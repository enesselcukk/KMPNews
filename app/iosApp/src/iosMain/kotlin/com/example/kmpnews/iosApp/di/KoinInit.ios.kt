package com.example.kmpnews.iosApp.di

import com.example.kmpnews.core.datastore.DefaultUserSettingsRepository
import com.example.kmpnews.core.datastore.UserSettingsRepository
import com.example.kmpnews.core.database.DatabaseFactory
import com.example.kmpnews.core.database.NewsDatabase
import com.example.kmpnews.core.navigation.navigationModule
import com.example.kmpnews.core.network.di.newsApiModule
import com.example.kmpnews.feature.detail.data.di.detailDataModule
import com.example.kmpnews.feature.detail.domain.di.detailDomainModule
import com.example.kmpnews.feature.detail.presentation.di.detailPresentationModule
import com.example.kmpnews.feature.home.data.di.homeDataModule
import com.example.kmpnews.feature.home.domain.di.homeDomainModule
import com.example.kmpnews.feature.home.presentation.di.homePresentationModule
import com.example.kmpnews.feature.search.data.di.searchDataModule
import com.example.kmpnews.feature.search.domain.di.searchDomainModule
import com.example.kmpnews.feature.search.presentation.di.searchPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

private val iosPlatformModule = module {
    single<NewsDatabase> {
        DatabaseFactory().createDatabase()
    }
    single { get<NewsDatabase>().homeHeadlineDao() }
    single<UserSettingsRepository> { DefaultUserSettingsRepository() }
}

fun initAppKoin() {
    if (KoinPlatform.getKoinOrNull() != null) return

    startKoin {
        modules(
            iosPlatformModule,
            newsApiModule,
            navigationModule,
            homeDataModule,
            homeDomainModule,
            homePresentationModule,
            detailDataModule,
            detailDomainModule,
            detailPresentationModule,
            searchDataModule,
            searchDomainModule,
            searchPresentationModule,
        )
    }
}
