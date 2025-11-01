package com.jalmarquest.shared.crafting

import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.ItemAddResult
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.model.Player

/**
 * Stateless manager for crafting operations.
 * Follows the functional pattern established by InventoryManager, CurrencyManager, and EquipmentManager.
 * 
 * All operations return a new Player instance and a result, maintaining immutability.
 */
object CraftingManager {
    
    /**
     * Attempts to craft an item using the specified recipe.
     * 
     * Validation steps:
     * 1. Recipe exists in catalog
     * 2. Player meets level requirement
     * 3. Player has all required materials in inventory
     * 4. Inventory has space for crafted item
     * 
     * If all validations pass:
     * - Removes materials from inventory
     * - Adds crafted item to inventory
     * - Returns Success result
     * 
     * @param player The player attempting to craft
     * @param recipeId The ID of the recipe to craft
     * @return Pair of new Player state and crafting result
     */
    fun craft(player: Player, recipeId: String): Pair<Player, CraftingResult> {
        // Validate recipe exists
        val recipe = RecipeCatalog.getRecipe(recipeId)
            ?: return player to CraftingResult.Failure.RecipeNotFound(recipeId)
        
        // Validate player level
        if (player.level < recipe.requiredLevel) {
            return player to CraftingResult.Failure.LevelTooLow(
                requiredLevel = recipe.requiredLevel,
                playerLevel = player.level
            )
        }
        
        // Validate materials
        val materialCheck = validateMaterials(player, recipe)
        if (materialCheck !is MaterialValidation.Sufficient) {
            return player to CraftingResult.Failure.InsufficientMaterials(
                missing = (materialCheck as MaterialValidation.Insufficient).missing
            )
        }
        
        // Validate inventory space for output
        if (!player.inventory.canFit(recipe.output.itemId, recipe.output.quantity)) {
            return player to CraftingResult.Failure.InventoryFull
        }
        
        // Execute crafting: remove materials then add output
        var currentPlayer = player
        val consumedMaterials = mutableListOf<Pair<String, Int>>()
        
        // Remove all input materials
        recipe.inputs.forEach { input ->
            val (newPlayer, _) = InventoryManager.removeItem(
                currentPlayer.inventory,
                input.itemId,
                input.quantity
            )
            currentPlayer = currentPlayer.copy(inventory = newPlayer)
            consumedMaterials.add(input.itemId to input.quantity)
        }
        
        // Add crafted item
        val (finalInventory, addResult) = InventoryManager.addItem(
            currentPlayer.inventory,
            recipe.output.itemId,
            recipe.output.quantity
        )
        
        // Check if item was successfully added
        when (addResult) {
            is ItemAddResult.Success -> {
                val finalPlayer = currentPlayer.copy(inventory = finalInventory)
                return finalPlayer to CraftingResult.Success(
                    recipe = recipe,
                    itemCrafted = recipe.output.itemId,
                    quantityCrafted = recipe.output.quantity,
                    materialsConsumed = consumedMaterials
                )
            }
            else -> {
                // This shouldn't happen since we validated canFit, but handle gracefully
                return currentPlayer to CraftingResult.Failure.InventoryFull
            }
        }
    }
    
    /**
     * Checks if the player can craft the specified recipe.
     * Returns true only if ALL conditions are met:
     * - Recipe exists
     * - Player meets level requirement
     * - Player has all required materials
     * - Inventory has space for output
     * 
     * @param player The player to check
     * @param recipeId The recipe to check
     * @return True if player can craft, false otherwise
     */
    fun canCraft(player: Player, recipeId: String): Boolean {
        val recipe = RecipeCatalog.getRecipe(recipeId) ?: return false
        
        // Check level
        if (player.level < recipe.requiredLevel) return false
        
        // Check materials
        if (validateMaterials(player, recipe) !is MaterialValidation.Sufficient) return false
        
        // Check inventory space
        if (!player.inventory.canFit(recipe.output.itemId, recipe.output.quantity)) return false
        
        return true
    }
    
    /**
     * Validates that the player has all required materials for a recipe.
     * 
     * @param player The player whose inventory to check
     * @param recipe The recipe to validate materials for
     * @return MaterialValidation result
     */
    fun validateMaterials(player: Player, recipe: Recipe): MaterialValidation {
        val missing = mutableListOf<Pair<String, Int>>()  // (itemId, deficit)
        
        recipe.inputs.forEach { input ->
            val hasQuantity = player.inventory.getItemQuantity(input.itemId)
            if (hasQuantity < input.quantity) {
                missing.add(input.itemId to (input.quantity - hasQuantity))
            }
        }
        
        return if (missing.isEmpty()) {
            MaterialValidation.Sufficient
        } else {
            MaterialValidation.Insufficient(missing)
        }
    }
    
    /**
     * Returns a list of recipes the player can currently craft.
     * Filters by level requirement AND available materials.
     * 
     * @param player The player to check
     * @return List of craftable recipes
     */
    fun getCraftableRecipes(player: Player): List<Recipe> {
        return RecipeCatalog.getAllRecipes().filter { recipe ->
            canCraft(player, recipe.id)
        }
    }
    
    /**
     * Returns a list of recipes the player meets the level requirement for.
     * Does NOT check materials or inventory space.
     * 
     * @param player The player to check
     * @return List of level-appropriate recipes
     */
    fun getUnlockedRecipes(player: Player): List<Recipe> {
        return RecipeCatalog.getAvailableRecipes(player)
    }
}

// ===== RESULT TYPES =====

/**
 * Result of a crafting operation.
 */
sealed class CraftingResult {
    data class Success(
        val recipe: Recipe,
        val itemCrafted: String,
        val quantityCrafted: Int,
        val materialsConsumed: List<Pair<String, Int>>
    ) : CraftingResult()
    
    sealed class Failure : CraftingResult() {
        data class RecipeNotFound(val recipeId: String) : Failure()
        data class LevelTooLow(val requiredLevel: Int, val playerLevel: Int) : Failure()
        data class InsufficientMaterials(val missing: List<Pair<String, Int>>) : Failure()
        object InventoryFull : Failure()
    }
}

/**
 * Result of material validation.
 */
sealed class MaterialValidation {
    object Sufficient : MaterialValidation()
    data class Insufficient(val missing: List<Pair<String, Int>>) : MaterialValidation()
}
