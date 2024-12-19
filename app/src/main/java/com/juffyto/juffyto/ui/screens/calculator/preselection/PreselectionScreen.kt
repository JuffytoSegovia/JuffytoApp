package com.juffyto.juffyto.ui.screens.calculator.preselection

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.ActividadExtracurricular
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.Companion.DEPARTAMENTOS
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.CondicionPriorizable
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionViewModel.PreselectionState
import com.juffyto.juffyto.ui.screens.calculator.sharing.ShareUtils
import com.juffyto.juffyto.utils.AdMobConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreselectionScreen(
    onBackClick: () -> Unit,
    viewModel: PreselectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
                        text = "Preselección",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    if (state.currentStep == 3) {
                        val view = LocalView.current

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    // Primero hacer scroll al inicio
                                    scrollState.animateScrollTo(0)
                                    // Pequeño delay para asegurar que el scroll se completó
                                    delay(300)
                                    // Luego compartir
                                    view.parent?.let { layout ->
                                        ShareUtils.shareReport(
                                            preselectionLayout = layout as View
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir reporte",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
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
                        onRestartClick = { viewModel.resetCalculator() },
                        scrollState = scrollState // Pasar el scrollState
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
            supportingText = state.nombreError?.let { { Text(it) } },
            singleLine = true
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
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
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
            onValueChange = {
                // Solo permitimos números y validamos antes de actualizar
                if (it.isEmpty() || it.matches(Regex("^\\d+$"))) { viewModel.updatePuntajeENP(it) }
            },
            label = { Text("Puntaje ENP (0-120)") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.puntajeENPError != null,
            supportingText = state.puntajeENPError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
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
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
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
        var showQuintilDialog by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedDepartamento,
                onExpandedChange = { expandedDepartamento = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.departamento.ifEmpty {
                        state.departamento
                    }, // Mostrar el valor completo incluyendo puntos
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Departamento donde culmino la secundaria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartamento)
                    },
                    modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    isError = state.departamentoError != null,
                    supportingText = state.departamentoError?.let { { Text(it) } }
                )

                ExposedDropdownMenu(
                    expanded = expandedDepartamento,
                    onDismissRequest = { expandedDepartamento = false }
                ) {
                    DEPARTAMENTOS.forEach { (departamento, puntaje) ->
                        val textoCompleto = "$departamento - $puntaje puntos"
                        DropdownMenuItem(
                            text = { Text(textoCompleto) },
                            onClick = {
                                viewModel.updateDepartamento(textoCompleto)  // Pasamos el texto completo
                                expandedDepartamento = false
                            }
                        )
                    }
                }
            }

            // Ícono de información
            IconButton(onClick = { showQuintilDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Información sobre quintiles"
                )
            }
        }

        // Diálogo de información
        if (showQuintilDialog) {
            AlertDialog(
                onDismissRequest = { showQuintilDialog = false },
                title = {
                    Text(
                        "📌Información de Tasa de Transición",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "El puntaje se otorga según el departamento donde el postulante culminó la secundaria:",
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("✅Quintil 1 (10 puntos):", fontWeight = FontWeight.Bold)
                        Text("Amazonas, Ucayali, Ayacucho, Puno, Loreto")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("✅Quintil 2 (7 puntos):", fontWeight = FontWeight.Bold)
                        Text("San Martín, Cusco, Huánuco, Apurímac, Huancavelica")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("✅Quintil 3 (5 puntos):", fontWeight = FontWeight.Bold)
                        Text("Áncash, Tacna, Madre de Dios, Moquegua, Pasco, Cajamarca")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("✅Quintil 4 (2 puntos):", fontWeight = FontWeight.Bold)
                        Text("Arequipa, Piura, Junín, Tumbes")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("✅Quintil 5 (0 puntos):", fontWeight = FontWeight.Bold)
                        Text("Ica, Lambayeque, Lima, La Libertad")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showQuintilDialog = false }) {
                        Text("Entendido")
                    }
                }
            )
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
                    modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
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

        // Agregar este botón después del botón Continuar en StepOne
        Button(
            onClick = { viewModel.resetCalculator() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Limpiar Formulario")
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
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.CONCURSO_NACIONAL),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.CONCURSO_NACIONAL) },
                    label = "(CE) Concurso Escolar Nacional (1°, 2º o 3º Puesto Etapa Nacional) o Concurso Escolar Internacional - 5 puntos"
                )

                ActividadExtracurricularCheckbox(
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.CONCURSO_PARTICIPACION),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.CONCURSO_PARTICIPACION) },
                    label = "(CEP) Participación en la Etapa Nacional de los Concursos Escolares Nacionales - 2 puntos"
                )

                ActividadExtracurricularCheckbox(
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.JUEGOS_NACIONALES),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.JUEGOS_NACIONALES) },
                    label = "(JD) Juegos Deportivos Escolares (1°, 2º o 3° puesto en Etapa Nacional) - 5 puntos"
                )

                ActividadExtracurricularCheckbox(
                    checked = state.actividadesExtracurriculares.contains(ActividadExtracurricular.JUEGOS_PARTICIPACION),
                    onCheckedChange = { viewModel.toggleActividadExtracurricular(ActividadExtracurricular.JUEGOS_PARTICIPACION) },
                    label = "(JDP) Participación en la Etapa Nacional de los Juegos Deportivos Escolares Nacionales - 2 puntos"
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
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.DISCAPACIDAD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.DISCAPACIDAD) },
                    label = "(D) Discapacidad - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.DISCAPACIDAD)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.BOMBEROS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.BOMBEROS) },
                    label = "(B) Bomberos activos e hijos de bomberos - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.BOMBEROS)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.VOLUNTARIOS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.VOLUNTARIOS) },
                    label = "(V) Voluntarios reconocidos por el MIMP - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.VOLUNTARIOS)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.COMUNIDAD_NATIVA),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.COMUNIDAD_NATIVA) },
                    label = "(IA) Pertenencia a Comunidad Campesina, Comunidad Nativa Amazónica o Población Afroperuana - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.COMUNIDAD_NATIVA)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.METALES_PESADOS),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.METALES_PESADOS) },
                    label = "(PEM) Población expuesta a metales pesados y otras sustancias químicas - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.METALES_PESADOS)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.POBLACION_BENEFICIARIA),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.POBLACION_BENEFICIARIA) },
                    label = "(PD) Población beneficiaria (Res. Suprema Nº 264-2022-JUS) - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.POBLACION_BENEFICIARIA)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.ORFANDAD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.ORFANDAD) },
                    label = "(OR) Población beneficiaria de la Ley N° 31405 (orfandad) - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.ORFANDAD)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.DESPROTECCION),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.DESPROTECCION) },
                    label = "(DF) Desprotección familiar - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.DESPROTECCION)
                )

                CondicionPriorizableCheckbox(
                    checked = state.condicionesPriorizables.contains(CondicionPriorizable.AGENTE_SALUD),
                    onCheckedChange = { viewModel.toggleCondicionPriorizable(CondicionPriorizable.AGENTE_SALUD) },
                    label = "(ACS) Agente Comunitario de Salud - 5 puntos",
                    enabled = viewModel.isCondicionEnabled(CondicionPriorizable.AGENTE_SALUD)
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
                Text("Calcular Puntaje")
            }
        }
    }
}

