package com.jalmar.quest.movement

import kotlinx.coroutines.test.runTest
import com.jalmar.quest.model.Player
import com.jalmar.quest.model.PlayerStats
import com.jalmar.quest.model.Position
import com.jalmar.quest.tilemap.*
import com.jalmar.quest.tilemap.catalog.TileMapCatalog
import com.jalmar.quest.tilemap.model.TileCoordinate
import kotlin.test.*

class TileMovementManagerTest {
    private lateinit var tileMapManager: TileMapManager
    private lateinit var mapTriggerManager: MapTriggerManager
    private lateinit var movementManager: TileMovementManager
    private lateinit var testPlayer: Player
    
    @BeforeTest
    fun setup() = runTest {
        tileMapManager = TileMapManager()
        mapTriggerManager = MapTriggerManager()
        movementManager = TileMovementManager(tileMapManager, mapTriggerManager)
        
        testPlayer = Player(
            id = "test",
            name = "Jalmar",
            level = 1,
            stats = PlayerStats(currentStamina = 100),
            position = Position(10, 15, "buttonburgh")
        )
        
        // Load test map
        tileMapManager.loadMap(TileMapCatalog.createButtonburghMap())
        movementManager.initializePlayer(testPlayer, "buttonburgh", 10, 15)
    }
    
    @Test
    fun `moveToTile should succeed with valid path`() = runTest {
        val result = movementManager.moveToTile(testPlayer, 12, 15)
        
        assertTrue(result is TileMovementResult.Success)
        val success = result as TileMovementResult.Success
        assertEquals(3, success.path.tiles.size) // Start, middle, end
        assertEquals(2, success.staminaCost) // 2 tiles moved
        assertEquals(3, success.timeCost) // Minimum 1 tick per tile
    }
    
    @Test
    fun `moveToTile should fail with insufficient stamina`() = runTest {
        val lowStaminaPlayer = testPlayer.copy(
            stats = PlayerStats(currentStamina = 1)
        )
        
        val result = movementManager.moveToTile(lowStaminaPlayer, 15, 15)
        
        assertTrue(result is TileMovementResult.InsufficientStamina)
        val failure = result as TileMovementResult.InsufficientStamina
        assertEquals(1, failure.current)
        assertTrue(failure.required > 1)
    }
    
    @Test
    fun `moveToTile should fail when no path exists`() = runTest {
        // Try to move to a wall
        val result = movementManager.moveToTile(testPlayer, 0, 0)
        
        assertTrue(result is TileMovementResult.NoPath)
    }
    
    @Test
    fun `moveDirection should move player one tile`() = runTest {
        val result = movementManager.moveDirection(testPlayer, GridDirection.UP)
        
        assertTrue(result is TileMovementResult.MovedToTile)
        val moved = result as TileMovementResult.MovedToTile
        assertEquals(10, moved.newPosition.x)
        assertEquals(14, moved.newPosition.y) // Moved up
        assertEquals(1, moved.staminaCost)
        assertEquals(1, moved.timeCost)
    }
    
    @Test
    fun `moveDirection should fail when blocked`() = runTest {
        // Move player near wall
        movementManager.initializePlayer(testPlayer, "buttonburgh", 1, 1)
        
        // Try to move into wall
        val result = movementManager.moveDirection(testPlayer, GridDirection.LEFT)
        
        assertTrue(result is TileMovementResult.Blocked)
    }
    
    @Test
    fun `moveDirection should detect POI`() = runTest {
        // Move player next to Inn entrance
        movementManager.initializePlayer(testPlayer, "buttonburgh", 5, 6)
        
        // Move onto Inn entrance
        val result = movementManager.moveDirection(testPlayer, GridDirection.UP)
        
        assertTrue(result is TileMovementResult.MovedWithInteraction)
        val interaction = result as TileMovementResult.MovedWithInteraction
        assertEquals("gilded_seed_inn", interaction.poiId)
        assertEquals(com.jalmar.quest.tilemap.model.POIType.ENTRANCE, interaction.poiType)
    }
    
