package com.jalmarquest.shared.time

import com.jalmarquest.shared.model.Season
import com.jalmarquest.shared.model.TimeOfDay
import com.jalmarquest.shared.model.WorldTime
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import kotlin.test.*

class TimeManagerTest {
    
    private lateinit var timeManager: TimeManager
    
    @BeforeTest
    fun setup() {
        timeManager = TimeManager()
    }
    
    @Test
    fun `initial time should be Spring Day 1 at 6am`() {
        val time = timeManager.worldTime.value
        
        assertEquals(Season.SPRING, time.season)
        assertEquals(1, time.day)
        assertEquals(6, time.hour)
        assertEquals(0, time.minute)
        assertEquals(0, time.totalTicks)
    }
    
    @Test
    fun `tick should advance time by one tick`() = runTest {
        timeManager.tick()
        
        assertEquals(1, timeManager.worldTime.value.totalTicks)
    }
    
    @Test
    fun `60 ticks should advance one minute`() = runTest {
        repeat(60) {
            timeManager.tick()
        }
        
        val time = timeManager.worldTime.value
        assertEquals(1, time.minute)
        assertEquals(6, time.hour)
    }
    
    @Test
    fun `advanceMinutes should work correctly`() = runTest {
        timeManager.advanceMinutes(30)
        
        val time = timeManager.worldTime.value
        assertEquals(30, time.minute)
        assertEquals(6, time.hour)
    }
    
    @Test
    fun `60 minutes should advance one hour`() = runTest {
        timeManager.advanceMinutes(60)
        
        val time = timeManager.worldTime.value
        assertEquals(0, time.minute)
        assertEquals(7, time.hour)
    }
    
    @Test
    fun `advanceHours should work correctly`() = runTest {
        timeManager.advanceHours(5)
        
        val time = timeManager.worldTime.value
        assertEquals(11, time.hour)
    }
    
    @Test
    fun `24 hours should advance one day`() = runTest {
        timeManager.advanceHours(24)
        
        val time = timeManager.worldTime.value
        assertEquals(2, time.day)
        assertEquals(6, time.hour)
    }
    
    @Test
    fun `advanceDays should work correctly`() = runTest {
        timeManager.advanceDays(10)
        
        assertEquals(11, timeManager.worldTime.value.day)
    }
    
    @Test
    fun `30 days should advance one season`() = runTest {
        timeManager.advanceDays(30)
        
        val time = timeManager.worldTime.value
        assertEquals(Season.SUMMER, time.season)
        assertEquals(1, time.day)
    }
    
    @Test
    fun `seasons should cycle correctly`() = runTest {
        // Spring -> Summer
        timeManager.advanceDays(30)
        assertEquals(Season.SUMMER, timeManager.getCurrentSeason())
        
        // Summer -> Autumn
        timeManager.advanceDays(30)
        assertEquals(Season.AUTUMN, timeManager.getCurrentSeason())
        
        // Autumn -> Winter
        timeManager.advanceDays(30)
        assertEquals(Season.WINTER, timeManager.getCurrentSeason())
        
        // Winter -> Spring (new year)
        timeManager.advanceDays(30)
        assertEquals(Season.SPRING, timeManager.getCurrentSeason())
    }
    
    @Test
    fun `isDay should return true during daytime hours`() = runTest {
        // 6 AM - should be day
        assertTrue(timeManager.isDay())
        
        // 12 PM - should be day
        timeManager.advanceHours(6)
        assertTrue(timeManager.isDay())
        
        // 6 PM - should be day
        timeManager.advanceHours(6)
        assertTrue(timeManager.isDay())
    }
    
    @Test
    fun `isNight should return true during nighttime hours`() = runTest {
        // 10 PM
        timeManager.advanceHours(16)
        assertTrue(timeManager.isNight())
        
        // 2 AM
        timeManager.advanceHours(4)
        assertTrue(timeManager.isNight())
    }
    
    @Test
    fun `getTimeOfDay should return correct periods`() = runTest {
        // 6 AM - Morning
        assertEquals(TimeOfDay.MORNING, timeManager.getCurrentTimeOfDay())
        
        // 12 PM - Afternoon
        timeManager.advanceHours(6)
        assertEquals(TimeOfDay.AFTERNOON, timeManager.getCurrentTimeOfDay())
        
        // 6 PM - Evening
        timeManager.advanceHours(6)
        assertEquals(TimeOfDay.EVENING, timeManager.getCurrentTimeOfDay())
        
        // 10 PM - Night
        timeManager.advanceHours(4)
        assertEquals(TimeOfDay.NIGHT, timeManager.getCurrentTimeOfDay())
    }
    
    @Test
    fun `pause should stop time progression`() = runTest {
        timeManager.pause()
        
        val timeBefore = timeManager.worldTime.value.totalTicks
        timeManager.tick()
        val timeAfter = timeManager.worldTime.value.totalTicks
        
        assertEquals(timeBefore, timeAfter)
    }
    
