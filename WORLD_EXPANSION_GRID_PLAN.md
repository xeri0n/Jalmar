# JalmarQuest World Expansion Grid Plan
## 500-Location Expansion Across 8 Biomes

**Date:** November 2025  
**Current Baseline:** 46 locations (gridX: -3 to 3, gridY: -2 to 7)  
**Expansion Target:** 500 new locations (546 total)  
**Performance Budgets:** Catalog load <2s, Memory <10MB, Lookup <100μs

---

## Grid Allocation Strategy

### Existing Grid Footprint Analysis
**Current Surface Grid Coverage:**
- **X Range:** -3 to 3 (7 columns)
- **Y Range:** -2 to 7 (10 rows)
- **Total Surface Coordinates Used:** 38 unique coordinates (5 locations share (0,0) via UP/DOWN)
- **Underground (CAVE):** 3 locations using special coordinates with UP/DOWN connections

**Expansion Strategy:**
- **Horizontal Expansion:** Extend X range to -10 to +10 (21 columns)
- **Vertical Expansion:** Extend Y range to -8 to +15 (24 rows)
- **Total Available Grid:** ~500 unique surface coordinates + multi-level caves
- **Connection Philosophy:** Manhattan distance pathfinding, coherent regional clustering

---

## REGION 1: GRASSLAND EXPANSION (~90 New Locations)

**Target Count:** 90 new grassland locations (9 existing + 90 new = 99 total GRASSLAND)

**Sub-Regions:**

### 1A: Buttonburgh Outskirts (15 locations, Levels 1-3)
**Grid Zone:** X: -2 to 2, Y: -1 to 2  
**Anchor:** starting_village (0,0), meadow_path (0,1), crossroads (1,1)  
**Theme:** Safe exploration zone, tutorial areas, civilian spaces  
**Settlements:** 2-3 minor hamlets (grain storage, chicken coop, vegetable patch)  
**Examples:**
- `pebble_plaza` (0,-1): Market square where birds trade seeds
- `dandelion_grove` (1,-1): Towering yellow flowers
- `puddle_lake` (-1,1): Grand "lake" to button quail
- `garden_gnome_shadow` (2,2): Looming statue casting ominous shade
- `compost_heap_foothills` (-2,2): Warm, aromatic mountain of decay

### 1B: Wildflower Plains (20 locations, Levels 2-5)
**Grid Zone:** X: -4 to 4, Y: 3 to 5  
**Anchor:** meadow_path (0,1) → northern expansion  
**Theme:** Open fields with seasonal flower blooms, butterfly encounters  
**Notable Landmarks:** "The Great Sprinkler" (active only in summer), Clover Kingdom  
**Examples:**
- `clover_kingdom` (0,6): Three-leaf and four-leaf clover fields
- `butterfly_migration_route` (2,5): Seasonal aerial display
- `morning_dew_meadow` (-3,4): Sparkling droplets at sunrise
- `thistle_forest` (3,4): Prickly purple towers
- `grasshopper_leap` (1,5): Training ground for jump practice

### 1C: Southern Prairies (15 locations, Levels 3-6)
**Grid Zone:** X: -3 to 5, Y: -3 to -1  
**Anchor:** rolling_hills (1,0), dunes_sea (2,-1)  
**Theme:** Transition zone between grassland and desert, dry grasses  
**Examples:**
- `tumbleweed_crossing` (3,-2): Desert plants invading grassland
- `gopher_burrow_network` (4,-1): Underground maze entrance
- `sun_baked_plain` (4,0): Cracked earth, sparse vegetation
- `prairie_dog_town` (5,-1): Settlement of watchful rodents

### 1D: Western Meadows (15 locations, Levels 2-5)
**Grid Zone:** X: -5 to -2, Y: -1 to 3  
**Anchor:** windmill_farm (-1,0), harbor_town (-2,0)  
**Theme:** Agricultural zone, human activity, safe havens  
**Examples:**
- `haystack_fortress` (-4,1): Massive golden barricade
- `irrigation_ditch` (-3,0): Miniature river for quail
- `scarecrow_watchtower` (-5,2): Raggedy sentinel
- `wheelbarrow_graveyard` (-4,0): Rusted metal monuments

