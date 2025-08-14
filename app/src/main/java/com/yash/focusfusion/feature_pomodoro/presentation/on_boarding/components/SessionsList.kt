package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val backgroundColor = if (isSelected) Color(0xffFF8D61) else Color(0xffF9F9F9)
    val sessionTextColor = if (isSelected) Color(0xffFFFFFF) else Color(0xff000000)
    val motivationTextcolor = if (isSelected) Color(0xffFFE8DA) else Color(0xff747474)

    Row(
        modifier = modifier
            .padding(top = 10.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$numberOfSessions", fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.roboto_medium)),
            color = sessionTextColor
        )
        Text(
            text = "$motivationQuote", fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            color = motivationTextcolor
        )

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true, backgroundColor = 0xffFFFFFF
)
@Composable
private fun SessionsListPrev() {
    SessionsList(
        "1 Session",
        "Quick Win",
        isSelected = false,
        {},
        modifier = Modifier.padding(vertical = 60.dp)
    )
}