package com.jalmarquest.shared.persistence

/**
 * Adapter to make FileIO (expect/actual class) compatible with FileIOInterface.
 * Used for dependency injection in production code.
 */
class FileIOAdapter(private val fileIO: FileIO) : FileIOInterface {
    override suspend fun writeFile(filename: String, content: String) {
        fileIO.writeFile(filename, content)
    }
    
    override suspend fun readFile(filename: String): String? {
        return fileIO.readFile(filename)
    }
    
    override suspend fun deleteFile(filename: String) {
        fileIO.deleteFile(filename)
    }
    
    override suspend fun fileExists(filename: String): Boolean {
        return fileIO.fileExists(filename)
    }
    
    override suspend fun listFiles(directory: String): List<String>? {
        return fileIO.listFiles(directory)
    }
    
    override suspend fun getFileSize(filename: String): Long {
        return fileIO.getFileSize(filename)
    }
}
