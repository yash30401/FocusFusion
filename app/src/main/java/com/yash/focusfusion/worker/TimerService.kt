import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.yash.focusfusion.MainActivity
import com.yash.focusfusion.R
import com.yash.focusfusion.core.util.Constants.CHECKINGSERVICESLOGS
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject


class TimerService : Service() {

    private val channelId = "TimerServiceChannel"
    private val notificationId = 1

    private var timeLeft = 0
    private var isTimerRunning = false
    private var timerJob: Job? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(CHECKINGSERVICESLOGS, "Entering onStartCommand")
        when (intent?.action) {
            ACTION_START -> {
                Log.d(CHECKINGSERVICESLOGS, "Entering Action Start")
                timeLeft = intent.getIntExtra(EXTRA_TIME_LEFT, 0)
                startTimer()
            }

            ACTION_STOP -> stopTimer()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    fun startTimer() {
        if (isTimerRunning) return
        isTimerRunning = true
        timerJob = serviceScope.launch {
            while (isTimerRunning && timeLeft > 0) {
                delay(1000)
                timeLeft--
                updateNotification()
                sendBroadcast(Intent("TIMER_UPDATE").apply {
                    putExtra("timeLeft", timeLeft)
                })
            }
            stopSelf()
        }
        startForeground(notificationId, createNotification())
    }

    private fun stopTimer() {
        isTimerRunning = false
        timerJob?.cancel()
        stopForeground(true)
        stopSelf()
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Timer Running")
            .setContentText("Time left: ${timeLeft} seconds")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }

    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_TIME_LEFT = "EXTRA_TIME_LEFT"

        @RequiresApi(Build.VERSION_CODES.O)
        fun startService(context: Context, timeLeft: Int) {
            Intent(context, TimerService::class.java).apply {
                this.action = ACTION_START
                this.putExtra(EXTRA_TIME_LEFT, timeLeft)
                context.applicationContext.startService(this)
            }

        }

        fun stopService(context: Context) {
            Log.d(CHECKINGSERVICESLOGS, "Stopping")
            val stopIntent = Intent(context, TimerService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(stopIntent)
        }
    }
}
