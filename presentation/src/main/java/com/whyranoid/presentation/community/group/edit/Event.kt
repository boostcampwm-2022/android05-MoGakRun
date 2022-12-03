package com.whyranoid.presentation.community.group.edit

sealed class Event {
    data class EditGroupButtonClick(val isSuccess: Boolean = true) : Event()
    object AddRuleButtonClick : Event()
}
