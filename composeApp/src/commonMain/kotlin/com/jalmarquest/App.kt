package com.jalmarquest

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jalmar.quest.tilemap.DungeonCrawlerMapGenerator
import com.jalmar.quest.tilemap.TileMapCatalog
import com.jalmar.quest.tilemap.TileMapManager
import com.jalmarquest.shared.battlepass.BattlePassManager
import com.jalmarquest.shared.battlepass.SeasonCatalog
import com.jalmarquest.shared.battlepass.TileExplorationChallengeTracker
import com.jalmarquest.shared.butterfly.ButterflyEffectManager
import com.jalmarquest.shared.model.UserPreferences
import com.jalmarquest.shared.persistence.PreferencesManager
import com.jalmarquest.shared.persistence.SaveManager
import com.jalmarquest.shared.quest.QuestManager
import com.jalmarquest.shared.state.GameStateManager
import com.jalmarquest.ui.screens.ArchetypeSelectionScreen
import com.jalmarquest.ui.screens.MainMenuScreen
import com.jalmarquest.ui.screens.PlayerArchetype
import com.jalmarquest.ui.screens.SettingsScreen
import com.jalmarquest.ui.screens.TileGameScreen
import com.jalmarquest.ui.theme.JQTheme
import com.jalmarquest.ui.theme.ThemeMode
import com.jalmarquest.ui.theme.JQTypography
import com.jalmarquest.ui.theme.JQAnimations
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.system.exitProcess

