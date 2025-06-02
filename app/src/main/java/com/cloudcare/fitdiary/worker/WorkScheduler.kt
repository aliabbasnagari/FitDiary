package com.cloudcare.fitdiary.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object WorkScheduler {
    fun scheduleDailyReminder(context: Context, reminderTime: LocalTime) {
        val workManager = WorkManager.getInstance(context)
        val currentTime = LocalDateTime.now()
        val targetTime = LocalDateTime.of(currentTime.toLocalDate(), reminderTime)
        val initialDelay = if (currentTime.isBefore(targetTime)) {
            Duration.between(currentTime, targetTime).toMillis()
        } else {
            // Schedule for next day if time has passed
            Duration.between(currentTime, targetTime.plusDays(1)).toMillis()
        }

        val workRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(
            repeatInterval = 1, // Repeat every day
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(ReminderNotificationWorker.WORK_NAME)
            .build()

        workManager.cancelAllWorkByTag(ReminderNotificationWorker.WORK_NAME)
        workManager.enqueueUniquePeriodicWork(
            ReminderNotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelReminders(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(ReminderNotificationWorker.WORK_NAME)
    }
}