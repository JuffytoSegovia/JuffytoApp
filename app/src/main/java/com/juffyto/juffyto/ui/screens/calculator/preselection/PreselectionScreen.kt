package com.juffyto.juffyto.ui.screens.calculator.preselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.ActividadExtracurricular
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.CondicionPriorizable
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.PreselectionState
import com.juffyto.juffyto.utils.AdMobConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreselectionScreen(
    onBackClick: () -> Unit,
    viewModel: PreselectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    BackHandler {
        if (state.currentStep > 1) {
            viewModel.previousStep()
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Calculadora de Preselección",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de progreso
            LinearProgressIndicator(
                progress = { state.currentStep / 3f },  // Cambiado a lambda
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Contenido según el paso actual
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (state.currentStep) {
                    1 -> StepOne(
                        state = state,
                        viewModel = viewModel,
                        onNextClick = { viewModel.nextStep() }
                    )
                    2 -> StepTwo(
                        state = state,
                        viewModel = viewModel,
                        onBackClick = { viewModel.previousStep() },
                        onNextClick = {
                            viewModel.calculateScore()
                            viewModel.nextStep()
                        }
                    )
                    3 -> StepThree(
                        state = state,
                        viewModel = viewModel,
                        onBackClick = { viewModel.previousStep() },
                        onRestartClick = { viewModel.resetCalculator() }
                    )
                }
            }

            // Banner de anuncios
            AdmobBanner(
                adUnitId = AdMobConstants.getBannerAdUnitId(),
                adSize = AdMobConstants.AdSizes.FULL_WIDTH
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepOne(
    state: PreselectionState,
    viewModel: PreselectionViewModel,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo Nombre
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.updateNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nombreError != null,
            supportingText = state.nombreError?.let { { Text(it) } }
        )

        // Selector de Modalidad
        var expandedModalidad by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedModalidad,
            onExpandedChange = { expandedModalidad = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.modalidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Modalidad") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedModalidad)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                isError = state.modalidadError != null,
                supportingText = state.modalidadError?.let { { Text(it) } }
            )
            ExposedDropdownMenu(
                expanded = expandedModalidad,
                onDismissRequest = { expandedModalidad = false }
            ) {
                PreselectionViewModel.MODALIDADES.forEach { modalidad ->
                    DropdownMenuItem(
                        text = { Text(modalidad) },
                        onClick = {
                            viewModel.updateModalidad(modalidad)
                            expandedModalidad = false
                        }
                    )
                }
            }
        }

        // Campo Puntaje ENP
        OutlinedTextField(
            value = state.puntajeENP,
            onValueChange = { viewModel.updatePuntajeENP(it) },
            label = { Text("Puntaje ENP (0-120)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = state.puntajeENPError != null,
            supportingText = state.puntajeENPError?.let { { Text(it) } }
        )

        // Selector SISFOH
        var expandedSisfoh by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedSisfoh,
            onExpandedChange = { expandedSisfoh = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.clasificacionSISFOH,
                onValueChange = {},
                readOnly = true,
                label = { Text("Clasificación SISFOH") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSisfoh)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                isError = state.sisfohError != null,
                supportingText = state.sisfohError?.let { { Text(it) } }
            )
            ExposedDropdownMenu(
                expanded = expandedSisfoh,
                onDismissRequest = { expandedSisfoh = false }
            ) {
                val opciones = if (state.modalidad == "Ordinaria") {
                    PreselectionViewModel.SISFOH_ORDINARIA
                } else {
                    PreselectionViewModel.SISFOH_OTRAS
                }
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            viewModel.updateSISFOH(opcion)
                            expandedSisfoh = false
                        }
                    )
                }
            }
        }

        // Selector de Departamento
        var expandedDepartamento by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedDepartamento,
            onExpandedChange = { expandedDepartamento = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.departamento,
                onValueChange = {},
                readOnly = true,
                label = { Text("Departamento") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartamento)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                isError = state.departamentoError != null,
                supportingText = state.departamentoError?.let { { Text(it) } }
            )
            ExposedDropdownMenu(
                expanded = expandedDepartamento,
                onDismissRequest = { expandedDepartamento = false }
            ) {
                PreselectionViewModel.DEPARTAMENTOS.keys.forEach { departamento ->
                    DropdownMenuItem(
                        text = { Text(departamento) },
                        onClick = {
                            viewModel.updateDepartamento(departamento)
                            expandedDepartamento = false
                        }
                    )
                }
            }
        }

        // Campo de Lengua Originaria (solo para EIB)
        if (state.mostrarLenguaOriginaria) {
            var expandedLengua by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedLengua,
                onExpandedChange = { expandedLengua = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.lenguaOriginaria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Lengua Originaria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLengua)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    isError = state.lenguaOriginariaError != null,
                    supportingText = state.lenguaOriginariaError?.let { { Text(it) } }
                )
                ExposedDropdownMenu(
                    expanded = expandedLengua,
                    onDismissRequest = { expandedLengua = false }
                ) {
                    PreselectionViewModel.LENGUAS_ORIGINARIAS.forEach { lengua ->
                        DropdownMenuItem(
                            text = { Text(lengua) },
                            onClick = {
                                viewModel.updateLenguaOriginaria(lengua)
                                expandedLengua = false
                            }
                        )
                    }
                }
            }
        }

        // Botón Continuar
        Button(
            onClick = {
                if (viewModel.validarPasoActual()) {
                    onNextClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = state.habilitarContinuar
        ) {
            Text("Continuar")
        }
    }
}

