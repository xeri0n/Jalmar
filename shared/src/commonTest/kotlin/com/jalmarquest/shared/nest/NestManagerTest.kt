package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.ItemAddResult
import com.jalmarquest.shared.inventory.ItemCatalog
import kotlin.test.*

class NestManagerTest {
    
    // Helper function to create inventory with materials
    // Uses increased maxWeight to accommodate materials needed for nest upgrades
    private fun createInventoryWithMaterials(materials: Map<String, Int>): Inventory {
        // Calculate total weight needed
        val totalWeight = materials.entries.sumOf { (itemId, quantity) ->
            val item = ItemCatalog.getItem(itemId) ?: error("Item $itemId not found in catalog")
            item.weight * quantity
        }
        
        // Create inventory with enough capacity (add 20% buffer)
        var inventory = Inventory(maxWeight = (totalWeight * 1.2).toInt())
        
        materials.forEach { (itemId, quantity) ->
            val (newInventory, result) = InventoryManager.addItem(inventory, itemId, quantity)
            // Verify item was added successfully
            if (result !is ItemAddResult.Success) {
                throw IllegalArgumentException("Failed to add $quantity x $itemId to test inventory: $result. Total weight: ${inventory.currentWeight()}/${inventory.maxWeight}")
            }
            inventory = newInventory
        }
        return inventory
    }
    
    // ==== BASIC NEST CREATION TESTS ====
    
    @Test
    fun `createBasicNest should create BASIC tier nest`() {
        val nest = NestManager.createBasicNest()
        
        assertEquals(NestTier.BASIC, nest.tier)
        assertEquals("player_nest", nest.id)
        assertNull(nest.customName)
    }
    
    @Test
    fun `createBasicNest should accept custom name`() {
        val nest = NestManager.createBasicNest(customName = "My Cozy Nest")
        
        assertEquals("My Cozy Nest", nest.customName)
        assertEquals("My Cozy Nest", nest.getDisplayName())
    }
    
    @Test
    fun `createBasicNest should accept custom ID`() {
        val nest = NestManager.createBasicNest(id = "special_nest")
        
        assertEquals("special_nest", nest.id)
    }
    
    // ==== RENAME NEST TESTS ====
    
    @Test
    fun `renameNest should update custom name`() {
        val nest = NestManager.createBasicNest()
        val renamed = NestManager.renameNest(nest, "The Gilded Perch")
        
        assertEquals("The Gilded Perch", renamed.customName)
        assertEquals(nest.id, renamed.id)
        assertEquals(nest.tier, renamed.tier)
    }
    
    @Test
    fun `renameNest should clear custom name when null provided`() {
        val nest = NestManager.createBasicNest(customName = "Old Name")
        val renamed = NestManager.renameNest(nest, null)
        
        assertNull(renamed.customName)
        assertEquals("Simple Nest", renamed.getDisplayName())
    }
    
    // ==== STAT MODIFIERS TESTS ====
    
    @Test
    fun `getStatModifiers should return correct modifiers for BASIC`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val modifiers = NestManager.getStatModifiers(nest)
        
