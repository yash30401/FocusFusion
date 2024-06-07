package com.yash.focusfusion.feature_pomodoro.data.mapper

import com.yash.focusfusion.feature_pomodoro.data.local.entity.SessionEntity
import com.yash.focusfusion.feature_pomodoro.domain.model.Session

fun SessionEntity.toSession(): Session {
    return Session(startTime, endTime, duration, taskTag)
}

fun Session.toSessionEntity(): SessionEntity {
    return SessionEntity(
        startTime = startTime,
        endTime = endTime,
        duration = duration,
        taskTag = taskTag
    )
}