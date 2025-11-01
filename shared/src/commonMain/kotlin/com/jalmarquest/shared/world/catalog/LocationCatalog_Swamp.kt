package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * SWAMP region catalog - 55 new locations expanding the wetland areas
 * Sub-regions: Outer Marshlands (5A), Deep Swamp (5B), Witch's Domain (5C),
 *              Sunken Ruins Zone (5D), Mangrove Labyrinths (5E)
 * Connects to existing locations: mire_maw, boglanter, witch_hut, rotten_hollow, sunken_temple
 */
internal val SWAMP_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 5A: Outer Marshlands (15 locations, levels 7-9) ====================
        // Grid: X: -4 to -2, Y: 3 to 5
        // Theme: Shallow water, cattails, frog chorus, transition from forest to swamp
        
        Location(
            id = "cattail_thicket",
            name = "Cattail Thicket",
            description = LocationDescription.simple(
                "Dense stands of cattails rise like a forest of brown velvet torches. Each cattail is taller than you many times over, their fuzzy seedheads towering overhead. The thicket is nearly impassable—wading through the shallow water while navigating the dense stems requires constant effort. Red-winged blackbirds nest here in massive numbers, their calls a constant soundtrack."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("mire_maw", Direction.EAST),
                LocationConnection("mosquito_cloud", Direction.EAST),
                LocationConnection("animated_scarecrow_field", Direction.NORTH),
                LocationConnection("cypress_cathedral", Direction.SOUTH),
                LocationConnection("boglanter", Direction.SOUTH),
                LocationConnection("frog_croaking_pond", Direction.WEST),
                LocationConnection("reed_maze", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "frog_croaking_pond",
            name = "Frog Croaking Pond",
            description = LocationDescription.withAllSeasons(
                spring = "The pond explodes with life as thousands of frogs emerge from winter dormancy. The croaking is deafening—a cacophony that drowns out all other sounds. Each frog is your size or larger, their inflated throat pouches ridiculous and alarming. They pay you little attention, focused entirely on mating. Frog eggs fill the water in gelatinous masses.",
                summer = "Summer heat makes the pond sluggish and rank. Algae blooms turn the water pea-soup green. The frogs are quieter now, hunting insects during twilight hours. Mosquitoes and dragonflies fill the air in vast swarms. The humidity is oppressive, making every breath feel like drinking.",
                autumn = "Autumn brings a second, smaller frog chorus as juveniles mature. The water cools, becoming clearer. Migrating birds stop here to feast on frogs, creating a dangerous time for all pond residents. Leaves falling into the water create floating islands that shift with the current.",
                winter = "Winter silences the frogs—they've burrowed into mud to hibernate. The pond may freeze on cold nights, though the ice is thin and treacherous. A few hardy frogs remain active, visible through clear ice. The quiet is eerie after months of croaking."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("cattail_thicket", Direction.EAST),
                LocationConnection("heron_hunting_shallows", Direction.SOUTH),
                LocationConnection("boglanter", Direction.SOUTHEAST),
                LocationConnection("quickmud_trap", Direction.NORTH),
                LocationConnection("swamp_willow_weep", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "quickmud_trap",
            name = "Quickmud Trap",
            description = LocationDescription.simple(
                "This section of marsh looks solid but hides deadly quickmud beneath a deceptive crust. Step on the wrong spot and you sink—fast. The mud has a liquid quality, flowing around anything that penetrates the surface. Struggling makes it worse. Bones visible in the mud testify to animals that failed to escape. Navigation requires careful testing of each step and knowing when to retreat."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 5,
            connections = listOf(
                LocationConnection("frog_croaking_pond", Direction.SOUTH),
                LocationConnection("corpse_flower_glade", Direction.NORTHWEST),
                LocationConnection("reed_maze", Direction.EAST),
                LocationConnection("methane_bubble_bog", Direction.NORTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 8,
            lore = "Quickmud is clay-rich soil saturated with water, creating a non-Newtonian fluid. It's denser than water but less dense than most animals, meaning you'll sink but won't go under completely—unless you panic and struggle, which breaks the surface tension."
        ),
        
        Location(
            id = "reed_maze",
            name = "Reed Maze",
            description = LocationDescription.simple(
                "Tall reeds grow in confusing patterns, creating a natural maze. Each reed is bamboo-like, tall and straight, growing so densely that visibility extends only a few body-lengths. The water between reeds varies from ankle to chest-deep (for you, that's very deep). Getting lost is easy; the reeds all look identical, and the croaking of frogs echoes confusingly. Patience and careful navigation are required."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("cattail_thicket", Direction.SOUTH),
                LocationConnection("turtle_basking_log", Direction.EAST),
                LocationConnection("quickmud_trap", Direction.WEST),
                LocationConnection("mire_maw", Direction.EAST),
                LocationConnection("stagnant_channel", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "mosquito_cloud",
            name = "Mosquito Cloud",
            description = LocationDescription.simple(
                "A permanent cloud of mosquitoes hovers over this stagnant water. The insects are your size—terrifying bloodsuckers with needle proboscises. The cloud is so dense it obscures vision. The whining buzz is maddening. Moving through requires accepting that you'll be bitten repeatedly. The mosquitoes are attracted to carbon dioxide and warmth, making hiding impossible."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 4,
            connections = listOf(
                LocationConnection("cattail_thicket", Direction.WEST),
                LocationConnection("turtle_basking_log", Direction.NORTH),
                LocationConnection("blood_moon_altar", Direction.NORTH),
                LocationConnection("mire_maw", Direction.SOUTH),
                LocationConnection("rotten_hollow", Direction.NORTH),
                LocationConnection("dragonfly_hunting_ground", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "dragonfly_hunting_ground",
            name = "Dragonfly Hunting Ground",
            description = LocationDescription.simple(
                "Massive dragonflies patrol this area, hunting mosquitoes and other insects. Each dragonfly is impressive—from your perspective, they're the size of small birds, aerial predators of terrifying efficiency. They hover, dart, and snatch prey from the air with perfect precision. Fortunately, you're too large to be prey, but watching them hunt is both beautiful and unsettling."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 4,
            connections = listOf(
                LocationConnection("mosquito_cloud", Direction.WEST),
                LocationConnection("salamander_spawning_pool", Direction.SOUTH),
                LocationConnection("beaver_dam_ruins", Direction.NORTH),
                LocationConnection("rotten_hollow", Direction.NORTH),
                LocationConnection("thorn_brake", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "turtle_basking_log",
            name = "Turtle Basking Log",
            description = LocationDescription.simple(
                "A massive fallen log provides a highway across deep water. The log is completely covered with basking turtles—dozens of them, ranging from palm-sized to dinner-plate-sized (both enormous from your perspective). The turtles ignore you unless you approach too close, then they plunge into the water with alarming splashes. The log is slick with algae, making crossing treacherous."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 5,
            connections = listOf(
                LocationConnection("reed_maze", Direction.WEST),
                LocationConnection("duckweed_carpet", Direction.NORTH),
                LocationConnection("mosquito_cloud", Direction.SOUTH),
                LocationConnection("stagnant_channel", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "stagnant_channel",
            name = "Stagnant Channel",
            description = LocationDescription.simple(
                "A waterway that barely flows, its surface covered in duckweed and algae. The water beneath is dark and mysterious, hiding unknown depths. Gas bubbles rise occasionally, releasing foul-smelling methane. The channel is deep enough that swimming might be required—but the thought of what might lurk beneath the opaque surface is terrifying."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 6,
            connections = listOf(
                LocationConnection("reed_maze", Direction.SOUTH),
                LocationConnection("duckweed_carpet", Direction.EAST),
                LocationConnection("turtle_basking_log", Direction.SOUTH),
                LocationConnection("rotten_hollow", Direction.EAST),
                LocationConnection("leech_pool", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "heron_hunting_shallows",
            name = "Heron Hunting Shallows",
            description = LocationDescription.simple(
                "Shallow water attracts great blue herons—gigantic wading birds from your perspective. Each heron stands motionless for minutes, then strikes with lightning speed, its spear-like beak impaling fish or frogs. To you, these herons are titans, their legs like tree trunks, their beaks like spears. They might mistake you for prey. Stealth and caution are essential."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 3,
            connections = listOf(
                LocationConnection("frog_croaking_pond", Direction.NORTH),
                LocationConnection("water_lily_field", Direction.EAST),
                LocationConnection("bullfrog_territory", Direction.EAST),
                LocationConnection("spell_component_bog", Direction.WEST),
                LocationConnection("snapping_turtle_den", Direction.SOUTH),
                LocationConnection("swamp_willow_weep", Direction.WEST),
                LocationConnection("boglanter", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 8
        ),
        
        Location(
            id = "water_lily_field",
            name = "Water Lily Field",
            description = LocationDescription.simple(
                "Enormous water lily pads float on the surface, each one large enough for you to stand on. The lilies are in various stages of bloom—some bearing white or pink flowers the size of your entire body. The pads are surprisingly sturdy, but wet and slippery. Hopping from pad to pad is possible but risky. Beneath the lilies, the water is deep and dark."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 3,
            connections = listOf(
                LocationConnection("mire_maw", Direction.NORTH),
                LocationConnection("salamander_spawning_pool", Direction.EAST),
                LocationConnection("bullfrog_territory", Direction.WEST),
                LocationConnection("heron_hunting_shallows", Direction.WEST),
                LocationConnection("thorn_brake", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "bullfrog_territory",
            name = "Bullfrog Territory",
            description = LocationDescription.simple(
                "This pond section is claimed by massive bullfrogs—each one nearly as large as you. Their deep, resonant croaks vibrate through the ground. The dominant male is particularly aggressive, defending his territory from all intruders. Fighting a bullfrog your own size is a real possibility here. Their powerful legs and sticky tongues make them formidable opponents."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("boglanter", Direction.NORTH),
                LocationConnection("water_lily_field", Direction.EAST),
                LocationConnection("heron_hunting_shallows", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "snapping_turtle_den",
            name = "Snapping Turtle Den",
            description = LocationDescription.simple(
                "An ancient snapping turtle has claimed this deep pool. The turtle is ancient and huge—its shell could serve as a shield for a human. Its beak can snap through bone. Fortunately, it's mostly sluggish, lying in wait for prey. The den is littered with bones and shells of the turtle's past meals. Approach with extreme caution, or better yet, go around."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 2,
            connections = listOf(
                LocationConnection("heron_hunting_shallows", Direction.NORTH),
                LocationConnection("spell_component_bog", Direction.EAST),
                LocationConnection("root_tangle", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 9,
            lore = "Snapping turtles can live over 100 years. This one has survived countless predators, outlasted droughts and floods, and grown to impressive size. It's a living fossil, a reminder of when reptiles ruled the earth."
        ),
        
        Location(
            id = "duckweed_carpet",
            name = "Duckweed Carpet",
            description = LocationDescription.simple(
                "The water surface is completely covered by duckweed—tiny floating plants that create a solid-looking green carpet. Walking on it is impossible; you sink through immediately. But the duckweed is so dense it obscures the water beneath, making it hard to judge depth. Some areas are shallow, others deep. Navigating requires faith and careful probing."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("stagnant_channel", Direction.WEST),
                LocationConnection("strangler_fig_grove", Direction.NORTH),
                LocationConnection("turtle_basking_log", Direction.SOUTH),
                LocationConnection("rotten_hollow", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "beaver_dam_ruins",
            name = "Beaver Dam Ruins",
            description = LocationDescription.simple(
                "An abandoned beaver dam creates a partial barrier across the swamp. The dam is massive—a complex structure of logs, sticks, and mud. For you, it's like navigating a jumbled wooden fortress. The beavers are long gone, but their engineering remains impressive. Water trickles through gaps in the dam, creating miniature waterfalls. Crossing requires climbing over and through the debris."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 5,
            connections = listOf(
                LocationConnection("dragonfly_hunting_ground", Direction.SOUTH),
                LocationConnection("rotten_hollow", Direction.WEST),
                LocationConnection("fungal_bloom_island", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "salamander_spawning_pool",
            name = "Salamander Spawning Pool",
            description = LocationDescription.simple(
                "A vernal pool filled with salamanders in various life stages. Adult salamanders your size or larger patrol the shallows. Their larvae—aquatic juveniles with external gills—swarm in the water. The pool will dry up eventually, forcing the salamanders to move or perish. The temporary nature creates urgency in the ecosystem, a race against evaporation."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 3,
            connections = listOf(
                LocationConnection("dragonfly_hunting_ground", Direction.NORTH),
                LocationConnection("water_lily_field", Direction.WEST),
                LocationConnection("thorn_brake", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),

        // ==================== SUB-REGION 5B: Deep Swamp (15 locations, levels 8-11) ====================
        // Grid: X: -5 to -2, Y: 4 to 7
        // Theme: Stagnant water, decay, dangerous gases, difficult navigation
        
        Location(
            id = "methane_bubble_bog",
            name = "Methane Bubble Bog",
            description = LocationDescription.simple(
                "Decomposing organic matter produces methane gas that bubbles up constantly. The bubbles range from small and frequent to large and explosive. The gas is flammable—any spark could ignite a devastating fire. The smell is horrible, like rotten eggs amplified. Breathing the concentrated gas can be toxic. This bog is beautiful in a horrifying way, deadly and mesmerizing."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 6,
            connections = listOf(
                LocationConnection("quickmud_trap", Direction.SOUTH),
                LocationConnection("bioluminescent_bacteria_pool", Direction.NORTH),
                LocationConnection("swamp_pitcher_plant_colony", Direction.WEST),
                LocationConnection("leech_pool", Direction.EAST),
                LocationConnection("corpse_flower_glade", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 9,
            lore = "Methane bubbles in swamps create 'swamp gas' or 'will-o'-the-wisps' when they spontaneously ignite. Superstitious travelers once believed these lights were spirits. The truth—combusting swamp farts—is somehow more disturbing."
        ),
        
        Location(
            id = "corpse_flower_glade",
            name = "Corpse Flower Glade",
            description = LocationDescription.simple(
                "Rare corpse flowers bloom here, their massive flowers reeking of rotting meat. The smell is designed to attract carrion flies for pollination, and it works horrifyingly well. The stench is overpowering, capable of inducing nausea. But the flowers are spectacular—dark red and textured like meat, enormous from your perspective. Flies swarm so thickly they block light."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 6,
            connections = listOf(
                LocationConnection("methane_bubble_bog", Direction.EAST),
                LocationConnection("water_moccasin_nest", Direction.SOUTH),
                LocationConnection("quickmud_trap", Direction.SOUTHEAST),
                LocationConnection("swamp_pitcher_plant_colony", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9
        ),
        
        Location(
            id = "leech_pool",
            name = "Leech Pool",
            description = LocationDescription.simple(
                "The water writhes with leeches—bloodsucking worms ranging from finger-length to forearm-length (both terrifying scales from your perspective). Crossing this pool means accepting that leeches will attach. They're surprisingly fast swimmers, homing in on warmth and movement. The leeches inject an anticoagulant, making wounds bleed freely. This is one of the swamp's most dreaded locations."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 7,
            connections = listOf(
                LocationConnection("stagnant_channel", Direction.SOUTH),
                LocationConnection("water_moccasin_nest", Direction.WEST),
                LocationConnection("strangler_fig_grove", Direction.EAST),
                LocationConnection("methane_bubble_bog", Direction.WEST),
                LocationConnection("bloodsucking_vine_tangle", Direction.NORTH),
                LocationConnection("sunken_temple", Direction.EAST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 10
        ),
        
        Location(
            id = "bloodsucking_vine_tangle",
            name = "Bloodsucking Vine Tangle",
            description = LocationDescription.simple(
                "Parasitic vines hang from cypress trees, covered in tiny thorns that pierce skin and draw blood. The vines are semi-motile, reaching toward warmth. Getting tangled means being slowly exsanguinated by dozens of tiny wounds. The vines are beautiful in a horrific way—red from the blood they've absorbed, glistening with moisture. Navigating requires constant alertness and quick reflexes."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 8,
            connections = listOf(
                LocationConnection("leech_pool", Direction.SOUTH),
                LocationConnection("bog_iron_deposit", Direction.EAST),
                LocationConnection("mist_shrouded_mere", Direction.WEST),
                LocationConnection("idol_chamber", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11
        ),
        
        Location(
            id = "fungal_bloom_island",
            name = "Fungal Bloom Island",
            description = LocationDescription.simple(
                "A small island covered entirely in fungi—mushrooms, brackets, slime molds, and stranger things. The fungi are bizarrely colored: electric blues, neon oranges, toxic greens. Many are bioluminescent, glowing in the dim swamp light. Some fungi release spores that cause hallucinations. Others are lethally poisonous. The island is beautiful, alien, and extremely dangerous."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 6,
            connections = listOf(
                LocationConnection("beaver_dam_ruins", Direction.SOUTH),
                LocationConnection("ancient_aqueduct", Direction.WEST),
                LocationConnection("rotten_hollow", Direction.WEST),
                LocationConnection("carnivorous_plant_garden", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9,
            lore = "Some swamp fungi are hallucinogenic, used in shamanic rituals by indigenous peoples. Others are deadly poisonous, killing within hours. Distinguishing between them requires expertise most don't have."
        ),
        
        Location(
            id = "carnivorous_plant_garden",
            name = "Carnivorous Plant Garden",
            description = LocationDescription.simple(
                "Pitcher plants, sundews, and Venus flytraps grow in dense clusters. Each plant is your size or larger, their traps capable of capturing prey your scale. Pitcher plants are deep wells filled with digestive enzymes. Sundews glisten with sticky droplets that snare anything touching them. Venus flytraps snap shut with alarming speed. This garden is beautiful, deadly, and actively hunting."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 7,
            connections = listOf(
                LocationConnection("fungal_bloom_island", Direction.SOUTH),
                LocationConnection("strangler_fig_grove", Direction.WEST),
                LocationConnection("mosaic_courtyard", Direction.EAST),
                LocationConnection("statue_garden", Direction.EAST),
                LocationConnection("collapsed_library", Direction.SOUTH),
                LocationConnection("sunken_temple", Direction.WEST),
                LocationConnection("drowned_plaza", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 10,
            lore = "Carnivorous plants evolved in nutrient-poor swamps, supplementing scarce minerals by consuming insects and small animals. From your perspective, you're definitely in the 'small animal' category."
        ),
        
        Location(
            id = "alligator_slide",
            name = "Alligator Slide",
            description = LocationDescription.simple(
                "A muddy bank worn smooth by alligators entering and exiting the water. The slide is like a slick highway, impossible to climb in reverse. At the bottom, the water is deep and dark—alligator territory. Massive reptiles bask on nearby banks, their eyes watching constantly. Each alligator is a dragon from your perspective, armored and deadly. Absolute stealth or absolute speed is required here."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 4,
            connections = listOf(
                LocationConnection("swamp_willow_weep", Direction.EAST),
                LocationConnection("swamp_willow_weep", Direction.SOUTH),
                LocationConnection("hex_circle", Direction.NORTH),
                LocationConnection("poison_garden", Direction.NORTH),
                LocationConnection("swamp_pitcher_plant_colony", Direction.NORTH),
                LocationConnection("root_tangle", Direction.SOUTH)
            ),
            encounterRate = 0.95,
            recommendedLevel = 11
        ),
        
        Location(
            id = "swamp_pitcher_plant_colony",
            name = "Swamp Pitcher Plant Colony",
            description = LocationDescription.simple(
                "Giant pitcher plants grow in clusters, their red and green tubes rising like chimneys. Each pitcher is deep enough to swallow you whole. The rims are slippery, designed to make escape impossible. Inside, digestive enzymes slowly dissolve trapped prey. Bones and insect shells litter the bottom of each pitcher. The plants are passive hunters, patient and deadly."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 5,
            connections = listOf(
                LocationConnection("corpse_flower_glade", Direction.NORTH),
                LocationConnection("water_moccasin_nest", Direction.NORTH),
                LocationConnection("alligator_slide", Direction.SOUTH),
                LocationConnection("methane_bubble_bog", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 9
        ),
        
        Location(
            id = "strangler_fig_grove",
            name = "Strangler Fig Grove",
            description = LocationDescription.simple(
                "Fig vines have wrapped around host trees, slowly strangling them. The figs create lattices of aerial roots, hollow tubes where trees once stood. The grove is a necropolis—trees killed and replaced by their parasites. The figs bear fruit attracting wildlife, creating a strange oasis of life amid death. Navigating the hollow roots is like exploring a natural cathedral."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 7,
            connections = listOf(
                LocationConnection("duckweed_carpet", Direction.SOUTH),
                LocationConnection("throne_room_flooded", Direction.NORTH),
                LocationConnection("cypress_cathedral", Direction.SOUTH),
                LocationConnection("leech_pool", Direction.WEST),
                LocationConnection("sunken_temple", Direction.NORTH),
                LocationConnection("carnivorous_plant_garden", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "mist_shrouded_mere",
            name = "Mist-Shrouded Mere",
            description = LocationDescription.simple(
                "Thick fog perpetually blankets this deep pool. Visibility extends only a few body-lengths. Sounds are muffled and distorted. The mist creates a disorienting environment where direction becomes uncertain. Things move in the fog—splashes, rustling, shapes that disappear when approached. Whether these are real threats or imagination fueled by poor visibility is impossible to determine."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 8,
            connections = listOf(
                LocationConnection("bloodsucking_vine_tangle", Direction.EAST),
                LocationConnection("bioluminescent_bacteria_pool", Direction.SOUTH),
                LocationConnection("peat_bog_deep", Direction.WEST),
                LocationConnection("voodoo_doll_grove", Direction.NORTH),
                LocationConnection("witch_hut", Direction.SOUTH),
                LocationConnection("hex_circle", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "bog_iron_deposit",
            name = "Bog Iron Deposit",
            description = LocationDescription.simple(
                "Iron-rich water has deposited ferrous minerals on plant roots and rocks, creating orange and red stains. The bog iron forms in lumps and sheets that ancient peoples mined for metalworking. The water here tastes metallic and stains everything it touches rust-colored. The deposits create natural sculptures, iron-coated branches and roots that look like alien art."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 8,
            connections = listOf(
                LocationConnection("bloodsucking_vine_tangle", Direction.WEST),
                LocationConnection("sunken_temple", Direction.SOUTH),
                LocationConnection("idol_chamber", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 10,
            lore = "Bog iron was humanity's first accessible iron source. The iron forms when bacteria oxidize dissolved iron in oxygen-poor swamp water. Ancient smiths harvested bog iron to create tools and weapons."
        ),
        
        Location(
            id = "water_moccasin_nest",
            name = "Water Moccasin Nest",
            description = LocationDescription.simple(
                "Venomous water moccasins congregate here in horrifying numbers. The snakes are aggressive, defending their territory from all intruders. Each snake is longer than you are tall, thick-bodied and muscular. Their venom is hemotoxic, destroying blood and tissue. The snakes swim with deadly grace, their heads above water, watching for threats or prey. This is their domain, and you are an intruder."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 7,
            connections = listOf(
                LocationConnection("swamp_pitcher_plant_colony", Direction.SOUTH),
                LocationConnection("bioluminescent_bacteria_pool", Direction.EAST),
                LocationConnection("peat_bog_deep", Direction.NORTH),
                LocationConnection("corpse_flower_glade", Direction.NORTH),
                LocationConnection("leech_pool", Direction.EAST)
            ),
            encounterRate = 0.95,
            recommendedLevel = 11
        ),
        
        Location(
            id = "bioluminescent_bacteria_pool",
            name = "Bioluminescent Bacteria Pool",
            description = LocationDescription.simple(
                "At night, this pool glows with eerie blue-green light—bioluminescent bacteria activated by disturbance. Every movement creates trails of light, every splash a burst of illumination. The effect is magical, transforming the hostile swamp into an otherworldly fairyland. But the light attracts predators, making stealth impossible. Beauty and danger are inseparable here."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 7,
            connections = listOf(
                LocationConnection("methane_bubble_bog", Direction.SOUTH),
                LocationConnection("water_moccasin_nest", Direction.WEST),
                LocationConnection("mist_shrouded_mere", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9,
            lore = "Bioluminescent bacteria produce light through chemical reactions, advertising their presence to fish that eat them and spread them to new locations. The bacteria glow brighter when disturbed, a defense mechanism that attracts predators to their disturbers."
        ),
        
        Location(
            id = "peat_bog_deep",
            name = "Deep Peat Bog",
            description = LocationDescription.simple(
                "Compressed plant matter has formed thick peat layers—partially decayed vegetation that feels spongy underfoot. The peat is acidic and oxygen-poor, preserving anything that falls into it. Bodies buried here don't decay normally; they become 'bog bodies,' mummified by the environment. Walking on peat is exhausting; each step sinks deep, requiring constant effort to extract your feet."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 8,
            connections = listOf(
                LocationConnection("water_moccasin_nest", Direction.SOUTH),
                LocationConnection("mist_shrouded_mere", Direction.EAST),
                LocationConnection("toadstool_ring_cursed", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10,
            lore = "Peat bogs preserve organic material for millennia. 'Bog bodies' recovered from European peat bogs are so well-preserved that archaeologists can determine their last meals, observe their clothing, and even read facial expressions frozen 2,000 years ago."
        ),
        
        Location(
            id = "cypress_cathedral",
            name = "Cypress Cathedral",
            description = LocationDescription.simple(
                "Ancient bald cypress trees rise from deep water, their massive trunks creating a natural cathedral. Cypress knees—woody protrusions from roots—surround each tree like congregation members. Spanish moss drapes from branches, filtering light into green shadows. The atmosphere is reverent, solemn, and beautiful. This place feels sacred, a natural temple millions of years old."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("cattail_thicket", Direction.NORTH),
                LocationConnection("familiar_nesting_tree", Direction.WEST),
                LocationConnection("ancient_aqueduct", Direction.EAST),
                LocationConnection("swamp_willow_weep", Direction.WEST),
                LocationConnection("strangler_fig_grove", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 8,
            isSafeZone = true
        ),

        // ==================== SUB-REGION 5C: Witch's Domain (10 locations, levels 9-12) ====================
        // Grid: X: -5 to -3, Y: 4 to 5
        // Theme: Cursed lands, dark magic, unnatural growth, malevolent presence
        
        Location(
            id = "hex_circle",
            name = "Hex Circle",
            description = LocationDescription.simple(
                "A perfect circle of dead vegetation marks this cursed ground. Nothing grows within the circle—the soil is ash-gray and sterile. Around the perimeter, bizarre growths thrive: twisted plants with thorns like needles, mushrooms that ooze black liquid, flowers that smell of decay. Symbols carved into trees suggest this was a ritual site. The air feels wrong here, heavy with malevolence."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 4,
            connections = listOf(
                LocationConnection("witch_hut", Direction.EAST),
                LocationConnection("witch_cauldron_spring", Direction.EAST),
                LocationConnection("poison_garden", Direction.EAST),
                LocationConnection("mist_shrouded_mere", Direction.EAST),
                LocationConnection("alligator_slide", Direction.SOUTH),
                LocationConnection("toadstool_ring_cursed", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11
        ),
        
        Location(
            id = "toadstool_ring_cursed",
            name = "Cursed Toadstool Ring",
            description = LocationDescription.simple(
                "A fairy ring of mushrooms, but wrong. The toadstools are blood-red with white spots, classic 'fairy tale' appearance but sinister. Entering the ring causes disorientation—directions become confused, time feels distorted. Some say those who sleep within the ring never wake. The toadstools glow faintly at night, pulsing like slow heartbeats. Local wildlife avoids this place."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 9,
            connections = listOf(
                LocationConnection("peat_bog_deep", Direction.SOUTH),
                LocationConnection("hex_circle", Direction.SOUTH),
                LocationConnection("voodoo_doll_grove", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 11,
            lore = "Fairy rings form when fungal mycelia spread radially from a central point, creating perfect circles. Some species cause the grass inside to die, others make it grow greener. Superstition holds that fairy rings are magical portals or cursed ground."
        ),
        
        Location(
            id = "voodoo_doll_grove",
            name = "Voodoo Doll Grove",
            description = LocationDescription.simple(
                "Crude dolls hang from tree branches—hundreds of them. Each doll is made from twisted grass, feathers, and scraps of cloth, pierced with thorns or nails. They spin slowly in the breeze, creating a disturbing mobile. The grove is a warning, a curse, or both. Whether the dolls have power or are merely psychological warfare is unclear, but the effect is undeniable: terror."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 9,
            connections = listOf(
                LocationConnection("toadstool_ring_cursed", Direction.WEST),
                LocationConnection("cult_hideout", Direction.EAST),
                LocationConnection("mist_shrouded_mere", Direction.SOUTH),
                LocationConnection("idol_chamber", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "poison_garden",
            name = "Poison Garden",
            description = LocationDescription.simple(
                "Every plant here is toxic. Oleander with beautiful but deadly flowers. Hemlock with lacy leaves and lethal sap. Nightshade berries that kill within hours. Castor beans containing ricin. The garden is intentionally cultivated, plants arranged with deliberate care. Someone tends this garden, harvesting poisons for unknown purposes. Touching anything could be fatal."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 5,
            connections = listOf(
                LocationConnection("witch_hut", Direction.NORTH),
                LocationConnection("witch_cauldron_spring", Direction.NORTH),
                LocationConnection("spell_component_bog", Direction.SOUTH),
                LocationConnection("animated_scarecrow_field", Direction.EAST),
                LocationConnection("hex_circle", Direction.WEST),
                LocationConnection("alligator_slide", Direction.SOUTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 10,
            lore = "Historically, 'poison gardens' were cultivated by apothecaries, assassins, and witches. Many common garden plants are deadly: foxglove contains digitalis, wisteria seeds cause severe illness, and rhubarb leaves contain oxalic acid."
        ),
        
        Location(
            id = "witch_cauldron_spring",
            name = "Witch's Cauldron Spring",
            description = LocationDescription.simple(
                "A bubbling spring creates a natural cauldron effect, water heated by underground thermal activity. The water is cloudy with minerals, smelling of sulfur. Strange ingredients float on the surface—herbs, bones, unidentifiable objects. The spring is clearly used for brewing potions or rituals. Nearby, empty glass vials and dried plants suggest ongoing use. The witch returns here regularly."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("witch_hut", Direction.EAST),
                LocationConnection("poison_garden", Direction.SOUTH),
                LocationConnection("hex_circle", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 12,
            shopAvailable = true,
            lore = "The witch who inhabits this region is neither wholly evil nor entirely benign. She trades in potions, curses, and knowledge, dealing fairly with those who show respect but cursing those who threaten her domain."
        ),
        
        Location(
            id = "animated_scarecrow_field",
            name = "Animated Scarecrow Field",
            description = LocationDescription.simple(
                "Scarecrows stand in shallow water, their ragged clothes flapping. But these scarecrows move—heads turning to track your passage, arms shifting position when you're not looking directly at them. Whether they're truly animated or your imagination is playing tricks is terrifyingly unclear. The scarecrows guard something, their silent vigil unbroken for unknown years."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("witch_hut", Direction.NORTH),
                LocationConnection("familiar_nesting_tree", Direction.NORTH),
                LocationConnection("blood_moon_altar", Direction.EAST),
                LocationConnection("cattail_thicket", Direction.SOUTH),
                LocationConnection("poison_garden", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "blood_moon_altar",
            name = "Blood Moon Altar",
            description = LocationDescription.simple(
                "A stone altar rises from the swamp, its surface stained dark with old blood. Carvings depict a lunar cycle, with the full moon prominently featured. During full moons, this altar becomes a ritual site—candles, offerings, and ceremonies occur here. The altar predates the current witch, suggesting a longer tradition of dark magic in this swamp. Power lingers here, palpable and unsettling."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 4,
            connections = listOf(
                LocationConnection("rotten_hollow", Direction.NORTH),
                LocationConnection("animated_scarecrow_field", Direction.WEST),
                LocationConnection("mosquito_cloud", Direction.SOUTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 11
        ),
        
        Location(
            id = "familiar_nesting_tree",
            name = "Familiar Nesting Tree",
            description = LocationDescription.simple(
                "A gnarled willow houses the witch's familiars—black cats, ravens, toads, and stranger creatures. They nest in hollow branches, watching all who pass. The familiars are intelligent, possibly more so than ordinary animals. They communicate with each other through calls and gestures, coordinating surveillance. Whether they'll attack intruders or merely report back to their mistress is uncertain."
            ),
            biome = BiomeType.SWAMP,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("witch_hut", Direction.WEST),
                LocationConnection("animated_scarecrow_field", Direction.SOUTH),
                LocationConnection("cypress_cathedral", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "spell_component_bog",
            name = "Spell Component Bog",
            description = LocationDescription.simple(
                "This section of swamp is rich in magical reagents: rare mushrooms, peculiar roots, bioluminescent moss, and other ingredients sought by practitioners of magic. The bog is intentionally maintained—certain plants cultivated, others removed. Harvesting here is theft from the witch's personal garden, but the temptation is strong. Rare components grow nowhere else."
            ),
            biome = BiomeType.SWAMP,
            gridX = -4,
            gridY = 3,
            connections = listOf(
                LocationConnection("poison_garden", Direction.NORTH),
                LocationConnection("fiddler_crab_colony", Direction.WEST),
                LocationConnection("swamp_willow_weep", Direction.WEST),
                LocationConnection("heron_hunting_shallows", Direction.EAST),
                LocationConnection("snapping_turtle_den", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10
        ),
        
        Location(
            id = "swamp_willow_weep",
            name = "Swamp Willow Weep",
            description = LocationDescription.simple(
                "Weeping willows trail their branches in dark water, creating curtains of green that obscure vision. The willows are ancient, their trunks massive and gnarled. Local legend says the trees weep for those lost in the swamp, their branches reaching down to touch the water in perpetual mourning. Moving through the willow curtains is like passing through veils, each layer revealing deeper mysteries."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 3,
            connections = listOf(
                LocationConnection("spell_component_bog", Direction.EAST),
                LocationConnection("alligator_slide", Direction.WEST),
                LocationConnection("frog_croaking_pond", Direction.NORTH),
                LocationConnection("cypress_cathedral", Direction.EAST),
                LocationConnection("heron_hunting_shallows", Direction.EAST),
                LocationConnection("alligator_slide", Direction.NORTH),
                LocationConnection("root_tangle", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),

        // ==================== SUB-REGION 5D: Sunken Ruins Zone (10 locations, levels 10-14) ====================
        // Grid: X: -3 to 0, Y: 5 to 7
        // Theme: Submerged civilization, cultist activity, ancient structures, eldritch mysteries
        
        Location(
            id = "drowned_plaza",
            name = "Drowned Plaza",
            description = LocationDescription.simple(
                "Stone paving blocks lie beneath shallow water—a plaza that once stood above ground. Statues corroded by centuries of submersion stand in formation, their features eroded beyond recognition. The plaza is vast, suggesting the ruins are extensive. Fish swim between statues. Aquatic plants grow through cracks in the stonework. This was once a gathering place; now it's an aquatic graveyard."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 8,
            connections = listOf(
                LocationConnection("carnivorous_plant_garden", Direction.SOUTH),
                LocationConnection("sacrificial_pit", Direction.EAST),
                LocationConnection("idol_chamber", Direction.WEST),
                LocationConnection("cult_hideout", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 11
        ),
        
        Location(
            id = "cult_hideout",
            name = "Cult Hideout",
            description = LocationDescription.simple(
                "A partially intact building serves as headquarters for cultists who worship something in the sunken ruins. Fresh torches, sleeping mats, and ritual paraphernalia indicate recent use. The cultists study ancient texts, perform sacrifices, and guard access to deeper ruins. They're organized, dangerous, and fanatical. Evidence suggests they're attempting to summon or awaken something best left undisturbed."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 9,
            connections = listOf(
                LocationConnection("drowned_plaza", Direction.SOUTH),
                LocationConnection("voodoo_doll_grove", Direction.WEST),
                LocationConnection("ritual_pool", Direction.EAST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 12,
            isSettlement = true
        ),
        
        Location(
            id = "idol_chamber",
            name = "Idol Chamber",
            description = LocationDescription.simple(
                "A massive stone idol dominates this chamber—a blasphemous sculpture depicting something between octopus, dragon, and nightmare. The idol is partially submerged, water lapping at carvings of tentacles and eyes. The sculpture exudes wrongness; looking at it too long causes headaches and disturbing dreams. Offerings pile around its base: bones, coins, stranger things. Cultists worship here regularly."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 9,
            connections = listOf(
                LocationConnection("bloodsucking_vine_tangle", Direction.SOUTH),
                LocationConnection("bog_iron_deposit", Direction.SOUTH),
                LocationConnection("voodoo_doll_grove", Direction.WEST),
                LocationConnection("drowned_plaza", Direction.EAST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 13,
            lore = "The idol predates known civilizations, carved by peoples lost to history. Its non-Euclidean geometry and alien features suggest influences from beyond normal reality. Scholars who study it too closely tend to go mad."
        ),
        
        Location(
            id = "ritual_pool",
            name = "Ritual Pool",
            description = LocationDescription.simple(
                "A stone-lined pool carved with intricate runes that glow faintly in moonlight. The water is unnaturally clear despite the surrounding swamp, allowing visibility to the bottom where strange symbols are carved. Cultists use this pool for baptisms and summoning rituals. The water has odd properties—it's warmer than ambient temperature, and things submerged in it decay slower than normal."
            ),
            biome = BiomeType.SWAMP,
            gridX = 0,
            gridY = 9,
            connections = listOf(
                LocationConnection("cult_hideout", Direction.WEST),
                LocationConnection("sacrificial_pit", Direction.SOUTH),
                LocationConnection("underwater_passage", Direction.DOWN)
            ),
            encounterRate = 0.80,
            recommendedLevel = 12
        ),
        
        Location(
            id = "collapsed_library",
            name = "Collapsed Library",
            description = LocationDescription.simple(
                "Ruins of a library, its roof collapsed and interior flooded. Waterlogged books and scrolls litter the floor, most destroyed beyond reading. But a few texts survived in sealed containers—ancient knowledge preserved by accident. The library holds secrets about the civilization that built these ruins, their fall, and what they worshipped. Recovering intact texts requires careful excavation."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 6,
            connections = listOf(
                LocationConnection("sunken_temple", Direction.SOUTH),
                LocationConnection("throne_room_flooded", Direction.WEST),
                LocationConnection("carnivorous_plant_garden", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 11,
            shopAvailable = true,
            lore = "Before the ruins sank, this was a great library containing knowledge from a now-dead civilization. Cultists and scholars compete to recover its surviving texts, which detail arcane rituals, historical events, and astronomical observations."
        ),
        
        Location(
            id = "sacrificial_pit",
            name = "Sacrificial Pit",
            description = LocationDescription.simple(
                "A deep pit filled with bones—thousands of them, both animal and human. The pit was used for sacrifices, victims thrown alive to whatever lurked below. The bones are old, but recent additions suggest the practice continues. At the bottom, partially submerged in fetid water, something moves. Whether it's scavengers or something worse is unclear. The pit radiates malevolence."
            ),
            biome = BiomeType.SWAMP,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("drowned_plaza", Direction.WEST),
                LocationConnection("mosaic_courtyard", Direction.SOUTH),
                LocationConnection("ritual_pool", Direction.NORTH),
                LocationConnection("deep_dark", Direction.DOWN)
            ),
            encounterRate = 0.85,
            recommendedLevel = 13
        ),
        
        Location(
            id = "ancient_aqueduct",
            name = "Ancient Aqueduct",
            description = LocationDescription.simple(
                "Stone arches of an aqueduct stretch across the swamp, partially collapsed but still impressive. The aqueduct once carried fresh water to the city, now it's overgrown with vines and moss. Walking atop the aqueduct provides a dry path through otherwise impassable swamp, but the stonework is crumbling and treacherous. Below, the swamp has reclaimed what the ancients built."
            ),
            biome = BiomeType.SWAMP,
            gridX = -1,
            gridY = 5,
            connections = listOf(
                LocationConnection("sunken_temple", Direction.NORTH),
                LocationConnection("cypress_cathedral", Direction.WEST),
                LocationConnection("fungal_bloom_island", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10
        ),
        
        Location(
            id = "mosaic_courtyard",
            name = "Mosaic Courtyard",
            description = LocationDescription.simple(
                "An intact mosaic floor lies beneath crystal-clear shallow water. The mosaic depicts scenes from the ancient civilization: processions, battles, astronomical charts, and disturbing rituals. The artistry is exquisite, each tile carefully placed. Studying the mosaic reveals historical information, but also raises disturbing questions about what these people worshipped and why their civilization fell."
            ),
            biome = BiomeType.SWAMP,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("carnivorous_plant_garden", Direction.WEST),
                LocationConnection("statue_garden", Direction.SOUTH),
                LocationConnection("sacrificial_pit", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 11
        ),
        
        Location(
            id = "statue_garden",
            name = "Statue Garden",
            description = LocationDescription.simple(
                "Dozens of statues stand in formation, each depicting a different figure—priests, warriors, nobles, and stranger beings. The statues are remarkably preserved, their expressions hauntingly lifelike. Some local legends claim these aren't statues but people turned to stone by ancient magic. Whether that's true or superstition, the garden has an uncanny quality that makes you avoid meeting the statues' stone gazes."
            ),
            biome = BiomeType.SWAMP,
            gridX = 0,
            gridY = 6,
            connections = listOf(
                LocationConnection("sunken_temple", Direction.WEST),
                LocationConnection("mosaic_courtyard", Direction.NORTH),
                LocationConnection("carnivorous_plant_garden", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10
        ),
        
        Location(
            id = "throne_room_flooded",
            name = "Flooded Throne Room",
            description = LocationDescription.simple(
                "A grand throne room now filled waist-deep with murky water. The throne sits on a raised dais, just above water level—a massive stone seat carved with disturbing imagery. Murals on the walls depict the ruler who sat here, wearing a crown of tentacles and eyes. The throne radiates power and madness. Sitting on it would be monumentally foolish, yet the temptation is strangely strong."
            ),
            biome = BiomeType.SWAMP,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("sunken_temple", Direction.NORTH),
                LocationConnection("strangler_fig_grove", Direction.SOUTH),
                LocationConnection("collapsed_library", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 14,
            lore = "The last king of the sunken civilization was said to have made pacts with eldritch beings, trading his sanity and his people's freedom for power. His reign ended in catastrophe, the city sinking into the swamp as punishment—or transformation."
        ),

        // ==================== SUB-REGION 5E: Mangrove Labyrinths (10 locations, levels 8-11) ====================
        // Grid: X: -6 to -4, Y: 2 to 4
        // Theme: Twisted roots, maze-like passages, tidal influences, difficult navigation
        
        Location(
            id = "root_tangle",
            name = "Root Tangle",
            description = LocationDescription.simple(
                "Mangrove roots create an impenetrable maze—woody fingers rising from water and mud in chaotic patterns. The roots arch, twist, and interweave, forming natural tunnels and barriers. Navigation requires climbing over, ducking under, and squeezing between roots. The tangle is three-dimensional, extending both above and below water. Getting lost is easy; finding your way out can take hours."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 2,
            connections = listOf(
                LocationConnection("swamp_willow_weep", Direction.NORTH),
                LocationConnection("fiddler_crab_colony", Direction.NORTH),
                LocationConnection("mud_skip_channel", Direction.SOUTH),
                LocationConnection("barnacle_bridge", Direction.WEST),
                LocationConnection("alligator_slide", Direction.NORTH),
                LocationConnection("snapping_turtle_den", Direction.EAST),
                LocationConnection("mangrove_canopy", Direction.WEST),
                LocationConnection("mudskipper_flats", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9
        ),
        
        Location(
            id = "mangrove_canopy",
            name = "Mangrove Canopy",
            description = LocationDescription.simple(
                "High in the mangrove branches, a network of platforms and passages exists above the water. Vines and branches create bridges. The canopy is home to tree-dwelling creatures: birds, insects, snakes, and stranger things. Traveling through the canopy is faster than slogging through water below, but falling would be disastrous. The view from above reveals the true extent of the mangrove maze."
            ),
            biome = BiomeType.SWAMP,
            gridX = -6,
            gridY = 2,
            connections = listOf(
                LocationConnection("root_tangle", Direction.EAST),
                LocationConnection("fiddler_crab_colony", Direction.NORTHEAST),
                LocationConnection("mudskipper_flats", Direction.SOUTH),
                LocationConnection("hermit_village", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9,
            lore = "Mangroves are salt-tolerant trees that thrive in coastal tidal zones. Their prop roots filter salt from water, allowing them to survive in conditions that kill other trees. The roots also stabilize coastlines, preventing erosion."
        ),
        
        Location(
            id = "mudskipper_flats",
            name = "Mudskipper Flats",
            description = LocationDescription.simple(
                "Tidal mudflats exposed at low tide, covered at high tide. Mudskippers—bizarre fish that walk on land using their fins—populate these flats in vast numbers. Each mudskipper is your size, flopping across mud with surprising speed. They're harmless but startling. The mud is deep and clingy, making movement exhausting. Timing travel with tides is essential; being caught here during high tide means swimming or drowning."
            ),
            biome = BiomeType.SWAMP,
            gridX = -6,
            gridY = 1,
            connections = listOf(
                LocationConnection("mangrove_canopy", Direction.NORTH),
                LocationConnection("mud_skip_channel", Direction.EAST),
                LocationConnection("oyster_reef", Direction.WEST),
                LocationConnection("root_tangle", Direction.NORTH),
                LocationConnection("hermit_village", Direction.WEST),
                LocationConnection("tidepool", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 8
        ),
        
        Location(
            id = "hermit_village",
            name = "Hermit Village",
            description = LocationDescription.simple(
                "A small community of hermits lives here, their huts built on stilts above high-tide lines. The hermits are reclusive but not hostile, trading preserved fish and knowledge of the swamp for supplies. They know secret paths through the mangrove maze and respect the swamp's dangers. This is a rare safe haven in hostile terrain, but the hermits' hospitality has limits."
            ),
            biome = BiomeType.SWAMP,
            gridX = -7,
            gridY = 2,
            connections = listOf(
                LocationConnection("mangrove_canopy", Direction.EAST),
                LocationConnection("barnacle_bridge", Direction.EAST),
                LocationConnection("salt_marsh_transition", Direction.NORTH),
                LocationConnection("mudskipper_flats", Direction.EAST),
                LocationConnection("oyster_reef", Direction.SOUTH)
            ),
            encounterRate = 0.30,
            recommendedLevel = 8,
            isSettlement = true,
            isSafeZone = true,
            shopAvailable = true
        ),
        
        Location(
            id = "oyster_reef",
            name = "Oyster Reef",
            description = LocationDescription.simple(
                "A reef of oysters encrusts mangrove roots and rocks, creating a jagged landscape of shells. Each oyster shell has razor-sharp edges capable of inflicting deep cuts. Walking here requires extreme care; one slip could cause serious injury. But oysters are food, and pearls occasionally form within. The reef attracts crabs, fish, and birds—all potential food sources or threats."
            ),
            biome = BiomeType.SWAMP,
            gridX = -7,
            gridY = 1,
            connections = listOf(
                LocationConnection("hermit_village", Direction.NORTH),
                LocationConnection("ghost_crab_burrows", Direction.SOUTH),
                LocationConnection("mudskipper_flats", Direction.EAST),
                LocationConnection("tidepool", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "fiddler_crab_colony",
            name = "Fiddler Crab Colony",
            description = LocationDescription.simple(
                "Thousands of fiddler crabs swarm these mudflats, their oversized claws waving in ritualistic displays. Each crab is small from a human perspective but your size or larger from a button quail's view. The crabs are aggressive when defending burrows, their claws capable of painful pinches. The colony creates a moving carpet of claws and shells, impressive and intimidating."
            ),
            biome = BiomeType.SWAMP,
            gridX = -6,
            gridY = 3,
            connections = listOf(
                LocationConnection("root_tangle", Direction.SOUTH),
                LocationConnection("barnacle_bridge", Direction.NORTH),
                LocationConnection("mangrove_canopy", Direction.SOUTHWEST),
                LocationConnection("spell_component_bog", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "mud_skip_channel",
            name = "Mud Skip Channel",
            description = LocationDescription.simple(
                "A narrow water channel winds through mangrove roots, deep enough for swimming but too narrow for comfort. The channel's name comes from the technique required to traverse it—hopping from root to exposed mud to floating debris, skipping across without falling into deep water. The channel floods and drains with tides, changing dramatically throughout the day."
            ),
            biome = BiomeType.SWAMP,
            gridX = -5,
            gridY = 1,
            connections = listOf(
                LocationConnection("root_tangle", Direction.NORTH),
                LocationConnection("mudskipper_flats", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "barnacle_bridge",
            name = "Barnacle Bridge",
            description = LocationDescription.simple(
                "A fallen tree trunk encrusted with barnacles spans deep water. The barnacles create a rough, sharp surface—walking on them is painful but provides grip. Barnacles are filter feeders, extending feathery appendages that wave in the current. They're harmless but disturbing to see up close, especially at your scale where each barnacle is visible in detail."
            ),
            biome = BiomeType.SWAMP,
            gridX = -6,
            gridY = 4,
            connections = listOf(
                LocationConnection("fiddler_crab_colony", Direction.SOUTH),
                LocationConnection("salt_marsh_transition", Direction.WEST),
                LocationConnection("root_tangle", Direction.EAST),
                LocationConnection("hermit_village", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "salt_marsh_transition",
            name = "Salt Marsh Transition",
            description = LocationDescription.simple(
                "Where mangrove gives way to salt marsh, the vegetation changes—cordgrass replaces trees, creating a golden sea of waving stalks. The marsh is influenced by tides, alternately flooded and exposed. Salt crusts form on exposed mud during low tide. This transitional ecosystem supports unique wildlife adapted to dramatic salinity changes. The marsh extends westward toward the coast."
            ),
            biome = BiomeType.SWAMP,
            gridX = -7,
            gridY = 3,
            connections = listOf(
                LocationConnection("hermit_village", Direction.SOUTH),
                LocationConnection("ghost_crab_burrows", Direction.SOUTH),
                LocationConnection("barnacle_bridge", Direction.EAST),
                LocationConnection("harbor_town", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 8
        ),
        
        Location(
            id = "ghost_crab_burrows",
            name = "Ghost Crab Burrows",
            description = LocationDescription.simple(
                "The beach-swamp transition is riddled with ghost crab burrows—perfect circular holes in the sand. Ghost crabs are pale, almost translucent, emerging at night to hunt. They're incredibly fast, disappearing into burrows instantly when threatened. Each burrow goes surprisingly deep, creating a honeycomb of tunnels beneath the surface. Step in the wrong spot and your foot plunges through."
            ),
            biome = BiomeType.SWAMP,
            gridX = -7,
            gridY = 0,
            connections = listOf(
                LocationConnection("oyster_reef", Direction.NORTH),
                LocationConnection("mangrove_edge", Direction.EAST),
                LocationConnection("salt_marsh_transition", Direction.NORTH),
                LocationConnection("harbor_town", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        )
    )
}

