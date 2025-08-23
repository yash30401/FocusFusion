package com.yash.focusfusion.feature_pomodoro.domain.model

import java.time.LocalDate

data class DayBox(
    val date: LocalDate,
    val hasSession:Boolean
)
