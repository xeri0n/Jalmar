# Phase 7.5 - Gossip & Rumor System ✅

**Duration:** Day 9  
**Status:** COMPLETE (integrated with GameState, 35 tests passing, 1,083 total tests)

## Overview

Implemented the Gossip & Rumor System - an AI-powered social network simulation where player actions become rumors that spread through NPC populations with authentic "telephone game" mutations. The system tracks original truth and mutated versions, calculates spread probability based on relationships/proximity/factions, applies cooldowns to prevent spam, manages reputation effects for faction members, and creates emergent storytelling through cascading truth degradation.

## Core Philosophy

- **Telephone Game Authenticity:** 4-stage mutation progression mimics real information degradation
- **Social Network Tracking:** Complete "who told whom" graph for visualizing rumor spread
- **Reputation Consequences:** Faction-based reputation changes from rumor content
- **Stateless Functional Design:** All state in data classes, pure functions, 100% testable
- **Probability-Based Spreading:** 50% base chance with relationship/proximity/faction multipliers
- **Cooldown Management:** Tick-based cooldowns prevent NPCs from spamming same rumors
- **Long-Term Impact:** Rumors become permanent world history, affecting future interactions

## Files Created

```
shared/src/commonMain/kotlin/com/jalmarquest/shared/gossip/
├── Gossip.kt (~280 lines)
│   ├── Rumor data class (11 fields, truth tracking)
│   ├── RumorCategory enum (6 types)
│   ├── TruthLevel enum (4 levels based on mutation count)
│   ├── MutationType enum (4 types)
│   ├── RumorMutation data class
│   ├── ReputationEffect data class
│   ├── RumorTemplate data class
│   ├── MutationPath data class
│   ├── GossipState data class (5 maps/lists)
│   ├── RumorStatistics data class
│   └── StartRumorResult/SpreadRumorResult sealed classes
│
├── GossipManager.kt (~390 lines)
│   ├── startRumor() - Create new rumor from player action
│   ├── spreadRumor() - Spread with probability checks and mutations
│   ├── attemptMutation() - 20% chance to mutate during spread
│   ├── applyMutationToText() - Text transformation by type
│   ├── calculateSpreadProbability() - Base 50% + multipliers
│   ├── applyReputationEffects() - Faction-based reputation
│   └── 6 query operations (by NPC, category, truth level, stats)
│
├── RumorCatalog.kt (~550 lines)
│   ├── 10 complete rumor templates
│   ├── getAllTemplates() → Map of 10 templates
│   ├── getTemplate(id) → RumorTemplate?
│   └── getTemplatesByCategory() → List<RumorTemplate>
│
└── GossipManagerTest.kt (~900 lines)
    └── 35 comprehensive tests (100% pass rate)
```

## Mutation Progression System

### Truth Levels (Based on Mutation Count)

1. **ACCURATE (0-1 mutations):** Close to original truth
2. **EXAGGERATED (2-3 mutations):** Numbers inflated, details enhanced
3. **DISTORTED (4-5 mutations):** Facts changed, major alterations
4. **MYTHICAL (6+ mutations):** Supernatural, legendary status

### Mutation Types (Applied by Count)

1. **EXAGGERATE (0-1 mutations):** Multiply all numbers by 2
   - Example: "defeated 3 ants" → "defeated 6 ants"
   - Regex: `\\d+` → multiply match by 2

2. **EMBELLISH (2-3 mutations):** Add dramatic details
   - Example: "defeated ants" → "defeated ants (single-handedly!)"
   - Append: " (single-handedly!)"

3. **DISTORT (4-5 mutations):** Change facts
   - Example: "defeated ants" → "defeated beetles"
   - Replace: "ant" → "beetle", "Ant" → "Beetle"

4. **MYTHOLOGIZE (6+ mutations):** Add supernatural elements
   - Example: "defeated beetles" → "slew the legendary beetles"
   - Replace: "defeated" → "slew the legendary"

### Complete Progression Example (rumor_defeated_enemies)

```
ACCURATE (0-1 mutations):
"Jalmar defeated 3 ants near the garden path."

↓ EXAGGERATE (multiply numbers ×2)

EXAGGERATED (2-3 mutations):
"Jalmar defeated 6 ants near the garden path (single-handedly!)"

↓ DISTORT (change facts: ant → beetle)

DISTORTED (4-5 mutations):
"Jalmar defeated 12 beetles near the garden path (single-handedly!)"

↓ MYTHOLOGIZE (add supernatural)

MYTHICAL (6+ mutations):
"Jalmar slew the legendary 24 beetles near the garden path (single-handedly!)"
```

