package com.jalmarquest.shared.crafting

import kotlinx.serialization.Serializable

/**
 * Defines a crafting recipe in JalmarQuest.
 * Recipes transform input materials into output items.
 */
@Serializable
data class Recipe(
    /** Unique identifier for this recipe */
    val id: String,
    
    /** Display name for UI */
    val name: String,
    
    /** Category of the crafted item */
    val category: CraftingCategory,
    
    /** Required materials with quantities */
    val inputs: List<RecipeInput>,
    
    /** Output item and quantity */
    val output: RecipeOutput,
    
    /** Minimum player level required (default 1) */
    val requiredLevel: Int = 1,
    
    /** Description/flavor text */
    val description: String = ""
) {
    init {
        require(id.isNotBlank()) { "Recipe ID cannot be blank" }
        require(name.isNotBlank()) { "Recipe name cannot be blank" }
        require(inputs.isNotEmpty()) { "Recipe must have at least one input" }
        require(requiredLevel >= 1) { "Required level must be at least 1: $requiredLevel" }
    }
    
    /**
     * Returns a formatted string of required materials for UI display.
     * Example: "3x Twig, 1x Acorn Cap"
     */
    fun formattedInputs(): String {
        return inputs.joinToString(", ") { "${it.quantity}x ${it.itemId}" }
    }
    
    /**
     * Returns a formatted string of the output for UI display.
     * Example: "1x Twig Spear"
     */
    fun formattedOutput(): String {
        return "${output.quantity}x ${output.itemId}"
    }
}
