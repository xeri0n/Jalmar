package dev.xeri0n.jalmarquest.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmarquest.shared.navigation.MapNavigationManager
import com.jalmarquest.shared.navigation.NavigationRoute
import dev.xeri0n.jalmarquest.ui.components.JQButton
import dev.xeri0n.jalmarquest.ui.components.JQButtonVariant
import dev.xeri0n.jalmarquest.ui.components.JQCard
import dev.xeri0n.jalmarquest.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * AAA-Tier Parchment Map Screen
 * 
 * Displays an aged parchment map with:
 * - Animated unfurling effect
 * - Canvas-based vintage map rendering
 * - Animated route visualization (dotted line animation)
 * - Player position marker
 * - Compass rose
 * - Calligraphy-style text
 * - Distance and time estimates
 * 
 * Opens when player uses "Map to Buttonburgh" item from inventory.
 */
@Composable
fun ParchmentMapScreen(
    currentLocationId: String,
    navigationRoute: NavigationRoute?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Unfurling animation (entrance effect)
    val unfurlProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "unfurl_animation"
    )
    
    // Dotted line animation (continuous)
    val dottedLinePhase by rememberInfiniteTransition(label = "dotted_line_transition")
        .animateFloat(
            initialValue = 0f,
            targetValue = 40f,  // Full dash cycle
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "dotted_line_phase"
        )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark.copy(alpha = 0.95f)),  // Dimmed background
        contentAlignment = Alignment.Center
    ) {
        // Parchment map with animated unfurling
        JQCard(
            modifier = Modifier
                .fillMaxWidth(0.9f * unfurlProgress)
                .fillMaxHeight(0.85f * unfurlProgress)
                .graphicsLayer {
                    alpha = unfurlProgress
                    scaleX = unfurlProgress
                    scaleY = unfurlProgress
                },
            backgroundColor = ParchmentBase,
            borderColor = ParchmentBorder
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.large)
            ) {
                // Header: Map title
                MapHeader(
                    destinationName = "Buttonburgh",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                // Main map canvas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (navigationRoute != null && !navigationRoute.isEmpty()) {
                        ParchmentMapCanvas(
                            route = navigationRoute,
                            currentLocationId = currentLocationId,
                            dottedLinePhase = dottedLinePhase,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // No route available (already in Buttonburgh or no path)
                        Text(
                            text = if (currentLocationId.startsWith("buttonburgh")) {
                                "You are already in Buttonburgh!"
                            } else {
                                "No route found to Buttonburgh.\nExplore to discover more paths."
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                color = ParchmentInk
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                // Footer: Route info + Close button
                if (navigationRoute != null && !navigationRoute.isEmpty()) {
                    MapFooter(
                        route = navigationRoute,
                        onClose = onClose,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // Just close button if no route
                    JQButton(
                        onClick = onClose,
                        text = "Close Map",
                        variant = JQButtonVariant.SECONDARY,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

/**
 * Map header with calligraphy title.
 */
@Composable
private fun MapHeader(
    destinationName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Journey to $destinationName",
            style = GameTypography.questTitle.copy(
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = ParchmentInk,
                letterSpacing = 2.sp
            )
        )
        
        // Decorative divider
        Spacer(modifier = Modifier.height(Spacing.small))
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(2.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ParchmentInk.copy(alpha = 0.3f),
                            ParchmentInk.copy(alpha = 0.6f),
                            ParchmentInk.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

/**
 * Canvas-based parchment map rendering with route visualization.
 */
@Composable
private fun ParchmentMapCanvas(
    route: NavigationRoute,
    currentLocationId: String,
    dottedLinePhase: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        // Draw aged parchment texture (subtle noise pattern)
        drawParchmentTexture()
        
        // Draw compass rose in top-right corner
        drawCompassRose(
            center = Offset(canvasWidth - 80f, 80f),
            radius = 50f
        )
        
        // Draw route if waypoints exist
        if (route.waypoints.isNotEmpty()) {
            // Calculate positions for waypoints (scaled to canvas)
            val positions = calculateWaypointPositions(
                waypoints = route.waypoints,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight
            )
            
            // Draw animated route line (dotted)
            drawRouteLine(
                positions = positions,
                dottedLinePhase = dottedLinePhase
            )
            
            // Draw waypoint markers
            positions.forEachIndexed { index, position ->
                val waypoint = route.waypoints[index]
                val isDestination = index == positions.lastIndex
                val isCurrentLocation = waypoint.locationId == currentLocationId
                
                drawWaypointMarker(
                    position = position,
                    label = waypoint.locationName,
                    isDestination = isDestination,
                    isCurrentLocation = isCurrentLocation,
                    textMeasurer = textMeasurer
                )
            }
        }
    }
}

/**
 * Draws subtle parchment texture (aged paper effect).
 */
private fun DrawScope.drawParchmentTexture() {
    // Draw subtle gradient overlay for depth
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                ParchmentBase.copy(alpha = 0f),
                ParchmentBase.copy(alpha = 0.1f),
                ParchmentBase.copy(alpha = 0.2f)
            ),
            center = Offset(size.width * 0.3f, size.height * 0.3f),
            radius = size.width * 0.8f
        )
    )
}

/**
 * Draws ornate compass rose (N/S/E/W indicator).
 */
private fun DrawScope.drawCompassRose(center: Offset, radius: Float) {
    val compassInk = ParchmentInk.copy(alpha = 0.6f)
    
    // Outer circle
    drawCircle(
        color = compassInk,
        radius = radius,
        center = center,
        style = Stroke(width = 2f)
    )
    
    // Cardinal directions (N, S, E, W)
    val directions = listOf("N", "E", "S", "W")
    directions.forEachIndexed { index, direction ->
        val angle = index * 90f - 90f  // Start at North (top)
        val radians = Math.toRadians(angle.toDouble())
        
        // Draw direction line
        val lineEnd = Offset(
            x = center.x + (radius * 0.8f * cos(radians)).toFloat(),
            y = center.y + (radius * 0.8f * sin(radians)).toFloat()
        )
        
        drawLine(
            color = compassInk,
            start = center,
            end = lineEnd,
            strokeWidth = 2f
        )
        
        // Direction arrow tip
        if (direction == "N") {
            // North gets special arrow decoration
            val arrowTip = Offset(
                x = center.x,
                y = center.y - radius
            )
            drawCircle(
                color = SeedGold,
                radius = 6f,
                center = arrowTip
            )
        }
    }
}

/**
 * Calculates screen positions for waypoints based on their grid coordinates.
 */
private fun calculateWaypointPositions(
    waypoints: List<com.jalmarquest.shared.navigation.NavigationWaypoint>,
    canvasWidth: Float,
    canvasHeight: Float
): List<Offset> {
    if (waypoints.isEmpty()) return emptyList()
    
    // Find min/max coordinates for scaling
    val minX = waypoints.minOf { it.gridX }
    val maxX = waypoints.maxOf { it.gridX }
    val minY = waypoints.minOf { it.gridY }
    val maxY = waypoints.maxOf { it.gridY }
    
    // Add padding
    val paddingX = canvasWidth * 0.15f
    val paddingY = canvasHeight * 0.15f
    
    val scaleX = (canvasWidth - 2 * paddingX) / (maxX - minX).coerceAtLeast(1)
    val scaleY = (canvasHeight - 2 * paddingY) / (maxY - minY).coerceAtLeast(1)
    
    return waypoints.map { waypoint ->
        Offset(
            x = paddingX + (waypoint.gridX - minX) * scaleX,
            y = paddingY + (waypoint.gridY - minY) * scaleY
        )
    }
}

/**
 * Draws animated dotted route line connecting waypoints.
 */
private fun DrawScope.drawRouteLine(
    positions: List<Offset>,
    dottedLinePhase: Float
) {
    for (i in 0 until positions.size - 1) {
        drawLine(
            color = GrassGreen,
            start = positions[i],
            end = positions[i + 1],
            strokeWidth = 4f,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(20f, 20f),  // 20px dash, 20px gap
                phase = dottedLinePhase
            )
        )
    }
}

/**
 * Draws waypoint marker with label.
 */
private fun DrawScope.drawWaypointMarker(
    position: Offset,
    label: String,
    isDestination: Boolean,
    isCurrentLocation: Boolean,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    // Marker circle
    val markerColor = when {
        isCurrentLocation -> QuailBrown  // Player location
        isDestination -> SeedGold  // Buttonburgh
        else -> GrassGreen  // Waypoint
    }
    
    val markerRadius = if (isDestination || isCurrentLocation) 12f else 8f
    
    // Outer glow
    drawCircle(
        color = markerColor.copy(alpha = 0.3f),
        radius = markerRadius + 6f,
        center = position
    )
    
    // Main marker
    drawCircle(
        color = markerColor,
        radius = markerRadius,
        center = position
    )
    
    // Inner highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.5f),
        radius = markerRadius * 0.4f,
        center = position.copy(x = position.x - 2f, y = position.y - 2f)
    )
    
    // Label text (below marker)
    if (isDestination || isCurrentLocation) {
        val textLayoutResult = textMeasurer.measure(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = if (isDestination) FontWeight.Bold else FontWeight.Normal,
                color = ParchmentInk
            )
        )
        
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = position.x - textLayoutResult.size.width / 2,
                y = position.y + markerRadius + 8f
            )
        )
    }
}

/**
 * Map footer with route statistics and close button.
 */
@Composable
private fun MapFooter(
    route: NavigationRoute,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Route statistics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Distance
            RouteStatCard(
                icon = "📏",
                label = "Distance",
                value = "${route.straightLineDistance} units"
            )
            
            // Time
            RouteStatCard(
                icon = "⏱️",
                label = "Travel Time",
                value = route.formattedTime()
            )
            
            // Stamina
            RouteStatCard(
                icon = "⚡",
                label = "Stamina Cost",
                value = "${route.totalStaminaCost}"
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        // Close button
        JQButton(
            onClick = onClose,
            text = "Close Map",
            variant = JQButtonVariant.PRIMARY,
            modifier = Modifier.width(200.dp)
        )
    }
}

/**
 * Individual route statistic card.
 */
@Composable
private fun RouteStatCard(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Serif,
                color = ParchmentInk.copy(alpha = 0.7f)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = ParchmentInk
            )
        )
    }
}

// Parchment color constants
private val ParchmentBase = Color(0xFFF4E8D0)  // Aged paper color
private val ParchmentBorder = Color(0xFF8B7355)  // Brown border
private val ParchmentInk = Color(0xFF2C1810)  // Sepia ink color
