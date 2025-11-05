package com.jalmarquest.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jalmar.quest.items.WorldItemManager
import com.jalmar.quest.npc.NPCManager
import com.jalmar.quest.tilemap.TileMapManager
import com.jalmar.quest.tilemap.MapTransitionManager
import com.jalmar.quest.tilemap.model.*
import com.jalmar.quest.interaction.InteractionManager
import com.jalmar.quest.interaction.Interactable
import com.jalmar.quest.interaction.InteractionResult
import com.jalmar.quest.interaction.InteractionData
import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.npc.NPC
import com.jalmarquest.shared.combat.*
import com.jalmarquest.ui.components.DialogueWindow
import com.jalmarquest.ui.components.InventoryPanel
import com.jalmarquest.ui.components.ChallengeNotification
import com.jalmarquest.ui.components.QuestNotification
import com.jalmarquest.ui.components.FloatingXPText
import com.jalmarquest.ui.components.FloatingXPData
import com.jalmarquest.ui.components.StatAllocationPanel
import com.jalmarquest.ui.components.LevelUpAnimation
import com.jalmarquest.ui.components.QuestRewardsPanel
import com.jalmarquest.ui.components.RecipeNotification
import com.jalmarquest.ui.components.RecipeNotificationData
import com.jalmarquest.utils.ImageLoader
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * Main tile-based game screen with dungeon crawler rendering.
 * Features fog of war, tile discovery, and OutWar-style navigation.
 */
