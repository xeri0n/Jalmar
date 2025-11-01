package dev.xeri0n.jalmarquest.ui.viewmodel

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.state.GameStateManager
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameViewModel(
    private val gameStateManager: GameStateManager,
    private val locationManager: LocationManager,
    private val scope: CoroutineScope
) {
    val gameState: StateFlow<GameState?> = gameStateManager.gameState
    
    val currentLocation = gameState
        .map { state ->
            state?.let { locationManager.getLocation(it.player.position.locationId) }
        }
        .stateIn(scope, SharingStarted.Eagerly, null)
}