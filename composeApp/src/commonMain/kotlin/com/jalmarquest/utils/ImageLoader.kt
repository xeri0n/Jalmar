package com.jalmarquest.utils

import androidx.compose.ui.graphics.ImageBitmap

expect object ImageLoader {
    fun loadImageBitmap(path: String): ImageBitmap?
}
