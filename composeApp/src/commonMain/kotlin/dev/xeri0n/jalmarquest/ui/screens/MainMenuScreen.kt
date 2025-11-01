package dev.xeri0n.jalmarquest.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.xeri0n.jalmarquest.ui.components.*
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * Main Menu Screen
 * 
 * AAA-tier main menu with:
 * - Animated background gradients
 * - Floating particle effects (future)
 * - Parallax title (future)
 * - Smooth button transitions
 * - Accessibility support
 * 
 * @param onNewGame Callback when New Game is clicked
 * @param onLoadGame Callback when Load Game is clicked
 * @param onSettings Callback when Settings is clicked
 * @param onQuit Callback when Quit is clicked
 */
@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit,
    onLoadGame: () -> Unit,
    onSettings: () -> Unit,
    onQuit: () -> Unit
) {
    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "background_gradient")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )
    
    // Create animated gradient colors
    val gradientColors = listOf(
        BackgroundDark,
        QuailBrownDark.copy(alpha = 0.3f),
        GrassGreenDark.copy(alpha = 0.2f),
        BackgroundDark
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = animatedOffset * 500f,
                    endY = (animatedOffset + 1f) * 1000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game Title
            GameTitle()
            
            Spacer(modifier = Modifier.height(Spacing.massive))
            
            // Menu Buttons
            MenuButtonsColumn(
                onNewGame = onNewGame,
                onLoadGame = onLoadGame,
                onSettings = onSettings,
                onQuit = onQuit
            )
            
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            
            // Version info (bottom)
            Text(
                text = "Version 0.1.0-alpha | Milestone 6 in progress",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Animated Game Title
 */
@Composable
private fun GameTitle() {
    // Fade in animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "title_fade_in"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(Spacing.huge)
    ) {
        // Main title
        Text(
            text = "JALMARQUEST",
            style = MaterialTheme.typography.displayLarge,
            color = SeedGold.copy(alpha = alpha),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        // Subtitle
        Text(
            text = "A Tiny Hero's Big Adventure",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha * 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        // Quail emoji/icon
        Text(
            text = "🐦",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(Spacing.medium)
        )
    }
}

/**
 * Menu Buttons Column with staggered fade-in animations
 */
@Composable
private fun MenuButtonsColumn(
    onNewGame: () -> Unit,
    onLoadGame: () -> Unit,
    onSettings: () -> Unit,
    onQuit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.mediumLarge),
        modifier = Modifier.widthIn(max = 400.dp)
    ) {
        // New Game Button (gradient, prominent)
        AnimatedMenuButton(delay = 0) {
            JQGradientButton(
                text = "New Game",
                onClick = onNewGame,
                modifier = Modifier.fillMaxWidth(),
                gradient = Brush.horizontalGradient(
                    colors = listOf(GrassGreen, GrassGreenLight)
                ),
                leadingIcon = {
                    Text("⚔️", style = MaterialTheme.typography.titleMedium)
                }
            )
        }
        
        // Load Game Button
        AnimatedMenuButton(delay = 100) {
            JQButton(
                text = "Load Game",
                onClick = onLoadGame,
                modifier = Modifier.fillMaxWidth(),
                variant = JQButtonVariant.SECONDARY,
                size = JQButtonSize.LARGE,
                leadingIcon = {
                    Text("💾", style = MaterialTheme.typography.titleMedium)
                }
            )
        }
        
        // Settings Button
        AnimatedMenuButton(delay = 200) {
            JQButton(
                text = "Settings",
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth(),
                variant = JQButtonVariant.SECONDARY,
                size = JQButtonSize.LARGE,
                leadingIcon = {
                    Text("⚙️", style = MaterialTheme.typography.titleMedium)
                }
            )
        }
        
        // Quit Button
        AnimatedMenuButton(delay = 300) {
            JQButton(
                text = "Quit",
                onClick = onQuit,
                modifier = Modifier.fillMaxWidth(),
                variant = JQButtonVariant.TERTIARY,
                size = JQButtonSize.LARGE,
                leadingIcon = {
                    Text("🚪", style = MaterialTheme.typography.titleMedium)
                }
            )
        }
    }
}

/**
 * Wrapper for menu button with fade-in animation
 */
@Composable
private fun AnimatedMenuButton(
    delay: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "button_fade_in"
    )
    
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_slide_in"
    )
    
    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .then(Modifier.run {
                if (alpha < 1f) this.fillMaxWidth().height(ComponentSize.buttonHeightLarge)
                else this
            })
    ) {
        if (alpha > 0f) {
            Box(modifier = Modifier.graphicsLayer(alpha = alpha)) {
                content()
            }
        }
    }
}
