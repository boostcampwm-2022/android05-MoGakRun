package com.whyranoid.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.whyranoid.domain.model.RunningHistory

@Entity(tableName = "running_history")
data class RunningHistoryEntity(
    @PrimaryKey
    val historyId: String,
    val startedAt: Long,
    val finishedAt: Long,
    val totalRunningTime: Int,
    val pace: Double,
    val totalDistance: Double
)

fun RunningHistoryEntity.toRunningHistory(): RunningHistory {
    return RunningHistory(
        historyId = historyId,
        startedAt = startedAt,
        finishedAt = finishedAt,
        totalRunningTime = totalRunningTime,
        pace = pace,
        totalDistance = totalDistance
    )
}