    @Test
    fun `interactAtCurrentPosition should trigger POI`() = runTest {
        // Place player on quest giver
        movementManager.initializePlayer(testPlayer, "buttonburgh", 10, 7)
        
        val result = movementManager.interactAtCurrentPosition(testPlayer)
        
        assertTrue(result is TileMovementResult.Interaction)
        val interaction = result as TileMovementResult.Interaction
        assertEquals("elder_quill", interaction.poiId)
        assertEquals(com.jalmar.quest.tilemap.model.POIType.QUEST_GIVER, interaction.poiType)
    }
    
    @Test
    fun `handleTileReached should discover tiles`() = runTest {
        val initialDiscoveries = tileMapManager.discoveredTiles.value.size
        
        movementManager.handleTileReached(testPlayer, 18, 18)
        
        val newDiscoveries = tileMapManager.discoveredTiles.value
        assertTrue(newDiscoveries.contains(TileCoordinate(18, 18)))
        assertTrue(newDiscoveries.size > initialDiscoveries)
    }
    
    @Test
    fun `movement should respect terrain costs`() = runTest {
        // Load dungeon with mud tiles
        val dungeonMap = TileMapCatalog.createTestDungeonMap()
        tileMapManager.loadMap(dungeonMap)
        movementManager.initializePlayer(testPlayer, "test_dungeon", 6, 7)
        
        // Move onto mud (cost 3)
        val result = movementManager.moveDirection(testPlayer, GridDirection.DOWN)
        
        assertTrue(result is TileMovementResult.MovedToTile)
        val moved = result as TileMovementResult.MovedToTile
        assertEquals(3, moved.staminaCost) // Mud costs 3
    }
    
    @Test
    fun `pathfinding should work across complex terrain`() = runTest {
        val result = movementManager.moveToTile(testPlayer, 10, 7)
        
        assertTrue(result is TileMovementResult.Success)
        val success = result as TileMovementResult.Success
        assertTrue(success.path.tiles.size > 2) // Not a straight line due to obstacles
        assertTrue(success.staminaCost > 0)
    }
    
    @Test
    fun `multiple players should have separate avatars`() = runTest {
        val player2 = testPlayer.copy(id = "player2")
        
        movementManager.initializePlayer(testPlayer, "buttonburgh", 10, 15)
        movementManager.initializePlayer(player2, "buttonburgh", 5, 5)
        
        val pos1 = movementManager.getPlayerPosition(testPlayer.id)
        val pos2 = movementManager.getPlayerPosition(player2.id)
        
        assertNotNull(pos1)
        assertNotNull(pos2)
        assertNotEquals(pos1, pos2)
    }
    
    @Test
    fun `movement should fail at map boundaries`() = runTest {
        // Move to edge
        movementManager.initializePlayer(testPlayer, "buttonburgh", 1, 1)
        
        // Try to move out of bounds
        val result = movementManager.moveDirection(testPlayer, GridDirection.UP)
        assertTrue(result is TileMovementResult.Blocked) // Wall at edge
        
        val result2 = movementManager.moveDirection(testPlayer, GridDirection.LEFT)
        assertTrue(result2 is TileMovementResult.Blocked) // Wall at edge
    }
    
    @Test
    fun `quail level stupid - trying to walk into fountain`() = runTest {
        // Position next to fountain
        movementManager.initializePlayer(testPlayer, "buttonburgh", 8, 9)
        
        // Try to walk into water
        val result = movementManager.moveDirection(testPlayer, GridDirection.RIGHT)
        
        assertTrue(result is TileMovementResult.Blocked)
        val blocked = result as TileMovementResult.Blocked
        assertTrue(blocked.reason.contains("blocked", ignoreCase = true))
    }
    
    @Test
    fun `concurrent movements should be thread safe`() = runTest {
        val jobs = List(5) { index ->
            kotlinx.coroutines.launch {
                val player = testPlayer.copy(id = "player$index")
                movementManager.initializePlayer(player, "buttonburgh", 5 + index, 5)
                movementManager.moveDirection(player, GridDirection.DOWN)
            }
        }
        
        jobs.forEach { it.join() }
        // Should complete without race conditions
    }
}
