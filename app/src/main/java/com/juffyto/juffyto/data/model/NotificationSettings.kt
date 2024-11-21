package com.juffyto.juffyto.data.model

import java.time.ZoneId

data class NotificationSettings(
    val enabled: Boolean = true,
    val notificationTime: String = "09:00 AM",
    val notificationFrequency: NotificationFrequency = NotificationFrequency.DAILY,
    val zoneId: ZoneId = ZoneId.of("America/Lima")
)

enum class NotificationFrequency {
    DAILY,
    WEEKLY,
    IMPORTANT_ONLY;

    fun getDisplayName(): String = when(this) {
        DAILY -> "diariamente"
        WEEKLY -> "semanalmente"
        IMPORTANT_ONLY -> "solo en fechas importantes"
    }
}