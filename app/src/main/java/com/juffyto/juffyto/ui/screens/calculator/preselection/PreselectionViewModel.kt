package com.juffyto.juffyto.ui.screens.calculator.preselection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreselectionViewModel : ViewModel() {
    private val _state = MutableStateFlow(PreselectionState())
    val state: StateFlow<PreselectionState> = _state.asStateFlow()

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
        val error: String? = null
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
        _state.value = _state.value.copy(nombre = nombre)
    }

    fun updateModalidad(modalidad: String) {
        _state.value = _state.value.copy(modalidad = modalidad)
    }

    fun updatePuntajeENP(puntaje: String) {
        _state.value = _state.value.copy(puntajeENP = puntaje)
    }

    fun updateSISFOH(clasificacion: String) {
        _state.value = _state.value.copy(clasificacionSISFOH = clasificacion)
    }

    fun updateDepartamento(departamento: String) {
        _state.value = _state.value.copy(departamento = departamento)
    }

    // Agregar después de la función updateDepartamento
    fun updateLenguaOriginaria(lengua: String) {
        _state.value = _state.value.copy(lenguaOriginaria = lengua)
    }

    fun toggleActividadExtracurricular(actividad: ActividadExtracurricular) {
        val currentActividades = _state.value.actividadesExtracurriculares.toMutableSet()
        if (currentActividades.contains(actividad)) {
            currentActividades.remove(actividad)
        } else {
            currentActividades.add(actividad)
        }
        _state.value = _state.value.copy(actividadesExtracurriculares = currentActividades)
    }

    fun toggleCondicionPriorizable(condicion: CondicionPriorizable) {
        val currentCondiciones = _state.value.condicionesPriorizables.toMutableSet()
        if (currentCondiciones.contains(condicion)) {
            currentCondiciones.remove(condicion)
        } else {
            currentCondiciones.add(condicion)
        }
        _state.value = _state.value.copy(condicionesPriorizables = currentCondiciones)
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
        return try {
            val valor = puntaje.toInt()
            when {
                valor < 0 -> "El puntaje no puede ser negativo"
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
                val puntajeError = if (state.puntajeENP.isNotEmpty()) validarPuntajeENP(state.puntajeENP) else "El puntaje es requerido"

                _state.value = state.copy(
                    nombreError = nombreError,
                    puntajeENPError = puntajeError,
                    modalidadError = if (state.modalidad.isEmpty()) "Seleccione una modalidad" else null,
                    sisfohError = if (state.clasificacionSISFOH.isEmpty()) "Seleccione una clasificación" else null,
                    departamentoError = if (state.departamento.isEmpty()) "Seleccione un departamento" else null,
                    lenguaOriginariaError = if (state.mostrarLenguaOriginaria && state.lenguaOriginaria.isEmpty())
                        "Seleccione una lengua originaria" else null
                )

                valido = nombreError == null && puntajeError == null &&
                        state.modalidad.isNotEmpty() && state.clasificacionSISFOH.isNotEmpty() &&
                        state.departamento.isNotEmpty() &&
                        (!state.mostrarLenguaOriginaria || state.lenguaOriginaria.isNotEmpty())
            }
            // Añadiremos validaciones para los otros pasos después
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

    // Funciones para calcular puntaje
    // Reemplazar la función calculateScore actual
    fun calculateScore() {
        val state = _state.value
        var puntajeTotal = 0

        // Puntaje ENP
        puntajeTotal += state.puntajeENP.toIntOrNull() ?: 0

        // Puntaje SISFOH
        puntajeTotal += calcularPuntajeSISFOH(state.clasificacionSISFOH, state.modalidad)

        // Puntaje por departamento
        puntajeTotal += DEPARTAMENTOS[state.departamento] ?: 0

        // Puntaje actividades extracurriculares (máximo 10 puntos)
        puntajeTotal += calcularPuntajeActividades(state.actividadesExtracurriculares)

        // Puntaje condiciones priorizables (máximo 25 puntos)
        puntajeTotal += calcularPuntajeCondiciones(state.condicionesPriorizables)

        // Puntaje lengua originaria (solo para modalidad EIB)
        if (state.modalidad == "EIB") {
            puntajeTotal += calcularPuntajeLenguaOriginaria(state.lenguaOriginaria)
        }

        _state.value = state.copy(
            puntajeTotal = puntajeTotal,
            showResults = true
        )
    }

    // Agregar esta función después del calculateScore
    fun obtenerDesglosePuntaje(): String {
        val state = _state.value
        return buildString {
            appendLine("✅ Modalidad: ${state.modalidad}")
            appendLine("✅ ENP: ${state.puntajeENP} puntos")
            appendLine("✅ SISFOH: ${calcularPuntajeSISFOH(state.clasificacionSISFOH, state.modalidad)} puntos")
            appendLine("✅ Quintil: ${DEPARTAMENTOS[state.departamento] ?: 0} puntos")
            appendLine("✅ Actividades extracurriculares: ${calcularPuntajeActividades(state.actividadesExtracurriculares)} puntos")
            appendLine("✅ Condiciones priorizables: ${calcularPuntajeCondiciones(state.condicionesPriorizables)} puntos")
            if (state.modalidad == "EIB") {
                appendLine("✅ Lengua originaria: ${calcularPuntajeLenguaOriginaria(state.lenguaOriginaria)} puntos")
            }
        }
    }

    private fun calcularPuntajeSISFOH(clasificacion: String, modalidad: String): Int {
        return when {
            clasificacion.contains("extrema") -> 5
            clasificacion.contains("Pobreza (P)") && modalidad != "Ordinaria" -> 2
            else -> 0
        }
    }

    private fun calcularPuntajeActividades(actividades: Set<ActividadExtracurricular>): Int {
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

    private fun calcularPuntajeCondiciones(condiciones: Set<CondicionPriorizable>): Int {
        return minOf(condiciones.size * 5, 25) // 5 puntos por condición, máximo 25 puntos
    }

    private fun calcularPuntajeLenguaOriginaria(lengua: String): Int {
        return when {
            lengua.contains("primera prioridad") -> 10
            lengua.contains("segunda prioridad") -> 5
            else -> 0
        }
    }

    fun resetCalculator() {
        _state.value = PreselectionState()
    }
}