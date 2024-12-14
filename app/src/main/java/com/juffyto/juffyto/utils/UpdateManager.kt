package com.juffyto.juffyto.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateManager(private val context: Context) : DefaultLifecycleObserver {
    private val updateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)
    private var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            updateManager.completeUpdate()
        }
    }

    fun setActivityResultLauncher(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        activityResultLauncher = launcher
    }

    fun checkForUpdates() {
        updateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (shouldUpdate(appUpdateInfo)) {
                startUpdate(appUpdateInfo)
            }
        }
    }

    private fun shouldUpdate(appUpdateInfo: AppUpdateInfo): Boolean {
        return (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) &&
                ((appUpdateInfo.clientVersionStalenessDays() ?: 0) >= 1) &&
                (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo) {
        try {
            val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()

            // Usando la versión correcta de startUpdateFlowForResult
            val started = updateManager.startUpdateFlowForResult(
                appUpdateInfo,
                context as Activity,
                updateOptions,
                UPDATE_REQUEST_CODE
            )

            if (!started) {
                Log.e("UpdateManager", "No se pudo iniciar el flujo de actualización")
            }
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        updateManager.registerListener(installStateUpdatedListener)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        updateManager.unregisterListener(installStateUpdatedListener)
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 500
    }
}