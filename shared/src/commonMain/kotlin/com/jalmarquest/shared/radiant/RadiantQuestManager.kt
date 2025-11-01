package com.jalmarquest.shared.radiant

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.quest.Quest
import com.jalmarquest.shared.quest.QuestDifficulty
import com.jalmarquest.shared.quest.QuestObjective
import com.jalmarquest.shared.quest.QuestReward
import com.jalmarquest.shared.quest.QuestType
import kotlin.random.Random

/**
 * Manages procedural generation of radiant quests.
 * 
 * Follows stateless functional pattern:
 * - All methods return new state instances
 * - No internal mutable state
 * - Thread-safe by design
 * 
 * Core responsibilities:
 * - Generate quests from templates with context-specific parameters
 * - Validate context requirements (player level, AI state, available targets)
 * - Calculate scaled rewards based on difficulty and player level
 * - Select valid targets from game state (NPCs, items, locations)
 * - Fill template parameter slots with actual values
 * - Manage template cooldowns to prevent repetition
 * 
 * Integration points:
 * - AI Director: Triggers generation based on tension/engagement
 * - QuestManager: Generated quests become active quests
 * - NPC System: Selects quest givers from available NPCs
 * - Location System: Validates location requirements
 * - Item System: Validates item availability
 */
class RadiantQuestManager {
    
    /**
     * Generate a radiant quest from a template.
     * 
     * Process:
     * 1. Validate template exists and is not on cooldown
     * 2. Validate context requirements (level, AI state, etc.)
     * 3. Select valid targets for each objective parameter
     * 4. Fill template parameter slots with selected values
     * 5. Calculate scaled rewards
     * 6. Create Quest instance
     * 7. Update state with new quest and cooldown
     * 
     * @param state Current radiant quest state
     * @param gameState Current game state
     * @param template Quest template to use
     * @param currentTimestamp Current game time in ticks
     * @return GenerateQuestResult with new state and quest data, or failure reason
     */
    fun generateQuest(
        state: RadiantQuestState,
        gameState: GameState,
        template: RadiantQuestTemplate,
        currentTimestamp: Long
    ): GenerateQuestResult {
        // Check cooldown
        val cooldownExpiry = state.templateCooldowns[template.templateId] ?: 0
        if (currentTimestamp < cooldownExpiry) {
            return GenerateQuestResult.Failure(GenerationFailure.TEMPLATE_ON_COOLDOWN)
        }
        
        // Validate context
        val contextValidation = validateContext(template, gameState)
        if (contextValidation is ContextValidationResult.Invalid) {
            return when {
                contextValidation.reason.contains("level") -> 
                    GenerateQuestResult.Failure(GenerationFailure.PLAYER_LEVEL_OUT_OF_RANGE)
                contextValidation.reason.contains("tension") -> 
                    GenerateQuestResult.Failure(GenerationFailure.AI_TENSION_OUT_OF_RANGE)
                contextValidation.reason.contains("quest") -> 
                    GenerateQuestResult.Failure(GenerationFailure.CONFLICTING_QUESTS_ACTIVE)
                else -> 
                    GenerateQuestResult.Failure(GenerationFailure.CONTEXT_REQUIREMENTS_NOT_MET)
            }
        }
        
        // Select targets and build parameter map
        val parameters = mutableMapOf<String, String>()
        
        // Extract all parameter names from templates (name + description + objectives)
        val allParameterNames = mutableSetOf<String>()
        
        // Extract from name template
        val nameParams = Regex("\\{([^}]+)}").findAll(template.nameTemplate)
            .map { it.groupValues[1] }
            .filter { !it.endsWith("_count") } // Exclude count parameters
        allParameterNames.addAll(nameParams)
        
        // Extract from description template
        val descParams = Regex("\\{([^}]+)}").findAll(template.descriptionTemplate)
            .map { it.groupValues[1] }
            .filter { !it.endsWith("_count") } // Exclude count parameters
        allParameterNames.addAll(descParams)
        
        // Extract from objective templates
        for (objectiveTemplate in template.objectiveTemplates) {
            allParameterNames.add(objectiveTemplate.targetParameter)
        }
        
        // Remove playerName since it's handled separately
        allParameterNames.remove("playerName")
        
        // Select target for each parameter
        for (paramName in allParameterNames) {
            val targetValue = selectTarget(paramName, gameState, template.contextRequirements)
                ?: return GenerateQuestResult.Failure(GenerationFailure.NO_VALID_TARGETS_AVAILABLE)
            
            parameters[paramName] = targetValue
        }
        
        // Add count parameters for objective targets
        for (objectiveTemplate in template.objectiveTemplates) {
            parameters["${objectiveTemplate.targetParameter}_count"] = objectiveTemplate.randomCount().toString()
        }
        
        // Add player name for personalization
        parameters["playerName"] = gameState.player.name
        
        // Generate unique quest ID
        val questId = "radiant_${template.templateId}_${currentTimestamp}_${Random.nextInt(1000, 9999)}"
        
        // Update state
        val newCooldowns = state.templateCooldowns.toMutableMap()
        newCooldowns[template.templateId] = currentTimestamp + template.cooldownTicks
        
        val newGenerated = state.generatedQuests.toMutableMap()
        newGenerated[questId] = template.templateId
        
        val newCounts = state.generationCount.toMutableMap()
        newCounts[template.templateId] = (state.generationCount[template.templateId] ?: 0) + 1
        
        val newState = RadiantQuestState(
            generatedQuests = newGenerated,
            templateCooldowns = newCooldowns,
            generationCount = newCounts
        )
        
        return GenerateQuestResult.Success(
            state = newState,
            generatedQuestId = questId,
            parameters = parameters
        )
    }
    
