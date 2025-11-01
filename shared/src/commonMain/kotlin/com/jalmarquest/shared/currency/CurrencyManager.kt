package com.jalmarquest.shared.currency

import com.jalmarquest.shared.model.Player
import kotlinx.serialization.Serializable

/**
 * Result of a currency transaction operation.
 */
@Serializable
sealed class CurrencyResult {
    /**
     * Transaction completed successfully.
     * 
     * @property newBalance The balance after the transaction
     * @property amountChanged The amount added or removed (always positive)
     */
    data class Success(
        val newBalance: Long,
        val amountChanged: Long
    ) : CurrencyResult()
    
    /**
     * Transaction failed.
     */
    sealed class Failure : CurrencyResult() {
        /** Player doesn't have enough currency to spend */
        data object InsufficientFunds : Failure()
        
        /** Adding this amount would exceed Long.MAX_VALUE */
        data object OverflowRisk : Failure()
        
        /** Amount is negative or otherwise invalid */
        data object InvalidAmount : Failure()
    }
}

/**
 * Manager for currency operations (Seeds and Glimmer Shards).
 * 
 * Provides safe currency transactions with overflow protection and validation.
 * Uses functional approach - operations return new Player instances.
 * Thread-safety is handled by GameStateManager's Mutex when updating Player.
 * 
 * **Currency Types:**
 * - **Seeds:** Common currency earned through gameplay (defeating enemies, quests, selling items)
 * - **Glimmer Shards:** Premium currency (rare drops, achievements, IAP in future)
 * 
 * **Safety Features:**
 * - Overflow protection: Prevents exceeding Long.MAX_VALUE
 * - Negative validation: Rejects negative amounts
 * - Insufficient funds checking: Validates before spending
 * - Atomic operations: All-or-nothing transactions
 */
object CurrencyManager {
    
    /**
     * Maximum safe currency value (Long.MAX_VALUE).
     */
    const val MAX_CURRENCY: Long = Long.MAX_VALUE
    
    // ==== SEEDS OPERATIONS ====
    
    /**
     * Adds Seeds to player's wallet.
     * 
     * @param player Current player state
     * @param amount Amount to add (must be positive)
     * @return Pair of (new player, result)
     */
    fun addSeeds(player: Player, amount: Long): Pair<Player, CurrencyResult> {
        if (amount <= 0) {
            return player to CurrencyResult.Failure.InvalidAmount
        }
        
        // Check for overflow
        if (player.seeds > MAX_CURRENCY - amount) {
            return player to CurrencyResult.Failure.OverflowRisk
        }
        
        val newBalance = player.seeds + amount
        val newPlayer = player.copy(seeds = newBalance)
        
        return newPlayer to CurrencyResult.Success(newBalance, amount)
    }
    
    /**
     * Removes Seeds from player's wallet.
     * 
     * @param player Current player state
     * @param amount Amount to remove (must be positive)
     * @return Pair of (new player, result)
     */
    fun removeSeeds(player: Player, amount: Long): Pair<Player, CurrencyResult> {
        if (amount <= 0) {
            return player to CurrencyResult.Failure.InvalidAmount
        }
        
        if (player.seeds < amount) {
            return player to CurrencyResult.Failure.InsufficientFunds
        }
        
        val newBalance = player.seeds - amount
        val newPlayer = player.copy(seeds = newBalance)
        
        return newPlayer to CurrencyResult.Success(newBalance, amount)
    }
    
    // ==== GLIMMER SHARDS OPERATIONS ====
    
    /**
     * Adds Glimmer Shards to player's wallet.
     * 
     * @param player Current player state
     * @param amount Amount to add (must be positive)
     * @return Pair of (new player, result)
     */
    fun addGlimmerShards(player: Player, amount: Long): Pair<Player, CurrencyResult> {
        if (amount <= 0) {
            return player to CurrencyResult.Failure.InvalidAmount
        }
        
        // Check for overflow
        if (player.glimmerShards > MAX_CURRENCY - amount) {
            return player to CurrencyResult.Failure.OverflowRisk
        }
        
        val newBalance = player.glimmerShards + amount
        val newPlayer = player.copy(glimmerShards = newBalance)
        
        return newPlayer to CurrencyResult.Success(newBalance, amount)
    }
    
    /**
     * Removes Glimmer Shards from player's wallet.
     * 
     * @param player Current player state
     * @param amount Amount to remove (must be positive)
     * @return Pair of (new player, result)
     */
    fun removeGlimmerShards(player: Player, amount: Long): Pair<Player, CurrencyResult> {
        if (amount <= 0) {
            return player to CurrencyResult.Failure.InvalidAmount
        }
        
        if (player.glimmerShards < amount) {
            return player to CurrencyResult.Failure.InsufficientFunds
        }
        
        val newBalance = player.glimmerShards - amount
        val newPlayer = player.copy(glimmerShards = newBalance)
        
        return newPlayer to CurrencyResult.Success(newBalance, amount)
    }
    
    // ==== UTILITY METHODS ====
    
    /**
     * Checks if player can afford a purchase.
     * 
     * @param player Current player state
     * @param seedsCost Seeds cost (default 0)
     * @param glimmerShardsCost Glimmer Shards cost (default 0)
     * @return True if player has enough of both currencies
     */
    fun canAfford(
        player: Player,
        seedsCost: Long = 0,
        glimmerShardsCost: Long = 0
    ): Boolean {
        return player.seeds >= seedsCost && player.glimmerShards >= glimmerShardsCost
    }
    
    /**
     * Performs a multi-currency transaction (deducts both currencies atomically).
     * 
     * @param player Current player state
     * @param seedsCost Seeds to deduct
     * @param glimmerShardsCost Glimmer Shards to deduct
     * @return Pair of (new player, success flag)
     */
    fun purchase(
        player: Player,
        seedsCost: Long = 0,
        glimmerShardsCost: Long = 0
    ): Pair<Player, Boolean> {
        if (seedsCost < 0 || glimmerShardsCost < 0) {
            return player to false
        }
        
        if (!canAfford(player, seedsCost, glimmerShardsCost)) {
            return player to false
        }
        
        // Deduct both currencies atomically
        var newPlayer = player
        
        if (seedsCost > 0) {
            val (p, result) = removeSeeds(newPlayer, seedsCost)
            if (result !is CurrencyResult.Success) {
                return player to false
            }
            newPlayer = p
        }
        
        if (glimmerShardsCost > 0) {
            val (p, result) = removeGlimmerShards(newPlayer, glimmerShardsCost)
            if (result !is CurrencyResult.Success) {
                return player to false
            }
            newPlayer = p
        }
        
        return newPlayer to true
    }
    
    /**
     * Formats Seeds amount for UI display.
     * Example: 1000 → "1,000 Seeds"
     */
    fun formatSeeds(amount: Long): String {
        return "${formatNumber(amount)} Seeds"
    }
    
    /**
     * Formats Glimmer Shards amount for UI display.
     * Example: 50 → "50 Glimmer Shards"
     */
    fun formatGlimmerShards(amount: Long): String {
        return "${formatNumber(amount)} Glimmer Shards"
    }
    
    /**
     * Formats a number with thousand separators.
     * Example: 1000000 → "1,000,000"
     */
    private fun formatNumber(number: Long): String {
        return number.toString().reversed().chunked(3).joinToString(",").reversed()
    }
}
