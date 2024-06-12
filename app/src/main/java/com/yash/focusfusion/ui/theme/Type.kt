package com.yash.focusfusion.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yash.focusfusion.R

@RequiresApi(Build.VERSION_CODES.Q)
val fontFamily = FontFamily(
    listOf(
        Font(R.font.jost_bold),
        Font(R.font.jost_thin),
        Font(R.font.jost_black),
        Font(R.font.jost_light),
        Font(R.font.jost_black_italic),
        Font(R.font.jost_bolditalic),
        Font(R.font.jost_extra_bold),
        Font(R.font.jost_extra_bold_italic),
        Font(R.font.jost_extra_light),
        Font(R.font.jost_extra_light_italic),
        Font(R.font.jost_italic),
        Font(R.font.jost_light_italic),
        Font(R.font.jost_medium),
        Font(R.font.jost_medium_italic),
        Font(R.font.jost_regular),
        Font(R.font.jost_semi_bold),
        Font(R.font.jost_semi_bold_italic),
        Font(R.font.jost_thin_italic),
    )
)

// Set of Material typography styles to start with
@RequiresApi(Build.VERSION_CODES.Q)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)