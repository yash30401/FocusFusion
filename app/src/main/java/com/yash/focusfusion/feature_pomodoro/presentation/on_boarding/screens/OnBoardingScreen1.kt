package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.OnBoardingUiEvent
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.FeatureList
import com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components.FeatureListData
import kotlinx.coroutines.delay

@Composable
fun OnBoardingScreen1(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnBoardingScreen1Prev() {

    val itemVisibility = remember { mutableStateListOf<Boolean>() }
    feautreListItem.forEach { itemVisibility.add(false) }

    val hapticFeedback = LocalHapticFeedback.current


    LaunchedEffect(key1 = Unit) {
        // Trigger the animation for each item with a small delay
        feautreListItem.forEachIndexed { index, _ ->
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
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "focus\nfusion",
                fontFamily = FontFamily(Font(R.font.fugaz_one_regular)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
                fontSize = 50.sp,
                color = Color(0xffFF8D61)
            )


            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp)

            ) {
                itemsIndexed(feautreListItem) {index,item ->
                    AnimatedVisibility(
                        visible = itemVisibility[index],
                        enter = slideInVertically(
                            initialOffsetY = { -it/2 }
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 500)
                        )
                    ) {
                        FeatureList(item.image, item.featureText)
                    }

                }
            }
        }

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

val feautreListItem = listOf<FeatureListData>(
    FeatureListData(
        R.drawable.timer_clock,
        "Finish Work Without Distractions"
    ),
    FeatureListData(
        R.drawable.take_break,
        "Pause To Power Up"
    ),
    FeatureListData(
        R.drawable.streak_icon,
        "Track Your Streaks And Wins"
    ),
    FeatureListData(
        R.drawable.chart_icon,
        "Turn Consistency Into Results"
    )
)