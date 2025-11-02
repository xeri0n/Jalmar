package dev.xeri.jalmarquest.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon

// On Android, pointer icons don't apply (touch interface)
internal actual fun Modifier.pointerHoverIcon(icon: PointerIcon): Modifier = this

// On Android, hover events don't apply (touch interface)
internal actual fun Modifier.onHover(onHover: (Boolean) -> Unit): Modifier = this
