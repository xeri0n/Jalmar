package com.jalmarquest.shared.model

import com.jalmarquest.shared.ai.AIDirector
import com.jalmarquest.shared.butterfly.ButterflyEffectState
import com.jalmarquest.shared.companion.CompanionProgress
import com.jalmarquest.shared.dialogue.DialogueMemory
import com.jalmarquest.shared.difficulty.DifficultyState
import com.jalmarquest.shared.events.WorldEventState
import com.jalmarquest.shared.gossip.GossipState
import com.jalmarquest.shared.nest.Nest
import com.jalmarquest.shared.nest.NestManager
import com.jalmarquest.shared.npc.NPCRelationship
import com.jalmarquest.shared.npc.FactionStanding
import com.jalmarquest.shared.radiant.RadiantQuestState
import com.jalmarquest.shared.weather.Weather
import com.jalmarquest.shared.world.LocationDiscovery
import kotlinx.serialization.Serializable

/**
 * Root game state containing all game data.
 * This is the single source of truth for the game.
 */
@Serializable
data class GameState(
    val version: Int = 1,
    val player: Player,
    val nest: Nest = NestManager.createBasicNest(),
    val aiDirector: AIDirector = AIDirector(),
    val butterflyEffect: ButterflyEffectState = ButterflyEffectState(),
    val worldEvents: WorldEventState = WorldEventState(),
    val radiantQuests: RadiantQuestState = RadiantQuestState(),
    val gossipState: GossipState = GossipState(),
    val difficultyState: DifficultyState = DifficultyState(),
    val worldTime: WorldTime = WorldTime(),
    val weather: Weather = Weather.CLEAR_SKY,
    val statistics: PlayerStatistics = PlayerStatistics(),
    val achievements: List<AchievementProgress> = emptyList(),
    val discoveredLocations: Set<String> = emptySet(),
    val locationDiscoveries: Map<String, LocationDiscovery> = emptyMap(),
    val unlockedRecipes: Set<String> = emptySet(),
    val completedQuests: Set<String> = emptySet(),
    val activeQuests: List<String> = emptyList(),
    val dialogueMemory: DialogueMemory = DialogueMemory(),
    val npcRelationships: List<NPCRelationship> = emptyList(),
    val factionStandings: List<FactionStanding> = emptyList(),
    val flags: Map<String, Boolean> = emptyMap(),
    val recruitedCompanions: Set<String> = emptySet(),
    val activeCompanionId: String? = null,
    val companionProgress: Map<String, CompanionProgress> = emptyMap(),
    val saveTimestamp: Long = 0L
) {
    companion object {
        const val CURRENT_VERSION = 1
        
        fun createNew(playerName: String, playerId: String): GameState {
            return GameState(
                version = CURRENT_VERSION,
                player = Player(
                    id = playerId,
                    name = playerName
                ),
                saveTimestamp = System.currentTimeMillis()
            )
        }
    }
    
    fun isCompatibleVersion(): Boolean = version <= CURRENT_VERSION
}

/**
 * World time tracking with seasons and day/night cycle.
 */
@Serializable
data class WorldTime(
    val totalTicks: Long = 0,
    val season: Season = Season.SPRING,
    val day: Int = 1,
    val hour: Int = 6,
    val minute: Int = 0
) {
    companion object {
        const val TICKS_PER_MINUTE = 60
        const val MINUTES_PER_HOUR = 60
        const val HOURS_PER_DAY = 24
        const val DAYS_PER_SEASON = 30
    }
    
    fun isDay(): Boolean = hour in 6..18
    
    fun isNight(): Boolean = !isDay()
    
    fun getTimeOfDay(): TimeOfDay = when (hour) {
        in 5..11 -> TimeOfDay.MORNING
        in 12..17 -> TimeOfDay.AFTERNOON
        in 18..21 -> TimeOfDay.EVENING
        else -> TimeOfDay.NIGHT
    }
}

@Serializable
enum class Season {
    SPRING, SUMMER, AUTUMN, WINTER
}

enum class TimeOfDay {
    MORNING, AFTERNOON, EVENING, NIGHT
}
