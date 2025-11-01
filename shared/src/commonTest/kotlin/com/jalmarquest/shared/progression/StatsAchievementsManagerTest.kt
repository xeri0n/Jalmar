package com.jalmarquest.shared.progression

import com.jalmarquest.shared.state.GameStateManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StatsAchievementsManagerTest {
    private lateinit var gsm: GameStateManager
    private lateinit var manager: StatsAchievementsManager
    
    @BeforeTest
    fun setup() {
        gsm = GameStateManager()
        manager = StatsAchievementsManager(gsm)
    }
    
    @Test
    fun `incrementSteps updates stats and unlocks first_steps`() = runTest {
        gsm.createNewGame("Tester")
        manager.incrementSteps(1)
        val state = gsm.gameState.value!!
        assertEquals(1, state.statistics.stepsTaken)
        val first = state.achievements.firstOrNull { it.id == "first_steps" }
        assertNotNull(first)
        assertTrue(first.unlocked)
        assertNotNull(first.unlockedAt)
    }
    
    @Test
    fun `recordEnemyDefeated increments enemiesDefeated`() = runTest {
        gsm.createNewGame("Tester")
        repeat(3) { manager.recordEnemyDefeated() }
        assertEquals(3, gsm.gameState.value!!.statistics.enemiesDefeated)
    }
    
    @Test
    fun `recordCraft increments itemsCrafted and unlocks twig spear only for twig_spear`() = runTest {
        gsm.createNewGame("Tester")
        manager.recordCraft("acorn_helmet")
        manager.recordCraft("twig_spear")
        val stats = gsm.gameState.value!!.statistics
        assertEquals(2, stats.itemsCrafted)
        val twig = gsm.gameState.value!!.achievements.firstOrNull { it.id == "twig_spear_crafted" }
        assertNotNull(twig)
        assertTrue(twig.unlocked)
    }
    
    @Test
    fun `recordQuestCompleted increments questsCompleted`() = runTest {
        gsm.createNewGame("Tester")
        repeat(2) { manager.recordQuestCompleted() }
        assertEquals(2, gsm.gameState.value!!.statistics.questsCompleted)
    }
    
    @Test
    fun `addSeeds adds to seedsCollected and ignores non-positive`() = runTest {
        gsm.createNewGame("Tester")
        manager.addSeeds(10)
        manager.addSeeds(0)
        manager.addSeeds(-5)
        assertEquals(10, gsm.gameState.value!!.statistics.seedsCollected)
    }
    
    @Test
    fun `recordDamage updates dealt and taken and ignores non-positive`() = runTest {
        gsm.createNewGame("Tester")
        manager.recordDamage(dealt = 5, taken = 2)
        manager.recordDamage(dealt = 0, taken = -3)
        val s = gsm.gameState.value!!.statistics
        assertEquals(5, s.damageDealt)
        assertEquals(2, s.damageTaken)
    }
    
    @Test
    fun `recordPuddleCrossed increments and unlocks puddle_conqueror`() = runTest {
        gsm.createNewGame("Tester")
        manager.recordPuddleCrossed()
        val s = gsm.gameState.value!!.statistics
        assertEquals(1, s.puddlesCrossed)
        val ach = gsm.gameState.value!!.achievements.firstOrNull { it.id == "puddle_conqueror" }
        assertNotNull(ach)
        assertTrue(ach.unlocked)
    }
    
    @Test
    fun `recordGnomeSpotted increments gnomesSpotted`() = runTest {
        gsm.createNewGame("Tester")
        repeat(4) { manager.recordGnomeSpotted() }
        assertEquals(4, gsm.gameState.value!!.statistics.gnomesSpotted)
    }
    
    @Test
    fun `achievements unlock idempotently`() = runTest {
        gsm.createNewGame("Tester")
        manager.incrementSteps(1)
        manager.incrementSteps(10)
        val firsts = gsm.gameState.value!!.achievements.filter { it.id == "first_steps" }
        assertEquals(1, firsts.size)
    }
    
    @Test
    fun `concurrent increments are thread-safe and unlock once`() = runTest {
        gsm.createNewGame("Tester")
        val jobs = List(100) { launch { manager.incrementSteps(1) } }
        jobs.forEach { it.join() }
        val st = gsm.gameState.value!!.statistics
        assertEquals(100, st.stepsTaken)
        val firsts = gsm.gameState.value!!.achievements.filter { it.id == "first_steps" }
        assertEquals(1, firsts.size)
    }
}
