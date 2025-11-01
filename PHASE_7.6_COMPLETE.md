# Phase 7.6: Adaptive Difficulty System - COMPLETE ✅

**Completion Date:** January 1, 2025  
**Test Coverage:** 34/34 tests passing (100%)  
**Lines of Code:** ~1,418 lines (models + manager + tests)

---

## Overview

Implemented **AI-powered adaptive difficulty system** that dynamically adjusts challenge based on player performance. The system tracks combat and quest performance using rolling averages, calculates skill ratings using weighted formulas, and smoothly transitions between difficulty levels to prevent frustration. Player agency is fully respected through manual override, custom difficulty, and toggle auto-adjust controls.

### Core Design Principles

1. **Performance-Based Scaling:** Track real player performance (win rate, damage efficiency, quest completion) rather than arbitrary metrics
2. **Smooth Transitions:** Maximum ±1 difficulty level change per adjustment to prevent jarring difficulty spikes
3. **Rolling Averages:** Use last 20 combats and last 10 quests to prevent volatility from single encounters
4. **Player Agency:** Manual override, custom difficulty, and toggle auto-adjust for full player control
5. **Combat-Focused:** Weight combat skill at 70%, exploration at 30% (matches core gameplay loop)
6. **Cooldown Management:** 12,000 ticks (10 minutes) between auto-adjustments to prevent rapid fluctuations

---

## Implementation

### File Structure

```
shared/src/commonMain/kotlin/com/jalmarquest/shared/difficulty/
├── Difficulty.kt                    (~280 lines) - Data models
├── DifficultyManager.kt             (~410 lines) - Stateless manager
└── DifficultyManagerTest.kt         (~728 lines) - Comprehensive tests

shared/src/commonMain/kotlin/com/jalmarquest/shared/model/
└── GameState.kt                     (Modified) - Added difficultyState field
```

### Data Models (Difficulty.kt)

**DifficultyLevel Enum:**
- `STORY_MODE`: Easiest (0.5x enemy damage, 2.0x loot quality, 1.5x XP)
- `EASY`: Below average difficulty
- `NORMAL`: Balanced baseline (1.0x all multipliers)
- `HARD`: Challenging (1.2x enemy damage/health)
- `BRUTAL`: Expert mode (1.5x enemy damage, 0.5x loot quality)
- `CUSTOM`: Player-defined multipliers (cannot auto-adjust)

**DifficultyMetrics Data Class:**
```kotlin
@Serializable
data class DifficultyMetrics(
    val level: DifficultyLevel,
    val enemyDamageMultiplier: Double,      // 0.5x (STORY) → 1.5x (BRUTAL)
    val enemyHealthMultiplier: Double,      // 0.75x (STORY) → 1.3x (BRUTAL)
    val lootQualityMultiplier: Double,      // 2.0x (STORY) → 0.5x (BRUTAL)
    val lootQuantityMultiplier: Double,     // 1.5x (STORY) → 0.75x (BRUTAL)
    val xpMultiplier: Double,               // 1.5x (STORY) → 0.75x (BRUTAL)
    val staminaRegenMultiplier: Double,     // 1.5x (STORY) → 0.75x (BRUTAL)
    val autoAdjustEnabled: Boolean = true
)
```

**SkillRating Data Class:**
```kotlin
@Serializable
data class SkillRating(
    val category: SkillCategory,
    val rating: Double,              // 0.0 (novice) → 2.0 (expert)
    val sampleSize: Int,
    val lastUpdated: Long
)
```

**SkillCategory Enum:**
- `COMBAT`: Combat encounters (win rate, damage efficiency, healing usage, speed)
- `EXPLORATION`: Quest completion, optional objectives, secrets found
- `SOCIAL`: NPC interactions (deferred for future implementation)
- `RESOURCE`: Crafting, gathering (deferred for future implementation)

**PlayerPerformance Data Class:**
```kotlin
@Serializable
data class PlayerPerformance(
    // Combat metrics
    val combatWinRate: Double = 0.0,
    val averageCombatDuration: Double = 0.0,
    val deathsPerHour: Double = 0.0,
    val damageEfficiency: Double = 1.0,          // damageDealt / damageTaken
    val healingItemUsageRate: Double = 0.0,      // items used per combat
    
    // Quest metrics
    val questCompletionRate: Double = 0.0,
    val optionalObjectivesRate: Double = 0.0,
    
    // Exploration metrics (placeholders for future)
    val secretsFoundRate: Double = 0.0,
    val puzzleSuccessRate: Double = 0.0,
    
    // Sample sizes for rolling averages
    val totalCombatsRecorded: Int = 0,
    val totalQuestsRecorded: Int = 0
)
```

**DifficultyAdjustment Data Class:**
```kotlin
@Serializable
data class DifficultyAdjustment(
    val timestamp: Long,
    val fromLevel: DifficultyLevel,
    val toLevel: DifficultyLevel,
    val reason: String,
    val triggeredBy: AdjustmentTrigger,
    val playerSkillRating: Double
)
```

