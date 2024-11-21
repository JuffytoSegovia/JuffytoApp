package com.juffyto.juffyto.ui.screens.settings

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val timeOptions = remember {
        listOf(
            "08:00 AM",
            "09:00 AM",
            "10:00 AM",
            "11:00 AM",
            "12:00 PM",
            "01:00 PM",
            "02:00 PM",
            "03:00 PM",
            "04:00 PM",
            "05:00 PM",
            "06:00 PM"
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Hora de notificación",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedTime,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .exposedDropdownSize(), // Corregido: usando exposedDropdownSize en lugar de menuAnchor
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                placeholder = {
                    Text("Selecciona la hora (Hora Perú)")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                timeOptions.forEach { time ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = time,
                                fontWeight = if (time == selectedTime) FontWeight.Medium else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onTimeSelected(time)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        leadingIcon = if (time == selectedTime) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else null,
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
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