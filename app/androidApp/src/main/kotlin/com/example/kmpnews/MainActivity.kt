package com.example.kmpnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kmpnews.shared.KmpNewsApp
import com.example.kmpnews.shared.di.initAppKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initAppKoin(applicationContext)
        setContent {
            KmpNewsApp()
        }
    }
}
