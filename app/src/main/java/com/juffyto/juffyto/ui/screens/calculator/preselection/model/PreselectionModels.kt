package com.juffyto.juffyto.ui.screens.calculator.preselection.model

enum class ActividadExtracurricular {
    CONCURSO_NACIONAL,      // 5 puntos
    CONCURSO_PARTICIPACION, // 2 puntos
    JUEGOS_NACIONALES,      // 5 puntos
    JUEGOS_PARTICIPACION    // 2 puntos
}

enum class CondicionPriorizable {
    DISCAPACIDAD,           // 5 puntos
    BOMBEROS,               // 5 puntos
    VOLUNTARIOS,            // 5 puntos
    COMUNIDAD_NATIVA,       // 5 puntos
    METALES_PESADOS,        // 5 puntos
    POBLACION_BENEFICIARIA, // 5 puntos
    ORFANDAD,               // 5 puntos
    DESPROTECCION,          // 5 puntos
    AGENTE_SALUD           // 5 puntos
}

object PreselectionConstants {
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

    // Lenguas originarias
    val LENGUAS_ORIGINARIAS = listOf(
        "Hablante de lengua de primera prioridad - 10 puntos",
        "Hablante de lengua de segunda prioridad - 5 puntos"
    )

    // Puntajes máximos
    const val MAX_PUNTAJE_ACTIVIDADES = 10
    const val MAX_PUNTAJE_CONDICIONES = 25
    const val MAX_PUNTAJE_ENP = 120
    const val MAX_PUNTAJE_EIB = 180
    const val MAX_PUNTAJE_NORMAL = 170
}