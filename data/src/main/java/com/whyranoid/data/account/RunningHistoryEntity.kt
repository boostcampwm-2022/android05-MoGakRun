package com.whyranoid.data.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.whyranoid.domain.model.RunningHistory

@Entity(tableName = "running_history")
data class RunningHistoryEntity(
    @PrimaryKey
    val historyId: String,
    @ColumnInfo(name = "started_at") val startedAt: Long,
    @ColumnInfo(name = "finished_at") val finishedAt: Long,
    @ColumnInfo(name = "total_running_time") val totalRunningTime: Int,
    @ColumnInfo(name = "pace") val pace: Double,
    @ColumnInfo(name = "total_distance") val totalDistance: Double
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
