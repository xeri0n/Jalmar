package com.jalmarquest.shared.state

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.movement.MovementResult
import com.jalmarquest.shared.time.TimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Centralized, thread-safe game state manager.
 * All state mutations must go through this class to ensure consistency.
 */
class GameStateManager {
    private val mutex = Mutex()
    private val _gameState = MutableStateFlow<GameState?>(null)
    
    /**
     * Observable game state. Null means no game is loaded.
     */
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()
    
    /**
     * Create a new game with the given player name.
     */
    suspend fun createNewGame(playerName: String): GameState {
        return mutex.withLock {
            val playerId = generatePlayerId()
            val newState = GameState.createNew(playerName, playerId)
            _gameState.value = newState
            newState
        }
    }
    
    /**
     * Load an existing game state.
     */
    suspend fun loadGame(state: GameState) {
        mutex.withLock {
            require(state.isCompatibleVersion()) { 
                "Incompatible save version: ${state.version}" 
            }
            _gameState.value = state
        }
    }
    
    /**
     * Clear the current game state.
     */
    suspend fun clearGame() {
        mutex.withLock {
            _gameState.value = null
        }
    }
    
    /**
     * Update the game state atomically.
     */
    suspend fun updateState(update: (GameState) -> GameState) {
        mutex.withLock {
            val current = _gameState.value ?: throw IllegalStateException("No game loaded")
            _gameState.value = update(current)
        }
    }
    
    // Player mutations
    
    suspend fun updatePlayerStats(update: (PlayerStats) -> PlayerStats) {
        updateState { state ->
            state.copy(
                player = state.player.copy(
                    stats = update(state.player.stats)
                )
            )
        }
    }
    
    suspend fun updatePlayerPosition(newPosition: Position) {
        updateState { state ->
            state.copy(
                player = state.player.copy(position = newPosition)
            )
        }
    }
    
    /**
     * Execute a successful movement, updating player position, consuming stamina,
     * and advancing world time.
     * This should be called AFTER MovementManager validates the move.
     * 
     * @param movementResult The successful movement result from MovementManager
     * @return The updated player position
     * @throws IllegalArgumentException if movementResult is not Success
     */
    suspend fun executeMove(movementResult: MovementResult.Success): Position {
        return mutex.withLock {
            val current = _gameState.value ?: throw IllegalStateException("No game loaded")
            
            // Validate stamina is sufficient (defensive check)
            require(current.player.stats.currentStamina >= movementResult.staminaCost) {
                "Insufficient stamina for movement: ${current.player.stats.currentStamina} < ${movementResult.staminaCost}"
            }
            
            // Create new position
            val newPosition = Position(
                x = current.player.position.x,
                y = current.player.position.y,
                locationId = movementResult.newLocationId
            )
            
            // Advance world time by movement time cost
            val newWorldTime = TimeManager.advanceWorldTime(current.worldTime, movementResult.timeCost)
            
            // Update player with new position and reduced stamina
            val updatedPlayer = current.player.copy(
                position = newPosition,
                stats = current.player.stats.copy(
                    currentStamina = current.player.stats.currentStamina - movementResult.staminaCost
                )
            )
            
            _gameState.value = current.copy(
                player = updatedPlayer,
                worldTime = newWorldTime
            )
            newPosition
        }
    }
    
    suspend fun addExperience(amount: Long) {
        updateState { state ->
            val newExp = state.player.experience + amount
            var player = state.player.copy(experience = newExp)
            
            // Auto-level up
            while (player.canLevelUp()) {
                player = levelUpPlayer(player)
            }
            
            state.copy(player = player)
        }
    }
    
    suspend fun addSeeds(amount: Long) {
        require(amount >= 0) { "Cannot add negative seeds" }
        updateState { state ->
            state.copy(
                player = state.player.copy(
                    seeds = state.player.seeds + amount
                )
            )
        }
    }
    
    suspend fun removeSeeds(amount: Long): Boolean {
        require(amount >= 0) { "Cannot remove negative seeds" }
        return try {
            updateState { state ->
                if (state.player.seeds < amount) {
                    throw InsufficientCurrencyException("Not enough seeds")
                }
                state.copy(
                    player = state.player.copy(
                        seeds = state.player.seeds - amount
                    )
                )
            }
            true
        } catch (e: InsufficientCurrencyException) {
            false
        }
    }
    
    suspend fun addGlimmerShards(amount: Long) {
        require(amount >= 0) { "Cannot add negative glimmer shards" }
        updateState { state ->
            state.copy(
                player = state.player.copy(
                    glimmerShards = state.player.glimmerShards + amount
                )
            )
        }
    }
    
