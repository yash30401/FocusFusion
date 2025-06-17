package com.yash.focusfusion.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime

object GreetingUtil {



        @RequiresApi(Build.VERSION_CODES.O)
        private fun LocalTime.getGreetingText(): String {
            return when {
                this.isBefore(LocalTime.NOON) -> "Good Morning"
                this.isBefore(LocalTime.of(17, 0)) -> "Good Afternoon"
                else -> "Good Evening"
            }
        }

        /**
         * Optional: Get greeting based on current time.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentGreeting(): String {
            return LocalTime.now().getGreetingText()
        }

}