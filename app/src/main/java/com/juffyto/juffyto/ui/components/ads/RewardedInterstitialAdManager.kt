package com.juffyto.juffyto.ui.components.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.juffyto.juffyto.utils.AdMobConstants

class RewardedInterstitialAdManager(private val context: Context) {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private val adRequestInProgress = mutableMapOf<String, Boolean>()
    private var loadAttempts = 0
    private val maxAttempts = 2

    init {
        loadAd()
    }

    fun loadAd() {
        val requestId = System.currentTimeMillis().toString()
        if (adRequestInProgress[requestId] == true) return

        adRequestInProgress[requestId] = true

        RewardedInterstitialAd.load(
            context,
            AdMobConstants.getRewardedInterstitialAdUnitId(),
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("RewardedInterstitial", "Error al cargar anuncio: $error")
                    rewardedInterstitialAd = null
                    adRequestInProgress[requestId] = false
                    loadAd() // Intentar cargar de nuevo
                }

                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d("RewardedInterstitial", "Anuncio cargado exitosamente")
                    rewardedInterstitialAd = ad
                    adRequestInProgress[requestId] = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onRewarded: () -> Unit) {
        val ad = rewardedInterstitialAd
        if (ad == null) {
            loadAttempts++
            if (loadAttempts >= maxAttempts) {
                onRewarded()
                loadAttempts = 0
                return
            }
            loadAd()
            Toast.makeText(context, "Cargando, por favor intenta de nuevo", Toast.LENGTH_SHORT).show()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Cuando el usuario cierra el anuncio
                rewardedInterstitialAd = null
                loadAd() // Cargar el siguiente anuncio
                loadAttempts = 0 // Reiniciar intentos
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("RewardedInterstitial", "Error al mostrar anuncio: $error")
                rewardedInterstitialAd = null
                loadAd()
                loadAttempts = 0
            }
        }

        ad.show(activity) { rewardItem ->
            onRewarded() // Solo se llama si el usuario completa el anuncio
            loadAttempts = 0
        }
    }
}