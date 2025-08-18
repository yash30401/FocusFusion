package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.MaterialTheme
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
import com.yash.focusfusion.ui.theme.FocusFusionTheme

@Composable
fun SessionsList(
    numberOfSessions: String, motivationQuote: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    // CHANGE 1: Define colors based on the theme and selection state
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val sessionTextColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val motivationTextColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.7f
        )

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
            color = motivationTextColor
        )

    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SessionsListPrev() {
    FocusFusionTheme {
        SessionsList(
            "1 Session",
            "Quick Win",
            isSelected = false,
            {},
            modifier = Modifier.padding(vertical = 60.dp)
        )
    }
}