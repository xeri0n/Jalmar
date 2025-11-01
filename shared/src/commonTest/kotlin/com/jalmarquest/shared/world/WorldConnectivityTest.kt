package com.jalmarquest.shared.world

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Comprehensive connectivity and constraint validation test for the entire world graph.
 * 
 * This test validates:
 * 1. Location ID uniqueness (545 unique IDs)
 * 2. Connection validity (all references point to existing locations)
 * 3. Graph reachability (all locations accessible from starting_village via BFS)
 * 4. Constraint validation (encounterRate, recommendedLevel, grid coordinates)
 * 
 * CRITICAL: This test must pass with 100% reachability before production.
 */
class WorldConnectivityTest {
    
    @Test
    fun `all 545 location IDs should be unique`() {
        val allIds = LocationCatalog.allLocations.map { it.id }
        val uniqueIds = allIds.toSet()
        
        assertEquals(
            allIds.size, 
            uniqueIds.size,
            "Found ${allIds.size - uniqueIds.size} duplicate location IDs. All IDs must be unique."
        )
        
        // Expected total: 46 base + 499 expansion = 545 locations
        assertTrue(
            allIds.size >= 545,
            "Expected at least 545 locations, but found ${allIds.size}"
        )
    }
    
    @Test
    fun `all location connections should reference valid location IDs`() {
        val allIds = LocationCatalog.allLocations.map { it.id }.toSet()
        val invalidConnections = mutableListOf<String>()
        
        LocationCatalog.allLocations.forEach { location ->
            location.connections.forEach { connection ->
                if (connection.targetLocationId !in allIds) {
                    invalidConnections.add(
                        "${location.id} -> ${connection.targetLocationId} (${connection.direction})"
                    )
                }
            }
        }
        
        if (invalidConnections.isNotEmpty()) {
            val message = buildString {
                appendLine("Found ${invalidConnections.size} broken connections:")
                invalidConnections.take(20).forEach { appendLine("  - $it") }
                if (invalidConnections.size > 20) {
                    appendLine("  ... and ${invalidConnections.size - 20} more")
                }
            }
            fail(message)
        }
    }
    
    @Test
    fun `all locations should be reachable from starting_village via graph traversal`() {
        // Build adjacency map for graph traversal
        val adjacencyMap = mutableMapOf<String, MutableList<String>>()
        LocationCatalog.allLocations.forEach { location ->
            adjacencyMap[location.id] = mutableListOf()
            location.connections.forEach { connection ->
                adjacencyMap[location.id]?.add(connection.targetLocationId)
            }
        }
        
        // BFS from starting_village
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add("starting_village")
        visited.add("starting_village")
        
        while (queue.isNotEmpty()) {
            val currentId = queue.removeFirst()
            val neighbors = adjacencyMap[currentId] ?: emptyList()
            
            neighbors.forEach { neighborId ->
                if (neighborId !in visited) {
                    visited.add(neighborId)
                    queue.add(neighborId)
                }
            }
        }
        
        // Find unreachable locations
        val allLocationIds = LocationCatalog.allLocations.map { it.id }.toSet()
        val unreachable = allLocationIds - visited
        
        if (unreachable.isNotEmpty()) {
            val unreachableList = unreachable.sorted().take(50)
            val message = buildString {
                appendLine("Found ${unreachable.size} unreachable locations (not connected to starting_village):")
                unreachableList.forEach { appendLine("  - $it") }
                if (unreachable.size > 50) {
                    appendLine("  ... and ${unreachable.size - 50} more")
                }
                appendLine()
                appendLine("All locations must be accessible via the connection graph.")
            }
            fail(message)
        }
        
        // Success: All locations reachable
        assertEquals(
            allLocationIds.size,
            visited.size,
            "All ${allLocationIds.size} locations should be reachable from starting_village"
        )
    }
    
    @Test
    fun `all locations should have valid encounter rates`() {
        val invalidEncounterRates = LocationCatalog.allLocations
            .filter { it.encounterRate < 0.0 || it.encounterRate > 1.0 }
            .map { "${it.id}: encounterRate=${it.encounterRate}" }
        
        if (invalidEncounterRates.isNotEmpty()) {
            val message = buildString {
                appendLine("Found ${invalidEncounterRates.size} locations with invalid encounter rates:")
                appendLine("Encounter rate must be between 0.0 and 1.0 (inclusive)")
                invalidEncounterRates.take(20).forEach { appendLine("  - $it") }
                if (invalidEncounterRates.size > 20) {
                    appendLine("  ... and ${invalidEncounterRates.size - 20} more")
                }
            }
            fail(message)
        }
    }
    
    @Test
    fun `all locations should have valid recommended levels`() {
        val invalidLevels = LocationCatalog.allLocations
            .filter { it.recommendedLevel < 1 || it.recommendedLevel > 50 }
            .map { "${it.id}: level=${it.recommendedLevel}" }
        
        if (invalidLevels.isNotEmpty()) {
            val message = buildString {
                appendLine("Found ${invalidLevels.size} locations with invalid recommended levels:")
                appendLine("Recommended level must be between 1 and 50 (inclusive)")
                invalidLevels.take(20).forEach { appendLine("  - $it") }
                if (invalidLevels.size > 20) {
                    appendLine("  ... and ${invalidLevels.size - 20} more")
                }
            }
            fail(message)
        }
    }
    