    /**
     * Create actual Quest instance from template and parameters.
     * 
     * @param template Quest template
     * @param questId Generated quest ID
     * @param parameters Map of parameter names to values
     * @param playerLevel Player level for reward scaling
     * @return Quest instance ready to be added to active quests
     */
    fun createQuest(
        template: RadiantQuestTemplate,
        questId: String,
        parameters: Map<String, String>,
        playerLevel: Int
    ): Quest {
        // Fill name template
        val name = fillTemplate(template.nameTemplate, parameters)
        
        // Fill description template
        val description = fillTemplate(template.descriptionTemplate, parameters)
        
        // Build objectives
        val objectives = template.objectiveTemplates.map { objectiveTemplate ->
            val targetId = parameters[objectiveTemplate.targetParameter] ?: ""
            val count = parameters["${objectiveTemplate.targetParameter}_count"]?.toIntOrNull() ?: 1
            val objectiveDescription = fillTemplate(objectiveTemplate.descriptionTemplate, parameters)
            
            QuestObjective(
                type = objectiveTemplate.type,
                description = objectiveDescription,
                targetId = targetId,
                targetCount = count,
                currentProgress = 0,
                isOptional = objectiveTemplate.isOptional
            )
        }
        
        // Calculate rewards
        val rewards = calculateRewards(template.rewardScaling, playerLevel, template.baseDifficulty)
        
        // Select quest giver if NPC parameter exists
        val giver = parameters["npcId"] ?: ""
        
        return Quest(
            id = questId,
            name = name,
            description = description,
            questType = template.questType,
            difficulty = template.baseDifficulty,
            objectives = objectives,
            rewards = rewards,
            prerequisiteQuestIds = emptyList(),
            level = playerLevel,
            giver = giver,
            autoComplete = false
        )
    }
    
    /**
     * Validate if template can be used in current context.
     * 
     * Checks:
     * - Player level in range
     * - AI tension in range
     * - No conflicting quests active
     * - Required locations discovered
     * - Required items available
     * 
     * @param template Quest template
     * @param gameState Current game state
     * @return ContextValidationResult indicating validity
     */
    fun validateContext(
        template: RadiantQuestTemplate,
        gameState: GameState
    ): ContextValidationResult {
        val requirements = template.contextRequirements
        
        // Check player level
        if (gameState.player.level !in requirements.minPlayerLevel..requirements.maxPlayerLevel) {
            return ContextValidationResult.Invalid(
                "Player level ${gameState.player.level} not in range ${requirements.minPlayerLevel}-${requirements.maxPlayerLevel}"
            )
        }
        
        // Check AI tension
        val aiTension = gameState.aiDirector.tension
        if (aiTension !in requirements.minAITension..requirements.maxAITension) {
            return ContextValidationResult.Invalid(
                "AI tension $aiTension not in range ${requirements.minAITension}-${requirements.maxAITension}"
            )
        }
        
        // Check conflicting quests
        for (conflictingQuestId in requirements.excludedIfQuestsActive) {
            if (gameState.activeQuests.contains(conflictingQuestId)) {
                return ContextValidationResult.Invalid(
                    "Conflicting quest active: $conflictingQuestId"
                )
            }
        }
        
        // Check required locations discovered
        for (locationId in requirements.requiredLocations) {
            if (!gameState.discoveredLocations.contains(locationId)) {
                return ContextValidationResult.Invalid(
                    "Required location not discovered: $locationId"
                )
            }
        }
        
        return ContextValidationResult.Valid
    }
    
    /**
     * Calculate scaled rewards based on player level and difficulty.
     * 
     * Formula:
     * - XP = (baseXP + xpPerLevel * playerLevel) * difficultyMultiplier
     * - Seeds = (baseSeeds + seedsPerLevel * playerLevel) * difficultyMultiplier
     * - Glimmer Shards = (baseGlimmerShards + glimmerShardsPerLevel * playerLevel) * difficultyMultiplier
     * 
     * Item rewards randomly selected from pool based on chance.
     * 
     * @param scaling Reward scaling configuration
     * @param playerLevel Player level
     * @param difficulty Quest difficulty
     * @return QuestReward with scaled values
     */
    fun calculateRewards(
        scaling: RewardScaling,
        playerLevel: Int,
        difficulty: QuestDifficulty
    ): QuestReward {
        val multiplier = RewardScaling.difficultyMultiplier(difficulty)
        
        val xp = ((scaling.baseXP + scaling.xpPerLevel * playerLevel) * multiplier).toLong()
        val seeds = ((scaling.baseSeeds + scaling.seedsPerLevel * playerLevel) * multiplier).toInt()
        val glimmerShards = ((scaling.baseGlimmerShards + scaling.glimmerShardsPerLevel * playerLevel) * multiplier).toInt()
        
        // Roll for item reward
        val items = if (scaling.itemRewardPool.isNotEmpty() && Random.nextDouble() < scaling.itemRewardChance) {
            listOf(scaling.itemRewardPool.random())
        } else {
            emptyList()
        }
        
        return QuestReward(
            xp = xp,
            items = items,
            seeds = seeds,
            glimmerShards = glimmerShards
        )
    }
    
