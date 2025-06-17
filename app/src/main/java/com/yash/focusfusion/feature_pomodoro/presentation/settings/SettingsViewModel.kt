package com.yash.focusfusion.feature_pomodoro.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetUserNameUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.SaveUserNameUseCase
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.OnboardingNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.onNameChanged -> {
                handleNameChange(event.name)
            }

            is SettingsUiEvent.onTimerChange -> {}
        }
    }

    private fun handleNameChange(name: String) = viewModelScope.launch {
        try {
            if (validateName(name)) {
                saveUserNameUseCase(name)
            }
            _uiState.value = _uiState.value.copy(
                name = getUserNameUseCase(),
                isLoading = false,
            )
        } catch (e: Exception) {
            handleError("An unexpected error occurred")
        }
    }

    private fun validateName(name: String): Boolean {
        return name.trim().length >= 2
    }

    private fun handleError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = message
        )
    }

}