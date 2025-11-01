package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.GameState

/**
 * Desktop implementation of CloudSyncManager.
 * Currently a stub implementation - cloud sync not yet supported on desktop.
 * Future: Integrate with Steam Cloud or third-party service.
 */
actual class CloudSyncManager {
    actual suspend fun uploadSave(slotName: String, content: String): Result<Unit> {
        return Result.failure(
            CloudSyncException.SyncFailed("Cloud sync not yet implemented for desktop")
        )
    }
    
    actual suspend fun downloadSave(slotName: String): Result<String> {
        return Result.failure(
            CloudSyncException.SyncFailed("Cloud sync not yet implemented for desktop")
        )
    }
    
    actual suspend fun cloudSaveExists(slotName: String): Boolean {
        return false
    }
    
    actual suspend fun deleteCloudSave(slotName: String): Result<Unit> {
        return Result.failure(
            CloudSyncException.SyncFailed("Cloud sync not yet implemented for desktop")
        )
    }
    
    actual suspend fun listCloudSaves(): List<CloudSaveInfo> {
        return emptyList()
    }
    
    actual fun resolveConflict(
        localSave: GameState,
        cloudSave: GameState,
        strategy: ConflictResolutionStrategy
    ): GameState {
        // Default: use most recent
        return when (strategy) {
            ConflictResolutionStrategy.MOST_RECENT -> {
                if (localSave.saveTimestamp > cloudSave.saveTimestamp) localSave else cloudSave
            }
            ConflictResolutionStrategy.PREFER_LOCAL -> localSave
            ConflictResolutionStrategy.PREFER_CLOUD -> cloudSave
            ConflictResolutionStrategy.HIGHER_LEVEL -> {
                if (localSave.player.level >= cloudSave.player.level) localSave else cloudSave
            }
            ConflictResolutionStrategy.MORE_PLAYTIME -> {
                if (localSave.player.playTimeSeconds >= cloudSave.player.playTimeSeconds) localSave else cloudSave
            }
        }
    }
}