## 10 Rumor Templates

### Heroic Deeds (3 templates)

**1. rumor_defeated_enemies**
- Base: "{playerName} defeated {enemyCount} {enemyType} near {location}."
- Spread: 70%, Mutation: 25%
- Reputation: +10 buttonburgh_citizens
- Example progression shown above

**2. rumor_saved_npc**
- Base: "{playerName} rescued {npcName} from {danger}."
- Mutations: Add "risking life and limb" → "giant spider" → "dragon-sized spider"
- Spread: 80%, Mutation: 20%
- Reputation: +15 buttonburgh_citizens

**3. rumor_champion_duel**
- Base: "{playerName} challenged the arena champion and won!"
- Mutations: "in under 10 seconds" → "legendary champion, 5 seconds" → "immortal champion, single blow"
- Spread: 90%, Mutation: 30%
- Reputation: +20 buttonburgh_citizens

### Crimes (1 template)

**4. rumor_stole_from_npc**
- Base: "{playerName} stole {itemCount} {itemType} from {npcName}."
- Mutations: Double count, add "broad daylight" → "entire inventory" → "entire fortune"
- Spread: 60%, Mutation: 25%
- Reputation: -15 buttonburgh_citizens

### Failures (2 templates)

**5. rumor_fled_combat**
- Base: "{playerName} fled from {enemyCount} {enemyType} near {location}."
- Mutations: Double count, "abandoning quest" → "giant beetles" → "dragon-beetles, abandoning town"
- Spread: 50%, Mutation: 30%
- Reputation: -10 buttonburgh_citizens

**6. rumor_quest_failure**
- Base: "{playerName} failed to deliver {npcName}'s package on time."
- Mutations: "losing half contents" → "entire shipment" → "destroying entire business"
- Spread: 40%, Mutation: 25%
- Reputation: -8 buttonburgh_citizens

### NPC Gossip (1 template)

**7. rumor_npc_romance**
- Base: "{npcName1} and {npcName2} were seen sharing seeds."
- Mutations: "under moonlight" → "planning nest together" → "forbidden romance uniting clans"
- Spread: **90%** (juicy gossip spreads fast!)
- Mutation: **35%** (details get embellished quickly)
- Reputation: None (neutral gossip)

### World Events (2 templates)

**8. rumor_weather_disaster**
- Base: "A rainstorm flooded {location} yesterday."
- Mutations: "massive, washing away nests" → "half of Buttonburgh" → "biblical flood, entire districts"
- Spread: 70%, Mutation: 30%
- Reputation: None

**9. rumor_mysterious_light**
- Base: "Strange lights were seen near {location} last night."
- Mutations: "glowing, moving in patterns" → "spelling ancient runes" → "otherworldly beings, prophecies"
- Spread: 80%, Mutation: 40%
- Reputation: None

### Discoveries (1 template)

**10. rumor_found_treasure**
- Base: "{playerName} found a cache of {itemCount} seeds hidden under a leaf."
- Mutations: Double count, "feed a family" → "legendary cache, months" → "ancient hoard, richest quail alive"
- Spread: 60%, Mutation: 30%
- Reputation: +5 buttonburgh_citizens

## Spread Algorithm

### Base Probability Calculation

```kotlin
fun calculateSpreadProbability(
    sourceNPCId: String,
    targetNPCId: String,
    gameState: GameState
): Double {
    val baseProbability = 0.5  // 50% base chance
    
    // Multipliers (placeholders for future integration):
    val relationshipMultiplier = 1.0  // TODO: NPC Relationship System
    val proximityMultiplier = 1.0     // TODO: LocationManager integration
    val factionMultiplier = 1.0       // TODO: Faction System
    
    val finalProbability = baseProbability * 
        relationshipMultiplier * 
        proximityMultiplier * 
        factionMultiplier
    
    return finalProbability.coerceIn(0.0, 1.0)
}
```

### Spread Flow

