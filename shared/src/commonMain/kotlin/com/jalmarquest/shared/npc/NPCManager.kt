package com.jalmarquest.shared.npc

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.WorldTime
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages NPC-related operations including schedules, relationships, and factions.
 * 
 * Thread-safe manager using Mutex for concurrent access protection.
 * Uses stateless functional pattern - all operations return new GameState instances.
 */
class NPCManager {
    private val mutex = Mutex()
    
    /**
     * Gets an NPC by ID from the catalog.
     */
    fun getNPC(npcId: String): NPC? {
        return NPCCatalog.getNPC(npcId)
    }
    
    /**
     * Gets an NPC's current location based on world time.
     */
    fun getCurrentLocation(npcId: String, worldTime: WorldTime): String? {
        val npc = getNPC(npcId) ?: return null
        return npc.getCurrentLocation(worldTime.hour)
    }
    
    /**
     * Gets an NPC's current activity.
     */
    fun getCurrentActivity(npcId: String, worldTime: WorldTime): String? {
        val npc = getNPC(npcId) ?: return null
        return npc.getCurrentActivity(worldTime.hour)
    }
    
    /**
     * Gets all NPCs currently at a specific location.
     */
    fun getNPCsAtLocation(locationId: String, worldTime: WorldTime): List<NPC> {
        return NPCCatalog.getNPCsAtLocation(locationId, worldTime.hour)
    }
    
    /**
     * Checks if an NPC is available for interaction at player's location.
     */
    fun isNPCAvailable(npcId: String, playerLocationId: String, worldTime: WorldTime): Boolean {
        val npcLocation = getCurrentLocation(npcId, worldTime) ?: return false
        return npcLocation == playerLocationId
    }
    
    /**
     * Gets player's relationship with an NPC.
     * Returns a neutral relationship (score 0) if not yet established.
     */
    suspend fun getRelationship(gameState: GameState, npcId: String): NPCRelationship = mutex.withLock {
        gameState.npcRelationships.find { it.npcId == npcId }
            ?: NPCRelationship(npcId = npcId, score = 0)
    }
    
    /**
     * Modifies player's relationship with an NPC.
     * Returns updated GameState with modified relationship.
     */
    suspend fun modifyRelationship(
        gameState: GameState,
        npcId: String,
        change: Int
    ): GameState = mutex.withLock {
        // Find existing relationship or create new one
        val existing = gameState.npcRelationships.find { it.npcId == npcId }
        val updated = (existing ?: NPCRelationship(npcId = npcId, score = 0)).modify(change)
        
        // Replace or add relationship
        val newRelationships = if (existing != null) {
            gameState.npcRelationships.map { if (it.npcId == npcId) updated else it }
        } else {
            gameState.npcRelationships + updated
        }
        
        return gameState.copy(npcRelationships = newRelationships)
    }
    
    /**
     * Gets all NPC relationships for the player.
     */
    suspend fun getAllRelationships(gameState: GameState): List<NPCRelationship> = mutex.withLock {
        return gameState.npcRelationships
    }
    
    /**
     * Gets player's standing with a faction.
     * Returns neutral standing (reputation 0) if not yet established.
     */
    suspend fun getFactionStanding(gameState: GameState, factionId: String): FactionStanding = mutex.withLock {
        gameState.factionStandings.find { it.factionId == factionId }
            ?: FactionStanding(factionId = factionId, reputation = 0)
    }
    
    /**
     * Modifies player's reputation with a faction.
     * Returns updated GameState with modified faction standing.
     */
    suspend fun modifyFactionReputation(
        gameState: GameState,
        factionId: String,
        change: Int
    ): GameState = mutex.withLock {
        // Find existing standing or create new one
        val existing = gameState.factionStandings.find { it.factionId == factionId }
        val updated = (existing ?: FactionStanding(factionId = factionId, reputation = 0)).modify(change)
        
        // Replace or add standing
        val newStandings = if (existing != null) {
            gameState.factionStandings.map { if (it.factionId == factionId) updated else it }
        } else {
            gameState.factionStandings + updated
        }
        
        return gameState.copy(factionStandings = newStandings)
    }
    
    /**
     * Modifies reputation with all NPCs in a faction.
     * Useful for faction-wide events (e.g., completing a major quest).
     */
    suspend fun modifyFactionNPCRelationships(
        gameState: GameState,
        factionId: String,
        change: Int
    ): GameState {
        var updatedState = gameState
        val factionNPCs = NPCCatalog.getNPCsByFaction(factionId)
        
        factionNPCs.forEach { npc ->
            updatedState = modifyRelationship(updatedState, npc.id, change)
        }
        
        return updatedState
    }
    
    /**
     * Gets all faction standings for the player.
     */
    suspend fun getAllFactionStandings(gameState: GameState): List<FactionStanding> = mutex.withLock {
        return gameState.factionStandings
    }
    
    /**
     * Gets all quest givers currently at player's location.
     */
    fun getAvailableQuestGivers(playerLocationId: String, worldTime: WorldTime): List<NPC> {
        val npcsAtLocation = getNPCsAtLocation(playerLocationId, worldTime)
        return npcsAtLocation.filter { it.questGiverIds.isNotEmpty() }
    }
    
    /**
     * Gets all merchants currently at player's location.
     */
    fun getAvailableMerchants(playerLocationId: String, worldTime: WorldTime): List<NPC> {
        val npcsAtLocation = getNPCsAtLocation(playerLocationId, worldTime)
        return npcsAtLocation.filter { it.merchantInventory.isNotEmpty() }
    }
    
    /**
     * Checks if player meets relationship requirement for an NPC interaction.
     */
    suspend fun meetsRelationshipRequirement(
        gameState: GameState,
        npcId: String,
        minimumScore: Int
    ): Boolean {
        val relationship = getRelationship(gameState, npcId)
        return relationship.score >= minimumScore
    }
    
    /**
     * Checks if player meets faction reputation requirement.
     */
    suspend fun meetsFactionRequirement(
        gameState: GameState,
        factionId: String,
        minimumReputation: Int
    ): Boolean {
        val standing = getFactionStanding(gameState, factionId)
        return standing.reputation >= minimumReputation
    }
    
    /**
     * Gets NPC's personality summary.
     */
    fun getNPCPersonality(npcId: String): String? {
        val npc = getNPC(npcId) ?: return null
        return npc.personality.getSummary()
    }
    
    /**
     * Gets all NPCs with a specific occupation.
     */
    fun getNPCsByOccupation(occupation: NPCOccupation): List<NPC> {
        return NPCCatalog.getNPCsByOccupation(occupation)
    }
    
    /**
     * Gets faction information.
     */
    fun getFaction(factionId: String): Faction? {
        return FactionCatalog.getFaction(factionId)
    }
    
    /**
     * Checks if two factions are allies.
     */
    fun areFactionsAllied(factionId1: String, factionId2: String): Boolean {
        return FactionCatalog.areAllies(factionId1, factionId2)
    }
    
    /**
     * Checks if two factions are enemies.
     */
    fun areFactionsEnemies(factionId1: String, factionId2: String): Boolean {
        return FactionCatalog.areEnemies(factionId1, factionId2)
    }
}
