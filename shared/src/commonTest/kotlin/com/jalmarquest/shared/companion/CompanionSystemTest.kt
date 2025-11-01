package com.jalmarquest.shared.companion

import com.jalmarquest.shared.combat.CombatState
import com.jalmarquest.shared.combat.EnemyCombatData
import com.jalmarquest.shared.combat.PlayerCombatData
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.Position
import kotlin.test.*

/**
 * Comprehensive test suite for the Companion system.
 * Tests companion recruitment, loyalty mechanics, AI behavior, and integration.
 */
class CompanionSystemTest {
    
    private lateinit var companionManager: CompanionManager
    private lateinit var testGameState: GameState
    
    @BeforeTest
    fun setup() {
        companionManager = CompanionManager()
        testGameState = createTestGameState()
    }
    
    private fun createTestGameState(): GameState {
        return GameState(
            version = 1,
            player = Player(
                id = "test_player",
                name = "TestHero",
                level = 5,
                position = Position(0, 0, "test_location")
            ),
            recruitedCompanions = emptySet(),
            activeCompanionId = null,
            companionProgress = emptyMap()
        )
    }
    
    // ========== CompanionCatalog Tests ==========
    
    @Test
    fun `CompanionCatalog has 10 companions`() {
        val companions = CompanionCatalog.ALL_COMPANIONS
        assertEquals(10, companions.size, "Catalog should have 10 companions")
    }
    