@Composable
fun TileGameScreen(
    tileMapManager: TileMapManager,
    gameStateManager: com.jalmarquest.shared.state.GameStateManager? = null,
    questManager: com.jalmarquest.shared.quest.QuestManager? = null,
    questChallengeTracker: com.jalmarquest.shared.battlepass.QuestChallengeTracker? = null,
    combatChallengeTracker: com.jalmarquest.shared.battlepass.CombatChallengeTracker? = null,
    npcChallengeTracker: com.jalmarquest.shared.battlepass.NPCChallengeTracker? = null,
    itemChallengeTracker: com.jalmarquest.shared.battlepass.ItemChallengeTracker? = null,
    challengeTracker: com.jalmarquest.shared.battlepass.TileExplorationChallengeTracker? = null,
    challengeNotificationsQueue: SnapshotStateList<com.jalmarquest.shared.battlepass.ChallengeCompletionEvent>? = null,
    preferences: com.jalmarquest.shared.model.UserPreferences? = null, // TTS and accessibility settings
    onBackToMenu: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val currentMap by tileMapManager.currentMap.collectAsState()
    val gameState by gameStateManager?.gameState?.collectAsState() ?: remember { mutableStateOf(null) }
    val npcManager = remember { NPCManager() }
    
    // Quest notification state
    val questNotifications = remember { mutableStateListOf<com.jalmarquest.ui.components.QuestNotificationData>() }
    
    // WorldItemManager with ItemEvent callback for challenge tracking and quest objectives
    val worldItemManager = remember(questNotifications) { 
        WorldItemManager(
            onItemPickup = { event ->
                // Track Battle Pass challenges
                itemChallengeTracker?.onItemEvent(event)
                
                // Update quest objectives (COLLECT)
                gameState?.let { state ->
                    questManager?.let { qm ->
                        scope.launch {
                            val result = qm.updateObjective(
                                state,
                                com.jalmarquest.shared.quest.ObjectiveType.COLLECT,
                                event.itemId,
                                event.quantity
                            )
                            
                            when (result) {
                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                    // Update game state with quest progress
                                    gameStateManager?.updateState { result.gameState }
                                    
                                    // Show quest notification for each progressed quest
                                    result.questsProgressed.forEach { questId ->
                                        // Find the quest to display notification
                                        val quest = qm.getActiveQuests(result.gameState).find { it.id == questId }
                                            ?: qm.getCompletedQuests(result.gameState).find { it.id == questId }
                                        
                                        if (quest != null) {
                                            val isComplete = result.gameState.completedQuests.contains(questId)
                                            questNotifications.add(
                                                com.jalmarquest.ui.components.QuestNotificationData(
                                                    questName = quest.name,
                                                    objectiveDescription = "Collected ${event.itemName} ×${event.quantity}",
                                                    isQuestComplete = isComplete
                                                )
                                            )
                                        }
                                        println("📜 Quest Updated: $questId (COLLECT ${event.itemName} ×${event.quantity})")
                                    }
                                }
                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                    // No active quests tracking this item
                                }
                            }
                        }
                    }
                }
            }
        ) 
    }
    
    val mapTransitionManager = remember { MapTransitionManager() }
    
    // InteractionManager with NPC interaction callback for challenge tracking
    val interactionManager = remember { 
        InteractionManager(
            npcManager = npcManager,
            worldItemManager = worldItemManager,
            tileMapManager = tileMapManager,
            mapTransitionManager = mapTransitionManager,
            onNPCInteraction = { event ->
                npcChallengeTracker?.onNPCInteraction(event)
            }
        ) 
    }
    
    val combatManager = remember { CombatManager(com.jalmarquest.shared.butterfly.ButterflyEffectManager()) }
    
    var playerPosition by remember { mutableStateOf(TileCoordinate(5, 5)) }
    var selectedPath by remember { mutableStateOf<List<TileCoordinate>?>(null) }
    var discoveredTiles by remember { mutableStateOf(setOf<TileCoordinate>()) }
    var isMinimapExpanded by remember { mutableStateOf(false) }
    var isBannerExpanded by remember { mutableStateOf(false) }
    
    // Battle Pass state
    var isBattlePassOpen by remember { mutableStateOf(false) }
    val challengeNotifications = remember { mutableStateListOf<com.jalmarquest.ui.components.ChallengeNotificationData>() }
    
    // Floating XP text state
    val floatingXPTexts = remember { mutableStateListOf<FloatingXPData>() }
    
    // Stat allocation panel state
    var isStatAllocationOpen by remember { mutableStateOf(false) }
    
    // Level-up animation state
    var showLevelUpAnimation by remember { mutableStateOf(false) }
    var levelUpNewLevel by remember { mutableStateOf(1) }
    
    // Quest rewards panel state
    var showQuestRewards by remember { mutableStateOf(false) }
    var completedQuest by remember { mutableStateOf<com.jalmarquest.shared.quest.Quest?>(null) }
    
    // Recipe notification state
    val recipeNotifications = remember { mutableStateListOf<RecipeNotificationData>() }
    
    // Helper function to show quest notifications
    fun showQuestNotification(questId: String, objectiveDescription: String) {
        questManager?.let { qm ->
            gameState?.let { state ->
                val quest = qm.getActiveQuests(state).find { it.id == questId }
                    ?: qm.getCompletedQuests(state).find { it.id == questId }
                
                if (quest != null) {
                    val isComplete = state.completedQuests.contains(questId)
                    questNotifications.add(
                        com.jalmarquest.ui.components.QuestNotificationData(
                            questName = quest.name,
                            objectiveDescription = objectiveDescription,
                            isQuestComplete = isComplete
                        )
                    )
                    
                    // Show rewards panel when quest completes
                    if (isComplete) {
                        completedQuest = quest
                        showQuestRewards = true
                        
                        // Show recipe notifications for unlocked recipes
                        quest.rewards.unlockRecipeIds.forEach { recipeId ->
                            recipeNotifications.add(
                                RecipeNotificationData(
                                    recipeName = recipeId, // In full game, look up recipe name from catalog
                                    recipeId = recipeId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Watch for challenge completion events from the queue
    LaunchedEffect(challengeNotificationsQueue) {
        challengeNotificationsQueue?.let { queue ->
            while (true) {
                kotlinx.coroutines.delay(100) // Check every 100ms
                if (queue.isNotEmpty()) {
                    val event = queue.removeAt(0)
                    challengeNotifications.add(
                        com.jalmarquest.ui.components.ChallengeNotificationData(
                            message = "✅ ${event.challengeTitle} Complete!",
                            xpAwarded = event.xpAwarded,
                            tiersUnlocked = event.tiersUnlocked
                        )
                    )
                }
            }
        }
    }
    
    // Initialize map transition manager
    LaunchedEffect(Unit) {
        currentMap?.let { map ->
            mapTransitionManager.initialize(map)
        }
    }
    
    // Interaction state
    var nearbyInteractables by remember { mutableStateOf<List<Interactable>>(emptyList()) }
    var interactionMessage by remember { mutableStateOf<String?>(null) }
    var zoneChangeMessage by remember { mutableStateOf<String?>(null) }
    
    // NPC interaction state
    var activeNPC by remember { mutableStateOf<NPC?>(null) }
    var npcRelationship by remember { mutableStateOf(0) }
    var activeDialogueData by remember { mutableStateOf<InteractionData.NPCDialogue?>(null) }
    
    // Combat state
    var activeCombat by remember { mutableStateOf<CombatState?>(null) }
    var combatEnemy by remember { mutableStateOf<com.jalmarquest.shared.combat.Enemy?>(null) }
    var nearbyEnemies by remember { mutableStateOf<List<Pair<TileCoordinate, com.jalmarquest.shared.combat.Enemy>>>(emptyList()) }
    
    // Inventory state
    var playerInventory by remember { mutableStateOf(Inventory(maxSlots = 30, maxWeight = 12000)) }
    var isInventoryOpen by remember { mutableStateOf(false) }
    var pickupMessage by remember { mutableStateOf<String?>(null) }
    
    // Crafting state
    var isCraftingScreenOpen by remember { mutableStateOf(false) }
    
    // Camera controls
    var cameraZoom by remember { mutableStateOf(1f) } // 1x, 2x, or 0.5x zoom
    
    // Player stats
    var currentStamina by remember { mutableStateOf(100) }
    val maxStamina = 100
    var currentHealth by remember { mutableStateOf(100) }
    val maxHealth = 100
    var currentMana by remember { mutableStateOf(50) }
    val maxMana = 50
    var staminaWarning by remember { mutableStateOf(false) }
    
    // Background image
    var backgroundImage by remember { mutableStateOf<ImageBitmap?>(null) }
    
    // Load background image
    LaunchedEffect(Unit) {
        val paths = listOf(
            "game_background.png",
            "drawable/game_background.png",
            "composeResources/drawable/game_background.png"
        )
        for (path in paths) {
            val loadedImage = ImageLoader.loadImageBitmap(path)
            if (loadedImage != null) {
                backgroundImage = loadedImage
                println("✅ Loaded game background from: $path")
                break
            }
        }
        if (backgroundImage == null) {
            println("⚠️ Failed to load game background image")
        }
    }
    
    // Stamina regeneration (1 stamina per second)
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            if (currentStamina < maxStamina) {
                currentStamina = (currentStamina + 1).coerceAtMost(maxStamina)
            }
        }
    }
    
    // Spawn items from map POIs
    LaunchedEffect(currentMap) {
        currentMap?.let { map ->
            // Clear existing items when switching maps
            worldItemManager.clearAllItems()
            
            // Scan map for ITEM POIs and spawn them in the world
            for (x in 0 until map.width) {
                for (y in 0 until map.height) {
                    val tile = map.getTileAt(x, y)
                    if (tile?.poiType == POIType.ITEM) {
                        val itemId = tile.poiData
                        if (!itemId.isNullOrBlank()) {
                            // Default quantity of 1 for each item POI
                            // In future, could parse quantity from poiData if needed
                            worldItemManager.placeItem(TileCoordinate(x, y), itemId, 1)
                        }
                    }
                }
            }
        }
    }
    
    // Spawn some enemies in Buttonburgh (for testing)
    LaunchedEffect(currentMap) {
        currentMap?.let { map ->
            if (map.id == "buttonburgh") {
                // Add grasshopper enemies at specific coordinates
                // Note: This modifies the tile's POI type
                // In production, you'd use a proper enemy spawn system
            }
        }
    }
    
    // Auto-clear pickup message
    LaunchedEffect(pickupMessage) {
        if (pickupMessage != null) {
            kotlinx.coroutines.delay(3000)
            pickupMessage = null
        }
    }
    
    // Auto-discover tiles around player position (vision radius)
    LaunchedEffect(playerPosition) {
        val newDiscovered = mutableSetOf<TileCoordinate>()
        newDiscovered.addAll(discoveredTiles)
        
        val previouslyDiscovered = discoveredTiles.toSet() // Snapshot before discovery
        
        currentMap?.let { map ->
            // Reduced vision radius: 3 tiles in each direction (7x7 grid = 49 tiles max)
            for (dy in -3..3) {
                for (dx in -3..3) {
                    val x = playerPosition.x + dx
                    val y = playerPosition.y + dy
                    if (x >= 0 && y >= 0 && x < map.width && y < map.height) {
                        val coord = TileCoordinate(x, y)
                        val wasNew = newDiscovered.add(coord) // Returns true if newly added
                        
                        // Track new tile discoveries for Battle Pass challenges
                        if (wasNew && challengeTracker != null) {
                            scope.launch {
                                val wasAlreadyDiscovered = previouslyDiscovered.contains(coord)
                                challengeTracker.onTileDiscovered(
                                    mapId = map.id,
                                    x = x,
                                    y = y,
                                    wasAlreadyDiscovered = wasAlreadyDiscovered
                                )
                            }
                        }
                    }
                }
            }
        }
        discoveredTiles = newDiscovered
        
        // Update nearby interactables when player moves
        nearbyInteractables = interactionManager.getAdjacentInteractables(playerPosition)
        
        // Update nearby enemies when player moves
        currentMap?.let { map ->
            val enemies = mutableListOf<Pair<TileCoordinate, com.jalmarquest.shared.combat.Enemy>>()
            for (dy in -1..1) {
                for (dx in -1..1) {
                    if (dx == 0 && dy == 0) continue // Skip player's own tile
                    val checkX = playerPosition.x + dx
                    val checkY = playerPosition.y + dy
                    if (map.isInBounds(checkX, checkY)) {
                        val tile = map.getTileAt(checkX, checkY)
                        if (tile?.poiType == POIType.ENEMY) {
                            val enemyId = tile.poiData
                            if (enemyId != null) {
                                val enemy = com.jalmarquest.shared.combat.EnemyCatalog.allEnemies.find { it.id == enemyId }
                                if (enemy != null) {
                                    enemies.add(Pair(TileCoordinate(checkX, checkY), enemy))
                                }
                            }
                        }
                    }
                }
            }
            nearbyEnemies = enemies
        }
    }
    
    // Auto-clear interaction message
    LaunchedEffect(interactionMessage) {
        if (interactionMessage != null) {
            kotlinx.coroutines.delay(3000)
            interactionMessage = null
        }
    }
    
    // Auto-clear zone change message
    LaunchedEffect(zoneChangeMessage) {
        if (zoneChangeMessage != null) {
            kotlinx.coroutines.delay(3000)
            zoneChangeMessage = null
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Background image
        backgroundImage?.let { bitmap ->
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = "Fantasy forest background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        // Top center messages (zone changes, pickups, interactions)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .zIndex(100f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zone change message
            if (zoneChangeMessage != null) {
                Surface(
                    color = Color(0xEE1a1a1a),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 8.dp
                ) {
                    Text(
                        zoneChangeMessage ?: "",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFFD700),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
            
            // Pickup message
            if (pickupMessage != null) {
                Surface(
                    color = Color(0xEE1a1a1a),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 8.dp
                ) {
                    Text(
                        "✓ $pickupMessage",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF44ff44),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
            
            // Interaction message
            if (interactionMessage != null) {
                Surface(
                    color = Color(0xEE1a1a1a),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 8.dp
                ) {
                    Text(
                        "ℹ $interactionMessage",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF44aaff),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        }
        
        // When zoom is 1x, show framed UI with side panels
        if (cameraZoom == 1f) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Panel - Character Stats & Info
                Surface(
                    modifier = Modifier.width(180.dp).fillMaxHeight(),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "ADVENTURER",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 11.sp,
                            color = Color(0xFFFFD700)
                        )
                        HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
                        
                        Text("Jalmar the Brave", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = Color(0xFFCCCCCC))
                        Text("Level 1 Button Quail", style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = Color(0xFF888888))
                        
                        Spacer(modifier = Modifier.height(1.dp))
                        
                        StatBar("Health", currentHealth, maxHealth, Color(0xFF44ff44))
                        StatBar("Stamina", currentStamina, maxStamina, Color(0xFF44ccff))
                        StatBar("Mana", currentMana, maxMana, Color(0xFFcc44ff))
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        // Stamina warning
                        if (staminaWarning) {
                            Text("⚠ Not enough stamina!", style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = Color(0xFFff4444))
                        }
                        
                        // Nearby interactables
                        if (nearbyInteractables.isNotEmpty() || nearbyEnemies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("NEARBY", style = MaterialTheme.typography.titleSmall, fontSize = 10.sp, color = Color(0xFFFFD700))
                            HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
                            
                            // Show nearby enemies with attack buttons
                            nearbyEnemies.forEach { (enemyCoord, enemy) ->
                                Button(
                                    onClick = {
                                        // Initiate combat with this enemy
                                        combatEnemy = enemy
                                        
                                        val playerCombatData = PlayerCombatData(
                                            id = "player",
                                            name = "Jalmar",
                                            maxHp = maxHealth,
                                            currentHp = currentHealth,
                                            strength = 5,
                                            agility = 8,
                                            vitality = 5,
                                            intelligence = 3,
                                            luck = 6,
                                            weaponDamage = 5,
                                            armorDefense = 2,
                                            activeStatusEffects = emptyList()
                                        )
                                        
                                        val enemyCombatData = EnemyCombatData(
                                            id = "enemy_${enemy.id}",
                                            name = enemy.name,
                                            maxHp = enemy.maxHp,
                                            currentHp = enemy.maxHp,
                                            strength = enemy.strength,
                                            agility = enemy.agility,
                                            vitality = enemy.vitality,
                                            intelligence = enemy.intelligence,
                                            luck = enemy.luck,
                                            baseDamage = enemy.baseDamage,
                                            defense = enemy.defense,
                                            xpReward = enemy.xpReward,
                                            catalogId = enemy.id,
                                            activeStatusEffects = emptyList()
                                        )
                                        
                                        val newCombat = combatManager.initiateCombat(
                                            combatId = "combat_${System.currentTimeMillis()}",
                                            player = playerCombatData,
                                            companion = null,
                                            enemies = listOf(enemyCombatData)
                                        )
                                        activeCombat = newCombat
                                    },
                                    modifier = Modifier.fillMaxWidth().height(28.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC4444)),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("⚔", fontSize = 12.sp)
                                        Text(
                                            "Attack ${enemy.name}",
                                            fontSize = 9.sp,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                            
                            nearbyInteractables.take(3).forEach { interactable ->
                                Button(
                                    onClick = {
                                        scope.launch {
                                            // Use gameState if available, otherwise create dummy for interaction
                                            val currentGameState = gameState ?: com.jalmarquest.shared.model.GameState.createNew("Player", "player_temp")
                                            val result = interactionManager.interact(interactable, playerInventory, currentGameState)
                                            when (result) {
                                                is InteractionResult.Success -> {
                                                    interactionMessage = result.message
                                                    
                                                    // Handle specific interaction types
                                                    when (val data = result.data) {
                                                        is InteractionData.NPCDialogue -> {
                                                            // Open dialogue window
                                                            val npc = npcManager.getNPC(data.npcId)
                                                            if (npc != null) {
                                                                activeNPC = npc
                                                                npcRelationship = data.relationshipScore
                                                                activeDialogueData = data
                                                                
                                                                // Update quest objectives (TALK)
                                                                currentGameState?.let { state ->
                                                                    questManager?.let { qm ->
                                                                        scope.launch {
                                                                            val questResult = qm.updateObjective(
                                                                                state,
                                                                                com.jalmarquest.shared.quest.ObjectiveType.TALK,
                                                                                data.npcId,
                                                                                1
                                                                            )
                                                                            
                                                                            when (questResult) {
                                                                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                                                    // Update game state with quest progress
                                                                                    gameStateManager?.updateState { questResult.gameState }
                                                                                    
                                                                                    // Show quest notification and log progress
                                                                                    questResult.questsProgressed.forEach { questId ->
                                                                                        println("📜 Quest Updated: $questId (TALK ${data.npcId})")
                                                                                        showQuestNotification(questId, "Talked to ${data.npcName}")
                                                                                    }
                                                                                }
                                                                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                                                                    // No active quests tracking this NPC
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        is InteractionData.QuestGiver -> {
                                                            // Open quest dialogue window (same as NPC for now)
                                                            val npc = npcManager.getNPC(data.npcId)
                                                            if (npc != null) {
                                                                activeNPC = npc
                                                                npcRelationship = data.relationshipScore
                                                                // Store quest info for future quest UI
                                                                println("Quest Giver: ${data.npcName}")
                                                                println("  Available Quests: ${data.availableQuests}")
                                                                println("  Active Quests: ${data.activeQuests}")
                                                                println("  Completable Quests: ${data.completableQuests}")
                                                                
                                                                // Update quest objectives (TALK) - Quest givers count as talking too
                                                                currentGameState?.let { state ->
                                                                    questManager?.let { qm ->
                                                                        scope.launch {
                                                                            val questResult = qm.updateObjective(
                                                                                state,
                                                                                com.jalmarquest.shared.quest.ObjectiveType.TALK,
                                                                                data.npcId,
                                                                                1
                                                                            )
                                                                            
                                                                            when (questResult) {
                                                                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                                                    // Update game state with quest progress
                                                                                    gameStateManager?.updateState { questResult.gameState }
                                                                                    
                                                                                    // Show quest notification and log progress
                                                                                    questResult.questsProgressed.forEach { questId ->
                                                                                        println("📜 Quest Updated: $questId (TALK ${data.npcId})")
                                                                                        showQuestNotification(questId, "Talked to ${data.npcName}")
                                                                                    }
                                                                                }
                                                                                is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                                                                    // No active quests tracking this NPC
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        is InteractionData.ItemPickup -> {
                                                            if (data.addedToInventory) {
                                                                // Refresh nearby interactables (item was picked up)
                                                                nearbyInteractables = interactionManager.getAdjacentInteractables(playerPosition)
                                                            }
                                                        }
                                                        is InteractionData.MapTransition -> {
                                                            // Handle map transition
                                                            val transitionData = MapTransitionManager.TransitionData(
                                                                targetMapId = data.targetMapId,
                                                                targetCoordinate = TileCoordinate(data.targetX, data.targetY),
                                                                transitionType = data.transitionType
                                                            )
                                                            
                                                            val transitionResult = mapTransitionManager.transitionToMap(transitionData)
                                                            when (transitionResult) {
                                                                is MapTransitionManager.TransitionResult.Success -> {
                                                                    // Load the new map
                                                                    tileMapManager.loadMap(transitionResult.newMap)
                                                                    tileMapManager.setCurrentMap(transitionResult.newMap.id)
                                                                    
                                                                    // Move player to spawn point
                                                                    playerPosition = transitionResult.spawnCoordinate
                                                                    
                                                                    // Reset discovered tiles for new map
                                                                    discoveredTiles = setOf()
                                                                    
                                                                    // Update nearby interactables
                                                                    nearbyInteractables = interactionManager.getAdjacentInteractables(playerPosition)
                                                                    
                                                                    // Update quest objectives (REACH - entering new map)
                                                                    gameState?.let { state ->
                                                                        questManager?.let { qm ->
                                                                            scope.launch {
                                                                                val result = qm.updateObjective(
                                                                                    state,
                                                                                    com.jalmarquest.shared.quest.ObjectiveType.REACH,
                                                                                    transitionResult.newMap.id,
                                                                                    1
                                                                                )
                                                                                
                                                                                when (result) {
                                                                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                                                        gameStateManager?.updateState { result.gameState }
                                                                                        result.questsProgressed.forEach { questId ->
                                                                                            println("📜 Quest Updated: $questId (REACH ${transitionResult.newMap.id})")
                                                                                            showQuestNotification(questId, "Reached ${transitionResult.newMap.name}")
                                                                                        }
                                                                                    }
                                                                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {}
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    
                                                                    zoneChangeMessage = "Entered ${transitionResult.newMap.name}"
                                                                }
                                                                is MapTransitionManager.TransitionResult.Failure -> {
                                                                    interactionMessage = "Cannot transition: ${transitionResult.reason}"
                                                                }
                                                            }
                                                        }
                                                        is InteractionData.CraftingStation -> {
                                                            // Open crafting screen
                                                            isCraftingScreenOpen = true
                                                        }
                                                        else -> {
                                                            // Other interaction types
                                                        }
                                                    }
                                                }
                                                is InteractionResult.Failure -> {
                                                    interactionMessage = "Failed: ${result.reason}"
                                                }
                                                is InteractionResult.NoInteraction -> {
                                                    interactionMessage = "Nothing to interact with"
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().height(28.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4a4a6a)),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        interactable.description,
                                        fontSize = 9.sp,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        
                        // Battle Pass button
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { isBattlePassOpen = true },
                            modifier = Modifier.fillMaxWidth().height(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6a4a6a)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("🏆 Battle Pass", fontSize = 9.sp)
                        }
                        
                        // Stat Allocation button
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { isStatAllocationOpen = true },
                            modifier = Modifier.fillMaxWidth().height(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6a5a4a)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            enabled = (gameState?.player?.stats?.availableStatPoints ?: 0) > 0
                        ) {
                            val points = gameState?.player?.stats?.availableStatPoints ?: 0
                            Text("⚡ Stats ($points pts)", fontSize = 9.sp)
                        }
                        
                        // Inventory button
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { isInventoryOpen = true },
                            modifier = Modifier.fillMaxWidth().height(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4a6a4a)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("📦 Inventory (${playerInventory.currentSlotCount()}/${playerInventory.maxSlots})", fontSize = 9.sp)
                        }
                    }
                }
                
                // Center - Game View
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    currentMap?.let { map ->
                        TileMapView(
                            map = map,
                            playerPosition = playerPosition,
                            selectedPath = selectedPath,
                            discoveredTiles = discoveredTiles,
                            cameraZoom = cameraZoom,
                            worldItemManager = worldItemManager,
                            onTileClick = { clickedCoord ->
                                scope.launch {
                                    if (discoveredTiles.contains(clickedCoord)) {
                                        val clickedTile = map.getTileAt(clickedCoord.x, clickedCoord.y)
                                        
                                        // Check if clicking on player's current tile (pickup items)
                                        if (clickedCoord == playerPosition) {
                                            val items = worldItemManager.getItemsAt(clickedCoord)
                                            if (items.isNotEmpty()) {
                                                // Pick up first item
                                                val worldItem = items.first()
                                                val item = ItemCatalog.getItem(worldItem.itemId)
                                                if (item != null) {
                                                    println("🔍 Picking up: ${item.name} ×${worldItem.quantity}")
                                                    println("🔍 Current inventory slots: ${playerInventory.slots.size}")
                                                    
                                                    val (newInventory, result) = InventoryManager.addItem(
                                                        playerInventory,
                                                        worldItem.itemId,
                                                        worldItem.quantity
                                                    )
                                                    
                                                    println("🔍 Add item result: $result")
                                                    println("🔍 New inventory slots: ${newInventory.slots.size}")
                                                    
                                                    when (result) {
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Success -> {
                                                            playerInventory = newInventory
                                                            println("✅ Inventory updated! Slots: ${playerInventory.slots.size}, Items: ${playerInventory.slots.map { "${it.itemId}×${it.quantity}" }}")
                                                            worldItemManager.pickupItem(clickedCoord, worldItem.itemId)
                                                            pickupMessage = "Picked up ${item.name} ×${worldItem.quantity}"
                                                            
                                                            // Track item collection for challenges
                                                            itemChallengeTracker?.onItemEvent(
                                                                com.jalmarquest.shared.battlepass.ItemEvent(
                                                                    eventType = com.jalmarquest.shared.battlepass.ItemEventType.COLLECTED,
                                                                    itemId = worldItem.itemId,
                                                                    itemName = item.name,
                                                                    quantity = worldItem.quantity,
                                                                    rarity = item.rarity.name.lowercase() // Convert enum to lowercase string
                                                                )
                                                            )
                                                        }
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Failure.InventoryFull -> {
                                                            pickupMessage = "Inventory full!"
                                                        }
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Failure.WeightExceeded -> {
                                                            pickupMessage = "Too heavy! (${item.formattedWeight()})"
                                                        }
                                                        else -> {
                                                            pickupMessage = "Cannot pick up item"
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        // Check if clicking on an NPC tile (adjacent to player)
                                        else if (clickedTile?.poiType == POIType.NPC && 
                                            playerPosition.distanceTo(clickedCoord) <= 1) {
                                            // Open NPC dialogue
                                            val npcId = clickedTile.poiData
                                            if (npcId != null) {
                                                val npc = npcManager.getNPC(npcId)
                                                if (npc != null) {
                                                    activeNPC = npc
                                                    npcRelationship = npcManager.getRelationship(npcId)
                                                    
                                                    // Track NPC interaction for challenges
                                                    npcChallengeTracker?.onNPCInteraction(
                                                        com.jalmarquest.shared.battlepass.NPCInteractionEvent(
                                                            npcId = npc.id,
                                                            npcName = npc.name
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        // Check if clicking on an ENEMY tile (adjacent to player)
                                        else if (clickedTile?.poiType == POIType.ENEMY && 
                                            playerPosition.distanceTo(clickedCoord) <= 1) {
                                            // Start combat
                                            println("🎯 Enemy clicked! Tile: $clickedTile")
                                            val enemyId = clickedTile.poiData
                                            if (enemyId != null) {
                                                println("🔍 Looking for enemy with ID: $enemyId")
                                                val enemy = com.jalmarquest.shared.combat.EnemyCatalog.allEnemies.find { it.id == enemyId }
                                                if (enemy != null) {
                                                    println("✅ Found enemy: ${enemy.name}")
                                                    combatEnemy = enemy
                                                    
                                                    // Create combat state
                                                    val playerCombatData = PlayerCombatData(
                                                        id = "player",
                                                        name = "Jalmar",
                                                        maxHp = maxHealth,
                                                        currentHp = currentHealth,
                                                        strength = 5,
                                                        agility = 8,
                                                        vitality = 5,
                                                        intelligence = 3,
                                                        luck = 6,
                                                        weaponDamage = 5,
                                                        armorDefense = 2,
                                                        activeStatusEffects = emptyList()
                                                    )
                                                    
                                                    val enemyCombatData = EnemyCombatData(
                                                        id = "enemy_${enemy.id}",
                                                        name = enemy.name,
                                                        maxHp = enemy.maxHp,
                                                        currentHp = enemy.maxHp,
                                                        strength = enemy.strength,
                                                        agility = enemy.agility,
                                                        vitality = enemy.vitality,
                                                        intelligence = enemy.intelligence,
                                                        luck = enemy.luck,
                                                        baseDamage = enemy.baseDamage,
                                                        defense = enemy.defense,
                                                        xpReward = enemy.xpReward,
                                                        catalogId = enemy.id,
                                                        activeStatusEffects = emptyList()
                                                    )
                                                    
                                                    val newCombat = combatManager.initiateCombat(
                                                        combatId = "combat_${System.currentTimeMillis()}",
                                                        player = playerCombatData,
                                                        companion = null,
                                                        enemies = listOf(enemyCombatData)
                                                    )
                                                    println("⚔️ Combat initiated! Turn order: ${newCombat.turnOrder}")
                                                    activeCombat = newCombat
                                                } else {
                                                    println("❌ Enemy not found in catalog!")
                                                }
                                            } else {
                                                println("❌ Enemy tile has no poiData!")
                                            }
                                        }
                                        // Check if clicking on an ITEM tile (adjacent to player)
                                        else if (clickedTile?.poiType == POIType.ITEM && 
                                            playerPosition.distanceTo(clickedCoord) <= 1) {
                                            // Pick up items at this location
                                            val items = worldItemManager.getItemsAt(clickedCoord)
                                            if (items.isNotEmpty()) {
                                                val worldItem = items.first()
                                                val item = ItemCatalog.getItem(worldItem.itemId)
                                                if (item != null) {
                                                    val (newInventory, result) = InventoryManager.addItem(
                                                        playerInventory,
                                                        worldItem.itemId,
                                                        worldItem.quantity
                                                    )
                                                    
                                                    when (result) {
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Success -> {
                                                            playerInventory = newInventory
                                                            worldItemManager.pickupItem(clickedCoord, worldItem.itemId)
                                                            pickupMessage = "Picked up ${item.name} ×${worldItem.quantity}"
                                                        }
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Failure.InventoryFull -> {
                                                            pickupMessage = "Inventory full!"
                                                        }
                                                        is com.jalmarquest.shared.inventory.ItemAddResult.Failure.WeightExceeded -> {
                                                            pickupMessage = "Too heavy! (${item.formattedWeight()})"
                                                        }
                                                        else -> {
                                                            pickupMessage = "Cannot pick up item"
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            // Normal movement
                                            val path = tileMapManager.findPath(playerPosition, clickedCoord)
                                            if (path.success && path.path.isNotEmpty()) {
                                                // Calculate stamina cost for the entire path
                                                // Exclude starting position from cost calculation
                                                val pathToMove = path.path.filter { it != playerPosition }
                                                val totalStaminaCost = pathToMove.sumOf { coord ->
                                                    val tile = map.getTileAt(coord.x, coord.y)
                                                    val cost = tile?.terrainType?.movementCost ?: 1.0
                                                    kotlin.math.max(1, cost.toInt()) // Minimum 1 stamina per tile
                                                }
                                                
                                                // Check if player has enough stamina
                                                if (currentStamina >= totalStaminaCost) {
                                                    selectedPath = path.path
                                                    playerPosition = clickedCoord
                                                    currentStamina = (currentStamina - totalStaminaCost).coerceAtLeast(0)
                                                    staminaWarning = false
                                                    
                                                    // Auto-pickup items at the new position
                                                    val items = worldItemManager.getItemsAt(clickedCoord)
                                                    if (items.isNotEmpty()) {
                                                        val worldItem = items.first()
                                                        val item = ItemCatalog.getItem(worldItem.itemId)
                                                        if (item != null) {
                                                            println("🔍 Auto-picking up: ${item.name} ×${worldItem.quantity}")
                                                            println("🔍 Current inventory slots: ${playerInventory.slots.size}")
                                                            
                                                            val (newInventory, result) = InventoryManager.addItem(
                                                                playerInventory,
                                                                worldItem.itemId,
                                                                worldItem.quantity
                                                            )
                                                            
                                                            println("🔍 Add item result: $result")
                                                            println("🔍 New inventory slots: ${newInventory.slots.size}")
                                                            
                                                            when (result) {
                                                                is com.jalmarquest.shared.inventory.ItemAddResult.Success -> {
                                                                    playerInventory = newInventory
                                                                    println("✅ Inventory updated! Slots: ${playerInventory.slots.size}, Items: ${playerInventory.slots.map { "${it.itemId}×${it.quantity}" }}")
                                                                    worldItemManager.pickupItem(clickedCoord, worldItem.itemId)
                                                                    pickupMessage = "Picked up ${item.name} ×${worldItem.quantity}"
                                                                }
                                                                is com.jalmarquest.shared.inventory.ItemAddResult.Failure.InventoryFull -> {
                                                                    pickupMessage = "Inventory full!"
                                                                }
                                                                is com.jalmarquest.shared.inventory.ItemAddResult.Failure.WeightExceeded -> {
                                                                    pickupMessage = "Too heavy! (${item.formattedWeight()})"
                                                                }
                                                                else -> {
                                                                    pickupMessage = "Cannot pick up item"
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    // Not enough stamina - show warning
                                                    staminaWarning = true
                                                    scope.launch {
                                                        kotlinx.coroutines.delay(2000)
                                                        staminaWarning = false
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize(0.95f)
                                .aspectRatio(1f)
                        )
                    }
                }
                
                // Right Panel - Minimap & Controls
                Surface(
                    modifier = Modifier.width(180.dp).fillMaxHeight(),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Header with MAP title and hamburger menu button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("MAP", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFD700))
                            
                            // Hamburger menu button
                            IconButton(
                                onClick = onBackToMenu,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Column(
                                    modifier = Modifier.size(18.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                }
                            }
                        }
                        HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
                        
                        currentMap?.let { map ->
                            MiniMap(map = map, playerPosition = playerPosition, discoveredTiles = discoveredTiles)
                        }
                        
                        HorizontalDivider(color = Color(0xFF444444))
                        
                        Text("NEARBY", style = MaterialTheme.typography.titleSmall, fontSize = 12.sp, color = Color(0xFFFFD700))
                        
                        // Scan tiles around player for POIs and interactables
                        val nearbyPOIs = remember(playerPosition, currentMap) {
                            currentMap?.let { map ->
                                val pois = mutableListOf<Pair<POIType, Int>>()
                                for (dy in -3..3) {
                                    for (dx in -3..3) {
                                        if (dx == 0 && dy == 0) continue
                                        val x = playerPosition.x + dx
                                        val y = playerPosition.y + dy
                                        if (x >= 0 && y >= 0 && x < map.width && y < map.height) {
                                            val tile = map.getTileAt(x, y)
                                            if (tile?.poiType != POIType.NONE && tile?.poiType != null) {
                                                val distance = kotlin.math.abs(dx) + kotlin.math.abs(dy)
                                                pois.add(tile.poiType to distance)
                                            }
                                        }
                                    }
                                }
                                pois.sortedBy { it.second }
                            } ?: emptyList()
                        }
                        
                        if (nearbyPOIs.isEmpty()) {
                            Text("Nothing of interest nearby", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = Color(0xFF666666), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        } else {
                            nearbyPOIs.take(5).forEach { (poiType, distance) ->
                                val poiIcon = when (poiType) {
                                    POIType.NPC -> "👤"
                                    POIType.ENEMY -> "⚔"
                                    POIType.SHOP -> "🏪"
                                    POIType.INN -> "🛏"
                                    POIType.ITEM -> "📦"
                                    POIType.QUEST_MARKER -> "❗"
                                    POIType.RESOURCE -> "⛏"
                                    POIType.CRAFTING_STATION -> "🔨"
                                    POIType.ENTRANCE -> "🚪"
                                    POIType.EXIT -> "🚪"
                                    POIType.HOUSE -> "🏠"
                                    else -> "•"
                                }
                                
                                val poiColor = when (poiType) {
                                    POIType.NPC -> Color(0xFFffff00)
                                    POIType.ENEMY -> Color(0xFFff0000)
                                    POIType.SHOP -> Color(0xFFffaa00)
                                    POIType.INN -> Color(0xFFff88ff)
                                    POIType.ITEM -> Color(0xFFaaffff)
                                    POIType.QUEST_MARKER -> Color(0xFFaa00ff)
                                    POIType.RESOURCE -> Color(0xFFff8800)
                                    POIType.CRAFTING_STATION -> Color(0xFFaa5500)
                                    POIType.ENTRANCE -> Color(0xFF00aaff)
                                    POIType.EXIT -> Color(0xFF00ff00)
                                    else -> Color(0xFFaaaaaa)
                                }
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(poiIcon, fontSize = 12.sp, modifier = Modifier.padding(end = 6.dp))
                                    Text(
                                        "${poiType.name.replace("_", " ")} ($distance tiles)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = poiColor,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Zoomed view - fullscreen map with floating UI
            currentMap?.let { map ->
                TileMapView(
                    map = map,
                    playerPosition = playerPosition,
                    selectedPath = selectedPath,
                    discoveredTiles = discoveredTiles,
                    cameraZoom = cameraZoom,
                    worldItemManager = worldItemManager,
                    onTileClick = { clickedCoord ->
                        scope.launch {
                            if (discoveredTiles.contains(clickedCoord)) {
                                val path = tileMapManager.findPath(playerPosition, clickedCoord)
                                if (path.success && path.path.isNotEmpty()) {
                                    selectedPath = path.path
                                    playerPosition = clickedCoord
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Floating banner (only when NOT at 1x)
            Column(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
                Surface(
                    color = Color(0xDD000000),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.pointerInput(Unit) { detectTapGestures { isBannerExpanded = !isBannerExpanded } }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentMap?.name ?: "Loading...", style = MaterialTheme.typography.titleLarge, color = Color(0xFFFFD700), modifier = Modifier.weight(1f))
                            Text(if (isBannerExpanded) "−" else "+", style = MaterialTheme.typography.titleLarge, color = Color(0xFFFFD700))
                        }
                        
                        if (isBannerExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Position: (${playerPosition.x}, ${playerPosition.y})", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFCCCCCC))
                            Text("Explored: ${discoveredTiles.size} tiles", style = MaterialTheme.typography.bodySmall, color = Color(0xFF88FF88))
                            
                            currentMap?.getTileAt(playerPosition)?.let { tile ->
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = Color(0xFF444444))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Terrain: ${tile.terrainType.name}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFAAAAAA))
                                if (tile.poiType != POIType.NONE) {
                                    Text("POI: ${tile.poiType.name}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFAA00))
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8b4513))) {
                                Text("Back to Menu")
                            }
                        }
                    }
                }
            }
            
            // Floating minimap (only when NOT at 1x)
            if (cameraZoom != 1f) {
                currentMap?.let { map ->
                    Column(modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                        if (isMinimapExpanded) {
                            Surface(modifier = Modifier.size(200.dp), color = Color(0xDD000000), shape = MaterialTheme.shapes.medium) {
                                MiniMap(map = map, playerPosition = playerPosition, discoveredTiles = discoveredTiles, modifier = Modifier.padding(8.dp))
                            }
                        }
                        
                        Button(
                            onClick = { isMinimapExpanded = !isMinimapExpanded },
                            modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xDD000000)),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Text(if (isMinimapExpanded) "−" else "MAP", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
        
        // Zoom controls at bottom center (always visible)
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { cameraZoom = when (cameraZoom) { 2f -> 1f; 1f -> 0.5f; else -> 0.5f } },
                modifier = Modifier.size(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (cameraZoom == 0.5f) Color(0xFF4a9a4a) else Color(0xFF2a2a2a)),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text("−", fontSize = 24.sp, color = Color.White)
            }
            
            Button(
                onClick = { cameraZoom = 1f },
                modifier = Modifier.width(80.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (cameraZoom == 1f) Color(0xFF4a9a4a) else Color(0xFF2a2a2a)),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(
                    when (cameraZoom) { 2f -> "ZOOM 2×"; 0.5f -> "ZOOM ½×"; else -> "ZOOM 1×" },
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            
            Button(
                onClick = { cameraZoom = when (cameraZoom) { 0.5f -> 1f; 1f -> 2f; else -> 2f } },
                modifier = Modifier.size(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (cameraZoom == 2f) Color(0xFF4a9a4a) else Color(0xFF2a2a2a)),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text("+", fontSize = 24.sp, color = Color.White)
            }
        }
        
        // Dialogue window overlay
        activeNPC?.let { npc ->
            DialogueWindow(
                npc = npc,
                relationshipScore = npcRelationship,
                preferences = preferences, // Pass TTS preferences
                onChoice = { choiceIndex ->
                    scope.launch {
                        // TODO: Wire to DialogueManager for full dialogue trees
                        // For now, handle basic relationship modification
                        npcManager.modifyRelationship(npc.id, 5)
                        npcRelationship = npcManager.getRelationship(npc.id)
                        
                        // Track TALK quest objectives
                        gameState?.let { state ->
                            questManager?.let { qm ->
                                val result = qm.updateObjective(
                                    state,
                                    com.jalmarquest.shared.quest.ObjectiveType.TALK,
                                    npc.id,
                                    1
                                )
                                
                                when (result) {
                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                        gameStateManager?.updateState { result.gameState }
                                        result.questsProgressed.forEach { questId ->
                                            println("📜 Quest Updated: $questId (TALK ${npc.name})")
                                            showQuestNotification(questId, "Talked to ${npc.name}")
                                        }
                                    }
                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {}
                                }
                            }
                        }
                    }
                },
                onClose = {
                    activeNPC = null
                }
            )
        }
        
        // Inventory panel overlay
        if (isInventoryOpen) {
            InventoryPanel(
                inventory = playerInventory,
                equippedItems = gameState?.player?.equippedItems ?: emptyMap(),
                onItemClick = { item ->
                    println("📦 Item clicked: ${item.name}")
                },
                onItemEquip = if (gameStateManager != null) { { itemId ->
                    scope.launch {
                        val result = gameStateManager.equipItem(itemId)
                        when (result) {
                            is com.jalmarquest.shared.equipment.EquipmentResult.Success.Equipped -> {
                                println("⚔️ Equipped ${result.itemId} in ${result.slot} slot")
                                if (result.replacedItemId != null) {
                                    println("⚔️ Replaced ${result.replacedItemId}")
                                }
                            }
                            is com.jalmarquest.shared.equipment.EquipmentResult.Failure -> {
                                println("❌ Equipment failed: $result")
                            }
                            else -> {}
                        }
                    }
                }} else null,
                onClose = {
                    isInventoryOpen = false
                }
            )
        }
        
        // Crafting screen overlay
        if (isCraftingScreenOpen) {
            val craftingManager = remember { com.jalmarquest.shared.crafting.CraftingManager }
            val recipeCatalog = remember { com.jalmarquest.shared.crafting.RecipeCatalog }
            val inventoryManager = remember { com.jalmarquest.shared.inventory.InventoryManager }
            
            // Create temporary player data for crafting (level 10 for demo)
            val tempPlayer = remember {
                Player(
                    id = "player_1",
                    name = "Jalmar",
                    level = 10,
                    inventory = playerInventory
                )
            }
            
            CraftingScreen(
                player = tempPlayer,
                playerInventory = playerInventory,
                craftingManager = craftingManager,
                recipeCatalog = recipeCatalog,
                inventoryManager = inventoryManager,
                onItemCrafted = { itemId, quantity ->
                    // Emit ItemEvent for challenge tracking
                    val itemName = itemId.split("_").joinToString(" ") { it.capitalize() }
                    itemChallengeTracker?.onItemEvent(
                        com.jalmarquest.shared.battlepass.ItemEvent(
                            eventType = com.jalmarquest.shared.battlepass.ItemEventType.CRAFTED,
                            itemId = itemId,
                            itemName = itemName,
                            quantity = quantity
                        )
                    )
                    
                    // Update quest objectives (CRAFT)
                    gameState?.let { state ->
                        questManager?.let { qm ->
                            scope.launch {
                                val result = qm.updateObjective(
                                    state,
                                    com.jalmarquest.shared.quest.ObjectiveType.CRAFT,
                                    itemId,
                                    quantity
                                )
                                
                                when (result) {
                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                        // Update game state with quest progress
                                        gameStateManager?.updateState { result.gameState }
                                        
                                        // Show quest notification with item name
                                        result.questsProgressed.forEach { questId ->
                                            println("📜 Quest Updated: $questId (CRAFT $itemName ×$quantity)")
                                            showQuestNotification(questId, "Crafted $itemName ×$quantity")
                                        }
                                    }
                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                        // No active quests tracking this item
                                    }
                                }
                            }
                        }
                    }
                    
                    interactionMessage = "✨ Crafted ${quantity}x $itemName! ✨"
                },
                onClose = {
                    isCraftingScreenOpen = false
                }
            )
        }
        
        // Combat screen overlay
        activeCombat?.let { combat ->
            CombatScreen(
                combatState = combat,
                onPlayerAction = { action ->
                    scope.launch {
                        var (newCombat, result) = combatManager.executeAction(combat, action)
                        println("⚔️ Player action executed: $result")
                        
                        // Advance turn after player action
                        newCombat = combatManager.advanceTurn(newCombat)
                        println("➡️ Turn advanced to: ${newCombat.getCurrentTurnParticipantId()}")
                        
                        activeCombat = newCombat
                        
                        // Update player health from combat
                        currentHealth = newCombat.player.currentHp
                        
                        // Check if combat is over
                        if (newCombat.isCombatOver()) {
                            val isVictory = newCombat.enemies.all { it.currentHp <= 0 }
                            
                            // Track combat for challenges
                            combatChallengeTracker?.onCombatCompleted(
                                com.jalmarquest.shared.battlepass.CombatEvent(
                                    victory = isVictory,
                                    enemiesDefeated = if (isVictory) newCombat.enemies.size else 0,
                                    enemyType = combatEnemy?.id?.substringBefore("_") // Extract type from ID (e.g., "goblin_1" -> "goblin")
                                )
                            )
                            
                            if (isVictory) {
                                // Generate combat rewards (XP + loot)
                                val rewards = combatManager.generateCombatRewards(newCombat.enemies)
                                
                                // Drop loot items at combat location
                                rewards.itemsLooted.forEach { (itemId, quantity) ->
                                    worldItemManager.placeItem(playerPosition, itemId, quantity)
                                }
                                
                                // Update quest objectives (KILL) for each defeated enemy
                                gameState?.let { state ->
                                    questManager?.let { qm ->
                                        newCombat.enemies.forEach { enemy ->
                                            val catalogEnemy = com.jalmarquest.shared.combat.EnemyCatalog.allEnemies.find { it.id == enemy.catalogId }
                                            if (catalogEnemy != null) {
                                                val result = qm.updateObjective(
                                                    state,
                                                    com.jalmarquest.shared.quest.ObjectiveType.KILL,
                                                    catalogEnemy.id,
                                                    1
                                                )
                                                
                                                when (result) {
                                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                        // Update game state with quest progress
                                                        scope.launch {
                                                            gameStateManager?.updateState { result.gameState }
                                                        }
                                                        
                                                        // Show quest notification and log progress
                                                        result.questsProgressed.forEach { questId ->
                                                            println("📜 Quest Updated: $questId (KILL ${catalogEnemy.id})")
                                                            showQuestNotification(questId, "Defeated ${catalogEnemy.name}")
                                                        }
                                                    }
                                                    is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                                        // No active quests tracking this enemy
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                // Show victory message with rewards
                                val rewardsSummary = rewards.summary()
                                interactionMessage = "Victory! Defeated ${combatEnemy?.name}. $rewardsSummary"
                                
                                // Award XP to player
                                val xpReward = combatEnemy?.xpReward?.toLong() ?: 0L
                                if (xpReward > 0) {
                                    scope.launch {
                                        gameStateManager?.addExperience(xpReward)
                                        
                                        // Show floating XP text
                                        floatingXPTexts.add(FloatingXPData(amount = xpReward.toInt()))
                                        
                                        // Check if player leveled up and track LEVEL objectives
                                        val newLevel = gameStateManager?.gameState?.value?.player?.level
                                        if (newLevel != null && newLevel > (gameState?.player?.level ?: 1)) {
                                            println("🎉 LEVEL UP! Now level $newLevel")
                                            
                                            // Show level-up animation
                                            showLevelUpAnimation = true
                                            levelUpNewLevel = newLevel
                                            
                                            // Open stat allocation panel (after animation)
                                            scope.launch {
                                                kotlinx.coroutines.delay(3500) // Wait for animation
                                                isStatAllocationOpen = true
                                            }
                                            
                                            // Update LEVEL objectives
                                            gameState?.let { state ->
                                                questManager?.let { qm ->
                                                    val result = qm.updateObjective(
                                                        state,
                                                        com.jalmarquest.shared.quest.ObjectiveType.LEVEL,
                                                        newLevel.toString(),
                                                        1
                                                    )
                                                    
                                                    when (result) {
                                                        is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                            gameStateManager?.updateState { result.gameState }
                                                            result.questsProgressed.forEach { questId ->
                                                                println("📜 Quest Updated: $questId (LEVEL $newLevel)")
                                                                showQuestNotification(questId, "Reached level $newLevel")
                                                            }
                                                        }
                                                        is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {}
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                println("🎁 Combat Rewards: $rewardsSummary (+$xpReward XP)")
                                
                                // Degrade equipment durability after combat
                                scope.launch {
                                    gameState?.player?.let { player ->
                                        val equipmentManager = com.jalmarquest.shared.equipment.EquipmentManager
                                        var updatedPlayer = player
                                        val brokenItems = mutableListOf<String>()
                                        
                                        // Degrade weapon durability
                                        if (player.equippedItems.containsKey(com.jalmarquest.shared.equipment.EquipmentSlot.WEAPON)) {
                                            val weaponEquipment = player.equippedItems[com.jalmarquest.shared.equipment.EquipmentSlot.WEAPON]
                                            val (newPlayer, result) = equipmentManager.degradeDurability(
                                                updatedPlayer,
                                                com.jalmarquest.shared.equipment.EquipmentSlot.WEAPON,
                                                amount = 1
                                            )
                                            updatedPlayer = newPlayer
                                            
                                            if (result is com.jalmarquest.shared.equipment.DurabilityResult.Degraded && result.itemBroke) {
                                                weaponEquipment?.let { equip ->
                                                    val item = com.jalmarquest.shared.inventory.ItemCatalog.getItem(equip.itemId)
                                                    brokenItems.add(item?.name ?: equip.itemId)
                                                }
                                            }
                                        }
                                        
                                        // Degrade armor durability
                                        listOf(
                                            com.jalmarquest.shared.equipment.EquipmentSlot.HEAD,
                                            com.jalmarquest.shared.equipment.EquipmentSlot.BODY,
                                            com.jalmarquest.shared.equipment.EquipmentSlot.LEGS,
                                            com.jalmarquest.shared.equipment.EquipmentSlot.FEET
                                        ).forEach { slot ->
                                            if (updatedPlayer.equippedItems.containsKey(slot)) {
                                                val armorEquipment = updatedPlayer.equippedItems[slot]
                                                val (newPlayer, result) = equipmentManager.degradeDurability(
                                                    updatedPlayer,
                                                    slot,
                                                    amount = 1
                                                )
                                                updatedPlayer = newPlayer
                                                
                                                if (result is com.jalmarquest.shared.equipment.DurabilityResult.Degraded && result.itemBroke) {
                                                    armorEquipment?.let { equip ->
                                                        val item = com.jalmarquest.shared.inventory.ItemCatalog.getItem(equip.itemId)
                                                        brokenItems.add(item?.name ?: equip.itemId)
                                                    }
                                                }
                                            }
                                        }
                                        
                                        // Update game state with degraded equipment
                                        gameStateManager?.updateState { gameState ->
                                            gameState.copy(player = updatedPlayer)
                                        }
                                        
                                        // Show notifications for broken items
                                        if (brokenItems.isNotEmpty()) {
                                            brokenItems.forEach { itemName ->
                                                println("⚠️ BROKEN: $itemName (0 durability)")
                                            }
                                            interactionMessage = "Victory! ${brokenItems.joinToString(", ")} BROKE during combat! (Check inventory)"
                                        }
                                        
                                        println("🔧 Equipment durability degraded after combat")
                                    }
                                }
                                
                                // Remove enemy from map
                                currentMap?.let { map ->
                                    val enemyTile = map.tiles.find { 
                                        it.poiType == POIType.ENEMY && it.poiData == combatEnemy?.id 
                                    }
                                    enemyTile?.let { tile ->
                                        scope.launch {
                                            val success = tileMapManager.updateTilePOI(
                                                x = tile.coordinate.x,
                                                y = tile.coordinate.y,
                                                newPoiType = POIType.NONE,
                                                newPoiData = null
                                            )
                                            if (success) {
                                                println("🗑️ Removed ENEMY POI from (${tile.coordinate.x}, ${tile.coordinate.y})")
                                            } else {
                                                println("⚠️ Failed to remove ENEMY POI")
                                            }
                                        }
                                    }
                                }
                            } else {
                                interactionMessage = "Defeated..."
                            }
                            
                            // Close combat after delay
                            kotlinx.coroutines.delay(2000)
                            activeCombat = null
                            combatEnemy = null
                        }
                    }
                },
                onEnemyTurn = { currentCombat ->
                    println("🤖 onEnemyTurn callback triggered!")
                    scope.launch {
                        val currentTurnId = currentCombat.getCurrentTurnParticipantId()
                        val enemy = currentCombat.getEnemy(currentTurnId)
                        println("🤖 Current turn ID: $currentTurnId, Found enemy: ${enemy?.name}")
                        
                        if (enemy != null) {
                            // Get enemy behavior type from catalog
                            val catalogEnemy = com.jalmarquest.shared.combat.EnemyCatalog.allEnemies.find { it.id == enemy.catalogId }
                            val behaviorType = catalogEnemy?.behaviorType ?: EnemyBehaviorType.AGGRESSIVE
                            println("🤖 Enemy behavior: $behaviorType")
                            
                            val enemyAction = com.jalmarquest.shared.combat.EnemyAI.decideAction(
                                enemy.id,
                                behaviorType,
                                currentCombat
                            )
                            println("🤖 Enemy action: $enemyAction")
                            var (newCombat, _) = combatManager.executeAction(currentCombat, enemyAction)
                            println("🤖 Combat executed! New player HP: ${newCombat.player.currentHp}")
                            
                            // Advance turn after enemy action
                            newCombat = combatManager.advanceTurn(newCombat)
                            println("➡️ Turn advanced to: ${newCombat.getCurrentTurnParticipantId()}")
                            
                            activeCombat = newCombat
                            currentHealth = newCombat.player.currentHp
                            
                            // Check if combat ended after enemy turn
                            if (newCombat.isCombatOver()) {
                                val isVictory = newCombat.enemies.all { it.currentHp <= 0 }
                                println("🤖 Combat over! Victory: $isVictory")
                                
                                // Track combat for challenges
                                combatChallengeTracker?.onCombatCompleted(
                                    com.jalmarquest.shared.battlepass.CombatEvent(
                                        victory = isVictory,
                                        enemiesDefeated = if (isVictory) newCombat.enemies.size else 0,
                                        enemyType = combatEnemy?.id?.substringBefore("_") // Extract type from ID (e.g., "goblin_1" -> "goblin")
                                    )
                                )
                                
                                if (isVictory) {
                                    // Generate combat rewards (XP + loot)
                                    val rewards = combatManager.generateCombatRewards(newCombat.enemies)
                                    
                                    // Drop loot items at combat location
                                    rewards.itemsLooted.forEach { (itemId, quantity) ->
                                        worldItemManager.placeItem(playerPosition, itemId, quantity)
                                    }
                                    
                                    // Update quest objectives (KILL) for each defeated enemy
                                    gameState?.let { state ->
                                        questManager?.let { qm ->
                                            newCombat.enemies.forEach { enemy ->
                                                val catalogEnemy = com.jalmarquest.shared.combat.EnemyCatalog.allEnemies.find { it.id == enemy.catalogId }
                                                if (catalogEnemy != null) {
                                                    val result = qm.updateObjective(
                                                        state,
                                                        com.jalmarquest.shared.quest.ObjectiveType.KILL,
                                                        catalogEnemy.id,
                                                        1
                                                    )
                                                    
                                                    when (result) {
                                                        is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                            // Update game state with quest progress
                                                            scope.launch {
                                                                gameStateManager?.updateState { result.gameState }
                                                            }
                                                            
                                                            // Show quest notification and log progress
                                                            result.questsProgressed.forEach { questId ->
                                                                println("📜 Quest Updated: $questId (KILL ${catalogEnemy.id})")
                                                                showQuestNotification(questId, "Defeated ${catalogEnemy.name}")
                                                            }
                                                        }
                                                        is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {
                                                            // No active quests tracking this enemy
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                    // Show victory message with rewards
                                    val rewardsSummary = rewards.summary()
                                    interactionMessage = "Victory! Defeated ${combatEnemy?.name}. $rewardsSummary"
                                    
                                    // Award XP to player
                                    val xpReward = combatEnemy?.xpReward?.toLong() ?: 0L
                                    if (xpReward > 0) {
                                        scope.launch {
                                            gameStateManager?.addExperience(xpReward)
                                            
                                            // Show floating XP text
                                            floatingXPTexts.add(FloatingXPData(amount = xpReward.toInt()))
                                            
                                            // Check if player leveled up and track LEVEL objectives
                                            val newLevel = gameStateManager?.gameState?.value?.player?.level
                                            if (newLevel != null && newLevel > (gameState?.player?.level ?: 1)) {
                                                println("🎉 LEVEL UP! Now level $newLevel")
                                                
                                                // Show level-up animation
                                                showLevelUpAnimation = true
                                                levelUpNewLevel = newLevel
                                                
                                                // Open stat allocation panel (after animation)
                                                scope.launch {
                                                    kotlinx.coroutines.delay(3500) // Wait for animation
                                                    isStatAllocationOpen = true
                                                }
                                                
                                                // Update LEVEL objectives
                                                gameState?.let { state ->
                                                    questManager?.let { qm ->
                                                        val result = qm.updateObjective(
                                                            state,
                                                            com.jalmarquest.shared.quest.ObjectiveType.LEVEL,
                                                            newLevel.toString(),
                                                            1
                                                        )
                                                        
                                                        when (result) {
                                                            is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.Success -> {
                                                                gameStateManager?.updateState { result.gameState }
                                                                result.questsProgressed.forEach { questId ->
                                                                    println("📜 Quest Updated: $questId (LEVEL $newLevel)")
                                                                    showQuestNotification(questId, "Reached level $newLevel")
                                                                }
                                                            }
                                                            is com.jalmarquest.shared.quest.QuestManager.UpdateObjectiveResult.NoChange -> {}
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    println("🎁 Combat Rewards: $rewardsSummary (+$xpReward XP)")
                                } else {
                                    interactionMessage = "Defeated..."
                                }
                                
                                kotlinx.coroutines.delay(2000)
                                activeCombat = null
                                combatEnemy = null
                            }
                        } else {
                            println("❌ No enemy found for turn ID: $currentTurnId")
                        }
                    }
                },
                onFlee = {
                    scope.launch {
                        // 50% chance to flee
                        if (kotlin.random.Random.nextBoolean()) {
                            interactionMessage = "Fled from combat!"
                            activeCombat = null
                            combatEnemy = null
                        } else {
                            interactionMessage = "Could not escape!"
                            // Enemy gets free attack
                            activeCombat?.let { combat ->
                                val enemy = combat.enemies.firstOrNull()
                                if (enemy != null) {
                                    val enemyAction = CombatAction.Attack(combat.player.id)
                                    val (newCombat, _) = combatManager.executeAction(combat, enemyAction)
                                    activeCombat = newCombat
                                    currentHealth = newCombat.player.currentHp
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Battle Pass overlay
        if (isBattlePassOpen && gameStateManager != null && challengeTracker != null) {
            BattlePassPanel(
                gameStateManager = gameStateManager,
                onClose = { isBattlePassOpen = false }
            )
        }
        
        // Stat Allocation overlay
        if (isStatAllocationOpen && gameStateManager != null) {
            val currentGameState = gameState
            if (currentGameState != null) {
                StatAllocationPanel(
                    player = currentGameState.player,
                    onStatAllocated = { statName, points ->
                        scope.launch {
                            val result = gameStateManager.allocateStat(statName, points)
                            if (result is com.jalmarquest.shared.progression.StatAllocationResult.Success) {
                                println("✅ Allocated $points points to $statName")
                            } else if (result is com.jalmarquest.shared.progression.StatAllocationResult.Failure) {
                                println("❌ Failed to allocate: ${result.reason}")
                            }
                        }
                    },
                    onClose = { isStatAllocationOpen = false }
                )
            }
        }
        
        // Level-up animation (fullscreen overlay)
        if (showLevelUpAnimation) {
            LevelUpAnimation(
                newLevel = levelUpNewLevel,
                onDismiss = { showLevelUpAnimation = false }
            )
        }
        
        // Quest rewards panel
        if (showQuestRewards && completedQuest != null) {
            QuestRewardsPanel(
                quest = completedQuest!!,
                rewards = completedQuest!!.rewards,
                onClose = {
                    showQuestRewards = false
                    completedQuest = null
                }
            )
        }
        
        // Challenge completion notifications (top of screen)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            challengeNotifications.firstOrNull()?.let { notification ->
                ChallengeNotification(
                    message = notification.message,
                    xpAwarded = notification.xpAwarded,
                    tiersUnlocked = notification.tiersUnlocked,
                    onDismiss = {
                        challengeNotifications.removeAt(0)
                    },
                    modifier = Modifier.zIndex(1000f)
                )
            }
        }
        
        // Quest notifications (below challenge notifications)
        Box(
            modifier = Modifier.fillMaxSize().padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            questNotifications.firstOrNull()?.let { notification ->
                QuestNotification(
                    questName = notification.questName,
                    objectiveDescription = notification.objectiveDescription,
                    isQuestComplete = notification.isQuestComplete,
                    onDismiss = {
                        questNotifications.removeAt(0)
                    },
                    modifier = Modifier.zIndex(999f)
                )
            }
        }
        
        // Floating XP text (center of screen)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            floatingXPTexts.firstOrNull()?.let { xpData ->
                FloatingXPText(
                    amount = xpData.amount,
                    onDismiss = {
                        floatingXPTexts.removeAt(0)
                    },
                    modifier = Modifier.zIndex(998f)
                )
            }
        }
        
        // Recipe notifications (below quest notifications)
        Box(
            modifier = Modifier.fillMaxSize().padding(top = 200.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            recipeNotifications.firstOrNull()?.let { notification ->
                RecipeNotification(
                    recipeName = notification.recipeName,
                    recipeId = notification.recipeId,
                    onDismiss = {
                        recipeNotifications.removeAt(0)
                    },
                    modifier = Modifier.zIndex(997f)
                )
            }
        }
    }
}

/**
 * Renders the tile map with camera, lighting, fog of war, POI markers, and world items.
 */
@Composable
fun TileMapView(
    map: TileMap,
    playerPosition: TileCoordinate,
    selectedPath: List<TileCoordinate>?,
    discoveredTiles: Set<TileCoordinate>,
    cameraZoom: Float = 1f,
    worldItemManager: WorldItemManager,
    onTileClick: (TileCoordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    val baseTileSize = 54f
    val tileSize = baseTileSize * cameraZoom
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val scope = rememberCoroutineScope()
    
    // Track items at each visible coordinate
    var worldItems by remember { mutableStateOf<Map<TileCoordinate, Boolean>>(emptyMap()) }
    
    // Update world items map when player moves
    LaunchedEffect(playerPosition, discoveredTiles) {
        scope.launch {
            val newItemsMap = mutableMapOf<TileCoordinate, Boolean>()
            discoveredTiles.forEach { coord ->
                if (worldItemManager.hasItemsAt(coord)) {
                    newItemsMap[coord] = true
                }
            }
            worldItems = newItemsMap
        }
    }
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .pointerInput(tileSize, playerPosition, canvasSize) {
                detectTapGestures { offset ->
                    // Only process clicks if canvas has been sized
                    if (canvasSize.width == 0f || canvasSize.height == 0f) return@detectTapGestures
                    
                    val tilesWide = (canvasSize.width / tileSize).toInt()
                    val tilesHigh = (canvasSize.height / tileSize).toInt()
                    
                    // Always center camera on player
                    val cameraX = playerPosition.x - tilesWide / 2
                    val cameraY = playerPosition.y - tilesHigh / 2
                    
                    // Calculate which tile was clicked
                    val clickedScreenTileX = (offset.x / tileSize).toInt()
                    val clickedScreenTileY = (offset.y / tileSize).toInt()
                    
                    val tileX = clickedScreenTileX + cameraX
                    val tileY = clickedScreenTileY + cameraY
                    
                    println("Click: screen($clickedScreenTileX, $clickedScreenTileY) camera($cameraX, $cameraY) world($tileX, $tileY)")
                    
                    if (map.isInBounds(tileX, tileY)) {
                        onTileClick(TileCoordinate(tileX, tileY))
                    }
                }
            }
    ) {
        canvasSize = size
        
        val tilesWide = (size.width / tileSize).toInt()
        val tilesHigh = (size.height / tileSize).toInt()
        
        // Always center camera on player (no edge constraints)
        val cameraX = playerPosition.x - tilesWide / 2
        val cameraY = playerPosition.y - tilesHigh / 2
        
        // Render tiles with proper bounds checking for negative camera positions
        for (y in 0..tilesHigh) {
            for (x in 0..tilesWide) {
                val worldX = x + cameraX
                val worldY = y + cameraY
                
                // Skip if outside map bounds
                if (!map.isInBounds(worldX, worldY)) continue
                
                val tile = map.getTileAt(worldX, worldY) ?: continue
                val coord = TileCoordinate(worldX, worldY)
                
                val screenX = x * tileSize
                val screenY = y * tileSize
                
                if (!discoveredTiles.contains(coord)) continue
                
                val baseColor = when (tile.terrainType) {
                    TerrainType.STONE -> if (tile.isWalkable) Color(0xFFa0a0a0) else Color(0xFF808080)
                    TerrainType.GRASS -> Color(0xFF4a9a4a)
                    TerrainType.DIRT -> Color(0xFFc8a870)
                    TerrainType.WATER -> Color(0xFF3a8ace)
                    TerrainType.SAND -> Color(0xFFf4e4c8)
                    TerrainType.MUD -> Color(0xFF8c7a6a)
                    TerrainType.WOOD_FLOOR -> Color(0xFFcb8a4b)
                    TerrainType.CARPET -> Color(0xFFcb4b4b)
                    TerrainType.TILE_FLOOR -> Color(0xFFececec)
                    TerrainType.GRAVEL -> Color(0xFF9a9a9a)
                    TerrainType.SNOW -> Color(0xFFffffff)
                    TerrainType.ICE -> Color(0xFFe0f8ff)
                }
                
                val lightedColor = applyLighting(baseColor, tile.lightLevel)
                
                drawRect(color = lightedColor, topLeft = Offset(screenX, screenY), size = Size(tileSize, tileSize))
                drawRect(color = Color(0x22000000), topLeft = Offset(screenX, screenY), size = Size(tileSize, tileSize), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f))
                
                if (selectedPath?.contains(coord) == true) {
                    drawRect(color = Color(0x4400ff00), topLeft = Offset(screenX, screenY), size = Size(tileSize, tileSize))
                }
                
                // Draw POI markers with recognizable icons
                if (tile.poiType != POIType.NONE) {
                    drawPOIIcon(
                        poiType = tile.poiType,
                        centerX = screenX + tileSize / 2,
                        centerY = screenY + tileSize / 2,
                        size = tileSize * 0.6f
                    )
                }
                
                // Draw world items (loot drops from enemies or placed items)
                if (worldItems[coord] == true) {
                    val itemColor = Color(0xFFffdd00) // Gold color for loot
                    
                    // Draw sparkle effect
                    drawCircle(
                        color = itemColor.copy(alpha = 0.6f),
                        radius = tileSize * 0.4f,
                        center = Offset(screenX + tileSize / 2, screenY + tileSize / 2)
                    )
                    drawCircle(
                        color = itemColor,
                        radius = tileSize * 0.3f,
                        center = Offset(screenX + tileSize / 2, screenY + tileSize / 2)
                    )
                    
                    // Draw small diamond shape in center
                    drawCircle(
                        color = Color.White,
                        radius = tileSize * 0.15f,
                        center = Offset(screenX + tileSize / 2, screenY + tileSize / 2)
                    )
                }
            }
        }
        
        val playerScreenX = (playerPosition.x - cameraX) * tileSize
        val playerScreenY = (playerPosition.y - cameraY) * tileSize
        
        drawCircle(color = Color(0xFFffffff), radius = tileSize * 0.4f, center = Offset(playerScreenX + tileSize / 2, playerScreenY + tileSize / 2))
        drawCircle(color = Color(0xFF0088ff), radius = tileSize * 0.3f, center = Offset(playerScreenX + tileSize / 2, playerScreenY + tileSize / 2))
    }
}

/**
 * Draws a recognizable icon for each POI type.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPOIIcon(
    poiType: POIType,
    centerX: Float,
    centerY: Float,
    size: Float
) {
    val halfSize = size / 2f
    
    when (poiType) {
        POIType.EXIT, POIType.ENTRANCE -> {
            // Door icon - rectangle with arch
            val doorColor = if (poiType == POIType.EXIT) Color(0xFF00ff00) else Color(0xFF00aaff)
            val doorWidth = size * 0.6f
            val doorHeight = size * 0.8f
            
            // Door frame
            drawRect(
                color = Color(0xFF8b4513),
                topLeft = Offset(centerX - doorWidth / 2, centerY - doorHeight / 2),
                size = Size(doorWidth, doorHeight)
            )
            
            // Door interior
            drawRect(
                color = doorColor,
                topLeft = Offset(centerX - doorWidth / 2 + 2, centerY - doorHeight / 2 + 2),
                size = Size(doorWidth - 4, doorHeight - 4)
            )
            
            // Door handle
            drawCircle(
                color = Color(0xFFffd700),
                radius = size * 0.08f,
                center = Offset(centerX + doorWidth / 4, centerY)
            )
        }
        
        POIType.NPC -> {
            // Person silhouette - circle head + rectangle body
            val npcColor = Color(0xFFffff00)
            
            // Head
            drawCircle(
                color = npcColor,
                radius = size * 0.2f,
                center = Offset(centerX, centerY - size * 0.15f)
            )
            
            // Body
            drawRect(
                color = npcColor,
                topLeft = Offset(centerX - size * 0.15f, centerY),
                size = Size(size * 0.3f, size * 0.35f)
            )
        }
        
        POIType.ENEMY -> {
            // Skull icon - circle + eyes + nose
            val enemyColor = Color(0xFFff0000)
            
            // Skull outline
            drawCircle(
                color = enemyColor,
                radius = size * 0.3f,
                center = Offset(centerX, centerY)
            )
            
            // Eyes (black rectangles)
            drawRect(
                color = Color.Black,
                topLeft = Offset(centerX - size * 0.18f, centerY - size * 0.1f),
                size = Size(size * 0.12f, size * 0.15f)
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(centerX + size * 0.06f, centerY - size * 0.1f),
                size = Size(size * 0.12f, size * 0.15f)
            )
            
            // Nose triangle
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(centerX, centerY + size * 0.05f)
                    lineTo(centerX - size * 0.08f, centerY + size * 0.18f)
                    lineTo(centerX + size * 0.08f, centerY + size * 0.18f)
                    close()
                },
                color = Color.Black
            )
        }
        
        POIType.SHOP -> {
            // Shop/coin purse icon
            val shopColor = Color(0xFFffaa00)
            
            // Bag shape
            drawCircle(
                color = shopColor,
                radius = size * 0.25f,
                center = Offset(centerX, centerY + size * 0.05f)
            )
            
            // Drawstring
            drawLine(
                color = Color(0xFF8b4513),
                start = Offset(centerX - size * 0.15f, centerY - size * 0.18f),
                end = Offset(centerX + size * 0.15f, centerY - size * 0.18f),
                strokeWidth = 3f
            )
            
            // Gold coin symbol
            drawCircle(
                color = Color(0xFFffd700),
                radius = size * 0.12f,
                center = Offset(centerX, centerY + size * 0.05f)
            )
        }
        
        POIType.INN -> {
            // Bed icon
            val innColor = Color(0xFFff88ff)
            
            // Bed frame
            drawRect(
                color = Color(0xFF8b4513),
                topLeft = Offset(centerX - size * 0.3f, centerY - size * 0.1f),
                size = Size(size * 0.6f, size * 0.3f)
            )
            
            // Pillow
            drawCircle(
                color = innColor,
                radius = size * 0.15f,
                center = Offset(centerX - size * 0.15f, centerY)
            )
            
            // Blanket
            drawRect(
                color = innColor.copy(alpha = 0.7f),
                topLeft = Offset(centerX - size * 0.05f, centerY - size * 0.08f),
                size = Size(size * 0.3f, size * 0.25f)
            )
        }
        
        POIType.CRAFTING_STATION -> {
            // Anvil icon
            val craftColor = Color(0xFFaa5500)
            
            // Anvil base
            drawRect(
                color = Color(0xFF666666),
                topLeft = Offset(centerX - size * 0.25f, centerY + size * 0.15f),
                size = Size(size * 0.5f, size * 0.15f)
            )
            
            // Anvil top
            drawRect(
                color = Color(0xFF888888),
                topLeft = Offset(centerX - size * 0.2f, centerY - size * 0.1f),
                size = Size(size * 0.4f, size * 0.25f)
            )
            
            // Hammer head
            drawCircle(
                color = craftColor,
                radius = size * 0.12f,
                center = Offset(centerX + size * 0.15f, centerY - size * 0.2f)
            )
            
            // Hammer handle
            drawLine(
                color = Color(0xFF8b4513),
                start = Offset(centerX + size * 0.15f, centerY - size * 0.2f),
                end = Offset(centerX + size * 0.25f, centerY - size * 0.35f),
                strokeWidth = 3f
            )
        }
        
        POIType.QUEST_MARKER -> {
            // Exclamation mark
            val questColor = Color(0xFFaa00ff)
            
            // Background circle
            drawCircle(
                color = questColor.copy(alpha = 0.3f),
                radius = size * 0.35f,
                center = Offset(centerX, centerY)
            )
            
            // Exclamation stem
            drawRect(
                color = questColor,
                topLeft = Offset(centerX - size * 0.08f, centerY - size * 0.25f),
                size = Size(size * 0.16f, size * 0.35f)
            )
            
            // Exclamation dot
            drawCircle(
                color = questColor,
                radius = size * 0.08f,
                center = Offset(centerX, centerY + size * 0.2f)
            )
        }
        
        POIType.RESOURCE -> {
            // Tree/plant icon
            val resourceColor = Color(0xFFff8800)
            
            // Tree trunk
            drawRect(
                color = Color(0xFF8b4513),
                topLeft = Offset(centerX - size * 0.08f, centerY),
                size = Size(size * 0.16f, size * 0.3f)
            )
            
            // Tree foliage (3 circles)
            drawCircle(
                color = resourceColor,
                radius = size * 0.18f,
                center = Offset(centerX, centerY - size * 0.15f)
            )
            drawCircle(
                color = resourceColor,
                radius = size * 0.15f,
                center = Offset(centerX - size * 0.15f, centerY - size * 0.05f)
            )
            drawCircle(
                color = resourceColor,
                radius = size * 0.15f,
                center = Offset(centerX + size * 0.15f, centerY - size * 0.05f)
            )
        }
        
        POIType.ITEM -> {
            // Treasure chest icon
            val itemColor = Color(0xFFaaffff)
            
            // Chest bottom
            drawRect(
                color = Color(0xFF8b4513),
                topLeft = Offset(centerX - size * 0.25f, centerY),
                size = Size(size * 0.5f, size * 0.25f)
            )
            
            // Chest lid
            drawRect(
                color = Color(0xFFa0826d),
                topLeft = Offset(centerX - size * 0.25f, centerY - size * 0.15f),
                size = Size(size * 0.5f, size * 0.15f)
            )
            
            // Lock/clasp
            drawCircle(
                color = Color(0xFFffd700),
                radius = size * 0.08f,
                center = Offset(centerX, centerY + size * 0.05f)
            )
        }
        
        POIType.HOUSE -> {
            // House icon
            val houseColor = Color(0xFF888888)
            
            // House walls
            drawRect(
                color = houseColor,
                topLeft = Offset(centerX - size * 0.25f, centerY - size * 0.05f),
                size = Size(size * 0.5f, size * 0.35f)
            )
            
            // Roof triangle
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(centerX, centerY - size * 0.3f)
                    lineTo(centerX - size * 0.3f, centerY - size * 0.05f)
                    lineTo(centerX + size * 0.3f, centerY - size * 0.05f)
                    close()
                },
                color = Color(0xFFa0826d)
            )
            
            // Door
            drawRect(
                color = Color(0xFF8b4513),
                topLeft = Offset(centerX - size * 0.08f, centerY + size * 0.05f),
                size = Size(size * 0.16f, size * 0.25f)
            )
        }
        
        else -> {
            // Default: simple circle marker
            drawCircle(
                color = Color(0xFFaaaaaa),
                radius = size * 0.3f,
                center = Offset(centerX, centerY)
            )
        }
    }
}

private fun applyLighting(baseColor: Color, lightLevel: Int): Color {
    val factor = lightLevel / 100f
    return Color(
        red = (baseColor.red * factor).coerceIn(0f, 1f),
        green = (baseColor.green * factor).coerceIn(0f, 1f),
        blue = (baseColor.blue * factor).coerceIn(0f, 1f),
        alpha = baseColor.alpha
    )
}

@Composable
fun MiniMap(
    map: TileMap,
    playerPosition: TileCoordinate,
    discoveredTiles: Set<TileCoordinate>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xCC000000), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFF444444), RoundedCornerShape(6.dp))
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Map canvas - smaller aspect ratio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pixelsPerTile = size.width / map.width.toFloat()
                
                discoveredTiles.forEach { coord ->
                    val tile = map.getTileAt(coord.x, coord.y) ?: return@forEach
                    
                    val tileColor = when {
                        !tile.isWalkable -> Color(0xFF888888)
                        tile.poiType != POIType.NONE -> Color(0xFFffaa00)
                        tile.terrainType == TerrainType.WATER -> Color(0xFF1e5a8e)
                        else -> Color(0xFF2d5a2d)
                    }
                    
                    drawRect(color = tileColor.copy(alpha = 0.6f), topLeft = Offset(coord.x * pixelsPerTile, coord.y * pixelsPerTile), size = Size(pixelsPerTile, pixelsPerTile))
                }
                
                drawCircle(color = Color(0xFFff0000), radius = pixelsPerTile * 2f, center = Offset((playerPosition.x + 0.5f) * pixelsPerTile, (playerPosition.y + 0.5f) * pixelsPerTile))
                drawCircle(color = Color(0xFFff8888), radius = pixelsPerTile * 1.5f, center = Offset((playerPosition.x + 0.5f) * pixelsPerTile, (playerPosition.y + 0.5f) * pixelsPerTile))
            }
            
            Text("MAP", fontSize = 9.sp, color = Color.White, modifier = Modifier.align(Alignment.TopStart))
        }
        
        // Compact location information
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                map.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFCCCCCC),
                fontSize = 11.sp,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Pos: (${playerPosition.x}, ${playerPosition.y})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF888888),
                    fontSize = 9.sp
                )
                Text(
                    "${discoveredTiles.size} tiles",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF88ff88),
                    fontSize = 9.sp
                )
            }
            
            // Current tile info - very compact
            map.getTileAt(playerPosition)?.let { tile ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        tile.terrainType.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFaaaaaa),
                        fontSize = 9.sp
                    )
                    if (tile.poiType != POIType.NONE) {
                        Text(
                            tile.poiType.name.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFffaa00),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBar(label: String, current: Int, max: Int, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = Color(0xFFcccccc))
            Text("$current/$max", style = MaterialTheme.typography.bodySmall, fontSize = 8.sp, color = Color(0xFF888888))
        }
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Box(modifier = Modifier.fillMaxWidth().height(5.dp).background(Color(0xFF333333), RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(current.toFloat() / max.toFloat()).background(color, RoundedCornerShape(2.dp)))
        }
    }
}

/**
 * Battle Pass Panel Overlay
 * 
 * Shows current season, tier progress, active challenges, and rewards.
 */
@Composable
fun BattlePassPanel(
    gameStateManager: com.jalmarquest.shared.state.GameStateManager,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val gameState by gameStateManager.gameState.collectAsState()
    val currentTimeMillis = remember { System.currentTimeMillis() }
    
    // Mock Battle Pass manager for display (in full integration, get from App)
    val battlePassManager = remember {
        val springSeason = com.jalmarquest.shared.battlepass.SeasonCatalog.createSpringSeason(2025, currentTimeMillis)
        com.jalmarquest.shared.battlepass.BattlePassManager(
            seasonCatalog = com.jalmarquest.shared.battlepass.SeasonCatalog,
            activeSeason = springSeason
        )
    }
    
    val season = battlePassManager.getCurrentSeason(currentTimeMillis)
    
    // Semi-transparent overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDD000000))
            .pointerInput(Unit) {
                detectTapGestures { /* Block clicks */ }
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1a1a1a),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🏆 SEASONAL CHRONICLE",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFFFD700)
                    )
                    IconButton(onClick = onClose) {
                        Text("✕", fontSize = 24.sp, color = Color(0xFF888888))
                    }
                }
                
                HorizontalDivider(color = Color(0xFF444444), thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
                
                if (season != null) {
                    // Season info
                    Text(
                        season.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFCCCCCC)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Tier progress
                    val currentXP = gameState?.player?.seasonProgress?.currentXP ?: 0
                    val currentTier = gameState?.player?.seasonProgress?.currentTier ?: 0
                    val (tierXP, tierNeeded) = battlePassManager.calculateTierProgress(currentXP, currentTier, season)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tier $currentTier", style = MaterialTheme.typography.titleMedium, color = Color(0xFFFFFFFF))
                        Text("$currentXP XP", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF888888))
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(Color(0xFF333333), RoundedCornerShape(12.dp))
                    ) {
                        if (tierNeeded > 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(tierXP.toFloat() / tierNeeded.toFloat())
                                    .background(Color(0xFFFFD700), RoundedCornerShape(12.dp))
                            )
                        }
                        
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$tierXP / $tierNeeded XP",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Active challenges
                    Text(
                        "ACTIVE CHALLENGES",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val dailyChallenges = battlePassManager.getDailyChallenges(currentTimeMillis)
                    val weeklyChallenges = battlePassManager.getWeeklyChallenges(currentTimeMillis)
                    
                    // Scrollable challenge list
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dailyChallenges.take(3).forEach { challenge ->
                            ChallengeCard(challenge, gameState)
                        }
                        weeklyChallenges.take(2).forEach { challenge ->
                            ChallengeCard(challenge, gameState)
                        }
                    }
                    
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No active season",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF888888)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Challenge Card Component
 */
@Composable
private fun ChallengeCard(
    challenge: com.jalmarquest.shared.battlepass.Challenge,
    gameState: com.jalmarquest.shared.model.GameState?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF2a2a2a)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    challenge.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    "+${challenge.xpReward} XP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFD700)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFAAAAAA)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress for each objective
            challenge.objectives.forEach { objective ->
                val chProgress = gameState?.player?.challengeProgress?.get(challenge.id)
                val progress = chProgress?.objectiveProgress?.get(objective.id) ?: 0
                val isComplete = progress >= objective.target
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${objective.type.name.replace("_", " ")}: $progress / ${objective.target}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isComplete) Color(0xFF00FF00) else Color(0xFF888888),
                        fontSize = 11.sp
                    )
                    Text(
                        if (isComplete) "✓" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00FF00),
                        fontSize = 11.sp
                    )
                }
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0xFF444444), RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((progress.toFloat() / objective.target).coerceAtMost(1f))
                            .background(
                                if (isComplete) Color(0xFF00FF00) else Color(0xFF4a9a4a),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}