### 1E: Far Northern Fields (15 locations, Levels 4-8)
**Grid Zone:** X: -3 to 3, Y: 6 to 8  
**Anchor:** elderwood (0,2) → grassland-forest transition  
**Theme:** Wild grasslands giving way to forest edge, untamed  
**Examples:**
- `wildflower_sea` (0,7): Endless rainbow blooms
- `seed_head_forest` (2,7): Tall grass stalks like trees
- `rabbit_warren_outskirts` (-2,7): Entrance to burrow complex
- `fence_line_patrol` (3,6): Territorial boundary marker

### 1F: Eastern Grazing Lands (10 locations, Levels 5-9)
**Grid Zone:** X: 5 to 8, Y: -2 to 2  
**Anchor:** foothill_pass (2,0) → mountain transition  
**Theme:** Rocky grasslands, sparse cover, challenging terrain  
**Examples:**
- `boulder_field` (6,0): Navigation obstacle course
- `eagle_shadow_plains` (7,1): Aerial predator territory
- `rocky_outcrop` (8,0): Vantage point overlooking lands
- `wind_swept_plateau` (6,2): Constant breeze zone

---

## REGION 2: FOREST EXPANSION (~85 New Locations)

**Target Count:** 85 new forest locations (8 existing + 85 new = 93 total FOREST)

**Sub-Regions:**

### 2A: Outer Forest Ring (15 locations, Levels 3-5)
**Grid Zone:** X: -3 to 3, Y: 2 to 4  
**Anchor:** elderwood (0,2), mushroom_glade (-1,2), hunters_lodge (1,3)  
**Theme:** Accessible forest, birdsong, sunlight filtering through leaves  
**Examples:**
- `fern_valley` (-2,3): Prehistoric-looking undergrowth
- `birch_grove` (2,3): White bark trees, serene atmosphere
- `fallen_log_bridge` (0,3.5): Crossing over stream (non-integer coordinate strategy?)
- `squirrel_highway` (3,3): Treetop path used by mammals

### 2B: Deep Woods (20 locations, Levels 5-8)
**Grid Zone:** X: -4 to 4, Y: 5 to 8  
**Anchor:** ancient_tree_heart (0,4), forest_shrine (0,5)  
**Theme:** Dense canopy, reduced light, primal forest  
**Examples:**
- `moss_carpet_clearing` (1,6): Soft green floor
- `woodpecker_grove` (-3,6): Constant hammering sounds
- `owl_territory` (2,6): Nocturnal danger zone
- `fairy_ring` (0,8): Mysterious mushroom circle

### 2C: Western Wetland Woods (15 locations, Levels 6-9)
**Grid Zone:** X: -5 to -2, Y: 3 to 6  
**Anchor:** thorn_brake (-1,3) → mire_maw (-2,3) transition  
**Theme:** Forest merging into swamp, soggy ground, mist  
**Examples:**
- `willow_weep` (-4,4): Hanging branches touching water
- `fogbank_hollow` (-5,5): Perpetual mist zone
- `cypress_knees` (-3,5): Protruding roots like spikes
- `heron_shallows` (-4,3): Wading bird hunting grounds

### 2D: Eastern Mountain Woods (15 locations, Levels 6-10)
**Grid Zone:** X: 3 to 6, Y: 2 to 5  
**Anchor:** hunters_lodge (1,3) → foothill_pass (2,0) connection  
**Theme:** Pine forests, rocky soil, altitude increase  
**Examples:**
- `pine_needle_carpet` (4,3): Soft, fragrant forest floor
- `boulder_moss_grove` (5,4): Stones covered in green
- `chipmunk_cache` (4,2): Nut storage facility
- `avalanche_scar` (6,3): Forest clearing from past disaster

### 2E: Enchanted Groves (10 locations, Levels 7-11)
**Grid Zone:** X: -2 to 2, Y: 9 to 11  
**Anchor:** ancient_tree_heart (0,4) → northern expansion  
**Theme:** Magical forests, bioluminescence, otherworldly  
**Examples:**
- `glowshroom_cathedral` (0,10): Massive mushroom chamber
- `singing_stones` (1,9): Musical rock formations
- `dreamweaver_glade` (-1,10): Reality seems fluid
- `eldergrove_council` (0,11): Ancient tree parliament

### 2F: Thornwood Labyrinth (10 locations, Levels 8-12)
**Grid Zone:** X: -4 to 0, Y: 3 to 4  
**Anchor:** thorn_brake (-1,3) expansion  
**Theme:** Hostile vegetation, maze-like, danger  
**Examples:**
- `briarblade_gauntlet` (-2,4): Thorns like swords
- `thistle_throne` (-3,3): Ancient seat of nature
- `poison_ivy_curtain` (-4,3): Natural barrier

