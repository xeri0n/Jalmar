package com.jalmarquest.shared.progression

import com.jalmarquest.shared.model.AchievementProgress
import com.jalmarquest.shared.model.AchievementsCatalog
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.PlayerStatistics
import com.jalmarquest.shared.state.GameStateManager

/**
 * Thread-safe manager that updates player statistics and unlocks achievements.
 * All mutations route through GameStateManager.updateState using its Mutex.
 */
class StatsAchievementsManager(
    private val gameStateManager: GameStateManager
) {
    // ---- Stats ----
    suspend fun incrementSteps(amount: Long) {
        if (amount <= 0) return
        updateStats { it.copy(stepsTaken = it.stepsTaken + amount) }
        maybeUnlock("first_steps") { s -> s.statistics.stepsTaken >= 1 }
    }

    suspend fun recordEnemyDefeated() {
        updateStats { it.copy(enemiesDefeated = it.enemiesDefeated + 1) }
    }

    suspend fun recordCraft(itemId: String) {
        updateStats { it.copy(itemsCrafted = it.itemsCrafted + 1) }
        if (itemId == "twig_spear") {
            maybeUnlock("twig_spear_crafted") { true }
        }
    }

    suspend fun recordQuestCompleted() {
        updateStats { it.copy(questsCompleted = it.questsCompleted + 1) }
    }

    suspend fun addSeeds(amount: Long) {
        if (amount <= 0) return
        updateStats { it.copy(seedsCollected = it.seedsCollected + amount) }
    }

    suspend fun recordDamage(dealt: Long = 0, taken: Long = 0) {
        if (dealt <= 0 && taken <= 0) return
        updateStats { it.copy(
            damageDealt = it.damageDealt + maxOf(0, dealt),
            damageTaken = it.damageTaken + maxOf(0, taken)
        ) }
    }

    suspend fun recordPuddleCrossed() {
        updateStats { it.copy(puddlesCrossed = it.puddlesCrossed + 1) }
        maybeUnlock("puddle_conqueror") { s -> s.statistics.puddlesCrossed >= 1 }
    }

    suspend fun recordGnomeSpotted() {
        updateStats { it.copy(gnomesSpotted = it.gnomesSpotted + 1) }
    }

    // ---- Internals ----
    private suspend fun updateStats(mutator: (PlayerStatistics) -> PlayerStatistics) {
        gameStateManager.updateState { state ->
            state.copy(statistics = mutator(state.statistics))
        }
    }

    private suspend fun maybeUnlock(id: String, predicate: (GameState) -> Boolean) {
        // Fast check first: if already unlocked, skip
        val state = gameStateManager.gameState.value ?: return
        if (state.achievements.any { it.id == id && it.unlocked }) return

        gameStateManager.updateState { current ->
            // Re-check inside lock
            if (current.achievements.any { it.id == id && it.unlocked }) return@updateState current
            if (!predicate(current)) return@updateState current

            // Only unlock if known in catalog; if not present, allow but keep flexible
            AchievementsCatalog.get(id)
            val newProgress = AchievementProgress(
                id = id,
                unlocked = true,
                unlockedAt = System.currentTimeMillis()
            )
            val filtered = current.achievements.filterNot { it.id == id }
            current.copy(achievements = filtered + newProgress)
        }
    }
}