```kotlin
// 1. Filter valid targets
val validTargets = targetNPCIds.filter { targetId ->
    !rumor.isKnownBy(targetId) &&  // Doesn't already know
    !state.isSpreadOnCooldown(rumorId, targetId, currentTimestamp)  // Not on cooldown
}

// 2. For each valid target, roll probability
for (targetId in validTargets) {
    val spreadProb = calculateSpreadProbability(sourceNPCId, targetId, gameState)
    
    if (Random.nextDouble() < spreadProb) {
        // 3. Attempt mutation (20% default chance)
        val (mutatedRumor, mutation) = attemptMutation(currentRumor, mutationChance = 0.2)
        
        // 4. Update rumor knowledge
        updatedRumor.knownByNPCs += targetId
        updatedRumor.sourceMap[targetId] = sourceNPCId
        
        // 5. Set cooldown (7200 ticks = 1 day default)
        cooldowns["${rumorId}_${targetId}"] = currentTimestamp + 7200
        
        // 6. Update statistics
        stats.timesSpread++
        stats.totalReach = updatedRumor.knownByNPCs.size
        if (mutation != null) stats.mutationHistory += mutation
    }
}
```

## Data Models

### Rumor

```kotlin
@Serializable
data class Rumor(
    val id: String,                               // "rumor_1731337200_abc123"
    val rumorKey: String,                         // "rumor_defeated_enemies"
    val category: RumorCategory,                  // HEROIC_DEED
    val originalText: String,                     // Original truth
    val currentText: String,                      // Mutated version
    val truthLevel: TruthLevel,                   // ACCURATE → MYTHICAL
    val mutationCount: Int,                       // 0 → infinity
    val subjectId: String,                        // "player_1"
    val originNPCId: String,                      // First NPC who knew
    val knownByNPCs: Set<String> = emptySet(),    // All NPCs who heard it
    val sourceMap: Map<String, String> = emptyMap(), // targetId → sourceId (who told whom)
    val reputationEffects: List<ReputationEffect> = emptyList(),
    val timestamp: Long,                          // Creation tick
    val metadata: Map<String, String> = emptyMap()
) {
    fun calculateTruthLevel(): TruthLevel = when (mutationCount) {
        in 0..1 -> TruthLevel.ACCURATE
        in 2..3 -> TruthLevel.EXAGGERATED
        in 4..5 -> TruthLevel.DISTORTED
        else -> TruthLevel.MYTHICAL
    }
    
    fun isKnownBy(npcId: String): Boolean = npcId in knownByNPCs
    fun getSource(npcId: String): String? = sourceMap[npcId]
}
```

### GossipState

```kotlin
@Serializable
data class GossipState(
    val activeRumors: Map<String, Rumor> = emptyMap(),              // rumorId → Rumor
    val npcGossipMemory: Map<String, List<String>> = emptyMap(),    // npcId → List<rumorId>
    val spreadCooldowns: Map<String, Long> = emptyMap(),            // "rumorId_npcId" → expiry
    val reputationHistory: List<ReputationEffect> = emptyList(),    // All reputation changes
    val rumorStatistics: Map<String, RumorStatistics> = emptyMap()  // rumorId → stats
) {
    fun getRumorsKnownBy(npcId: String): List<Rumor>
    fun npcKnowsRumor(npcId: String, rumorId: String): Boolean
    fun isSpreadOnCooldown(rumorId: String, npcId: String, currentTimestamp: Long): Boolean
    fun getTotalReputationChange(factionId: String): Int
}
```

### RumorStatistics

```kotlin
@Serializable
data class RumorStatistics(
    val rumorId: String,
    val timesSpread: Int = 0,                              // How many times shared
    val totalReach: Int = 0,                               // Total NPCs who heard it
    val mutationHistory: List<RumorMutation> = emptyList(), // All mutations applied
    val averageTruthLevel: Double = 1.0                    // Average (1.0-4.0)
)
```

## Test Coverage (35 tests, 100% pass rate)

### Rumor Creation Tests (5)

- ✅ `startRumor should create new rumor with correct data`
- ✅ `startRumor should add origin NPC to gossip memory`
- ✅ `startRumor should create initial statistics`
- ✅ `startRumor should reject blank rumor key`
- ✅ `startRumor should reject blank subject ID`

### Rumor Spreading Tests (8)

- ✅ `spreadRumor should spread to valid targets`
- ✅ `spreadRumor should reject if source doesn't know rumor`
- ✅ `spreadRumor should reject if rumor not found`
- ✅ `spreadRumor should set cooldowns for recipients`
- ✅ `spreadRumor should respect cooldowns`
- ✅ `spreadRumor should skip NPCs who already know`
- ✅ `spreadRumor should update statistics`
- ✅ `spreadRumor should update NPC memory`

