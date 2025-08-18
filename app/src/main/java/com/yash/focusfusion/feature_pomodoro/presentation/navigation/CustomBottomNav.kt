package com.yash.focusfusion.feature_pomodoro.presentation.navigation

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
import com.yash.focusfusion.ui.theme.FocusFusionTheme

@Composable
fun CustomBottomNav(
    navController: NavController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
) {
    val currentRoute = currentRoute(navController)

    // Interaction source to track button presses for the click animation
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animate the scale of the FAB when pressed
    val fabScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "fabScale"
    )

    // This will control the FAB's vertical entry animation
    val fabOffsetY = remember { Animatable(100f) }

    LaunchedEffect(Unit) {
        fabOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 400, delayMillis = 100)
        )
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp)
                .height(90.dp)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surface)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                val iconTint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                Column(

                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // Disable ripple for a cleaner look
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = if (selected) painterResource(item.selectedIcon) else painterResource(
                            item.unselectedIcon,
                        ),
                        contentDescription = "NavItems",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // --- Animation Wrapper for the FAB ---
        AnimatedVisibility(
            visible = currentRoute != BottomNavItem.Timer.route, // Hide FAB on the Timer screen
            enter = slideInVertically { it } + fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                // Apply the entry animation offset and the static "half outside" offset
                .offset(y = fabOffsetY.value.dp)
                .offset(y = (-26).dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (currentRoute != BottomNavItem.Timer.route) {
                        navController.navigate(BottomNavItem.Timer.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                },
                interactionSource = interactionSource, // Attach the interaction source
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .scale(fabScale) // Apply the click animation scale
                    .size(56.dp)
                    .shadow(
                        elevation = 5.dp,
                        shape = CircleShape,
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    )
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Session",
                    modifier = Modifier.size(32.dp)
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

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CustomBottomNavPreview() {
    val navController = rememberNavController()

    FocusFusionTheme {
        CustomBottomNav(
            navController,
            items = listOf(
                BottomNavItem.Home,
                BottomNavItem.Profile
            )
        )
    }

}