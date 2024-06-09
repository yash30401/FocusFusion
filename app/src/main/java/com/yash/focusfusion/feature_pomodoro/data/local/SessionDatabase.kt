package com.yash.focusfusion.feature_pomodoro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yash.focusfusion.feature_pomodoro.data.local.SessionDao
import com.yash.focusfusion.feature_pomodoro.data.local.entity.SessionEntity

@Database(entities = [SessionEntity::class], version = 1)
abstract class SessionDatabase:RoomDatabase() {

    abstract val dao:SessionDao

}