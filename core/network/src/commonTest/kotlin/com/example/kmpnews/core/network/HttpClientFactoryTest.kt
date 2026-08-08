package com.example.kmpnews.core.network

import kotlin.test.Test
import kotlin.test.assertNotNull

class HttpClientFactoryTest {
    @Test
    fun createDefaultHttpClient_isNotNull() {
        val client = createDefaultHttpClient()
        assertNotNull(client)
        client.close()
    }
}
