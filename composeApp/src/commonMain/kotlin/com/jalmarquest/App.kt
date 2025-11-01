package com.jalmarquest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.state.GameStateManager
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.movement.MovementResult
import dev.xeri0n.jalmarquest.ui.navigation.Screen
import dev.xeri0n.jalmarquest.ui.screens.MainMenuScreen
import dev.xeri0n.jalmarquest.ui.screens.SettingsScreen
import dev.xeri0n.jalmarquest.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.system.exitProcess

/**
 * Main entry point for JalmarQuest with navigation support.
 * 
 * Uses NavHost to navigate between:
 * - Main Menu (start screen)
 * - Game Screen (gameplay)
 * - Settings Screen (user preferences)
 * - Save/Load Screen (game saves management)
 */
@Composable
fun JalmarQuestScreen(gameStateManager: GameStateManager) {
    val navController = rememberNavController()
    val settingsViewModel = remember { SettingsViewModel() }
    val gameState by gameStateManager.gameState.collectAsState()
    
    // Determine starting route based on game state
    val startDestination = if (gameState == null) Screen.MainMenu.route else Screen.Game.route
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Main Menu
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onNewGame = {
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.MainMenu.route) { inclusive = true }
                    }
                },
                onLoadGame = {
                    navController.navigate(Screen.SaveLoad.route)
                },
                onSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onQuit = {
                    exitProcess(0)
                }
            )
        }
        
        // Game Screen
        composable(Screen.Game.route) {
            val currentGameState = gameState
            if (currentGameState != null) {
                GameplayScreen(
                    gameState = currentGameState,
                    gameStateManager = gameStateManager,
                    onNavigateToMenu = {
                        navController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.Game.route) { inclusive = true }
                        }
                    }
                )
            } else {
                // No game loaded, show character creation
                CharacterCreationScreen(
                    gameStateManager = gameStateManager,
                    onGameCreated = {
                        // Stay on Game screen, state will update
                    },
                    onBack = {
                        navController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.Game.route) { inclusive = true }
                        }
                    }
                )
            }
        }
        
        // Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Save/Load Screen (placeholder for now)
        composable(Screen.SaveLoad.route) {
            // TODO: Implement SaveLoadScreen integration
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Save/Load Screen",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            }
        }
    }
}

/**
 * Character creation screen for new games.
 * Shown when navigating to Game screen with no active game state.
 */
@Composable
fun CharacterCreationScreen(
    gameStateManager: GameStateManager,
    onGameCreated: () -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var playerName by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Your Quail",
                style = MaterialTheme.typography.headlineLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Enter your name") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("Back")
                }
                
                Button(
                    onClick = {
                        scope.launch {
                            if (playerName.isNotBlank()) {
                                gameStateManager.createNewGame(playerName)
                                onGameCreated()
                            }
                        }
                    },
                    enabled = playerName.isNotBlank()
                ) {
                    Text("Start Adventure")
                }
            }
        }
    }
}

