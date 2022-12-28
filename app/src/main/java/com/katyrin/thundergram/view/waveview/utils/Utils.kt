package com.katyrin.thundergram.view.waveview.utils

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

fun Context.dp(dp: Int): Float =
    TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)