package com.jalmarquest.shared.progression

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExperienceManagerTest {
    
    // Helper to create test player
    private fun createTestPlayer(
        level: Int = 1,
        experience: Long = 0,
        maxHealth: Int = 100,
        currentHealth: Int = 100,
        attack: Int = 10,
        defense: Int = 10,
        magicPower: Int = 10,
        speed: Int = 10,
        luck: Int = 10,
        availableStatPoints: Int = 0
    ): Player {
        val stats = PlayerStats(
            maxHealth = maxHealth,
            currentHealth = currentHealth,
            maxStamina = 100,
            currentStamina = 100,
            maxMagic = 100,
            currentMagic = 100,
            attack = attack,
            defense = defense,
            magicPower = magicPower,
            speed = speed,
            luck = luck,
            availableStatPoints = availableStatPoints
        )
        return Player(
            id = "test_player",
            name = "Hero",
            level = level,
            experience = experience,
            stats = stats,
            position = Position(x = 0, y = 0, locationId = "starting_village")
        )
    }
    
    // ============ XP Calculation Tests ============
    
    @Test
    fun `calculateXpForLevel should return 0 for level 1`() {
        val xp = ExperienceManager.calculateXpForLevel(1)
        assertEquals(0L, xp)
    }
    
    @Test
    fun `calculateXpForLevel should return 400 for level 2`() {
        val xp = ExperienceManager.calculateXpForLevel(2)
        assertEquals(400L, xp) // 100 * 2^2 = 400
    }
    
    @Test
    fun `calculateXpForLevel should return 1300 for level 3`() {
        val xp = ExperienceManager.calculateXpForLevel(3)
        // Level 2: 100 * 2^2 = 400
        // Level 3: 100 * 3^2 = 900
        // Total cumulative: 400 + 900 = 1300
        assertEquals(1300L, xp)
    }
    
    @Test
    fun `calculateXpForNextLevel should return correct value for level 1`() {
        val xp = ExperienceManager.calculateXpForNextLevel(1)
        assertEquals(400L, xp) // 100 * 2^2 = 400
    }
    
    @Test
    fun `calculateXpForNextLevel should return correct value for level 5`() {
        val xp = ExperienceManager.calculateXpForNextLevel(5)
        assertEquals(3600L, xp) // 100 * 6^2 = 3600
    }
    
    @Test
    fun `calculateXpForLevel should throw for level 0`() {
        try {
            ExperienceManager.calculateXpForLevel(0)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Level must be 1-50"))
        }
    }
    
    @Test
    fun `calculateXpForLevel should throw for level 51`() {
        try {
            ExperienceManager.calculateXpForLevel(51)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Level must be 1-50"))
        }
    }
    
    // ============ XP Granting Tests ============
    
    @Test
    fun `grantXp should increase experience without level-up`() {
        val player = createTestPlayer(level = 1, experience = 0)
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 200)
        
        assertEquals(200L, updatedPlayer.experience)
        assertEquals(1, updatedPlayer.level)
        assertTrue(result is XpGrantResult.XpGained)
        assertEquals(200L, (result as XpGrantResult.XpGained).xpGained)
    }
    
    @Test
    fun `grantXp should level up when threshold reached`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 100)
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 400)
        
        assertEquals(2, updatedPlayer.level)
        assertEquals(0L, updatedPlayer.experience) // Exactly 400 = level 2, no excess
        assertTrue(result is XpGrantResult.LeveledUp)
        
        val levelUpResult = result as XpGrantResult.LeveledUp
        assertEquals(2, levelUpResult.newLevel)
        assertEquals(1, levelUpResult.levelsGained)
        assertEquals(5, levelUpResult.statPointsEarned)
        assertEquals(110, levelUpResult.newMaxHp)
    }
    
    @Test
    fun `grantXp should carry over excess XP after level-up`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 100)
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 500)
        
        assertEquals(2, updatedPlayer.level)
        assertEquals(100L, updatedPlayer.experience) // 500 - 400 = 100 excess
        assertTrue(result is XpGrantResult.LeveledUp)
    }
    
    @Test
    fun `grantXp should handle multiple level-ups in one grant`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 100)
        // Level 2 = 400 XP, Level 3 = 400 + 900 = 1300 total
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 1300)
        
        assertEquals(3, updatedPlayer.level)
        assertEquals(0L, updatedPlayer.experience)
        assertTrue(result is XpGrantResult.LeveledUp)
        
        val levelUpResult = result as XpGrantResult.LeveledUp
        assertEquals(2, levelUpResult.levelsGained)
        assertEquals(10, levelUpResult.statPointsEarned) // 2 levels * 5 points
        assertEquals(120, levelUpResult.newMaxHp) // 100 + 20
    }
    
    @Test
    fun `grantXp should heal on level-up`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 50)
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 400)
        
        assertEquals(2, updatedPlayer.level)
        assertEquals(110, updatedPlayer.stats.maxHealth)
        assertEquals(60, updatedPlayer.stats.currentHealth) // 50 + 10 HP from level-up
    }
    
    @Test
    fun `grantXp should not exceed max HP on level-up`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 95)
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 400)
        
        assertEquals(2, updatedPlayer.level)
        assertEquals(110, updatedPlayer.stats.maxHealth)
        assertEquals(105, updatedPlayer.stats.currentHealth) // 95 + 10, capped at new max would be 110, but we add 10 so it's 105
        // Actually re-reading code: newCurrentHealth = min(currentHealth + HP_PER_LEVEL, newMaxHealth)
        // So min(95 + 10, 110) = min(105, 110) = 105
        assertEquals(105, updatedPlayer.stats.currentHealth)
    }
    
    @Test
    fun `grantXp should throw for negative XP`() {
        val player = createTestPlayer()
        try {
            ExperienceManager.grantXp(player, -100)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("XP amount must be positive"))
        }
    }
    
    @Test
    fun `grantXp should throw for zero XP`() {
        val player = createTestPlayer()
        try {
            ExperienceManager.grantXp(player, 0)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("XP amount must be positive"))
        }
    }
    
    @Test
    fun `grantXp should not level up at max level`() {
        val player = createTestPlayer(level = 50, experience = 0, maxHealth = 590) // 100 + 49*10
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 1_000_000)
        
        assertEquals(50, updatedPlayer.level)
        assertEquals(1_000_000L, updatedPlayer.experience)
        assertTrue(result is XpGrantResult.XpGained)
    }
    
    // ============ Stat Allocation Tests ============
    
    @Test
    fun `allocateStat should increase attack`() {
        val player = createTestPlayer(attack = 10, availableStatPoints = 5)
        val (updatedPlayer, result) = ExperienceManager.allocateStat(player, "attack", 3)
        
        assertTrue(result is StatAllocationResult.Success)
        assertEquals(13, updatedPlayer.stats.attack)
        assertEquals(2, updatedPlayer.stats.availableStatPoints) // 5 - 3
    }
    
    @Test
    fun `allocateStat should accept short stat names`() {
        val player = createTestPlayer(speed = 10, availableStatPoints = 5)
        val (updatedPlayer, result) = ExperienceManager.allocateStat(player, "spd", 2)
        
        assertTrue(result is StatAllocationResult.Success)
        assertEquals(12, updatedPlayer.stats.speed)
    }
    
    @Test
    fun `allocateStat should work for all stats`() {
        val basePlayer = createTestPlayer(availableStatPoints = 25)
        
        // Attack
        val (p1, r1) = ExperienceManager.allocateStat(basePlayer, "atk", 5)
        assertTrue(r1 is StatAllocationResult.Success)
        assertEquals(15, p1.stats.attack)
        
        // Speed
        val (p2, r2) = ExperienceManager.allocateStat(p1, "spd", 5)
        assertTrue(r2 is StatAllocationResult.Success)
        assertEquals(15, p2.stats.speed)
        
        // Defense
        val (p3, r3) = ExperienceManager.allocateStat(p2, "def", 5)
        assertTrue(r3 is StatAllocationResult.Success)
        assertEquals(15, p3.stats.defense)
        
        // Magic Power
        val (p4, r4) = ExperienceManager.allocateStat(p3, "magic", 5)
        assertTrue(r4 is StatAllocationResult.Success)
        assertEquals(15, p4.stats.magicPower)
        
        // Luck
        val (p5, r5) = ExperienceManager.allocateStat(p4, "lck", 5)
        assertTrue(r5 is StatAllocationResult.Success)
        assertEquals(15, p5.stats.luck)
        assertEquals(0, p5.stats.availableStatPoints)
    }
    
    @Test
    fun `allocateStat should fail with insufficient points`() {
        val player = createTestPlayer(availableStatPoints = 2)
        val (_, result) = ExperienceManager.allocateStat(player, "attack", 5)
        
        assertTrue(result is StatAllocationResult.Failure)
        val failure = result as StatAllocationResult.Failure
        assertEquals(StatAllocationFailureReason.INSUFFICIENT_POINTS, failure.reason)
    }
    
    @Test
    fun `allocateStat should fail with invalid stat name`() {
        val player = createTestPlayer(availableStatPoints = 5)
        val (_, result) = ExperienceManager.allocateStat(player, "charisma", 3)
        
        assertTrue(result is StatAllocationResult.Failure)
        val failure = result as StatAllocationResult.Failure
        assertEquals(StatAllocationFailureReason.INVALID_STAT_NAME, failure.reason)
    }
    
    @Test
    fun `allocateStat should fail with negative points`() {
        val player = createTestPlayer(availableStatPoints = 5)
        val (_, result) = ExperienceManager.allocateStat(player, "attack", -3)
        
        assertTrue(result is StatAllocationResult.Failure)
        val failure = result as StatAllocationResult.Failure
        assertEquals(StatAllocationFailureReason.NEGATIVE_AMOUNT, failure.reason)
    }
    
    @Test
    fun `allocateStat should enforce stat cap at 999`() {
        val player = createTestPlayer(attack = 995, availableStatPoints = 10)
        val (_, result) = ExperienceManager.allocateStat(player, "attack", 5)
        
        assertTrue(result is StatAllocationResult.Failure)
        val failure = result as StatAllocationResult.Failure
        assertEquals(StatAllocationFailureReason.STAT_CAP_EXCEEDED, failure.reason)
    }
    
    @Test
    fun `allocateStat should allow allocation up to cap`() {
        val player = createTestPlayer(attack = 995, availableStatPoints = 10)
        val (updatedPlayer, result) = ExperienceManager.allocateStat(player, "attack", 4)
        
        assertTrue(result is StatAllocationResult.Success)
        assertEquals(999, updatedPlayer.stats.attack)
        assertEquals(6, updatedPlayer.stats.availableStatPoints)
    }
    
    @Test
    fun `allocateStat should be case-insensitive`() {
        val player = createTestPlayer(availableStatPoints = 5)
        
        val (p1, r1) = ExperienceManager.allocateStat(player, "ATTACK", 1)
        assertTrue(r1 is StatAllocationResult.Success)
        
        val (p2, r2) = ExperienceManager.allocateStat(p1, "Speed", 1)
        assertTrue(r2 is StatAllocationResult.Success)
        
        val (p3, r3) = ExperienceManager.allocateStat(p2, "DeFenSe", 1)
        assertTrue(r3 is StatAllocationResult.Success)
    }
    
    // ============ Utility Function Tests ============
    
    @Test
    fun `getLevelProgress should return 0 for level 1 with 0 XP`() {
        val player = createTestPlayer(level = 1, experience = 0)
        val progress = ExperienceManager.getLevelProgress(player)
        assertEquals(0.0f, progress)
    }
    
    @Test
    fun `getLevelProgress should return 0_5 for halfway to next level`() {
        val player = createTestPlayer(level = 1, experience = 200) // 200 / 400 = 0.5
        val progress = ExperienceManager.getLevelProgress(player)
        assertEquals(0.5f, progress, 0.001f)
    }
    
    @Test
    fun `getLevelProgress should return 1_0 at max level`() {
        val player = createTestPlayer(level = 50, experience = 0)
        val progress = ExperienceManager.getLevelProgress(player)
        assertEquals(1.0f, progress)
    }
    
    @Test
    fun `getLevelProgress should handle level 2+ correctly`() {
        // At level 2, need 900 XP to reach level 3 (cost for L3)
        // But player already has 400 XP (cumulative to reach L2)
        // So progress = (current - 400) / (900 - 400) = (current - 400) / 500
        
        // If player has experience = 400 (exactly at level 2 threshold), progress = 0
        val player = createTestPlayer(level = 2, experience = 400)
        val progress = ExperienceManager.getLevelProgress(player)
        assertEquals(0.0f, progress, 0.001f)
        
        // At experience = 650 (400 + 250), progress = 250/500 = 0.5
        val player2 = createTestPlayer(level = 2, experience = 650)
        val progress2 = ExperienceManager.getLevelProgress(player2)
        assertEquals(0.5f, progress2, 0.01f)
    }
    
    @Test
    fun `getTotalStatPointsForLevel should return 0 for level 1`() {
        val points = ExperienceManager.getTotalStatPointsForLevel(1)
        assertEquals(0, points)
    }
    
    @Test
    fun `getTotalStatPointsForLevel should return 5 for level 2`() {
        val points = ExperienceManager.getTotalStatPointsForLevel(2)
        assertEquals(5, points)
    }
    
    @Test
    fun `getTotalStatPointsForLevel should return 245 for level 50`() {
        val points = ExperienceManager.getTotalStatPointsForLevel(50)
        assertEquals(245, points) // (50 - 1) * 5 = 245
    }
    
    @Test
    fun `getTotalStatPointsForLevel should throw for invalid levels`() {
        try {
            ExperienceManager.getTotalStatPointsForLevel(0)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Level must be 1-50"))
        }
        
        try {
            ExperienceManager.getTotalStatPointsForLevel(51)
            error("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Level must be 1-50"))
        }
    }
    
    // ============ Integration Tests ============
    
    @Test
    fun `full progression from level 1 to 3 with stat allocation`() {
        var player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 100)
        
        // Grant XP to reach level 2
        val (p1, r1) = ExperienceManager.grantXp(player, 400)
        player = p1
        assertEquals(2, player.level)
        assertEquals(5, player.stats.availableStatPoints)
        
        // Allocate stats
        val (p2, r2) = ExperienceManager.allocateStat(player, "attack", 3)
        player = p2
        assertEquals(13, player.stats.attack)
        assertEquals(2, player.stats.availableStatPoints)
        
        // Grant more XP to reach level 3
        val (p3, r3) = ExperienceManager.grantXp(player, 900)
        player = p3
        assertEquals(3, player.level)
        assertEquals(7, player.stats.availableStatPoints) // 2 + 5 from level 3
        assertEquals(120, player.stats.maxHealth) // 100 + 10 + 10
    }
    
    @Test
    fun `combat XP reward simulation`() {
        val player = createTestPlayer(level = 1, experience = 0)
        
        // Simulate defeating enemies
        val (p1, _) = ExperienceManager.grantXp(player, 25) // Grasshopper
        val (p2, _) = ExperienceManager.grantXp(p1, 30) // Beetle
        val (p3, _) = ExperienceManager.grantXp(p2, 20) // Ant
        val (p4, _) = ExperienceManager.grantXp(p3, 25) // Ladybug
        
        // Total: 100 XP, should not level up yet (need 400)
        assertEquals(1, p4.level)
        assertEquals(100L, p4.experience)
        
        // Keep fighting...
        val (p5, _) = ExperienceManager.grantXp(p4, 300)
        
        // Total: 400 XP, should level up to 2
        assertEquals(2, p5.level)
        assertEquals(0L, p5.experience)
    }
    
    @Test
    fun `rapid leveling with large XP grant`() {
        val player = createTestPlayer(level = 1, experience = 0, maxHealth = 100, currentHealth = 100)
        
        // Grant enough XP to reach level 5
        // L2 = 400, L3 = 400+900=1300, L4 = 1300+1600=2900, L5 = 2900+2500=5400
        val (updatedPlayer, result) = ExperienceManager.grantXp(player, 5400)
        
        assertEquals(5, updatedPlayer.level)
        assertEquals(0L, updatedPlayer.experience)
        assertTrue(result is XpGrantResult.LeveledUp)
        
        val levelUpResult = result as XpGrantResult.LeveledUp
        assertEquals(4, levelUpResult.levelsGained)
        assertEquals(20, levelUpResult.statPointsEarned) // 4 * 5
        assertEquals(140, levelUpResult.newMaxHp) // 100 + 40
    }
    
    @Test
    fun `calculateCombatXp should sum enemy rewards`() {
        val xp = ExperienceManager.calculateCombatXp(listOf(25, 30, 20))
        assertEquals(75L, xp)
    }
    
    @Test
    fun `calculateCombatXp should handle single enemy`() {
        val xp = ExperienceManager.calculateCombatXp(listOf(100))
        assertEquals(100L, xp)
    }
    
    @Test
    fun `calculateCombatXp should return 0 for empty list`() {
        val xp = ExperienceManager.calculateCombatXp(emptyList())
        assertEquals(0L, xp)
    }
}
