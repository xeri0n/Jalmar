package com.jalmarquest.ui.accessibility

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp

/**
 * Default focus indicator color (golden).
 */
private val FocusColor = Color(0xFFFFD700) // JQColors.PrimaryGold

/**
 * Adds keyboard navigation support to a composable.
 * 
 * Features:
 * - Tab/Shift+Tab navigation
 * - Enter/Space activation
 * - Escape to close/dismiss
 * - Visual focus indicator (golden border)
 * - Screen reader support via semantics
 * 
 * @param enabled Whether keyboard navigation is enabled
 * @param contentDescription Accessibility description for screen readers
 * @param onActivate Called when Enter or Space is pressed
 * @param onEscape Called when Escape is pressed (optional)
 * @param focusRequester Optional FocusRequester for programmatic focus control
 */
@Composable
fun Modifier.keyboardNavigable(
    enabled: Boolean = true,
    contentDescription: String? = null,
    onActivate: (() -> Unit)? = null,
    onEscape: (() -> Unit)? = null,
    focusRequester: FocusRequester? = null
): Modifier {
    if (!enabled) return this
    
    var isFocused by remember { mutableStateOf(false) }
    
    return this
        .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
        .focusable()
        .onPreviewKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.Enter, Key.NumPadEnter, Key.Spacebar -> {
                        onActivate?.invoke()
                        true
                    }
                    Key.Escape -> {
                        onEscape?.invoke()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
        .then(
            if (isFocused) {
                Modifier.border(3.dp, FocusColor)
            } else {
                Modifier
            }
        )
        .then(
            if (contentDescription != null) {
                Modifier.semantics {
                    this.contentDescription = contentDescription
                }
            } else {
                Modifier
            }
        )
}

/**
 * Focus indicator modifier - adds visual highlight when focused.
 * Use this for custom focus styling instead of default border.
 * 
 * @param focusedColor Border color when focused
 * @param unfocusedColor Border color when unfocused (default: transparent)
 */
@Composable
fun Modifier.focusIndicator(
    focusedColor: Color = FocusColor,
    unfocusedColor: Color = Color.Transparent
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    
    return this
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
        .border(
            width = if (isFocused) 3.dp else 1.dp,
            color = if (isFocused) focusedColor else unfocusedColor
        )
}

/**
 * Focus group for managing tab navigation order.
 * 
 * Example:
 * ```
 * val focusRequesters = remember { List(5) { FocusRequester() } }
 * FocusGroup(focusRequesters) { index ->
 *     Button(
 *         onClick = { ... },
 *         modifier = Modifier.keyboardNavigable(focusRequester = focusRequesters[index])
 *     ) { Text("Button $index") }
 * }
 * ```
 */
@Composable
fun FocusGroup(
    focusRequesters: List<FocusRequester>,
    content: @Composable (index: Int) -> Unit
) {
    focusRequesters.forEachIndexed { index, focusRequester ->
        val nextIndex = (index + 1) % focusRequesters.size
        val prevIndex = (index - 1 + focusRequesters.size) % focusRequesters.size
        
        key(index) {
            content(index)
        }
    }
}

/**
 * Auto-focus first element on composition.
 * Use in dialogs, menus, and screens to improve keyboard accessibility.
 * 
 * @param focusRequester FocusRequester for the element to focus
 */
@Composable
fun AutoFocus(focusRequester: FocusRequester) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
