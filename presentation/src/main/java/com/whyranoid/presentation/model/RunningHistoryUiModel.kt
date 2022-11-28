package com.whyranoid.presentation.model

import com.whyranoid.domain.model.RunningHistory

data class RunningHistoryUiModel(
    val date: String,
    val totalRunningTime: String,
    val startedAt: String,
    val finishedAt: String,
    val totalDistance: String,
    val pace: String
)

// TODO 원하는 형태로 변환하도록 코드 수정해야함
fun RunningHistory.toRunningHistoryUiModel(): RunningHistoryUiModel {
    return RunningHistoryUiModel(
        date = startedAt.toString(),
        totalRunningTime = totalRunningTime.toString(),
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        totalDistance = totalDistance.toString(),
        pace = pace.toString()
    )
}
