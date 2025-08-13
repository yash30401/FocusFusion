package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@Composable
fun FeatureList(
    @DrawableRes image: Int,
    featureText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 10.dp)
            .background(
                color = Color(0xffF9F9F9),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = CircleShape)
                .padding(2.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "$featureText",
                alignment = Alignment.Center,
            )
        }
        Text(
            text = featureText, fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)), modifier =
                Modifier.padding(start = 10.dp)
        )

    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    backgroundColor = 0xffFFFFFF
)
@Composable
private fun FeatureListPrev() {
    FeatureList(
        R.drawable.timer_clock,
        "Finish Work Without Distractions"
    )
}

data class FeatureListData(
    @DrawableRes val image: Int,
    val featureText: String,
)