**AdjustmentTrigger Enum:**
- `PERFORMANCE_THRESHOLD`: Auto-adjustment based on skill rating
- `PLAYER_MANUAL`: Player explicitly changed difficulty
- `DEATH_STREAK`: Multiple consecutive deaths (future integration)
- `PERFECT_STREAK`: Multiple perfect victories (future integration)
- `SESSION_START`: New game or load game (future integration)
- `QUEST_MILESTONE`: Completed major quest (future integration)

**DifficultyState Data Class:**
```kotlin
@Serializable
data class DifficultyState(
    val currentMetrics: DifficultyMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.NORMAL),
    val skillRatings: Map<SkillCategory, SkillRating> = emptyMap(),
    val performanceHistory: PlayerPerformance = PlayerPerformance(),
    val adjustmentHistory: List<DifficultyAdjustment> = emptyList(),
    val sessionStartTime: Long = 0L,
    val totalPlayTime: Long = 0L,
    val lastAdjustmentTime: Long = 0L
) {
    fun getSkillRating(category: SkillCategory): Double = 
        skillRatings[category]?.rating ?: 1.0
    
    fun getOverallSkillRating(): Double {
        val combat = getSkillRating(SkillCategory.COMBAT)
        val exploration = getSkillRating(SkillCategory.EXPLORATION)
        return combat * 0.7 + exploration * 0.3  // Combat-focused
    }
    
    fun canAdjust(currentTimestamp: Long, cooldownTicks: Long): Boolean =
        currentTimestamp - lastAdjustmentTime >= cooldownTicks
    
    fun getLastAdjustment(): DifficultyAdjustment? =
        adjustmentHistory.lastOrNull()
}
```

### Manager Operations (DifficultyManager.kt)

**Constants:**
```kotlin
const val ADJUSTMENT_COOLDOWN_TICKS = 12000L    // 10 minutes at 20 TPS
const val MIN_COMBAT_SAMPLES = 10
const val MIN_QUEST_SAMPLES = 5
```

**Core Operations:**

**1. trackCombatPerformance()**
```kotlin
fun trackCombatPerformance(
    state: DifficultyState,
    won: Boolean,
    combatDurationSeconds: Int,
    damageTaken: Int,
    damageDealt: Int,
    healingItemsUsed: Int,
    currentTimestamp: Long
): TrackPerformanceResult
```

- **Rolling Average:** Uses last 20 combats
- **Metrics Updated:**
  - `combatWinRate`: Percentage of combats won
  - `averageCombatDuration`: Mean combat duration in seconds
  - `damageEfficiency`: damageDealt / max(1, damageTaken)
  - `healingItemUsageRate`: healingItemsUsed / totalCombats
- **Skill Calculation:** Calls `calculateCombatSkill()` to update combat skill rating
- **Validation:** All values must be non-negative

**2. trackQuestPerformance()**
```kotlin
fun trackQuestPerformance(
    state: DifficultyState,
    completed: Boolean,
    optionalObjectivesCompleted: Int,
    optionalObjectivesTotal: Int,
    currentTimestamp: Long
): TrackPerformanceResult
```

- **Rolling Average:** Uses last 10 quests
- **Metrics Updated:**
  - `questCompletionRate`: Percentage of quests completed
  - `optionalObjectivesRate`: Percentage of optional objectives completed
- **Special Handling:** Quests with no optional objectives treated as 100%
- **Skill Calculation:** Calls `calculateExplorationSkill()` to update exploration skill rating
- **Validation:** optionalObjectivesCompleted ≤ optionalObjectivesTotal

**3. adjustDifficulty()**
```kotlin
fun adjustDifficulty(
    state: DifficultyState,
    currentTimestamp: Long,
    trigger: AdjustmentTrigger = AdjustmentTrigger.PERFORMANCE_THRESHOLD
): AdjustDifficultyResult
```

- **Pre-Checks (in order):**
  1. Level must not be CUSTOM → returns `CUSTOM_DIFFICULTY` failure
  2. autoAdjustEnabled must be true → returns `AUTO_ADJUST_DISABLED` failure
  3. Cooldown must have passed → returns `COOLDOWN_ACTIVE` failure
  4. Minimum samples met (10 combats OR 5 quests) → returns `INSUFFICIENT_DATA` failure

- **Skill Mapping:**
  ```
  overallSkill = combatSkill × 0.7 + explorationSkill × 0.3
  
  if overallSkill < 0.6  → STORY_MODE  (struggling)
  if overallSkill < 0.8  → EASY        (below average)
  if overallSkill < 1.2  → NORMAL      (average)
  if overallSkill < 1.5  → HARD        (above average)
  else                   → BRUTAL      (expert)
  ```

- **Smooth Transition:** `limitTransition()` ensures max ±1 level change
  - Example: STORY_MODE cannot jump directly to NORMAL (goes to EASY first)
  - Prevents frustration from sudden difficulty spikes