---

## REGION 3: MOUNTAIN EXPANSION (~75 New Locations)

**Target Count:** 75 new mountain locations (7 existing + 75 new = 82 total MOUNTAIN)

**Sub-Regions:**

### 3A: Lower Slopes (15 locations, Levels 3-6)
**Grid Zone:** X: 2 to 5, Y: -1 to 2  
**Anchor:** foothill_pass (2,0), cragpeak (2,1)  
**Theme:** Rocky trails, increasing elevation, scrub vegetation  
**Examples:**
- `switchback_trail` (3,1): Zigzagging ascent
- `marmot_colony` (4,1): Whistling rodent village
- `scree_slope` (5,0): Unstable gravel hillside
- `mountain_spring` (3,0): Fresh water source

### 3B: Mid-Range Peaks (20 locations, Levels 6-10)
**Grid Zone:** X: 3 to 7, Y: 2 to 5  
**Anchor:** stonebridge_gorge (2,2), eagles_nest (2,3), dwarven_outpost (3,2)  
**Theme:** Challenging climbs, thin air, dramatic vistas  
**Examples:**
- `wind_howl_pass` (5,3): Constant gale-force winds
- `precipice_outlook` (6,4): Sheer cliff edge
- `mountain_goat_trails` (4,4): Narrow ledges
- `rockfall_canyon` (7,2): Active geological hazard

### 3C: High Peaks (15 locations, Levels 10-15)
**Grid Zone:** X: 1 to 4, Y: 5 to 8  
**Anchor:** frostpeak (2,4), mountain_temple (1,4)  
**Theme:** Snow-covered, extreme altitude, rare air  
**Examples:**
- `cloudpiercer_summit` (3,6): Above the clouds
- `avalanche_corridor` (2,6): Deadly snow slides
- `eagle_aerie_cluster` (4,6): Multiple nests
- `sky_shrine` (3,7): Altar to the heavens

### 3D: Western Range (10 locations, Levels 8-12)
**Grid Zone:** X: -1 to 2, Y: 4 to 6  
**Anchor:** mountain_temple (1,4) → aurora_fields (1,5) connection  
**Theme:** Isolated peaks, monastery outposts  
**Examples:**
- `meditation_ledge` (0,6): Monk training spot
- `hermit_cave` (1,6): Reclusive sage dwelling
- `prayer_flag_peak` (2,5): Colorful banners flapping

### 3E: Eastern Cliffs (10 locations, Levels 9-13)
**Grid Zone:** X: 5 to 8, Y: 3 to 6  
**Anchor:** Extension of existing mountain range  
**Theme:** Sheer faces, technical climbing required  
**Examples:**
- `vertical_maze` (7,4): Wall of stone corridors
- `cliff_swallow_city` (8,5): Thousands of mud nests
- `anchor_point_alpha` (6,5): Critical climbing station

### 3F: Underground Mountain Caverns (5 locations, Levels 10-14)
**Grid Zone:** Multi-level caves connected via UP/DOWN  
**Anchor:** crystal_mines (2,2) expansion  
**Theme:** Subterranean mountain exploration  
**Examples:**
- `gemstone_vein` (4,3 underground): Rich ore deposits
- `underground_river` (3,4 underground): Flowing beneath peaks
- `bat_colony_hall` (5,2 underground): Thousands roosting

---

## REGION 4: DESERT EXPANSION (~60 New Locations)

**Target Count:** 60 new desert locations (5 existing + 60 new = 65 total DESERT)

**Sub-Regions:**

### 4A: Outer Dunes (15 locations, Levels 4-7)
**Grid Zone:** X: 1 to 5, Y: -3 to -1  
**Anchor:** dunes_sea (2,-1), oasis_verdant (1,-1)  
**Theme:** Shifting sands, mirages, heat distortion  
**Examples:**
- `wandering_dunes` (4,-2): Mobile sand mountains
- `caravanserai_ruins` (3,-3): Ancient rest stop
- `sand_devil_alley` (5,-3): Dust whirlwinds

### 4B: Deep Desert (15 locations, Levels 7-11)
**Grid Zone:** X: 4 to 8, Y: -5 to -2  
**Anchor:** scorpion_gulch (3,-1), mirage_spire (2,-2)  
**Theme:** Extreme heat, dangerous wildlife, desolation  
**Examples:**
- `sun_altar` (6,-3): Ancient worship site
- `scorched_earth` (7,-4): Glass-like sand from heat
- `bone_field` (8,-3): Graveyard of lost travelers

