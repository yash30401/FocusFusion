package com.yash.focusfusion.feature_pomodoro.domain.model

data class Session(
    val time: Long,
    val duration: Int,
    val taskTag: TaskTag
)
