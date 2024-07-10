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
        Font(R.font.jost_bold, weight = FontWeight.Bold),
        Font(R.font.jost_thin,weight = FontWeight.Thin),
        Font(R.font.jost_black,weight = FontWeight.Black),
        Font(R.font.jost_light,weight = FontWeight.Light),
        Font(R.font.jost_black_italic,weight = FontWeight.Black),
        Font(R.font.jost_bolditalic,weight = FontWeight.Bold),
        Font(R.font.jost_extra_bold,weight = FontWeight.ExtraBold),
        Font(R.font.jost_extra_bold_italic,weight = FontWeight.ExtraBold),
        Font(R.font.jost_extra_light,weight = FontWeight.ExtraLight),
        Font(R.font.jost_extra_light_italic,weight = FontWeight.ExtraLight),
        Font(R.font.jost_italic),
        Font(R.font.jost_light_italic,weight = FontWeight.Light),
        Font(R.font.jost_medium,weight = FontWeight.Medium),
        Font(R.font.jost_medium_italic,weight = FontWeight.Medium),
        Font(R.font.jost_regular,weight = FontWeight.Normal),
        Font(R.font.jost_semi_bold,weight = FontWeight.SemiBold),
        Font(R.font.jost_semi_bold_italic,weight = FontWeight.SemiBold),
        Font(R.font.jost_thin_italic,weight = FontWeight.Thin),
    )
)

// Set of Material typography styles to start with
@RequiresApi(Build.VERSION_CODES.Q)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    ,
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)