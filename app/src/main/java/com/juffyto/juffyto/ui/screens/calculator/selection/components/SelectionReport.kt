package com.juffyto.juffyto.ui.screens.calculator.selection.components

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.ui.components.ads.RewardedInterstitialAdManager
import com.juffyto.juffyto.ui.components.dialogs.RewardedAdDialog
import com.juffyto.juffyto.ui.screens.calculator.selection.SelectionViewModel
import com.juffyto.juffyto.ui.screens.calculator.selection.model.IES
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SelectionReport(
    nombre: String,
    puntajeTotal: Int,
    desglosePuntaje: String,
    ies: IES,
    modalidad: String,
    onShowRecommendations: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SelectionViewModel,
    rewardedInterstitialAdManager: RewardedInterstitialAdManager,
    context: Context,
    modifier: Modifier = Modifier,
) {
    var showRecommendationsAdDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📢Reporte de Selección para",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    puntajeTotal >= 120 -> Color(0xFF4CAF50)
                    puntajeTotal >= 110 -> Color(0xFF2196F3)
                    puntajeTotal >= 100 -> Color(0xFFFFA726)
                    else -> Color(0xFFF44336)
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯Tu puntaje de selección es:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$puntajeTotal puntos",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "📊Desglose del puntaje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = desglosePuntaje)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "➕Fórmula de Selección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Puntaje Total = PS + C + G + S",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFBB86FC).copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "🏢IES Seleccionada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ies.nombreIES,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Puntaje Total IES:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "+${ies.calcularPuntajeTotal()} puntos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "🚫Puntaje máximo para la modalidad ${modalidad}:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (modalidad == "EIB") "210 puntos" else "200 puntos",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = when {
                        puntajeTotal >= 120 -> "😎 Mensaje para ti"
                        puntajeTotal >= 110 -> "🤓 Mensaje para ti"
                        puntajeTotal >= 100 -> "🤔 Mensaje para ti"
                        else -> "😭 Mensaje para ti"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        puntajeTotal >= 120 -> "¡Felicidades! Tienes una excelente probabilidad de ganar la beca. Asegúrate de completar tu postulación en las fechas indicadas."
                        puntajeTotal >= 110 -> "¡Muy bien! Tienes buenas posibilidades de ganar la beca. No olvides estar pendiente del cronograma de postulación."
                        puntajeTotal >= 100 -> "Tienes posibilidades de ganar la beca. Te recomendamos revisar otras IES que podrían aumentar tu puntaje."
                        else -> "Tu puntaje está por debajo del promedio. Te sugerimos explorar otras IES que podrían mejorar tu puntaje significativamente."
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        val uriHandler = LocalUriHandler.current
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text ="📌Información de la calculadora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Esta calculadora está basada en los criterios y puntajes establecidos en:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tabla 6: Criterios y puntaje de Selección - Bases del Concurso Beca 18 - Convocatoria 2025",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://cdn.www.gob.pe/uploads/document/file/6938514/5987321-rde-n-113-2024-minedu-vmgi-pronabec.pdf?v=1726261404")
                    },
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "💯 Interpretación del puntaje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                LeyendaColor(
                    color = Color(0xFF4CAF50),
                    texto = "120+ puntos: Excelentes posibilidades",
                    style = MaterialTheme.typography.bodyMedium
                )
                LeyendaColor(
                    color = Color(0xFF2196F3),
                    texto = "110-119 puntos: Buenas posibilidades",
                    style = MaterialTheme.typography.bodyMedium
                )
                LeyendaColor(
                    color = Color(0xFFFFA726),
                    texto = "100-109 puntos: Posibilidades moderadas",
                    style = MaterialTheme.typography.bodyMedium
                )
                LeyendaColor(
                    color = Color(0xFFF44336),
                    texto = "Menos de 100 puntos: Bajas posibilidades",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Nota: Estos rangos son referenciales y no garantizan la obtención de la beca.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Button(
            onClick = {
                if (viewModel.isAccessUnlocked()) {
                    onShowRecommendations()
                } else {
                    showRecommendationsAdDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver IES Recomendadas")
        }

        if (showRecommendationsAdDialog) {
            RewardedAdDialog(
                title = "Ver IES Recomendadas",
                description = "Mira el anuncio completo para ver las IES que podrían mejorar tu puntaje y aumentar tus posibilidades de ganar la beca.",
                onConfirm = {
                    showRecommendationsAdDialog = false
                    rewardedInterstitialAdManager.showAd(context as Activity) {
                        viewModel.unlockAccess()
                        onShowRecommendations()
                    }
                },
                onDismiss = {
                    showRecommendationsAdDialog = false
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Anterior")
            }
            Button(
                onClick = { viewModel.resetCalculator() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reiniciar")
            }
        }
    }
}

@Composable
private fun LeyendaColor(
    color: Color,
    texto: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            style = style
        )
    }
}