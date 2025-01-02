package com.juffyto.juffyto.ui.screens.calculator.selection.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun IESFilters(
    tiposDisponibles: Set<String>,
    gestionesDisponibles: Set<String>,
    tiposSeleccionados: Set<String>,
    gestionesSeleccionadas: Set<String>,
    onTipoSelected: (String) -> Unit,
    onGestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de Tipos de IES
        Column {
            Text(
                text = "Tipo de IES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            tiposDisponibles.forEach { tipo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = tiposSeleccionados.contains(tipo),
                        onCheckedChange = { onTipoSelected(tipo) }
                    )
                    Text(
                        text = tipo,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Sección de Gestión de IES
        Column {
            Text(
                text = "Gestión de IES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            gestionesDisponibles.forEach { gestion ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = gestionesSeleccionadas.contains(gestion),
                        onCheckedChange = { onGestionSelected(gestion) }
                    )
                    Text(
                        text = gestion,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}