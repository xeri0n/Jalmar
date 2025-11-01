package com.jalmarquest.shared.model

import kotlin.test.*

class PlayerStatsTest {
    
    @Test
    fun `default stats should be valid`() {
        val stats = PlayerStats()
        
        assertEquals(100, stats.maxHealth)
        assertEquals(100, stats.currentHealth)
        assertFalse(stats.isDead())
        assertTrue(stats.isFullHealth())
    }
    
    @Test
    fun `healthPercentage should calculate correctly`() {
        val stats = PlayerStats(maxHealth = 100, currentHealth = 50)
        
        assertEquals(0.5f, stats.healthPercentage())
    }
    
    @Test
    fun `stats should validate current not exceeding max`() {
        assertFails {
            PlayerStats(maxHealth = 100, currentHealth = 150)
        }
    }
    
    @Test
    fun `stats should not allow negative values`() {
        assertFails {
            PlayerStats(currentHealth = -10)
        }
    }
    
    @Test
    fun `isDead should return true when health is zero`() {
        val stats = PlayerStats(currentHealth = 0)
        
        assertTrue(stats.isDead())
    }
}

class PlayerTest {
    
    @Test
    fun `default player should be valid`() {
        val player = Player(id = "test", name = "TestPlayer")
        
        assertEquals("TestPlayer", player.name)
        assertEquals(1, player.level)
        assertEquals(0, player.experience)
    }
    
    @Test
    fun `player name cannot be blank`() {
        assertFails {
            Player(id = "test", name = "")
        }
    }
    
    @Test
    fun `level must be between 1 and 50`() {
        assertFails {
            Player(id = "test", name = "Test", level = 0)
        }
        
        assertFails {
            Player(id = "test", name = "Test", level = 51)
        }
    }
    
    @Test
    fun `canLevelUp should return true when exp threshold reached`() {
        val player = Player(
            id = "test",
            name = "Test",
            level = 1,
            experience = 500
        )
        
        assertTrue(player.canLevelUp())
    }
    
    @Test
    fun `canLevelUp should return false at max level`() {
        val player = Player(
            id = "test",
            name = "Test",
            level = 50,
            experience = 999999
        )
        
        assertFalse(player.canLevelUp())
    }
    
    @Test
    fun `getExperienceForNextLevel should increase with level`() {
        val player = Player(id = "test", name = "Test", level = 1)
        
        val expForLevel2 = player.getExperienceForNextLevel()
        val playerLevel5 = player.copy(level = 5)
        val expForLevel6 = playerLevel5.getExperienceForNextLevel()
        
        assertTrue(expForLevel6 > expForLevel2)
    }
}

class PositionTest {
    
    @Test
    fun `distanceTo should calculate correctly in same location`() {
        val pos1 = Position(0, 0, "location1")
        val pos2 = Position(3, 4, "location1")
        
        assertEquals(5.0, pos2.distanceTo(pos1))
    }
    
    @Test
    fun `distanceTo should return infinity for different locations`() {
        val pos1 = Position(0, 0, "location1")
        val pos2 = Position(0, 0, "location2")
        
        assertTrue(pos1.distanceTo(pos2).isInfinite())
    }
}