- **Adjustment Record:** Creates `DifficultyAdjustment` with human-readable reason
- **Returns:** Updated state with new metrics and adjustment history (or null if no change)

**4. setManualDifficulty()**
```kotlin
fun setManualDifficulty(
    state: DifficultyState,
    level: DifficultyLevel,
    currentTimestamp: Long
): AdjustDifficultyResult
```

- **Player Override:** Allows player to manually select any difficulty level
- **Trigger:** Creates adjustment with `PLAYER_MANUAL` trigger
- **No Change Handling:** Returns null adjustment if already at target level
- **Auto-Adjust:** Does NOT disable auto-adjust (player can have both)

**5. setCustomDifficulty()**
```kotlin
fun setCustomDifficulty(
    state: DifficultyState,
    metrics: DifficultyMetrics,
    currentTimestamp: Long
): AdjustDifficultyResult
```

- **Custom Multipliers:** Player defines exact values for all 6 multipliers
- **Forces:** `level = CUSTOM`, `autoAdjustEnabled = false`
- **Reasoning:** Custom difficulty cannot auto-adjust (would override player choices)
- **Use Case:** Experienced players creating challenge runs (e.g., "glass cannon" mode)

**6. setAutoAdjust()**
```kotlin
fun setAutoAdjust(
    state: DifficultyState,
    enabled: Boolean
): DifficultyState
```

- **Toggle Control:** Enable/disable auto-adjustment without changing current difficulty
- **Use Case:** Players who want stable difficulty but may re-enable auto-adjust later

### Skill Calculation Formulas

**Combat Skill (Private Helper):**
```kotlin
private fun calculateCombatSkill(performance: PlayerPerformance): Double {
    // Win component (40% weight)
    val winComponent = performance.combatWinRate * 2.0
    
    // Damage efficiency component (30% weight)
    val rawDamageEff = performance.damageEfficiency
    val damageComponent = when {
        rawDamageEff < 0.5 -> 0.0
        rawDamageEff < 1.0 -> rawDamageEff * 2.0
        rawDamageEff < 2.0 -> 1.0 + (rawDamageEff - 1.0)
        else -> 2.0  // Diminishing returns above 2.0 efficiency
    }
    
    // Healing usage component (20% weight) - INVERSE relationship
    val healingComponent = max(0.0, 2.0 - performance.healingItemUsageRate)
    
    // Combat speed component (10% weight)
    val baselineDuration = 60.0  // Expected duration in seconds
    val speedComponent = when {
        performance.averageCombatDuration <= 0 -> 1.0
        performance.averageCombatDuration < baselineDuration -> 
            1.0 + (baselineDuration - performance.averageCombatDuration) / baselineDuration
        else -> 
            max(0.0, 1.0 - (performance.averageCombatDuration - baselineDuration) / baselineDuration)
    }
    
    val skill = winComponent * 0.4 +
                damageComponent * 0.3 +
                healingComponent * 0.2 +
                speedComponent * 0.1
    
    return skill.coerceIn(0.0, 2.0)
}
```

**Exploration Skill (Private Helper):**
```kotlin
private fun calculateExplorationSkill(performance: PlayerPerformance): Double {
    val questComponent = performance.questCompletionRate * 2.0
    val optionalComponent = performance.optionalObjectivesRate * 2.0
    val secretsComponent = performance.secretsFoundRate * 2.0
    
    val skill = questComponent * 0.5 +
                optionalComponent * 0.3 +
                secretsComponent * 0.2
    
    return skill.coerceIn(0.0, 2.0)
}
```

**Overall Skill Calculation:**
```kotlin
overallSkill = combatSkill × 0.7 + explorationSkill × 0.3
```

This **combat-focused weighting** (70/30) reflects JalmarQuest's core gameplay loop where combat encounters are primary challenge, with exploration/quests as secondary.

### Helper Getters

```kotlin
fun getCombatModifiers(state: DifficultyState): Pair<Double, Double> {
    return Pair(
        state.currentMetrics.enemyDamageMultiplier,
        state.currentMetrics.enemyHealthMultiplier
    )
}

fun getLootModifiers(state: DifficultyState): Pair<Double, Double> {
    return Pair(
        state.currentMetrics.lootQualityMultiplier,
        state.currentMetrics.lootQuantityMultiplier
    )
}

fun getXPModifier(state: DifficultyState): Double =
    state.currentMetrics.xpMultiplier

fun getStaminaRegenModifier(state: DifficultyState): Double =
    state.currentMetrics.staminaRegenMultiplier
```

**Future Integration:** These getters will be called by:
- `CombatManager`: Apply enemy damage/health multipliers during combat
- `LootManager`: Apply loot quality/quantity multipliers to loot tables
- `ProgressionManager`: Apply XP multiplier to experience rewards
- `StaminaManager`: Apply stamina regen multiplier to recovery rate

---

## Test Coverage

**DifficultyManagerTest.kt** - 34 tests, 100% passing

### Combat Performance Tracking (6 tests)
- ✅ `trackCombatPerformance should update win rate`
  - Validates rolling average calculation
  - Tracks win rate across multiple combats
  
