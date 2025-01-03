package com.juffyto.juffyto.ui.screens.calculator.preselection.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@Composable
fun RecommendationsDialog(
    puntajeENP: Int,
    puntajeTotal: Int,
    onDismiss: () -> Unit
) {
    var yaRendioENP by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🧐 Recomendaciones Personalizadas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Análisis del ENP
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "📊 Análisis del ENP",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val preguntasCorrectas = puntajeENP / 2
                            val preguntasFaltantes = 60 - preguntasCorrectas
                            val puntosPosibles = preguntasFaltantes * 2

                            if (puntajeENP > 0) {
                                Text("• Has respondido correctamente aproximadamente $preguntasCorrectas de 60 preguntas")
                                if (preguntasFaltantes > 0) {
                                    Text("• Podrías mejorar tu puntaje hasta en $puntosPosibles puntos adicionales")
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "¿Ya rendiste el ENP?",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = yaRendioENP,
                            onCheckedChange = { yaRendioENP = it }
                        )
                    }
                }

                item {
                    // Recomendaciones según estado del ENP
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "🎯 Situación actual",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (yaRendioENP) {
                                Text(
                                    text = "Ya rendiste el ENP:",
                                    fontWeight = FontWeight.Bold
                                )
                                Text("• Verifica que tu estimación de respuestas correctas sea precisa")
                                Text("• Recuerda que el puntaje del ENP ya no puede modificarse")
                                Text("• En la etapa de selección podrás sumar puntos adicionales según la IES elegible")
                                Text("• Mantén listos tus documentos para las siguientes etapas")
                            } else {
                                Text(
                                    text = "Aún no rindes el ENP:",
                                    fontWeight = FontWeight.Bold
                                )
                                Text("• Este es el criterio donde puedes obtener más puntos (hasta 120)")
                                Text("• Aprovecha el tiempo para prepararte adecuadamente")
                                Text("• Enfócate en las áreas donde necesitas mejorar")
                                Text("• Practica con simulacros para familiarizarte con el formato")
                                Text("• Revisa el material de estudio disponible")
                            }
                        }
                    }
                }


                item {
                    // Estructura del ENP
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "📋 Estructura del ENP",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("El ENP consta de:")
                            Text("• 30 preguntas de competencia matemática")
                            Text("• 30 preguntas de competencia lectora")
                            Text("• Duración: 2 horas (120 minutos)")
                            Text("• Cada respuesta correcta vale 2 puntos")
                            Text("• No hay puntos en contra por respuestas incorrectas")
                        }
                    }
                }

                item {
                    // Recomendaciones de estudio
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "💡 Recomendaciones de Estudio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("🔢Para mejorar en competencia matemática:")
                            Text("• Practica aritmética, álgebra y geometría")
                            Text("• Resuelve problemas de razonamiento matemático")
                            Text("• Administra bien el tiempo por pregunta")

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📖Para mejorar en competencia lectora:")
                            Text("• Lee textos variados y analiza su contenido")
                            Text("• Practica comprensión de lectura")
                            Text("• Identifica ideas principales y secundarias")
                        }
                    }
                }

                item {
                    // Recursos de práctica
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "📚 Recursos de Práctica",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Utiliza los simulacros disponibles en PDF")
                            Text("• Practica con simulacros en línea")
                            Text("• Revisa preguntas de exámenes anteriores")
                            Text("• Participa en grupos de estudio")
                        }
                    }
                }

                if (puntajeTotal >= 90) {
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
                                    text = "🎯 Próximos Pasos",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Mantén tus documentos actualizados")
                                Text("• Revisa las fechas del cronograma")
                                Text("• Prepárate para la etapa de selección")
                            }
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