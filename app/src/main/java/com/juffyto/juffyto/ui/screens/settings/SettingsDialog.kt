package com.juffyto.juffyto.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juffyto.juffyto.data.model.NotificationSettings

@Composable
fun SettingsDialog(
    settings: NotificationSettings,
    onDismiss: () -> Unit,
    onSaveSettings: (NotificationSettings) -> Unit
) {
    var currentSettings by remember { mutableStateOf(settings) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título
                Text(
                    text = "Configuración de Notificaciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Switch de Notificaciones
                NotificationSwitch(
                    enabled = currentSettings.enabled,
                    onEnabledChange = { enabled ->
                        currentSettings = currentSettings.copy(enabled = enabled)
                    }
                )

                if (currentSettings.enabled) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de Hora
                    TimeSelector(
                        selectedTime = currentSettings.notificationTime,
                        onTimeSelected = { time ->
                            currentSettings = currentSettings.copy(notificationTime = time)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de Frecuencia
                    FrequencySelector(
                        selectedFrequency = currentSettings.notificationFrequency,
                        onFrequencySelected = { frequency ->
                            currentSettings = currentSettings.copy(notificationFrequency = frequency)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSaveSettings(currentSettings) }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationSwitch(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Notificaciones Push",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Recibe alertas sobre fechas importantes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange
            )
        }
    }
}