### Mutation Logic Tests (6)

- ✅ `Rumor calculateTruthLevel should return ACCURATE for 0-1 mutations`
- ✅ `Rumor calculateTruthLevel should return EXAGGERATED for 2-3 mutations`
- ✅ `Rumor calculateTruthLevel should return DISTORTED for 4-5 mutations`
- ✅ `Rumor calculateTruthLevel should return MYTHICAL for 6+ mutations`
- ✅ `spreadRumor may apply mutations during spreading`
- ✅ `spreadRumor updates truth level after mutations`

### Gossip State Tests (5)

- ✅ `GossipState getRumorsKnownBy should return correct rumors`
- ✅ `GossipState npcKnowsRumor should return correct result`
- ✅ `GossipState isSpreadOnCooldown should work correctly`
- ✅ `GossipState getTotalReputationChange should sum correctly`
- ✅ `Rumor isKnownBy and getSource should work correctly`

### Query Operation Tests (6)

- ✅ `getRumorsKnownBy should return NPC's rumors`
- ✅ `getRumorsByCategory should filter correctly`
- ✅ `getRumorsByTruthLevel should filter correctly`
- ✅ `getRumorStatistics should return correct statistics`
- ✅ `getMostSpreadRumor should return rumor with highest reach`
- ✅ `getMostMutatedRumor should return rumor with highest count`

### Catalog Tests (5)

- ✅ `RumorCatalog should provide all 10 templates`
- ✅ `RumorCatalog getTemplate should return correct template`
- ✅ `RumorCatalog getTemplate should return null for unknown ID`
- ✅ `RumorCatalog getTemplatesByCategory should filter correctly`
- ✅ `RumorCatalog templates should have mutation paths`

## GameState Integration

```kotlin
@Serializable
data class GameState(
    val version: Int = 1,
    val player: Player,
    // ... other fields
    val gossipState: GossipState = GossipState(),  // ← NEW FIELD
    // ... other fields
) {
    // GameState now serializes/deserializes gossip data
}
```

**Integration Verified:**
- ✅ Compiles successfully with `gossipState` field
- ✅ All 1,083 tests passing (35 gossip + 1,048 existing)
- ✅ No breaking changes to existing systems
- ✅ kotlinx.serialization handles GossipState correctly

## Future Integration Points

### AI Director (Deferred for Coordinator)

```kotlin
// When player defeats enemies:
aiDirector.onPlayerAction("defeated_enemies") { actionData →
    val rumor = GossipManager.startRumor(
        state = gameState.gossipState,
        rumorKey = "rumor_defeated_enemies",
        subjectId = gameState.player.id,
        originNPCId = findNearestNPC(gameState.player.position),
        originalText = "Hero defeated ${actionData.count} ${actionData.enemyType}.",
        category = RumorCategory.HEROIC_DEED,
        currentTimestamp = gameState.worldTime.totalTicks
    )
    
    if (rumor is StartRumorResult.Success) {
        gameState.gossipState = rumor.state
    }
}
```

### Dialogue System (Deferred for Coordinator)

```kotlin
// When talking to NPC:
dialogueManager.addGossipOptions(npcId) { →
    val rumors = GossipManager.getRumorsKnownBy(gameState.gossipState, npcId)
    
    for (rumor in rumors) {
        addChoice("Ask about ${rumor.rumorKey}") {
            npc.say(rumor.currentText)
            
            // Optionally spread to player's companion
            if (gameState.activeCompanionId != null) {
                GossipManager.spreadRumor(
                    state = gameState.gossipState,
                    rumorId = rumor.id,
                    sourceNPCId = npcId,
                    targetNPCIds = listOf(gameState.activeCompanionId!!),
                    gameState = gameState,
                    currentTimestamp = gameState.worldTime.totalTicks
                )
            }
        }
    }
}
```

### NPC Conversations (Automatic Spreading)

```kotlin
// NPCs spread rumors during idle time:
npcManager.updateNPC(npcId, deltaTime) { npc →
    if (npc.isIdleAndNearOtherNPCs) {
        val nearbyNPCs = getNearbyNPCs(npc.position, radius = 5.0)
        val knownRumors = GossipManager.getRumorsKnownBy(gameState.gossipState, npcId)
        
        for (rumor in knownRumors) {
            GossipManager.spreadRumor(
                state = gameState.gossipState,
                rumorId = rumor.id,
                sourceNPCId = npcId,
                targetNPCIds = nearbyNPCs.map { it.id },
                gameState = gameState,
                currentTimestamp = gameState.worldTime.totalTicks,
                cooldownTicks = 7200  // 1 day cooldown
            )
        }
    }
}
```

