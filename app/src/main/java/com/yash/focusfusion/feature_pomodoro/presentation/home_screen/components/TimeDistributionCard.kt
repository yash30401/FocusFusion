package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.yash.focusfusion.R
import java.util.concurrent.TimeUnit

@Composable
fun TimeDistributionCard(
    @DrawableRes icon: Int,
    task: String,
    totalTimeInMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.background(Color.Yellow)) {
            Row {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "$task"
                )
                Text(text = "$task")
            }

            LinearProgressIndicator(
                modifier = Modifier,
                progress = { 0.6f },
                color = Color(0xff9463ED),
                trackColor = Color(0xffF0F0F0)
            )
            Text(text = TimeUnit.MINUTES.toHours(totalTimeInMinutes.toLong()).toString()+"hrs")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xffFFFDFC
)
@Composable
private fun TimeDistributionCardPreview() {
    TimeDistributionCard(
        R.drawable.books,
        "Study",
        820
    )
}