package com.jalmarquest.shared.combat

import com.jalmarquest.shared.inventory.Inventory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EnemyTest {
    
    // ========== LootDrop Tests ==========
    
    @Test
    fun `LootDrop validation requires positive quantities`() {
        assertFailsWith<IllegalArgumentException> {
            LootDrop("twig", minQuantity = 0, maxQuantity = 1, dropChance = 0.5f)
        }
        assertFailsWith<IllegalArgumentException> {
            LootDrop("twig", minQuantity = -1, maxQuantity = 1, dropChance = 0.5f)
        }
    }
    
    @Test
    fun `LootDrop validation requires max greater than or equal to min`() {
        assertFailsWith<IllegalArgumentException> {
            LootDrop("twig", minQuantity = 5, maxQuantity = 3, dropChance = 0.5f)
        }
    }
    
    @Test
    fun `LootDrop validation requires drop chance in range 0-1`() {
        assertFailsWith<IllegalArgumentException> {
            LootDrop("twig", minQuantity = 1, maxQuantity = 1, dropChance = -0.1f)
        }
        assertFailsWith<IllegalArgumentException> {
            LootDrop("twig", minQuantity = 1, maxQuantity = 1, dropChance = 1.5f)
        }
    }
    
    @Test
    fun `LootDrop allows equal min and max quantity`() {
        val drop = LootDrop("twig", minQuantity = 3, maxQuantity = 3, dropChance = 1.0f)
        assertEquals(3, drop.minQuantity)
        assertEquals(3, drop.maxQuantity)
    }
    
    // ========== LootTable Tests ==========
    
    @Test
    fun `LootTable summary shows all drops`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 1, 3, 0.7f),
                LootDrop("seed", 1, 1, 0.5f)
            )
        )
        
        val summary = lootTable.summary()
        assertTrue(summary.contains("twig"))
        assertTrue(summary.contains("70%"))
        assertTrue(summary.contains("seed"))
        assertTrue(summary.contains("50%"))
    }
    
    @Test
    fun `LootTable with no drops shows 'No loot'`() {
        val emptyTable = LootTable()
        assertEquals("No loot", emptyTable.summary())
    }
    
    // ========== Enemy Tests ==========
    
    @Test
    fun `Enemy validation requires positive maxHp`() {
        assertFailsWith<IllegalArgumentException> {
            Enemy(
                id = "test",
                name = "Test",
                description = "Test enemy",
                maxHp = 0,
                strength = 5,
                agility = 5,
                vitality = 5,
                intelligence = 5,
                luck = 5,
                baseDamage = 3,
                defense = 2,
                behaviorType = EnemyBehaviorType.AGGRESSIVE
            )
        }
    }
    
    @Test
    fun `Enemy validation requires non-negative stats`() {
        assertFailsWith<IllegalArgumentException> {
            Enemy(
                id = "test",
                name = "Test",
                description = "Test",
                maxHp = 10,
                strength = -1,
                agility = 5,
                vitality = 5,
                intelligence = 5,
                luck = 5,
                baseDamage = 3,
                defense = 2,
                behaviorType = EnemyBehaviorType.AGGRESSIVE
            )
        }
    }
    
    @Test
    fun `Enemy validation requires level at least 1`() {
        assertFailsWith<IllegalArgumentException> {
            Enemy(
                id = "test",
                name = "Test",
                description = "Test",
                maxHp = 10,
                strength = 5,
                agility = 5,
                vitality = 5,
                intelligence = 5,
                luck = 5,
                baseDamage = 3,
                defense = 2,
                behaviorType = EnemyBehaviorType.AGGRESSIVE,
                level = 0
            )
        }
    }
    
    @Test
    fun `Enemy toCombatData creates valid EnemyCombatData`() {
        val enemy = Enemy(
            id = "grasshopper",
            name = "The Hopper",
            description = "Test",
            maxHp = 25,
            strength = 4,
            agility = 14,
            vitality = 3,
            intelligence = 2,
            luck = 6,
            baseDamage = 3,
            defense = 1,
            behaviorType = EnemyBehaviorType.FLEEING
        )
        
        val combatData = enemy.toCombatData("enemy_instance_1")
        
        assertEquals("enemy_instance_1", combatData.id)
        assertEquals("The Hopper", combatData.name)
        assertEquals(25, combatData.currentHp)
        assertEquals(25, combatData.maxHp)
        assertEquals(4, combatData.strength)
        assertEquals(14, combatData.agility)
        assertEquals(3, combatData.baseDamage)
        assertEquals(1, combatData.defense)
    }
    
    @Test
    fun `Enemy statBlock contains all relevant info`() {
        val enemy = Enemy(
            id = "beetle",
            name = "Armored Titan",
            description = "Test",
            maxHp = 45,
            strength = 8,
            agility = 4,
            vitality = 12,
            intelligence = 2,
            luck = 3,
            baseDamage = 6,
            defense = 5,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            xpReward = 25,
            level = 2
        )
        
        val statBlock = enemy.statBlock()
        
        assertTrue(statBlock.contains("Armored Titan"))
        assertTrue(statBlock.contains("Level 2"))
        assertTrue(statBlock.contains("HP: 45"))
        assertTrue(statBlock.contains("ATK: 6"))
        assertTrue(statBlock.contains("DEF: 5"))
        assertTrue(statBlock.contains("AGGRESSIVE"))
        assertTrue(statBlock.contains("XP: 25"))
    }
    
    // ========== EnemyCatalog Tests ==========
    
    @Test
    fun `EnemyCatalog contains 10+ enemies`() {
        assertTrue(EnemyCatalog.allEnemies.size >= 10, "Expected at least 10 enemies, got ${EnemyCatalog.allEnemies.size}")
    }
    
    @Test
    fun `EnemyCatalog getEnemy returns correct enemy`() {
        val grasshopper = EnemyCatalog.getEnemy("grasshopper")
        assertNotNull(grasshopper)
        assertEquals("The Hopper", grasshopper.name)
        assertEquals(EnemyBehaviorType.FLEEING, grasshopper.behaviorType)
    }
    
    @Test
    fun `EnemyCatalog getEnemy returns null for invalid ID`() {
        val invalid = EnemyCatalog.getEnemy("nonexistent_enemy")
        assertEquals(null, invalid)
    }
    
    @Test
    fun `EnemyCatalog getEnemiesByLevel filters correctly`() {
        val level1Enemies = EnemyCatalog.getEnemiesByLevel(1)
        assertTrue(level1Enemies.isNotEmpty())
        assertTrue(level1Enemies.all { it.level == 1 })
    }
    
    @Test
    fun `EnemyCatalog getEnemiesByLevelRange includes range boundaries`() {
        val enemies = EnemyCatalog.getEnemiesByLevelRange(1, 3)
        assertTrue(enemies.isNotEmpty())
        assertTrue(enemies.all { it.level in 1..3 })
    }
    
    @Test
    fun `EnemyCatalog getEnemiesByBehavior filters correctly`() {
        val aggressiveEnemies = EnemyCatalog.getEnemiesByBehavior(EnemyBehaviorType.AGGRESSIVE)
        assertTrue(aggressiveEnemies.isNotEmpty())
        assertTrue(aggressiveEnemies.all { it.behaviorType == EnemyBehaviorType.AGGRESSIVE })
    }
    
    @Test
    fun `All catalog enemies have valid loot tables`() {
        EnemyCatalog.allEnemies.forEach { enemy ->
            enemy.lootTable.drops.forEach { drop ->
                assertTrue(drop.minQuantity >= 1, "${enemy.id} has invalid loot drop min quantity")
                assertTrue(drop.maxQuantity >= drop.minQuantity, "${enemy.id} has invalid loot drop max quantity")
                assertTrue(drop.dropChance in 0.0..1.0, "${enemy.id} has invalid drop chance")
            }
        }
    }
    
    @Test
    fun `All catalog enemies have non-zero XP rewards`() {
        EnemyCatalog.allEnemies.forEach { enemy ->
            assertTrue(enemy.xpReward > 0, "${enemy.id} has no XP reward")
        }
    }
    
    // ========== EnemyAI Tests ==========
    
    private fun createTestCombatState(
        enemyHp: Int = 50,
        enemyMaxHp: Int = 50,
        enemyId: String = "enemy1",
        statusEffects: List<StatusEffect> = emptyList()
    ): CombatState {
        val player = PlayerCombatData(
            id = "player1",
            name = "Jalmar",
            currentHp = 100,
            maxHp = 100,
            strength = 10,
            agility = 10,
            vitality = 10,
            intelligence = 5,
            luck = 5
        )
        
        val enemy = EnemyCombatData(
            id = enemyId,
            name = "Test Enemy",
            currentHp = enemyHp,
            maxHp = enemyMaxHp,
            strength = 8,
            agility = 8,
            vitality = 8,
            intelligence = 3,
            luck = 3,
            baseDamage = 5,
            defense = 2,
            activeStatusEffects = statusEffects
        )
        
        return CombatState(
            combatId = "test_combat",
            player = player,
            enemies = listOf(enemy),
            turnOrder = listOf("player1", enemyId)
        )
    }
    
    @Test
    fun `EnemyAI Aggressive always attacks`() {
        val state = createTestCombatState()
        
        repeat(10) {
            val action = EnemyAI.decideAction("enemy1", EnemyBehaviorType.AGGRESSIVE, state)
            assertTrue(action is CombatAction.Attack)
        }
    }
    
    @Test
    fun `EnemyAI Defensive attacks when HP above 50 percent`() {
        val state = createTestCombatState(enemyHp = 30, enemyMaxHp = 50) // 60% HP
        
        val action = EnemyAI.decideAction("enemy1", EnemyBehaviorType.DEFENSIVE, state)
        assertTrue(action is CombatAction.Attack)
    }
    
    @Test
    fun `EnemyAI Fleeing flees when HP below 30 percent`() {
        val state = createTestCombatState(enemyHp = 10, enemyMaxHp = 50) // 20% HP
        
        val action = EnemyAI.decideAction("enemy1", EnemyBehaviorType.FLEEING, state)
        assertTrue(action is CombatAction.Flee)
    }
    
    @Test
    fun `EnemyAI Fleeing attacks when HP above 30 percent`() {
        val state = createTestCombatState(enemyHp = 20, enemyMaxHp = 50) // 40% HP
        
        val action = EnemyAI.decideAction("enemy1", EnemyBehaviorType.FLEEING, state)
        assertTrue(action is CombatAction.Attack)
    }
    
    @Test
    fun `EnemyAI Random chooses varied actions`() {
        val state = createTestCombatState()
        
        val actions = mutableSetOf<String>()
        repeat(20) {
            val action = EnemyAI.decideAction("enemy1", EnemyBehaviorType.RANDOM, state)
            actions.add(action::class.simpleName ?: "")
        }
        
        // Should see both Attack and Flee in 20 rolls (highly probable)
        assertTrue(actions.size > 1, "Random AI should produce varied actions")
    }
    
    @Test
    fun `EnemyAI shouldFlee returns true when HP critical`() {
        val state = createTestCombatState(enemyHp = 5, enemyMaxHp = 50) // 10% HP
        val enemy = state.enemies[0]
        
        assertTrue(EnemyAI.shouldFlee(enemy, state))
    }
    
    @Test
    fun `EnemyAI shouldFlee returns false when HP healthy`() {
        val state = createTestCombatState(enemyHp = 40, enemyMaxHp = 50) // 80% HP
        val enemy = state.enemies[0]
        
        assertFalse(EnemyAI.shouldFlee(enemy, state))
    }
    
    // ========== LootSystem Tests ==========
    
    @Test
    fun `LootSystem generateLoot with 100 percent drop chance always drops`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 1, 1, 1.0f)
            )
        )
        
        val result = LootSystem.generateLoot(lootTable)
        
        assertTrue(result is LootResult.Success)
        val success = result as LootResult.Success
        assertEquals(1, success.itemsDropped.size)
        assertEquals("twig", success.itemsDropped[0].first)
        assertEquals(1, success.itemsDropped[0].second)
    }
    
    @Test
    fun `LootSystem generateLoot with 0 percent drop chance never drops`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 1, 1, 0.0f)
            )
        )
        
        val result = LootSystem.generateLoot(lootTable)
        
        assertTrue(result is LootResult.NoLoot)
    }
    
    @Test
    fun `LootSystem generateLoot respects quantity range`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 3, 3, 1.0f) // Always drop exactly 3
            )
        )
        
        val result = LootSystem.generateLoot(lootTable)
        
        assertTrue(result is LootResult.Success)
        val success = result as LootResult.Success
        assertEquals(3, success.itemsDropped[0].second)
    }
    
    @Test
    fun `LootSystem generateLoot can drop multiple items`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 1, 1, 1.0f),
                LootDrop("seed", 1, 1, 1.0f)
            )
        )
        
        val result = LootSystem.generateLoot(lootTable)
        
        assertTrue(result is LootResult.Success)
        val success = result as LootResult.Success
        assertEquals(2, success.itemsDropped.size)
    }
    
    @Test
    fun `LootResult Success summary formats correctly`() {
        val result = LootResult.Success(
            listOf("twig" to 3, "seed" to 1)
        )
        
        val summary = result.summary()
        assertTrue(summary.contains("3 x twig"))
        assertTrue(summary.contains("1 x seed"))
    }
    
    @Test
    fun `LootSystem generateAndAddLoot returns loot result`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 2, 2, 1.0f)
            )
        )
        
        val inventory = Inventory(maxSlots = 10, maxWeight = 10000)
        
        val (_, result) = LootSystem.generateAndAddLoot(lootTable, inventory)
        
        assertTrue(result is LootResult.Success)
        val success = result as LootResult.Success
        assertEquals(1, success.itemsDropped.size)
        assertEquals("twig", success.itemsDropped[0].first)
        assertEquals(2, success.itemsDropped[0].second)
    }
    
    @Test
    fun `LootSystem calculateExpectedLoot returns correct averages`() {
        val lootTable = LootTable(
            drops = listOf(
                LootDrop("twig", 1, 3, 0.5f), // Average: 2 * 0.5 = 1.0
                LootDrop("seed", 5, 5, 0.2f)  // Average: 5 * 0.2 = 1.0
            )
        )
        
        val expected = LootSystem.calculateExpectedLoot(lootTable)
        
        assertEquals(1.0f, expected["twig"])
        assertEquals(1.0f, expected["seed"])
    }
    
    // ========== Integration Tests ==========
    
    @Test
    fun `Enemy from catalog can be converted to combat data`() {
        val grasshopper = EnemyCatalog.getEnemy("grasshopper")
        assertNotNull(grasshopper)
        
        val combatData = grasshopper.toCombatData("combat_grasshopper_1")
        
        assertEquals("combat_grasshopper_1", combatData.id)
        assertEquals(grasshopper.name, combatData.name)
        assertEquals(grasshopper.maxHp, combatData.maxHp)
    }
    
    @Test
    fun `Enemy AI makes appropriate decisions for catalog enemies`() {
        // Test beetle (aggressive)
        val beetle = EnemyCatalog.getEnemy("beetle")!!
        val beetleCombat = beetle.toCombatData("beetle1")
        val state1 = CombatState(
            combatId = "test",
            player = PlayerCombatData("player", "Jalmar", 100, 100, 10, 10, 10, 5, 5),
            enemies = listOf(beetleCombat),
            turnOrder = listOf("player", "beetle1")
        )
        
        val action1 = EnemyAI.decideAction("beetle1", beetle.behaviorType, state1)
        assertTrue(action1 is CombatAction.Attack, "Aggressive beetle should attack")
        
        // Test grasshopper (fleeing, low HP)
        val grasshopper = EnemyCatalog.getEnemy("grasshopper")!!
        val grasshopperCombat = grasshopper.toCombatData("grasshopper1").copy(currentHp = 5)
        val state2 = CombatState(
            combatId = "test",
            player = PlayerCombatData("player", "Jalmar", 100, 100, 10, 10, 10, 5, 5),
            enemies = listOf(grasshopperCombat),
            turnOrder = listOf("player", "grasshopper1")
        )
        
        val action2 = EnemyAI.decideAction("grasshopper1", grasshopper.behaviorType, state2)
        assertTrue(action2 is CombatAction.Flee, "Low-HP fleeing grasshopper should flee")
    }
    
    @Test
    fun `Loot can be generated from catalog enemy loot tables`() {
        val beetle = EnemyCatalog.getEnemy("beetle")!!
        
        // Run multiple times to test probabilistic drops
        var gotBeetleShell = false
        var gotTwig = false
        
        repeat(20) {
            val result = LootSystem.generateLoot(beetle.lootTable)
            if (result is LootResult.Success) {
                result.itemsDropped.forEach { (itemId, _) ->
                    when (itemId) {
                        "beetle_shell" -> gotBeetleShell = true
                        "twig" -> gotTwig = true
                    }
                }
            }
        }
        
        // With 20 attempts, should get both items (90% and 50% drop rates)
        assertTrue(gotBeetleShell, "Should have gotten beetle shell in 20 attempts (90% drop rate)")
    }
}
