package com.jalmarquest.shared.inventory

import kotlin.test.*

/**
 * Tests for the Inventory system including ItemCatalog, Item validation,
 * InventoryManager operations, stacking logic, capacity constraints, and serialization.
 */
class InventoryTest {
    
    // ==== ITEM CATALOG TESTS ====
    
    @Test
    fun `ItemCatalog should contain all expected items`() {
        val allItems = ItemCatalog.getAllItems()
        assertTrue(allItems.isNotEmpty(), "ItemCatalog should not be empty")
        
        // Verify key items exist
        assertNotNull(ItemCatalog.getItem("twig"))
        assertNotNull(ItemCatalog.getItem("acorn_cap"))
        assertNotNull(ItemCatalog.getItem("sunflower_seed"))
        assertNotNull(ItemCatalog.getItem("twig_spear"))
    }
    
    @Test
    fun `ItemCatalog should validate successfully`() {
        // Should not throw any exceptions
        ItemCatalog.validate()
    }
    
    @Test
    fun `ItemCatalog should filter by type correctly`() {
        val materials = ItemCatalog.getItemsByType(ItemType.MATERIAL)
        val equipment = ItemCatalog.getItemsByType(ItemType.EQUIPMENT)
        val consumables = ItemCatalog.getItemsByType(ItemType.CONSUMABLE)
        
        assertTrue(materials.isNotEmpty())
        assertTrue(equipment.isNotEmpty())
        assertTrue(consumables.isNotEmpty())
        
        // Verify types are correct
        assertTrue(materials.all { it.type == ItemType.MATERIAL })
        assertTrue(equipment.all { it.type == ItemType.EQUIPMENT })
        assertTrue(consumables.all { it.type == ItemType.CONSUMABLE })
    }
    
    // ==== ITEM VALIDATION TESTS ====
    
    @Test
    fun `Item should reject blank ID`() {
        assertFails {
            Item(
                id = "",
                name = "Test",
                description = "Test",
                type = ItemType.MATERIAL
            )
        }
    }
    
    @Test
    fun `Item should reject blank name`() {
        assertFails {
            Item(
                id = "test",
                name = "",
                description = "Test",
                type = ItemType.MATERIAL
            )
        }
    }
    
    @Test
    fun `Item should reject negative value`() {
        assertFails {
            Item(
                id = "test",
                name = "Test",
                description = "Test",
                type = ItemType.MATERIAL,
                value = -1
            )
        }
    }
    
    @Test
    fun `Item should reject zero or negative weight`() {
        assertFails {
            Item(
                id = "test",
                name = "Test",
                description = "Test",
                type = ItemType.MATERIAL,
                weight = 0
            )
        }
    }
    
    @Test
    fun `Item should reject invalid maxStack`() {
        assertFails {
            Item(
                id = "test",
                name = "Test",
                description = "Test",
                type = ItemType.MATERIAL,
                maxStack = 0
            )
        }
        
        assertFails {
            Item(
                id = "test",
                name = "Test",
                description = "Test",
                type = ItemType.MATERIAL,
                maxStack = 100
            )
        }
    }
    
    @Test
    fun `Item weight formatting should work correctly`() {
        val seed = Item(id = "test_seed", name = "Seed", description = "Test", type = ItemType.MATERIAL, weight = 10)
        assertEquals("0.01g", seed.formattedWeight())
        
        val twig = Item(id = "test_twig", name = "Twig", description = "Test", type = ItemType.MATERIAL, weight = 500)
        assertEquals("0.5g", twig.formattedWeight())
        
        val pebble = Item(id = "test_pebble", name = "Pebble", description = "Test", type = ItemType.MATERIAL, weight = 2000)
        assertEquals("2.0g", pebble.formattedWeight())
    }
    
    // ==== INVENTORY ADD TESTS ====
    
    @Test
    fun `addItem should add stackable items correctly`() {
        val inventory = Inventory()
        val (newInventory, result) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        
        assertTrue(result is ItemAddResult.Success)
        assertEquals(1, newInventory.currentSlotCount())
        assertEquals(10, newInventory.getItemQuantity("sunflower_seed"))
    }
    
