package com.jalmarquest.shared.dungeon

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DungeonTest {
    
    // ============ DungeonRoom Tests ============
    
    @Test
    fun `DungeonRoom should validate non-blank ID`() {
        try {
            DungeonRoom(
                id = "",
                name = "Test Room",
                description = "A test room",
                roomType = RoomType.COMBAT
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("ID cannot be blank"))
        }
    }
    
    @Test
    fun `DungeonRoom entrance should not allow enemies`() {
        try {
            DungeonRoom(
                id = "test_entrance",
                name = "Entrance",
                description = "Starting room",
                roomType = RoomType.ENTRANCE,
                enemyIds = listOf("enemy_1")
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("cannot have enemies"))
        }
    }
    
    @Test
    fun `DungeonRoom boss must have exactly 1 enemy`() {
        try {
            DungeonRoom(
                id = "test_boss",
                name = "Boss Room",
                description = "Boss chamber",
                roomType = RoomType.BOSS,
                enemyIds = listOf("boss_1", "boss_2") // Too many
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("exactly 1 enemy"))
        }
    }
    
    @Test
    fun `DungeonRoom boss with 1 enemy should succeed`() {
        val room = DungeonRoom(
            id = "test_boss",
            name = "Boss Room",
            description = "Boss chamber",
            roomType = RoomType.BOSS,
            enemyIds = listOf("boss_1")
        )
        
        assertEquals(RoomType.BOSS, room.roomType)
        assertEquals(1, room.enemyIds.size)
    }
    
    @Test
    fun `DungeonRoom canEnter should allow non-boss rooms always`() {
        val room = DungeonRoom(
            id = "test_combat",
            name = "Combat Room",
            description = "Fight!",
            roomType = RoomType.COMBAT,
            enemyIds = listOf("enemy_1")
        )
        
        assertTrue(room.canEnter(floorCleared = false))
        assertTrue(room.canEnter(floorCleared = true))
    }
    
    @Test
    fun `DungeonRoom canEnter should require floor cleared for boss`() {
        val room = DungeonRoom(
            id = "test_boss",
            name = "Boss Room",
            description = "Boss!",
            roomType = RoomType.BOSS,
            enemyIds = listOf("boss_1")
        )
        
        assertFalse(room.canEnter(floorCleared = false))
        assertTrue(room.canEnter(floorCleared = true))
    }
    
    // ============ DungeonFloor Tests ============
    
    @Test
    fun `DungeonFloor should validate floor number`() {
        try {
            DungeonFloor(
                floorNumber = 0,
                rooms = listOf(
                    DungeonRoom("r1", "Room 1", "Desc", RoomType.ENTRANCE)
                ),
                entranceRoomId = "r1"
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("must be >= 1"))
        }
    }
    
    @Test
    fun `DungeonFloor should validate entrance room exists`() {
        try {
            DungeonFloor(
                floorNumber = 1,
                rooms = listOf(
                    DungeonRoom("r1", "Room 1", "Desc", RoomType.ENTRANCE)
                ),
                entranceRoomId = "nonexistent"
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("not found"))
        }
    }
    
    @Test
    fun `DungeonFloor should validate boss room type`() {
        try {
            DungeonFloor(
                floorNumber = 1,
                rooms = listOf(
                    DungeonRoom("r1", "Room 1", "Desc", RoomType.ENTRANCE),
                    DungeonRoom("r2", "Room 2", "Desc", RoomType.COMBAT, enemyIds = listOf("e1"))
                ),
                entranceRoomId = "r1",
                bossRoomId = "r2" // Not a boss room!
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("must have type BOSS"))
        }
    }
    
    @Test
    fun `DungeonFloor isFloorCleared should check all combat rooms`() {
        val floor = DungeonFloor(
            floorNumber = 1,
            rooms = listOf(
                DungeonRoom("entrance", "Entrance", "Desc", RoomType.ENTRANCE),
                DungeonRoom("combat1", "Combat 1", "Desc", RoomType.COMBAT, enemyIds = listOf("e1"), isCleared = true),
                DungeonRoom("combat2", "Combat 2", "Desc", RoomType.COMBAT, enemyIds = listOf("e2"), isCleared = false),
                DungeonRoom("boss", "Boss", "Desc", RoomType.BOSS, enemyIds = listOf("b1"))
            ),
            entranceRoomId = "entrance",
            bossRoomId = "boss"
        )
        
        assertFalse(floor.isFloorCleared()) // combat2 not cleared
        
        val clearedFloor = floor.copy(
            rooms = floor.rooms.map { if (it.id == "combat2") it.copy(isCleared = true) else it }
        )
        
        assertTrue(clearedFloor.isFloorCleared())
    }
    
    @Test
    fun `DungeonFloor getRoom should find room by ID`() {
        val floor = DungeonFloor(
            floorNumber = 1,
            rooms = listOf(
                DungeonRoom("r1", "Room 1", "Desc", RoomType.ENTRANCE),
                DungeonRoom("r2", "Room 2", "Desc", RoomType.COMBAT, enemyIds = listOf("e1"))
            ),
            entranceRoomId = "r1"
        )
        
        assertNotNull(floor.getRoom("r1"))
        assertNotNull(floor.getRoom("r2"))
        assertNull(floor.getRoom("nonexistent"))
    }
    
    // ============ Dungeon Tests ============
    
    @Test
    fun `Dungeon should validate base level`() {
        try {
            Dungeon(
                id = "test",
                name = "Test",
                description = "Desc",
                theme = "Theme",
                difficulty = DungeonDifficulty.EASY,
                baseLevel = 0,
                floorCount = 3
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Base level must be >= 1"))
        }
    }
    
    @Test
    fun `Dungeon should validate floor count`() {
        try {
            Dungeon(
                id = "test",
                name = "Test",
                description = "Desc",
                theme = "Theme",
                difficulty = DungeonDifficulty.EASY,
                baseLevel = 1,
                floorCount = 0
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Floor count must be >= 1"))
        }
    }
    
    @Test
    fun `Dungeon getEnemyLevelForFloor should scale correctly`() {
        val dungeon = Dungeon(
            id = "test",
            name = "Test",
            description = "Desc",
            theme = "Theme",
            difficulty = DungeonDifficulty.EASY,
            baseLevel = 5,
            floorCount = 3
        )
        
        assertEquals(5, dungeon.getEnemyLevelForFloor(1)) // Base level
        assertEquals(6, dungeon.getEnemyLevelForFloor(2)) // Base + 1
        assertEquals(7, dungeon.getEnemyLevelForFloor(3)) // Base + 2
    }
    
    @Test
    fun `Dungeon difficultyDescription should format correctly`() {
        val easyDungeon = Dungeon(
            id = "test",
            name = "Test",
            description = "Desc",
            theme = "Theme",
            difficulty = DungeonDifficulty.EASY,
            baseLevel = 1,
            floorCount = 3
        )
        
        assertTrue(easyDungeon.difficultyDescription().contains("Easy"))
        assertTrue(easyDungeon.difficultyDescription().contains("1-5"))
    }
    
    // ============ DungeonProgress Tests ============
    
    @Test
    fun `DungeonProgress should validate dungeon ID`() {
        try {
            DungeonProgress(
                dungeonId = "",
                currentFloor = 1,
                currentRoomId = "r1"
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Dungeon ID cannot be blank"))
        }
    }
    
    @Test
    fun `DungeonProgress should validate current floor`() {
        try {
            DungeonProgress(
                dungeonId = "test",
                currentFloor = 0,
                currentRoomId = "r1"
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("must be >= 1"))
        }
    }
    
    @Test
    fun `DungeonProgress should track cleared rooms`() {
        val progress = DungeonProgress(
            dungeonId = "test",
            currentFloor = 1,
            currentRoomId = "r1",
            clearedRooms = setOf("r1", "r2", "r3")
        )
        
        assertEquals(3, progress.clearedRooms.size)
        assertTrue(progress.clearedRooms.contains("r2"))
    }
    
    // ============ DungeonCatalog Tests ============
    
    @Test
    fun `DungeonCatalog should have at least 5 dungeons`() {
        assertTrue(DungeonCatalog.getTotalDungeonCount() >= 5)
    }
    
    @Test
    fun `DungeonCatalog should have no duplicate IDs`() {
        // Should not throw
        DungeonCatalog.validateCatalog()
    }
    
    @Test
    fun `DungeonCatalog getDungeon should find by ID`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")
        assertNotNull(dungeon)
        assertEquals("Abandoned Burrow", dungeon.name)
    }
    
    @Test
    fun `DungeonCatalog getDungeon should return null for invalid ID`() {
        val dungeon = DungeonCatalog.getDungeon("nonexistent")
        assertNull(dungeon)
    }
    
    @Test
    fun `DungeonCatalog getDungeonsByDifficulty should filter correctly`() {
        val easyDungeons = DungeonCatalog.getDungeonsByDifficulty(DungeonDifficulty.EASY)
        assertTrue(easyDungeons.isNotEmpty())
        assertTrue(easyDungeons.all { it.difficulty == DungeonDifficulty.EASY })
    }
    
    @Test
    fun `DungeonCatalog getDungeonsForLevel should return appropriate dungeons`() {
        val level5Dungeons = DungeonCatalog.getDungeonsForLevel(5)
        assertTrue(level5Dungeons.isNotEmpty())
        
        // All returned dungeons should be accessible at level 5
        level5Dungeons.forEach { dungeon ->
            assertTrue(5 >= dungeon.baseLevel)
            assertTrue(5 <= dungeon.baseLevel + dungeon.floorCount)
        }
    }
    
    @Test
    fun `DungeonCatalog getDungeonsByTheme should search themes`() {
        val undergroundDungeons = DungeonCatalog.getDungeonsByTheme("underground")
        assertTrue(undergroundDungeons.isNotEmpty())
        assertTrue(undergroundDungeons.any { it.theme.contains("Underground", ignoreCase = true) })
    }
    
    // ============ DungeonGenerator Tests ============
    
    @Test
    fun `DungeonGenerator should create floor with entrance`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        val floor = DungeonGenerator.generateFloor(
            dungeon = dungeon,
            floorNumber = 1,
            enemyPool = listOf("enemy_1", "enemy_2", "boss_1"),
            random = Random(42) // Deterministic
        )
        
        assertNotNull(floor.getRoom(floor.entranceRoomId))
        assertEquals(RoomType.ENTRANCE, floor.getRoom(floor.entranceRoomId)?.roomType)
    }
    
    @Test
    fun `DungeonGenerator should create floor with boss`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        val floor = DungeonGenerator.generateFloor(
            dungeon = dungeon,
            floorNumber = 1,
            enemyPool = listOf("enemy_1", "enemy_2", "boss_1"),
            random = Random(42)
        )
        
        assertNotNull(floor.bossRoomId)
        assertNotNull(floor.getRoom(floor.bossRoomId!!))
        assertEquals(RoomType.BOSS, floor.getRoom(floor.bossRoomId!!)?.roomType)
    }
    
    @Test
    fun `DungeonGenerator should create combat rooms`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        val floor = DungeonGenerator.generateFloor(
            dungeon = dungeon,
            floorNumber = 1,
            enemyPool = listOf("enemy_1", "enemy_2", "enemy_3"),
            random = Random(42)
        )
        
        val combatRooms = floor.rooms.filter { it.roomType == RoomType.COMBAT }
        assertTrue(combatRooms.isNotEmpty())
        assertTrue(combatRooms.all { it.enemyIds.isNotEmpty() })
    }
    
    @Test
    fun `DungeonGenerator should create treasure room`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        val floor = DungeonGenerator.generateFloor(
            dungeon = dungeon,
            floorNumber = 1,
            enemyPool = listOf("enemy_1"),
            random = Random(42)
        )
        
        val treasureRooms = floor.rooms.filter { it.roomType == RoomType.TREASURE }
        assertEquals(1, treasureRooms.size)
    }
    
    @Test
    fun `DungeonGenerator should throw for invalid floor number`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        try {
            DungeonGenerator.generateFloor(
                dungeon = dungeon,
                floorNumber = 99, // Out of bounds
                enemyPool = listOf("enemy_1"),
                random = Random(42)
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Floor number must be"))
        }
    }
    
    @Test
    fun `DungeonGenerator should throw for empty enemy pool`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        try {
            DungeonGenerator.generateFloor(
                dungeon = dungeon,
                floorNumber = 1,
                enemyPool = emptyList(),
                random = Random(42)
            )
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Enemy pool cannot be empty"))
        }
    }
    
    @Test
    fun `DungeonGenerator should scale room count with floor depth`() {
        val dungeon = DungeonCatalog.getDungeon("garden_gnome_fortress")!! // 5 floors
        
        val floor1 = DungeonGenerator.generateFloor(dungeon, 1, listOf("e1", "e2"), Random(42))
        val floor5 = DungeonGenerator.generateFloor(dungeon, 5, listOf("e1", "e2"), Random(42))
        
        // Higher floors should have more combat rooms
        val floor1Combat = floor1.rooms.count { it.roomType == RoomType.COMBAT }
        val floor5Combat = floor5.rooms.count { it.roomType == RoomType.COMBAT }
        
        assertTrue(floor5Combat >= floor1Combat)
    }
    
    @Test
    fun `DungeonGenerator should use deterministic random`() {
        val dungeon = DungeonCatalog.getDungeon("abandoned_burrow")!!
        
        val floor1 = DungeonGenerator.generateFloor(dungeon, 1, listOf("e1", "e2", "e3"), Random(42))
        val floor2 = DungeonGenerator.generateFloor(dungeon, 1, listOf("e1", "e2", "e3"), Random(42))
        
        // Same seed should produce same layout
        assertEquals(floor1.rooms.size, floor2.rooms.size)
        assertEquals(floor1.rooms[0].name, floor2.rooms[0].name)
    }
}
