package com.juffyto.juffyto.ui.screens.settings

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juffyto.juffyto.data.model.NotificationSettings
import com.juffyto.juffyto.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    settings: NotificationSettings,
    onDismiss: () -> Unit,
    onSaveSettings: (NotificationSettings) -> Unit
) {
    var currentSettings by remember { mutableStateOf(settings) }

    LaunchedEffect(settings) {
        currentSettings = settings
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }

                    Text(
                        text = "Configuración de Notificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NotificationSwitch(
                            enabled = currentSettings.enabled,
                            onEnabledChange = { enabled ->
                                currentSettings = currentSettings.copy(enabled = enabled)
                                // Ya no guardamos inmediatamente el estado
                            }
                        )

                        AnimatedVisibility(
                            visible = currentSettings.enabled,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                TimeSelector(
                                    selectedTime = currentSettings.notificationTime,
                                    onTimeSelected = { time ->
                                        currentSettings = currentSettings.copy(notificationTime = time)
                                    }
                                )

                                FrequencySelector(
                                    selectedFrequency = currentSettings.notificationFrequency,
                                    onFrequencySelected = { frequency ->
                                        currentSettings = currentSettings.copy(
                                            notificationFrequency = frequency
                                        )
                                    }
                                )

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = buildString {
                                                append("Recibirás notificaciones a las ${currentSettings.notificationTime} ")
                                                append(currentSettings.notificationFrequency.getDisplayName())
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Botones siempre visibles
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                currentSettings = settings
                                onDismiss()
                            }
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                onSaveSettings(currentSettings)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            )
                        ) {
                            Text("Guardar cambios")
                        }
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notificaciones Push",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
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