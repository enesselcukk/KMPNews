package com.example.kmpnews

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform