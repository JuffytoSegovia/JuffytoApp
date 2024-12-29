package com.juffyto.juffyto.ui.screens.calculator.preselection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreselectionViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = PreselectionStorage(application)
    private val _state = MutableStateFlow(PreselectionState())
    val state: StateFlow<PreselectionState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            storage.getData.collect { data ->
                _state.value = _state.value.copy(
                    nombre = data.nombre,
                    modalidad = data.modalidad,
                    puntajeENP = data.puntajeENP,
                    clasificacionSISFOH = data.sisfoh,
                    departamento = data.departamento,
                    lenguaOriginaria = data.lenguaOriginaria,
                    actividadesExtracurriculares = data.actividades.mapNotNull {
                        try { ActividadExtracurricular.valueOf(it) } catch (_: Exception) { null }
                    }.toSet(),
                    condicionesPriorizables = data.condiciones.mapNotNull {
                        try { CondicionPriorizable.valueOf(it) } catch (_: Exception) { null }
                    }.toSet(),
                    mostrarLenguaOriginaria = data.modalidad == "EIB"
                )
                validarPasoActual()
            }
        }
    }

    data class PreselectionState(
        // Paso 1: Datos básicos
        val nombre: String = "",
        val modalidad: String = "",
        val puntajeENP: String = "",
        val clasificacionSISFOH: String = "",
        val departamento: String = "",
        val lenguaOriginaria: String = "",

        // Errores de validación
        val nombreError: String? = null,
        val modalidadError: String? = null,
        val puntajeENPError: String? = null,
        val sisfohError: String? = null,
        val departamentoError: String? = null,
        val lenguaOriginariaError: String? = null,

        // Estados de UI
        val mostrarLenguaOriginaria: Boolean = false,
        val habilitarContinuar: Boolean = false,

        // Paso 2: Actividades y Condiciones
        val actividadesExtracurriculares: Set<ActividadExtracurricular> = emptySet(),
        val condicionesPriorizables: Set<CondicionPriorizable> = emptySet(),

        // Control de navegación y resultados
        val currentStep: Int = 1,
        val puntajeTotal: Int? = null,
        val showResults: Boolean = false,
        val error: String? = null,
        val desglosePuntaje: String = ""
    )

    enum class ActividadExtracurricular {
        CONCURSO_NACIONAL,           // 5 puntos
        CONCURSO_PARTICIPACION,      // 2 puntos
        JUEGOS_NACIONALES,          // 5 puntos
        JUEGOS_PARTICIPACION        // 2 puntos
    }

    enum class CondicionPriorizable {
        DISCAPACIDAD,               // 5 puntos
        BOMBEROS,                   // 5 puntos
        VOLUNTARIOS,                // 5 puntos
        COMUNIDAD_NATIVA,           // 5 puntos
        METALES_PESADOS,            // 5 puntos
        POBLACION_BENEFICIARIA,     // 5 puntos
        ORFANDAD,                   // 5 puntos
        DESPROTECCION,              // 5 puntos
        AGENTE_SALUD                // 5 puntos
    }

    companion object {
        // Modalidades disponibles
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

        // Opciones SISFOH
        val SISFOH_ORDINARIA = listOf(
            "Pobreza extrema (PE) - 5 puntos",
            "Pobreza (P) - 0 puntos"
        )

        val SISFOH_OTRAS = listOf(
            "Pobreza extrema (PE) - 5 puntos",
            "Pobreza (P) - 2 puntos",
            "No pobre - 0 puntos"
        )

        // Departamentos y sus puntajes
        val DEPARTAMENTOS = mapOf(
            "Amazonas" to 10,
            "Ucayali" to 10,
            "Ayacucho" to 10,
            "Puno" to 10,
            "Loreto" to 10,
            "San Martín" to 7,
            "Cusco" to 7,
            "Huánuco" to 7,
            "Apurímac" to 7,
            "Huancavelica" to 7,
            "Áncash" to 5,
            "Tacna" to 5,
            "Madre de Dios" to 5,
            "Moquegua" to 5,
            "Pasco" to 5,
            "Cajamarca" to 5,
            "Arequipa" to 2,
            "Piura" to 2,
            "Junín" to 2,
            "Tumbes" to 2,
            "Ica" to 0,
            "Lambayeque" to 0,
            "Lima" to 0,
            "La Libertad" to 0
        )

        // Opciones de lengua originaria
        val LENGUAS_ORIGINARIAS = listOf(
            "Hablante de lengua de primera prioridad - 10 puntos",
            "Hablante de lengua de segunda prioridad - 5 puntos"
        )
    }

    // Funciones para actualizar el estado
    fun updateNombre(nombre: String) {
        val error = validarNombre(nombre)
        _state.value = _state.value.copy(
            nombre = nombre,
            nombreError = error
        )
        validarPasoActual()
        saveCurrentState()
    }

    fun updateModalidad(modalidad: String) {
        _state.value = _state.value.copy(
            modalidad = modalidad,
            mostrarLenguaOriginaria = modalidad == "EIB"
        )
        validarPasoActual()
        saveCurrentState()
    }

    fun updatePuntajeENP(puntaje: String) {
        val error = validarPuntajeENP(puntaje)
        _state.value = _state.value.copy(
            puntajeENP = puntaje,
            puntajeENPError = error
        )
        validarPasoActual()
        saveCurrentState()
    }

    fun updateSISFOH(clasificacion: String) {
        _state.value = _state.value.copy(clasificacionSISFOH = clasificacion)
        validarPasoActual()
        saveCurrentState()
    }

    fun updateDepartamento(departamento: String) {
        _state.value = _state.value.copy(departamento = departamento)
        validarPasoActual()
        saveCurrentState()
    }

    fun updateLenguaOriginaria(lengua: String) {
        _state.value = _state.value.copy(
            lenguaOriginaria = lengua,
            lenguaOriginariaError = null
        )
        validarPasoActual()
        saveCurrentState()
    }

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

    // Funciones de validación
    private fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 3 -> "Coloca un nombre válido, mínimo 3 caracteres"
            else -> null
        }
    }

    private fun validarPuntajeENP(puntaje: String): String? {
        return try {
            val valor = puntaje.toIntOrNull()
            when {
                valor == null -> "Ingrese un número válido"
                valor > 120 -> "El puntaje no puede ser mayor a 120"
                valor % 2 != 0 -> "El puntaje debe ser un número par"
                else -> null
            }
        } catch (_: NumberFormatException) {
            "Ingrese un número válido"
        }
    }

    // Función para validar todos los campos del paso actual
    fun validarPasoActual(): Boolean {
        val state = _state.value
        var valido = true

        when (state.currentStep) {
            1 -> {
                val nombreError = validarNombre(state.nombre)
                val puntajeError = if (state.puntajeENP.isNotEmpty())
                    validarPuntajeENP(state.puntajeENP)
                else
                    "El puntaje es requerido"

                // Validar todos los campos y guardar los errores
                val modalidadError = if (state.modalidad.isEmpty())
                    "Seleccione una modalidad"
                else
                    null

                val sisfohError = if (state.clasificacionSISFOH.isEmpty())
                    "Seleccione una clasificación"
                else
                    null

                val departamentoError = if (state.departamento.isEmpty())
                    "Seleccione un departamento"
                else
                    null

                val lenguaOriginariaError = if (state.mostrarLenguaOriginaria && state.lenguaOriginaria.isEmpty())
                    "Seleccione una lengua originaria"
                else
                    null

                // Actualizar el estado con todos los errores
                _state.value = state.copy(
                    nombreError = nombreError,
                    puntajeENPError = puntajeError,
                    modalidadError = modalidadError,
                    sisfohError = sisfohError,
                    departamentoError = departamentoError,
                    lenguaOriginariaError = lenguaOriginariaError
                )

                // Verificar si todos los campos requeridos están completos y válidos
                valido = nombreError == null &&
                        puntajeError == null &&
                        state.modalidad.isNotEmpty() &&
                        state.clasificacionSISFOH.isNotEmpty() &&
                        state.departamento.isNotEmpty() &&
                        (!state.mostrarLenguaOriginaria || state.lenguaOriginaria.isNotEmpty())
            }
            2 -> {valido = true} //no necesita validación
            3 -> {valido = true} // El paso 3 solo muestra resultados, no necesita validación
        }
        _state.value = state.copy(habilitarContinuar = valido)
        return valido
    }

    // Navegación entre pasos
    fun nextStep() {
        if (_state.value.currentStep < 3) {
            _state.value = _state.value.copy(
                currentStep = _state.value.currentStep + 1
            )
        }
    }

    fun previousStep() {
        if (_state.value.currentStep > 1) {
            _state.value = _state.value.copy(
                currentStep = _state.value.currentStep - 1
            )
        }
    }

    fun calculateScore() {
        val state = _state.value
        var puntajeTotal = 0

        // Calcular puntajes individuales
        val puntajeENP = state.puntajeENP.toIntOrNull() ?: 0
        val puntajeSISFOH = calcularPuntajeSISFOH(state.clasificacionSISFOH, state.modalidad)
        val departamento = state.departamento.split(" - ").first()
        val puntajeQuintil = DEPARTAMENTOS[departamento] ?: 0
        val puntajeActividades = calcularPuntajeActividades(state.actividadesExtracurriculares)
        val puntajePriorizable = calcularPuntajeCondiciones(state.condicionesPriorizables)
        val puntajeLengua = if (state.modalidad == "EIB") {
            calcularPuntajeLenguaOriginaria(state.lenguaOriginaria)
        } else 0

        // Determinar el quintil
        val quintil = when (puntajeQuintil) {
            10 -> "1"
            7 -> "2"
            5 -> "3"
            2 -> "4"
            else -> "5"
        }

        // Sumar todos los puntajes
        puntajeTotal = puntajeENP + puntajeSISFOH + puntajeQuintil +
                puntajeActividades + puntajePriorizable + puntajeLengua

        // Generar el desglose usando el mismo formato que la UI
        val desglose = buildString {
            appendLine("✅ Modalidad: ${state.modalidad}")
            appendLine("✅ ENP: $puntajeENP puntos")
            appendLine("✅ SISFOH: $puntajeSISFOH puntos")
            appendLine("✅ Tasa de transición (Quintil $quintil): $puntajeQuintil puntos")
            appendLine("✅ Actividades extracurriculares: $puntajeActividades puntos")
            appendLine("✅ Condiciones priorizables: $puntajePriorizable puntos")
            if (state.modalidad == "EIB") {
                appendLine("✅ Lengua originaria: $puntajeLengua puntos")
            }
        }

        _state.value = state.copy(
            puntajeTotal = puntajeTotal,
            desglosePuntaje = desglose,
            showResults = true
        )
    }

    fun calcularPuntajeSISFOH(clasificacion: String, modalidad: String): Int {
        return when {
            clasificacion.contains("extrema") -> 5
            clasificacion.contains("Pobreza (P)") && modalidad != "Ordinaria" -> 2
            else -> 0
        }
    }

    fun calcularPuntajeActividades(actividades: Set<ActividadExtracurricular>): Int {
        var puntaje = 0
        for (actividad in actividades) {
            puntaje += when (actividad) {
                ActividadExtracurricular.CONCURSO_NACIONAL,
                ActividadExtracurricular.JUEGOS_NACIONALES -> 5
                ActividadExtracurricular.CONCURSO_PARTICIPACION,
                ActividadExtracurricular.JUEGOS_PARTICIPACION -> 2
            }
        }
        return minOf(puntaje, 10) // Máximo 10 puntos
    }

    fun isCondicionEnabled(condicion: CondicionPriorizable): Boolean {
        return when (condicion) {
            CondicionPriorizable.DESPROTECCION ->
                _state.value.modalidad == "Protección"
            CondicionPriorizable.COMUNIDAD_NATIVA ->
                _state.value.modalidad != "CNA y PA"
            CondicionPriorizable.ORFANDAD ->
                _state.value.modalidad != "Protección"
            else -> true
        }
    }

    fun calcularPuntajeCondiciones(condiciones: Set<CondicionPriorizable>): Int {
        return minOf(condiciones.size * 5, 25) // 5 puntos por condición, máximo 25 puntos
    }

    fun calcularPuntajeLenguaOriginaria(lengua: String): Int {
        return when {
            lengua.contains("primera prioridad") -> 10
            lengua.contains("segunda prioridad") -> 5
            else -> 0
        }
    }

    fun limpiarActividadesExtracurriculares() {
        _state.value = _state.value.copy(
            actividadesExtracurriculares = emptySet()
        )
        saveCurrentState()
    }

    fun limpiarCondicionesPriorizables() {
        _state.value = _state.value.copy(
            condicionesPriorizables = emptySet()
        )
        saveCurrentState()
    }

    private fun saveCurrentState() {
        viewModelScope.launch {
            storage.saveData(
                PreselectionData(
                    nombre = _state.value.nombre,
                    modalidad = _state.value.modalidad,
                    puntajeENP = _state.value.puntajeENP,
                    sisfoh = _state.value.clasificacionSISFOH,
                    departamento = _state.value.departamento,
                    lenguaOriginaria = _state.value.lenguaOriginaria,
                    actividades = _state.value.actividadesExtracurriculares.map { it.name }.toSet(),
                    condiciones = _state.value.condicionesPriorizables.map { it.name }.toSet()
                )
            )
        }
    }

    fun resetCalculator() {
        viewModelScope.launch {
            storage.clearData()
            _state.value = PreselectionState()
        }
    }
}