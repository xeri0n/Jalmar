package com.jalmarquest.shared.crafting

import kotlinx.serialization.Serializable

/**
 * Represents a required input material for a recipe.
 */
@Serializable
data class RecipeInput(
    /** Item ID from ItemCatalog */
    val itemId: String,
    
    /** Quantity required */
    val quantity: Int
) {
    init {
        require(itemId.isNotBlank()) { "Recipe input item ID cannot be blank" }
        require(quantity > 0) { "Recipe input quantity must be positive: $quantity" }
    }
}

/**
 * Represents the output of a crafting recipe.
 */
@Serializable
data class RecipeOutput(
    /** Item ID from ItemCatalog */
    val itemId: String,
    
    /** Quantity produced */
    val quantity: Int = 1
) {
    init {
        require(itemId.isNotBlank()) { "Recipe output item ID cannot be blank" }
        require(quantity > 0) { "Recipe output quantity must be positive: $quantity" }
    }
}
