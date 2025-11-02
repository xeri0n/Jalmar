package com.jalmar.quest.state

import com.jalmarquest.shared.model.TilePosition
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmar.quest.movement.TileMovementResult
import com.jalmar.quest.tilemap.model.TileCoordinate
import com.jalmar.quest.tilemap.MapTriggerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GameStateManager(
    private val mapTriggerManager: MapTriggerManager? = null
) {
    private val mutex = Mutex()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()
    
    private suspend fun updateState(update: (GameState) -> GameState) {
        mutex.withLock {
            _gameState.value = update(_gameState.value ?: error("No game loaded"))
        }
    }
    
    suspend fun initializeNewGame(playerName: String, startingMapId: String = "buttonburgh") {
        val initialState = GameState(
            player = Player(
                id = "player_${System.currentTimeMillis()}",
                name = playerName,
                level = 1,
                stats = PlayerStats(),
                position = Position(0, 0, startingMapId),
                tilePosition = TilePosition(startingMapId, 0, 0)
            ),
            currentMapId = startingMapId
        )
        
        mutex.withLock {
            _gameState.value = initialState
        }
    }
    
    suspend fun processTileMovement(result: TileMovementResult, mapId: String) {
        when (result) {
            is TileMovementResult.Success -> {
                val newPos = result.newPosition
                updateState { gameState ->
                    gameState.copy(
                        player = gameState.player.copy(
                            stats = gameState.player.stats.copy(
                                currentStamina = (gameState.player.stats.currentStamina - result.staminaCost).coerceAtLeast(0)
                            ),
                            tilePosition = TilePosition(mapId, newPos.x, newPos.y),
                            position = Position(newPos.x, newPos.y, mapId)
                        ),
                        currentMapId = mapId
                    )
                }
            }
            else -> {
                // No state change for other result types
            }
        }
    }
    
    suspend fun transitionToMap(newMapId: String, spawnX: Int, spawnY: Int) {
        updateState { gameState ->
            gameState.copy(
                player = gameState.player.copy(
                    tilePosition = TilePosition(newMapId, spawnX, spawnY),
                    position = Position(spawnX, spawnY, newMapId)
                ),
                currentMapId = newMapId
            )
        }
    }
    
    suspend fun createNewGame(playerName: String) {
        initializeNewGame(playerName, "buttonburgh")
    }
    
    suspend fun updateTilePosition(mapId: String, x: Int, y: Int) {
        updateState { gameState ->
            gameState.copy(
                player = gameState.player.copy(
                    tilePosition = TilePosition(mapId, x, y),
                    position = Position(x, y, mapId)
                )
            )
        }
    }
    
    suspend fun discoverTile(mapId: String, x: Int, y: Int) {
        updateState { gameState ->
            val currentDiscovered = gameState.discoveredTiles[mapId]?.toMutableSet() ?: mutableSetOf()
            currentDiscovered.add("$x:$y")
            gameState.copy(
                discoveredTiles = gameState.discoveredTiles.toMutableMap().apply {
                    put(mapId, currentDiscovered)
                }
            )
        }
    }
}
