package com.juffyto.juffyto.ui.screens.calculator.selection.model

data class IES(
    val codigoTipoIES: String,
    val tipoIES: String,
    val regionIES: String,
    val nombreIES: String,
    val siglasIES: String,
    val topIES: String,
    val rankingIES: String,
    val puntajeRankingIES: String,
    val gestionIES: String,
    val puntajeGestionIES: String,
    val ratioSelectividad: String,
    val puntajeRatioSelectividad: String,
    val puntosExtraPAO: String
) {
    // Funciones auxiliares para cálculos
    fun calcularPuntajeRanking(): Int {
        return when (topIES) {
            "Top 1 al 6" -> 10
            "Top 7 al 12" -> 7
            "Top 13 al 19" -> 5
            else -> 0
        }
    }

    fun calcularPuntajeGestion(): Int {
        return when (gestionIES) {
            "PÚBLICA" -> 10
            "PRIVADA", "PRIVADA ASOCIATIVA" -> 5
            else -> 0
        }
    }

    fun calcularPuntajeSelectividad(): Int {
        return when (ratioSelectividad) {
            "QUINTIL 5" -> 10
            "QUINTIL 4" -> 7
            "QUINTIL 3" -> 5
            "QUINTIL 2" -> 2
            else -> 0
        }
    }

    fun calcularPuntajeTotal(): Int {
        return calcularPuntajeRanking() + calcularPuntajeGestion() + calcularPuntajeSelectividad()
    }
}