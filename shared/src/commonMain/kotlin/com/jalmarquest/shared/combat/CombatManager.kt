package com.jalmarquest.shared.combat

import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Stateless combat manager following functional pattern.
 * All combat operations are pure functions that return new state.
 */
object CombatManager {
    
    /**
     * Initializes a new combat encounter.
     * Determines turn order based on initiative (agility stat).
     * 
     * @param combatId Unique identifier for this combat
     * @param player Player combat data
     * @param companion Optional companion combat data (if active companion present)
     * @param enemies List of enemy combat data
     * @return Initial combat state with turn order determined
     */
    fun initiateCombat(
        combatId: String,
        player: PlayerCombatData,
        companion: com.jalmarquest.shared.companion.CompanionCombatData? = null,
        enemies: List<EnemyCombatData>
    ): CombatState {
        require(enemies.isNotEmpty()) { "Cannot initiate combat with no enemies" }
        require(player.currentHp > 0) { "Cannot initiate combat with dead player" }
        
        // Determine turn order by initiative (agility + random 1-10)
        val participants = mutableListOf<Pair<String, Int>>(
            player.id to (player.agility + Random.nextInt(1, 11))
        )
        
        // Add companion to initiative if present and alive
        if (companion != null && companion.currentHp > 0) {
            participants.add(companion.id to (companion.agility + Random.nextInt(1, 11)))
        }
        
        // Add all enemies
        participants.addAll(enemies.map { enemy ->
            enemy.id to (enemy.agility + Random.nextInt(1, 11))
        })
        
        val turnOrder = participants
            .sortedByDescending { it.second }
            .map { it.first }
        
        val startMessage = if (companion != null) {
            "Combat started with ${companion.name}! Turn order determined."
        } else {
            "Combat started! Turn order determined."
        }
        
        return CombatState(
            combatId = combatId,
            player = player,
            companion = companion,
            enemies = enemies,
            turnOrder = turnOrder,
            currentTurnIndex = 0,
            roundNumber = 1,
            isPlayerDefending = false,
            combatLog = listOf(startMessage)
        )
    }
    
    /**
     * Executes a combat action for the current turn participant.
     * 
     * @param state Current combat state
     * @param action The action to execute
     * @return Pair of updated combat state and action result
     */
    fun executeAction(
        state: CombatState,
        action: CombatAction
    ): Pair<CombatState, CombatActionResult> {
        // Check if combat is already over
        if (state.isCombatOver()) {
            return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_ALREADY_DEAD)
        }
        
        // Get current actor
        val actorId = state.getCurrentTurnParticipantId()
        val isPlayerActor = actorId == state.player.id
        val isCompanionActor = state.companion != null && actorId == state.companion.id
        
        // Check for stun
        val actorEffects = when {
            isPlayerActor -> state.player.activeStatusEffects
            isCompanionActor -> state.companion?.activeStatusEffects ?: emptyList()
            else -> state.getEnemy(actorId)?.activeStatusEffects ?: emptyList()
        }
        
        if (actorEffects.any { it.type == StatusEffectType.STUN }) {
            val actorName = when {
                isPlayerActor -> state.player.name
                isCompanionActor -> state.companion?.name ?: "Companion"
                else -> state.getEnemy(actorId)?.name ?: "Enemy"
            }
            val stunMessage = "$actorName is stunned and cannot act!"
            return state.addToLog(stunMessage) to CombatActionResult.Failure(CombatActionFailureReason.STUNNED)
        }
        
