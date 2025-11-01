package com.jalmarquest.shared.navigation

import com.jalmarquest.shared.movement.MovementManager
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationManager
import kotlin.math.abs

/**
 * Manages map-based navigation and route calculation.
 * Provides pathfinding from current location to destinations (especially Buttonburgh).
 * 
 * Used by the Map item to display routes and estimate travel time/costs.
 */
class MapNavigationManager(
    private val locationManager: LocationManager,
    private val movementManager: MovementManager
) {
    
    /**
     * Calculates the optimal route from one location to another.
     * Uses A* pathfinding from MovementManager.
     * 
     * @param fromLocationId Starting location ID
     * @param toLocationId Destination location ID
     * @param playerLevel Player's current level for unlock requirements
     * @return NavigationRoute with waypoints, distance, and estimated time/stamina
     */
    fun calculateRoute(
        fromLocationId: String, 
        toLocationId: String,
        playerLevel: Int = 1,
        unlockedFlags: Set<String> = emptySet()
    ): NavigationRoute? {
        val startLocation = locationManager.getLocation(fromLocationId) ?: return null
        val endLocation = locationManager.getLocation(toLocationId) ?: return null
        
        // Use MovementManager's A* pathfinding
        val path = movementManager.findPath(
            startLocationId = fromLocationId,
            goalLocationId = toLocationId,
            playerLevel = playerLevel,
            unlockedFlags = unlockedFlags
        ) ?: return null
        
        if (path.isEmpty()) {
            return null  // No path found
        }
        
        // Calculate total stamina cost and time
        var totalStamina = 0
        var totalTime = 0
        val waypoints = mutableListOf<NavigationWaypoint>()
        
        for (i in 0 until path.size - 1) {
            val currentLoc = locationManager.getLocation(path[i]) ?: continue
            val nextLoc = locationManager.getLocation(path[i + 1]) ?: continue
            
            // Find connection between locations
            val connection = currentLoc.connections.find { conn ->
                conn.targetLocationId == nextLoc.id
            }
            
            if (connection != null) {
                val movementCost = com.jalmarquest.shared.movement.MovementCost.forBiome(nextLoc.biome)
                val staminaCost = movementCost.baseStaminaCost
                val timeCost = connection.travelTime
                
                totalStamina += staminaCost
                totalTime += timeCost
                
                waypoints.add(NavigationWaypoint(
                    locationId = nextLoc.id,
                    locationName = nextLoc.name,
                    gridX = nextLoc.gridX,
                    gridY = nextLoc.gridY,
                    cumulativeStamina = totalStamina,
                    cumulativeTime = totalTime,
                    terrainDescription = nextLoc.biome.name.lowercase().replace('_', ' ')
                ))
            }
        }
        
        // Calculate straight-line distance for map visualization
        val distance = calculateDistance(startLocation, endLocation)
        
        return NavigationRoute(
            startLocationId = fromLocationId,
            endLocationId = toLocationId,
            waypoints = waypoints,
            totalStaminaCost = totalStamina,
            totalTimeMinutes = totalTime,
            straightLineDistance = distance,
            pathLength = path.size
        )
    }
    
    /**
     * Calculates route specifically to Buttonburgh (the hub city).
     * This is the primary use case for the Map item.
     */
    fun calculateRouteToButtonburgh(
        fromLocationId: String,
        playerLevel: Int = 1,
        unlockedFlags: Set<String> = emptySet()
    ): NavigationRoute? {
        return calculateRoute(fromLocationId, "buttonburgh", playerLevel, unlockedFlags)
    }
    
    /**
     * Checks if a location is Buttonburgh or within Buttonburgh district.
     */
    fun isInButtonburgh(locationId: String): Boolean {
        return locationId.startsWith("buttonburgh") || locationId == "starting_village"
    }
    
    /**
     * Calculates Manhattan distance between two locations for map visualization.
     * Returns distance in abstract "map units" (not real meters).
     */
    private fun calculateDistance(from: Location, to: Location): Int {
        return abs(from.gridX - to.gridX) + abs(from.gridY - to.gridY)
    }
    
    /**
     * Estimates travel time in real-world minutes (for player reference).
     * Assumes 1 in-game minute = 3 seconds real-time (20:1 time compression).
     */
    fun estimateRealWorldTime(inGameMinutes: Int): String {
        val realSeconds = inGameMinutes * 3  // 20:1 compression
        return when {
            realSeconds < 60 -> "${realSeconds}s"
            realSeconds < 3600 -> "${realSeconds / 60}m ${realSeconds % 60}s"
            else -> "${realSeconds / 3600}h ${(realSeconds % 3600) / 60}m"
        }
    }
}

/**
 * Represents a calculated navigation route between two locations.
 * 
 * @property startLocationId Origin location ID
 * @property endLocationId Destination location ID
 * @property waypoints Ordered list of locations along the route
 * @property totalStaminaCost Total stamina required to traverse the route
 * @property totalTimeMinutes Total in-game time to traverse (in minutes)
 * @property straightLineDistance Straight-line distance for map visualization
 * @property pathLength Number of location hops in the path
 */
data class NavigationRoute(
    val startLocationId: String,
    val endLocationId: String,
    val waypoints: List<NavigationWaypoint>,
    val totalStaminaCost: Int,
    val totalTimeMinutes: Int,
    val straightLineDistance: Int,
    val pathLength: Int
) {
    /**
     * Checks if the route is empty (no waypoints).
     */
    fun isEmpty(): Boolean = waypoints.isEmpty()
    
    /**
     * Gets formatted distance string for UI display.
     */
    fun formattedDistance(): String = "$straightLineDistance units"
    
    /**
     * Gets formatted time string for UI display.
     */
    fun formattedTime(): String {
        val hours = totalTimeMinutes / 60
        val minutes = totalTimeMinutes % 60
        return when {
            hours == 0 -> "${minutes}m"
            minutes == 0 -> "${hours}h"
            else -> "${hours}h ${minutes}m"
        }
    }
}

/**
 * Represents a single waypoint along a navigation route.
 * 
 * @property locationId Location ID for this waypoint
 * @property locationName Display name of the location
 * @property gridX Grid X coordinate for map rendering
 * @property gridY Grid Y coordinate for map rendering
 * @property cumulativeStamina Total stamina used to reach this waypoint
 * @property cumulativeTime Total in-game time to reach this waypoint (minutes)
 * @property terrainDescription Human-readable terrain type
 */
data class NavigationWaypoint(
    val locationId: String,
    val locationName: String,
    val gridX: Int,
    val gridY: Int,
    val cumulativeStamina: Int,
    val cumulativeTime: Int,
    val terrainDescription: String
)
