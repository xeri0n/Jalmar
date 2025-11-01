package com.jalmarquest.shared.model

import kotlinx.serialization.Serializable

/**
 * Core player stats representing health, stamina, magic, and other attributes.
 */
@Serializable
data class PlayerStats(
    val maxHealth: Int = 100,
    val currentHealth: Int = 100,
    val maxStamina: Int = 100,
    val currentStamina: Int = 100,
    val maxMagic: Int = 100,
    val currentMagic: Int = 100,
    val attack: Int = 10,
    val defense: Int = 10,
    val magicPower: Int = 10,
    val speed: Int = 10,
    val luck: Int = 10,
    val availableStatPoints: Int = 0
) {
    init {
        require(currentHealth >= 0) { "Health cannot be negative" }
        require(currentStamina >= 0) { "Stamina cannot be negative" }
        require(currentMagic >= 0) { "Magic cannot be negative" }
        require(currentHealth <= maxHealth) { "Current health cannot exceed max" }
        require(currentStamina <= maxStamina) { "Current stamina cannot exceed max" }
        require(currentMagic <= maxMagic) { "Current magic cannot exceed max" }
    }
    
    fun isDead(): Boolean = currentHealth <= 0
    
    fun isFullHealth(): Boolean = currentHealth == maxHealth
    
    fun healthPercentage(): Float = currentHealth.toFloat() / maxHealth.toFloat()
    
    fun staminaPercentage(): Float = currentStamina.toFloat() / maxStamina.toFloat()
    
    fun magicPercentage(): Float = currentMagic.toFloat() / maxMagic.toFloat()
}
