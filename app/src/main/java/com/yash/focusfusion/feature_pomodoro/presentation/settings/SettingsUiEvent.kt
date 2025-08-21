package com.yash.focusfusion.feature_pomodoro.presentation.settings

import com.yash.focusfusion.ui.theme.ThemeMode

sealed class SettingsUiEvent {
    data class onNameChanged(val name: String) : SettingsUiEvent()
    data class onTimerChange(val time: Int) : SettingsUiEvent()
    object ShowNameChangeDialog : SettingsUiEvent()
    object HideNameChangeDialog : SettingsUiEvent()
    object ShowPrivacyPolicyDialog : SettingsUiEvent()
    object HidePrivacyPolicyDialog : SettingsUiEvent()

    data class OnThemeChanged(val theme: ThemeMode) : SettingsUiEvent()
    data class OnIsSessionEndSoundEnabled(val value: Boolean) : SettingsUiEvent()
}