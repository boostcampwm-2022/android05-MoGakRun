package com.whyranoid.presentation.community.group.detail

sealed class Event {
    object RecruitButtonClick : Event()
    object ExitGroupButtonClick : Event()
    data class RecruitSnackBarButtonClick(val isSuccess: Boolean = true) : Event()
}
