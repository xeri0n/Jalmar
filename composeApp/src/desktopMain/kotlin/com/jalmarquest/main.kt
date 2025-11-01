package com.jalmarquest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.jalmarquest.shared.di.platformModule
import com.jalmarquest.shared.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun main() {
    startKoin {
        modules(sharedModule, platformModule)
    }
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "JalmarQuest",
            state = rememberWindowState(width = 800.dp, height = 600.dp)
        ) {
            MaterialTheme {
                JalmarQuestScreen(
                    gameStateManager = koinInject()
                )
            }
        }
    }
}
