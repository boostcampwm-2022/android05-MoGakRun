package com.whyranoid.presentation.running.runningdatamanager

import com.naver.maps.geometry.LatLng
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.presentation.model.RunningHistoryUiModel

data class RunningPosition(
    val latitude: Double,
    val longitude: Double
) : java.io.Serializable

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
