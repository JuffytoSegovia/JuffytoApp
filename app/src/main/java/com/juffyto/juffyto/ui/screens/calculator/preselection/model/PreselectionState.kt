package com.juffyto.juffyto.ui.screens.calculator.preselection.model

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
    val showRecommendations: Boolean = false,
    val error: String? = null,
    val desglosePuntaje: String = "",

    // Estado de navegación
    val shouldNavigateBack: Boolean = false,
    val lastStep: Int = 1,

    // Estado de acciones
    val isAccessUnlocked: Boolean = false
)