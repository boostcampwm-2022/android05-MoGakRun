package com.whyranoid.presentation.community.group

sealed class Event {
    data class CreateGroupButtonClick(val isCompleted: Boolean = true) : Event()
    object WarningButtonClick : Event()
}
