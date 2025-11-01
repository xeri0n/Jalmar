package event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Central event bus for all game systems to communicate.
 * Enables loose coupling between managers while maintaining reactivity.
 */
object EventBus {
    private val _events = MutableSharedFlow<GameEvent>(replay = 0, extraBufferCapacity = 100)
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()
    
    suspend fun emit(event: GameEvent) {
        _events.emit(event)
    }
}

/**
 * Base interface for all game events
 */
sealed interface GameEvent {
    // Player events
    data class PlayerMoved(val newLocationId: String, val oldLocationId: String) : GameEvent
    data class PlayerLeveledUp(val newLevel: Int, val archetype: String?) : GameEvent
    data class PlayerDied(val cause: String) : GameEvent
    
    // Combat events
    data class CombatStarted(val enemyId: String, val locationId: String) : GameEvent
    data class CombatEnded(val victory: Boolean, val rewards: CombatRewards?) : GameEvent
    data class EnemyDefeated(val enemyId: String, val loot: List<String>) : GameEvent
    
    // Quest events
    data class QuestStarted(val questId: Int) : GameEvent
    data class QuestCompleted(val questId: Int, val choiceMade: String?) : GameEvent
    data class QuestObjectiveCompleted(val questId: Int, val objectiveIndex: Int) : GameEvent
    
    // Item/Inventory events
    data class ItemAcquired(val itemId: String, val quantity: Int, val source: String) : GameEvent
    data class ItemCrafted(val recipeId: String, val itemId: String, val quantity: Int) : GameEvent
    data class ItemEquipped(val itemId: String, val slot: String) : GameEvent
    data class ShinyCollected(val shinyId: String, val value: Int) : GameEvent
    
    // Nest events
    data class NestUpgraded(val upgradeId: String, val tier: Int) : GameEvent
    data class CosmeticPlaced(val cosmeticId: String, val position: Pair<Int, Int>) : GameEvent
    data class CritterAdded(val critterId: String) : GameEvent
    
    // NPC/Relationship events
    data class NPCInteraction(val npcId: String, val interactionType: String) : GameEvent
    data class AffinityChanged(val npcId: String, val newAffinity: Int, val change: Int) : GameEvent
    data class GiftGiven(val npcId: String, val itemId: String) : GameEvent
    
    // World events
    data class LocationDiscovered(val locationId: String) : GameEvent
    data class TimeChanged(val hour: Int, val day: Int, val season: String) : GameEvent
    data class WeatherChanged(val newWeather: String, val locationId: String) : GameEvent
    data class ResourceHarvested(val nodeId: String, val resourceType: String, val amount: Int) : GameEvent
    
    // Faction events
    data class FactionReputationChanged(val factionId: String, val newRep: Int, val change: Int) : GameEvent
    data class TerritoryControlChanged(val territoryId: String, val newFaction: String) : GameEvent
    
    // Economy events
    data class CurrencyChanged(val currencyType: String, val amount: Int, val change: Int) : GameEvent
    data class TransactionCompleted(val shopId: String, val itemId: String, val price: Int) : GameEvent
    
    // Achievement/Progress events
    data class AchievementUnlocked(val achievementId: String) : GameEvent
    data class ThoughtInternalized(val thoughtId: String) : GameEvent
    data class SkillLevelUp(val skillId: String, val newLevel: Int) : GameEvent
    data class TalentPointEarned(val archetype: String) : GameEvent
    
    // AI Director events
    data class WorldEventTriggered(val eventType: String, val description: String) : GameEvent
    data class RadiantQuestGenerated(val questTemplate: String) : GameEvent
    data class ButterflyEffectTriggered(val choiceTag: String, val consequence: String) : GameEvent
    
    // Save/Load events
    data class GameSaved(val saveSlot: Int) : GameEvent
    data class GameLoaded(val saveSlot: Int) : GameEvent
    
    // IAP/Monetization events
    data class PurchaseCompleted(val productId: String, val shards: Int) : GameEvent
    data class SeasonalChronicleProgress(val tier: Int, val xp: Int) : GameEvent
}

data class CombatRewards(
    val experience: Int,
    val seeds: Int,
    val items: List<String>,
    val thoughts: List<String>? = null
)
