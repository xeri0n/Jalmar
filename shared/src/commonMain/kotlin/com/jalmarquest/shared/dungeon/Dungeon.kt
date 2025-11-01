package com.jalmarquest.shared.dungeon

import kotlinx.serialization.Serializable

/**
 * Type of room within a dungeon floor.
 */
enum class RoomType {
    ENTRANCE,    // Starting room, no enemies
    COMBAT,      // Standard combat encounter
    TREASURE,    // Guaranteed loot, no/weak enemies
    BOSS,        // Floor boss, high difficulty, rare loot
    REST,        // Safe zone, restore HP/stamina
    TRAP,        // Environmental hazard, no enemies
    PUZZLE       // Skill check, rewards on success
}

/**
 * Difficulty tier for dungeons.
 */
enum class DungeonDifficulty {
    EASY,        // Level 1-5 recommended
    MEDIUM,      // Level 6-15 recommended
    HARD,        // Level 16-30 recommended
    EXPERT,      // Level 31-45 recommended
    LEGENDARY    // Level 46-50 recommended
}

/**
 * A single room within a dungeon floor.
 * 
 * @property id Unique room identifier (e.g., "burrow_f1_r2")
 * @property name Display name (e.g., "Narrow Tunnel")
 * @property description Room narrative description
 * @property roomType Type of room (entrance, combat, treasure, boss, etc.)
 * @property enemyIds List of enemy IDs to spawn (from EnemyCatalog)
 * @property lootTableOverride Optional custom loot (null = use enemy drops)
 * @property connections List of connected room IDs (for navigation)
 * @property isCleared Whether enemies have been defeated
 */
@Serializable
data class DungeonRoom(
    val id: String,
    val name: String,
    val description: String,
    val roomType: RoomType,
    val enemyIds: List<String> = emptyList(),
    val lootTableOverride: List<String> = emptyList(), // Item IDs for guaranteed loot
    val connections: List<String> = emptyList(),
    val isCleared: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "Room ID cannot be blank" }
        require(name.isNotBlank()) { "Room name cannot be blank" }
        require(description.isNotBlank()) { "Room description cannot be blank" }
        
        // Entrance and Rest rooms should not have enemies
        if (roomType == RoomType.ENTRANCE || roomType == RoomType.REST) {
            require(enemyIds.isEmpty()) { "$roomType rooms cannot have enemies" }
        }
        
        // Boss rooms must have exactly 1 enemy
        if (roomType == RoomType.BOSS) {
            require(enemyIds.size == 1) { "Boss rooms must have exactly 1 enemy, got ${enemyIds.size}" }
        }
    }
    
    /**
     * Returns whether this room can be entered.
     * Boss rooms require floor to be cleared first.
     */
    fun canEnter(floorCleared: Boolean): Boolean {
        return when (roomType) {
            RoomType.BOSS -> floorCleared
            else -> true
        }
    }
}

/**
 * A single floor within a dungeon.
 * 
 * @property floorNumber Floor index (1-based, e.g., floor 1, floor 2)
 * @property rooms List of rooms on this floor
 * @property entranceRoomId ID of starting room for this floor
 * @property bossRoomId ID of boss room (null if no boss)
 */
@Serializable
data class DungeonFloor(
    val floorNumber: Int,
    val rooms: List<DungeonRoom>,
    val entranceRoomId: String,
    val bossRoomId: String? = null
) {
    init {
        require(floorNumber >= 1) { "Floor number must be >= 1, got $floorNumber" }
        require(rooms.isNotEmpty()) { "Floor must have at least 1 room" }
        require(rooms.any { it.id == entranceRoomId }) { "Entrance room '$entranceRoomId' not found in floor $floorNumber" }
        
        if (bossRoomId != null) {
            require(rooms.any { it.id == bossRoomId }) { "Boss room '$bossRoomId' not found in floor $floorNumber" }
            require(rooms.find { it.id == bossRoomId }?.roomType == RoomType.BOSS) { 
                "Boss room '$bossRoomId' must have type BOSS" 
            }
        }
    }
    
    /**
     * Returns whether all non-boss rooms are cleared.
     */
    fun isFloorCleared(): Boolean {
        return rooms
            .filter { it.roomType != RoomType.BOSS && it.roomType != RoomType.ENTRANCE }
            .all { it.isCleared }
    }
    
    /**
     * Finds a room by ID.
     */
    fun getRoom(roomId: String): DungeonRoom? = rooms.find { it.id == roomId }
}

