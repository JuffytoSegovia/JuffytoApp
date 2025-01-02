package com.juffyto.juffyto.ui.screens.calculator.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.juffyto.juffyto.ui.screens.calculator.selection.model.IES
import com.juffyto.juffyto.ui.screens.calculator.selection.model.SelectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectionViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = SelectionStorage(application)
    private val _state = MutableStateFlow(SelectionState())
    val state: StateFlow<SelectionState> = _state.asStateFlow()

    private val _iesData = MutableStateFlow<List<IES>>(emptyList())
    val iesData: StateFlow<List<IES>> = _iesData.asStateFlow()

    init {
        loadIESData()
        viewModelScope.launch {
            storage.getData.collect { data ->
                val iesSeleccionada = if (data.iesSeleccionada.isNotEmpty()) {
                    _iesData.value.find { it.nombreIES == data.iesSeleccionada }
                } else null

                // Actualizar el estado con los datos guardados
                _state.value = _state.value.copy(
                    nombre = data.nombre,
                    modalidad = data.modalidad,
                    puntajePreseleccion = data.puntajePreseleccion,
                    regionIES = data.regionIES,
                    tiposIESSeleccionados = data.tiposIES,
                    gestionesIESSeleccionadas = data.gestionesIES,
                    iesSeleccionada = iesSeleccionada,
                    currentStep = data.currentStep
                )

                if (data.regionIES.isNotEmpty()) {
                    updateAvailableLists(_iesData.value)
                    filtrarIES()
                }

                // Validar el formulario después de cargar los datos
                validarFormulario()

                // Si estamos en el paso 2, recalcular el puntaje
                if (data.currentStep == 2 && iesSeleccionada != null) {
                    calcularPuntaje()
                }
            }
        }
    }

    private fun loadIESData() {
        try {
            val jsonString = getApplication<Application>().assets
                .open("IES.json")
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<IES>>() {}.type
            val iesLista = Gson().fromJson<List<IES>>(jsonString, type)
            _iesData.value = iesLista
            updateAvailableLists(iesLista)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveCurrentState() {
        viewModelScope.launch {
            storage.saveData(
                SelectionStorage.SelectionData(
                    nombre = _state.value.nombre,
                    modalidad = _state.value.modalidad,
                    puntajePreseleccion = _state.value.puntajePreseleccion,
                    regionIES = _state.value.regionIES,
                    tiposIES = _state.value.tiposIESSeleccionados,
                    gestionesIES = _state.value.gestionesIESSeleccionadas,
                    iesSeleccionada = _state.value.iesSeleccionada?.nombreIES ?: "",
                    currentStep = _state.value.currentStep
                )
            )
        }
    }

    private fun updateAvailableLists(iesList: List<IES>) {
        _state.value = _state.value.copy(
            iesDisponibles = iesList,
            tiposIESDisponibles = iesList.map { it.tipoIES }.toSet(),
            gestionesIESDisponibles = iesList.map { it.gestionIES }.toSet()
        )
    }

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
            modalidadError = if (modalidad.isBlank()) "Seleccione una modalidad" else null
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updatePuntajePreseleccion(puntaje: String) {
        val error = validarPuntajePreseleccion(puntaje)
        _state.value = _state.value.copy(
            puntajePreseleccion = puntaje,
            puntajePreseleccionError = error
        )
        validarFormulario()
        saveCurrentState()
    }

    fun updateRegionIES(region: String) {
        _state.value = _state.value.copy(
            regionIES = region,
            regionIESError = if (region.isBlank()) "Seleccione una región" else null,
            // Limpiar selecciones al cambiar de región
            tiposIESSeleccionados = emptySet(),
            gestionesIESSeleccionadas = emptySet(),
            iesSeleccionada = null
        )
        filtrarIES()
        validarFormulario()
        saveCurrentState()
    }

    fun toggleTipoIES(tipo: String) {
        val currentTipos = _state.value.tiposIESSeleccionados.toMutableSet()
        if (currentTipos.contains(tipo)) {
            currentTipos.remove(tipo)
        } else {
            currentTipos.add(tipo)
        }
        _state.value = _state.value.copy(
            tiposIESSeleccionados = currentTipos,
            iesSeleccionada = null
        )
        filtrarIES()
        saveCurrentState()
    }

    fun toggleGestionIES(gestion: String) {
        val currentGestiones = _state.value.gestionesIESSeleccionadas.toMutableSet()
        if (currentGestiones.contains(gestion)) {
            currentGestiones.remove(gestion)
        } else {
            currentGestiones.add(gestion)
        }
        _state.value = _state.value.copy(
            gestionesIESSeleccionadas = currentGestiones,
            iesSeleccionada = null
        )
        filtrarIES()
        saveCurrentState()
    }

    fun selectIES(ies: IES) {
        _state.value = _state.value.copy(
            iesSeleccionada = ies,
            iesError = null
        )
        validarFormulario()
        saveCurrentState()
    }

    private fun filtrarIES() {
        val regionActual = _state.value.regionIES
        val iesFiltradas = if (regionActual.isEmpty()) {
            _iesData.value
        } else {
            _iesData.value.filter { ies ->
                ies.regionIES == regionActual &&
                        (_state.value.tiposIESSeleccionados.isEmpty() ||
                                _state.value.tiposIESSeleccionados.contains(ies.tipoIES)) &&
                        (_state.value.gestionesIESSeleccionadas.isEmpty() ||
                                _state.value.gestionesIESSeleccionadas.contains(ies.gestionIES))
            }
        }

        // Actualizar tipos y gestiones disponibles basado en la región
        val tiposDisponibles = iesFiltradas.map { it.tipoIES }.toSet()
        val gestionesDisponibles = iesFiltradas.map { it.gestionIES }.toSet()

        _state.value = _state.value.copy(
            iesDisponibles = iesFiltradas,
            tiposIESDisponibles = tiposDisponibles,
            gestionesIESDisponibles = gestionesDisponibles
        )
    }

    private fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    private fun validarPuntajePreseleccion(puntaje: String): String? {
        if (puntaje.contains(".") || puntaje.contains(",") || puntaje.contains("-")) {
            return "Solo se permiten números enteros positivos"
        }

        return try {
            val puntajeNum = puntaje.toIntOrNull()
            val maxPuntaje = if (_state.value.modalidad == "EIB") 180 else 170

            when {
                puntaje.isBlank() -> "El puntaje es requerido"
                puntajeNum == null -> "Ingrese un número válido"
                puntajeNum < 0 -> "El puntaje no puede ser negativo"
                puntajeNum > maxPuntaje -> "El puntaje máximo es $maxPuntaje"
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
                state.puntajePreseleccion.isNotBlank() &&
                state.regionIES.isNotBlank() &&
                state.iesSeleccionada != null &&
                state.nombreError == null &&
                state.modalidadError == null &&
                state.puntajePreseleccionError == null &&
                state.regionIESError == null

        _state.value = state.copy(habilitarCalcular = isValid)
    }

    fun calcularPuntaje() {
        val state = _state.value
        val ies = state.iesSeleccionada ?: return

        val puntajePreseleccion = state.puntajePreseleccion.toIntOrNull() ?: 0
        val puntajeRanking = ies.calcularPuntajeRanking()
        val puntajeGestion = ies.calcularPuntajeGestion()
        val puntajeSelectividad = ies.calcularPuntajeSelectividad()
        val puntajeTotal = puntajePreseleccion + puntajeRanking + puntajeGestion + puntajeSelectividad

        val desglose = buildString {
            appendLine("✅ Modalidad: ${state.modalidad}")
            appendLine("✅ PS (Puntaje de Preselección): $puntajePreseleccion")
            appendLine("✅ C (Puntaje por Ranking): $puntajeRanking")
            appendLine("✅ G (Puntaje por Gestión): $puntajeGestion")
            appendLine("✅ S (Puntaje por Selectividad): $puntajeSelectividad")
        }

        _state.value = state.copy(
            currentStep = 2,
            puntajeTotal = puntajeTotal,
            desglosePuntaje = desglose
        )
        saveCurrentState()
    }

    fun previousStep() {
        if (_state.value.currentStep > 1) {
            _state.value = _state.value.copy(currentStep = _state.value.currentStep - 1)
            saveCurrentState()
        }
    }

    fun resetCalculator() {
        viewModelScope.launch {
            storage.clearData()
            _state.value = SelectionState()
            loadIESData()
        }
    }

    fun toggleRecommendationsDialog() {
        _state.value = _state.value.copy(
            showRecommendationsDialog = !_state.value.showRecommendationsDialog
        )
    }

    fun getRecommendedIES(): List<IES> {
        val currentIES = _state.value.iesSeleccionada
        val currentPuntaje = currentIES?.calcularPuntajeTotal() ?: 0

        return _iesData.value.filter { ies ->
            ies.calcularPuntajeTotal() > currentPuntaje
        }.sortedByDescending { it.calcularPuntajeTotal() }
    }

    private val _accessUnlocked = MutableStateFlow(false)

    fun unlockAccess() {
        _accessUnlocked.value = true
    }

    fun isAccessUnlocked(): Boolean {
        return _accessUnlocked.value
    }

    companion object {
        val MODALIDADES = listOf(
            "Ordinaria",
            "CNA y PA",
            "EIB",
            "Protección",
            "FF. AA.",
            "VRAEM",
            "Huallaga",
            "REPARED"
        )
    }
}