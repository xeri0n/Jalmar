package stub

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

/**
 * Stub implementation for Gossip & Rumor System (Phase 7.5).
 * Foundation for AI-powered information spread.
 */
class StubGossipManager {
    private val _activeRumors = MutableStateFlow<List<Rumor>>(emptyList())
    val activeRumors: StateFlow<List<Rumor>> = _activeRumors.asStateFlow()
    
    suspend fun startRumor(rumorType: String, subject: String, origin: String) {
        // Stub: Will create new rumors based on events
    }
    
    suspend fun spreadRumor(rumorId: String, fromNpc: String, toNpc: String) {
        // Stub: Will implement rumor propagation algorithm
    }
    
    suspend fun mutateRumor(rumorId: String): Rumor? {
        // Stub: Will implement rumor mutation/distortion
        return null
    }
    
    suspend fun checkReputationImpact(rumorId: String, targetId: String): Int {
        // Stub: Will calculate reputation effects
        return 0
    }
    
    suspend fun getNpcKnownRumors(npcId: String): List<Rumor> {
        // Stub: Will get rumors known by specific NPC
        return emptyList()
    }
}

@Serializable
data class Rumor(
    val id: String,
    val type: RumorType,
    val subject: String,
    val content: String,
    val origin: String,
    val spreadCount: Int = 0,
    val mutationLevel: Int = 0,
    val believers: List<String> = emptyList(),
    val deniers: List<String> = emptyList()
)

@Serializable
enum class RumorType {
    HEROIC_DEED,
    SCANDAL,
    WARNING,
    PROPHECY,
    TRADE_TIP,
    FACTION_NEWS,
    ROMANCE,
    CONSPIRACY
}
