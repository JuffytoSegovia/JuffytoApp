package com.juffyto.juffyto.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object DateUtils {
    private val PERU_ZONE_ID = ZoneId.of("America/Lima")

    // Variable para simular diferentes fechas en pruebas
    private var currentTestDateTime: LocalDateTime? = null

    fun getCurrentDateTimeInPeru(): LocalDateTime {
        return currentTestDateTime ?: LocalDateTime.now(PERU_ZONE_ID)
    }

    fun getCurrentDateInPeru(): LocalDate {
        return getCurrentDateTimeInPeru().toLocalDate()
    }

    // Función para pruebas: simular una fecha específica
    fun setTestDateTime(dateTime: LocalDateTime) {
        currentTestDateTime = dateTime
    }

    // Función para pruebas: avanzar el tiempo simulado
    fun advanceTime(minutes: Long) {
        currentTestDateTime = (currentTestDateTime ?: LocalDateTime.now(PERU_ZONE_ID))
            .plusMinutes(minutes)
    }

    // Función para pruebas: resetear al tiempo real
    fun resetToRealTime() {
        currentTestDateTime = null
    }
}