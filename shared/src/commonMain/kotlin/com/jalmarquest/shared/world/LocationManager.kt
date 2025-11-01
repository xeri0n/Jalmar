package com.jalmarquest.shared.world

import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.model.Season
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages location queries, navigation, and discovery.
 */
class LocationManager {
    private val mutex = Mutex()
    private val discoveries = mutableMapOf<String, LocationDiscovery>()
    
    /**
     * Get a location by ID.
     */
    fun getLocation(locationId: String): Location? {
        return LocationCatalog.getLocation(locationId)
    }
    
    /**
     * Get all locations.
     */
    fun getAllLocations(): List<Location> {
        return LocationCatalog.allLocations
    }
    
    /**
     * Get locations by biome type.
     */
    fun getLocationsByBiome(biome: BiomeType): List<Location> {
        return LocationCatalog.getLocationsByBiome(biome)
    }
    
    /**
     * Get all settlement locations.
     */
    fun getSettlements(): List<Location> {
        return LocationCatalog.getSettlements()
    }
    
    /**
     * Get all locations with fast travel available.
     */
    fun getFastTravelLocations(): List<Location> {
        return LocationCatalog.getFastTravelLocations()
    }
    
    /**
     * Get the seasonal description for a location.
     * 
     * Returns the appropriate description based on the current season.
     * If the location has a LocationDescription with seasonal variants,
     * returns the season-specific text. Otherwise returns the base description.
     * 
     * @param locationId The ID of the location
     * @param season The current season
     * @return The seasonal description, or null if location not found
     */
    fun getSeasonalDescription(locationId: String, season: Season): String? {
        val location = getLocation(locationId) ?: return null
        
        // If location has a description object, use it to get seasonal variant
        return location.description.getSeasonalDescription(season)
    }
    
    /**
     * Discover a new location.
     */
    suspend fun discoverLocation(locationId: String) {
        mutex.withLock {
            if (locationId !in discoveries) {
                discoveries[locationId] = LocationDiscovery(
                    locationId = locationId,
                    discoveredAt = System.currentTimeMillis(),
                    visitCount = 1,
                    fastTravelUnlocked = false
                )
            } else {
                val current = discoveries[locationId]!!
                discoveries[locationId] = current.copy(
                    visitCount = current.visitCount + 1
                )
            }
        }
    }
    
    /**
     * Check if a location has been discovered.
     */
    fun isDiscovered(locationId: String): Boolean {
        return locationId in discoveries
    }
    
    /**
     * Get discovery info for a location.
     */
    fun getDiscovery(locationId: String): LocationDiscovery? {
        return discoveries[locationId]
    }
    
    /**
     * Get all discovered locations.
     */
    fun getDiscoveredLocations(): List<Location> {
        return discoveries.keys.mapNotNull { getLocation(it) }
    }
    
    /**
     * Unlock fast travel for a location.
     */
    suspend fun unlockFastTravel(locationId: String) {
        mutex.withLock {
            val discovery = discoveries[locationId] ?: return
            discoveries[locationId] = discovery.copy(fastTravelUnlocked = true)
        }
    }
    
    /**
     * Check if fast travel is unlocked for a location.
     */
    fun isFastTravelUnlocked(locationId: String): Boolean {
        return discoveries[locationId]?.fastTravelUnlocked == true
    }
    
    /**
     * Get all locations where fast travel is unlocked.
     */
    fun getUnlockedFastTravelLocations(): List<Location> {
        return discoveries
            .filter { it.value.fastTravelUnlocked }
            .keys
            .mapNotNull { getLocation(it) }
    }
    
    /**
     * Get available exits from a location based on player level and unlocked flags.
     */
    fun getAvailableExits(
        locationId: String,
        playerLevel: Int,
        unlockedFlags: Set<String>
    ): List<LocationConnection> {
        val location = getLocation(locationId) ?: return emptyList()
        return location.getAvailableExits(playerLevel, unlockedFlags)
    }
    
    /**
     * Check if player can travel from one location to another.
     */
    fun canTravel(
        fromLocationId: String,
        toLocationId: String,
        playerLevel: Int,
        unlockedFlags: Set<String>
    ): Boolean {
        val availableExits = getAvailableExits(fromLocationId, playerLevel, unlockedFlags)
        return availableExits.any { it.targetLocationId == toLocationId }
    }
    
