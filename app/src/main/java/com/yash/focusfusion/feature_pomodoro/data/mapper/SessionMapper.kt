package com.yash.focusfusion.feature_pomodoro.data.mapper

import com.yash.focusfusion.feature_pomodoro.data.local.entity.SessionEntity
import com.yash.focusfusion.feature_pomodoro.domain.model.Session

fun SessionEntity.toSession(): Session {
    return Session(time, duration, taskTag)
}

fun Session.toSessionEntity(): SessionEntity {
    return SessionEntity(
        time = time,
        duration = duration,
        taskTag = taskTag
    )
}