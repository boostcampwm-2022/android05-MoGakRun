package com.whyranoid.data.model

import com.whyranoid.domain.model.FinishNotification
import com.whyranoid.domain.model.RunningHistory
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
    val finishedAt: Long = 0L,
    val historyId: String = "",
    val pace: Double = 0.0,
    val startedAt: Long = 0L,
    val totalDistance: Double = 0.0,
    val totalRunningTime: Int = 0
) : GroupNotificationResponse

fun StartNotificationResponse.toStartNotification() =
    StartNotification(
        uid = this.uid,
        startedAt = this.startedAt
    )

fun FinishNotificationResponse.toFinishNotification() =
    FinishNotification(
        uid = this.uid,
        runningHistory = RunningHistory(
            historyId = this.historyId,
            startedAt = this.startedAt,
            finishedAt = this.finishedAt,
            totalRunningTime = this.totalRunningTime,
            pace = this.pace,
            totalDistance = this.totalDistance
        )
    )