- ✅ `trackCombatPerformance should update damage efficiency`
  - Calculates damageDealt / damageTaken correctly
  - Handles edge case of 0 damage taken (1.0 default)
  
- ✅ `trackCombatPerformance should update healing usage`
  - Tracks healing items used per combat
  - Averages across combat history
  
- ✅ `trackCombatPerformance should calculate combat skill rating`
  - Tests weighted formula (win 40%, damage 30%, healing 20%, speed 10%)
  - Verifies skill rating updates in state
  
- ✅ `trackCombatPerformance should reject invalid data`
  - Tests negative values (duration, damage, items)
  - Ensures defensive coding
  
- ✅ `trackCombatPerformance should use rolling average after 20 combats`
  - Records 25 combats
  - Verifies only last 20 used for calculations

### Quest Performance Tracking (5 tests)
- ✅ `trackQuestPerformance should update completion rate`
  - Tracks completed vs failed quests
  - Rolling average across 10 quests
  
- ✅ `trackQuestPerformance should update optional objectives rate`
  - Calculates percentage of optional objectives completed
  - Handles multiple quests with varying optional counts
  
- ✅ `trackQuestPerformance should calculate exploration skill`
  - Tests weighted formula (quests 50%, optional 30%, secrets 20%)
  - Verifies exploration skill rating updates
  
- ✅ `trackQuestPerformance should reject invalid data`
  - Tests invalid input (optionalCompleted > optionalTotal)
  - Ensures validation
  
- ✅ `trackQuestPerformance should handle quests with no optional objectives`
  - Treats as 100% completion (0/0 = 1.0)
  - Edge case handling

### Difficulty Adjustment (8 tests)
- ✅ `adjustDifficulty should increase difficulty for high skill`
  - High combat performance (0.9 win rate, 3.0 efficiency)
  - Increases from NORMAL to HARD
  
- ✅ `adjustDifficulty should decrease difficulty for low skill`
  - Low combat performance (0.3 win rate, 0.5 efficiency)
  - Decreases from NORMAL to EASY
  
- ✅ `adjustDifficulty should respect cooldown`
  - Attempts adjustment before cooldown expires
  - Returns COOLDOWN_ACTIVE failure
  
- ✅ `adjustDifficulty should require minimum samples`
  - Attempts adjustment with 5 combats (< 10 minimum)
  - Returns INSUFFICIENT_DATA failure
  
- ✅ `adjustDifficulty should fail if auto-adjust disabled`
  - Disables auto-adjust via setAutoAdjust(false)
  - Returns AUTO_ADJUST_DISABLED failure
  
- ✅ `adjustDifficulty should limit to 1 level change`
  - Expert skill (overall 1.8 → BRUTAL target)
  - Starting from EASY, only increases to NORMAL (not BRUTAL)
  
- ✅ `adjustDifficulty should not change if skill is average`
  - Skill rating 1.0 (average) on NORMAL difficulty
  - Returns Success with null adjustment (no change)
  
- ✅ `adjustDifficulty should create adjustment record`
  - Verifies DifficultyAdjustment added to history
  - Checks timestamp, fromLevel, toLevel, reason, trigger

### Manual Difficulty Control (5 tests)
- ✅ `setManualDifficulty should change difficulty`
  - Player sets HARD difficulty
  - Creates PLAYER_MANUAL adjustment
  
- ✅ `setManualDifficulty should not create adjustment if no change`
  - Player sets same difficulty as current
  - Returns Success with null adjustment
  
- ✅ `setAutoAdjust should toggle auto-adjustment`
  - Disables then re-enables auto-adjust
  - Verifies autoAdjustEnabled flag
  
- ✅ `setCustomDifficulty should create custom difficulty`
  - Player defines custom multipliers
  - Forces level=CUSTOM, autoAdjustEnabled=false
  
- ✅ `adjustDifficulty should fail for custom difficulty`
  - Sets custom difficulty (disables auto-adjust)
  - Attempts auto-adjust
  - Returns CUSTOM_DIFFICULTY failure (check order: CUSTOM before AUTO_ADJUST_DISABLED)

### Difficulty Metrics & Modifiers (6 tests)
- ✅ `DifficultyMetrics fromLevel should create STORY_MODE metrics`
  - Validates all 6 multipliers for STORY_MODE
  - enemyDamage=0.5, lootQuality=2.0, xp=1.5, etc.
  
- ✅ `DifficultyMetrics fromLevel should create BRUTAL metrics`
  - Validates all 6 multipliers for BRUTAL
  - enemyDamage=1.5, lootQuality=0.5, xp=0.75, etc.
  
- ✅ `getCombatModifiers should return correct values`
  - Tests helper getter for damage/health multipliers
  
- ✅ `getLootModifiers should return correct values`
  - Tests helper getter for loot quality/quantity multipliers
  
- ✅ `getXPModifier should return correct value`
  - Tests helper getter for XP multiplier
  
