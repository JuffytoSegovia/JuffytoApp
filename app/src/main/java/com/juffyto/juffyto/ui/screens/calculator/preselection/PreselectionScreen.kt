package com.juffyto.juffyto.ui.screens.calculator.preselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.components.ads.RewardedInterstitialAdManager
import com.juffyto.juffyto.ui.screens.calculator.preselection.components.ActivitiesSection
import com.juffyto.juffyto.ui.screens.calculator.preselection.components.PreselectionForm
import com.juffyto.juffyto.ui.screens.calculator.preselection.components.PreselectionReport
import com.juffyto.juffyto.ui.screens.calculator.preselection.components.PrioritizedSection
import com.juffyto.juffyto.ui.screens.calculator.preselection.components.RecommendationsDialog
import com.juffyto.juffyto.ui.screens.calculator.sharing.ShareUtils
import com.juffyto.juffyto.utils.AdMobConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreselectionScreen(
    onBackClick: () -> Unit,
    rewardedInterstitialAdManager: RewardedInterstitialAdManager,
    viewModel: PreselectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Manejar retroceso del sistema
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
                    if (state.currentStep == 3) {
                        val view = LocalView.current
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(0)
                                    view.parent?.let { layout ->
                                        ShareUtils.sharePreselectionReport(layout as android.view.View)
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
            // Barra de progreso
            LinearProgressIndicator(
                progress = { state.currentStep / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Contenido según el paso actual
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (state.currentStep) {
                    1 -> PreselectionForm(
                        state = state,
                        onUpdateNombre = viewModel::updateNombre,
                        onUpdateModalidad = viewModel::updateModalidad,
                        onUpdatePuntajeENP = viewModel::updatePuntajeENP,
                        onUpdateSISFOH = viewModel::updateSISFOH,
                        onUpdateDepartamento = viewModel::updateDepartamento,
                        onUpdateLenguaOriginaria = viewModel::updateLenguaOriginaria,
                        onNavigateNext = { viewModel.nextStep() },
                        onReset = { viewModel.resetCalculator() },
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                    2 -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActivitiesSection(
                            selectedActivities = state.actividadesExtracurriculares,
                            onActivityToggled = viewModel::toggleActividadExtracurricular,
                            onClearActivities = viewModel::limpiarActividadesExtracurriculares
                        )
                        PrioritizedSection(
                            selectedConditions = state.condicionesPriorizables,
                            modalidad = state.modalidad,
                            onConditionToggled = viewModel::toggleCondicionPriorizable,
                            onClearConditions = viewModel::limpiarCondicionesPriorizables
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { viewModel.previousStep() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Anterior")
                            }
                            Button(
                                onClick = {
                                    viewModel.calculateScore()
                                    viewModel.nextStep()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Calcular Puntaje")
                            }
                        }
                    }
                    3 -> PreselectionReport(
                        nombre = state.nombre,
                        puntajeTotal = state.puntajeTotal ?: 0,
                        modalidad = state.modalidad,
                        desglosePuntaje = state.desglosePuntaje,
                        onShowRecommendations = { viewModel.toggleRecommendationsDialog() },
                        onBackClick = { viewModel.previousStep() },
                        onReset = { viewModel.resetCalculator() },
                        viewModel = viewModel,
                        rewardedInterstitialAdManager = rewardedInterstitialAdManager,
                        context = context,
                        modifier = Modifier.verticalScroll(scrollState)
                    )
                }
            }

            // Banner de anuncios
            AdmobBanner(
                adUnitId = AdMobConstants.getBannerAdUnitId(),
                adSize = AdMobConstants.AdSizes.FULL_WIDTH
            )

            // Diálogo de recomendaciones
            if (state.showRecommendations) {
                RecommendationsDialog(
                    puntajeENP = state.puntajeENP.toIntOrNull() ?: 0,
                    puntajeTotal = state.puntajeTotal ?: 0,
                    onDismiss = { viewModel.toggleRecommendationsDialog() }
                )
            }
        }
    }
}