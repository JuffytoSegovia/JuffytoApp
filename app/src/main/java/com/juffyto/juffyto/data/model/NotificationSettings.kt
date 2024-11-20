package com.juffyto.juffyto.data.model

data class NotificationSettings(
    val enabled: Boolean = true,
    val notificationTime: String = "09:00",
    val notificationFrequency: NotificationFrequency = NotificationFrequency.DAILY
)

enum class NotificationFrequency {
    DAILY,
    WEEKLY,
    IMPORTANT_ONLY
}