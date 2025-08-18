package com.yash.focusfusion

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.yash.focusfusion.core.util.TaskTagMap
import com.yash.focusfusion.feature_pomodoro.data.local.datastore.DatastoreManager
import com.yash.focusfusion.feature_pomodoro.domain.model.TaskTag
import com.yash.focusfusion.feature_pomodoro.presentation.home_screen.HomeScreen
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsScreen
import com.yash.focusfusion.feature_pomodoro.presentation.insights.InsightsViewModel
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.CustomBottomNav
import com.yash.focusfusion.feature_pomodoro.presentation.navigation.model.BottomNavItem
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.onBoardingScreen
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens.OnBoardingScreen1
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens.OnBoardingScreen2
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens.OnBoardingScreen3
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens.OnBoardingScreen4
import com.yash.focusfusion.feature_pomodoro.presentation.settings.SettingsScreen
import com.yash.focusfusion.feature_pomodoro.presentation.settings.SettingsViewModel
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerScreen
import com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.TimerSharedViewModel
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var timeLeft: Long by mutableStateOf(1500000L) // Default to 25:00
    private var cancelTimeLeft: Long by mutableStateOf(10000L)

    private var taskTag: TaskTag by mutableStateOf(TaskTag.STUDY)

    private lateinit var dataStoreManager: DatastoreManager
    private var extraTime: Int by mutableStateOf(0)
    private var isTimerRunning: Boolean by mutableStateOf(false)
    private val timerSharedViewModel: TimerSharedViewModel by viewModels()
    private var isOnBoardingCompleted: Boolean = false
    private var focusTime: Int = 1

    private val settingsViewModel: SettingsViewModel by viewModels()

    private val timerUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val isFinished = intent?.getBooleanExtra("IS_FINISHED", false) ?: false

            timeLeft = intent?.getLongExtra("TIME_LEFT", 0L) ?: 0L
            cancelTimeLeft = intent?.getLongExtra("CANCEL_TIME_LEFT", 0L) ?: 0L
            Log.d("CANCEL_TIME_MAIN_ACITIVITY", cancelTimeLeft.toString())
            extraTime = intent?.getIntExtra("EXTRA_TIME", 0) ?: 0
            isTimerRunning = if (isFinished) false else timeLeft > 0

            val taskToTaskTag = intent?.getStringExtra("TASK_TAG")

            taskTag = TaskTagMap.mapTaskTagString(taskToTaskTag.toString())

            lifecycleScope.launch(Dispatchers.IO) {
                dataStoreManager.saveTimeLeft(timeLeft)
                dataStoreManager.saveExtraTime(extraTime)
                dataStoreManager.saveContinueTimer(isTimerRunning)
                dataStoreManager.saveTaskTag(taskTag.toString())

                timerSharedViewModel.updateTimeLeft(timeLeft)
                timerSharedViewModel.updateExtraTime(extraTime)
                timerSharedViewModel.updateIsRunning(isTimerRunning)
                timerSharedViewModel.updateWorkTag(taskTag)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                dataStoreManager.saveCancelTimeLeft(cancelTimeLeft)
                timerSharedViewModel.updateCancelTimeLeft(cancelTimeLeft)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        dataStoreManager = DatastoreManager(this)
        val timerLeft = TimeUnit.MINUTES.toMillis(5)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        registerReceiver(
            timerUpdateReceiver, IntentFilter("TIMER_UPDATE"),
            RECEIVER_EXPORTED
        )

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.timeLeftFlow.collect { savedTimeLeft ->
                timeLeft = savedTimeLeft
                timerSharedViewModel.updateTimeLeft(savedTimeLeft)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.cancelTimeFlow.collect { savedCancelTimeLeft ->
                cancelTimeLeft = savedCancelTimeLeft
                timerSharedViewModel.updateCancelTimeLeft(savedCancelTimeLeft)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.extraTime.collect { savedExtraTime ->
                extraTime = savedExtraTime
                timerSharedViewModel.updateExtraTime(savedExtraTime)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.continueTimerFlow.collect { shouldContinue ->
                isTimerRunning = shouldContinue
                timerSharedViewModel.updateIsRunning(shouldContinue)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.taskTag.collect { taskTag ->
                timerSharedViewModel.updateWorkTag(TaskTagMap.mapTaskTagString(taskTag))
                Log.d("TASKTAGFIND", taskTag.toString())
            }
        }


        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.focusTime.collect {
                Log.d("PROGRESSSTARTTIME", "Main Ac:- ${it}")
                focusTime = it
                withContext(Dispatchers.Main) {
                    timerSharedViewModel.updateFocusTime(it)
                }
            }

        }

        setContent {

            val selectedTheme by settingsViewModel.themeState.collectAsStateWithLifecycle()

            FocusFusionTheme(selectedTheme = selectedTheme) {
//                   TimerScreen(context = this@MainActivity, timerSharedViewModel = timerSharedViewModel)
                val navController = rememberNavController()

                var isLoading by remember { mutableStateOf(true) }

                var startDestination by remember { mutableStateOf("OnBoardingScreen1") }
                val userName by dataStoreManager.userNameFlow.collectAsState(initial = "")

                LaunchedEffect(Unit) {
                    isOnBoardingCompleted = dataStoreManager.onBoardingCompletedFlow.first()
                    val continueTimer = dataStoreManager.continueTimerFlow.first()

                    startDestination = when {
                        continueTimer -> BottomNavItem.Timer.route
                        !isOnBoardingCompleted -> "OnBoardingScreen1"
                        else -> BottomNavItem.Home.route
                    }

                    isLoading = false
                }



                Scaffold(bottomBar = {

                    // Observe the current route
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    if (currentRoute != "OnBoardingScreen1" && currentRoute?.contains(
                            "OnBoarding",
                            false
                        ) != true
                    ) {
                        CustomBottomNav(
                            navController = navController,
                            items = listOf(BottomNavItem.Home, BottomNavItem.Profile)
                        )
                    }
                }
                ) { innerPadding ->

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier
                                .padding(innerPadding)

                        )
                        {
                            composable(
                                "OnBoardingScreen1"
                            ) {
//                                onBoardingScreen({
//                                    navController.navigate(BottomNavItem.Home.route) {
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
//                                })

                                OnBoardingScreen1(navController)
                            }

                            composable("OnBoardingScreen2") {
                                OnBoardingScreen2(navController)
                            }

                            composable("OnBoardingScreen3") {
                                OnBoardingScreen3(navController)
                            }

                            composable("OnBoardingScreen4") {
                                OnBoardingScreen4(navController)
                            }

                            composable("OnBoardingScreen") {
                                onBoardingScreen({
                                    navController.navigate(BottomNavItem.Home.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                            }

                            composable(
                                BottomNavItem.Home.route,
                                enterTransition = {
                                    when (initialState.destination.route) {
                                        "OnBoardingScreen1" -> slideInVertically(
                                            initialOffsetY = { it },
                                            animationSpec = tween(
                                                durationMillis = 400,
                                                easing = EaseInOut
                                            )
                                        ) + fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )

                                        BottomNavItem.Profile.route -> slideInHorizontally(
                                            initialOffsetX = { -it },
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )

                                        BottomNavItem.Timer.route -> slideInHorizontally(
                                            initialOffsetX = { -it },
                                            animationSpec = tween(
                                                durationMillis = 400,
                                                easing = EaseInOut
                                            )
                                        )

                                        else -> fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                },
                                exitTransition = {
                                    when (targetState.destination.route) {
                                        BottomNavItem.Profile.route -> slideOutHorizontally(
                                            targetOffsetX = { -it },
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )

                                        BottomNavItem.Timer.route -> slideOutHorizontally(
                                            targetOffsetX = { -it },
                                            animationSpec = tween(
                                                durationMillis = 400,
                                                easing = EaseInOut
                                            )
                                        )

                                        else -> fadeOut(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                }
                            ) {
                                HomeScreen(navController, userName)
                            }
                            composable(
                                BottomNavItem.Profile.route,
                                enterTransition = {
                                    when (initialState.destination.route) {
                                        BottomNavItem.Home.route -> slideInHorizontally(
                                            initialOffsetX = { it },
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )

                                        BottomNavItem.Timer.route -> slideInHorizontally(
                                            initialOffsetX = { it },
                                            animationSpec = tween(
                                                durationMillis = 400,
                                                easing = EaseInOut
                                            )
                                        )

                                        else -> fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                },
                                exitTransition = {
                                    when (targetState.destination.route) {
                                        BottomNavItem.Home.route -> slideOutHorizontally(
                                            targetOffsetX = { it },
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )

                                        BottomNavItem.Timer.route -> slideOutHorizontally(
                                            targetOffsetX = { it },
                                            animationSpec = tween(
                                                durationMillis = 400,
                                                easing = EaseInOut
                                            )
                                        )

                                        else -> fadeOut(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                }
                            ) {
                                SettingsScreen()
                            }
                            composable(
                                "Insights"
                            ) {
                                InsightsScreen()
                            }
                            composable(
                                BottomNavItem.Timer.route,
                                enterTransition = {
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = EaseInOut
                                        )
                                    ) + fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 400,
                                            easing = EaseInOut
                                        )
                                    )
                                },
                                exitTransition = {
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(
                                            durationMillis = 400,
                                            easing = EaseInOut
                                        )
                                    ) + fadeOut(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = EaseInOut
                                        )
                                    )
                                },
                                popExitTransition = {
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(
                                            durationMillis = 400,
                                            easing = EaseInOut
                                        )
                                    ) + fadeOut(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = EaseInOut
                                        )
                                    )
                                }
                            ) {
                                TimerScreen(
                                    LocalContext.current, timerSharedViewModel,
                                )
                            }
                        }
                    }
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerUpdateReceiver)
    }
}
