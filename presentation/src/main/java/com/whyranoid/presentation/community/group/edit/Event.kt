package com.whyranoid.presentation.community.group.edit

// TODO : create와 거의 동일함..
sealed class Event {
    data class EditGroupButtonClick(val isSuccess: Boolean = true) : Event()
    object WarningButtonClick : Event()
    object AddRuleButtonClick : Event()
}
