package com.jalmarquest.shared.persistence

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual class FileIO {
    private val baseDir: String by lazy {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        (paths.first() as String) + "/JalmarQuest"
    }
    
    actual suspend fun writeFile(filename: String, content: String) {
        val path = "$baseDir/$filename"
        val dir = (path as NSString).stringByDeletingLastPathComponent
        
        // Create directory if needed
        NSFileManager.defaultManager.createDirectoryAtPath(
            dir,
            true,
            null,
            null
        )
        
        // Write file
        (content as NSString).writeToFile(
            path,
            true,
            NSUTF8StringEncoding,
            null
        )
    }
    
    actual suspend fun readFile(filename: String): String? {
        val path = "$baseDir/$filename"
        return NSString.stringWithContentsOfFile(
            path,
            NSUTF8StringEncoding,
            null
        ) as? String
    }
    
    actual suspend fun deleteFile(filename: String) {
        val path = "$baseDir/$filename"
        NSFileManager.defaultManager.removeItemAtPath(path, null)
    }
    
    actual suspend fun fileExists(filename: String): Boolean {
        val path = "$baseDir/$filename"
        return NSFileManager.defaultManager.fileExistsAtPath(path)
    }
    
    actual suspend fun listFiles(directory: String): List<String> {
        val path = "$baseDir/$directory"
        val contents = NSFileManager.defaultManager.contentsOfDirectoryAtPath(path, null)
        return (contents as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
    }
}
