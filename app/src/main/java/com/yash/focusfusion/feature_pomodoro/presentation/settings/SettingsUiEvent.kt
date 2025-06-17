package com.yash.focusfusion.feature_pomodoro.presentation.settings

sealed class SettingsUiEvent {
    data class onNameChanged(val name: String) : SettingsUiEvent()
    data class onTimerChange(val time: Int) : SettingsUiEvent()
}