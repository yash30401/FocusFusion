package com.yash.focusfusion.feature_pomodoro.presentation.insights

sealed class InsightsEvent {
    data class DayEvent(val date: Long) : InsightsEvent()

    data class WeekEvent(
        val startDate: String,
        val endDate: String,
        val month: String,
        val year: String
    ) : InsightsEvent()

    data class MonthEvent(val month: String, val year: String) : InsightsEvent()

    data class YearEvent(val year: String) : InsightsEvent()
}