package com.jalmarquest.shared.inventory

import com.jalmarquest.shared.equipment.EquipmentSlot
import com.jalmarquest.shared.equipment.StatModifier
import kotlinx.serialization.Serializable

/**
 * Represents an item in JalmarQuest.
 * 
 * Weight is measured in MILLIGRAMS for quail-scale precision:
 * - Button quail body weight: ~50,000mg (50g)
 * - Max realistic carry capacity: ~12,000mg (12g, ~24% body weight)
 * - Seed: ~10mg (0.01g)
 * - Twig: ~500mg (0.5g)
 * - Acorn cap: ~300mg (0.3g)
 * - Small pebble: ~2,000mg (2g)
 * 
 * @property id Unique identifier (e.g., "twig_spear", "sunflower_seed")
 * @property name Display name for UI
 * @property description Flavor text describing the item
 * @property type Item category (consumable, equipment, material, etc.)
 * @property rarity Rarity tier affecting value and appearance
 * @property value Base vendor value in Seeds
 * @property weight Weight in MILLIGRAMS (quail-scale realism)
 * @property stackable Whether multiple instances can occupy one inventory slot
 * @property maxStack Maximum quantity per stack (1-99)
 * @property usable Whether the item can be used directly (e.g., consume food)
 * @property consumable Whether the item is destroyed upon use
 * @property questItem Whether this is a quest-specific item (usually non-droppable)
 * @property equipmentSlot If EQUIPMENT type, which slot it occupies (null otherwise)
 * @property stats If EQUIPMENT type, stat modifiers it provides (null otherwise)
 * @property maxDurability If EQUIPMENT type, starting/max durability (null otherwise)
 * @property setId Optional set identifier for set bonus mechanics (null if not part of a set)
 */
@Serializable
data class Item(
    val id: String,
    val name: String,
    val description: String,
    val type: ItemType,
    val rarity: ItemRarity = ItemRarity.COMMON,
    val value: Int = 0,
    val weight: Int = 1,
    val stackable: Boolean = true,
    val maxStack: Int = 99,
    val usable: Boolean = false,
    val consumable: Boolean = false,
    val questItem: Boolean = false,
    val equipmentSlot: EquipmentSlot? = null,
    val stats: StatModifier? = null,
    val maxDurability: Int? = null,
    val setId: String? = null
) {
    init {
        require(id.isNotBlank()) { "Item ID cannot be blank" }
        require(name.isNotBlank()) { "Item name cannot be blank" }
        require(value >= 0) { "Item value cannot be negative: $value" }
        require(weight > 0) { "Item weight must be positive: $weight" }
        require(maxStack in 1..99) { "Max stack must be 1-99: $maxStack" }
        require(!stackable || maxStack > 1) { "Stackable items must have maxStack > 1" }
        
        // Equipment-specific validation
        if (type == ItemType.EQUIPMENT) {
            require(equipmentSlot != null) { "EQUIPMENT type items must have an equipment slot" }
            require(stats != null) { "EQUIPMENT type items must have stats (use StatModifier() for zero stats)" }
            require(maxDurability != null && maxDurability > 0) { 
                "EQUIPMENT type items must have positive max durability: $maxDurability" 
            }
            require(!stackable) { "EQUIPMENT items cannot be stackable" }
            require(maxStack == 1) { "EQUIPMENT items must have maxStack = 1" }
        } else {
            require(equipmentSlot == null) { "Only EQUIPMENT type items can have an equipment slot" }
            require(stats == null) { "Only EQUIPMENT type items can have stats" }
            require(maxDurability == null) { "Only EQUIPMENT type items can have durability" }
        }
    }
    
    /**
     * Returns the weight in grams for human-readable display.
     * Example: 500mg → 0.5g
     */
    fun weightInGrams(): Double = weight / 1000.0
    
    /**
     * Returns a formatted weight string for UI display.
     * Example: "0.5g", "2.0g", "0.01g"
     */
    fun formattedWeight(): String {
        val grams = weightInGrams()
        return if (grams < 0.1) {
            String.format(java.util.Locale.US, "%.2fg", grams)  // Show 2 decimals for tiny items
        } else {
            String.format(java.util.Locale.US, "%.1fg", grams)  // Show 1 decimal for larger items
        }
    }
}
