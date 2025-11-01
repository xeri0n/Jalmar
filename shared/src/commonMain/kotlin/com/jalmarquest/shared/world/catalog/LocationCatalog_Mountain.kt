package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * MOUNTAIN region catalog - 75 new locations expanding the mountain/alpine areas
 * Sub-regions: Lower Slopes (3A), Mid-Range Peaks (3B), High Peaks (3C),
 *              Western Range (3D), Eastern Cliffs (3E), Underground Mountain Caverns (3F)
 * Connects to existing locations: foothill_pass, dwarven_outpost, frostpeak
 */
internal val MOUNTAIN_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 3A: Lower Slopes (15 locations, levels 3-6) ====================
        // Grid: X: 2-4, Y: -1 to 2
        // Theme: Transition from grassland/forest to mountains, gentle slopes, rocky terrain
        
        Location(
            id = "scree_slope_approach",
            name = "Scree Slope Approach",
            description = LocationDescription.withAllSeasons(
                spring = "Loose pebbles cascade down the gentle mountainside like a river of stone. Tiny wildflowers sprout between scattered boulders the size of your entire body, their roots gripping the unstable ground with desperate tenacity.",
                summer = "Heat radiates from sun-baked rocks, creating shimmering mirages on the scree-covered slope. Each step sends miniature avalanches of pebbles tumbling down—for you, it's like walking on a constantly shifting ocean of stone.",
                autumn = "Fallen leaves collect in natural pockets between rocks, creating amber and crimson pools against the gray stone. The scree crunches with a satisfying crispness under your talons, frosted by the cooling mountain air.",
                winter = "Ice transforms the loose rocks into a treacherous skating rink. Each pebble is coated in crystalline frost, making the entire slope glitter like a field of diamonds—beautiful but deadly for a tiny quail's footing."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 0,
            connections = listOf(
                LocationConnection("foothill_pass", Direction.SOUTH),
                LocationConnection("boulder_field", Direction.EAST),
                LocationConnection("alpine_meadow_lower", Direction.NORTH),
                LocationConnection("switchback_trail_start", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 3
        ),
        
        Location(
            id = "alpine_meadow_lower",
            name = "Lower Alpine Meadow",
            description = LocationDescription.simple(
                "A pocket of relative flatness nestled between rocky outcrops. Tough mountain grasses and stunted flowers create a miniature paradise—what would be ankle-high to a human towers over you like a dense forest. Marmot burrows riddle the hillside, each entrance a cavern you could walk into."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 1,
            connections = listOf(
                LocationConnection("scree_slope_approach", Direction.SOUTH),
                LocationConnection("boulder_garden", Direction.NORTH),
                LocationConnection("marmot_colony", Direction.EAST),
                LocationConnection("stone_stairway_natural", Direction.WEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 4,
            isSafeZone = true
        ),
        
        Location(
            id = "switchback_trail_start",
            name = "Switchback Trail Beginning",
            description = LocationDescription.simple(
                "A narrow animal path zigzags up the mountainside—'narrow' meaning it's barely wide enough for you. Deep ruts carved by centuries of deer hooves create trenches you must navigate like canyons. The trail climbs steadily, switchbacking every few tail-lengths."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 0,
            connections = listOf(
                LocationConnection("scree_slope_approach", Direction.EAST),
                LocationConnection("foothill_pass", Direction.SOUTH),
                LocationConnection("switchback_trail_middle", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 3
        ),
        
        Location(
            id = "switchback_trail_middle",
            name = "Switchback Trail Midpoint",
            description = LocationDescription.simple(
                "The trail continues its relentless zigzag ascent. You're high enough now that looking down gives you vertigo—the grasslands below are a distant green carpet. Mountain goat tracks scar the path, each hoof print a crater you could nest in."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 1,
            connections = listOf(
                LocationConnection("switchback_trail_start", Direction.SOUTH),
                LocationConnection("stone_stairway_natural", Direction.EAST),
                LocationConnection("eagles_rest_overlook", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 4
        ),
        
        Location(
            id = "stone_stairway_natural",
            name = "Natural Stone Stairway",
            description = LocationDescription.simple(
                "Erosion and geological upheaval have created a series of rock shelves that ascend like giant's stairs. For a human, these would be gentle steps. For you, each 'step' is a chest-high cliff requiring a flutter-jump to scale. Lichen patterns the stone in fluorescent greens and oranges."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 1,
            connections = listOf(
                LocationConnection("switchback_trail_middle", Direction.WEST),
                LocationConnection("western_summit_overlook", Direction.WEST),
                LocationConnection("alpine_meadow_lower", Direction.EAST),
                LocationConnection("crystal_seep", Direction.NORTH)
            ),
            encounterRate = 0.40,
            recommendedLevel = 4
        ),
        
        Location(
            id = "boulder_garden",
            name = "Boulder Garden",
            description = LocationDescription.simple(
                "Massive rocks lie scattered across the slope like toys abandoned by a giant child. Each boulder creates a complex microclimate—shaded northern faces drip with moss, sun-baked southern faces are barren and hot. You navigate the spaces between them, a labyrinth of stone."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 2,
            connections = listOf(
                LocationConnection("alpine_meadow_lower", Direction.SOUTH),
                LocationConnection("crystal_seep", Direction.WEST),
                LocationConnection("marmot_colony", Direction.SOUTH),
                LocationConnection("marmot_colony", Direction.EAST),
                LocationConnection("cliff_face_lower", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 5
        ),
        
        Location(
            id = "marmot_colony",
            name = "Marmot Colony",
            description = LocationDescription.simple(
                "The mountainside is riddled with burrow entrances, each large enough for you to explore. Marmots the size of bears (from your perspective) sun themselves on flat rocks, their whistling alarm calls echoing across the peaks. This is their kingdom, and you are a very small visitor."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 1,
            connections = listOf(
                LocationConnection("alpine_meadow_lower", Direction.WEST),
                LocationConnection("boulder_garden", Direction.WEST),
                LocationConnection("boulder_garden", Direction.NORTH),
                LocationConnection("pika_rockpile", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 5,
            isSettlement = true
        ),
        
        Location(
            id = "pika_rockpile",
            name = "Pika Rockpile",
            description = LocationDescription.simple(
                "Countless small rocks create a treacherous maze where pikas—creatures nearly your own size—dart between crevices. They've cached dried grasses in rock hollows, creating fragrant 'haypiles' that smell wonderfully of summer even in autumn. High-pitched calls echo from all directions."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 1,
            connections = listOf(
                LocationConnection("marmot_colony", Direction.WEST),
                LocationConnection("dwarven_outpost", Direction.NORTH),
                LocationConnection("talus_field", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5
        ),
        
        Location(
            id = "crystal_seep",
            name = "Crystal Seep",
            description = LocationDescription.simple(
                "Mineral-rich water seeps from a crack in the mountainside, depositing brilliant crystals that sparkle like treasure. The tiny spring creates a vertical garden of moss and miniature ferns. The water tastes sharp and metallic, cold enough to make your beak ache."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 2,
            connections = listOf(
                LocationConnection("stone_stairway_natural", Direction.SOUTH),
                LocationConnection("eagles_rest_overlook", Direction.WEST),
                LocationConnection("boulder_garden", Direction.EAST),
                LocationConnection("lichen_falls", Direction.NORTH)
            ),
            encounterRate = 0.30,
            recommendedLevel = 5
        ),
        
        Location(
            id = "eagles_rest_overlook",
            name = "Eagle's Rest Overlook",
            description = LocationDescription.simple(
                "A broad, flat stone juts from the mountainside like a natural balcony. This is a favorite perching spot for eagles—the white wash staining the rock proves it. Standing here is terrifying and exhilarating: the world spreads below you in miniature, and the sky feels close enough to touch."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 2,
            connections = listOf(
                LocationConnection("switchback_trail_middle", Direction.SOUTH),
                LocationConnection("windswept_ridge", Direction.NORTH),
                LocationConnection("crystal_seep", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 6,
            lore = "Eagles use this overlook as a hunting perch. The bones of countless small animals litter the crevices—a sobering reminder of the food chain."
        ),
        
        Location(
            id = "talus_field",
            name = "Talus Field",
            description = LocationDescription.simple(
                "A vast slope of broken angular rocks, ranging from pebble-sized (your perspective) to house-sized (anyone's perspective). Each rock sits at a precarious angle, threatening to shift underfoot. Navigating this field requires constant attention—one wrong step could trigger a rockslide."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 5,
            gridY = 1,
            connections = listOf(
                LocationConnection("pika_rockpile", Direction.WEST),
                LocationConnection("mountain_juniper_grove", Direction.NORTH),
                LocationConnection("cliff_base_east", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5
        ),
        
        Location(
            id = "mountain_juniper_grove",
            name = "Mountain Juniper Grove",
            description = LocationDescription.simple(
                "Stunted junipers cling to the rocky soil, their twisted trunks and dense foliage creating a miniature forest for you. The trees' aromatic berries are the size of your head, and their scale-like needles form a fragrant carpet. These ancient, wind-sculpted trees are over a century old."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 5,
            gridY = 2,
            connections = listOf(
                LocationConnection("talus_field", Direction.SOUTH),
                LocationConnection("dwarven_outpost", Direction.WEST),
                LocationConnection("windbreak_wall", Direction.NORTH)
            ),
            encounterRate = 0.40,
            recommendedLevel = 6
        ),
        
        Location(
            id = "windswept_ridge",
            name = "Windswept Ridge",
            description = LocationDescription.simple(
                "A narrow spine of rock where the wind never stops. Gusts strong enough to bowl you over howl past constantly, carrying grit that stings exposed skin. The vegetation here hugs the ground in desperate mats, and you must do the same to avoid being blown off the mountain entirely."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 3,
            connections = listOf(
                LocationConnection("eagles_rest_overlook", Direction.SOUTH),
                LocationConnection("lichen_falls", Direction.EAST),
                LocationConnection("mountain_saddle", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 6
        ),
        
        Location(
            id = "lichen_falls",
            name = "Lichen Falls",
            description = LocationDescription.simple(
                "Water cascades down a moss-covered rock face in delicate curtains. For you, it's a waterfall of epic proportions, the spray creating perpetual rainbows. Brilliant orange and chartreuse lichens cover every surface, fed by the constant moisture. The roar of falling water is deafening at this scale."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 3,
            connections = listOf(
                LocationConnection("crystal_seep", Direction.SOUTH),
                LocationConnection("windswept_ridge", Direction.WEST),
                LocationConnection("cliff_face_lower", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 6
        ),
        
        Location(
            id = "cliff_face_lower",
            name = "Lower Cliff Face",
            description = LocationDescription.simple(
                "A sheer rock wall rises before you like an impossibly tall building. Cracks and ledges pattern the stone, each one a potential route upward for a determined climber. Pitons and rope remnants left by human climbers dangle from crevices—to you, they're massive metal spikes and cables thick as your body."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 2,
            connections = listOf(
                LocationConnection("boulder_garden", Direction.SOUTH),
                LocationConnection("lichen_falls", Direction.WEST),
                LocationConnection("dwarven_outpost", Direction.EAST),
                LocationConnection("cliff_ledge_network", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6
        ),

        // ==================== SUB-REGION 3B: Mid-Range Peaks (15 locations, levels 7-10) ====================
        // Grid: X: 1-5, Y: 3-5
        // Theme: Higher elevations, treeline, alpine zone, mountain goats, exposed peaks
        
        Location(
            id = "mountain_saddle",
            name = "Mountain Saddle",
            description = LocationDescription.simple(
                "A U-shaped depression between two peaks creates a natural pass. Snow lingers here even in summer, filling the saddle's low point with a brilliant white field. The wind funnels through this gap with focused intensity, carrying the scent of ice and stone from the high peaks above."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 4,
            connections = listOf(
                LocationConnection("windswept_ridge", Direction.SOUTH),
                LocationConnection("snowfield_permanent", Direction.NORTH),
                LocationConnection("goat_trail_high", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "cliff_ledge_network",
            name = "Cliff Ledge Network",
            description = LocationDescription.simple(
                "A series of narrow shelves and ledges zigzag up the cliff face, connected by precarious jumps and flutter-flights. Each ledge hosts its own microecosystem: a few tufts of grass, perhaps a tenacious flower, and the droppings of the raptors who use these ledges as feeding platforms."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 3,
            connections = listOf(
                LocationConnection("cliff_face_lower", Direction.SOUTH),
                LocationConnection("goat_trail_high", Direction.NORTH),
                LocationConnection("dwarven_outpost", Direction.EAST),
                LocationConnection("alpine_tundra_pocket", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "goat_trail_high",
            name = "High Goat Trail",
            description = LocationDescription.simple(
                "Mountain goats have carved a path along an impossibly steep slope. The trail is barely wider than your body, with a vertical drop on one side and a vertical rise on the other. Fresh goat droppings the size of your head mark the path—and serve as useful navigational aids."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 4,
            connections = listOf(
                LocationConnection("mountain_saddle", Direction.WEST),
                LocationConnection("cliff_ledge_network", Direction.SOUTH),
                LocationConnection("saltlick_springs", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "saltlick_springs",
            name = "Saltlick Springs",
            description = LocationDescription.simple(
                "Mineral-rich water bubbles from the mountainside, depositing salt and other minerals in colorful terraces. This natural salt lick attracts mountain goats, bighorn sheep, and other creatures from miles around. The ground is trampled to powder by countless hooves, and the air tastes distinctly salty."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 4,
            connections = listOf(
                LocationConnection("goat_trail_high", Direction.WEST),
                LocationConnection("alpine_tundra_pocket", Direction.SOUTH),
                LocationConnection("ram_skull_cairn", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 8,
            lore = "The minerals in this spring are essential to the diet of mountain ungulates. Some say the water has healing properties—though drinking water this mineral-heavy might cure you or kill you."
        ),
        
        Location(
            id = "alpine_tundra_pocket",
            name = "Alpine Tundra Pocket",
            description = LocationDescription.simple(
                "Above the treeline, a flat expanse of alpine meadow stretches out. Tiny flowers bloom in brilliant carpets—what would be ground-hugging for a human is a waist-high meadow for you. The growing season here is measured in weeks, and plants race to flower and seed before the snow returns."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 4,
            connections = listOf(
                LocationConnection("cliff_ledge_network", Direction.SOUTH),
                LocationConnection("saltlick_springs", Direction.NORTH),
                LocationConnection("dwarven_outpost", Direction.SOUTHEAST),
                LocationConnection("scree_chute", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 8,
            isSafeZone = true
        ),
        
        Location(
            id = "ram_skull_cairn",
            name = "Ram Skull Cairn",
            description = LocationDescription.simple(
                "A pile of stones topped by the massive skull of a bighorn ram marks this spot. The skull's curled horns are larger than your entire body, bleached white by sun and weather. This cairn serves as a landmark visible for miles, and locals whisper that it marks the grave of a legendary ram who ruled these peaks for two decades."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 4,
            connections = listOf(
                LocationConnection("saltlick_springs", Direction.WEST),
                LocationConnection("scree_chute", Direction.SOUTH),
                LocationConnection("windbreak_wall", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 9,
            lore = "The ram whose skull crowns this cairn was named 'Cloudhorn' by local shepherds. He sired hundreds of offspring and defended his territory from all challengers for twenty years before falling to a golden eagle."
        ),
        
        Location(
            id = "scree_chute",
            name = "Scree Chute",
            description = LocationDescription.simple(
                "A steep gully filled with loose rock plunges down the mountainside. This is a natural highway for rocks—gravity's express lane. The entire chute shifts and groans under its own weight, and crossing it requires quick, decisive movement before the whole thing slides out from under you."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 3,
            connections = listOf(
                LocationConnection("alpine_tundra_pocket", Direction.WEST),
                LocationConnection("ram_skull_cairn", Direction.NORTH),
                LocationConnection("windbreak_wall", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "windbreak_wall",
            name = "Windbreak Wall",
            description = LocationDescription.simple(
                "A natural stone ridge creates shelter from the prevailing winds. On the leeward side, snow accumulates in deep drifts, while the windward side is scoured clean. Krummholz—wind-stunted trees—huddle in the protected zone, their branches all pointing away from the wind like directional arrows."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 5,
            gridY = 3,
            connections = listOf(
                LocationConnection("mountain_juniper_grove", Direction.SOUTH),
                LocationConnection("cliff_base_east", Direction.SOUTH),
                LocationConnection("scree_chute", Direction.NORTH),
                LocationConnection("ram_skull_cairn", Direction.WEST),
                LocationConnection("cliff_base_east", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 8
        ),
        
        Location(
            id = "cliff_base_east",
            name = "Eastern Cliff Base",
            description = LocationDescription.simple(
                "The eastern face of the mountain rises in a series of dramatic cliffs and buttresses. Rockfall debris litters the base—house-sized boulders that crashed down from above. Each boulder creates shade and shelter, hosting unique communities of moss, lichen, and small creatures."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 2,
            connections = listOf(
                LocationConnection("talus_field", Direction.WEST),
                LocationConnection("windbreak_wall", Direction.WEST),
                LocationConnection("windbreak_wall", Direction.NORTH),
                LocationConnection("hanging_valley_approach", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "snowfield_permanent",
            name = "Permanent Snowfield",
            description = LocationDescription.withAllSeasons(
                spring = "Even in spring, this high-altitude snowfield remains frozen. Meltwater trickles along the surface in tiny rivulets, carving channels through the snow. The snow's surface is crusty and supportive—until suddenly your foot breaks through into a hidden air pocket.",
                summer = "The snowfield shrinks to its minimum extent but never fully melts. The exposed rock around its edges is polished smooth by centuries of ice movement. Patches of red 'watermelon snow'—algae that thrives in cold water—tint the surface pink.",
                autumn = "Fresh snow begins to accumulate atop last year's compacted base. The boundary between old ice and new snow is visible as a blue line in the banks. Early storms add inches daily, and soon this field will grow to cover the entire upper mountain.",
                winter = "The snowfield becomes indistinguishable from the rest of the mountain—everything is buried under deep powder. Only the shape of the terrain, the gentle bowl that traps snow, hints at the ancient ice beneath the fresh fall."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 5,
            connections = listOf(
                LocationConnection("mountain_saddle", Direction.SOUTH),
                LocationConnection("ice_cave_entrance", Direction.NORTH),
                LocationConnection("bergschrund_gap", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 9
        ),
        
        Location(
            id = "bergschrund_gap",
            name = "Bergschrund Gap",
            description = LocationDescription.simple(
                "A deep crevasse separates the permanent snowfield from the living rock of the mountain. This 'bergschrund' yawns like a blue-walled chasm, its depths filled with shadows and the tinkle of falling ice. Crossing requires a leap of faith—or a careful route around the edges."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 5,
            connections = listOf(
                LocationConnection("snowfield_permanent", Direction.WEST),
                LocationConnection("summit_approach", Direction.NORTH),
                LocationConnection("windblast_col", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "windblast_col",
            name = "Windblast Col",
            description = LocationDescription.simple(
                "A narrow pass where the wind reaches truly terrifying speeds. The constant blast has carved the rock into smooth, aerodynamic shapes. Nothing grows here; nothing can. The wind's roar is a constant presence, making communication impossible and thought difficult."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 5,
            connections = listOf(
                LocationConnection("bergschrund_gap", Direction.WEST),
                LocationConnection("summit_approach", Direction.NORTHWEST),
                LocationConnection("summit_approach", Direction.NORTH),
                LocationConnection("cornice_ridge", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 10
        ),
        
        Location(
            id = "cornice_ridge",
            name = "Cornice Ridge",
            description = LocationDescription.simple(
                "Wind-driven snow has built spectacular overhanging cornices along this ridgeline—frozen waves of snow that curl out over empty space. Walking on a cornice is deadly; they can collapse without warning. The ridge proper is knife-edge narrow, requiring you to carefully balance along its spine."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 5,
            connections = listOf(
                LocationConnection("windblast_col", Direction.WEST),
                LocationConnection("frostpeak", Direction.NORTH),
                LocationConnection("serrac_maze", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "serrac_maze",
            name = "Serrac Maze",
            description = LocationDescription.simple(
                "Towering blocks of ice—serracs—lean at crazy angles, creating a labyrinth of blue corridors and frozen chambers. These ice towers are house-sized and inherently unstable, constantly creaking and groaning as they shift. One might topple at any moment, making this maze as dangerous as it is beautiful."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 5,
            gridY = 5,
            connections = listOf(
                LocationConnection("cornice_ridge", Direction.WEST),
                LocationConnection("ice_bridge_suspended", Direction.NORTH),
                LocationConnection("hanging_valley_approach", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10
        ),
        
        Location(
            id = "hanging_valley_approach",
            name = "Hanging Valley Approach",
            description = LocationDescription.simple(
                "A glacially-carved valley hangs on the mountainside, its floor hundreds of feet above the slopes below. A waterfall cascades from the valley's lip, plunging into space in a free-fall that creates perpetual mist. Reaching the valley requires navigating the slick rocks beside this thundering curtain of water."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 3,
            connections = listOf(
                LocationConnection("cliff_base_east", Direction.WEST),
                LocationConnection("cliff_face_vertical", Direction.EAST),
                LocationConnection("serrac_maze", Direction.NORTH),
                LocationConnection("hidden_cirque", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 9
        ),

        // ==================== SUB-REGION 3C: High Peaks (15 locations, levels 10-15) ====================
        // Grid: X: 1-5, Y: 6-8
        // Theme: Summit regions, extreme altitude, permanent ice, minimal life, epic views
        
        Location(
            id = "summit_approach",
            name = "Summit Approach",
            description = LocationDescription.simple(
                "The final slope before the summit stretches upward at an exhausting angle. Every breath at this altitude comes hard; the air feels thin and insufficient. Ice and rock mingle in a treacherous surface where one wrong step could send you sliding hundreds of feet downslope."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 6,
            connections = listOf(
                LocationConnection("bergschrund_gap", Direction.SOUTH),
                LocationConnection("windblast_col", Direction.SOUTH),
                LocationConnection("ice_cave_entrance", Direction.WEST),
                LocationConnection("windblast_col", Direction.SOUTHEAST),
                LocationConnection("summit_mount_titan", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 11
        ),
        
        Location(
            id = "summit_mount_titan",
            name = "Summit of Mount Titan",
            description = LocationDescription.simple(
                "The highest point for miles in every direction. From here, you can see the entire world laid out below—grasslands, forests, distant desert shimmers, the glint of the sea. The wind never stops, the air is painfully thin, and the cold is absolute. Yet standing here, on top of the world, you feel simultaneously insignificant and mighty."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 7,
            connections = listOf(
                LocationConnection("summit_approach", Direction.SOUTH),
                LocationConnection("north_face_descent", Direction.NORTH),
                LocationConnection("summit_ice_field", Direction.EAST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 12,
            lore = "Humans call this peak 'Mount Titan,' though from your perspective, even a modest hill is titanic. The summit register—a waterproof box where climbers sign their names—is the size of a storage shed. Their triumph is your everyday reality."
        ),
        
        Location(
            id = "ice_cave_entrance",
            name = "Ice Cave Entrance",
            description = LocationDescription.simple(
                "A blue-walled tunnel leads into the heart of the glacier. Formed by meltwater carving through the ice, the cave's walls glow with ethereal light. The temperature drops noticeably as you enter, and the sound of dripping water echoes from unseen depths. This cave reshapes itself annually as the glacier flows."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 6,
            connections = listOf(
                LocationConnection("snowfield_permanent", Direction.SOUTH),
                LocationConnection("ravens_roost_peak", Direction.NORTH),
                LocationConnection("ice_cathedral", Direction.DOWN),
                LocationConnection("summit_approach", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 10
        ),
        
        Location(
            id = "ice_bridge_suspended",
            name = "Suspended Ice Bridge",
            description = LocationDescription.simple(
                "A natural arch of ice spans a deep crevasse, formed by wind-driven snow compacting over a gap. The bridge is translucent; you can see through to the blue depths below. It's thick enough to support your weight—probably—but crossing requires nerves of steel and absolute faith in frozen physics."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 5,
            gridY = 6,
            connections = listOf(
                LocationConnection("serrac_maze", Direction.SOUTH),
                LocationConnection("summit_ice_field", Direction.WEST),
                LocationConnection("eastern_precipice", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 11
        ),
        
        Location(
            id = "summit_ice_field",
            name = "Summit Ice Field",
            description = LocationDescription.simple(
                "A vast plateau of ice crowns the upper mountain. Wind-sculpted into fantastic shapes—sastrugi—the surface looks like a frozen ocean caught mid-wave. Each ice ridge and furrow is sized perfectly to obstruct a quail's progress, turning a flat field into an exhausting obstacle course."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 3,
            gridY = 7,
            connections = listOf(
                LocationConnection("summit_mount_titan", Direction.WEST),
                LocationConnection("knife_edge_traverse", Direction.EAST),
                LocationConnection("north_face_descent", Direction.NORTHWEST),
                LocationConnection("pinnacle_spire", Direction.EAST),
                LocationConnection("ice_bridge_suspended", Direction.EAST),
                LocationConnection("north_face_descent", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 12
        ),
        
        Location(
            id = "north_face_descent",
            name = "North Face Descent",
            description = LocationDescription.simple(
                "The mountain's north face plunges away in a series of ice cliffs and rock bands. This side sees no sun in winter, remaining perpetually frozen and shadowed. The descent route requires careful navigation between cliff bands, and one mistake means a very long fall."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 8,
            connections = listOf(
                LocationConnection("summit_mount_titan", Direction.SOUTH),
                LocationConnection("summit_ice_field", Direction.SOUTH),
                LocationConnection("summit_ice_field", Direction.SOUTHEAST),
                LocationConnection("ravens_roost_peak", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 13
        ),
        
        Location(
            id = "ravens_roost_peak",
            name = "Raven's Roost Peak",
            description = LocationDescription.simple(
                "A secondary summit just below the main peak serves as a gathering point for ravens. These intelligent birds have learned that climbers sometimes leave food scraps, and they wait here for opportunities. Their raucous calls echo across the peaks, and their presence adds an eerie vitality to the lifeless altitude."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 8,
            connections = listOf(
                LocationConnection("north_face_descent", Direction.EAST),
                LocationConnection("ice_cave_entrance", Direction.SOUTH),
                LocationConnection("snow_plume_ridge", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 12
        ),
        
        Location(
            id = "eastern_precipice",
            name = "Eastern Precipice",
            description = LocationDescription.simple(
                "The mountain's eastern edge ends in a sheer drop of staggering verticality. Standing at the edge and looking down induces immediate vertigo—the slope below is so far away it looks like a map. Updrafts surge along this cliff face, strong enough to support soaring eagles and lift a small quail off their feet."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 6,
            connections = listOf(
                LocationConnection("ice_bridge_suspended", Direction.WEST),
                LocationConnection("stream_source_springs", Direction.EAST),
                LocationConnection("hidden_cirque", Direction.SOUTH),
                LocationConnection("pinnacle_spire", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 11
        ),
        
        Location(
            id = "pinnacle_spire",
            name = "Pinnacle Spire",
            description = LocationDescription.simple(
                "A needle of rock juts from the mountainside, its summit barely wide enough for you to stand. Reaching this spire requires technical climbing—or brave flutter-flights between ledges. The view from the top is unmatched, a 360-degree panorama of mountains, valleys, and distant horizons."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 7,
            connections = listOf(
                LocationConnection("eastern_precipice", Direction.SOUTH),
                LocationConnection("cloud_bank_passage", Direction.EAST),
                LocationConnection("summit_ice_field", Direction.WEST),
                LocationConnection("knife_edge_traverse", Direction.NORTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 13
        ),
        
        Location(
            id = "hidden_cirque",
            name = "Hidden Cirque",
            description = LocationDescription.simple(
                "A bowl-shaped valley carved by ancient glaciers hides on the mountain's flank. Steep walls surround it on three sides, making it nearly invisible from below. A tiny alpine lake fills the cirque's floor, its water an impossible blue. This sheltered spot hosts rare alpine flowers found nowhere else."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 7,
            gridY = 4,
            connections = listOf(
                LocationConnection("hanging_valley_approach", Direction.WEST),
                LocationConnection("spray_zone_ledges", Direction.EAST),
                LocationConnection("eastern_precipice", Direction.NORTH),
                LocationConnection("waterfall_head", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 10,
            isSafeZone = true,
            lore = "This cirque remains hidden from casual observation. Local legends claim it's enchanted, that time moves differently here, and that the flowers growing in the alpine meadow can cure any ailment—if you can survive reaching them."
        ),
        
        Location(
            id = "snow_plume_ridge",
            name = "Snow Plume Ridge",
            description = LocationDescription.simple(
                "Wind tears snow from this exposed ridgeline in constant streaming plumes, creating a dramatic white banner visible for miles. Walking this ridge means fighting the wind for every step, snow pelting you from all directions. The exposure is total—fall to either side and you won't stop rolling for a very long time."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("ravens_roost_peak", Direction.EAST),
                LocationConnection("western_shoulder", Direction.SOUTH),
                LocationConnection("sunset_horn", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 13
        ),
        
        Location(
            id = "sunset_horn",
            name = "Sunset Horn",
            description = LocationDescription.simple(
                "A westward-facing peak catches the last rays of sun each evening, glowing gold and orange while the valleys below sink into shadow. This is a sacred spot to local mountain peoples, who make pilgrimages here to watch the sunset. Prayer flags flutter in the wind, each strip of cloth larger than your entire body."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -1,
            gridY = 8,
            connections = listOf(
                LocationConnection("snow_plume_ridge", Direction.EAST),
                LocationConnection("western_shoulder", Direction.SOUTHEAST),
                LocationConnection("meditation_cave", Direction.WEST),
                LocationConnection("western_shoulder", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 12,
            isSafeZone = true,
            lore = "Pilgrims have left offerings at this peak for generations: bells, carved stones, and prayer flags. The constant wind makes the bells chime in eerie harmonies, a music that never stops."
        ),
        
        Location(
            id = "meditation_cave",
            name = "Meditation Cave",
            description = LocationDescription.simple(
                "A natural rock shelter faces westward, offering protection from the elements and a perfect view of the sunset. The cave's walls are covered in ancient paintings and carvings—human handprints, animal figures, mysterious symbols. This has been a sacred site for millennia, and the air feels heavy with accumulated reverence."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -2,
            gridY = 8,
            connections = listOf(
                LocationConnection("sunset_horn", Direction.EAST),
                LocationConnection("monastery_ruins", Direction.SOUTH),
                LocationConnection("prayer_wheel_courtyard", Direction.SOUTH),
                LocationConnection("western_shoulder", Direction.SOUTH)
            ),
            encounterRate = 0.30,
            recommendedLevel = 11,
            isSafeZone = true,
            lore = "Hermits and mystics have meditated in this cave for centuries. Some claim to hear voices in the wind, receive visions in the dancing shadows, or achieve enlightenment in the thin mountain air."
        ),
        
        Location(
            id = "knife_edge_traverse",
            name = "Knife Edge Traverse",
            description = LocationDescription.simple(
                "The mountain's spine narrows to an impossibly thin ridge—literally knife-edge sharp in places. You must walk along the very crest, with vertical drops on both sides. One side plunges into shadow, the other is sun-blasted, creating a bizarre microclimate where you're simultaneously cold and hot."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 8,
            connections = listOf(
                LocationConnection("pinnacle_spire", Direction.SOUTH),
                LocationConnection("summit_ice_field", Direction.WEST),
                LocationConnection("thunderhead_col", Direction.NORTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 14
        ),
        
        Location(
            id = "thunderhead_col",
            name = "Thunderhead Col",
            description = LocationDescription.simple(
                "This high pass between peaks seems to attract lightning. Storms gather here with frightening speed, and the rock is scarred by countless lightning strikes. When a storm hits, the air crackles with electricity, making your feathers stand on end. Shelter is nonexistent—you're the tallest thing around."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 6,
            gridY = 9,
            connections = listOf(
                LocationConnection("knife_edge_traverse", Direction.SOUTH),
                LocationConnection("frostpeak", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 15,
            lore = "The constant lightning has fused the surface rock into glassy fulgurite tubes. Local legend claims that during storms, the spirits of the mountain speak in the thunder."
        ),

        // ==================== SUB-REGION 3D: Western Range (10 locations, levels 8-12) ====================
        // Grid: X: -3 to 0, Y: 4-7
        // Theme: Parallel mountain range, quieter than main peaks, hidden valleys, monastic sites
        
        Location(
            id = "western_shoulder",
            name = "Western Shoulder",
            description = LocationDescription.simple(
                "A broad, relatively gentle slope extends westward from the main peak. This 'shoulder' provides the easiest ascent route, though 'easy' is relative at this altitude. Alpine wildflowers bloom in sheltered pockets, their colors shockingly vivid against the gray and white landscape."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("snow_plume_ridge", Direction.NORTH),
                LocationConnection("sunset_horn", Direction.NORTH),
                LocationConnection("sunset_horn", Direction.NORTHWEST),
                LocationConnection("meditation_cave", Direction.NORTH),
                LocationConnection("monastery_ruins", Direction.WEST),
                LocationConnection("western_treeline", Direction.SOUTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 10
        ),
        
        Location(
            id = "monastery_ruins",
            name = "Ancient Monastery Ruins",
            description = LocationDescription.simple(
                "Stone walls rise from the mountainside, remnants of a monastery built centuries ago and long abandoned. For you, these ruins are a vast complex of chambers and corridors. Prayer wheels the size of barrels still spin in the wind, their creaking rotation a constant presence. The walls offer shelter from the weather—a rare luxury at this altitude."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -1,
            gridY = 7,
            connections = listOf(
                LocationConnection("western_shoulder", Direction.EAST),
                LocationConnection("meditation_cave", Direction.NORTH),
                LocationConnection("prayer_wheel_courtyard", Direction.WEST),
                LocationConnection("hermit_cave_cluster", Direction.SOUTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 11,
            isSettlement = true,
            isSafeZone = true,
            lore = "Monks once lived here in extreme asceticism, believing that altitude brought them closer to enlightenment. They abandoned the monastery during a particularly harsh winter—or so the story goes. Some claim the monks achieved transcendence and simply walked into the sky."
        ),
        
        Location(
            id = "prayer_wheel_courtyard",
            name = "Prayer Wheel Courtyard",
            description = LocationDescription.simple(
                "A plaza surrounded by stone walls hosts nine massive prayer wheels. Each wheel is taller than a human, inscribed with countless prayers and mantras. The wind keeps them spinning constantly, their bronze bodies gleaming where centuries of hands have touched them. The combined creaking creates an eerie symphony."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -2,
            gridY = 7,
            connections = listOf(
                LocationConnection("monastery_ruins", Direction.EAST),
                LocationConnection("meditation_cave", Direction.NORTH),
                LocationConnection("hanging_bell_tower", Direction.WEST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 11,
            lore = "According to tradition, spinning each wheel releases the prayers inscribed on it. By this measure, the wind has sent countless prayers into the universe. Some believe the constant prayers have sanctified this entire mountain."
        ),
        
        Location(
            id = "hanging_bell_tower",
            name = "Hanging Bell Tower",
            description = LocationDescription.simple(
                "A stone tower clings to a cliff edge, housing an enormous bronze bell. The bell no longer has a clapper, but the wind makes it vibrate, producing a low hum that resonates in your bones. Reaching the bell chamber requires navigating partially collapsed stairs—each step is a leap for you."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -3,
            gridY = 7,
            connections = listOf(
                LocationConnection("prayer_wheel_courtyard", Direction.EAST),
                LocationConnection("valley_of_echoes", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 12,
            lore = "The bell was cast from bronze donated by a thousand villages. Its tone, when struck properly, could supposedly be heard for fifty miles. Now the wind plays it like a instrument, creating music never intended by its makers."
        ),
        
        Location(
            id = "hermit_cave_cluster",
            name = "Hermit Cave Cluster",
            description = LocationDescription.simple(
                "Dozens of small caves pock the cliff face, each barely large enough for a human to sit cross-legged—spacious apartments from your perspective. Hermits once occupied these caves, spending years in solitary meditation. Personal items remain: wooden bowls, prayer beads, and blankets slowly decomposing in the dry air."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -1,
            gridY = 6,
            connections = listOf(
                LocationConnection("monastery_ruins", Direction.NORTH),
                LocationConnection("western_treeline", Direction.EAST),
                LocationConnection("valley_of_echoes", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 10
        ),
        
        Location(
            id = "valley_of_echoes",
            name = "Valley of Echoes",
            description = LocationDescription.simple(
                "A narrow valley between two ridges creates perfect acoustics. Every sound echoes multiple times, creating a confusing auditory landscape. A single chirp becomes a chorus, footsteps multiply into an imagined army. The monks used this valley to train in meditation, learning to distinguish reality from reflection."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("hermit_cave_cluster", Direction.EAST),
                LocationConnection("hanging_bell_tower", Direction.NORTH),
                LocationConnection("sound_stone_circle", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10
        ),
        
        Location(
            id = "sound_stone_circle",
            name = "Sound Stone Circle",
            description = LocationDescription.simple(
                "Thirteen standing stones arranged in a perfect circle, each stone carefully chosen for its resonant properties. Tapping different stones produces different tones, creating a lithophone that the monks used for ceremonies. The stones are sized for humans, making each one a pillar you must navigate around."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -2,
            gridY = 5,
            connections = listOf(
                LocationConnection("valley_of_echoes", Direction.NORTH),
                LocationConnection("western_treeline", Direction.NORTHEAST),
                LocationConnection("western_treeline", Direction.EAST),
                LocationConnection("sky_burial_platform", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 9,
            lore = "Each stone was transported from a different sacred site, chosen for its unique tone. Together, they can produce a scale spanning multiple octaves. On quiet days, the wind plays them in random melodies."
        ),
        
        Location(
            id = "western_treeline",
            name = "Western Treeline",
            description = LocationDescription.simple(
                "The last trees cling to existence at this altitude, stunted and twisted by constant wind. These 'krummholz' are centuries old but barely taller than you. They grow in thick mats, their branches all pointing away from the prevailing wind. Moving through them is like navigating a dense forest—though it's only waist-high to a human."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 0,
            gridY = 6,
            connections = listOf(
                LocationConnection("western_shoulder", Direction.NORTH),
                LocationConnection("sound_stone_circle", Direction.WEST),
                LocationConnection("hermit_cave_cluster", Direction.WEST),
                LocationConnection("sound_stone_circle", Direction.SOUTHWEST),
                LocationConnection("dwarven_outpost", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 8
        ),
        
        Location(
            id = "sky_burial_platform",
            name = "Sky Burial Platform",
            description = LocationDescription.simple(
                "A stone platform built on a cliff edge served as a site for sky burials—where the dead were left for vultures. Bones scattered across the platform testify to its purpose. Vultures still circle here, riding updrafts, their shadows passing over the platform in constant rotation. The view is spectacular and sobering."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -2,
            gridY = 4,
            connections = listOf(
                LocationConnection("sound_stone_circle", Direction.NORTH),
                LocationConnection("vulture_roost_crags", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9,
            lore = "In the monks' tradition, feeding one's body to vultures was the final act of generosity—returning nutrients to the ecosystem. The vultures here are fat and healthy, well-fed for generations."
        ),
        
        Location(
            id = "vulture_roost_crags",
            name = "Vulture Roost Crags",
            description = LocationDescription.simple(
                "Tall rock spires provide nesting sites for dozens of vultures. White streaks of droppings paint the cliffs, and the air reeks of carrion and bird. The vultures themselves are enormous—from your perspective, each one is the size of a small dragon. They eye you with calculating interest."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("sky_burial_platform", Direction.EAST),
                LocationConnection("western_summit_overlook", Direction.SOUTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 10
        ),

        // ==================== SUB-REGION 3E: Eastern Cliffs (10 locations, levels 9-13) ====================
        // Grid: X: 7-9, Y: 2-6
        // Theme: Dramatic vertical terrain, technical climbing, eagle nests, exposed faces
        
        Location(
            id = "waterfall_head",
            name = "Waterfall Head",
            description = LocationDescription.simple(
                "A mountain stream reaches the edge of a cliff and plunges into space, falling hundreds of feet to the valley below. Standing at the water's edge, you can peer over the precipice—the view induces instant vertigo. Spray creates perpetual rainbows, and the roar of falling water is overwhelming at this proximity."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 7,
            gridY = 4,
            connections = listOf(
                LocationConnection("hidden_cirque", Direction.WEST),
                LocationConnection("spray_zone_ledges", Direction.DOWN),
                LocationConnection("stream_source_springs", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 10
        ),
        
        Location(
            id = "spray_zone_ledges",
            name = "Spray Zone Ledges",
            description = LocationDescription.simple(
                "A series of narrow ledges descend beside the waterfall, constantly drenched in spray. Everything here is slick with moisture and vibrant with moss and ferns. The constant mist creates a microclimate where plants thrive despite the altitude. Each ledge is barely wide enough for you, requiring careful navigation."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 7,
            gridY = 3,
            connections = listOf(
                LocationConnection("waterfall_head", Direction.UP),
                LocationConnection("hidden_cirque", Direction.WEST),
                LocationConnection("eagles_eyrie", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10
        ),
        
        Location(
            id = "stream_source_springs",
            name = "Stream Source Springs",
            description = LocationDescription.simple(
                "Multiple springs bubble from the mountainside, their combined flow forming the stream that feeds the waterfall. The water is ice-cold and crystal clear, tasting of minerals and stone. Cushions of bright green moss surround each spring, creating islands of vibrant life in the rocky wasteland."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 7,
            gridY = 5,
            connections = listOf(
                LocationConnection("waterfall_head", Direction.SOUTH),
                LocationConnection("thermal_spiral_zone", Direction.EAST),
                LocationConnection("eastern_precipice", Direction.WEST),
                LocationConnection("crystal_cavern_entrance", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 11
        ),
        
        Location(
            id = "eagles_eyrie",
            name = "Eagle's Eyrie",
            description = LocationDescription.simple(
                "A massive nest of sticks and branches clings to a cliff ledge. Each stick is larger than you, and the nest as a whole could house several families. White down feathers and bone fragments litter the nest. The occupants—golden eagles—are thankfully absent, but they could return at any moment."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 3,
            connections = listOf(
                LocationConnection("spray_zone_ledges", Direction.WEST),
                LocationConnection("piton_ladder", Direction.EAST),
                LocationConnection("cliff_face_vertical", Direction.SOUTH),
                LocationConnection("thermal_spiral_zone", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11,
            lore = "Eagles have nested at this site for generations. The accumulation of bones beneath the nest tells the story of countless hunts—rabbits, marmots, and yes, even birds your size."
        ),
        
        Location(
            id = "cliff_face_vertical",
            name = "Vertical Cliff Face",
            description = LocationDescription.simple(
                "A wall of stone rises straight up for hundreds of feet. For technical climbers, this is a challenging route. For you, it's a landscape of ledges, cracks, and features to navigate. Each handhold for a human is a cave or corridor for you. Climbing hardware left by previous ascents provides convenient anchor points."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 2,
            connections = listOf(
                LocationConnection("eagles_eyrie", Direction.NORTH),
                LocationConnection("hanging_valley_approach", Direction.WEST),
                LocationConnection("piton_ladder", Direction.UP)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "piton_ladder",
            name = "Piton Ladder",
            description = LocationDescription.simple(
                "A vertical line of climbing pitons provides a route up an otherwise unclimbable section. Each piton—a metal spike driven into the rock—is the size of a fence post from your perspective. They're spaced evenly, creating a ladder you can navigate with determined flutter-jumps."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 3,
            connections = listOf(
                LocationConnection("cliff_face_vertical", Direction.DOWN),
                LocationConnection("eagles_eyrie", Direction.WEST),
                LocationConnection("thermal_spiral_zone", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 10
        ),
        
        Location(
            id = "thermal_spiral_zone",
            name = "Thermal Spiral Zone",
            description = LocationDescription.simple(
                "Rising air currents create a invisible spiral staircase of warm air. Eagles and hawks ride these thermals effortlessly, spiraling upward without flapping. You can feel the updraft lifting your feathers, offering the tantalizing possibility of soaring flight—if you're brave enough to launch yourself into empty air."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 4,
            connections = listOf(
                LocationConnection("piton_ladder", Direction.SOUTH),
                LocationConnection("eagles_eyrie", Direction.SOUTH),
                LocationConnection("stream_source_springs", Direction.WEST),
                LocationConnection("cloud_bank_passage", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 12
        ),
        
        Location(
            id = "crystal_cavern_entrance",
            name = "Crystal Cavern Entrance",
            description = LocationDescription.simple(
                "A cave mouth opens in the cliff face, its interior sparkling with quartz crystals. Sunlight refracts through the crystals, creating rainbow patterns on the walls. The cave extends deep into the mountain—a connection to the underground network that honeycombs these peaks."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 5,
            connections = listOf(
                LocationConnection("stream_source_springs", Direction.WEST),
                LocationConnection("quartz_cathedral", Direction.DOWN),
                LocationConnection("cloud_bank_passage", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 11
        ),
        
        Location(
            id = "cloud_bank_passage",
            name = "Cloud Bank Passage",
            description = LocationDescription.simple(
                "At this altitude, you're often above the clouds. Walking through a cloud is surreal—visibility drops to feet, moisture condenses on every surface, and the world becomes a gray void. Navigation requires faith in your sense of direction. When the clouds clear, the revelation of the landscape is breathtaking."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 8,
            gridY = 5,
            connections = listOf(
                LocationConnection("crystal_cavern_entrance", Direction.NORTH),
                LocationConnection("thermal_spiral_zone", Direction.SOUTH),
                LocationConnection("pinnacle_spire", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 12
        ),
        
        Location(
            id = "western_summit_overlook",
            name = "Western Summit Overlook",
            description = LocationDescription.simple(
                "A minor peak on the western range provides stunning views of the main mountain massif to the east. The entire mountain is laid out before you like a 3D map. This is a popular destination for less extreme climbers, and the summit register shows hundreds of proud signatures."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("vulture_roost_crags", Direction.NORTH),
                LocationConnection("stone_stairway_natural", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 9,
            lore = "From here, you can see the entire route to the main summit. Climbers often come here first as acclimatization before attempting the higher peaks."
        ),

        // ==================== SUB-REGION 3F: Underground Mountain Caverns (5 locations, levels 9-13) ====================
        // Grid: X: 1-4, Y: 4-6 (underground level)
        // Theme: Cave systems, underground streams, mineral formations, cave-adapted creatures
        
        Location(
            id = "ice_cathedral",
            name = "Ice Cathedral",
            description = LocationDescription.simple(
                "Deep inside the glacier, a vast chamber carved by meltwater creates a cathedral of blue ice. Columns of ice support the 'ceiling,' and frozen waterfalls decorate the walls like pipe organs. The light filtering through the ice creates an ethereal glow. This chamber is constantly changing as the glacier flows—what exists today may be gone next year."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 6,
            connections = listOf(
                LocationConnection("ice_cave_entrance", Direction.UP),
                LocationConnection("glacial_stream_tunnel", Direction.EAST),
                LocationConnection("frozen_grotto", Direction.SOUTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 11,
            lore = "The glacier's movement gives this chamber a lifespan measured in decades. Glaciologists have mapped it, but their maps become obsolete within years as the ice flows and reshapes the space."
        ),
        
        Location(
            id = "glacial_stream_tunnel",
            name = "Glacial Stream Tunnel",
            description = LocationDescription.simple(
                "Meltwater has carved a tunnel through solid ice, creating a blue corridor that pulses with the sound of running water. The stream flowing along the floor is shallow for you—ankle-deep—but incredibly cold. The tunnel changes elevation unpredictably, sometimes requiring you to swim through partially flooded sections."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 6,
            connections = listOf(
                LocationConnection("ice_cathedral", Direction.WEST),
                LocationConnection("quartz_cathedral", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 12
        ),
        
        Location(
            id = "frozen_grotto",
            name = "Frozen Grotto",
            description = LocationDescription.simple(
                "A side chamber off the main ice cave system contains formations of incredible delicacy—ice flowers, frozen bubbles, and rime ice that looks like frost-covered ferns. The air here is absolutely still and painfully cold. Every breath forms clouds of condensation, and frost forms on your feathers within minutes."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 5,
            connections = listOf(
                LocationConnection("ice_cathedral", Direction.NORTH),
                LocationConnection("moulin_shaft", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 11
        ),
        
        Location(
            id = "quartz_cathedral",
            name = "Quartz Cathedral",
            description = LocationDescription.simple(
                "A vast cavern whose walls are studded with enormous quartz crystals—each one taller than you and clear as glass. Light entering through cracks in the ceiling refracts through the crystals in spectacular displays. The floor is covered in crystal fragments, creating a crunching carpet that sparkles with each step."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 5,
            connections = listOf(
                LocationConnection("glacial_stream_tunnel", Direction.WEST),
                LocationConnection("crystal_cavern_entrance", Direction.UP),
                LocationConnection("deep_darkness_passages", Direction.SOUTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 12,
            lore = "Geologists estimate these crystals took millions of years to form. The largest specimens are six feet tall—ancient minerals that were old when the first humans walked the earth."
        ),
        
        Location(
            id = "moulin_shaft",
            name = "Moulin Shaft",
            description = LocationDescription.simple(
                "A vertical shaft drilled by meltwater plunges through the glacier like a well. Water spirals down the shaft in a constant cascade, the roar echoing from unseen depths. Reaching the shaft's bottom reveals a connection to deeper cave systems beneath the mountain. The descent is treacherous—slick ice walls and constant spray."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 5,
            connections = listOf(
                LocationConnection("frozen_grotto", Direction.WEST),
                LocationConnection("underground_lake_cavern", Direction.DOWN),
                LocationConnection("deep_darkness_passages", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 13
        ),
        
        Location(
            id = "underground_lake_cavern",
            name = "Underground Lake Cavern",
            description = LocationDescription.simple(
                "A vast chamber deep beneath the mountain contains a lake of perfectly still water. The water is so clear you can't judge its depth—it could be inches or fathoms. Blind cave fish drift in the darkness, having never seen sunlight. Stalactites hang from the ceiling, their tips nearly touching stalagmites rising from below."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 2,
            gridY = 4,
            connections = listOf(
                LocationConnection("moulin_shaft", Direction.UP),
                LocationConnection("deep_darkness_passages", Direction.EAST),
                LocationConnection("dwarven_deep_mines", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 13,
            lore = "This lake has no surface outlet—water only leaves through cracks in the rock. The lake level has remained constant for millennia, an underground reservoir fed by glacial melt from above."
        ),
        
        Location(
            id = "deep_darkness_passages",
            name = "Deep Darkness Passages",
            description = LocationDescription.simple(
                "Tunnels wind through the mountain's heart in total darkness. No light has ever reached these passages naturally. The walls are warm—geothermal heat from the earth's depths makes the air humid and close. Strange sounds echo from the darkness: dripping water, shifting rocks, and perhaps something else moving in the black."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 4,
            connections = listOf(
                LocationConnection("quartz_cathedral", Direction.NORTH),
                LocationConnection("moulin_shaft", Direction.WEST),
                LocationConnection("underground_lake_cavern", Direction.WEST),
                LocationConnection("lava_tube_entrance", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 13
        ),
        
        Location(
            id = "dwarven_deep_mines",
            name = "Dwarven Deep Mines",
            description = LocationDescription.simple(
                "Ancient mining tunnels branch from the natural caves. These were carved by dwarves centuries ago, searching for precious metals and gems. Tool marks still pattern the walls, and rusty mine carts sit on narrow-gauge tracks. For you, these carts are the size of small buildings—convenient shelter or navigation landmarks."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 1,
            gridY = 4,
            connections = listOf(
                LocationConnection("underground_lake_cavern", Direction.EAST),
                LocationConnection("dwarven_outpost", Direction.UP)
            ),
            encounterRate = 0.60,
            recommendedLevel = 12,
            isSettlement = true,
            lore = "The dwarves abandoned these mines long ago, having extracted all the easily accessible ore. But local legends claim they delved too deep and found something they shouldn't have—something that made them flee in terror."
        ),
        
        Location(
            id = "lava_tube_entrance",
            name = "Ancient Lava Tube Entrance",
            description = LocationDescription.simple(
                "A perfectly circular tunnel carved by ancient lava flow connects to the mountain cave system. The walls are smooth, frozen mid-flow millions of years ago. This tube descends toward the volcanic regions far below, offering a passage between mountain and fire. The air here is noticeably warmer, carrying the faint scent of sulfur."
            ),
            biome = BiomeType.MOUNTAIN,
            gridX = 4,
            gridY = 3,
            connections = listOf(
                LocationConnection("deep_darkness_passages", Direction.NORTH),
                LocationConnection("dwarven_outpost", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 12,
            lore = "This lava tube is a remnant of when these mountains were volcanically active, millions of years before any quail or human walked the earth. It serves as a reminder that even mountains are temporary features in geological time."
        )
    )
}
