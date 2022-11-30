package com.whyranoid.presentation.model

import com.whyranoid.domain.model.RunningHistory

data class RunningHistoryUiModel(
    val historyId: String,
    val date: Long,
    val totalRunningTime: Int,
    val startedAt: Long,
    val finishedAt: Long,
    val totalDistance: Double,
    val pace: Double
) : java.io.Serializable

// TODO 원하는 형태로 변환하도록 코드 수정해야함
fun RunningHistory.toRunningHistoryUiModel(): RunningHistoryUiModel {
    return RunningHistoryUiModel(
        historyId = historyId,
        date = startedAt,
        totalRunningTime = totalRunningTime,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalDistance = totalDistance,
        pace = pace
    )
}
