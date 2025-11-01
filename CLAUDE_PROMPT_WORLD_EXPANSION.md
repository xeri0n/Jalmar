# Claude Sonnet 4.5 — World Expansion Task (AAA Quality, 500 New Locations)

You are a senior worldbuilder and Kotlin Multiplatform engineer collaborating on JalmarQuest, a "tiny hero, big world" narrative RPG starring a real button quail, Jalmar. You must produce both top-tier world design and production-grade KMP code that integrates cleanly with the existing architecture. Failure is not an option—deliver AAA+ quality.

---

## Context Snapshot (Do Not Re-derive)

Current codebase facts (authoritative):
- Language/Arch: Kotlin Multiplatform (KMP). Core logic in `shared/src/commonMain` using the Catalog pattern for static content.
- World model signatures:
  - Direction: `enum class Direction { NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST, UP, DOWN }`
  - Biomes: `enum class BiomeType { FOREST, MOUNTAIN, GRASSLAND, DESERT, SWAMP, TUNDRA, COASTAL, CAVE }`
  - Biome properties: `BiomeProperties.getDefaultProperties(type)` already implemented.
  - Location:
    ```kotlin
    @Serializable
    data class Location(
        val id: String,
        val name: String,
        val description: LocationDescription,
        val biome: BiomeType,
        val gridX: Int,
        val gridY: Int,
        val connections: List<LocationConnection> = emptyList(),
        val isSettlement: Boolean = false,
        val hasFastTravel: Boolean = false,
        val isSafeZone: Boolean = false,
        val shopAvailable: Boolean = false,
        val innAvailable: Boolean = false,
        val questGiverIds: List<String> = emptyList(),
        val encounterRate: Double = 1.0,
        val recommendedLevel: Int = 1,
        val lore: String = ""
    ) {
        init {
            require(id.isNotBlank())
            require(name.isNotBlank())
            require(encounterRate >= 0.0)
            require(recommendedLevel >= 1)
        }
    }
    ```
  - Location description:
    ```kotlin
    @Serializable
    data class LocationDescription(
        val base: String,
        val spring: String? = null,
        val summer: String? = null,
        val autumn: String? = null,
        val winter: String? = null
    )
    ```
  - Connection:
    ```kotlin
    @Serializable
    data class LocationConnection(
        val targetLocationId: String,
        val direction: Direction,
        val travelTime: Int = 1,
        val requiredLevel: Int = 1,
        val isHidden: Boolean = false,
        val isBlocked: Boolean = false,
        val unlockCondition: String? = null
    )
    ```
- Pathfinding: A* over grid with Manhattan heuristic. `gridX, gridY` must remain a coherent navigable graph.
- Save/serialization: kotlinx.serialization; keep data classes compatible.
- Java version: 17.

### Current World Inventory (authoritative baseline)
- Catalog: `LocationCatalog` currently exposes 8 regions totaling 46 locations:
  - GRASSLAND: 9 (starting area + Buttonburgh buildings + outskirts)
  - FOREST: 8
  - MOUNTAIN: 7
  - DESERT: 5
  - SWAMP: 5
  - TUNDRA: 4
  - COASTAL: 5
  - CAVE/UNDERGROUND: 3
- Tone pillars: “tiny hero, big world,” re-contextualize mundane → epic (twig → Twig Spear, puddle → lake, garden gnome → titan). Sincere, cozy, with self-aware humor.
- Accessibility: TTS-friendly descriptions.

---

## Mission
Create a huge, immersive world expansion by adding exactly 500 new, fully wired locations across the existing 8 biomes, maintaining AAA+ narrative and systems quality. Include ALL additional artifacts required for a production-ready integration.

You must deliver BOTH world content and ready-to-merge KMP code, with validations and tests. Zero broken references; zero compile failures.

---

## Non-Negotiable Constraints
- Do NOT modify or delete existing location IDs or behavior.
- Use ONLY existing `BiomeType` values (FOREST, MOUNTAIN, GRASSLAND, DESERT, SWAMP, TUNDRA, COASTAL, CAVE).
- No negative stat-like values anywhere that enforce `require(...) >= 0` style validation.
- All new IDs must be globally unique, kebab_or_snake case (e.g., `sunken_reed_maze`).
- Connections must form a coherent traversable grid. Avoid overlaps: do not reuse an existing pair (gridX, gridY) unless deliberate multi-level (CAVE with UP/DOWN from a surface tile) and then clearly signpost with Direction.UP/DOWN links.
- Seasonal descriptions: make base always present; seasonal variants optional but encouraged for signature locales.
- Encounter rate >= 0.0; recommendedLevel in [1..50]. Safe zones must have encounterRate=0.0.
- Quest/NPC cross-refs: If you reference any NPC or quest IDs that do not exist, you must also add them to their catalogs in the same output pack with correct serialization and tests, or leave `questGiverIds` empty. Prefer avoiding new hard references unless you implement them.
- Performance budgets must continue to pass: startup < 2s; memory < 10MB for catalogs; lookups < 100μs. Follow existing patterns; no heavyweight runtime logic in static catalogs.

---

## World Design Requirements (AAA Quality)
- Biome spread targets (flexible ±10%):
  - GRASSLAND: ~90
  - FOREST: ~85
  - MOUNTAIN: ~75
  - DESERT: ~60
  - SWAMP: ~55
  - TUNDRA: ~50
  - COASTAL: ~50
  - CAVE/UNDERGROUND: ~35
  Total = 500 new locations.
