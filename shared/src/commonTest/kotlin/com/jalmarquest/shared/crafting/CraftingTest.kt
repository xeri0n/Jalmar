package com.jalmarquest.shared.crafting

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.InventorySlot
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.*

/**
 * Comprehensive tests for the Crafting system including Recipe validation,
 * CraftingManager operations, and material consumption.
 */
class CraftingTest {
    
    private fun createTestPlayer(
        level: Int = 1,
        inventorySlots: List<InventorySlot> = emptyList()
    ): Player {
        return Player(
            id = "test_player",
            name = "Test Crafter",
            level = level,
            stats = PlayerStats(),
            position = Position(0, 0, "starting_village"),
            inventory = Inventory(slots = inventorySlots)
        )
    }
    
    // ===== RECIPE DATA CLASS TESTS =====
    
    @Test
    fun `Recipe should validate inputs not empty`() {
        assertFails {
            Recipe(
                id = "test",
                name = "Test",
                category = CraftingCategory.EQUIPMENT,
                inputs = emptyList(),
                output = RecipeOutput("twig_spear", 1)
            )
        }
    }
    
    @Test
    fun `Recipe should validate required level minimum`() {
        assertFails {
            Recipe(
                id = "test",
                name = "Test",
                category = CraftingCategory.EQUIPMENT,
                inputs = listOf(RecipeInput("twig", 1)),
                output = RecipeOutput("twig_spear", 1),
                requiredLevel = 0
            )
        }
    }
    
    @Test
    fun `Recipe formattedInputs should format correctly`() {
        val recipe = Recipe(
            id = "test",
            name = "Test",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("twig", 3),
                RecipeInput("acorn_cap", 1)
            ),
            output = RecipeOutput("twig_spear", 1)
        )
        
