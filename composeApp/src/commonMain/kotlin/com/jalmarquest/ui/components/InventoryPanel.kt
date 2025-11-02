package com.jalmarquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventorySlot
import com.jalmarquest.shared.inventory.Item
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.inventory.ItemRarity

/**
 * Full-screen inventory panel.
 * Shows grid of items with details panel on the right.
 */
@Composable
fun InventoryPanel(
    inventory: Inventory,
    onItemClick: (Item) -> Unit = {},
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    
    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
                .background(Color(0xEE0a0a0a), RoundedCornerShape(16.dp))
                .border(3.dp, Color(0xFFaa8844), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INVENTORY",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${inventory.currentSlotCount()}/${inventory.maxSlots} slots • ${inventory.currentWeight()}/${inventory.maxWeight}mg",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF888888)
                        )
                    }
                    
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8b4513)
                        )
                    ) {
                        Text("Close")
                    }
                }
                
                HorizontalDivider(
                    color = Color(0xFF444444),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left: Item grid
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(6),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(inventory.slots) { slot ->
                                ItemSlotCard(
                                    slot = slot,
                                    isSelected = slot.itemId?.let { ItemCatalog.getItem(it) } == selectedItem,
                                    onClick = {
                                        slot.itemId?.let { itemId ->
                                            val item = ItemCatalog.getItem(itemId)
                                            if (item != null) {
                                                selectedItem = item
                                                onItemClick(item)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Right: Item details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color(0xFF1a1a1a), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF333333), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        if (selectedItem != null) {
                            ItemDetailsPanel(item = selectedItem!!)
                        } else {
                            Text(
                                text = "Select an item to view details",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSlotCard(
    slot: InventorySlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = slot.itemId?.let { ItemCatalog.getItem(it) }
    
    Box(
        modifier = modifier
            .size(80.dp)
            .background(
                if (isSelected) Color(0xFF3a3a3a) else Color(0xFF2a2a2a),
                RoundedCornerShape(8.dp)
            )
            .border(
                2.dp,
                if (isSelected) Color(0xFFFFD700) else Color(0xFF444444),
                RoundedCornerShape(8.dp)
            )
            .clickable(enabled = item != null) { onClick() }
            .padding(8.dp)
    ) {
        if (item != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Item icon (emoji for now)
                Text(
                    text = getItemEmoji(item),
                    fontSize = 32.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Item name
                Text(
                    text = item.name,
                    fontSize = 9.sp,
                    color = getRarityColor(item.rarity),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Quantity
                if (slot.quantity > 1) {
                    Text(
                        text = "×${slot.quantity}",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        } else {
            // Empty slot
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x22000000), RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun ItemDetailsPanel(item: Item, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // Item icon
        Text(
            text = getItemEmoji(item),
            fontSize = 64.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Item name
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleLarge,
            color = getRarityColor(item.rarity),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Rarity
        Text(
            text = item.rarity.name,
            fontSize = 11.sp,
            color = getRarityColor(item.rarity).copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCCCCCC),
            lineHeight = 18.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats
        DetailRow("Type", item.type.name.lowercase().replaceFirstChar { it.uppercase() })
        DetailRow("Value", "${item.value} seeds")
        DetailRow("Weight", item.formattedWeight())
        
        if (item.stackable) {
            DetailRow("Max Stack", item.maxStack.toString())
        }
        
        if (item.usable) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✓ Usable",
                fontSize = 11.sp,
                color = Color(0xFF88ff88)
            )
        }
        
        if (item.questItem) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "★ Quest Item",
                fontSize = 11.sp,
                color = Color(0xFFFFD700)
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF888888)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color(0xFFCCCCCC)
        )
    }
}

private fun getItemEmoji(item: Item): String {
    return when (item.id) {
        "twig" -> "🌿"
        "acorn_cap" -> "🥜"
        "pebble" -> "🪨"
        "feather" -> "🪶"
        "dried_leaf" -> "🍂"
        "grass_blade" -> "🌾"
        "bark_chip" -> "🪵"
        "pine_needle" -> "🌲"
        "spider_silk" -> "🕸️"
        "moss_clump" -> "🟢"
        "sunflower_seed" -> "🌻"
        "millet_grain" -> "🌾"
        else -> when (item.type) {
            com.jalmarquest.shared.inventory.ItemType.EQUIPMENT -> "⚔️"
            com.jalmarquest.shared.inventory.ItemType.CONSUMABLE -> "🍖"
            com.jalmarquest.shared.inventory.ItemType.MATERIAL -> "📦"
            com.jalmarquest.shared.inventory.ItemType.QUEST -> "📜"
            else -> "❓"
        }
    }
}

private fun getRarityColor(rarity: ItemRarity): Color {
    return when (rarity) {
        ItemRarity.COMMON -> Color(0xFFAAAAAA)
        ItemRarity.UNCOMMON -> Color(0xFF44ff44)
        ItemRarity.RARE -> Color(0xFF4488ff)
        ItemRarity.EPIC -> Color(0xFFaa44ff)
        ItemRarity.LEGENDARY -> Color(0xFFFFD700)
    }
}
