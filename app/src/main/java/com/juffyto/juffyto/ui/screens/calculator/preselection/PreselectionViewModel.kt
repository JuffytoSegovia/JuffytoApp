package com.juffyto.juffyto.ui.screens.calculator.preselection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.ActividadExtracurricular
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.CondicionPriorizable
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionConstants
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreselectionViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = PreselectionStorage(application)
    private val _state = MutableStateFlow(PreselectionState())
    val state: StateFlow<PreselectionState> = _state.asStateFlow()

    private val _accessUnlocked = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            storage.getData.collect { data ->
                _state.value = _state.value.copy(
                    nombre = data.nombre,
                    modalidad = data.modalidad,
                    puntajeENP = data.puntajeENP,
                    clasificacionSISFOH = data.clasificacionSISFOH,
                    departamento = data.departamento,
                    lenguaOriginaria = data.lenguaOriginaria,
                    actividadesExtracurriculares = data.actividadesExtracurriculares,
                    condicionesPriorizables = data.condicionesPriorizables,
                    currentStep = data.currentStep,
                    mostrarLenguaOriginaria = data.modalidad == "EIB",
                    puntajeTotal = data.puntajeTotal, // Agregamos esto
                    desglosePuntaje = data.desglosePuntaje // Y esto
                )
                // Si estamos en el paso 3, recalculamos el puntaje
                if (data.currentStep == 3) {
                    calculateScore()
                }
                validarFormulario()
            }
        }
    }

    // Funciones de actualización de campos
    fun updateNombre(nombre: String) {
        val error = validarNombre(nombre)
        _state.value = _state.value.copy(
            nombre = nombre,
            nombreError = error
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updateModalidad(modalidad: String) {
        _state.value = _state.value.copy(
            modalidad = modalidad,
            mostrarLenguaOriginaria = modalidad == "EIB",
            modalidadError = if (modalidad.isBlank()) "Seleccione una modalidad" else null
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updatePuntajeENP(puntaje: String) {
        val error = validarPuntajeENP(puntaje)
        _state.value = _state.value.copy(
            puntajeENP = puntaje,
            puntajeENPError = error
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updateSISFOH(clasificacion: String) {
        _state.value = _state.value.copy(
            clasificacionSISFOH = clasificacion,
            sisfohError = if (clasificacion.isBlank()) "Seleccione una clasificación" else null
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updateDepartamento(departamento: String) {
        _state.value = _state.value.copy(
            departamento = departamento,
            departamentoError = if (departamento.isBlank()) "Seleccione un departamento" else null
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updateLenguaOriginaria(lengua: String) {
        _state.value = _state.value.copy(
            lenguaOriginaria = lengua,
            lenguaOriginariaError = null
        )
        validarFormulario()
        saveCurrentState()
    }

    // Funciones para actividades y condiciones
    fun toggleActividadExtracurricular(actividad: ActividadExtracurricular) {
        val currentActividades = _state.value.actividadesExtracurriculares.toMutableSet()
        if (currentActividades.contains(actividad)) {
            currentActividades.remove(actividad)
        } else {
            currentActividades.add(actividad)
        }
        _state.value = _state.value.copy(actividadesExtracurriculares = currentActividades)
        saveCurrentState()
    }

    fun limpiarActividadesExtracurriculares() {
        _state.value = _state.value.copy(actividadesExtracurriculares = emptySet())
        saveCurrentState()
    }

    fun toggleCondicionPriorizable(condicion: CondicionPriorizable) {
        val currentCondiciones = _state.value.condicionesPriorizables.toMutableSet()
        if (currentCondiciones.contains(condicion)) {
            currentCondiciones.remove(condicion)
        } else {
            currentCondiciones.add(condicion)
        }
        _state.value = _state.value.copy(condicionesPriorizables = currentCondiciones)
        saveCurrentState()
    }

    fun limpiarCondicionesPriorizables() {
        _state.value = _state.value.copy(condicionesPriorizables = emptySet())
        saveCurrentState()
    }

    // Funciones de navegación y cálculo
    fun nextStep() {
        if (_state.value.currentStep < 3) {
            _state.value = _state.value.copy(
                currentStep = _state.value.currentStep + 1
            )
            saveCurrentState()
        }
    }

    fun previousStep() {
        if (_state.value.currentStep > 1) {
            _state.value = _state.value.copy(
                currentStep = _state.value.currentStep - 1
            )
            saveCurrentState()
        }
    }

    // Funciones de validación
    private fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    private fun validarPuntajeENP(puntaje: String): String? {
        if (puntaje.contains(".") || puntaje.contains(",") || puntaje.contains("-")) {
            return "Solo se permiten números enteros positivos"
        }

        return try {
            val puntajeNum = puntaje.toIntOrNull()
            when {
                puntaje.isBlank() -> "El puntaje es requerido"
                puntajeNum == null -> "Ingrese un número válido"
                puntajeNum < 0 -> "El puntaje no puede ser negativo"
                puntajeNum > PreselectionConstants.MAX_PUNTAJE_ENP -> "El puntaje máximo es ${PreselectionConstants.MAX_PUNTAJE_ENP}"
                puntajeNum % 2 != 0 -> "El puntaje debe ser un número par"
                else -> null
            }
        } catch (_: NumberFormatException) {
            "Ingrese un número válido"
        }
    }

    private fun validarFormulario() {
        val state = _state.value

        val isValid = state.nombre.isNotBlank() &&
                state.modalidad.isNotBlank() &&
                state.puntajeENP.isNotBlank() &&
                state.clasificacionSISFOH.isNotBlank() &&
                state.departamento.isNotBlank() &&
                (!state.mostrarLenguaOriginaria || state.lenguaOriginaria.isNotBlank()) &&
                state.nombreError == null &&
                state.modalidadError == null &&
                state.puntajeENPError == null &&
                state.sisfohError == null &&
                state.departamentoError == null

        _state.value = state.copy(habilitarContinuar = isValid)
    }

    // Cálculo de puntajes
    fun calculateScore() {
        val state = _state.value

        val puntajeENP = state.puntajeENP.toIntOrNull() ?: 0
        val puntajeSISFOH = calcularPuntajeSISFOH(state.clasificacionSISFOH, state.modalidad)
        val departamento = state.departamento.split(" - ")[0]
        val puntajeQuintil = PreselectionConstants.DEPARTAMENTOS[departamento] ?: 0
        val puntajeActividades = calcularPuntajeActividades()
        val puntajePriorizable = calcularPuntajeCondiciones()
        val puntajeLengua = if (state.modalidad == "EIB") {
            calcularPuntajeLengua(state.lenguaOriginaria)
        } else 0

        val puntajeTotal = puntajeENP + puntajeSISFOH + puntajeQuintil +
                puntajeActividades + puntajePriorizable + puntajeLengua

        val desglose = buildString {
            appendLine("✅ Modalidad: ${state.modalidad}")
            appendLine("✅ ENP: $puntajeENP puntos")
            appendLine("✅ SISFOH: $puntajeSISFOH puntos")
            appendLine("✅ Tasa de transición: $puntajeQuintil puntos")
            appendLine("✅ Actividades extracurriculares: $puntajeActividades puntos")
            appendLine("✅ Condiciones priorizables: $puntajePriorizable puntos")
            if (state.modalidad == "EIB") {
                appendLine("✅ Lengua originaria: $puntajeLengua puntos")
            }
        }

        _state.value = state.copy(
            puntajeTotal = puntajeTotal,
            desglosePuntaje = desglose
        )
    }

    private fun calcularPuntajeSISFOH(clasificacion: String, modalidad: String): Int {
        return when {
            clasificacion.contains("extrema") -> 5
            clasificacion.contains("Pobreza (P)") && modalidad != "Ordinaria" -> 2
            else -> 0
        }
    }

    private fun calcularPuntajeActividades(): Int {
        var puntaje = 0
        _state.value.actividadesExtracurriculares.forEach { actividad ->
            puntaje += when (actividad) {
                ActividadExtracurricular.CONCURSO_NACIONAL,
                ActividadExtracurricular.JUEGOS_NACIONALES -> 5
                ActividadExtracurricular.CONCURSO_PARTICIPACION,
                ActividadExtracurricular.JUEGOS_PARTICIPACION -> 2
            }
        }
        return minOf(puntaje, PreselectionConstants.MAX_PUNTAJE_ACTIVIDADES)
    }

    private fun calcularPuntajeCondiciones(): Int {
        return minOf(_state.value.condicionesPriorizables.size * 5,
            PreselectionConstants.MAX_PUNTAJE_CONDICIONES)
    }

    private fun calcularPuntajeLengua(lengua: String): Int {
        return when {
            lengua.contains("primera prioridad") -> 10
            lengua.contains("segunda prioridad") -> 5
            else -> 0
        }
    }

    // Funciones de diálogo de recomendaciones
    fun toggleRecommendationsDialog() {
        _state.value = _state.value.copy(
            showRecommendations = !_state.value.showRecommendations
        )
    }

    fun unlockAccess() {
        _accessUnlocked.value = true
    }

    fun isAccessUnlocked(): Boolean {
        return _accessUnlocked.value
    }

    // Persistencia
    private fun saveCurrentState() {
        viewModelScope.launch {
            storage.saveData(state.value)
        }
    }

    fun resetCalculator() {
        viewModelScope.launch {
            storage.clearData()
            _state.value = PreselectionState()
        }
    }
}