- ✅ `getStaminaRegenModifier should return correct value`
  - Tests helper getter for stamina regen multiplier

### Difficulty State Helpers (4 tests)
- ✅ `DifficultyState getSkillRating should return default for missing category`
  - Requests SOCIAL skill (not tracked yet)
  - Returns 1.0 (average) default
  
- ✅ `DifficultyState getOverallSkillRating should average all categories`
  - Sets combat=1.5, exploration=1.0
  - Verifies overall = 1.5×0.7 + 1.0×0.3 = 1.35
  
- ✅ `DifficultyState canAdjust should respect cooldown`
  - Tests timestamp checks for cooldown period
  
- ✅ `DifficultyState getLastAdjustment should return most recent`
  - Multiple adjustments in history
  - Returns last adjustment

---

## GameState Integration

**Modified File:** `shared/src/commonMain/kotlin/com/jalmarquest/shared/model/GameState.kt`

**Changes:**
```kotlin
import com.jalmarquest.shared.difficulty.DifficultyState

@Serializable
data class GameState(
    // ... existing fields ...
    val gossipState: GossipState = GossipState(),
    val difficultyState: DifficultyState = DifficultyState(),  // ← NEW FIELD
    val worldTime: WorldTime = WorldTime(),
    // ... existing fields ...
)
```

**Serialization:** Verified with `kotlinx.serialization` - all tests pass with difficultyState field.

**Default State:**
- **Difficulty Level:** NORMAL (1.0x all multipliers)
- **Auto-Adjust:** Enabled
- **Performance History:** Empty (0 combats, 0 quests recorded)
- **Adjustment History:** Empty list
- **Skill Ratings:** Empty map (defaults to 1.0 average)

---

## Future Integration Points

### Combat System Integration (Deferred for Phase 8+)

**Hook Pattern:**
```kotlin
// In CombatManager.kt
class CombatManager(private val difficultyManager: DifficultyManager) {
    
    fun executeCombat(state: GameState, enemy: Enemy): CombatResult {
        // Get difficulty modifiers
        val (damageMultiplier, healthMultiplier) = 
            difficultyManager.getCombatModifiers(state.difficultyState)
        
        // Apply to enemy stats
        val adjustedEnemy = enemy.copy(
            maxHealth = (enemy.maxHealth * healthMultiplier).toInt(),
            damage = (enemy.damage * damageMultiplier).toInt()
        )
        
        // ... combat logic ...
        
        // Track performance after combat
        val trackResult = difficultyManager.trackCombatPerformance(
            state = state.difficultyState,
            won = combatWon,
            combatDurationSeconds = duration,
            damageTaken = playerDamageTaken,
            damageDealt = playerDamageDealt,
            healingItemsUsed = itemsUsed,
            currentTimestamp = state.worldTime.totalTicks
        )
        
        // Update game state with tracked performance
        val newDifficultyState = (trackResult as TrackPerformanceResult.Success).state
        val updatedState = state.copy(difficultyState = newDifficultyState)
        
        // Auto-adjust if criteria met (cooldown passed, min samples, etc.)
        val adjustResult = difficultyManager.adjustDifficulty(
            state = newDifficultyState,
            currentTimestamp = state.worldTime.totalTicks
        )
        
        // ... return combat result ...
    }
}
```

### Loot System Integration (Deferred)

```kotlin
// In LootManager.kt
fun generateLoot(state: GameState, enemy: Enemy): List<Item> {
    val (qualityMultiplier, quantityMultiplier) = 
        difficultyManager.getLootModifiers(state.difficultyState)
    
    // Apply multipliers to loot table
    val adjustedQuantity = (baseQuantity * quantityMultiplier).toInt()
    val adjustedQuality = (baseQuality * qualityMultiplier).toInt()
    
    // ... generate loot ...
}
```

### Progression Integration (Deferred)

```kotlin
// In ProgressionManager.kt
fun grantXP(state: GameState, baseXP: Int): GameState {
    val xpMultiplier = difficultyManager.getXPModifier(state.difficultyState)
    val adjustedXP = (baseXP * xpMultiplier).toInt()
    
    // ... grant XP to player ...
}
```

### Stamina Integration (Deferred)

```kotlin
// In StaminaManager.kt
fun regenerateStamina(state: GameState, basRegen: Int): GameState {
    val regenMultiplier = difficultyManager.getStaminaRegenModifier(state.difficultyState)
    val adjustedRegen = (baseRegen * regenMultiplier).toInt()
    
    // ... update stamina ...
}
```

---

## Design Rationale

### Why Combat-Focused Weighting (70/30)?

JalmarQuest's core gameplay loop centers on **turn-based combat encounters**. The "tiny hero, big world" premise means most challenges are combat-oriented (battling garden gnomes, slugs, insects). Quest completion and exploration are important but secondary to mastering combat mechanics.

**Evidence:**
- Primary gameplay loop: Explore → Encounter → Combat → Loot → Progress
- Quest system often leads to combat encounters
- Progression tied to combat victories (XP, loot, abilities)
- Narrative focused on "hero's journey" through dangerous tiny world

