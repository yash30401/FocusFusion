package com.yash.focusfusion.feature_pomodoro.presentation.home_screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.focusfusion.R
import com.yash.focusfusion.core.util.getListOfWeeksNameWithDuration
import com.yash.focusfusion.core.util.getTaskTagIconRes
import com.yash.focusfusion.core.util.getTimeListInFormattedWayWithDuration
import com.yash.focusfusion.core.util.getTotalDurationWeeklyUsingHashMap
import com.yash.focusfusion.feature_pomodoro.domain.model.Session
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.GreetingHead
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.HomeScreenWaveLineChart
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeDistributionCard
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components.TimeRange
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsScreen
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.CustomBottomNav
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.model.BottomNavItem
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.collections.component1
import kotlin.collections.component2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val lastWeekSessionState by homeScreenViewModel.lastWeekSessions.collectAsState()
    val weeklySessionState by homeScreenViewModel.weeklySessions.collectAsState()
    val currentDayTotalHours by homeScreenViewModel.currentDayHours.collectAsState()
    var minutesFocused by remember { mutableStateOf<List<Float>>(emptyList()) }

    val streak by homeScreenViewModel.streak.collectAsStateWithLifecycle()

    Log.d("STREAKWORK", streak.toString())


    Log.d("TOTALTIMETODAY", currentDayTotalHours.toString())
    val todaysDate = remember { LocalDate.now() }

    val dateFormat = SimpleDateFormat("dd,MMM yyyy")
    var date: Date? by remember {
        mutableStateOf(
            dateFormat.parse(
                todaysDate.format(DateTimeFormatter.ofPattern("dd,MMM yyyy"))
            )
        )
    }

    val lastWeekStartDate = LocalDate.now()
        .minusWeeks(1)
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    val lastWeekEndDate = lastWeekStartDate.plusDays(6)

    val lastWeekStartTimestamp = lastWeekStartDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val lastWeekEndTimestamp = lastWeekEndDate
        .atTime(23, 59, 59)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val currentWeekStartDate = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    val currentWeekEndDate = currentWeekStartDate.plusDays(6)

    val currentWeekStartTimestamp = currentWeekStartDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val currentWeekEndTimestamp = currentWeekEndDate
        .atTime(23, 59, 59)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()


    fetchLastWeekSessions(homeScreenViewModel, lastWeekStartTimestamp, lastWeekEndTimestamp)

    initHomeViewModel(
        homeScreenViewModel,
        currentWeekStartTimestamp,
        currentWeekEndTimestamp,
        date?.time
    )

    minutesFocused = getMappedDataForChart(weeklySessionState)


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())  // Add scroll modifier
        ) {
            GreetingHead(userName, modifier = Modifier)
            HomeScreenWaveLineChart(
                streak,
                navController,
                minutesFocused,
                TimeRange.Week,
                modifier = Modifier.padding(16.dp),
                lineColor = Color(0xff9463ED),
                currentDayTotalHours = TimeUnit.SECONDS.toHours(currentDayTotalHours.toLong())
                    .toInt(),
                strokeWidth = 5f,
                xOffset = 90f,
                waveAmplitude = 1f,
            )
            if (!weeklySessionState.isNullOrEmpty()) {
                Text(
                    text = "Weekly Time Distribution",
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium))
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(max = 2000.dp)
            ) {
                val weeklyTimeDistributionByTag = weeklySessionState.groupBy {
                    it.taskTag
                }.mapValues {
                    it.value.sumOf { it.duration }
                }

                val lastWeekTimeDistributionByTag = lastWeekSessionState.groupBy {
                    it.taskTag
                }.mapValues {
                    it.value.sumOf { it.duration }
                }

                val listOfTotalDurationInWeekByTask =
                    weeklyTimeDistributionByTag.map { it }.toList()
                val listOfTotalDurationInLastWeekByTask =
                    lastWeekTimeDistributionByTag.map { it }.toList()

                itemsIndexed(
                    items = listOfTotalDurationInWeekByTask,
                    key = { index, (taskTag, duration) -> "$taskTag-$duration-${taskTag.hashCode()}" }
                ) { index, (taskTag, duration) ->
                    var isVisible by remember { mutableStateOf(false) }
//
//
                    val lastWeekDuration =
                        listOfTotalDurationInLastWeekByTask.getOrNull(index)?.value ?: 0

                    Log.d("LASTWEEKDURATION", lastWeekDuration.toString())

                    // Animated visibility for sliding in items
                    LaunchedEffect(Unit) {
                        isVisible =
                            true // Trigger the animation once when the item enters composition
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible, enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 1000)
                        ) + fadeIn(animationSpec = tween(durationMillis = 1000))
                    ) {
                        TimeDistributionCard(
                            getTaskTagIconRes(taskTag),
                            taskTag,
                            TimeUnit.SECONDS.toMinutes(duration.toLong()).toInt(),
                            TimeUnit.SECONDS.toMinutes(lastWeekDuration.toLong()).toInt()
                        )
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun fetchLastWeekSessions(
    homeScreenViewModel: HomeScreenViewModel,
    lastWeekStartTimestamp: Long,
    lastWeekEndTimestamp: Long,
) {

    homeScreenViewModel.onEvent(
        HomeEvent.LastWeekEvent(
            lastWeekStartTimestamp,
            lastWeekEndTimestamp
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMappedDataForChart(weeklySessionState: List<Session>): List<Float> {
    val timeListInFormattedWay =
        getTimeListInFormattedWayWithDuration(weeklySessionState)
    val extractedListOfWeekWithDuration = getListOfWeeksNameWithDuration(
        timeListInFormattedWay
    )
    val totalDurationData =
        getTotalDurationWeeklyUsingHashMap(extractedListOfWeekWithDuration)

    return totalDurationData.map {
        TimeUnit.SECONDS.toMinutes(it.value.toLong()).toFloat()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun initHomeViewModel(
    homeScreenViewModel: HomeScreenViewModel,
    currentWeekStartTimestamp: Long,
    currentWeekSEndTimestamp: Long,
    todaysDateInMillis: Long?,
) {
    homeScreenViewModel.onEvent(
        HomeEvent.WeekEvent(
            currentWeekStartTimestamp,
            currentWeekSEndTimestamp
        )
    )

    homeScreenViewModel.onEvent(HomeEvent.todaysHours(todaysDateInMillis ?: 0L))
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    val navController = rememberNavController()

    val weeklySessionState = listOf<Session>(Session(1749576488000, 23, TaskTag.STUDY))
    val lastWeekSessionState = listOf<Session>(Session(1749148088000, 2, TaskTag.STUDY))
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())  // Add scroll modifier
        ) {
            GreetingHead("Yash", modifier = Modifier)
            HomeScreenWaveLineChart(
                2,
                navController,
                listOf(1f, 5f, 2f),
                TimeRange.Week,
                modifier = Modifier.padding(16.dp),
                lineColor = Color(0xff9463ED),
                currentDayTotalHours = TimeUnit.SECONDS.toHours(8.toLong())
                    .toInt(),
                strokeWidth = 5f,
                xOffset = 90f,
                waveAmplitude = 1f,
            )
            if (!weeklySessionState.isNullOrEmpty()) {
                Text(
                    text = "Weekly Time Distribution",
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium))
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(max = 2000.dp)
            ) {
                val weeklyTimeDistributionByTag = weeklySessionState.groupBy {
                    it.taskTag
                }.mapValues {
                    it.value.sumOf { it.duration }
                }

                val lastWeekTimeDistributionByTag = lastWeekSessionState.groupBy {
                    it.taskTag
                }.mapValues {
                    it.value.sumOf { it.duration }
                }

                val listOfTotalDurationInWeekByTask =
                    weeklyTimeDistributionByTag.map { it }.toList()
                val listOfTotalDurationInLastWeekByTask =
                    lastWeekTimeDistributionByTag.map { it }.toList()

                itemsIndexed(
                    items = listOfTotalDurationInWeekByTask,
                    key = { index, (taskTag, duration) -> "$taskTag-$duration-${taskTag.hashCode()}" }
                ) { index, (taskTag, duration) ->
                    var isVisible by remember { mutableStateOf(false) }
//
//
                    val lastWeekDuration =
                        listOfTotalDurationInLastWeekByTask.getOrNull(index)?.value ?: 0

                    Log.d("LASTWEEKDURATION", lastWeekDuration.toString())

                    // Animated visibility for sliding in items
                    LaunchedEffect(Unit) {
                        isVisible =
                            true // Trigger the animation once when the item enters composition
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible, enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 1000)
                        ) + fadeIn(animationSpec = tween(durationMillis = 1000))
                    ) {
                        TimeDistributionCard(
                            getTaskTagIconRes(taskTag),
                            taskTag,
                            TimeUnit.SECONDS.toMinutes(duration.toLong()).toInt(),
                            TimeUnit.SECONDS.toMinutes(lastWeekDuration.toLong()).toInt()
                        )
                    }
                }
            }

        }
    }
}