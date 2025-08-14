package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.CompleteOnboardingUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetUserNameUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.SaveUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val onboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    fun onEvent(event: OnBoardingUiEvent) {
        when (event) {
            OnBoardingUiEvent.OnGetStartedClicked -> {
                handleGetStarted()
            }

            is OnBoardingUiEvent.OnNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    userName = event.name,
                    isNameValid = validateName(event.name),
                    error = null
                )
            }

            OnBoardingUiEvent.OnNavigationEventConsumed -> _uiState.value =
                _uiState.value.copy(navigationEvent = null)
        }
    }

    private fun validateName(name: String): Boolean {
        return name.trim().length >= 2
    }

    private fun handleGetStarted() = viewModelScope.launch {
        val currentState = _uiState.value
        val trimmedName = currentState.userName.trim()

        if (!validateName(trimmedName)) {
            _uiState.value = currentState.copy(
                isNameValid = false,
                error = "Please enter a valid name (at least 2 characters)"
            )
            return@launch
        }

        _uiState.value = currentState.copy(isLoading = true, error = null)

        try {
            saveUserNameUseCase(trimmedName)
            onboardingUseCase()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                navigationEvent = OnboardingNavigationEvent.NavigateToMain
            )
        } catch (e: Exception) {
            handleError("An unexpected error occurred")
        }
    }

    private fun handleError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = message
        )
    }

}