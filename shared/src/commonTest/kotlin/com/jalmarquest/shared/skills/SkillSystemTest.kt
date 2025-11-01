package com.jalmarquest.shared.skills

import com.jalmarquest.shared.combat.*
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class SkillSystemTest {
    
    private fun createTestPlayer(
        level: Int = 1,
        skillPoints: Int = 0,
        learnedSkills: Set<String> = emptySet()
    ): Player {
        return Player(
            id = "test_player",
            name = "TestHero",
            level = level,
            skillPoints = skillPoints,
            learnedSkills = learnedSkills,
            position = Position(0, 0, "test_location")
        )
    }
    
    // ============ CATALOG TESTS ============
    
    @Test
    fun `catalog should contain 57 skills`() {
        assertEquals(57, SkillCatalog.allSkills.size)
    }
    
    @Test
    fun `catalog should have no duplicate IDs`() {
        val ids = SkillCatalog.allSkills.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }
    
    @Test
    fun `each archetype should have 19 skills`() {
        assertEquals(19, SkillCatalog.getSkillsByArchetype(SkillArchetype.FIGHTER).size)
        assertEquals(19, SkillCatalog.getSkillsByArchetype(SkillArchetype.RANGER).size)
        assertEquals(19, SkillCatalog.getSkillsByArchetype(SkillArchetype.GUARDIAN).size)
    }
    
    @Test
    fun `getSkill should find skills by ID`() {
        val skill = SkillCatalog.getSkill("fighter_twig_strike")
        assertNotNull(skill)
        assertEquals("Twig Strike", skill.name)
    }
    
    @Test
    fun `getSkill should return null for invalid ID`() {
        val skill = SkillCatalog.getSkill("nonexistent_skill")
        assertEquals(null, skill)
    }
    
    // ============ SKILL MANAGER TESTS ============
    
    @Test
    fun `canLearnSkill should succeed for valid skill`() {
        val player = createTestPlayer(level = 1, skillPoints = 1)
        val result = SkillManager.canLearnSkill(player, "fighter_twig_strike")
        
        assertTrue(result is SkillLearnResult.Success)
    }
    
    @Test
    fun `canLearnSkill should fail if already learned`() {
        val player = createTestPlayer(level = 1, skillPoints = 1, learnedSkills = setOf("fighter_twig_strike"))
        val result = SkillManager.canLearnSkill(player, "fighter_twig_strike")
        
        assertTrue(result is SkillLearnResult.Failure)
        assertEquals(SkillLearnFailureReason.ALREADY_LEARNED, (result as SkillLearnResult.Failure).reason)
    }
    
    @Test
    fun `canLearnSkill should fail if level too low`() {
        val player = createTestPlayer(level = 5, skillPoints = 5)
        val result = SkillManager.canLearnSkill(player, "fighter_whirlwind_slash") // Tier 2, requires level 10
        
        assertTrue(result is SkillLearnResult.Failure)
        assertEquals(SkillLearnFailureReason.LEVEL_TOO_LOW, (result as SkillLearnResult.Failure).reason)
    }
    
    @Test
    fun `canLearnSkill should fail if insufficient skill points`() {
        val player = createTestPlayer(level = 10, skillPoints = 1)
        val result = SkillManager.canLearnSkill(player, "fighter_whirlwind_slash") // Costs 2 points
        
        assertTrue(result is SkillLearnResult.Failure)
        assertEquals(SkillLearnFailureReason.INSUFFICIENT_SKILL_POINTS, (result as SkillLearnResult.Failure).reason)
    }
    
    @Test
    fun `canLearnSkill should fail if missing prerequisites`() {
        val player = createTestPlayer(level = 10, skillPoints = 2)
        val result = SkillManager.canLearnSkill(player, "fighter_whirlwind_slash") // Requires twig_strike
        
        assertTrue(result is SkillLearnResult.Failure)
        assertEquals(SkillLearnFailureReason.MISSING_PREREQUISITES, (result as SkillLearnResult.Failure).reason)
    }
    
    @Test
    fun `learnSkill should add skill and consume points`() {
        val player = createTestPlayer(level = 1, skillPoints = 1)
        val (updatedPlayer, result) = SkillManager.learnSkill(player, "fighter_twig_strike")
        
        assertTrue(result is SkillLearnResult.Success)
        assertTrue("fighter_twig_strike" in updatedPlayer.learnedSkills)
        assertEquals(0, updatedPlayer.skillPoints)
    }
    
    @Test
    fun `learnSkill should fail if prerequisites not met`() {
        val player = createTestPlayer(level = 10, skillPoints = 2)
        val (updatedPlayer, result) = SkillManager.learnSkill(player, "fighter_whirlwind_slash")
        
        assertTrue(result is SkillLearnResult.Failure)
        assertFalse("fighter_whirlwind_slash" in updatedPlayer.learnedSkills)
        assertEquals(2, updatedPlayer.skillPoints) // Points not consumed
    }
    
    @Test
    fun `getLearnedSkills should return learned skills`() {
        val player = createTestPlayer(
            learnedSkills = setOf("fighter_twig_strike", "ranger_seed_shot")
        )
        val learned = SkillManager.getLearnedSkills(player)
        
        assertEquals(2, learned.size)
        assertTrue(learned.any { it.id == "fighter_twig_strike" })
        assertTrue(learned.any { it.id == "ranger_seed_shot" })
    }
    
    @Test
    fun `getAvailableSkills should return unlocked skills`() {
        val player = createTestPlayer(level = 10, skillPoints = 5)
        val available = SkillManager.getAvailableSkills(player, SkillArchetype.FIGHTER)
        
        // Should include tier 1 and tier 2 (level 10+)
        assertTrue(available.any { it.tier == SkillTier.TIER_1 })
        assertTrue(available.any { it.tier == SkillTier.TIER_2 })
        assertFalse(available.any { it.tier == SkillTier.TIER_3 }) // Tier 3 requires level 20
    }
    
    @Test
    fun `getLockedSkills should return level-locked skills`() {
        val player = createTestPlayer(level = 10)
        val locked = SkillManager.getLockedSkills(player, SkillArchetype.FIGHTER)
        
        // Should include tier 3+ (level 20+)
        assertTrue(locked.any { it.tier == SkillTier.TIER_3 })
        assertTrue(locked.any { it.tier == SkillTier.ULTIMATE })
    }
    
    @Test
    fun `calculateTotalSkillPoints should match level progression`() {
        assertEquals(0, SkillManager.calculateTotalSkillPoints(1))
        assertEquals(1, SkillManager.calculateTotalSkillPoints(2))
        assertEquals(9, SkillManager.calculateTotalSkillPoints(10))
        assertEquals(49, SkillManager.calculateTotalSkillPoints(50))
    }
    
    @Test
    fun `calculateSpentSkillPoints should sum spent points`() {
        val player = createTestPlayer(
            learnedSkills = setOf(
                "fighter_twig_strike", // 1 point
                "fighter_power_stance", // 1 point
                "fighter_whirlwind_slash" // 2 points
            )
        )
        val spent = SkillManager.calculateSpentSkillPoints(player)
        
        assertEquals(4, spent)
    }
    
    @Test
    fun `hasSkill should check if player has learned skill`() {
        val player = createTestPlayer(learnedSkills = setOf("fighter_twig_strike"))
        
        assertTrue(SkillManager.hasSkill(player, "fighter_twig_strike"))
        assertFalse(SkillManager.hasSkill(player, "ranger_seed_shot"))
    }
    
    // ============ COMBAT INTEGRATION TESTS ============
    
    @Test
    fun `skill with damage effect should deal damage in combat`() {
        val player = PlayerCombatData(
            id = "player1",
            name = "Hero",
            currentHp = 100,
            maxHp = 100,
            strength = 10,
            agility = 8,
            vitality = 10,
            intelligence = 5,
            luck = 5,
            weaponDamage = 5,
            armorDefense = 2,
            activeStatusEffects = emptyList()
        )
        
        val enemy = EnemyCombatData(
            id = "enemy1",
            name = "Test Enemy",
            currentHp = 50,
            maxHp = 50,
            strength = 5,
            agility = 5,
            vitality = 5,
            intelligence = 3,
            luck = 3,
            baseDamage = 5,
            defense = 3,
            activeStatusEffects = emptyList()
        )
        
        val state = CombatManager.initiateCombat("test_combat_1", player, enemies = listOf(enemy))
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.UseSkill("fighter_twig_strike", "enemy1")
        )
        
        assertTrue(result is CombatActionResult.Success)
        assertTrue((result as CombatActionResult.Success).damageDealt > 0)
        
        // Enemy should have taken damage
        val updatedEnemy = newState.enemies.find { it.id == "enemy1" }
        assertNotNull(updatedEnemy)
        assertTrue(updatedEnemy.currentHp < 50)
    }
    
    @Test
    fun `skill with heal effect should restore health`() {
        val player = PlayerCombatData(
            id = "player1",
            name = "Hero",
            currentHp = 50, // Damaged
            maxHp = 100,
            strength = 10,
            agility = 8,
            vitality = 10,
            intelligence = 5,
            luck = 5,
            weaponDamage = 5,
            armorDefense = 2,
            activeStatusEffects = emptyList()
        )
        
        val enemy = EnemyCombatData(
            id = "enemy1",
            name = "Test Enemy",
            currentHp = 50,
            maxHp = 50,
            strength = 5,
            agility = 5,
            vitality = 5,
            intelligence = 3,
            luck = 3,
            baseDamage = 5,
            defense = 3,
            activeStatusEffects = emptyList()
        )
        
        val state = CombatManager.initiateCombat("test_combat_2", player, enemies = listOf(enemy))
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.UseSkill("guardian_bark_armor", "player1") // Self-heal
        )
        
        assertTrue(result is CombatActionResult.Success)
        assertTrue((result as CombatActionResult.Success).healingDone > 0)
        
        // Player should have more health
        assertTrue(newState.player.currentHp > 50)
    }
    
    @Test
    fun `skill with status effect should apply status`() {
        val player = PlayerCombatData(
            id = "player1",
            name = "Hero",
            currentHp = 100,
            maxHp = 100,
            strength = 10,
            agility = 8,
            vitality = 10,
            intelligence = 5,
            luck = 5,
            weaponDamage = 5,
            armorDefense = 2,
            activeStatusEffects = emptyList()
        )
        
        val enemy = EnemyCombatData(
            id = "enemy1",
            name = "Test Enemy",
            currentHp = 50,
            maxHp = 50,
            strength = 5,
            agility = 5,
            vitality = 5,
            intelligence = 3,
            luck = 3,
            baseDamage = 5,
            defense = 3,
            activeStatusEffects = emptyList()
        )
        
        val state = CombatManager.initiateCombat("test_combat_3", player, enemies = listOf(enemy))
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.UseSkill("fighter_headbutt", "enemy1") // Applies stun
        )
        
        assertTrue(result is CombatActionResult.Success)
        
        // Enemy should have stun status
        val updatedEnemy = newState.enemies.find { it.id == "enemy1" }
        assertNotNull(updatedEnemy)
        assertTrue(updatedEnemy.activeStatusEffects.any { it.type == StatusEffectType.STUN })
    }
    
    @Test
    fun `multi-hit skill should deal damage multiple times`() {
        val player = PlayerCombatData(
            id = "player1",
            name = "Hero",
            currentHp = 100,
            maxHp = 100,
            strength = 10,
            agility = 8,
            vitality = 10,
            intelligence = 5,
            luck = 5,
            weaponDamage = 5,
            armorDefense = 2,
            activeStatusEffects = emptyList()
        )
        
        val enemy = EnemyCombatData(
            id = "enemy1",
            name = "Test Enemy",
            currentHp = 100,
            maxHp = 100,
            strength = 5,
            agility = 5,
            vitality = 5,
            intelligence = 3,
            luck = 3,
            baseDamage = 5,
            defense = 3,
            activeStatusEffects = emptyList()
        )
        
        val state = CombatManager.initiateCombat("test_combat_4", player, enemies = listOf(enemy))
        val (newState, result) = CombatManager.executeAction(
            state,
            CombatAction.UseSkill("ranger_feather_dart", "enemy1") // 2 hits
        )
        
        assertTrue(result is CombatActionResult.Success)
        // Damage should be from 2 hits
        val totalDamage = (result as CombatActionResult.Success).damageDealt
        assertTrue(totalDamage > 5) // Should be at least base damage of 2 hits
    }
    
    // ============ SKILL DATA MODEL TESTS ============
    
    @Test
    fun `skill should validate required fields`() {
        try {
            Skill(
                id = "",
                name = "Test",
                description = "Test",
                archetype = SkillArchetype.FIGHTER,
                tier = SkillTier.TIER_1,
                effects = listOf(com.jalmarquest.shared.skills.SkillEffect.Damage(10))
            )
            error("Should have thrown")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("ID cannot be blank"))
        }
    }
    
    @Test
    fun `passive skill should only have PassiveStats effects`() {
        try {
            Skill(
                id = "invalid",
                name = "Invalid",
                description = "Test",
                archetype = SkillArchetype.FIGHTER,
                tier = SkillTier.TIER_1,
                effects = listOf(com.jalmarquest.shared.skills.SkillEffect.Damage(10)),
                isPassive = true
            )
            error("Should have thrown")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Passive skills can only have PassiveStats"))
        }
    }
    
    @Test
    fun `skill tier should match required level and cost`() {
        assertEquals(1, SkillTier.TIER_1.requiredLevel)
        assertEquals(1, SkillTier.TIER_1.skillPointCost)
        
        assertEquals(10, SkillTier.TIER_2.requiredLevel)
        assertEquals(2, SkillTier.TIER_2.skillPointCost)
        
        assertEquals(40, SkillTier.ULTIMATE.requiredLevel)
        assertEquals(5, SkillTier.ULTIMATE.skillPointCost)
    }
}

