package com.jalmarquest.shared.persistence

import kotlinx.coroutines.test.runTest
import kotlin.test.*

/**
 * Tests for BackupManager.
 * Covers backup creation, restoration, listing, deletion, and cleanup.
 */
class BackupManagerTest {
    private lateinit var fileIO: MockFileIO
    private lateinit var backupManager: BackupManager
    
    @BeforeTest
    fun setup() {
        fileIO = MockFileIO()
        backupManager = BackupManager(fileIO, maxBackups = 5)
    }
    
    // ==================== Backup Creation Tests ====================
    
    @Test
    fun `createBackup should succeed when save exists`() = runTest {
        // Create original save file
        fileIO.writeFile("saves/slot1.jqsave", "test save content")
        
        val result = backupManager.createBackup("slot1")
        
        assertTrue(result.isSuccess)
        val backupFilename = result.getOrNull()!!
        assertTrue(backupFilename.startsWith("saves/backups/slot1_"))
        assertTrue(backupFilename.endsWith(".backup"))
        
        // Verify backup file was created
        val backupContent = fileIO.readFile(backupFilename)
        assertEquals("test save content", backupContent)
    }
    
    @Test
    fun `createBackup should fail when save does not exist`() = runTest {
        val result = backupManager.createBackup("nonexistent")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is BackupException.SaveNotFound)
    }
    
    @Test
    fun `createBackup should cleanup old backups when max exceeded`() = runTest {
        // Create original save
        fileIO.writeFile("saves/slot1.jqsave", "content")
        
        // Create 6 backups (max is 5)
        repeat(6) {
            backupManager.createBackup("slot1")
            kotlinx.coroutines.delay(10) // Ensure different timestamps
        }
        
        // Should have only 5 backups remaining (maxBackups = 5)
        val backups = backupManager.listBackups("slot1")
        assertEquals(5, backups.size)
    }
    
    // ==================== Backup Restoration Tests ====================
    
    @Test
    fun `restoreBackup should restore backup to save slot`() = runTest {
        // Create original save and backup
        fileIO.writeFile("saves/slot1.jqsave", "original content")
        val backupResult = backupManager.createBackup("slot1")
        val backupFilename = backupResult.getOrNull()!!
        
        // Modify original save
        fileIO.writeFile("saves/slot1.jqsave", "modified content")
        
        // Restore backup
        val restoreResult = backupManager.restoreBackup(backupFilename, "slot1")
        
        assertTrue(restoreResult.isSuccess)
        assertEquals("original content", fileIO.readFile("saves/slot1.jqsave"))
    }
    
    @Test
    fun `restoreBackup should fail when backup does not exist`() = runTest {
        val result = backupManager.restoreBackup("saves/backups/nonexistent.backup", "slot1")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is BackupException.BackupNotFound)
    }
    
    // ==================== List Backups Tests ====================
    
    @Test
    fun `listBackups should return empty list when no backups exist`() = runTest {
        val backups = backupManager.listBackups("slot1")
        
        assertTrue(backups.isEmpty())
    }
    
    @Test
    fun `listBackups should return backups sorted by timestamp descending`() = runTest {
        fileIO.writeFile("saves/slot1.jqsave", "content")
        
        // Create 3 backups with delays
        repeat(3) {
            backupManager.createBackup("slot1")
            kotlinx.coroutines.delay(10)
        }
        
        val backups = backupManager.listBackups("slot1")
        
        assertEquals(3, backups.size)
        // Verify sorted by timestamp descending (newest first)
        for (i in 0 until backups.size - 1) {
            assertTrue(backups[i].timestamp > backups[i + 1].timestamp)
        }
    }
    
    @Test
    fun `listBackups should only return backups for specified slot`() = runTest {
        fileIO.writeFile("saves/slot1.jqsave", "content1")
        fileIO.writeFile("saves/slot2.jqsave", "content2")
        
        backupManager.createBackup("slot1")
        kotlinx.coroutines.delay(10)
        backupManager.createBackup("slot2")
        kotlinx.coroutines.delay(10)
        backupManager.createBackup("slot1")
        
        val slot1Backups = backupManager.listBackups("slot1")
        val slot2Backups = backupManager.listBackups("slot2")
        
        assertEquals(2, slot1Backups.size)
        assertEquals(1, slot2Backups.size)
        
        slot1Backups.forEach { backup ->
            assertEquals("slot1", backup.slotName)
        }
    }
    
    // ==================== Delete Backup Tests ====================
    
    @Test
    fun `deleteBackup should remove backup file`() = runTest {
        fileIO.writeFile("saves/slot1.jqsave", "content")
        val backupResult = backupManager.createBackup("slot1")
        val backupFilename = backupResult.getOrNull()!!
        
        val deleteResult = backupManager.deleteBackup(backupFilename)
        
        assertTrue(deleteResult.isSuccess)
        assertFalse(fileIO.fileExists(backupFilename))
    }
    
    @Test
    fun `deleteBackup should fail when backup does not exist`() = runTest {
        val result = backupManager.deleteBackup("saves/backups/nonexistent.backup")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is BackupException.BackupNotFound)
    }
    
    // ==================== Clear Backups Tests ====================
    
    @Test
    fun `clearBackups should delete all backups for slot`() = runTest {
        fileIO.writeFile("saves/slot1.jqsave", "content")
        
        repeat(3) {
            backupManager.createBackup("slot1")
            kotlinx.coroutines.delay(10) // Ensure different timestamps
        }
        
        val clearResult = backupManager.clearBackups("slot1")
        
        assertTrue(clearResult.isSuccess)
        assertEquals(3, clearResult.getOrNull())
        
        val backups = backupManager.listBackups("slot1")
        assertTrue(backups.isEmpty())
    }
    
    @Test
    fun `clearBackups should not affect other slots`() = runTest {
        fileIO.writeFile("saves/slot1.jqsave", "content1")
        fileIO.writeFile("saves/slot2.jqsave", "content2")
        
        backupManager.createBackup("slot1")
        backupManager.createBackup("slot2")
        
        backupManager.clearBackups("slot1")
        
        assertEquals(0, backupManager.listBackups("slot1").size)
        assertEquals(1, backupManager.listBackups("slot2").size)
    }
    
    // ==================== BackupInfo Tests ====================
    
    @Test
    fun `BackupInfo formattedSize should display bytes correctly`() {
        val info = BackupInfo("file", "slot1", 0L, 512L)
        assertEquals("512 B", info.formattedSize())
    }
    
    @Test
    fun `BackupInfo formattedSize should display kilobytes correctly`() {
        val info = BackupInfo("file", "slot1", 0L, 2048L)
        assertEquals("2 KB", info.formattedSize())
    }
    
    @Test
    fun `BackupInfo formattedSize should display megabytes correctly`() {
        val info = BackupInfo("file", "slot1", 0L, 2 * 1024 * 1024L)
        assertEquals("2 MB", info.formattedSize())
    }
    
    @Test
    fun `BackupInfo formattedTimestamp should return ISO format`() {
        val timestamp = 1704067200000L // 2024-01-01 00:00:00 UTC
        val info = BackupInfo("file", "slot1", timestamp, 0L)
        val formatted = info.formattedTimestamp()
        
        assertTrue(formatted.startsWith("2024-01-01"))
    }
}
