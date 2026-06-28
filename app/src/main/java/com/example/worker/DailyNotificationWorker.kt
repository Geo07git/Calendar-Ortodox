package com.example.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.data.CalendarRepository
import java.time.LocalDate

class DailyNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.failure()
        }

        val prefs = com.example.data.NotificationPreferences(context)
        if (!prefs.notificationsEnabled) {
            return Result.success()
        }

        val repository = CalendarRepository()
        val today = LocalDate.now()
        val todayInfo = repository.getDayInfo(today)

        var shouldNotify = false
        var notificationTitle = "Sărbătoarea de azi"

        if (prefs.notifyHolidays && (todayInfo.isRedCross || todayInfo.isBlackCross)) {
            shouldNotify = true
            notificationTitle = if (todayInfo.isRedCross) "Sărbătoare importantă" else "Sărbătoare"
        }

        if (prefs.notifySaints && !shouldNotify) {
            shouldNotify = true
            notificationTitle = "Sfinții zilei"
        }

        if (prefs.notifyFasting && todayInfo.fastingType != com.example.data.FastingType.NONE) {
            shouldNotify = true
            // If it's just a fasting notification, change title if not already set by holidays/saints
            if (notificationTitle == "Sfinții zilei" && !prefs.notifySaints) {
               notificationTitle = "Zi de post"
            }
        }

        // If no preference matched the current day's info, don't notify
        if (!shouldNotify) {
            return Result.success()
        }

        showNotification(notificationTitle, todayInfo.title, todayInfo.fastingType.description)

        return Result.success()
    }

    private fun showNotification(header: String, title: String, description: String) {
        val channelId = "orthodox_calendar_daily"
        val notificationId = 1

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            // We use the default launcher icon since we don't have a specific custom one right now
            .setSmallIcon(android.R.drawable.ic_popup_reminder) 
            .setContentTitle(header)
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title\n$description"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)

        // Create channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notificări zilnice"
            val channelDescription = "Notificări cu sfinții și sărbătorile zilei"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Permission wasn't granted
        }
    }
}
