package com.juffyto.juffyto.ui.screens.enp

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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

@Composable
private fun EnpHomeContent(
    onSimulationClick: () -> Unit,
    onSolutionsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 16.dp), // Ajustamos el padding
        verticalArrangement = Arrangement.spacedBy(24.dp) // Aumentamos el espacio entre elementos
    ) {
        // Card informativa con nuevo estilo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centramos el contenido
            ) {
                Text(
                    text = "ENP Reconstruido Beca 18 2025",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("• Área de Competencia Matemática")
                    Text("• 30 preguntas")
                    Text("• 2 puntos por respuesta correcta")
                    Text("• No hay puntos en contra")
                    Text("• Tiempo sugerido: 60 minutos")
                }
            }
        }

        // Botón Simulacro
        Button(
            onClick = onSimulationClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Primary),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Default.PlayArrow, "Iniciar simulacro")
                Text("Iniciar Simulacro")
                Text(
                    "Pon a prueba tus conocimientos",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        //Botón solucionario
        Button(
            onClick = onSolutionsClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Primary),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Default.AutoStories, "Ver solucionario")
                Text("Solucionario")
                Text(
                    "Revisa las soluciones paso a paso",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnpScreen(
    viewModel: EnpViewModel,
    onBackClick: () -> Unit
) {
    val enpState by viewModel.enpState.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val showSolution by viewModel.showSolution.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val simulationAnswers by viewModel.simulationAnswers.collectAsState()
    val currentQuestion = viewModel.questions[currentQuestionIndex]

    // Añadir aquí el BackHandler
    BackHandler {
        if (viewModel.canNavigateBack()) {
            if (enpState.mode == EnpMode.SIMULATION) {
                viewModel.showExitConfirmation()
            } else {
                viewModel.setMode(EnpMode.HOME)
            }
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (enpState.mode) {
                            EnpMode.HOME -> "ENP BECA 18 2025"
                            EnpMode.SIMULATION -> "Simulacro ENP"
                            EnpMode.SOLUTIONS -> "Solucionario ENP"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (viewModel.canNavigateBack()) {
                                if (enpState.mode == EnpMode.SIMULATION) {
                                    viewModel.showExitConfirmation()
                                } else {
                                    viewModel.setMode(EnpMode.HOME)
                                }
                            } else {
                                onBackClick()
                            }
                        }
                    ) {
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
        when (enpState.mode) {
            EnpMode.HOME -> {
                EnpHomeContent(
                    onSimulationClick = { viewModel.setMode(EnpMode.SIMULATION) },
                    onSolutionsClick = { viewModel.setMode(EnpMode.SOLUTIONS) }
                )
            }

            EnpMode.SIMULATION, EnpMode.SOLUTIONS -> {
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
                        // Barra de progreso y contador
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                progress = { viewModel.getCurrentProgress() }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Pregunta ${currentQuestionIndex + 1} de ${viewModel.questions.size}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (enpState.mode == EnpMode.SIMULATION) {
                                    Text(
                                        text = "Respondidas: ${simulationAnswers.size} de ${viewModel.questions.size}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Primary
                                    )
                                }
                            }
                        }

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
                            val isSelected = when (enpState.mode) {
                                EnpMode.SIMULATION -> simulationAnswers[currentQuestionIndex] == index
                                else -> userAnswers[currentQuestionIndex] == index
                            }
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
                                        isCorrect -> Color(0xFFE8F5E9)
                                        isSelected -> MaterialTheme.colorScheme.primaryContainer
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                            ) {
                                Text(
                                    text = "${('a' + index)}) $option",
                                    modifier = Modifier.padding(16.dp),
                                    color = when {
                                        isCorrect -> Color(0xFF2E7D32)
                                        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }

                        // Botón de solución (solo en modo SOLUTIONS)
                        if (enpState.mode == EnpMode.SOLUTIONS) {
                            Button(
                                onClick = { viewModel.toggleSolution() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(if (showSolution) "Ocultar solución" else "Ver solución")
                            }

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

                        // Botón de finalizar (solo en modo simulación)
                        if (enpState.mode == EnpMode.SIMULATION) {
                            val unansweredQuestions = viewModel.getUnansweredQuestions()
                            Button(
                                onClick = { viewModel.calculateScore() },
                                enabled = unansweredQuestions.isEmpty(),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    if (unansweredQuestions.isEmpty()) {
                                        Text("Finalizar simulacro")
                                    } else {
                                        Text("Preguntas pendientes:")
                                        Text(
                                            text = unansweredQuestions.joinToString(", ") { (it + 1).toString() },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }

                        // Banner de anuncios
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            val adUnitId = AdMobConstants.getBannerAdUnitId()
                            AdmobBanner(
                                adUnitId = adUnitId,
                                adSize = AdMobConstants.AdSizes.FULL_WIDTH
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de resultados del simulacro
    if (enpState.mode == EnpMode.SIMULATION && enpState.simulationCompleted) {
        AlertDialog(
            onDismissRequest = { /* No permitir cerrar al tocar fuera */ },
            title = { Text("Resultados del Simulacro") },
            text = {
                Column {
                    Text("Respuestas correctas: ${viewModel.getCorrectAnswers()} de 30")
                    Text("Puntaje final: ${enpState.score} de 60 puntos")
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.setMode(EnpMode.SOLUTIONS) }
                ) {
                    Text("Ver solucionario")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.resetSimulation()
                        viewModel.setMode(EnpMode.HOME)
                    }
                ) {
                    Text("Volver al inicio")
                }
            }
        )
    }

    // Nuevo diálogo de confirmación de salida
    val showExitConfirmation by viewModel.showExitConfirmation.collectAsState()

    if (showExitConfirmation && enpState.mode == EnpMode.SIMULATION) {
        AlertDialog(
            onDismissRequest = { viewModel.hideExitConfirmation() },
            title = { Text("¿Estás seguro?") },
            text = { Text("Si sales ahora perderás tu progreso en el simulacro") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.hideExitConfirmation()
                        viewModel.resetSimulation()
                        viewModel.setMode(EnpMode.HOME)
                    }
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.hideExitConfirmation() }
                ) {
                    Text("Continuar simulacro")
                }
            }
        )
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