        assertEquals(1.0f, modifiers.hpRegenMultiplier)
        assertEquals(1.0f, modifiers.staminaRegenMultiplier)
        assertEquals(1.0f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `getStatModifiers should return correct modifiers for COMFORTABLE`() {
        val nest = Nest(id = "test", tier = NestTier.COMFORTABLE)
        val modifiers = NestManager.getStatModifiers(nest)
        
        assertEquals(1.10f, modifiers.hpRegenMultiplier)
        assertEquals(1.05f, modifiers.staminaRegenMultiplier)
        assertEquals(1.0f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `getStatModifiers should return correct modifiers for LUXURIOUS`() {
        val nest = Nest(id = "test", tier = NestTier.LUXURIOUS)
        val modifiers = NestManager.getStatModifiers(nest)
        
        assertEquals(1.20f, modifiers.hpRegenMultiplier)
        assertEquals(1.10f, modifiers.staminaRegenMultiplier)
        assertEquals(1.05f, modifiers.xpGainMultiplier)
    }
    
    // ==== VISUAL STATE TESTS ====
    
    @Test
    fun `getVisualState should return correct state for each tier`() {
        val basicNest = Nest(id = "test", tier = NestTier.BASIC)
        val comfortableNest = Nest(id = "test", tier = NestTier.COMFORTABLE)
        val luxuriousNest = Nest(id = "test", tier = NestTier.LUXURIOUS)
        
        val basicVisual = NestManager.getVisualState(basicNest)
        val comfortableVisual = NestManager.getVisualState(comfortableNest)
        val luxuriousVisual = NestManager.getVisualState(luxuriousNest)
        
        assertEquals(NestTier.BASIC, basicVisual.tier)
        assertEquals(NestTier.COMFORTABLE, comfortableVisual.tier)
        assertEquals(NestTier.LUXURIOUS, luxuriousVisual.tier)
        
        assertTrue(basicVisual.asciiArt.isNotBlank())
        assertTrue(comfortableVisual.asciiArt.contains("Cozy"))
        assertTrue(luxuriousVisual.asciiArt.contains("Paradise"))
    }
    
    // ==== UPGRADE REQUIREMENTS TESTS ====
    
    @Test
    fun `getUpgradeRequirements should return requirements for BASIC nest`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val requirements = NestManager.getUpgradeRequirements(nest)
        
        assertNotNull(requirements)
        assertEquals(NestTier.COMFORTABLE, requirements.targetTier)
        assertEquals(20, requirements.requiredMaterials["twig"])
        assertEquals(30, requirements.requiredMaterials["dried_leaf"])
        assertEquals(10, requirements.requiredMaterials["grass_blade"])
    }
    
    @Test
    fun `getUpgradeRequirements should return requirements for COMFORTABLE nest`() {
        val nest = Nest(id = "test", tier = NestTier.COMFORTABLE)
        val requirements = NestManager.getUpgradeRequirements(nest)
        
        assertNotNull(requirements)
        assertEquals(NestTier.LUXURIOUS, requirements.targetTier)
        assertEquals(50, requirements.requiredMaterials["twig"])
        assertEquals(40, requirements.requiredMaterials["dried_leaf"])
        assertEquals(20, requirements.requiredMaterials["grass_blade"])
        assertEquals(10, requirements.requiredMaterials["spider_silk"])
        assertEquals(5, requirements.requiredMaterials["feather"])
    }
    
    @Test
    fun `getUpgradeRequirements should return null for LUXURIOUS nest`() {
        val nest = Nest(id = "test", tier = NestTier.LUXURIOUS)
        val requirements = NestManager.getUpgradeRequirements(nest)
        
        assertNull(requirements)
    }
    
    // ==== CAN UPGRADE TESTS ====
    
    @Test
    fun `canUpgrade should return true when all conditions met`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        assertTrue(NestManager.canUpgrade(nest, playerLevel = 5, inventory))
    }
    
    @Test
    fun `canUpgrade should return false when player level too low`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        assertFalse(NestManager.canUpgrade(nest, playerLevel = 4, inventory))
    }
    
