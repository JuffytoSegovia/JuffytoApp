package com.juffyto.juffyto.ui.screens.chronogram.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juffyto.juffyto.ui.screens.chronogram.model.Stage
import com.juffyto.juffyto.ui.theme.CardDark
import com.juffyto.juffyto.ui.theme.CardLight
import com.juffyto.juffyto.ui.theme.Primary
import kotlinx.coroutines.delay
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing

@Composable
fun StageSection(
    stage: Stage,
    isCurrentStage: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    // Encontrar la fase actual o próxima
    val activeOrNextPhaseIndex = remember(stage.phases) {
        if (isCurrentStage) {
            stage.phases.indexOfFirst { phase ->
                phase.isActive || (!phase.isPast && !phase.isActive)
            }
        } else -1
    }

    // Scroll horizontal mejorado con mejor centrado
    LaunchedEffect(activeOrNextPhaseIndex, isCurrentStage) {
        if (activeOrNextPhaseIndex >= 0 && isCurrentStage) {
            delay(300)

            val screenWidth = configuration.screenWidthDp.dp
            val cardWidth = 280.dp
            val spacing = 12.dp
            val sidePadding = 16.dp

            with(density) {
                val availableWidth = (screenWidth - (sidePadding * 2)).toPx()
                val cardWidthPx = cardWidth.toPx()
                val spacingPx = spacing.toPx()

                // Nuevo cálculo ajustado para mejor centrado
                val totalCardWidth = cardWidthPx + spacingPx
                val targetPosition = (activeOrNextPhaseIndex * totalCardWidth) -
                        ((availableWidth - cardWidthPx) / 10f) // Ajustado el divisor a 3.2f

                val scrollPosition = targetPosition.coerceIn(0f, scrollState.maxValue.toFloat())

                scrollState.animateScrollTo(
                    value = scrollPosition.toInt(),
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isCurrentStage) {
                    Modifier.border(
                        width = 2.dp,
                        color = Primary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) CardDark else CardLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentStage) 4.dp else 2.dp
        ),
        border = if (isSystemInDarkTheme()) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        } else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stage.title,
                color = Primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.width(4.dp))

                stage.phases.forEach { phase ->
                    PhaseCard(
                        phase = phase,
                        modifier = Modifier.width(280.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}