package com.example.data

import android.content.Context
import android.content.SharedPreferences

class NotificationPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    var notifyHolidays: Boolean
        get() = prefs.getBoolean(KEY_HOLIDAYS, true)
        set(value) = prefs.edit().putBoolean(KEY_HOLIDAYS, value).apply()

    var notifySaints: Boolean
        get() = prefs.getBoolean(KEY_SAINTS, true)
        set(value) = prefs.edit().putBoolean(KEY_SAINTS, value).apply()

    var notifyFasting: Boolean
        get() = prefs.getBoolean(KEY_FASTING, false)
        set(value) = prefs.edit().putBoolean(KEY_FASTING, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_ENABLED, value).apply()

    companion object {
        private const val KEY_HOLIDAYS = "notify_holidays"
        private const val KEY_SAINTS = "notify_saints"
        private const val KEY_FASTING = "notify_fasting"
        private const val KEY_ENABLED = "notifications_enabled"
    }
}
