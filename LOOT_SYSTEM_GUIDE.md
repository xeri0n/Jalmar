# Loot System Implementation Guide

## Overview

The loot system in JalmarQuest enables:
1. **Enemy loot drops** - Defeated enemies drop items based on their loot tables
2. **World item pickup** - Players can pick up items from the ground
3. **Combat rewards** - XP and loot are calculated and distributed after combat

## Components

### 1. Combat Rewards (`CombatRewards` in CombatState.kt)

Data class that encapsulates rewards from a combat encounter:

```kotlin
@Serializable
data class CombatRewards(
    val xpGained: Int,
    val itemsLooted: List<Pair<String, Int>>, // (itemId, quantity)
    val defeatedEnemies: List<String>
)
```

**Features:**
- Validates non-negative XP
- Ensures positive item quantities
- Provides formatted summary via `summary()` method

### 2. Loot Generation (`CombatManager.generateCombatRewards()`)

Processes all defeated enemies and generates rewards:

```kotlin
fun generateCombatRewards(defeatedEnemies: List<EnemyCombatData>): CombatRewards
```

**How it works:**
1. Iterates through each defeated enemy
2. Looks up enemy in `EnemyCatalog` by `catalogId`
3. Adds XP from `enemy.xpReward`
4. Generates loot using `LootSystem.generateLoot(enemy.lootTable)`
5. Consolidates duplicate items (e.g., 2× twig + 3× twig = 5× twig)
6. Returns `CombatRewards` with totals

### 3. World Item Management (`WorldItemManager`)

Manages items placed on tiles in the world:

```kotlin
class WorldItemManager {
    suspend fun placeItem(coordinate: TileCoordinate, itemId: String, quantity: Int = 1)
    suspend fun getItemsAt(coordinate: TileCoordinate): List<WorldItem>
    suspend fun pickupItem(coordinate: TileCoordinate, itemId: String): WorldItem?
    suspend fun hasItemsAt(coordinate: TileCoordinate): Boolean
}
```

**Thread-safe** with `Mutex` protection for concurrent access.

### 4. Visual Indicators (`TileMapView`)

Items on the ground are rendered with:
- **Gold sparkle effect** (Color 0xFFffdd00)
- **Two-layer circle** (outer glow + inner bright)
- **White diamond center** for visibility

## Usage Examples

### Example 1: Generate Combat Rewards

```kotlin
// After combat ends in victory
val defeatedEnemies: List<EnemyCombatData> = combat.enemies // From CombatState

val rewards = CombatManager.generateCombatRewards(defeatedEnemies)

println("Victory! ${rewards.summary()}")
// Output: "Victory! 50 XP | 3× twig | 2× seed | 1× beetle_shell"
```

### Example 2: Drop Loot at Combat Location

```kotlin
// When combat ends, drop loot at the combat tile
val combatLocation = TileCoordinate(playerX, playerY)
val rewards = CombatManager.generateCombatRewards(defeatedEnemies)

// Place each looted item on the ground
rewards.itemsLooted.forEach { (itemId, quantity) ->
    worldItemManager.placeItem(combatLocation, itemId, quantity)
}

// Show notification
pickupMessage = "Defeated enemies! ${rewards.summary()}"
```

### Example 3: Pick Up Item from Ground

```kotlin
// Player clicks on adjacent tile with items
if (worldItemManager.hasItemsAt(clickedCoord)) {
    val items = worldItemManager.getItemsAt(clickedCoord)
    val worldItem = items.first()
    
    val (newInventory, result) = InventoryManager.addItem(
        playerInventory,
        worldItem.itemId,
        worldItem.quantity
    )
    
    when (result) {
        is ItemAddResult.Success -> {
            playerInventory = newInventory
            worldItemManager.pickupItem(clickedCoord, worldItem.itemId)
            val item = ItemCatalog.getItem(worldItem.itemId)
            pickupMessage = "Picked up ${item?.name} ×${worldItem.quantity}"
        }
        is ItemAddResult.Failure.InventoryFull -> {
            pickupMessage = "Inventory full!"
        }
        is ItemAddResult.Failure.WeightExceeded -> {
            pickupMessage = "Too heavy!"
        }
    }
}
```

### Example 4: Full Combat Victory Flow

