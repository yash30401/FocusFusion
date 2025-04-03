package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent

sealed class HomeEvent {
    data class WeekEvent(
        val startTimestamp:Long,
        val endTimestamp:Long
    ) : HomeEvent()

    data class LastWeekEvent(
        val startTimestamp:Long,
        val endTimestamp:Long
    ):HomeEvent()

    data class todaysHours(val date:Long):HomeEvent()
}