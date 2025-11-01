package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import kotlinx.serialization.Serializable

/**
 * Result of upgrading a nest.
 */
@Serializable
sealed class NestUpgradeResult {
    /** Successfully upgraded nest */
    data class Success(
        val newTier: NestTier,
        val materialsConsumed: Map<String, Int>
    ) : NestUpgradeResult()
    
    /** Failed to upgrade nest */
    sealed class Failure : NestUpgradeResult() {
        /** Player doesn't meet level requirement for target tier */
        data class LevelRequirementNotMet(val requiredLevel: Int, val playerLevel: Int) : Failure()
        
        /** Invalid tier progression (e.g., BASIC → LUXURIOUS, or downgrade) */
        data class InvalidProgression(val currentTier: NestTier, val targetTier: NestTier) : Failure()
        
        /** Already at maximum tier */
        data object AlreadyMaxTier : Failure()
        
        /** Missing required materials */
        data class InsufficientMaterials(val missingMaterials: Map<String, Int>) : Failure()
    }
}

/**
 * Manager for nest operations.
 * 
 * Uses functional approach - operations return new Nest/Inventory instances.
 * Thread-safety is handled by GameStateManager's Mutex when updating GameState.nest.
 * 
 * This is a stateless utility object, not a stateful manager requiring DI.
 */
object NestManager {
    
    /**
     * Attempts to upgrade a nest to the next tier.
     * 
     * Validates:
     * - Player level meets requirement
     * - Tier progression is valid (linear only)
     * - All materials are available in inventory
     * 
     * On success:
     * - Returns new Nest with upgraded tier
     * - Returns new Inventory with materials consumed
     * 
     * On failure:
     * - Returns original nest and inventory unchanged
     * 
     * @param currentNest Current nest to upgrade
     * @param playerLevel Player's current level (for requirement check)
     * @param inventory Player's inventory (for material consumption)
     * @return Triple of (new nest, new inventory, upgrade result)
     */
    fun upgradeNest(
        currentNest: Nest,
        playerLevel: Int,
        inventory: Inventory
    ): Triple<Nest, Inventory, NestUpgradeResult> {
        // Check if already at max tier
        val targetTier = currentNest.tier.getNextTier()
            ?: return Triple(
                currentNest,
                inventory,
                NestUpgradeResult.Failure.AlreadyMaxTier
            )
        
        // Validate level requirement
        if (playerLevel < targetTier.requiredLevel) {
            return Triple(
                currentNest,
                inventory,
                NestUpgradeResult.Failure.LevelRequirementNotMet(
                    requiredLevel = targetTier.requiredLevel,
                    playerLevel = playerLevel
                )
            )
        }
        
        // Validate tier progression (should always be valid if getNextTier() succeeded)
        if (!currentNest.tier.canUpgradeTo(targetTier)) {
            return Triple(
                currentNest,
                inventory,
                NestUpgradeResult.Failure.InvalidProgression(currentNest.tier, targetTier)
            )
        }
        
        // Get material requirements
        val requirements = NestUpgradeRequirements.forTier(currentNest.tier)
            ?: return Triple(
                currentNest,
                inventory,
                NestUpgradeResult.Failure.AlreadyMaxTier
            )
        
        // Check if all materials are available
        val missingMaterials = mutableMapOf<String, Int>()
        requirements.requiredMaterials.forEach { (itemId, requiredQuantity) ->
            val available = inventory.getItemQuantity(itemId)
            if (available < requiredQuantity) {
                missingMaterials[itemId] = requiredQuantity - available
            }
        }
        
        if (missingMaterials.isNotEmpty()) {
            return Triple(
                currentNest,
                inventory,
                NestUpgradeResult.Failure.InsufficientMaterials(missingMaterials)
            )
        }
        
        // Consume materials (atomic operation - all or nothing)
        var newInventory = inventory
        requirements.requiredMaterials.forEach { (itemId, quantity) ->
            val (updatedInventory, _) = InventoryManager.removeItem(newInventory, itemId, quantity)
            newInventory = updatedInventory
        }
        
        // Upgrade nest
        val upgradedNest = currentNest.copy(tier = targetTier)
        
        return Triple(
            upgradedNest,
            newInventory,
            NestUpgradeResult.Success(
                newTier = targetTier,
                materialsConsumed = requirements.requiredMaterials
            )
        )
    }
    
    /**
     * Gets the stat modifiers for a given nest.
     * Pure function - no side effects.
     * 
     * @param nest The nest to calculate bonuses for
     * @return Stat modifiers based on nest tier
     */
    fun getStatModifiers(nest: Nest): NestStatModifiers {
        return NestStatModifiers.fromTier(nest.tier)
    }
    