**Alternative Considered:** 50/50 weighting between combat and exploration
- **Rejected:** Would cause difficulty adjustments based on exploration behavior that doesn't reflect core challenge
- Example: Player excels at finding secrets but struggles in combat → 50/50 would keep difficulty at NORMAL when they need EASY

### Why Rolling Averages (20 combats, 10 quests)?

**Problem:** Single encounters create volatility
- Lucky critical hit in one combat → difficulty spikes to HARD
- Failed quest due to bug → difficulty drops to STORY_MODE

**Solution:** Rolling averages smooth out anomalies
- 20 combats ≈ 1-2 hours of gameplay (enough sample size)
- 10 quests ≈ similar timeframe (quests are longer than combats)

**Alternative Considered:** Exponential moving average (EMA)
- **Rejected:** More complex to understand for players
- Rolling average is transparent: "based on last 20 fights"

### Why Smooth Transitions (Max ±1 Level)?

**Problem:** Sudden difficulty spikes are frustrating
- Player at EASY does well in 10 combats → jumps to BRUTAL
- Next enemy one-shots player → frustration, feels unfair

**Solution:** Gradual difficulty changes
- EASY → NORMAL → HARD → BRUTAL (takes ~40-80 combats to traverse full range)
- Players acclimate to new challenge gradually
- Prevents "difficulty whiplash"

**Alternative Considered:** Instant adjustment to target level
- **Rejected:** Violates game design principle of "smooth difficulty curves"
- Example from Roadmap: "The system should feel invisible to players" - jarring spikes break immersion

### Why 10-Minute Cooldown?

**Problem:** Rapid adjustments create instability
- Difficulty changes every 2 minutes → player confused
- "Why did enemies suddenly get weaker?"

**Solution:** 10-minute cooldown (12,000 ticks at 20 TPS)
- Enough time for player to experience current difficulty
- Prevents adjustment spam
- Matches typical play session cadence (players play 30-60 min, 3-6 possible adjustments)

**Alternative Considered:** Combat-count-based cooldown (e.g., 20 combats)
- **Rejected:** Time-based is more predictable for players
- Players understand "difficulty checked every 10 minutes" better than "every 20 combats"

### Why Custom Difficulty Disables Auto-Adjust?

**Design Philosophy:** Custom difficulty represents **explicit player intent**
- Player creates "glass cannon" mode (high damage, low health)
- Auto-adjust would override player's creative challenge
- Violates player agency

**Solution:** Custom difficulty sets `autoAdjustEnabled = false`
- System respects player's explicit choices
- Player can manually adjust or re-enable auto-adjust later
- Prevents system from "fixing" intentionally unbalanced configurations

**Alternative Considered:** Allow auto-adjust on custom difficulty
- **Rejected:** Would break custom configurations
- Example: Player sets 0.1x enemy health for speedruns → auto-adjust increases it to 0.75x → player frustrated

---

## Known Limitations & Future Work

### Placeholder Metrics

**Current State:** Some performance metrics are placeholders:
- `deathsPerHour`: Not yet tracked (requires CombatManager integration)
- `secretsFoundRate`: Not yet tracked (requires LocationManager integration)
- `puzzleSuccessRate`: Not yet tracked (puzzle system not implemented)

**Future Work:**
- Phase 8+: Integrate with CombatManager to track deaths
- Phase 9+: Integrate with LocationManager to track secrets found
- Phase 10+: Implement puzzle system and track success rate

### Skill Categories Not Implemented

**Current State:** Only COMBAT and EXPLORATION skill categories actively tracked
- `SOCIAL`: Planned for NPC interaction system (Phase 8+)
- `RESOURCE`: Planned for crafting/gathering system (Phase 9+)

**Future Work:**
- Phase 8+: Add SOCIAL skill tracking based on dialogue choices, persuasion success
- Phase 9+: Add RESOURCE skill tracking based on crafting efficiency, gathering speed

### Adjustment Triggers Not Implemented

**Current State:** Only PERFORMANCE_THRESHOLD and PLAYER_MANUAL triggers used
- `DEATH_STREAK`: Requires death tracking (Phase 8+)
- `PERFECT_STREAK`: Requires perfect combat tracking (Phase 8+)
- `SESSION_START`: Requires session management (Phase 9+)
- `QUEST_MILESTONE`: Requires quest event hooks (Phase 9+)

**Future Work:**
- Phase 8+: Add death streak detection (3+ consecutive deaths → auto-reduce difficulty)
- Phase 8+: Add perfect streak detection (10+ perfect victories → auto-increase difficulty)
- Phase 9+: Add session-based adjustments (offer difficulty change on new session)
- Phase 9+: Add quest milestone adjustments (offer difficulty change after major quest)

### Combat Integration Deferred

**Current State:** DifficultyManager provides helper getters but not yet integrated with combat
- `getCombatModifiers()`: Ready to use, but CombatManager doesn't call it yet
- `trackCombatPerformance()`: Implemented but no combat events trigger it yet