- Each location must include:
  - A vivid, TTS-friendly base description (1-3 paragraphs), true to “tiny hero” scale.
  - Optional seasonal variants for signature locales.
  - Sensible `recommendedLevel` curve by distance from Buttonburgh and by biome danger profile.
  - Connections: 2-5 logical exits, directions consistent, no one-way traps unless justifiable with unlockCondition lore.
  - If `isSettlement`: consider `shopAvailable`/`innAvailable`, `hasFastTravel` sparingly, `isSafeZone` true.
  - `lore`: 1-3 sentences of world history or tiny-epic re-contextualization hook.
- Macro-structure & theming:
  - Regions should cluster into sub-areas (e.g., Elderwood Fringe, Fungus Belt, Rootspire, etc.) with escalating challenge.
  - Hidden shortcuts and late-game fast-travel anchors.
  - Environmental storytelling: human backyard objects as megastructures; fauna as titans.
  - Resource ecology: ensure logical distribution for crafting loops (twigs, acorn caps, silk, minerals, seeds).

---

## Engineering Requirements
- Code organization:
  - Keep the Catalog pattern. Due to size, split into region files:
    - `shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Grassland.kt`
    - `.../LocationCatalog_Forest.kt`
    - `.../LocationCatalog_Mountain.kt`
    - `.../LocationCatalog_Desert.kt`
    - `.../LocationCatalog_Swamp.kt`
    - `.../LocationCatalog_Tundra.kt`
    - `.../LocationCatalog_Coastal.kt`
    - `.../LocationCatalog_Cave.kt`
  - Maintain a central `LocationCatalog.kt` that aggregates `allLocations` by concatenating region lists.
  - Follow existing data classes exactly; use kotlinx.serialization; no platform-specific code.
- Grid and connectivity:
  - Use an integer grid. Maintain Manhattan-coherent distances.
  - Avoid collisions: each (gridX, gridY) used by at most one surface location; underground layers use UP/DOWN links from a surface anchor with their own unique grid coordinates (e.g., same X,Y but `CAVE` on a designated sublayer—encode via ID and clear connections, not extra coordinates).
  - Every new location must be reachable from `starting_village` via a finite set of connections without unmet unlocks.
- Validation & tests (must include and pass):
  - Update/extend existing integration tests (similar to `QuickValidationTest`) to verify:
    - Unique location IDs
    - All connection target IDs exist
    - No impossible one-way softlocks (any locked path must have at least one alternate route or obtainable unlock)
    - Encounter and recommendedLevel constraints
    - Seasonal description non-empty base
  - Add performance sanity tests (fast lookups, catalog load time rough check without hard flakiness).

---

## Output Contract (Deliverables)
Return a single unified answer containing:
1. A short executive summary (1-2 paragraphs).
2. The complete Kotlin source additions/edits as separate code blocks per file (ready to paste):
   - 8 region files with 500 new `Location` entries in total (clearly delineate per file).
   - Updated `LocationCatalog.kt` that imports/aggregates region lists and appends them to existing locations without modification.
3. A `WorldConnectivityTest.kt` in `shared/src/commonTest/.../world/` that validates:
   - All location IDs unique
   - All connections refer to existing IDs
   - All locations reachable from `starting_village`
   - All `LocationDescription.base` non-empty
   - `encounterRate >= 0.0`, `recommendedLevel >= 1`
4. A `PerformanceSmokeTest.kt` ensuring:
   - Catalog load completes under 2000 ms on desktop target
   - Lookup of 1000 random IDs averages < 100 μs
5. A compact Region Map Index table (Markdown) listing sub-regions, (gridX,gridY) ranges, and notable hubs.

Formatting:
- Use minimal imports and preserve package paths under `com.jalmarquest.shared.world`.
- Keep each code block labeled with its full file path comment at the top.
- Keep lines <= 120 chars when possible; wrap descriptions reasonably for readability.

---

## Examples (Style Only — Do Not Duplicate IDs)
```kotlin
// File: shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Grassland.kt
package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.*

internal val GRASSLAND_LOCATIONS: List<Location> = listOf(
    Location(
        id = "thimble_lake",
        name = "Thimble Lake",
        description = LocationDescription.withAllSeasons(
            spring = "A grand lake by quail reckoning: a deep puddle ringed with moss and dandelions.",
            summer = "The sun turns the ripple-ringed surface into hammered bronze; swallows dart like lancers.",
            autumn = "Leaves drift like galleons; the water smells of iron and old rain.",
            winter = "A thin pane of ice sings when tapped; you step carefully, a knight on glass."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 3, gridY = 1,
        connections = listOf(
            LocationConnection("meadow_path", Direction.SOUTH),
            LocationConnection("rolling_hills", Direction.WEST)
        ),
        isSettlement = false,
        hasFastTravel = false,
        isSafeZone = false,
        encounterRate = 0.4,
        recommendedLevel = 3,
        lore = "Humans call it a puddle. Jalmar calls it a borderless sea."
    )
)
```

---

## Quality Gates (Must Pass)
- Compile on JVM/desktop with JDK 17.
- All tests pass (existing + new) with zero failures.
- No broken references; `LocationManager.getLocation(id)` must resolve for every connection target.
- Catalog load time under 2s; memory footprint under 10 MB.
- World remains navigable end-to-end using A* with Manhattan heuristic.

---

## Final Checklist (Return This Section Completed)
- [ ] 500 new locations added across 8 biome files
- [ ] Aggregation updated in `LocationCatalog.kt` without touching existing entries
- [ ] All IDs unique and validated
- [ ] All connections valid and world fully reachable
- [ ] Seasonal descriptions present on headline locales
- [ ] Tests added and passing (integration + performance smoke)
- [ ] Performance budgets respected
- [ ] TTS-friendly descriptions, authenticity maintained

Deliver now. AAA+ quality; no excuses, no omissions.