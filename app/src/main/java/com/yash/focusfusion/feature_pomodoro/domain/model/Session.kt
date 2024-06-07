package com.yash.focusfusion.feature_pomodoro.domain.model

data class Session(
    val startTime: Long,
    val endTime: Long,
    val duration: Int,
    val taskTag: TaskTag
)