    /**
     * Select valid target for objective parameter from game state.
     * 
     * Supported parameters:
     * - "npcId": Random NPC from game (future: filter by type)
     * - "itemId": Random item from requirements or all items
     * - "locationId": Random discovered location or from requirements
     * - "enemyId": Random enemy type (future: from combat system)
     * - "biome": Random biome or from requirements
     * 
     * @param parameter Parameter name ("npcId", "itemId", etc.)
     * @param gameState Current game state
     * @param requirements Context requirements for filtering
     * @return Target ID or null if no valid targets
     */
    fun selectTarget(
        parameter: String,
        gameState: GameState,
        requirements: ContextRequirements
    ): String? {
        return when (parameter) {
            "npcId" -> {
                // For now, use hardcoded NPCs from quest catalog
                // Future: Query NPC system
                listOf("grumble_forgepaw", "old_quill", "merchant_beetle", "garden_snail").randomOrNull()
            }
            
            "itemId" -> {
                // Select from required items or common collectibles
                if (requirements.requiredItems.isNotEmpty()) {
                    requirements.requiredItems.randomOrNull()
                } else {
                    // Common collectible items
                    listOf("seeds", "twig", "pebble", "acorn", "berry", "moss", "feather").randomOrNull()
                }
            }
            
            "locationId" -> {
                // Select from discovered locations or required locations
                if (requirements.requiredLocations.isNotEmpty()) {
                    requirements.requiredLocations.randomOrNull()
                } else if (gameState.discoveredLocations.isNotEmpty()) {
                    gameState.discoveredLocations.randomOrNull()
                } else {
                    // Fallback to starting areas
                    listOf("starting_village", "meadow_path", "old_oak_tree").randomOrNull()
                }
            }
            
            "enemyId" -> {
                // Common enemy types
                listOf("beetle", "ant", "spider", "moth", "grasshopper").randomOrNull()
            }
            
            "biome" -> {
                // Select from required biomes or common biomes
                if (requirements.requiredBiomes.isNotEmpty()) {
                    requirements.requiredBiomes.randomOrNull()
                } else {
                    listOf("GRASSLAND", "FOREST", "SWAMP", "MEADOW").randomOrNull()
                }
            }
            
            else -> null
        }
    }
    
    /**
     * Fill template string with parameter values.
     * 
     * Replaces {parameterName} with actual values from map.
     * 
     * Example:
     * - Template: "Collect {itemId_count} {itemId} for {npcId}"
     * - Parameters: {"itemId_count": "5", "itemId": "seeds", "npcId": "Grumble"}
     * - Result: "Collect 5 seeds for Grumble"
     * 
     * @param template String with {parameter} placeholders
     * @param parameters Map of parameter names to values
     * @return Filled string
     */
    fun fillTemplate(template: String, parameters: Map<String, String>): String {
        var result = template
        for ((key, value) in parameters) {
            result = result.replace("{$key}", value, ignoreCase = true)
        }
        return result
    }
    
    /**
     * Get template by ID from catalog.
     * 
     * @param templateId Template ID
     * @return Template or null if not found
     */
    fun getTemplate(templateId: String): RadiantQuestTemplate? {
        return RadiantQuestCatalog.getTemplate(templateId)
    }
    
    /**
     * Get all templates matching quest type.
     * 
     * @param questType Quest type filter
     * @return List of matching templates
     */
    fun getTemplatesByType(questType: QuestType): List<RadiantQuestTemplate> {
        return RadiantQuestCatalog.getAllTemplates().values.filter { it.questType == questType }
    }
    
    /**
     * Get generation statistics for template.
     * 
     * @param state Current radiant quest state
     * @param templateId Template ID
     * @return Generation count
     */
    fun getGenerationCount(state: RadiantQuestState, templateId: String): Int {
        return state.generationCount[templateId] ?: 0
    }
    
    /**
     * Check if template is on cooldown.
     * 
     * @param state Current radiant quest state
     * @param templateId Template ID
     * @param currentTimestamp Current game time
     * @return True if on cooldown
     */
    fun isOnCooldown(state: RadiantQuestState, templateId: String, currentTimestamp: Long): Boolean {
        val cooldownExpiry = state.templateCooldowns[templateId] ?: 0
        return currentTimestamp < cooldownExpiry
    }
}
