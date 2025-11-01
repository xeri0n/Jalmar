package com.jalmarquest.shared.time

import com.jalmarquest.shared.model.Season
import com.jalmarquest.shared.model.TimeOfDay
import com.jalmarquest.shared.model.WorldTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages game time progression with seasons, day/night cycles, and speed controls.
 * 
 * Time Structure:
 * - 60 ticks = 1 minute
 * - 60 minutes = 1 hour
 * - 24 hours = 1 day
 * - 30 days = 1 season
 * - 4 seasons = 1 year
 */
class TimeManager(initialTime: WorldTime = WorldTime()) {
    
    companion object {
        /**
         * Advance a WorldTime by the specified number of minutes.
         * This is a pure function that doesn't mutate the original WorldTime.
         * Used for simulating time passage outside the TimeManager (e.g., during movement).
         */
        fun advanceWorldTime(worldTime: WorldTime, minutes: Int): WorldTime {
            require(minutes >= 0) { "Cannot advance time by negative minutes" }
            if (minutes == 0) return worldTime
            
            var current = worldTime
            repeat(minutes) {
                current = advanceOneMinute(current)
            }
            return current
        }
        
        private fun advanceOneMinute(worldTime: WorldTime): WorldTime {
            val newMinute = worldTime.minute + 1
            var newHour = worldTime.hour
            var newDay = worldTime.day
            var newSeason = worldTime.season
            
            // Hour change
            if (newMinute >= WorldTime.MINUTES_PER_HOUR) {
                newHour = worldTime.hour + 1
                
                // Day change
                if (newHour >= WorldTime.HOURS_PER_DAY) {
                    newDay = worldTime.day + 1
                    newHour = 0
                    
                    // Season change
                    if (newDay > WorldTime.DAYS_PER_SEASON) {
                        newDay = 1
                        newSeason = when (newSeason) {
                            Season.SPRING -> Season.SUMMER
                            Season.SUMMER -> Season.AUTUMN
                            Season.AUTUMN -> Season.WINTER
                            Season.WINTER -> Season.SPRING
                        }
                    }
                }
                
                return WorldTime(
                    totalTicks = worldTime.totalTicks + (WorldTime.TICKS_PER_MINUTE * WorldTime.MINUTES_PER_HOUR),
                    season = newSeason,
                    day = newDay,
                    hour = newHour,
                    minute = 0
                )
            }
            
            // Just increment minute
            return WorldTime(
                totalTicks = worldTime.totalTicks + WorldTime.TICKS_PER_MINUTE,
                season = worldTime.season,
                day = worldTime.day,
                hour = worldTime.hour,
                minute = newMinute
            )
        }
    }
    
    private val mutex = Mutex()
    private val _worldTime = MutableStateFlow(initialTime)
    
    /**
     * Observable world time state.
     */
    val worldTime: StateFlow<WorldTime> = _worldTime.asStateFlow()
    
    private var _isPaused = false
    private var _timeSpeed = 1.0
    
    /**
     * Whether time progression is paused.
     */
    val isPaused: Boolean
        get() = _isPaused
    
    /**
     * Current time speed multiplier (1.0 = normal, 2.0 = double speed, etc.)
     */
    val timeSpeed: Double
        get() = _timeSpeed
    
    /**
     * Advance time by one tick.
     * Returns the updated WorldTime.
     */
    suspend fun tick(): WorldTime {
        if (_isPaused) return _worldTime.value
        
        return mutex.withLock {
            val current = _worldTime.value
            val newTime = calculateNextTick(current)
            _worldTime.value = newTime
            newTime
        }
    }
    
    /**
     * Advance time by multiple ticks at once.
     * Useful for fast-forwarding or processing large time jumps.
     */
    suspend fun advanceTicks(ticks: Long): WorldTime {
        if (_isPaused || ticks <= 0) return _worldTime.value
        
        return mutex.withLock {
            var current = _worldTime.value
            repeat(ticks.toInt()) {
                current = calculateNextTick(current)
            }
            _worldTime.value = current
            current
        }
    }
    
    /**
     * Advance time by minutes.
     */
    suspend fun advanceMinutes(minutes: Int): WorldTime {
        return advanceTicks(minutes * WorldTime.TICKS_PER_MINUTE.toLong())
    }
    
    /**
     * Advance time by hours.
     */
    suspend fun advanceHours(hours: Int): WorldTime {
        return advanceMinutes(hours * WorldTime.MINUTES_PER_HOUR)
    }
    
