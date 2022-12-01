package com.whyranoid.presentation.util.pxdp

import android.content.Context

object PxDpUtil {
    fun dpToPx(context: Context, dp: Int): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun pxToDp(context: Context, px: Int): Int {
        val scale: Float = context.resources.displayMetrics.density
        val mul = when (scale) {
            1.0f -> 4.0f
            1.5f -> 8 / 3.0f
            2.0f -> 2.0f
            else -> 1.0f
        }
        return (px / (scale * mul)).toInt()
    }
}
