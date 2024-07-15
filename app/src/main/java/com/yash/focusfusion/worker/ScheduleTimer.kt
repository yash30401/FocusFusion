package com.yash.focusfusion.worker

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.yash.focusfusion.core.util.Constants.KEY_TIMER_DURATION
import com.yash.focusfusion.core.util.Constants.TIMER_WORK_TAG

object ScheduleTimer {

    fun startTimer(context: Context, durationInSeconds: Int) {
        val inputData = Data.Builder()
            .putInt(KEY_TIMER_DURATION, durationInSeconds)
            .build()

        val timeWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<TimerWorker>()
            .setInputData(inputData)
            .addTag(TIMER_WORK_TAG)
            .build()

        val work = WorkManager.getInstance(context).enqueue(timeWorkRequest)

    }
    fun cancelTimer(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TIMER_WORK_TAG)
    }
}