package com.jalmarquest.shared.combat

/**
 * Interface for entities that can participate in combat.
 * Both Player and Enemy should implement this interface.
 */
interface CombatParticipant {
    /** Unique identifier for this participant */
    val id: String
    
    /** Display name */
    val name: String
    
    /** Current hit points */
    val currentHp: Int
    
    /** Maximum hit points */
    val maxHp: Int
    
    /** Strength stat (affects physical damage) */
    val strength: Int
    
    /** Agility stat (affects initiative and dodge) */
    val agility: Int
    
    /** Vitality stat (affects HP and defense) */
    val vitality: Int
    
    /** Intelligence stat (affects skill damage) */
    val intelligence: Int
    
    /** Luck stat (affects critical hits) */
    val luck: Int
    
    /** Current active status effects */
    val activeStatusEffects: List<StatusEffect>
    
    /**
     * Returns whether this participant is still alive.
     */
    fun isAlive(): Boolean = currentHp > 0
    
    /**
     * Returns whether this participant is dead.
     */
    fun isDead(): Boolean = currentHp <= 0
    
    /**
     * Returns the current HP as a percentage (0.0 to 1.0).
     */
    fun hpPercentage(): Float = if (maxHp > 0) currentHp.toFloat() / maxHp.toFloat() else 0f
    
    /**
     * Returns whether this participant has a specific status effect active.
     */
    fun hasStatusEffect(type: StatusEffectType): Boolean = 
        activeStatusEffects.any { it.type == type }
    
    /**
     * Returns the first active status effect of the given type, or null.
     */
    fun getStatusEffect(type: StatusEffectType): StatusEffect? = 
        activeStatusEffects.find { it.type == type }
}
