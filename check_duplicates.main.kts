#!/usr/bin/env kotlin

// Quick script to check for duplicate IDs in location files
import java.io.File

val locationFiles = listOf(
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/LocationCatalog.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Grassland.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Forest.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Mountain.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Desert.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Swamp.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Tundra.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Coastal.kt",
    "shared/src/commonMain/kotlin/com/jalmarquest/shared/world/catalog/LocationCatalog_Cave.kt"
)

val idPattern = Regex("""id\s*=\s*"([^"]+)"""")
val allIds = mutableListOf<Pair<String, String>>() // Pair of (id, filename)

locationFiles.forEach { filePath ->
    val file = File(filePath)
    if (file.exists()) {
        val content = file.readText()
        idPattern.findAll(content).forEach { match ->
            val id = match.groupValues[1]
            allIds.add(id to file.name)
        }
    }
}

println("Total IDs found: ${allIds.size}")
println("Unique IDs: ${allIds.map { it.first }.toSet().size}\n")

// Find duplicates
val duplicates = allIds.groupBy { it.first }
    .filter { it.value.size > 1 }
    .toList()
    .sortedByDescending { it.second.size }

if (duplicates.isEmpty()) {
    println("✅ No duplicate IDs found!")
} else {
    println("❌ Found ${duplicates.size} duplicate IDs:\n")
    duplicates.forEach { (id, occurrences) ->
        println("  '$id' appears ${occurrences.size} times in:")
        occurrences.forEach { (_, filename) ->
            println("    - $filename")
        }
        println()
    }
}
