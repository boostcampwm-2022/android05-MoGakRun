package com.whyranoid.data.model

import com.whyranoid.domain.model.StartNotification

sealed interface GroupNotificationResponse {
    val type: String
    val uid: String
}

data class StartNotificationResponse(
    override val type: String = "",
    override val uid: String = "",
    val startedAt: Long = 0L
) : GroupNotificationResponse

data class FinishNotificationResponse(
    override val type: String = "",
    override val uid: String = "",
    val historyId: String = ""
) : GroupNotificationResponse

fun StartNotificationResponse.toStartNotification() =
    StartNotification(
        uid = this.uid,
        startedAt = this.startedAt
    )
