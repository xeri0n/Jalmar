package com.jalmarquest.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.io.File

actual object ImageLoader {
    actual fun loadImageBitmap(path: String): ImageBitmap? {
        return try {
            // Try loading from resources first
            val resourceStream = javaClass.classLoader.getResourceAsStream(path)
            if (resourceStream != null) {
                val bytes = resourceStream.readBytes()
                Image.makeFromEncoded(bytes).toComposeImageBitmap()
            } else {
                // Try as absolute file path
                val file = File(path)
                if (file.exists()) {
                    val bytes = file.readBytes()
                    Image.makeFromEncoded(bytes).toComposeImageBitmap()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println("Failed to load image from $path: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
