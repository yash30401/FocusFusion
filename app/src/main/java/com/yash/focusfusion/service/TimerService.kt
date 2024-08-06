package com.yash.focusfusion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.DatastoreUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private var cancelTimeCountDownTimer:CountDownTimer?=null

    private var timeLeft: Long = 0
    private var extraTime: Int = 0
    private var cancelTimeLeft:Long = 0

    @Inject
    lateinit var datastoreUseCases: DatastoreUseCases

    private val scope = CoroutineScope(Dispatchers.IO)
    private var extraTimeJob: Job? = null

    val broadcastIntent = Intent("TIMER_UPDATE")

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                timeLeft = intent.getLongExtra("TIME_LEFT", 0L)
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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
                sendBroadcast(broadcastIntent)
            }

            override fun onFinish() {
                showCompletionNotification()
                startExtraTimerInTheBackground()
            }
        }.start()

        cancelTimeCountDownTimer = object :CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                this@TimerService.cancelTimeLeft = millisUntilFinished


                broadcastIntent.putExtra("CANCEL_TIME_LEFT", millisUntilFinished)
                sendBroadcast(broadcastIntent)
            }

            override fun onFinish() {

            }

        }.start()

        scope.launch {
            datastoreUseCases.saveContinueTimerUseCase(true)
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
            datastoreUseCases.saveContinueTimerUseCase(false)
        }
    }

    private fun resetTimer() {
        timeLeft = 1500000L // 25 minutes
        extraTime = 0
        cancelTimeLeft = 10000L

        scope.launch {
            datastoreUseCases.saveTimeLeftUseCase(timeLeft)
            datastoreUseCases.saveCancelTimeLeftUseCase(cancelTimeLeft)
            datastoreUseCases.saveExtraTimeUseCase(extraTime)
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
            )
            val channel2 = NotificationChannel(
                "102",
                "Timer Completed",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(channel1, channel2))
        }
    }

    override fun onDestroy() {
        stopTimer()
        scope.launch {
            datastoreUseCases.saveTimeLeftUseCase(timeLeft)
            datastoreUseCases.saveCancelTimeLeftUseCase(cancelTimeLeft)
            datastoreUseCases.saveExtraTimeUseCase(extraTime)
        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        stopSelf()
    }

    companion object {
        fun startService(context: Context, timeLeft: Long) {
            val startIntent = Intent(context, TimerService::class.java).apply {
                action = Actions.START.toString()
                putExtra("TIME_LEFT", timeLeft)
            }
            context.startService(startIntent)
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
