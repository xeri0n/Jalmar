package dev.xeri0n.jalmarquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.xeri0n.jalmarquest.ui.components.*
import dev.xeri0n.jalmarquest.ui.theme.*
import dev.xeri0n.jalmarquest.ui.viewmodel.SettingsViewModel
import dev.xeri0n.jalmarquest.ui.viewmodel.TextSize
import androidx.compose.runtime.collectAsState

/**
 * Settings Screen
 * 
 * Comprehensive settings interface with:
 * - Categorized settings (Audio, Display, Gameplay, Controls)
 * - Sliders for volume controls
 * - Toggles for boolean settings
 * - Dropdown selectors for enums
 * - Reset to defaults button
 * - Accessibility-focused design
 * 
 * @param viewModel SettingsViewModel managing state
 * @param onBack Callback to navigate back
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    JQIconButton(
                        icon = { Text("←", style = MaterialTheme.typography.headlineSmall) },
                        onClick = onBack,
                        contentDescription = "Back"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(Spacing.large),
            verticalArrangement = Arrangement.spacedBy(Spacing.extraLarge)
        ) {
            // Audio Settings Section
            AudioSettingsSection(viewModel)
            
            Divider()
            
            // Display Settings Section
            DisplaySettingsSection(viewModel)
            
            Divider()
            
            // Gameplay Settings Section
            GameplaySettingsSection(viewModel)
            
            Divider()
            
            // Controls Settings Section
            ControlsSettingsSection(viewModel)
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Reset Button
            JQButton(
                text = "Reset to Defaults",
                onClick = { viewModel.resetToDefaults() },
                modifier = Modifier.fillMaxWidth(),
                variant = JQButtonVariant.WARNING,
                leadingIcon = {
                    Text("⚠️", style = MaterialTheme.typography.titleMedium)
                }
            )
            
            Spacer(modifier = Modifier.height(Spacing.huge))
        }
    }
}

/**
 * Audio Settings Section
 */
@Composable
private fun AudioSettingsSection(viewModel: SettingsViewModel) {
    val musicVolume by viewModel.musicVolume.collectAsState()
    val sfxVolume by viewModel.sfxVolume.collectAsState()
    val ttsVolume by viewModel.ttsVolume.collectAsState()
    val ttsEnabled by viewModel.ttsEnabled.collectAsState()
    
    SettingsSection(title = "Audio") {
        // Music Volume
        SettingSlider(
            label = "Music Volume",
            value = musicVolume,
            onValueChange = { viewModel.setMusicVolume(it) },
            valueLabel = "${(musicVolume * 100).toInt()}%"
        )
        
        // SFX Volume
        SettingSlider(
            label = "Sound Effects Volume",
            value = sfxVolume,
            onValueChange = { viewModel.setSfxVolume(it) },
            valueLabel = "${(sfxVolume * 100).toInt()}%"
        )
        
        // TTS Toggle
        SettingToggle(
            label = "Text-to-Speech Narration",
            description = "Hear dialogue and notifications spoken aloud",
            checked = ttsEnabled,
            onCheckedChange = { viewModel.setTtsEnabled(it) }
        )
        
        // TTS Volume (only if enabled)
        if (ttsEnabled) {
            SettingSlider(
                label = "Narration Volume",
                value = ttsVolume,
                onValueChange = { viewModel.setTtsVolume(it) },
                valueLabel = "${(ttsVolume * 100).toInt()}%"
            )
        }
    }
}

/**
 * Display Settings Section
 */
