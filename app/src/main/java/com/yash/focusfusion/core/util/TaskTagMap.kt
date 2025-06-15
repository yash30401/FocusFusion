package com.yash.focusfusion.core.util

import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag

object TaskTagMap {
    fun mapTaskTagString(taskTag: String): TaskTag {
        return when (taskTag) {
            "STUDY" -> TaskTag.STUDY
            "WORK" -> TaskTag.WORK
            "EXERCISE" -> TaskTag.EXERCISE
            "SPORT" -> TaskTag.SPORT
            "RELAX" -> TaskTag.RELAX
            "ENTERTAINMENT" -> TaskTag.ENTERTAINMENT
            "SOCIAL" -> TaskTag.SOCIAL
            "OTHER" -> TaskTag.OTHER

            else -> {
                TaskTag.STUDY
            }
        }
    }
}