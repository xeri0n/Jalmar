package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.GameState

/**
 * Cloud sync interface for save file synchronization across devices.
 * Platform-specific implementations:
 * - Android: Google Play Games Services
 * - iOS: iCloud
 * - Desktop: Future third-party service (Steam Cloud, etc.)
 */
expect class CloudSyncManager {
    /**
     * Upload save data to cloud storage.
     * @param slotName The save slot identifier
     * @param content The serialized GameState JSON
     * @return Result with success or sync error
     */
    suspend fun uploadSave(slotName: String, content: String): Result<Unit>
    
    /**
     * Download save data from cloud storage.
     * @param slotName The save slot identifier
     * @return Result with save content or sync error
     */
    suspend fun downloadSave(slotName: String): Result<String>
    
    /**
     * Check if cloud save exists for a slot.
     * @param slotName The save slot identifier
     * @return True if cloud save exists
     */
    suspend fun cloudSaveExists(slotName: String): Boolean
    
    /**
     * Delete cloud save for a slot.
     * @param slotName The save slot identifier
     * @return Result with success or error
     */
    suspend fun deleteCloudSave(slotName: String): Result<Unit>
    
    /**
     * List all cloud saves with metadata.
     * @return List of cloud save metadata
     */
    suspend fun listCloudSaves(): List<CloudSaveInfo>
    
    /**
     * Resolve sync conflict between local and cloud save.
     * @param localSave The local GameState
     * @param cloudSave The cloud GameState
     * @param strategy The conflict resolution strategy
     * @return The resolved GameState
     */
    fun resolveConflict(
        localSave: GameState,
        cloudSave: GameState,
        strategy: ConflictResolutionStrategy
    ): GameState
}

/**
 * Information about a cloud save.
 */
data class CloudSaveInfo(
    val slotName: String,
    val playerName: String,
    val level: Int,
    val timestamp: Long,
    val deviceId: String
)

/**
 * Strategy for resolving conflicts between local and cloud saves.
 */
enum class ConflictResolutionStrategy {
    /**
     * Always use the save with the latest timestamp.
     */
    MOST_RECENT,
    
    /**
     * Always use the local save.
     */
    PREFER_LOCAL,
    
    /**
     * Always use the cloud save.
     */
    PREFER_CLOUD,
    
    /**
     * Use the save with the higher player level.
     */
    HIGHER_LEVEL,
    
    /**
     * Use the save with more playtime.
     */
    MORE_PLAYTIME
}

/**
 * Cloud sync exceptions.
 */
sealed class CloudSyncException(message: String) : Exception(message) {
    class NotAuthenticated(message: String) : CloudSyncException(message)
    class NetworkError(message: String) : CloudSyncException(message)
    class SyncConflict(message: String) : CloudSyncException(message)
    class StorageQuotaExceeded(message: String) : CloudSyncException(message)
    class SyncFailed(message: String) : CloudSyncException(message)
}
