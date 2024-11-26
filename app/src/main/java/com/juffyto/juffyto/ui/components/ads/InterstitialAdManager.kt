package com.juffyto.juffyto.ui.components.ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.juffyto.juffyto.utils.AdMobConstants

class InterstitialAdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private val prefs: SharedPreferences = context.getSharedPreferences("ad_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_AD_SHOWN = "last_ad_shown"
        private const val KEY_IS_FIRST_SESSION = "is_first_session"
        private const val MIN_INTERVAL_BETWEEN_ADS = 6000L // 2 minutos en milisegundos
    }

    init {
        loadAd()
        checkFirstSession()
    }

    private fun checkFirstSession() {
        if (!prefs.contains(KEY_IS_FIRST_SESSION)) {
            prefs.edit().putBoolean(KEY_IS_FIRST_SESSION, true).apply()
        }
    }

    private fun canShowAd(): Boolean {
        // No mostrar anuncios en la primera sesión
        if (prefs.getBoolean(KEY_IS_FIRST_SESSION, true)) {
            prefs.edit().putBoolean(KEY_IS_FIRST_SESSION, false).apply()
            return false
        }

        // Verificar el tiempo transcurrido desde el último anuncio
        val lastShown = prefs.getLong(KEY_LAST_AD_SHOWN, 0L)
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastShown) >= MIN_INTERVAL_BETWEEN_ADS
    }

    private fun updateLastShownTime() {
        prefs.edit().putLong(KEY_LAST_AD_SHOWN, System.currentTimeMillis()).apply()
    }

    fun loadAd() {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdMobConstants.getInterstitialAdUnitId(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("InterstitialAd", "Error al cargar anuncio: $error")
                    interstitialAd = null
                    isLoading = false
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("InterstitialAd", "Anuncio cargado exitosamente")
                    interstitialAd = ad
                    isLoading = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
        if (!canShowAd()) {
            Log.d("InterstitialAd", "No se cumple el tiempo mínimo entre anuncios")
            onAdDismissed()
            return
        }

        val ad = interstitialAd
        if (ad == null) {
            Log.d("InterstitialAd", "Anuncio no disponible, cargando nuevo")
            loadAd()
            onAdDismissed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("InterstitialAd", "Anuncio cerrado")
                interstitialAd = null
                loadAd()
                updateLastShownTime()
                onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("InterstitialAd", "Error al mostrar anuncio: $error")
                interstitialAd = null
                loadAd()
                onAdDismissed()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("InterstitialAd", "Anuncio mostrado exitosamente")
                interstitialAd = null
            }
        }

        ad.show(activity)
    }
}