package com.yash.focusfusion

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TimerService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private var cancelTimeCountDownTimer: CountDownTimer? = null
    private var mediaPlayer: MediaPlayer? = null

    private var timeLeft: Long = 0
    private var extraTime: Int = 0
    private var cancelTimeLeft: Long = 0
    private var taskTag: String = ""

    private lateinit var dataStoreManager: DatastoreManager

    private val scope = CoroutineScope(Dispatchers.IO)
    private var extraTimeJob: Job? = null

    val broadcastIntent = Intent("TIMER_UPDATE")

    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DatastoreManager(this)
        createNotificationChannels()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                timeLeft = intent.getLongExtra("TIME_LEFT", 0L)
                taskTag = intent.getStringExtra("TASK_TAG").toString()
                startTimer(timeLeft)
            }

            Actions.FINISH.toString() -> {
                stopTimer()
                resetTimer()
            }
        }
        return START_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun startTimer(timeLeft: Long) {
        val notification = NotificationCompat.Builder(this, "101")
            .setContentTitle("FocusFusion")
            .setContentText("Time remaining: $timeLeft")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        startForeground(1, notification)

        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                this@TimerService.timeLeft = millisUntilFinished

                val updatedNotification = NotificationCompat.Builder(this@TimerService, "101")
                    .setContentTitle("FocusFusion")
                    .setContentText("Time remaining: ${formatTime(millisUntilFinished)}")
                    .setSmallIcon(R.drawable.baseline_timer_24)
                    .setSilent(true)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                startForeground(1, updatedNotification)


                broadcastIntent.putExtra("TIME_LEFT", millisUntilFinished)
                broadcastIntent.putExtra("TASK_TAG", taskTag.toString())
                sendBroadcast(broadcastIntent)
            }

            override fun onFinish() {
                showCompletionNotification()
                startExtraTimerInTheBackground()
            }
        }.start()

        cancelTimeCountDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                this@TimerService.cancelTimeLeft = millisUntilFinished


                broadcastIntent.putExtra("CANCEL_TIME_LEFT", millisUntilFinished)
                sendBroadcast(broadcastIntent)
            }

            override fun onFinish() {

            }

        }.start()

        scope.launch {
            dataStoreManager.saveContinueTimer(true)
            dataStoreManager.saveTaskTag(taskTag)
            dataStoreManager.saveTimerStartTime(System.currentTimeMillis())
        }
    }

    private fun showCompletionNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "102")
            .setContentTitle("FocusFusion")
            .setContentText("Your session completed!")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        notificationManager.notify(2, notification)
        playAlarm()
    }

    private fun startExtraTimerInTheBackground() {
        extraTimeJob = scope.launch {
            while (true) {
                delay(1000)
                extraTime++

                broadcastIntent.putExtra("EXTRA_TIME", extraTime)
                sendBroadcast(broadcastIntent)
            }
        }
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        cancelTimeCountDownTimer?.cancel()
        cancelTimeCountDownTimer = null

        extraTimeJob?.cancel()
        extraTimeJob = null

        scope.launch {
            dataStoreManager.saveContinueTimer(false)
        }
    }

    private fun resetTimer() {
        scope.launch {
            val savedFocusTime = dataStoreManager.focusTime.first()
            timeLeft = savedFocusTime * 60_000L
            cancelTimeLeft = 10_000L
            extraTime = 0
            taskTag = "STUDY"

            dataStoreManager.saveTimeLeft(timeLeft)
            dataStoreManager.saveCancelTimeLeft(cancelTimeLeft)
            dataStoreManager.saveExtraTime(extraTime)
            dataStoreManager.saveTaskTag(taskTag)
        }
        val notification = NotificationCompat.Builder(this, "101")
            .setContentTitle("FocusFusion")
            .setContentText("Time remaining: $timeLeft")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(1, notification)


        broadcastIntent.putExtra("TIME_LEFT", timeLeft)
        broadcastIntent.putExtra("CANCEL_TIME_LEFT", cancelTimeLeft)
        broadcastIntent.putExtra("EXTRA_TIME", extraTime)
        broadcastIntent.putExtra("IS_FINISHED", true)
        broadcastIntent.putExtra("TASK_TAG", taskTag)
        sendBroadcast(broadcastIntent)

        stopForeground(true)
        stopSelf()
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                "101",
                "Timer Running",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows the current timer progress"
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            val channel2 = NotificationChannel(
                "102",
                "Timer Completed",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when timer is completed"
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(channel1, channel2))
        }
    }

    private fun playAlarm(){
        mediaPlayer = MediaPlayer.create(this,R.raw.session_end_sound)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        stopTimer()
        scope.launch {
            dataStoreManager.saveTimeLeft(timeLeft)
            dataStoreManager.saveCancelTimeLeft(cancelTimeLeft)
            dataStoreManager.saveExtraTime(extraTime)
            dataStoreManager.saveTaskTag(taskTag)
        }
        mediaPlayer?.release()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        stopSelf()
        mediaPlayer?.release()
    }

    companion object {
        fun startService(context: Context, timeLeft: Long, taskTag: String) {
            val startIntent = Intent(context, TimerService::class.java).apply {
                action = Actions.START.toString()
                putExtra("TIME_LEFT", timeLeft)
                putExtra("TASK_TAG", taskTag)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, TimerService::class.java).apply {
                action = Actions.FINISH.toString()
            }
            context.startService(stopIntent)
        }
    }

    enum class Actions {
        START, FINISH
    }
}
