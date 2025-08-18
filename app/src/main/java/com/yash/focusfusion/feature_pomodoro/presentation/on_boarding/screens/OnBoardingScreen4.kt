package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabItem
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimePeriodTabs
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.TimeRange
import com.yash.focusfusion.feature_pomodoro.presentation.insights.components.WaveLineChartWithAxes
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.SessionsList
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.WaveLineChartForOnBoarding
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import kotlinx.coroutines.delay

@Composable
fun OnBoardingScreen4(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    val minutesWorked = listOf(30f, 80f, 50f, 120f, 100f, 40f, 200f)


    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                        append("Track Your\n")
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
                        append("Progress")
                    }
                }

            )

            Spacer(Modifier.height(90.dp))



            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("10 Hours")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_regular))),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    ) {
                        append(" This Week")
                    }
                }
            )
            TimePeriodTabs(1) { }

            WaveLineChartForOnBoarding(
                minutesData = minutesWorked,
                timeRange = TimeRange.Week,
                overallTotalDurationInMinutes = 620,
                lineColor = MaterialTheme.colorScheme.secondary,
                strokeWidth = 5f,
                xOffset = 90f,
                waveAmplitude = 1f,
                onPreviousClick = { dayOrRange, month, year ->

                },
                onNextClick = { dayOrRange, month, year -> }
            )

        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("OnBoardingScreen") {
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
private fun OnBoardingScreen4Prev() {

    val navController = rememberNavController()
    FocusFusionTheme{
        OnBoardingScreen4(navController)
    }
}