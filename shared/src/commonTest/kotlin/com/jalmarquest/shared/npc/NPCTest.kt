package com.jalmarquest.shared.npc

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.model.WorldTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NPCTest {
    
    // ===== DATA MODEL TESTS =====
    
    @Test
    fun `NPCPersonality should validate ranges`() {
        val personality = NPCPersonality(
            friendliness = 8,
            courage = 7,
            wisdom = 10,
            humor = 6,
            traits = listOf("wise", "patient")
        )
        
        assertEquals(8, personality.friendliness)
        assertEquals(7, personality.courage)
        assertEquals(10, personality.wisdom)
        assertEquals(6, personality.humor)
        assertEquals(2, personality.traits.size)
    }
    
    @Test
    fun `NPCPersonality getSummary should return descriptors`() {
        val personality = NPCPersonality(
            friendliness = 9, // cheerful
            courage = 2,      // timid
            wisdom = 8,       // wise
            humor = 3,        // serious
            traits = listOf("mysterious")
        )
        
        val summary = personality.getSummary()
        assertTrue(summary.contains("cheerful"))
        assertTrue(summary.contains("timid"))
        assertTrue(summary.contains("wise"))
        assertTrue(summary.contains("serious"))
        assertTrue(summary.contains("mysterious"))
    }
    
    @Test
    fun `ScheduleEntry should contain hour correctly`() {
        val entry = ScheduleEntry(
            startHour = 8,
            endHour = 12,
            locationId = "village",
            activity = "working"
        )
        
        assertTrue(entry.containsHour(8))
        assertTrue(entry.containsHour(10))
        assertFalse(entry.containsHour(12)) // Exclusive end
        assertFalse(entry.containsHour(7))
        assertEquals(4, entry.getDuration())
    }
    
    @Test
    fun `NPCSchedule should find entry for hour`() {
        val schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "home", "sleeping"),
                ScheduleEntry(8, 18, "work", "working"),
                ScheduleEntry(18, 24, "home", "resting")
            )
        )
        
        assertEquals("home", schedule.getEntryForHour(5)?.locationId)
        assertEquals("work", schedule.getEntryForHour(12)?.locationId)
        assertEquals("home", schedule.getEntryForHour(20)?.locationId)
    }
    
    @Test
    fun `NPCSchedule should return all locations`() {
        val schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "home", "sleeping"),
                ScheduleEntry(8, 18, "work", "working"),
                ScheduleEntry(18, 24, "home", "resting")
            )
        )
        
        val locations = schedule.getAllLocations()
        assertEquals(2, locations.size)
        assertTrue(locations.contains("home"))
        assertTrue(locations.contains("work"))
    }
    
    @Test
    fun `NPC getCurrentLocation should return schedule location`() {
        val npc = NPCCatalog.elderQuail
        
        assertEquals("buttonburgh_village", npc.getCurrentLocation(3))  // Sleeping
        assertEquals("buttonburgh_village", npc.getCurrentLocation(10)) // Meditating
        assertEquals("buttonburgh_village", npc.getCurrentLocation(15)) // Greeting visitors
    }
    
    @Test
    fun `NPC getCurrentActivity should return schedule activity`() {
        val npc = NPCCatalog.elderQuail
        
        assertEquals("sleeping", npc.getCurrentActivity(3))
        assertEquals("meditating", npc.getCurrentActivity(10))
        assertEquals("greeting_visitors", npc.getCurrentActivity(15))
    }
    
    @Test
    fun `NPC isAtLocation should check current location`() {
        val npc = NPCCatalog.grumbleForgepaw
        
        assertTrue(npc.isAtLocation("the_quailsmith", 10))  // At work
        assertTrue(npc.isAtLocation("buttonburgh_village", 12)) // Lunch
        assertFalse(npc.isAtLocation("buttonburgh_village", 10)) // At work, not village
    }
    
    @Test
    fun `NPCRelationship should calculate status correctly`() {
        assertEquals(RelationshipStatus.NEUTRAL, NPCRelationship("npc1", 0).getStatus())
        assertEquals(RelationshipStatus.FRIENDLY, NPCRelationship("npc1", 25).getStatus())
        assertEquals(RelationshipStatus.FRIEND, NPCRelationship("npc1", 50).getStatus())
        assertEquals(RelationshipStatus.BEST_FRIEND, NPCRelationship("npc1", 75).getStatus())
        assertEquals(RelationshipStatus.NEUTRAL, NPCRelationship("npc1", -25).getStatus()) // Still neutral
        assertEquals(RelationshipStatus.UNFRIENDLY, NPCRelationship("npc1", -26).getStatus()) // Unfriendly
        assertEquals(RelationshipStatus.UNFRIENDLY, NPCRelationship("npc1", -50).getStatus()) // -50 is UNFRIENDLY
        assertEquals(RelationshipStatus.HOSTILE, NPCRelationship("npc1", -51).getStatus()) // -51 is HOSTILE
        assertEquals(RelationshipStatus.ENEMY, NPCRelationship("npc1", -100).getStatus())
    }
    
    @Test
    fun `NPCRelationship modify should clamp to range`() {
        val relationship = NPCRelationship("npc1", 50)
        
        val increased = relationship.modify(60)
        assertEquals(100, increased.score) // Clamped to max
        
        val decreased = relationship.modify(-200)
        assertEquals(-100, decreased.score) // Clamped to min
    }
    
    @Test
    fun `NPCRelationship should check positive and negative`() {
        assertTrue(NPCRelationship("npc1", 30).isPositive())
        assertFalse(NPCRelationship("npc1", 10).isPositive())
        
        assertTrue(NPCRelationship("npc1", -30).isNegative())
        assertFalse(NPCRelationship("npc1", -10).isNegative())
    }
    
    @Test
    fun `FactionStanding should calculate tier correctly`() {
        assertEquals(ReputationTier.NEUTRAL, FactionStanding("faction1", 0).getTier())
        assertEquals(ReputationTier.HONORED, FactionStanding("faction1", 25).getTier())
        assertEquals(ReputationTier.REVERED, FactionStanding("faction1", 50).getTier())
        assertEquals(ReputationTier.EXALTED, FactionStanding("faction1", 75).getTier())
        assertEquals(ReputationTier.NEUTRAL, FactionStanding("faction1", -25).getTier()) // Still neutral
        assertEquals(ReputationTier.UNFRIENDLY, FactionStanding("faction1", -26).getTier()) // Unfriendly
        assertEquals(ReputationTier.UNFRIENDLY, FactionStanding("faction1", -50).getTier()) // -50 is UNFRIENDLY
        assertEquals(ReputationTier.HOSTILE, FactionStanding("faction1", -51).getTier()) // -51 is HOSTILE
        assertEquals(ReputationTier.HATED, FactionStanding("faction1", -100).getTier())
    }
    
    @Test
    fun `Faction should check ally and enemy status`() {
        val faction = FactionCatalog.buttonburghCouncil
        
        assertTrue(faction.isAlly("buttonburgh_citizens"))
        assertTrue(faction.isEnemy("sparrow_raiders"))
        assertFalse(faction.isAlly("sparrow_raiders"))
        assertFalse(faction.isEnemy("buttonburgh_citizens"))
    }
    
    // ===== NPC CATALOG TESTS =====
    
    @Test
    fun `NPCCatalog should have 16 NPCs`() {
        assertEquals(16, NPCCatalog.getTotalNPCCount())
        assertEquals(16, NPCCatalog.allNPCs.size)
    }
    
    @Test
    fun `NPCCatalog should find NPC by ID`() {
        val npc = NPCCatalog.getNPC("elder_quail")
        assertNotNull(npc)
        assertEquals("Elder Quail", npc.name)
        assertEquals(NPCSpecies.BUTTON_QUAIL, npc.species)
    }
    
    @Test
    fun `NPCCatalog should return null for invalid NPC ID`() {
        assertNull(NPCCatalog.getNPC("invalid_npc"))
    }
    
    @Test
    fun `NPCCatalog should filter NPCs by location and time`() {
        val worldTime = WorldTime(hour = 10) // Morning
        
        // Elder Quail meditating at village at 10am
        val npcsAtVillage = NPCCatalog.getNPCsAtLocation("buttonburgh_village", 10)
        assertTrue(npcsAtVillage.any { it.id == "elder_quail" })
        
        // Grumble at quailsmith working at 10am
        val npcsAtQuailsmith = NPCCatalog.getNPCsAtLocation("the_quailsmith", 10)
        assertTrue(npcsAtQuailsmith.any { it.id == "grumble_forgepaw" })
    }
    
    @Test
    fun `NPCCatalog should filter by occupation`() {
        val warriors = NPCCatalog.getNPCsByOccupation(NPCOccupation.WARRIOR)
        assertEquals(2, warriors.size) // Captain Bravewing, Sparrow Scout
        
        val children = NPCCatalog.getNPCsByOccupation(NPCOccupation.CHILD)
        assertEquals(3, children.size) // Pip, Chirp, Cheep
    }
    
    @Test
    fun `NPCCatalog should filter by faction`() {
        val citizens = NPCCatalog.getNPCsByFaction("buttonburgh_citizens")
        assertTrue(citizens.size >= 3) // Pip, Farmer Cluck, Chirp, Cheep
        
        val guards = NPCCatalog.getNPCsByFaction("buttonburgh_guard")
        assertEquals(2, guards.size) // Captain Bravewing, Guard Peckins
    }
    
    @Test
    fun `NPCCatalog should return quest givers`() {
        val questGivers = NPCCatalog.getQuestGivers()
        assertTrue(questGivers.size >= 5)
        assertTrue(questGivers.any { it.id == "elder_quail" })
        assertTrue(questGivers.any { it.id == "grumble_forgepaw" })
    }
    
    @Test
    fun `NPCCatalog should return merchants`() {
        val merchants = NPCCatalog.getMerchants()
        assertTrue(merchants.size >= 5)
        assertTrue(merchants.any { it.id == "grumble_forgepaw" })
        assertTrue(merchants.any { it.id == "mabel_innkeeper" })
        assertTrue(merchants.any { it.id == "merchant_seedsworth" })
    }
    
    @Test
    fun `NPCCatalog should validate successfully`() {
        assertTrue(NPCCatalog.validateCatalog())
    }
    
    // ===== FACTION CATALOG TESTS =====
    
    @Test
    fun `FactionCatalog should have 11 factions`() {
        assertEquals(11, FactionCatalog.getTotalFactionCount())
    }
    
    @Test
    fun `FactionCatalog should find faction by ID`() {
        val faction = FactionCatalog.getFaction("buttonburgh_council")
        assertNotNull(faction)
        assertEquals("Buttonburgh Council", faction.name)
    }
    
    @Test
    fun `FactionCatalog should return null for invalid faction ID`() {
        assertNull(FactionCatalog.getFaction("invalid_faction"))
    }
    
    @Test
    fun `FactionCatalog should find allied factions`() {
        val allies = FactionCatalog.getAlliedFactions("buttonburgh_council")
        assertTrue(allies.size >= 5)
        assertTrue(allies.any { it.id == "buttonburgh_citizens" })
        assertTrue(allies.any { it.id == "buttonburgh_guard" })
    }
    
    @Test
    fun `FactionCatalog should find enemy factions`() {
        val enemies = FactionCatalog.getEnemyFactions("buttonburgh_council")
        assertTrue(enemies.any { it.id == "sparrow_raiders" })
    }
    
    @Test
    fun `FactionCatalog should check alliance status`() {
        assertTrue(FactionCatalog.areAllies("buttonburgh_council", "buttonburgh_citizens"))
        assertFalse(FactionCatalog.areAllies("buttonburgh_council", "sparrow_raiders"))
    }
    
    @Test
    fun `FactionCatalog should check enemy status`() {
        assertTrue(FactionCatalog.areEnemies("buttonburgh_council", "sparrow_raiders"))
        assertFalse(FactionCatalog.areEnemies("buttonburgh_council", "buttonburgh_citizens"))
    }
    
    @Test
    fun `FactionCatalog should validate successfully`() {
        assertTrue(FactionCatalog.validateCatalog())
    }
    
    // ===== NPC MANAGER TESTS =====
    
    private fun createTestGameState(): GameState {
        return GameState(
            player = Player(
                id = "test_player",
                name = "Hero",
                level = 5,
                stats = PlayerStats(),
                position = Position(0, 0, "buttonburgh_village")
            )
        )
    }
    
    @Test
    fun `NPCManager getNPC should return NPC from catalog`() {
        val manager = NPCManager()
        val npc = manager.getNPC("elder_quail")
        
        assertNotNull(npc)
        assertEquals("Elder Quail", npc.name)
    }
    
    @Test
    fun `NPCManager getCurrentLocation should return NPC location at time`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 10)
        
        val location = manager.getCurrentLocation("elder_quail", worldTime)
        assertEquals("buttonburgh_village", location)
    }
    
    @Test
    fun `NPCManager getCurrentActivity should return NPC activity`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 10)
        
        val activity = manager.getCurrentActivity("elder_quail", worldTime)
        assertEquals("meditating", activity)
    }
    
    @Test
    fun `NPCManager getNPCsAtLocation should return NPCs at location`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 10)
        
        val npcs = manager.getNPCsAtLocation("buttonburgh_village", worldTime)
        assertTrue(npcs.isNotEmpty())
        assertTrue(npcs.any { it.id == "elder_quail" })
    }
    
    @Test
    fun `NPCManager isNPCAvailable should check NPC at player location`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 10)
        
        assertTrue(manager.isNPCAvailable("elder_quail", "buttonburgh_village", worldTime))
        assertFalse(manager.isNPCAvailable("elder_quail", "invalid_location", worldTime))
    }
    
    @Test
    fun `NPCManager getRelationship should return neutral for new NPC`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        val relationship = manager.getRelationship(gameState, "elder_quail")
        assertEquals("elder_quail", relationship.npcId)
        assertEquals(0, relationship.score)
        assertEquals(RelationshipStatus.NEUTRAL, relationship.getStatus())
    }
    
    @Test
    fun `NPCManager modifyRelationship should update relationship score`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        val updatedState = manager.modifyRelationship(gameState, "elder_quail", 30)
        val relationship = manager.getRelationship(updatedState, "elder_quail")
        
        assertEquals(30, relationship.score)
        assertEquals(RelationshipStatus.FRIENDLY, relationship.getStatus())
    }
    
    @Test
    fun `NPCManager modifyRelationship should stack changes`() = runTest {
        val manager = NPCManager()
        var gameState = createTestGameState()
        
        gameState = manager.modifyRelationship(gameState, "elder_quail", 30)
        gameState = manager.modifyRelationship(gameState, "elder_quail", 25)
        
        val relationship = manager.getRelationship(gameState, "elder_quail")
        assertEquals(55, relationship.score)
        assertEquals(RelationshipStatus.FRIEND, relationship.getStatus())
    }
    
    @Test
    fun `NPCManager getFactionStanding should return neutral for new faction`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        val standing = manager.getFactionStanding(gameState, "buttonburgh_council")
        assertEquals("buttonburgh_council", standing.factionId)
        assertEquals(0, standing.reputation)
        assertEquals(ReputationTier.NEUTRAL, standing.getTier())
    }
    
    @Test
    fun `NPCManager modifyFactionReputation should update reputation`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        val updatedState = manager.modifyFactionReputation(gameState, "buttonburgh_council", 40)
        val standing = manager.getFactionStanding(updatedState, "buttonburgh_council")
        
        assertEquals(40, standing.reputation)
        assertEquals(ReputationTier.HONORED, standing.getTier())
    }
    
    @Test
    fun `NPCManager modifyFactionNPCRelationships should update all faction NPCs`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        val updatedState = manager.modifyFactionNPCRelationships(gameState, "buttonburgh_council", 20)
        val elderRelationship = manager.getRelationship(updatedState, "elder_quail")
        
        assertEquals(20, elderRelationship.score)
    }
    
    @Test
    fun `NPCManager getAvailableQuestGivers should return quest givers at location`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 15) // Afternoon
        
        val questGivers = manager.getAvailableQuestGivers("buttonburgh_village", worldTime)
        assertTrue(questGivers.any { it.id == "elder_quail" })
    }
    
    @Test
    fun `NPCManager getAvailableMerchants should return merchants at location`() {
        val manager = NPCManager()
        val worldTime = WorldTime(hour = 10)
        
        val merchants = manager.getAvailableMerchants("the_gilded_seed_inn", worldTime)
        assertTrue(merchants.any { it.id == "mabel_innkeeper" })
    }
    
    @Test
    fun `NPCManager meetsRelationshipRequirement should check minimum score`() = runTest {
        val manager = NPCManager()
        var gameState = createTestGameState()
        
        gameState = manager.modifyRelationship(gameState, "elder_quail", 60)
        
        assertTrue(manager.meetsRelationshipRequirement(gameState, "elder_quail", 50))
        assertFalse(manager.meetsRelationshipRequirement(gameState, "elder_quail", 70))
    }
    
    @Test
    fun `NPCManager meetsFactionRequirement should check minimum reputation`() = runTest {
        val manager = NPCManager()
        var gameState = createTestGameState()
        
        gameState = manager.modifyFactionReputation(gameState, "buttonburgh_council", 30)
        
        assertTrue(manager.meetsFactionRequirement(gameState, "buttonburgh_council", 25))
        assertFalse(manager.meetsFactionRequirement(gameState, "buttonburgh_council", 50))
    }
    
    @Test
    fun `NPCManager getNPCPersonality should return personality summary`() {
        val manager = NPCManager()
        val personality = manager.getNPCPersonality("elder_quail")
        
        assertNotNull(personality)
        assertTrue(personality.contains("wise"))
    }
    
    @Test
    fun `NPCManager getNPCsByOccupation should filter by occupation`() {
        val manager = NPCManager()
        val warriors = manager.getNPCsByOccupation(NPCOccupation.WARRIOR)
        
        assertEquals(2, warriors.size)
    }
    
    @Test
    fun `NPCManager getFaction should return faction from catalog`() {
        val manager = NPCManager()
        val faction = manager.getFaction("buttonburgh_council")
        
        assertNotNull(faction)
        assertEquals("Buttonburgh Council", faction.name)
    }
    
    @Test
    fun `NPCManager areFactionsAllied should check alliance status`() {
        val manager = NPCManager()
        
        assertTrue(manager.areFactionsAllied("buttonburgh_council", "buttonburgh_citizens"))
        assertFalse(manager.areFactionsAllied("buttonburgh_council", "sparrow_raiders"))
    }
    
    @Test
    fun `NPCManager areFactionsEnemies should check enemy status`() {
        val manager = NPCManager()
        
        assertTrue(manager.areFactionsEnemies("buttonburgh_council", "sparrow_raiders"))
        assertFalse(manager.areFactionsEnemies("buttonburgh_council", "buttonburgh_citizens"))
    }
    
    @Test
    fun `NPCManager should handle concurrent relationship modifications`() = runTest {
        val manager = NPCManager()
        val gameState = createTestGameState()
        
        // Simulate concurrent modifications (mutex should handle this)
        val state1 = manager.modifyRelationship(gameState, "elder_quail", 10)
        val state2 = manager.modifyRelationship(state1, "grumble_forgepaw", 15)
        
        val elderRel = manager.getRelationship(state2, "elder_quail")
        val grumbleRel = manager.getRelationship(state2, "grumble_forgepaw")
        
        assertEquals(10, elderRel.score)
        assertEquals(15, grumbleRel.score)
    }
}
