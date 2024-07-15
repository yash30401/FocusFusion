package com.yash.focusfusion.worker

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject


@HiltWorker
class TimerWorker @Inject constructor(
    context: Context,
    workerParam: WorkerParameters
) : Worker(context, workerParam) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun doWork(): Result {

    }

}