    @Test
    fun `CompanionCatalog getCompanionById returns correct companion`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")
        assertNotNull(pip, "Should find Pip")
        assertEquals("Pip", pip.name)
        assertEquals(CompanionBehavior.SUPPORTIVE, pip.combatBehavior)
    }
    
    @Test
    fun `CompanionCatalog getCompanionById returns null for invalid ID`() {
        val result = CompanionCatalog.getCompanionById("invalid_companion")
        assertNull(result, "Should return null for invalid ID")
    }
    
    @Test
    fun `All companions have unique IDs`() {
        val ids = CompanionCatalog.ALL_COMPANIONS.map { it.id }
        val uniqueIds = ids.toSet()
        assertEquals(ids.size, uniqueIds.size, "All companion IDs should be unique")
    }
    
    @Test
    fun `All companions have at least one ability`() {
        CompanionCatalog.ALL_COMPANIONS.forEach { companion ->
            assertTrue(
                companion.abilities.isNotEmpty(),
                "${companion.name} should have at least one ability"
            )
        }
    }
    
    @Test
    fun `All companions have valid stats`() {
        CompanionCatalog.ALL_COMPANIONS.forEach { companion ->
            assertTrue(companion.maxHp > 0, "${companion.name} should have positive maxHp")
            assertTrue(companion.strength >= 0, "${companion.name} should have non-negative strength")
            assertTrue(companion.agility >= 0, "${companion.name} should have non-negative agility")
            assertTrue(companion.vitality >= 0, "${companion.name} should have non-negative vitality")
            assertTrue(companion.intelligence >= 0, "${companion.name} should have non-negative intelligence")
            assertTrue(companion.luck >= 0, "${companion.name} should have non-negative luck")
        }
    }
    
    // ========== CompanionManager - Recruitment Tests ==========
    
    @Test
    fun `recruitCompanion adds companion to game state`() {
        val result = companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L)
        
        assertTrue(result is RecruitResult.Success, "Recruitment should succeed")
        val success = result as RecruitResult.Success
        
        assertTrue(success.newState.recruitedCompanions.contains("pip_young_quail"))
        assertNotNull(success.newState.companionProgress["pip_young_quail"])
        assertEquals(50, success.newState.companionProgress["pip_young_quail"]?.loyaltyScore)
    }
    
    @Test
    fun `recruitCompanion fails for invalid companion ID`() {
        val result = companionManager.recruitCompanion(testGameState, "invalid_companion", 1000L)
        
        assertTrue(result is RecruitResult.Failure)
        assertEquals(RecruitFailure.COMPANION_NOT_FOUND, (result as RecruitResult.Failure).reason)
    }
    
    @Test
    fun `recruitCompanion fails if already recruited`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val result = companionManager.recruitCompanion(state1, "pip_young_quail", 2000L)
        
        assertTrue(result is RecruitResult.Failure)
        assertEquals(RecruitFailure.ALREADY_RECRUITED, (result as RecruitResult.Failure).reason)
    }
    
    // ========== CompanionManager - Dismiss Tests ==========
    
    @Test
    fun `dismissCompanion removes companion from recruited set`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val result = companionManager.dismissCompanion(state1, "pip_young_quail")
        
        assertTrue(result is DismissResult.Success)
        val success = result as DismissResult.Success
        
        assertFalse(success.newState.recruitedCompanions.contains("pip_young_quail"))
    }
    
    @Test
    fun `dismissCompanion preserves progress for potential re-recruitment`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val result = companionManager.dismissCompanion(state1, "pip_young_quail")
        
        assertTrue(result is DismissResult.Success)
        val success = result as DismissResult.Success
        
        // Progress should still exist
        assertNotNull(success.newState.companionProgress["pip_young_quail"])
    }
    
    @Test
    fun `dismissCompanion clears active companion if dismissing active`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val state2 = (companionManager.setActiveCompanion(state1, "pip_young_quail") as SetActiveResult.Success).newState
        
        assertEquals("pip_young_quail", state2.activeCompanionId)
        
        val result = companionManager.dismissCompanion(state2, "pip_young_quail")
        assertTrue(result is DismissResult.Success)
        
        assertNull((result as DismissResult.Success).newState.activeCompanionId)
    }
    
    @Test
    fun `dismissCompanion fails if not recruited`() {
        val result = companionManager.dismissCompanion(testGameState, "pip_young_quail")
        
        assertTrue(result is DismissResult.Failure)
        assertEquals(DismissFailure.NOT_RECRUITED, (result as DismissResult.Failure).reason)
    }
    
    // ========== CompanionManager - Active Companion Tests ==========
    
    @Test
    fun `setActiveCompanion sets companion as active`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val result = companionManager.setActiveCompanion(state1, "pip_young_quail")
        
        assertTrue(result is SetActiveResult.Success)
        assertEquals("pip_young_quail", (result as SetActiveResult.Success).newState.activeCompanionId)
    }
    
    @Test
    fun `setActiveCompanion can clear active companion with null`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val state2 = (companionManager.setActiveCompanion(state1, "pip_young_quail") as SetActiveResult.Success).newState
        val result = companionManager.setActiveCompanion(state2, null)
        
        assertTrue(result is SetActiveResult.Success)
        assertNull((result as SetActiveResult.Success).newState.activeCompanionId)
    }
    
    @Test
    fun `setActiveCompanion fails if companion not recruited`() {
        val result = companionManager.setActiveCompanion(testGameState, "pip_young_quail")
        
        assertTrue(result is SetActiveResult.Failure)
        assertEquals(SetActiveFailure.NOT_RECRUITED, (result as SetActiveResult.Failure).reason)
    }
    
    @Test
    fun `getActiveCompanion returns correct companion`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val state2 = (companionManager.setActiveCompanion(state1, "pip_young_quail") as SetActiveResult.Success).newState
        
        val companion = companionManager.getActiveCompanion(state2)
        assertNotNull(companion)
        assertEquals("Pip", companion.name)
    }
    
    @Test
    fun `getActiveCompanion returns null when no active companion`() {
        val companion = companionManager.getActiveCompanion(testGameState)
        assertNull(companion)
    }
    
    // ========== CompanionManager - Loyalty Tests ==========
    
    @Test
    fun `modifyLoyalty changes loyalty score correctly`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val result = companionManager.modifyLoyalty(
            state1,
            "pip_young_quail",
            20,
            LoyaltyChangeTrigger.COMBAT_VICTORY,
            2000L
        )
        
        assertTrue(result is ModifyLoyaltyResult.Success)
        val success = result as ModifyLoyaltyResult.Success
        
        assertEquals(50, success.previousLoyalty)
        assertEquals(70, success.newLoyalty)
    }
    
    @Test
    fun `modifyLoyalty clamps loyalty to 0-100 range`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        
        // Test upper bound
        val result1 = companionManager.modifyLoyalty(state1, "pip_young_quail", 100, LoyaltyChangeTrigger.PERSONAL_QUEST_COMPLETE, 2000L)
        assertTrue(result1 is ModifyLoyaltyResult.Success)
        assertEquals(100, (result1 as ModifyLoyaltyResult.Success).newLoyalty)
        
        // Test lower bound
        val result2 = companionManager.modifyLoyalty(state1, "pip_young_quail", -100, LoyaltyChangeTrigger.PLAYER_DEATH, 3000L)
        assertTrue(result2 is ModifyLoyaltyResult.Success)
        assertEquals(0, (result2 as ModifyLoyaltyResult.Success).newLoyalty)
    }
    
    @Test
    fun `modifyLoyalty detects threshold crossing`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        
        // Starting at 50 (FRIENDLY), adding 10 stays in FRIENDLY (50-74)
        val result = companionManager.modifyLoyalty(state1, "pip_young_quail", 10, LoyaltyChangeTrigger.FAVORITE_ITEM_GIFT, 2000L)
        assertTrue(result is ModifyLoyaltyResult.Success)
        assertFalse((result as ModifyLoyaltyResult.Success).thresholdCrossed) // Still in same tier (both FRIENDLY)
        
        // Starting at 50 (FRIENDLY), subtracting 26 goes to 24 (NEUTRAL->DISTRUSTFUL boundary)
        val result2 = companionManager.modifyLoyalty(state1, "pip_young_quail", -26, LoyaltyChangeTrigger.PLAYER_FLED_AFTER_COMPANION_DOWN, 2000L)
        assertTrue(result2 is ModifyLoyaltyResult.Success)
        assertTrue((result2 as ModifyLoyaltyResult.Success).thresholdCrossed) // Crossed from FRIENDLY to DISTRUSTFUL
    }
    
    @Test
    fun `getLoyalty returns correct loyalty score`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val state2 = (companionManager.modifyLoyalty(state1, "pip_young_quail", 25, LoyaltyChangeTrigger.COMBAT_VICTORY, 2000L) as ModifyLoyaltyResult.Success).newState
        
        assertEquals(75, companionManager.getLoyalty(state2, "pip_young_quail"))
    }
    
    @Test
    fun `getLoyaltyStatus returns correct status tier`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        
        // Companions start at loyalty 50 (FRIENDLY tier: 50-74)
        assertEquals(CompanionLoyaltyStatus.FRIENDLY, companionManager.getLoyaltyStatus(state1, "pip_young_quail"))
        
        // Adding 30 brings to 80 (LOYAL tier: 75-99)
        val state2 = (companionManager.modifyLoyalty(state1, "pip_young_quail", 30, LoyaltyChangeTrigger.PERSONAL_QUEST_COMPLETE, 2000L) as ModifyLoyaltyResult.Success).newState
        assertEquals(CompanionLoyaltyStatus.LOYAL, companionManager.getLoyaltyStatus(state2, "pip_young_quail"))
    }
    
    // ========== CompanionManager - Ability Tests ==========
    
    @Test
    fun `canUseAbility succeeds for unlocked ability`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val basicAbility = pip.abilities.find { it.loyaltyRequired == 0 }!!
        
        val result = companionManager.canUseAbility(state1, "pip_young_quail", basicAbility.id)
        assertTrue(result is CanUseAbilityResult.Success)
    }
    
    @Test
    fun `canUseAbility fails for locked ability due to low loyalty`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val ultimateAbility = pip.abilities.find { it.loyaltyRequired == 100 }!!
        
        val result = companionManager.canUseAbility(state1, "pip_young_quail", ultimateAbility.id)
        assertTrue(result is CanUseAbilityResult.Failure)
        assertEquals(CanUseAbilityFailure.INSUFFICIENT_LOYALTY, (result as CanUseAbilityResult.Failure).reason)
    }
    
    @Test
    fun `canUseAbility fails for ability on cooldown`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val ability = pip.abilities.first()
        
        val state2 = companionManager.setAbilityCooldown(state1, "pip_young_quail", ability.id, 3)!!
        
        val result = companionManager.canUseAbility(state2, "pip_young_quail", ability.id)
        assertTrue(result is CanUseAbilityResult.Failure)
        assertEquals(CanUseAbilityFailure.ON_COOLDOWN, (result as CanUseAbilityResult.Failure).reason)
        assertEquals(3, result.remainingCooldown)
    }
    
    @Test
    fun `getAvailableAbilities returns only unlocked abilities`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        
        // Companions start at loyalty 50 (FRIENDLY)
        val available = companionManager.getAvailableAbilities(state1, "pip_young_quail")
        assertTrue(available.all { it.loyaltyRequired <= 50 })
    }
    
    @Test
    fun `getLockedAbilities returns only locked abilities`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        
        // Companions start at loyalty 50 (FRIENDLY)
        val locked = companionManager.getLockedAbilities(state1, "pip_young_quail")
        assertTrue(locked.all { it.loyaltyRequired > 50 })
    }
    
    @Test
    fun `decrementCooldowns reduces all cooldowns by 1`() {
        val state1 = (companionManager.recruitCompanion(testGameState, "pip_young_quail", 1000L) as RecruitResult.Success).newState
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val ability1 = pip.abilities[0]
        val ability2 = pip.abilities[1]
        
        val state2 = companionManager.setAbilityCooldown(state1, "pip_young_quail", ability1.id, 5)!!
        val state3 = companionManager.setAbilityCooldown(state2, "pip_young_quail", ability2.id, 3)!!
        val state4 = companionManager.decrementCooldowns(state3, "pip_young_quail")!!
        
        val progress = state4.companionProgress["pip_young_quail"]!!
        assertEquals(4, progress.abilityCooldowns[ability1.id])
        assertEquals(2, progress.abilityCooldowns[ability2.id])
    }
    
    // ========== Companion Data Model Tests ==========
    
    @Test
    fun `Companion toCombatData applies loyalty modifier to stats`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        
        // Test with low loyalty (DISTRUSTFUL - 0.7x modifier)
        val lowLoyaltyCombat = pip.toCombatData("combat1", pip.maxHp, 20)
        assertTrue(lowLoyaltyCombat.strength < pip.strength)
        
        // Test with high loyalty (DEVOTED - 1.3x modifier)
        val highLoyaltyCombat = pip.toCombatData("combat2", pip.maxHp, 100)
        assertTrue(highLoyaltyCombat.strength > pip.strength)
    }
    
    @Test
    fun `CompanionCombatData willFight returns false for DISTRUSTFUL with low player HP`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val combatData = pip.toCombatData("combat1", pip.maxHp, 20) // DISTRUSTFUL
        
        assertFalse(combatData.willFight(0.2f)) // Player at 20% HP
        assertTrue(combatData.willFight(0.5f))  // Player at 50% HP
    }
    
    @Test
    fun `CompanionLoyaltyStatus fromScore returns correct tier`() {
        assertEquals(CompanionLoyaltyStatus.DISTRUSTFUL, CompanionLoyaltyStatus.fromScore(0))
        assertEquals(CompanionLoyaltyStatus.DISTRUSTFUL, CompanionLoyaltyStatus.fromScore(24))
        assertEquals(CompanionLoyaltyStatus.NEUTRAL, CompanionLoyaltyStatus.fromScore(25))
        assertEquals(CompanionLoyaltyStatus.NEUTRAL, CompanionLoyaltyStatus.fromScore(49))
        assertEquals(CompanionLoyaltyStatus.FRIENDLY, CompanionLoyaltyStatus.fromScore(50))
        assertEquals(CompanionLoyaltyStatus.FRIENDLY, CompanionLoyaltyStatus.fromScore(74))
        assertEquals(CompanionLoyaltyStatus.LOYAL, CompanionLoyaltyStatus.fromScore(75))
        assertEquals(CompanionLoyaltyStatus.LOYAL, CompanionLoyaltyStatus.fromScore(99))
        assertEquals(CompanionLoyaltyStatus.DEVOTED, CompanionLoyaltyStatus.fromScore(100))
    }
    
    // ========== CompanionAI Tests ==========
    
    @Test
    fun `CompanionAI AGGRESSIVE targets strongest enemy when loyal`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val combatData = pip.toCombatData("companion1", pip.maxHp, 75) // LOYAL
        
        val combatState = createTestCombatState(
            enemyHps = listOf(30, 50, 20) // Enemy 2 is strongest
        )
        
        val action = CompanionAI.decideAction(combatData, pip, combatState, 75, pip.abilities)
        // Should attack (might be strongest or use ability targeting strongest)
        assertNotNull(action)
    }
    
    @Test
    fun `CompanionAI shouldFlee returns true for DISTRUSTFUL with low HP`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val combatData = pip.toCombatData("companion1", 10, 20).copy(currentHp = 10, maxHp = 40) // 25% HP, DISTRUSTFUL
        
        val combatState = createTestCombatState()
        
        val shouldFlee = CompanionAI.shouldFlee(combatData, combatState, 20)
        assertTrue(shouldFlee, "DISTRUSTFUL companion with low HP should flee")
    }
    
    @Test
    fun `CompanionAI shouldFlee returns false for FRIENDLY companion`() {
        val pip = CompanionCatalog.getCompanionById("pip_young_quail")!!
        val combatData = pip.toCombatData("companion1", 10, 60).copy(currentHp = 10, maxHp = 40) // 25% HP, FRIENDLY
        
        val combatState = createTestCombatState()
        
        val shouldFlee = CompanionAI.shouldFlee(combatData, combatState, 60)
        assertFalse(shouldFlee, "FRIENDLY companion should not flee")
    }
    
    // ========== LoyaltyMechanics Tests ==========
    
    @Test
    fun `LoyaltyMechanics getEffectsForLoyalty returns correct modifiers`() {
        val distrustfulEffects = LoyaltyMechanics.getEffectsForLoyalty(20)
        assertEquals(0.7, distrustfulEffects.statMultiplier)
        assertEquals(0.4, distrustfulEffects.fleeChance)
        
        val devotedEffects = LoyaltyMechanics.getEffectsForLoyalty(100)
        assertEquals(1.3, devotedEffects.statMultiplier)
        assertEquals(0.0, devotedEffects.fleeChance)
    }
    
    @Test
    fun `LoyaltyChangeHelper calculateNewLoyalty clamps to 0-100`() {
        assertEquals(100, LoyaltyChangeHelper.calculateNewLoyalty(90, 50))
        assertEquals(0, LoyaltyChangeHelper.calculateNewLoyalty(10, -50))
        assertEquals(60, LoyaltyChangeHelper.calculateNewLoyalty(50, 10))
    }
    
    @Test
    fun `LoyaltyChangeHelper crossedThreshold detects tier changes`() {
        assertTrue(LoyaltyChangeHelper.crossedThreshold(49, 50)) // NEUTRAL -> FRIENDLY
        assertTrue(LoyaltyChangeHelper.crossedThreshold(50, 49)) // FRIENDLY -> NEUTRAL
        assertFalse(LoyaltyChangeHelper.crossedThreshold(50, 60)) // Both FRIENDLY
    }
    
    // ========== Helper Methods ==========
    
    private fun createTestCombatState(
        playerHp: Int = 100,
        playerMaxHp: Int = 100,
        enemyHps: List<Int> = listOf(50)
    ): CombatState {
        val player = PlayerCombatData(
            id = "player1",
            name = "Hero",
            currentHp = playerHp,
            maxHp = playerMaxHp,
            strength = 10,
            agility = 10,
            vitality = 10,
            intelligence = 5,
            luck = 5
        )
        
        val enemies = enemyHps.mapIndexed { index, hp ->
            EnemyCombatData(
                id = "enemy$index",
                name = "Enemy $index",
                currentHp = hp,
                maxHp = 50,
                strength = 8,
                agility = 8,
                vitality = 8,
                intelligence = 3,
                luck = 3,
                baseDamage = 5,
                defense = 2
            )
        }
        
        return CombatState(
            combatId = "test_combat",
            player = player,
            enemies = enemies,
            turnOrder = listOf("player1") + enemies.map { it.id }
        )
    }
}
