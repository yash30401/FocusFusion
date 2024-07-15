package com.yash.focusfusion.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yash.focusfusion.R
import com.yash.focusfusion.core.util.Constants.CHANNEL_ID
import com.yash.focusfusion.core.util.Constants.KEY_TIMER_DURATION
import com.yash.focusfusion.core.util.Constants.NOTIFICATION_ID
import kotlinx.coroutines.delay
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
        val timerDuration = inputData.getInt(KEY_TIMER_DURATION, 25 * 60)
        return runTimer(timerDuration)
    }

    private fun runTimer(timerDuration: Int): Result {
        var timeLeft = timerDuration
        while (timeLeft > 0) {
            Thread.sleep(1000)
            timeLeft--
            updateNotification(timeLeft)
        }
        return Result.success()
    }

    private fun updateNotification(timeLeft: Int) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Focus Fusion")
            .setContentText("Time left: ${formatTime(timeLeft)}")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Timer Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

}