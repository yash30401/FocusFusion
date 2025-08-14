package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun SessionsList(
    numberOfSessions: String, motivationQuote: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 10.dp)
            .background(
                color = Color(0xffF9F9F9),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$numberOfSessions", fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.roboto_medium)),
        )
        Text(
            text = "$motivationQuote", fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            color = Color(0xff747474)
        )

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true, backgroundColor = 0xffFFFFFF
)
@Composable
private fun SessionsListPrev() {
    SessionsList("1 Session", "Quick Win", modifier = Modifier.padding(vertical = 60.dp))
}