package stub

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

/**
 * Stub implementation for the Leaderboard System (Phase 8.3).
 * Foundation for competitive ranking systems.
 */
class StubLeaderboardManager {
    private val _rankings = MutableStateFlow<Map<LeaderboardCategory, List<LeaderboardEntry>>>(emptyMap())
    val rankings: StateFlow<Map<LeaderboardCategory, List<LeaderboardEntry>>> = _rankings.asStateFlow()
    
    suspend fun submitScore(category: LeaderboardCategory, score: Int) {
        // Stub: Will implement score submission
    }
    
    suspend fun getRankings(category: LeaderboardCategory, count: Int = 100): List<LeaderboardEntry> {
        // Stub: Will fetch rankings from backend
        return emptyList()
    }
    
    suspend fun getPlayerRank(category: LeaderboardCategory, playerId: String): Int? {
        // Stub: Will get specific player's rank
        return null
    }
    
    suspend fun refreshLeaderboards() {
        // Stub: Will sync with backend
    }
    
    suspend fun resetSeasonalLeaderboards() {
        // Stub: Will handle seasonal resets
    }
}

@Serializable
enum class LeaderboardCategory {
    HOARD_VALUE,
    PLAYER_LEVEL,
    QUESTS_COMPLETED,
    COMBAT_VICTORIES,
    CRAFTING_MASTERY,
    EXPLORATION_PERCENTAGE,
    SEASONAL_CHRONICLE_TIER,
    GUILD_CONTRIBUTION
}

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val playerId: String,
    val playerName: String,
    val score: Int,
    val timestamp: Long,
    val metadata: Map<String, String> = emptyMap()
)
