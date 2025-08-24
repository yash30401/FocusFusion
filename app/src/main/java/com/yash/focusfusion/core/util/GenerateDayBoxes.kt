package com.yash.focusfusion.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.yash.focusfusion.feature_pomodoro.domain.model.DayBox
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun generateDayBoxes(
    sessionDates: Set<LocalDate>,
    startDate: LocalDate = LocalDate.of(2024, 1, 1),
    endDate: LocalDate = LocalDate.now(),
): List<List<DayBox>> {
    // Ensure startDate aligns to Monday
    var current = startDate.with(DayOfWeek.MONDAY)

    val weeks = mutableListOf<List<DayBox>>()

    while (current <= endDate) {
        val week = (0..6).map { offset ->
            val day = current.plusDays(offset.toLong())
            DayBox(
                date = day,
                hasSession = day in sessionDates
            )
        }
        weeks.add(week)
        current = current.plusWeeks(1)
    }

    return weeks
}