## Technical Debt & Future Enhancements

### Spread Probability Multipliers (Placeholders)

**Current:** All multipliers = 1.0 (hardcoded)

**Future Integration:**

1. **Relationship Multiplier:**
   - Needs: NPC Relationship System (Phase 5.3)
   - Formula: `relationshipLevel / 100.0` (0.0-1.0 range)
   - Best friends: 1.0× (100%), Enemies: 0.0× (0%)

2. **Proximity Multiplier:**
   - Needs: LocationManager integration
   - Formula: `1.0 / (distance + 1)` (closer = higher)
   - Same location: 1.0×, Adjacent: 0.5×, Far: 0.1×

3. **Faction Multiplier:**
   - Needs: Faction System
   - Formula: `1.5×` if same faction, `0.5×` if rival factions
   - Enables echo chambers and faction-specific rumors

### Reputation Effects (Simplified)

**Current:** Assumes all NPCs in "buttonburgh_citizens" faction

**Future:**
- Check actual faction membership
- Multi-faction reputation (citizen + guard + merchant guilds)
- Compound reputation effects (stealing from merchant affects both merchant guild AND citizens)

### Mutation Text Transformations (Basic)

**Current:** Simple regex replacements (multiply numbers, append text, replace words)

**Future:** Template-driven transformations
- RumorTemplate.mutationPaths currently not fully utilized
- Each MutationPath could have custom textTransform functions
- Enable complex transformations like:
  - Swapping subject/object
  - Changing verb tenses
  - Inserting random adjectives from catalog

## Quail-Scale Authenticity

All rumor templates maintain sincere "tiny hero, big world" tone:

- ✅ "defeated 3 ants" → "slew legendary 24 beetles"
- ✅ "found cache of 5 seeds" → "discovered ancient hoard, richest quail alive"
- ✅ "rainstorm flooded garden path" → "biblical flood destroyed entire districts"
- ✅ "seen sharing seeds" → "forbidden romance uniting clans"

**Re-contextualization Examples:**
- Ant → Beetle → Giant Beetle → Dragon-Beetle
- Seeds → Cache → Legendary Cache → Ancient Hoard
- Puddle → Flood → Massive Flood → Biblical Flood
- Sharing → Romance → Planning Nest → Forbidden Romance

## Community Co-Creation Opportunity

Present gossip system to r/quails and r/JalmarQuest:

**Prompts:**
1. "What rumors would your quails spread?"
2. "What's the most 'quail level stupid' rumor you can imagine?"
3. "Design a rumor template for a quail event you've witnessed"
4. "Share your funniest 'telephone game' experience"

**Expected Community Templates:**
- rumor_dust_bath_party (WORLD_EVENT)
- rumor_seed_hoarder (NPC_GOSSIP)
- rumor_broody_male (NPC_GOSSIP - Easter egg)
- rumor_great_escape (FAILURE - escaping from predator)
- rumor_hatched_chicks (DISCOVERY - new companions)

## Success Criteria (All Met ✅)

- ✅ Telephone game mutation system (4 types, 4 truth levels)
- ✅ Social network tracking (who told whom)
- ✅ Reputation effects for faction members
- ✅ Probability-based spreading with cooldowns
- ✅ 10 diverse rumor templates across all categories
- ✅ Complete mutation progression examples
- ✅ 35+ comprehensive tests (100% pass rate)
- ✅ Integration with GameState
- ✅ Stateless functional design
- ✅ Quail-scale authenticity maintained
- ✅ No breaking changes (1,083 total tests passing)

## Velocity Impact

- **Phases Completed:** 24 (was 23)
- **Days Completed:** 9
- **Average:** 2.7 phases/day (was 2.6)
- **Milestone 7 Progress:** 71% (was 57%) - 5 of 7 phases complete
- **Tests:** 1,083 (was 1,048) - Added 35 gossip tests
- **Code:** ~2,120 lines added across 4 files

**Milestone 7 Remaining:**
- Phase 7.6: Adaptive Difficulty System
- Phase 7.7: Player Behavior Learning

---

**Phase 7.5 COMPLETE ✅**  
**Next:** Continue with Phase 7.6 (Adaptive Difficulty System)
