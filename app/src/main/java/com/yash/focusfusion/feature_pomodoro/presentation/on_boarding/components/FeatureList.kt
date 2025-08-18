package com.yash.focusfusion.feature_pomodoro.presentation.on_boarding.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R
import com.yash.focusfusion.ui.theme.FocusFusionTheme
import kotlin.math.sin

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
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "$featureText",
                alignment = Alignment.Center,
                modifier = Modifier.padding(2.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
        Text(
            text = featureText, fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular)), modifier =
                Modifier.padding(start = 10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeatureListPrev() {
    FocusFusionTheme {
        FeatureList(
            R.drawable.timer_clock,
            "Finish Work Without Distractions"
        )
    }
}

data class FeatureListData(
    @DrawableRes val image: Int,
    val featureText: String,
)