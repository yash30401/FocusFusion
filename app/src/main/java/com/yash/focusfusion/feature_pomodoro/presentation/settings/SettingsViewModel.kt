package com.yash.focusfusion.feature_pomodoro.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case.*
import com.yash.focusfusion.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
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
    private val saveIsSessionEndSoundEnabledUseCase: SaveIsSessionEndSoundEnabledUseCase,
    private val getIsSessionEndSoundEnabledUseCase: GetIsSessionEndSoundEnabledUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    val themeState: StateFlow<ThemeMode> = getThemeModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val soundEnabledState: StateFlow<Boolean> = getIsSessionEndSoundEnabledUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Track ongoing operations
    private var saveNameJob: Job? = null
    private var saveTimeJob: Job? = null
    private var saveThemeJob: Job? = null
    private var saveSoundJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() = viewModelScope.launch {
        try {
            val userName = getUserNameUseCase()
            val focusTime = getFocusTimeUseCase().first()

            _uiState.update {
                it.copy(
                    name = userName,
                    timeInterval = focusTime,
                    isLoading = false,
                )
            }
        } catch (e: Exception) {
            handleError("Failed to load settings: ${e.message}")
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.onNameChanged -> {
                saveNameJob?.cancel()
                saveNameJob = handleNameChange(event.name)
            }
            is SettingsUiEvent.onTimerChange -> {
                saveTimeJob?.cancel()
                saveTimeJob = handleTimeChange(event.time)
            }
            is SettingsUiEvent.OnThemeChanged -> {
                saveThemeJob?.cancel()
                saveThemeJob = handleThemeChange(event.theme)
            }
            is SettingsUiEvent.OnIsSessionEndSoundEnabled -> {
                saveSoundJob?.cancel()
                saveSoundJob = handleSoundToggle(event.value)
            }
            SettingsUiEvent.HideNameChangeDialog -> _uiState.update { it.copy(isNameDialogvisible = false) }
            SettingsUiEvent.ShowNameChangeDialog -> _uiState.update { it.copy(isNameDialogvisible = true) }
            SettingsUiEvent.HidePrivacyPolicyDialog -> _uiState.update { it.copy(isPrivacyPolicyDialogVisible = false) }
            SettingsUiEvent.ShowPrivacyPolicyDialog -> _uiState.update { it.copy(isPrivacyPolicyDialogVisible = true) }
        }
    }

    private fun handleNameChange(name: String) = viewModelScope.launch {
        val trimmedName = name.trim()

        _uiState.update {
            it.copy(
                name = name, // Show raw input
                isLoading = true,
                error = null
            )
        }

        try {
            if (validateName(trimmedName)) {
                saveUserNameUseCase(trimmedName)
                _uiState.update {
                    it.copy(
                        name = trimmedName,
                        isLoading = false,
                        isNameDialogvisible = false
                    )
                }
            } else {
                handleError("Name must be at least 2 characters long")
            }
        } catch (e: Exception) {
            val previousName = getUserNameUseCase()
            _uiState.update {
                it.copy(
                    name = previousName,
                    isLoading = false
                )
            }
            handleError("Failed to save name: ${e.message}")
        }
    }

    private fun handleTimeChange(time: Int) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            saveFocusTimeUseCase(time)
            val savedTime = getFocusTimeUseCase().first()
            _uiState.update { it.copy(timeInterval = savedTime, isLoading = false) }
        } catch (e: Exception) {
            handleError("Failed to save focus time: ${e.message}")
        }
    }

    private fun handleThemeChange(mode: ThemeMode) = viewModelScope.launch {
        saveThemeModeUseCase(mode)
    }

    private fun handleSoundToggle(value: Boolean) = viewModelScope.launch {
        try {
            saveIsSessionEndSoundEnabledUseCase(value)
        } catch (e: Exception) {
            handleError("Failed to update sound setting: ${e.message}")
        }
    }

    private fun validateName(name: String) = name.length >= 2

    private fun handleError(message: String) {
        _uiState.update { it.copy(isLoading = false, error = message) }
    }

    override fun onCleared() {
        super.onCleared()
        saveNameJob?.cancel()
        saveTimeJob?.cancel()
        saveThemeJob?.cancel()
        saveSoundJob?.cancel()
    }
}
