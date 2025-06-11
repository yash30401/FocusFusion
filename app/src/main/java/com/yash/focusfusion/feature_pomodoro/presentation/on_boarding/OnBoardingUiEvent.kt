package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding

sealed class OnBoardingUiEvent {
    data class OnNameChanged(val name: String) : OnBoardingUiEvent()
    object OnGetStartedClicked : OnBoardingUiEvent()
    object OnNavigationEventConsumed : OnBoardingUiEvent()
}