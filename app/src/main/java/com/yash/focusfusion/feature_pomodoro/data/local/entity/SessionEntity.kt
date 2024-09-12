package com.yash.focusfusion.feature_pomodoro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id:Int= 0,
    val time:Long,
    val duration:Int,
    val taskTag:TaskTag
)
