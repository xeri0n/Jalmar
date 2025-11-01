package com.jalmarquest.shared.persistence

/**
 * Interface for file I/O operations.
 * Allows mocking for tests and platform-specific implementations.
 */
interface FileIOInterface {
    suspend fun writeFile(filename: String, content: String)
    suspend fun readFile(filename: String): String?
    suspend fun deleteFile(filename: String)
    suspend fun fileExists(filename: String): Boolean
    suspend fun listFiles(directory: String): List<String>?
    suspend fun getFileSize(filename: String): Long
}