### 4C: Desert Canyons (10 locations, Levels 6-10)
**Grid Zone:** X: 2 to 5, Y: -4 to -2  
**Anchor:** scorpion_gulch (3,-1) expansion  
**Theme:** Narrow gorges, flash flood danger, shade pockets  
**Examples:**
- `echo_canyon` (4,-3): Sound reverberates endlessly
- `slot_canyon` (3,-4): Tight passage through rock
- `rattlesnake_den` (5,-4): Serpent nesting grounds

### 4D: Oasis Network (10 locations, Levels 5-8)
**Grid Zone:** X: 0 to 3, Y: -3 to -1  
**Anchor:** oasis_verdant (1,-1) expansion  
**Theme:** Life-sustaining water sources, palm clusters  
**Examples:**
- `palm_oasis_minor` (1,-3): Small water pool
- `date_palm_grove` (2,-3): Fruit-bearing trees
- `desert_spring` (0,-2): Hidden water source

### 4E: Sandstone Formations (5 locations, Levels 8-12)
**Grid Zone:** X: 0 to 2, Y: -4 to -2  
**Anchor:** sandstone_ruins (1,-2) expansion  
**Theme:** Eroded monuments, arches, balanced rocks  
**Examples:**
- `needle_spires` (0,-3): Thin stone towers
- `arch_of_ages` (1,-4): Natural bridge
- `hoodoo_forest` (2,-4): Bizarre rock pillars

### 4F: Buried Complexes (5 locations, Levels 10-15)
**Grid Zone:** Multi-level underground via DOWN connections  
**Anchor:** forgotten_catacombs (1,-5) expansion  
**Theme:** Ancient civilizations buried by time  
**Examples:**
- `pharaohs_antechamber` (1,-6 underground): Royal entrance
- `cursed_vault` (2,-6 underground): Treasure and traps
- `mummy_preparation_hall` (1,-7 underground): Embalming chamber

---

## REGION 5: SWAMP EXPANSION (~55 New Locations)

**Target Count:** 55 new swamp locations (5 existing + 55 new = 60 total SWAMP)

**Sub-Regions:**

### 5A: Outer Marshlands (15 locations, Levels 7-9)
**Grid Zone:** X: -4 to -2, Y: 3 to 5  
**Anchor:** mire_maw (-2,3), boglanter (-3,3)  
**Theme:** Shallow water, cattails, frog chorus  
**Examples:**
- `cattail_thicket` (-3.5,4): Dense reed beds
- `frog_croaking_pond` (-4,4): Deafening amphibians
- `quickmud_trap` (-3,4.5): Deadly suction hazard

### 5B: Deep Swamp (15 locations, Levels 8-11)
**Grid Zone:** X: -5 to -2, Y: 4 to 7  
**Anchor:** rotten_hollow (-2,4), sunken_temple (-2,5)  
**Theme:** Stagnant water, decay, dangerous gases  
**Examples:**
- `methane_bubble_bog` (-4,6): Flammable gas vents
- `corpse_flower_glade` (-5,5): Horrible smell
- `leech_pool` (-3,6): Parasitic waters

### 5C: Witch's Domain (10 locations, Levels 9-12)
**Grid Zone:** X: -5 to -3, Y: 4 to 5  
**Anchor:** witch_hut (-3,4) expansion  
**Theme:** Cursed lands, dark magic, unnatural growth  
**Examples:**
- `hex_circle` (-4,5): Ritual grounds
- `toadstool_ring_cursed` (-5,4): Malevolent fungi
- `voodoo_doll_grove` (-4,4.5): Creepy effigies

### 5D: Sunken Ruins Zone (10 locations, Levels 10-14)
**Grid Zone:** X: -3 to 0, Y: 5 to 7  
**Anchor:** sunken_temple (-2,5) expansion  
**Theme:** Submerged civilization, cultist activity  
**Examples:**
- `drowned_plaza` (-1,6): Underwater square
- `cult_hideout` (-2,6): Secret worshipper base
- `idol_chamber` (-1,7): Blasphemous statue