    @Test
    fun `addItem should stack items up to maxStack`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 50)
        val (inv2, _) = InventoryManager.addItem(inv1, "sunflower_seed", 40)
        
        // Should have 2 slots: 99 + 1 (overflow to new stack)
        // Wait, max stack is 99, so 50 + 40 = 90 in one stack
        assertEquals(1, inv2.currentSlotCount())
        assertEquals(90, inv2.getItemQuantity("sunflower_seed"))
    }
    
    @Test
    fun `addItem should create new stack when maxStack exceeded`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 99)
        val (inv2, result) = InventoryManager.addItem(inv1, "sunflower_seed", 10)
        
        assertTrue(result is ItemAddResult.Success)
        assertEquals(2, inv2.currentSlotCount())
        assertEquals(109, inv2.getItemQuantity("sunflower_seed"))
    }
    
    @Test
    fun `addItem should reject invalid item ID`() {
        val inventory = Inventory()
        val (newInventory, result) = InventoryManager.addItem(inventory, "nonexistent_item", 1)
        
        assertTrue(result is ItemAddResult.Failure.InvalidItem)
        assertEquals(inventory, newInventory)  // Unchanged
    }
    
    @Test
    fun `addItem should fail when weight exceeded`() {
        val inventory = Inventory(maxWeight = 1000)  // 1g max
        val (newInventory, result) = InventoryManager.addItem(inventory, "pebble", 1)  // 2g pebble
        
        assertTrue(result is ItemAddResult.Failure.WeightExceeded)
        assertEquals(0, newInventory.currentSlotCount())
    }
    
    @Test
    fun `addItem should fail when slots full`() {
        val inventory = Inventory(maxSlots = 2)
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 1)
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        val (inv3, result) = InventoryManager.addItem(inv2, "pebble", 1)
        
        assertTrue(result is ItemAddResult.Failure.InventoryFull)
        assertEquals(2, inv3.currentSlotCount())
    }
    
    @Test
    fun `addItem should handle non-stackable items`() {
        val inventory = Inventory()
        val (newInventory, result) = InventoryManager.addItem(inventory, "twig_spear", 1)
        
        assertTrue(result is ItemAddResult.Success)
        assertEquals(1, newInventory.currentSlotCount())
        assertEquals(1, newInventory.getItemQuantity("twig_spear"))
    }
    
    @Test
    fun `addItem should require separate slots for non-stackable items`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig_spear", 2)
        
        assertEquals(2, inv1.currentSlotCount())  // 2 slots for 2 non-stackable items
    }
    
    // ==== INVENTORY REMOVE TESTS ====
    
    @Test
    fun `removeItem should remove items correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 50)
        val (inv2, result) = InventoryManager.removeItem(inv1, "sunflower_seed", 20)
        
        assertTrue(result is ItemRemoveResult.Success)
        assertEquals(30, inv2.getItemQuantity("sunflower_seed"))
    }
    
    @Test
    fun `removeItem should remove entire slot when quantity matches`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        val (inv2, result) = InventoryManager.removeItem(inv1, "sunflower_seed", 10)
        
        assertTrue(result is ItemRemoveResult.Success)
        assertEquals(0, inv2.currentSlotCount())
        assertEquals(0, inv2.getItemQuantity("sunflower_seed"))
    }
    
    @Test
    fun `removeItem should fail when item not found`() {
        val inventory = Inventory()
        val (newInventory, result) = InventoryManager.removeItem(inventory, "sunflower_seed", 1)
        
        assertTrue(result is ItemRemoveResult.ItemNotFound)
        assertEquals(inventory, newInventory)
    }
    
    @Test
    fun `removeItem should fail when insufficient quantity`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        val (inv2, result) = InventoryManager.removeItem(inv1, "sunflower_seed", 20)
        
        assertTrue(result is ItemRemoveResult.InsufficientQuantity)
        val failure = result as ItemRemoveResult.InsufficientQuantity
        assertEquals(10, failure.available)
        assertEquals(20, failure.requested)
    }
    
    // ==== INVENTORY CAPACITY TESTS ====
    
    @Test
    fun `Inventory should calculate weight correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 100)  // 100 * 10mg = 1000mg = 1g
        
        assertEquals(1000, inv1.currentWeight())
    }
    
    @Test
    fun `Inventory should track slot usage correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 5)
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 3)
        
        assertEquals(2, inv2.currentSlotCount())
        assertEquals(18, inv2.remainingSlots())
    }
    
    @Test
    fun `Inventory canFit should check weight constraints`() {
        val inventory = Inventory(maxWeight = 2000)  // 2g max
        assertTrue(inventory.canFit("sunflower_seed", 100))  // 1g
        assertFalse(inventory.canFit("pebble", 2))  // 4g (exceeds limit)
    }
    
    @Test
    fun `Inventory canFit should check slot constraints for stackable items`() {
        val inventory = Inventory(maxSlots = 2, maxWeight = 50000)  // 50g to avoid weight issues
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 50)
        
        // Slot 0 has 50 twigs (can fit 49 more since maxStack is 99)
        assertTrue(inv1.canFit("twig", 49))  // Can stack with existing
        assertTrue(inv1.canFit("twig", 40))  // Can stack with existing
        assertTrue(inv1.canFit("acorn_cap", 1), "Should fit acorn_cap in remaining slot")
        
        // Fill the second slot
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        assertFalse(inv2.canFit("pebble", 1))  // Now no slots remaining
    }
    
    // ==== SORT AND FILTER TESTS ====
    
    @Test
    fun `sortInventory should sort by name correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 1)
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        val (inv3, _) = InventoryManager.addItem(inv2, "pebble", 1)
        
        val sorted = InventoryManager.sortInventory(inv3, SortCriteria.NAME)
        
        assertEquals("acorn_cap", sorted.slots[0].itemId)  // "Acorn Cap" comes first
        assertEquals("pebble", sorted.slots[1].itemId)
        assertEquals("twig", sorted.slots[2].itemId)
    }
    
    @Test
    fun `sortInventory should sort by rarity correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig_spear", 1)  // Uncommon
        val (inv2, _) = InventoryManager.addItem(inv1, "twig", 1)  // Common
        val (inv3, _) = InventoryManager.addItem(inv2, "feather_charm", 1)  // Rare
        
        val sorted = InventoryManager.sortInventory(inv3, SortCriteria.RARITY)
        
        assertEquals("twig", sorted.slots[0].itemId)  // Common first
        assertEquals("twig_spear", sorted.slots[1].itemId)  // Uncommon
        assertEquals("feather_charm", sorted.slots[2].itemId)  // Rare last
    }
    
    @Test
    fun `filterItems should filter by type correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 1)  // Material
        val (inv2, _) = InventoryManager.addItem(inv1, "twig_spear", 1)  // Equipment
        val (inv3, _) = InventoryManager.addItem(inv2, "sunflower_seed", 1)  // Consumable
        
        val materials = InventoryManager.filterItems(inv3) { it.type == ItemType.MATERIAL }
        val equipment = InventoryManager.getEquipment(inv3)
        
        assertEquals(1, materials.size)
        assertEquals("twig", materials[0].itemId)
        assertEquals(1, equipment.size)
        assertEquals("twig_spear", equipment[0].itemId)
    }
    
    // ==== QUICK SLOT TESTS ====
    
    @Test
    fun `setQuickSlot should assign item to quick slot`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        val (inv2, success) = InventoryManager.setQuickSlot(inv1, 0, "sunflower_seed")
        
        assertTrue(success)
        assertEquals("sunflower_seed", inv2.quickSlots[0])
    }
    
    @Test
    fun `setQuickSlot should fail for item not in inventory`() {
        val inventory = Inventory()
        val (newInventory, success) = InventoryManager.setQuickSlot(inventory, 0, "sunflower_seed")
        
        assertFalse(success)
        assertNull(newInventory.quickSlots[0])
    }
    
    @Test
    fun `setQuickSlot should clear slot when null provided`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        val (inv2, _) = InventoryManager.setQuickSlot(inv1, 0, "sunflower_seed")
        val (inv3, success) = InventoryManager.setQuickSlot(inv2, 0, null)
        
        assertTrue(success)
        assertNull(inv3.quickSlots[0])
    }
    
    @Test
    fun `setQuickSlot should reject invalid slot index`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 10)
        val (_, success1) = InventoryManager.setQuickSlot(inv1, -1, "sunflower_seed")
        val (_, success2) = InventoryManager.setQuickSlot(inv1, 4, "sunflower_seed")
        
        assertFalse(success1)
        assertFalse(success2)
    }
    
    // ==== TRANSFER SLOT TESTS ====
    
    @Test
    fun `transferSlot should swap different items`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 1)
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        val (inv3, success) = InventoryManager.transferSlot(inv2, 0, 1)
        
        assertTrue(success)
        assertEquals("acorn_cap", inv3.slots[0].itemId)
        assertEquals("twig", inv3.slots[1].itemId)
    }
    
    @Test
    fun `transferSlot should stack same items`() {
        // Create inventory with TWO separate partial stacks that can be combined
        val inventory = Inventory()
        // Add 30 seeds
        val (inv1, _) = InventoryManager.addItem(inventory, "sunflower_seed", 30)
        // Add blocker to prevent auto-stacking
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        // Add 20 more seeds (will create NEW slot since we can't stack non-adjacent)
        // Wait, addItem WILL find the first slot and stack there!
        
        // Different approach: manually construct inventory with 2 separate stacks
        val slot1 = InventorySlot("sunflower_seed", 30)
        val slot2 = InventorySlot("sunflower_seed", 20)
        val testInv = Inventory(slots = listOf(slot1, slot2))
        
        assertEquals(2, testInv.currentSlotCount())
        
        // Transfer from slot 0 to slot 1
        val (result, success) = InventoryManager.transferSlot(testInv, 0, 1)
        
        assertTrue(success)
        // Should combine: 20 + 30 = 50 in one slot
        assertEquals(1, result.currentSlotCount())
        assertEquals(50, result.getItemQuantity("sunflower_seed"))
    }
    
    // ==== HELPER METHOD TESTS ====
    
    @Test
    fun `hasItem should check item presence correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 5)
        
        assertTrue(InventoryManager.hasItem(inv1, "twig", 1))
        assertTrue(InventoryManager.hasItem(inv1, "twig", 5))
        assertFalse(InventoryManager.hasItem(inv1, "twig", 6))
        assertFalse(InventoryManager.hasItem(inv1, "acorn_cap", 1))
    }
    
    @Test
    fun `getItemsByType should return correct items`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 1)
        val (inv2, _) = InventoryManager.addItem(inv1, "acorn_cap", 1)
        val (inv3, _) = InventoryManager.addItem(inv2, "sunflower_seed", 1)
        
        val materials = InventoryManager.getItemsByType(inv3, ItemType.MATERIAL)
        val consumables = InventoryManager.getConsumables(inv3)
        
        assertEquals(2, materials.size)  // twig, acorn_cap
        assertEquals(1, consumables.size)  // sunflower_seed
    }
    
    // ==== SERIALIZATION TEST ====
    
    @Test
    fun `Inventory should serialize and deserialize correctly`() {
        val inventory = Inventory()
        val (inv1, _) = InventoryManager.addItem(inventory, "twig", 10)
        val (inv2, _) = InventoryManager.addItem(inv1, "sunflower_seed", 50)
        val (inv3, _) = InventoryManager.setQuickSlot(inv2, 0, "sunflower_seed")
        
        val json = kotlinx.serialization.json.Json
        val serialized = json.encodeToString(Inventory.serializer(), inv3)
        val deserialized = json.decodeFromString(Inventory.serializer(), serialized)
        
        assertEquals(inv3.currentSlotCount(), deserialized.currentSlotCount())
        assertEquals(inv3.getItemQuantity("twig"), deserialized.getItemQuantity("twig"))
        assertEquals(inv3.getItemQuantity("sunflower_seed"), deserialized.getItemQuantity("sunflower_seed"))
        assertEquals(inv3.quickSlots[0], deserialized.quickSlots[0])
    }
}
