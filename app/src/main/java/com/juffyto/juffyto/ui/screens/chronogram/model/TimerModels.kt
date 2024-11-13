package com.juffyto.juffyto.ui.screens.chronogram.model

import com.juffyto.juffyto.utils.DateUtils
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class TimeRemaining(
    val months: Long,
    val days: Long,
    val hours: Long,
    val minutes: Long
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

fun calculateTimeRemaining(target: LocalDateTime): TimeRemaining {
    val now = DateUtils.getCurrentDateTimeInPeru()
    var remaining = if (target.isAfter(now)) {
        java.time.Duration.between(now, target)
    } else {
        java.time.Duration.ZERO
    }

    val months = ChronoUnit.MONTHS.between(now.toLocalDate(), target.toLocalDate())
    remaining = remaining.minusDays(ChronoUnit.DAYS.between(now.toLocalDate(), now.toLocalDate().plusMonths(months)))

    val days = remaining.toDays()
    remaining = remaining.minusDays(days)

    val hours = remaining.toHours()
    remaining = remaining.minusHours(hours)

    val minutes = remaining.toMinutes()

    return TimeRemaining(
        months = months,
        days = days,
        hours = hours,
        minutes = minutes
    )
}