package com.whyranoid.runningdata.model

data class RunningHistoryModel(
    val historyId: String = "",
    val startedAt: Long = 0L,
    val finishedAt: Long = 0L,
    val totalRunningTime: Int = 0,
    val pace: Double = 0.0,
    val totalDistance: Double = 0.0
) : java.io.Serializable
