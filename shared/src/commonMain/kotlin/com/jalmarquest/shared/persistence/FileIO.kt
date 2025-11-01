package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.GameState

/**
 * Platform-specific file I/O operations.
 * Must be implemented for each platform (Android, iOS, Desktop).
 */
expect class FileIO {
    suspend fun writeFile(filename: String, content: String)
    suspend fun readFile(filename: String): String?
    suspend fun deleteFile(filename: String)
    suspend fun fileExists(filename: String): Boolean
    suspend fun listFiles(directory: String): List<String>?
    suspend fun getFileSize(filename: String): Long
}
