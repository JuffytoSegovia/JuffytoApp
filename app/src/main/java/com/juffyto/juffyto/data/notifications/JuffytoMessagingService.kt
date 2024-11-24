package com.juffyto.juffyto.data.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JuffytoMessagingService : FirebaseMessagingService() {

    private val notificationHelper by lazy { NotificationHelper(this) }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        scope.launch {
            // Aquí guardaremos el token en las preferencias
            // Implementaremos esto después
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Verificar si el mensaje contiene una notificación
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "Juffyto"
            val message = notification.body ?: ""
            notificationHelper.showNotification(title, message)
        }

        // Verificar si hay datos adicionales
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage.data)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        // Aquí manejaremos los datos específicos de la notificación
        // como tipo de notificación, fase del cronograma, etc.
        val title = data["title"] ?: "Juffyto"
        val message = data["message"] ?: ""
        notificationHelper.showNotification(title, message)
    }

    companion object {
        private const val TAG = "JuffytoMessaging"
    }
}