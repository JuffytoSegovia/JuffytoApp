package com.juffyto.juffyto.data.notifications

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: return Result.failure()
        val message = inputData.getString(KEY_MESSAGE) ?: return Result.failure()

        notificationHelper.showNotification(title, message)

        // Programar la siguiente notificación según la frecuencia
        scheduleNextNotification()

        return Result.success()
    }

    private fun scheduleNextNotification() {
        // Aquí implementaremos la lógica para programar la siguiente notificación
        // basada en la frecuencia configurada
    }

    companion object {
        const val WORK_NAME = "notification_work" // Cambiado a público
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"

        fun schedule(
            context: Context,
            title: String,
            message: String,
            notificationTime: LocalTime
        ) {
            val now = LocalDateTime.now()
            val scheduledTime = LocalDateTime.now()
                .with(notificationTime)
                .let {
                    if (it.isBefore(now)) it.plusDays(1) else it
                }

            val delay = Duration.between(now, scheduledTime)
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        KEY_TITLE to title,
                        KEY_MESSAGE to message
                    )
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
        }
    }
}