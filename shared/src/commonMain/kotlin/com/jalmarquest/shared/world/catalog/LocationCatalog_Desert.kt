package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * DESERT region catalog - 60 new locations expanding the arid zones
 * Sub-regions: Outer Dunes (4A), Deep Desert (4B), Desert Canyons (4C),
 *              Oasis Network (4D), Sandstone Formations (4E), Buried Complexes (4F)
 * Connects to existing locations: dunes_sea, oasis_verdant, scorpion_gulch, sandstone_ruins
 */
internal val DESERT_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 4A: Outer Dunes (15 locations, levels 4-7) ====================
        // Grid: X: 1-5, Y: -3 to -1
        // Theme: Shifting sands, mirages, heat distortion, transition from grassland to desert
        
        Location(
            id = "sand_ripple_plains",
            name = "Sand Ripple Plains",
            description = LocationDescription.simple(
                "The grasslands end abruptly, replaced by an ocean of sand. Small ripples pattern the surface like frozen waves—each ripple crest is as tall as you are. The sand is fine and golden, sifting between your toes with every step. Heat radiates from the ground in visible waves, making distant objects shimmer and dance."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -2,
            connections = listOf(
                LocationConnection("oasis_verdant", Direction.NORTH),
                LocationConnection("dunes_sea", Direction.EAST),
                LocationConnection("wandering_dunes", Direction.SOUTH),
                LocationConnection("heat_shimmer_flats", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 4
        ),
        
        Location(
            id = "wandering_dunes",
            name = "Wandering Dunes",
            description = LocationDescription.withAllSeasons(
                spring = "The dunes shift constantly in spring winds, their shapes changing day by day. What was a valley yesterday is now a ridge. Navigating requires constant vigilance—landmarks you passed an hour ago may have vanished entirely, buried under tons of migrating sand.",
                summer = "Summer heat makes the wandering dunes almost liquid. The sand flows like slow water, cascading down slopes in miniature avalanches. Walking uphill is exhausting—for every two steps forward, you slide one step back. The heat is so intense that touching the sand surface would burn exposed skin.",
                autumn = "Autumn storms reshape the wandering dunes dramatically. High winds scour the crests, sending sand streaming horizontally. You must shelter in the valleys between dunes, watching sand rivers flow overhead. After each storm, the landscape is completely transformed.",
                winter = "Winter brings relative stability to the wandering dunes. Cold nights create a crust on the sand that holds shapes longer. The dunes become a frozen sea, their forms locked in place until spring thaws and winds return to set them wandering again."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -3,
            connections = listOf(
                LocationConnection("sand_ripple_plains", Direction.NORTH),
                LocationConnection("dunes_sea", Direction.NORTHEAST),
                LocationConnection("caravanserai_ruins", Direction.EAST),
                LocationConnection("mirage_oasis", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 5
        ),
        
        Location(
            id = "heat_shimmer_flats",
            name = "Heat Shimmer Flats",
            description = LocationDescription.simple(
                "A broad expanse of packed sand creates a relatively flat surface—rare in the dune fields. The heat here is tremendous, radiating from the ground in visible waves. Mirages are constant: you see lakes that don't exist, distant mountains that vanish on approach, phantom travelers who disappear when you get close. Navigation requires trusting your instincts over your eyes."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -2,
            connections = listOf(
                LocationConnection("sand_ripple_plains", Direction.EAST),
                LocationConnection("ocotillo_fence", Direction.WEST),
                LocationConnection("mesquite_thicket", Direction.SOUTH),
                LocationConnection("century_plant_field", Direction.NORTH),
                LocationConnection("oasis_verdant", Direction.NORTHEAST),
                LocationConnection("tumbleweed_crossing", Direction.NORTH),
                LocationConnection("cactus_sentinel_grove", Direction.WEST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 4
        ),
        
        Location(
            id = "cactus_sentinel_grove",
            name = "Cactus Sentinel Grove",
            description = LocationDescription.simple(
                "Towering saguaro cacti rise from the sand like ancient sentinels. Each cactus is massive from your perspective—a giant green pillar covered in sharp spines. Their shadows provide precious shade during the hottest hours. Woodpecker holes dot their sides, creating apartments for desert birds. Some cacti are centuries old, having seen countless generations of quail come and go."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -2,
            connections = listOf(
                LocationConnection("heat_shimmer_flats", Direction.EAST),
                LocationConnection("barrel_cactus_maze", Direction.SOUTH),
                LocationConnection("tumbleweed_crossing", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5
        ),
        
        Location(
            id = "caravanserai_ruins",
            name = "Caravanserai Ruins",
            description = LocationDescription.simple(
                "Crumbling stone walls mark where a desert trading post once stood. The ruins are vast—a human-sized building is a sprawling complex to you. Broken pottery litters the ground, each shard the size of a shield. Carved stone troughs once held water for camels; now they're dry basins filled with windblown sand. Inscriptions in unknown languages cover the walls, slowly being erased by sand-laden winds."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -3,
            connections = listOf(
                LocationConnection("wandering_dunes", Direction.WEST),
                LocationConnection("scorpion_gulch", Direction.NORTH),
                LocationConnection("sand_devil_alley", Direction.EAST),
                LocationConnection("merchant_skeleton", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 6,
            lore = "This caravanserai served desert traders for generations before being abandoned. Local legend claims it was cursed after a merchant was murdered here for his gold. On quiet nights, some say you can hear the jingle of camel bells and the murmur of bargaining voices."
        ),
        
        Location(
            id = "sand_devil_alley",
            name = "Sand Devil Alley",
            description = LocationDescription.simple(
                "A corridor between dunes where dust devils form with alarming frequency. These miniature tornadoes spin constantly, varying from ankle-high (for you, that's terrifying) to towering columns visible for miles. The whirlwinds create a haunting howl, and stepping into one means being picked up and thrown. You must time your crossing carefully, darting between devils."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -3,
            connections = listOf(
                LocationConnection("caravanserai_ruins", Direction.WEST),
                LocationConnection("dunes_sea", Direction.NORTH),
                LocationConnection("vulture_roost_spire", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 6
        ),
        
        Location(
            id = "mirage_oasis",
            name = "Mirage Oasis",
            description = LocationDescription.simple(
                "This spot is cursed with the most convincing mirages in the desert. You see a beautiful oasis with palm trees and cool water—but it's never quite where it appears. Walk toward it and it retreats. The real trick is that sometimes there IS water here, but only after rare rains. Learning to distinguish the phantom from the real is a survival skill."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -4,
            connections = listOf(
                LocationConnection("wandering_dunes", Direction.NORTH),
                LocationConnection("merchant_skeleton", Direction.EAST),
                LocationConnection("date_palm_hideaway", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 5,
            lore = "Dozens of travelers have died chasing the mirages here. Their bones, when found, are always facing the direction of the phantom oasis, as if they died still believing it was just over the next dune."
        ),
        
        Location(
            id = "merchant_skeleton",
            name = "Merchant's Skeleton",
            description = LocationDescription.simple(
                "The bleached bones of a long-dead merchant lie half-buried in sand. The skeleton is gigantic from your perspective—each rib is like a curved beam. Scattered coins and worthless trade goods surround the remains. A broken water flask lies just beyond the skeleton's outstretched hand—they died reaching for it. The scene is a sobering reminder of the desert's cruelty."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -4,
            connections = listOf(
                LocationConnection("caravanserai_ruins", Direction.NORTH),
                LocationConnection("mirage_oasis", Direction.WEST),
                LocationConnection("vulture_roost_spire", Direction.NORTHEAST),
                LocationConnection("sun_bleached_basin", Direction.SOUTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "vulture_roost_spire",
            name = "Vulture Roost Spire",
            description = LocationDescription.simple(
                "A lone sandstone spire rises from the dunes, its top crowned with vulture nests. The birds circle constantly, riding thermals, their keen eyes scanning for anything dying or dead. The rock is streaked white with decades of droppings. Bones litter the base—the remains of the vultures' meals. The birds watch you with calculating interest, waiting to see if you'll become their next meal."
            ),
            biome = BiomeType.DESERT,
            gridX = 5,
            gridY = -3,
            connections = listOf(
                LocationConnection("sand_devil_alley", Direction.WEST),
                LocationConnection("merchant_skeleton", Direction.SOUTHWEST),
                LocationConnection("scorched_earth_flats", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 7
        ),
        
        Location(
            id = "barrel_cactus_maze",
            name = "Barrel Cactus Maze",
            description = LocationDescription.simple(
                "Hundreds of barrel cacti create a natural labyrinth. Each cactus is squat and round, covered in fearsome spines that curve like fishhooks. The cacti grow so closely together that navigating between them requires careful squeezing. Some cacti hold water inside—you can hear it slosh when wind rocks them. Harvesting that water without getting impaled is the challenge."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -3,
            connections = listOf(
                LocationConnection("cactus_sentinel_grove", Direction.NORTH),
                LocationConnection("desert_spring_hidden", Direction.SOUTH),
                LocationConnection("date_palm_hideaway", Direction.EAST),
                LocationConnection("creosote_ring", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 5
        ),
        
        Location(
            id = "date_palm_hideaway",
            name = "Date Palm Hideaway",
            description = LocationDescription.simple(
                "A cluster of date palms grows around a hidden spring—real water, not a mirage. The palms provide blessed shade, and dates hang in massive clusters. Each date is the size of your entire body, sweet and nourishing. This hideaway is a secret treasure, known only to those who've explored thoroughly. The spring water tastes mineral-rich and cold."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -4,
            connections = listOf(
                LocationConnection("mirage_oasis", Direction.EAST),
                LocationConnection("barrel_cactus_maze", Direction.WEST),
                LocationConnection("palm_oasis_minor", Direction.SOUTH)
            ),
            encounterRate = 0.30,
            recommendedLevel = 5,
            isSafeZone = true
        ),
        
        Location(
            id = "creosote_ring",
            name = "Creosote Ring",
            description = LocationDescription.simple(
                "A perfect circle of creosote bushes grows in the sand, their arrangement too precise to be natural. The bushes are ancient, their root systems toxic to other plants—nothing else grows within the ring. After rain, the creosote releases a distinctive smell, sharp and medicinal. Local legend claims this ring was planted by desert shamans as a protective barrier."
            ),
            biome = BiomeType.DESERT,
            gridX = -2,
            gridY = -3,
            connections = listOf(
                LocationConnection("barrel_cactus_maze", Direction.EAST),
                LocationConnection("tumbleweed_crossing", Direction.NORTH),
                LocationConnection("joshua_tree_forest", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6,
            lore = "The creosote bushes in this ring are clones, all grown from a single ancient plant. Biologists estimate the ring is over 1,000 years old, making it older than most human civilizations in the region."
        ),
        
        Location(
            id = "joshua_tree_forest",
            name = "Joshua Tree Forest",
            description = LocationDescription.simple(
                "Bizarre Joshua trees grow in twisted formations, their spiky branches reaching skyward like supplicating arms. Each tree is unique, shaped by wind and weather into individual sculptures. The trees are ancient and slow-growing; some are centuries old. Their scale is imposing—what's merely a large shrub to humans is a towering forest to you."
            ),
            biome = BiomeType.DESERT,
            gridX = -3,
            gridY = -3,
            connections = listOf(
                LocationConnection("creosote_ring", Direction.EAST),
                LocationConnection("yucca_spike_field", Direction.SOUTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "yucca_spike_field",
            name = "Yucca Spike Field",
            description = LocationDescription.simple(
                "Sharp yucca plants bristle from the sand like a bed of spears. Each plant's leaves end in needle-sharp points, and their flower stalks tower overhead. Moving through this field requires acrobatic maneuvering to avoid impalement. Yucca moths flutter between the flowers in a ancient symbiotic dance—the only insects that can pollinate these plants."
            ),
            biome = BiomeType.DESERT,
            gridX = -3,
            gridY = -4,
            connections = listOf(
                LocationConnection("joshua_tree_forest", Direction.NORTH),
                LocationConnection("desert_spring_hidden", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "desert_spring_hidden",
            name = "Hidden Desert Spring",
            description = LocationDescription.simple(
                "Water seeps from a crack in the bedrock, creating a tiny spring surrounded by lush vegetation. Ferns and grasses grow in wild profusion, fed by the precious moisture. The spring is well-hidden, sheltered by overhanging rocks. Animals from miles around know this location—their tracks converge here like roads leading to a city."
            ),
            biome = BiomeType.DESERT,
            gridX = -2,
            gridY = -4,
            connections = listOf(
                LocationConnection("yucca_spike_field", Direction.WEST),
                LocationConnection("smoke_tree_wash", Direction.WEST),
                LocationConnection("barrel_cactus_maze", Direction.NORTH),
                LocationConnection("palm_oasis_minor", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 6,
            isSafeZone = true
        ),

        // ==================== SUB-REGION 4B: Deep Desert (15 locations, levels 7-11) ====================
        // Grid: X: 4-8, Y: -5 to -2
        // Theme: Extreme heat, dangerous wildlife, desolation, true desert heart
        
        Location(
            id = "scorched_earth_flats",
            name = "Scorched Earth Flats",
            description = LocationDescription.simple(
                "The sand here has been fused into glass by intense heat—whether from sun or ancient fires is unknown. Walking on the glassy surface produces musical tinkling sounds. The glass is slick and treacherous, offering no traction. Extreme heat radiates from it, creating an oven-like environment. Nothing grows here; nothing lives here. It's beautiful and deadly in equal measure."
            ),
            biome = BiomeType.DESERT,
            gridX = 6,
            gridY = -3,
            connections = listOf(
                LocationConnection("vulture_roost_spire", Direction.WEST),
                LocationConnection("dust_devil_crossing", Direction.NORTH),
                LocationConnection("sun_altar", Direction.EAST),
                LocationConnection("salt_flat_expanse", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "sun_altar",
            name = "Sun Altar",
            description = LocationDescription.simple(
                "A stone platform rises from the desert, its surface carved with sun symbols and ancient script. This altar was built by a civilization that worshipped the sun as a deity—appropriate, given the sun's dominance here. Bronze mirrors tarnished green with age surround the altar, once used to focus sunlight. Standing at the altar during noon is like standing in a furnace."
            ),
            biome = BiomeType.DESERT,
            gridX = 7,
            gridY = -3,
            connections = listOf(
                LocationConnection("scorched_earth_flats", Direction.WEST),
                LocationConnection("bone_field", Direction.EAST),
                LocationConnection("obsidian_field", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9,
            lore = "The sun cult that built this altar believed that sacrifices made here would ensure good harvests. The dark stains on the stone suggest they were very serious about their beliefs. The cult vanished centuries ago, but the altar remains."
        ),
        
        Location(
            id = "bone_field",
            name = "Bone Field",
            description = LocationDescription.simple(
                "Countless bones carpet the sand—the remains of animals that died trying to cross this waterless expanse. Ribs, skulls, and limb bones of various species create a macabre landscape. Some bones are ancient, weathered smooth by sand. Others are relatively fresh, still bearing scraps of dried flesh. This field is a graveyard and a warning: the desert takes all debts in the end."
            ),
            biome = BiomeType.DESERT,
            gridX = 8,
            gridY = -3,
            connections = listOf(
                LocationConnection("sun_altar", Direction.WEST),
                LocationConnection("sidewinder_den", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 10,
            lore = "Naturalists have catalogued bones from dozens of species here, some extinct for centuries. The field is a paleontological treasure trove—and a death trap for the unwary."
        ),
        
        Location(
            id = "salt_flat_expanse",
            name = "Salt Flat Expanse",
            description = LocationDescription.simple(
                "A vast depression filled with crystallized salt creates a blindingly white plain. The salt crust crunches underfoot, sometimes breaking through to mud beneath. In places, salt crystals have grown into fantastic shapes—pyramids, cubes, and hexagonal pillars. The glare off the white surface is intense, capable of causing snow blindness. Mirages are extreme here, distorting distance and perspective."
            ),
            biome = BiomeType.DESERT,
            gridX = 6,
            gridY = -4,
            connections = listOf(
                LocationConnection("scorched_earth_flats", Direction.NORTH),
                LocationConnection("obsidian_field", Direction.EAST),
                LocationConnection("tar_pit_seep", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "obsidian_field",
            name = "Obsidian Field",
            description = LocationDescription.simple(
                "Chunks of volcanic glass—obsidian—litter the sand, evidence of ancient volcanic activity. Each piece is sharp enough to draw blood with the lightest touch. The obsidian is jet black, absorbing heat until it's too hot to approach. Ancient peoples mined this field for tools; broken blades and worked cores remain. Walking here requires extreme care—one wrong step could mean a serious cut."
            ),
            biome = BiomeType.DESERT,
            gridX = 7,
            gridY = -4,
            connections = listOf(
                LocationConnection("sun_altar", Direction.NORTH),
                LocationConnection("salt_flat_expanse", Direction.WEST),
                LocationConnection("sidewinder_den", Direction.EAST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 9
        ),
        
        Location(
            id = "sidewinder_den",
            name = "Sidewinder Den",
            description = LocationDescription.simple(
                "Rocky outcrops provide shelter for sidewinder rattlesnakes—venomous serpents that move by throwing their bodies sideways. Each snake is longer than you are tall, and their venom is deadly. The rocks are riddled with snake holes, and shed snake skins litter the ground. The distinctive sidewinding tracks pattern the sand. Approach with extreme caution—or better yet, don't approach at all."
            ),
            biome = BiomeType.DESERT,
            gridX = 8,
            gridY = -4,
            connections = listOf(
                LocationConnection("bone_field", Direction.NORTH),
                LocationConnection("obsidian_field", Direction.WEST),
                LocationConnection("scorpion_palace", Direction.SOUTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 10
        ),
        
        Location(
            id = "sun_bleached_basin",
            name = "Sun-Bleached Basin",
            description = LocationDescription.simple(
                "A depression in the desert creates a natural bowl that traps heat like an oven. The temperature here regularly exceeds what's bearable. Everything is bleached white by the relentless sun—rocks, bones, even the sand itself. A few hardy lichens cling to north-facing rock surfaces, but otherwise, life has surrendered to the heat. Crossing this basin during day is near-suicide; twilight or dawn are the only safe times."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -4,
            connections = listOf(
                LocationConnection("merchant_skeleton", Direction.NORTH),
                LocationConnection("canyon_overlook", Direction.SOUTH),
                LocationConnection("flash_flood_channel", Direction.SOUTH),
                LocationConnection("ancient_lakebed", Direction.WEST),
                LocationConnection("tar_pit_seep", Direction.EAST),
                LocationConnection("gila_monster_rocks", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "tar_pit_seep",
            name = "Tar Pit Seep",
            description = LocationDescription.simple(
                "Black tar oozes from the ground, pooling in sticky puddles that trap anything unlucky enough to step in them. The tar smells of petroleum and decay. Bones protrude from the largest pools—ancient animals that became mired and died. The tar is eternally sticky, never fully hardening. Gas bubbles rise and pop with soft gloops. Navigating around the seeps requires careful attention; a moment's inattention could be fatal."
            ),
            biome = BiomeType.DESERT,
            gridX = 5,
            gridY = -5,
            connections = listOf(
                LocationConnection("salt_flat_expanse", Direction.NORTH),
                LocationConnection("gila_monster_rocks", Direction.WEST),
                LocationConnection("sun_bleached_basin", Direction.WEST),
                LocationConnection("petrified_forest_edge", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9,
            lore = "Paleontologists dream of excavating these tar pits. The perfectly preserved bones include species extinct for millennia. But the seeps are too remote and too dangerous for organized study."
        ),
        
        Location(
            id = "petrified_forest_edge",
            name = "Petrified Forest Edge",
            description = LocationDescription.simple(
                "Ancient trees, now turned to stone, lie scattered across the desert. These petrified logs are beautiful—their wood grain perfectly preserved in colorful stone. Some logs are intact, others shattered into chunks of rainbow-colored rock. This forest died millions of years ago, buried and mineralized, then exposed by erosion. It's a snapshot of an ancient world when this desert was lush forest."
            ),
            biome = BiomeType.DESERT,
            gridX = 6,
            gridY = -5,
            connections = listOf(
                LocationConnection("tar_pit_seep", Direction.WEST),
                LocationConnection("scorpion_palace", Direction.EAST),
                LocationConnection("scorpion_palace", Direction.NORTH),
                LocationConnection("fossil_bed_canyon", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "scorpion_palace",
            name = "Scorpion Palace",
            description = LocationDescription.simple(
                "A labyrinth of rocks creates perfect habitat for bark scorpions—tiny but extremely venomous arachnids. From your perspective, these scorpions are formidable opponents, their stingers capable of delivering painful, potentially lethal venom. The rocks are riddled with scorpion burrows. At night, the scorpions emerge by the hundreds, their bodies glowing under UV light. This is their kingdom, and you are an intruder."
            ),
            biome = BiomeType.DESERT,
            gridX = 7,
            gridY = -5,
            connections = listOf(
                LocationConnection("sidewinder_den", Direction.NORTH),
                LocationConnection("petrified_forest_edge", Direction.SOUTH),
                LocationConnection("petrified_forest_edge", Direction.WEST),
                LocationConnection("forgotten_catacombs", Direction.DOWN)
            ),
            encounterRate = 0.95,
            recommendedLevel = 11,
            lore = "The concentration of scorpions here is unnatural. Some believe a scorpion queen—a creature of myth—rules this palace from deep beneath the rocks."
        ),
        
        Location(
            id = "gila_monster_rocks",
            name = "Gila Monster Rocks",
            description = LocationDescription.simple(
                "Large boulders provide shelter for Gila monsters—venomous lizards that move slowly but strike quickly. Each lizard is massive from your perspective, their beaded skin in black and orange warning patterns. They're lethargic in the heat but deadly if provoked. Their venom glands are clearly visible beneath their jaws. The rocks they inhabit stay cooler than open desert, making this a contested territory."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -5,
            connections = listOf(
                LocationConnection("sun_bleached_basin", Direction.NORTH),
                LocationConnection("ancient_lakebed", Direction.NORTH),
                LocationConnection("tar_pit_seep", Direction.EAST),
                LocationConnection("slot_canyon_entrance", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 9
        ),
        
        Location(
            id = "fossil_bed_canyon",
            name = "Fossil Bed Canyon",
            description = LocationDescription.simple(
                "Erosion has exposed a canyon wall dense with fossils—ancient shells, bones, and plant impressions preserved in stone. This area was once an ocean floor, millions of years before it became desert. Ammonite spirals the size of dinner plates are embedded in the rock. Trilobites, ancient fish, and creatures with no modern relatives are visible. It's a museum of deep time, written in stone."
            ),
            biome = BiomeType.DESERT,
            gridX = 6,
            gridY = -6,
            connections = listOf(
                LocationConnection("petrified_forest_edge", Direction.NORTH),
                LocationConnection("echo_canyon", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10,
            lore = "Geologists have identified fossils here from the Paleozoic Era—over 250 million years old. The canyon is a timeline of Earth's history, each layer representing a different age."
        ),
        
        Location(
            id = "dust_devil_crossing",
            name = "Dust Devil Crossing",
            description = LocationDescription.simple(
                "Where two valleys converge, wind patterns create constant dust devils—sometimes a dozen spinning simultaneously. These whirlwinds range from small twisters to towering columns that reach the clouds. The sound is deafening, a collective howl that drowns out thought. Debris caught in the devils—sticks, bones, rocks—becomes dangerous projectiles. Timing your crossing between devils is essential and nerve-wracking."
            ),
            biome = BiomeType.DESERT,
            gridX = 5,
            gridY = -2,
            connections = listOf(
                LocationConnection("scorched_earth_flats", Direction.SOUTH),
                LocationConnection("sandstone_arch_natural", Direction.NORTH),
                LocationConnection("ancient_lakebed", Direction.SOUTH),
                LocationConnection("scorpion_gulch", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 7
        ),
        
        Location(
            id = "ancient_lakebed",
            name = "Ancient Lakebed",
            description = LocationDescription.simple(
                "A vast flat expanse marks where a lake existed thousands of years ago. The lakebed is cracked into polygonal tiles, each crack deep enough for you to fall into. During rare rains, the lakebed briefly holds water again, creating a shallow temporary lake. But most of the time it's dry, baked hard, reflecting heat like a mirror. Ancient shorelines are visible as faint terraces on surrounding hills."
            ),
            biome = BiomeType.DESERT,
            gridX = 5,
            gridY = -4,
            connections = listOf(
                LocationConnection("sun_bleached_basin", Direction.EAST),
                LocationConnection("dust_devil_crossing", Direction.NORTH),
                LocationConnection("gila_monster_rocks", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "sandstone_arch_natural",
            name = "Natural Sandstone Arch",
            description = LocationDescription.simple(
                "Wind and water have carved a perfect arch through a sandstone fin. The arch is delicate-looking but has stood for millennia. Its span is wide enough for you to walk through comfortably. The arch frames distant vistas dramatically, creating natural picture frames of desert landscape. Sunrise and sunset through the arch are spectacular, the stone glowing orange and red."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -2,
            connections = listOf(
                LocationConnection("scorpion_gulch", Direction.EAST),
                LocationConnection("hoodoo_forest", Direction.SOUTH),
                LocationConnection("balanced_rock_formation", Direction.SOUTH),
                LocationConnection("dust_devil_crossing", Direction.SOUTH),
                LocationConnection("hoodoo_forest", Direction.WEST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 7
        ),

        // ==================== SUB-REGION 4C: Desert Canyons (10 locations, levels 6-10) ====================
        // Grid: X: 2-5, Y: -4 to -2
        // Theme: Narrow gorges, flash flood danger, shade pockets, dramatic geology
        
        Location(
            id = "echo_canyon",
            name = "Echo Canyon",
            description = LocationDescription.simple(
                "A narrow slot canyon with walls that rise vertically on both sides. Sound behaves strangely here—every footstep, every chirp echoes repeatedly, creating a confusing auditory landscape. Shout and you hear your voice return from a dozen directions. The acoustics are so perfect that whispers from far down the canyon sound like they're right next to your ear. The constant echoes make stealth impossible."
            ),
            biome = BiomeType.DESERT,
            gridX = 5,
            gridY = -6,
            connections = listOf(
                LocationConnection("fossil_bed_canyon", Direction.EAST),
                LocationConnection("slot_canyon_entrance", Direction.WEST),
                LocationConnection("flash_flood_channel", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "slot_canyon_entrance",
            name = "Slot Canyon Entrance",
            description = LocationDescription.simple(
                "The canyon narrows to barely shoulder-width (for you, that's comfortable), but continues to deepen. Walls rise hundreds of times your height on either side, smooth and sculpted by ancient floods. The slot is dim even at noon, sunlight only reaching the bottom for brief moments. Polished stones litter the floor, rounded by water that hasn't flowed here in decades—but could return at any moment."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -5,
            connections = listOf(
                LocationConnection("gila_monster_rocks", Direction.EAST),
                LocationConnection("window_rock", Direction.WEST),
                LocationConnection("echo_canyon", Direction.EAST),
                LocationConnection("rattlesnake_den", Direction.WEST),
                LocationConnection("slot_canyon_narrows", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "slot_canyon_narrows",
            name = "Slot Canyon Narrows",
            description = LocationDescription.simple(
                "The canyon squeezes even tighter, becoming a sinuous crack in the earth. You can touch both walls simultaneously. The rock is sculpted into flowing curves, striped in reds, oranges, and purples. Light filters down from above in shafts, illuminating dust motes. The beauty is breathtaking—but also ominous. If it rains miles away, a flash flood could fill this slot in minutes, with no escape."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -6,
            connections = listOf(
                LocationConnection("slot_canyon_entrance", Direction.NORTH),
                LocationConnection("amphitheater_chamber", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 8,
            lore = "Flash floods in slot canyons are the desert's deadliest hazard. The walls are too smooth to climb, and water can rise fifteen feet in seconds. Never enter a slot canyon if rain threatens anywhere in the watershed."
        ),
        
        Location(
            id = "amphitheater_chamber",
            name = "Amphitheater Chamber",
            description = LocationDescription.simple(
                "The slot canyon suddenly opens into a circular chamber carved by swirling floodwaters. The walls curve inward, creating natural acoustics that amplify sound. A small pool of clear water fills the chamber's center—rainwater trapped in the rock. The chamber's beauty is cathedral-like, with fluted walls and a ceiling open to the sky. It's a natural sanctuary in the heart of the desert."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -7,
            connections = listOf(
                LocationConnection("slot_canyon_narrows", Direction.NORTH),
                LocationConnection("petroglyph_gallery", Direction.WEST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 8,
            isSafeZone = true
        ),
        
        Location(
            id = "flash_flood_channel",
            name = "Flash Flood Channel",
            description = LocationDescription.simple(
                "Debris piled against rocks tells the story of violent flash floods. Tree branches, bones, and boulders are wedged high above the current canyon floor—evidence of water levels during floods. The channel is dry now, but stained rock shows how high water can rise. Navigating requires climbing over and around flood debris, a chaotic obstacle course."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -5,
            connections = listOf(
                LocationConnection("echo_canyon", Direction.SOUTH),
                LocationConnection("sun_bleached_basin", Direction.NORTH),
                LocationConnection("canyon_overlook", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "rattlesnake_den",
            name = "Rattlesnake Den",
            description = LocationDescription.simple(
                "A cave at the canyon's base serves as winter den for dozens of rattlesnakes. During cold months, the snakes huddle together for warmth. In summer, they disperse, but some always remain. The cave floor writhes with serpentine bodies, and the rattling of their tails creates an ominous percussion. Approaching is extremely dangerous—multiple snakes will strike if threatened."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -5,
            connections = listOf(
                LocationConnection("slot_canyon_entrance", Direction.EAST),
                LocationConnection("palm_oasis_minor", Direction.NORTH),
                LocationConnection("petroglyph_gallery", Direction.SOUTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 9
        ),
        
        Location(
            id = "petroglyph_gallery",
            name = "Petroglyph Gallery",
            description = LocationDescription.simple(
                "Ancient peoples carved images into the canyon walls: spirals, handprints, bighorn sheep, mysterious symbols. The petroglyphs cover hundreds of feet of rock face, telling stories in a language lost to time. Some images are clearly representational; others abstract and cryptic. The gallery is a sacred site, a connection to humans who walked this desert millennia before recorded history."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -6,
            connections = listOf(
                LocationConnection("rattlesnake_den", Direction.NORTH),
                LocationConnection("amphitheater_chamber", Direction.EAST),
                LocationConnection("ancestor_shrine", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 8,
            lore = "Archaeologists have dated some petroglyphs here to over 5,000 years old. The images include extinct animals, suggesting the desert was once far more hospitable. The site is protected, though enforcement this remote is difficult."
        ),
        
        Location(
            id = "ancestor_shrine",
            name = "Ancestor Shrine",
            description = LocationDescription.simple(
                "A small alcove in the canyon wall contains offerings: pottery shards, woven baskets, stone tools, and faded textiles. This shrine has been maintained for generations, with new offerings added regularly by descendants of the desert's original peoples. Standing here, you feel the weight of centuries, the unbroken line of reverence connecting past to present."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -7,
            connections = listOf(
                LocationConnection("petroglyph_gallery", Direction.NORTH),
                LocationConnection("needle_spires", Direction.WEST)
            ),
            encounterRate = 0.30,
            recommendedLevel = 8,
            isSafeZone = true,
            lore = "The shrine is considered sacred ground. Even the most hardened desert creatures seem to avoid disturbing it. Whether this is coincidence or something more mysterious is a matter of debate."
        ),
        
        Location(
            id = "canyon_overlook",
            name = "Canyon Overlook",
            description = LocationDescription.simple(
                "A precarious ledge overhangs the canyon, providing a stunning vista of the labyrinthine gorges below. The drop is dizzying, and the ledge is barely wide enough for comfortable standing. Wind gusts threaten to push you over the edge. But the view is worth the danger: a maze of canyons stretches to the horizon, their walls glowing in shades of red and orange."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -4,
            connections = listOf(
                LocationConnection("flash_flood_channel", Direction.EAST),
                LocationConnection("balanced_rock_formation", Direction.NORTH),
                LocationConnection("devils_playground", Direction.SOUTHWEST),
                LocationConnection("mushroom_rock_garden", Direction.SOUTH),
                LocationConnection("sun_bleached_basin", Direction.NORTH),
                LocationConnection("hoodoo_forest", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "balanced_rock_formation",
            name = "Balanced Rock Formation",
            description = LocationDescription.simple(
                "Massive boulders balance impossibly on narrow stone pedestals, defying gravity and common sense. Erosion has undercut the supporting pillars, leaving the boulders perched like they could topple at any moment. Some are house-sized, others merely car-sized (both scales enormous from your perspective). The formations are temporary on geological timescales—eventually they'll fall. Whether that's today or in a thousand years is unknown."
            ),
            biome = BiomeType.DESERT,
            gridX = 4,
            gridY = -3,
            connections = listOf(
                LocationConnection("canyon_overlook", Direction.SOUTH),
                LocationConnection("sandstone_arch_natural", Direction.NORTH),
                LocationConnection("hoodoo_forest", Direction.SOUTHWEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),

        // ==================== SUB-REGION 4D: Oasis Network (10 locations, levels 5-8) ====================
        // Grid: X: 0-3, Y: -3 to -1
        // Theme: Life-sustaining water sources, palm clusters, contrast between lush and barren
        
        Location(
            id = "palm_oasis_minor",
            name = "Minor Palm Oasis",
            description = LocationDescription.simple(
                "A small spring feeds a cluster of palm trees, creating an island of green in the sand sea. The palms are not as grand as at larger oases, but they provide vital shade and sustenance. Date clusters hang within reach. Small birds nest in the fronds, filling the air with song. The spring water pools in a natural basin—clear, cold, and incredibly precious."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -4,
            connections = listOf(
                LocationConnection("date_palm_hideaway", Direction.NORTH),
                LocationConnection("devils_playground", Direction.EAST),
                LocationConnection("irrigated_gulch", Direction.WEST),
                LocationConnection("rattlesnake_den", Direction.SOUTH),
                LocationConnection("desert_spring_hidden", Direction.WEST),
                LocationConnection("oasis_verdant", Direction.NORTHEAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 5,
            isSafeZone = true
        ),
        
        Location(
            id = "irrigated_gulch",
            name = "Irrigated Gulch",
            description = LocationDescription.simple(
                "An underground aquifer surfaces in this narrow gulch, creating a ribbon of moisture that sustains unexpected plant life. Ferns, grasses, and even a few wildflowers thrive here. The contrast with the surrounding desert is stark—step five feet to either side and you're back in barren wasteland. Animals know this gulch; their tracks converge here from all directions."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -3,
            connections = listOf(
                LocationConnection("oasis_verdant", Direction.NORTH),
                LocationConnection("palm_oasis_minor", Direction.EAST),
                LocationConnection("mesquite_thicket", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 6
        ),
        
        Location(
            id = "mesquite_thicket",
            name = "Mesquite Thicket",
            description = LocationDescription.simple(
                "Dense mesquite trees grow in a tangled thicket, their roots tapping deep groundwater. The trees are armed with vicious thorns, making passage difficult and painful. But beneath the thorny canopy, the shade is deep and temperatures are bearable. Seed pods litter the ground—nutritious and sweet. Many desert creatures make their homes in this thorny fortress."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -3,
            connections = listOf(
                LocationConnection("irrigated_gulch", Direction.EAST),
                LocationConnection("ocotillo_fence", Direction.NORTH),
                LocationConnection("heat_shimmer_flats", Direction.NORTH),
                LocationConnection("smoke_tree_wash", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6
        ),
        
        Location(
            id = "smoke_tree_wash",
            name = "Smoke Tree Wash",
            description = LocationDescription.simple(
                "A dry wash lined with smoke trees—so named because their gray-green foliage looks like smoke from a distance. The wash only flows water after rare rains, but the trees' roots tap moisture far below. In spring, the smoke trees burst into purple blooms, transforming the drab wash into a violet wonderland. The rest of the year, it's merely pleasant shade."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -4,
            connections = listOf(
                LocationConnection("mesquite_thicket", Direction.NORTH),
                LocationConnection("desert_spring_hidden", Direction.EAST),
                LocationConnection("palo_verde_stand", Direction.WEST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),
        
        Location(
            id = "palo_verde_stand",
            name = "Palo Verde Stand",
            description = LocationDescription.simple(
                "Green-barked palo verde trees grow in a loose cluster, their chlorophyll-filled trunks allowing photosynthesis even without leaves. During droughts, the trees drop their leaves to conserve water, relying on their green bark. In good times, they leaf out and bloom in masses of yellow flowers. The stand provides crucial shade and food for desert wildlife."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -4,
            connections = listOf(
                LocationConnection("smoke_tree_wash", Direction.EAST),
                LocationConnection("ironwood_grove", Direction.SOUTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "ironwood_grove",
            name = "Ironwood Grove",
            description = LocationDescription.simple(
                "Ancient ironwood trees—some over 500 years old—create a grove of living sculpture. Ironwood is incredibly dense, hard as its name suggests. Dead branches don't decay for decades, creating a tangle of living and dead wood. The trees bloom in purple flowers that contrast beautifully with the blue-gray foliage. This grove is a cathedral of time and resilience."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -5,
            connections = listOf(
                LocationConnection("palo_verde_stand", Direction.NORTH),
                LocationConnection("window_rock", Direction.EAST),
                LocationConnection("labyrinth_mesa", Direction.NORTH),
                LocationConnection("needle_spires", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 7,
            lore = "Ironwood is so dense it doesn't float in water. Desert peoples valued it for tools that lasted generations. These groves are protected now, but centuries of harvest have made them rare."
        ),
        
        Location(
            id = "century_plant_field",
            name = "Century Plant Field",
            description = LocationDescription.simple(
                "Agave plants—misnamed 'century plants' though they live only 20-30 years—cover a hillside. Most are low rosettes of spiky leaves. But some have sent up flowering stalks that tower like flagpoles, each stalk taller than you many times over. The plants die after flowering, their energy exhausted. The field is a mix of living plants, blooming stalks, and dead husks."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -2,
            connections = listOf(
                LocationConnection("oasis_verdant", Direction.EAST),
                LocationConnection("fairy_duster_meadow", Direction.EAST),
                LocationConnection("heat_shimmer_flats", Direction.SOUTH),
                LocationConnection("tumbleweed_crossing", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5
        ),
        
        Location(
            id = "fairy_duster_meadow",
            name = "Fairy Duster Meadow",
            description = LocationDescription.simple(
                "Low shrubs covered in pink, fluffy flowers create a meadow of unexpected beauty. The flowers look like tiny feather dusters—hence the name. Hummingbirds and bees work the blooms in frantic activity. The meadow blooms several times a year if rain permits, each bloom transforming the drab desert into a pink wonderland. The scent is sweet and attracts visitors from miles around."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -2,
            connections = listOf(
                LocationConnection("oasis_verdant", Direction.SOUTH),
                LocationConnection("desert_lavender_patch", Direction.NORTH),
                LocationConnection("century_plant_field", Direction.WEST),
                LocationConnection("dunes_sea", Direction.EAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 5
        ),
        
        Location(
            id = "ocotillo_fence",
            name = "Ocotillo Fence",
            description = LocationDescription.simple(
                "Ocotillo plants grow in a dense line, their spiny stems creating a natural fence. Most of the year, the ocotillos are bare spiny sticks. But after rain, they burst into leaf and flower, their tips crowned with red blooms that hummingbirds adore. The fence is an effective barrier—the spines are numerous and sharp. Getting through requires finding a gap or braving the thorns."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -1,
            connections = listOf(
                LocationConnection("heat_shimmer_flats", Direction.EAST),
                LocationConnection("mesquite_thicket", Direction.SOUTH),
                LocationConnection("tumbleweed_crossing", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6
        ),
        
        Location(
            id = "desert_lavender_patch",
            name = "Desert Lavender Patch",
            description = LocationDescription.simple(
                "Fragrant desert lavender shrubs fill a sheltered pocket, their purple flowers and aromatic leaves creating a sensory oasis. The scent is intense, especially in the heat—a mix of lavender, mint, and something uniquely desert. Butterflies and bees work the flowers constantly. Standing in this patch, surrounded by purple blooms and intoxicating scent, it's easy to forget the harsh desert surrounding this pocket paradise."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -1,
            connections = listOf(
                LocationConnection("oasis_verdant", Direction.WEST),
                LocationConnection("fairy_duster_meadow", Direction.SOUTH),
                LocationConnection("dunes_sea", Direction.EAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 5
        ),

        // ==================== SUB-REGION 4E: Sandstone Formations (10 locations, levels 8-12) ====================
        // Grid: X: 0-2, Y: -4 to -2
        // Theme: Eroded monuments, arches, balanced rocks, dramatic geology
        
        Location(
            id = "needle_spires",
            name = "Needle Spires",
            description = LocationDescription.simple(
                "Thin towers of sandstone rise like needles, some over a hundred feet tall but only a few feet wide at the base. Erosion has sculpted these spires from harder rock layers, leaving behind these improbable formations. Some spires have toppled, leaving rubble fields. Others lean at alarming angles, defying gravity. Navigating between the spires is like walking through a stone forest."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -6,
            connections = listOf(
                LocationConnection("ancestor_shrine", Direction.EAST),
                LocationConnection("window_rock", Direction.NORTH),
                LocationConnection("ironwood_grove", Direction.WEST),
                LocationConnection("arch_of_ages", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "arch_of_ages",
            name = "Arch of Ages",
            description = LocationDescription.simple(
                "A massive natural arch spans a canyon, its opening wide enough to drive a truck through. The arch is layered stone, each stripe a different era of deposition. Standing beneath it, you feel the weight of geological time—this arch took millions of years to form and will eventually collapse, but probably not today. Hopefully not today. Light through the arch creates dramatic shadows that shift throughout the day."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -7,
            connections = listOf(
                LocationConnection("needle_spires", Direction.NORTH),
                LocationConnection("hoodoo_forest", Direction.EAST),
                LocationConnection("sandstone_ruins", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10,
            lore = "Geologists estimate this arch is stable for another 500-1000 years. But arches can collapse suddenly, without warning. Several famous arches have fallen in recent decades, reminding us that even stone is temporary."
        ),
        
        Location(
            id = "hoodoo_forest",
            name = "Hoodoo Forest",
            description = LocationDescription.simple(
                "Bizarre rock pillars called hoodoos crowd together like a petrified forest. Each hoodoo has a different shape—some wear capstones like hats, others are smooth columns, still others are fantastically twisted. The hoodoos cast complex shadows, creating a maze of light and dark. Wind howling through the formations creates an eerie music, a song of stone and air."
            ),
            biome = BiomeType.DESERT,
            gridX = 2,
            gridY = -7,
            connections = listOf(
                LocationConnection("arch_of_ages", Direction.WEST),
                LocationConnection("sandstone_arch_natural", Direction.EAST),
                LocationConnection("canyon_overlook", Direction.EAST),
                LocationConnection("sandstone_arch_natural", Direction.NORTH),
                LocationConnection("balanced_rock_formation", Direction.NORTHEAST),
                LocationConnection("mushroom_rock_garden", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10
        ),
        
        Location(
            id = "mushroom_rock_garden",
            name = "Mushroom Rock Garden",
            description = LocationDescription.simple(
                "Rocks shaped like giant mushrooms dot the landscape—narrow stems supporting wide caps. Differential erosion created these shapes: the cap rock is harder, protecting the softer stone beneath. Some mushrooms are delicately balanced, others thick and sturdy. The garden is surreal, like something from a fever dream. You half expect the stones to start walking."
            ),
            biome = BiomeType.DESERT,
            gridX = 3,
            gridY = -7,
            connections = listOf(
                LocationConnection("hoodoo_forest", Direction.WEST),
                LocationConnection("canyon_overlook", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "window_rock",
            name = "Window Rock",
            description = LocationDescription.simple(
                "A perfectly circular hole through a rock wall creates a natural window. The hole is large enough for you to walk through easily. Through the window, you can see framed vistas of distant landscapes—the composition changes as you move, each position offering a new view. Sunrise and sunset through the window are spectacular, the hole serving as a natural viewfinder for celestial events."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -5,
            connections = listOf(
                LocationConnection("needle_spires", Direction.SOUTH),
                LocationConnection("devils_playground", Direction.NORTH),
                LocationConnection("labyrinth_mesa", Direction.WEST),
                LocationConnection("twin_buttes", Direction.WEST),
                LocationConnection("ironwood_grove", Direction.WEST),
                LocationConnection("slot_canyon_entrance", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 8
        ),
        
        Location(
            id = "labyrinth_mesa",
            name = "Labyrinth Mesa",
            description = LocationDescription.simple(
                "A flat-topped mesa is cut by countless narrow canyons, creating a three-dimensional maze. The canyons are just wide enough to navigate, their walls closing in overhead. Getting lost in the labyrinth is easy; finding your way out requires careful navigation. Dead ends abound, and many passages loop back on themselves. But the solitude and beauty make the challenge worthwhile."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -5,
            connections = listOf(
                LocationConnection("window_rock", Direction.EAST),
                LocationConnection("twin_buttes", Direction.SOUTH),
                LocationConnection("ironwood_grove", Direction.SOUTH),
                LocationConnection("painted_cliffs", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9
        ),
        
        Location(
            id = "painted_cliffs",
            name = "Painted Cliffs",
            description = LocationDescription.simple(
                "Layered sandstone cliffs display every color of the earth rainbow—reds, oranges, yellows, whites, purples, even greens. Each layer represents a different geological period, a different ancient environment. The colors are vivid, almost garish, intensified by sun and shadow. Artists and photographers make pilgrimages here to capture the spectacular palette nature has painted on stone."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -6,
            connections = listOf(
                LocationConnection("labyrinth_mesa", Direction.EAST),
                LocationConnection("twin_buttes", Direction.NORTH),
                LocationConnection("wave_rock", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 10,
            lore = "Each color in the painted cliffs tells a story: red from iron oxide, white from gypsum, green from copper minerals. Reading the rocks, geologists can reconstruct ancient climates, seas, and deserts that existed millions of years before the present desert."
        ),
        
        Location(
            id = "wave_rock",
            name = "Wave Rock",
            description = LocationDescription.simple(
                "A rock face carved by wind into the shape of a frozen wave, complete with crest and curl. The similarity to ocean waves is uncanny and disorienting—your mind expects to hear surf, smell salt, feel spray. But it's solid stone, locked in place. The 'wave' is large enough that you could stand on its crest. The formation is a testament to wind's patient power."
            ),
            biome = BiomeType.DESERT,
            gridX = -1,
            gridY = -7,
            connections = listOf(
                LocationConnection("painted_cliffs", Direction.NORTH),
                LocationConnection("sandstone_ruins", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 11
        ),
        
        Location(
            id = "twin_buttes",
            name = "Twin Buttes",
            description = LocationDescription.simple(
                "Two nearly identical buttes rise from the desert floor like symmetrical towers. The buttes are separated by a narrow gap just wide enough to walk through. Each butte is layered stone, with resistant caprock protecting softer layers beneath. Climbing either butte provides spectacular views—and a healthy respect for heights. Local legends claim the buttes were once a single mountain, split by a giant's axe."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -6,
            connections = listOf(
                LocationConnection("labyrinth_mesa", Direction.NORTH),
                LocationConnection("painted_cliffs", Direction.SOUTH),
                LocationConnection("window_rock", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "devils_playground",
            name = "Devil's Playground",
            description = LocationDescription.simple(
                "A field of strangely eroded rocks creates a landscape of stone chaos. Rocks are balanced, stacked, arched, and scattered in seemingly random patterns. Some rocks look carved by intelligent hands; others are clearly natural but look artificial. The playground is aptly named—it looks like a titan child's abandoned game. Navigation is confusing, as the rock formations all look similar but subtly different."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -4,
            connections = listOf(
                LocationConnection("window_rock", Direction.SOUTH),
                LocationConnection("palm_oasis_minor", Direction.WEST),
                LocationConnection("canyon_overlook", Direction.NORTHEAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),

        // ==================== SUB-REGION 4F: Buried Complexes (5 locations, levels 10-15) ====================
        // Grid: Underground levels connected via DOWN
        // Theme: Ancient civilizations buried by time, tombs, curses, undead
        
        Location(
            id = "tomb_entrance_steps",
            name = "Tomb Entrance Steps",
            description = LocationDescription.simple(
                "Stone steps descend into the earth, leading to chambers buried beneath the desert. The steps are weathered smooth by centuries of use and sand. Hieroglyphs cover the walls, warnings and prayers in a dead language. The air growing cooler as you descend, carrying the scent of ancient dust and stone. This entrance has been sealed and unsealed countless times, by archaeologists, looters, and curious explorers."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -5,
            connections = listOf(
                LocationConnection("sandstone_ruins", Direction.UP),
                LocationConnection("pharaohs_antechamber", Direction.DOWN)
            ),
            encounterRate = 0.75,
            recommendedLevel = 11
        ),
        
        Location(
            id = "pharaohs_antechamber",
            name = "Pharaoh's Antechamber",
            description = LocationDescription.simple(
                "A vast chamber carved from bedrock, its walls covered in painted scenes of gods and kings. Massive columns support the ceiling, each pillar thick enough that you couldn't wrap your arms around it even if you were human-sized. Broken pottery and looted treasures litter the floor—this tomb was robbed millennia ago. But the paintings remain, telling stories of a civilization that turned sand into empire."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -6,
            connections = listOf(
                LocationConnection("tomb_entrance_steps", Direction.UP),
                LocationConnection("forgotten_catacombs", Direction.EAST),
                LocationConnection("cursed_vault", Direction.SOUTH),
                LocationConnection("mummy_preparation_hall", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 12,
            lore = "The pharaoh buried here reigned 3,000 years ago. Their name has been chiseled from every inscription—a damnatio memoriae inflicted by a successor. Now only the defaced images remain, a nameless ruler in a plundered tomb."
        ),
        
        Location(
            id = "cursed_vault",
            name = "Cursed Vault",
            description = LocationDescription.simple(
                "A sealed chamber that was never meant to be opened. The door bears warnings in multiple languages: 'Death to all who enter,' 'Turn back while you live,' 'The curse is real.' Someone ignored the warnings and broke the seal. Inside, treasures gleam in lamplight—gold, jewels, artifacts of incredible beauty. But there are also bones, their positions suggesting the deceased died in agony. The air feels wrong here, heavy and malevolent."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -7,
            connections = listOf(
                LocationConnection("pharaohs_antechamber", Direction.NORTH),
                LocationConnection("desert_trap_gauntlet", Direction.SOUTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 14,
            lore = "Every archaeologist who opened this vault died within a year—accidents, illnesses, and one murder. Coincidence or curse? Scientists scoff, but locals refuse to enter. The treasure remains, unclaimed and untouched."
        ),
        
        Location(
            id = "mummy_preparation_hall",
            name = "Mummy Preparation Hall",
            description = LocationDescription.simple(
                "Stone tables fill this chamber where bodies were prepared for eternity. Canopic jars—containers for preserved organs—line the walls, their lids shaped like animal heads. The scent of ancient natron and resins lingers despite millennia. Mummified bodies lie on some tables, their wrappings partially unwound. Tools of the embalmer's trade remain: bronze hooks, obsidian blades, and linen wraps."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -6,
            connections = listOf(
                LocationConnection("pharaohs_antechamber", Direction.EAST),
                LocationConnection("desert_bone_maze", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 13,
            lore = "Mummification was sacred science, practiced by specialists who guarded their secrets zealously. The process took 70 days and cost a fortune—ensuring only the elite achieved preserved immortality."
        ),
        
        Location(
            id = "desert_trap_gauntlet",
            name = "Desert Trap Gauntlet",
            description = LocationDescription.simple(
                "A corridor designed to kill intruders. Pressure plates trigger arrow traps, ceiling blocks crash down, floor panels drop away to reveal pits. Some traps still function after thousands of years; others have failed. Skeletons of ancient and modern looters prove the traps' effectiveness. Navigating requires careful observation, quick reflexes, and a bit of luck. One wrong step could be your last."
            ),
            biome = BiomeType.DESERT,
            gridX = 1,
            gridY = -8,
            connections = listOf(
                LocationConnection("cursed_vault", Direction.NORTH),
                LocationConnection("desert_bone_maze", Direction.WEST)
            ),
            encounterRate = 0.95,
            recommendedLevel = 15,
            lore = "Tomb builders competed to create the most ingenious traps. This gauntlet is considered a masterwork—seven different trap types in one corridor. Engineers study it to understand ancient mechanical ingenuity."
        ),
        
        Location(
            id = "desert_bone_maze",
            name = "Desert Bone Maze",
            description = LocationDescription.simple(
                "Walls constructed entirely of human bones create a labyrinth beneath the desert. Skulls form pillars, femurs create archways, ribs pattern the ceiling. The maze is both architecture and art, macabre and mesmerizing. Who built it and why? Theories range from mass burial to ritual sacrifice to artistic obsession. The bones belong to thousands of individuals, arranged with geometric precision."
            ),
            biome = BiomeType.DESERT,
            gridX = 0,
            gridY = -7,
            connections = listOf(
                LocationConnection("mummy_preparation_hall", Direction.NORTH),
                LocationConnection("desert_trap_gauntlet", Direction.EAST),
                LocationConnection("deep_dark", Direction.DOWN)
            ),
            encounterRate = 0.90,
            recommendedLevel = 14,
            lore = "Anthropologists estimate 10,000+ individuals contributed bones to this maze. Whether they were honored dead or sacrificial victims is hotly debated. The maze's purpose remains one of archaeology's enduring mysteries."
        )
    )
}
