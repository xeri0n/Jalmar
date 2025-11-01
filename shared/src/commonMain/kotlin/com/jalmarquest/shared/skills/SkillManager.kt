package com.jalmarquest.shared.skills

import com.jalmarquest.shared.model.Player

/**
 * Stateless manager for skill learning and progression.
 * Thread-safety delegated to GameStateManager's Mutex.
 */
object SkillManager {
    
    /**
     * Check if a player can learn a specific skill.
     * 
     * @param player Current player state
     * @param skillId Skill to check
     * @return Result with Success or specific failure reason
     */
    fun canLearnSkill(player: Player, skillId: String): SkillLearnResult {
        val skill = SkillCatalog.getSkill(skillId)
            ?: return SkillLearnResult.Failure(SkillLearnFailureReason.SKILL_NOT_FOUND)
        
        // Already learned
        if (skillId in player.learnedSkills) {
            return SkillLearnResult.Failure(SkillLearnFailureReason.ALREADY_LEARNED)
        }
        
        // Level requirement
        if (player.level < skill.getRequiredLevel()) {
            return SkillLearnResult.Failure(SkillLearnFailureReason.LEVEL_TOO_LOW)
        }
        
        // Skill points requirement
        if (player.skillPoints < skill.getSkillPointCost()) {
            return SkillLearnResult.Failure(SkillLearnFailureReason.INSUFFICIENT_SKILL_POINTS)
        }
        
        // Prerequisite skills
        val missingPrereqs = skill.prerequisiteSkills.filter { it !in player.learnedSkills }
        if (missingPrereqs.isNotEmpty()) {
            return SkillLearnResult.Failure(
                SkillLearnFailureReason.MISSING_PREREQUISITES,
                missingPrereqs
            )
        }
        
        return SkillLearnResult.Success(skill)
    }
    
    /**
     * Learn a skill, consuming skill points and adding to learned skills.
     * Call canLearnSkill first to validate.
     * 
     * @param player Current player state
     * @param skillId Skill to learn
     * @return Pair of (updated player, result)
     */
    fun learnSkill(player: Player, skillId: String): Pair<Player, SkillLearnResult> {
        val result = canLearnSkill(player, skillId)
        if (result is SkillLearnResult.Failure) {
            return player to result
        }
        
        val skill = (result as SkillLearnResult.Success).skill
        val updatedPlayer = player.copy(
            learnedSkills = player.learnedSkills + skillId,
            skillPoints = player.skillPoints - skill.getSkillPointCost()
        )
        
        return updatedPlayer to SkillLearnResult.Success(skill)
    }
    
    /**
     * Get all skills the player has learned.
     */
    fun getLearnedSkills(player: Player): List<Skill> {
        return player.learnedSkills.mapNotNull { SkillCatalog.getSkill(it) }
    }
    
    /**
     * Get all skills available for the player to learn (meets level requirement).
     */
    fun getAvailableSkills(player: Player, archetype: SkillArchetype? = null): List<Skill> {
        val skills = if (archetype != null) {
            SkillCatalog.getSkillsByArchetype(archetype)
        } else {
            SkillCatalog.allSkills
        }
        
        return skills.filter { skill ->
            skill.id !in player.learnedSkills && player.level >= skill.getRequiredLevel()
        }
    }
    
    /**
     * Get all skills locked by level requirement.
     */
    fun getLockedSkills(player: Player, archetype: SkillArchetype? = null): List<Skill> {
        val skills = if (archetype != null) {
            SkillCatalog.getSkillsByArchetype(archetype)
        } else {
            SkillCatalog.allSkills
        }
        
        return skills.filter { skill ->
            skill.id !in player.learnedSkills && player.level < skill.getRequiredLevel()
        }
    }
    
    /**
     * Calculate total skill points earned by a player at their current level.
     * Players earn 1 skill point per level.
     */
    fun calculateTotalSkillPoints(level: Int): Int {
        require(level in 1..50) { "Level must be 1-50" }
        return level - 1 // Level 1 = 0 points, Level 50 = 49 points
    }
    
    /**
     * Calculate total skill points spent on learned skills.
     */
    fun calculateSpentSkillPoints(player: Player): Int {
        return getLearnedSkills(player).sumOf { it.getSkillPointCost() }
    }
    
    /**
     * Check if player has a specific skill learned.
     */
    fun hasSkill(player: Player, skillId: String): Boolean {
        return skillId in player.learnedSkills
    }
}

/**
 * Result of attempting to learn a skill.
 */
sealed class SkillLearnResult {
    data class Success(val skill: Skill) : SkillLearnResult()
    data class Failure(
        val reason: SkillLearnFailureReason,
        val missingPrerequisites: List<String> = emptyList()
    ) : SkillLearnResult()
}

/**
 * Reasons why learning a skill might fail.
 */
enum class SkillLearnFailureReason {
    SKILL_NOT_FOUND,
    ALREADY_LEARNED,
    LEVEL_TOO_LOW,
    INSUFFICIENT_SKILL_POINTS,
    MISSING_PREREQUISITES
}
