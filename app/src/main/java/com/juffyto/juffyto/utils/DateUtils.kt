package com.juffyto.juffyto.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object DateUtils {
    private val PERU_ZONE_ID = ZoneId.of("America/Lima")

    fun getCurrentDateInPeru(): LocalDate {
        return LocalDate.now(PERU_ZONE_ID)
    }

    fun getCurrentDateTimeInPeru(): LocalDateTime {
        return LocalDateTime.now(PERU_ZONE_ID)
    }
}