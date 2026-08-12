package com.example.kmpnews.core.network.client

import kotlin.test.Test
import kotlin.test.assertNotNull

class NewsApiHttpClientFactoryTest {
    @Test
    fun create_isNotNull() {
        val client = NewsApiHttpClientFactory.create(
            baseUrl = "https://example.com/v2/",
            enableLogging = false,
        )
        assertNotNull(client)
        client.close()
    }
}
