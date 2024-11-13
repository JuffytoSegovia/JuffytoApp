package com.juffyto.juffyto.ui.screens.chronogram

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.juffyto.juffyto.ui.screens.chronogram.components.StageSection
import com.juffyto.juffyto.ui.screens.chronogram.components.TimerContent
import com.juffyto.juffyto.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronogramScreen(
    viewModel: ChronogramViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
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
                    IconButton(onClick = onSettingsClick) {
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
}

@Composable
private fun ChronogramContent(viewModel: ChronogramViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        viewModel.stages.forEach { stage ->
            StageSection(stage = stage)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}