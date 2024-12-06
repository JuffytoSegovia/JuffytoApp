package com.juffyto.juffyto.ui.components.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.juffyto.juffyto.utils.AdMobConstants

class RewardedAdManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null
    private val adRequestInProgress = mutableMapOf<String, Boolean>()
    private var loadAttempts = 0
    private val maxAttempts = 3

    init {
        loadAd()  // Carga inicial
    }

    fun loadAd() {
        val requestId = System.currentTimeMillis().toString()
        if (adRequestInProgress[requestId] == true) return

        adRequestInProgress[requestId] = true

        RewardedAd.load(
            context,
            AdMobConstants.getRewardedAdUnitId(),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("RewardedAd", "Error al cargar anuncio: $error")
                    rewardedAd = null
                    adRequestInProgress[requestId] = false
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("RewardedAd", "Anuncio cargado exitosamente")
                    rewardedAd = ad
                    adRequestInProgress[requestId] = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onRewarded: () -> Unit) {
        val ad = rewardedAd
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
                rewardedAd = null
                loadAd()
                loadAttempts = 0 // Reiniciar intentos
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("RewardedAd", "Error al mostrar anuncio: $error")
                rewardedAd = null
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