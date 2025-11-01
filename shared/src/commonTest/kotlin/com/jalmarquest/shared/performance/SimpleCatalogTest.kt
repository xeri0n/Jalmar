package com.jalmarquest.shared.performance

import com.jalmarquest.shared.inventory.ItemCatalog
import kotlin.test.Test
import kotlin.test.assertTrue

class SimpleCatalogTest {
    @Test
    fun `load ItemCatalog`() {
        println("Loading ItemCatalog...")
        val items = ItemCatalog.getAllItems()
        println("Loaded ${items.size} items")
        assertTrue(items.size > 200)
    }
}
