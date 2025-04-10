package com.yash.focusfusion.feature_pomodoro.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.model.BottomNavItem

@Composable
fun CustomBottomNav(
    navController: NavController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
) {
    val currentRoute = currentRoute(navController)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xffF8F8F8))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    }
                    .padding(8.dp)
            ) {
                Image(
                    painter = if (selected) painterResource(item.selectedIcon) else painterResource(
                        item.unselectedIcon,
                    ),
                    contentDescription = "NavItems",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomBottomNavPreview() {
    val navController = rememberNavController()

    CustomBottomNav(
        navController,
        items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Profile
        )
    )

}