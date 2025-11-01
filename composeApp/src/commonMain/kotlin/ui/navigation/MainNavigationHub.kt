package ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main navigation hub that provides access to all game features.
 * This is the central UI entry point for players.
 */
@Composable
fun MainNavigationHub(
    onNavigateToHub: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToNest: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToCrafting: () -> Unit,
    onNavigateToQuests: () -> Unit,
    onNavigateToThoughts: () -> Unit,
    onNavigateToConcoctions: () -> Unit,
    onNavigateToWorldInfo: () -> Unit,
    onNavigateToChronicle: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToCompanions: () -> Unit,
    onNavigateToFactions: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSaveLoad: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Jalmar Quest - Main Hub",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Core Gameplay
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Core Activities", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToHub) { Text("Buttonburgh Hub") }
                    TextButton(onClick = onNavigateToExplore) { Text("Explore World") }
                    TextButton(onClick = onNavigateToQuests) { Text("Quest Log") }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Character Management
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Character", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToInventory) { Text("Inventory") }
                    TextButton(onClick = onNavigateToSkills) { Text("Skills") }
                    TextButton(onClick = onNavigateToThoughts) { Text("Thoughts") }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Crafting & Economy
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Crafting & Economy", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToCrafting) { Text("Crafting") }
                    TextButton(onClick = onNavigateToConcoctions) { Text("Alchemy") }
                    TextButton(onClick = onNavigateToShop) { Text("Shop") }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Home & Social
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Home & Social", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToNest) { Text("Nest") }
                    TextButton(onClick = onNavigateToCompanions) { Text("Companions") }
                    TextButton(onClick = onNavigateToFactions) { Text("Factions") }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Meta & Progression
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Meta & Progression", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToChronicle) { Text("Battle Pass") }
                    TextButton(onClick = onNavigateToWorldInfo) { Text("Codex") }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // System
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onNavigateToSettings) { Text("Settings") }
            TextButton(onClick = onNavigateToSaveLoad) { Text("Save/Load") }
        }
    }
}