    /**
     * Gets the visual state for a given nest.
     * Pure function - no side effects.
     * 
     * @param nest The nest to get visual state for
     * @return Visual state based on nest tier
     */
    fun getVisualState(nest: Nest): NestVisualState {
        return NestVisualState.forTier(nest.tier)
    }
    
    /**
     * Gets the upgrade requirements for a given nest.
     * Returns null if nest is already at max tier.
     * 
     * @param nest The nest to check upgrade requirements for
     * @return Upgrade requirements or null if max tier
     */
    fun getUpgradeRequirements(nest: Nest): NestUpgradeRequirements? {
        return NestUpgradeRequirements.forTier(nest.tier)
    }
    
    /**
     * Checks if a nest can be upgraded with the player's current level and inventory.
     * 
     * @param nest Current nest
     * @param playerLevel Player's current level
     * @param inventory Player's inventory
     * @return True if upgrade is possible, false otherwise
     */
    fun canUpgrade(nest: Nest, playerLevel: Int, inventory: Inventory): Boolean {
        val targetTier = nest.tier.getNextTier() ?: return false
        
        // Check level requirement
        if (playerLevel < targetTier.requiredLevel) return false
        
        // Check materials
        val requirements = NestUpgradeRequirements.forTier(nest.tier) ?: return false
        return requirements.requiredMaterials.all { (itemId, quantity) ->
            inventory.getItemQuantity(itemId) >= quantity
        }
    }
    
    /**
     * Creates a new basic nest with the given ID.
     * Helper function for initializing a player's first nest.
     * 
     * @param id Unique identifier for the nest
     * @param customName Optional custom name (null for default)
     * @return New nest at BASIC tier
     */
    fun createBasicNest(id: String = "player_nest", customName: String? = null): Nest {
        return Nest(id = id, tier = NestTier.BASIC, customName = customName)
    }
    
    /**
     * Renames a nest.
     * 
     * @param nest Current nest
     * @param newName New custom name (null to clear custom name)
     * @return New nest with updated name
     */
    fun renameNest(nest: Nest, newName: String?): Nest {
        return nest.copy(customName = newName)
    }
    
    // ========== COSMETIC MANAGEMENT ==========
    
    /**
     * Places a cosmetic in the nest.
     * 
     * @param nest Current nest
     * @param cosmeticId ID of cosmetic to place
     * @param x X coordinate on grid (0-7)
     * @param y Y coordinate on grid (0-5)
     * @return Pair of (new nest, placement result)
     */
    fun placeCosmetic(
        nest: Nest,
        cosmeticId: String,
        x: Int,
        y: Int
    ): Pair<Nest, CosmeticManager.PlacementResult> {
        val cosmeticManager = CosmeticManager()
        val result = cosmeticManager.placeCosmetic(
            currentPlacedCosmetics = nest.placedCosmetics,
            unlockedCosmetics = nest.unlockedCosmetics,
            cosmeticId = cosmeticId,
            x = x,
            y = y
        )
        
        return when (result) {
            is CosmeticManager.PlacementResult.Success -> {
                val updatedNest = nest.copy(placedCosmetics = result.updatedPlacedCosmetics)
                Pair(updatedNest, result)
            }
            is CosmeticManager.PlacementResult.Failure -> {
                Pair(nest, result)
            }
        }
    }
    
    /**
     * Removes a cosmetic from the nest.
     * 
     * @param nest Current nest
     * @param x X coordinate of cosmetic to remove
     * @param y Y coordinate of cosmetic to remove
     * @return Pair of (new nest, removal result)
     */
    fun removeCosmetic(
        nest: Nest,
        x: Int,
        y: Int
    ): Pair<Nest, CosmeticManager.RemovalResult> {
        val cosmeticManager = CosmeticManager()
        val result = cosmeticManager.removeCosmetic(
            currentPlacedCosmetics = nest.placedCosmetics,
            x = x,
            y = y
        )
        
        return when (result) {
            is CosmeticManager.RemovalResult.Success -> {
                val updatedNest = nest.copy(placedCosmetics = result.updatedPlacedCosmetics)
                Pair(updatedNest, result)
            }
            is CosmeticManager.RemovalResult.Failure -> {
                Pair(nest, result)
            }
        }
    }
    
    /**
     * Unlocks a cosmetic (makes it available for placement).
     * 
     * @param nest Current nest
     * @param cosmeticId ID of cosmetic to unlock
     * @return New nest with cosmetic unlocked
     */
    fun unlockCosmetic(nest: Nest, cosmeticId: String): Nest {
        return nest.copy(unlockedCosmetics = nest.unlockedCosmetics + cosmeticId)
    }
    