    @Test
    fun `all locations should have valid grid coordinates`() {
        val invalidCoordinates = LocationCatalog.allLocations
            .filter { it.gridX < -10 || it.gridX > 10 || it.gridY < -9 || it.gridY > 15 }
            .map { "${it.id}: (${it.gridX}, ${it.gridY})" }
        
        if (invalidCoordinates.isNotEmpty()) {
            val message = buildString {
                appendLine("Found ${invalidCoordinates.size} locations with invalid grid coordinates:")
                appendLine("Grid X must be in range [-10, 10], Grid Y must be in range [-9, 15]")
                invalidCoordinates.take(20).forEach { appendLine("  - $it") }
                if (invalidCoordinates.size > 20) {
                    appendLine("  ... and ${invalidCoordinates.size - 20} more")
                }
            }
            fail(message)
        }
    }
    
    @Test
    fun `all biome types should be represented in the world`() {
        val representedBiomes = LocationCatalog.allLocations
            .map { it.biome }
            .toSet()
        
        val expectedBiomes = BiomeType.values().toSet()
        val missingBiomes = expectedBiomes - representedBiomes
        
        if (missingBiomes.isNotEmpty()) {
            fail("Missing biome types in world: ${missingBiomes.joinToString()}")
        }
        
        assertEquals(
            expectedBiomes.size,
            representedBiomes.size,
            "All 8 biome types should be represented in the 545 locations"
        )
    }
    
    @Test
    fun `world should have reasonable distribution across biomes`() {
        val biomeCounts = LocationCatalog.allLocations
            .groupingBy { it.biome }
            .eachCount()
        
        val message = buildString {
            appendLine("Biome distribution across ${LocationCatalog.allLocations.size} locations:")
            BiomeType.values().forEach { biome ->
                val count = biomeCounts[biome] ?: 0
                val percentage = (count * 100.0 / LocationCatalog.allLocations.size)
                appendLine("  ${biome.name}: $count locations (${String.format("%.1f", percentage)}%)")
            }
        }
        
        println(message)
        
        // Verify each biome has at least 5 locations (minimum viable region)
        BiomeType.values().forEach { biome ->
            val count = biomeCounts[biome] ?: 0
            assertTrue(
                count >= 5,
                "Biome ${biome.name} has only $count locations. Minimum is 5 for viable gameplay."
            )
        }
    }
    
    @Test
    fun `starting_village should exist and be accessible`() {
        val startingVillage = LocationCatalog.getLocation("starting_village")
        
        assertTrue(
            startingVillage != null,
            "starting_village must exist as the game's entry point"
        )
        
        assertTrue(
            startingVillage!!.connections.isNotEmpty(),
            "starting_village must have connections to other locations"
        )
        
        assertEquals(
            "Buttonburgh",
            startingVillage.name,
            "starting_village should be named Buttonburgh"
        )
    }
    
    @Test
    fun `graph should have bidirectional connections where appropriate`() {
        // Build reverse connection map
        val reverseConnections = mutableMapOf<String, MutableSet<String>>()
        LocationCatalog.allLocations.forEach { location ->
            location.connections.forEach { connection ->
                reverseConnections
                    .getOrPut(connection.targetLocationId) { mutableSetOf() }
                    .add(location.id)
            }
        }
        
        // Check for locations with only incoming connections (potential dead ends)
        val potentialDeadEnds = LocationCatalog.allLocations
            .filter { it.connections.isEmpty() }
            .filter { reverseConnections[it.id]?.isNotEmpty() == true }
        
        if (potentialDeadEnds.isNotEmpty()) {
            val message = buildString {
                appendLine("Found ${potentialDeadEnds.size} potential dead-end locations:")
                appendLine("(Locations with incoming connections but no outgoing connections)")
                potentialDeadEnds.take(10).forEach { location ->
                    val incomingFrom = reverseConnections[location.id]?.joinToString(", ") ?: "none"
                    appendLine("  - ${location.id} (incoming from: $incomingFrom)")
                }
                if (potentialDeadEnds.size > 10) {
                    appendLine("  ... and ${potentialDeadEnds.size - 10} more")
                }
                appendLine()
                appendLine("Note: This is informational. Dead ends may be intentional (boss rooms, special locations).")
            }
            println(message)
        }
    }
    
    @Test
    fun `world graph statistics should be reasonable`() {
        val totalLocations = LocationCatalog.allLocations.size
        val totalConnections = LocationCatalog.allLocations.sumOf { it.connections.size }
        val avgConnectionsPerLocation = totalConnections.toDouble() / totalLocations
        
        val connectivityStats = buildString {
            appendLine()
            appendLine("========== WORLD CONNECTIVITY STATISTICS ==========")
            appendLine("Total Locations: $totalLocations")
            appendLine("Total Connections: $totalConnections")
            appendLine("Average Connections per Location: ${String.format("%.2f", avgConnectionsPerLocation)}")
            appendLine()
            
            val locationsByConnectionCount = LocationCatalog.allLocations
                .groupingBy { it.connections.size }
                .eachCount()
                .toSortedMap()
            
            appendLine("Connection Distribution:")
            locationsByConnectionCount.forEach { (count, locations) ->
                appendLine("  $count connections: $locations locations")
            }
            appendLine()
            
            val isolatedLocations = LocationCatalog.allLocations.filter { it.connections.isEmpty() }
            if (isolatedLocations.isNotEmpty()) {
                appendLine("Isolated Locations (0 connections): ${isolatedLocations.size}")
                isolatedLocations.take(5).forEach { appendLine("  - ${it.id}") }
                if (isolatedLocations.size > 5) {
                    appendLine("  ... and ${isolatedLocations.size - 5} more")
                }
            }
            appendLine("===================================================")
        }
        
        println(connectivityStats)
        
        // Validate reasonable connectivity
        assertTrue(
            avgConnectionsPerLocation >= 2.0,
            "Average connections per location should be at least 2.0 for good world connectivity. Found: ${String.format("%.2f", avgConnectionsPerLocation)}"
        )
        
        assertTrue(
            totalLocations >= 545,
            "Expected at least 545 locations (46 base + 499 expansion). Found: $totalLocations"
        )
    }
}
