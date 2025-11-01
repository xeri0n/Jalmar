package com.jalmarquest.shared.dungeon

import kotlin.random.Random

/**
 * Generates dungeon floors procedurally.
 * Uses simple linear progression for now (entrance → combat → treasure → boss).
 */
object DungeonGenerator {
    
    /**
     * Generates a complete dungeon floor with rooms and connections.
     * 
     * @param dungeon The parent dungeon
     * @param floorNumber Floor number (1-based)
     * @param enemyPool List of enemy IDs available for spawning
     * @param random Random instance for deterministic generation (optional)
     * @return Generated dungeon floor
     */
    fun generateFloor(
        dungeon: Dungeon,
        floorNumber: Int,
        enemyPool: List<String>,
        random: Random = Random.Default
    ): DungeonFloor {
        require(floorNumber in 1..dungeon.floorCount) { 
            "Floor number must be 1-${dungeon.floorCount}, got $floorNumber" 
        }
        require(enemyPool.isNotEmpty()) { "Enemy pool cannot be empty" }
        
        val rooms = mutableListOf<DungeonRoom>()
        val floorId = "${dungeon.id}_f${floorNumber}"
        
        // 1. Entrance room (always first)
        val entranceId = "${floorId}_entrance"
        rooms.add(
            DungeonRoom(
                id = entranceId,
                name = "Floor $floorNumber Entrance",
                description = generateRoomDescription(dungeon.theme, RoomType.ENTRANCE, floorNumber),
                roomType = RoomType.ENTRANCE,
                enemyIds = emptyList(),
                connections = listOf("${floorId}_combat_1")
            )
        )
        
        // 2. Combat rooms (2-4 depending on floor)
        val combatRoomCount = 2 + (floorNumber / 2).coerceAtMost(2) // 2-4 rooms
        for (i in 1..combatRoomCount) {
            val roomId = "${floorId}_combat_$i"
            val nextRoom = if (i < combatRoomCount) {
                "${floorId}_combat_${i + 1}"
            } else {
                "${floorId}_treasure"
            }
            
            rooms.add(
                DungeonRoom(
                    id = roomId,
                    name = generateRoomName(dungeon.theme, RoomType.COMBAT, i),
                    description = generateRoomDescription(dungeon.theme, RoomType.COMBAT, floorNumber),
                    roomType = RoomType.COMBAT,
                    enemyIds = selectEnemies(enemyPool, 1 + random.nextInt(2), random), // 1-2 enemies
                    connections = listOf(nextRoom)
                )
            )
        }
        
        // 3. Treasure room
        val treasureId = "${floorId}_treasure"
        rooms.add(
            DungeonRoom(
                id = treasureId,
                name = "Treasure Chamber",
                description = generateRoomDescription(dungeon.theme, RoomType.TREASURE, floorNumber),
                roomType = RoomType.TREASURE,
                enemyIds = emptyList(),
                lootTableOverride = generateTreasureLoot(floorNumber),
                connections = listOf("${floorId}_boss")
            )
        )
        
        // 4. Boss room (final)
        val bossId = "${floorId}_boss"
        val bossEnemyId = selectBossEnemy(enemyPool, random)
        rooms.add(
            DungeonRoom(
                id = bossId,
                name = "Boss Chamber",
                description = generateRoomDescription(dungeon.theme, RoomType.BOSS, floorNumber),
                roomType = RoomType.BOSS,
                enemyIds = listOf(bossEnemyId),
                lootTableOverride = if (floorNumber == dungeon.floorCount) dungeon.guaranteedLoot else emptyList(),
                connections = emptyList() // Dead end
            )
        )
        
        return DungeonFloor(
            floorNumber = floorNumber,
            rooms = rooms,
            entranceRoomId = entranceId,
            bossRoomId = bossId
        )
    }
    
    /**
     * Selects random enemies from pool.
     */
    private fun selectEnemies(pool: List<String>, count: Int, random: Random): List<String> {
        val shuffled = pool.shuffled(random)
        return shuffled.take(count.coerceAtMost(pool.size))
    }
    
    /**
     * Selects a boss enemy (highest level in pool).
     */
    private fun selectBossEnemy(pool: List<String>, random: Random): String {
        // For now, just pick random. In future, could filter by level/difficulty
        return pool.random(random)
    }
    
    /**
     * Generates treasure loot based on floor depth.
     */
    private fun generateTreasureLoot(floorNumber: Int): List<String> {
        // Simple loot scaling: more floors = better loot
        return when {
            floorNumber <= 2 -> listOf("twig", "seed")
            floorNumber <= 4 -> listOf("acorn_cap", "grass_blade")
            else -> listOf("beetle_shell", "spider_silk")
        }
    }
    
