package com.jalmarquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.jalmarquest.shared.state.GameStateManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    
    private val gameStateManager: GameStateManager by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            App()
        }
    }
}
