# JalmarQuest Tile-Based Navigation System Guide

## Overview
The tile-based navigation system provides a 2D grid-based world where Jalmar explores, discovers POIs, and triggers game events. This system completely replaces the old directional movement with a more visual and interactive approach.

## Architecture

### Core Components

#### 1. TileMapManager
- Manages tile map loading and state
- Provides A* pathfinding between tiles
- Tracks discovered tiles for fog of war
- Thread-safe with Mutex protection

#### 2. Tile & TileMap Models
- `Tile`: Individual grid cell with terrain, walkability, and POIs
- `TileMap`: Complete map grid with metadata
- `TerrainType`: Different terrain affects movement costs
- `POIType`: Interactive elements on tiles

#### 3. PlayerMapAvatar
- Tracks player position on the grid
- Manages movement paths and animation state
- Handles facing direction for visual feedback

#### 4. MapTriggerManager
- Event-driven POI interaction system
- Emits triggers for: quests, encounters, resources, transitions
- Connects tile interactions to game systems

#### 5. TileMovementManager
- Processes movement requests (click-to-move or directional)
- Validates stamina requirements
- Integrates with GameStateManager for state updates

## Terrain Types & Movement Costs

| Terrain | Cost | Description | Visual |
|---------|------|-------------|--------|
| GRASS | 1 | Soft grass, easy traversal | Green |
| DIRT | 1 | Packed earth paths | Brown |
| STONE | 1 | Pebble paths (mountains to quail) | Gray |
| WATER | N/A | Puddles (impassable lakes) | Blue |
| MUD | 3 | Sticky mud (slow movement) | Dark Brown |
| SAND | 2 | Sandy patches | Tan |
| WOOD | 1 | Twig bridges/platforms | Wood Brown |
| WALL | N/A | Garden walls (impassable) | Dark Gray |
| HEDGE | N/A | Dense hedge barriers | Dark Green |

## POI Types & Interactions

| POI Type | Auto-Trigger | Description | Color |
|----------|--------------|-------------|-------|
| QUEST_GIVER | No | NPCs with quests | Gold |
| QUEST_OBJECTIVE | No | Quest target locations | Orange |
| ENCOUNTER | Yes | Combat triggers | Red |
| RESOURCE | No | Gatherable items | Purple |
| ENTRANCE | No | Building entrances | Blue |
| EXIT | Yes | Map transitions | Green |
| MERCHANT | No | Trading NPCs | Brown |
| LORE_FRAGMENT | No | Discoverable lore | Cyan |
| SAVE_POINT | No | Manual save locations | White |
| LANDMARK | No | Notable features | Orange |

## Movement System

### Click-to-Move
```kotlin
// Player clicks on tile (12, 15)
val result = tileMovementManager.moveToTile(player, 12, 15)
// A* pathfinding finds optimal route
// Player follows path with stamina cost
```

### Directional Movement
```kotlin
// Player presses arrow key
val result = tileMovementManager.moveDirection(player, GridDirection.UP)
// Moves one tile in direction if valid
```

### Stamina Costs
- Base cost = terrain movement cost
- Total path cost = sum of all tiles traversed
- Movement fails if stamina < required

## Fog of War

### Discovery Mechanics
- 3x3 area auto-discovered around spawn
- Tiles discovered as player moves
- Discovery persists in save games
- Mini-map shows only discovered areas

### Visibility Levels
- **Full (distance ≤ 2)**: Complete visibility
- **Partial (distance ≤ 3)**: 50% fog
- **Hidden (distance > 3)**: 90% fog
- **Undiscovered**: Black tiles

## Event System

### Butterfly Effect Integration
All interactions tracked for long-term consequences:
- Movement paths taken
- POIs discovered/interacted
- Failed movement attempts
- Map transitions

### Trigger Flow
1. Player enters/interacts with POI tile
2. MapTriggerManager emits appropriate trigger
3. Game systems subscribe to relevant triggers
4. State changes tracked in GameStateManager

## Visual Rendering

### TileMapRenderer Features
- Camera follows player (viewport scrolling)
- Smooth movement animations (300ms transitions)
- POI pulse effects (attracts attention)
- Fog of war with gradient edges
- Authentic quail avatar with direction indicator

### Mini-Map
- 150x150dp overview
- Shows discovered areas
- Player position indicator
- Color-coded terrain/POIs

## Creating New Maps

### Map Definition
```kotlin
fun createNewMap(): TileMap {
    val tiles = mutableListOf<Tile>()
    // Define 20x20 grid
    for (y in 0 until 20) {
        for (x in 0 until 20) {
            tiles.add(Tile(
                x = x, y = y,
                terrainType = TerrainType.GRASS,
                isWalkable = true
                // Add POIs as needed
            ))
        }
    }
    return TileMap(
        id = "new_area",
        name = "New Area",
        width = 20, height = 20,
        tiles = tiles,
        spawnPoint = TileCoordinate(10, 10)
    )
}
```

## Testing

### Key Test Scenarios
- Pathfinding around obstacles
- Stamina validation
- POI trigger emission
- Thread-safe concurrent access
- Fog of war discovery
- Map transitions
- "Quail level stupid" edge cases

### Running Tests
```bash
./gradlew :shared:commonTest
```

## Migration from Old System

### Automatic Conversion
- Old Position(x, y, locationId) → TilePosition(mapId, x, y)
- Location IDs mapped to tile map IDs
- Backwards compatible save system

### Removed Components
- Direction enum (NORTH, SOUTH, EAST, WEST)
- Old MovementManager
- Location-based connections

## Performance Considerations

- A* pathfinding optimized with Manhattan heuristic
- Viewport culling (only render visible tiles)
- StateFlow for efficient UI updates
- Mutex protection prevents race conditions

## Community Features

### Planned Additions
- Map editor for community-created areas
- Shareable map codes
- Tile set themes (seasonal variations)
- Custom POI types for mods

## Troubleshooting

### Common Issues

**Issue**: Player stuck, can't move
**Solution**: Check stamina, ensure target tile is walkable

**Issue**: POI not triggering
**Solution**: Verify POI type auto-trigger setting, check interaction

**Issue**: Fog of war not updating
**Solution**: Ensure discoverTile() called on movement

**Issue**: Performance lag with large maps
**Solution**: Verify viewport culling is working, check tile count