    /**
     * Advance time by days.
     */
    suspend fun advanceDays(days: Int): WorldTime {
        return advanceHours(days * WorldTime.HOURS_PER_DAY)
    }
    
    /**
     * Set the world time to a specific value.
     * Use with caution - prefer using advance methods for natural progression.
     */
    suspend fun setTime(newTime: WorldTime) {
        mutex.withLock {
            _worldTime.value = newTime
        }
    }
    
    /**
     * Pause time progression.
     */
    fun pause() {
        _isPaused = true
    }
    
    /**
     * Resume time progression.
     */
    fun resume() {
        _isPaused = false
    }
    
    /**
     * Toggle pause state.
     */
    fun togglePause() {
        _isPaused = !_isPaused
    }
    
    /**
     * Set time speed multiplier.
     * @param speed Multiplier (1.0 = normal, 2.0 = double speed, 0.5 = half speed)
     */
    fun setTimeSpeed(speed: Double) {
        require(speed > 0) { "Time speed must be positive" }
        _timeSpeed = speed
    }
    
    /**
     * Reset time to the beginning (Day 1, Spring, 6:00 AM)
     */
    suspend fun resetTime() {
        setTime(WorldTime())
    }
    
    /**
     * Get the current season.
     */
    fun getCurrentSeason(): Season = _worldTime.value.season
    
    /**
     * Get the current time of day.
     */
    fun getCurrentTimeOfDay(): TimeOfDay = _worldTime.value.getTimeOfDay()
    
    /**
     * Check if it's currently daytime.
     */
    fun isDay(): Boolean = _worldTime.value.isDay()
    
    /**
     * Check if it's currently nighttime.
     */
    fun isNight(): Boolean = _worldTime.value.isNight()
    
    /**
     * Get the current day number.
     */
    fun getCurrentDay(): Int = _worldTime.value.day
    
    /**
     * Get the current hour (0-23).
     */
    fun getCurrentHour(): Int = _worldTime.value.hour
    
    /**
     * Get total elapsed game time in ticks.
     */
    fun getTotalTicks(): Long = _worldTime.value.totalTicks
    
    /**
     * Calculate time until next sunrise.
     * Returns the number of hours until 6:00 AM.
     */
    fun hoursUntilSunrise(): Int {
        val current = _worldTime.value
        return if (current.hour < 6) {
            6 - current.hour
        } else {
            24 - current.hour + 6
        }
    }
    
    /**
     * Calculate time until next sunset.
     * Returns the number of hours until 19:00 (7 PM).
     */
    fun hoursUntilSunset(): Int {
        val current = _worldTime.value
        return if (current.hour < 19) {
            19 - current.hour
        } else {
            24 - current.hour + 19
        }
    }
    
    /**
     * Calculate days until next season.
     */
    fun daysUntilNextSeason(): Int {
        return WorldTime.DAYS_PER_SEASON - _worldTime.value.day + 1
    }
    
    /**
     * Get the next season.
     */
    fun getNextSeason(): Season {
        return when (_worldTime.value.season) {
            Season.SPRING -> Season.SUMMER
            Season.SUMMER -> Season.AUTUMN
            Season.AUTUMN -> Season.WINTER
            Season.WINTER -> Season.SPRING
        }
    }
    
    private fun calculateNextTick(current: WorldTime): WorldTime {
        val newTotalTicks = current.totalTicks + 1
        
        // Calculate new minute
        var newMinute = current.minute
        var newHour = current.hour
        var newDay = current.day
        var newSeason = current.season
        
        // Increment minute every TICKS_PER_MINUTE ticks
        if (newTotalTicks % WorldTime.TICKS_PER_MINUTE == 0L) {
            newMinute++
            
            // Hour change
            if (newMinute >= WorldTime.MINUTES_PER_HOUR) {
                newMinute = 0
                newHour++
                
                // Day change
                if (newHour >= WorldTime.HOURS_PER_DAY) {
                    newHour = 0
                    newDay++
                    
                    // Season change
                    if (newDay > WorldTime.DAYS_PER_SEASON) {
                        newDay = 1
                        newSeason = when (newSeason) {
                            Season.SPRING -> Season.SUMMER
                            Season.SUMMER -> Season.AUTUMN
                            Season.AUTUMN -> Season.WINTER
                            Season.WINTER -> Season.SPRING
                        }
                    }
                }
            }
        }
        
        return WorldTime(
            totalTicks = newTotalTicks,
            season = newSeason,
            day = newDay,
            hour = newHour,
            minute = newMinute
        )
    }
}
