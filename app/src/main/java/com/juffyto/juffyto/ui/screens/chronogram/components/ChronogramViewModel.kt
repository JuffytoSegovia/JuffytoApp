package com.juffyto.juffyto.ui.screens.chronogram.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.juffyto.juffyto.ui.screens.chronogram.model.Phase
import com.juffyto.juffyto.ui.screens.chronogram.model.Stage
import com.juffyto.juffyto.utils.DateUtils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow

class ChronogramViewModel(application: Application) : AndroidViewModel(application) {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Cambiamos a un simple StateFlow sin persistencia
    private val _testModeEnabled = MutableStateFlow(false)
    val testModeEnabled: StateFlow<Boolean> = _testModeEnabled

    fun setTestModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _testModeEnabled.value = enabled
            if (!enabled) {
                DateUtils.resetToRealTime()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        DateUtils.resetToRealTime()
        _testModeEnabled.value = false
    }

    val stages = listOf(
        Stage(
            title = "ETAPA DE PRESELECCIÓN",
            phases = listOf(
                Phase(
                    title = "Fase de Postulación para la Preselección",
                    startDate = LocalDate.parse("13/09/2024", formatter),
                    endDate = LocalDate.parse("18/10/2024", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Fase de Validación de la Postulación",
                    startDate = LocalDate.parse("20/09/2024", formatter),
                    endDate = LocalDate.parse("11/11/2024", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de los resultados de la Fase de Validación de la Postulación para la Preselección",
                    startDate = null,
                    endDate = null,
                    singleDate = LocalDate.parse("15/11/2024", formatter)
                ),
                Phase(
                    title = "Fase de Aplicación del Examen Nacional de Preselección",
                    startDate = null,
                    endDate = null,
                    singleDate = LocalDate.parse("01/12/2024", formatter)
                ),
                Phase(
                    title = "Fase de Preselección",
                    startDate = LocalDate.parse("26/12/2024", formatter),
                    endDate = LocalDate.parse("07/01/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de Preseleccionados",
                    startDate = null,
                    endDate = null,
                    singleDate = LocalDate.parse("08/01/2025", formatter)
                )
            )
        ),
        Stage(
            title = "ETAPA DE SELECCIÓN: PRIMER MOMENTO",
            phases = listOf(
                Phase(
                    title = "Fase de Postulación para la Selección",
                    startDate = LocalDate.parse("09/01/2025", formatter),
                    endDate = LocalDate.parse("03/03/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Subsanación de expedientes",
                    startDate = LocalDate.parse("21/02/2025", formatter),
                    endDate = LocalDate.parse("10/03/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Validación de expedientes",
                    startDate = LocalDate.parse("21/02/2025", formatter),
                    endDate = LocalDate.parse("17/03/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Fase de Asignación de puntajes y Selección",
                    startDate = LocalDate.parse("18/03/2025", formatter),
                    endDate = LocalDate.parse("21/03/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de Seleccionados",
                    startDate = null,
                    endDate = null,
                    singleDate = LocalDate.parse("24/03/2025", formatter)
                ),
                Phase(
                    title = "Fase de Aceptación de la Beca",
                    startDate = LocalDate.parse("25/03/2025", formatter),
                    endDate = LocalDate.parse("02/04/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de Lista de Becarios",
                    startDate = LocalDate.parse("28/03/2025", formatter),
                    endDate = LocalDate.parse("28/04/2025", formatter),  // Un mes después
                    singleDate = null
                )
            )
        ),
        Stage(
            title = "ETAPA DE SELECCIÓN: SEGUNDO MOMENTO",
            phases = listOf(
                Phase(
                    title = "Fase de Postulación para la Selección",
                    startDate = LocalDate.parse("07/04/2025", formatter),
                    endDate = LocalDate.parse("07/05/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Subsanación de expedientes",
                    startDate = LocalDate.parse("30/04/2025", formatter),
                    endDate = LocalDate.parse("14/05/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Validación de expedientes",
                    startDate = LocalDate.parse("30/04/2025", formatter),
                    endDate = LocalDate.parse("21/05/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Fase de Asignación de puntajes y Selección",
                    startDate = LocalDate.parse("22/05/2025", formatter),
                    endDate = LocalDate.parse("27/05/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de Seleccionados",
                    startDate = null,
                    endDate = null,
                    singleDate = LocalDate.parse("28/05/2025", formatter)
                ),
                Phase(
                    title = "Fase de Aceptación de la Beca",
                    startDate = LocalDate.parse("29/05/2025", formatter),
                    endDate = LocalDate.parse("05/06/2025", formatter),
                    singleDate = null
                ),
                Phase(
                    title = "Publicación de Lista de Becarios",
                    startDate = LocalDate.parse("30/05/2025", formatter),
                    endDate = LocalDate.parse("30/06/2025", formatter),  // Un mes después
                    singleDate = null
                )
            )
        )
    )

    // Lista plana de todas las fases para fácil acceso
    val allPhases = stages.flatMap { it.phases }

    // Función para obtener la etapa actual
    fun getCurrentStage(): Stage? {
        return stages.find { stage ->
            stage.phases.any { phase -> phase.isActive }
        } ?: stages.find { stage ->
            stage.phases.any { phase -> !phase.isPast && !phase.isActive }
        }
    }

}