    @Test
    fun `canUpgrade should return false when materials insufficient`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 10,  // Need 20
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        assertFalse(NestManager.canUpgrade(nest, playerLevel = 5, inventory))
    }
    
    @Test
    fun `canUpgrade should return false for max tier nest`() {
        val nest = Nest(id = "test", tier = NestTier.LUXURIOUS)
        val inventory = Inventory()
        
        assertFalse(NestManager.canUpgrade(nest, playerLevel = 50, inventory))
    }
    
    // ==== UPGRADE SUCCESS TESTS ====
    
    @Test
    fun `upgradeNest should succeed from BASIC to COMFORTABLE with valid conditions`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 25,  // Extra materials
                "dried_leaf" to 35,
                "grass_blade" to 15
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals(NestTier.COMFORTABLE, newNest.tier)
        
        // Verify materials were consumed
        assertEquals(5, newInventory.getItemQuantity("twig"))  // 25 - 20
        assertEquals(5, newInventory.getItemQuantity("dried_leaf"))  // 35 - 30
        assertEquals(5, newInventory.getItemQuantity("grass_blade"))  // 15 - 10
    }
    
    @Test
    fun `upgradeNest should succeed from COMFORTABLE to LUXURIOUS with valid conditions`() {
        val nest = Nest(id = "test", tier = NestTier.COMFORTABLE)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 50,
                "dried_leaf" to 40,
                "grass_blade" to 20,
                "spider_silk" to 10,
                "feather" to 5
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 10, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals(NestTier.LUXURIOUS, newNest.tier)
        
        // Verify all materials consumed
        assertEquals(0, newInventory.getItemQuantity("twig"))
        assertEquals(0, newInventory.getItemQuantity("dried_leaf"))
        assertEquals(0, newInventory.getItemQuantity("grass_blade"))
        assertEquals(0, newInventory.getItemQuantity("spider_silk"))
        assertEquals(0, newInventory.getItemQuantity("feather"))
    }
    
    @Test
    fun `upgradeNest should preserve custom name after upgrade`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC, customName = "My Special Nest")
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val (newNest, _, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals("My Special Nest", newNest.customName)
    }
    
    // ==== UPGRADE FAILURE TESTS ====
    
    @Test
    fun `upgradeNest should fail when player level too low`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 3, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure.LevelRequirementNotMet)
        val failure = result as NestUpgradeResult.Failure.LevelRequirementNotMet
        assertEquals(5, failure.requiredLevel)
        assertEquals(3, failure.playerLevel)
        
        // Nest and inventory should be unchanged
        assertEquals(nest, newNest)
        assertEquals(inventory, newInventory)
    }
    
    @Test
    fun `upgradeNest should fail when at max tier`() {
        val nest = Nest(id = "test", tier = NestTier.LUXURIOUS)
        val inventory = Inventory()
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 50, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure.AlreadyMaxTier)
        assertEquals(nest, newNest)
        assertEquals(inventory, newInventory)
    }
    
    @Test
    fun `upgradeNest should fail when missing one material`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 25,  // Need 30
                "grass_blade" to 10
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure.InsufficientMaterials)
        val failure = result as NestUpgradeResult.Failure.InsufficientMaterials
        assertEquals(5, failure.missingMaterials["dried_leaf"])
        
        // Nest and inventory should be unchanged
        assertEquals(nest, newNest)
        assertEquals(inventory, newInventory)
    }
    
    @Test
    fun `upgradeNest should fail when missing multiple materials`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 10,  // Need 20 (missing 10)
                "dried_leaf" to 20,  // Need 30 (missing 10)
                "grass_blade" to 10  // Correct amount
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure.InsufficientMaterials)
        val failure = result as NestUpgradeResult.Failure.InsufficientMaterials
        assertEquals(10, failure.missingMaterials["twig"])
        assertEquals(10, failure.missingMaterials["dried_leaf"])
        assertNull(failure.missingMaterials["grass_blade"])
        
        // Inventory should be unchanged (atomic operation)
        assertEquals(nest, newNest)
        assertEquals(inventory, newInventory)
    }
    
    @Test
    fun `upgradeNest should fail when completely missing a material`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30
                // Missing grass_blade entirely
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure.InsufficientMaterials)
        val failure = result as NestUpgradeResult.Failure.InsufficientMaterials
        assertEquals(10, failure.missingMaterials["grass_blade"])
    }
    
    // ==== ATOMIC OPERATION TESTS ====
    
    @Test
    fun `upgradeNest should consume all materials atomically on success`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val (_, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        
        // All materials should be consumed
        assertEquals(0, newInventory.getItemQuantity("twig"))
        assertEquals(0, newInventory.getItemQuantity("dried_leaf"))
        assertEquals(0, newInventory.getItemQuantity("grass_blade"))
    }
    
    @Test
    fun `upgradeNest should not consume any materials on failure`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 15,  // Insufficient
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val originalTwigCount = inventory.getItemQuantity("twig")
        val originalLeafCount = inventory.getItemQuantity("dried_leaf")
        val originalGrassCount = inventory.getItemQuantity("grass_blade")
        
        val (_, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Failure)
        
        // No materials should be consumed
        assertEquals(originalTwigCount, newInventory.getItemQuantity("twig"))
        assertEquals(originalLeafCount, newInventory.getItemQuantity("dried_leaf"))
        assertEquals(originalGrassCount, newInventory.getItemQuantity("grass_blade"))
    }
    
    // ==== EDGE CASE TESTS ====
    
    @Test
    fun `upgradeNest should work with exact material amounts`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,  // Exact
                "dried_leaf" to 30,  // Exact
                "grass_blade" to 10  // Exact
            )
        )
        
        val (newNest, newInventory, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals(NestTier.COMFORTABLE, newNest.tier)
        assertEquals(0, newInventory.getItemQuantity("twig"))
    }
    
    @Test
    fun `upgradeNest should work at exact level requirement`() {
        val nest = Nest(id = "test", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val (newNest, _, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)  // Exact level requirement
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals(NestTier.COMFORTABLE, newNest.tier)
    }
    
    @Test
    fun `upgradeNest should preserve nest ID after upgrade`() {
        val nest = Nest(id = "custom_nest_id", tier = NestTier.BASIC)
        val inventory = createInventoryWithMaterials(
            mapOf(
                "twig" to 20,
                "dried_leaf" to 30,
                "grass_blade" to 10
            )
        )
        
        val (newNest, _, result) = NestManager.upgradeNest(nest, playerLevel = 5, inventory)
        
        assertTrue(result is NestUpgradeResult.Success)
        assertEquals("custom_nest_id", newNest.id)
    }
}
