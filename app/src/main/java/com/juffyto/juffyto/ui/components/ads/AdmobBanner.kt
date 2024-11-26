package com.juffyto.juffyto.ui.components.ads

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

@Composable
fun AdmobBanner(
    adUnitId: String,
    adSize: AdSize = AdSize.BANNER,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                this.adUnitId = adUnitId
                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        // Log el error
                        Log.e("AdmobBanner", "Error al cargar anuncio: $error")
                        // Reintentar inmediatamente
                        loadAd(AdRequest.Builder().build())
                    }

                    override fun onAdLoaded() {
                        // Log de éxito
                        Log.d("AdmobBanner", "Anuncio cargado exitosamente")
                    }
                }
                // Cargar el anuncio
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}