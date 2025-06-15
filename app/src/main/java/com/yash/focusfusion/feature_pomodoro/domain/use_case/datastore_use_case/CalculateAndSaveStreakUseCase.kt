package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.yash.focusfusion.feature_pomodoro.domain.repository.DatastoreRepository
import com.yash.focusfusion.feature_pomodoro.domain.use_case.session_use_case.GetSessionsForDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CalculateAndSaveStreakUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val getSessionsForDateUseCase: GetSessionsForDateUseCase,
) {



    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        Log.d("STREAKWORK"," Start Cal")

        var currentStreak: Int = datastoreRepository.streak.first()
        Log.d("STREAKWORK"," Current Streak:- ${currentStreak}")
        val todaysDate = Instant.now().toEpochMilli()
//        val yesterdayDate = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()

        val todaysSessions = getSessionsForDateUseCase(todaysDate).first()
        val todaysSessionsSize = todaysSessions.size
//        val yesterdaySessionSize = getSessionsForDateUseCase.invoke(yesterdayDate).count()
        Log.d("STREAKWORK"," SessionSize:- ${todaysSessionsSize}")

        if (todaysSessionsSize >= 1) {
            currentStreak++
            datastoreRepository.saveStreakCount(currentStreak)
        } else {
            datastoreRepository.saveStreakCount(0)
        }
    }

}