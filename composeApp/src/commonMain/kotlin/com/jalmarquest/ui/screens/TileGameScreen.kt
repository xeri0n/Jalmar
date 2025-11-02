package com.jalmarquest.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmar.quest.items.WorldItemManager
import com.jalmar.quest.npc.NPCManager
import com.jalmar.quest.tilemap.TileMapManager
import com.jalmar.quest.tilemap.model.*
import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.npc.NPC
import com.jalmarquest.ui.components.DialogueWindow
import com.jalmarquest.ui.components.InventoryPanel
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * Main tile-based game screen with dungeon crawler rendering.
 * Features fog of war, tile discovery, and OutWar-style navigation.
 */
@Composable
fun TileGameScreen(
    tileMapManager: TileMapManager,
    onBackToMenu: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val currentMap by tileMapManager.currentMap.collectAsState()
    val npcManager = remember { NPCManager() }
    val worldItemManager = remember { WorldItemManager() }
    
    var playerPosition by remember { mutableStateOf(TileCoordinate(6, 6)) }
    var selectedPath by remember { mutableStateOf<List<TileCoordinate>?>(null) }
    var discoveredTiles by remember { mutableStateOf(setOf<TileCoordinate>()) }
    var isMinimapExpanded by remember { mutableStateOf(false) }
    var isBannerExpanded by remember { mutableStateOf(false) }
    
    // NPC interaction state
    var activeNPC by remember { mutableStateOf<NPC?>(null) }
    var npcRelationship by remember { mutableStateOf(0) }
    
    // Inventory state
    var playerInventory by remember { mutableStateOf(Inventory(maxSlots = 30, maxWeight = 12000)) }
    var isInventoryOpen by remember { mutableStateOf(false) }
    var pickupMessage by remember { mutableStateOf<String?>(null) }
    
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
    
    // Stamina regeneration (1 stamina per second)
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            if (currentStamina < maxStamina) {
                currentStamina = (currentStamina + 1).coerceAtMost(maxStamina)
            }
        }
    }
    
    // Spawn some test items in the world
    LaunchedEffect(Unit) {
        worldItemManager.placeItem(TileCoordinate(8, 6), "twig", 3)
        worldItemManager.placeItem(TileCoordinate(10, 7), "acorn_cap", 2)
        worldItemManager.placeItem(TileCoordinate(5, 8), "feather", 1)
        worldItemManager.placeItem(TileCoordinate(7, 10), "pebble", 1)
    }
    
    // Auto-clear pickup message
    LaunchedEffect(pickupMessage) {
        if (pickupMessage != null) {
            kotlinx.coroutines.delay(3000)
            pickupMessage = null
        }
    }
    
    // Auto-discover tiles around player position
    LaunchedEffect(playerPosition) {
        val newDiscovered = mutableSetOf<TileCoordinate>()
        newDiscovered.addAll(discoveredTiles)
        
        currentMap?.let { map ->
            for (dy in -10..10) {
                for (dx in -10..10) {
                    val x = playerPosition.x + dx
                    val y = playerPosition.y + dy
                    if (x >= 0 && y >= 0 && x < map.width && y < map.height) {
                        newDiscovered.add(TileCoordinate(x, y))
                    }
                }
            }
        }
        discoveredTiles = newDiscovered
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // When zoom is 1x, show framed UI with side panels
        if (cameraZoom == 1f) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Panel - Character Stats & Info
                Surface(
                    modifier = Modifier.width(250.dp).fillMaxHeight(),
                    color = Color(0xFF1a1a1a)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "ADVENTURER",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFFD700)
                        )
                        HorizontalDivider(color = Color(0xFF444444))
                        
                        Text("Jalmar the Brave", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFCCCCCC))
                        Text("Level 1 Button Quail", style = MaterialTheme.typography.bodySmall, color = Color(0xFF888888))
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text("STATS", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFD700))
                        HorizontalDivider(color = Color(0xFF444444))
                        
                        StatBar("Health", currentHealth, maxHealth, Color(0xFF44ff44))
                        StatBar("Stamina", currentStamina, maxStamina, Color(0xFF44ccff))
                        StatBar("Mana", currentMana, maxMana, Color(0xFFcc44ff))
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Stamina warning
                        if (staminaWarning) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("⚠ Not enough stamina!", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFff4444))
                        }
                        
                        // Pickup message
                        if (pickupMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("✓ $pickupMessage", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF44ff44))
                        }
                        
                        // Inventory button
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { isInventoryOpen = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4a6a4a))
                        ) {
                            Text("📦 Inventory (${playerInventory.currentSlotCount()}/${playerInventory.maxSlots})")
                        }
                    }
                }
                
                // Center - Game View
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
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
                                        // Check if clicking on an NPC tile (adjacent to player)
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
                                                }
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
                                                val totalStaminaCost = path.path.sumOf { coord ->
                                                    val tile = map.getTileAt(coord.x, coord.y)
                                                    (tile?.terrainType?.movementCost ?: 1.0).toInt()
                                                }
                                                
                                                // Check if player has enough stamina
                                                if (currentStamina >= totalStaminaCost) {
                                                    selectedPath = path.path
                                                    playerPosition = clickedCoord
                                                    currentStamina = (currentStamina - totalStaminaCost).coerceAtLeast(0)
                                                    staminaWarning = false
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
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
                // Right Panel - Minimap & Controls
                Surface(
                    modifier = Modifier.width(250.dp).fillMaxHeight(),
                    color = Color(0xFF1a1a1a)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header with MAP title and hamburger menu button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("MAP", style = MaterialTheme.typography.titleMedium, color = Color(0xFFFFD700))
                            
                            // Hamburger menu button
                            IconButton(
                                onClick = onBackToMenu,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Column(
                                    modifier = Modifier.size(20.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFFFD700)))
                                }
                            }
                        }
                        HorizontalDivider(color = Color(0xFF444444))
                        
                        currentMap?.let { map ->
                            MiniMap(map = map, playerPosition = playerPosition, discoveredTiles = discoveredTiles)
                        }
                        
                        HorizontalDivider(color = Color(0xFF444444))
                        
                        Text("NEARBY", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFD700))
                        
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
                            Text("Nothing of interest nearby", style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
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
                                    Text(poiIcon, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                                    Text(
                                        "${poiType.name.replace("_", " ")} ($distance tiles)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = poiColor,
                                        fontSize = 11.sp
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
                onChoice = { choiceIndex ->
                    scope.launch {
                        // Handle dialogue choice
                        // For now, just modify relationship slightly
                        npcManager.modifyRelationship(npc.id, 5)
                        npcRelationship = npcManager.getRelationship(npc.id)
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
                onItemClick = { item ->
                    // TODO: Handle item use/equip
                },
                onClose = {
                    isInventoryOpen = false
                }
            )
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
    val baseTileSize = 32f
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
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cameraX = (playerPosition.x - (canvasSize.width / tileSize / 2)).toInt().coerceAtLeast(0)
                    val cameraY = (playerPosition.y - (canvasSize.height / tileSize / 2)).toInt().coerceAtLeast(0)
                    
                    val tileX = (offset.x / tileSize).toInt() + cameraX
                    val tileY = (offset.y / tileSize).toInt() + cameraY
                    
                    if (map.isInBounds(tileX, tileY)) {
                        onTileClick(TileCoordinate(tileX, tileY))
                    }
                }
            }
    ) {
        canvasSize = size
        
        val tilesWide = (size.width / tileSize).toInt()
        val tilesHigh = (size.height / tileSize).toInt()
        val cameraX = (playerPosition.x - tilesWide / 2).coerceAtLeast(0).coerceAtMost((map.width - tilesWide).coerceAtLeast(0))
        val cameraY = (playerPosition.y - tilesHigh / 2).coerceAtLeast(0).coerceAtMost((map.height - tilesHigh).coerceAtLeast(0))
        
        for (y in 0 until min(tilesHigh + 1, map.height - cameraY)) {
            for (x in 0 until min(tilesWide + 1, map.width - cameraX)) {
                val worldX = x + cameraX
                val worldY = y + cameraY
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
                
                // Draw POI markers (NPCs, shops, etc.)
                if (tile.poiType != POIType.NONE) {
                    val poiColor = when (tile.poiType) {
                        POIType.ENTRANCE -> Color(0xFF00aaff)
                        POIType.EXIT -> Color(0xFF00ff00)
                        POIType.NPC -> Color(0xFFffff00)
                        POIType.ENEMY -> Color(0xFFff0000)
                        POIType.QUEST_MARKER -> Color(0xFFaa00ff)
                        POIType.RESOURCE -> Color(0xFFff8800)
                        POIType.CRAFTING_STATION -> Color(0xFFaa5500)
                        POIType.SHOP -> Color(0xFFffaa00)
                        POIType.ITEM -> Color(0xFFaaffff)
                        POIType.INN -> Color(0xFFff88ff)
                        POIType.HOUSE -> Color(0xFF888888)
                        else -> Color.Gray
                    }
                    
                    drawCircle(color = poiColor.copy(alpha = 0.8f), radius = tileSize * 0.35f, center = Offset(screenX + tileSize / 2, screenY + tileSize / 2))
                    drawCircle(color = poiColor, radius = tileSize * 0.25f, center = Offset(screenX + tileSize / 2, screenY + tileSize / 2))
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
            .background(Color(0xCC000000), RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFF444444), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
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
            
            Text("MAP", fontSize = 10.sp, color = Color.White, modifier = Modifier.align(Alignment.TopStart))
        }
        
        // Compact location information
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                map.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFCCCCCC),
                fontSize = 12.sp,
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
                    fontSize = 10.sp
                )
                Text(
                    "${discoveredTiles.size} tiles",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF88ff88),
                    fontSize = 10.sp
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
                        fontSize = 10.sp
                    )
                    if (tile.poiType != POIType.NONE) {
                        Text(
                            tile.poiType.name.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFffaa00),
                            fontSize = 10.sp
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
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFFcccccc))
            Text("$current/$max", style = MaterialTheme.typography.bodySmall, color = Color(0xFF888888))
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFF333333), RoundedCornerShape(4.dp))) {
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(current.toFloat() / max.toFloat()).background(color, RoundedCornerShape(4.dp)))
        }
    }
}
