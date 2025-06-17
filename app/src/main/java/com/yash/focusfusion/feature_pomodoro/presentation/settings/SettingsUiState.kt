package com.yash.focusfusion.feature_pomodoro.presentation.settings

data class SettingsUiState(
    val name: String = "",
    val timeInterval: Int = 25,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNameValid: Boolean = true,
)
