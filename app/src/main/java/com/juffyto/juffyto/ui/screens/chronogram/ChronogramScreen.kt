package com.juffyto.juffyto.ui.screens.chronogram

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.InterstitialAdManager
import com.juffyto.juffyto.ui.screens.chronogram.components.ChronogramViewModel
import com.juffyto.juffyto.ui.screens.chronogram.components.StageSection
import com.juffyto.juffyto.ui.screens.chronogram.components.TimerContent
import com.juffyto.juffyto.ui.screens.settings.SettingsDialog
import com.juffyto.juffyto.ui.screens.settings.SettingsViewModel
import com.juffyto.juffyto.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.utils.AdMobConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronogramScreen(
    viewModel: ChronogramViewModel,
    settingsViewModel: SettingsViewModel = viewModel(),
    interstitialAdManager: InterstitialAdManager,
    onBackClick: (Boolean) -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }
    val settings by settingsViewModel.settings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(1) }
    val tabs = listOf("Cronograma", "Contador")
    val context = LocalContext.current

    // Manejar el botón de atrás del dispositivo
    BackHandler {
        onBackClick(true)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Cambiar a CenterAlignedTopAppBar
                title = {
                    Text(
                        text = "Cronograma Beca 18",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick(true)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Configuración",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // Usar los colores específicos para CenterAlignedTopAppBar
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs mejorados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Button(
                        onClick = { selectedTab = index },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index) Primary else Color.LightGray.copy(alpha = 0.2f),
                            contentColor = if (selectedTab == index) Color.White else Color.DarkGray
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = if (selectedTab == index) 4.dp else 0.dp
                        )
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            // Contenido según la tab seleccionada
            when (selectedTab) {
                0 -> ChronogramContent(viewModel = viewModel)
                1 -> TimerContent(phases = viewModel.allPhases)
            }
        }
    }

    if (showSettings) {
        SettingsDialog(
            settings = settings,
            onDismiss = { showSettings = false },
            onSaveSettings = { newSettings ->
                settingsViewModel.updateSettings(newSettings)
                (context as? Activity)?.let { activity ->
                    interstitialAdManager.showAd(activity) {
                        showSettings = false
                    }
                } ?: run {
                    showSettings = false
                }
            }
        )
    }
}

@Composable
private fun ChronogramContent(viewModel: ChronogramViewModel) {
    val currentStage = viewModel.getCurrentStage()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll inicial a la etapa actual
    LaunchedEffect(Unit) {
        currentStage?.let { stage ->
            val index = viewModel.stages.indexOf(stage)
            if (index >= 0) {
                coroutineScope.launch {
                    delay(100)
                    listState.animateScrollToItem(
                        index = index,
                        scrollOffset = 0
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Contenido del cronograma en un Box con weight para que ocupe el espacio disponible
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = listState
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(
                    count = viewModel.stages.size,
                    key = { index -> viewModel.stages[index].title }
                ) { index ->
                    StageSection(
                        stage = viewModel.stages[index],
                        isCurrentStage = viewModel.stages[index] == currentStage
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }

        // Banner al final
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            val adUnitId = AdMobConstants.getBannerAdUnitId()
            // Log para verificar qué ID se está usando
            Log.d("MenuScreen", "Usando AdUnit ID: $adUnitId")

            AdmobBanner(
                adUnitId = adUnitId,
                adSize = AdMobConstants.AdSizes.FULL_WIDTH
            )
        }
    }
}