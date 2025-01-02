package com.juffyto.juffyto.ui.screens.calculator.selection.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.ui.screens.calculator.selection.model.IES

@Composable
fun RecommendationsDialog(
    currentPuntaje: Int,
    currentRegion: String,
    recommendedIES: List<IES>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🧐IES Recomendadas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "En base a tu puntaje actual ($currentPuntaje puntos), te mostramos IES que podrían mejorar tu puntaje:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                val iesEnRegion = recommendedIES.filter { it.regionIES == currentRegion }
                if (iesEnRegion.isNotEmpty()) {
                    item {
                        Text(
                            text = "👉En tu región ($currentRegion):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(iesEnRegion) { ies ->
                        RecommendedIESCard(
                            ies = ies
                        )
                    }
                }

                val iesOtrasRegiones = recommendedIES.filter { it.regionIES != currentRegion }
                if (iesOtrasRegiones.isNotEmpty()) {
                    item {
                        Text(
                            text = "👉En otras regiones:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(iesOtrasRegiones) { ies ->
                        RecommendedIESCard(
                            ies = ies
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "⚠️Información Importante",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Informate sobre el admisión de las IES.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Verifica la disponibilidad de tu carrera.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Ten en cuenta la ubicación e ingreso.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• El puntaje que podrías sumar a tu PS.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entendido")
            }
        }
    )
}

@Composable
private fun RecommendedIESCard(
    ies: IES
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "🏢"+ies.nombreIES,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Región: ${ies.regionIES}")
                    Text("Tipo: ${ies.tipoIES}")
                    Text("Gestión: ${ies.gestionIES}")
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    val puntajeC = ies.calcularPuntajeRanking()
                    val puntajeG = ies.calcularPuntajeGestion()
                    val puntajeS = ies.calcularPuntajeSelectividad()

                    Text("C: +$puntajeC")
                    Text("G: +$puntajeG")
                    Text("S: +$puntajeS")
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val puntajeIES = ies.calcularPuntajeTotal()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Podrías obtener:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "+$puntajeIES puntos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}