/**
 * A complete dungeon with multiple floors.
 * 
 * @property id Unique dungeon identifier (e.g., "abandoned_burrow")
 * @property name Display name (e.g., "Abandoned Burrow")
 * @property description Dungeon lore/narrative
 * @property theme Visual/narrative theme (e.g., "Underground Tunnels")
 * @property difficulty Recommended level range
 * @property baseLevel Minimum enemy level (floor 1)
 * @property floorCount Total number of floors
 * @property rewardXpBonus XP multiplier for completing dungeon (1.0 = no bonus, 2.0 = double)
 * @property guaranteedLoot Items guaranteed on final boss defeat (item IDs)
 */
@Serializable
data class Dungeon(
    val id: String,
    val name: String,
    val description: String,
    val theme: String,
    val difficulty: DungeonDifficulty,
    val baseLevel: Int,
    val floorCount: Int,
    val rewardXpBonus: Float = 1.0f,
    val guaranteedLoot: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Dungeon ID cannot be blank" }
        require(name.isNotBlank()) { "Dungeon name cannot be blank" }
        require(description.isNotBlank()) { "Dungeon description cannot be blank" }
        require(theme.isNotBlank()) { "Dungeon theme cannot be blank" }
        require(baseLevel >= 1) { "Base level must be >= 1, got $baseLevel" }
        require(floorCount >= 1) { "Floor count must be >= 1, got $floorCount" }
        require(rewardXpBonus >= 0.0f) { "XP bonus must be >= 0, got $rewardXpBonus" }
    }
    
    /**
     * Calculates enemy level for a specific floor.
     * Formula: baseLevel + (floorNumber - 1)
     */
    fun getEnemyLevelForFloor(floorNumber: Int): Int {
        require(floorNumber in 1..floorCount) { "Floor number must be 1-$floorCount, got $floorNumber" }
        return baseLevel + (floorNumber - 1)
    }
    
    /**
     * Returns formatted difficulty description.
     */
    fun difficultyDescription(): String = when (difficulty) {
        DungeonDifficulty.EASY -> "Easy (Level $baseLevel-${baseLevel + 4})"
        DungeonDifficulty.MEDIUM -> "Medium (Level $baseLevel-${baseLevel + 9})"
        DungeonDifficulty.HARD -> "Hard (Level $baseLevel-${baseLevel + 14})"
        DungeonDifficulty.EXPERT -> "Expert (Level $baseLevel-${baseLevel + 14})"
        DungeonDifficulty.LEGENDARY -> "Legendary (Level $baseLevel+)"
    }
}

/**
 * Player's current state within a dungeon run.
 * 
 * @property dungeonId ID of dungeon being explored
 * @property currentFloor Current floor number (1-based)
 * @property currentRoomId ID of room player is currently in
 * @property clearedRooms Set of room IDs that have been cleared
 * @property collectedLoot Set of item IDs that have been looted
 */
@Serializable
data class DungeonProgress(
    val dungeonId: String,
    val currentFloor: Int,
    val currentRoomId: String,
    val clearedRooms: Set<String> = emptySet(),
    val collectedLoot: Set<String> = emptySet()
) {
    init {
        require(dungeonId.isNotBlank()) { "Dungeon ID cannot be blank" }
        require(currentFloor >= 1) { "Current floor must be >= 1, got $currentFloor" }
        require(currentRoomId.isNotBlank()) { "Current room ID cannot be blank" }
    }
}
