package com.whyranoid.presentation.running

import com.naver.maps.geometry.LatLng
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.runningdata.model.RunningHistoryModel
import com.whyranoid.runningdata.model.RunningPosition

fun RunningPosition.toLatLng(): LatLng {
    return LatLng(
        this.latitude,
        this.longitude
    )
}

fun RunningHistoryModel.toRunningHistoryUiModel() =
    RunningHistoryUiModel(
        historyId = historyId,
        date = startedAt,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalRunningTime = totalRunningTime,
        pace = pace,
        totalDistance = totalDistance
    )

fun RunningHistoryModel.toRunningHistory() =
    RunningHistory(
        historyId = historyId,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalRunningTime = totalRunningTime,
        pace = pace,
        totalDistance = totalDistance
    )
