package com.whyranoid.domain.model

data class RunningHistory(
    val historyId: String,
    val startedAt: Long,
    val finishedAt: Long,
    val totalRunningTime: Int,
    val pace: Double,
    val totalDistance: Double
)