@Composable
private fun ActividadExtracurricularCheckbox(
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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    enabled: Boolean  // Recibimos enabled como parámetro
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            color = if (enabled)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}

@Composable
private fun StepThree(
    state: PreselectionState,
    viewModel: PreselectionViewModel,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit,
    scrollState: ScrollState // Nuevo parámetro
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con nombre - Mejorado el centrado
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
                horizontalAlignment = Alignment.CenterHorizontally  // Centra el contenido
            ) {
                Text(
                    text = "Reporte de Preselección para",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center  // Asegura el centrado
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center  // Centra el nombre
                )
            }
        }

        // Puntaje Total
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    (state.puntajeTotal ?: 0) >= 80 -> Color(0xFF4CAF50)
                    (state.puntajeTotal ?: 0) >= 70 -> Color(0xFFFFA726)
                    else -> Color(0xFFF44336)
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Tu puntaje estimado de preselección es:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${state.puntajeTotal ?: 0} puntos",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
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

                state.desglosePuntaje.split("\n").forEach { line ->
                    Text(line)
                }
            }
        }

        // Card de fórmula
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fórmula de Preselección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fórmula específica según la modalidad
                Text(
                    text = when (state.modalidad) {
                        "Ordinaria" ->
                            "PS = ENP + PE + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25"
                        "CNA y PA" ->
                            "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + PEM + PD + OR + ACS)max 25"
                        "EIB" ->
                            "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25 + LO"
                        "Protección" ->
                            "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + ACS)max 25 + DF"
                        else -> // Para FF. AA., VRAEM, Huallaga y REPARED
                            "PS = ENP + (PE o P) + T + (CE o CEP + JD o JDP)max 10 + (D + B + V + IA + PEM + PD + OR + ACS)max 25"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Puntaje máximo
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Puntaje máximo para la modalidad ${state.modalidad}:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (state.modalidad == "EIB") "180 puntos" else "170 puntos",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Card del mensaje de ánimo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when {
                            (state.puntajeTotal ?: 0) >= 100 -> Icons.Default.Star
                            (state.puntajeTotal ?: 0) >= 70 -> Icons.Default.ThumbUp
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mensaje para ti",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = when {
                        (state.puntajeTotal ?: 0) >= 100 ->
                            "¡Felicidades! Tienes grandes posibilidades de ganar la beca. " +
                                    "Mantén todos tus documentos listos para la siguiente etapa."
                        (state.puntajeTotal ?: 0) >= 70 ->
                            "¡Buen esfuerzo! Estás en buen camino para obtener la beca. " +
                                    "Asegúrate de tener toda tu documentación en orden."
                        else ->
                            "Cada punto cuenta. No te desanimes y sigue preparándote. " +
                                    "Aún puedes mejorar tu puntaje en el ENP y revisar si cumples con más condiciones priorizables."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Card de la fuente de información
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Información de la calculadora",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Esta calculadora está basada en los criterios y puntajes establecidos en:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                val uriHandler = LocalUriHandler.current
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