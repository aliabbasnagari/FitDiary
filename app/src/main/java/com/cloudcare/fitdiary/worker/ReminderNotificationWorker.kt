package com.cloudcare.fitdiary.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.cloudcare.fitdiary.MainActivity
import com.cloudcare.fitdiary.R

class ReminderNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        createNotificationChannel()
        showNotification()
        return Result.success()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Health Data Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminder to add health data"
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification() {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "app://com.cloudcare.fitdiary/add_entry".toUri(),
            applicationContext,
            MainActivity::class.java
        )

        // Create PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon
            .setContentTitle("Health Data Reminder")
            .setContentText("Time to add your health data!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "health_reminder_channel"
        const val NOTIFICATION_ID = 1
        const val WORK_NAME = "health_reminder_work"
    }
}