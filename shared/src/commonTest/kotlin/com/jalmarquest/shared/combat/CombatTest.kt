package com.jalmarquest.shared.combat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class CombatTest {
    
    // Test helpers
    private fun createTestPlayer(
        hp: Int = 100,
        strength: Int = 10,
        agility: Int = 10,
        vitality: Int = 10,
        weaponDamage: Int = 5,
        armorDefense: Int = 3
    ) = PlayerCombatData(
        id = "player1",
        name = "Jalmar",
        currentHp = hp,
        maxHp = 100,
        strength = strength,
        agility = agility,
        vitality = vitality,
        intelligence = 5,
        luck = 5,
        weaponDamage = weaponDamage,
        armorDefense = armorDefense
    )
    
    private fun createTestEnemy(
        id: String = "enemy1",
        name: String = "Grasshopper",
        hp: Int = 50,
        strength: Int = 8,
        agility: Int = 12,
        baseDamage: Int = 4,
        defense: Int = 2
    ) = EnemyCombatData(
        id = id,
        name = name,
        currentHp = hp,
        maxHp = 50,
        strength = strength,
        agility = agility,
        vitality = 8,
        intelligence = 3,
        luck = 3,
        baseDamage = baseDamage,
        defense = defense
    )
    
    // ========== StatusEffect Tests ==========
    
    @Test
    fun `StatusEffect validation requires positive duration`() {
        assertFailsWith<IllegalArgumentException> {
            StatusEffect(StatusEffectType.POISON, remainingRounds = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            StatusEffect(StatusEffectType.POISON, remainingRounds = -1)
        }
    }
    
    @Test
    fun `StatusEffect tick decrements duration`() {
        val effect = StatusEffect(StatusEffectType.POISON, remainingRounds = 3)
        val ticked = effect.tick()
        assertNotNull(ticked)
        assertEquals(2, ticked.remainingRounds)
    }
    
    @Test
    fun `StatusEffect tick returns null when expired`() {
        val effect = StatusEffect(StatusEffectType.STUN, remainingRounds = 1)
        val ticked = effect.tick()
        assertNull(ticked)
    }
    
    @Test
    fun `StatusEffect description includes remaining rounds`() {
        val effect = StatusEffect(StatusEffectType.WEAKEN, remainingRounds = 2)
        val desc = effect.description()
        assertTrue(desc.contains("2 rounds"))
        assertTrue(desc.contains("-30% damage"))
    }
    
    // ========== CombatParticipant Tests ==========
    
    @Test
    fun `CombatParticipant isAlive returns correct status`() {
        val living = createTestPlayer(hp = 50)
        val dead = createTestPlayer(hp = 0)
        
        assertTrue(living.isAlive())
        assertFalse(dead.isAlive())
    }
    
    @Test
    fun `CombatParticipant hpPercentage calculates correctly`() {
        val player = createTestPlayer(hp = 75) // 75/100 = 0.75
        assertEquals(0.75f, player.hpPercentage())
    }
    
    @Test
    fun `CombatParticipant hasStatusEffect detects effects`() {
        val player = createTestPlayer().copy(
            activeStatusEffects = listOf(
                StatusEffect(StatusEffectType.POISON, 2),
                StatusEffect(StatusEffectType.STRENGTHEN, 3)
            )
        )
        
        assertTrue(player.hasStatusEffect(StatusEffectType.POISON))
        assertTrue(player.hasStatusEffect(StatusEffectType.STRENGTHEN))
        assertFalse(player.hasStatusEffect(StatusEffectType.BURN))
    }
    
    // ========== CombatState Tests ==========
    
    @Test
    fun `CombatState validation requires at least one enemy`() {
        val player = createTestPlayer()
        assertFailsWith<IllegalArgumentException> {
            CombatState(
                combatId = "combat1",
                player = player,
                enemies = emptyList(),
                turnOrder = listOf("player1")
            )
        }
    }
    
    @Test
    fun `CombatState isPlayerTurn correctly identifies player turn`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        val state = CombatState(
            combatId = "combat1",
            player = player,
            enemies = listOf(enemy),
            turnOrder = listOf("player1", "enemy1"),
            currentTurnIndex = 0
        )
        
        assertTrue(state.isPlayerTurn())
        
        val enemyTurnState = state.copy(currentTurnIndex = 1)
        assertFalse(enemyTurnState.isPlayerTurn())
    }
    
    @Test
    fun `CombatState isVictory returns true when all enemies dead`() {
        val player = createTestPlayer(hp = 50)
        val enemy = createTestEnemy(hp = 0)
        val state = CombatState(
            combatId = "combat1",
            player = player,
            enemies = listOf(enemy),
            turnOrder = listOf("player1", "enemy1")
        )
        
        assertTrue(state.isVictory())
        assertFalse(state.isDefeat())
        assertTrue(state.isCombatOver())
    }
    
    @Test
    fun `CombatState isDefeat returns true when player dead`() {
        val player = createTestPlayer(hp = 0)
        val enemy = createTestEnemy(hp = 30)
        val state = CombatState(
            combatId = "combat1",
            player = player,
            enemies = listOf(enemy),
            turnOrder = listOf("player1", "enemy1")
        )
        
        assertTrue(state.isDefeat())
        assertFalse(state.isVictory())
        assertTrue(state.isCombatOver())
    }
    
    @Test
    fun `CombatState livingEnemyCount counts correctly`() {
        val player = createTestPlayer()
        val enemy1 = createTestEnemy(id = "enemy1", hp = 30)
        val enemy2 = createTestEnemy(id = "enemy2", hp = 0)
        val enemy3 = createTestEnemy(id = "enemy3", hp = 20)
        
        val state = CombatState(
            combatId = "combat1",
            player = player,
            enemies = listOf(enemy1, enemy2, enemy3),
            turnOrder = listOf("player1", "enemy1", "enemy2", "enemy3")
        )
        
        assertEquals(2, state.livingEnemyCount())
    }
    
    // ========== CombatManager - Initialization Tests ==========
    
    @Test
    fun `initiateCombat creates valid combat state`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        assertEquals("combat1", state.combatId)
        assertEquals(player, state.player)
        assertEquals(1, state.enemies.size)
        assertEquals(2, state.turnOrder.size) // player + enemy
        assertEquals(1, state.roundNumber)
        assertFalse(state.isPlayerDefending)
        assertTrue(state.combatLog.isNotEmpty())
    }
    
    @Test
    fun `initiateCombat fails with no enemies`() {
        val player = createTestPlayer()
        assertFailsWith<IllegalArgumentException> {
            CombatManager.initiateCombat("combat1", player, enemies = emptyList())
        }
    }
    
    @Test
    fun `initiateCombat fails with dead player`() {
        val player = createTestPlayer(hp = 0)
        val enemy = createTestEnemy()
        assertFailsWith<IllegalArgumentException> {
            CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        }
    }
    
    @Test
    fun `initiateCombat turn order favors higher agility`() {
        val slowPlayer = createTestPlayer(agility = 5)
        val fastEnemy = createTestEnemy(agility = 20)
        
        // Run multiple times to account for randomness
        var enemyWentFirst = 0
        repeat(10) {
            val state = CombatManager.initiateCombat("combat${it}", slowPlayer, enemies = listOf(fastEnemy))
            if (state.turnOrder[0] == "enemy1") enemyWentFirst++
        }
        
        // Fast enemy should go first most of the time (at least 7/10)
        assertTrue(enemyWentFirst >= 7, "Fast enemy should usually go first, but only did $enemyWentFirst/10 times")
    }
    
    // ========== CombatManager - Damage Calculation Tests ==========
    
    @Test
    fun `calculateDamage uses weapon and strength`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10)
        val enemy = createTestEnemy(defense = 0)
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        // Expected: weaponDamage (10) + (strength * 0.5 = 5) - defense (0) = 15
        assertEquals(15, damage)
    }
    
    @Test
    fun `calculateDamage subtracts defense`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10)
        val enemy = createTestEnemy(defense = 8)
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        // Expected: 15 (from above) - 8 (defense) = 7
        assertEquals(7, damage)
    }
    
    @Test
    fun `calculateDamage minimum is 1`() {
        val player = createTestPlayer(weaponDamage = 1, strength = 0)
        val enemy = createTestEnemy(defense = 100)
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        assertEquals(1, damage, "Damage should never be less than 1")
    }
    
    @Test
    fun `calculateDamage applies STRENGTHEN modifier`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.STRENGTHEN, 2))
        )
        val enemy = createTestEnemy(defense = 0)
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        // Expected: 15 * 1.3 = 19.5 → 20 (rounded)
        assertEquals(20, damage)
    }
    
    @Test
    fun `calculateDamage applies WEAKEN modifier`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.WEAKEN, 2))
        )
        val enemy = createTestEnemy(defense = 0)
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        // Expected: 15 * 0.7 = 10.5 → 11 (rounded)
        assertEquals(11, damage)
    }
    
    @Test
    fun `calculateDamage applies VULNERABLE modifier to defender`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10)
        val enemy = createTestEnemy(defense = 8).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.VULNERABLE, 2))
        )
        
        val damage = CombatManager.calculateDamage(player, enemy)
        
        // Expected: 15 - (8 * 0.75 = 6) = 9
        assertEquals(9, damage)
    }
    
    @Test
    fun `calculateDamage applies defend modifier`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10)
        val enemy = createTestEnemy(defense = 0)
        
        val damage = CombatManager.calculateDamage(player, enemy, isDefending = true)
        
        // Expected: 15 * 0.5 = 7.5 → 8 (rounded)
        assertEquals(8, damage)
    }
    
    // ========== CombatManager - Action Execution Tests ==========
    
    @Test
    fun `executeAction Attack damages target`() {
        val player = createTestPlayer(weaponDamage = 10, strength = 10)
        val enemy = createTestEnemy(id = "enemy1", hp = 50, defense = 0)
        var state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        // Ensure it's the player's turn
        state = state.copy(
            turnOrder = listOf("player1", "enemy1"),
            currentTurnIndex = 0
        )
        
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.Attack("enemy1")
        )
        
        assertTrue(result is CombatActionResult.Success)
        assertTrue((result as CombatActionResult.Success).damageDealt > 0)
        assertTrue(newState.enemies[0].currentHp < enemy.currentHp)
    }
    
    @Test
    fun `executeAction Attack fails on dead target`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy(hp = 0)
        val state = CombatState(
            combatId = "combat1",
            player = player,
            enemies = listOf(enemy),
            turnOrder = listOf("player1", "enemy1")
        )
        
        val (_, result) = CombatManager.executeAction(
            state,
            CombatAction.Attack("enemy1")
        )
        
        assertTrue(result is CombatActionResult.Failure)
        assertEquals(CombatActionFailureReason.TARGET_ALREADY_DEAD, (result as CombatActionResult.Failure).reason)
    }
    
    @Test
    fun `executeAction Defend sets defending flag`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        var state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        // Ensure it's the player's turn
        state = state.copy(
            turnOrder = listOf("player1", "enemy1"),
            currentTurnIndex = 0
        )
        
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.Defend
        )
        
        assertTrue(result is CombatActionResult.Success)
        assertTrue(newState.isPlayerDefending)
    }
    
    @Test
    fun `executeAction fails when stunned`() {
        val player = createTestPlayer().copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.STUN, 1))
        )
        val enemy = createTestEnemy()
        var state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        // Update state with stunned player
        state = state.copy(player = player)
        
        // Ensure it's the player's turn by setting turn order and index
        state = state.copy(
            turnOrder = listOf("player1", "enemy1"),
            currentTurnIndex = 0
        )
        
        val (_, result) = CombatManager.executeAction(
            state,
            CombatAction.Attack("enemy1")
        )
        
        assertTrue(result is CombatActionResult.Failure)
        assertEquals(CombatActionFailureReason.STUNNED, (result as CombatActionResult.Failure).reason)
    }
    
    @Test
    fun `executeAction Flee has chance based on agility`() {
        val fastPlayer = createTestPlayer(agility = 30)
        val slowEnemy = createTestEnemy(agility = 5)
        val state = CombatManager.initiateCombat("combat1", fastPlayer, enemies = listOf(slowEnemy))
        
        // Run multiple flee attempts
        var successes = 0
        repeat(10) {
            val (_, result) = CombatManager.executeAction(state, CombatAction.Flee)
            if (result is CombatActionResult.Success) successes++
        }
        
        // Fast player should succeed most of the time (at least 7/10)
        assertTrue(successes >= 7, "Fast player should usually flee successfully, but only did $successes/10 times")
    }
    
    // ========== CombatManager - Turn Advancement Tests ==========
    
    @Test
    fun `advanceTurn increments turn index`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        val currentIndex = state.currentTurnIndex
        val newState = CombatManager.advanceTurn(state)
        
        assertEquals((currentIndex + 1) % 2, newState.currentTurnIndex)
    }
    
    @Test
    fun `advanceTurn wraps around and increments round`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            currentTurnIndex = 1 // Last turn in round
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        assertEquals(0, newState.currentTurnIndex)
        assertEquals(state.roundNumber + 1, newState.roundNumber)
    }
    
    @Test
    fun `advanceTurn resets defending flag`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            isPlayerDefending = true
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        assertFalse(newState.isPlayerDefending)
    }
    
    @Test
    fun `advanceTurn applies poison damage to player`() {
        val player = createTestPlayer(hp = 100).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.POISON, 2))
        )
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            currentTurnIndex = 1 // Set to last turn so next advance triggers new round
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        // Poison deals 5% of max HP = 5 damage
        assertTrue(newState.player.currentHp < 100, "Player should take poison damage")
        assertEquals(95, newState.player.currentHp)
    }
    
    @Test
    fun `advanceTurn applies burn damage to enemy`() {
        val player = createTestPlayer()
        val enemy = createTestEnemy(hp = 50).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.BURN, 2))
        )
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            enemies = listOf(enemy),
            currentTurnIndex = 1
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        // Burn deals 8% of max HP = 4 damage
        assertTrue(newState.enemies[0].currentHp < 50)
        assertEquals(46, newState.enemies[0].currentHp)
    }
    
    @Test
    fun `advanceTurn applies regeneration to player`() {
        val player = createTestPlayer(hp = 50).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.REGENERATION, 2))
        )
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            player = player,
            currentTurnIndex = 1
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        // Regeneration heals 10% of max HP = 10 healing
        assertTrue(newState.player.currentHp > 50)
        assertEquals(60, newState.player.currentHp)
    }
    
    @Test
    fun `advanceTurn ticks status effects and removes expired`() {
        val player = createTestPlayer().copy(
            activeStatusEffects = listOf(
                StatusEffect(StatusEffectType.POISON, 1), // Will expire
                StatusEffect(StatusEffectType.STRENGTHEN, 3) // Will remain
            )
        )
        val enemy = createTestEnemy()
        val state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            player = player,
            currentTurnIndex = 1
        )
        
        val newState = CombatManager.advanceTurn(state)
        
        assertEquals(1, newState.player.activeStatusEffects.size)
        assertEquals(StatusEffectType.STRENGTHEN, newState.player.activeStatusEffects[0].type)
        assertEquals(2, newState.player.activeStatusEffects[0].remainingRounds)
    }
    
    // ========== Integration Tests ==========
    
    @Test
    fun `full combat flow - player defeats enemy`() {
        val player = createTestPlayer(weaponDamage = 20, strength = 20)
        val enemy = createTestEnemy(hp = 30, defense = 0)
        var state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy))
        
        // Execute attacks until enemy is dead
        var turns = 0
        while (!state.isCombatOver() && turns < 10) {
            if (state.isPlayerTurn()) {
                val (newState, _) = CombatManager.executeAction(state, CombatAction.Attack("enemy1"))
                state = newState
            }
            state = CombatManager.advanceTurn(state)
            turns++
        }
        
        assertTrue(state.isVictory())
        assertEquals(0, state.enemies[0].currentHp)
    }
    
    @Test
    fun `full combat flow - status effects persist across rounds`() {
        val player = createTestPlayer(hp = 100).copy(
            activeStatusEffects = listOf(StatusEffect(StatusEffectType.POISON, 3))
        )
        val enemy = createTestEnemy()
        var state = CombatManager.initiateCombat("combat1", player, enemies = listOf(enemy)).copy(
            player = player,
            currentTurnIndex = 1 // Start at end of first round
        )
        
        // Advance through 3 rounds
        repeat(3) {
            state = CombatManager.advanceTurn(state)
            // Skip to end of each round
            state = state.copy(currentTurnIndex = 1)
        }
        
        // After 3 rounds, poison should have expired
        assertTrue(state.player.activeStatusEffects.isEmpty())
        // Player should have taken 5 damage per round = 15 total
        assertEquals(85, state.player.currentHp)
    }
}