    @Test
    fun `resume should restart time progression`() = runTest {
        timeManager.pause()
        timeManager.resume()
        
        timeManager.tick()
        
        assertEquals(1, timeManager.worldTime.value.totalTicks)
    }
    
    @Test
    fun `togglePause should switch pause state`() = runTest {
        assertFalse(timeManager.isPaused)
        
        timeManager.togglePause()
        assertTrue(timeManager.isPaused)
        
        timeManager.togglePause()
        assertFalse(timeManager.isPaused)
    }
    
    @Test
    fun `setTimeSpeed should change speed multiplier`() {
        timeManager.setTimeSpeed(2.0)
        assertEquals(2.0, timeManager.timeSpeed)
        
        timeManager.setTimeSpeed(0.5)
        assertEquals(0.5, timeManager.timeSpeed)
    }
    
    @Test
    fun `setTimeSpeed should reject negative values`() {
        assertFails {
            timeManager.setTimeSpeed(-1.0)
        }
    }
    
    @Test
    fun `resetTime should return to initial state`() = runTest {
        timeManager.advanceDays(100)
        timeManager.resetTime()
        
        val time = timeManager.worldTime.value
        assertEquals(Season.SPRING, time.season)
        assertEquals(1, time.day)
        assertEquals(6, time.hour)
        assertEquals(0, time.minute)
    }
    
    @Test
    fun `hoursUntilSunrise should calculate correctly`() = runTest {
        // Reset to ensure we're at 6 AM
        timeManager.resetTime()
        
        // At 6 AM, next sunrise is 24 hours away
        assertEquals(24, timeManager.hoursUntilSunrise())
        
        // At midnight (18 hours ahead), 6 hours until sunrise
        timeManager.advanceHours(18)
        assertEquals(6, timeManager.hoursUntilSunrise())
        
        // At 3 AM (3 hours later), 3 hours until sunrise
        timeManager.advanceHours(3)
        assertEquals(3, timeManager.hoursUntilSunrise())
    }
    
    @Test
    fun `hoursUntilSunset should calculate correctly`() = runTest {
        // At 6 AM, 13 hours until sunset (7 PM)
        assertEquals(13, timeManager.hoursUntilSunset())
        
        // At noon, 7 hours until sunset
        timeManager.advanceHours(6)
        assertEquals(7, timeManager.hoursUntilSunset())
        
        // At 8 PM, 23 hours until next sunset
        timeManager.advanceHours(8)
        assertEquals(23, timeManager.hoursUntilSunset())
    }
    
    @Test
    fun `daysUntilNextSeason should calculate correctly`() = runTest {
        // Day 1, 30 days until next season
        assertEquals(30, timeManager.daysUntilNextSeason())
        
        // Day 15, 16 days until next season
        timeManager.advanceDays(14)
        assertEquals(16, timeManager.daysUntilNextSeason())
        
        // Day 30, 1 day until next season
        timeManager.advanceDays(15)
        assertEquals(1, timeManager.daysUntilNextSeason())
    }
    
    @Test
    fun `getNextSeason should return correct season`() = runTest {
        assertEquals(Season.SUMMER, timeManager.getNextSeason())
        
        timeManager.advanceDays(30)
        assertEquals(Season.AUTUMN, timeManager.getNextSeason())
        
        timeManager.advanceDays(30)
        assertEquals(Season.WINTER, timeManager.getNextSeason())
        
        timeManager.advanceDays(30)
        assertEquals(Season.SPRING, timeManager.getNextSeason())
    }
    
    @Test
    fun `setTime should update time directly`() = runTest {
        val newTime = WorldTime(
            totalTicks = 5000,
            season = Season.WINTER,
            day = 15,
            hour = 12,
            minute = 30
        )
        
        timeManager.setTime(newTime)
        
        assertEquals(newTime, timeManager.worldTime.value)
    }
    
    @Test
    fun `time should remain consistent across multiple operations`() = runTest {
        // Advance by a complex pattern
        timeManager.advanceHours(25) // 1 day + 1 hour
        timeManager.advanceMinutes(90) // 1.5 hours
        timeManager.advanceDays(5) // 5 days
        
        val time = timeManager.worldTime.value
        
        // Should be Day 7 (1 + 5 + 1 from hours), 8:30 AM (6 + 1 + 1.5)
        assertEquals(7, time.day)
        assertEquals(8, time.hour)
        assertEquals(30, time.minute)
    }
    
    @Test
    fun `concurrent tick operations should be thread-safe`() = runTest {
        val jobs = List(100) { index ->
            launch {
                timeManager.tick()
            }
        }
        
        jobs.forEach { job -> job.join() }
        
        // Should have advanced exactly 100 ticks
        assertEquals(100, timeManager.worldTime.value.totalTicks)
    }
}
