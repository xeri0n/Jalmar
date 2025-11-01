package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.ItemRarity
import com.jalmarquest.shared.model.AchievementProgress
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CosmeticTest {
    
    // TEST DATA
    
    private val testPlayer = Player(
        id = "test-player",
        name = "Jalmar",
        level = 10,
        position = Position(0, 0, "starting_village")
    )
    
    private val testGameState = GameState(
        player = testPlayer,
        achievements = listOf(
            AchievementProgress(id = "beetle_hunter", unlocked = true, unlockedAt = 1000L),
            AchievementProgress(id = "defeated_garden_gnome_king", unlocked = true, unlockedAt = 2000L)
        ),
        completedQuests = setOf("firefly_rescue", "king_of_buttonburgh"),
        discoveredLocations = setOf("swamp_heart", "forest_floor", "flower_garden", "cave_depths")
    )
    
    private val manager = CosmeticManager()
    
    // COSMETIC DATA MODEL TESTS
    
    @Test
    fun `Cosmetic validates width and height constraints`() {
        val result = runCatching {
            Cosmetic(
                id = "invalid",
                name = "Invalid",
                description = "Test",
                type = CosmeticType.WALL_DECORATION,
                rarity = ItemRarity.COMMON,
                width = 0,  // Invalid
                height = 1,
                prestigeValue = 10,
                unlockCondition = null
            )
        }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("width") == true)
    }
    
    @Test
    fun `Cosmetic validates prestige is non-negative`() {
        val result = runCatching {
            Cosmetic(
                id = "invalid",
                name = "Invalid",
                description = "Test",
                type = CosmeticType.WALL_DECORATION,
                rarity = ItemRarity.COMMON,
                width = 1,
                height = 1,
                prestigeValue = -5,  // Invalid
                unlockCondition = null
            )
        }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Prestige value") == true)
    }
    
    @Test
    fun `PlacedCosmetic validates grid coordinates`() {
        val result = runCatching {
            PlacedCosmetic(
                cosmeticId = "test",
                gridX = -1,  // Invalid
                gridY = 0
            )
        }
        assertTrue(result.isFailure)
    }
    
    @Test
    fun `NestGridConfig validates bounds correctly`() {
        assertTrue(NestGridConfig.isWithinBounds(0, 0))
        assertTrue(NestGridConfig.isWithinBounds(7, 5))  // Max bounds
        assertFalse(NestGridConfig.isWithinBounds(8, 0))  // Out of bounds
        assertFalse(NestGridConfig.isWithinBounds(0, 6))  // Out of bounds
        assertFalse(NestGridConfig.isWithinBounds(-1, 0))
    }
    
    // CATALOG TESTS
    
    @Test
    fun `CosmeticCatalog contains 40+ items`() {
        assertTrue(CosmeticCatalog.totalCount >= 40, "Expected at least 40 cosmetics, got ${CosmeticCatalog.totalCount}")
    }
    
    @Test
    fun `CosmeticCatalog has no duplicate IDs`() {
        val result = CosmeticCatalog.validateCatalog()
        assertTrue(result.isSuccess, "Catalog validation failed: ${result.exceptionOrNull()?.message}")
    }
    
    @Test
    fun `getCosmeticById returns correct cosmetic`() {
        val cosmetic = CosmeticCatalog.getCosmeticById("wall_twig_picture")
        assertEquals("wall_twig_picture", cosmetic?.id)
        assertEquals("Twig Picture Frame", cosmetic?.name)
        assertEquals(CosmeticType.WALL_DECORATION, cosmetic?.type)
    }
    
    @Test
    fun `getCosmeticsByType filters correctly`() {
        val wallDecorations = CosmeticCatalog.getCosmeticsByType(CosmeticType.WALL_DECORATION)
        assertTrue(wallDecorations.size >= 10, "Expected at least 10 wall decorations")
        assertTrue(wallDecorations.all { it.type == CosmeticType.WALL_DECORATION })
    }
    
    @Test
    fun `getCosmeticsByRarity filters correctly`() {
        val legendaryItems = CosmeticCatalog.getCosmeticsByRarity(ItemRarity.LEGENDARY)
        assertTrue(legendaryItems.isNotEmpty())
        assertTrue(legendaryItems.all { it.rarity == ItemRarity.LEGENDARY })
    }
    
    @Test
    fun `getStartingCosmetics returns only unlocked items`() {
        val starting = CosmeticCatalog.getStartingCosmetics()
        assertTrue(starting.isNotEmpty())
        assertTrue(starting.all { it.unlockCondition == null })
    }
    
    @Test
    fun `getCosmeticsUnlockedByAchievement finds achievement cosmetics`() {
        val beetleCosmetics = CosmeticCatalog.getCosmeticsUnlockedByAchievement("beetle_hunter")
        assertTrue(beetleCosmetics.isNotEmpty())
        assertTrue(beetleCosmetics.all { 
            (it.unlockCondition as? UnlockCondition.Achievement)?.achievementId == "beetle_hunter"
        })
    }
    
    // PLACEMENT TESTS
    
    @Test
    fun `placeCosmetic succeeds with valid placement`() {
        val unlockedCosmetics = setOf("wall_twig_picture")
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 0,
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Success)
        val success = result as CosmeticManager.PlacementResult.Success
        assertEquals(1, success.updatedPlacedCosmetics.size)
        assertEquals("wall_twig_picture", success.updatedPlacedCosmetics[0].cosmeticId)
    }
    
    @Test
    fun `placeCosmetic fails when cosmetic not found`() {
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = emptySet(),
            cosmeticId = "nonexistent_cosmetic",
            x = 0,
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.COSMETIC_NOT_FOUND, failure.reason)
    }
    
    @Test
    fun `placeCosmetic fails when cosmetic is locked`() {
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = emptySet(),  // Not unlocked
            cosmeticId = "wall_beetle_shell",  // Requires achievement
            x = 0,
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.COSMETIC_LOCKED, failure.reason)
    }
    
    @Test
    fun `placeCosmetic fails when out of bounds`() {
        val unlockedCosmetics = setOf("wall_twig_picture")
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 10,  // Out of bounds (grid is 8x6)
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.OUT_OF_BOUNDS, failure.reason)
    }
    
    @Test
    fun `placeCosmetic fails when cosmetic extends beyond bounds`() {
        val unlockedCosmetics = setOf("wall_leaf_banner")  // 2x1 cosmetic
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_leaf_banner",
            x = 7,  // Would extend to x=9, out of bounds
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.OUT_OF_BOUNDS, failure.reason)
    }
    
    @Test
    fun `placeCosmetic fails when collision occurs`() {
        val existing = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0)
        val unlockedCosmetics = setOf("wall_twig_picture", "floor_leaf_rug")
        
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = listOf(existing),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "floor_leaf_rug",  // 2x2 cosmetic
            x = 0,  // Would overlap with existing
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.COLLISION, failure.reason)
    }
    
    @Test
    fun `placeCosmetic succeeds when cosmetics are adjacent but not overlapping`() {
        val existing = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0)  // 1x1
        val unlockedCosmetics = setOf("wall_twig_picture", "floor_leaf_rug")
        
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = listOf(existing),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "floor_leaf_rug",  // 2x2 cosmetic
            x = 1,  // Adjacent, not overlapping
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Success)
        val success = result as CosmeticManager.PlacementResult.Success
        assertEquals(2, success.updatedPlacedCosmetics.size)
    }
    
    @Test
    fun `placeCosmetic allows multiple instances of same cosmetic at different positions`() {
        val existing = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0)
        val unlockedCosmetics = setOf("wall_twig_picture")
        
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = listOf(existing),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 2,  // Different position
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Success)
        val success = result as CosmeticManager.PlacementResult.Success
        assertEquals(2, success.updatedPlacedCosmetics.size)
    }
    
    @Test
    fun `placeCosmetic fails when placing same cosmetic at same position`() {
        val existing = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0)
        val unlockedCosmetics = setOf("wall_twig_picture")
        
        val result = manager.placeCosmetic(
            currentPlacedCosmetics = listOf(existing),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 0,  // Same position
            y = 0
        )
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.ALREADY_PLACED, failure.reason)
    }
    
    // REMOVAL TESTS
    
    @Test
    fun `removeCosmetic succeeds when cosmetic exists at position`() {
        val placed = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 2, gridY = 3)
        val result = manager.removeCosmetic(
            currentPlacedCosmetics = listOf(placed),
            x = 2,
            y = 3
        )
        
        assertTrue(result is CosmeticManager.RemovalResult.Success)
        val success = result as CosmeticManager.RemovalResult.Success
        assertEquals(0, success.updatedPlacedCosmetics.size)
        assertEquals(placed, success.removedCosmetic)
    }
    
    @Test
    fun `removeCosmetic works when clicking anywhere within cosmetic bounds`() {
        // Place a 2x2 cosmetic at (1, 1)
        val placed = PlacedCosmetic(cosmeticId = "floor_leaf_rug", gridX = 1, gridY = 1)  // 2x2
        
        // Click at different positions within the cosmetic
        val result1 = manager.removeCosmetic(listOf(placed), x = 1, y = 1)  // Top-left
        assertTrue(result1 is CosmeticManager.RemovalResult.Success)
        
        val result2 = manager.removeCosmetic(listOf(placed), x = 2, y = 1)  // Top-right
        assertTrue(result2 is CosmeticManager.RemovalResult.Success)
        
        val result3 = manager.removeCosmetic(listOf(placed), x = 1, y = 2)  // Bottom-left
        assertTrue(result3 is CosmeticManager.RemovalResult.Success)
        
        val result4 = manager.removeCosmetic(listOf(placed), x = 2, y = 2)  // Bottom-right
        assertTrue(result4 is CosmeticManager.RemovalResult.Success)
    }
    
    @Test
    fun `removeCosmetic fails when no cosmetic at position`() {
        val placed = PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 2, gridY = 3)
        val result = manager.removeCosmetic(
            currentPlacedCosmetics = listOf(placed),
            x = 5,  // Different position
            y = 5
        )
        
        assertTrue(result is CosmeticManager.RemovalResult.Failure)
    }
    
    // VALIDATION TESTS
    
    @Test
    fun `canPlace returns true for valid placement`() {
        val unlockedCosmetics = setOf("wall_twig_picture")
        val canPlace = manager.canPlace(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 0,
            y = 0
        )
        assertTrue(canPlace)
    }
    
    @Test
    fun `canPlace returns false for locked cosmetic`() {
        val canPlace = manager.canPlace(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = emptySet(),  // Not unlocked
            cosmeticId = "wall_beetle_shell",
            x = 0,
            y = 0
        )
        assertFalse(canPlace)
    }
    
    @Test
    fun `canPlace returns false for out of bounds`() {
        val unlockedCosmetics = setOf("wall_twig_picture")
        val canPlace = manager.canPlace(
            currentPlacedCosmetics = emptyList(),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 10,
            y = 0
        )
        assertFalse(canPlace)
    }
    
    @Test
    fun `canPlace returns false for collision`() {
        val existing = PlacedCosmetic(cosmeticId = "floor_leaf_rug", gridX = 0, gridY = 0)  // 2x2
        val unlockedCosmetics = setOf("wall_twig_picture", "floor_leaf_rug")
        val canPlace = manager.canPlace(
            currentPlacedCosmetics = listOf(existing),
            unlockedCosmetics = unlockedCosmetics,
            cosmeticId = "wall_twig_picture",
            x = 1,  // Would overlap
            y = 1
        )
        assertFalse(canPlace)
    }
    
    // PRESTIGE TESTS
    
    @Test
    fun `calculateTotalPrestige sums all placed cosmetic values`() {
        val placed = listOf(
            PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0),  // 5 prestige
            PlacedCosmetic(cosmeticId = "floor_leaf_rug", gridX = 2, gridY = 0),     // 6 prestige
            PlacedCosmetic(cosmeticId = "furniture_twig_perch", gridX = 5, gridY = 0) // 5 prestige
        )
        
        val total = manager.calculateTotalPrestige(placed)
        assertEquals(16, total)
    }
    
    @Test
    fun `calculateTotalPrestige returns 0 for empty list`() {
        val total = manager.calculateTotalPrestige(emptyList())
        assertEquals(0, total)
    }
    
    @Test
    fun `calculateTotalPrestige handles missing cosmetics gracefully`() {
        val placed = listOf(
            PlacedCosmetic(cosmeticId = "wall_twig_picture", gridX = 0, gridY = 0),
            PlacedCosmetic(cosmeticId = "nonexistent", gridX = 2, gridY = 0)  // Doesn't exist
        )
        
        val total = manager.calculateTotalPrestige(placed)
        assertEquals(5, total)  // Only counts the valid one
    }
    
    // UNLOCK CONDITION TESTS
    
    @Test
    fun `getAvailableCosmetics includes always-unlocked items`() {
        val available = manager.getAvailableCosmetics(testGameState, emptySet())
        val alwaysUnlocked = available.filter { it.unlockCondition == null }
        assertTrue(alwaysUnlocked.isNotEmpty())
    }
    
    @Test
    fun `getAvailableCosmetics includes achievement-unlocked items`() {
        val available = manager.getAvailableCosmetics(testGameState, emptySet())
        val beetleShell = available.find { it.id == "wall_beetle_shell" }
        
        // Player has "beetle_hunter" achievement
        assertTrue(beetleShell != null, "Beetle shell should be available with achievement")
    }
    
    @Test
    fun `getAvailableCosmetics includes level-unlocked items`() {
        val available = manager.getAvailableCosmetics(testGameState, emptySet())
        val levelItems = available.filter { 
            val condition = it.unlockCondition as? UnlockCondition.Level
            condition != null && condition.requiredLevel <= testGameState.player.level
        }
        assertTrue(levelItems.isNotEmpty())
    }
    
    @Test
    fun `getAvailableCosmetics excludes already unlocked items`() {
        val alreadyUnlocked = setOf("wall_twig_picture", "floor_leaf_rug")
        val available = manager.getAvailableCosmetics(testGameState, alreadyUnlocked)
        
        assertFalse(available.any { it.id == "wall_twig_picture" })
        assertFalse(available.any { it.id == "floor_leaf_rug" })
    }
    
    @Test
    fun `getAvailableCosmetics includes quest-unlocked items`() {
        val available = manager.getAvailableCosmetics(testGameState, emptySet())
        val fireflyJar = available.find { it.id == "wall_firefly_jar_hanging" }
        
        // Player completed "firefly_rescue" quest
        assertTrue(fireflyJar != null, "Firefly jar should be available after quest")
    }
    
    @Test
    fun `getAvailableCosmetics includes boss-unlocked items`() {
        val available = manager.getAvailableCosmetics(testGameState, emptySet())
        val gnomeItems = available.filter {
            (it.unlockCondition as? UnlockCondition.Boss)?.enemyId == "garden_gnome_king"
        }
        
        // Player has defeated garden gnome king
        assertTrue(gnomeItems.isNotEmpty())
    }
    
    // PURCHASE TESTS
    
    @Test
    fun `purchaseCosmetic succeeds with sufficient currency`() {
        val result = manager.purchaseCosmetic(
            currentUnlocked = emptySet(),
            currentSeeds = 1000,
            currentGlimmerShards = 10,
            cosmeticId = "floor_grass_mat"  // Costs 500 seeds
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Success)
        val success = result as CosmeticManager.PurchaseResult.Success
        assertTrue(success.updatedUnlocked.contains("floor_grass_mat"))
        assertEquals(500, success.newSeeds)
        assertEquals(10, success.newGlimmerShards)
    }
    
    @Test
    fun `purchaseCosmetic fails with insufficient seeds`() {
        val result = manager.purchaseCosmetic(
            currentUnlocked = emptySet(),
            currentSeeds = 100,  // Not enough
            currentGlimmerShards = 10,
            cosmeticId = "floor_grass_mat"  // Costs 500 seeds
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Failure)
        val failure = result as CosmeticManager.PurchaseResult.Failure
        assertTrue(failure.reason.contains("seeds"))
    }
    
    @Test
    fun `purchaseCosmetic fails with insufficient glimmer shards`() {
        val result = manager.purchaseCosmetic(
            currentUnlocked = emptySet(),
            currentSeeds = 5000,
            currentGlimmerShards = 1,  // Not enough
            cosmeticId = "floor_silk_rug"  // Costs 2000 seeds + 5 glimmer shards
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Failure)
        val failure = result as CosmeticManager.PurchaseResult.Failure
        assertTrue(failure.reason.contains("glimmer shards"))
    }
    
    @Test
    fun `purchaseCosmetic fails when already unlocked`() {
        val result = manager.purchaseCosmetic(
            currentUnlocked = setOf("floor_grass_mat"),
            currentSeeds = 1000,
            currentGlimmerShards = 10,
            cosmeticId = "floor_grass_mat"
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Failure)
        val failure = result as CosmeticManager.PurchaseResult.Failure
        assertTrue(failure.reason.contains("Already unlocked"))
    }
    
    @Test
    fun `purchaseCosmetic fails when not purchasable`() {
        val result = manager.purchaseCosmetic(
            currentUnlocked = emptySet(),
            currentSeeds = 10000,
            currentGlimmerShards = 100,
            cosmeticId = "wall_beetle_shell"  // Unlocked by achievement, not purchase
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Failure)
        val failure = result as CosmeticManager.PurchaseResult.Failure
        assertTrue(failure.reason.contains("Not available for purchase"))
    }
}

