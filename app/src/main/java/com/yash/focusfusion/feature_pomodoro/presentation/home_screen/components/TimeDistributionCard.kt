package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.yash.focusfusion.R

@Composable
fun TimeDistributionCard(
    @DrawableRes icon: Int,
    task: String,
    totalTimeInMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.background(Color.Red)) {
            Row {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "$task"
                )
                Text(text = "$task")
            }
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
        5000
    )
}