package com.jalmarquest.shared.world

import kotlin.test.Test
import kotlin.test.fail

class DiagnosticTest {
    @Test
    fun `find duplicate location IDs`() {
        val allIds = LocationCatalog.allLocations.map { it.id }
        val duplicateIds = allIds.groupBy { it }
            .filter { it.value.size > 1 }
            .map { "${it.key} appears ${it.value.size} times" }
        
        val message = buildString {
            appendLine("\n========== DUPLICATE IDs ==========")
            if (duplicateIds.isEmpty()) {
                appendLine("No duplicate IDs found ✅")
            } else {
                appendLine("Found ${duplicateIds.size} duplicate IDs:")
                duplicateIds.forEach { appendLine("  - $it") }
            }
            appendLine("Total location IDs: ${allIds.size}")
            appendLine("Unique location IDs: ${allIds.toSet().size}")
        }
        
        if (duplicateIds.isNotEmpty()) {
            fail(message)
        } else {
            println(message)
        }
    }
    
    @Test
    fun `find duplicate location names`() {
        val allNames = LocationCatalog.allLocations.map { it.name }
        val duplicateNames = allNames.groupBy { it }
            .filter { it.value.size > 1 }
            .map { "${it.key} appears ${it.value.size} times" }
        
        val message = buildString {
            appendLine("\n========== DUPLICATE NAMES ==========")
            if (duplicateNames.isEmpty()) {
                appendLine("No duplicate names found ✅")
            } else {
                appendLine("Found ${duplicateNames.size} duplicate names:")
                duplicateNames.forEach { appendLine("  - $it") }
            }
            appendLine("Total location names: ${allNames.size}")
            appendLine("Unique location names: ${allNames.toSet().size}")
        }
        
        if (duplicateNames.isNotEmpty()) {
            fail(message)
        } else {
            println(message)
        }
    }
    
    @Test
    fun `find broken connections`() {
        val allIds = LocationCatalog.allLocations.map { it.id }.toSet()
        val brokenConnections = mutableListOf<String>()
        
        LocationCatalog.allLocations.forEach { location ->
            location.connections.forEach { connection ->
                if (connection.targetLocationId !in allIds) {
                    brokenConnections.add("${location.id} -> ${connection.targetLocationId} (${connection.direction})")
                }
            }
        }
        
        val message = buildString {
            appendLine("\n========== BROKEN CONNECTIONS ==========")
            appendLine("Total locations: ${LocationCatalog.allLocations.size}")
            appendLine("Total unique IDs: ${allIds.size}")
            if (brokenConnections.isEmpty()) {
                appendLine("No broken connections found ✅")
            } else {
                appendLine("Found ${brokenConnections.size} broken connections:")
                brokenConnections.take(50).forEach { appendLine("  - $it") }
                if (brokenConnections.size > 50) {
                    appendLine("  ... and ${brokenConnections.size - 50} more")
                }
            }
        }
        
        if (brokenConnections.isNotEmpty()) {
            fail(message)
        } else {
            println(message)
        }
    }
}