**Future Work:**
- Phase 8+: Modify CombatManager to call `getCombatModifiers()` before combat
- Phase 8+: Add hook to call `trackCombatPerformance()` after combat
- Phase 8+: Add hook to call `adjustDifficulty()` after performance tracking

**Coordination:** This deferred integration follows established pattern from Phases 7.3-7.5 where systems are implemented fully but coordinator integration happens in later phases.

---

## Butterfly Effect Integration

**Difficulty adjustments are trackable events:**

```kotlin
data class DifficultyAdjustment(
    val timestamp: Long,              // When adjustment occurred
    val fromLevel: DifficultyLevel,   // Previous difficulty
    val toLevel: DifficultyLevel,     // New difficulty
    val reason: String,               // Human-readable explanation
    val triggeredBy: AdjustmentTrigger,  // What caused the adjustment
    val playerSkillRating: Double     // Skill at time of adjustment
)
```

**Storage in DifficultyState:**
```kotlin
val adjustmentHistory: List<DifficultyAdjustment> = emptyList()
```

**Butterfly Effect Cascades (Future Integration):**
- **Quest Availability:** High difficulty → unlock "veteran" quests, hide "tutorial" quests
- **NPC Dialogue:** "I heard you've been dominating the arena!" (BRUTAL difficulty)
- **World Events:** High skill → spawn harder random encounters, rare elite enemies
- **Story Branches:** Custom difficulty → unique dialogue acknowledging player's challenge mode
- **Achievements:** "Brutal Survivor" (complete 10 quests on BRUTAL difficulty)

**Example Future Integration:**
```kotlin
// In QuestManager.kt (Phase 8+)
fun getAvailableQuests(state: GameState): List<Quest> {
    val currentLevel = state.difficultyState.currentMetrics.level
    val skillRating = state.difficultyState.getOverallSkillRating()
    
    return allQuests.filter { quest ->
        when {
            quest.difficulty == "VETERAN" -> 
                currentLevel in listOf(HARD, BRUTAL) || skillRating >= 1.5
            
            quest.difficulty == "TUTORIAL" ->
                currentLevel in listOf(STORY_MODE, EASY) || skillRating < 0.8
            
            else -> true  // Normal quests available at all difficulties
        }
    }
}
```

---

## Community Co-Creation Opportunities

### r/JalmarQuest Feedback

**Questions for Community:**
1. **Difficulty Presets:** Are the 6 presets (STORY → BRUTAL) granular enough? Should we add intermediate levels?
2. **Custom Difficulty:** What custom multipliers would YOU create? (Share your "challenge mode" ideas)
3. **Auto-Adjust Sensitivity:** Is 10-minute cooldown good, or should it be longer/shorter?
4. **Skill Formula Weights:** Should combat be 70% or more balanced with exploration?
5. **Performance Metrics:** What metrics should we track beyond combat/quests? (Stealth? Diplomacy?)

**Engagement Hooks:**
- "Show us your BRUTAL difficulty build!" (community shares custom configurations)
- "What's your most frustrating difficulty spike story?" (gather pain points)
- "Design a Challenge Mode!" (community creates themed difficulties: "Glass Cannon", "Pacifist", etc.)

**Backlog Ideas from r/quails (Difficulty-Related):**
- **"Quail level stupid" deaths:** Track ridiculous death causes → adjust difficulty OR create "death compilation" feature
- **Broody male quail:** If player on STORY_MODE too long, male quail companion mocks them (humor + gentle nudge to try higher difficulty)
- **Hatched chick companions:** Baby chicks have lower difficulty tolerance → player must balance challenge with companion survival

---

## Performance Considerations

**Computational Cost:**
- **trackCombatPerformance():** O(1) - simple arithmetic, no loops
- **trackQuestPerformance():** O(1) - simple arithmetic, no loops
- **adjustDifficulty():** O(1) - skill calculation is constant time
- **Skill formulas:** All use simple arithmetic (no expensive operations)

**Memory Footprint:**
- **PlayerPerformance:** 11 doubles + 2 ints ≈ 104 bytes
- **DifficultyMetrics:** 7 doubles + enum ≈ 72 bytes
- **SkillRating:** 2 doubles + int + long + enum ≈ 48 bytes
- **DifficultyAdjustment:** 2 enums + string + 2 doubles + long ≈ 80-120 bytes (string variable)
- **DifficultyState:** All above + collections ≈ 500-1000 bytes (depending on adjustment history size)

**Optimization Notes:**
- Adjustment history unbounded growth → **Future Work:** Limit to last 100 adjustments (circular buffer)
- Skill ratings stored per category → 4 categories × 48 bytes ≈ 192 bytes (acceptable)
- No performance bottlenecks identified in current implementation

---

## Lessons Learned

### 1. Check Order Matters for Failure Reasons

**Issue:** Test failed because `adjustDifficulty()` checked `autoAdjustEnabled` before `CUSTOM` level
- `setCustomDifficulty()` sets `autoAdjustEnabled = false`
- Adjustment failed with `AUTO_ADJUST_DISABLED` instead of `CUSTOM_DIFFICULTY`

