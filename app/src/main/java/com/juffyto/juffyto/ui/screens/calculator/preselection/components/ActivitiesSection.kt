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
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.ActividadExtracurricular
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionConstants

@Composable
fun ActivitiesSection(
    selectedActivities: Set<ActividadExtracurricular>,
    onActivityToggled: (ActividadExtracurricular) -> Unit,
    onClearActivities: () -> Unit,
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
                    text = "Actividades Extracurriculares",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = onClearActivities,
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
                text = "Máximo ${PreselectionConstants.MAX_PUNTAJE_ACTIVIDADES} puntos en total",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkboxes de actividades
            ActivityCheckbox(
                checked = selectedActivities.contains(ActividadExtracurricular.CONCURSO_NACIONAL),
                onCheckedChange = { onActivityToggled(ActividadExtracurricular.CONCURSO_NACIONAL) },
                label = "(CE) Concurso Escolar Nacional (1°, 2º o 3º Puesto Etapa Nacional) o Concurso Escolar Internacional - 5 puntos"
            )

            ActivityCheckbox(
                checked = selectedActivities.contains(ActividadExtracurricular.CONCURSO_PARTICIPACION),
                onCheckedChange = { onActivityToggled(ActividadExtracurricular.CONCURSO_PARTICIPACION) },
                label = "(CEP) Participación en la Etapa Nacional de los Concursos Escolares Nacionales - 2 puntos"
            )

            ActivityCheckbox(
                checked = selectedActivities.contains(ActividadExtracurricular.JUEGOS_NACIONALES),
                onCheckedChange = { onActivityToggled(ActividadExtracurricular.JUEGOS_NACIONALES) },
                label = "(JD) Juegos Deportivos Escolares (1°, 2º o 3° puesto en Etapa Nacional) - 5 puntos"
            )

            ActivityCheckbox(
                checked = selectedActivities.contains(ActividadExtracurricular.JUEGOS_PARTICIPACION),
                onCheckedChange = { onActivityToggled(ActividadExtracurricular.JUEGOS_PARTICIPACION) },
                label = "(JDP) Participación en la Etapa Nacional de los Juegos Deportivos Escolares Nacionales - 2 puntos"
            )
        }
    }
}

@Composable
private fun ActivityCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
    }
}