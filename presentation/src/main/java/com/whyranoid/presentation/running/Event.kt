package com.whyranoid.presentation.running

import com.whyranoid.runningdata.model.RunningFinishData

sealed interface Event {
    data class FinishButtonClick(val runningFinishData: RunningFinishData) : Event
    object RunningFinishFailure : Event
}