    /**
     * Find path between two locations using BFS.
     * Returns list of location IDs representing the path, or null if no path exists.
     */
    fun findPath(
        fromLocationId: String,
        toLocationId: String,
        playerLevel: Int,
        unlockedFlags: Set<String>
    ): List<String>? {
        if (fromLocationId == toLocationId) return listOf(fromLocationId)
        
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<Pair<String, List<String>>>()
        queue.add(fromLocationId to listOf(fromLocationId))
        
        while (queue.isNotEmpty()) {
            val (currentId, path) = queue.removeFirst()
            
            if (currentId in visited) continue
            visited.add(currentId)
            
            val exits = getAvailableExits(currentId, playerLevel, unlockedFlags)
            
            for (exit in exits) {
                if (exit.targetLocationId == toLocationId) {
                    return path + toLocationId
                }
                
                if (exit.targetLocationId !in visited) {
                    queue.add(exit.targetLocationId to path + exit.targetLocationId)
                }
            }
        }
        
        return null // No path found
    }
    
    /**
     * Calculate total travel time for a path.
     */
    fun calculatePathTime(
        path: List<String>,
        playerLevel: Int,
        unlockedFlags: Set<String>
    ): Int {
        if (path.size < 2) return 0
        
        var totalTime = 0
        
        for (i in 0 until path.size - 1) {
            val fromId = path[i]
            val toId = path[i + 1]
            
            val exits = getAvailableExits(fromId, playerLevel, unlockedFlags)
            val connection = exits.find { it.targetLocationId == toId }
            
            totalTime += connection?.travelTime ?: 1
        }
        
        return totalTime
    }
    
    /**
     * Get neighboring locations (directly connected).
     */
    fun getNeighbors(locationId: String): List<Location> {
        val location = getLocation(locationId) ?: return emptyList()
        return location.connections.mapNotNull { getLocation(it.targetLocationId) }
    }
    
    /**
     * Get locations within a certain grid distance.
     */
    fun getLocationsInRadius(centerX: Int, centerY: Int, radius: Int): List<Location> {
        return getAllLocations().filter { location ->
            val dx = location.gridX - centerX
            val dy = location.gridY - centerY
            kotlin.math.sqrt((dx * dx + dy * dy).toDouble()) <= radius
        }
    }
    
    /**
     * Get the closest settlement to a location.
     */
    fun getNearestSettlement(locationId: String): Location? {
        val location = getLocation(locationId) ?: return null
        val settlements = getSettlements()
        
        return settlements.minByOrNull { settlement ->
            val dx = settlement.gridX - location.gridX
            val dy = settlement.gridY - location.gridY
            dx * dx + dy * dy
        }
    }
    
    /**
     * Load discoveries from saved data.
     */
    suspend fun loadDiscoveries(savedDiscoveries: Map<String, LocationDiscovery>) {
        mutex.withLock {
            discoveries.clear()
            discoveries.putAll(savedDiscoveries)
        }
    }
    
    /**
     * Get current discoveries for saving.
     */
    fun getDiscoveriesForSave(): Map<String, LocationDiscovery> {
        return discoveries.toMap()
    }
    
    /**
     * Clear all discoveries (for new game).
     */
    suspend fun clearDiscoveries() {
        mutex.withLock {
            discoveries.clear()
        }
    }
    
    /**
     * Get statistics about discovered locations.
     */
    fun getDiscoveryStats(): DiscoveryStats {
        val totalLocations = getAllLocations().size
        val discovered = discoveries.size
        val fastTravelUnlocked = discoveries.values.count { it.fastTravelUnlocked }
        val totalVisits = discoveries.values.sumOf { it.visitCount }
        val biomes = discoveries.keys
            .mapNotNull { getLocation(it)?.biome }
            .toSet()
        
        return DiscoveryStats(
            totalLocations = totalLocations,
            discoveredLocations = discovered,
            fastTravelUnlocked = fastTravelUnlocked,
            totalVisits = totalVisits,
            biomesExplored = biomes.size,
            explorationPercentage = (discovered.toDouble() / totalLocations * 100).toInt()
        )
    }
}

/**
 * Statistics about location discovery.
 */
data class DiscoveryStats(
    val totalLocations: Int,
    val discoveredLocations: Int,
    val fastTravelUnlocked: Int,
    val totalVisits: Int,
    val biomesExplored: Int,
    val explorationPercentage: Int
)
