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
import com.juffyto.juffyto.ui.screens.chronogram.components.ChronogramViewModel
import com.juffyto.juffyto.ui.screens.chronogram.components.StageSection
import com.juffyto.juffyto.ui.screens.chronogram.components.TimerContent
import com.juffyto.juffyto.ui.screens.settings.SettingsDialog
import com.juffyto.juffyto.ui.screens.settings.SettingsViewModel
import com.juffyto.juffyto.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronogramScreen(
    viewModel: ChronogramViewModel, // ChronogramViewModel existente
    settingsViewModel: SettingsViewModel = viewModel(), // Añadir este ViewModel
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }
    val settings by settingsViewModel.settings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(1) }  // Cambiado a 1 para que empiece en Contador
    val tabs = listOf("Cronograma", "Contador")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cronograma Beca 18",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSettings = true }) { // Modificar esta acción
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Configuración",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                showSettings = false
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
                // Usar coroutineScope para garantizar que el scroll se realice
                coroutineScope.launch {
                    // Añadir delay para asegurar que la UI esté lista
                    delay(100)
                    listState.animateScrollToItem(
                        index = index,
                        // Scroll al inicio de la etapa
                        scrollOffset = 0
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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