package com.juffyto.juffyto.ui.screens.enp

import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.theme.Primary
import com.juffyto.juffyto.utils.AdMobConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnpScreen(
    viewModel: EnpViewModel,
    onBackClick: () -> Unit
) {
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val showSolution by viewModel.showSolution.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val currentQuestion = viewModel.questions[currentQuestionIndex] // Cambiar esta línea

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ENP Beca 18 2025",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Contenido principal con scroll
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Barra de progreso
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    progress = { viewModel.getCurrentProgress() }
                )

                Text(
                    text = "Pregunta ${currentQuestionIndex + 1} de ${viewModel.questions.size}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                // Pregunta
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Opciones de respuesta
                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = userAnswers[currentQuestionIndex] == index
                    val isCorrect = showSolution && index == currentQuestion.correctOptionIndex

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            if (!showSolution) viewModel.selectAnswer(index)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isCorrect -> Color(0xFFE8F5E9)  // Verde claro
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                    ) {
                        Text(
                            text = "${('a' + index)}) $option",
                            modifier = Modifier.padding(16.dp),
                            color = when {
                                isCorrect -> Color(0xFF2E7D32)  // Verde oscuro
                                isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }

                // Botón para mostrar/ocultar solución
                Button(
                    onClick = { viewModel.toggleSolution() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(if (showSolution) "Ocultar solución" else "Ver solución")
                }

                // Solución
                if (showSolution) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Propiedad matemática si existe
                            currentQuestion.property?.let { property ->
                                Text(
                                    text = "Propiedad utilizada:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                                Text(
                                    text = property,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            // Pasos de la solución
                            currentQuestion.solutionSteps.forEach { step ->
                                Text(
                                    text = "${step.stepNumber}. ${step.description}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(top = 8.dp),
                                    fontWeight = if (step.isHighlighted) FontWeight.Bold else FontWeight.Normal
                                )

                                step.formulas.forEach { formula ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                        )
                                    ) {
                                        Text(
                                            text = formula,
                                            modifier = Modifier.padding(8.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                            }
                        }

                        currentQuestion.svgDiagram?.let { svgContent ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Representación gráfica:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            SvgDiagram(svgContent)
                        }
                    }
                }
            }

            // Navegación inferior y banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Botones de navegación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { viewModel.moveToPreviousQuestion() },
                        enabled = viewModel.canMoveToPrevious()
                    ) {
                        Text("Anterior")
                    }

                    Button(
                        onClick = { viewModel.moveToNextQuestion() },
                        enabled = viewModel.canMoveToNext()
                    ) {
                        Text("Siguiente")
                    }
                }

                // Banner de anuncios
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    val adUnitId = AdMobConstants.getBannerAdUnitId()
                    Log.d("EnpScreen", "Usando AdUnit ID: $adUnitId")

                    AdmobBanner(
                        adUnitId = adUnitId,
                        adSize = AdMobConstants.AdSizes.FULL_WIDTH
                    )
                }
            }
        }
    }
}

@Composable
fun SvgDiagram(svgContent: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                loadDataWithBaseURL(
                    null,
                    """
                    <html>
                        <body style="margin:0; padding:0; background-color: transparent; display: flex; justify-content: center; align-items: center;">
                            <div style="width: 100%; display: flex; justify-content: center;">
                                $svgContent
                            </div>
                        </body>
                    </html>
                    """.trimIndent(),
                    "text/html",
                    "UTF-8",
                    null
                )
                setBackgroundColor(Color.Transparent.toArgb())
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Aumentamos la altura para evitar recortes
            .padding(horizontal = 16.dp) // Añadimos padding horizontal para alinear con el texto
    )
}