### 5E: Mangrove Labyrinths (5 locations, Levels 8-11)
**Grid Zone:** X: -6 to -4, Y: 2 to 4  
**Anchor:** Western swamp edge  
**Theme:** Twisted roots, maze-like passages  
**Examples:**
- `root_tangle` (-5,3): Impassable web of wood
- `mangrove_canopy` (-6,3): Dense overhead coverage
- `mud_skip_channel` (-5,2): Navigation challenge

---

## REGION 6: TUNDRA EXPANSION (~50 New Locations)

**Target Count:** 50 new tundra locations (4 existing + 50 new = 54 total TUNDRA)

**Sub-Regions:**

### 6A: Southern Tundra (15 locations, Levels 12-15)
**Grid Zone:** X: 0 to 3, Y: 5 to 7  
**Anchor:** frozen_waste (2,5), aurora_fields (1,5)  
**Theme:** Transition from mountain to ice, sparse trees  
**Examples:**
- `frost_bite_ridge` (2,5.5): Dangerously cold
- `tundra_wolf_den` (3,6): Predator territory
- `frozen_lake` (1,6.5): Thick ice surface

### 6B: Central Ice Fields (15 locations, Levels 13-16)
**Grid Zone:** X: 1 to 4, Y: 6 to 9  
**Anchor:** icecrystal_cavern (2,6), frozen_waste (2,5)  
**Theme:** Endless white expanse, whiteout conditions  
**Examples:**
- `blizzard_zone` (3,7): Zero visibility
- `ice_spike_field` (4,7): Crystalline protrusions
- `polar_bear_territory` (2,8): Apex predator land

### 6C: Northern Wastes (10 locations, Levels 15-18)
**Grid Zone:** X: 1 to 3, Y: 8 to 12  
**Anchor:** frostgiant_lair (2,7) expansion  
**Theme:** Extreme cold, frostgiant civilization  
**Examples:**
- `ice_fortress_outer_wall` (2,9): Giant architecture
- `frozen_throne_approach` (2,10): Path to king
- `mammoth_graveyard` (3,9): Ancient beast bones

### 6D: Western Aurora Lands (5 locations, Levels 12-14)
**Grid Zone:** X: -1 to 1, Y: 6 to 8  
**Anchor:** aurora_fields (1,5) expansion  
**Theme:** Magical northern lights, mystical  
**Examples:**
- `aurora_veil` (0,7): Shimmering light curtain
- `sky_walker_plateau` (0,8): Shamanic ritual site
- `spirit_ice` (-1,7): Glowing frozen formations

### 6E: Ice Caves (5 locations, Levels 14-17)
**Grid Zone:** Multi-level underground via DOWN  
**Anchor:** icecrystal_cavern (2,6) expansion  
**Theme:** Subglacial passages, frozen beauty  
**Examples:**
- `glacier_tunnel` (2,7 underground): Blue ice corridor
- `frozen_waterfall_cavern` (3,6 underground): Ice sculpture
- `permafrost_chamber` (2,8 underground): Oldest ice

---

## REGION 7: COASTAL EXPANSION (~50 New Locations)

**Target Count:** 50 new coastal locations (5 existing + 50 new = 55 total COASTAL)

**Sub-Regions:**

### 7A: Harbor District Expansion (10 locations, Levels 5-7)
**Grid Zone:** X: -3 to -2, Y: -1 to 2  
**Anchor:** harbor_town (-2,0), cliffside (-2,1)  
**Theme:** Bustling docks, trade, civilization  
**Examples:**
- `fishing_wharf` (-2.5,0): Active fishermen
- `merchant_row` (-2,0.5): Shop district
- `harbor_lighthouse` (-3,0): Navigation beacon

### 7B: Northern Cliffs (15 locations, Levels 4-8)
**Grid Zone:** X: -4 to -2, Y: 2 to 5  
**Anchor:** lighthouse_point (-2,2), cliffside (-2,1)  
**Theme:** Sheer drops, seabird colonies, waves  
**Examples:**
- `gull_nesting_cliff` (-3,3): Thousands of birds
- `seal_haul_out` (-4,2): Sunbathing mammals
- `cliff_cave_network` (-3,4): Sea-carved tunnels

### 7C: Southern Coves (10 locations, Levels 6-9)
**Grid Zone:** X: -4 to -2, Y: -3 to -1  
**Anchor:** shipwreck_cove (-2,-1), tidepool (-3,-1)  
**Theme:** Hidden beaches, pirate lore, treasure  
**Examples:**
- `smugglers_cave` (-3,-2): Secret storage
- `pirate_graveyard` (-4,-1): Marked graves
- `treasure_beach` (-2,-3): Gold coins washing up

