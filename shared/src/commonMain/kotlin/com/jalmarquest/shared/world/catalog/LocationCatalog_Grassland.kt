package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * Grassland region locations for JalmarQuest world expansion.
 * Contains ~90 grassland locations across 6 sub-regions.
 * 
 * Sub-regions:
 * - 1A: Buttonburgh Outskirts (15 locations, Levels 1-3)
 * - 1B: Wildflower Plains (20 locations, Levels 2-5)
 * - 1C: Southern Prairies (15 locations, Levels 3-6)
 * - 1D: Western Meadows (15 locations, Levels 2-5)
 * - 1E: Far Northern Fields (15 locations, Levels 4-8)
 * - 1F: Eastern Grazing Lands (10 locations, Levels 5-9)
 */
internal val GRASSLAND_LOCATIONS: List<Location> by lazy {
    listOf(
        // ========== SUB-REGION 1A: BUTTONBURGH OUTSKIRTS (15 locations) ==========
        
        Location(
            id = "pebble_plaza",
            name = "Pebble Plaza",
            description = LocationDescription.withAllSeasons(
                spring = "What the humans call their 'gravel driveway' is, to you, a magnificent plaza paved with smooth stones. Each pebble is a work of geological art, warm in the spring sun. Sparrows conduct business here—hopping about importantly, pecking at seeds scattered between the stones. A particularly round pebble serves as the 'Negotiation Stone' where territorial disputes are settled. The humans' car occasionally rumbles through like a mechanical leviathan, but you've learned its schedule.",
                summer = "The Pebble Plaza shimmers with heat waves rising from sun-baked stones. This is dust-bathing heaven—the fine gravel powder between pebbles creates perfect wallowing pits. You've claimed the prime spot near the humans' tire tracks, where the stones retain warmth well into evening. A robin eyes your territory jealously from the fence. The afternoon sun makes the whole plaza glow like a field of tiny moons.",
                autumn = "Fallen leaves invade Pebble Plaza, turning it into an obstacle course of orange and brown. The seeds scattered here are premium quality—the humans have been careless with their bird feeder again. Profit. The pebbles are cool now, perfect for morning patrols. You've noticed the ants are storing food beneath certain stones—a supply chain worth monitoring.",
                winter = "Pebble Plaza transforms into a treacherous ice rink. Each stone wears a glaze of frost, and your tiny claws slip with indignant frequency. The humans have scattered salt (you avoid it—tastes terrible), creating safe pathways between the frozen zones. A chickadee slipped yesterday. You pretended not to see. Professional courtesy."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = -1,
            connections = listOf(
                LocationConnection("starting_village", Direction.NORTH),
                LocationConnection("buffalo_grass_expanse", Direction.SOUTH),
                LocationConnection("dandelion_grove", Direction.EAST),
                LocationConnection("puddle_lake", Direction.WEST)
            ),
            encounterRate = 0.1,
            recommendedLevel = 1,
            lore = "The Pebble Plaza has seen more dramatic negotiations than most human parliaments. Three species of sparrows, two types of finches, and one very territorial robin maintain a fragile peace here."
        ),
        
        Location(
            id = "dandelion_grove",
            name = "Dandelion Grove",
            description = LocationDescription.simple(
                "To the humans, this is 'that patch of lawn we forgot to mow.' To you, it's an enchanted forest. Dandelions tower overhead like ancient oaks, their yellow crowns blazing in the sun. The stems are thick as tree trunks (to a button quail), and between them grows a carpet of clover and wild grass. Bees work the flowers with industrial efficiency—you give them respectful distance. When the dandelions go to seed, this grove becomes a snow globe of white fluff, swirling with every breeze. Pure magic."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = -1,
            connections = listOf(
                LocationConnection("pebble_plaza", Direction.WEST),
                LocationConnection("starting_village", Direction.NORTH),
                LocationConnection("garden_gnome_shadow", Direction.NORTHEAST)
            ),
            encounterRate = 0.2,
            recommendedLevel = 1
        ),
        
        Location(
            id = "puddle_lake",
            name = "The Puddle Lake",
            description = LocationDescription.withAllSeasons(
                spring = "After the rain, this low spot in the yard becomes Lake Magnificence—a glittering expanse of water that stretches nearly two feet across. The edges are treacherous mud, perfect for leaving tiny footprints. You can see your reflection in the surface, looking appropriately heroic. Earthworms surface around the perimeter like confused sea monsters. The puddle will last for days, a temporary inland sea in your domain.",
                summer = "The Great Drought has claimed Puddle Lake. Only a damp patch remains, cracked mud forming miniature canyons. You search for remaining earthworms in the dry lakebed—archaeological expedition. A few stubborn puddles persist in the deepest spots, warm as bathwater, claimed by mosquito larvae. The humans' sprinkler sometimes refills the basin, creating brief floods.",
                autumn = "Puddle Lake has returned with vengeance! Autumn rains keep it perpetually full, leaves floating on the surface like miniature boats. You've learned to navigate the shore carefully—one slip means wet feathers and indignity. A toad has taken up residence, croaking proprietarily. You've reached an understanding: the toad keeps the bugs down, you don't judge its warty appearance.",
                winter = "Puddle Lake has frozen solid, becoming Ice Sheet Terror. The surface is clear enough to see blades of grass trapped beneath like prehistoric amber. You test it carefully with one claw—it holds. You slide across, momentum carrying you faster than intended, and crash into a twig. The chickadees are laughing. They absolutely saw that."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = -1,
            connections = listOf(
                LocationConnection("pebble_plaza", Direction.EAST),
                LocationConnection("flagstone_patio", Direction.SOUTHWEST),
                LocationConnection("vegetable_garden_edge", Direction.SOUTH),
                LocationConnection("clover_patch_west", Direction.WEST),
                LocationConnection("starting_village", Direction.NORTHEAST),
                LocationConnection("irrigation_ditch", Direction.WEST)
            ),
            encounterRate = 0.15,
            recommendedLevel = 1,
            lore = "Puddle Lake's ecosystem supports complex life: earthworms, mosquito larvae, one territorial toad, and occasional visiting beetles. Ecologically speaking, it's very important."
        ),
        
        Location(
            id = "garden_gnome_shadow",
            name = "Garden Gnome's Shadow",
            description = LocationDescription.simple(
                "The humans think the ceramic garden gnome is 'cute.' You know better. It looms twelve inches tall—a titan frozen in perpetual cheerfulness, holding a fishing rod over an empty pond. Its painted eyes follow you. You're sure of it. The shadow it casts is a safe zone from aerial predators but psychologically unsettling. Moss grows on its north side (you've learned this is 'navigation'). The gnome has witnessed everything in this yard for years. What has it seen? What does it know? Why is it smiling?"
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 2,
            connections = listOf(
                LocationConnection("starting_village", Direction.SOUTHWEST),
                LocationConnection("stepping_stone_path", Direction.WEST),
                LocationConnection("dandelion_grove", Direction.SOUTHWEST),
                LocationConnection("compost_heap_foothills", Direction.WEST),
                LocationConnection("seedling_nursery", Direction.NORTH)
            ),
            encounterRate = 0.3,
            recommendedLevel = 2,
            lore = "Local legend says the garden gnome was placed by the humans' grandmother forty years ago. It has survived storms, floods, and three different lawn mower incidents. It is eternal. It is watching."
        ),
        
        Location(
            id = "compost_heap_foothills",
            name = "Compost Heap Foothills",
            description = LocationDescription.simple(
                "The humans' compost bin is a mountain of organic decay that radiates warmth year-round. The smell is, frankly, incredible—rotting vegetables, grass clippings, coffee grounds, and things you can't identify but definitely approve of. The pile is alive with activity: beetles, worms, flies, and one very fat rat named Gerald (you've established diplomatic relations). The foothills around the heap are scattered with mulch, providing excellent dust-bathing material. This is prime real estate."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 2,
            connections = listOf(
                LocationConnection("starting_village", Direction.SOUTHEAST),
                LocationConnection("mulch_mountain", Direction.SOUTHWEST),
                LocationConnection("rain_barrel_reservoir", Direction.EAST),
                LocationConnection("garden_gnome_shadow", Direction.EAST),
                LocationConnection("wheelbarrow_graveyard", Direction.WEST),
                LocationConnection("wildflower_border", Direction.NORTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 2
        ),
        
        Location(
            id = "seedling_nursery",
            name = "The Seedling Nursery",
            description = LocationDescription.simple(
                "The humans have planted something in neat rows with little wooden markers. You can't read, but you respect the organization. Tiny green shoots emerge from dark soil—fragile, tender, probably delicious. The humans check these obsessively, which means they're important. You patrol the perimeter, keeping the neighborhood cat away (it was eyeing the nursery as a litter box). The soil is freshly turned, perfect for finding bugs. This is honest work."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 3,
            connections = listOf(
                LocationConnection("garden_gnome_shadow", Direction.SOUTH),
                LocationConnection("morning_dew_meadow", Direction.WEST),
                LocationConnection("black_eyed_susan_stand", Direction.NORTH),
                LocationConnection("butterfly_migration_route", Direction.NORTH),
                LocationConnection("purple_verbena_valley", Direction.EAST),
                LocationConnection("thistle_forest", Direction.NORTHEAST),
                LocationConnection("forgotten_flowerpot", Direction.WEST),
                LocationConnection("crossroads", Direction.WEST),
                LocationConnection("morning_dew_meadow", Direction.NORTH)
            ),
            isSettlement = false,
            encounterRate = 0.25,
            recommendedLevel = 2
        ),
        
        Location(
            id = "wheelbarrow_graveyard",
            name = "Wheelbarrow Graveyard",
            description = LocationDescription.simple(
                "Three rusted wheelbarrows lean against the shed in various states of decay. One is missing a wheel, one has a cracked bucket, and one is simply too old to remember what happened. They've become habitat: spiders nest in the corners, mice store seeds in the buckets, and you've found the space beneath them excellent for shelter during sudden rain. The humans say they'll 'fix them eventually.' You've been here six months. The wheelbarrows aren't going anywhere."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = 1,
            connections = listOf(
                LocationConnection("compost_heap_foothills", Direction.EAST),
                LocationConnection("rock_garden_ruins", Direction.SOUTH),
                LocationConnection("chicken_wire_village", Direction.NORTH),
                LocationConnection("tool_shed_shadow", Direction.EAST),
                LocationConnection("windmill_farm", Direction.SOUTH),
                LocationConnection("haystack_fortress", Direction.NORTH),
                LocationConnection("scarecrow_watchtower", Direction.NORTHWEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 2
        ),
        
        Location(
            id = "irrigation_ditch",
            name = "Irrigation Ditch",
            description = LocationDescription.simple(
                "A shallow channel dug by the humans to redirect rainwater away from the house. To them, it's drainage. To you, it's a miniature river canyon. Water trickles through after rain, carving patterns in the mud. The ditch is lined with smooth stones and occasional grass clumps. You've learned to navigate it lengthwise, using it as a hidden travel route. Frogs congregate here during wet seasons. The ditch connects to Puddle Lake, making it part of a vast hydraulic network (you think very strategically about water)."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 0,
            connections = listOf(
                LocationConnection("puddle_lake", Direction.EAST),
                LocationConnection("rock_garden_ruins", Direction.WEST),
                LocationConnection("garden_hose_maze", Direction.NORTHEAST),
                LocationConnection("grain_storage_hamlet", Direction.SOUTH),
                LocationConnection("tool_shed_shadow", Direction.NORTH),
                LocationConnection("clover_patch_west", Direction.SOUTH),
                LocationConnection("windmill_farm", Direction.NORTH),
                LocationConnection("harbor_town", Direction.WEST)
            ),
            encounterRate = 0.2,
            recommendedLevel = 2
        ),
        
        Location(
            id = "wildflower_border",
            name = "Wildflower Border",
            description = LocationDescription.simple(
                "The humans planted wildflowers along the fence 'for the pollinators.' Success! The border explodes with color: purple coneflowers, black-eyed susans, bee balm, and others you can't name. Butterflies visit in squadrons. Bees work with focused intensity. You navigate the stems carefully, as everything here seems designed to be too tall for comfort. Seeds fall regularly, making this a sustainable food source. The humans smile when they see this border. You smile when you find seeds. Everyone wins."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 3,
            connections = listOf(
                LocationConnection("compost_heap_foothills", Direction.SOUTH),
                LocationConnection("mulch_mountain", Direction.WEST),
                LocationConnection("haystack_fortress", Direction.WEST),
                LocationConnection("meadow_path", Direction.EAST),
                LocationConnection("morning_dew_meadow", Direction.NORTH)
            ),
            encounterRate = 0.3,
            recommendedLevel = 2
        ),
        
        Location(
            id = "haystack_fortress",
            name = "Haystack Fortress",
            description = LocationDescription.simple(
                "Someone's old hay bales sit stacked near the fence, slowly decomposing into the earth. To you, this is a fortress: golden walls reaching skyward, tunnels burrowed by mice, strategic vantage points from the top (if you can climb it). The hay provides warmth in winter, shelter from rain, and excellent hiding spots during aerial predator alerts. A family of mice lives in the base—you've negotiated passage rights in exchange for predator warnings. The fortress smells of dry grass and summer memories."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = 2,
            connections = listOf(
                LocationConnection("wheelbarrow_graveyard", Direction.SOUTH),
                LocationConnection("ironweed_patch", Direction.NORTH),
                LocationConnection("clothesline_meadow", Direction.SOUTHWEST),
                LocationConnection("chicken_wire_village", Direction.NORTHWEST),
                LocationConnection("scarecrow_watchtower", Direction.NORTH),
                LocationConnection("wildflower_border", Direction.EAST)
            ),
            encounterRate = 0.25,
            recommendedLevel = 3
        ),
        
        Location(
            id = "scarecrow_watchtower",
            name = "Scarecrow Watchtower",
            description = LocationDescription.simple(
                "The humans' attempt at a scarecrow consists of: two sticks, a burlap sack head with button eyes, and a flannel shirt that's seen better days. It doesn't scare crows (three are perched on its arms right now), but it serves as an excellent landmark. The scarecrow stands guard over the western meadow, its shadow stretching long in morning light. You use it as a navigation reference point—'turn left at the scarecrow' is a valid direction. The crows have named it Gerald. Different Gerald from the compost rat."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -5,
            gridY = 2,
            connections = listOf(
                LocationConnection("haystack_fortress", Direction.SOUTH),
                LocationConnection("chicken_wire_village", Direction.WEST),
                LocationConnection("wheelbarrow_graveyard", Direction.SOUTHEAST),
                LocationConnection("morning_dew_meadow", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),
        
        Location(
            id = "morning_dew_meadow",
            name = "Morning Dew Meadow",
            description = LocationDescription.withAllSeasons(
                spring = "In spring mornings, this patch of untamed grass becomes a wonderland of dew drops. Each blade wears tiny crystal spheres that catch the sunrise. The grass is tall enough to brush your belly as you walk through, leaving you damp but refreshed. Spiderwebs strung between stalks become visible, outlined in water droplets—architectural marvels you carefully avoid. The meadow smells green and alive. This is what renewal feels like.",
                summer = "The morning dew evaporates within minutes during summer, but those minutes are glorious. The meadow is thick with seed heads—timothy, fescue, wild oat—all bending under moisture's weight. You time your visits for maximum dampness, using the dew as nature's bath. Crickets sing from the grass depths. The sun rises fast, burning off the magic, leaving only dry stems and memories.",
                autumn = "Autumn dew lingers longer in the cooling air. The meadow's grasses have turned gold and brown, seed heads heavy with autumn's harvest. Dew clings to spiderwebs in elaborate patterns—some webs are empty, their engineers departed or deceased. The meadow feels melancholy but beautiful, like a song about summer ending. You collect seeds from the damp stalks, planning for winter.",
                winter = "Morning frost replaces dew in winter. The meadow becomes a field of ice sculptures—every grass blade crystallized, every seed head wearing a coat of white. The frost crunches under your feet (very satisfying). The meadow is silent except for your footsteps, the usual insect chorus frozen into stillness. Beautiful, cold, and very much not for bathing."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("wildflower_border", Direction.SOUTH),
                LocationConnection("ironweed_patch", Direction.WEST),
                LocationConnection("goldenrod_glade", Direction.NORTH),
                LocationConnection("seedling_nursery", Direction.SOUTH),
                LocationConnection("bee_balm_grove", Direction.NORTH),
                LocationConnection("seedling_nursery", Direction.EAST),
                LocationConnection("scarecrow_watchtower", Direction.WEST),
                LocationConnection("clover_kingdom", Direction.NORTH)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3
        ),
        
        Location(
            id = "rain_barrel_reservoir",
            name = "Rain Barrel Reservoir",
            description = LocationDescription.simple(
                "The humans collect rainwater in a large barrel attached to the shed's downspout. The barrel's base leaks slightly, creating a perpetual damp zone with moss and mud—paradise for earthworms and pill bugs. You can't reach the water inside (too high), but the ecosystem around it is thriving. A dripping sound provides constant percussion. Mosquitoes breed in the barrel (the humans don't know—you're not telling). The reservoir makes you feel very clever about water management."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 2,
            connections = listOf(
                LocationConnection("starting_village", Direction.SOUTH),
                LocationConnection("garden_hose_maze", Direction.SOUTH),
                LocationConnection("tool_shed_shadow", Direction.WEST),
                LocationConnection("compost_heap_foothills", Direction.WEST),
                LocationConnection("meadow_path", Direction.EAST)
            ),
            encounterRate = 0.2,
            recommendedLevel = 1
        ),
        
        Location(
            id = "stepping_stone_path",
            name = "Stepping Stone Path",
            description = LocationDescription.simple(
                "The humans laid decorative stones in a winding path through the grass. Each stone is a flat oval, spaced one human-step apart. For you, this is an archipelago—islands of warm stone in a grass sea. You hop from stone to stone, each one sun-baked and comfortable under your feet. The path leads from the house to the shed, curving unnecessarily (humans enjoy inefficiency). Ants have built highways along the path edges, their columns marching with purpose between stones."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 2,
            connections = listOf(
                LocationConnection("starting_village", Direction.SOUTHWEST),
                LocationConnection("forgotten_flowerpot", Direction.EAST),
                LocationConnection("crossroads", Direction.NORTHEAST),
                LocationConnection("garden_gnome_shadow", Direction.EAST)
            ),
            encounterRate = 0.15,
            recommendedLevel = 1
        ),
        
        Location(
            id = "forgotten_flowerpot",
            name = "The Forgotten Flowerpot",
            description = LocationDescription.simple(
                "A terracotta pot lays on its side in the grass, abandoned by the humans seasons ago. Whatever plant it once held is long gone, leaving only dirt and determination. The pot has become a shelter: spiders nest in the drainage hole, pill bugs colonize the interior, and you've used it as a rain shelter twice. The curved interior amplifies sound wonderfully—your alarm calls echo with impressive authority. The pot's orange color attracts heat, making it warm even in cool weather."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 3,
            connections = listOf(
                LocationConnection("meadow_path", Direction.SOUTH),
                LocationConnection("milkweed_patch", Direction.NORTH),
                LocationConnection("stepping_stone_path", Direction.WEST),
                LocationConnection("seedling_nursery", Direction.EAST)
            ),
            encounterRate = 0.25,
            recommendedLevel = 2
        ),
        
        // ========== SUB-REGION 1B: WILDFLOWER PLAINS (20 locations) ==========
        
        Location(
            id = "clover_kingdom",
            name = "The Clover Kingdom",
            description = LocationDescription.simple(
                "An explosion of clover has claimed this section of yard. Three-leaf clovers carpet the ground in dense green, interspersed with white flowers that bees worship. You search for four-leaf clovers because the humans say they're lucky (found two so far—luck pending). The clover is soft underfoot, springy, pleasant to walk on. Rabbits graze here at dawn—you maintain polite distance. The Kingdom feeds its subjects well: clover seeds are edible if you're desperate, and the insects it attracts are excellent."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 6,
            connections = listOf(
                LocationConnection("morning_dew_meadow", Direction.SOUTH),
                LocationConnection("wild_bergamot_field", Direction.WEST),
                LocationConnection("joe_pye_weed_tower", Direction.WEST),
                LocationConnection("grasshopper_leap", Direction.NORTHEAST),
                LocationConnection("blazing_star_spike", Direction.NORTH),
                LocationConnection("rabbit_warren_outskirts", Direction.NORTH),
                LocationConnection("the_great_sprinkler", Direction.NORTH),
                LocationConnection("bee_balm_grove", Direction.WEST),
                LocationConnection("milkweed_patch", Direction.SOUTH),
                LocationConnection("fire_pit_clearing", Direction.WEST),
                LocationConnection("yarrow_circle", Direction.EAST),
                LocationConnection("wildflower_sea", Direction.NORTH),
                LocationConnection("butterfly_migration_route", Direction.EAST),
                LocationConnection("rabbit_warren_outskirts", Direction.WEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 3,
            lore = "The Clover Kingdom produces approximately 847 flowers per square foot during peak season. Mathematically, it's a miracle of nature. Ecologically, it's a buffet."
        ),
        
        Location(
            id = "butterfly_migration_route",
            name = "Butterfly Migration Route",
            description = LocationDescription.simple(
                "Twice a year, butterflies pass through in numbers that defy belief. The air fills with orange and black wings (monarchs, the humans call them), floating south in autumn and north in spring. They rest on every available surface—flowers, fence posts, your head if you stand still long enough. The migration route follows the meadow's edge where wildflowers grow thickest. You feel small watching them, these tiny creatures traveling thousands of miles. Meanwhile, you're concerned about the journey to the compost heap. Perspective."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 5,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.WEST),
                LocationConnection("aster_alley", Direction.NORTH),
                LocationConnection("black_eyed_susan_stand", Direction.NORTHEAST),
                LocationConnection("oxeye_daisy_drift", Direction.EAST),
                LocationConnection("seedling_nursery", Direction.SOUTH),
                LocationConnection("thistle_forest", Direction.EAST),
                LocationConnection("grasshopper_leap", Direction.NORTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),
        
        Location(
            id = "thistle_forest",
            name = "Thistle Forest",
            description = LocationDescription.simple(
                "What started as one thistle plant has become a prickly fortress. Purple flowers tower overhead on spiny stems, their bases thick as branches. Goldfinches descend in flocks to harvest seeds, singing while they work. You navigate carefully—thistle spines are no joke, and you've learned this through painful experience. The forest floor is scattered with down from mature seed heads, soft white fluff that clings to your feathers. Thistles are technically weeds, but tell that to the goldfinches."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = 4,
            connections = listOf(
                LocationConnection("butterfly_migration_route", Direction.WEST),
                LocationConnection("oxeye_daisy_drift", Direction.NORTH),
                LocationConnection("purple_verbena_valley", Direction.WEST),
                LocationConnection("switchgrass_savanna", Direction.EAST),
                LocationConnection("seedling_nursery", Direction.SOUTHWEST),
                LocationConnection("tumbleweed_crossing", Direction.SOUTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 4
        ),
        
        Location(
            id = "grasshopper_leap",
            name = "Grasshopper Leap",
            description = LocationDescription.simple(
                "This patch of tall grass hosts a grasshopper population of truly impressive density. Each step sends dozens flying in random directions—a chaos of green bodies and clicking wings. You've learned to hunt them (not successfully, but enthusiastically). The grasshoppers use this area as a training ground for their young, teaching them the ancient art of 'jump when stepped on.' The grass here is chewed at every level, evidence of constant herbivory. It's like a gymnasium for insects."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 5,
            connections = listOf(
                LocationConnection("butterfly_migration_route", Direction.SOUTH),
                LocationConnection("aster_alley", Direction.NORTHEAST),
                LocationConnection("black_eyed_susan_stand", Direction.SOUTH),
                LocationConnection("milkweed_patch", Direction.WEST),
                LocationConnection("wildflower_sea", Direction.NORTHWEST),
                LocationConnection("yarrow_circle", Direction.NORTH),
                LocationConnection("clover_kingdom", Direction.SOUTHWEST),
                LocationConnection("wildflower_sea", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 3
        ),
        
        Location(
            id = "bee_balm_grove",
            name = "Bee Balm Grove",
            description = LocationDescription.simple(
                "Red tubular flowers explode from sturdy stalks in midsummer, and the bees go absolutely feral for them. The grove hums with constant activity—honeybees, bumblebees, carpenter bees, and others you can't identify but respect. The flowers smell minty and sharp, medicinal. Hummingbirds compete with bees for access, their helicopter-like hovering equally impressive and terrifying. You give the grove wide berth during peak hours (too much aerial traffic), but evening visits reveal a calmer, fragrant sanctuary."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 4,
            connections = listOf(
                LocationConnection("morning_dew_meadow", Direction.SOUTH),
                LocationConnection("coneflower_citadel", Direction.NORTH),
                LocationConnection("the_great_sprinkler", Direction.NORTHWEST),
                LocationConnection("clover_kingdom", Direction.EAST),
                LocationConnection("fence_line_patrol", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 4
        ),

        Location(
            id = "the_great_sprinkler",
            name = "The Great Sprinkler",
            description = LocationDescription.simple(
                "The humans' oscillating sprinkler is a mechanical marvel that transforms into a seasonal water feature. During summer, it activates at dawn, sending arcs of water across the meadow with rhythmic precision. You've learned its pattern: swoosh left for 4 seconds, swoosh right for 4 seconds, deadly pause, repeat. Running through it is a test of courage and timing. Get it wrong, you're soaked. Get it right, you're a legend. Birds line up to bathe in its spray. The sprinkler creates rainbows in morning light—actual magic."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 5,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.SOUTH),
                LocationConnection("wild_bergamot_field", Direction.NORTH),
                LocationConnection("goldenrod_glade", Direction.WEST),
                LocationConnection("rabbit_warren_outskirts", Direction.NORTHWEST),
                LocationConnection("coneflower_citadel", Direction.SOUTH),
                LocationConnection("bee_balm_grove", Direction.SOUTHEAST),
                LocationConnection("rabbit_warren_outskirts", Direction.EAST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3,
            lore = "The Great Sprinkler operates on a timer the humans set and promptly forget about. It has watered the lawn, the fence, the neighbor's cat, and three separate delivery people. It cares for none of them equally."
        ),

        Location(
            id = "black_eyed_susan_stand",
            name = "Black-Eyed Susan Stand",
            description = LocationDescription.simple(
                "Golden petals surround dark centers like tiny suns with black holes at their hearts. The black-eyed susans grow in a dense stand, their stems creating a forest of rough green pillars. Each flower head tracks the sun throughout the day (you've watched). Goldfinches perch on the stems, bending them down to reach seeds. The flowers attract beetles, bees, and butterflies—a constant rotation of visitors. Beneath the canopy, the soil is cool and damp, perfect for finding grubs."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 4,
            connections = listOf(
                LocationConnection("seedling_nursery", Direction.SOUTH),
                LocationConnection("purple_verbena_valley", Direction.SOUTH),
                LocationConnection("grasshopper_leap", Direction.NORTH),
                LocationConnection("butterfly_migration_route", Direction.SOUTHWEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 3
        ),

        Location(
            id = "milkweed_patch",
            name = "Milkweed Patch",
            description = LocationDescription.simple(
                "The milkweed plants are critical infrastructure for monarch butterflies—their caterpillars eat nothing else. You patrol this patch with protective zeal, keeping predators at bay (as much as a button quail can). The plants exude sticky white sap when broken, earning their name. Bright orange and black caterpillars munch leaves with mechanical efficiency. In late summer, seed pods split open, releasing silk-attached seeds that float on the wind like miniature paratroopers. The whole operation is very organized."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 5,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.NORTH),
                LocationConnection("coneflower_citadel", Direction.WEST),
                LocationConnection("forgotten_flowerpot", Direction.SOUTH),
                LocationConnection("grasshopper_leap", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),

        Location(
            id = "coneflower_citadel",
            name = "Coneflower Citadel",
            description = LocationDescription.simple(
                "Purple coneflowers rise on thick stems, their spiky centers standing proud above drooping petals. The 'citadel' is really just a cluster of these flowers, but they grow so densely that they create walls and passages between them. Goldfinches love the seed heads, performing acrobatics to reach them. The flowers are tough, surviving rain and wind without bending. You use the citadel as a landmark and occasional shelter—the overlapping petals create surprisingly good cover."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 5,
            connections = listOf(
                LocationConnection("the_great_sprinkler", Direction.NORTH),
                LocationConnection("joe_pye_weed_tower", Direction.NORTH),
                LocationConnection("bee_balm_grove", Direction.SOUTH),
                LocationConnection("milkweed_patch", Direction.EAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 3
        ),

        Location(
            id = "aster_alley",
            name = "Aster Alley",
            description = LocationDescription.simple(
                "Late summer brings the asters—purple, pink, and white flowers on bushy plants that dominate this corner of the yard. The alley forms between two rows of asters, creating a natural pathway. Bees work frantically here, knowing winter is coming, loading up on these final flowers. The asters smell faintly sweet, attracting moths at night—you've seen them, pale wings glowing in darkness. The plants will stand through winter, brown and dried, skeletal sentinels until spring returns."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 6,
            connections = listOf(
                LocationConnection("butterfly_migration_route", Direction.SOUTH),
                LocationConnection("partridge_pea_patch", Direction.NORTH),
                LocationConnection("yarrow_circle", Direction.WEST),
                LocationConnection("grasshopper_leap", Direction.SOUTHWEST),
                LocationConnection("seed_head_forest", Direction.NORTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 4
        ),

        Location(
            id = "goldenrod_glade",
            name = "Goldenrod Glade",
            description = LocationDescription.simple(
                "The goldenrod explodes in autumn with plumes of tiny yellow flowers, turning entire sections of the yard into gold. The humans complain about allergies (goldenrod is innocent—it's ragweed's fault, but they blame everything yellow). The glade hosts countless insects: bees, wasps, flies, beetles, all frantically gathering pollen before frost. The stems are rigid, architectural, creating structure in the wild grass. You navigate the glade carefully—too much insect activity for comfort, but the seeds are worth the risk."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("the_great_sprinkler", Direction.EAST),
                LocationConnection("ironweed_patch", Direction.SOUTH),
                LocationConnection("moth_mullein_grove", Direction.NORTH),
                LocationConnection("fence_line_patrol", Direction.EAST),
                LocationConnection("morning_dew_meadow", Direction.SOUTH),
                LocationConnection("fence_line_patrol", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 4
        ),

        Location(
            id = "yarrow_circle",
            name = "Yarrow Circle",
            description = LocationDescription.simple(
                "White-clustered flowers top ferny stems in a perfect natural circle (probably not that perfect, but you appreciate the aesthetic). Yarrow smells pungent and medicinal—the humans use it for something (you've seen them harvest it). Butterflies visit in rotation, landing on the flat flower clusters like miniature landing pads. The circle feels intentional, even though it's random nature. You use it as a meditation spot, sitting in the center, feeling very zen and nature-connected."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 6,
            connections = listOf(
                LocationConnection("grasshopper_leap", Direction.SOUTH),
                LocationConnection("blazing_star_spike", Direction.NORTHWEST),
                LocationConnection("clover_kingdom", Direction.WEST),
                LocationConnection("aster_alley", Direction.EAST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3
        ),

        Location(
            id = "wild_bergamot_field",
            name = "Wild Bergamot Field",
            description = LocationDescription.simple(
                "Lavender-pink flowers crowd atop square stems, their unique shape unmistakable. The bergamot (the humans call it) attracts specialized bees and the occasional hummingbird. The scent is strong, aromatic, almost overwhelming in midday heat. The field sways as a unit in the breeze, creating wave patterns across its surface. You traverse it quickly—the dense stems make navigation challenging, and you prefer open sight lines for predator awareness. Still, beautiful in its way."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("the_great_sprinkler", Direction.SOUTH),
                LocationConnection("joe_pye_weed_tower", Direction.EAST),
                LocationConnection("fence_line_patrol", Direction.NORTHEAST),
                LocationConnection("fence_line_patrol", Direction.NORTH),
                LocationConnection("clover_kingdom", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 4
        ),

        Location(
            id = "blazing_star_spike",
            name = "Blazing Star Spike",
            description = LocationDescription.simple(
                "Tall spikes of purple flowers reach toward the sky like natural antenna. The blazing star (liatris, technically) blooms from top to bottom, unusually. Butterflies adore it—you've counted fifteen different species visiting in one afternoon. The spike creates a vertical landmark in the otherwise horizontal meadow. Bees work the flowers methodically, starting at the top each morning. The plant's corm is edible (you've heard), but digging it up seems like betrayal to something so majestic."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.SOUTH),
                LocationConnection("limestone_outcrop", Direction.NORTHEAST),
                LocationConnection("wildflower_sea", Direction.NORTHEAST),
                LocationConnection("wildflower_sea", Direction.NORTH),
                LocationConnection("yarrow_circle", Direction.SOUTHEAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 4
        ),

        Location(
            id = "oxeye_daisy_drift",
            name = "Oxeye Daisy Drift",
            description = LocationDescription.simple(
                "White petals with yellow centers create a sea of cheerful faces nodding in the breeze. The daisies grow in drifts, clustering together like they're gossiping. They're simple flowers—no fancy colors or shapes—but reliable and abundant. Bees visit for pollen, covering themselves in yellow dust. The stems are good for seed hunting beneath, and the flowers provide decent shade for a quail-sized creature. The drift feels safe, familiar, like home should feel."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = 5,
            connections = listOf(
                LocationConnection("thistle_forest", Direction.SOUTH),
                LocationConnection("boulder_field", Direction.SOUTHEAST),
                LocationConnection("switchgrass_savanna", Direction.SOUTH),
                LocationConnection("butterfly_migration_route", Direction.WEST),
                LocationConnection("boulder_field", Direction.EAST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 4
        ),

        Location(
            id = "joe_pye_weed_tower",
            name = "Joe Pye Weed Tower",
            description = LocationDescription.simple(
                "The tallest wildflower in the yard, joe pye weed reaches six feet high with massive domed flower clusters of dusty pink. It towers over you like a skyscraper over a quail. The stem is thick, architectural, purple-tinged. Butterflies and bees cover the flower heads so densely they form a living carpet of wings. The tower sways alarmingly in wind but never topples—deep roots, you suppose. You use it as a navigation beacon: 'Turn left at the giant pink tower.'"
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 6,
            connections = listOf(
                LocationConnection("coneflower_citadel", Direction.SOUTH),
                LocationConnection("clover_kingdom", Direction.EAST),
                LocationConnection("wild_bergamot_field", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 4,
            lore = "Joe Pye Weed is named after a Native American healer who used it medicinally. The plant doesn't know this. It just keeps growing taller every year, reaching for the sun with single-minded determination."
        ),

        Location(
            id = "switchgrass_savanna",
            name = "Switchgrass Savanna",
            description = LocationDescription.simple(
                "Native prairie grass grows in massive clumps, its seed heads golden and feathery. The switchgrass creates a savanna landscape—open ground between clumps, the grass itself forming trees in your scale. Wind makes the seed heads dance, creating rustling music. The grass provides nesting material for birds (not you, but you note it professionally). In autumn, the whole savanna turns burnt orange and tan, beautiful in a melancholy way. Seeds are abundant, and the structure provides excellent predator cover."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 4,
            gridY = 4,
            connections = listOf(
                LocationConnection("thistle_forest", Direction.WEST),
                LocationConnection("eagle_shadow_plains", Direction.EAST),
                LocationConnection("chipmunk_cache", Direction.NORTH),
                LocationConnection("oxeye_daisy_drift", Direction.NORTH),
                LocationConnection("sun_baked_plain", Direction.SOUTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 5
        ),

        Location(
            id = "ironweed_patch",
            name = "Ironweed Patch",
            description = LocationDescription.simple(
                "Deep purple flowers on iron-strong stems justify this plant's name. The ironweed is nearly impossible to pull (the humans have tried), its roots going deep into earth. Butterflies favor it strongly—something about the nectar. The patch creates a purple haze in late summer, contrasting beautifully with yellow goldenrod nearby. You respect the ironweed's stubbornness, its refusal to be domesticated or controlled. It grows where it wants, how it wants, with an attitude you admire."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("goldenrod_glade", Direction.NORTH),
                LocationConnection("morning_dew_meadow", Direction.EAST),
                LocationConnection("haystack_fortress", Direction.SOUTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 4
        ),

        Location(
            id = "purple_verbena_valley",
            name = "Purple Verbena Valley",
            description = LocationDescription.simple(
                "Low-growing verbena carpets a slight depression in the yard with clusters of tiny purple flowers. The valley (really just a low spot) collects water after rain, creating lush growth. Bees work the verbena from dawn to dusk—it's apparently bee cocaine. The scent is light, sweet, pleasant. Walking through the valley covers your feet in pollen—unavoidable but not unwelcome. Small flies also frequent the flowers, creating a layered insect ecosystem you navigate carefully."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 4,
            connections = listOf(
                LocationConnection("black_eyed_susan_stand", Direction.NORTH),
                LocationConnection("seedling_nursery", Direction.WEST),
                LocationConnection("thistle_forest", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),

        Location(
            id = "partridge_pea_patch",
            name = "Partridge Pea Patch",
            description = LocationDescription.simple(
                "Small yellow flowers with red centers appear almost artificial in their perfection. The partridge pea is a legume, the humans explain, fixing nitrogen in soil (you nod as if you understand). The seed pods are small and numerous, popular with actual partridges (your much larger relatives—it's complicated). Ants tend aphids on the stems in exchange for honeydew, a fascinating agricultural arrangement you observe from respectful distance. The patch hums with quiet industry."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = 6,
            connections = listOf(
                LocationConnection("aster_alley", Direction.SOUTH),
                LocationConnection("fence_line_patrol", Direction.NORTH),
                LocationConnection("seed_head_forest", Direction.NORTHWEST),
                LocationConnection("seed_head_forest", Direction.NORTH),
                LocationConnection("fence_line_patrol", Direction.WEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 4
        ),

        // ========== SUB-REGION 1C: SOUTHERN PRAIRIES (First 5 of 15 locations) ==========

        Location(
            id = "tumbleweed_crossing",
            name = "Tumbleweed Crossing",
            description = LocationDescription.simple(
                "The grass here is sparser, drier, influenced by proximity to the desert transition zone. Actual tumbleweeds (Russian thistle, gone to seed) blow through occasionally, bouncing across the prairie like tiny cages of despair. The crossing marks where grassland meets something harsher—the soil lighter, the plants more drought-adapted. You see prairie dogs in the distance (they wave, you think). The wind is constant here, carrying scents from far away: sage, dust, and something that might be rain."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = -2,
            connections = listOf(
                LocationConnection("rolling_hills", Direction.WEST),
                LocationConnection("ocotillo_fence", Direction.SOUTH),
                LocationConnection("dried_creek_bed", Direction.SOUTH),
                LocationConnection("sagebrush_border", Direction.SOUTH),
                LocationConnection("cactus_sentinel_grove", Direction.SOUTH),
                LocationConnection("creosote_ring", Direction.SOUTH),
                LocationConnection("prairie_dog_town", Direction.SOUTHEAST),
                LocationConnection("heat_shimmer_flats", Direction.SOUTH),
                LocationConnection("century_plant_field", Direction.SOUTH),
                LocationConnection("thistle_forest", Direction.NORTH),
                LocationConnection("gopher_burrow_network", Direction.EAST),
                LocationConnection("sun_baked_plain", Direction.NORTHEAST),
                LocationConnection("anthill_metropolis", Direction.SOUTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 4
        ),

        Location(
            id = "gopher_burrow_network",
            name = "Gopher Burrow Network",
            description = LocationDescription.simple(
                "The ground here is riddled with holes—entrances to an underground city you can only imagine. Gophers pop up randomly, survey the landscape, and disappear. They've created mounds of excavated soil, miniature mountains in the prairie. You've established trade relations: you don't attack, they share tunnel-digging techniques (you can't dig, but you listen politely). The network extends for yards, connecting beneath the surface. It's engineering on a scale you respect but can't replicate."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 4,
            gridY = -1,
            connections = listOf(
                LocationConnection("tumbleweed_crossing", Direction.WEST),
                LocationConnection("dried_creek_bed", Direction.WEST),
                LocationConnection("sun_baked_plain", Direction.NORTH),
                LocationConnection("prairie_dog_town", Direction.SOUTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5,
            lore = "The Gopher Burrow Network has an estimated 200+ entrances and covers 0.3 acres. The gophers refuse to comment on actual population numbers, citing security concerns."
        ),

        Location(
            id = "sun_baked_plain",
            name = "Sun-Baked Plain",
            description = LocationDescription.simple(
                "The grass here is sparse, yellow, struggling in poor soil and full sun. The ground is hard-packed clay that cracks in geometric patterns during dry spells. Heat radiates visibly in shimmering waves. This is harsh country—too exposed for comfort, too dry for abundance. Yet life persists: lizards bask on exposed rocks, ants march in determined lines, and drought-resistant plants send deep roots into stubborn earth. You cross quickly, preferring the coverage of lusher zones, but you respect the plain's austere beauty."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 4,
            gridY = 0,
            connections = listOf(
                LocationConnection("gopher_burrow_network", Direction.SOUTH),
                LocationConnection("boulder_field", Direction.EAST),
                LocationConnection("switchgrass_savanna", Direction.NORTH),
                LocationConnection("tumbleweed_crossing", Direction.SOUTHWEST),
                LocationConnection("boulder_field", Direction.NORTH),
                LocationConnection("rolling_hills", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5
        ),

        Location(
            id = "prairie_dog_town",
            name = "Prairie Dog Town",
            description = LocationDescription.simple(
                "A community of prairie dogs has established a proper town here—dozens of burrow entrances, elaborate tunnel systems, and constant social activity. They stand on hind legs, surveying for threats, barking warnings when hawks pass overhead. You've learned their alarm calls (very useful). The dogs tolerate your presence—you're too small to be a threat and occasionally warn them of approaching cats. It's a functioning civilization, complete with social hierarchy and property disputes."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 5,
            gridY = -1,
            connections = listOf(
                LocationConnection("gopher_burrow_network", Direction.NORTH),
                LocationConnection("sagebrush_border", Direction.SOUTHWEST),
                LocationConnection("little_bluestem_prairie", Direction.SOUTH),
                LocationConnection("tumbleweed_crossing", Direction.NORTHWEST),
                LocationConnection("dunes_sea", Direction.SOUTH)
            ),
            isSettlement = true,
            encounterRate = 0.4,
            recommendedLevel = 5,
            lore = "Prairie Dog Town operates under a complex social structure involving sentries, nursery guards, and foraging parties. Democracy is not practiced. The current mayor is Big Bertha, age 4, weighing 2.3 pounds of pure authority."
        ),

        Location(
            id = "dried_creek_bed",
            name = "Dried Creek Bed",
            description = LocationDescription.simple(
                "What was once a seasonal creek is now a depression filled with smooth stones and stubborn weeds. Water flows here only after heavy rain, briefly transforming the bed into a rushing stream before evaporating back into memory. The stones are good for dust bathing, and insects shelter beneath them during heat. You've found interesting seeds washed down from upstream. The bed cuts through the prairie like a scar, evidence of water's power and absence."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = -1,
            connections = listOf(
                LocationConnection("tumbleweed_crossing", Direction.NORTH),
                LocationConnection("buffalo_grass_expanse", Direction.NORTHWEST),
                LocationConnection("rolling_hills", Direction.NORTHWEST),
                LocationConnection("gopher_burrow_network", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 4
        ),

        Location(
            id = "sagebrush_border",
            name = "Sagebrush Border",
            description = LocationDescription.simple(
                "The grassland's southern edge gives way to scattered sagebrush—silvery-green shrubs that smell pungent and medicinal. The transition zone supports both ecosystems: prairie grasses yielding to desert plants. Lizards are more common here, and you've spotted roadrunners in the distance (impressive birds, poor conversationalists). The sagebrush provides cover but limited food. You use this border as a demarcation line: beyond lies desert country, a different world entirely."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 4,
            gridY = -2,
            connections = listOf(
                LocationConnection("tumbleweed_crossing", Direction.NORTH),
                LocationConnection("little_bluestem_prairie", Direction.EAST),
                LocationConnection("anthill_metropolis", Direction.WEST),
                LocationConnection("prairie_dog_town", Direction.NORTHEAST),
                LocationConnection("dunes_sea", Direction.SOUTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 5
        ),

        Location(
            id = "buffalo_grass_expanse",
            name = "Buffalo Grass Expanse",
            description = LocationDescription.simple(
                "Short, curly grass covers the ground in a dense mat—buffalo grass, native and resilient. The expanse is easier to navigate than tall grass areas, offering good visibility. The grass's roots form a tough sod that resists erosion. Grazing mammals (rabbits, mostly) keep it trimmed. The expanse feels open, exposed, but strangely safe—you can see threats coming from distance. Prairie birds nest here in shallow scrapes, trusting camouflage over cover."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = -1,
            connections = listOf(
                LocationConnection("rolling_hills", Direction.EAST),
                LocationConnection("dried_creek_bed", Direction.SOUTHEAST),
                LocationConnection("pebble_plaza", Direction.NORTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 3
        ),

        Location(
            id = "anthill_metropolis",
            name = "Anthill Metropolis",
            description = LocationDescription.simple(
                "Multiple anthills cluster in this area, their mounds rising like miniature volcanoes. Red harvester ants march in highways between colonies, transporting seeds with industrial efficiency. You watch their organization with professional admiration—they're small, but they've conquered their world through cooperation and determination (relatable). The ants mostly ignore you unless you step on a mound (lesson learned). The metropolis is a study in insect engineering."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = -3,
            connections = listOf(
                LocationConnection("tumbleweed_crossing", Direction.NORTH),
                LocationConnection("sagebrush_border", Direction.EAST),
                LocationConnection("dunes_sea", Direction.SOUTHEAST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 4
        ),

        Location(
            id = "little_bluestem_prairie",
            name = "Little Bluestem Prairie",
            description = LocationDescription.simple(
                "Native little bluestem grass grows in elegant clumps, its stems turning bronze and purple in autumn. This is authentic prairie, what the land looked like before humans arrived. The grass is tall enough to hide you completely, providing excellent cover. Seeds are abundant, and the structure attracts sparrows and other seed-eaters. You feel historical significance here—this grass has grown in these plains for thousands of years. You're part of that continuity now."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 5,
            gridY = -2,
            connections = listOf(
                LocationConnection("prairie_dog_town", Direction.NORTH),
                LocationConnection("sagebrush_border", Direction.WEST),
                LocationConnection("dunes_sea", Direction.SOUTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 5
        ),

        // ========== SUB-REGION 1D: WESTERN MEADOWS (15 locations) ==========

        Location(
            id = "grain_storage_hamlet",
            name = "Grain Storage Hamlet",
            description = LocationDescription.simple(
                "The humans' grain storage (three metal bins on the property edge) has become a settlement for seed-dependent creatures. Mice nest in the bin bases, sparrows roost on top, and you patrol the spillage zone where grain leaks from imperfect seals. The hamlet isn't pretty—industrial, functional—but it provides resources year-round. The bins echo when tapped, creating eerie music. Cats hunt here regularly; you've established safe routes through their territories via careful observation."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = -1,
            connections = listOf(
                LocationConnection("irrigation_ditch", Direction.NORTH),
                LocationConnection("rock_garden_ruins", Direction.NORTH),
                LocationConnection("mint_patch_overgrowth", Direction.WEST),
                LocationConnection("drainage_ditch_west", Direction.WEST),
                LocationConnection("clover_patch_west", Direction.EAST),
                LocationConnection("windmill_farm", Direction.EAST),
                LocationConnection("harbor_town", Direction.WEST)
            ),
            isSettlement = true,
            shopAvailable = true,
            encounterRate = 0.3,
            recommendedLevel = 3,
            lore = "The Grain Storage Hamlet operates under an informal treaty: mice control the spillage, sparrows get the elevated positions, and everyone flees when the humans arrive with machinery."
        ),

        Location(
            id = "chicken_wire_village",
            name = "Chicken Wire Village",
            description = LocationDescription.simple(
                "Old chicken wire fencing lies in rolls near the shed, abandoned by humans mid-project. The wire has become framework for a village: sparrows nest in the coils, mice tunnel beneath, and vines grow through creating natural camouflage. You navigate the wire carefully—it's sharp, rusty, tetanus-inducing. But the village offers excellent shelter from weather and predators. The community is tight-knit, everyone knowing everyone, gossip flowing freely through the wire mesh."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -5,
            gridY = 1,
            connections = listOf(
                LocationConnection("scarecrow_watchtower", Direction.EAST),
                LocationConnection("mint_patch_overgrowth", Direction.SOUTH),
                LocationConnection("clothesline_meadow", Direction.SOUTH),
                LocationConnection("raspberry_thicket", Direction.NORTHWEST),
                LocationConnection("property_line_fence", Direction.WEST),
                LocationConnection("fire_pit_clearing", Direction.NORTHWEST),
                LocationConnection("haystack_fortress", Direction.SOUTHEAST),
                LocationConnection("wheelbarrow_graveyard", Direction.SOUTH)
            ),
            isSettlement = true,
            encounterRate = 0.25,
            recommendedLevel = 3
        ),

        Location(
            id = "tool_shed_shadow",
            name = "Tool Shed Shadow",
            description = LocationDescription.simple(
                "The shed's north side never sees direct sun, creating a permanently cool, damp microclimate. Moss grows thick, ferns sprout, and mushrooms appear after rain. The shadow zone feels different from surrounding grassland—quieter, darker, mysterious. Insects that prefer shade congregate here: beetles, spiders, centipedes. You visit cautiously (some of those centipedes are concerning), but the shade is merciful during summer heat. The humans store things in the shed; you store memories of things you've found in its shadow."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 1,
            connections = listOf(
                LocationConnection("wheelbarrow_graveyard", Direction.WEST),
                LocationConnection("garden_hose_maze", Direction.EAST),
                LocationConnection("mulch_mountain", Direction.NORTH),
                LocationConnection("irrigation_ditch", Direction.SOUTH),
                LocationConnection("rain_barrel_reservoir", Direction.EAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 2
        ),

        Location(
            id = "garden_hose_maze",
            name = "Garden Hose Maze",
            description = LocationDescription.simple(
                "The humans' garden hose lies coiled in elaborate loops near the spigot. To you, it's a maze of green rubber tunnels and arches. Water still drips from the connection (the humans haven't fixed the leak), creating a small puddle ecosystem. You've learned to navigate the hose maze, using it as cover when crossing open ground. Frogs sometimes shelter in the coils. The rubber smells odd but provides excellent sun-warmed basking spots."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 1,
            connections = listOf(
                LocationConnection("tool_shed_shadow", Direction.WEST),
                LocationConnection("flagstone_patio", Direction.EAST),
                LocationConnection("rain_barrel_reservoir", Direction.NORTH),
                LocationConnection("irrigation_ditch", Direction.SOUTHWEST)
            ),
            encounterRate = 0.2,
            recommendedLevel = 2
        ),

        Location(
            id = "mulch_mountain",
            name = "Mulch Mountain",
            description = LocationDescription.simple(
                "A pile of wood chip mulch sits waiting to be spread on garden beds. To you, it's a mountain of aromatic wood fragments, warm from decomposition, hosting countless insects. The mulch is soft underfoot, easy to dig through for bugs and worms. The pile shifts and settles, creating new tunnels and caves daily. Mice nest deep inside, enjoying the warmth. You climb to the summit regularly (6 inches high, very impressive) for the strategic view it offers."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 2,
            connections = listOf(
                LocationConnection("compost_heap_foothills", Direction.NORTHEAST),
                LocationConnection("clothesline_meadow", Direction.WEST),
                LocationConnection("tool_shed_shadow", Direction.SOUTH),
                LocationConnection("wildflower_border", Direction.EAST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),

        Location(
            id = "flagstone_patio",
            name = "Flagstone Patio",
            description = LocationDescription.simple(
                "The humans laid flagstones in an attempt at a patio. Gaps between stones have filled with soil and sprouted grass, creating a checkerboard landscape. The stones retain heat wonderfully—you sunbathe here in late afternoon, warming your belly on smooth rock. Ants travel the gaps like highways. Spiders hunt in the crevices. The patio is close to the house, which means humans, which means potential food spillage. You maintain regular patrols."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 0,
            connections = listOf(
                LocationConnection("starting_village", Direction.EAST),
                LocationConnection("vegetable_garden_edge", Direction.EAST),
                LocationConnection("puddle_lake", Direction.NORTHEAST),
                LocationConnection("garden_hose_maze", Direction.WEST)
            ),
            encounterRate = 0.15,
            recommendedLevel = 1
        ),

        Location(
            id = "clover_patch_west",
            name = "Western Clover Patch",
            description = LocationDescription.simple(
                "White clover spreads in a thick carpet on the western lawn, different from the main Clover Kingdom but equally productive. The flowers attract bees in drowsy afternoon crowds. This patch is your secondary territory, claimed and defended from sparrow incursions. The clover is soft, cool, pleasant to rest on. You've memorized every tuft, every flower cluster, every ant highway through the stems. This is yours."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = -1,
            connections = listOf(
                LocationConnection("irrigation_ditch", Direction.NORTH),
                LocationConnection("grain_storage_hamlet", Direction.WEST),
                LocationConnection("puddle_lake", Direction.EAST)
            ),
            encounterRate = 0.25,
            recommendedLevel = 2
        ),

        Location(
            id = "rock_garden_ruins",
            name = "Rock Garden Ruins",
            description = LocationDescription.simple(
                "The humans attempted a decorative rock garden years ago. Now only scattered stones and struggling succulents remain. The rocks provide basking spots and landmarks. A few hardy plants persist: sedum, hens-and-chicks, something prickly you avoid. The garden has gone feral but retains hints of its designed past. You appreciate the aesthetic—controlled chaos, nature reclaiming human artifice. The rocks remember their arrangement even as wilderness returns."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = 0,
            connections = listOf(
                LocationConnection("grain_storage_hamlet", Direction.SOUTH),
                LocationConnection("wheelbarrow_graveyard", Direction.NORTH),
                LocationConnection("irrigation_ditch", Direction.EAST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3
        ),

        Location(
            id = "mint_patch_overgrowth",
            name = "Mint Patch Overgrowth",
            description = LocationDescription.simple(
                "The humans planted mint in a container. The mint escaped. Now it carpets an entire section of yard, aggressive and aromatic. The scent is overwhelming—refreshing to some, intense to you. The mint grows so densely it suppresses other plants, creating a monoculture. Bees love the purple flower spikes. You traverse the patch quickly (the smell affects your sinuses). The humans say mint is invasive. The mint doesn't care about human classifications."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -5,
            gridY = 0,
            connections = listOf(
                LocationConnection("chicken_wire_village", Direction.NORTH),
                LocationConnection("drainage_ditch_west", Direction.SOUTH),
                LocationConnection("property_line_fence", Direction.NORTH),
                LocationConnection("grain_storage_hamlet", Direction.EAST),
                LocationConnection("windmill_farm", Direction.SOUTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 3
        ),

        Location(
            id = "clothesline_meadow",
            name = "Clothesline Meadow",
            description = LocationDescription.simple(
                "The humans' clothesline stretches across the western yard, posts planted in grass. Laundry flaps overhead when in use, creating moving shade patterns. The meadow beneath is flattened from human foot traffic but recovers quickly. You've learned to time your crossings for when laundry hangs—the scent of detergent is pleasant, and the sheets create ground cover from aerial threats. The clothesline posts make excellent landmarks for navigation."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -4,
            gridY = 2,
            connections = listOf(
                LocationConnection("haystack_fortress", Direction.NORTHEAST),
                LocationConnection("fire_pit_clearing", Direction.NORTH),
                LocationConnection("chicken_wire_village", Direction.NORTH),
                LocationConnection("mulch_mountain", Direction.EAST)
            ),
            encounterRate = 0.25,
            recommendedLevel = 2
        ),

        Location(
            id = "fire_pit_clearing",
            name = "Fire Pit Clearing",
            description = LocationDescription.simple(
                "The humans built a fire pit with stacked stones in the western yard. The clearing around it is bare dirt, compressed from human gatherings. Ash and charcoal fragments litter the ground (you avoid them—taste terrible). The stone ring provides habitat for insects that don't mind disturbed ground. When fires burn, you observe from safe distance, fascinated by flame. The next day, you investigate the warm ashes, finding interesting things dropped by careless humans."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -5,
            gridY = 3,
            connections = listOf(
                LocationConnection("chicken_wire_village", Direction.SOUTHEAST),
                LocationConnection("raspberry_thicket", Direction.NORTH),
                LocationConnection("clothesline_meadow", Direction.SOUTH),
                LocationConnection("clover_kingdom", Direction.EAST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3
        ),

        Location(
            id = "raspberry_thicket",
            name = "Raspberry Thicket",
            description = LocationDescription.simple(
                "Wild raspberry canes have escaped the humans' garden, forming a prickly thicket on the property edge. The canes arch overhead, creating thorny tunnels. Berries ripen in summer, attracting birds and humans alike. You navigate carefully—raspberry thorns respect nothing. The thicket provides excellent cover from predators; hawks won't pursue into such dense vegetation. Underneath, the soil is rich and bug-filled, making the danger worthwhile."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -6,
            gridY = 2,
            connections = listOf(
                LocationConnection("fire_pit_clearing", Direction.SOUTH),
                LocationConnection("property_line_fence", Direction.SOUTH),
                LocationConnection("chicken_wire_village", Direction.SOUTHEAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 4
        ),

        Location(
            id = "drainage_ditch_west",
            name = "Western Drainage Ditch",
            description = LocationDescription.simple(
                "A shallow ditch drains water from the western yard toward the property line. Similar to the irrigation ditch but wilder, less maintained. Cattails grow in the wettest sections, and frogs chorus from hidden pools. The ditch connects to a larger system beyond your territory, carrying water to mysterious destinations. You use it as a travel corridor, sheltered from overhead threats. Water flowing here whispers of storms upstream."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -5,
            gridY = -1,
            connections = listOf(
                LocationConnection("mint_patch_overgrowth", Direction.NORTH),
                LocationConnection("grain_storage_hamlet", Direction.EAST),
                LocationConnection("harbor_town", Direction.WEST)
            ),
            encounterRate = 0.3,
            recommendedLevel = 3
        ),

        Location(
            id = "vegetable_garden_edge",
            name = "Vegetable Garden Edge",
            description = LocationDescription.simple(
                "The humans' vegetable garden is fenced (to keep rabbits out), but you slip under easily. The edge zones are your focus—spillover from the garden's abundance. Tomato plants sprawl beyond their cages, squash vines escape boundaries, and bugs fleeing human intervention shelter here. You eat insects attracted to vegetables (indirect benefit). The garden smells of basil, tomato leaves, and earth. The humans work here often; you've learned their schedule."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 0,
            connections = listOf(
                LocationConnection("flagstone_patio", Direction.WEST),
                LocationConnection("puddle_lake", Direction.NORTH),
                LocationConnection("starting_village", Direction.EAST)
            ),
            isSettlement = true,
            encounterRate = 0.35,
            recommendedLevel = 2,
            lore = "The vegetable garden produces tomatoes, peppers, squash, beans, and human satisfaction in roughly equal measures. The rabbits remain determined to access it. The fence remains effective. This tension has defined three growing seasons."
        ),

        Location(
            id = "property_line_fence",
            name = "Property Line Fence",
            description = LocationDescription.simple(
                "A wooden fence marks the western property boundary—your territory's edge. The fence is weathered, gaps appearing between boards, posts leaning with age. You slip through gaps regularly, aware that beyond lies neighbor territory (different dangers, different rules). The fence line hosts vines and weeds that humans on both sides ignore, creating a neutral buffer zone. Fence-sitting birds exchange gossip here, speaking of yards beyond."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -6,
            gridY = 1,
            connections = listOf(
                LocationConnection("chicken_wire_village", Direction.EAST),
                LocationConnection("raspberry_thicket", Direction.NORTH),
                LocationConnection("mint_patch_overgrowth", Direction.SOUTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 4
        ),

        // ========== SUB-REGION 1E: FAR NORTHERN FIELDS (15 locations) ==========

        Location(
            id = "wildflower_sea",
            name = "Wildflower Sea",
            description = LocationDescription.simple(
                "Beyond the immediate yard, wildflowers grow in absolute profusion—a sea of color stretching to the horizon (fifteen feet, but still impressive). Every species from the wildflower border grows here in mass: coneflowers, asters, susans, everything. Walking through requires determination; the stems are dense, flowers overhead creating perpetual twilight. Pollinators work in such numbers the air hums audibly. This is nature unleashed, beautiful and slightly terrifying in its abundance."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.SOUTH),
                LocationConnection("grasshopper_leap", Direction.SOUTH),
                LocationConnection("blazing_star_spike", Direction.SOUTH),
                LocationConnection("field_mouse_village", Direction.WEST),
                LocationConnection("rabbit_warren_outskirts", Direction.WEST),
                LocationConnection("limestone_outcrop", Direction.EAST),
                LocationConnection("morning_glory_trellis", Direction.NORTH),
                LocationConnection("sunflower_sentinel", Direction.NORTH),
                LocationConnection("butterfly_bush_grove", Direction.NORTHWEST),
                LocationConnection("blazing_star_spike", Direction.SOUTHWEST),
                LocationConnection("grasshopper_leap", Direction.SOUTHEAST),
                LocationConnection("seed_head_forest", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 5
        ),

        Location(
            id = "seed_head_forest",
            name = "Seed Head Forest",
            description = LocationDescription.simple(
                "Tall grass species have gone to seed, their heads towering overhead like a forest canopy. Timothy, foxtail, bromegrass—all architectural wonders from your perspective. The seed heads sway in unison with the wind, creating wave patterns. Seeds rain down constantly, a manna from above. This is harvest season's cathedral, every surface covered in golden grain. Birds descend in flocks for the feast. You compete, succeed occasionally, feel very small and very connected to cycles larger than yourself."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 7,
            connections = listOf(
                LocationConnection("wildflower_sea", Direction.WEST),
                LocationConnection("partridge_pea_patch", Direction.SOUTH),
                LocationConnection("limestone_outcrop", Direction.WEST),
                LocationConnection("morning_glory_trellis", Direction.NORTHWEST),
                LocationConnection("tallgrass_labyrinth", Direction.NORTH),
                LocationConnection("compass_plant_landmark", Direction.EAST),
                LocationConnection("aster_alley", Direction.SOUTH),
                LocationConnection("partridge_pea_patch", Direction.SOUTHEAST),
                LocationConnection("fence_line_patrol", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),

        Location(
            id = "rabbit_warren_outskirts",
            name = "Rabbit Warren Outskirts",
            description = LocationDescription.simple(
                "The rabbits maintain an extensive burrow system in the far northern field. You're too small to be a threat, so they tolerate your presence at the warren's edge. Baby rabbits play near entrances under watchful parent eyes. The area is beaten down from rabbit traffic, creating paths through the grass. You've learned rabbit warning signals—useful when hawks appear. The warren represents a civilization different from yours but recognizable in its family structures."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 7,
            connections = listOf(
                LocationConnection("clover_kingdom", Direction.SOUTH),
                LocationConnection("field_mouse_village", Direction.EAST),
                LocationConnection("moth_mullein_grove", Direction.WEST),
                LocationConnection("the_great_sprinkler", Direction.WEST),
                LocationConnection("clover_kingdom", Direction.EAST),
                LocationConnection("dew_collection_hollow", Direction.NORTH),
                LocationConnection("butterfly_bush_grove", Direction.NORTH),
                LocationConnection("wildflower_sea", Direction.EAST),
                LocationConnection("the_great_sprinkler", Direction.SOUTHEAST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5,
            lore = "The rabbit warren houses an estimated 12-15 rabbits across three generations. Their society is matriarchal, led by Big Mama, a cottontail of formidable size and disposition."
        ),

        Location(
            id = "fence_line_patrol",
            name = "Fence Line Patrol Route",
            description = LocationDescription.simple(
                "The northern fence line marks your known world's edge. You patrol this route regularly, checking for breaches, new growth, interesting changes. The fence is chain-link here, different from the western wooden fence, offering different tactical advantages. Vines grow through the links, creating living walls. Beyond the fence lies the neighbor's yard—foreign territory you observe but rarely enter. The patrol route is duty, routine, and comforting in its predictability."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = 6,
            connections = listOf(
                LocationConnection("partridge_pea_patch", Direction.SOUTH),
                LocationConnection("wild_bergamot_field", Direction.SOUTH),
                LocationConnection("goldenrod_glade", Direction.SOUTH),
                LocationConnection("partridge_pea_patch", Direction.EAST),
                LocationConnection("bee_balm_grove", Direction.SOUTH),
                LocationConnection("compass_plant_landmark", Direction.NORTH),
                LocationConnection("seed_head_forest", Direction.WEST),
                LocationConnection("wild_bergamot_field", Direction.SOUTHWEST),
                LocationConnection("goldenrod_glade", Direction.WEST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 5
        ),

        Location(
            id = "morning_glory_trellis",
            name = "Morning Glory Trellis",
            description = LocationDescription.simple(
                "Morning glory vines climb a forgotten trellis near the northern fence, purple and blue flowers opening at dawn and closing by noon. The vines twist around any available support, creating a dense green wall. You navigate beneath, protected from above by the canopy of heart-shaped leaves. The flowers attract hummingbirds and bees. By afternoon, the flowers are spent, conserving energy for tomorrow's show. The cycle is reliable, beautiful, and slightly melancholy."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 8,
            connections = listOf(
                LocationConnection("wildflower_sea", Direction.SOUTH),
                LocationConnection("tallgrass_labyrinth", Direction.EAST),
                LocationConnection("sunflower_sentinel", Direction.WEST),
                LocationConnection("bird_bath_oasis", Direction.NORTH),
                LocationConnection("elderwood", Direction.NORTH),
                LocationConnection("seed_head_forest", Direction.SOUTHEAST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 6
        ),

        Location(
            id = "butterfly_bush_grove",
            name = "Butterfly Bush Grove",
            description = LocationDescription.simple(
                "Three butterfly bushes grow in the far northern yard, their purple flower spikes magnets for every butterfly species in the region. The bushes provide vertical structure in otherwise horizontal grassland. You shelter beneath them during rain, the broad leaves shedding water away from the trunk. The bushes smell sweet, almost honey-like. Butterflies rest on the blooms like living ornaments, wings slowly opening and closing. It's possibly the most beautiful location in your territory."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 8,
            connections = listOf(
                LocationConnection("rabbit_warren_outskirts", Direction.SOUTH),
                LocationConnection("field_mouse_village", Direction.SOUTH),
                LocationConnection("sunflower_sentinel", Direction.EAST),
                LocationConnection("dew_collection_hollow", Direction.WEST),
                LocationConnection("wildflower_sea", Direction.SOUTHEAST),
                LocationConnection("elderwood", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),

        Location(
            id = "limestone_outcrop",
            name = "Limestone Outcrop",
            description = LocationDescription.simple(
                "A small limestone outcrop breaks through the soil in the northern field—probably why the humans gave up farming this section. The rock is weathered, pocketed, covered in lichen. It provides a vantage point and sun-warmed basking surface. Lizards compete for prime rock positions. Snakes sometimes coil beneath overhangs (you keep your distance). The outcrop feels ancient, permanent, indifferent to the grass and creatures that come and go around it."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 7,
            connections = listOf(
                LocationConnection("wildflower_sea", Direction.WEST),
                LocationConnection("blazing_star_spike", Direction.SOUTHWEST),
                LocationConnection("seed_head_forest", Direction.EAST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5
        ),

        Location(
            id = "field_mouse_village",
            name = "Field Mouse Village",
            description = LocationDescription.simple(
                "Field mice maintain a complex network of surface tunnels and grass nests in the far northern field. Their trails crisscross the area, worn smooth by tiny feet. You've negotiated access rights in exchange for hawk warnings (you're better at spotting aerial threats). The mice are industrious, constantly gathering seeds, rebuilding nests, evading predators. Their village is invisible to humans but sophisticated in its layout—nursery areas, food storage, escape routes all carefully planned."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -1,
            gridY = 7,
            connections = listOf(
                LocationConnection("rabbit_warren_outskirts", Direction.WEST),
                LocationConnection("wildflower_sea", Direction.EAST),
                LocationConnection("butterfly_bush_grove", Direction.NORTH)
            ),
            isSettlement = true,
            encounterRate = 0.4,
            recommendedLevel = 5
        ),

        Location(
            id = "sunflower_sentinel",
            name = "Sunflower Sentinel",
            description = LocationDescription.simple(
                "One volunteer sunflower grew to massive height (eight feet) before the humans could remove it. Now it stands as a landmark, visible from anywhere in your territory. The flower head is enormous, facing east to greet the sun each morning. Goldfinches work the seeds in acrobatic displays. The stalk is thick as a tree trunk (to you), strong enough to resist wind. The Sunflower Sentinel is both navigation beacon and source of wonder—how did this plant achieve such majesty?"
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("wildflower_sea", Direction.SOUTH),
                LocationConnection("morning_glory_trellis", Direction.EAST),
                LocationConnection("butterfly_bush_grove", Direction.WEST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6,
            lore = "The Sunflower Sentinel germinated from birdseed spillage, grew unmolested due to human inattention, and achieved glory through perseverance and luck. It will stand until winter frost claims it, but its legacy will live in the seeds it scatters."
        ),

        Location(
            id = "tallgrass_labyrinth",
            name = "Tallgrass Labyrinth",
            description = LocationDescription.simple(
                "Big bluestem and Indiangrass grow in dense stands creating a maze of stems taller than houses (from your perspective). Navigation requires memory and attention—one wrong turn and you're lost in identical green corridors. The labyrinth is disorienting but thrilling. Sounds echo strangely. Light filters down in shafts. You feel simultaneously vulnerable and hidden. The grass sways overhead like a living ceiling. This is wilderness, untamed and magnificent."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 2,
            gridY = 8,
            connections = listOf(
                LocationConnection("seed_head_forest", Direction.SOUTH),
                LocationConnection("bird_bath_oasis", Direction.NORTHWEST),
                LocationConnection("compass_plant_landmark", Direction.SOUTH),
                LocationConnection("morning_glory_trellis", Direction.WEST),
                LocationConnection("elderwood", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7
        ),

        Location(
            id = "dew_collection_hollow",
            name = "Dew Collection Hollow",
            description = LocationDescription.simple(
                "A gentle depression in the northern field collects morning dew in quantities that border on miraculous. Grass here is perpetually lush, green even in drought. The hollow becomes a miniature wetland after rain, hosting frogs and aquatic insects. Morning visits reveal a sparkling wonderland of water droplets. By midday, it's merely grass. The hollow's magic is temporary, dependent on conditions, but predictable in its cycles. You time visits for maximum enchantment."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -2,
            gridY = 8,
            connections = listOf(
                LocationConnection("rabbit_warren_outskirts", Direction.SOUTH),
                LocationConnection("moth_mullein_grove", Direction.SOUTH),
                LocationConnection("northern_property_corner", Direction.WEST),
                LocationConnection("butterfly_bush_grove", Direction.EAST),
                LocationConnection("elderwood", Direction.NORTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 6
        ),

        Location(
            id = "compass_plant_landmark",
            name = "Compass Plant Landmark",
            description = LocationDescription.simple(
                "A tall compass plant grows in the northern field, its leaves oriented north-south (hence the name—the humans explained this). The plant is distinctive enough to serve as a landmark, and you've learned to use it for navigation. Yellow flowers bloom on a tall stalk, attracting specialized bees. The leaves are coarse, alien-looking, unlike surrounding grass. The plant knows something about direction that you don't, following rules written in its genes. You respect botanical wisdom."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 3,
            gridY = 7,
            connections = listOf(
                LocationConnection("fence_line_patrol", Direction.SOUTH),
                LocationConnection("seed_head_forest", Direction.WEST),
                LocationConnection("tallgrass_labyrinth", Direction.NORTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 6
        ),

        Location(
            id = "moth_mullein_grove",
            name = "Moth Mullein Grove",
            description = LocationDescription.simple(
                "Moth mullein plants cluster in the far northern reaches—tall stalks with white flowers and fuzzy leaves. The flowers are delicate, opening in succession up the stalk. Moths visit at dusk (you've watched from safe distance). The mullein leaves are incredibly soft, covered in fine hairs that feel pleasant to brush against. The grove marks the edge of actively managed yard, transitioning to true wildland. Beyond lies territory you've explored but don't control."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 7,
            connections = listOf(
                LocationConnection("rabbit_warren_outskirts", Direction.EAST),
                LocationConnection("northern_property_corner", Direction.NORTH),
                LocationConnection("goldenrod_glade", Direction.SOUTH),
                LocationConnection("dew_collection_hollow", Direction.NORTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 6
        ),

        Location(
            id = "bird_bath_oasis",
            name = "Bird Bath Oasis",
            description = LocationDescription.simple(
                "The humans placed a bird bath in the far northern yard, a concrete basin on a pedestal. The water attracts every bird species in the region—sparrows, finches, robins, even the occasional bluebird. You can't reach the water (too high), but you benefit from the social hub it creates. Birds gossip here, sharing information about food sources and predators. You listen, learn, integrate intelligence into your territorial awareness. The oasis is neutral ground where even rivals tolerate each other briefly."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 1,
            gridY = 9,
            connections = listOf(
                LocationConnection("morning_glory_trellis", Direction.SOUTH),
                LocationConnection("singing_stones", Direction.NORTH),
                LocationConnection("tallgrass_labyrinth", Direction.SOUTHEAST),
                LocationConnection("elderwood", Direction.NORTH)
            ),
            isSafeZone = true,
            encounterRate = 0.3,
            recommendedLevel = 6
        ),

        Location(
            id = "northern_property_corner",
            name = "Northern Property Corner",
            description = LocationDescription.simple(
                "Where the northern and western fences meet, a corner post marks absolute territory boundary. The corner is overgrown with vines and forgotten by humans. This is the farthest point from the house, the wildest section of your domain. You patrol here weekly, more from ritual than necessity. The corner post leans, weathered, hosting lichens that measure time in decades. Standing here, you can survey two fence lines, feel the weight of defended territory, know the satisfaction of claimed land."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = -3,
            gridY = 8,
            connections = listOf(
                LocationConnection("moth_mullein_grove", Direction.SOUTH),
                LocationConnection("crystal_cascade", Direction.NORTH),
                LocationConnection("dew_collection_hollow", Direction.EAST),
                LocationConnection("elderwood", Direction.NORTHEAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        ),

        // ========== SUB-REGION 1F: EASTERN GRAZING LANDS (10 locations) ==========

        Location(
            id = "boulder_field",
            name = "Boulder Field",
            description = LocationDescription.simple(
                "Scattered boulders dot the eastern grassland like sleeping giants. The humans say glaciers left them (you don't know what glaciers are, but you nod). Each boulder is a landmark, basking spot, and territorial marker. Lizards compete for prime rock surfaces. Moss grows on the shaded sides. You've named each boulder privately: The Sentinel, The Throne, The Watchstone. Navigating between them requires memorization—the field tests your spatial awareness daily."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 6,
            gridY = 0,
            connections = listOf(
                LocationConnection("sun_baked_plain", Direction.WEST),
                LocationConnection("eastern_fence_border", Direction.NORTH),
                LocationConnection("sun_baked_plain", Direction.SOUTH),
                LocationConnection("scree_slope_approach", Direction.WEST),
                LocationConnection("oxeye_daisy_drift", Direction.WEST),
                LocationConnection("wind_swept_plateau", Direction.NORTHEAST),
                LocationConnection("sandsage_transition", Direction.EAST),
                LocationConnection("shortgrass_training_ground", Direction.NORTH),
                LocationConnection("grama_grass_expanse", Direction.EAST),
                LocationConnection("rocky_outcrop", Direction.NORTH),
                LocationConnection("oxeye_daisy_drift", Direction.NORTHWEST),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),

        Location(
            id = "rocky_outcrop",
            name = "Rocky Outcrop",
            description = LocationDescription.simple(
                "A cluster of rocks rises from the grassland, creating a miniature mountain range. The outcrop provides elevation—you can see across vast territories from the summit. Wind is constant here, carrying scents from distant places. Hawks perch on the highest rocks, hunting. You approach cautiously, aware of aerial danger, but the view is worth the risk. The rocks are warm, ancient, indifferent to the dramas playing out on their surfaces. Geology as philosophy."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 8,
            gridY = 0,
            connections = listOf(
                LocationConnection("boulder_field", Direction.SOUTH),
                LocationConnection("eagle_shadow_plains", Direction.NORTHWEST),
                LocationConnection("grama_grass_expanse", Direction.SOUTH),
                LocationConnection("prickly_pear_patch", Direction.NORTH),
                LocationConnection("wind_swept_plateau", Direction.NORTH),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7,
            lore = "The Rocky Outcrop is composed of granite, deposited during the last ice age approximately 12,000 years ago. The rocks don't remember this, but geologists do."
        ),

        Location(
            id = "wind_swept_plateau",
            name = "Wind-Swept Plateau",
            description = LocationDescription.simple(
                "Slightly elevated ground in the eastern territory creates a plateau where wind blows unceasingly. Grass here is short, adapted to constant wind stress. The plateau offers panoramic views but zero shelter. You cross quickly, leaning into wind, feathers ruffled. The exposure is both exhilarating and exhausting. Hawks ride the wind currents overhead, barely flapping. You feel very small, very exposed, very alive. The plateau teaches humility."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 6,
            gridY = 2,
            connections = listOf(
                LocationConnection("rocky_outcrop", Direction.SOUTH),
                LocationConnection("eastern_fence_border", Direction.EAST),
                LocationConnection("shortgrass_training_ground", Direction.SOUTHWEST),
                LocationConnection("yucca_stand", Direction.NORTH),
                LocationConnection("boulder_field", Direction.SOUTHWEST),
                LocationConnection("eagle_shadow_plains", Direction.NORTH)
            ),
            encounterRate = 0.7,
            recommendedLevel = 7
        ),

        Location(
            id = "eagle_shadow_plains",
            name = "Eagle Shadow Plains",
            description = LocationDescription.simple(
                "The eastern plains host regular eagle patrols. Their shadows sweep across grass like dark omens. You've learned to freeze when shadows pass, becoming invisible through stillness. The plains are otherwise beautiful—golden grass, wildflower patches, gentle rolls. But the eagle presence adds tension to every moment. Other creatures feel it too; everyone moves differently here, watchful, aware. The plains teach constant vigilance. You've never been caught. You intend to keep that record."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 7,
            gridY = 1,
            connections = listOf(
                LocationConnection("wind_swept_plateau", Direction.SOUTH),
                LocationConnection("grama_grass_expanse", Direction.SOUTHWEST),
                LocationConnection("yucca_stand", Direction.NORTHEAST),
                LocationConnection("prickly_pear_patch", Direction.EAST),
                LocationConnection("rocky_outcrop", Direction.SOUTHEAST),
                LocationConnection("switchgrass_savanna", Direction.WEST)
            ),
            encounterRate = 0.8,
            recommendedLevel = 8
        ),

        Location(
            id = "grama_grass_expanse",
            name = "Grama Grass Expanse",
            description = LocationDescription.simple(
                "Blue grama grass carpets the eastern reaches with curling seed heads that look like eyelashes. The grass is short, making navigation easy and exposure high. You move quickly here, minimizing time in the open. The grama is native, resilient, adapted to grazing pressure that no longer exists (no buffalo in this yard, sadly). The expanse feels ancient, connected to prairie history stretching back centuries. You're a tiny part of that continuity."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 7,
            gridY = 0,
            connections = listOf(
                LocationConnection("boulder_field", Direction.WEST),
                LocationConnection("rocky_outcrop", Direction.NORTH),
                LocationConnection("eagle_shadow_plains", Direction.NORTHEAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),

        Location(
            id = "prickly_pear_patch",
            name = "Prickly Pear Patch",
            description = LocationDescription.simple(
                "Prickly pear cacti grow in the driest section of eastern grassland—unusual this far north, but the microclimate supports them. The cacti are low, spiny, flowering yellow in early summer. You give them wide berth (those spines are serious), but admire their tenacity. The patch creates a barrier few creatures cross willingly. Fruits appear after flowering—prickly pears, technically edible, but obtaining them seems more trouble than value. The cacti stand defiant in a landscape of grass."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 8,
            gridY = 1,
            connections = listOf(
                LocationConnection("rocky_outcrop", Direction.SOUTH),
                LocationConnection("eagle_shadow_plains", Direction.WEST),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),

        Location(
            id = "yucca_stand",
            name = "Yucca Stand",
            description = LocationDescription.simple(
                "A cluster of yucca plants grows near the eastern fence—spiky rosettes of stiff leaves with occasional flowering stalks. The yuccas are architectural, sculptural, intimidating. Their leaf tips are needle-sharp (you've learned this through painful experience). Moths pollinate the flowers in a specialized relationship. The stand creates an obstacle you navigate around rather than through. The yuccas are patient, slow-growing, outliving most creatures around them. Respect."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 7,
            gridY = 2,
            connections = listOf(
                LocationConnection("wind_swept_plateau", Direction.SOUTH),
                LocationConnection("eagle_shadow_plains", Direction.SOUTHWEST),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 8
        ),

        Location(
            id = "eastern_fence_border",
            name = "Eastern Fence Border",
            description = LocationDescription.simple(
                "The eastern fence marks where grassland meets foothills, where your territory transitions to mountain country. The fence is barbed wire here, agricultural, functional rather than decorative. Beyond lies steeper terrain, rockier soil, different vegetation. You patrol this border regularly, noting changes, checking for breaches. The fence represents a threshold—grassland behind, mountains ahead, choices about which world to inhabit on any given day."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 6,
            gridY = 1,
            connections = listOf(
                LocationConnection("wind_swept_plateau", Direction.WEST),
                LocationConnection("shortgrass_training_ground", Direction.WEST),
                LocationConnection("boulder_field", Direction.SOUTH),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        ),

        Location(
            id = "shortgrass_training_ground",
            name = "Shortgrass Training Ground",
            description = LocationDescription.simple(
                "The shortest grass in your territory grows here, nibbled by rabbits and naturally adapted to drought. The training ground (you call it this) is where you practice running at full speed—excellent visibility, few obstacles, good footing. You time yourself between landmarks, improving speed incrementally. Other creatures use it similarly; you've seen rabbits doing sprints, testing their escape abilities. The ground is shared gymnasium where survival skills are honed daily."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 5,
            gridY = 1,
            connections = listOf(
                LocationConnection("boulder_field", Direction.SOUTH),
                LocationConnection("sandsage_transition", Direction.SOUTH),
                LocationConnection("eastern_fence_border", Direction.EAST),
                LocationConnection("wind_swept_plateau", Direction.NORTHEAST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 6
        ),

        Location(
            id = "sandsage_transition",
            name = "Sandsage Transition",
            description = LocationDescription.simple(
                "Where grassland gives way to foothill scrubland, sandsage appears—aromatic silver shrubs marking the biome shift. The transition zone hosts species from both ecosystems. Grass thins, rocks appear more frequently, and the vegetation changes character. You feel the shift in terrain under your feet, grassland's softness yielding to mountain's hardness. The sandsage smells wild, medicinal, promising new territories beyond. The transition invites exploration while reminding you of home behind."
            ),
            biome = BiomeType.GRASSLAND,
            gridX = 5,
            gridY = 0,
            connections = listOf(
                LocationConnection("boulder_field", Direction.WEST),
                LocationConnection("shortgrass_training_ground", Direction.NORTH),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        )
    )
}
