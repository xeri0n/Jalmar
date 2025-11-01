package com.jalmarquest.shared.persistence

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages automatic backups of save files.
 * Creates backups before overwriting saves, maintains configurable backup count,
 * enables restoration of previous saves.
 */
class BackupManager(
    private val fileIO: FileIOInterface,
    private val maxBackups: Int = 5
) {
    private val mutex = Mutex()
    private val backupDirectory = "saves/backups"
    private var sequenceCounter = 0 // Prevents timestamp collisions
    
    companion object {
        const val BACKUP_EXTENSION = ".backup"
    }
    
    /**
     * Create a backup of the current save before overwriting.
     * 
     * @param slotName The save slot to backup
     * @return Result with backup filename or error
     */
    suspend fun createBackup(slotName: String): Result<String> {
        return mutex.withLock {
            try {
                // Check if original save exists
                val originalFile = "saves/$slotName.jqsave"
                if (!fileIO.fileExists(originalFile)) {
                    return Result.failure(BackupException.SaveNotFound("Save file not found: $slotName"))
                }
                
                // Read original save content
                val content = fileIO.readFile(originalFile)
                    ?: return Result.failure(BackupException.BackupFailed("Failed to read save file"))
                
                // Generate backup filename with timestamp and sequence counter
                val timestamp = System.currentTimeMillis()
                val sequence = sequenceCounter++
                val backupFilename = "$backupDirectory/${slotName}_${timestamp}_${sequence}$BACKUP_EXTENSION"
                
                // Write backup
                fileIO.writeFile(backupFilename, content)
                
                // Cleanup old backups
                cleanupOldBackups(slotName)
                
                Result.success(backupFilename)
            } catch (e: Exception) {
                Result.failure(BackupException.BackupFailed("Backup creation failed: ${e.message}"))
            }
        }
    }
    
    /**
     * Restore a backup to the main save slot.
     * 
     * @param backupFilename The backup file to restore
     * @param slotName The target save slot
     * @return Result with success or error
     */
    suspend fun restoreBackup(backupFilename: String, slotName: String): Result<Unit> {
        return mutex.withLock {
            try {
                // Validate backup exists
                if (!fileIO.fileExists(backupFilename)) {
                    return Result.failure(BackupException.BackupNotFound("Backup file not found"))
                }
                
                // Read backup content
                val content = fileIO.readFile(backupFilename)
                    ?: return Result.failure(BackupException.RestoreFailed("Failed to read backup"))
                
                // Write to main save slot
                val saveFilename = "saves/$slotName.jqsave"
                fileIO.writeFile(saveFilename, content)
                
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(BackupException.RestoreFailed("Restore failed: ${e.message}"))
            }
        }
    }
    
    /**
     * List all backups for a specific save slot.
     * 
     * @param slotName The save slot to list backups for
     * @return List of backup metadata sorted by timestamp (newest first)
     */
    suspend fun listBackups(slotName: String): List<BackupInfo> {
        return mutex.withLock {
            try {
                val backups = mutableListOf<BackupInfo>()
                val files = fileIO.listFiles(backupDirectory) ?: return emptyList()
                
                val prefix = "${slotName}_"
                files.filter { it.startsWith(prefix) && it.endsWith(BACKUP_EXTENSION) }
                    .forEach { filename ->
                        // Extract parts from filename: slotname_timestamp[_sequence].backup
                        val core = filename.removePrefix(prefix)
                            .removeSuffix(BACKUP_EXTENSION)
                        val tsPart = core.substringBefore('_')
                        val seqPart = core.substringAfter('_', missingDelimiterValue = "0")
                        val timestamp = tsPart.toLongOrNull()
                        val sequence = seqPart.toLongOrNull() ?: 0L
                        
                        if (timestamp != null) {
                            val fullPath = "$backupDirectory/$filename"
                            val size = fileIO.getFileSize(fullPath)
                            
                            backups.add(
                                BackupInfo(
                                    filename = fullPath,
                                    slotName = slotName,
                                    timestamp = timestamp,
                                    sizeBytes = size
                                )
                            )
                        }
                    }
                
                // Sort by timestamp desc, then by sequence desc to keep latest first
                backups.sortedWith(compareByDescending<BackupInfo> { it.timestamp }
                    .thenByDescending { extractSequence(it.filename, it.slotName) })
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun extractSequence(fullPath: String, slotName: String): Long {
        // fullPath: saves/backups/slot_timestamp_sequence.backup
        val name = fullPath.substringAfterLast('/')
        val core = name.removePrefix("${slotName}_").removeSuffix(BACKUP_EXTENSION)
        val seqPart = core.substringAfter('_', missingDelimiterValue = "0")
        return seqPart.toLongOrNull() ?: 0L
    }
    
    /**
     * Delete a specific backup file.
     * 
     * @param backupFilename The backup file to delete
     * @return Result with success or error
     */
    suspend fun deleteBackup(backupFilename: String): Result<Unit> {
        return mutex.withLock {
            try {
                if (!fileIO.fileExists(backupFilename)) {
                    return Result.failure(BackupException.BackupNotFound("Backup not found"))
                }
                
                fileIO.deleteFile(backupFilename)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(BackupException.BackupFailed("Failed to delete backup: ${e.message}"))
            }
        }
    }
    
    /**
     * Delete all backups for a specific save slot.
     * 
     * @param slotName The save slot to clear backups for
     * @return Result with count of deleted backups or error
     */
    /**
     * Delete all backups for a save slot.
     * 
     * @param slotName The save slot to clear backups for
     * @return Result with count of deleted backups or error
     */
    suspend fun clearBackups(slotName: String): Result<Int> {
        return mutex.withLock {
            try {
                // Get list of backup files inline (avoid mutex deadlock)
                val files = fileIO.listFiles(backupDirectory) ?: emptyList()
                val prefix = "${slotName}_"
                val backupFiles = files.filter { it.startsWith(prefix) && it.endsWith(BACKUP_EXTENSION) }
                
                var deletedCount = 0
                backupFiles.forEach { filename ->
                    try {
                        fileIO.deleteFile("$backupDirectory/$filename")
                        deletedCount++
                    } catch (e: Exception) {
                        // Continue deleting other backups even if one fails
                    }
                }
                
                Result.success(deletedCount)
            } catch (e: Exception) {
                Result.failure(BackupException.BackupFailed("Failed to clear backups: ${e.message}"))
            }
        }
    }
    
    /**
     * Keep only the most recent N backups, delete older ones.
     * Called automatically after each backup creation.
     */
    /**
     * Clean up old backups to maintain max backup count.
     * MUST be called from within mutex.withLock {} - does NOT acquire mutex itself.
     * 
     * @param slotName The save slot to clean up
     */
    private suspend fun cleanupOldBackups(slotName: String) {
        try {
            // Get list of backup files (inline without calling listBackups to avoid mutex deadlock)
            val files = fileIO.listFiles(backupDirectory) ?: return
            val prefix = "${slotName}_"
            val backupFiles = files.filter { it.startsWith(prefix) && it.endsWith(BACKUP_EXTENSION) }
                .sortedDescending() // Filenames with timestamps sort naturally (newest first)
            
            // Delete backups beyond max count
            if (backupFiles.size > maxBackups) {
                backupFiles.drop(maxBackups).forEach { filename ->
                    fileIO.deleteFile("$backupDirectory/$filename")
                }
            }
        } catch (e: Exception) {
            // Cleanup failure is non-critical, log but don't throw
        }
    }
}

/**
 * Information about a backup file.
 */
data class BackupInfo(
    val filename: String,
    val slotName: String,
    val timestamp: Long,
    val sizeBytes: Long
) {
    /**
     * Format timestamp for UI display.
     */
    fun formattedTimestamp(): String {
        val date = kotlinx.datetime.Instant.fromEpochMilliseconds(timestamp)
        return date.toString()  // ISO 8601 format
    }
    
    /**
     * Format file size for UI display.
     */
    fun formattedSize(): String {
        return when {
            sizeBytes < 1024 -> "$sizeBytes B"
            sizeBytes < 1024 * 1024 -> "${sizeBytes / 1024} KB"
            else -> "${sizeBytes / (1024 * 1024)} MB"
        }
    }
}

/**
 * Backup-related exceptions.
 */
sealed class BackupException(message: String) : Exception(message) {
    class SaveNotFound(message: String) : BackupException(message)
    class BackupNotFound(message: String) : BackupException(message)
    class BackupFailed(message: String) : BackupException(message)
    class RestoreFailed(message: String) : BackupException(message)
}
