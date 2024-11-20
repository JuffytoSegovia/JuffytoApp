package com.juffyto.juffyto.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.data.model.NotificationFrequency

@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Hora de notificación",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DropdownMenu(
            expanded = false, // Implementar lógica de expansión
            onDismissRequest = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("09:00", "12:00", "18:00").forEach { time ->
                DropdownMenuItem(
                    text = { Text(time) },
                    onClick = { onTimeSelected(time) }
                )
            }
        }
    }
}

@Composable
fun FrequencySelector(
    selectedFrequency: NotificationFrequency,
    onFrequencySelected: (NotificationFrequency) -> Unit
) {
    Column {
        Text(
            text = "Frecuencia de recordatorios",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
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
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = when (frequency) {
                        NotificationFrequency.DAILY -> "Notificación diaria del estado actual"
                        NotificationFrequency.WEEKLY -> "Resumen semanal de próximos eventos"
                        NotificationFrequency.IMPORTANT_ONLY -> "Solo notifica fechas críticas"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}