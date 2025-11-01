package com.jalmarquest.shared.persistence

/**
 * Mock FileIO for testing BackupManager.
 * Uses in-memory storage instead of actual file system.
 */
class MockFileIO : FileIOInterface {
    private val storage = mutableMapOf<String, String>()
    
    override suspend fun writeFile(filename: String, content: String) {
        storage[filename] = content
    }
    
    override suspend fun readFile(filename: String): String? {
        return storage[filename]
    }
    
    override suspend fun deleteFile(filename: String) {
        storage.remove(filename)
    }
    
    override suspend fun fileExists(filename: String): Boolean {
        return storage.containsKey(filename)
    }
    
    override suspend fun listFiles(directory: String): List<String>? {
        // Return ONLY filenames (not full paths) for files in the directory
        // This matches the behavior of real FileIO desktop implementation
        val prefix = if (directory.endsWith("/")) directory else "$directory/"
        return storage.keys
            .filter { it.startsWith(prefix) }
            .map { it.removePrefix(prefix) }
            .toList()
    }
    
    override suspend fun getFileSize(filename: String): Long {
        return storage[filename]?.toByteArray()?.size?.toLong() ?: 0L
    }
    
    fun clear() {
        storage.clear()
    }
}
