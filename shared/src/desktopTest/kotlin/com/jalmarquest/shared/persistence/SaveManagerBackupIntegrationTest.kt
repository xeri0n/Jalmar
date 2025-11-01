package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.GameState
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class SaveManagerBackupIntegrationTest {
    private lateinit var fileIO: FileIO
    private lateinit var backupManager: BackupManager
    private lateinit var saveManager: SaveManager

    @BeforeTest
    fun setup() {
        fileIO = FileIO()
        // Route BackupManager through the same filesystem via adapter
        backupManager = BackupManager(FileIOAdapter(fileIO), maxBackups = 5)
        saveManager = SaveManager(fileIO, backupManager)
    }

    private suspend fun cleanupSlot(slot: String) {
        // delete save
        if (saveManager.saveExists(slot)) {
            saveManager.deleteSave(slot)
        }
        // delete backups
        backupManager.clearBackups(slot)
    }

    @Test
    fun `saving twice creates one backup`() = runTest {
        val slot = "itest_slot_${System.currentTimeMillis()}"
        try {
            val initial = GameState.createNew(playerName = "Jalmar", playerId = "test")
            val first = saveManager.saveGame(initial, slot)
            assertTrue(first.isSuccess)

            // Initially no backups
            val beforeBackups = backupManager.listBackups(slot)
            assertTrue(beforeBackups.isEmpty())

            // Save again (should create a backup of previous save)
            val updated = initial.copy(player = initial.player.copy(level = initial.player.level + 1))
            val second = saveManager.saveGame(updated, slot)
            assertTrue(second.isSuccess)

            val backups = backupManager.listBackups(slot)
            assertEquals(1, backups.size)
        } finally {
            cleanupSlot(slot)
        }
    }

    @Test
    fun `retains only max backups`() = runTest {
        val slot = "itest_slot_${System.currentTimeMillis()}"
        val max = 5
        try {
            val baseState = GameState.createNew("Jalmar", "test")
            // First save (no backup yet)
            assertTrue(saveManager.saveGame(baseState, slot).isSuccess)
            // Create several overwrites -> backups each time
            repeat(10) { i ->
                val next = baseState.copy(player = baseState.player.copy(level = baseState.player.level + i + 1))
                // small delay not required since we sequence backups with counter
                assertTrue(saveManager.saveGame(next, slot).isSuccess)
            }
            val backups = backupManager.listBackups(slot)
            assertTrue(backups.size <= max)
            assertEquals(max, backups.size)
        } finally {
            cleanupSlot(slot)
        }
    }

    @Test
    fun `restore latest backup reverts last save`() = runTest {
        val slot = "itest_slot_${System.currentTimeMillis()}"
        try {
            val base = GameState.createNew("Jalmar", "test")
            assertTrue(saveManager.saveGame(base, slot).isSuccess)

            val progressed = base.copy(player = base.player.copy(level = base.player.level + 4))
            assertTrue(saveManager.saveGame(progressed, slot).isSuccess)

            // Now we have one backup (of base)
            val backups = backupManager.listBackups(slot)
            assertEquals(1, backups.size)
            val latest = backups.first()

            // Overwrite save with an even more progressed state
            val progressedAgain = base.copy(player = base.player.copy(level = base.player.level + 8))
            assertTrue(saveManager.saveGame(progressedAgain, slot).isSuccess)

            // Restore the latest backup -> should bring us back to 'progressed' previous, NO: backup was of the previous save before overwrite
            // After first overwrite, backup was 'base'. After second overwrite, backup is 'progressed'. So latest backup should be 'progressed'.
            val latest2 = backupManager.listBackups(slot).first()
            assertTrue(backupManager.restoreBackup(latest2.filename, slot).isSuccess)

            // Load and validate properties match the 'progressed' state
            val loaded = saveManager.loadGame(slot).getOrNull()
            assertNotNull(loaded)
            assertEquals(progressed.player.level, loaded.player.level)
        } finally {
            cleanupSlot(slot)
        }
    }
}
