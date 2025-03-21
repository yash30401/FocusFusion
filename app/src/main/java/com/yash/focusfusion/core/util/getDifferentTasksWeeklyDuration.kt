package com.yash.focusfusion.core.util

import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag

fun getDifferentTasksWeekluDuration(sessions: List<Session>): List<Pair<TaskTag, Int>> {
    val hashMapOfTasks = HashMap<TaskTag, Int>()

    for (i in 0..sessions.size) {
        val key = sessions[i].taskTag
        if (hashMapOfTasks.containsKey(key)) {
            val currentDuration = hashMapOfTasks.getValue(key)
            val addDuration = sessions[i].duration + currentDuration
            hashMapOfTasks.put(key, addDuration)
        } else {
            hashMapOfTasks.put(key, sessions[i].duration)
        }
    }
    return hashMapOfTasks.map {
        Pair(it.key, it.value)
    }
}