package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * CAVE REGION - Underground locations expanding crystal_mines, deep_dark, forgotten_catacombs
 * Total: 35 locations across 4 sub-regions
 * Level Range: 8-20 (mid to endgame content)
 * Theme: Underground exploration, crystal mining, eldritch horror, ancient tombs
 */
internal val CAVE_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 8A: Crystal Mines Expansion (10 locations, levels 8-12) ====================
        
        Location(
            id = "amethyst_chamber",
            name = "Amethyst Chamber",
            description = LocationDescription.simple("A cavern lined with massive amethyst crystals glowing faintly purple. The crystals hum with subtle energy. Miners prize these for beauty and magical properties, but crystal elementals guard them fiercely."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = 3,
            connections = listOf(
                LocationConnection("crystal_mines", Direction.UP),
                LocationConnection("quartz_tunnel", Direction.NORTH),
                LocationConnection("geode_grotto", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "quartz_tunnel",
            name = "Quartz Tunnel",
            description = LocationDescription.simple("Quartz veins crisscross walls like frozen lightning, creating disorienting optical effects. The tunnel requires crawling, claustrophobic and unstable. Quartz fragments litter the floor, sharp as razors."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = 4,
            connections = listOf(
                LocationConnection("amethyst_chamber", Direction.SOUTH),
                LocationConnection("rose_quartz_cave", Direction.EAST),
                LocationConnection("collapsed_tunnel", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 9
        ),
        
        Location(
            id = "rose_quartz_cave",
            name = "Rose Quartz Cave",
            description = LocationDescription.simple("Rose quartz fills this cave with soft pink hues. Light passing through bathes everything in rosy glow. The cave has a peaceful atmosphere, unusual for underground. Miners report feeling calmer here, stress melting away."),
            biome = BiomeType.CAVE,
            gridX = 3,
            gridY = 4,
            connections = listOf(
                LocationConnection("quartz_tunnel", Direction.WEST),
                LocationConnection("geode_grotto", Direction.SOUTH),
                LocationConnection("smoky_quartz_shaft", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 9,
            isSafeZone = true
        ),
        
        Location(
            id = "smoky_quartz_shaft",
            name = "Smoky Quartz Shaft",
            description = LocationDescription.simple("A vertical shaft descending through smoky quartz darkened by radiation. Climbing is treacherous, handholds carved into walls. Miners use smoky quartz as protective talismans against negative energy."),
            biome = BiomeType.CAVE,
            gridX = 3,
            gridY = 5,
            connections = listOf(
                LocationConnection("rose_quartz_cave", Direction.SOUTH),
                LocationConnection("citrine_alcove", Direction.EAST),
                LocationConnection("crystal_dragon_lair", Direction.DOWN)
            ),
            encounterRate = 0.75,
            recommendedLevel = 10
        ),
        
        Location(
            id = "citrine_alcove",
            name = "Citrine Alcove",
            description = LocationDescription.simple("Golden citrine crystals cluster in this small alcove, glowing warmly. Merchants pay premium for citrine. But the warmth and light attract dangerous creatures."),
            biome = BiomeType.CAVE,
            gridX = 4,
            gridY = 5,
            connections = listOf(
                LocationConnection("smoky_quartz_shaft", Direction.WEST),
                LocationConnection("geode_grotto", Direction.SOUTH),
                LocationConnection("crystal_forge", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 10
        ),
        
        Location(
            id = "geode_grotto",
            name = "Geode Grotto",
            description = LocationDescription.simple("Geodes in various stages fill this grotto. Some intact, others cracked open revealing spectacular crystals. A treasure trove for collectors and miners."),
            biome = BiomeType.CAVE,
            gridX = 3,
            gridY = 3,
            connections = listOf(
                LocationConnection("amethyst_chamber", Direction.WEST),
                LocationConnection("rose_quartz_cave", Direction.NORTH),
                LocationConnection("citrine_alcove", Direction.NORTH),
                LocationConnection("collapsed_tunnel", Direction.SOUTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 9
        ),
        
        Location(
            id = "collapsed_tunnel",
            name = "Collapsed Tunnel",
            description = LocationDescription.simple("Rubble blocks this tunnel from a cave-in. Gaps allow squeezing through but further collapse is likely. Blood stains and abandoned tools tell of hasty evacuation. Something trapped on the other side scratches, trying to dig through."),
            biome = BiomeType.CAVE,
            gridX = 1,
            gridY = 4,
            connections = listOf(
                LocationConnection("quartz_tunnel", Direction.EAST),
                LocationConnection("geode_grotto", Direction.NORTH),
                LocationConnection("gas_pocket_chamber", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11
        ),
        
        Location(
            id = "crystal_forge",
            name = "Crystal Forge",
            description = LocationDescription.simple("A masterwork forge using geothermal heat to work metal and crystal. Crystalline lenses focus heat, quartz anvils withstand extreme temperatures. A master smith crafts crystal-infused weapons and tools here."),
            biome = BiomeType.CAVE,
            gridX = 5,
            gridY = 5,
            connections = listOf(
                LocationConnection("citrine_alcove", Direction.WEST),
                LocationConnection("crystal_dragon_lair", Direction.SOUTH)
            ),
            encounterRate = 0.40,
            recommendedLevel = 10,
            isSettlement = true,
            shopAvailable = true,
            isSafeZone = true
        ),
        
        Location(
            id = "crystal_dragon_lair",
            name = "Crystal Dragon Lair",
            description = LocationDescription.simple("A crystal dragon has claimed the deepest chambers. Massive, with faceted scales like cut gemstones, breath weapon a focused light beam. The lair is filled with hoarded crystals. The dragon is intelligent and can be bargained with, but doesn't tolerate theft."),
            biome = BiomeType.CAVE,
            gridX = 4,
            gridY = 4,
            connections = listOf(
                LocationConnection("smoky_quartz_shaft", Direction.UP),
                LocationConnection("deep_shaft_junction", Direction.DOWN),
                LocationConnection("crystal_forge", Direction.NORTH),
                LocationConnection("deep_shaft_junction", Direction.SOUTH)
            ),
            encounterRate = 1.0,
            recommendedLevel = 12
        ),
        
        Location(
            id = "gas_pocket_chamber",
            name = "Gas Pocket Chamber",
            description = LocationDescription.simple("Pockets of various gases stratify by density creating invisible layers. Carbon dioxide at floor level, methane at ceiling, oxygen in narrow bands. Flames can ignite methane pockets causing explosions. Navigating requires finding the right elevation to breathe."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = 3,
            connections = listOf(
                LocationConnection("collapsed_tunnel", Direction.NORTH),
                LocationConnection("drowning_tunnel", Direction.DOWN),
                LocationConnection("deep_shaft_junction", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 11
        ),

        // ==================== SUB-REGION 8B: Deep Dark Network (15 locations, levels 15-20) ====================
        
        Location(
            id = "whisper_corridor",
            name = "Whisper Corridor",
            description = LocationDescription.simple("Voices whisper from darkness in languages that hurt to hear. The whispers bypass physical senses, speaking directly to consciousness. Those who spend too long develop mental instability, whispers colonizing thoughts."),
            biome = BiomeType.CAVE,
            gridX = 0,
            gridY = -5,
            connections = listOf(
                LocationConnection("deep_dark", Direction.UP),
                LocationConnection("shadow_pool", Direction.SOUTH),
                LocationConnection("tentacle_pit", Direction.DOWN)
            ),
            encounterRate = 0.90,
            recommendedLevel = 16
        ),
        
        Location(
            id = "shadow_pool",
            name = "Shadow Pool",
            description = LocationDescription.simple("A pool of liquid darkness that absorbs light completely. Things move within that shouldn't exist in three dimensions. Touching the liquid causes shadows to detach from objects, moving independently with malicious intent."),
            biome = BiomeType.CAVE,
            gridX = 0,
            gridY = -6,
            connections = listOf(
                LocationConnection("whisper_corridor", Direction.NORTH),
                LocationConnection("fungal_forest_deep", Direction.EAST),
                LocationConnection("tentacle_pit", Direction.SOUTH),
                LocationConnection("madness_chamber", Direction.WEST)
            ),
            encounterRate = 0.95,
            recommendedLevel = 18
        ),
        
        Location(
            id = "tentacle_pit",
            name = "Tentacle Pit",
            description = LocationDescription.simple("A vertical shaft descending into incomprehensible depths. Massive tentacles covered in suckers and hooks emerge seeking prey. The tentacles belong to something vast and ancient that dreams in the deep dark. Approaching risks madness or being dragged down."),
            biome = BiomeType.CAVE,
            gridX = 0,
            gridY = -7,
            connections = listOf(
                LocationConnection("whisper_corridor", Direction.UP),
                LocationConnection("shadow_pool", Direction.NORTH),
                LocationConnection("altar_of_the_deep_one", Direction.DOWN)
            ),
            encounterRate = 1.0,
            recommendedLevel = 19
        ),
        
        Location(
            id = "madness_chamber",
            name = "Madness Chamber",
            description = LocationDescription.simple("Reality breaks down here. Geometry doesn't work—impossible angles, spaces larger inside than outside, corridors looping back impossibly. Spending time damages sanity, the mind unable to process contradictions."),
            biome = BiomeType.CAVE,
            gridX = -1,
            gridY = -6,
            connections = listOf(
                LocationConnection("shadow_pool", Direction.EAST),
                LocationConnection("library_of_forbidden_knowledge", Direction.WEST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 18
        ),
        
        Location(
            id = "library_of_forbidden_knowledge",
            name = "Library of Forbidden Knowledge",
            description = LocationDescription.simple("An ancient library carved into rock, shelves filled with books in languages predating humanity. The books contain forbidden knowledge—truths that drive readers insane. Some whisper when unopened, trying to lure readers. Knowledge has prices paid in sanity."),
            biome = BiomeType.CAVE,
            gridX = -2,
            gridY = -6,
            connections = listOf(
                LocationConnection("madness_chamber", Direction.EAST),
                LocationConnection("hermit_sage_sanctuary", Direction.WEST),
                LocationConnection("altar_of_the_deep_one", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 18
        ),
        
        Location(
            id = "altar_of_the_deep_one",
            name = "Altar of the Deep One",
            description = LocationDescription.simple("An altar carved from black stone, dedicated to the entity in the tentacle pit. Covered in offerings—gems, bones, stranger things. The altar radiates malevolence. Using it might grant power but the cost would be terrible."),
            biome = BiomeType.CAVE,
            gridX = -1,
            gridY = -8,
            connections = listOf(
                LocationConnection("tentacle_pit", Direction.UP),
                LocationConnection("library_of_forbidden_knowledge", Direction.NORTH),
                LocationConnection("seal_chamber", Direction.SOUTH)
            ),
            encounterRate = 1.0,
            recommendedLevel = 19
        ),
        
        Location(
            id = "seal_chamber",
            name = "Seal Chamber",
            description = LocationDescription.simple("Magical seals designed to contain the Deep One. Ancient runes carved into floor, walls, ceiling form containment array. The seals glow weakly, power fading. If they fail, the Deep One will be free. A ticking countdown to apocalypse."),
            biome = BiomeType.CAVE,
            gridX = -2,
            gridY = -8,
            connections = listOf(
                LocationConnection("altar_of_the_deep_one", Direction.NORTH),
                LocationConnection("abyss_edge", Direction.DOWN)
            ),
            encounterRate = 0.90,
            recommendedLevel = 19
        ),
        
        Location(
            id = "abyss_edge",
            name = "Abyss Edge",
            description = LocationDescription.simple("The deepest accessible point—a ledge overlooking an abyss beyond light's reach. Cold air rises carrying sounds that aren't natural. The abyss is where the Deep One dwells, its presence felt as crushing psychic weight. Standing here means confronting existential insignificance."),
            biome = BiomeType.CAVE,
            gridX = -1,
            gridY = -9,
            connections = listOf(
                LocationConnection("seal_chamber", Direction.UP)
            ),
            encounterRate = 1.0,
            recommendedLevel = 20
        ),
        
        Location(
            id = "hermit_sage_sanctuary",
            name = "Hermit Sage Sanctuary",
            description = LocationDescription.simple("A hermit sage has made sanctuary in the deep dark seeking isolation and enlightenment. Ancient, possibly inhuman, possessing knowledge of the deep's secrets. Protected by wards keeping horrors at bay—a safe zone in the most dangerous region. Trades knowledge for rare items or services."),
            biome = BiomeType.CAVE,
            gridX = -3,
            gridY = -4,
            connections = listOf(
                LocationConnection("deep_dark", Direction.UP),
                LocationConnection("meditation_grotto", Direction.NORTH),
                LocationConnection("library_of_forbidden_knowledge", Direction.EAST)
            ),
            encounterRate = 0.30,
            recommendedLevel = 16,
            isSettlement = true,
            shopAvailable = true,
            isSafeZone = true
        ),
        
        Location(
            id = "blind_fish_lake",
            name = "Blind Fish Lake",
            description = LocationDescription.simple("An underground lake populated by eyeless, pale cave fish adapted to perpetual darkness. Something much larger lives in the depths—something with tentacles and too many eyes. The fish avoid it. You should too."),
            biome = BiomeType.CAVE,
            gridX = 1,
            gridY = -4,
            connections = listOf(
                LocationConnection("deep_dark", Direction.UP),
                LocationConnection("fungal_forest_deep", Direction.SOUTH),
                LocationConnection("drowning_tunnel", Direction.EAST)
            ),
            encounterRate = 0.90,
            recommendedLevel = 17
        ),
        
        Location(
            id = "drowning_tunnel",
            name = "Drowning Tunnel",
            description = LocationDescription.simple("A tunnel that floods periodically as underground water levels rise. Water marks show it floods to ceiling height. Being caught when water rises means drowning in darkness. Corpses of creatures that mistimed their crossing drift as warnings."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = -4,
            connections = listOf(
                LocationConnection("blind_fish_lake", Direction.WEST),
                LocationConnection("gas_pocket_chamber", Direction.UP)
            ),
            encounterRate = 0.95,
            recommendedLevel = 17
        ),
        
        Location(
            id = "fungal_forest_deep",
            name = "Fungal Forest",
            description = LocationDescription.simple("Giant bioluminescent fungi create an underground forest. Mushrooms the size of trees glow in impossible colors. The fungi digest organic matter including living creatures. Spore clouds cause hallucinations and respiratory damage. Beautiful and deadly."),
            biome = BiomeType.CAVE,
            gridX = 1,
            gridY = -5,
            connections = listOf(
                LocationConnection("blind_fish_lake", Direction.NORTH),
                LocationConnection("shadow_pool", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 16
        ),
        
        Location(
            id = "meditation_grotto",
            name = "Meditation Grotto",
            description = LocationDescription.simple("A small grotto with exceptional acoustics used by the hermit sage for meditation. Natural crystals hum at specific frequencies creating meditative soundscapes. A pocket of tranquility in hostile environment. Meditation here grants mental clarity and visions."),
            biome = BiomeType.CAVE,
            gridX = -4,
            gridY = -3,
            connections = listOf(
                LocationConnection("hermit_sage_sanctuary", Direction.SOUTH),
                LocationConnection("deep_dark", Direction.UP)
            ),
            encounterRate = 0.20,
            recommendedLevel = 15,
            isSafeZone = true
        ),
        
        Location(
            id = "echo_chamber_junction",
            name = "Echo Chamber Junction",
            description = LocationDescription.simple("A central junction where multiple tunnels converge creating complex echo patterns. Sounds from all connected passages overlap into acoustic chaos. Serves as navigation hub but constant noise makes communication difficult. You can hear events far away, echoes arriving from distant systems."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = -5,
            connections = listOf(
                LocationConnection("deep_dark", Direction.UP),
                LocationConnection("deep_shaft_junction", Direction.WEST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 15
        ),
        
        Location(
            id = "deep_shaft_junction",
            name = "Deep Shaft Junction",
            description = LocationDescription.simple("A vertical shaft connecting multiple cave levels with passages branching at different depths. Climbing allows access to various systems—crystal mines above, deep dark below, catacombs to the side. Falling means death. A critical navigation chokepoint."),
            biome = BiomeType.CAVE,
            gridX = 2,
            gridY = -4,
            connections = listOf(
                LocationConnection("crystal_dragon_lair", Direction.UP),
                LocationConnection("crystal_dragon_lair", Direction.NORTH),
                LocationConnection("gas_pocket_chamber", Direction.NORTH),
                LocationConnection("echo_chamber_junction", Direction.EAST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 14
        ),

        // ==================== SUB-REGION 8C: Catacombs Expansion (5 locations, levels 10-14) ====================
        
        Location(
            id = "cave_bone_maze",
            name = "Cave Bone Maze",
            description = LocationDescription.simple("Walls constructed entirely from human bones create a macabre labyrinth. Skulls stare from every surface. The maze protects deeper chambers, designed to confuse intruders. Skeletal guardians patrol, animating from the walls themselves."),
            biome = BiomeType.CAVE,
            gridX = -5,
            gridY = 0,
            connections = listOf(
                LocationConnection("forgotten_catacombs", Direction.UP),
                LocationConnection("ossuary_chapel", Direction.NORTH),
                LocationConnection("plague_victim_crypt", Direction.SOUTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 12
        ),
        
        Location(
            id = "ossuary_chapel",
            name = "Ossuary Chapel",
            description = LocationDescription.simple("A chapel where bones are arranged in artistic patterns—chandeliers of skulls, altars decorated with bone mosaics. Has eerie beauty, craftsmanship transforming macabre materials into art. Protected by ancient consecration keeping undead at bay."),
            biome = BiomeType.CAVE,
            gridX = -5,
            gridY = 1,
            connections = listOf(
                LocationConnection("cave_bone_maze", Direction.SOUTH),
                LocationConnection("forgotten_catacombs", Direction.UP)
            ),
            encounterRate = 0.40,
            recommendedLevel = 11,
            isSafeZone = true
        ),
        
        Location(
            id = "plague_victim_crypt",
            name = "Plague Victim Crypt",
            description = LocationDescription.simple("Mass graves for plague victims, hundreds per pit. Bodies buried in haste during outbreaks. Still carries disease risk—plague bacteria survive in corpses for centuries. Disturbing graves risks infection from ancient pathogens. Filled with restless spirits."),
            biome = BiomeType.CAVE,
            gridX = -5,
            gridY = -1,
            connections = listOf(
                LocationConnection("cave_bone_maze", Direction.NORTH),
                LocationConnection("cave_trap_gauntlet", Direction.SOUTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 13
        ),
        
        Location(
            id = "cave_trap_gauntlet",
            name = "Cave Trap Gauntlet",
            description = LocationDescription.simple("A corridor designed to kill intruders through elaborate traps—spike pits, falling blocks, poison darts, blade pendulums. Ancient engineers designed with lethal creativity. Some mechanical, others magical. Bodies of previous treasure hunters litter the corridor as warnings."),
            biome = BiomeType.CAVE,
            gridX = -5,
            gridY = -2,
            connections = listOf(
                LocationConnection("plague_victim_crypt", Direction.NORTH),
                LocationConnection("mummy_throne_room", Direction.SOUTH)
            ),
            encounterRate = 0.95,
            recommendedLevel = 14
        ),
        
        Location(
            id = "mummy_throne_room",
            name = "Mummy Throne Room",
            description = LocationDescription.simple("A mummified king sits upon his throne wearing gold mask and jeweled regalia. The room is filled with funerary goods meant for the afterlife. The mummy may be dormant or hostile depending on whether protective magics were disturbed. Final chamber containing greatest treasures and deadliest guardian."),
            biome = BiomeType.CAVE,
            gridX = -5,
            gridY = -3,
            connections = listOf(
                LocationConnection("cave_trap_gauntlet", Direction.NORTH)
            ),
            encounterRate = 1.0,
            recommendedLevel = 14
        )
    )
}