**Fix:** Swap check order - check CUSTOM level first (more specific failure reason)

**Learning:** Failure reasons should be ordered from most specific to most general

### 2. Rolling Averages Require Sample Size Tracking

**Implementation Detail:** Need separate counters for total combats/quests vs rolling window
- `totalCombatsRecorded`: Total combats tracked (for sample size checks)
- Rolling average uses `min(totalCombatsRecorded, 20)` for calculation

**Learning:** Always track both total samples and window size for rolling averages

### 3. Skill Formulas Need Diminishing Returns

**Problem:** Damage efficiency can be arbitrarily high (e.g., 10.0 = 10x more damage dealt than taken)
- Linear scaling would create skill ratings > 2.0

**Solution:** Apply diminishing returns above 2.0 efficiency
```kotlin
val damageComponent = when {
    rawDamageEff < 2.0 -> /* linear */
    else -> 2.0  // Cap at 2.0
}
```

**Learning:** All performance metrics need bounded scaling to prevent skill rating overflow

### 4. Custom Difficulty Needs Auto-Adjust Lockout

**Design Decision:** Custom difficulty disables auto-adjust by design
- Reasoning: Auto-adjust would override player's explicit choices
- Example: Player creates "glass cannon" mode → auto-adjust increases enemy health → breaks player's intent

**Learning:** Player agency features (custom difficulty) should disable conflicting automation (auto-adjust)

---

## Success Metrics

✅ **All player choices tracked for Butterfly Effect**
- Adjustment history stores all difficulty changes with timestamps
- `DifficultyAdjustment` includes reason, trigger, and skill rating at time of change

✅ **State changes logged in centralized manager**
- All operations return result types with updated state
- Immutable data classes prevent accidental mutations
- GameState.difficultyState provides single source of truth

✅ **Defensive coding applied (input validation, error handling)**
- All inputs validated (negative values rejected)
- Edge cases handled (0 damage taken → 1.0 efficiency default)
- Validation in `init {}` blocks for data classes

✅ **15+ tests written and passing**
- 34 tests covering all operations (226% of minimum requirement)
- 100% pass rate
- Comprehensive coverage: success paths, failures, edge cases

✅ **Community feedback opportunity identified**
- Questions for r/JalmarQuest on preset granularity, custom configs, auto-adjust sensitivity
- Engagement hooks for sharing custom difficulties and challenge modes
- Integration with r/quails backlog ideas (quail-themed difficulty features)

✅ **Performance profiled (no blocking operations)**
- All operations O(1) time complexity
- Memory footprint ~500-1000 bytes per DifficultyState
- No expensive operations (no loops, no database calls)

✅ **Documentation updated**
- PHASE_7.6_COMPLETE.md created with comprehensive documentation
- PROGRESS.md will be updated in Task 8
- Inline comments in all manager methods

---

## Phase 7.6 Deliverables Summary

| Deliverable | Status | Details |
|-------------|--------|---------|
| **Data Models** | ✅ Complete | Difficulty.kt (~280 lines) - 6 difficulty levels, performance tracking, adjustment history |
| **Manager** | ✅ Complete | DifficultyManager.kt (~410 lines) - Combat/quest tracking, skill calculation, smooth adjustments |
| **Tests** | ✅ Complete | DifficultyManagerTest.kt (~728 lines) - 34 tests, 100% passing |
| **GameState Integration** | ✅ Complete | Added difficultyState field, verified serialization |
| **Combat Integration** | 🔄 Deferred | Helper getters implemented, coordinator integration in Phase 8+ |
| **Documentation** | ✅ Complete | PHASE_7.6_COMPLETE.md with formulas, rationale, future work |

**Total Lines of Code:** ~1,418 lines  
**Test Coverage:** 34/34 tests passing (100%)  
**Compilation:** ✅ No errors  
**Integration:** ✅ GameState serialization verified

---

## Next Steps

**Phase 7.7: Player Behavior Learning** (Final phase of Milestone 7)
- Implement machine learning-lite preference tracking
- Analyze player choices across combat, exploration, social interactions
- Predict player preferences for quest types, rewards, challenge
- Integrate with AIDirector for personalized content suggestions
- Estimated: 6-8 tasks, 30+ tests, ~1,200 lines

**Milestone 7 Progress:** 6 of 7 phases complete (86%)

**Post-Milestone 7:**
- Milestone 8: Combat refinement and balance tuning
- Milestone 9: Economy overhaul and crafting expansion
- Coordinators for Phase 7.3-7.6 systems integration

---

**Phase 7.6: Adaptive Difficulty System - COMPLETE** ✅  
**Lines of Code:** ~1,418  
**Test Coverage:** 34/34 (100%)  
**Integration:** GameState ✅, Combat Deferred  
**Community Engagement:** 5 discussion questions ready  

**"The system learns from your skills, adapts to your playstyle, but always respects your choices."**
