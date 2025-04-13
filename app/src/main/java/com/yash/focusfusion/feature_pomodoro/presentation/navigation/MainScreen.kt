package com.yash.focusfusion.feature_pomodoro.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.HomeScreen
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsScreen
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.model.BottomNavItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            CustomBottomNav(
                navController = navController,
                items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Profile
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Profile.route) { InsightsScreen() }
        }
    }
}