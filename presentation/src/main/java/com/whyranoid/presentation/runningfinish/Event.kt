package com.whyranoid.presentation.runningfinish

import com.whyranoid.presentation.model.RunningHistoryUiModel

sealed class Event {
    data class PositiveButtonClick(val runningHistory: RunningHistoryUiModel) : Event()
    object NegativeButtonButtonClick : Event()
}
