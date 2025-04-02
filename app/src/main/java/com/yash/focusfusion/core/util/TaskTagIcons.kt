package com.yash.focusfusion.core.util

import androidx.annotation.DrawableRes
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag

object TaskTagIcons {

    @DrawableRes
    fun getIconRes(task:TaskTag):Int{
        return when(task){
            TaskTag.WORK -> R.drawable.work
            TaskTag.STUDY ->  R.drawable.books
            TaskTag.EXERCISE -> R.drawable.gym
            TaskTag.SPORT -> R.drawable.person_with_ball
            TaskTag.RELAX -> R.drawable.sleeping_accommodation
            TaskTag.ENTERTAINMENT -> R.drawable.entertainment
            TaskTag.SOCIAL -> R.drawable.social
            TaskTag.OTHER -> R.drawable.other
        }
    }
}