enum class GameScreen {
    MAIN_MENU,
    ARCHETYPE_SELECTION,
    SETTINGS,
    LOADING_DUNGEON,
    LOADING_EXPLORER,
    DUNGEON_CRAWLER,
    TILE_EXPLORER
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(GameScreen.MAIN_MENU) }
    var userPreferences by remember { mutableStateOf(UserPreferences()) }
    var selectedArchetype by remember { mutableStateOf<PlayerArchetype?>(null) }
    var hasSaveFiles by remember { mutableStateOf(false) }
    var mostRecentSave by remember { mutableStateOf<com.jalmarquest.shared.persistence.SaveSlotInfo?>(null) }
    val tileMapManager = remember { TileMapManager() }
    val scope = rememberCoroutineScope()
    
    // Theme state (from user preferences or settings)
    var themeMode by remember { mutableStateOf(ThemeMode.STANDARD) }
    var fontScale by remember { mutableStateOf(JQTypography.FontScale.MEDIUM) }
    
    // Inject SaveManager via Koin
    val saveManager: SaveManager = koinInject()
    
    // Inject PreferencesManager via Koin
    val preferencesManager: PreferencesManager = koinInject()
    
    // Challenge notification queue (shared across screens)
    val challengeNotifications = remember { androidx.compose.runtime.snapshots.SnapshotStateList<com.jalmarquest.shared.battlepass.ChallengeCompletionEvent>() }
    
    // Initialize Butterfly Effect Engine (required by QuestManager)
    val butterflyEffectManager = remember { ButterflyEffectManager() }
    
    // Initialize main GameStateManager and Battle Pass system
    val gameStateManager = remember { GameStateManager() }
    val battlePassManager = remember { 
        // Create a Spring season starting now (for demonstration)
        val currentTime = System.currentTimeMillis()
        val springSeason = SeasonCatalog.createSpringSeason(2025, currentTime)
        
        BattlePassManager(
            seasonCatalog = SeasonCatalog,
            activeSeason = springSeason
        ) 
    }
    val challengeTracker = remember { 
        TileExplorationChallengeTracker(
            gameStateManager = gameStateManager,
            battlePassManager = battlePassManager,
            onChallengeCompleted = { event ->
                // Add to notification queue
                challengeNotifications.add(event)
            }
        )
    }
    
    // Initialize Quest Manager with callback for quest-based challenges
    val questManager = remember { 
        QuestManager(butterflyEffectManager = butterflyEffectManager)
    }
    
    // Quest Challenge Tracker - links quest completions to Battle Pass challenges
    val questChallengeTracker = remember {
        com.jalmarquest.shared.battlepass.QuestChallengeTracker(
            gameStateManager = gameStateManager,
            battlePassManager = battlePassManager,
            scope = scope,
            onChallengeCompleted = { event ->
                // Add to notification queue (same queue as tile exploration)
                challengeNotifications.add(event)
            }
        )
    }
    
    // Combat Challenge Tracker - tracks combat victories and enemy kills
    val combatChallengeTracker = remember {
        com.jalmarquest.shared.battlepass.CombatChallengeTracker(
            gameStateManager = gameStateManager,
            battlePassManager = battlePassManager,
            scope = scope,
            onChallengeCompleted = { event ->
                challengeNotifications.add(event)
            }
        )
    }
    
    // NPC Challenge Tracker - tracks unique NPC interactions
    val npcChallengeTracker = remember {
        com.jalmarquest.shared.battlepass.NPCChallengeTracker(
            gameStateManager = gameStateManager,
            battlePassManager = battlePassManager,
            scope = scope,
            onChallengeCompleted = { event ->
                challengeNotifications.add(event)
            }
        )
    }
    
    // Item Challenge Tracker - tracks item collection, crafting, and shop purchases
    val itemChallengeTracker = remember {
        com.jalmarquest.shared.battlepass.ItemChallengeTracker(
            gameStateManager = gameStateManager,
            battlePassManager = battlePassManager,
            scope = scope,
            onChallengeCompleted = { event ->
                challengeNotifications.add(event)
            }
        )
    }
    
    // Check for existing save files on startup
    LaunchedEffect(Unit) {
        scope.launch {
            // Load preferences
            val prefsResult = preferencesManager.loadPreferences()
            prefsResult.onSuccess { loadedPrefs ->
                userPreferences = loadedPrefs
                println("⚙️ Loaded preferences: Volume=${loadedPrefs.masterVolume}, TTS=${loadedPrefs.ttsEnabled}")
            }.onFailure {
                println("⚙️ No preferences found, using defaults")
            }
            
            // Load save files
            val saves = saveManager.listSaves()
            hasSaveFiles = saves.isNotEmpty()
            
            // Get most recent save (highest timestamp)
            mostRecentSave = saves.maxByOrNull { it.timestamp }
            
            if (hasSaveFiles) {
                mostRecentSave?.let { save ->
                    println("📁 Found save: ${save.playerName} (Level ${save.level}) - ${save.slotName}")
                }
            } else {
                println("📁 No save files found")
            }
        }
    }
    
    // Apply JQTheme (replaces MaterialTheme)
    JQTheme(
        themeMode = themeMode,
        fontScale = fontScale
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Animated screen transitions
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    // Forward navigation (entering new screen)
                    if (targetState.ordinal > initialState.ordinal) {
                        JQAnimations.screenEnter() togetherWith JQAnimations.screenExit()
                    } else {
                        // Back navigation (returning to previous screen)
                        JQAnimations.screenPopEnter() togetherWith JQAnimations.screenPopExit()
                    }
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                GameScreen.MAIN_MENU -> {
                    MainMenuScreen(
                        onStartDungeonCrawler = {
                            currentScreen = GameScreen.LOADING_DUNGEON
                        },
                        onStartTileExplorer = {
                            // If no save files, go to archetype selection first
                            if (hasSaveFiles && mostRecentSave != null) {
                                // Load most recent save
                                scope.launch {
                                    val loadResult = saveManager.loadGame(mostRecentSave!!.slotName)
                                    loadResult.onSuccess { loadedState ->
                                        gameStateManager.loadGame(loadedState)
                                        println("✅ Loaded save: ${loadedState.player.name} (Level ${loadedState.player.level})")
                                        currentScreen = GameScreen.LOADING_EXPLORER
                                    }.onFailure { error ->
                                        println("❌ Failed to load save: ${error.message}")
                                        // Fall back to new game
                                        currentScreen = GameScreen.ARCHETYPE_SELECTION
                                    }
                                }
                            } else {
                                currentScreen = GameScreen.ARCHETYPE_SELECTION
                            }
                        },
                        onOpenSettings = {
                            currentScreen = GameScreen.SETTINGS
                        },
                        onQuit = {
                            exitProcess(0)
                        },
                        hasSaveFiles = hasSaveFiles,
                        mostRecentSave = mostRecentSave,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                GameScreen.ARCHETYPE_SELECTION -> {
                    ArchetypeSelectionScreen(
                        onArchetypeSelected = { archetype ->
                            selectedArchetype = archetype
                            // Create new game with selected archetype
                            scope.launch {
                                val newState = gameStateManager.createNewGame("Hero") // Default name, can be customized later
                                
                                // Apply archetype bonuses to player stats
                                val updatedPlayer = when (archetype) {
                                    PlayerArchetype.SCAVENGER -> {
                                        newState.player.copy(
                                            stats = newState.player.stats.copy(
                                                currentStamina = newState.player.stats.currentStamina + 10
                                            )
                                        )
                                    }
                                    PlayerArchetype.DIPLOMAT -> {
                                        newState.player.copy(
                                            stats = newState.player.stats.copy(
                                                currentStamina = newState.player.stats.currentStamina + 5
                                            )
                                        )
                                    }
                                    PlayerArchetype.FORAGER -> {
                                        newState.player.copy(
                                            stats = newState.player.stats.copy(
                                                maxStamina = newState.player.stats.maxStamina + 20,
                                                currentStamina = newState.player.stats.currentStamina + 20
                                            )
                                        )
                                    }
                                }
                                
                                // Update game state with archetype-modified player
                                gameStateManager.updateState { it.copy(player = updatedPlayer) }
                                
                                println("🎮 New game created: ${updatedPlayer.name} (${archetype.displayName})")
                                currentScreen = GameScreen.LOADING_EXPLORER
                            }
                        },
                        onBack = {
                            currentScreen = GameScreen.MAIN_MENU
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                GameScreen.SETTINGS -> {
                    SettingsScreen(
                        preferences = userPreferences,
                        onPreferencesChanged = { newPrefs ->
                            userPreferences = newPrefs
                            // Persist to PreferencesManager
                            scope.launch {
                                val saveResult = preferencesManager.savePreferences(newPrefs)
                                saveResult.onSuccess {
                                    println("⚙️ Preferences saved successfully")
                                }.onFailure { error ->
                                    println("❌ Failed to save preferences: ${error.message}")
                                }
                            }
                        },
                        onBack = {
                            currentScreen = GameScreen.MAIN_MENU
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
                            // Initialize new game if not already loaded
                            if (gameStateManager.gameState.value == null) {
                                val playerName = selectedArchetype?.displayName ?: "Jalmar"
                                gameStateManager.createNewGame(playerName)
                            }
                            
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
                        gameStateManager = gameStateManager,
                        questManager = questManager,
                        questChallengeTracker = questChallengeTracker,
                        combatChallengeTracker = combatChallengeTracker,
                        npcChallengeTracker = npcChallengeTracker,
                        itemChallengeTracker = itemChallengeTracker,
                        challengeTracker = challengeTracker,
                        challengeNotificationsQueue = challengeNotifications,
                        preferences = userPreferences, // Pass TTS and accessibility preferences
                        onBackToMenu = {
                            currentScreen = GameScreen.MAIN_MENU
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                } // End when
            } // End AnimatedContent
        } // End Surface
    } // End JQTheme
} // End App function

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
