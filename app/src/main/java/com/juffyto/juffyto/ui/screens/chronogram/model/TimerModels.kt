package com.juffyto.juffyto.ui.screens.chronogram.model

import java.time.LocalDateTime

data class TimeRemaining(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)

data class PhaseTimer(
    val phase: Phase,
    val isActive: Boolean,
    val timeRemaining: TimeRemaining,
    val type: TimerType
) {
    enum class TimerType {
        ACTIVE_ENDING,    // Para fases activas, contando hacia su finalización
        UPCOMING_STARTING // Para fases próximas, contando hacia su inicio
    }
}

fun calculateTimeRemaining(now: LocalDateTime, target: LocalDateTime): TimeRemaining {
    var remaining = if (target.isAfter(now)) {
        java.time.Duration.between(now, target)
    } else {
        java.time.Duration.ZERO
    }

    val days = remaining.toDays()
    remaining = remaining.minusDays(days)

    val hours = remaining.toHours()
    remaining = remaining.minusHours(hours)

    val minutes = remaining.toMinutes()
    remaining = remaining.minusMinutes(minutes)

    val seconds = remaining.seconds

    return TimeRemaining(
        days = days,
        hours = hours,
        minutes = minutes,
        seconds = seconds
    )
}