### 7D: Offshore Rocks (10 locations, Levels 7-10)
**Grid Zone:** X: -5 to -3, Y: -2 to 3  
**Anchor:** Accessible from coastal zones  
**Theme:** Isolated rocks, kelp forests, marine life  
**Examples:**
- `seal_rock` (-5,0): Barking colony
- `kelp_forest_shallows` (-4,1): Underwater forest
- `barnacle_fortress` (-5,1): Encrusted stone

### 7E: Underwater Zones (5 locations, Levels 10-15)
**Grid Zone:** Multi-level via DOWN connections  
**Anchor:** tidepool (-3,-1) → deep_dark (0,-5) connection exists  
**Theme:** Subaquatic exploration, shipwrecks  
**Examples:**
- `coral_reef_garden` (-4,-2 underwater): Colorful life
- `sunken_galleon` (-3,-3 underwater): Pirate ship
- `octopus_lair` (-5,-1 underwater): Intelligent predator

---

## REGION 8: CAVE EXPANSION (~35 New Locations)

**Target Count:** 35 new cave locations (3 existing + 35 new = 38 total CAVE)

**Sub-Regions:**

### 8A: Crystal Mines Expansion (10 locations, Levels 8-12)
**Grid Zone:** Multi-level underground near (2,2)  
**Anchor:** crystal_mines (2,2) expansion  
**Theme:** Mining operations, gem veins, dwarven influence  
**Examples:**
- `amethyst_chamber` (2,3 underground): Purple crystals
- `miners_ghost_camp` (3,2 underground): Abandoned tools
- `collapsed_tunnel` (2,1 underground): Cave-in hazard

### 8B: The Deep Dark Network (15 locations, Levels 15-20)
**Grid Zone:** Multi-level deep underground  
**Anchor:** deep_dark (0,-5) expansion  
**Theme:** Eldritch horrors, absolute darkness, alien  
**Examples:**
- `whisper_corridor` (0,-6 underground): Voices in dark
- `blind_fish_lake` (1,-6 underground): Eyeless ecosystem
- `tentacle_pit` (-1,-6 underground): Unknown creature
- `madness_chamber` (0,-7 underground): Mind-breaking space

### 8C: Catacombs Expansion (5 locations, Levels 16-18)
**Grid Zone:** Multi-level under desert  
**Anchor:** forgotten_catacombs (1,-5) expansion  
**Theme:** Undead guardians, ancient traps, curses  
**Examples:**
- `bone_maze` (2,-5 underground): Skeletal architecture
- `mummy_throne_room` (1,-6 underground): Royal resting
- `trap_gauntlet` (1,-5.5 underground): Mechanical death

### 8C: Connecting Tunnels (5 locations, Levels 12-17)
**Grid Zone:** Various underground connections  
**Anchor:** Link existing cave systems  
**Theme:** Natural passages, underground rivers  
**Examples:**
- `subterranean_rapids` (1,-4 underground): Fast water
- `glow_worm_tunnel` (0,-4 underground): Bioluminescent
- `echo_chamber_junction` (1,-3 underground): Crossroads

---

## Grid Coordinate Allocation Summary

### New Grid Boundaries
**Surface Grid:**
- **X Range:** -10 to +10 (21 columns total)
- **Y Range:** -8 to +15 (24 rows total)
- **Available Surface Coordinates:** ~500 unique positions

**Underground Grid:**
- Multi-level system using same X/Y but different elevation markers
- Connected via UP/DOWN direction in LocationConnection
- ~40-50 underground locations planned

### Reserved Coordinates (Do Not Use)
**Existing 46 locations already occupy these surface coordinates:**
(0,0), (0,1), (1,0), (-1,0), (1,1), (0,2), (0,3), (-1,2), (1,3), (0,4), (-1,3), (1,4), (0,5), (2,0), (2,1), (2,2), (2,3), (2,4), (3,2), (1,-1), (2,-1), (1,-2), (3,-1), (2,-2), (-2,3), (-2,4), (-3,4), (-3,3), (-2,5), (2,5), (2,6), (1,5), (2,7), (-2,0), (-2,1), (-2,-1), (-2,2), (-3,-1)

---

## Connection Strategy

### Regional Anchors
Each new sub-region must connect to at least ONE existing location to ensure full reachability from starting_village.