        assertEquals("3x twig, 1x acorn_cap", recipe.formattedInputs())
    }
    
    @Test
    fun `Recipe formattedOutput should format correctly`() {
        val recipe = Recipe(
            id = "test",
            name = "Test",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(RecipeInput("twig", 3)),
            output = RecipeOutput("twig_spear", 2)
        )
        
        assertEquals("2x twig_spear", recipe.formattedOutput())
    }
    
    // ===== RECIPE CATALOG TESTS =====
    
    @Test
    fun `RecipeCatalog should contain expected recipes`() {
        val allRecipes = RecipeCatalog.getAllRecipes()
        
        assertTrue(allRecipes.isNotEmpty())
        assertTrue(allRecipes.size >= 10)  // At least 10 initial recipes
        
        assertNotNull(RecipeCatalog.getRecipe("twig_spear"))
        assertNotNull(RecipeCatalog.getRecipe("acorn_helmet"))
        assertNotNull(RecipeCatalog.getRecipe("leaf_cloak"))
        assertNotNull(RecipeCatalog.getRecipe("feather_charm"))
    }
    
    @Test
    fun `RecipeCatalog getRecipesByCategory should filter correctly`() {
        val equipmentRecipes = RecipeCatalog.getRecipesByCategory(CraftingCategory.EQUIPMENT)
        val consumableRecipes = RecipeCatalog.getRecipesByCategory(CraftingCategory.CONSUMABLE)
        
        assertTrue(equipmentRecipes.isNotEmpty())
        assertTrue(consumableRecipes.isNotEmpty())
        
        equipmentRecipes.forEach { recipe ->
            assertEquals(CraftingCategory.EQUIPMENT, recipe.category)
        }
    }
    
    @Test
    fun `RecipeCatalog getAvailableRecipes should filter by level`() {
        val lowLevelPlayer = createTestPlayer(level = 1)
        val highLevelPlayer = createTestPlayer(level = 10)
        
        val lowLevelRecipes = RecipeCatalog.getAvailableRecipes(lowLevelPlayer)
        val highLevelRecipes = RecipeCatalog.getAvailableRecipes(highLevelPlayer)
        
        // High level player should have access to more or equal recipes
        assertTrue(highLevelRecipes.size >= lowLevelRecipes.size)
        
        // All recipes for low level player should have requiredLevel <= 1
        lowLevelRecipes.forEach { recipe ->
            assertTrue(recipe.requiredLevel <= 1)
        }
    }
    
    @Test
    fun `RecipeCatalog validateRecipes should pass`() {
        assertTrue(RecipeCatalog.validateRecipes())
    }
    
    // ===== CRAFTING MANAGER - SUCCESSFUL CRAFTING TESTS =====
    
    @Test
    fun `craft should successfully craft twig spear`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 3)  // Exact materials needed
            )
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "twig_spear")
        
        assertTrue(result is CraftingResult.Success)
        val success = result as CraftingResult.Success
        assertEquals("twig_spear", success.itemCrafted)
        assertEquals(1, success.quantityCrafted)
        assertEquals(1, success.materialsConsumed.size)
        assertEquals("twig" to 3, success.materialsConsumed[0])
        
        // Verify materials removed
        assertEquals(0, newPlayer.inventory.getItemQuantity("twig"))
        
        // Verify item added
        assertEquals(1, newPlayer.inventory.getItemQuantity("twig_spear"))
    }
    
    @Test
    fun `craft should successfully craft acorn helmet with multiple materials`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("acorn_cap", 1),
                InventorySlot("grass_blade", 2)
            )
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "acorn_helmet")
        
        assertTrue(result is CraftingResult.Success)
        val success = result as CraftingResult.Success
        assertEquals("acorn_helmet", success.itemCrafted)
        assertEquals(2, success.materialsConsumed.size)
        
        // Verify materials removed
        assertEquals(0, newPlayer.inventory.getItemQuantity("acorn_cap"))
        assertEquals(0, newPlayer.inventory.getItemQuantity("grass_blade"))
        
        // Verify item added
        assertEquals(1, newPlayer.inventory.getItemQuantity("acorn_helmet"))
    }
    
    @Test
    fun `craft should work with excess materials`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 10)  // More than needed (need 3)
            )
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "twig_spear")
        
        assertTrue(result is CraftingResult.Success)
        
        // Verify only required amount consumed, excess remains
        assertEquals(7, newPlayer.inventory.getItemQuantity("twig"))
        assertEquals(1, newPlayer.inventory.getItemQuantity("twig_spear"))
    }
    
    // ===== CRAFTING MANAGER - FAILURE TESTS =====
    
    @Test
    fun `craft should fail when recipe not found`() {
        val player = createTestPlayer()
        
        val (newPlayer, result) = CraftingManager.craft(player, "nonexistent_recipe")
        
        assertTrue(result is CraftingResult.Failure.RecipeNotFound)
        assertEquals(player, newPlayer)  // Player unchanged
    }
    
    @Test
    fun `craft should fail when level too low`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("feather", 1),
                InventorySlot("shiny_button", 1)
            )
        )
        
        // feather_charm requires level 5
        val (newPlayer, result) = CraftingManager.craft(player, "feather_charm")
        
        assertTrue(result is CraftingResult.Failure.LevelTooLow)
        val failure = result as CraftingResult.Failure.LevelTooLow
        assertEquals(5, failure.requiredLevel)
        assertEquals(1, failure.playerLevel)
        
        // Materials not consumed
        assertEquals(1, newPlayer.inventory.getItemQuantity("feather"))
        assertEquals(1, newPlayer.inventory.getItemQuantity("shiny_button"))
    }
    
    @Test
    fun `craft should fail when insufficient materials`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 2)  // Need 3 for twig_spear
            )
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "twig_spear")
        
        assertTrue(result is CraftingResult.Failure.InsufficientMaterials)
        val failure = result as CraftingResult.Failure.InsufficientMaterials
        assertEquals(1, failure.missing.size)
        assertEquals("twig" to 1, failure.missing[0])  // Missing 1 twig
        
        // Materials not consumed
        assertEquals(2, newPlayer.inventory.getItemQuantity("twig"))
    }
    
    @Test
    fun `craft should fail when missing one of multiple materials`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("acorn_cap", 1)
                // Missing grass_blade
            )
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "acorn_helmet")
        
        assertTrue(result is CraftingResult.Failure.InsufficientMaterials)
        val failure = result as CraftingResult.Failure.InsufficientMaterials
        assertTrue(failure.missing.any { it.first == "grass_blade" })
    }
    
    @Test
    fun `craft should fail when inventory full`() {
        // Create player with inventory at max slots
        val fullSlots = (1..20).map { InventorySlot("twig", 99) }.toMutableList()
        fullSlots[0] = InventorySlot("twig", 3)  // Only 3 twigs for crafting
        
        val player = createTestPlayer(
            level = 1,
            inventorySlots = fullSlots
        )
        
        val (newPlayer, result) = CraftingManager.craft(player, "twig_spear")
        
        assertTrue(result is CraftingResult.Failure.InventoryFull)
        
        // Materials not consumed when inventory full
        assertTrue(newPlayer.inventory.getItemQuantity("twig") >= 3)
    }
    
    // ===== VALIDATION TESTS =====
    
    @Test
    fun `validateMaterials should return Sufficient when materials available`() {
        val player = createTestPlayer(
            inventorySlots = listOf(
                InventorySlot("twig", 5)
            )
        )
        
        val recipe = RecipeCatalog.getRecipe("twig_spear")!!
        val validation = CraftingManager.validateMaterials(player, recipe)
        
        assertTrue(validation is MaterialValidation.Sufficient)
    }
    
    @Test
    fun `validateMaterials should return Insufficient with missing amounts`() {
        val player = createTestPlayer(
            inventorySlots = listOf(
                InventorySlot("twig", 1)  // Need 3
            )
        )
        
        val recipe = RecipeCatalog.getRecipe("twig_spear")!!
        val validation = CraftingManager.validateMaterials(player, recipe)
        
        assertTrue(validation is MaterialValidation.Insufficient)
        val insufficient = validation as MaterialValidation.Insufficient
        assertEquals("twig" to 2, insufficient.missing[0])  // Missing 2 twigs
    }
    
    @Test
    fun `canCraft should return true when all conditions met`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 3)
            )
        )
        
        assertTrue(CraftingManager.canCraft(player, "twig_spear"))
    }
    
    @Test
    fun `canCraft should return false when level too low`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("feather", 1),
                InventorySlot("shiny_button", 1)
            )
        )
        
        assertFalse(CraftingManager.canCraft(player, "feather_charm"))  // Requires level 5
    }
    
    @Test
    fun `canCraft should return false when missing materials`() {
        val player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 1)  // Need 3
            )
        )
        
        assertFalse(CraftingManager.canCraft(player, "twig_spear"))
    }
    
    @Test
    fun `canCraft should return false when recipe not found`() {
        val player = createTestPlayer(level = 1)
        
        assertFalse(CraftingManager.canCraft(player, "nonexistent_recipe"))
    }
    
    // ===== HELPER METHOD TESTS =====
    
    @Test
    fun `getCraftableRecipes should return only craftable recipes`() {
        val player = createTestPlayer(
            level = 5,
            inventorySlots = listOf(
                InventorySlot("twig", 10),
                InventorySlot("acorn_cap", 5),
                InventorySlot("grass_blade", 10)
            )
        )
        
        val craftableRecipes = CraftingManager.getCraftableRecipes(player)
        
        // Should include recipes player can actually craft right now
        assertTrue(craftableRecipes.any { it.id == "twig_spear" })
        assertTrue(craftableRecipes.any { it.id == "acorn_helmet" })
        
        // All returned recipes should pass canCraft
        craftableRecipes.forEach { recipe ->
            assertTrue(CraftingManager.canCraft(player, recipe.id))
        }
    }
    
    @Test
    fun `getUnlockedRecipes should return level-appropriate recipes only`() {
        val lowLevelPlayer = createTestPlayer(level = 1)
        val highLevelPlayer = createTestPlayer(level = 10)
        
        val lowUnlocked = CraftingManager.getUnlockedRecipes(lowLevelPlayer)
        val highUnlocked = CraftingManager.getUnlockedRecipes(highLevelPlayer)
        
        // High level should have more unlocked
        assertTrue(highUnlocked.size >= lowUnlocked.size)
        
        // Low level shouldn't have feather_charm (level 5 required)
        assertFalse(lowUnlocked.any { it.id == "feather_charm" })
        
        // High level should have feather_charm
        assertTrue(highUnlocked.any { it.id == "feather_charm" })
    }
    
    // ===== COMPONENT VALIDATION TESTS =====
    
    @Test
    fun `RecipeInput should validate quantity positive`() {
        assertFails {
            RecipeInput("twig", 0)
        }
        assertFails {
            RecipeInput("twig", -1)
        }
    }
    
    @Test
    fun `RecipeOutput should validate quantity positive`() {
        assertFails {
            RecipeOutput("twig_spear", 0)
        }
        assertFails {
            RecipeOutput("twig_spear", -1)
        }
    }
    
    // ===== INTEGRATION TEST =====
    
    @Test
    fun `crafting workflow should work end-to-end`() {
        // Start with materials
        var player = createTestPlayer(
            level = 1,
            inventorySlots = listOf(
                InventorySlot("twig", 10),
                InventorySlot("acorn_cap", 2),
                InventorySlot("grass_blade", 10)
            )
        )
        
        // Craft twig spear
        val (player2, result1) = CraftingManager.craft(player, "twig_spear")
        assertTrue(result1 is CraftingResult.Success)
        assertEquals(7, player2.inventory.getItemQuantity("twig"))  // 10 - 3 = 7
        assertEquals(1, player2.inventory.getItemQuantity("twig_spear"))
        
        // Craft acorn helmet
        val (player3, result2) = CraftingManager.craft(player2, "acorn_helmet")
        assertTrue(result2 is CraftingResult.Success)
        assertEquals(1, player3.inventory.getItemQuantity("acorn_cap"))  // 2 - 1 = 1
        assertEquals(8, player3.inventory.getItemQuantity("grass_blade"))  // 10 - 2 = 8
        assertEquals(1, player3.inventory.getItemQuantity("acorn_helmet"))
        
        // Now have 2 equipment items
        assertTrue(InventoryManager.hasItem(player3.inventory, "twig_spear"))
        assertTrue(InventoryManager.hasItem(player3.inventory, "acorn_helmet"))
    }
}
