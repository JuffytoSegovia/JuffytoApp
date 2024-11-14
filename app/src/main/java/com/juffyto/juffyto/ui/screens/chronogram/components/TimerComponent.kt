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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun TimerContent(phases: List<Phase>) {
    var timeRemaining by remember { mutableStateOf<List<PhaseTimer>>(emptyList()) }
    var showTestControls by remember { mutableStateOf(false) }

    // Actualizar el tiempo cada segundo
    LaunchedEffect(key1 = phases) {
        while (true) {
            val currentTimers = calculateTimers(phases)
            timeRemaining = currentTimers
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Opción de prueba
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modo prueba",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = showTestControls,
                onCheckedChange = { showTestControls = it },
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Controles de prueba
        if (showTestControls) {
            TestControls()
        }

        // Fases en curso
        Text(
            text = "FASES EN CURSO",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(top = if (showTestControls) 8.dp else 0.dp)
        )

        val activePhases = timeRemaining
            .filter { it.isActive }
            .sortedBy { it.phase.startDate ?: it.phase.singleDate }

        if (activePhases.isEmpty()) {
            Text(
                text = "No hay fases en curso actualmente",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        } else {
            activePhases.forEach { phaseTimer ->
                TimerCard(phaseTimer)
            }
        }

        // Próxima fase
        Text(
            text = "PRÓXIMAS FASES",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(top = 24.dp)
        )

        val nextPhase = timeRemaining
            .filter { !it.isActive }
            .minByOrNull { it.phase.startDate ?: it.phase.singleDate ?: LocalDate.MAX }

        if (nextPhase != null) {
            TimerCard(nextPhase)
        } else {
            Text(
                text = "No hay fases próximas programadas",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TestControls() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)  // Espaciado uniforme entre elementos
        ) {
            Text(
                text = "Controles de prueba",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            // Primera fila de botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)  // Espaciado uniforme entre botones
            ) {
                Button(
                    onClick = { DateUtils.advanceTime(1) },
                    modifier = Modifier
                        .weight(1f)  // Peso igual para cada botón
                        .height(48.dp),  // Altura fija para todos los botones
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "+1 min",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button(
                    onClick = { DateUtils.advanceTime(60) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "+1 hora",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Segunda fila de botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { DateUtils.advanceTime(1440) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "+1 día",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button(
                    onClick = { DateUtils.advanceTime(10080) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "+1 semana",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Botón de reset
            Button(
                onClick = { DateUtils.resetToRealTime() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Error),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Resetear al cronograma en vivo",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                TimeUnit("Días", phaseTimer.timeRemaining.days)
                TimeUnit("Horas", phaseTimer.timeRemaining.hours)
                TimeUnit("Min", phaseTimer.timeRemaining.minutes)
                TimeUnit("Seg", phaseTimer.timeRemaining.seconds)
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

    // Encontrar todas las fases activas (pueden ser múltiples)
    val activePhases = phases.filter { it.isActive }
    activePhases.forEach { phase ->
        when {
            phase.singleDate != null -> {
                // Para eventos de un solo día
                val endDateTime = phase.singleDate.atTime(23, 59, 59)
                timers.add(
                    PhaseTimer(
                        phase = phase,
                        isActive = true,
                        timeRemaining = calculateTimeRemaining(now, endDateTime),
                        type = PhaseTimer.TimerType.ACTIVE_ENDING
                    )
                )
            }
            phase.endDate != null -> {
                // Para fases con período
                val endDateTime = phase.endDate.atTime(23, 59, 59)
                timers.add(
                    PhaseTimer(
                        phase = phase,
                        isActive = true,
                        timeRemaining = calculateTimeRemaining(now, endDateTime),
                        type = PhaseTimer.TimerType.ACTIVE_ENDING
                    )
                )
            }
        }
    }

    // Encontrar la próxima fase si hay menos de 3 fases activas
    if (activePhases.size < 3) {
        phases.filter { !it.isPast && !it.isActive }
            .sortedBy { phase ->
                phase.startDate?.atStartOfDay() ?:
                phase.singleDate?.atStartOfDay() ?:
                LocalDateTime.MAX
            }
            .take(3 - activePhases.size)  // Tomar solo las próximas necesarias
            .forEach { nextPhase ->
                val startDateTime = (nextPhase.startDate ?: nextPhase.singleDate)?.atStartOfDay()
                startDateTime?.let {
                    timers.add(
                        PhaseTimer(
                            phase = nextPhase,
                            isActive = false,
                            timeRemaining = calculateTimeRemaining(now, it),
                            type = PhaseTimer.TimerType.UPCOMING_STARTING
                        )
                    )
                }
            }
    }

    // Ordenar por fecha de inicio/fin
    return timers.sortedBy { timer ->
        if (timer.isActive) {
            timer.phase.endDate?.atStartOfDay() ?:
            timer.phase.singleDate?.atStartOfDay()
        } else {
            timer.phase.startDate?.atStartOfDay() ?:
            timer.phase.singleDate?.atStartOfDay()
        }
    }
}