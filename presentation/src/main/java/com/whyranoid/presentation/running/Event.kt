package com.whyranoid.presentation.running

sealed interface Event {
    data class TrackingButtonClick(val mode: TrackingMode) : Event
}
