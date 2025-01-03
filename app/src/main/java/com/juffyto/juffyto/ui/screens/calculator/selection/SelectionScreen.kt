package com.juffyto.juffyto.ui.screens.calculator.selection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.components.ads.RewardedInterstitialAdManager
import com.juffyto.juffyto.ui.screens.calculator.selection.components.*
import com.juffyto.juffyto.ui.screens.calculator.selection.model.SelectionState
import com.juffyto.juffyto.ui.screens.calculator.sharing.ShareUtils
import com.juffyto.juffyto.utils.AdMobConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionScreen(
    onBackClick: () -> Unit,
    rewardedInterstitialAdManager: RewardedInterstitialAdManager,
    viewModel: SelectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
                        text = "Selección",
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
                actions = {
                    if (state.currentStep == 2) {
                        val view = LocalView.current
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(0)
                                    view.parent?.let { layout ->
                                        ShareUtils.shareSelectionReport(layout as android.view.View)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir reporte",
                                tint = Color.White
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
            LinearProgressIndicator(
                progress = { state.currentStep / 2f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (state.currentStep) {
                    1 -> SelectionForm(
                        state = state,
                        viewModel = viewModel
                    )
                    2 -> SelectionReport(
                        nombre = state.nombre,
                        puntajeTotal = state.puntajeTotal ?: 0,
                        desglosePuntaje = state.desglosePuntaje,
                        ies = state.iesSeleccionada!!,
                        modalidad = state.modalidad,
                        onShowRecommendations = { viewModel.toggleRecommendationsDialog() },
                        onBackClick = { viewModel.previousStep() },
                        viewModel = viewModel,
                        rewardedInterstitialAdManager = rewardedInterstitialAdManager,
                        context = context,
                        modifier = Modifier.verticalScroll(scrollState)
                    )
                }
            }

            AdmobBanner(
                adUnitId = AdMobConstants.getBannerAdUnitId(),
                adSize = AdMobConstants.AdSizes.FULL_WIDTH
            )

            if (state.showRecommendationsDialog) {
                RecommendationsDialog(
                    currentPuntaje = state.puntajeTotal ?: 0,
                    currentRegion = state.regionIES,
                    recommendedIES = viewModel.getRecommendedIES(),
                    onDismiss = { viewModel.toggleRecommendationsDialog() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionForm(
    state: SelectionState,
    viewModel: SelectionViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.updateNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nombreError != null,
            supportingText = state.nombreError?.let { { Text(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        var expandedModalidad by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedModalidad,
            onExpandedChange = { expandedModalidad = it }
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
                SelectionViewModel.MODALIDADES.forEach { modalidad ->
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

        OutlinedTextField(
            value = state.puntajePreseleccion,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    viewModel.updatePuntajePreseleccion(it)
                }
            },
            label = {
                Text(
                    "Puntaje de Preselección (0-${if (state.modalidad == "EIB") "180" else "170"})"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = state.puntajePreseleccionError != null,
            supportingText = state.puntajePreseleccionError?.let { { Text(it) } },
            singleLine = true
        )

        var expandedRegion by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedRegion,
            onExpandedChange = { expandedRegion = it }
        ) {
            OutlinedTextField(
                value = state.regionIES,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región IES donde estudiaras") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion)
                },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                isError = state.regionIESError != null,
                supportingText = state.regionIESError?.let { { Text(it) } }
            )

            ExposedDropdownMenu(
                expanded = expandedRegion,
                onDismissRequest = { expandedRegion = false }
            ) {
                viewModel.iesData.collectAsState().value
                    .map { it.regionIES }
                    .distinct()
                    .sorted()
                    .forEach { region ->
                        DropdownMenuItem(
                            text = { Text(region) },
                            onClick = {
                                viewModel.updateRegionIES(region)
                                expandedRegion = false
                            }
                        )
                    }
            }
        }

        if (state.regionIES.isNotEmpty()) {
            if (state.tiposIESDisponibles.isNotEmpty()) {
                IESFilters(
                    tiposDisponibles = state.tiposIESDisponibles,
                    gestionesDisponibles = state.gestionesIESDisponibles,
                    tiposSeleccionados = state.tiposIESSeleccionados,
                    gestionesSeleccionadas = state.gestionesIESSeleccionadas,
                    onTipoSelected = { viewModel.toggleTipoIES(it) },
                    onGestionSelected = { viewModel.toggleGestionIES(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                var expandedIES by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedIES,
                    onExpandedChange = { expandedIES = it }
                ) {
                    OutlinedTextField(
                        value = state.iesSeleccionada?.nombreIES ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Institución de Educación Superior") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedIES)
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                        isError = state.iesError != null,
                        supportingText = state.iesError?.let { { Text(it) } }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedIES,
                        onDismissRequest = { expandedIES = false }
                    ) {
                        state.iesDisponibles.forEach { ies ->
                            DropdownMenuItem(
                                text = { Text(ies.nombreIES) },
                                onClick = {
                                    viewModel.selectIES(ies)
                                    expandedIES = false
                                }
                            )
                        }
                    }
                }

                state.iesSeleccionada?.let { ies ->
                    Spacer(modifier = Modifier.height(16.dp))
                    IESDetails(ies = ies)
                }
            }
        }

        Button(
            onClick = { viewModel.calcularPuntaje() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = state.habilitarCalcular
        ) {
            Text("Calcular Puntaje")
        }

        OutlinedButton(
            onClick = { viewModel.resetCalculator() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar Formulario")
        }
    }
}