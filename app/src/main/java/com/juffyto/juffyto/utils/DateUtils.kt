package com.juffyto.juffyto.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object DateUtils {
    private val PERU_ZONE_ID = ZoneId.of("America/Lima")
    private var currentTestDateTime: LocalDateTime? = null
    private var isTestModeActive = false

    fun getCurrentDateTimeInPeru(): LocalDateTime {
        return if (isTestModeActive) currentTestDateTime ?: LocalDateTime.now(PERU_ZONE_ID)
        else LocalDateTime.now(PERU_ZONE_ID)
    }

    fun getCurrentDateInPeru(): LocalDate {
        return getCurrentDateTimeInPeru().toLocalDate()
    }

    fun advanceTime(minutes: Long) {
        currentTestDateTime = (currentTestDateTime ?: LocalDateTime.now(PERU_ZONE_ID))
            .plusMinutes(minutes)
        isTestModeActive = true
    }

    fun resetToRealTime() {
        currentTestDateTime = null
        isTestModeActive = false
    }

    fun ensureRealTimeMode() {
        resetToRealTime()
    }
}