    /**
     * Purchases a cosmetic with currency.
     * 
     * @param nest Current nest
     * @param currentSeeds Player's current seeds
     * @param currentGlimmerShards Player's current glimmer shards
     * @param cosmeticId ID of cosmetic to purchase
     * @return Triple of (new nest, new seeds, new glimmer shards, purchase result)
     */
    fun purchaseCosmetic(
        nest: Nest,
        currentSeeds: Long,
        currentGlimmerShards: Long,
        cosmeticId: String
    ): Pair<Nest, CosmeticManager.PurchaseResult> {
        val cosmeticManager = CosmeticManager()
        val result = cosmeticManager.purchaseCosmetic(
            currentUnlocked = nest.unlockedCosmetics,
            currentSeeds = currentSeeds,
            currentGlimmerShards = currentGlimmerShards,
            cosmeticId = cosmeticId
        )
        
        return when (result) {
            is CosmeticManager.PurchaseResult.Success -> {
                val updatedNest = nest.copy(unlockedCosmetics = result.updatedUnlocked)
                Pair(updatedNest, result)
            }
            is CosmeticManager.PurchaseResult.Failure -> {
                Pair(nest, result)
            }
        }
    }
    
    /**
     * Checks if a cosmetic can be placed at the given position.
     * 
     * @param nest Current nest
     * @param cosmeticId ID of cosmetic to check
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if cosmetic can be placed, false otherwise
     */
    fun canPlaceCosmetic(nest: Nest, cosmeticId: String, x: Int, y: Int): Boolean {
        val cosmeticManager = CosmeticManager()
        return cosmeticManager.canPlace(
            currentPlacedCosmetics = nest.placedCosmetics,
            unlockedCosmetics = nest.unlockedCosmetics,
            cosmeticId = cosmeticId,
            x = x,
            y = y
        )
    }
    
    // ==================== Critter Management ====================
    
    /**
     * Adopts a critter into the nest.
     * 
     * @param nest Current nest
     * @param critterId ID of critter to adopt
     * @return Pair of (updated nest, adopt result)
     */
    fun adoptCritter(nest: Nest, critterId: String): Pair<Nest, AdoptResult> {
        val critterManager = CritterManager()
        val (updatedCritters, result) = critterManager.adoptCritter(
            currentCritters = nest.critters,
            critterId = critterId,
            nestTier = nest.tier
        )
        
        return if (result is AdoptResult.Success) {
            Pair(nest.copy(critters = updatedCritters), result)
        } else {
            Pair(nest, result)
        }
    }
    
    /**
     * Feeds a critter to restore satisfaction.
     * 
     * @param nest Current nest
     * @param critterId ID of critter to feed
     * @return Pair of (updated nest, feed result)
     */
    fun feedCritter(nest: Nest, critterId: String): Pair<Nest, FeedResult> {
        val critter = nest.critters.find { it.critterId == critterId }
            ?: return Pair(nest, FeedResult.Failure("Critter not found in nest"))
        
        val critterManager = CritterManager()
        val (updatedCritter, result) = critterManager.feedCritter(critter)
        
        return if (result is FeedResult.Success) {
            val updatedCritters = nest.critters.map {
                if (it.critterId == critterId) updatedCritter else it
            }
            Pair(nest.copy(critters = updatedCritters), result)
        } else {
            Pair(nest, result)
        }
    }
    
    /**
     * Releases a critter from the nest.
     * 
     * @param nest Current nest
     * @param critterId ID of critter to release
     * @return Updated nest
     */
    fun releaseCritter(nest: Nest, critterId: String): Nest {
        val critterManager = CritterManager()
        val updatedCritters = critterManager.releaseCritter(nest.critters, critterId)
        return nest.copy(critters = updatedCritters)
    }
    
    /**
     * Renames a critter.
     * 
     * @param nest Current nest
     * @param critterId ID of critter to rename
     * @param newName New custom name (null to clear)
     * @return Updated nest
     */
    fun renameCritter(nest: Nest, critterId: String, newName: String?): Nest {
        val critterManager = CritterManager()
        val updatedCritters = critterManager.renameCritter(nest.critters, critterId, newName)
        return nest.copy(critters = updatedCritters)
    }
    
    /**
     * Advances time for all critters in the nest (daily update).
     * Updates satisfaction and removes critters that leave.
     * 
     * @param nest Current nest
     * @return Updated nest
     */
    fun advanceCritterDay(nest: Nest): Nest {
        val critterManager = CritterManager()
        val updatedCritters = critterManager.advanceDay(
            currentCritters = nest.critters,
            nestTier = nest.tier,
            placedCosmetics = nest.placedCosmetics
        )
        return nest.copy(critters = updatedCritters)
    }
}

