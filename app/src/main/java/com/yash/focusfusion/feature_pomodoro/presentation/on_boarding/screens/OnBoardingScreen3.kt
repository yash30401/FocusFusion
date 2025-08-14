package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.FeatureList
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.SessionsList
import kotlinx.coroutines.delay

@Composable
fun OnBoardingScreen3(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnBoardingScreen3Prev() {

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
            .background(Color.White)
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily =
                                FontFamily(listOf(Font(R.font.roboto_extra_bold))),
                            fontSize = 40.sp,
                            color = Color(0xff000000),
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
                            color = Color(0xff000000),
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
                            color = Color(0xff8958E2),
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
                color = Color(0xff000000),
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
                        SessionsList(item.numberOfSessions, item.motivationQuote)
                    }

                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffFF8D61)
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                text = "Get Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

val sessionListItem = listOf<SessionListItem>(
    SessionListItem("1 Session","Quick Win"),
    SessionListItem("2 Sessions","Daily Flow"),
    SessionListItem("3 Sessions","Productivity Boost"),
    SessionListItem("4 Sessions","Deep Work Mode"),
    SessionListItem("5 Sessions","Master of Focus"),
    SessionListItem("6 Sessions","Unstoppable"),
    SessionListItem("7+ Sessions","Focus Legend"),
)

data class SessionListItem(
    val numberOfSessions:String,
    val motivationQuote:String
)