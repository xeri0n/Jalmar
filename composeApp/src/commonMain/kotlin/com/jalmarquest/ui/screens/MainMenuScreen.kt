package com.jalmarquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmarquest.utils.ImageLoader

/**
 * Main Menu Screen for JalmarQuest
 * Features the beautiful artwork background with fantasy-styled UI overlay
 */
@Composable
fun MainMenuScreen(
    onStartDungeonCrawler: () -> Unit,
    onStartTileExplorer: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backgroundImage by remember { mutableStateOf<ImageBitmap?>(null) }
    
    LaunchedEffect(Unit) {
        // Try multiple resource paths
        val paths = listOf(
            "jalmarquest_background.png",
            "drawable/jalmarquest_background.png",
            "composeResources/drawable/jalmarquest_background.png",
            "resources/jalmarquest_background.png"
        )
        
        for (path in paths) {
            val loadedImage = ImageLoader.loadImageBitmap(path)
            if (loadedImage != null) {
                println("Successfully loaded image from: $path")
                backgroundImage = loadedImage
                break
            } else {
                println("Failed to load image from: $path")
            }
        }
    }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background image - your beautiful JalmarQuest artwork
        backgroundImage?.let { bitmap ->
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = "JalmarQuest - Enchanted Forest with Jalmar and Blue Butterfly",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            // Fallback gradient while loading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0d1a0d),
                                Color(0xFF1a2a1a),
                                Color(0xFF2d4a2d),
                                Color(0xFF3a5a3a),
                                Color(0xFF4a6a4a)
                            )
                        )
                    )
            )
        }
        
        // UI Overlay - Title at top, buttons on right
        Box(modifier = Modifier.fillMaxSize()) {
            // Game Title - Top Center with magical carved wood effect
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            ) {
                MagicalTitle(text = "Jalmar Quest")
            }
            
            // Menu Buttons - Right side with stone/mushroom aesthetic
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.End
            ) {
                FantasyMenuButton(
                    text = "Start Game",
                    onClick = onStartTileExplorer,
                    isSelected = true
                )
                
                FantasyMenuButton(
                    text = "Load Game",
                    onClick = { /* TODO */ }
                )
                
                FantasyMenuButton(
                    text = "Settings",
                    onClick = { /* TODO */ }
                )
                
                FantasyMenuButton(
                    text = "Quit",
                    onClick = onQuit
                )
            }
        }
    }
}

/**
 * Magical carved wood/golden title effect
 */
@Composable
private fun MagicalTitle(text: String) {
    Text(
        text = text,
        fontSize = 72.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFFFFD700), // Polished gold
        style = MaterialTheme.typography.displayLarge.copy(
            shadow = Shadow(
                color = Color(0xFF8B4513), // Warm brown shadow (carved wood)
                offset = Offset(6f, 6f),
                blurRadius = 12f
            ),
            letterSpacing = 4.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFFFFAA00),
                spotColor = Color(0xFFFFDD88)
            )
    )
}

/**
 * Fantasy-styled menu button - polished river stone / glowing mushroom cap aesthetic
 */
@Composable
private fun FantasyMenuButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    // Determine glow based on selected or hover state
    val showGlow = isSelected || isHovered
    val glowColor = if (isSelected) Color(0xFFFFDD88) else Color(0xFFFFEEAA)
    
    Box(
        modifier = modifier
            .width(280.dp)
            .height(70.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Outer glow effect (firefly-yellow for selected/hover)
        if (showGlow) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 0.dp, y = 0.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(35.dp),
                        ambientColor = glowColor,
                        spotColor = glowColor
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(35.dp)
                    )
            )
        }
        
        // Main button - polished stone/mushroom cap
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFD2B48C), // Tan (polished stone top)
                            Color(0xFFA0826D)  // Darker tan (stone bottom)
                        )
                    ),
                    shape = RoundedCornerShape(35.dp)
                )
                .border(
                    width = 3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEED5B7), // Light edge (highlight)
                            Color(0xFF8B7355)  // Dark edge (shadow)
                        )
                    ),
                    shape = RoundedCornerShape(35.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Carved/glowing text
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (showGlow) Color(0xFF2F1810) else Color(0xFF3E2723), // Darker when glowing
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = if (showGlow) {
                            Color(0xFFFFDD88).copy(alpha = 0.8f) // Golden glow
                        } else {
                            Color(0xFFEED5B7).copy(alpha = 0.5f) // Subtle highlight
                        },
                        offset = Offset(0f, -1f),
                        blurRadius = if (showGlow) 8f else 2f
                    ),
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
