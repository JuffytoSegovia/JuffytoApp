package com.juffyto.juffyto.data.notifications

import android.app.Application
import android.content.Context
import androidx.work.*
import com.juffyto.juffyto.data.model.NotificationFrequency
import com.juffyto.juffyto.data.preferences.SettingsPreferences
import com.juffyto.juffyto.ui.screens.chronogram.components.ChronogramViewModel
import com.juffyto.juffyto.ui.screens.chronogram.model.Phase
import kotlinx.coroutines.flow.first
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val notificationHelper = NotificationHelper(context)
    private val settingsPreferences = SettingsPreferences(context)
    private val chronogramViewModel = ChronogramViewModel(context.applicationContext as Application)

    private fun Phase.isImportantPhase(): Boolean {
        return when {
            // Fases de Preselección
            title.contains("Fase de Postulación para la Preselección") -> true
            title.contains("Publicación de los resultados de la Fase de Validación") -> true
            title.contains("Examen Nacional de Preselección") -> true
            title.contains("Publicación de Preseleccionados") -> true

            // Fases de Selección (ambos momentos)
            title.contains("Fase de Postulación para la Selección") -> true
            title.contains("Subsanación de expedientes") -> true
            title.contains("Publicación de Seleccionados") -> true
            title.contains("Fase de Aceptación de la Beca") -> true
            title.contains("Publicación de Lista de Becarios") -> true
            else -> false
        }
    }

    private fun getPhaseSpecificMessage(phase: Phase, daysLeft: Long? = null): String {
        val urgencyPrefix = when {
            daysLeft != null && daysLeft <= 1 -> "¡ÚLTIMO DÍA! "
            daysLeft != null && daysLeft <= 3 -> "¡URGENTE! Solo quedan $daysLeft días. "
            daysLeft != null && daysLeft <= 7 -> "¡IMPORTANTE! Menos de una semana. "
            else -> ""
        }

        val message = when {
            phase.title.contains("Postulación", ignoreCase = true) ->
                "¡Completa tu postulación! Ten listos todos los documentos."
            phase.title.contains("Subsanación", ignoreCase = true) ->
                "Corrige las observaciones en tus documentos."
            phase.title.contains("Examen", ignoreCase = true) ->
                "Prepárate para tu evaluación. ¡Tu futuro te espera!"
            phase.title.contains("Publicación", ignoreCase = true) ->
                "¡Revisa los resultados! Un paso más cerca de tu meta."
            phase.title.contains("Validación", ignoreCase = true) ->
                "PRONABEC está revisando los expedientes. Ten paciencia."
            phase.title.contains("Aceptación", ignoreCase = true) ->
                "¡Momento crucial! Revisa los términos y condiciones de la beca."
            else -> "Mantente atento a las actualizaciones de esta fase."
        }

        return urgencyPrefix + message
    }

    override suspend fun doWork(): Result {
        val settings = settingsPreferences.settings.first()

        if (!settings.enabled) {
            return Result.success()
        }

        // Obtener fases activas y próximas
        val activePhases = chronogramViewModel.allPhases.filter { it.isActive }
        val nextPhase = chronogramViewModel.allPhases
            .filter { !it.isPast && !it.isActive }
            .minByOrNull { phase ->
                phase.startDate ?: phase.singleDate ?: LocalDate.MAX
            }

        // Construir el mensaje según la frecuencia y las fases
        val (title, message) = when (settings.notificationFrequency) {
            NotificationFrequency.DAILY -> createNotification(activePhases, nextPhase)
            NotificationFrequency.WEEKLY -> createNotification(activePhases, nextPhase)
            NotificationFrequency.IMPORTANT_ONLY -> createNotification(activePhases, nextPhase, true)
        }

        notificationHelper.showNotification(title, message)
        scheduleNextNotification(settings.notificationTime, settings.notificationFrequency)

        return Result.success()
    }

    private fun createNotification(
        activePhases: List<Phase>,
        nextPhase: Phase?,
        onlyImportant: Boolean = false
    ): Pair<String, String> {
        return when {
            activePhases.isNotEmpty() -> {
                val phase = if (onlyImportant) {
                    activePhases.firstOrNull { it.isImportantPhase() }
                } else {
                    activePhases.first()
                } ?: return "Beca 18" to "No hay fases importantes programadas próximamente."

                if (phase.endDate != null) {
                    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), phase.endDate)
                    phase.title to getPhaseSpecificMessage(phase, daysLeft)
                } else {
                    phase.title to getPhaseSpecificMessage(phase)
                }
            }
            nextPhase != null -> {
                if (onlyImportant && !nextPhase.isImportantPhase()) {
                    return "Beca 18" to "No hay fechas importantes programadas próximamente."
                }

                val startDate = nextPhase.startDate ?: nextPhase.singleDate
                if (startDate != null) {
                    val daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), startDate)
                    nextPhase.title to "Empieza en $daysUntil días. ${getPhaseSpecificMessage(nextPhase)}"
                } else {
                    "Beca 18" to "No hay fases activas en este momento."
                }
            }
            else -> "Beca 18" to "No hay fases activas en este momento."
        }
    }

    private fun scheduleNextNotification(timeString: String, frequency: NotificationFrequency) {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val notificationTime = LocalTime.parse(timeString, formatter)

        val now = LocalDateTime.now()
        val nextTime = LocalDateTime.now()
            .with(notificationTime)
            .let {
                when (frequency) {
                    NotificationFrequency.DAILY -> if (it.isBefore(now)) it.plusDays(1) else it
                    NotificationFrequency.WEEKLY -> if (it.isBefore(now)) it.plusWeeks(1) else it
                    NotificationFrequency.IMPORTANT_ONLY -> if (it.isBefore(now)) it.plusDays(1) else it
                }
            }

        val delay = Duration.between(now, nextTime)

        val activePhases = chronogramViewModel.allPhases.filter { it.isActive }
        val nextPhase = chronogramViewModel.allPhases
            .filter { !it.isPast && !it.isActive }
            .minByOrNull { phase ->
                phase.startDate ?: phase.singleDate ?: LocalDate.MAX
            }

        val (title, message) = createNotification(activePhases, nextPhase, frequency == NotificationFrequency.IMPORTANT_ONLY)

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    KEY_TITLE to title,
                    KEY_MESSAGE to message
                )
            )
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    companion object {
        const val WORK_NAME = "notification_work"
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