package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * Forest region locations for JalmarQuest world expansion.
 * Contains ~85 forest locations across 6 sub-regions.
 * 
 * Sub-regions:
 * - 2A: Outer Forest Ring (15 locations, Levels 3-5)
 * - 2B: Deep Woods (20 locations, Levels 5-8)
 * - 2C: Western Wetland Woods (15 locations, Levels 6-9)
 * - 2D: Eastern Mountain Woods (15 locations, Levels 6-10)
 * - 2E: Enchanted Groves (10 locations, Levels 7-11)
 * - 2F: Thornwood Labyrinth (10 locations, Levels 8-12)
 */
internal val FOREST_LOCATIONS: List<Location> by lazy {
    listOf(
        // ========== SUB-REGION 2A: OUTER FOREST RING (15 locations) ==========
        
        Location(
            id = "fern_valley",
            name = "Fern Valley",
            description = LocationDescription.simple(
                "Ancient ferns carpet this shallow valley between the first trees. The fronds arch overhead like primitive parasols, their curled tips (fiddleheads, the humans call them) emerging in spring with alien beauty. The valley floor is soft with decomposing leaves, silent underfoot—perfect for stealthy movement. Filtered sunlight creates green-tinted illumination. Everything here feels prehistoric, as if dinosaurs might step through the ferns at any moment. You half expect them. Instead, you find beetles and the occasional salamander."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 3,
            connections = listOf(
                LocationConnection("elderwood", Direction.SOUTH),
                LocationConnection("maple_syrup_grove", Direction.WEST),
                LocationConnection("fallen_log_bridge", Direction.EAST),
                LocationConnection("wild_grape_tangle", Direction.SOUTH),
                LocationConnection("mushroom_glade", Direction.EAST),
                LocationConnection("birch_grove", Direction.NORTHEAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 3
        ),
        
        Location(
            id = "birch_grove",
            name = "Birch Grove",
            description = LocationDescription.simple(
                "White-barked birch trees rise like pale columns in a living cathedral. The bark peels in papery layers—you've investigated this thoroughly. Sunlight filters through leaves more easily here than in darker forest sections, creating dappled patterns on the forest floor. The birches stand slightly apart from each other, allowing navigation between them. Birds favor these trees for nesting; you hear constant chatter overhead. The grove feels lighter, airier, less threatening than deep forest. A good transition zone from grassland to woodland."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 3,
            connections = listOf(
                LocationConnection("elderwood", Direction.SOUTH),
                LocationConnection("songbird_nesting_area", Direction.SOUTHWEST),
                LocationConnection("fern_valley", Direction.SOUTHWEST),
                LocationConnection("squirrel_highway", Direction.EAST),
                LocationConnection("oak_council_circle", Direction.NORTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 3
        ),
        
        Location(
            id = "squirrel_highway",
            name = "Squirrel Highway",
            description = LocationDescription.simple(
                "The squirrels have established aerial routes through interconnected tree branches—highways they traverse with acrobatic precision. You watch from below, envious of their three-dimensional travel capabilities. Dropped acorns rain down regularly (sometimes deliberately aimed, you suspect). The squirrels chatter warnings when predators approach, a service you appreciate despite their territorial attitudes toward you. The highway represents infrastructure beyond your ability but within your awareness—a reminder that many creatures share this forest."
            ),
            biome = BiomeType.FOREST,
            gridX = 3,
            gridY = 3,
            connections = listOf(
                LocationConnection("birch_grove", Direction.WEST),
                LocationConnection("hunters_lodge", Direction.NORTH),
                LocationConnection("acorn_harvest_ground", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 4
        ),
        
        Location(
            id = "acorn_harvest_ground",
            name = "Acorn Harvest Ground",
            description = LocationDescription.simple(
                "Beneath massive oak trees, acorns litter the ground in autumn abundance. This is prime foraging territory—squirrels, jays, mice, deer, and you all compete for the bounty. Acorns are too large for you to eat directly (engineering problem), but the insects attracted to rotting nuts are perfect. The oaks tower overhead, ancient and indifferent to the scramble beneath them. Their roots break through the surface like wooden serpents. During mast years, the harvest is overwhelming; lean years bring competition that turns aggressive."
            ),
            biome = BiomeType.FOREST,
            gridX = 4,
            gridY = 3,
            connections = listOf(
                LocationConnection("squirrel_highway", Direction.WEST),
                LocationConnection("chipmunk_cache", Direction.SOUTHEAST),
                LocationConnection("oak_council_circle", Direction.NORTHWEST),
                LocationConnection("pine_needle_carpet", Direction.EAST),
                LocationConnection("oak_council_circle", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 4
        ),
        
        Location(
            id = "oak_council_circle",
            name = "Oak Council Circle",
            description = LocationDescription.simple(
                "Five ancient oak trees stand in a rough circle, their branches intertwining overhead to create a natural dome. The space feels intentional, significant, though likely it's just random growth patterns (or is it?). The ground is bare of undergrowth, carpeted only in leaves and acorns. You've witnessed actual councils here—owls gathering at night, crows holding loud discussions at dawn. The oaks witness everything with silent authority. You feel observed when standing in the circle's center."
            ),
            biome = BiomeType.FOREST,
            gridX = 3,
            gridY = 4,
            connections = listOf(
                LocationConnection("birch_grove", Direction.SOUTH),
                LocationConnection("dogwood_understory", Direction.NORTH),
                LocationConnection("acorn_harvest_ground", Direction.SOUTH),
                LocationConnection("sassafras_grove", Direction.NORTH),
                LocationConnection("pine_transition_zone", Direction.NORTHEAST),
                LocationConnection("beech_tree_grove", Direction.WEST),
                LocationConnection("acorn_harvest_ground", Direction.SOUTHEAST),
                LocationConnection("moss_carpet_clearing", Direction.NORTH)
            ),
            encounterRate = 0.4,
            recommendedLevel = 5
        ),
        
        Location(
            id = "fallen_log_bridge",
            name = "Fallen Log Bridge",
            description = LocationDescription.simple(
                "A massive tree fell years ago, creating a bridge across a shallow creek. The log is thick enough to walk across safely (for you), though weathering has made it slippery with moss and rot. Mushrooms grow from the bark in tiered formations. Beetles bore through the dead wood, their tunnels creating intricate networks. The creek below babbles pleasantly—you've learned to cross the log rather than wade through water. The bridge represents nature's infrastructure, temporary but functional."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 3,
            connections = listOf(
                LocationConnection("elderwood", Direction.SOUTHWEST),
                LocationConnection("songbird_nesting_area", Direction.EAST),
                LocationConnection("hollow_tree_apartment", Direction.NORTH),
                LocationConnection("beech_tree_grove", Direction.EAST),
                LocationConnection("fern_valley", Direction.WEST),
                LocationConnection("creek_pebble_beach", Direction.NORTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 3
        ),
        
        Location(
            id = "creek_pebble_beach",
            name = "Creek Pebble Beach",
            description = LocationDescription.simple(
                "Where the forest creek widens, a small beach of smooth pebbles has formed. The stones are water-worn, perfectly round, pleasant under your feet. The creek here is shallow enough to wade across, and you do so regularly to access both banks. Crayfish hide under stones—you've learned to flip them for hunting (limited success). The beach is a rest stop, a crossing point, and a navigation landmark. Water striders skate on the surface, defying physics with casual grace."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 4,
            connections = listOf(
                LocationConnection("fallen_log_bridge", Direction.SOUTH),
                LocationConnection("frog_chorus_pool", Direction.NORTH),
                LocationConnection("hollow_tree_apartment", Direction.WEST),
                LocationConnection("moss_carpet_clearing", Direction.EAST),
                LocationConnection("woodland_spring", Direction.NORTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 4
        ),
        
        Location(
            id = "woodland_spring",
            name = "Woodland Spring",
            description = LocationDescription.simple(
                "Clear water bubbles from between rocks, forming the creek's source. The spring never runs dry, never freezes completely, maintaining constant flow and temperature. Moss grows thick around it in emerald cushions. The water is cold, clean, perfect for drinking. Animals from across the forest visit—deer, raccoons, foxes, all maintaining uneasy truces at the water's edge. You drink here regularly, aware of the spring's importance as neutral ground where forest law supersedes individual conflicts."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 5,
            connections = listOf(
                LocationConnection("creek_pebble_beach", Direction.SOUTH),
                LocationConnection("bracket_fungus_forest", Direction.NORTH),
                LocationConnection("frog_chorus_pool", Direction.SOUTH),
                LocationConnection("hollow_tree_apartment", Direction.SOUTH),
                LocationConnection("deer_trail_network", Direction.NORTH),
                LocationConnection("old_stone_wall", Direction.NORTH),
                LocationConnection("ancient_tree_heart", Direction.NORTH),
                LocationConnection("moss_carpet_clearing", Direction.EAST)
            ),
            isSafeZone = true,
            encounterRate = 0.3,
            recommendedLevel = 5,
            lore = "The Woodland Spring has flowed for centuries, predating all current residents. Local legend says it's blessed by forest spirits. The water does taste remarkably good, which is evidence enough."
        ),
        
        Location(
            id = "maple_syrup_grove",
            name = "Maple Syrup Grove",
            description = LocationDescription.simple(
                "Sugar maple trees dominate this grove, their bark scarred where humans once tapped them for sap. Those operations ceased years ago, but the trees remember—their scars healed into distinctive patterns. In autumn, the maples explode with color: orange, red, yellow leaves creating a canopy of fire. The grove floor becomes carpeted in these leaves, crunching satisfyingly underfoot. Maple seeds (helicopters!) spin down in spring, and you've watched them with fascination, wishing for similar flight capabilities."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 3,
            connections = listOf(
                LocationConnection("fern_valley", Direction.EAST),
                LocationConnection("elderwood", Direction.SOUTH),
                LocationConnection("hickory_nut_field", Direction.WEST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 3
        ),
        
        Location(
            id = "hickory_nut_field",
            name = "Hickory Nut Field",
            description = LocationDescription.simple(
                "Hickory trees drop nuts with shells so hard they seem engineered for warfare. The nuts attract squirrels with sophisticated cracking techniques—you lack both the jaw strength and the knowledge. But the field provides other resources: insects attracted to rotting husks, and the hickory wood itself hosts various beetles. The trees have distinctive shaggy bark, peeling in long strips. Walking here during nut-fall season is mildly dangerous—getting hit by a falling hickory nut is memorable (you remember)."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 2,
            connections = listOf(
                LocationConnection("maple_syrup_grove", Direction.EAST),
                LocationConnection("multiflora_rose_wall", Direction.NORTH),
                LocationConnection("greenbrier_tangle", Direction.NORTHWEST),
                LocationConnection("mushroom_glade", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 4
        ),
        
        Location(
            id = "beech_tree_grove",
            name = "Beech Tree Grove",
            description = LocationDescription.simple(
                "Beech trees with smooth gray bark create a grove of living columns. The bark is unmarred except where humans carved initials decades ago—the trees grew around these wounds, preserving them. Beech nuts are small but numerous, favored by many forest creatures. The fallen leaves are tough, leathery, slow to decompose. The grove has an open understory—beech trees shade so effectively that little grows beneath them. This makes navigation easy and visibility excellent."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 4,
            connections = listOf(
                LocationConnection("oak_council_circle", Direction.EAST),
                LocationConnection("dogwood_understory", Direction.EAST),
                LocationConnection("moss_carpet_clearing", Direction.NORTH),
                LocationConnection("fallen_log_bridge", Direction.WEST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 4
        ),
        
        Location(
            id = "wild_grape_tangle",
            name = "Wild Grape Tangle",
            description = LocationDescription.simple(
                "Wild grapevines have climbed trees and tangled between them, creating a three-dimensional maze. The vines are thick as rope, strong enough to support weight (not yours—you tried). Purple grapes ripen in late summer, attracting birds and mammals in feeding frenzies. The tangle is difficult to navigate, requiring careful route-finding through natural gaps. Thorns hide among the vines, waiting to catch unwary travelers. But the grape leaves provide excellent cover, and the fruit-drunk wasps ignore you completely."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("fern_valley", Direction.NORTH),
                LocationConnection("thistle_throne", Direction.SOUTH),
                LocationConnection("heron_shallows", Direction.NORTHWEST),
                LocationConnection("briarblade_gauntlet", Direction.NORTHWEST),
                LocationConnection("mushroom_glade", Direction.EAST),
                LocationConnection("willow_weep", Direction.WEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 5
        ),
        
        Location(
            id = "songbird_nesting_area",
            name = "Songbird Nesting Area",
            description = LocationDescription.simple(
                "Dense shrubs and small trees create ideal nesting habitat for songbirds. The area erupts with dawn chorus—dozens of species singing territorial claims and mating calls. You navigate carefully, aware that disturbing nests brings aggressive parent attacks (learned this early). The birds tolerate your presence as long as you maintain respectful distance. The nesting area provides indirect benefits: dropped seeds, insect populations stirred up by foraging birds, and advance warning of predators from bird alarm calls."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 3,
            connections = listOf(
                LocationConnection("elderwood", Direction.NORTH),
                LocationConnection("birch_grove", Direction.NORTHEAST),
                LocationConnection("fallen_log_bridge", Direction.WEST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 3
        ),
        
        Location(
            id = "dogwood_understory",
            name = "Dogwood Understory",
            description = LocationDescription.simple(
                "Flowering dogwood trees form an understory layer beneath taller oaks and maples. Their white blooms (actually bracts, the humans explain) create a second canopy in spring. The dogwoods bear red berries in fall that birds devour eagerly. The understory creates layered habitat—different species occupy different vertical zones. You operate at ground level but benefit from the complexity above. The dogwoods' horizontal branching pattern creates natural perches and platforms throughout the mid-story."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 4,
            connections = listOf(
                LocationConnection("oak_council_circle", Direction.SOUTH),
                LocationConnection("sassafras_grove", Direction.NORTHEAST),
                LocationConnection("moss_carpet_clearing", Direction.NORTHEAST),
                LocationConnection("beech_tree_grove", Direction.WEST),
                LocationConnection("moss_carpet_clearing", Direction.NORTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 4
        ),
        
        Location(
            id = "hollow_tree_apartment",
            name = "Hollow Tree Apartment",
            description = LocationDescription.simple(
                "A massive hollow tree hosts multiple residents in different cavities: raccoons in the upper floors, opossums mid-level, mice in the basement. You've negotiated passage rights through the ground floor (the basement tenants are reasonable). The tree is still alive despite its hollowness, pumping sap through its remaining walls. The apartment complex operates under unspoken rules—everyone minds their business, shares predator warnings, and tolerates occasional resource disputes. It's civilization in miniature."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 4,
            connections = listOf(
                LocationConnection("fallen_log_bridge", Direction.SOUTH),
                LocationConnection("creek_pebble_beach", Direction.EAST),
                LocationConnection("woodland_spring", Direction.NORTH)
            ),
            isSettlement = true,
            encounterRate = 0.4,
            recommendedLevel = 5
        ),
        
        // ========== SUB-REGION 2B: DEEP WOODS (20 locations) ==========
        
        Location(
            id = "moss_carpet_clearing",
            name = "Moss Carpet Clearing",
            description = LocationDescription.simple(
                "Thick moss covers the ground in emerald softness, creating the forest's finest flooring. The clearing is small, surrounded by old-growth trees whose canopy blocks most sunlight—ideal conditions for moss. Walking here is silent, cushioned, pleasant underfoot. The moss retains moisture, hosting salamanders and slugs beneath. You rest here often, the moss providing comfortable bedding. The clearing feels sacred in its quietness, a meditation space in the forest's chaos."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 6,
            connections = listOf(
                LocationConnection("oak_council_circle", Direction.SOUTH),
                LocationConnection("dogwood_understory", Direction.SOUTH),
                LocationConnection("sassafras_grove", Direction.SOUTH),
                LocationConnection("pine_transition_zone", Direction.EAST),
                LocationConnection("owl_territory", Direction.NORTHWEST),
                LocationConnection("spider_web_canyon", Direction.NORTH),
                LocationConnection("beech_tree_grove", Direction.SOUTH),
                LocationConnection("deer_trail_network", Direction.WEST),
                LocationConnection("striped_maple_glade", Direction.SOUTH),
                LocationConnection("rhododendron_tunnel", Direction.EAST),
                LocationConnection("old_stone_wall", Direction.WEST),
                LocationConnection("creek_pebble_beach", Direction.WEST),
                LocationConnection("dogwood_understory", Direction.SOUTHWEST),
                LocationConnection("woodland_spring", Direction.WEST),
                LocationConnection("woodpecker_grove", Direction.NORTH)
            ),
            encounterRate = 0.35,
            recommendedLevel = 6
        ),
        
        Location(
            id = "woodpecker_grove",
            name = "Woodpecker Grove",
            description = LocationDescription.simple(
                "The constant hammering of woodpeckers echoes through this grove of dead and dying trees. The birds drill for insects, excavate nest cavities, and drum territorial messages. The grove is full of holes—every tree swiss-cheesed with woodpecker excavations. Old nest holes house other species: chickadees, flying squirrels, bees. The dead trees provide abundant insects but limited cover. You move carefully, aware that all this hammering attracts predators looking for distracted woodpeckers."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 6,
            connections = listOf(
                LocationConnection("moss_carpet_clearing", Direction.SOUTH),
                LocationConnection("bracket_fungus_forest", Direction.SOUTH),
                LocationConnection("ancient_tree_heart", Direction.EAST),
                LocationConnection("owl_territory", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 6
        ),
        
        Location(
            id = "owl_territory",
            name = "Owl Territory",
            description = LocationDescription.simple(
                "Great horned owls nest in the deep woods, hunting at night with silent efficiency. Their territory is marked by pellets—regurgitated fur and bone bundles scattered beneath roosting trees. You examine these forensically, learning what the owls hunt (mostly mice and voles, occasionally rabbits). The owls ignore you (too small to be worth the effort), but their presence keeps other predators cautious. Daylight navigation is safe; you avoid nocturnal visits. The hooting at night carries authority that demands respect."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 6,
            connections = listOf(
                LocationConnection("woodpecker_grove", Direction.SOUTH),
                LocationConnection("wild_turkey_dust_bath", Direction.NORTH),
                LocationConnection("spider_web_canyon", Direction.SOUTH),
                LocationConnection("moss_carpet_clearing", Direction.SOUTHEAST),
                LocationConnection("fairy_ring", Direction.NORTH)
            ),
            encounterRate = 0.7,
            recommendedLevel = 7
        ),
        
        Location(
            id = "fairy_ring",
            name = "Fairy Ring",
            description = LocationDescription.simple(
                "Mushrooms grow in a perfect circle in a deep woods clearing—a fairy ring, the humans call it. The ring is large, perhaps eight feet in diameter, composed of honey mushrooms connected underground by vast mycelial networks. The grass inside the ring grows lusher, greener than surrounding areas. You feel strange standing in the ring's center—observed, significant, part of something larger than yourself. The mushrooms appear and disappear with the seasons, but the ring persists year after year in the exact same location."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("owl_territory", Direction.SOUTH),
                LocationConnection("raccoon_latrine", Direction.EAST),
                LocationConnection("witch_hazel_thicket", Direction.WEST),
                LocationConnection("ancient_tree_heart", Direction.SOUTHWEST),
                LocationConnection("glowshroom_cathedral", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7,
            lore = "Fairy rings are produced by fungi growing outward from a central point, sometimes for decades or centuries. This ring is approximately 50 years old based on growth rate estimates. The fairies have not been available for comment."
        ),
        
        Location(
            id = "deer_trail_network",
            name = "Deer Trail Network",
            description = LocationDescription.simple(
                "Deer have worn paths through the deep woods, creating a network of trails connecting feeding areas to bedding sites. The trails are obvious—packed earth, clear of obstacles, maintained by constant traffic. You use these trails regularly, appreciating the deer's engineering. The trails represent shared infrastructure, used by many species. Following them ensures you won't get lost in the dense woods. Occasionally you encounter actual deer on their own trails—tense moments of mutual assessment before they bound away."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 6,
            connections = listOf(
                LocationConnection("woodland_spring", Direction.SOUTH),
                LocationConnection("bracket_fungus_forest", Direction.WEST),
                LocationConnection("frog_chorus_pool", Direction.SOUTHWEST),
                LocationConnection("lightning_scar_clearing", Direction.NORTH),
                LocationConnection("old_stone_wall", Direction.EAST),
                LocationConnection("moss_carpet_clearing", Direction.EAST),
                LocationConnection("ancient_tree_heart", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),
        
        Location(
            id = "bracket_fungus_forest",
            name = "Bracket Fungus Forest",
            description = LocationDescription.simple("Dead trees throughout this section host massive bracket fungi—shelf-like growths projecting from trunks like woody parasols. The fungi decompose the wood slowly, converting dead trees back to soil nutrients. The brackets provide platforms (too high for you), shelter for insects, and fascinating architecture. You've learned to identify trees nearing collapse by their fungal loads—useful survival knowledge. The forest here smells of decay and renewal, the endless cycle of death feeding new life."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 6,
            connections = listOf(
                LocationConnection("deer_trail_network", Direction.EAST),
                LocationConnection("poison_ivy_warning_zone", Direction.NORTHEAST),
                LocationConnection("woodland_spring", Direction.SOUTH),
                LocationConnection("woodpecker_grove", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 6
        ),
        
        Location(
            id = "spider_web_canyon",
            name = "Spider Web Canyon",
            description = LocationDescription.simple(
                "Between two dense thickets, spiders have strung webs in layered networks—a canyon of silk that catches morning dew in spectacular displays. The webs are unavoidable; you push through carefully, collecting strands on your feathers (annoying). The spiders are large orb weavers, rebuilding their webs each night with patient determination. The canyon is beautiful but high-maintenance to navigate. You've learned optimal times (mid-morning after dew evaporates but before afternoon web-building) for passage."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 7,
            connections = listOf(
                LocationConnection("moss_carpet_clearing", Direction.SOUTH),
                LocationConnection("chipmunk_burrow_city", Direction.EAST),
                LocationConnection("honeysuckle_invasion", Direction.WEST),
                LocationConnection("wild_turkey_dust_bath", Direction.NORTHEAST),
                LocationConnection("raccoon_latrine", Direction.NORTH),
                LocationConnection("owl_territory", Direction.NORTH),
                LocationConnection("ancient_tree_heart", Direction.WEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7
        ),
        
        Location(
            id = "lightning_scar_clearing",
            name = "Lightning Scar Clearing",
            description = LocationDescription.simple(
                "A massive oak was struck by lightning years ago, splitting the trunk and creating a clearing where it fell. The dead oak slowly decomposes, hosting beetles, carpenter ants, fungi, and opportunistic plants. The clearing provides rare sunlight in deep forest, encouraging different vegetation: ferns, wildflowers, berry bushes. The lightning scar is visible as charred wood on the remaining stump. The clearing represents catastrophic change becoming opportunity—destruction feeding renewal."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 7,
            connections = listOf(
                LocationConnection("deer_trail_network", Direction.SOUTH),
                LocationConnection("honeysuckle_invasion", Direction.EAST),
                LocationConnection("poison_ivy_warning_zone", Direction.NORTH),
                LocationConnection("ancient_tree_heart", Direction.EAST),
                LocationConnection("witch_hazel_thicket", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        ),
        
        Location(
            id = "wild_turkey_dust_bath",
            name = "Wild Turkey Dust Bath",
            description = LocationDescription.simple(
                "Wild turkeys maintain a dust-bathing area in a patch of dry, loose soil. The depressions are turkey-sized (enormous to you), filled with fine dust that birds use to control parasites. You've observed the dust-bathing ritual: vigorous flapping, dust clouds, satisfied preening afterward. You use the same dust (excellent quality), careful to avoid encounters with actual turkeys (they're large and easily startled). The area represents shared resources across species—mutual tolerance based on mutual benefit."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 7,
            connections = listOf(
                LocationConnection("owl_territory", Direction.SOUTH),
                LocationConnection("chipmunk_burrow_city", Direction.NORTH),
                LocationConnection("spider_web_canyon", Direction.SOUTHWEST),
                LocationConnection("silverleaf_canopy", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "witch_hazel_thicket",
            name = "Witch Hazel Thicket",
            description = LocationDescription.simple(
                "Witch hazel shrubs form a dense thicket in the deep woods. Their unique trait: blooming in late autumn when most plants are dormant, yellow ribbon-like flowers appearing on bare branches. The flowers smell faintly sweet, attracting late-season pollinators. The shrubs' branches zigzag at odd angles, creating a tangled obstacle course. Witch hazel seeds explode from capsules when ripe—you've been startled by this multiple times. The thicket feels slightly magical, blooming when nothing else dares."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 8,
            connections = listOf(
                LocationConnection("lightning_scar_clearing", Direction.SOUTH),
                LocationConnection("mirror_pool", Direction.NORTHWEST),
                LocationConnection("skunk_cabbage_bog", Direction.SOUTH),
                LocationConnection("poison_ivy_warning_zone", Direction.SOUTH),
                LocationConnection("dreamweaver_glade", Direction.NORTH),
                LocationConnection("fairy_ring", Direction.EAST),
                LocationConnection("ancient_tree_heart", Direction.SOUTHEAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 8
        ),
        
        Location(
            id = "raccoon_latrine",
            name = "Raccoon Latrine",
            description = LocationDescription.simple(
                "Raccoons designate specific areas as communal latrines—this fallen log is one such site. The evidence is unmistakable and avoided by all sensible creatures (parasites, disease risk). But the latrine serves as information hub: you can tell how many raccoons use it, what they're eating, their health status. The raccoons maintain complex social structures; the latrine is part of their communication network. You observe from safe distance, gathering intelligence while respecting the raccoons' boundaries."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 8,
            connections = listOf(
                LocationConnection("spider_web_canyon", Direction.SOUTH),
                LocationConnection("barred_owl_roost", Direction.EAST),
                LocationConnection("fairy_ring", Direction.WEST),
                LocationConnection("silverleaf_canopy", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "pine_transition_zone",
            name = "Pine Transition Zone",
            description = LocationDescription.simple(
                "The hardwood forest transitions to pine here, the two ecosystems meeting in a mixed zone. White pines tower above oaks and maples, their needles creating acidic soil that suppresses undergrowth. The pine needle carpet is thick, soft, fragrant. The transition hosts species from both ecosystems: pine-dependent birds alongside hardwood specialists. You appreciate the diversity while noting navigation differences—pine groves require different tactics than deciduous woods."
            ),
            biome = BiomeType.FOREST,
            gridX = 3,
            gridY = 6,
            connections = listOf(
                LocationConnection("oak_council_circle", Direction.SOUTHWEST),
                LocationConnection("boulder_moss_grove", Direction.EAST),
                LocationConnection("pine_needle_carpet", Direction.SOUTHEAST),
                LocationConnection("moss_carpet_clearing", Direction.WEST),
                LocationConnection("pine_needle_carpet", Direction.EAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),
        
        Location(
            id = "poison_ivy_warning_zone",
            name = "Poison Ivy Warning Zone",
            description = LocationDescription.simple(
                "Poison ivy grows with aggressive enthusiasm in this section, vining up trees and carpeting the ground. The three-leafed pattern is distinctive; you've learned to identify and avoid it (you're not personally affected, but humans are very concerned about it). The zone requires careful navigation through poison-ivy-free corridors. Deer eat the leaves with impunity. Birds eat the berries. The ivy provides valuable wildlife habitat despite its reputation. You respect its success even while avoiding direct contact."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 7,
            connections = listOf(
                LocationConnection("lightning_scar_clearing", Direction.SOUTH),
                LocationConnection("skunk_cabbage_bog", Direction.NORTH),
                LocationConnection("cinnamon_fern_grove", Direction.WEST),
                LocationConnection("bracket_fungus_forest", Direction.SOUTHWEST),
                LocationConnection("witch_hazel_thicket", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "honeysuckle_invasion",
            name = "Honeysuckle Invasion",
            description = LocationDescription.simple(
                "Invasive honeysuckle has colonized this forest section, forming dense tangles beneath the canopy. The shrubs smell sweetly fragrant when flowering, attracting pollinators with nectar rewards. But the honeysuckle outcompetes native plants, creating monocultures that reduce diversity. You navigate the tangles pragmatically—they provide excellent cover and produce abundant berries (birds love them). The invasion represents ecological change you witness but can't influence, adaptation being your only option."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("ancient_tree_heart", Direction.NORTH),
                LocationConnection("spider_web_canyon", Direction.EAST),
                LocationConnection("lightning_scar_clearing", Direction.WEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7
        ),
        
        Location(
            id = "chipmunk_burrow_city",
            name = "Chipmunk Burrow City",
            description = LocationDescription.simple(
                "Chipmunks have excavated extensive burrow systems beneath rocky outcrops in the deep woods. Entrances dot the area, each hole leading to underground chambers for food storage, nesting, and hibernation. The chipmunks are vocal, territorial, and numerous. You've established neutral relations—mutual predator warnings benefit both species. The city represents sophisticated underground architecture you can only imagine. Surface observations reveal complex social dynamics, property disputes, and food-hoarding competition."
            ),
            biome = BiomeType.FOREST,
            gridX = 3,
            gridY = 7,
            connections = listOf(
                LocationConnection("wild_turkey_dust_bath", Direction.SOUTH),
                LocationConnection("barred_owl_roost", Direction.NORTH),
                LocationConnection("spider_web_canyon", Direction.WEST),
                LocationConnection("silverleaf_canopy", Direction.NORTH)
            ),
            isSettlement = true,
            encounterRate = 0.5,
            recommendedLevel = 7
        ),
        
        Location(
            id = "skunk_cabbage_bog",
            name = "Skunk Cabbage Bog",
            description = LocationDescription.simple(
                "In a wet depression in the deep woods, skunk cabbage thrives—bizarre plants that bloom in late winter, melting snow with metabolic heat. The plants smell exactly as their name suggests when disturbed (you've learned not to disturb them). The bog is muddy, treacherous, rich with specialized wildlife. Salamanders breed in vernal pools here. The cabbage leaves grow massive by summer, creating a prehistoric-looking landscape. You traverse the bog edges, avoiding the wettest sections."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 7,
            connections = listOf(
                LocationConnection("poison_ivy_warning_zone", Direction.SOUTH),
                LocationConnection("alder_thicket", Direction.SOUTH),
                LocationConnection("jewelweed_jungle", Direction.NORTHWEST),
                LocationConnection("marsh_marigold_meadow", Direction.WEST),
                LocationConnection("cinnamon_fern_grove", Direction.EAST),
                LocationConnection("witch_hazel_thicket", Direction.NORTH),
                LocationConnection("willow_weep", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "frog_chorus_pool",
            name = "Frog Chorus Pool",
            description = LocationDescription.simple(
                "A vernal pool—seasonal wetland that fills with spring snowmelt and rain, then dries by summer—hosts explosive frog breeding events. The chorus in spring is deafening: hundreds of frogs calling simultaneously in mating frenzy. Eggs appear as jelly masses, then tadpoles swarm the shallows. By summer, the pool dries completely, preventing fish from establishing and eating frog young. You visit regularly, hunting insects attracted to the pool. The frogs tolerate your presence as long as you don't hunt them directly (you don't—ethical choice)."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 5,
            connections = listOf(
                LocationConnection("woodland_spring", Direction.NORTH),
                LocationConnection("cypress_knees", Direction.SOUTHWEST),
                LocationConnection("deer_trail_network", Direction.NORTHEAST),
                LocationConnection("creek_pebble_beach", Direction.SOUTH)
            ),
            encounterRate = 0.45,
            recommendedLevel = 5
        ),
        
        Location(
            id = "sassafras_grove",
            name = "Sassafras Grove",
            description = LocationDescription.simple(
                "Sassafras trees with their distinctive mitten-shaped leaves grow in a sunlit grove. The trees smell spicy when bark is broken—you've investigated this thoroughly. Sassafras produces blue berries on red stems that birds adore. The grove has good visibility and numerous escape routes, making it tactically favorable. Deer browse the saplings, keeping the grove open. You've claimed this as secondary territory, visiting regularly to maintain presence and gather dropped berries."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 5,
            connections = listOf(
                LocationConnection("moss_carpet_clearing", Direction.NORTH),
                LocationConnection("oak_council_circle", Direction.SOUTH),
                LocationConnection("dogwood_understory", Direction.SOUTHWEST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 5
        ),
        
        Location(
            id = "old_stone_wall",
            name = "Old Stone Wall Ruins",
            description = LocationDescription.simple(
                "A moss-covered stone wall runs through the deep woods—property boundary from when this land was farmed a century ago. The wall is collapsed in sections, stones scattered, but the original line is traceable. Chipmunks nest in wall gaps, snakes sun on the stones, and lichens slowly dissolve the rock. The wall represents human history reclaimed by forest, order yielding to entropy. You use it as a navigation landmark and shelter—the stones provide sun-warmed basking spots and windbreaks."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 6,
            connections = listOf(
                LocationConnection("moss_carpet_clearing", Direction.EAST),
                LocationConnection("woodland_spring", Direction.SOUTH),
                LocationConnection("deer_trail_network", Direction.WEST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),
        
        Location(
            id = "barred_owl_roost",
            name = "Barred Owl Roost",
            description = LocationDescription.simple(
                "A pair of barred owls maintains a daytime roost in dense conifers, their 'who-cooks-for-you' calls echoing at dusk. The roost is identifiable by pellets and whitewash beneath the roost tree. The owls are tolerant of observation from respectful distance—you've watched them preen, nap, and regurgitate pellets. Their presence indicates healthy forest ecosystem (owls need abundant prey). You appreciate them as fellow hunters, operating different shifts in the eternal cycle of predation."
            ),
            biome = BiomeType.FOREST,
            gridX = 3,
            gridY = 8,
            connections = listOf(
                LocationConnection("chipmunk_burrow_city", Direction.SOUTH),
                LocationConnection("raccoon_latrine", Direction.WEST),
                LocationConnection("silverleaf_canopy", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 8
        ),
        
        // ========== SUB-REGION 2C: WESTERN WETLAND WOODS (15 locations) ==========
        
        Location(
            id = "willow_weep",
            name = "Willow Weep",
            description = LocationDescription.simple(
                "Weeping willow branches hang to the ground near a marshy area, creating curtained chambers. The willow grove marks the forest's transition to swamp—soil perpetually damp, standing water common. The hanging branches provide excellent cover but limited visibility. You part them carefully, aware of what might hide in the enclosed spaces. The willows grow fast, their roots stabilizing wet soil. The grove feels melancholy, beautiful in a water-logged way, constantly dripping even without rain."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("wild_grape_tangle", Direction.EAST),
                LocationConnection("buttonbush_swamp", Direction.WEST),
                LocationConnection("skunk_cabbage_bog", Direction.EAST),
                LocationConnection("devil_walking_stick", Direction.NORTH),
                LocationConnection("cardinal_flower_creek", Direction.SOUTH),
                LocationConnection("mushroom_glade", Direction.NORTHEAST),
                LocationConnection("cypress_knees", Direction.WEST),
                LocationConnection("fogbank_hollow", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 6
        ),

        Location(
            id = "cypress_knees",
            name = "Cypress Knees",
            description = LocationDescription.simple(
                "Bald cypress trees send up woody projections from their roots—cypress knees that protrude from swampy ground like wooden stalagmites. The knees provide oxygen to submerged roots and create a obstacle course for navigation. Water pools between them, hosting mosquito larvae and aquatic insects. The cypress trees tower overhead, their feathery foliage creating dappled shade. The knees make this area distinctive, alien, unlike typical forest. You hop between them, using the knees as platforms above standing water."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 5,
            connections = listOf(
                LocationConnection("willow_weep", Direction.EAST),
                LocationConnection("fogbank_hollow", Direction.NORTHWEST),
                LocationConnection("alder_thicket", Direction.NORTH),
                LocationConnection("cardinal_flower_creek", Direction.SOUTHWEST),
                LocationConnection("frog_chorus_pool", Direction.NORTHEAST),
                LocationConnection("thorn_brake", Direction.NORTH),
                LocationConnection("heron_shallows", Direction.WEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 7
        ),
        
        Location(
            id = "heron_shallows",
            name = "Heron Shallows",
            description = LocationDescription.simple(
                "Shallow water spreads through the forest floor where springs bubble up, creating wetland within woodland. A great blue heron hunts here regularly, standing motionless for minutes before striking at fish and frogs with spear-like precision. You observe the heron with professional respect—its patience and strike accuracy are remarkable. The shallows host rich aquatic life: fish, frogs, crayfish, aquatic insects. Navigation requires careful stepping to avoid deep spots. The heron tolerates your presence (you're not competition)."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 3,
            connections = listOf(
                LocationConnection("cypress_knees", Direction.EAST),
                LocationConnection("buttonbush_swamp", Direction.SOUTH),
                LocationConnection("poison_ivy_curtain", Direction.NORTH),
                LocationConnection("cardinal_flower_creek", Direction.EAST),
                LocationConnection("cattail_marsh", Direction.NORTH),
                LocationConnection("wild_grape_tangle", Direction.SOUTHEAST),
                LocationConnection("mire_maw", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 7
        ),
        
        Location(
            id = "fogbank_hollow",
            name = "Fogbank Hollow",
            description = LocationDescription.simple(
                "A depression in the wetland woods where cold air settles and fog forms with reliable frequency. Morning visits reveal a white world—fog so thick visibility drops to body-lengths. The fog muffles sound, disorients, creates eerie isolation. You navigate by memory and feel, sensing rather than seeing the landscape. The hollow's moisture supports lush moss and ferns. By midday the fog burns off, revealing ordinary woods. But the morning transformations are magical, turning familiar territory strange."
            ),
            biome = BiomeType.FOREST,
            gridX = -5,
            gridY = 5,
            connections = listOf(
                LocationConnection("willow_weep", Direction.SOUTH),
                LocationConnection("alder_thicket", Direction.EAST),
                LocationConnection("beaver_pond_edge", Direction.SOUTH),
                LocationConnection("marsh_marigold_meadow", Direction.NORTHEAST),
                LocationConnection("sphagnum_bog", Direction.WEST),
                LocationConnection("cypress_knees", Direction.SOUTHEAST),
                LocationConnection("cattail_marsh", Direction.WEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "cattail_marsh",
            name = "Cattail Marsh",
            description = LocationDescription.simple(
                "Where forest meets true wetland, cattails grow in dense stands—brown cylinder seed heads topping tall stalks. The marsh is too wet for trees but not open water, creating transitional habitat. Red-winged blackbirds nest in the cattails, their calls carrying across the marsh. Muskrats build lodges from cattail stalks. You navigate the marsh edges, avoiding the deepest water. The cattails serve multiple purposes: navigation obstacles, habitat boundaries, and source of fluffy seed material that coats everything in late summer."
            ),
            biome = BiomeType.FOREST,
            gridX = -6,
            gridY = 4,
            connections = listOf(
                LocationConnection("fogbank_hollow", Direction.EAST),
                LocationConnection("beaver_pond_edge", Direction.EAST),
                LocationConnection("sphagnum_bog", Direction.NORTH),
                LocationConnection("heron_shallows", Direction.SOUTH),
                LocationConnection("mire_maw", Direction.SOUTHWEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "buttonbush_swamp",
            name = "Buttonbush Swamp",
            description = LocationDescription.simple(
                "Buttonbush shrubs grow in standing water, their spherical white flower heads attracting butterflies and moths. The swamp is navigable only by jumping between exposed roots and partially submerged stumps—you've perfected this parkour. The water is dark, tannin-stained, hiding depth. Frogs chorus from the buttonbush branches. The swamp represents the forest's wettest extreme, where only specialized plants survive. You visit for insects and adventure, leaving before darkness when predators become active."
            ),
            biome = BiomeType.FOREST,
            gridX = -5,
            gridY = 3,
            connections = listOf(
                LocationConnection("heron_shallows", Direction.NORTH),
                LocationConnection("beaver_pond_edge", Direction.NORTH),
                LocationConnection("willow_weep", Direction.EAST),
                LocationConnection("mire_maw", Direction.WEST)
            ),
            encounterRate = 0.8,
            recommendedLevel = 9
        ),
        
        Location(
            id = "alder_thicket",
            name = "Alder Thicket",
            description = LocationDescription.simple(
                "Alder shrubs form impenetrable thickets in the wet woods, their nitrogen-fixing root nodules enriching waterlogged soil. The thicket is too dense to penetrate directly—you navigate around its edges or through discovered gaps. Alder catkins hang in spring, releasing clouds of pollen. The shrubs host specialized insects found nowhere else. The thicket represents botanical success in difficult conditions, adapting to perpetual wet feet and low oxygen. You respect its tenacity while cursing its obstruction of preferred routes."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 5,
            connections = listOf(
                LocationConnection("cypress_knees", Direction.SOUTH),
                LocationConnection("marsh_marigold_meadow", Direction.NORTH),
                LocationConnection("cinnamon_fern_grove", Direction.NORTHEAST),
                LocationConnection("fogbank_hollow", Direction.WEST),
                LocationConnection("skunk_cabbage_bog", Direction.NORTH)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "cardinal_flower_creek",
            name = "Cardinal Flower Creek",
            description = LocationDescription.simple(
                "A slow-moving creek winds through wet woods, its banks lined with brilliant red cardinal flowers in summer. The flowers are hummingbird-pollinated—you've watched the aerial displays, impressed despite yourself. The creek is shallow enough to wade but requires care (slippery stones, sudden depths). The cardinal flowers create stunning visual displays against dark water and green foliage. The creek serves as both landmark and travel corridor, connecting different wetland sections."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("willow_weep", Direction.NORTH),
                LocationConnection("cypress_knees", Direction.NORTHEAST),
                LocationConnection("heron_shallows", Direction.WEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7
        ),
        
        Location(
            id = "beaver_pond_edge",
            name = "Beaver Pond Edge",
            description = LocationDescription.simple(
                "Beavers dammed a forest creek, creating a pond that drowned trees and created standing snags. The pond edges fluctuate with dam maintenance—sometimes flooded, sometimes exposed mud. You navigate the ever-changing shoreline, adapting to the beavers' engineering projects. The pond attracts waterfowl, fish, and aquatic mammals. Dead trees host woodpeckers and nesting birds. The beavers rarely appear but their influence shapes the entire ecosystem. You avoid the lodge (beavers are large and temperamental) but benefit from the habitat they create."
            ),
            biome = BiomeType.FOREST,
            gridX = -5,
            gridY = 4,
            connections = listOf(
                LocationConnection("fogbank_hollow", Direction.NORTH),
                LocationConnection("buttonbush_swamp", Direction.SOUTH),
                LocationConnection("cattail_marsh", Direction.WEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "sphagnum_bog",
            name = "Sphagnum Bog",
            description = LocationDescription.simple(
                "Sphagnum moss carpets the wettest forest sections in thick, bouncy mats. The moss is acidic, waterlogged, oxygen-poor—hostile to most plants. But specialized species thrive: carnivorous sundews, pitcher plants (yes, really), and cranberries. The bog is surreal, alien, beautiful in an inhospitable way. You traverse it carefully—the moss looks solid but conceals deep, cold water. The bog represents extreme habitat within temperate forest, a window into different ecological rules."
            ),
            biome = BiomeType.FOREST,
            gridX = -6,
            gridY = 5,
            connections = listOf(
                LocationConnection("cattail_marsh", Direction.SOUTH),
                LocationConnection("pitcher_plant_colony", Direction.NORTH),
                LocationConnection("fogbank_hollow", Direction.EAST),
                LocationConnection("mire_maw", Direction.WEST)
            ),
            encounterRate = 0.8,
            recommendedLevel = 9
        ),
        
        Location(
            id = "marsh_marigold_meadow",
            name = "Marsh Marigold Meadow",
            description = LocationDescription.simple(
                "In spring, marsh marigolds bloom in golden profusion in wet forest meadow—their yellow flowers brightening the drab late-winter landscape. The flowers emerge before trees leaf out, taking advantage of sunlight that will disappear under summer canopy. The meadow is soggy, muddy, rich with emerging life. The marigolds are toxic (you've learned this through observation of other creatures), but their beauty is undeniable. By summer they've vanished, replaced by other wetland plants in endless succession."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 6,
            connections = listOf(
                LocationConnection("alder_thicket", Direction.SOUTH),
                LocationConnection("jewelweed_jungle", Direction.NORTH),
                LocationConnection("skunk_cabbage_bog", Direction.EAST),
                LocationConnection("fogbank_hollow", Direction.SOUTHWEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "jewelweed_jungle",
            name = "Jewelweed Jungle",
            description = LocationDescription.simple(
                "Jewelweed grows in dense stands along the wet woods edges—tall plants with orange tubular flowers and succulent stems. The plants are called jewelweed because water beads on the leaves like mercury, rolling off without wetting. The flowers attract hummingbirds and bees. Seed pods explode when touched (startling every time). The sap reportedly soothes poison ivy rash (humans say this—you're not affected so can't confirm). The jungle is navigable but wet—touching the plants showers you with accumulated dew."
            ),
            biome = BiomeType.FOREST,
            gridX = -5,
            gridY = 6,
            connections = listOf(
                LocationConnection("marsh_marigold_meadow", Direction.SOUTH),
                LocationConnection("pitcher_plant_colony", Direction.WEST),
                LocationConnection("sundew_carpet", Direction.NORTH),
                LocationConnection("skunk_cabbage_bog", Direction.SOUTHEAST),
                LocationConnection("mire_maw", Direction.WEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "cinnamon_fern_grove",
            name = "Cinnamon Fern Grove",
            description = LocationDescription.simple(
                "Massive cinnamon ferns grow in the wettest sections of forest—their fronds reaching five feet tall, creating miniature forests within forests. The fertile fronds are cinnamon-colored (hence the name), standing upright among green sterile fronds. The grove feels Jurassic, prehistoric, ancient beyond measure. You navigate beneath the frond canopy, feeling appropriately small. The ferns host few insects but create excellent cover. Their fiddleheads emerge in spring, edible to those with proper digestive equipment (not you)."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 6,
            connections = listOf(
                LocationConnection("skunk_cabbage_bog", Direction.WEST),
                LocationConnection("alder_thicket", Direction.SOUTHWEST),
                LocationConnection("poison_ivy_warning_zone", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 7
        ),
        
        Location(
            id = "pitcher_plant_colony",
            name = "Pitcher Plant Colony",
            description = LocationDescription.simple(
                "In the acidic, nutrient-poor sphagnum bog, pitcher plants solve nitrogen deficiency by becoming carnivorous. Their pitcher-shaped leaves trap insects that drown in digestive fluids. You observe this with fascinated horror—plants that eat animals violate natural order (or demonstrate its flexibility). The pitchers are beautiful in a sinister way: red-veined, alluring to prey, deadly efficient. The colony represents extreme adaptation to extreme conditions. You give them wide berth (they can't eat you, but the principle disturbs)."
            ),
            biome = BiomeType.FOREST,
            gridX = -6,
            gridY = 6,
            connections = listOf(
                LocationConnection("sphagnum_bog", Direction.SOUTH),
                LocationConnection("sundew_carpet", Direction.EAST),
                LocationConnection("jewelweed_jungle", Direction.EAST),
                LocationConnection("mire_maw", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9,
            lore = "The pitcher plants here are northern pitcher plants (Sarracenia purpurea), one of few carnivorous plants native to this region. They supplement poor soil nutrients by digesting insects. Nature finds a way."
        ),
        
        Location(
            id = "sundew_carpet",
            name = "Sundew Carpet",
            description = LocationDescription.simple(
                "Tiny sundew plants carpet sections of sphagnum bog—rosettes of red, sticky leaves that trap insects on adhesive droplets. The plants are small (appropriately scaled to you), beautiful up close, terrifying in concept. You've watched insects land on the glistening drops, struggle, get slowly engulfed and digested. The sundews are patient hunters, immobile but effective. The carpet sparkles in sunlight, droplets looking like dew (hence the name), hiding deadly purpose beneath beauty. Another reminder that danger comes in all sizes."
            ),
            biome = BiomeType.FOREST,
            gridX = -5,
            gridY = 7,
            connections = listOf(
                LocationConnection("jewelweed_jungle", Direction.SOUTH),
                LocationConnection("pitcher_plant_colony", Direction.WEST),
                LocationConnection("mire_maw", Direction.SOUTHWEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 9
        ),
        
        // ========== SUB-REGION 2D: EASTERN MOUNTAIN WOODS (15 locations) ==========
        
        Location(
            id = "pine_needle_carpet",
            name = "Pine Needle Carpet",
            description = LocationDescription.simple(
                "White pine forests create thick needle carpets underfoot—soft, silent, pleasant to walk on. The pines tower overhead with straight trunks reaching for sky. Undergrowth is minimal (pine needle acidity discourages competition), creating open forest floor with excellent visibility. Pine cones litter the ground, some partially eaten by squirrels, others whole and impressive. The carpet muffles sound—you move silently here. The forest smells of pine resin, clean and sharp. This is transition forest, elevation increasing toward mountains."
            ),
            biome = BiomeType.FOREST,
            gridX = 4,
            gridY = 3,
            connections = listOf(
                LocationConnection("acorn_harvest_ground", Direction.WEST),
                LocationConnection("birch_bark_grove", Direction.NORTH),
                LocationConnection("pine_transition_zone", Direction.WEST),
                LocationConnection("woodfern_slope", Direction.EAST),
                LocationConnection("pine_transition_zone", Direction.NORTHWEST),
                LocationConnection("boulder_moss_grove", Direction.NORTH),
                LocationConnection("chipmunk_cache", Direction.SOUTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),
        
        Location(
            id = "boulder_moss_grove",
            name = "Boulder Moss Grove",
            description = LocationDescription.simple(
                "Glacial erratics—massive boulders deposited by ancient ice sheets—dot the eastern forest, each one covered in thick moss cushions. The boulders provide landmarks, basking spots, and territorial markers. Moss grows in varieties you can't name but appreciate aesthetically. The grove is rockier than typical forest, elevation increasing noticeably. Trees grow between boulders in determined angles. The moss-covered rocks look like sleeping giants, permanent features in an ever-changing landscape."
            ),
            biome = BiomeType.FOREST,
            gridX = 5,
            gridY = 4,
            connections = listOf(
                LocationConnection("pine_needle_carpet", Direction.SOUTH),
                LocationConnection("birch_bark_grove", Direction.WEST),
                LocationConnection("woodfern_slope", Direction.SOUTH),
                LocationConnection("rhododendron_tunnel", Direction.NORTH),
                LocationConnection("hemlock_cathedral", Direction.NORTHEAST),
                LocationConnection("pine_transition_zone", Direction.WEST),
                LocationConnection("avalanche_scar", Direction.NORTH),
                LocationConnection("mountain_laurel_thicket", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "chipmunk_cache",
            name = "Chipmunk Cache",
            description = LocationDescription.simple(
                "Chipmunks store vast quantities of seeds and nuts in underground chambers throughout the eastern woods. This particular cache is legendary—a warehouse operation supplying multiple chipmunk families through winter. You've negotiated observation rights in exchange for predator warnings. Watching the chipmunks work is mesmerizing: they fill cheek pouches to capacity, transport seeds underground, return for more in tireless rotation. The cache represents planning, industry, and anxiety about scarcity you deeply understand."
            ),
            biome = BiomeType.FOREST,
            gridX = 4,
            gridY = 2,
            connections = listOf(
                LocationConnection("pine_needle_carpet", Direction.NORTH),
                LocationConnection("acorn_harvest_ground", Direction.NORTHWEST),
                LocationConnection("switchgrass_savanna", Direction.SOUTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 6
        ),
        
        Location(
            id = "avalanche_scar",
            name = "Avalanche Scar",
            description = LocationDescription.simple(
                "Years ago, heavy snow slid down the mountainside, taking trees with it and creating this scar through the forest. Young trees now reclaim the opening—aspen, birch, and pine competing for light. The scar provides rare sunlight in otherwise dense forest, encouraging berry bushes and wildflowers. You use the scar as a travel corridor between forest elevations. The avalanche path reminds you that mountains are dynamic, dangerous, indifferent to those living on their slopes."
            ),
            biome = BiomeType.FOREST,
            gridX = 6,
            gridY = 3,
            connections = listOf(
                LocationConnection("boulder_moss_grove", Direction.SOUTH),
                LocationConnection("mountain_laurel_thicket", Direction.SOUTHEAST),
                LocationConnection("woodfern_slope", Direction.SOUTHWEST),
                LocationConnection("mountain_laurel_thicket", Direction.EAST),
                LocationConnection("foothill_pass", Direction.SOUTHEAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 8
        ),
        
        Location(
            id = "mountain_laurel_thicket",
            name = "Mountain Laurel Thicket",
            description = LocationDescription.simple(
                "Mountain laurel forms evergreen thickets on rocky slopes—dense, twisted, impassable except through discovered gaps. The laurel blooms with pink-white flowers in late spring, creating spectacular displays. The leaves are toxic to most mammals (deer avoid them), but the thicket provides year-round cover. You navigate the laurel via tunnels beneath the branches, protected from aerial predators. The thicket represents specialized adaptation to mountain conditions—tough, persistent, beautiful, and slightly poisonous."
            ),
            biome = BiomeType.FOREST,
            gridX = 6,
            gridY = 4,
            connections = listOf(
                LocationConnection("boulder_moss_grove", Direction.WEST),
                LocationConnection("avalanche_scar", Direction.WEST),
                LocationConnection("porcupine_den_ridge", Direction.NORTHEAST),
                LocationConnection("spruce_forest_edge", Direction.NORTHEAST),
                LocationConnection("avalanche_scar", Direction.NORTHWEST),
                LocationConnection("hemlock_cathedral", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "hemlock_cathedral",
            name = "Hemlock Cathedral",
            description = LocationDescription.simple(
                "Ancient eastern hemlocks create cathedral-like groves with high canopies and deeply shaded interiors. The hemlocks are slow-growing, long-lived, creating forests within forests. Their needles are soft, short, creating fine-textured canopy. The cathedral floor is bare except for needles and fallen branches—almost no undergrowth survives the deep shade. The space feels sacred, quiet, reverent. You move through with appropriate solemnity, aware of the hemlocks' age and dignity. Some of these trees predate human settlement."
            ),
            biome = BiomeType.FOREST,
            gridX = 6,
            gridY = 5,
            connections = listOf(
                LocationConnection("mountain_laurel_thicket", Direction.SOUTH),
                LocationConnection("spruce_forest_edge", Direction.EAST),
                LocationConnection("rhododendron_tunnel", Direction.WEST),
                LocationConnection("mountain_ash_berry_grove", Direction.NORTHWEST),
                LocationConnection("witch_hobble_understory", Direction.NORTH),
                LocationConnection("boulder_moss_grove", Direction.SOUTHWEST),
                LocationConnection("porcupine_den_ridge", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 9
        ),
        
        Location(
            id = "porcupine_den_ridge",
            name = "Porcupine Den Ridge",
            description = LocationDescription.simple(
                "Porcupines den in rocky crevices along this ridge, venturing out at night to feed on bark and buds. Evidence of their residence: gnawed bark, tracks, droppings, and the occasional quill. You give porcupines wide berth (those quills are serious business), but you respect their defensive strategy—being too annoying to eat is valid survival tactic. The ridge provides good views downslope and protection from weather. The porcupines tolerate your presence (you're not a threat and they know it)."
            ),
            biome = BiomeType.FOREST,
            gridX = 7,
            gridY = 5,
            connections = listOf(
                LocationConnection("hemlock_cathedral", Direction.SOUTH),
                LocationConnection("spruce_forest_edge", Direction.SOUTH),
                LocationConnection("witch_hobble_understory", Direction.NORTHWEST),
                LocationConnection("mountain_laurel_thicket", Direction.SOUTHWEST),
                LocationConnection("foothill_pass", Direction.EAST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 9
        ),
        
        Location(
            id = "rhododendron_tunnel",
            name = "Rhododendron Tunnel",
            description = LocationDescription.simple(
                "Rhododendron shrubs form evergreen tunnels on mountain slopes—their twisted branches creating natural archways. The rhododendrons bloom spectacularly in late spring, pink and purple flowers covering the plants. The tunnels provide excellent cover and defined routes through otherwise difficult terrain. You use them regularly, appreciating the rhododendrons' architectural contributions. The tunnels are cool in summer (deep shade), protected in winter (evergreen cover), and beautiful year-round. Nature's infrastructure at its finest."
            ),
            biome = BiomeType.FOREST,
            gridX = 5,
            gridY = 5,
            connections = listOf(
                LocationConnection("boulder_moss_grove", Direction.SOUTH),
                LocationConnection("birch_bark_grove", Direction.SOUTHWEST),
                LocationConnection("striped_maple_glade", Direction.WEST),
                LocationConnection("mountain_ash_berry_grove", Direction.NORTH),
                LocationConnection("hemlock_cathedral", Direction.EAST),
                LocationConnection("moss_carpet_clearing", Direction.WEST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 8
        ),
        
        Location(
            id = "birch_bark_grove",
            name = "Birch Bark Grove",
            description = LocationDescription.simple(
                "Yellow birch trees with golden, peeling bark grow on rocky mountain slopes. The bark peels in papery layers, traditionally used by humans for fire-starting and canoe-building. The birches smell like wintergreen when bark is scratched (you've investigated). The grove is aesthetically striking—white and gold bark contrasting with dark evergreens. The trees grow from rocky crevices, roots gripping stone with impressive tenacity. The grove represents beauty in difficult conditions, elegance despite adversity."
            ),
            biome = BiomeType.FOREST,
            gridX = 4,
            gridY = 4,
            connections = listOf(
                LocationConnection("pine_needle_carpet", Direction.SOUTH),
                LocationConnection("striped_maple_glade", Direction.NORTH),
                LocationConnection("boulder_moss_grove", Direction.EAST),
                LocationConnection("rhododendron_tunnel", Direction.NORTHEAST)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        ),
        
        Location(
            id = "woodfern_slope",
            name = "Woodfern Slope",
            description = LocationDescription.simple(
                "A north-facing slope hosts extensive woodfern colonies—their delicate fronds creating lush green carpets beneath mixed forest. The slope retains moisture better than sunny exposures, supporting the ferns' needs. You traverse the slope carefully (it's steep), using exposed roots and rocks as foot-holds. The ferns provide cover and pleasant aesthetics but limited food resources. The slope marks transition between lower forest and higher mountain zones—you're gaining elevation with each visit."
            ),
            biome = BiomeType.FOREST,
            gridX = 5,
            gridY = 3,
            connections = listOf(
                LocationConnection("pine_needle_carpet", Direction.WEST),
                LocationConnection("boulder_moss_grove", Direction.NORTH),
                LocationConnection("avalanche_scar", Direction.NORTHEAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 7
        ),
        
        Location(
            id = "spruce_forest_edge",
            name = "Spruce Forest Edge",
            description = LocationDescription.simple(
                "Red spruce begins appearing at higher elevations, mixing with pines and hemlocks. The spruce mark the transition to boreal forest—ecosystems more typical of Canada appearing on mountain slopes. The spruce have shorter needles and denser growth than pines. The forest here feels different—colder, darker, more northern. You're approaching the limits of your comfortable elevation range but fascinated by the changing ecology. The spruce edge represents a threshold you can cross but might not wish to settle beyond."
            ),
            biome = BiomeType.FOREST,
            gridX = 7,
            gridY = 4,
            connections = listOf(
                LocationConnection("porcupine_den_ridge", Direction.NORTH),
                LocationConnection("hemlock_cathedral", Direction.WEST),
                LocationConnection("mountain_laurel_thicket", Direction.SOUTHWEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 9
        ),
        
        Location(
            id = "mountain_ash_berry_grove",
            name = "Mountain Ash Berry Grove",
            description = LocationDescription.simple(
                "Mountain ash trees (not actually ash, the humans explain) produce clusters of orange berries that birds devour in autumn. The grove becomes a feeding frenzy as migrants and residents compete for the fruit. You benefit from the chaos—dropped berries, distracted birds, abundant insects. The trees are small, twisted from wind exposure, tough from mountain conditions. The berry clusters are spectacularly bright, visible from distance, serving as navigation landmarks during berry season."
            ),
            biome = BiomeType.FOREST,
            gridX = 5,
            gridY = 6,
            connections = listOf(
                LocationConnection("rhododendron_tunnel", Direction.SOUTH),
                LocationConnection("witch_hobble_understory", Direction.EAST),
                LocationConnection("vernal_pool_forest", Direction.NORTH),
                LocationConnection("hemlock_cathedral", Direction.SOUTHEAST),
                LocationConnection("silverleaf_canopy", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 8
        ),
        
        Location(
            id = "witch_hobble_understory",
            name = "Witch Hobble Understory",
            description = LocationDescription.simple(
                "Hobblebush (witch hobble, the names vary) forms understory shrubs in mountain forests—their horizontal branches creating trip hazards (hence the name). The shrubs bloom with white flowers in spring and produce red berries that birds favor. The hobble creates navigation challenges—you duck under branches, hop over stems, and occasionally get caught (the name is accurate). Despite the inconvenience, the understory provides excellent cover and year-round interest. Adaptation includes learning to navigate the hobble's tricks."
            ),
            biome = BiomeType.FOREST,
            gridX = 6,
            gridY = 6,
            connections = listOf(
                LocationConnection("hemlock_cathedral", Direction.SOUTH),
                LocationConnection("vernal_pool_forest", Direction.WEST),
                LocationConnection("porcupine_den_ridge", Direction.SOUTHEAST),
                LocationConnection("mountain_ash_berry_grove", Direction.WEST)
            ),
            encounterRate = 0.6,
            recommendedLevel = 9
        ),
        
        Location(
            id = "striped_maple_glade",
            name = "Striped Maple Glade",
            description = LocationDescription.simple(
                "Striped maple (moosewood) with distinctive green-white striped bark grows in mountain forest understory. The small trees have large leaves, creating shade beneath shade. Moose browse the twigs (hence the alternate name), though you've never seen one here (thankfully—moose are enormous). The glade is cool, moist, lush with mosses and ferns. The striped bark is diagnostic—no other tree looks quite like it. The glade represents specialized mountain forest, elevation-specific ecosystems within larger forest."
            ),
            biome = BiomeType.FOREST,
            gridX = 4,
            gridY = 5,
            connections = listOf(
                LocationConnection("birch_bark_grove", Direction.SOUTH),
                LocationConnection("rhododendron_tunnel", Direction.EAST),
                LocationConnection("moss_carpet_clearing", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 7
        ),
        
        Location(
            id = "vernal_pool_forest",
            name = "Vernal Pool Forest",
            description = LocationDescription.simple(
                "Seasonal pools dot the mountain forest floor, filling with snowmelt and spring rain, drying by summer. The pools host explosive amphibian breeding—salamanders, frogs, and fairy shrimp appear as if by magic when water fills the depressions. By summer, the pools are just leafy hollows. The forest around pools is richer, moister, hosting different vegetation than surrounding slopes. You visit the pools regularly, timing visits to seasonal events. The pools represent ephemeral habitat, temporary but crucial."
            ),
            biome = BiomeType.FOREST,
            gridX = 5,
            gridY = 7,
            connections = listOf(
                LocationConnection("mountain_ash_berry_grove", Direction.SOUTH),
                LocationConnection("witch_hobble_understory", Direction.EAST),
                LocationConnection("silverleaf_canopy", Direction.WEST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 8
        ),
        
        // ========== SUB-REGION 2E: ENCHANTED GROVES (10 locations) ==========
        
        Location(
            id = "glowshroom_cathedral",
            name = "Glowshroom Cathedral",
            description = LocationDescription.simple(
                "In the deepest, darkest forest section, bioluminescent fungi grow on rotting logs and tree bases. The mushrooms glow faintly green at night—foxfire, caused by chemical reactions in fungal tissue. The cathedral is a night-time phenomenon; daytime reveals ordinary mushrooms. But visiting after dark transforms the forest into alien landscape—glowing points scattered across the forest floor like terrestrial stars. The effect is magical, eerie, beautiful, and slightly unsettling. You visit rarely (nighttime forest is dangerous) but the memories persist."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 10,
            connections = listOf(
                LocationConnection("fairy_ring", Direction.SOUTH),
                LocationConnection("eldergrove_council", Direction.NORTH),
                LocationConnection("moonlight_clearing", Direction.EAST),
                LocationConnection("singing_stones", Direction.EAST),
                LocationConnection("dreamweaver_glade", Direction.WEST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 10,
            lore = "The bioluminescent fungi (likely Armillaria species—honey mushrooms) produce light through luciferin-luciferase reactions. The purpose is unknown—attracting spore dispersers? Metabolic byproduct? The mushrooms aren't talking."
        ),
        
        Location(
            id = "singing_stones",
            name = "Singing Stones",
            description = LocationDescription.simple(
                "Wind blows through rock formations in the northern forest, creating musical tones—the singing stones. The effect requires specific wind direction and speed; you've learned to predict singing days by weather patterns. The stones' song ranges from low moans to high whistles, creating harmonics that sound deliberately composed. The phenomenon is purely physical—wind through rock gaps—but feels intentional, as if the stones communicate. You listen when they sing, finding the music both comforting and mysterious."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 9,
            connections = listOf(
                LocationConnection("glowshroom_cathedral", Direction.WEST),
                LocationConnection("eldergrove_council", Direction.NORTHWEST),
                LocationConnection("moonlight_clearing", Direction.NORTH),
                LocationConnection("whispering_willow", Direction.NORTHEAST),
                LocationConnection("bird_bath_oasis", Direction.SOUTH),
                LocationConnection("eldergrove_council", Direction.NORTH)
            ),
            encounterRate = 0.6,
            recommendedLevel = 9
        ),
        
        Location(
            id = "dreamweaver_glade",
            name = "Dreamweaver Glade",
            description = LocationDescription.simple(
                "This forest glade has a reputation—animals who rest here report vivid dreams. You've experienced this personally: sleep here brings dreams of flying, of being larger, of understanding speech. The cause is unknown—some mushroom species produce psychoactive compounds, or perhaps minerals in the soil, or pure coincidence and suggestion. Regardless, the glade feels special, significant, slightly dangerous. You visit when life requires perspective, dreams providing insights (or at least interesting hallucinations)."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 10,
            connections = listOf(
                LocationConnection("glowshroom_cathedral", Direction.EAST),
                LocationConnection("mirror_pool", Direction.WEST),
                LocationConnection("crystal_cascade", Direction.NORTHWEST),
                LocationConnection("starlight_grove", Direction.NORTHEAST),
                LocationConnection("witch_hazel_thicket", Direction.SOUTH),
                LocationConnection("eldergrove_council", Direction.NORTHEAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 10
        ),
        
        Location(
            id = "eldergrove_council",
            name = "Eldergrove Council",
            description = LocationDescription.simple(
                "The oldest trees in the forest grow here—oaks, maples, and hemlocks that predate European settlement. They stand like elders in council, their size and age commanding respect. The grove is quiet (deep shade suppresses undergrowth), reverent, sacred without need for human designation. You feel small here, aware of your brief lifespan against the trees' centuries. The eldergrove reminds you that forests measure time differently, that your urgencies are ephemeral against their patience."
            ),
            biome = BiomeType.FOREST,
            gridX = 0,
            gridY = 11,
            connections = listOf(
                LocationConnection("glowshroom_cathedral", Direction.SOUTH),
                LocationConnection("starlight_grove", Direction.NORTH),
                LocationConnection("moonlight_clearing", Direction.SOUTH),
                LocationConnection("singing_stones", Direction.SOUTH),
                LocationConnection("rainbow_falls", Direction.NORTH),
                LocationConnection("whispering_willow", Direction.SOUTHEAST),
                LocationConnection("singing_stones", Direction.SOUTHEAST),
                LocationConnection("dreamweaver_glade", Direction.SOUTHWEST)
            ),
            isSafeZone = true,
            encounterRate = 0.4,
            recommendedLevel = 11,
            lore = "The oldest oak in Eldergrove Council is approximately 350 years old based on core samples. It was a sapling when the first European settlers arrived. It remembers nothing of them."
        ),
        
        Location(
            id = "moonlight_clearing",
            name = "Moonlight Clearing",
            description = LocationDescription.simple(
                "A natural clearing where the canopy opens just enough to admit moonlight. On full moon nights, the clearing glows silver, transforming into magical space. Moths gather in the moonlight, white wings reflecting pale light. The clearing is ordinary by day but transcendent by night. You visit during full moons when safe, appreciating the transformation. The moonlight clearing represents nature's theater, free performances for those who notice, beauty available to the attentive."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 10,
            connections = listOf(
                LocationConnection("singing_stones", Direction.SOUTH),
                LocationConnection("rainbow_falls", Direction.NORTHWEST),
                LocationConnection("whispering_willow", Direction.EAST),
                LocationConnection("glowshroom_cathedral", Direction.WEST),
                LocationConnection("eldergrove_council", Direction.NORTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 10
        ),
        
        Location(
            id = "mirror_pool",
            name = "Mirror Pool",
            description = LocationDescription.simple(
                "A perfectly still forest pool reflects the canopy with such clarity that sky and earth appear identical. The pool is spring-fed, cold, clear, protected from wind by surrounding trees. Looking into it creates vertigo—you see yourself suspended between two forests, unsure which is real. The reflection is perfect on calm days, shattered by rain or wind. The pool is both landmark and meditation object, reminding you that reality depends on perspective."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 10,
            connections = listOf(
                LocationConnection("dreamweaver_glade", Direction.EAST),
                LocationConnection("witch_hazel_thicket", Direction.SOUTHEAST),
                LocationConnection("crystal_cascade", Direction.WEST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 10
        ),
        
        Location(
            id = "crystal_cascade",
            name = "Crystal Cascade",
            description = LocationDescription.simple(
                "A small stream cascades over quartz-rich rocks, the water sparkling as it catches light on crystal facets. The cascade is miniature (two feet high), but the crystal makes it spectacular. The rocks are wet, slippery, beautiful. The cascade creates constant music—water over stone in endless conversation. The spray keeps nearby rocks moist, supporting thick moss cushions. The cascade represents hidden beauty, small-scale magnificence, proof that wonders exist at every size."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 10,
            connections = listOf(
                LocationConnection("mirror_pool", Direction.EAST),
                LocationConnection("starlight_grove", Direction.EAST),
                LocationConnection("dreamweaver_glade", Direction.SOUTHEAST),
                LocationConnection("northern_property_corner", Direction.SOUTH)
            ),
            encounterRate = 0.5,
            recommendedLevel = 10
        ),
        
        Location(
            id = "whispering_willow",
            name = "The Whispering Willow",
            description = LocationDescription.simple(
                "A single ancient willow grows in the northern forest, its branches hanging in green curtains that whisper in the slightest breeze. The willow is far from water (unusual for its species), suggesting it found springs others missed. The tree is hollow but alive, hosting cavity-nesting birds and small mammals. The whispers sound almost like speech when wind moves through the branches. You rest beneath the willow often, finding the whispers comforting rather than unsettling. The tree feels knowing, aware, protective."
            ),
            biome = BiomeType.FOREST,
            gridX = 2,
            gridY = 10,
            connections = listOf(
                LocationConnection("moonlight_clearing", Direction.WEST),
                LocationConnection("singing_stones", Direction.SOUTHWEST),
                LocationConnection("eldergrove_council", Direction.NORTHWEST)
            ),
            encounterRate = 0.4,
            recommendedLevel = 10
        ),
        
        Location(
            id = "starlight_grove",
            name = "Starlight Grove",
            description = LocationDescription.simple(
                "Where the forest canopy thins near a north-facing cliff, gaps allow starlight through on clear nights. The grove becomes a planetarium, stars visible between branches in patterns that change with seasons. You visit on clear, dark nights, lying on moss and watching stars wheel overhead. The experience is humbling—awareness of vast universe, your smallness, the temporary nature of all concerns. The starlight grove offers perspective unavailable elsewhere, cosmic context for terrestrial struggles."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 11,
            connections = listOf(
                LocationConnection("eldergrove_council", Direction.SOUTH),
                LocationConnection("rainbow_falls", Direction.EAST),
                LocationConnection("dreamweaver_glade", Direction.SOUTHWEST),
                LocationConnection("crystal_cascade", Direction.WEST)
            ),
            encounterRate = 0.35,
            recommendedLevel = 11
        ),
        
        Location(
            id = "rainbow_falls",
            name = "Rainbow Falls",
            description = LocationDescription.simple(
                "A forest stream falls over a small ledge, creating mist that catches afternoon sun in rainbow displays. The falls are modest (three feet drop), but the rainbow effect is reliable on sunny afternoons. You've learned the optimal viewing times and positions. The rainbow represents physics made beautiful—light refraction through water droplets creating spectrum. But understanding the science doesn't diminish the wonder. Beauty is beautiful regardless of explanation."
            ),
            biome = BiomeType.FOREST,
            gridX = 1,
            gridY = 11,
            connections = listOf(
                LocationConnection("eldergrove_council", Direction.SOUTH),
                LocationConnection("starlight_grove", Direction.WEST),
                LocationConnection("moonlight_clearing", Direction.SOUTHEAST)
            ),
            encounterRate = 0.45,
            recommendedLevel = 11
        ),
        
        // ========== SUB-REGION 2F: THORNWOOD LABYRINTH (10 locations) ==========
        
        Location(
            id = "briarblade_gauntlet",
            name = "Briarblade Gauntlet",
            description = LocationDescription.simple(
                "Blackberry and raspberry brambles have grown into impenetrable thicket—thorns like blades creating a natural fortress. The gauntlet has a few discovered passages, but most routes are blocked by thorny tangles. Berries ripen in summer, attracting birds and bears (you avoid bear-visit times). The brambles provide excellent predator protection—nothing can pursue through the thorns. Navigating the gauntlet requires memory, patience, and tolerance for occasional thorn-pricks. The brambles are hostile terrain turned defensive asset."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 4,
            connections = listOf(
                LocationConnection("thorn_brake", Direction.SOUTH),
                LocationConnection("hawthorn_fortress", Direction.SOUTH),
                LocationConnection("poison_ivy_curtain", Direction.SOUTHWEST),
                LocationConnection("devil_walking_stick", Direction.SOUTHWEST),
                LocationConnection("thorn_cathedral", Direction.SOUTH),
                LocationConnection("wild_grape_tangle", Direction.SOUTHEAST),
                LocationConnection("thistle_throne", Direction.WEST)
            ),
            encounterRate = 0.8,
            recommendedLevel = 9
        ),
        
        Location(
            id = "thistle_throne",
            name = "Thistle Throne",
            description = LocationDescription.simple(
                "A massive bull thistle grows in a forest clearing—eight feet tall, covered in spines, crowned with purple flowers. The thistle dominates its clearing like a thorny monarch. Goldfinches work the seed heads, performing acrobatics to access food while avoiding spines. The throne is both landmark and obstacle, beautiful and dangerous. You circle it respectfully, appreciating the thistle's defensive strategy while maintaining safe distance. The throne represents botanical success through aggressive defense."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 3,
            connections = listOf(
                LocationConnection("briarblade_gauntlet", Direction.EAST),
                LocationConnection("barberry_maze", Direction.SOUTH),
                LocationConnection("poison_ivy_curtain", Direction.SOUTH),
                LocationConnection("thorn_cathedral", Direction.NORTHEAST),
                LocationConnection("thorn_brake", Direction.NORTHEAST),
                LocationConnection("wild_grape_tangle", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "poison_ivy_curtain",
            name = "Poison Ivy Curtain",
            description = LocationDescription.simple(
                "Poison ivy has climbed trees and created living curtains of three-leafed growth. The curtain is impassable for humans (severe allergic reactions), but you navigate through carefully. The ivy produces white berries that birds eat, spreading seeds throughout the forest. The curtain demonstrates plant success through chemical warfare—produce irritating oils, discourage browsers, dominate available space. You're immune but sympathetic to human suffering (they really should learn to identify it)."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 3,
            connections = listOf(
                LocationConnection("thistle_throne", Direction.NORTH),
                LocationConnection("barberry_maze", Direction.EAST),
                LocationConnection("hawthorn_fortress", Direction.NORTHEAST),
                LocationConnection("briarblade_gauntlet", Direction.NORTHEAST),
                LocationConnection("heron_shallows", Direction.SOUTH)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "barberry_maze",
            name = "Barberry Maze",
            description = LocationDescription.simple(
                "Invasive Japanese barberry forms dense, thorny mazes throughout this forest section. The shrubs have small leaves, sharp spines, and red berries that persist through winter. The maze is difficult to navigate—the barberry grows densely, blocking obvious routes. You've memorized passages through trial and error (emphasis on error—those spines are sharp). The barberry represents invasive success, foreign species outcompeting natives through sheer thorny aggression. Adaptation requires accepting new ecological realities."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 2,
            connections = listOf(
                LocationConnection("thistle_throne", Direction.NORTH),
                LocationConnection("multiflora_rose_wall", Direction.EAST),
                LocationConnection("greenbrier_tangle", Direction.SOUTH),
                LocationConnection("poison_ivy_curtain", Direction.WEST),
                LocationConnection("mushroom_glade", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 8
        ),
        
        Location(
            id = "multiflora_rose_wall",
            name = "Multiflora Rose Wall",
            description = LocationDescription.simple(
                "Multiflora rose (another invasive) has created living walls of recurved thorns. The rose blooms white and fragrant in spring, producing red rose hips that birds distribute. The walls are nearly impenetrable, creating boundaries within forest. You've found gaps, but navigation requires knowledge and care. The rose represents human introduction (originally planted for erosion control) gone wild, good intentions creating thorny complications. The wall is both obstacle and shelter, depending on which side you're on."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 2,
            connections = listOf(
                LocationConnection("mushroom_glade", Direction.NORTH),
                LocationConnection("wild_rose_rampart", Direction.NORTH),
                LocationConnection("greenbrier_tangle", Direction.WEST),
                LocationConnection("barberry_maze", Direction.WEST),
                LocationConnection("hickory_nut_field", Direction.SOUTH)
            ),
            encounterRate = 0.7,
            recommendedLevel = 8
        ),
        
        Location(
            id = "greenbrier_tangle",
            name = "Greenbrier Tangle",
            description = LocationDescription.simple(
                "Native greenbrier vines create tangles with thorns that hook and hold. The vines climb trees and sprawl across ground, creating three-dimensional obstacles. Greenbrier produces small berries that wildlife eat, and the young shoots are edible (humans eat them—you're not convinced). The tangle is frustrating to navigate but provides excellent cover. The greenbrier is native, adapted, successful through defensive strategy. You've learned to respect its territory while maintaining necessary passage routes."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 2,
            connections = listOf(
                LocationConnection("barberry_maze", Direction.NORTH),
                LocationConnection("multiflora_rose_wall", Direction.EAST),
                LocationConnection("hickory_nut_field", Direction.SOUTHEAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 9
        ),
        
        Location(
            id = "hawthorn_fortress",
            name = "Hawthorn Fortress",
            description = LocationDescription.simple(
                "Hawthorn trees with inch-long thorns grow in dense thicket formation. The fortress is aptly named—penetrating it seems militarily inadvisable. Hawthorns bloom white in spring, produce red fruits (haws) that birds eat. The thorns are serious weapons, capable of penetrating skin easily. You've found one safe passage through the fortress, guarding the knowledge carefully. The hawthorn represents defensive architecture at botanical level, thorns as fortification, survival through deterrence."
            ),
            biome = BiomeType.FOREST,
            gridX = -3,
            gridY = 4,
            connections = listOf(
                LocationConnection("thorn_brake", Direction.EAST),
                LocationConnection("devil_walking_stick", Direction.WEST),
                LocationConnection("briarblade_gauntlet", Direction.NORTH),
                LocationConnection("poison_ivy_curtain", Direction.SOUTHWEST)
            ),
            encounterRate = 0.8,
            recommendedLevel = 10
        ),
        
        Location(
            id = "devil_walking_stick",
            name = "Devil's Walking Stick Grove",
            description = LocationDescription.simple(
                "Aralia spinosa—devil's walking stick—grows in this grove, its stems covered in sharp prickles. The plant sends up thick, spiny shoots that look like they were designed specifically to discourage all contact. The grove blooms with white flower clusters in summer, and the berries attract migrating birds. You navigate the grove with extreme caution—the devil's walking stick earns its name. The grove represents botanical aggression at peak levels, thorns as lifestyle choice."
            ),
            biome = BiomeType.FOREST,
            gridX = -4,
            gridY = 4,
            connections = listOf(
                LocationConnection("hawthorn_fortress", Direction.EAST),
                LocationConnection("briarblade_gauntlet", Direction.NORTHEAST),
                LocationConnection("willow_weep", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11
        ),
        
        Location(
            id = "wild_rose_rampart",
            name = "Wild Rose Rampart",
            description = LocationDescription.simple(
                "Native wild roses form thorny ramparts along the thorn-brake edges. The roses bloom pink and fragrant, then produce bright red rose hips rich in vitamin C. The ramparts are defensive lines, thorns discouraging passage. You've established negotiated corridors through careful exploration. The wild roses are beautiful and dangerous simultaneously—a combination you understand personally. The rampart represents dual nature, defense and beauty integrated seamlessly."
            ),
            biome = BiomeType.FOREST,
            gridX = -1,
            gridY = 3,
            connections = listOf(
                LocationConnection("thorn_brake", Direction.WEST),
                LocationConnection("thorn_cathedral", Direction.WEST),
                LocationConnection("multiflora_rose_wall", Direction.SOUTH),
                LocationConnection("mushroom_glade", Direction.EAST)
            ),
            encounterRate = 0.7,
            recommendedLevel = 9
        ),
        
        Location(
            id = "thorn_cathedral",
            name = "Thorn Cathedral",
            description = LocationDescription.simple(
                "In the heart of the thornwood, thorny plants from multiple species have grown together, creating a cathedral-like space. Blackberry, raspberry, rose, hawthorn, and barberry all tangle overhead, their thorns interlacing into vaulted ceiling. The cathedral interior is surprisingly open—once inside, the thorns are mostly overhead. The space feels sacred, protected, earned through dangerous passage. You've claimed the cathedral as a meditation spot, appreciating the privacy thorns provide. Few creatures penetrate this deeply."
            ),
            biome = BiomeType.FOREST,
            gridX = -2,
            gridY = 3,
            connections = listOf(
                LocationConnection("wild_rose_rampart", Direction.EAST),
                LocationConnection("briarblade_gauntlet", Direction.NORTH),
                LocationConnection("thistle_throne", Direction.SOUTHWEST)
            ),
            isSafeZone = true,
            encounterRate = 0.3,
            recommendedLevel = 12,
            lore = "The Thorn Cathedral formed over approximately 15 years as multiple thorny species grew into each other. The interior space is maintained by deer bedding down (they appreciate the predator protection as much as you do)."
        )
    )
}
