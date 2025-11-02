package com.jalmarquest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jalmar.quest.tilemap.DungeonCrawlerMapGenerator
import com.jalmar.quest.tilemap.TileMapCatalog
import com.jalmar.quest.tilemap.TileMapManager
import com.jalmarquest.ui.screens.MainMenuScreen
import com.jalmarquest.ui.screens.TileGameScreen
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

enum class GameScreen {
    MAIN_MENU,
    LOADING_DUNGEON,
    LOADING_EXPLORER,
    DUNGEON_CRAWLER,
    TILE_EXPLORER
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(GameScreen.MAIN_MENU) }
    val tileMapManager = remember { TileMapManager() }
    val scope = rememberCoroutineScope()
    
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                GameScreen.MAIN_MENU -> {
                    MainMenuScreen(
                        onStartDungeonCrawler = {
                            currentScreen = GameScreen.LOADING_DUNGEON
                        },
                        onStartTileExplorer = {
                            currentScreen = GameScreen.LOADING_EXPLORER
                        },
                        onQuit = {
                            exitProcess(0)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                GameScreen.LOADING_DUNGEON -> {
                    LoadingScreen(message = "Generating The Vast Underground...")
                    LaunchedEffect(Unit) {
                        scope.launch {
                            val dungeonMap = DungeonCrawlerMapGenerator.generateMainDungeon()
                            tileMapManager.loadMap(dungeonMap)
                            tileMapManager.setCurrentMap(dungeonMap.id)
                            currentScreen = GameScreen.DUNGEON_CRAWLER
                        }
                    }
                }
                
                GameScreen.LOADING_EXPLORER -> {
                    LoadingScreen(message = "Loading Buttonburgh...")
                    LaunchedEffect(Unit) {
                        scope.launch {
                            val explorerMap = TileMapCatalog.createButtonburghMap()
                            tileMapManager.loadMap(explorerMap)
                            tileMapManager.setCurrentMap(explorerMap.id)
                            currentScreen = GameScreen.TILE_EXPLORER
                        }
                    }
                }
                
                GameScreen.DUNGEON_CRAWLER, GameScreen.TILE_EXPLORER -> {
                    TileGameScreen(
                        tileMapManager = tileMapManager,
                        onBackToMenu = {
                            currentScreen = GameScreen.MAIN_MENU
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(message, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
