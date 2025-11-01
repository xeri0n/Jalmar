package com.jalmarquest.shared.movement

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.weather.Weather
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.Location
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Manages player movement, pathfinding, and stamina costs.
 * Thread-safe movement operations with A* pathfinding.
 */
class MovementManager(
    private val locationManager: LocationManager
) {
    private val mutex = Mutex()
    
    /**
     * Attempt to move the player in a specific direction.
     * Returns MovementResult with success/failure details.
     * 
     * @param player Current player state
     * @param direction Direction to move
     * @param weather Current weather (affects stamina/time costs)
     * @param unlockedFlags Player's unlocked condition flags
     */
    suspend fun move(
        player: Player,
        direction: Direction,
        weather: Weather = Weather.CLEAR_SKY,
        unlockedFlags: Set<String> = emptySet()
    ): MovementResult {
        return mutex.withLock {
            val currentLocation = locationManager.getLocation(player.position.locationId)
                ?: return MovementResult.Failure(MovementFailureReason.LOCATION_NOT_FOUND)
            
            // Find connection in the specified direction
            val connection = currentLocation.connections.find { it.direction == direction }
                ?: return MovementResult.Failure(MovementFailureReason.INVALID_DIRECTION)
            
            // Check if path is blocked
            if (connection.isBlocked) {
                return MovementResult.Failure(MovementFailureReason.BLOCKED_PATH)
            }
            
            // Check level requirement
            if (player.level < connection.requiredLevel) {
                return MovementResult.Failure(MovementFailureReason.LEVEL_REQUIREMENT_NOT_MET)
            }
            
            // Check unlock condition
            connection.unlockCondition?.let { required ->
                if (!unlockedFlags.contains(required)) {
                    return MovementResult.Failure(MovementFailureReason.UNLOCK_CONDITION_NOT_MET)
                }
            }
            
            // Get target location
            val targetLocation = locationManager.getLocation(connection.targetLocationId)
                ?: return MovementResult.Failure(MovementFailureReason.LOCATION_NOT_FOUND)
            
            // Calculate base costs
            val movementCost = MovementCost.forBiome(targetLocation.biome)
            
            // Apply weather modifier to stamina cost
            // Weather slows movement, increasing stamina cost
            val weatherModifier = weather.effectiveMovementModifier()
            val baseStaminaCost = movementCost.baseStaminaCost
            val weatherAdjustedStamina = (baseStaminaCost / weatherModifier).roundToInt()
            val staminaCost = maxOf(1, weatherAdjustedStamina) // Minimum 1 stamina
            
            // Check stamina
            if (player.stats.currentStamina < staminaCost) {
                return MovementResult.Failure(MovementFailureReason.INSUFFICIENT_STAMINA)
            }
            
            // Calculate time cost (in minutes) - minimum 1 minute
            // Weather also affects time (slower movement = more time)
            val baseTimeCost = (connection.travelTime * movementCost.timeMultiplier).roundToInt()
            val weatherAdjustedTime = (baseTimeCost / weatherModifier).roundToInt()
            val timeCost = maxOf(1, weatherAdjustedTime)
            
            MovementResult.Success(
                newLocationId = targetLocation.id,
                staminaCost = staminaCost,
                timeCost = timeCost
            )
        }
    }
    
    /**
     * Find optimal path from start to goal using A* algorithm.
     * Returns null if no path exists.
     */
    fun findPath(
        startLocationId: String,
        goalLocationId: String,
        playerLevel: Int,
        unlockedFlags: Set<String> = emptySet()
    ): List<String>? {
        val startLocation = locationManager.getLocation(startLocationId) ?: return null
        val goalLocation = locationManager.getLocation(goalLocationId) ?: return null
        
        val openSet = mutableListOf(PathNode(startLocationId, 0, heuristic(startLocation, goalLocation)))
        val closedSet = mutableSetOf<String>()
        val gScores = mutableMapOf(startLocationId to 0)
        
        while (openSet.isNotEmpty()) {
            // Get node with lowest fCost
            val current = openSet.minByOrNull { it.fCost } ?: break
            
            if (current.locationId == goalLocationId) {
                return reconstructPath(current)
            }
            
            openSet.remove(current)
            closedSet.add(current.locationId)
            
            // Check neighbors
            val currentLocation = locationManager.getLocation(current.locationId) ?: continue
            val neighbors = currentLocation.getAvailableExits(playerLevel, unlockedFlags)
            
            for (connection in neighbors) {
                val neighborId = connection.targetLocationId
                if (closedSet.contains(neighborId)) continue
                
                val neighborLocation = locationManager.getLocation(neighborId) ?: continue
                val movementCost = MovementCost.forBiome(neighborLocation.biome)
                
                // Calculate tentative gScore
                val tentativeGScore = gScores[current.locationId]!! + movementCost.baseStaminaCost
                
                if (tentativeGScore < (gScores[neighborId] ?: Int.MAX_VALUE)) {
                    // This path is better
                    gScores[neighborId] = tentativeGScore
                    val hScore = heuristic(neighborLocation, goalLocation)
                    
                    val neighborNode = PathNode(
                        locationId = neighborId,
                        gCost = tentativeGScore,
                        hCost = hScore,
                        parent = current
                    )
                    
                    openSet.removeAll { it.locationId == neighborId }
                    openSet.add(neighborNode)
                }
            }
        }
        
        return null // No path found
    }
    
    /**
     * Calculate total stamina cost for a given path.
     */
    fun calculatePathStaminaCost(path: List<String>): Int {
        var totalCost = 0
        for (locationId in path) {
            val location = locationManager.getLocation(locationId) ?: continue
            val cost = MovementCost.forBiome(location.biome)
            totalCost += cost.baseStaminaCost
        }
        return totalCost
    }
    
    /**
     * Calculate total time cost for a given path (in minutes).
     */
    fun calculatePathTimeCost(path: List<String>): Int {
        if (path.size < 2) return 0
        
        var totalTime = 0
        for (i in 0 until path.size - 1) {
            val currentLocation = locationManager.getLocation(path[i]) ?: continue
            val nextLocationId = path[i + 1]
            
            val connection = currentLocation.connections.find { it.targetLocationId == nextLocationId }
                ?: continue
            
            val nextLocation = locationManager.getLocation(nextLocationId) ?: continue
            val movementCost = MovementCost.forBiome(nextLocation.biome)
            
            totalTime += (connection.travelTime * movementCost.timeMultiplier).toInt()
        }
        
        return totalTime
    }
    
    /**
     * Check if movement to a location is possible.
     */
    fun canMoveTo(
        fromLocationId: String,
        toLocationId: String,
        playerLevel: Int,
        playerStamina: Int,
        unlockedFlags: Set<String> = emptySet()
    ): Boolean {
        val fromLocation = locationManager.getLocation(fromLocationId) ?: return false
        
        val connection = fromLocation.connections.find { it.targetLocationId == toLocationId }
            ?: return false
        
        // Check level
        if (playerLevel < connection.requiredLevel) return false
        
        // Check unlock condition
        connection.unlockCondition?.let { required ->
            if (!unlockedFlags.contains(required)) return false
        }
        
        // Check stamina
        val toLocation = locationManager.getLocation(toLocationId) ?: return false
        val cost = MovementCost.forBiome(toLocation.biome)
        if (playerStamina < cost.baseStaminaCost) return false
        
        return true
    }
    
    /**
     * Get all locations reachable from current position with current stamina.
     */
    fun getReachableLocations(
        currentLocationId: String,
        playerLevel: Int,
        currentStamina: Int,
        unlockedFlags: Set<String> = emptySet()
    ): Set<String> {
        val reachable = mutableSetOf<String>()
        val visited = mutableSetOf<String>()
        val queue = mutableListOf(currentLocationId to currentStamina)
        
        while (queue.isNotEmpty()) {
            val (locationId, staminaRemaining) = queue.removeAt(0)
            
            if (visited.contains(locationId)) continue
            visited.add(locationId)
            reachable.add(locationId)
            
            val location = locationManager.getLocation(locationId) ?: continue
            val connections = location.getAvailableExits(playerLevel, unlockedFlags)
            
            for (connection in connections) {
                val neighborId = connection.targetLocationId
                if (visited.contains(neighborId)) continue
                
                val neighborLocation = locationManager.getLocation(neighborId) ?: continue
                val cost = MovementCost.forBiome(neighborLocation.biome)
                
                if (staminaRemaining >= cost.baseStaminaCost) {
                    queue.add(neighborId to (staminaRemaining - cost.baseStaminaCost))
                }
            }
        }
        
        return reachable
    }
    
    /**
     * Heuristic function for A* (Manhattan distance on grid).
     */
    private fun heuristic(from: Location, to: Location): Int {
        val dx = abs(from.gridX - to.gridX)
        val dy = abs(from.gridY - to.gridY)
        return dx + dy
    }
    
    /**
     * Reconstruct path from goal node back to start.
     */
    private fun reconstructPath(goalNode: PathNode): List<String> {
        val path = mutableListOf<String>()
        var current: PathNode? = goalNode
        
        while (current != null) {
            path.add(0, current.locationId)
            current = current.parent
        }
        
        return path
    }
}