@Composable
fun GameplayScreen(
    gameState: GameState,
    gameStateManager: GameStateManager,
    onNavigateToMenu: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val locationManager = koinInject<com.jalmarquest.shared.world.LocationManager>()
    val movementManager = koinInject<com.jalmarquest.shared.movement.MovementManager>()
    
    // Directly derive location from current gameState (no remember needed)
    val currentLocation = locationManager.getLocation(gameState.player.position.locationId)
    
    // Get enemies for current location
    val locationEnemies = remember(currentLocation) {
        if (currentLocation != null && !currentLocation.isSafeZone && currentLocation.encounterRate > 0.0) {
            com.jalmarquest.shared.combat.EnemyCatalog.getEnemiesForLocation(currentLocation.recommendedLevel)
        } else {
            emptyList()
        }
    }
    
    var notificationMessage by remember { mutableStateOf<String?>(null) }
    var showNotification by remember { mutableStateOf(false) }
    var showLocationDetails by remember { mutableStateOf(false) }
    
    // Auto-dismiss notification after 3 seconds
    LaunchedEffect(showNotification) {
        if (showNotification) {
            kotlinx.coroutines.delay(3000)
            showNotification = false
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar - Location Summary (Always Visible)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentLocation?.name ?: "Unknown",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${gameState.worldTime.season.name} • ${gameState.worldTime.getTimeOfDay().name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = gameState.weather.describe(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                            )
                        }
                        TextButton(
                            onClick = { showLocationDetails = !showLocationDetails }
                        ) {
                            Text(if (showLocationDetails) "Hide" else "Details")
                        }
                    }
                    
                    // Expandable location details
                    if (showLocationDetails) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentLocation?.description?.getSeasonalDescription(gameState.worldTime.season) ?: "You are somewhere...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Biome: ${currentLocation?.biome?.name ?: "Unknown"} • Pos: (${gameState.player.position.x}, ${gameState.player.position.y})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            // Scrollable content: Map, NPCs, Enemies, Info
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Map Section
                Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (gameState.player.position.locationId == "starting_village") "Buttonburgh City Map" else "World Map",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (gameState.player.position.locationId == "starting_village") {
                        ButtonburghCityMap(
                            onLocationClick = { direction ->
                                scope.launch {
                                    val result = movementManager.move(
                                        player = gameState.player,
                                        direction = direction,
                                        weather = gameState.weather
                                    )
                                    when (result) {
                                        is MovementResult.Success -> {
                                            gameStateManager.executeMove(result)
                                            val destLocation = locationManager.getLocation(result.newLocationId)
                                            notificationMessage = "Entered ${destLocation?.name}"
                                            showNotification = true
                                        }
                                        is MovementResult.Failure -> {
                                            notificationMessage = "Cannot enter: ${result.reason.name}"
                                            showNotification = true
                                        }
                                    }
                                }
                            }
                        )
                    } else if (currentLocation != null && currentLocation.connections.isNotEmpty()) {
                        WorldMapNavigation(
                            connections = currentLocation.connections,
                            onDirectionClick = { direction ->
                                scope.launch {
                                    val result = movementManager.move(
                                        player = gameState.player,
                                        direction = direction,
                                        weather = gameState.weather
                                    )
                                    when (result) {
                                        is MovementResult.Success -> {
                                            gameStateManager.executeMove(result)
                                            val destLocation = locationManager.getLocation(result.newLocationId)
                                            notificationMessage = "Moved ${direction.name} to ${destLocation?.name}"
                                            showNotification = true
                                        }
                                        is MovementResult.Failure -> {
                                            notificationMessage = "Cannot move: ${result.reason.name}"
                                            showNotification = true
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        Text("No exits available", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // NPCs and Enemies in Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // NPCs Column
                Card(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "NPCs",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (currentLocation != null && currentLocation.questGiverIds.isNotEmpty()) {
                            currentLocation.questGiverIds.take(2).forEach { npcId ->
                                Text(
                                    text = "• ${formatNpcName(npcId)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            if (currentLocation.questGiverIds.size > 2) {
                                Text(
                                    text = "+${currentLocation.questGiverIds.size - 2} more",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Text(
                                "None",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Enemies Column
                Card(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Enemies",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        if (locationEnemies.isNotEmpty()) {
                            // Show up to 2 enemies with win rates
                            locationEnemies.take(2).forEach { enemy ->
                                val winRate = com.jalmarquest.shared.combat.WinRateCalculator.calculateWinRate(
                                    gameState.player,
                                    enemy
                                )
                                val winRateColor = com.jalmarquest.shared.combat.WinRateCalculator.getWinRateColor(winRate)
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = enemy.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = when (winRateColor) {
                                                com.jalmarquest.shared.combat.WinRateColor.VERY_LOW,
                                                com.jalmarquest.shared.combat.WinRateColor.LOW -> MaterialTheme.colorScheme.error
                                                com.jalmarquest.shared.combat.WinRateColor.MEDIUM -> Color(0xFFFFA726) // Orange
                                                else -> MaterialTheme.colorScheme.primary
                                            }
                                        )
                                        Text(
                                            text = "Level ${enemy.level} • HP: ${enemy.maxHp}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        // Win rate with color indicator
                                        Text(
                                            text = "Win rate: $winRate%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = when (winRateColor) {
                                                com.jalmarquest.shared.combat.WinRateColor.VERY_HIGH -> Color(0xFF4CAF50) // Green
                                                com.jalmarquest.shared.combat.WinRateColor.HIGH -> Color(0xFF8BC34A) // Light green
                                                com.jalmarquest.shared.combat.WinRateColor.MEDIUM -> Color(0xFFFFC107) // Yellow
                                                com.jalmarquest.shared.combat.WinRateColor.LOW -> Color(0xFFFF9800) // Orange
                                                com.jalmarquest.shared.combat.WinRateColor.VERY_LOW -> Color(0xFFF44336) // Red
                                            }
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            notificationMessage = "Combat system coming soon!"
                                            showNotification = true
                                        },
                                        modifier = Modifier.padding(start = 8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = when (winRateColor) {
                                                com.jalmarquest.shared.combat.WinRateColor.VERY_LOW,
                                                com.jalmarquest.shared.combat.WinRateColor.LOW -> MaterialTheme.colorScheme.error
                                                else -> MaterialTheme.colorScheme.primary
                                            }
                                        )
                                    ) {
                                        Text("⚔", style = MaterialTheme.typography.titleMedium)
                                    }
                                }
                            }
                            
                            // Show count if more enemies exist
                            if (locationEnemies.size > 2) {
                                Text(
                                    text = "+${locationEnemies.size - 2} more enemy types",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Text(
                                "Safe Zone",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Player Stats
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Player Stats",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatChip("Lv", gameState.player.level.toString())
                        StatChip("HP", "${gameState.player.stats.currentHealth}/${gameState.player.stats.maxHealth}")
                        StatChip("Stam", "${gameState.player.stats.currentStamina}/${gameState.player.stats.maxStamina}")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // World Time
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "World Time",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatChip("Day", gameState.worldTime.day.toString())
                        StatChip("Time", formatTime(gameState.worldTime.hour, gameState.worldTime.minute))
                        StatChip(gameState.worldTime.season.name.take(3), gameState.worldTime.getTimeOfDay().name.take(4))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Statistics & Achievements
            StatisticsAndAchievementsCard(gameState)
            }
        }
        
        // Floating Notification Overlay (top-center)
        androidx.compose.animation.AnimatedVisibility(
            visible = showNotification,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp),
            enter = androidx.compose.animation.slideInVertically() + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.slideOutVertically() + androidx.compose.animation.fadeOut()
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = notificationMessage ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun StatChip(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ==================== HELPER FUNCTIONS ====================

fun formatTime(hour: Int, minute: Int): String {
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format("%d:%02d %s", displayHour, minute, period)
}

fun formatPlayTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%d:%02d:%02d", hours, minutes, secs)
}

fun formatNpcName(npcId: String): String {
    return npcId.split("_")
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}

@Composable
fun WorldMapNavigation(
    connections: List<com.jalmarquest.shared.world.LocationConnection>,
    onDirectionClick: (Direction) -> Unit
) {
    // Group connections by direction for compass layout
    val connectionMap = connections.filter { !it.isBlocked }
        .associateBy { it.direction }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // North row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Northwest
            DirectionButton(
                direction = Direction.NORTHWEST,
                enabled = connectionMap.containsKey(Direction.NORTHWEST),
                onClick = { onDirectionClick(Direction.NORTHWEST) }
            )
            // North
            DirectionButton(
                direction = Direction.NORTH,
                enabled = connectionMap.containsKey(Direction.NORTH),
                onClick = { onDirectionClick(Direction.NORTH) }
            )
            // Northeast
            DirectionButton(
                direction = Direction.NORTHEAST,
                enabled = connectionMap.containsKey(Direction.NORTHEAST),
                onClick = { onDirectionClick(Direction.NORTHEAST) }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Middle row (West, UP/DOWN, East)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // West
            DirectionButton(
                direction = Direction.WEST,
                enabled = connectionMap.containsKey(Direction.WEST),
                onClick = { onDirectionClick(Direction.WEST) },
                modifier = Modifier.weight(1f)
            )
            
            // Center: UP/DOWN buttons
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DirectionButton(
                    direction = Direction.UP,
                    enabled = connectionMap.containsKey(Direction.UP),
                    onClick = { onDirectionClick(Direction.UP) },
                    isVertical = true
                )
                Spacer(modifier = Modifier.height(4.dp))
                DirectionButton(
                    direction = Direction.DOWN,
                    enabled = connectionMap.containsKey(Direction.DOWN),
                    onClick = { onDirectionClick(Direction.DOWN) },
                    isVertical = true
                )
            }
            
            // East
            DirectionButton(
                direction = Direction.EAST,
                enabled = connectionMap.containsKey(Direction.EAST),
                onClick = { onDirectionClick(Direction.EAST) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // South row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Southwest
            DirectionButton(
                direction = Direction.SOUTHWEST,
                enabled = connectionMap.containsKey(Direction.SOUTHWEST),
                onClick = { onDirectionClick(Direction.SOUTHWEST) }
            )
            // South
            DirectionButton(
                direction = Direction.SOUTH,
                enabled = connectionMap.containsKey(Direction.SOUTH),
                onClick = { onDirectionClick(Direction.SOUTH) }
            )
            // Southeast
            DirectionButton(
                direction = Direction.SOUTHEAST,
                enabled = connectionMap.containsKey(Direction.SOUTHEAST),
                onClick = { onDirectionClick(Direction.SOUTHEAST) }
            )
        }
    }
}

@Composable
fun DirectionButton(
    direction: Direction,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVertical: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(
            if (isVertical) Modifier.width(80.dp) else Modifier.width(90.dp)
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = when (direction) {
                Direction.NORTH -> "N"
                Direction.SOUTH -> "S"
                Direction.EAST -> "E"
                Direction.WEST -> "W"
                Direction.NORTHEAST -> "NE"
                Direction.NORTHWEST -> "NW"
                Direction.SOUTHEAST -> "SE"
                Direction.SOUTHWEST -> "SW"
                Direction.UP -> "↑"
                Direction.DOWN -> "↓"
            },
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ButtonburghCityMap(onLocationClick: (Direction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row 1: The Hen Pen (UP)
        CityMapLocation(
            name = "The Hen Pen",
            direction = "UP",
            onClick = { onLocationClick(Direction.UP) }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Row 2: Center with exits
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Northwest exit
            ExitMarker("NW", "Windmill\nFarm") { onLocationClick(Direction.NORTHWEST) }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // North exit
            ExitMarker("N", "Meadow\nPath") { onLocationClick(Direction.NORTH) }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Northeast exit
            ExitMarker("NE", "Rolling\nHills") { onLocationClick(Direction.NORTHEAST) }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Row 3: West location, Center hub, East location
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // West: Gilded Seed Inn
            CityMapLocation(
                name = "Gilded Seed\nInn",
                direction = "WEST",
                onClick = { onLocationClick(Direction.WEST) },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Center: Buttonburgh hub
            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .border(2.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Buttonburgh\n(You are here)",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // East: The Quailsmith
            CityMapLocation(
                name = "The\nQuailsmith",
                direction = "EAST",
                onClick = { onLocationClick(Direction.EAST) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Row 4: Old Quill's Study (DOWN)
        CityMapLocation(
            name = "Old Quill's Study",
            direction = "DOWN",
            onClick = { onLocationClick(Direction.DOWN) }
        )
    }
}

@Composable
fun CityMapLocation(
    name: String,
    direction: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "[$direction]",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ExitMarker(
    abbreviation: String,
    locationName: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.width(90.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = abbreviation,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = locationName,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun StatisticsAndAchievementsCard(gameState: GameState) {
    var showAllStats by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Statistics & Achievements",
                    style = MaterialTheme.typography.titleSmall
                )
                TextButton(
                    onClick = { showAllStats = !showAllStats }
                ) {
                    Text(if (showAllStats) "Hide" else "Show")
                }
            }
            
            if (showAllStats) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Statistics Grid
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StatRow("Steps Taken", gameState.statistics.stepsTaken.toString())
                    StatRow("Enemies Defeated", gameState.statistics.enemiesDefeated.toString())
                    StatRow("Items Crafted", gameState.statistics.itemsCrafted.toString())
                    StatRow("Quests Completed", gameState.statistics.questsCompleted.toString())
                    StatRow("Seeds Collected", gameState.statistics.seedsCollected.toString())
                    StatRow("Damage Dealt", gameState.statistics.damageDealt.toString())
                    StatRow("Damage Taken", gameState.statistics.damageTaken.toString())
                    StatRow("Puddles Crossed", gameState.statistics.puddlesCrossed.toString())
                    StatRow("Gnomes Spotted", gameState.statistics.gnomesSpotted.toString())
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Achievements
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                val unlockedAchievements = gameState.achievements.filter { it.unlocked }
                
                if (unlockedAchievements.isEmpty()) {
                    Text(
                        text = "No achievements unlocked yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        unlockedAchievements.forEach { achievement ->
                            val definition = com.jalmarquest.shared.model.AchievementsCatalog.get(achievement.id)
                            AchievementRow(
                                name = definition?.name ?: achievement.id,
                                description = definition?.description ?: "Unknown achievement",
                                points = definition?.points ?: 0
                            )
                        }
                    }
                }
                
                // Summary line
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${unlockedAchievements.size} / ${com.jalmarquest.shared.model.AchievementsCatalog.all.size} achievements unlocked",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AchievementRow(name: String, description: String, points: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "🏆 $name",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${points}pts",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
