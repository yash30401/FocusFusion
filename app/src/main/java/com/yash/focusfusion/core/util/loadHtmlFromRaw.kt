package com.yash.focusfusion.core.util

import android.content.Context
import android.text.Spanned
import androidx.annotation.RawRes
import androidx.core.text.HtmlCompat

fun loadHtmlFromRaw(context: Context, @RawRes resId: Int): Spanned {
    val rawText = context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    return HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_LEGACY)
}