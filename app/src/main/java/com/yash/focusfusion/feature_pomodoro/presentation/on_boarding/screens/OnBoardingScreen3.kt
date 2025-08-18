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
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.FeatureList
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.SessionsList
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import kotlinx.coroutines.delay

@Composable
fun OnBoardingScreen3(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }

    val itemVisibility = remember { mutableStateListOf<Boolean>() }
    sessionListItem.forEach { itemVisibility.add(false) }

    val hapticFeedback = LocalHapticFeedback.current


    LaunchedEffect(key1 = Unit) {
        // Trigger the animation for each item with a small delay
        sessionListItem.forEachIndexed { index, _ ->
            delay(500L) // Adjust this delay for a slower or faster cascade
            hapticFeedback.performHapticFeedback(
                HapticFeedbackType.TextHandleMove
            )
            itemVisibility[index] = true
        }
    }

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
                        append("Set Your\n")
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
                        append("Focus")
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
                        append(" Goal")
                    }
                }

            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "How many sessions will you complete each day?",
                fontFamily = FontFamily(listOf(Font(R.font.roboto_regular))),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 17.sp
            )
            Spacer(Modifier.height(10.dp))


            LazyColumn(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .heightIn(max = 1000.dp)

            ) {
                itemsIndexed(sessionListItem) { index, item ->

                    AnimatedVisibility(
                        visible = itemVisibility[index],
                        enter = slideInVertically(
                            initialOffsetY = { -it / 2 }
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 500)
                        )
                    ) {
                        SessionsList(
                            item.numberOfSessions, item.motivationQuote,
                            isSelected = (selectedIndex == index), // Pass true if indexes match
                            onClick = { selectedIndex = index })
                    }

                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            enabled = if (selectedIndex == -1) false else true,
            onClick = {
                navController.navigate("OnBoardingScreen4") {
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
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
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
private fun OnBoardingScreen3Prev() {

    val navController = rememberNavController()
    FocusFusionTheme{
        OnBoardingScreen3(navController)
    }
}

val sessionListItem = listOf<SessionListItem>(
    SessionListItem("1 Session", "Quick Win"),
    SessionListItem("2 Sessions", "Daily Flow"),
    SessionListItem("3 Sessions", "Productivity Boost"),
    SessionListItem("4 Sessions", "Deep Work Mode"),
    SessionListItem("5 Sessions", "Master of Focus"),
    SessionListItem("6 Sessions", "Unstoppable"),
    SessionListItem("7+ Sessions", "Focus Legend"),
)

data class SessionListItem(
    val numberOfSessions: String,
    val motivationQuote: String,
)