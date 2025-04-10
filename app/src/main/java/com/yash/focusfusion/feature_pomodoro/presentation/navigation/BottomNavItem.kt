package com.yash.focusfusion.feature_pomodoro.presentation.navigation

import androidx.annotation.DrawableRes
import com.yash.focusfusion.R

sealed class BottomNavItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String,
) {
    object Home : BottomNavItem(
        "Home",
        R.drawable.home_selected,
        R.drawable.home_unselected,
        "Home"
    )

    object Profile:BottomNavItem(
        "Profile",
        R.drawable.profile_selected,
        R.drawable.profile_unselected,
        "Profile"
    )
}