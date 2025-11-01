package com.jalmarquest.shared.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileIO {
    private val baseDir = File(System.getProperty("user.home"), ".jalmarquest")
    
    init {
        baseDir.mkdirs()
    }
    
    actual suspend fun writeFile(filename: String, content: String) {
        withContext(Dispatchers.IO) {
            val file = File(baseDir, filename)
            file.parentFile?.mkdirs()
            file.writeText(content)
        }
    }
    
    actual suspend fun readFile(filename: String): String? {
        return withContext(Dispatchers.IO) {
            val file = File(baseDir, filename)
            if (file.exists()) file.readText() else null
        }
    }
    
    actual suspend fun deleteFile(filename: String) {
        withContext(Dispatchers.IO) {
            val file = File(baseDir, filename)
            file.delete()
        }
    }
    
    actual suspend fun fileExists(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            File(baseDir, filename).exists()
        }
    }
    
    actual suspend fun listFiles(directory: String): List<String>? {
        return withContext(Dispatchers.IO) {
            val dir = File(baseDir, directory)
            dir.listFiles()?.map { it.name }
        }
    }
    
    actual suspend fun getFileSize(filename: String): Long {
        return withContext(Dispatchers.IO) {
            val file = File(baseDir, filename)
            if (file.exists()) file.length() else 0L
        }
    }
}
