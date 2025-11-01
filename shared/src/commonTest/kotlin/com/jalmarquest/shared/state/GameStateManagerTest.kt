package com.jalmarquest.shared.state

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Position
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import kotlin.test.*

class GameStateManagerTest {
    
    private lateinit var stateManager: GameStateManager
    
    @BeforeTest
    fun setup() {
        stateManager = GameStateManager()
    }
    
    @Test
    fun `createNewGame should create valid game state`() = runTest {
        val state = stateManager.createNewGame("TestPlayer")
        
        assertEquals("TestPlayer", state.player.name)
        assertEquals(1, state.player.level)
        assertEquals(0, state.player.experience)
        assertNotNull(state.player.id)
    }
    
    @Test
    fun `loadGame should set game state`() = runTest {
        val originalState = GameState.createNew("LoadTest", "test123")
        stateManager.loadGame(originalState)
        
        assertEquals(originalState, stateManager.gameState.value)
    }
    
    @Test
    fun `updatePlayerPosition should change position`() = runTest {
        stateManager.createNewGame("TestPlayer")
        val newPosition = Position(10, 20, "new_location")
        
        stateManager.updatePlayerPosition(newPosition)
        
        assertEquals(newPosition, stateManager.gameState.value?.player?.position)
    }
    
    @Test
    fun `addExperience should increase experience`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        stateManager.addExperience(50)
        
        assertEquals(50, stateManager.gameState.value?.player?.experience)
    }
    
    @Test
    fun `addExperience should auto-level up when threshold reached`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        // Level 1 needs 400 XP to reach level 2
        stateManager.addExperience(500)
        
        val player = stateManager.gameState.value?.player
        assertEquals(2, player?.level)
        assertTrue(player?.stats?.maxHealth!! > 100)
    }
    
    @Test
    fun `addSeeds should increase currency`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        stateManager.addSeeds(100)
        
        assertEquals(100, stateManager.gameState.value?.player?.seeds)
    }
    
    @Test
    fun `removeSeeds should decrease currency if sufficient`() = runTest {
        stateManager.createNewGame("TestPlayer")
        stateManager.addSeeds(100)
        
        val success = stateManager.removeSeeds(50)
        
        assertTrue(success)
        assertEquals(50, stateManager.gameState.value?.player?.seeds)
    }
    
    @Test
    fun `removeSeeds should fail if insufficient`() = runTest {
        stateManager.createNewGame("TestPlayer")
        stateManager.addSeeds(10)
        
        val success = stateManager.removeSeeds(50)
        
        assertFalse(success)
        assertEquals(10, stateManager.gameState.value?.player?.seeds)
    }
    
    @Test
    fun `discoverLocation should add to discovered set`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        stateManager.discoverLocation("forest_01")
        stateManager.discoverLocation("cave_01")
        
        val locations = stateManager.gameState.value?.discoveredLocations
        assertTrue(locations?.contains("forest_01") == true)
        assertTrue(locations?.contains("cave_01") == true)
        assertEquals(2, locations?.size)
    }
    
    @Test
    fun `startQuest should add to active quests`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        stateManager.startQuest("quest_01")
        
        val activeQuests = stateManager.gameState.value?.activeQuests
        assertTrue(activeQuests?.contains("quest_01") == true)
    }
    
    @Test
    fun `completeQuest should move from active to completed`() = runTest {
        stateManager.createNewGame("TestPlayer")
        stateManager.startQuest("quest_01")
        
        stateManager.completeQuest("quest_01")
        
        val state = stateManager.gameState.value!!
        assertFalse(state.activeQuests.contains("quest_01"))
        assertTrue(state.completedQuests.contains("quest_01"))
    }
    
    @Test
    fun `setFlag and getFlag should work correctly`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        stateManager.setFlag("tutorial_complete", true)
        
        assertTrue(stateManager.getFlag("tutorial_complete"))
        assertFalse(stateManager.getFlag("nonexistent_flag"))
    }
    
    @Test
    fun `concurrent state updates should be thread-safe`() = runTest {
        stateManager.createNewGame("TestPlayer")
        
        // This tests the mutex protection
        val jobs = List(100) { index ->
            launch {
                stateManager.addSeeds(1)
            }
        }
        
        jobs.forEach { job -> job.join() }
        
        assertEquals(100, stateManager.gameState.value?.player?.seeds)
    }
}
