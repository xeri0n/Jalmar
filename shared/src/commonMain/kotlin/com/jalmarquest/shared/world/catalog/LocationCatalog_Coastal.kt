package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * COASTAL region catalog - 50 new locations expanding the seaside areas
 * Sub-regions: Harbor District Expansion (7A), Northern Cliffs (7B), Southern Coves (7C),
 *              Offshore Rocks (7D), Underwater Zones (7E)
 * Connects to existing locations: harbor_town, cliffside, lighthouse_point, shipwreck_cove, tidepool
 */
internal val COASTAL_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 7A: Harbor District Expansion (10 locations, levels 5-7) ====================
        // Grid: X: -3 to -2, Y: -1 to 2
        // Theme: Bustling docks, trade, civilization, maritime commerce
        
        Location(
            id = "fishing_wharf",
            name = "Fishing Wharf",
            description = LocationDescription.simple(
                "Wooden docks extend into the harbor, boats of all sizes tied alongside. Fishermen unload their catches—fish gleaming silver, crabs scuttling in baskets, octopi writhing in nets. The wharf smells of salt, fish, and tar. Gulls scream overhead, diving for scraps. From your perspective, the boats are floating fortresses, the fishermen giants going about their trade. The wharf is busy, chaotic, and alive with commerce."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 0,
            connections = listOf(
                LocationConnection("harbor_town", Direction.EAST),
                LocationConnection("merchant_row", Direction.NORTH),
                LocationConnection("fish_market", Direction.SOUTH),
                LocationConnection("dock_warehouse", Direction.WEST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 5
        ),
        
        Location(
            id = "merchant_row",
            name = "Merchant Row",
            description = LocationDescription.simple(
                "A street lined with shops and stalls—merchants selling everything from ship supplies to exotic goods from distant lands. Colorful awnings shade wares: silk fabrics, spices, ceramics, metalwork. The street is crowded with buyers and sellers, haggling creating a constant din. Merchants call out their wares, competing for attention. This is commerce at its most vibrant, goods from across the world concentrated in one street."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 1,
            connections = listOf(
                LocationConnection("fishing_wharf", Direction.SOUTH),
                LocationConnection("harbor_masters_office", Direction.NORTH),
                LocationConnection("dock_warehouse", Direction.SOUTHWEST),
                LocationConnection("harbor_town", Direction.EAST),
                LocationConnection("cliffside", Direction.NORTH),
                LocationConnection("sailmakers_loft", Direction.WEST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 5,
            shopAvailable = true
        ),
        
        Location(
            id = "fish_market",
            name = "Fish Market",
            description = LocationDescription.simple(
                "A covered market where fresh catch is sold daily. Ice keeps fish fresh in the heat. Vendors shout prices, customers inspect fish with critical eyes. The variety is astounding—dozens of species, each with distinct appearance and flavor. The market smells powerfully of fish and brine. Cats prowl underfoot, hoping for scraps. For you, navigating the market means dodging feet and avoiding being stepped on."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = -1,
            connections = listOf(
                LocationConnection("fishing_wharf", Direction.NORTH),
                LocationConnection("shipwrights_yard", Direction.SOUTH),
                LocationConnection("harbor_town", Direction.EAST),
                LocationConnection("shipwreck_cove", Direction.SOUTH),
                LocationConnection("salt_works", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5
        ),
        
        Location(
            id = "dock_warehouse",
            name = "Dock Warehouse",
            description = LocationDescription.simple(
                "Massive warehouses store imported and exported goods. Crates are stacked to the ceiling—tea, sugar, spices, textiles, manufactured goods. The warehouse is dim, lit by narrow windows. Rats are a constant problem, necessitating vigilance and traps. Dock workers move crates with hand trucks and cranes. The warehouse smells of wood, rope, and the accumulated scents of its diverse contents."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 0,
            connections = listOf(
                LocationConnection("fishing_wharf", Direction.EAST),
                LocationConnection("merchant_row", Direction.NORTHEAST),
                LocationConnection("salt_works", Direction.SOUTH),
                LocationConnection("customs_house", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "sailmakers_loft",
            name = "Sailmaker's Loft",
            description = LocationDescription.simple(
                "Canvas sails are cut and sewn in this large workspace. Sailmakers are skilled craftspeople, measuring and stitching with precision. The loft is bright, lit by large windows for detailed work. Canvas rolls, thread spools, and needles (each needle larger than you) fill the space. The smell is of canvas and pitch. Watching sailmakers work is mesmerizing—creating the massive sheets that power ships."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 1,
            connections = listOf(
                LocationConnection("merchant_row", Direction.EAST),
                LocationConnection("customs_house", Direction.NORTH),
                LocationConnection("customs_house", Direction.SOUTH),
                LocationConnection("rope_walk", Direction.NORTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5
        ),
        
        Location(
            id = "customs_house",
            name = "Customs House",
            description = LocationDescription.simple(
                "Officials inspect imported goods here, assessing taxes and checking for contraband. The customs house is orderly but bureaucratic, clerks recording every transaction. Merchants sometimes try to bribe officials, adding tension to routine inspections. The building is well-guarded—customs revenue funds the government, making protection essential. Navigating customs politics can be as dangerous as navigating storms."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 2,
            connections = listOf(
                LocationConnection("sailmakers_loft", Direction.SOUTH),
                LocationConnection("sailmakers_loft", Direction.NORTH),
                LocationConnection("dock_warehouse", Direction.SOUTH),
                LocationConnection("rope_walk", Direction.NORTH),
                LocationConnection("harbor_masters_office", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),
        
        Location(
            id = "rope_walk",
            name = "Rope Walk",
            description = LocationDescription.simple(
                "A long building where rope is made—fibers twisted into cord, cords twisted into rope. The rope walk must be very long to accommodate rope-making's linear process. Workers walk backward, spinning fibers as they go. The finished rope is strong enough to moor ships or rig sails. From your scale, even thin rope is a massive cable, each strand visible and substantial."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("customs_house", Direction.SOUTH),
                LocationConnection("sailmakers_loft", Direction.SOUTH),
                LocationConnection("harbor_masters_office", Direction.EAST),
                LocationConnection("cliff_stairway", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),
        
        Location(
            id = "harbor_masters_office",
            name = "Harbor Master's Office",
            description = LocationDescription.simple(
                "The harbor master coordinates ship traffic, assigns berths, and resolves disputes. The office overlooks the harbor, providing a commanding view of all activity. Charts cover the walls showing depths, hazards, and approaches. The harbor master is authoritative, decisions final and binding. In busy seasons, the office is chaotic—multiple ships arriving simultaneously, crews demanding priority, the harbor master juggling logistics."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 2,
            connections = listOf(
                LocationConnection("cliffside", Direction.NORTH),
                LocationConnection("merchant_row", Direction.SOUTH),
                LocationConnection("customs_house", Direction.WEST),
                LocationConnection("rope_walk", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 6,
            isSettlement = true
        ),
        
        Location(
            id = "salt_works",
            name = "Salt Works",
            description = LocationDescription.simple(
                "Seawater is evaporated in shallow pans, leaving salt behind. The salt works are labor-intensive—water must be channeled, evaporation monitored, salt harvested and processed. The finished product is essential for preserving fish and meat. The works smell strongly of brine. Salt crystals coat everything, glittering in sunlight. Workers are weathered and tough, accustomed to sun and repetitive labor."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -1,
            connections = listOf(
                LocationConnection("dock_warehouse", Direction.NORTH),
                LocationConnection("fish_market", Direction.EAST),
                LocationConnection("tidepool", Direction.SOUTH),
                LocationConnection("evaporation_ponds", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "shipwrights_yard",
            name = "Shipwright's Yard",
            description = LocationDescription.simple(
                "Ships are built and repaired here—a complex operation requiring skilled craftspeople. Hulls are constructed from massive timbers, caulked to be watertight, fitted with masts and rigging. The yard smells of sawdust, pitch, and paint. The sound of hammering and sawing is constant. Watching a ship take shape is impressive—tons of wood and metal transformed into a vessel capable of crossing oceans."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = -2,
            connections = listOf(
                LocationConnection("fish_market", Direction.NORTH),
                LocationConnection("shipwreck_cove", Direction.EAST),
                LocationConnection("dry_dock", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),

        // ==================== SUB-REGION 7B: Northern Cliffs (15 locations, levels 4-8) ====================
        // Grid: X: -4 to -2, Y: 2 to 5
        // Theme: Sheer drops, seabird colonies, waves crashing on rocks
        
        Location(
            id = "cliff_stairway",
            name = "Cliff Stairway",
            description = LocationDescription.simple(
                "Stairs carved into the cliff face provide access between harbor and clifftop. The stairs are steep, narrow, and worn smooth by generations of use. Climbing them is exhausting; descending them tests nerves as you look down sheer drops. Handrails help, but wind can still knock you off balance. The stairs are necessary but treacherous, a daily challenge for cliff dwellers."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("rope_walk", Direction.SOUTH),
                LocationConnection("guano_quarry", Direction.WEST),
                LocationConnection("cliffside", Direction.EAST),
                LocationConnection("clifftop_meadow", Direction.NORTH),
                LocationConnection("gull_nesting_cliff", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6
        ),
        
        Location(
            id = "clifftop_meadow",
            name = "Clifftop Meadow",
            description = LocationDescription.simple(
                "Grass grows on clifftops, fertilized by centuries of seabird droppings. The meadow is windswept but beautiful, wildflowers blooming in spring and summer. The view is spectacular—ocean extending to the horizon, ships visible as distant dots. The edge drops away to nothing, requiring constant awareness. Sheep graze here, kept back from the cliff edge by fencing."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("cliff_stairway", Direction.SOUTH),
                LocationConnection("puffin_burrows", Direction.SOUTH),
                LocationConnection("seal_haul_out", Direction.WEST),
                LocationConnection("lighthouse_point", Direction.EAST),
                LocationConnection("gull_nesting_cliff", Direction.WEST),
                LocationConnection("clifftop_ruins", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 5
        ),
        
        Location(
            id = "gull_nesting_cliff",
            name = "Gull Nesting Cliff",
            description = LocationDescription.simple(
                "Thousands of gulls nest on cliff ledges, their colony a raucous city. The noise is deafening—constant screaming and calling. Nests cover every available ledge, packed densely. The gulls are aggressive, defending nests by dive-bombing intruders. Guano coats the rocks white, accumulating in thick layers. The smell is overpowering. Navigating the colony requires helmets and nerve."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 3,
            connections = listOf(
                LocationConnection("cliff_stairway", Direction.EAST),
                LocationConnection("clifftop_meadow", Direction.EAST),
                LocationConnection("seal_haul_out", Direction.NORTH),
                LocationConnection("guano_quarry", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 7
        ),
        
        Location(
            id = "seal_haul_out",
            name = "Seal Haul-Out",
            description = LocationDescription.simple(
                "Seals haul out on rocky beaches below the cliffs, basking in sun and resting between fishing trips. The seals are massive—each one larger than a human from your perspective. They're surprisingly agile on land, capable of sudden lunges if threatened. Pups are born here in season, adding vulnerability and ferocity to the colony. Bull seals fight for dominance, their battles brutal and bloody."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("gull_nesting_cliff", Direction.SOUTH),
                LocationConnection("seal_rock", Direction.SOUTH),
                LocationConnection("kelp_forest_shallows", Direction.SOUTH),
                LocationConnection("clifftop_meadow", Direction.EAST),
                LocationConnection("cliff_cave_network", Direction.NORTH),
                LocationConnection("kelp_forest_shallows", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7,
            lore = "Harbor seals can hold their breath for 30+ minutes and dive to 1,500 feet depth. Their whiskers detect fish movement in murky water. Despite their size, they're prey for orcas and great white sharks."
        ),
        
        Location(
            id = "cliff_cave_network",
            name = "Cliff Cave Network",
            description = LocationDescription.simple(
                "Waves have carved caves into the cliff face—sea caves accessible only at low tide. The caves extend deep into rock, some connecting to form complex networks. Exploring them is dangerous—rising tide can trap you inside, waves surge unpredictably, footing is slick. But the caves are beautiful—rock polished smooth, pools reflecting light, barnacles and anemones decorating walls."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 5,
            connections = listOf(
                LocationConnection("seal_haul_out", Direction.SOUTH),
                LocationConnection("clifftop_ruins", Direction.EAST),
                LocationConnection("smugglers_cave", Direction.DOWN),
                LocationConnection("sea_arch", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "clifftop_ruins",
            name = "Clifftop Ruins",
            description = LocationDescription.simple(
                "Ancient stone foundations mark where a structure once stood. The building is long gone, collapsed or deliberately dismantled, but foundations remain. The ruins overlook the ocean, suggesting the building was a lighthouse, fort, or temple. Erosion threatens what remains—cliff edges are unstable, sections calving into the sea. The ruins are atmospheric, wind whistling through gaps in stonework."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("clifftop_meadow", Direction.SOUTH),
                LocationConnection("razorbill_nesting_ledge", Direction.EAST),
                LocationConnection("memorial_garden", Direction.NORTH),
                LocationConnection("sea_stack_colony", Direction.EAST),
                LocationConnection("lighthouse_point", Direction.EAST),
                LocationConnection("cliff_cave_network", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "sea_stack_colony",
            name = "Sea Stack Colony",
            description = LocationDescription.simple(
                "Erosion has isolated pillars of rock from the main cliff—sea stacks rising from waves. Seabirds nest on the stacks: puffins, murres, cormorants. The birds commute between stacks and ocean, fishing and returning with food. The stacks are inaccessible to land predators, making them ideal nesting sites. Watching thousands of birds swirl around the stacks is mesmerizing."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 4,
            connections = listOf(
                LocationConnection("lighthouse_point", Direction.SOUTH),
                LocationConnection("clifftop_ruins", Direction.WEST),
                LocationConnection("razorbill_nesting_ledge", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 6
        ),
        
        Location(
            id = "guano_quarry",
            name = "Guano Quarry",
            description = LocationDescription.simple(
                "Centuries of seabird droppings have accumulated into thick guano deposits. The guano is mined for fertilizer—rich in nitrogen, phosphorus, and potassium. Mining is unpleasant work: the smell is terrible, dust is choking, and birds attack workers. But guano is valuable, driving a small industry. The quarry demonstrates how even bird waste can become a resource."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 2,
            connections = listOf(
                LocationConnection("gull_nesting_cliff", Direction.NORTH),
                LocationConnection("cliff_stairway", Direction.EAST),
                LocationConnection("evaporation_ponds", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 6,
            lore = "Guano was so valuable in the 1800s that nations fought wars over access to deposits. Peruvian and Chilean guano islands produced millions of tons of fertilizer, crucial for agriculture before synthetic fertilizers."
        ),
        
        Location(
            id = "razorbill_nesting_ledge",
            name = "Razorbill Nesting Ledge",
            description = LocationDescription.simple(
                "Razorbills nest on narrow cliff ledges, their eggs precariously balanced on bare rock. The eggs are pyriform—pointed at one end—so they roll in circles rather than off ledges if disturbed. Razorbills are penguin-like, black and white, excellent swimmers. They dive for fish, bringing them back to chicks. The ledges are crowded, thousands of birds packed together."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 5,
            connections = listOf(
                LocationConnection("sea_stack_colony", Direction.SOUTH),
                LocationConnection("blowhole", Direction.NORTH),
                LocationConnection("wreck_viewing_platform", Direction.NORTH),
                LocationConnection("clifftop_ruins", Direction.WEST),
                LocationConnection("lighthouse_point", Direction.SOUTHEAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "sea_arch",
            name = "Sea Arch",
            description = LocationDescription.simple(
                "Wave erosion has carved an arch through a rocky promontory—a natural bridge over churning water. The arch is spectacular, demonstrating erosion's power. Walking across the arch's top is possible but terrifying; the rock is weathered and unstable. Below, waves crash through the arch, creating booming sounds. Eventually, the arch will collapse, becoming a sea stack, continuing the cycle of erosion."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = 5,
            connections = listOf(
                LocationConnection("cliff_cave_network", Direction.SOUTH),
                LocationConnection("kelp_forest_shallows", Direction.SOUTHWEST),
                LocationConnection("pelican_nesting_rock", Direction.WEST),
                LocationConnection("kelp_forest_shallows", Direction.WEST),
                LocationConnection("storm_watch_point", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "storm_watch_point",
            name = "Storm Watch Point",
            description = LocationDescription.simple(
                "The most exposed clifftop, where storms hit with full force. During storms, waves crash against cliffs with incredible violence, spray reaching clifftop heights. Wind is powerful enough to knock you over. But the view during storms is spectacular—nature's raw power on display. Storm watchers come here to witness the ocean's fury, accepting the danger for the experience."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = 6,
            connections = listOf(
                LocationConnection("sea_arch", Direction.SOUTH),
                LocationConnection("pelican_nesting_rock", Direction.WEST),
                LocationConnection("wreck_viewing_platform", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "wreck_viewing_platform",
            name = "Wreck Viewing Platform",
            description = LocationDescription.simple(
                "A clifftop platform overlooks a famous shipwreck site below. Ships that misjudged the approach or were driven by storms wrecked on rocks. Their remains are visible: broken timbers, rusted metal, barnacle-encrusted debris. The platform includes plaques commemorating lost ships and crews. The view is sobering, a reminder that the sea claims victims despite human skill and technology."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 6,
            connections = listOf(
                LocationConnection("storm_watch_point", Direction.WEST),
                LocationConnection("razorbill_nesting_ledge", Direction.SOUTH),
                LocationConnection("memorial_garden", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 7
        ),
        
        Location(
            id = "memorial_garden",
            name = "Memorial Garden",
            description = LocationDescription.simple(
                "A small garden memorializes sailors lost at sea. Stone markers list names and dates, flowers are planted and maintained. The garden is peaceful, offering clifftop views without the exposed danger of other points. Families visit to remember loved ones. The garden represents humanity's relationship with the ocean—respect, fear, love, and loss all intermingled."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = 6,
            connections = listOf(
                LocationConnection("wreck_viewing_platform", Direction.WEST),
                LocationConnection("blowhole", Direction.EAST),
                LocationConnection("clifftop_ruins", Direction.SOUTH),
                LocationConnection("lighthouse_point", Direction.SOUTHEAST)
            ),
            encounterRate = 0.40,
            recommendedLevel = 6,
            isSafeZone = true
        ),
        
        Location(
            id = "puffin_burrows",
            name = "Puffin Burrows",
            description = LocationDescription.simple(
                "Puffins dig burrows in clifftop soil for nesting. The burrows honeycomb the ground, creating a network of tunnels. Puffins are comical birds—brightly colored beaks, awkward on land but graceful swimmers. They catch fish and bring them back to chicks, multiple fish held crosswise in their beaks. The burrow colony is charming, puffins popping in and out of holes constantly."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 3,
            connections = listOf(
                LocationConnection("cliffside", Direction.SOUTH),
                LocationConnection("lighthouse_point", Direction.EAST),
                LocationConnection("clifftop_meadow", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 5,
            lore = "Atlantic puffins spend most of their lives at sea, coming to land only to breed. They can dive to 200 feet and 'fly' underwater using their wings. Puffin populations are declining due to climate change affecting fish availability."
        ),
        
        Location(
            id = "blowhole",
            name = "Blowhole",
            description = LocationDescription.simple(
                "Wave pressure forces water through a vertical shaft in the cliff, creating a blowhole that shoots spray high into the air. The blowhole erupts irregularly, timing dependent on wave size. Standing too close risks being drenched or knocked over by spray. The blowhole roars as it erupts, water thundering through confined space. It's spectacular and dangerous, a natural fountain powered by ocean energy."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("razorbill_nesting_ledge", Direction.SOUTH),
                LocationConnection("memorial_garden", Direction.WEST),
                LocationConnection("lighthouse_point", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),

        // ==================== SUB-REGION 7C: Southern Coves (10 locations, levels 6-9) ====================
        // Grid: X: -4 to -2, Y: -3 to -1
        // Theme: Hidden beaches, pirate lore, treasure hunting, smuggling
        
        Location(
            id = "smugglers_cave",
            name = "Smuggler's Cave",
            description = LocationDescription.simple(
                "A sea cave used by smugglers to hide contraband. The cave is accessible only during low tide, its entrance hidden from casual observation. Inside, the cave is surprisingly spacious, with side chambers for storage. Evidence of smuggling remains: old crates, rope, bottles. The cave still sees occasional use, making encounters with smugglers possible. Discretion is advisable."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -2,
            connections = listOf(
                LocationConnection("tidepool", Direction.NORTH),
                LocationConnection("cliff_cave_network", Direction.UP),
                LocationConnection("hidden_cove", Direction.SOUTH),
                LocationConnection("underwater_passage", Direction.DOWN)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "hidden_cove",
            name = "Hidden Cove",
            description = LocationDescription.simple(
                "A small beach sheltered by rock formations, invisible from most vantage points. The cove offers privacy and protection from waves. Pirates and smugglers used it for centuries—a perfect hideaway for illicit activities. The beach is beautiful: white sand, clear water, rocks forming natural pools. But the isolation that makes it attractive also makes it dangerous if trapped by rising tide."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -3,
            connections = listOf(
                LocationConnection("smugglers_cave", Direction.NORTH),
                LocationConnection("message_in_bottle_beach", Direction.EAST),
                LocationConnection("hermit_crab_colony", Direction.WEST),
                LocationConnection("underwater_passage", Direction.WEST),
                LocationConnection("shipwreck_cove", Direction.EAST),
                LocationConnection("treasure_beach", Direction.WEST),
                LocationConnection("pirate_graveyard", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "pirate_graveyard",
            name = "Pirate Graveyard",
            description = LocationDescription.simple(
                "Pirates buried their dead on this beach, marked by simple wooden crosses and cairns. The graveyard is grim—sand constantly shifting to reveal and conceal graves. Skulls and bones occasionally wash up, reminders of the pirates who lived and died by the sea. Local superstition claims the beach is haunted, pirates' ghosts still prowling. Whether true or not, the atmosphere is undeniably eerie."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -3,
            connections = listOf(
                LocationConnection("hidden_cove", Direction.NORTH),
                LocationConnection("mangrove_edge", Direction.SOUTH),
                LocationConnection("treasure_beach", Direction.EAST),
                LocationConnection("abandoned_dock", Direction.SOUTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 9,
            lore = "Pirates operated outside legal systems, creating their own rules. Pirate codes governed behavior, dividing loot and settling disputes. Famous pirates like Blackbeard and Anne Bonny became legends, their stories blending fact and fiction."
        ),
        
        Location(
            id = "treasure_beach",
            name = "Treasure Beach",
            description = LocationDescription.simple(
                "Legend claims pirates buried treasure here centuries ago. Treasure hunters have dug countless holes, searching for gold that may or may not exist. The beach is pockmarked with excavations, some fresh, others ancient. Occasionally, coins or artifacts wash up, fueling continued searches. Whether actual treasure remains or it's all been found is unknown, but the searching continues."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -4,
            connections = listOf(
                LocationConnection("hidden_cove", Direction.EAST),
                LocationConnection("pirate_graveyard", Direction.WEST),
                LocationConnection("abandoned_dock", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "abandoned_dock",
            name = "Abandoned Dock",
            description = LocationDescription.simple(
                "A rotting dock extends into the water, remnant of a failed enterprise or abandoned settlement. The wooden pilings are covered in barnacles and mussels. The dock is unstable, planks missing or broken. But it provides access to deeper water and serves as a fishing platform. Crabs and fish shelter under the dock, making it a productive hunting ground despite its decay."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -5,
            connections = listOf(
                LocationConnection("treasure_beach", Direction.NORTH),
                LocationConnection("sunken_galleon", Direction.DOWN),
                LocationConnection("pirate_graveyard", Direction.NORTH),
                LocationConnection("mangrove_edge", Direction.WEST),
                LocationConnection("shipwreck_cove", Direction.NORTHEAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "mangrove_edge",
            name = "Mangrove Edge",
            description = LocationDescription.simple(
                "Where ocean meets swamp, mangroves create a transitional ecosystem. The trees' prop roots extend into salt water, providing nursery habitat for fish and invertebrates. The mangroves filter water and protect coastlines from erosion. This edge zone is incredibly productive, teeming with life. Juvenile fish, crabs, and birds are abundant. The mangroves connect coastal and swamp ecosystems."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -5,
            connections = listOf(
                LocationConnection("abandoned_dock", Direction.EAST),
                LocationConnection("sea_urchin_garden", Direction.NORTH),
                LocationConnection("pirate_graveyard", Direction.NORTH),
                LocationConnection("ghost_crab_burrows", Direction.WEST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "message_in_bottle_beach",
            name = "Message in Bottle Beach",
            description = LocationDescription.simple(
                "Currents deposit flotsam on this beach in remarkable quantities. Among driftwood and seaweed, messages in bottles occasionally wash up—notes from distant ships, pleas for rescue, love letters cast to the waves. Most bottles are empty or contain unreadable fragments, but occasionally you find intact messages. Each bottle is a connection to strangers separated by time and ocean."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = -3,
            connections = listOf(
                LocationConnection("shipwreck_cove", Direction.SOUTH),
                LocationConnection("sea_glass_beach", Direction.EAST),
                LocationConnection("hidden_cove", Direction.WEST),
                LocationConnection("dry_dock", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "dry_dock",
            name = "Dry Dock",
            description = LocationDescription.simple(
                "A ship repair facility where vessels are pulled from water for hull maintenance. The dry dock drains, allowing access to the ship's bottom—normally underwater. Workers scrape barnacles, repair planking, replace copper sheathing. The dry dock is an engineering marvel, gates sealing to keep water out, pumps removing seepage. Ships are vulnerable here, dependent on skilled workers for seaworthiness."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = -3,
            connections = listOf(
                LocationConnection("shipwrights_yard", Direction.NORTH),
                LocationConnection("message_in_bottle_beach", Direction.SOUTH),
                LocationConnection("barnacle_fortress", Direction.WEST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),
        
        Location(
            id = "driftwood_sculpture_beach",
            name = "Driftwood Sculpture Beach",
            description = LocationDescription.simple(
                "Storms deposit massive amounts of driftwood here—tree trunks, branches, planks from wrecked ships. Artists have arranged the wood into sculptures: abstract forms, animals, faces. The sculptures are temporary, eventually destroyed by new storms, but artists return to create anew. The beach is a gallery where art and nature collaborate, each storm both destroying and providing new materials."
            ),
            biome = BiomeType.COASTAL,
            gridX = -1,
            gridY = -2,
            connections = listOf(
                LocationConnection("shipwreck_cove", Direction.WEST),
                LocationConnection("kelp_forest_shallows", Direction.WEST),
                LocationConnection("sea_glass_beach", Direction.SOUTH),
                LocationConnection("tidepool", Direction.NORTH),
                LocationConnection("kelp_forest_shallows", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),
        
        Location(
            id = "sea_glass_beach",
            name = "Sea Glass Beach",
            description = LocationDescription.simple(
                "Broken glass tumbled by waves becomes sea glass—smooth, frosted gems in various colors. This beach is covered in sea glass, glittering in sunlight like treasure. Collectors search for rare colors: red, orange, turquoise. The glass represents human waste transformed by nature into beauty. Each piece has a story—bottles, windows, dishes—broken and reclaimed by the ocean."
            ),
            biome = BiomeType.COASTAL,
            gridX = -1,
            gridY = -3,
            connections = listOf(
                LocationConnection("driftwood_sculpture_beach", Direction.NORTH),
                LocationConnection("message_in_bottle_beach", Direction.WEST),
                LocationConnection("coral_reef_garden", Direction.DOWN)
            ),
            encounterRate = 0.45,
            recommendedLevel = 6,
            lore = "Sea glass forms when glass is tumbled by waves and sand for years, edges smoothing and surface frosting. It can take 20-30 years to create. As glass bottles become less common, sea glass is becoming rarer, making it increasingly collectible."
        ),

        // ==================== SUB-REGION 7D: Offshore Rocks (10 locations, levels 7-10) ====================
        // Grid: X: -5 to -3, Y: -2 to 3
        // Theme: Isolated rocks, kelp forests, marine life, challenging navigation
        
        Location(
            id = "seal_rock",
            name = "Seal Rock",
            description = LocationDescription.simple(
                "A large rock offshore where seals haul out to rest and bask. Hundreds of seals cover the rock, barking constantly. The rock is slick with algae and seal waste, making footing treacherous. Bulls defend territory aggressively, fighting rivals and threatening intruders. Approaching seal rock during breeding season is particularly dangerous—protective mothers and aggressive bulls create a gauntlet of teeth and muscle."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = 0,
            connections = listOf(
                LocationConnection("kelp_forest_shallows", Direction.EAST),
                LocationConnection("cormorant_roosting_rock", Direction.NORTH),
                LocationConnection("seal_haul_out", Direction.NORTH),
                LocationConnection("barnacle_fortress", Direction.SOUTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 8
        ),
        
        Location(
            id = "kelp_forest_shallows",
            name = "Kelp Forest Shallows",
            description = LocationDescription.simple(
                "Giant kelp creates underwater forests in shallow water. From the surface, kelp appears as floating mats, but beneath extends a three-dimensional ecosystem. Kelp stipes rise like tree trunks, fronds forming canopy. Fish, octopi, and invertebrates live among kelp. Sea otters wrap themselves in kelp while sleeping to avoid drifting. The forest is beautiful and productive, one of ocean's richest habitats."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = 0,
            connections = listOf(
                LocationConnection("seal_rock", Direction.WEST),
                LocationConnection("sea_arch", Direction.EAST),
                LocationConnection("seal_haul_out", Direction.EAST),
                LocationConnection("driftwood_sculpture_beach", Direction.NORTH),
                LocationConnection("cormorant_roosting_rock", Direction.NORTH),
                LocationConnection("seal_haul_out", Direction.NORTH),
                LocationConnection("sea_arch", Direction.NORTHEAST),
                LocationConnection("barnacle_fortress", Direction.SOUTH),
                LocationConnection("driftwood_sculpture_beach", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7,
            lore = "Giant kelp can grow up to 2 feet per day, reaching 150+ feet in length. They attach to rocks via holdfasts and use gas-filled bladders to float fronds toward sunlight. Kelp forests support complex food webs and protect coastlines from erosion."
        ),
        
        Location(
            id = "barnacle_fortress",
            name = "Barnacle Fortress",
            description = LocationDescription.simple(
                "A rocky outcrop completely encrusted with barnacles—millions of them covering every surface. The barnacles create a textured landscape, sharp and inhospitable to soft-bodied creatures. Barnacles are filter feeders, extending feathery appendages to catch plankton when submerged. At low tide, they close tightly, surviving exposure. The 'fortress' nickname comes from the barnacles' defensive shell coverage."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = -1,
            connections = listOf(
                LocationConnection("seal_rock", Direction.NORTH),
                LocationConnection("kelp_forest_shallows", Direction.NORTH),
                LocationConnection("dry_dock", Direction.EAST),
                LocationConnection("mussel_bed_rocks", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8
        ),
        
        Location(
            id = "mussel_bed_rocks",
            name = "Mussel Bed Rocks",
            description = LocationDescription.simple(
                "Mussels colonize intertidal rocks in dense beds, each mussel attached by byssal threads—protein strands they produce. The beds are purple-blue, shells packed so tightly they seem continuous. Mussels filter water, consuming plankton and producing waste that supports other organisms. Starfish prey on mussels, creating gaps in beds that allow diversity. The mussel beds are edible but harvesting requires caution—red tide can contaminate them with deadly toxins."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = -2,
            connections = listOf(
                LocationConnection("barnacle_fortress", Direction.NORTH),
                LocationConnection("sea_urchin_garden", Direction.SOUTH),
                LocationConnection("anemone_pool", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "sea_urchin_garden",
            name = "Sea Urchin Garden",
            description = LocationDescription.simple(
                "Shallow areas are populated by sea urchins—spiny spheres that graze on algae. The urchins' spines provide defense against most predators, though sea otters have learned to crack them open. From your perspective, sea urchins are significant obstacles—spines capable of penetrating skin and breaking off, causing painful infections. The urchins are beautiful in their symmetry, alien and geometric."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = -3,
            connections = listOf(
                LocationConnection("mussel_bed_rocks", Direction.NORTH),
                LocationConnection("hermit_crab_colony", Direction.EAST),
                LocationConnection("anemone_pool", Direction.EAST),
                LocationConnection("mangrove_edge", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 8,
            lore = "Sea urchins are keystone species in kelp forest ecosystems. When urchin populations explode, they overgraze kelp, creating 'urchin barrens'—areas devoid of vegetation. Sea otters control urchin populations, demonstrating predator importance in ecosystem balance."
        ),
        
        Location(
            id = "anemone_pool",
            name = "Anemone Pool",
            description = LocationDescription.simple(
                "Tide pools filled with sea anemones—soft-bodied cnidarians with stinging tentacles. The anemones wave tentacles in the current, catching plankton and small animals. Clownfish (from your scale, quite large) sometimes live symbiotically with anemones, immune to stings. The pools are colorful—anemones in reds, greens, and purples. Touching anemones causes tentacles to retract and delivers mild stings."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -2,
            connections = listOf(
                LocationConnection("mussel_bed_rocks", Direction.WEST),
                LocationConnection("evaporation_ponds", Direction.NORTH),
                LocationConnection("sea_urchin_garden", Direction.WEST),
                LocationConnection("tidepool", Direction.EAST),
                LocationConnection("hermit_crab_colony", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 7
        ),
        
        Location(
            id = "hermit_crab_colony",
            name = "Hermit Crab Colony",
            description = LocationDescription.simple(
                "Hermit crabs swarm shallow areas, each occupying a borrowed shell. As crabs grow, they must find larger shells, leading to shell exchanges and occasionally violent competition. The crabs are scavengers, cleaning up dead matter. From your perspective, hermit crabs are significant creatures, each one substantial and potentially aggressive when defending shells. The colony is constantly moving, a living carpet of shells."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -3,
            connections = listOf(
                LocationConnection("anemone_pool", Direction.NORTH),
                LocationConnection("octopus_lair", Direction.DOWN),
                LocationConnection("sea_urchin_garden", Direction.WEST),
                LocationConnection("hidden_cove", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "cormorant_roosting_rock",
            name = "Cormorant Roosting Rock",
            description = LocationDescription.simple(
                "Cormorants roost on offshore rocks, drying their wings after fishing. Unlike most seabirds, cormorant feathers aren't fully waterproof—they absorb water, reducing buoyancy for diving but requiring drying afterward. The birds stand with wings spread, looking heraldic. Guano coats the rocks white. The cormorants fish cooperatively, driving schools into shallows where they're easier to catch."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = 1,
            connections = listOf(
                LocationConnection("seal_rock", Direction.SOUTH),
                LocationConnection("kelp_forest_shallows", Direction.SOUTH),
                LocationConnection("pelican_nesting_rock", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 7
        ),
        
        Location(
            id = "pelican_nesting_rock",
            name = "Pelican Nesting Rock",
            description = LocationDescription.simple(
                "Brown pelicans nest on isolated rocks, their large nests built from seaweed and sticks. Pelicans are spectacular fishers, plunging from height to catch fish in expandable throat pouches. From your perspective, pelicans are massive—their wingspans enormous, bills like scoops. Nesting season makes them territorial; approaching too close risks aggressive defense. The nests smell of fish and guano."
            ),
            biome = BiomeType.COASTAL,
            gridX = -5,
            gridY = 2,
            connections = listOf(
                LocationConnection("cormorant_roosting_rock", Direction.SOUTH),
                LocationConnection("storm_watch_point", Direction.EAST),
                LocationConnection("sea_arch", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "evaporation_ponds",
            name = "Evaporation Ponds",
            description = LocationDescription.simple(
                "Shallow ponds where seawater evaporates, concentrating salt. The ponds are artificially maintained for salt production. As concentration increases, ponds turn pink—color from salt-tolerant bacteria and algae. The ponds support unique extremophile life adapted to high salinity. Birds wade in ponds, feeding on brine shrimp and flies. The ponds are beautiful—pink water, white salt crusts, blue sky."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -1,
            connections = listOf(
                LocationConnection("salt_works", Direction.EAST),
                LocationConnection("guano_quarry", Direction.NORTH),
                LocationConnection("anemone_pool", Direction.SOUTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 6
        ),

        // ==================== SUB-REGION 7E: Underwater Zones (5 locations, levels 10-15) ====================
        // Grid: Multi-level via DOWN connections
        // Theme: Subaquatic exploration, shipwrecks, underwater caves
        
        Location(
            id = "coral_reef_garden",
            name = "Coral Reef Garden",
            description = LocationDescription.simple(
                "A small coral reef thrives in warm shallows—corals building calcium carbonate structures over centuries. The reef is colorful: corals in reds, yellows, purples; fish in electric blues and yellows; anemones swaying. The reef is an oasis of life, supporting hundreds of species. Snorkeling here reveals an alien world, beautiful and complex. But reefs are fragile—touching damages corals that took decades to grow."
            ),
            biome = BiomeType.COASTAL,
            gridX = -2,
            gridY = -4,
            connections = listOf(
                LocationConnection("sea_glass_beach", Direction.UP),
                LocationConnection("sunken_galleon", Direction.SOUTH),
                LocationConnection("octopus_lair", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10,
            lore = "Coral reefs cover <1% of ocean floor but support 25% of marine species. Corals are colonial animals living symbiotically with photosynthetic algae. Climate change and ocean acidification threaten reefs globally, bleaching corals and dissolving their structures."
        ),
        
        Location(
            id = "sunken_galleon",
            name = "Sunken Galleon",
            description = LocationDescription.simple(
                "A Spanish galleon rests on the seafloor, sunk centuries ago by storm or battle. The ship is remarkably preserved—hull intact, masts fallen, cannons visible. The galleon is encrusted with marine life: corals, sponges, barnacles. Fish shelter in the wreck, making it an artificial reef. Legends claim the ship carried treasure, attracting divers who search the wreck hoping for gold. The galleon is beautiful and haunting, a monument to maritime history."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -5,
            connections = listOf(
                LocationConnection("coral_reef_garden", Direction.NORTH),
                LocationConnection("abandoned_dock", Direction.UP)
            ),
            encounterRate = 0.80,
            recommendedLevel = 12
        ),
        
        Location(
            id = "octopus_lair",
            name = "Octopus Lair",
            description = LocationDescription.simple(
                "A giant Pacific octopus has claimed a rocky crevice as its lair. The octopus is intelligent, curious, and potentially dangerous. It decorates its lair entrance with shells and crab carapaces—trophies from meals. The octopus can change color and texture instantly, disappearing against backgrounds. It hunts at night, arms exploring crevices for prey. Encountering the octopus is thrilling and terrifying—intelligence meeting intelligence in an alien environment."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -4,
            connections = listOf(
                LocationConnection("coral_reef_garden", Direction.EAST),
                LocationConnection("hermit_crab_colony", Direction.UP),
                LocationConnection("underwater_cave_entrance", Direction.DOWN)
            ),
            encounterRate = 0.85,
            recommendedLevel = 13,
            lore = "Giant Pacific octopuses are the largest octopus species, reaching 15+ feet arm span and 100+ pounds. They're highly intelligent, using tools, solving puzzles, and recognizing individual humans. They live only 3-5 years, dying after reproducing once."
        ),
        
        Location(
            id = "underwater_passage",
            name = "Underwater Passage",
            description = LocationDescription.simple(
                "A submerged tunnel connects sea cave to open ocean. The passage is narrow and dark, requiring swimming through confined space while holding breath. Visibility is poor, navigation by feel more than sight. The passage is dangerous—getting stuck or lost means drowning. But it provides secret access to smuggler caves, making it valuable despite risks. Only the brave or desperate attempt the passage."
            ),
            biome = BiomeType.COASTAL,
            gridX = -3,
            gridY = -3,
            connections = listOf(
                LocationConnection("smugglers_cave", Direction.UP),
                LocationConnection("ritual_pool", Direction.UP),
                LocationConnection("hidden_cove", Direction.EAST),
                LocationConnection("underwater_cave_entrance", Direction.WEST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 14
        ),
        
        Location(
            id = "underwater_cave_entrance",
            name = "Underwater Cave Entrance",
            description = LocationDescription.simple(
                "The entrance to a submerged cave system, dark and foreboding. The cave descends into darkness, its extent unknown. Divers who explored it report passages extending deep into bedrock, some surfacing in air-filled chambers. The caves are dangerous—easy to get lost, running out of air far from exit. But the caves connect to deeper systems, possibly linking to forgotten catacombs and the deep dark below."
            ),
            biome = BiomeType.COASTAL,
            gridX = -4,
            gridY = -5,
            connections = listOf(
                LocationConnection("octopus_lair", Direction.UP),
                LocationConnection("underwater_passage", Direction.EAST),
                LocationConnection("deep_dark", Direction.DOWN)
            ),
            encounterRate = 0.95,
            recommendedLevel = 15
        )
    )
}

