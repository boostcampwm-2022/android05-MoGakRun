package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.RunningHistoryRepository
import javax.inject.Inject

class SaveRunningHistoryUseCase @Inject constructor(private val runningHistoryRepository: RunningHistoryRepository) {
    suspend operator fun invoke(
        historyId: String,
        startedAt: Long,
        finishedAt: Long,
        totalRunningTime: Int,
        pace: Double,
        totalDistance: Double
    ): Result<RunningHistory> {
        return runningHistoryRepository.saveRunningHistory(
            historyId = historyId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            totalRunningTime = totalRunningTime,
            pace = pace,
            totalDistance = totalDistance
        )
    }
}
