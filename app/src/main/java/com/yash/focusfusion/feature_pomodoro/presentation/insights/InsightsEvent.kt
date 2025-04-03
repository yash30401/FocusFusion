package com.yash.focusfusion.feature_pomodoro.presentation.insights

sealed class InsightsEvent {
    data class DayEvent(val date: Long) : InsightsEvent()

    data class WeekEvent(
        val startTimestamp:Long,
        val endTimestamp:Long
    ) : InsightsEvent()

    data class MonthEvent(val month: String, val year: String) : InsightsEvent()

    data class YearEvent(val year: String) : InsightsEvent()
}