    suspend fun removeGlimmerShards(amount: Long): Boolean {
        require(amount >= 0) { "Cannot remove negative glimmer shards" }
        return try {
            updateState { state ->
                if (state.player.glimmerShards < amount) {
                    throw InsufficientCurrencyException("Not enough glimmer shards")
                }
                state.copy(
                    player = state.player.copy(
                        glimmerShards = state.player.glimmerShards - amount
                    )
                )
            }
            true
        } catch (e: InsufficientCurrencyException) {
            false
        }
    }
    
    // World state mutations
    
    suspend fun discoverLocation(locationId: String) {
        updateState { state ->
            state.copy(
                discoveredLocations = state.discoveredLocations + locationId
            )
        }
    }
    
    suspend fun unlockRecipe(recipeId: String) {
        updateState { state ->
            state.copy(
                unlockedRecipes = state.unlockedRecipes + recipeId
            )
        }
    }
    
    suspend fun completeQuest(questId: String) {
        updateState { state ->
            state.copy(
                activeQuests = state.activeQuests - questId,
                completedQuests = state.completedQuests + questId
            )
        }
    }
    
    suspend fun startQuest(questId: String) {
        updateState { state ->
            if (questId in state.completedQuests || questId in state.activeQuests) {
                throw IllegalStateException("Quest already started or completed")
            }
            state.copy(
                activeQuests = state.activeQuests + questId
            )
        }
    }
    
    // Skill management
    
    suspend fun learnSkill(skillId: String): Boolean {
        return try {
            updateState { state ->
                val player = state.player
                val skill = com.jalmarquest.shared.skills.SkillCatalog.getSkill(skillId)
                    ?: throw IllegalArgumentException("Skill not found: $skillId")
                
                // Validation
                if (skillId in player.learnedSkills) {
                    throw IllegalStateException("Skill already learned")
                }
                if (player.level < skill.getRequiredLevel()) {
                    throw IllegalStateException("Level too low")
                }
                if (player.skillPoints < skill.getSkillPointCost()) {
                    throw IllegalStateException("Insufficient skill points")
                }
                val missingPrereqs = skill.prerequisiteSkills.filter { it !in player.learnedSkills }
                if (missingPrereqs.isNotEmpty()) {
                    throw IllegalStateException("Missing prerequisites: $missingPrereqs")
                }
                
                // Learn skill
                state.copy(
                    player = player.copy(
                        learnedSkills = player.learnedSkills + skillId,
                        skillPoints = player.skillPoints - skill.getSkillPointCost()
                    )
                )
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun resetSkills() {
        updateState { state ->
            val totalSpent = state.player.learnedSkills.mapNotNull {
                com.jalmarquest.shared.skills.SkillCatalog.getSkill(it)
            }.sumOf { it.getSkillPointCost() }
            
            state.copy(
                player = state.player.copy(
                    learnedSkills = emptySet(),
                    skillPoints = state.player.skillPoints + totalSpent
                )
            )
        }
    }
    
    suspend fun setFlag(key: String, value: Boolean) {
        updateState { state ->
            state.copy(
                flags = state.flags + (key to value)
            )
        }
    }
    
    suspend fun getFlag(key: String): Boolean {
        return mutex.withLock {
            _gameState.value?.flags?.get(key) ?: false
        }
    }
    
    // Helper functions
    
    private fun levelUpPlayer(player: Player): Player {
        val newLevel = player.level + 1
        val baseStatsIncrease = 5
        
        return player.copy(
            level = newLevel,
            skillPoints = player.skillPoints + 1, // Grant 1 skill point per level
            stats = player.stats.copy(
                maxHealth = player.stats.maxHealth + baseStatsIncrease,
                currentHealth = player.stats.maxHealth + baseStatsIncrease,
                maxStamina = player.stats.maxStamina + baseStatsIncrease,
                currentStamina = player.stats.maxStamina + baseStatsIncrease,
                maxMagic = player.stats.maxMagic + baseStatsIncrease,
                currentMagic = player.stats.maxMagic + baseStatsIncrease,
                attack = player.stats.attack + 2,
                defense = player.stats.defense + 2,
                magicPower = player.stats.magicPower + 2,
                speed = player.stats.speed + 1
            )
        )
    }
    
    private fun generatePlayerId(): String {
        return "player_${System.currentTimeMillis()}_${(0..9999).random()}"
    }
}

class InsufficientCurrencyException(message: String) : Exception(message)
