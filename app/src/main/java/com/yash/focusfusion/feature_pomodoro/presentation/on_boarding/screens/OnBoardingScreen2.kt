package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.FeatureList
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.HorizontalDial
import com.yash.focusfusion.feature_pomodoro.presentation.settings.SettingsUiEvent
import com.yash.focusfusion.feature_pomodoro.presentation.settings.SettingsViewModel
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import kotlinx.coroutines.delay

@Composable
fun OnBoardingScreen2(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    modifier: Modifier = Modifier,
) {

    val hapticFeedback = LocalHapticFeedback.current

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isVisible = !isVisible
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                lineHeight = 40.sp,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Set Your\n")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Session")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(" Time")
                    }
                }

            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "How much time do you want to spend in one focus session?",
                fontFamily = FontFamily(listOf(Font(R.font.roboto_regular))),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(70.dp))

            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { it / 2 },
                    animationSpec = tween(1500)
                ) + fadeIn(
                    animationSpec = tween(1000)
                )
            ) {
                HorizontalDial {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    settingsViewModel.onEvent(
                        event = SettingsUiEvent.onTimerChange(
                            it
                        )
                    )
                }
            }

        }

        Button(
            onClick = {
                navController.navigate("OnBoardingScreen3") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreen2Prev() {
    val navController = rememberNavController()
    FocusFusionTheme {
        val hapticFeedback = LocalHapticFeedback.current

        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(1000)
            isVisible = !isVisible
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    lineHeight = 40.sp,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                    FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                                fontSize = 40.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Set Your\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                    FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                                fontSize = 40.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Session")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                    FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                                fontSize = 40.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(" Time")
                        }
                    }

                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "How much time do you want to spend in one focus session?",
                    fontFamily = FontFamily(listOf(Font(R.font.roboto_regular))),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(70.dp))

                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        initialOffsetX = { it / 2 },
                        animationSpec = tween(2000)
                    ) + fadeIn(
                        animationSpec = tween(1000)
                    )
                ) {
                    HorizontalDial {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                        settingsViewModel.onEvent(
//                            event = SettingsUiEvent.onTimerChange(
//                                it
//                            )
//                        )
                    }
                }

            }

            Button(
                onClick = {
                    navController.navigate("OnBoardingScreen3") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color =MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}