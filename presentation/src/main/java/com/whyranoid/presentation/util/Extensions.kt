package com.whyranoid.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun LifecycleOwner.repeatWhenUiStarted(block: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block.invoke()
        }
    }
}

fun Date.dateToString(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this)
}
