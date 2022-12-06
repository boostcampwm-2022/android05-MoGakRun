package com.whyranoid.presentation.running

import com.whyranoid.presentation.running.runningdatamanager.RunningFinishData

sealed interface Event {
    data class FinishButtonClick(val runningFinishData: RunningFinishData) : Event
    object RunningFinishFailure : Event
}
