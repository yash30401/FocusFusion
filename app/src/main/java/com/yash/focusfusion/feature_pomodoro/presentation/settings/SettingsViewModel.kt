package com.yash.focusfusion.feature_pomodoro.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetFocusTimeUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetThemeModeUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.GetUserNameUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.SaveFocusTimeUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.SaveThemeModeUseCase
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.SaveUserNameUseCase
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.OnboardingNavigationEvent
import com.yash.focusfusion.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveFocusTimeUseCase: SaveFocusTimeUseCase,
    private val getFocusTimeUseCase: GetFocusTimeUseCase,
    private val saveThemeModeUseCase: SaveThemeModeUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    val themeState = getThemeModeUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    // Track ongoing operations to prevent concurrent modifications
    private var saveNameJob: Job? = null
    private var saveTimeJob: Job? = null
    private var saveThemeJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() = viewModelScope.launch {
        try {

            val userName = getUserNameUseCase()
            val focusTime = getFocusTimeUseCase().first()

            Log.d(
                "FOCUSTIMEINTERVAL", focusTime.toString()
            )

            _uiState.value = _uiState.value.copy(
                name = userName,
                timeInterval = focusTime,
                isLoading = false
            )

        } catch (e: Exception) {
            handleError("Failed to load settings: ${e.message}")
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.onNameChanged -> {
                saveNameJob?.cancel()  // Cancel previous operation
                saveNameJob = handleNameChange(event.name)
            }

            is SettingsUiEvent.onTimerChange -> {
                saveTimeJob?.cancel()
                saveTimeJob = handleTimeChange(event.time)
            }

            SettingsUiEvent.HideNameChangeDialog -> {
                _uiState.value = _uiState.value.copy(
                    isNameDialogvisible = false
                )
            }

            SettingsUiEvent.ShowNameChangeDialog -> {
                _uiState.value = _uiState.value.copy(
                    isNameDialogvisible = true
                )
            }

            SettingsUiEvent.HidePrivacyPolicyDialog -> _uiState.value = _uiState.value.copy(
                isPrivacyPolicyDialogVisible = false
            )

            SettingsUiEvent.ShowPrivacyPolicyDialog -> _uiState.value = _uiState.value.copy(
                isPrivacyPolicyDialogVisible = true
            )

            is SettingsUiEvent.OnThemeChanged -> {
                saveThemeJob?.cancel()
                saveThemeJob = handleThemeChange(event.theme)
            }
        }
    }

    private fun handleNameChange(name: String) = viewModelScope.launch {
        val trimmedName = name.trim()

        // Update UI immediately for better UX
        _uiState.value = _uiState.value.copy(
            name = name, // Show what user typed
            isLoading = true,
            error = null
        )

        try {
            if (validateName(trimmedName)) {
                saveUserNameUseCase(name)
                _uiState.value = _uiState.value.copy(
                    name = trimmedName,
                    isLoading = false,
                    isNameDialogvisible = false
                )
            } else {
                handleError("Name must be at least 2 characters long")
            }
        } catch (e: Exception) {
            // Revert to previous valid state
            val previousName = getUserNameUseCase()
            _uiState.value = _uiState.value.copy(
                name = previousName,
                isLoading = false
            )
            handleError("Failed to save name: ${e.message}")
        }
    }

    private fun handleTimeChange(time: Int) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        try {
            saveFocusTimeUseCase(time)

            // Verify the save was successful
            val savedTime = getFocusTimeUseCase().first()
            _uiState.value = _uiState.value.copy(
                timeInterval = savedTime,
                isLoading = false
            )

        } catch (e: Exception) {
            handleError("Failed to save focus time: ${e.message}")
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

    private fun handleThemeChange(mode: ThemeMode) = viewModelScope.launch {
        saveThemeModeUseCase.invoke(mode)
    }

    override fun onCleared() {
        super.onCleared()
        saveNameJob?.cancel()
        saveTimeJob?.cancel()
        saveThemeJob?.cancel()
    }

}