@Composable
private fun DisplaySettingsSection(viewModel: SettingsViewModel) {
    val darkMode by viewModel.darkMode.collectAsState()
    val textSize by viewModel.textSize.collectAsState()
    val highContrastMode by viewModel.highContrastMode.collectAsState()
    val animationsEnabled by viewModel.animationsEnabled.collectAsState()
    val particleEffects by viewModel.particleEffects.collectAsState()
    
    SettingsSection(title = "Display") {
        // Dark Mode Toggle
        SettingToggle(
            label = "Dark Mode",
            description = "Use dark theme to reduce eye strain",
            checked = darkMode,
            onCheckedChange = { viewModel.setDarkMode(it) }
        )
        
        // Text Size Selector
        SettingSelector(
            label = "Text Size",
            options = TextSize.entries.map { it.name },
            selectedOption = textSize.name,
            onOptionSelected = { selectedName ->
                viewModel.setTextSize(TextSize.valueOf(selectedName))
            }
        )
        
        // High Contrast Mode
        SettingToggle(
            label = "High Contrast Mode",
            description = "Increase contrast for better readability",
            checked = highContrastMode,
            onCheckedChange = { viewModel.setHighContrastMode(it) }
        )
        
        // Animations
        SettingToggle(
            label = "Animations",
            description = "Enable smooth transitions and effects",
            checked = animationsEnabled,
            onCheckedChange = { viewModel.setAnimationsEnabled(it) }
        )
        
        // Particle Effects
        SettingToggle(
            label = "Particle Effects",
            description = "Show damage numbers, level-up stars, etc.",
            checked = particleEffects,
            onCheckedChange = { viewModel.setParticleEffects(it) }
        )
    }
}

/**
 * Gameplay Settings Section
 */
@Composable
private fun GameplaySettingsSection(viewModel: SettingsViewModel) {
    val autosaveInterval by viewModel.autosaveInterval.collectAsState()
    val showTutorials by viewModel.showTutorials.collectAsState()
    val confirmDestructiveActions by viewModel.confirmDestructiveActions.collectAsState()
    val showDamageNumbers by viewModel.showDamageNumbers.collectAsState()
    val autoLootItems by viewModel.autoLootItems.collectAsState()
    
    SettingsSection(title = "Gameplay") {
        // Autosave Interval Slider
        SettingSlider(
            label = "Autosave Interval",
            value = autosaveInterval.toFloat() / 30f,
            onValueChange = { viewModel.setAutosaveInterval((it * 30).toInt().coerceAtLeast(1)) },
            valueLabel = "$autosaveInterval min",
            steps = 29
        )
        
        // Show Tutorials
        SettingToggle(
            label = "Show Tutorials",
            description = "Display helpful tips for new features",
            checked = showTutorials,
            onCheckedChange = { viewModel.setShowTutorials(it) }
        )
        
        // Confirm Destructive Actions
        SettingToggle(
            label = "Confirm Destructive Actions",
            description = "Ask for confirmation before selling/deleting items",
            checked = confirmDestructiveActions,
            onCheckedChange = { viewModel.setConfirmDestructiveActions(it) }
        )
        
        // Show Damage Numbers
        SettingToggle(
            label = "Show Damage Numbers",
            description = "Display damage values in combat",
            checked = showDamageNumbers,
            onCheckedChange = { viewModel.setShowDamageNumbers(it) }
        )
        
        // Auto-Loot Items
        SettingToggle(
            label = "Auto-Loot Items",
            description = "Automatically collect items after combat",
            checked = autoLootItems,
            onCheckedChange = { viewModel.setAutoLootItems(it) }
        )
    }
}

/**
 * Controls Settings Section
 */
@Composable
private fun ControlsSettingsSection(viewModel: SettingsViewModel) {
    val mouseSensitivity by viewModel.mouseSensitivity.collectAsState()
    val hapticFeedback by viewModel.hapticFeedback.collectAsState()
    
    SettingsSection(title = "Controls") {
        // Mouse Sensitivity (for future mouse controls)
        SettingSlider(
            label = "Mouse Sensitivity",
            value = mouseSensitivity,
            onValueChange = { viewModel.setMouseSensitivity(it) },
            valueLabel = "${(mouseSensitivity * 100).toInt()}%"
        )
        
        // Haptic Feedback
        SettingToggle(
            label = "Haptic Feedback",
            description = "Vibrate on button presses (mobile only)",
            checked = hapticFeedback,
            onCheckedChange = { viewModel.setHapticFeedback(it) }
        )
    }
}

// ============= Reusable Setting Components =============

/**
 * Settings Section Header
 */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        content()
    }
}

/**
 * Setting Toggle (Switch)
 */
@Composable
private fun SettingToggle(
    label: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * Setting Slider
 */
@Composable
private fun SettingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueLabel: String,
    steps: Int = 0
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Setting Selector (Dropdown)
 */
@Composable
private fun SettingSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Box {
            JQButton(
                text = selectedOption,
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                variant = JQButtonVariant.TERTIARY,
                trailingIcon = {
                    Text(if (expanded) "▲" else "▼")
                }
            )
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
