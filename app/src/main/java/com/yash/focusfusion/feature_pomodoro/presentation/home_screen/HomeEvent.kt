package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsEvent

sealed class HomeEvent {
    data class WeekEvent(
        val startDate: String,
        val endDate: String,
        val month: String,
        val year: String,
    ) : HomeEvent()

    data class todaysHours(val date:Long):HomeEvent()
}