        // Execute the action
        return when (action) {
            is CombatAction.Attack -> executeAttack(state, actorId, action.targetId)
            is CombatAction.Defend -> executeDefend(state, actorId)
            is CombatAction.UseSkill -> executeUseSkill(state, actorId, action.skillId, action.targetId)
            is CombatAction.UseItem -> executeUseItem(state, actorId, action.itemId, action.targetId)
            is CombatAction.Flee -> executeFlee(state)
        }
    }
    
    /**
     * Advances to the next turn.
     * Applies status effect ticks, increments round if needed.
     * 
     * @param state Current combat state
     * @return Updated combat state
     */
    fun advanceTurn(state: CombatState): CombatState {
        val nextTurnIndex = (state.currentTurnIndex + 1) % state.turnOrder.size
        val newRound = if (nextTurnIndex == 0) state.roundNumber + 1 else state.roundNumber
        
        var updatedState = state.copy(
            currentTurnIndex = nextTurnIndex,
            roundNumber = newRound,
            isPlayerDefending = false // Reset defend status
        )
        
        // If starting new round, tick status effects
        if (nextTurnIndex == 0) {
            updatedState = applyStatusEffectTicks(updatedState)
        }
        
        return updatedState
    }
    
    /**
     * Calculates damage for a physical attack.
     * Formula: (baseDamage + weaponDamage + (strength * 0.5)) * modifiers - defense
     * 
     * @param attacker The attacking participant
     * @param target The target participant
     * @param isDefending Whether target is in defensive stance
     * @return Damage dealt (minimum 1)
     */
    fun calculateDamage(
        attacker: CombatParticipant,
        target: CombatParticipant,
        isDefending: Boolean = false
    ): Int {
        // Base damage calculation
        val weaponDamage = when (attacker) {
            is PlayerCombatData -> attacker.weaponDamage
            is EnemyCombatData -> attacker.baseDamage
            is com.jalmarquest.shared.companion.CompanionCombatData -> {
                // Companions use strength as their base damage (scaled by loyalty via stat modifier)
                (attacker.strength * 0.8).roundToInt()
            }
            else -> 0
        }
        
        val strengthBonus = (attacker.strength * 0.5).roundToInt()
        var damage = weaponDamage + strengthBonus
        
        // Apply attacker status modifiers
        if (attacker.hasStatusEffect(StatusEffectType.STRENGTHEN)) {
            damage = (damage * 1.3).roundToInt()
        }
        if (attacker.hasStatusEffect(StatusEffectType.WEAKEN)) {
            damage = (damage * 0.7).roundToInt()
        }
        
        // Apply defender status modifiers
        var defense = when (target) {
            is PlayerCombatData -> target.armorDefense
            is EnemyCombatData -> target.defense
            is com.jalmarquest.shared.companion.CompanionCombatData -> {
                // Companions use vitality as defense (scaled by loyalty via stat modifier)
                (target.vitality * 0.3).roundToInt()
            }
            else -> 0
        }
        
        if (target.hasStatusEffect(StatusEffectType.VULNERABLE)) {
            defense = (defense * 0.75).roundToInt()
        }
        
        // Apply defensive stance (50% damage reduction)
        if (isDefending) {
            damage = (damage * 0.5).roundToInt()
        }
        
        // Final damage calculation
        val finalDamage = max(1, damage - defense)
        
        return finalDamage
    }
    
    /**
     * Applies status effect damage/healing at the start of a new round.
     */
    private fun applyStatusEffectTicks(state: CombatState): CombatState {
        var updatedState = state
        
        // Apply to player
        updatedState = applyStatusEffectsToPlayer(updatedState)
        
        // Apply to companion (if present)
        if (updatedState.companion != null) {
            updatedState = applyStatusEffectsToCompanion(updatedState)
        }
        
        // Apply to each enemy
        state.enemies.forEachIndexed { index, enemy ->
            updatedState = applyStatusEffectsToEnemy(updatedState, index)
        }
        
        return updatedState
    }
    
    private fun applyStatusEffectsToPlayer(state: CombatState): CombatState {
        var player = state.player
        var log = state.combatLog
        
        player.activeStatusEffects.forEach { effect ->
            when (effect.type) {
                StatusEffectType.POISON -> {
                    val damage = max(1, (player.maxHp * 0.05).roundToInt())
                    player = player.copy(currentHp = max(0, player.currentHp - damage))
                    log = log + "${player.name} takes $damage poison damage!"
                }
                StatusEffectType.BURN -> {
                    val damage = max(1, (player.maxHp * 0.08).roundToInt())
                    player = player.copy(currentHp = max(0, player.currentHp - damage))
                    log = log + "${player.name} takes $damage burn damage!"
                }
                StatusEffectType.REGENERATION -> {
                    val healing = max(1, (player.maxHp * 0.10).roundToInt())
                    player = player.copy(currentHp = kotlin.math.min(player.maxHp, player.currentHp + healing))
                    log = log + "${player.name} regenerates $healing HP!"
                }
                else -> {} // Other effects don't apply damage
            }
        }
        
        // Tick status effects
        val tickedEffects = player.activeStatusEffects.mapNotNull { it.tick() }
        player = player.copy(activeStatusEffects = tickedEffects)
        
        return state.copy(player = player, combatLog = log)
    }
    
    private fun applyStatusEffectsToCompanion(state: CombatState): CombatState {
        var companion = state.companion ?: return state
        var log = state.combatLog
        
        companion.activeStatusEffects.forEach { effect ->
            when (effect.type) {
                StatusEffectType.POISON -> {
                    val damage = max(1, (companion.maxHp * 0.05).roundToInt())
                    companion = companion.copy(currentHp = max(0, companion.currentHp - damage))
                    log = log + "${companion.name} takes $damage poison damage!"
                }
                StatusEffectType.BURN -> {
                    val damage = max(1, (companion.maxHp * 0.08).roundToInt())
                    companion = companion.copy(currentHp = max(0, companion.currentHp - damage))
                    log = log + "${companion.name} takes $damage burn damage!"
                }
                StatusEffectType.REGENERATION -> {
                    val healing = max(1, (companion.maxHp * 0.10).roundToInt())
                    companion = companion.copy(currentHp = kotlin.math.min(companion.maxHp, companion.currentHp + healing))
                    log = log + "${companion.name} regenerates $healing HP!"
                }
                else -> {} // Other effects don't apply damage
            }
        }
        
        // Tick status effects
        val tickedEffects = companion.activeStatusEffects.mapNotNull { it.tick() }
        companion = companion.copy(activeStatusEffects = tickedEffects)
        
        return state.copy(companion = companion, combatLog = log)
    }
    
    private fun applyStatusEffectsToEnemy(state: CombatState, enemyIndex: Int): CombatState {
        var enemy = state.enemies[enemyIndex]
        var log = state.combatLog
        
        enemy.activeStatusEffects.forEach { effect ->
            when (effect.type) {
                StatusEffectType.POISON -> {
                    val damage = max(1, (enemy.maxHp * 0.05).roundToInt())
                    enemy = enemy.copy(currentHp = max(0, enemy.currentHp - damage))
                    log = log + "${enemy.name} takes $damage poison damage!"
                }
                StatusEffectType.BURN -> {
                    val damage = max(1, (enemy.maxHp * 0.08).roundToInt())
                    enemy = enemy.copy(currentHp = max(0, enemy.currentHp - damage))
                    log = log + "${enemy.name} takes $damage burn damage!"
                }
                StatusEffectType.REGENERATION -> {
                    val healing = max(1, (enemy.maxHp * 0.10).roundToInt())
                    enemy = enemy.copy(currentHp = kotlin.math.min(enemy.maxHp, enemy.currentHp + healing))
                    log = log + "${enemy.name} regenerates $healing HP!"
                }
                else -> {}
            }
        }
        
        // Tick status effects
        val tickedEffects = enemy.activeStatusEffects.mapNotNull { it.tick() }
        enemy = enemy.copy(activeStatusEffects = tickedEffects)
        
        val updatedEnemies = state.enemies.toMutableList()
        updatedEnemies[enemyIndex] = enemy
        
        return state.copy(enemies = updatedEnemies, combatLog = log)
    }
    
    private fun executeAttack(
        state: CombatState,
        actorId: String,
        targetId: String
    ): Pair<CombatState, CombatActionResult> {
        val isPlayerAttacker = actorId == state.player.id
        val isCompanionAttacker = state.companion != null && actorId == state.companion.id
        
        // Get attacker
        val attacker: CombatParticipant = when {
            isPlayerAttacker -> state.player
            isCompanionAttacker -> state.companion!!
            else -> state.getEnemy(actorId) ?: return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_NOT_FOUND)
        }
        
        val isPlayerTarget = targetId == state.player.id
        val isCompanionTarget = state.companion != null && targetId == state.companion.id
        
        // Get target
        val target: CombatParticipant = when {
            isPlayerTarget -> state.player
            isCompanionTarget -> state.companion!!
            else -> state.getEnemy(targetId) ?: return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_NOT_FOUND)
        }
        
        // Check if target is alive
        if (target.isDead()) {
            return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_ALREADY_DEAD)
        }
        
        // Calculate damage
        val isDefending = isPlayerTarget && state.isPlayerDefending
        val damage = calculateDamage(attacker, target, isDefending)
        
        // Apply damage
        var updatedState = when {
            isPlayerTarget -> {
                val updatedPlayer = state.player.copy(currentHp = max(0, state.player.currentHp - damage))
                state.copy(player = updatedPlayer)
            }
            isCompanionTarget -> {
                val updatedCompanion = state.companion!!.copy(currentHp = max(0, state.companion.currentHp - damage))
                state.copy(companion = updatedCompanion)
            }
            else -> {
                val enemyIndex = state.enemies.indexOfFirst { it.id == targetId }
                val updatedEnemy = state.enemies[enemyIndex].copy(currentHp = max(0, state.enemies[enemyIndex].currentHp - damage))
                val updatedEnemies = state.enemies.toMutableList()
                updatedEnemies[enemyIndex] = updatedEnemy
                state.copy(enemies = updatedEnemies)
            }
        }
        
        val message = "${attacker.name} attacks ${target.name} for $damage damage!"
        updatedState = updatedState.addToLog(message)
        
        return updatedState to CombatActionResult.Success(message, damageDealt = damage)
    }
    
    private fun executeDefend(
        state: CombatState,
        actorId: String
    ): Pair<CombatState, CombatActionResult> {
        val isPlayer = actorId == state.player.id
        
        if (!isPlayer) {
            // Enemies can't defend yet (simple AI)
            return state to CombatActionResult.Failure(CombatActionFailureReason.INSUFFICIENT_RESOURCES)
        }
        
        val message = "${state.player.name} takes a defensive stance!"
        val updatedState = state.copy(isPlayerDefending = true).addToLog(message)
        
        return updatedState to CombatActionResult.Success(message)
    }
    
    private fun executeUseSkill(
        state: CombatState,
        actorId: String,
        skillId: String,
        targetId: String
    ): Pair<CombatState, CombatActionResult> {
        // Get skill from catalog
        val skill = com.jalmarquest.shared.skills.SkillCatalog.getSkill(skillId)
            ?: return state to CombatActionResult.Failure(CombatActionFailureReason.SKILL_NOT_FOUND)
        
        // Find actor (player or enemy)
        val actor: Any = if (actorId == state.player.id) {
            state.player
        } else {
            state.enemies.find { it.id == actorId }
                ?: return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_NOT_FOUND)
        }
        
        // Verify player has learned the skill (only check for player)
        if (actor is PlayerCombatData) {
            // TODO: Add actual learned skills check when integrated with player state
            // For now, assume all skills are usable if found in catalog
        }
        
        // Find target
        val targetExists = targetId == state.player.id || state.enemies.any { it.id == targetId }
        if (!targetExists) {
            return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_NOT_FOUND)
        }
        
        // Check if target is alive
        val targetAlive = if (targetId == state.player.id) {
            state.player.isAlive()
        } else {
            state.enemies.find { it.id == targetId }?.isAlive() == true
        }
        
        if (!targetAlive) {
            return state to CombatActionResult.Failure(CombatActionFailureReason.TARGET_ALREADY_DEAD)
        }
        
        // Apply skill effects
        var updatedState = state
        var totalDamage = 0
        var totalHealing = 0
        val appliedStatus = mutableListOf<StatusEffect>()
        
        // Get actor stats for damage calculations
        val actorStrength = if (actor is PlayerCombatData) actor.strength else (actor as EnemyCombatData).strength
        val actorName = if (actor is PlayerCombatData) actor.name else (actor as EnemyCombatData).name
        
        skill.effects.forEach { effect ->
            when (effect) {
                is com.jalmarquest.shared.skills.SkillEffect.Damage -> {
                    val damage = (effect.baseDamage + (actorStrength * effect.statScaling)).toInt()
                    totalDamage += damage
                    updatedState = applyDamageToTarget(updatedState, targetId, damage)
                }
                is com.jalmarquest.shared.skills.SkillEffect.AoEDamage -> {
                    // Apply to all enemies (if actor is player) or to player (if actor is enemy)
                    if (actor is PlayerCombatData) {
                        updatedState.enemies.filter { it.isAlive() }.forEach { enemy ->
                            val damage = (effect.baseDamage + (actorStrength * effect.statScaling)).toInt()
                            totalDamage += damage
                            updatedState = applyDamageToTarget(updatedState, enemy.id, damage)
                        }
                    } else {
                        // Enemy AoE would hit player (currently only player for simplicity)
                        val damage = (effect.baseDamage + (actorStrength * effect.statScaling)).toInt()
                        totalDamage += damage
                        updatedState = applyDamageToTarget(updatedState, state.player.id, damage)
                    }
                }
                is com.jalmarquest.shared.skills.SkillEffect.Heal -> {
                    totalHealing += effect.baseHealing
                    updatedState = applyHealingToTarget(updatedState, targetId, effect.baseHealing)
                }
                is com.jalmarquest.shared.skills.SkillEffect.ApplyStatus -> {
                    val status = StatusEffect(
                        type = effect.statusType,
                        remainingRounds = effect.duration
                    )
                    appliedStatus.add(status)
                    updatedState = applyStatusToTarget(updatedState, targetId, status)
                }
                is com.jalmarquest.shared.skills.SkillEffect.BuffAttack,
                is com.jalmarquest.shared.skills.SkillEffect.BuffDefense,
                is com.jalmarquest.shared.skills.SkillEffect.DebuffAttack -> {
                    // Buffs/debuffs handled via status effects in future enhancement
                    // For now, treat as generic strengthen/weaken
                }
                is com.jalmarquest.shared.skills.SkillEffect.MultiHit -> {
                    repeat(effect.hits) {
                        val damage = (effect.damagePerHit + (actorStrength * effect.statScaling)).toInt()
                        totalDamage += damage
                        updatedState = applyDamageToTarget(updatedState, targetId, damage)
                    }
                }
                is com.jalmarquest.shared.skills.SkillEffect.GuaranteedCrit -> {
                    // Crit multiplier applied to next damage effect
                }
                is com.jalmarquest.shared.skills.SkillEffect.IgnoreDefense -> {
                    // Defense ignore handled in damage calculation
                }
                else -> {
                    // Passive skills and other effects not applicable in combat execution
                }
            }
        }
        
        val actionDesc = "$actorName uses ${skill.name}!"
        updatedState = updatedState.copy(
            combatLog = updatedState.combatLog + actionDesc
        )
        
        return updatedState to CombatActionResult.Success(
            actionDescription = actionDesc,
            damageDealt = totalDamage,
            healingDone = totalHealing,
            statusEffectsApplied = appliedStatus
        )
    }
    
    private fun applyDamageToTarget(state: CombatState, targetId: String, damage: Int): CombatState {
        return if (targetId == state.player.id) {
            // Target is player
            val updatedPlayer = state.player.copy(
                currentHp = (state.player.currentHp - damage).coerceAtLeast(0)
            )
            state.copy(player = updatedPlayer)
        } else {
            // Target is enemy
            val enemyIndex = state.enemies.indexOfFirst { it.id == targetId }
            if (enemyIndex >= 0) {
                val updatedEnemy = state.enemies[enemyIndex].copy(
                    currentHp = (state.enemies[enemyIndex].currentHp - damage).coerceAtLeast(0)
                )
                val updatedEnemies = state.enemies.toMutableList()
                updatedEnemies[enemyIndex] = updatedEnemy
                state.copy(enemies = updatedEnemies)
            } else {
                state // Enemy not found, return unchanged state
            }
        }
    }
    
    private fun applyHealingToTarget(state: CombatState, targetId: String, healing: Int): CombatState {
        return if (targetId == state.player.id) {
            // Target is player
            val updatedPlayer = state.player.copy(
                currentHp = (state.player.currentHp + healing).coerceAtMost(state.player.maxHp)
            )
            state.copy(player = updatedPlayer)
        } else {
            // Target is enemy
            val enemyIndex = state.enemies.indexOfFirst { it.id == targetId }
            if (enemyIndex >= 0) {
                val updatedEnemy = state.enemies[enemyIndex].copy(
                    currentHp = (state.enemies[enemyIndex].currentHp + healing).coerceAtMost(state.enemies[enemyIndex].maxHp)
                )
                val updatedEnemies = state.enemies.toMutableList()
                updatedEnemies[enemyIndex] = updatedEnemy
                state.copy(enemies = updatedEnemies)
            } else {
                state // Enemy not found, return unchanged state
            }
        }
    }
    
    private fun applyStatusToTarget(state: CombatState, targetId: String, status: StatusEffect): CombatState {
        return if (targetId == state.player.id) {
            // Target is player
            val updatedPlayer = state.player.copy(
                activeStatusEffects = state.player.activeStatusEffects + status
            )
            state.copy(player = updatedPlayer)
        } else {
            // Target is enemy
            val enemyIndex = state.enemies.indexOfFirst { it.id == targetId }
            if (enemyIndex >= 0) {
                val updatedEnemy = state.enemies[enemyIndex].copy(
                    activeStatusEffects = state.enemies[enemyIndex].activeStatusEffects + status
                )
                val updatedEnemies = state.enemies.toMutableList()
                updatedEnemies[enemyIndex] = updatedEnemy
                state.copy(enemies = updatedEnemies)
            } else {
                state // Enemy not found, return unchanged state
            }
        }
    }
    
    private fun executeUseItem(
        state: CombatState,
        actorId: String,
        itemId: String,
        targetId: String?
    ): Pair<CombatState, CombatActionResult> {
        // Item usage in combat not yet implemented
        return state to CombatActionResult.Failure(CombatActionFailureReason.ITEM_NOT_FOUND)
    }
    
    private fun executeFlee(state: CombatState): Pair<CombatState, CombatActionResult> {
        // Flee success based on agility difference
        val averageEnemyAgility = state.enemies.filter { it.currentHp > 0 }.map { it.agility }.average()
        val fleeChance = max(0.1, kotlin.math.min(0.9, (state.player.agility - averageEnemyAgility) / 20.0 + 0.5))
        
        val success = Random.nextDouble() < fleeChance
        
        return if (success) {
            val message = "${state.player.name} successfully fled from combat!"
            state.addToLog(message) to CombatActionResult.Success(message)
        } else {
            val message = "${state.player.name} failed to flee!"
            state.addToLog(message) to CombatActionResult.Failure(CombatActionFailureReason.FLEE_FAILED)
        }
    }
}
