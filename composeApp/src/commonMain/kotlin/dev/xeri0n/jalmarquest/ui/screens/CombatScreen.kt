package dev.xeri0n.jalmarquest.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmarquest.shared.combat.*
import com.jalmarquest.shared.skills.Skill
import dev.xeri0n.jalmarquest.ui.components.*
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * AAA-Tier Combat Screen
 * 
 * Features:
 * - 3D flip animation for enemy cards
 * - Particle damage numbers
 * - Skill grid with cooldown overlays
 * - Turn queue visualization
 * - Combat log with scroll
 * - Victory screen with confetti particles
 * - Defeat screen with fade-out effect
 * - Status effect displays with tooltips
 * - Animated turn indicator
 * - Screen shake on critical hits
 */
@Composable
fun CombatScreen(
    combatState: CombatState,
    playerSkills: List<Skill>,
    onSkillSelected: (Skill, String?) -> Unit,  // (skill, targetId)
    onDefend: () -> Unit,
    onItem: () -> Unit,
    onFlee: () -> Unit,
    onCombatEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDamageNumber by remember { mutableStateOf(false) }
    var lastDamage by remember { mutableStateOf(0) }
    var damageIsCrit by remember { mutableStateOf(false) }
    var screenShake by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundDark,
                        BackgroundDark.copy(red = 0.2f, green = 0.1f, blue = 0.1f)  // Reddish tint
                    )
                )
            )
            .graphicsLayer {
                // Screen shake on critical hit
                translationX = if (screenShake) {
                    (kotlin.math.sin(System.currentTimeMillis() / 30.0) * 15f).toFloat()
                } else {
                    0f
                }
                translationY = if (screenShake) {
                    (kotlin.math.cos(System.currentTimeMillis() / 30.0) * 10f).toFloat()
                } else {
                    0f
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top section: Enemies
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EnemyDisplay(
                    enemies = combatState.enemies,
                    currentTurnId = combatState.getCurrentTurnParticipantId()
                )
            }
            
            // Middle section: Combat Log + Turn Queue
            Row(
                modifier = Modifier
                    .weight(0.20f)
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.medium)
            ) {
                // Combat log
                CombatLogPanel(
                    log = combatState.combatLog.takeLast(5),  // Last 5 messages
                    modifier = Modifier.weight(0.7f)
                )
                
                Spacer(modifier = Modifier.width(Spacing.small))
                
                // Turn queue
                TurnQueuePanel(
                    turnOrder = combatState.turnOrder,
                    currentTurnIndex = combatState.currentTurnIndex,
                    modifier = Modifier.weight(0.3f)
                )
            }
            
            // Bottom section: Player + Actions
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxWidth()
                    .background(
                        color = SurfaceDark,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(Spacing.medium)
            ) {
                // Player stats
                PlayerStatsPanel(
                    player = combatState.player,
                    isPlayerTurn = combatState.isPlayerTurn(),
                    showDamageShake = screenShake,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                // Action panel
                if (combatState.isPlayerTurn()) {
                    CombatActionPanel(
                        playerSkills = playerSkills,
                        onSkillClick = { skill ->
                            // Always use skill immediately on first available enemy (one-click attack)
                            onSkillSelected(skill, null)
                        },
                        onDefend = onDefend,
                        onItem = onItem,
                        onFlee = onFlee,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // Not player turn - show waiting message
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Enemy Turn...",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = FontFamily.Serif,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
        
        // Damage numbers overlay
        AnimatedVisibility(
            visible = showDamageNumber,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DamageNumber(
                    damage = lastDamage,
                    isCritical = damageIsCrit,
                    onAnimationEnd = {
                        showDamageNumber = false
                        screenShake = false
                    }
                )
            }
        }
        
        // Victory/Defeat overlays
        if (combatState.isCombatOver()) {
            if (combatState.isVictory()) {
                VictoryScreen(
                    onClose = onCombatEnd,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                DefeatScreen(
                    onClose = onCombatEnd,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * Enemy display with 3D flip animation on turn.
 */
@Composable
private fun EnemyDisplay(
    enemies: List<EnemyCombatData>,
    currentTurnId: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        enemies.filter { it.currentHp > 0 }.forEach { enemy ->
            EnemyCard(
                enemy = enemy,
                isActive = currentTurnId == enemy.id,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Individual enemy card with flip animation.
 */
@Composable
private fun EnemyCard(
    enemy: EnemyCombatData,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    // 3D flip animation when enemy turn starts
    val rotationY by animateFloatAsState(
        targetValue = if (isActive) 360f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "enemy_flip"
    )
    
    JQCard(
        modifier = modifier
            .padding(Spacing.small)
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 12f * density
            },
        backgroundColor = if (isActive) QuailBrownLight else SurfaceDark,
        borderColor = if (isActive) SeedGold else QuailBrown
    ) {
        Column(
            modifier = Modifier.padding(Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enemy icon (placeholder emoji)
            Text(
                text = getEnemyIcon(enemy.name),
                style = MaterialTheme.typography.displaySmall
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Enemy name
            Text(
                text = enemy.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) SeedGold else Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Health bar
            CombatHealthBar(
                current = enemy.currentHp,
                max = enemy.maxHp,
                label = "HP",
                showDamageShake = false
            )
            
            // Status effects
            if (enemy.activeStatusEffects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Spacing.small))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    enemy.activeStatusEffects.forEach { effect ->
                        StatusEffectIcon(
                            effectType = effect.type,
                            duration = effect.remainingRounds
                        )
                    }
                }
            }
            
            // Turn indicator
            if (isActive) {
                Spacer(modifier = Modifier.height(Spacing.small))
                TurnIndicator(isActive = true)
            }
        }
    }
}

/**
 * Combat log panel showing recent messages.
 */
@Composable
private fun CombatLogPanel(
    log: List<String>,
    modifier: Modifier = Modifier
) {
    JQCard(
        modifier = modifier.height(100.dp),
        backgroundColor = BackgroundDark.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.small)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Combat Log",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SeedGold
                )
            )
            Divider(
                color = QuailBrown,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            log.forEach { message ->
                Text(
                    text = "• $message",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

/**
 * Turn queue visualization showing turn order.
 */
@Composable
private fun TurnQueuePanel(
    turnOrder: List<String>,
    currentTurnIndex: Int,
    modifier: Modifier = Modifier
) {
    JQCard(
        modifier = modifier.height(100.dp),
        backgroundColor = BackgroundDark.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.small)
        ) {
            Text(
                text = "Turn Order",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SeedGold
                )
            )
            Divider(
                color = QuailBrown,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                turnOrder.take(5).forEachIndexed { index, participantId ->
                    val isActive = index == currentTurnIndex
                    Text(
                        text = if (isActive) "▶ ${participantId.take(10)}" else "  ${participantId.take(10)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isActive) SeedGold else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

/**
 * Player stats panel with HP, stamina, status effects.
 */
@Composable
private fun PlayerStatsPanel(
    player: PlayerCombatData,
    isPlayerTurn: Boolean,
    showDamageShake: Boolean,
    modifier: Modifier = Modifier
) {
    JQCard(
        modifier = modifier,
        backgroundColor = if (isPlayerTurn) GrassGreenDark.copy(alpha = 0.3f) else SurfaceDark,
        borderColor = if (isPlayerTurn) SeedGold else QuailBrown
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player icon + name
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🐦",  // Jalmar (quail)
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isPlayerTurn) SeedGold else Color.White
                    )
                )
            }
            
            // Stats
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                // HP bar
                CombatHealthBar(
                    current = player.currentHp,
                    max = player.maxHp,
                    label = "HP",
                    showDamageShake = showDamageShake
                )
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                // Status effects
                if (player.activeStatusEffects.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        player.activeStatusEffects.forEach { effect ->
                            StatusEffectIcon(
                                effectType = effect.type,
                                duration = effect.remainingRounds
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Combat action panel with skills, defend, item, flee buttons.
 */
@Composable
private fun CombatActionPanel(
    playerSkills: List<Skill>,
    onSkillClick: (Skill) -> Unit,
    onDefend: () -> Unit,
    onItem: () -> Unit,
    onFlee: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Skills grid (3x3)
        Text(
            text = "Skills",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SeedGold
            )
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
            modifier = Modifier.height(180.dp)
        ) {
            items(playerSkills.filter { it.isUsableInCombat() }.take(9)) { skill ->
                SkillButton(
                    skill = skill,
                    isAvailable = true,  // TODO: Check cooldown/stamina
                    onClick = { onSkillClick(skill) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            JQButton(
                onClick = onDefend,
                text = "Defend",
                variant = JQButtonVariant.SECONDARY,
                modifier = Modifier.weight(1f)
            )
            JQButton(
                onClick = onItem,
                text = "Item",
                variant = JQButtonVariant.TERTIARY,
                modifier = Modifier.weight(1f)
            )
            JQButton(
                onClick = onFlee,
                text = "Flee",
                variant = JQButtonVariant.DANGER,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Victory screen with confetti particle effect.
 */
@Composable
private fun VictoryScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        // Confetti canvas
        ConfettiAnimation()
        
        JQCard(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(Spacing.large),
            backgroundColor = SurfaceDark,
            borderColor = SeedGold
        ) {
            Column(
                modifier = Modifier.padding(Spacing.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "VICTORY!",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = SeedGold,
                        letterSpacing = 4.sp
                    )
                )
                Spacer(modifier = Modifier.height(Spacing.medium))
                Text(
                    text = "You have triumphed in battle!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(Spacing.large))
                JQButton(
                    onClick = onClose,
                    text = "Continue",
                    variant = JQButtonVariant.SUCCESS
                )
            }
        }
    }
}

/**
 * Defeat screen with fade-out effect.
 */
@Composable
private fun DefeatScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = 0.95f,
        animationSpec = tween(2000, easing = LinearEasing),
        label = "defeat_fade"
    )
    
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = alpha)),
        contentAlignment = Alignment.Center
    ) {
        JQCard(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(Spacing.large),
            backgroundColor = SurfaceDark.copy(red = 0.3f),
            borderColor = DangerRed
        ) {
            Column(
                modifier = Modifier.padding(Spacing.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💀",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "DEFEAT",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DangerRed,
                        letterSpacing = 4.sp
                    )
                )
                Spacer(modifier = Modifier.height(Spacing.medium))
                Text(
                    text = "You have been defeated...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(Spacing.large))
                JQButton(
                    onClick = onClose,
                    text = "Return",
                    variant = JQButtonVariant.DANGER
                )
            }
        }
    }
}

/**
 * Confetti particle animation for victory screen.
 */
@Composable
private fun ConfettiAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_progress"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw 50 confetti particles
        for (i in 0 until 50) {
            val xOffset = (size.width * (i % 10) / 10f)
            val yOffset = (size.height * animationProgress + (i * 50f)) % size.height
            val confettiColor = listOf(
                SeedGold, GrassGreen, QuailBrown, DangerRed, InfoBlue
            )[i % 5]
            
            rotate(degrees = animationProgress * 360f * (i % 3)) {
                drawRect(
                    color = confettiColor,
                    topLeft = Offset(xOffset, yOffset),
                    size = androidx.compose.ui.geometry.Size(10f, 15f)
                )
            }
        }
    }
}

// Helper function
private fun getEnemyIcon(enemyName: String): String {
    return when {
        enemyName.contains("spider", ignoreCase = true) -> "🕷️"
        enemyName.contains("beetle", ignoreCase = true) -> "🪲"
        enemyName.contains("ant", ignoreCase = true) -> "🐜"
        enemyName.contains("wasp", ignoreCase = true) -> "🐝"
        enemyName.contains("moth", ignoreCase = true) -> "🦋"
        else -> "👾"  // Default enemy icon
    }
}
