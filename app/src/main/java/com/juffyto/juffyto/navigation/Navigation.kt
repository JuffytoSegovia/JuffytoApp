package com.juffyto.juffyto.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Menu : Screen("menu_screen")
    object Chronogram : Screen("chronogram_screen")
    object Enp : Screen("enp_screen")
    object Calculator : Screen("calculator_screen")
    // Podemos agregar más rutas según necesitemos
}