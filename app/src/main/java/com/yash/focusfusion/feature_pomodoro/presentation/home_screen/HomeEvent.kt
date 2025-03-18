package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

sealed class HomeEvent {
    data class DayEvent(val date:Long):HomeEvent()
}