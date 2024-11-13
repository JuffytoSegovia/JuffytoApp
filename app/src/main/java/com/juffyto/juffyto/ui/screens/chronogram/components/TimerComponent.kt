package com.juffyto.juffyto.ui.screens.chronogram.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juffyto.juffyto.ui.screens.chronogram.model.*
import com.juffyto.juffyto.ui.theme.*
import com.juffyto.juffyto.utils.DateUtils
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun TimerContent(phases: List<Phase>) {
    var timeRemaining by remember { mutableStateOf<List<PhaseTimer>>(emptyList()) }

    // Actualizar el tiempo cada minuto
    LaunchedEffect(key1 = phases) {
        while (true) {
            val currentTimers = calculateTimers(phases)
            timeRemaining = currentTimers
            delay(60000) // Actualizar cada minuto
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        timeRemaining.forEach { phaseTimer ->
            TimerCard(phaseTimer)
        }

        if (timeRemaining.isEmpty()) {
            Text(
                text = "No hay fases activas o próximas",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TimerCard(phaseTimer: PhaseTimer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (phaseTimer.isActive) StatusActive else StatusUpcoming
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = phaseTimer.phase.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = if (phaseTimer.isActive) "Tiempo restante para finalizar:" else "Tiempo para comenzar:",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimeUnit("Meses", phaseTimer.timeRemaining.months)
                TimeUnit("Días", phaseTimer.timeRemaining.days)
                TimeUnit("Horas", phaseTimer.timeRemaining.hours)
                TimeUnit("Min", phaseTimer.timeRemaining.minutes)
            }
        }
    }
}

@Composable
private fun TimeUnit(label: String, value: Long) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format(Locale.getDefault(), "%02d", value),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun calculateTimers(phases: List<Phase>): List<PhaseTimer> {
    val now = DateUtils.getCurrentDateTimeInPeru()
    val timers = mutableListOf<PhaseTimer>()

    // Encontrar fases activas
    phases.filter { it.isActive }.forEach { phase ->
        phase.endDate?.let { endDate ->
            val endDateTime = endDate.atTime(23, 59, 59)
            timers.add(
                PhaseTimer(
                    phase = phase,
                    isActive = true,
                    timeRemaining = calculateTimeRemaining(endDateTime),
                    type = PhaseTimer.TimerType.ACTIVE_ENDING
                )
            )
        }
    }

    // Si no hay fases activas, encontrar la próxima fase
    if (timers.isEmpty()) {
        phases.filter { !it.isPast && !it.isActive }
            .minByOrNull { phase ->
                phase.startDate?.atStartOfDay() ?: phase.singleDate?.atStartOfDay() ?: LocalDateTime.MAX
            }?.let { nextPhase ->
                val startDateTime = (nextPhase.startDate ?: nextPhase.singleDate)?.atStartOfDay()
                startDateTime?.let {
                    timers.add(
                        PhaseTimer(
                            phase = nextPhase,
                            isActive = false,
                            timeRemaining = calculateTimeRemaining(it),
                            type = PhaseTimer.TimerType.UPCOMING_STARTING
                        )
                    )
                }
            }
    }

    return timers
}