package com.jalmarquest.shared.equipment

import kotlinx.serialization.Serializable

/**
 * Represents an equipped item with durability tracking.
 * References an Item from ItemCatalog by ID.
 */
@Serializable
data class Equipment(
    /** ID of the item from ItemCatalog */
    val itemId: String,
    
    /** Equipment slot this item occupies */
    val slot: EquipmentSlot,
    
    /** Current durability (0 = broken) */
    val currentDurability: Int,
    
    /** Maximum durability when fully repaired */
    val maxDurability: Int
) {
    init {
        require(itemId.isNotBlank()) { "Item ID cannot be blank" }
        require(currentDurability >= 0) { "Current durability cannot be negative: $currentDurability" }
        require(maxDurability > 0) { "Max durability must be positive: $maxDurability" }
        require(currentDurability <= maxDurability) { 
            "Current durability ($currentDurability) cannot exceed max ($maxDurability)" 
        }
    }
    
    /**
     * Returns true if the item is broken (0 durability).
     */
    fun isBroken(): Boolean = currentDurability == 0
    
    /**
     * Returns the durability as a percentage (0.0 to 1.0).
     */
    fun durabilityPercentage(): Double {
        return currentDurability.toDouble() / maxDurability.toDouble()
    }
    
    /**
     * Returns a formatted durability string for UI (e.g., "75/100").
     */
    fun formattedDurability(): String {
        return "$currentDurability/$maxDurability"
    }
}
