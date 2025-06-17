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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CalculateAndSaveStreakUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val getSessionsForDateUseCase: GetSessionsForDateUseCase,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        Log.d("STREAKWORK", "Starting streak recalculation...")

        var calculatedStreak = 0
        var checkDate = LocalDate.now()

        // First, check if the user has done any sessions today.
        // If not, the streak is based on days up to and including yesterday.
        val todaysSession = getSessionsForDateUseCase.invoke(
            checkDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        ).first()

        if (todaysSession.isEmpty()) {
            // If no sessions today yet, the streak is for consecutive days ending yesterday.
            // So we start our backward check from yesterday.
            checkDate = checkDate.minusDays(1)
        }

        while (true) {
            val dateInMillis =
                checkDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val sessionsOnDate = getSessionsForDateUseCase.invoke(dateInMillis).first()

            if (sessionsOnDate.isNotEmpty()) {
                // Found a day with sessions, increment streak and check the previous day.
                calculatedStreak++
                checkDate = checkDate.minusDays(1)
            } else {
                break
            }

            val oldStreak = datastoreRepository.streak.first()
            if (oldStreak != calculatedStreak) {
                datastoreRepository.saveStreakCount(calculatedStreak)
                Log.d(
                    "STREAKWORK",
                    "Streak recalculated. Old value: $oldStreak, New value: $calculatedStreak"
                )
            } else {
                Log.d("STREAKWORK", "Streak is up-to-date. Value: $calculatedStreak")
            }

        }
    }

}