@Composable
private fun StepTwo(
    state: PreselectionState,
    viewModel: PreselectionViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de Actividades Extracurriculares
        Text(
            text = "Actividades Extracurriculares",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Máximo 10 puntos en total",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Checkboxes para actividades extracurriculares
                ActividadExtracurricularCheckbox(
                    actividad = ActividadExtracurricular.CONCURSO_NACIONAL,
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.CONCURSO_NACIONAL),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.CONCURSO_NACIONAL) },
                    label = "Concurso Escolar Nacional (1°, 2º o 3º Puesto) - 5 puntos"
                )
                ActividadExtracurricularCheckbox(
                    actividad = ActividadExtracurricular.CONCURSO_PARTICIPACION,
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.CONCURSO_PARTICIPACION),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.CONCURSO_PARTICIPACION) },
                    label = "Participación en Concurso Nacional - 2 puntos"
                )
                ActividadExtracurricularCheckbox(
                    actividad = ActividadExtracurricular.JUEGOS_NACIONALES,
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.JUEGOS_NACIONALES),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.JUEGOS_NACIONALES) },
                    label = "Juegos Deportivos Escolares (1°, 2º o 3° puesto) - 5 puntos"
                )
                ActividadExtracurricularCheckbox(
                    actividad = ActividadExtracurricular.JUEGOS_PARTICIPACION,
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.JUEGOS_PARTICIPACION),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.JUEGOS_PARTICIPACION) },
                    label = "Participación en Juegos Deportivos - 2 puntos"
                )
            }
        }

        // Sección de Condiciones Priorizables
        Text(
            text = "Condiciones Priorizables",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Máximo 25 puntos en total",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Checkboxes para condiciones priorizables
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.DISCAPACIDAD,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.DISCAPACIDAD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.DISCAPACIDAD) },
                    label = "Discapacidad - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.BOMBEROS,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.BOMBEROS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.BOMBEROS) },
                    label = "Bomberos activos e hijos de bomberos - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.VOLUNTARIOS,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.VOLUNTARIOS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.VOLUNTARIOS) },
                    label = "Voluntarios reconocidos por el MIMP - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.COMUNIDAD_NATIVA,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.COMUNIDAD_NATIVA),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.COMUNIDAD_NATIVA) },
                    label = "Pertenencia a Comunidad Nativa Amazónica o Población Afroperuana - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.METALES_PESADOS,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.METALES_PESADOS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.METALES_PESADOS) },
                    label = "Población expuesta a metales pesados y otras sustancias químicas - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.POBLACION_BENEFICIARIA,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.POBLACION_BENEFICIARIA),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.POBLACION_BENEFICIARIA) },
                    label = "Población beneficiaria (Res. Suprema Nº 264-2022-JUS) - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.ORFANDAD,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.ORFANDAD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.ORFANDAD) },
                    label = "Población beneficiaria de la Ley N° 31405 (orfandad) - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.DESPROTECCION,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.DESPROTECCION),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.DESPROTECCION) },
                    label = "Desprotección familiar - 5 puntos"
                )
                CondicionPriorizableCheckbox(
                    condicion = CondicionPriorizable.AGENTE_SALUD,
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.AGENTE_SALUD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.AGENTE_SALUD) },
                    label = "Agente Comunitario de Salud - 5 puntos"
                )
            }
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
                onClick = onNextClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
private fun ActividadExtracurricularCheckbox(
    actividad: ActividadExtracurricular,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun CondicionPriorizableCheckbox(
    condicion: CondicionPriorizable,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun StepThree(
    state: PreselectionState,
    viewModel: PreselectionViewModel,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con nombre
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Reporte de Preselección",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = state.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Puntaje Total
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    (state.puntajeTotal ?: 0) >= 100 -> Color(0xFF4CAF50)
                    (state.puntajeTotal ?: 0) >= 70 -> Color(0xFFFFA726)
                    else -> Color(0xFFF44336)
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu puntaje estimado de preselección es:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "${state.puntajeTotal ?: 0} puntos",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Desglose de puntaje
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Desglose del puntaje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = viewModel.obtenerDesglosePuntaje(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Mensaje de recomendación
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Recomendación",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when {
                        (state.puntajeTotal ?: 0) >= 100 ->
                            "¡Felicidades! Tienes altas probabilidades de ser preseleccionado. Mantén tu documentación lista para la siguiente fase."
                        (state.puntajeTotal ?: 0) >= 70 ->
                            "Tienes posibilidades de ser preseleccionado. Asegúrate de tener todos tus documentos en orden."
                        else ->
                            "Tu puntaje está por debajo del promedio. Considera mejorar tu puntaje en el ENP y verificar si cumples con alguna condición priorizable adicional."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Botones de acción
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
                onClick = {
                    viewModel.resetCalculator()
                    onRestartClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reiniciar")
            }
        }
    }
}