**Primary Anchors:**
- GRASSLAND: starting_village (0,0), meadow_path (0,1), crossroads (1,1)
- FOREST: elderwood (0,2), hunters_lodge (1,3), ancient_tree_heart (0,4)
- MOUNTAIN: foothill_pass (2,0), dwarven_outpost (3,2), frostpeak (2,4)
- DESERT: dunes_sea (2,-1), oasis_verdant (1,-1), sandstone_ruins (1,-2)
- SWAMP: mire_maw (-2,3), witch_hut (-3,4), sunken_temple (-2,5)
- TUNDRA: frozen_waste (2,5), aurora_fields (1,5), frostgiant_lair (2,7)
- COASTAL: harbor_town (-2,0), cliffside (-2,1), shipwreck_cove (-2,-1)
- CAVE: crystal_mines (2,2), deep_dark (0,-5), forgotten_catacombs (1,-5)

### Connection Rules
1. **Average Connections Per Location:** 2-4 (some hubs may have 5-6)
2. **Bidirectional Logic:** If A connects to B via NORTH, B should connect to A via SOUTH
3. **Manhattan Distance Respect:** Adjacent grid coordinates should be 1 step apart
4. **No Impossible Locks:** Every new location must have at least 2 different paths to reach it (no single-point failures)
5. **Biome Transitions:** Locations on biome boundaries can connect across types (e.g., grassland→forest, mountain→tundra)

---

## Level Progression Zones

**Spatial Distribution of Difficulty:**
- **Levels 1-3:** Buttonburgh vicinity (gridX: -2 to 2, gridY: -1 to 2)
- **Levels 4-7:** First expansion ring (gridX: -5 to 5, gridY: -3 to 5)
- **Levels 8-12:** Second ring (gridX: -7 to 7, gridY: -5 to 8)
- **Levels 13-16:** Third ring (gridX: -9 to 9, gridY: -7 to 12)
- **Levels 17-20:** Endgame zones (extreme coordinates, deep underground)

**General Rule:** Recommended level = `1 + (Manhattan distance from starting_village × 0.4)` rounded up  
**Exceptions:** Dungeons, boss lairs, special zones may exceed formula

---

## Settlement Distribution

**Existing Settlements:** 8 (Buttonburgh, Hunter's Lodge, Dwarven Outpost, Mountain Temple, Verdant Oasis, Harbor Town, Witch's Hut)

**Planned New Settlements:** 12-15
- GRASSLAND: 3 new (grain storage hamlet, chicken coop village, garden tool outpost)
- FOREST: 2 new (ranger station, druid grove sanctuary)
- MOUNTAIN: 2 new (climber's base camp, sky monastery)
- DESERT: 2 new (nomad camp, sandstone trading post)
- SWAMP: 1 new (hermit village)
- TUNDRA: 1 new (ice fisher settlement)
- COASTAL: 2 new (fishing village, pearl diver cove)
- CAVE: 0 new (too dangerous for settlements)

---

## Performance Validation Targets

**Current Performance (46 locations):**
- Catalog Load: <100ms estimated
- Memory: <1MB estimated
- Lookup: <10μs estimated

**Target Performance (546 locations):**
- Catalog Load: <2000ms (10x content = 10x time budget acceptable)
- Memory: <10MB (safety margin)
- Lookup: <100μs (logarithmic degradation acceptable)

**Mitigation Strategies:**
- Lazy loading via `by lazy` for region catalogs
- Keep catalog objects simple (avoid nested complexity)
- Use efficient List (not MutableList) for immutability

---

## Next Steps (Task 3)

1. Generate LocationCatalog_Grassland.kt (~90 locations)
2. Generate LocationCatalog_Forest.kt (~85 locations)
3. Generate LocationCatalog_Mountain.kt (~75 locations)
4. Generate LocationCatalog_Desert.kt (~60 locations)
5. Generate LocationCatalog_Swamp.kt (~55 locations)
6. Generate LocationCatalog_Tundra.kt (~50 locations)
7. Generate LocationCatalog_Coastal.kt (~50 locations)
8. Generate LocationCatalog_Cave.kt (~35 locations)

Each file will contain:
- Package declaration and imports
- Internal val `[BIOME]_LOCATIONS` list
- All Location objects with vivid descriptions
- Seasonal variants for 10-15% of signature locations
- Coherent connections forming explorable network
- Balanced encounter rates and recommended levels
