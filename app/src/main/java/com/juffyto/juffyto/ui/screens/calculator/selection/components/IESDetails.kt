package com.juffyto.juffyto.ui.screens.calculator.selection.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.ui.screens.calculator.selection.model.IES

@Composable
fun IESDetails(
    ies: IES,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Detalles de la IES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Información básica
            Text("Tipo IES: ${ies.tipoIES}")
            Text("Gestión IES: ${ies.gestionIES}")
            Text("Región IES: ${ies.regionIES}")
            Text("Siglas IES: ${ies.siglasIES}")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Información de ranking
            Text(
                text = "Ranking y Puntajes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Top IES: ${ies.topIES}")
            Text("Ranking IES: ${ies.rankingIES}")
            Text("Puntaje Ranking: ${ies.calcularPuntajeRanking()} puntos")
            Text("Puntaje Gestión: ${ies.calcularPuntajeGestion()} puntos")
            Text("Ratio Selectividad: ${ies.ratioSelectividad}")
            Text("Puntaje Selectividad: ${ies.calcularPuntajeSelectividad()} puntos")

            Spacer(modifier = Modifier.height(8.dp))

            // Puntaje total
            Text(
                text = "Puntaje Total IES: ${ies.calcularPuntajeTotal()} puntos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}