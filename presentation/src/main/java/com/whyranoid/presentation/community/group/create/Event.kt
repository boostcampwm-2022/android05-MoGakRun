package com.whyranoid.presentation.community.group.create

sealed class Event {
    data class CreateGroupButtonClick(val isSuccess: Boolean = true) : Event()
    object WarningButtonClick : Event()
    object AddRuleButtonClick : Event()
    data class DuplicateCheckButtonClick(val isDuplicatedGroupName: Boolean = false) : Event()
}
