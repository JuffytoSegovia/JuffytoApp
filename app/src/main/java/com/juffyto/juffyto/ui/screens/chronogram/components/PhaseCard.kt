package com.juffyto.juffyto.ui.screens.chronogram.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juffyto.juffyto.ui.screens.chronogram.model.Phase
import com.juffyto.juffyto.ui.theme.PhaseCardDark
import com.juffyto.juffyto.ui.theme.PhaseCardLight
import com.juffyto.juffyto.ui.theme.StatusActive
import com.juffyto.juffyto.ui.theme.StatusCompleted
import com.juffyto.juffyto.ui.theme.StatusUpcoming
import java.time.format.DateTimeFormatter

@Composable
fun PhaseCard(
    phase: Phase,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val isSystemInDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = modifier
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme) PhaseCardDark else PhaseCardLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(
            width = 2.dp,
            color = when {
                phase.isPast -> StatusCompleted
                phase.isActive -> StatusActive
                else -> StatusUpcoming
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Contenido superior (título)
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = phase.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            // Fechas (siempre en la misma posición)
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    // Caso especial para la fase de examen
                    phase.title.contains("Examen Nacional", ignoreCase = true) -> {
                        DateRow(
                            label = "Fecha de ENP:",
                            date = phase.singleDate?.format(formatter) ?: ""
                        )
                    }
                    // Para fechas únicas
                    phase.singleDate != null -> {
                        Spacer(modifier = Modifier.height(24.dp)) // Espacio para alinear con las de dos fechas
                        DateRow(
                            label = "Fecha de publicación:",
                            date = phase.singleDate.format(formatter)
                        )
                    }
                    // Para fechas de inicio y fin
                    phase.startDate != null && phase.endDate != null -> {
                        DateRow(
                            label = "Fecha de inicio:",
                            date = phase.startDate.format(formatter)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DateRow(
                            label = "Fecha de fin:",
                            date = phase.endDate.format(formatter)
                        )
                    }
                }
            }

            // Estado en la parte inferior
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Surface(
                    color = when {
                        phase.isActive -> StatusActive.copy(alpha = 0.1f)
                        phase.isPast -> StatusCompleted.copy(alpha = 0.1f)
                        else -> StatusUpcoming.copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when {
                            phase.isActive -> "En curso"
                            phase.isPast -> "Finalizado"
                            else -> "Próximo"
                        },
                        color = when {
                            phase.isActive -> StatusActive
                            phase.isPast -> StatusCompleted
                            else -> StatusUpcoming
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateRow(
    label: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = date,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}