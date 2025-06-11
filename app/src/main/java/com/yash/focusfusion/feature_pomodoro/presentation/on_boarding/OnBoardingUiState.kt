package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding

data class OnBoardingUiState(
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNameValid: Boolean = true,
    val navigationEvent: OnboardingNavigationEvent? = null
)