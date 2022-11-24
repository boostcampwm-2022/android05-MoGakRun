package com.whyranoid.data.account

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.RunningHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunningHistoryRepositoryImpl @Inject constructor(
    private val runningHistoryLocalDataSource: RunningHistoryLocalDataSource
) : RunningHistoryRepository {
    override fun getRunningHistory(): Flow<List<RunningHistory>> {
        return runningHistoryLocalDataSource.getRunningHistory()
    }

    override suspend fun getUnpostedRunningHistory(): List<RunningHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRunningHistory(
        startedAt: Long,
        finishedAt: Long,
        totalRunningTime: Int,
        pace: Double,
        totalDistance: Double
    ): Result<RunningHistory> {
        TODO("Not yet implemented")
    }
}
