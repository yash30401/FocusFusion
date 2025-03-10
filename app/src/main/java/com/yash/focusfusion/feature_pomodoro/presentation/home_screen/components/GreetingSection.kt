package com.yash.focusfusion.feature_pomodoro.presentation.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@Composable
fun GreetingHead(
    nameOfPerson: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(10.dp)) {
        Text(
            "Good Morning,",
            fontFamily = FontFamily(Font(R.font.jost_medium)),
            color = Color(0xffFF8D61),
            fontSize = 28.sp
        )
        Text(
            nameOfPerson,
            fontFamily = FontFamily(Font(R.font.jost_semi_bold)),
            fontSize = 28.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GreetingHeadPreview() {
    GreetingHead("Yashveer")
}