package com.jalmarquest.shared.world.catalog

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationConnection
import com.jalmarquest.shared.world.LocationDescription

/**
 * TUNDRA region catalog - 50 new locations expanding the frozen northern wastes
 * Sub-regions: Southern Tundra (6A), Central Ice Fields (6B), Northern Wastes (6C),
 *              Western Aurora Lands (6D), Ice Caves (6E)
 * Connects to existing locations: frozen_waste, aurora_fields, icecrystal_cavern, frostgiant_lair
 */
internal val TUNDRA_LOCATIONS: List<Location> by lazy {
    listOf(
        // ==================== SUB-REGION 6A: Southern Tundra (15 locations, levels 12-15) ====================
        // Grid: X: 0 to 3, Y: 5 to 7
        // Theme: Transition from mountain to ice, sparse vegetation, increasing cold
        
        Location(
            id = "frost_bite_ridge",
            name = "Frostbite Ridge",
            description = LocationDescription.simple(
                "The temperature drops sharply on this exposed ridge. Wind cuts like knives, carrying ice crystals that sting exposed skin. Frostbite is a real danger—extremities go numb within minutes. The ridge offers a commanding view of the tundra stretching north, an endless white expanse. Sparse lichens cling to rocks, the only vegetation hardy enough to survive these conditions."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 5,
            connections = listOf(
                LocationConnection("frozen_waste", Direction.NORTH),
                LocationConnection("frozen_lake", Direction.NORTH),
                LocationConnection("erratic_boulder_field", Direction.WEST),
                LocationConnection("aurora_fields", Direction.WEST),
                LocationConnection("frostpeak", Direction.SOUTH),
                LocationConnection("permafrost_plain", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 12
        ),
        
        Location(
            id = "permafrost_plain",
            name = "Permafrost Plain",
            description = LocationDescription.simple(
                "The ground here is permanently frozen—permafrost extending hundreds of feet deep. Surface soil thaws briefly in summer, creating a thin active layer, but below remains solid ice year-round. Polygon patterns crack the surface where freezing and thawing create geometric shapes. Walking on permafrost is like walking on concrete—hard, unyielding, and utterly inhospitable."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 5,
            connections = listOf(
                LocationConnection("frost_bite_ridge", Direction.WEST),
                LocationConnection("tundra_wolf_den", Direction.NORTH),
                LocationConnection("mountain_temple", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 13,
            lore = "Permafrost contains frozen organic matter from thousands of years ago. Climate change is thawing permafrost globally, releasing ancient carbon and occasionally revealing perfectly preserved Ice Age animals."
        ),
        
        Location(
            id = "tundra_wolf_den",
            name = "Tundra Wolf Den",
            description = LocationDescription.simple(
                "A pack of arctic wolves has denned in a rocky outcrop. The wolves are massive—white-furred predators perfectly adapted to tundra life. Their den is surrounded by bones from kills: caribou, muskox, smaller prey. The wolves are intelligent pack hunters, coordinating attacks with frightening efficiency. From your perspective, each wolf is a dire beast, easily capable of killing you. Extreme caution required."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 6,
            connections = listOf(
                LocationConnection("permafrost_plain", Direction.SOUTH),
                LocationConnection("arctic_fox_den", Direction.EAST),
                LocationConnection("frozen_lake", Direction.WEST),
                LocationConnection("caribou_crossing", Direction.NORTH)
            ),
            encounterRate = 0.95,
            recommendedLevel = 14
        ),
        
        Location(
            id = "frozen_lake",
            name = "Frozen Lake",
            description = LocationDescription.withAllSeasons(
                spring = "Spring thaw weakens the ice dangerously. The surface appears solid but cracks spider-web beneath your feet. Water seeps through cracks, creating slush. Crossing now is treacherous—the ice could give way completely, plunging you into freezing water. Avoid the center where ice is thinnest.",
                summer = "The lake is ice-free in summer's brief warmth, its surface rippling with wind. The water is still frigid—barely above freezing. Waterfowl arrive to nest on small islands. Fish rise to feed on summer insects. The lake teems with life for a few precious weeks before winter returns.",
                autumn = "New ice forms at the edges, spreading inward nightly as temperatures plummet. The ice is thin—transparent sheets that reveal water beneath. Walking on new ice is suicide; it won't support weight. Within weeks, the lake will be solid again, locked in winter's grip.",
                winter = "Thick ice covers the lake—several feet of solid frozen water. The surface is snow-covered, creating a deceptively flat plain. Beneath the ice, fish swim in slow motion, conserving energy. The ice is strong enough to support incredible weight, creating a highway across the otherwise impassable lake."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 6,
            connections = listOf(
                LocationConnection("frost_bite_ridge", Direction.SOUTH),
                LocationConnection("lichen_field", Direction.WEST),
                LocationConnection("thermal_spring_oasis", Direction.EAST),
                LocationConnection("frozen_waste", Direction.WEST),
                LocationConnection("tundra_wolf_den", Direction.EAST),
                LocationConnection("ice_fishing_holes", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 13
        ),
        
        Location(
            id = "ice_fishing_holes",
            name = "Ice Fishing Holes",
            description = LocationDescription.simple(
                "Holes drilled through thick ice provide access to water below. Local peoples ice fish here, jigging for arctic char and trout. The holes freeze over quickly and must be constantly cleared. Peering into a hole reveals dark water below, occasionally illuminated by fish passing. The fishing community has established this as a semi-permanent camp during winter months."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 7,
            connections = listOf(
                LocationConnection("frozen_lake", Direction.SOUTH),
                LocationConnection("blizzard_zone", Direction.NORTH),
                LocationConnection("ice_fisher_camp", Direction.WEST),
                LocationConnection("caribou_crossing", Direction.EAST)
            ),
            encounterRate = 0.50,
            recommendedLevel = 12,
            isSettlement = true
        ),
        
        Location(
            id = "ice_fisher_camp",
            name = "Ice Fisher Camp",
            description = LocationDescription.simple(
                "A temporary settlement of ice fishers, their shelters huddled against the cold. Smoke rises from small stoves inside insulated tents. Fish hang frozen on racks—natural refrigeration. The fishers are tough, hardy folk who know tundra survival. They trade fish, warm clothing, and knowledge for supplies. This camp is one of civilization's furthest northern outposts, a tiny beacon of human presence in the frozen waste."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 7,
            connections = listOf(
                LocationConnection("aurora_fields", Direction.SOUTH),
                LocationConnection("lichen_field", Direction.SOUTH),
                LocationConnection("tundra_flower_bloom", Direction.WEST),
                LocationConnection("drum_dance_circle", Direction.WEST),
                LocationConnection("lemming_warren", Direction.NORTH),
                LocationConnection("ice_fishing_holes", Direction.EAST),
                LocationConnection("snow_drift_maze", Direction.NORTH),
                LocationConnection("aurora_veil", Direction.WEST)
            ),
            encounterRate = 0.25,
            recommendedLevel = 12,
            isSettlement = true,
            isSafeZone = true,
            shopAvailable = true
        ),
        
        Location(
            id = "caribou_crossing",
            name = "Caribou Crossing",
            description = LocationDescription.simple(
                "Migration routes of caribou herds cross here—thousands of animals moving between seasonal ranges. The caribou are enormous from your perspective, each one a moving mountain of muscle and antlers. The herd creates its own weather, body heat rising as mist. Predators follow the herd: wolves, bears, scavengers. The crossing is dangerous—being trampled by caribou hooves is a real risk."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 7,
            connections = listOf(
                LocationConnection("tundra_wolf_den", Direction.SOUTH),
                LocationConnection("arctic_fox_den", Direction.SOUTH),
                LocationConnection("ice_fishing_holes", Direction.WEST),
                LocationConnection("muskox_territory", Direction.NORTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 13,
            lore = "Caribou migrate farther than any land mammal—some herds travel over 3,000 miles annually. They navigate using the sun, landmarks, and possibly Earth's magnetic field, following routes established over millennia."
        ),
        
        Location(
            id = "lichen_field",
            name = "Lichen Field",
            description = LocationDescription.simple(
                "Rocks covered in vibrant lichens—orange, yellow, green, and gray. Lichens are composite organisms: fungus and algae living symbiotically. They're incredibly hardy, surviving conditions that kill other life. The lichens are also incredibly slow-growing—some patches here may be centuries old. Caribou graze on lichens, scraping them from rocks with specialized teeth."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 6,
            connections = listOf(
                LocationConnection("frozen_lake", Direction.EAST),
                LocationConnection("ptarmigan_nesting_ground", Direction.WEST),
                LocationConnection("aurora_fields", Direction.SOUTH),
                LocationConnection("ice_fisher_camp", Direction.NORTH)
            ),
            encounterRate = 0.55,
            recommendedLevel = 12
        ),
        
        Location(
            id = "ptarmigan_nesting_ground",
            name = "Ptarmigan Nesting Ground",
            description = LocationDescription.simple(
                "Ptarmigan—arctic grouse—nest on the ground in shallow depressions. The birds change color seasonally: white in winter for camouflage against snow, mottled brown in summer to blend with tundra. Their nests are nearly impossible to spot until you're almost stepping on them. The birds explode into flight when startled, their sudden movement terrifying at close range."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 6,
            connections = listOf(
                LocationConnection("aurora_fields", Direction.SOUTH),
                LocationConnection("wind_scour_zone", Direction.SOUTH),
                LocationConnection("tundra_flower_bloom", Direction.NORTH),
                LocationConnection("drum_dance_circle", Direction.NORTH),
                LocationConnection("lichen_field", Direction.EAST),
                LocationConnection("aurora_veil", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 12
        ),
        
        Location(
            id = "erratic_boulder_field",
            name = "Erratic Boulder Field",
            description = LocationDescription.simple(
                "Massive boulders scattered randomly across tundra—glacial erratics deposited when ancient glaciers melted. Each boulder is out of place, different rock types than local bedrock, transported from hundreds of miles away by ice. The erratics provide shelter from wind and predators. They're also landmarks in otherwise featureless tundra, navigation points that prevent getting lost."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 5,
            connections = listOf(
                LocationConnection("aurora_fields", Direction.NORTH),
                LocationConnection("frost_bite_ridge", Direction.EAST),
                LocationConnection("mountain_temple", Direction.SOUTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 13
        ),
        
        Location(
            id = "muskox_territory",
            name = "Muskox Territory",
            description = LocationDescription.simple(
                "Muskoxen graze here in defensive formations. When threatened, they form circles with calves in the center, adults facing outward with horns ready. Each muskox is a shaggy fortress—long fur draping to the ground, curved horns capable of goring predators. From your scale, they're living tanks, impervious and deadly. Avoiding them is wise; challenging them is suicide."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 8,
            connections = listOf(
                LocationConnection("caribou_crossing", Direction.SOUTH),
                LocationConnection("frozen_waterfall", Direction.SOUTH),
                LocationConnection("snow_bridge_pass", Direction.NORTH),
                LocationConnection("blizzard_zone", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 14,
            lore = "Muskoxen survived the Ice Age and remain virtually unchanged for 200,000 years. Their defensive circle tactic worked against wolves and bears but failed against human hunters with rifles, nearly driving them to extinction."
        ),
        
        Location(
            id = "tundra_flower_bloom",
            name = "Tundra Flower Bloom",
            description = LocationDescription.simple(
                "In summer's brief warmth, wildflowers explode across tundra—a carpet of purple saxifrage, yellow arctic poppies, white mountain avens. The flowers are tiny but incredibly numerous, transforming barren ground into a colorful garden. They bloom, pollinate, and set seed within weeks, racing to complete their life cycle before frost returns. The bloom attracts pollinators in vast numbers: bees, flies, butterflies."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("ptarmigan_nesting_ground", Direction.SOUTH),
                LocationConnection("hoarfrost_forest", Direction.NORTH),
                LocationConnection("ice_fisher_camp", Direction.EAST),
                LocationConnection("aurora_veil", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 12
        ),
        
        Location(
            id = "wind_scour_zone",
            name = "Wind Scour Zone",
            description = LocationDescription.simple(
                "Wind blows constantly here, scouring snow from exposed rock. The ground is bare, windswept clean of anything loose. Wind speed is terrifying—strong enough to knock you over, to steal breath from lungs. Nothing grows in the wind scour zone; nothing can survive constant battering. This is tundra at its most hostile, beauty stripped to raw survival challenge."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 5,
            connections = listOf(
                LocationConnection("aurora_fields", Direction.EAST),
                LocationConnection("ptarmigan_nesting_ground", Direction.NORTH),
                LocationConnection("ancient_tree_heart", Direction.SOUTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 13
        ),
        
        Location(
            id = "snow_bridge_pass",
            name = "Snow Bridge Pass",
            description = LocationDescription.simple(
                "A narrow pass between ice formations, bridged by compacted snow. The bridge is strong when frozen but weakens in warmer temperatures. Crossing requires trusting the snow's integrity—below is a deep crevasse that would be fatal to fall into. The pass is the easiest route north but also dangerous, requiring perfect timing and careful weight distribution."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 9,
            connections = listOf(
                LocationConnection("muskox_territory", Direction.SOUTH),
                LocationConnection("sastrugi_field", Direction.NORTH),
                LocationConnection("blizzard_zone", Direction.WEST),
                LocationConnection("ice_spike_field", Direction.NORTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 14
        ),
        
        Location(
            id = "lemming_warren",
            name = "Lemming Warren",
            description = LocationDescription.simple(
                "Lemmings burrow beneath snow and tundra, creating extensive tunnel networks. The small rodents are cyclic—some years they're abundant, other years scarce. When populations peak, lemmings are everywhere, their tunnels honeycomb the ground. They're prey for everything: foxes, owls, weasels, hawks. The warren is alive with squeaking, rustling, and the constant scurrying of tiny feet."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 8,
            connections = listOf(
                LocationConnection("ice_fisher_camp", Direction.SOUTH),
                LocationConnection("snow_drift_maze", Direction.NORTH),
                LocationConnection("snowy_owl_roost", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 12
        ),

        // ==================== SUB-REGION 6B: Central Ice Fields (15 locations, levels 13-16) ====================
        // Grid: X: 1 to 4, Y: 6 to 9
        // Theme: Endless white expanse, whiteout conditions, extreme survival challenge
        
        Location(
            id = "blizzard_zone",
            name = "Blizzard Zone",
            description = LocationDescription.simple(
                "Blizzards here are near-constant—wind-driven snow creating zero visibility. The world disappears into white chaos. Sound is muffled, direction becomes meaningless. Getting lost is immediate and potentially fatal. The blizzard is beautiful and terrifying, nature at her most indifferent to survival. Waiting it out is the only safe option, but blizzards can last days."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 8,
            connections = listOf(
                LocationConnection("muskox_territory", Direction.EAST),
                LocationConnection("sastrugi_field", Direction.NORTH),
                LocationConnection("snowy_owl_roost", Direction.NORTH),
                LocationConnection("snow_bridge_pass", Direction.EAST),
                LocationConnection("ice_fishing_holes", Direction.SOUTH),
                LocationConnection("snow_drift_maze", Direction.WEST)
            ),
            encounterRate = 0.85,
            recommendedLevel = 15
        ),
        
        Location(
            id = "snow_drift_maze",
            name = "Snow Drift Maze",
            description = LocationDescription.simple(
                "Wind sculpts snow into towering drifts—some reaching heights of multiple body-lengths. The drifts create a maze that changes with each storm. Paths that existed yesterday are blocked today; new routes appear as old ones vanish. The drifts are both obstacle and shelter, deadly barrier and life-saving windbreak. Navigation requires constant adaptation."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 9,
            connections = listOf(
                LocationConnection("ice_fisher_camp", Direction.SOUTH),
                LocationConnection("hoarfrost_forest", Direction.WEST),
                LocationConnection("ice_fog_bank", Direction.NORTH),
                LocationConnection("snowy_owl_roost", Direction.EAST),
                LocationConnection("lemming_warren", Direction.SOUTH),
                LocationConnection("blizzard_zone", Direction.EAST),
                LocationConnection("aurora_veil", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 14
        ),
        
        Location(
            id = "ice_spike_field",
            name = "Ice Spike Field",
            description = LocationDescription.simple(
                "Bizarre ice formations rise from the ground—vertical spikes and blades of ice reaching upward. The spikes form through sublimation and wind erosion, ice carved into alien shapes. They're beautiful and deadly, each spike sharp enough to impale. Navigating the field requires extreme care; falling onto spikes would be catastrophic. The spikes catch sunlight, creating prismatic displays."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 9,
            connections = listOf(
                LocationConnection("snow_bridge_pass", Direction.SOUTH),
                LocationConnection("frozen_waterfall", Direction.SOUTH),
                LocationConnection("polar_bear_territory", Direction.NORTH),
                LocationConnection("sastrugi_field", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 15,
            lore = "Ice spikes called 'penitentes' form in high-altitude dry environments. Sunlight causes differential melting and sublimation, creating spikes that can reach several meters tall. They're found on Earth and have been observed on Europa, Jupiter's ice moon."
        ),
        
        Location(
            id = "polar_bear_territory",
            name = "Polar Bear Territory",
            description = LocationDescription.simple(
                "The apex predator of the Arctic hunts here. Polar bears are perfectly adapted killers—white fur for camouflage, powerful limbs, incredible sense of smell. From your perspective, a polar bear is an unstoppable death machine, capable of detecting prey from miles away. The territory is marked by tracks, kills, and the ever-present danger that a bear might be watching right now."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 10,
            connections = listOf(
                LocationConnection("ice_spike_field", Direction.SOUTH),
                LocationConnection("pressure_ridge_maze", Direction.WEST),
                LocationConnection("seal_breathing_holes", Direction.NORTH)
            ),
            encounterRate = 0.95,
            recommendedLevel = 16
        ),
        
        Location(
            id = "sastrugi_field",
            name = "Sastrugi Field",
            description = LocationDescription.simple(
                "Wind-carved snow forms sastrugi—parallel ridges like frozen waves. The ridges make travel difficult, forcing constant climbing over hard-packed crests and dropping into troughs. Sastrugi indicate prevailing wind direction, a navigation aid for those who can read them. The field is monotonous and exhausting, testing endurance as much as navigational skill."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 9,
            connections = listOf(
                LocationConnection("snow_bridge_pass", Direction.SOUTH),
                LocationConnection("ice_spike_field", Direction.EAST),
                LocationConnection("blizzard_zone", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 14
        ),
        
        Location(
            id = "snowy_owl_roost",
            name = "Snowy Owl Roost",
            description = LocationDescription.simple(
                "Snowy owls perch on ice formations, their white plumage perfect camouflage. The owls are silent hunters, swooping down on lemmings and other prey with deadly precision. Each owl is magnificent—yellow eyes tracking movement, talons capable of crushing bones. From your perspective, they're aerial predators of terrifying efficiency. When owls hunt, nothing is safe."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 9,
            connections = listOf(
                LocationConnection("lemming_warren", Direction.WEST),
                LocationConnection("blizzard_zone", Direction.SOUTH),
                LocationConnection("snow_drift_maze", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 13
        ),
        
        Location(
            id = "ice_fog_bank",
            name = "Ice Fog Bank",
            description = LocationDescription.simple(
                "Supercooled water droplets suspended in air create ice fog—a phenomenon of extreme cold. The fog is dense, reducing visibility to arm's length. Ice crystals form on everything, coating fur, whiskers, and equipment. Breathing the fog causes ice to form in nostrils and lungs, making each breath painful. The fog is beautiful—glittering crystals suspended—but deadly to linger in."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 10,
            connections = listOf(
                LocationConnection("snow_drift_maze", Direction.SOUTH),
                LocationConnection("sky_walker_plateau", Direction.WEST),
                LocationConnection("spirit_ice_field", Direction.WEST),
                LocationConnection("giant_ice_quarry", Direction.NORTH),
                LocationConnection("sun_dog_vista", Direction.NORTH),
                LocationConnection("aurora_veil", Direction.WEST),
                LocationConnection("pressure_ridge_maze", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 14
        ),
        
        Location(
            id = "pressure_ridge_maze",
            name = "Pressure Ridge Maze",
            description = LocationDescription.simple(
                "Ice sheets colliding create pressure ridges—jumbled walls of broken ice thrust upward. The ridges form maze-like barriers, impassable walls requiring navigation around or climbing over. The ice is unstable, blocks shifting unpredictably. Between ridges, flat ice provides brief respite before the next barrier. The maze tests navigation, endurance, and nerve."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 10,
            connections = listOf(
                LocationConnection("ice_fog_bank", Direction.WEST),
                LocationConnection("sun_dog_vista", Direction.WEST),
                LocationConnection("crevasse_field", Direction.NORTH),
                LocationConnection("polar_bear_territory", Direction.EAST),
                LocationConnection("glacier_terminus", Direction.NORTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 15
        ),
        
        Location(
            id = "sun_dog_vista",
            name = "Sun Dog Vista",
            description = LocationDescription.simple(
                "Ice crystals in the atmosphere create sun dogs—bright spots flanking the sun, connected by a halo. The optical phenomenon is spectacular, turning the sky into a display of light and color. Sun dogs indicate extreme cold—the ice crystals forming only in specific temperature ranges. The vista provides a moment of beauty in the harsh tundra, a reminder that even ice can create art."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 10,
            connections = listOf(
                LocationConnection("ice_fog_bank", Direction.SOUTH),
                LocationConnection("pressure_ridge_maze", Direction.EAST),
                LocationConnection("glacier_terminus", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 14,
            lore = "Sun dogs (parhelia) form when sunlight refracts through hexagonal ice crystals aligned vertically. They appear 22 degrees from the sun and are accompanied by halos, light pillars, and other atmospheric optics during extreme cold."
        ),
        
        Location(
            id = "frozen_waterfall",
            name = "Frozen Waterfall",
            description = LocationDescription.simple(
                "A waterfall frozen mid-cascade, its flow halted by winter's grip. The ice forms spectacular columns and curtains, blue-white and translucent. Beneath the frozen surface, water still flows—you can hear it trickling. The waterfall will thaw in spring, resuming its flow, but now it's a frozen monument, a sculpture of ice displaying winter's power."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 8,
            connections = listOf(
                LocationConnection("muskox_territory", Direction.NORTH),
                LocationConnection("glacier_tunnel", Direction.DOWN),
                LocationConnection("arctic_fox_den", Direction.SOUTH),
                LocationConnection("ice_spike_field", Direction.NORTH),
                LocationConnection("icecrystal_cavern", Direction.DOWN)
            ),
            encounterRate = 0.65,
            recommendedLevel = 13
        ),
        
        Location(
            id = "hoarfrost_forest",
            name = "Hoarfrost Forest",
            description = LocationDescription.simple(
                "Stunted trees at tree line are completely encased in hoarfrost—delicate ice crystals coating every branch and needle. The forest glitters in sunlight, transformed into crystal sculptures. The hoarfrost is fragile, shattering at the slightest touch and releasing cascades of ice. Walking through the forest is like moving through a frozen dream, beautiful and otherworldly."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("tundra_flower_bloom", Direction.SOUTH),
                LocationConnection("medicine_wheel", Direction.NORTH),
                LocationConnection("aurora_veil", Direction.NORTH),
                LocationConnection("snow_drift_maze", Direction.EAST)
            ),
            encounterRate = 0.60,
            recommendedLevel = 13
        ),
        
        Location(
            id = "crevasse_field",
            name = "Crevasse Field",
            description = LocationDescription.simple(
                "Deep cracks split the ice—crevasses hidden beneath snow bridges. Each crack is a trap, potentially bottomless, capable of swallowing the unwary. Snow covers crevasses, making them invisible until you step through. Testing each step with a pole is essential. The field is treacherous, requiring constant vigilance and willingness to turn back when routes become too dangerous."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 11,
            connections = listOf(
                LocationConnection("pressure_ridge_maze", Direction.SOUTH),
                LocationConnection("blubber_rendering_platform", Direction.NORTHEAST),
                LocationConnection("mammoth_graveyard", Direction.NORTH),
                LocationConnection("seal_breathing_holes", Direction.NORTHEAST),
                LocationConnection("glacier_terminus", Direction.WEST),
                LocationConnection("ice_fortress_outer_wall", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 15
        ),
        
        Location(
            id = "arctic_fox_den",
            name = "Arctic Fox Den",
            description = LocationDescription.simple(
                "Arctic foxes den in snowbanks, their burrows insulated against cold. The foxes are small but fierce, scavengers and hunters who follow polar bears to clean up kills. Their fur is thick and white in winter, providing perfect camouflage. From your perspective, even a 'small' fox is formidable. The den is surrounded by cached food—lemmings, birds, fish—frozen solid and stored for lean times."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 7,
            connections = listOf(
                LocationConnection("caribou_crossing", Direction.NORTH),
                LocationConnection("thermal_spring_oasis", Direction.SOUTH),
                LocationConnection("frozen_waterfall", Direction.NORTH),
                LocationConnection("tundra_wolf_den", Direction.WEST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 13
        ),
        
        Location(
            id = "thermal_spring_oasis",
            name = "Thermal Spring Oasis",
            description = LocationDescription.simple(
                "A geothermal spring creates an oasis of relative warmth in the frozen tundra. Steam rises from unfrozen water, creating a microclimate where plants grow year-round. Animals congregate here, seeking warmth and unfrozen water. The spring is a rare safe zone, a gift of geothermal activity. Bathing in the spring provides relief from cold, though the contrast makes returning to tundra temperatures brutal."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 6,
            connections = listOf(
                LocationConnection("frozen_lake", Direction.WEST),
                LocationConnection("arctic_fox_den", Direction.NORTH),
                LocationConnection("icecrystal_cavern", Direction.DOWN)
            ),
            encounterRate = 0.40,
            recommendedLevel = 13,
            isSafeZone = true,
            lore = "Geothermal springs in arctic regions create unique ecosystems. Plants and animals not found elsewhere thrive in these thermal oases. Iceland's hot springs and Yellowstone's geysers demonstrate geothermal activity's power to create life in hostile environments."
        ),
        
        Location(
            id = "seal_breathing_holes",
            name = "Seal Breathing Holes",
            description = LocationDescription.simple(
                "Seals maintain breathing holes through thick ice, returning to them throughout the day. Polar bears know this and hunt seals at their holes, waiting motionless for hours. The holes are life and death—seals need them to breathe, bears need them to eat. Observing a hole means potentially witnessing brutal predation, nature's survival calculus laid bare on white ice."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 11,
            connections = listOf(
                LocationConnection("polar_bear_territory", Direction.SOUTH),
                LocationConnection("crevasse_field", Direction.SOUTHWEST),
                LocationConnection("pack_ice_edge", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 15
        ),

        // ==================== SUB-REGION 6C: Northern Wastes (10 locations, levels 15-18) ====================
        // Grid: X: 1 to 3, Y: 8 to 12
        // Theme: Extreme cold, frostgiant civilization, endgame difficulty
        
        Location(
            id = "ice_fortress_outer_wall",
            name = "Ice Fortress Outer Wall",
            description = LocationDescription.simple(
                "The frost giants have built fortifications from ice blocks—massive walls extending far overhead. The architecture is crude but effective, ice quarried and stacked into defensive barriers. Giant-sized gates punctuate the walls, each one a passageway you could walk through without bending. Guards patrol—frost giants in armor, carrying weapons scaled to their size. Approaching openly is suicidal."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 11,
            connections = listOf(
                LocationConnection("crevasse_field", Direction.SOUTH),
                LocationConnection("giant_ice_quarry", Direction.WEST),
                LocationConnection("blubber_rendering_platform", Direction.EAST),
                LocationConnection("glacier_terminus", Direction.SOUTH),
                LocationConnection("frostgiant_lair", Direction.NORTH),
                LocationConnection("frozen_throne_approach", Direction.NORTH)
            ),
            encounterRate = 0.90,
            recommendedLevel = 16
        ),
        
        Location(
            id = "frozen_throne_approach",
            name = "Frozen Throne Approach",
            description = LocationDescription.simple(
                "A grand processional avenue leads to the frost giant king's throne. Ice sculptures line the route—depictions of giant heroes, conquered foes, mythical beasts. The avenue is maintained perfectly, swept clear of snow. Approaching the throne uninvited is forbidden, punishable by death. The avenue radiates power and menace, a reminder that giants rule here and lesser beings exist at their sufferance."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 12,
            connections = listOf(
                LocationConnection("ice_fortress_outer_wall", Direction.SOUTH),
                LocationConnection("ancient_ice_core", Direction.NORTHEAST),
                LocationConnection("blubber_rendering_platform", Direction.SOUTH),
                LocationConnection("mammoth_graveyard", Direction.EAST),
                LocationConnection("frostgiant_lair", Direction.WEST),
                LocationConnection("throne_room_frozen", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 17
        ),
        
        Location(
            id = "throne_room_frozen",
            name = "Frozen Throne Room",
            description = LocationDescription.simple(
                "The frost giant king sits on a throne of solid ice, carved with runes of power and dominion. The king is massive even by giant standards—a primordial force made flesh and ice. His crown is frozen water, his cloak polar bear fur, his scepter a carved icicle capable of channeling devastating magic. This is the heart of frost giant power, where the king rules with absolute authority."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 13,
            connections = listOf(
                LocationConnection("frozen_throne_approach", Direction.SOUTH),
                LocationConnection("ancient_ice_core", Direction.EAST)
            ),
            encounterRate = 0.95,
            recommendedLevel = 18,
            lore = "Frost giants in Norse mythology were primordial beings, enemies of the gods. Ymir, the first frost giant, was killed by Odin and his brothers, who used Ymir's body to create the world—bones becoming mountains, blood becoming oceans, skull becoming sky."
        ),
        
        Location(
            id = "mammoth_graveyard",
            name = "Mammoth Graveyard",
            description = LocationDescription.simple(
                "Ancient mammoth remains litter this valley—bones and tusks from dozens of mammoths. Some died naturally, others were hunted. The bones are massive, reminders that even larger creatures once roamed here. Frost giants harvest ivory from the graveyard, carving tusks into art and tools. The graveyard is sacred to giants, a place where death is respected and remembered."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 12,
            connections = listOf(
                LocationConnection("frozen_throne_approach", Direction.WEST),
                LocationConnection("crevasse_field", Direction.SOUTH),
                LocationConnection("pack_ice_edge", Direction.EAST)
            ),
            encounterRate = 0.70,
            recommendedLevel = 16,
            lore = "Woolly mammoths survived until about 4,000 years ago on isolated Arctic islands, long after mainland populations died out. Some were so well-preserved in permafrost that their flesh was still edible millennia later."
        ),
        
        Location(
            id = "glacier_terminus",
            name = "Glacier Terminus",
            description = LocationDescription.simple(
                "Where the glacier ends, ice calves into the sea in spectacular fashion. Massive chunks break free, crashing into water with thunderous booms. The terminus is constantly changing, ice advancing in winter and retreating in summer. The calving creates icebergs, floating ice mountains that drift on currents. Standing near the terminus means risking being crushed by falling ice—beautiful and deadly."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 11,
            connections = listOf(
                LocationConnection("pressure_ridge_maze", Direction.SOUTH),
                LocationConnection("sun_dog_vista", Direction.SOUTH),
                LocationConnection("crevasse_field", Direction.EAST),
                LocationConnection("ice_fortress_outer_wall", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 15
        ),
        
        Location(
            id = "pack_ice_edge",
            name = "Pack Ice Edge",
            description = LocationDescription.simple(
                "Where solid ice meets open ocean, pack ice floats in shifting plates. The ice moves with currents and wind, grinding and colliding. Leads of open water appear and close unpredictably. Seals and whales surface in the leads. The pack ice edge is the most productive arctic ecosystem—where ice meets water, life flourishes. It's also treacherous, the ice constantly shifting."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 4,
            gridY = 12,
            connections = listOf(
                LocationConnection("seal_breathing_holes", Direction.SOUTH),
                LocationConnection("mammoth_graveyard", Direction.WEST)
            ),
            encounterRate = 0.80,
            recommendedLevel = 16
        ),
        
        Location(
            id = "giant_ice_quarry",
            name = "Giant Ice Quarry",
            description = LocationDescription.simple(
                "Frost giants mine ice here, cutting massive blocks for construction. The quarry is a canyon carved into glacial ice, walls displaying layers of ancient snowfall. Giants work with specialized tools—ice saws and picks scaled to their size. The quarry floor is littered with ice chips and shavings, each shard reflecting light. Watching giants work is mesmerizing and terrifying."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 11,
            connections = listOf(
                LocationConnection("ice_fortress_outer_wall", Direction.EAST),
                LocationConnection("aurora_observatory", Direction.NORTH),
                LocationConnection("ice_fog_bank", Direction.SOUTH),
                LocationConnection("frostgiant_lair", Direction.NORTH)
            ),
            encounterRate = 0.85,
            recommendedLevel = 16
        ),
        
        Location(
            id = "aurora_observatory",
            name = "Aurora Observatory",
            description = LocationDescription.simple(
                "Frost giant shamans study the aurora from this elevated platform. They believe the lights carry messages from ancient gods, prophecies written in colored fire. The observatory is open to the sky, offering unobstructed views of the northern lights. Shamans record patterns, interpreting omens and predictions. The observatory hums with magical energy, a nexus where earthly and celestial powers meet."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 12,
            connections = listOf(
                LocationConnection("giant_ice_quarry", Direction.SOUTH),
                LocationConnection("spirit_ice_field", Direction.WEST),
                LocationConnection("frostgiant_lair", Direction.EAST),
                LocationConnection("sky_walker_plateau", Direction.WEST)
            ),
            encounterRate = 0.75,
            recommendedLevel = 17
        ),
        
        Location(
            id = "ancient_ice_core",
            name = "Ancient Ice Core",
            description = LocationDescription.simple(
                "Deep ice contains layers from thousands of years—a frozen timeline of Earth's climate history. Bubbles trapped in ice hold ancient atmosphere. Dust layers mark volcanic eruptions. The core is a scientific treasure, but giants view it as sacred—memory of the world made solid. Extracting ice from the core requires permission and respect, lest you offend powers far greater than yourself."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 13,
            connections = listOf(
                LocationConnection("throne_room_frozen", Direction.WEST),
                LocationConnection("frozen_throne_approach", Direction.SOUTHWEST),
                LocationConnection("icecrystal_cavern", Direction.DOWN)
            ),
            encounterRate = 0.70,
            recommendedLevel = 17,
            lore = "Ice cores from Antarctica and Greenland provide climate records extending 800,000+ years. Analyzing ice reveals past temperatures, atmospheric composition, volcanic activity, and even cosmic dust from supernovae."
        ),
        
        Location(
            id = "blubber_rendering_platform",
            name = "Blubber Rendering Platform",
            description = LocationDescription.simple(
                "Giants process whale and seal blubber here, rendering fat into oil for lamps and heating. The smell is overpowering—rancid and fishy, permeating everything. Massive vats bubble over fires, blubber melting into oil. Bones are separated and used for construction and tools. The platform demonstrates giant efficiency—nothing is wasted, every part utilized."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 3,
            gridY = 11,
            connections = listOf(
                LocationConnection("ice_fortress_outer_wall", Direction.WEST),
                LocationConnection("crevasse_field", Direction.SOUTHWEST),
                LocationConnection("frozen_throne_approach", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 16
        ),

        // ==================== SUB-REGION 6D: Western Aurora Lands (5 locations, levels 12-14) ====================
        // Grid: X: -1 to 1, Y: 6 to 8
        // Theme: Magical northern lights, mystical, shamanic traditions
        
        Location(
            id = "aurora_veil",
            name = "Aurora Veil",
            description = LocationDescription.withAllSeasons(
                spring = "Spring auroras are delicate—pale greens and pinks dancing across twilight skies. The lights seem shy, appearing briefly then fading. They reflect the season's tentative warmth, winter's grip loosening. Watching auroras in spring feels hopeful, a promise that darkness will yield to light.",
                summer = "Summer's midnight sun prevents aurora viewing—the sky never darkens enough. But on rare cloudy nights or as summer wanes, faint auroras appear against dim skies. They're ghostly, barely visible, magic hiding in plain sight.",
                autumn = "Autumn brings the best auroras—long nights and clear skies create perfect viewing conditions. The lights explode across the sky in curtains and spirals, greens and purples and reds. They pulse and shimmer, alive with energy. Watching auroras in autumn is transcendent, connecting you to cosmic forces beyond comprehension.",
                winter = "Winter auroras are brilliant against pitch-black skies. The lights create enough illumination to navigate by, casting green and purple shadows. They crackle and hiss—electromagnetic interference audible to sensitive ears. Winter auroras feel almost tangible, close enough to touch."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 8,
            connections = listOf(
                LocationConnection("hoarfrost_forest", Direction.SOUTH),
                LocationConnection("ice_fisher_camp", Direction.EAST),
                LocationConnection("medicine_wheel", Direction.WEST),
                LocationConnection("drum_dance_circle", Direction.SOUTH),
                LocationConnection("tundra_flower_bloom", Direction.SOUTH),
                LocationConnection("ptarmigan_nesting_ground", Direction.SOUTH),
                LocationConnection("snow_drift_maze", Direction.EAST),
                LocationConnection("ice_fog_bank", Direction.EAST),
                LocationConnection("sky_walker_plateau", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 13,
            isSafeZone = true
        ),
        
        Location(
            id = "sky_walker_plateau",
            name = "Sky Walker Plateau",
            description = LocationDescription.simple(
                "Shamans come here to commune with aurora spirits, performing rituals beneath dancing lights. The plateau is sacred—marked by stone cairns and offerings. Shamans believe the auroras are pathways to spirit realms, bridges between worlds. They enter trances, spirits walking celestial paths. Whether this is genuine mysticism or cultural belief, the plateau radiates power that even skeptics feel."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 9,
            connections = listOf(
                LocationConnection("aurora_veil", Direction.SOUTH),
                LocationConnection("medicine_wheel", Direction.SOUTH),
                LocationConnection("ice_fog_bank", Direction.EAST),
                LocationConnection("aurora_observatory", Direction.EAST),
                LocationConnection("spirit_ice_field", Direction.NORTH)
            ),
            encounterRate = 0.60,
            recommendedLevel = 14,
            lore = "Indigenous Arctic peoples have rich aurora traditions. Some believe they're spirits of the dead, others see them as omens, still others as playful forces. Modern science explains auroras as solar wind interacting with Earth's magnetic field—poetry and physics describing the same wonder."
        ),
        
        Location(
            id = "spirit_ice_field",
            name = "Spirit Ice Field",
            description = LocationDescription.simple(
                "Ice here glows faintly even without auroras—bioluminescent bacteria or minerals creating eerie illumination. The ice is considered sacred, inhabited by spirits. Shamans harvest it for rituals, believing it contains captured aurora energy. The field is beautiful and unsettling, ice that seems alive, responding to presence with subtle changes in luminescence."
            ),
            biome = BiomeType.TUNDRA,
            gridX = -1,
            gridY = 10,
            connections = listOf(
                LocationConnection("sky_walker_plateau", Direction.SOUTH),
                LocationConnection("ice_fog_bank", Direction.EAST),
                LocationConnection("aurora_observatory", Direction.EAST)
            ),
            encounterRate = 0.65,
            recommendedLevel = 14
        ),
        
        Location(
            id = "drum_dance_circle",
            name = "Drum Dance Circle",
            description = LocationDescription.simple(
                "A ceremonial site where shamanic drum dances occur. Stones arranged in a circle mark the boundaries. During ceremonies, drumming creates rhythmic thunder, dancers moving in trance states. The dances invoke spirits, seek visions, and maintain cosmic balance. Witnessing a ceremony is profound—ancient traditions continuing despite modernity, human connection to the sacred surviving millennia."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 0,
            gridY = 7,
            connections = listOf(
                LocationConnection("aurora_veil", Direction.NORTH),
                LocationConnection("ptarmigan_nesting_ground", Direction.SOUTH),
                LocationConnection("ice_fisher_camp", Direction.EAST)
            ),
            encounterRate = 0.55,
            recommendedLevel = 12
        ),
        
        Location(
            id = "medicine_wheel",
            name = "Medicine Wheel",
            description = LocationDescription.simple(
                "Stones arranged in a wheel pattern—a sacred geometry used for ceremony and astronomy. The wheel aligns with cardinal directions and celestial events: solstices, equinoxes, star positions. It's both calendar and temple, practical astronomy and spiritual practice intertwined. The wheel is ancient, stones placed by peoples whose names are forgotten but whose knowledge endures in stone."
            ),
            biome = BiomeType.TUNDRA,
            gridX = -1,
            gridY = 8,
            connections = listOf(
                LocationConnection("aurora_veil", Direction.EAST),
                LocationConnection("hoarfrost_forest", Direction.SOUTH),
                LocationConnection("sky_walker_plateau", Direction.NORTH)
            ),
            encounterRate = 0.50,
            recommendedLevel = 13,
            isSafeZone = true,
            lore = "Medicine wheels are found across North America, stone structures dating back thousands of years. They served astronomical, ceremonial, and possibly navigational purposes. Many align with solar and stellar events, demonstrating sophisticated astronomical knowledge."
        ),

        // ==================== SUB-REGION 6E: Ice Caves (5 locations, levels 14-17) ====================
        // Grid: Multi-level underground via DOWN connections
        // Theme: Subglacial passages, frozen beauty, extreme danger
        
        Location(
            id = "glacier_tunnel",
            name = "Glacier Tunnel",
            description = LocationDescription.simple(
                "A tunnel carved through glacial ice by meltwater. The walls are smooth blue ice, translucent and glowing with captured light. The tunnel shifts constantly as ice moves, passages opening and closing with glacial flow. Water trickles along the floor, sometimes freezing, sometimes flowing. The tunnel is beautiful—a corridor of blue light—but unstable, capable of collapsing and crushing anyone inside."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 7,
            connections = listOf(
                LocationConnection("icecrystal_cavern", Direction.SOUTH),
                LocationConnection("frozen_waterfall", Direction.UP),
                LocationConnection("ice_cathedral_tundra", Direction.NORTH)
            ),
            encounterRate = 0.75,
            recommendedLevel = 14
        ),
        
        Location(
            id = "ice_cathedral_tundra",
            name = "Tundra Ice Cathedral",
            description = LocationDescription.simple(
                "A vast cavern within the glacier, its ceiling soaring overhead like a cathedral dome. Ice formations create natural columns and arches, architecture carved by water and time. Light filters through ice, creating blue-green illumination. The cathedral is silent except for occasional ice creaks and water drips. It's a natural temple, a space that inspires awe and reverence without any human modification."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 8,
            connections = listOf(
                LocationConnection("glacier_tunnel", Direction.SOUTH),
                LocationConnection("tundra_frozen_grotto", Direction.WEST),
                LocationConnection("tundra_moulin_shaft", Direction.DOWN)
            ),
            encounterRate = 0.60,
            recommendedLevel = 15,
            isSafeZone = true
        ),
        
        Location(
            id = "tundra_frozen_grotto",
            name = "Tundra Frozen Grotto",
            description = LocationDescription.simple(
                "A smaller cavern decorated with ice formations—stalactites, stalagmites, and flowstone, all frozen. The grotto is delicate, structures that took centuries to form and could be destroyed in seconds. Water seepage creates new formations constantly, ice growing molecule by molecule. The grotto is a museum of ice, each formation unique and irreplaceable."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 1,
            gridY = 8,
            connections = listOf(
                LocationConnection("ice_cathedral_tundra", Direction.EAST),
                LocationConnection("icecrystal_cavern", Direction.SOUTH),
                LocationConnection("underground_lake_frozen", Direction.NORTH)
            ),
            encounterRate = 0.65,
            recommendedLevel = 14
        ),
        
        Location(
            id = "tundra_moulin_shaft",
            name = "Tundra Moulin Shaft",
            description = LocationDescription.simple(
                "A vertical shaft where surface meltwater plunges into glacier depths. The shaft is cylindrical, walls polished smooth by water erosion. It drops far into darkness, water echoing from below. Moulins are dynamic—enlarging in summer as meltwater flows, shrinking in winter as flow ceases. Descending a moulin is possible but treacherous, ice slick and vertical sections requiring climbing expertise."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 9,
            connections = listOf(
                LocationConnection("ice_cathedral_tundra", Direction.UP),
                LocationConnection("underground_lake_frozen", Direction.NORTH)
            ),
            encounterRate = 0.80,
            recommendedLevel = 16,
            lore = "Moulins (French for 'mills') form when surface meltwater exploits cracks in glaciers, drilling vertical shafts. Some reach the glacier bed hundreds of meters down. Moulins influence glacier flow by lubricating the ice-rock interface."
        ),
        
        Location(
            id = "underground_lake_frozen",
            name = "Frozen Underground Lake",
            description = LocationDescription.simple(
                "Deep beneath the glacier, a lake of supercooled water remains liquid despite subzero temperatures. The water is crystal clear, revealing depths that seem bottomless. Unique microbial life thrives here, adapted to extreme cold and isolation. The lake is ancient, sealed from the surface for millennia. Contaminating it would be scientific catastrophe; studying it offers insights into life's limits."
            ),
            biome = BiomeType.TUNDRA,
            gridX = 2,
            gridY = 10,
            connections = listOf(
                LocationConnection("tundra_frozen_grotto", Direction.SOUTH),
                LocationConnection("tundra_moulin_shaft", Direction.SOUTH),
                LocationConnection("icecrystal_cavern", Direction.SOUTH)
            ),
            encounterRate = 0.70,
            recommendedLevel = 17,
            lore = "Subglacial lakes exist beneath Antarctic and Greenland ice sheets. Lake Vostok in Antarctica has been sealed for 15+ million years, potentially harboring unique life. These lakes challenge our understanding of life's environmental limits."
        )
    )
}

