package com.jalmarquest.shared.persistence

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileIO(private val context: Context) {
    
    actual suspend fun writeFile(filename: String, content: String) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            file.parentFile?.mkdirs()
            file.writeText(content)
        }
    }
    
    actual suspend fun readFile(filename: String): String? {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            if (file.exists()) file.readText() else null
        }
    }
    
    actual suspend fun deleteFile(filename: String) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            file.delete()
        }
    }
    
    actual suspend fun fileExists(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            File(context.filesDir, filename).exists()
        }
    }
    
    actual suspend fun listFiles(directory: String): List<String> {
        return withContext(Dispatchers.IO) {
            val dir = File(context.filesDir, directory)
            dir.listFiles()?.map { it.name } ?: emptyList()
        }
    }
}
