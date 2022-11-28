package com.whyranoid.presentation.model

import android.os.Parcelable
import com.whyranoid.domain.model.RunningHistory
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningHistoryUiModel(
    val historyId: String,
    val date: String,
    val totalRunningTime: String,
    val startedAt: String,
    val finishedAt: String,
    val totalDistance: String,
    val pace: String
) : Parcelable

// TODO 원하는 형태로 변환하도록 코드 수정해야함
fun RunningHistory.toRunningHistoryUiModel(): RunningHistoryUiModel {
    return RunningHistoryUiModel(
        historyId = historyId,
        date = startedAt.toString(),
        totalRunningTime = totalRunningTime.toString(),
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        totalDistance = totalDistance.toString(),
        pace = pace.toString()
    )
}
