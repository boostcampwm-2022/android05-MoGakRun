package com.whyranoid.presentation.community.group

sealed class Event {
    data class CreateGroupButtonClick(val isSuccess: Boolean = true) : Event()
    object WarningButtonClick : Event()
    object AddRuleButtonClick : Event()
}
