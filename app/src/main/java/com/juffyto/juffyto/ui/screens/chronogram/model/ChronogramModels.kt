package com.juffyto.juffyto.ui.screens.chronogram.model

import com.juffyto.juffyto.utils.DateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Phase(
    val title: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val singleDate: LocalDate?
) {
    val isActive: Boolean
        get() {
            val now = DateUtils.getCurrentDateInPeru()
            return when {
                singleDate != null -> singleDate == now
                startDate != null && endDate != null -> now in startDate..endDate
                else -> false
            }
        }

    val isPast: Boolean
        get() {
            val now = DateUtils.getCurrentDateInPeru() // Cambiado para usar la hora de Perú
            return when {
                singleDate != null -> singleDate.isBefore(now)
                endDate != null -> endDate.isBefore(now)
                else -> false
            }
        }

    val displayDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return when {
                singleDate != null -> singleDate.format(formatter)
                startDate != null && endDate != null ->
                    "${startDate.format(formatter)} - ${endDate.format(formatter)}"
                else -> ""
            }
        }
}

data class Stage(
    val title: String,
    val phases: List<Phase>
)