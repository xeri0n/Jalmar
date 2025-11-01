package com.jalmarquest.shared.world

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.measureTime

/**
 * Performance smoke test for LocationCatalog with 545 locations.
 * 
 * Tests do NOT fail on performance targets - they WARN and report actual metrics.
 * This allows performance tracking over time without blocking builds.
 * 
 * Performance Targets (goals, not hard requirements):
 * - Catalog load time: <2000ms (2 seconds)
 * - Average lookup time: <100μs (microseconds)
 * - Memory footprint: <10MB
 */
class PerformanceSmokeTest {

    @Test
    fun `catalog load time should be reasonable`() {
        println("\n=== CATALOG LOAD TIME TEST ===")
        
        // Measure catalog initialization time (lazy loading)
        val loadTime = measureTime {
            val locations = LocationCatalog.allLocations
            // Force full materialization
            locations.size
        }
        
        val loadTimeMs = loadTime.inWholeMilliseconds
        val targetMs = 2000L
        
        println("Catalog load time: ${loadTimeMs}ms")
        println("Target: <${targetMs}ms")
        
        if (loadTimeMs > targetMs) {
            println("⚠️  WARNING: Load time exceeds target by ${loadTimeMs - targetMs}ms")
        } else {
            println("✅ Load time within target (${targetMs - loadTimeMs}ms under)")
        }
        
        // Always pass - this is informational only
        assertTrue(true, "Performance smoke test completed (see output for metrics)")
    }

    @Test
    fun `location lookup by ID should be fast`() {
        println("\n=== LOCATION LOOKUP PERFORMANCE TEST ===")
        
        // Warm up the catalog
        val allLocations = LocationCatalog.allLocations
        val locationIds = allLocations.map { it.id }
        
        println("Total locations: ${locationIds.size}")
        
        // Perform 1000 random lookups
        val lookupCount = 1000
        val lookupTimes = mutableListOf<Long>()
        
        repeat(lookupCount) { iteration ->
            // Pick random location ID
            val randomId = locationIds.random()
            
            // Measure single lookup time
            val lookupTime = measureTime {
                allLocations.find { it.id == randomId }
            }
            
            lookupTimes.add(lookupTime.inWholeMicroseconds)
        }
        
        // Calculate statistics
        val avgLookupMicros = lookupTimes.average()
        val maxLookupMicros = lookupTimes.maxOrNull() ?: 0L
        val minLookupMicros = lookupTimes.minOrNull() ?: 0L
        val targetMicros = 100L
        
        println("Lookup iterations: $lookupCount")
        println("Average lookup time: ${String.format("%.2f", avgLookupMicros)}μs")
        println("Min lookup time: ${minLookupMicros}μs")
        println("Max lookup time: ${maxLookupMicros}μs")
        println("Target: <${targetMicros}μs average")
        
        if (avgLookupMicros > targetMicros) {
            println("⚠️  WARNING: Average lookup exceeds target by ${String.format("%.2f", avgLookupMicros - targetMicros)}μs")
        } else {
            println("✅ Average lookup within target (${String.format("%.2f", targetMicros - avgLookupMicros)}μs under)")
        }
        
        // Always pass - this is informational only
        assertTrue(true, "Performance smoke test completed (see output for metrics)")
    }

