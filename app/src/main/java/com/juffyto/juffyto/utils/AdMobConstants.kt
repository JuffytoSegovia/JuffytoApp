package com.juffyto.juffyto.utils

import com.google.android.gms.ads.AdSize

object AdMobConstants {
    // IDs de producción
    private const val BANNER_AD_UNIT_ID = "ca-app-pub-6175076564917081/3890989644"
    private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-6175076564917081/8449181754"

    // IDs de prueba
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Para controlar globalmente si estamos en modo prueba o producción
    private const val IS_TEST_MODE = true  // Cambiar a false para producción

    // Función para obtener el ID correcto según el ambiente
    fun getBannerAdUnitId(isTestMode: Boolean = IS_TEST_MODE): String {
        return if (isTestMode) TEST_BANNER_AD_UNIT_ID else BANNER_AD_UNIT_ID
    }

    // Nueva función para obtener ID de intersticiales
    fun getInterstitialAdUnitId(isTestMode: Boolean = IS_TEST_MODE): String {
        return if (isTestMode) TEST_INTERSTITIAL_AD_UNIT_ID else INTERSTITIAL_AD_UNIT_ID
    }

    // Tamaños de anuncios predefinidos
    object AdSizes {
        val SMALL_BANNER = AdSize.BANNER           // 320x50
        val LARGE_BANNER = AdSize.LARGE_BANNER     // 320x100
        val MEDIUM_BOX = AdSize.MEDIUM_RECTANGLE   // 300x250
        val FULL_WIDTH = AdSize.FULL_BANNER        // 468x60
    }
}