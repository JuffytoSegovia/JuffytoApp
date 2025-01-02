package com.juffyto.juffyto.ui.screens.calculator.selection.model

data class SelectionState(
    // Paso 1: Datos de entrada
    val nombre: String = "",
    val modalidad: String = "",
    val puntajePreseleccion: String = "",
    val regionIES: String = "",
    val tiposIESSeleccionados: Set<String> = emptySet(),
    val gestionesIESSeleccionadas: Set<String> = emptySet(),
    val iesSeleccionada: IES? = null,

    // Errores de validación
    val nombreError: String? = null,
    val modalidadError: String? = null,
    val puntajePreseleccionError: String? = null,
    val regionIESError: String? = null,
    val iesError: String? = null,

    // Estado de la UI
    val currentStep: Int = 1,
    val showRecommendationsDialog: Boolean = false,
    val puntajeTotal: Int? = null,
    val desglosePuntaje: String = "",
    val habilitarCalcular: Boolean = false,

    // Listas filtradas
    val iesDisponibles: List<IES> = emptyList(),
    val tiposIESDisponibles: Set<String> = emptySet(),
    val gestionesIESDisponibles: Set<String> = emptySet()
)