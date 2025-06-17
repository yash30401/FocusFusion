package com.yash.focusfusion.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime

object GreetingUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalTime.getGreetingText(): String {
        return if (this.isAfter(LocalTime.of(0, 0, 0))
            && this.isBefore(LocalTime.of(12, 0, 0))
        ) {
            "Good Morning"
        } else if (this.isAfter(LocalTime.of(12, 0, 0)) && this.isBefore(LocalTime.of(17, 0, 0))) {
            "Good Morning"
        } else {
            "Good Evening"
        }
    }
}