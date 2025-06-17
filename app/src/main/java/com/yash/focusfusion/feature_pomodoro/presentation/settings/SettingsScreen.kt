package com.yash.focusfusion.feature_pomodoro.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true, showSystemUi = true,
    backgroundColor = 0xffFFFDFC
)
@Composable
private fun SettingsScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                )
                .padding(top = 60.dp)
                .fillMaxWidth()
                .height(150.dp)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .background(Color(0xffF8F8F8), shape = RoundedCornerShape(20.dp))

        ) {
            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Yashveer",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.jost_medium)),
                )

                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Edit Name",
                        tint = Color.Black
                    )
                }
            }

        }
    }
}