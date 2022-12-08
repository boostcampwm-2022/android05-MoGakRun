package com.whyranoid.runningdata.model

import android.location.Location
import java.util.UUID

data class RunningData(
    val startTime: Long = 0L,
    val runningTime: Int = 0,
    val totalDistance: Double = 0.0,
    val pace: Double = 0.0,
    val runningPositionList: List<List<RunningPosition>> = listOf(emptyList()),
    val lastLocation: Location? = null
)

fun RunningData.toRunningFinishData() =
    RunningFinishData(
        RunningHistoryModel(
            historyId = UUID.randomUUID().toString(),
            startedAt = startTime,
            finishedAt = System.currentTimeMillis(),
            totalRunningTime = runningTime,
            pace = pace,
            totalDistance = totalDistance
        ),
        runningPositionList
    )
