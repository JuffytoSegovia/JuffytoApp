package com.juffyto.juffyto

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class JuffytoApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        // Inicializar WorkManager
        WorkManager.initialize(
            this,
            workManagerConfiguration
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}