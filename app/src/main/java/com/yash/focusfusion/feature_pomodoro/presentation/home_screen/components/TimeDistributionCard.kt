package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import java.util.concurrent.TimeUnit

@Composable
fun TimeDistributionCard(
    @DrawableRes icon: Int,
    task: String,
    totalTimeInMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xffF8F8F8), shape = RoundedCornerShape(20.dp))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .background(
                            Color(0xffF0F0F0),
                            RoundedCornerShape(100.dp)
                        )
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "$task"
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    text = "$task",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium))
                )
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .height(28.dp)
                    .width(150.dp)
                    .padding(vertical = 10.dp),
                progress = { 0.6f },
                color = Color(0xff9463ED),
                trackColor = Color(0xffF0F0F0),
                strokeCap = StrokeCap.Round
            )

            Text(
                text = TimeUnit.MINUTES.toHours(totalTimeInMinutes.toLong())
                    .toString() + "hrs",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Centers text within the Column
                    .wrapContentWidth(Alignment.CenterHorizontally), // Ensures it doesn’t stretch
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.jost_medium))
            )
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
        820,
        modifier = Modifier.padding(10.dp)
    )
}