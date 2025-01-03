package com.juffyto.juffyto.ui.screens.calculator.preselection.components

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.juffyto.juffyto.ui.components.ads.RewardedInterstitialAdManager
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionConstants
import com.juffyto.juffyto.ui.components.dialogs.RewardedAdDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

@Composable
fun PreselectionReport(
    nombre: String,
    puntajeTotal: Int,
    modalidad: String,
    desglosePuntaje: String,
    onShowRecommendations: () -> Unit,
    onBackClick: () -> Unit,
    onReset: () -> Unit,
    viewModel: PreselectionViewModel,
    rewardedInterstitialAdManager: RewardedInterstitialAdManager,
    context: Context,
    modifier: Modifier = Modifier
) {
    var showRecommendationsAdDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
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
                    text = "📢 Reporte de Preselección para",
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

        // Puntaje Total
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    puntajeTotal >= 90 -> Color(0xFF4CAF50)
                    puntajeTotal >= 80 -> Color(0xFFFFA726)
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
                    text = "🎯Tu puntaje de preselección será:",
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

        // Desglose del puntaje
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📊 Desglose del puntaje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = desglosePuntaje)
            }
        }

        // Fórmula
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
                    text = "➕ Fórmula de Preselección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (modalidad) {
                        "Ordinaria" -> "PS = ENP + PE + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25"
                        "CNA y PA" -> "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + PEM + PD + OR + ACS)max 25"
                        "EIB" -> "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25 + LO"
                        "Protección" -> "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + ACS)max 25 + DF"
                        else -> "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25"
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontStyle = FontStyle.Italic
                )
            }
        }

        // Puntaje máximo
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "🚫 Puntaje máximo para la modalidad $modalidad:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (modalidad == "EIB") "${PreselectionConstants.MAX_PUNTAJE_EIB} puntos"
                    else "${PreselectionConstants.MAX_PUNTAJE_NORMAL} puntos",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Mensaje de ánimo
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = when {
                        puntajeTotal >= 90 -> "😎 Mensaje para ti"
                        puntajeTotal >= 80 -> "🤓 Mensaje para ti"
                        else -> "😅 Mensaje para ti"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        puntajeTotal >= 90 -> "¡Felicidades! Tienes grandes posibilidades de ganar la beca. Mantén todos tus documentos listos para la siguiente etapa."
                        puntajeTotal >= 80 -> "¡Buen esfuerzo! Estás en buen camino para obtener la beca. No te rindas y mantén tus documentos listos."
                        else -> "Cada punto cuenta. Sigue trabajando duro y no pierdas la esperanza. Aún puedes mejorar tu puntaje en el ENP y revisar si cumples con más condiciones priorizables."
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Información de la calculadora
        val uriHandler = LocalUriHandler.current
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📌 Información de la calculadora",
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
                    text = "Tabla 4: Criterios y puntaje de Preselección - Bases del Concurso Beca 18 - Convocatoria 2025",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://cdn.www.gob.pe/uploads/document/file/6938514/5987321-rde-n-113-2024-minedu-vmgi-pronabec.pdf?v=1726261404")
                    },
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        // Interpretación del puntaje
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
                    texto = "90+ puntos: Grandes posibilidades",
                    style = MaterialTheme.typography.bodyMedium
                )
                LeyendaColor(
                    color = Color(0xFFFFA726),
                    texto = "80-89 puntos: Buenas posibilidades",
                    style = MaterialTheme.typography.bodyMedium
                )
                LeyendaColor(
                    color = Color(0xFFF44336),
                    texto = "Menos de 80 puntos: Necesitas mejorar",
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

        // Botón de recomendaciones
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
            Text("Ver Recomendaciones")
        }

        // Agregar el diálogo
        if (showRecommendationsAdDialog) {
            RewardedAdDialog(
                title = "Ver Recomendaciones",
                description = "Mira el anuncio completo para ver recomendaciones personalizadas que te ayudarán a mejorar tu puntaje.",
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

        // Botones de navegación
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
                onClick = onReset,
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