```kotlin
fun onCombatVictory(combat: CombatState, combatLocation: TileCoordinate) {
    scope.launch {
        // 1. Generate rewards
        val rewards = CombatManager.generateCombatRewards(combat.enemies)
        
        // 2. Add XP to player
        playerLevel.gainXP(rewards.xpGained)
        
        // 3. Drop loot on ground at combat location
        rewards.itemsLooted.forEach { (itemId, quantity) ->
            worldItemManager.placeItem(combatLocation, itemId, quantity)
        }
        
        // 4. Show victory message
        val enemyNames = rewards.defeatedEnemies.joinToString(", ")
        showNotification("Defeated $enemyNames! ${rewards.summary()}")
        
        // 5. Clear combat state
        activeCombat = null
    }
}
```

## Enemy Loot Tables

Enemies in `EnemyCatalog` define their loot:

```kotlin
Enemy(
    id = "grasshopper",
    name = "The Hopper",
    lootTable = LootTable(
        drops = listOf(
            LootDrop("twig", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
            LootDrop("seed", minQuantity = 1, maxQuantity = 3, dropChance = 0.6f)
        )
    ),
    xpReward = 12,
    level = 1
)
```

**Drop Mechanics:**
- Each item rolls independently against `dropChance`
- Quantity is random between `minQuantity` and `maxQuantity`
- Multiple items can drop from single enemy
- Items consolidate across multiple enemies

## Testing

Comprehensive tests in `CombatTest.kt`:

```kotlin
@Test
fun `generateCombatRewards calculates XP from single enemy`() {
    val enemy = createTestEnemy(catalogId = "grasshopper")
    val rewards = CombatManager.generateCombatRewards(listOf(enemy))
    assertEquals(12, rewards.xpGained) // Grasshopper XP
}

@Test
fun `generateCombatRewards consolidates duplicate items`() {
    val enemy1 = createTestEnemy(catalogId = "grasshopper")
    val enemy2 = createTestEnemy(catalogId = "grasshopper")
    val rewards = CombatManager.generateCombatRewards(listOf(enemy1, enemy2))
    
    // Items should be consolidated (no duplicates)
    val itemCounts = rewards.itemsLooted.groupBy { it.first }
    itemCounts.values.forEach { itemList ->
        assertEquals(1, itemList.size)
    }
}
```

## UI Integration Points

### TileGameScreen

1. **Item Pickup on Click:**
   - Check if clicked tile has items via `worldItemManager.hasItemsAt()`
   - Attempt to add to inventory
   - Remove from world on success
   - Show pickup message

2. **Visual Feedback:**
   - Gold sparkle effect renders on tiles with items
   - Pickup message displays for 3 seconds
   - Inventory weight/slots update in real-time

### TileMapView

- Tracks world items via `LaunchedEffect` on player movement
- Renders sparkle indicators for item locations
- Updates automatically when items are added/removed

## Performance

- **LootSystem.generateLoot()**: O(n) where n = number of loot table entries (typically 1-5)
- **generateCombatRewards()**: O(n × m) where n = enemies, m = loot entries per enemy
- **WorldItemManager**: Mutex-protected, O(1) tile lookups via HashMap
- **Item consolidation**: O(k log k) where k = total items dropped

## Future Enhancements

1. **Rarity-based sparkle colors** (common = white, rare = purple, epic = gold)
2. **Auto-pickup radius** for convenience
3. **Loot notification UI** showing all picked up items
4. **Combat loot window** displaying rewards before adding to inventory
5. **Loot multipliers** based on luck stat or difficulty settings

## Integration Checklist

When implementing combat in your game:

- [ ] Store enemy `catalogId` when creating `EnemyCombatData`
- [ ] Call `generateCombatRewards()` on combat victory
- [ ] Drop loot items at combat location using `WorldItemManager`
- [ ] Award XP to player
- [ ] Show victory notification with rewards summary
- [ ] Enable item pickup on adjacent tiles
- [ ] Render visual indicators for world items
- [ ] Test with multiple enemies and loot consolidation

## Architecture Notes

**Stateless Design:** `CombatManager.generateCombatRewards()` is a pure function with no side effects.

**Thread-Safety:** `WorldItemManager` uses Mutex for concurrent access safety.

**Separation of Concerns:**
- **CombatManager**: Reward calculation logic
- **LootSystem**: Probabilistic loot generation
- **WorldItemManager**: World item state
- **InventoryManager**: Player inventory operations
- **TileMapView**: Visual rendering

This architecture ensures modularity, testability, and easy integration with future features like multiplayer or save/load systems.
