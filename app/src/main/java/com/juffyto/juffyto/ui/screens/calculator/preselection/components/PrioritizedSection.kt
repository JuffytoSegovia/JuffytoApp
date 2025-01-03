package com.juffyto.juffyto.ui.screens.calculator.preselection.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.CondicionPriorizable
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionConstants

@Composable
fun PrioritizedSection(
    selectedConditions: Set<CondicionPriorizable>,
    modalidad: String,
    onConditionToggled: (CondicionPriorizable) -> Unit,
    onClearConditions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Título y botón limpiar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Condiciones Priorizables",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = onClearConditions,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Limpiar selección",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpiar")
                }
            }

            Text(
                text = "Máximo ${PreselectionConstants.MAX_PUNTAJE_CONDICIONES} puntos en total",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkboxes de condiciones priorizables
            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.DISCAPACIDAD),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.DISCAPACIDAD) },
                label = "(D) Discapacidad - 5 puntos",
                enabled = true
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.BOMBEROS),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.BOMBEROS) },
                label = "(B) Bomberos activos e hijos de bomberos - 5 puntos",
                enabled = true
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.VOLUNTARIOS),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.VOLUNTARIOS) },
                label = "(V) Voluntarios reconocidos por el MIMP - 5 puntos",
                enabled = true
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.COMUNIDAD_NATIVA),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.COMUNIDAD_NATIVA) },
                label = "(IA) Pertenencia a Comunidad Nativa Amazónica, Población Afroperuana o Comunidad Campesina - 5 puntos",
                enabled = modalidad != "CNA y PA"
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.METALES_PESADOS),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.METALES_PESADOS) },
                label = "(PEM) Población expuesta a metales pesados y otras sustancias químicas - 5 puntos",
                enabled = true
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.POBLACION_BENEFICIARIA),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.POBLACION_BENEFICIARIA) },
                label = "(PD) Población beneficiaria (Res. Suprema Nº 264-2022-JUS) - 5 puntos",
                enabled = true
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.ORFANDAD),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.ORFANDAD) },
                label = "(OR) Población beneficiaria de la Ley N° 31405 (orfandad) - 5 puntos",
                enabled = modalidad != "Protección"
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.DESPROTECCION),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.DESPROTECCION) },
                label = "(DF) Desprotección familiar - 5 puntos",
                enabled = modalidad == "Protección"
            )

            PriorityCheckbox(
                checked = selectedConditions.contains(CondicionPriorizable.AGENTE_SALUD),
                onCheckedChange = { onConditionToggled(CondicionPriorizable.AGENTE_SALUD) },
                label = "(ACS) Agente Comunitario de Salud - 5 puntos",
                enabled = true
            )
        }
    }
}

@Composable
private fun PriorityCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}