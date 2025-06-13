package com.yash.focusfusion.feature_pomodoro.domain.use_case.datastore_use_case

data class DatastoreUseCases(
    val getTimeLeftUseCase: GetTimeLeftUseCase,
    val getExtraTimeUseCase: GetExtraTimeUseCase,
    val getContinueTimerUseCase: GetContinueTimerUseCase,
    val getCancelTimeUseCase: GetCancelTimeUseCase,
    val saveTimeLeftUseCase: SaveTimeLeftUseCase,
    val saveExtraTimeUseCase: SaveExtraTimeUseCase,
    val saveContinueTimerUseCase: SaveContinueTimerUseCase,
    val saveCancelTimeLeftUseCase: SaveCancelTimeLeftUseCase,
    val saveUserNameUseCase: SaveUserNameUseCase,
    val getUserNameUseCase: GetUserNameUseCase,
    val onboardingUseCase: CompleteOnboardingUseCase
)