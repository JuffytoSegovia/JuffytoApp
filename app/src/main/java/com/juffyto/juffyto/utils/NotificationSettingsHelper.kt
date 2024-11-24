package com.juffyto.juffyto.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

object NotificationSettingsHelper {
    fun openNotificationSettings(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }
}