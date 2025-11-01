import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.inventory.ItemCatalog

fun main() {
    // Test adding materials
    var inventory = Inventory()
    
    listOf("twig", "dried_leaf", "grass_blade", "spider_silk", "feather").forEach { itemId ->
        println("\n=== Testing $itemId ===")
        val item = ItemCatalog.getItem(itemId)
        println("Item exists: ${item != null}")
        if (item != null) {
            println("Item: ${item.name}, weight=${item.weight}mg")
        }
        
        val (newInventory, result) = InventoryManager.addItem(inventory, itemId, 20)
        println("Add result: $result")
        
        if (result is InventoryManager.ItemAddResult.Success) {
            inventory = newInventory
            println("Successfully added! Quantity now: ${inventory.getItemQuantity(itemId)}")
        } else {
            println("FAILED to add!")
        }
    }
    
    println("\n=== Final Inventory ===")
    println("Total weight: ${inventory.getCurrentWeight()}mg / ${inventory.maxWeight}mg")
    println("Items: ${inventory.items.size}")
}
