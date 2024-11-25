package com.juffyto.juffyto.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.data.model.NotificationFrequency
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var hour by remember { mutableIntStateOf(9) }
    var minute by remember { mutableIntStateOf(0) }
    var isPM by remember { mutableStateOf(false) }

    // Inicializar valores desde selectedTime
    LaunchedEffect(selectedTime) {
        if (selectedTime != "Selecciona una hora") {
            try {
                val parts = selectedTime.split(":")
                val hourPart = parts[0].toInt()
                val minutePart = parts[1].substring(0, 2).toInt()
                val periodPart = selectedTime.endsWith("PM")

                hour = if (hourPart == 12) 12 else hourPart % 12
                minute = minutePart
                isPM = periodPart
            } catch (_: Exception) {
                hour = 9
                minute = 0
                isPM = false
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Hora de notificación",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (selectedTime == "Selecciona una hora")
                        "Selecciona la hora para recibir notificaciones"
                    else selectedTime,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
    }

    if (showTimePicker) {
        Dialog(
            onDismissRequest = { showTimePicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seleccionar hora",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Spinner de Hora
                        NumberSpinner(
                            value = hour,
                            onValueChange = { hour = it },
                            range = 1..12,
                            label = "Hora"
                        )

                        // Spinner de Minutos
                        NumberSpinner(
                            value = minute,
                            onValueChange = { minute = it },
                            range = 0..59,
                            label = "Min",
                            format = { it.toString().padStart(2, '0') }
                        )

                        // Selector AM/PM
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Periodo", style = MaterialTheme.typography.labelSmall)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(onClick = { isPM = !isPM }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Cambiar periodo"
                                    )
                                }
                                Text(if (isPM) "PM" else "AM")
                                IconButton(onClick = { isPM = !isPM }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Cambiar periodo"
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val formattedHour = if (hour == 12) "12" else hour.toString().padStart(2, '0')
                                val formattedMinute = minute.toString().padStart(2, '0')
                                val period = if (isPM) "PM" else "AM"
                                onTimeSelected("$formattedHour:$formattedMinute $period")
                                showTimePicker = false
                            }
                        ) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberSpinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String,
    format: (Int) -> String = { it.toString() }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        IconButton(
            onClick = {
                val newValue = if (value >= range.last) range.first else value + 1
                onValueChange(newValue)
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Incrementar"
            )
        }
        Text(
            text = format(value),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(
            onClick = {
                val newValue = if (value <= range.first) range.last else value - 1
                onValueChange(newValue)
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrementar"
            )
        }
    }
}

@Composable
fun FrequencySelector(
    selectedFrequency: NotificationFrequency,
    onFrequencySelected: (NotificationFrequency) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Frecuencia de recordatorios",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        NotificationFrequency.entries.forEach { frequency ->
            FrequencyOption(
                frequency = frequency,
                selected = frequency == selectedFrequency,
                onSelect = { onFrequencySelected(frequency) }
            )
        }
    }
}

@Composable
private fun FrequencyOption(
    frequency: NotificationFrequency,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .selectable(
                selected = selected,
                onClick = onSelect
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (frequency) {
                        NotificationFrequency.DAILY -> "Diario"
                        NotificationFrequency.WEEKLY -> "Semanal"
                        NotificationFrequency.IMPORTANT_ONLY -> "Solo fechas importantes"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Notificaciones " + frequency.getDisplayName(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}