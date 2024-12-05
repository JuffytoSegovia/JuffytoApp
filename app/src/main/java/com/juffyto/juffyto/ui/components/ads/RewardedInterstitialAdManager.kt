package com.juffyto.juffyto.ui.components.ads

import android.app.Activity
import android.content.Context
import android.util.Log
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
            loadAd()
            Log.d("RewardedInterstitial", "Anuncio no disponible, intentando cargar uno nuevo")
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedInterstitialAd = null
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("RewardedInterstitial", "Error al mostrar anuncio: $error")
                rewardedInterstitialAd = null
                loadAd()
            }
        }

        ad.show(activity) { rewardItem ->
            onRewarded()
        }
    }
}