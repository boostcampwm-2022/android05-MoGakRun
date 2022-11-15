package com.whyranoid.domain.model

sealed interface GroupNotification {
    val uid: String
}

data class StartNotification(
    override val uid: String,
    val startedAt: Long
) : GroupNotification

data class FinishNotification(
    override val uid: String,
    val runningHistory: RunningHistory
) : GroupNotification
