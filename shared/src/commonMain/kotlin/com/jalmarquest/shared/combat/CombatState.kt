package com.jalmarquest.shared.combat

import kotlinx.serialization.Serializable

/**
 * Represents the current state of an active combat encounter.
 * Immutable data structure - all mutations return a new CombatState.
 * 
 * @property combatId Unique identifier for this combat encounter
 * @property player The player participant
 * @property companion Optional companion participant (if active companion present)
 * @property enemies List of enemy participants
 * @property turnOrder Ordered list of participant IDs (determined by initiative)
 * @property currentTurnIndex Index into turnOrder (whose turn it is)
 * @property roundNumber Current round of combat (starts at 1)
 * @property isPlayerDefending Whether player used Defend action this round
 * @property combatLog List of combat event descriptions
 */
@Serializable
data class CombatState(
    val combatId: String,
    val player: PlayerCombatData,
    val companion: com.jalmarquest.shared.companion.CompanionCombatData? = null,
    val enemies: List<EnemyCombatData>,
    val turnOrder: List<String>,
    val currentTurnIndex: Int = 0,
    val roundNumber: Int = 1,
    val isPlayerDefending: Boolean = false,
    val combatLog: List<String> = emptyList()
) {
    init {
        require(enemies.isNotEmpty()) { "Combat must have at least one enemy" }
        require(turnOrder.isNotEmpty()) { "Turn order cannot be empty" }
        require(currentTurnIndex in turnOrder.indices) { "Current turn index out of bounds: $currentTurnIndex" }
        require(roundNumber >= 1) { "Round number must be at least 1, got $roundNumber" }
    }
    
    /**
     * Returns the ID of the participant whose turn it currently is.
     */
    fun getCurrentTurnParticipantId(): String = turnOrder[currentTurnIndex]
    
    /**
     * Returns whether it's currently the player's turn.
     */
    fun isPlayerTurn(): Boolean = getCurrentTurnParticipantId() == player.id
    
    /**
     * Returns whether it's currently the companion's turn.
     */
    fun isCompanionTurn(): Boolean = companion != null && getCurrentTurnParticipantId() == companion.id
    
    /**
     * Returns whether combat has ended (player dead or all enemies dead).
     * Companion death does not end combat.
     */
    fun isCombatOver(): Boolean = player.currentHp <= 0 || enemies.all { it.currentHp <= 0 }
    
    /**
     * Returns whether the player won (all enemies dead and player alive).
     */
    fun isVictory(): Boolean = enemies.all { it.currentHp <= 0 } && player.currentHp > 0
    
    /**
     * Returns whether the player lost (player dead).
     */
    fun isDefeat(): Boolean = player.currentHp <= 0
    
    /**
     * Returns the number of living enemies.
     */
    fun livingEnemyCount(): Int = enemies.count { it.currentHp > 0 }
    
    /**
     * Finds an enemy by ID.
     */
    fun getEnemy(enemyId: String): EnemyCombatData? = enemies.find { it.id == enemyId }
    
    /**
     * Adds a message to the combat log.
     */
    fun addToLog(message: String): CombatState = copy(combatLog = combatLog + message)
}

/**
 * Player combat data (simplified from full Player model).
 * Contains only combat-relevant fields.
 */
@Serializable
data class PlayerCombatData(
    override val id: String,
    override val name: String,
    override val currentHp: Int,
    override val maxHp: Int,
    override val strength: Int,
    override val agility: Int,
    override val vitality: Int,
    override val intelligence: Int,
    override val luck: Int,
    val weaponDamage: Int = 0,
    val armorDefense: Int = 0,
    override val activeStatusEffects: List<StatusEffect> = emptyList()
) : CombatParticipant {
    init {
        require(currentHp >= 0) { "Current HP cannot be negative: $currentHp" }
        require(maxHp > 0) { "Max HP must be positive: $maxHp" }
        require(strength >= 0) { "Strength cannot be negative: $strength" }
        require(agility >= 0) { "Agility cannot be negative: $agility" }
        require(vitality >= 0) { "Vitality cannot be negative: $vitality" }
        require(intelligence >= 0) { "Intelligence cannot be negative: $intelligence" }
        require(luck >= 0) { "Luck cannot be negative: $luck" }
        require(weaponDamage >= 0) { "Weapon damage cannot be negative: $weaponDamage" }
        require(armorDefense >= 0) { "Armor defense cannot be negative: $armorDefense" }
    }
}

/**
 * Enemy combat data.
 */
@Serializable
data class EnemyCombatData(
    override val id: String,
    override val name: String,
    override val currentHp: Int,
    override val maxHp: Int,
    override val strength: Int,
    override val agility: Int,
    override val vitality: Int,
    override val intelligence: Int,
    override val luck: Int,
    val baseDamage: Int = 5,
    val defense: Int = 0,
    val xpReward: Int = 0,
    val catalogId: String = "", // ID from EnemyCatalog for loot lookup
    override val activeStatusEffects: List<StatusEffect> = emptyList()
) : CombatParticipant {
    init {
        require(currentHp >= 0) { "Current HP cannot be negative: $currentHp" }
        require(maxHp > 0) { "Max HP must be positive: $maxHp" }
        require(strength >= 0) { "Strength cannot be negative: $strength" }
        require(agility >= 0) { "Agility cannot be negative: $agility" }
        require(vitality >= 0) { "Vitality cannot be negative: $vitality" }
        require(intelligence >= 0) { "Intelligence cannot be negative: $intelligence" }
        require(luck >= 0) { "Luck cannot be negative: $luck" }
        require(baseDamage >= 0) { "Base damage cannot be negative: $baseDamage" }
        require(defense >= 0) { "Defense cannot be negative: $defense" }
    }
}

/**
 * Rewards received from a combat encounter.
 * 
 * @property xpGained Total experience points from all defeated enemies
 * @property itemsLooted List of (itemId, quantity) pairs dropped by enemies
 * @property defeatedEnemies List of defeated enemy names for display
 */
@Serializable
data class CombatRewards(
    val xpGained: Int,
    val itemsLooted: List<Pair<String, Int>>,
    val defeatedEnemies: List<String>
) {
    init {
        require(xpGained >= 0) { "XP gained cannot be negative: $xpGained" }
        require(itemsLooted.all { it.second > 0 }) { "Item quantities must be positive" }
    }
    
    /**
     * Provides a formatted summary of combat rewards.
     */
    fun summary(): String {
        val parts = mutableListOf<String>()
        
        if (xpGained > 0) {
            parts.add("$xpGained XP")
        }
        
        if (itemsLooted.isNotEmpty()) {
            parts.add(itemsLooted.joinToString(", ") { (itemId, quantity) -> "$quantity× $itemId" })
        }
        
        return if (parts.isEmpty()) "No rewards" else parts.joinToString(" | ")
    }
}
