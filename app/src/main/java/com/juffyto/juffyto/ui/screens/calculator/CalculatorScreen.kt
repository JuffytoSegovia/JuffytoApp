package com.juffyto.juffyto.ui.screens.calculator

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juffyto.juffyto.ui.components.ads.AdmobBanner
import com.juffyto.juffyto.ui.screens.calculator.preselection.PreselectionScreen
import com.juffyto.juffyto.utils.AdMobConstants

@Composable
fun CalculatorScreen(
    onBackClick: () -> Unit,
    viewModel: CalculatorViewModel = viewModel()
) {
    val currentScreen = viewModel.currentScreen.collectAsState().value

    when (currentScreen) {
        CalculatorScreen.MAIN -> CalculatorMainScreen(
            onNavigateToPreselection = { viewModel.showPreselection() },
            onBackClick = onBackClick
        )
        CalculatorScreen.PRESELECTION -> PreselectionScreen(
            onBackClick = { viewModel.showMain() }
        )
        CalculatorScreen.SELECTION -> {
            // Por ahora solo mostraremos un mensaje
            Toast.makeText(LocalContext.current, "Próximamente", Toast.LENGTH_SHORT).show()
            viewModel.showMain()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorMainScreen(
    onNavigateToPreselection: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    BackHandler { onBackClick() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Calculadora Beca 18",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPreselection() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Calculadora de Preselección",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Preselección",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Calcula tu puntaje de preselección para Beca 18 2025",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast.makeText(context, "Disponible próximamente", Toast.LENGTH_SHORT).show()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Calculadora de Selección",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary  // Removido el alpha para que se vea igual
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Selección",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Calcula tu puntaje de selección para Beca 18 2025",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Espacio flexible para empujar el banner al final
            Spacer(modifier = Modifier.weight(1f))

            //Banner al final
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                val adUnitId = AdMobConstants.getBannerAdUnitId()
                AdmobBanner(
                    adUnitId = adUnitId,
                    adSize = AdMobConstants.AdSizes.SMALL_BANNER
                )
            }

            //Banner al final
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                val adUnitId = AdMobConstants.getBannerAdUnitId()
                AdmobBanner(
                    adUnitId = adUnitId,
                    adSize = AdMobConstants.AdSizes.LARGE_BANNER
                )
            }

            //Banner al final
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                val adUnitId = AdMobConstants.getBannerAdUnitId()
                AdmobBanner(
                    adUnitId = adUnitId,
                    adSize = AdMobConstants.AdSizes.MEDIUM_BOX
                )
            }
        }
    }
}