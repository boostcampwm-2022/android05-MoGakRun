package com.whyranoid.presentation.running

sealed interface Event {
    data class FinishButtonClick(val runningFinishData: RunningFinishData) : Event
    object RunningFinishFailure : Event
}
