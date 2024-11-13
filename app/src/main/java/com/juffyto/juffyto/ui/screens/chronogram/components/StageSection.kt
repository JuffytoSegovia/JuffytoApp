package com.juffyto.juffyto.ui.screens.chronogram.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juffyto.juffyto.ui.screens.chronogram.model.Stage
import com.juffyto.juffyto.ui.theme.CardDark
import com.juffyto.juffyto.ui.theme.CardLight
import com.juffyto.juffyto.ui.theme.Primary

@Composable
fun StageSection(
    stage: Stage,
    modifier: Modifier = Modifier
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme) CardDark else CardLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = if (isSystemInDarkTheme) {
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
                    .horizontalScroll(rememberScrollState()),
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