    /**
     * Generates a room name based on theme and type.
     */
    private fun generateRoomName(theme: String, roomType: RoomType, index: Int): String {
        return when (theme) {
            "Underground Tunnels" -> when (roomType) {
                RoomType.COMBAT -> listOf("Narrow Passage", "Earthen Chamber", "Root-Tangled Corridor")[index % 3]
                RoomType.TREASURE -> "Hidden Alcove"
                RoomType.BOSS -> "Central Burrow"
                else -> "Unknown Room"
            }
            "Decay & Decomposition" -> when (roomType) {
                RoomType.COMBAT -> listOf("Rotting Layer", "Fungal Cavern", "Larva Nest")[index % 3]
                RoomType.TREASURE -> "Decomposer's Cache"
                RoomType.BOSS -> "Heart of Decay"
                else -> "Unknown Room"
            }
            "Ceramic Stronghold" -> when (roomType) {
                RoomType.COMBAT -> listOf("Gnome's Arm", "Hollow Torso", "Ceramic Stairwell")[index % 3]
                RoomType.TREASURE -> "Gnome's Pocket"
                RoomType.BOSS -> "Gnome's Head Throne"
                else -> "Unknown Room"
            }
            "Aquatic Labyrinth" -> when (roomType) {
                RoomType.COMBAT -> listOf("Dripping Passage", "Flooded Channel", "Rusty Junction")[index % 3]
                RoomType.TREASURE -> "Drain Reservoir"
                RoomType.BOSS -> "Gutter King's Domain"
                else -> "Unknown Room"
            }
            "Rusted Ruins" -> when (roomType) {
                RoomType.COMBAT -> listOf("Toolbox Graveyard", "Nail Heap", "Collapsed Shelf")[index % 3]
                RoomType.TREASURE -> "Forgotten Toolbox"
                RoomType.BOSS -> "The Anvil Throne"
                else -> "Unknown Room"
            }
            else -> "Generic Room $index"
        }
    }
    
    /**
     * Generates a room description based on theme and type.
     */
    private fun generateRoomDescription(theme: String, roomType: RoomType, floorNumber: Int): String {
        val depthModifier = when {
            floorNumber <= 2 -> "near the surface"
            floorNumber <= 4 -> "deeper within"
            else -> "in the deepest reaches"
        }
        
        return when (theme) {
            "Underground Tunnels" -> when (roomType) {
                RoomType.ENTRANCE -> "A dimly lit tunnel entrance $depthModifier. The walls are packed earth, supported by gnarled roots."
                RoomType.COMBAT -> "A cramped passage $depthModifier. The sound of skittering echoes off the earthen walls."
                RoomType.TREASURE -> "A hidden alcove carved into the tunnel wall. Something glints in the darkness."
                RoomType.BOSS -> "A vast central chamber $depthModifier. This is clearly the lair of something powerful."
                else -> "A mysterious chamber $depthModifier."
            }
            "Decay & Decomposition" -> when (roomType) {
                RoomType.ENTRANCE -> "The outer layers of the compost heap. The smell of decay is strong but tolerable."
                RoomType.COMBAT -> "A warm, rotting layer $depthModifier. Heat rises from decomposing matter all around you."
                RoomType.TREASURE -> "A pocket of rich, composted material. The perfect spot for treasures to accumulate."
                RoomType.BOSS -> "The molten core of the heap. The heat is oppressive, and something massive stirs within."
                else -> "A layer of rotting organic matter."
            }
            "Ceramic Stronghold" -> when (roomType) {
                RoomType.ENTRANCE -> "The hollow base of the gnome statue. Ceramic walls tower overhead."
                RoomType.COMBAT -> "A narrow passage $depthModifier the gnome's interior. Beetles have fortified this area."
                RoomType.TREASURE -> "A hidden compartment in the gnome's structure. Loot has been stashed here."
                RoomType.BOSS -> "The gnome's head chamber. A throne of ceramic shards dominates the space."
                else -> "A ceramic chamber inside the gnome."
            }
            "Aquatic Labyrinth" -> when (roomType) {
                RoomType.ENTRANCE -> "The mouth of a metal gutter. Water drips steadily from above."
                RoomType.COMBAT -> "A slick metal channel $depthModifier. Murky water pools in the corners."
                RoomType.TREASURE -> "A dry alcove in the gutter system. Treasures have washed up here."
                RoomType.BOSS -> "The main drainage junction. Water cascades from multiple pipes, and something lurks in the depths."
                else -> "A metal passage filled with rainwater."
            }
            "Rusted Ruins" -> when (roomType) {
                RoomType.ENTRANCE -> "The entrance to the ruined tool shed. Rusted tools litter the ground."
                RoomType.COMBAT -> "A chamber $depthModifier filled with decaying tools. Danger lurks among the rust."
                RoomType.TREASURE -> "An old toolbox, its contents spilled across the floor. Some items remain intact."
                RoomType.BOSS -> "The heart of the ruins. A massive anvil serves as a throne for the apex predator."
                else -> "A chamber of rusted metal and rotting wood."
            }
            else -> "A nondescript room $depthModifier."
        }
    }
}