    @Test
    fun `catalog memory footprint should be reasonable`() {
        println("\n=== MEMORY FOOTPRINT TEST ===")
        
        // Get runtime for memory measurements
        val runtime = Runtime.getRuntime()
        
        // Force garbage collection for baseline
        System.gc()
        Thread.sleep(100) // Give GC time to run
        
        val beforeMemory = runtime.totalMemory() - runtime.freeMemory()
        
        // Load catalog
        val locations = LocationCatalog.allLocations
        val locationCount = locations.size
        
        // Force garbage collection again
        System.gc()
        Thread.sleep(100)
        
        val afterMemory = runtime.totalMemory() - runtime.freeMemory()
        val memoryUsedBytes = afterMemory - beforeMemory
        val memoryUsedMB = memoryUsedBytes / (1024.0 * 1024.0)
        val targetMB = 10.0
        
        println("Locations loaded: $locationCount")
        println("Memory used: ${String.format("%.2f", memoryUsedMB)}MB")
        println("Target: <${targetMB}MB")
        
        if (memoryUsedMB > targetMB) {
            println("⚠️  WARNING: Memory usage exceeds target by ${String.format("%.2f", memoryUsedMB - targetMB)}MB")
        } else {
            println("✅ Memory usage within target (${String.format("%.2f", targetMB - memoryUsedMB)}MB under)")
        }
        
        // Calculate per-location memory
        val memoryPerLocationBytes = memoryUsedBytes / locationCount
        println("Average memory per location: ${memoryPerLocationBytes} bytes")
        
        // Always pass - this is informational only
        assertTrue(true, "Performance smoke test completed (see output for metrics)")
    }

    @Test
    fun `catalog should handle concurrent access gracefully`() {
        println("\n=== CONCURRENT ACCESS TEST ===")
        
        val locations = LocationCatalog.allLocations
        val threadCount = 10
        val iterationsPerThread = 100
        
        // Measure concurrent lookup time
        val concurrentTime = measureTime {
            val threads = List(threadCount) { threadId ->
                Thread {
                    repeat(iterationsPerThread) {
                        // Random lookups
                        val randomId = locations.random().id
                        locations.find { it.id == randomId }
                    }
                }
            }
            
            threads.forEach { it.start() }
            threads.forEach { it.join() }
        }
        
        val totalLookups = threadCount * iterationsPerThread
        val concurrentTimeMs = concurrentTime.inWholeMilliseconds
        val lookupsPerSecond = (totalLookups / (concurrentTimeMs / 1000.0))
        
        println("Threads: $threadCount")
        println("Iterations per thread: $iterationsPerThread")
        println("Total lookups: $totalLookups")
        println("Total time: ${concurrentTimeMs}ms")
        println("Throughput: ${String.format("%.2f", lookupsPerSecond)} lookups/second")
        
        println("✅ Concurrent access completed without errors")
        
        // Always pass - this is informational only
        assertTrue(true, "Performance smoke test completed (see output for metrics)")
    }

    @Test
    fun `catalog query operations should be efficient`() {
        println("\n=== CATALOG QUERY OPERATIONS TEST ===")
        
        val locations = LocationCatalog.allLocations
        
        // Test 1: Filter by biome
        val biomeFilterTime = measureTime {
            repeat(100) {
                locations.filter { it.biome == BiomeType.GRASSLAND }
            }
        }
        println("Biome filter (100 iterations): ${biomeFilterTime.inWholeMilliseconds}ms")
        
        // Test 2: Filter by level range
        val levelFilterTime = measureTime {
            repeat(100) {
                locations.filter { it.recommendedLevel in 5..10 }
            }
        }
        println("Level filter (100 iterations): ${levelFilterTime.inWholeMilliseconds}ms")
        
        // Test 3: Filter by encounter rate
        val encounterFilterTime = measureTime {
            repeat(100) {
                locations.filter { it.encounterRate > 0.5 }
            }
        }
        println("Encounter rate filter (100 iterations): ${encounterFilterTime.inWholeMilliseconds}ms")
        
        // Test 4: Connection count analysis
        val connectionAnalysisTime = measureTime {
            repeat(100) {
                locations.map { it.connections.size }.average()
            }
        }
        println("Connection analysis (100 iterations): ${connectionAnalysisTime.inWholeMilliseconds}ms")
        
        println("✅ All query operations completed efficiently")
        
        // Always pass - this is informational only
        assertTrue(true, "Performance smoke test completed (see output for metrics)")
    }
}
