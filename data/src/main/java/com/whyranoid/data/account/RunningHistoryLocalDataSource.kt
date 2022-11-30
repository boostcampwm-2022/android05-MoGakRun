package com.whyranoid.data.account

import com.whyranoid.data.model.RunningHistoryEntity
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow

interface RunningHistoryLocalDataSource {
    fun getRunningHistory(): Flow<Result<List<RunningHistory>>>
    suspend fun saveRunningHistory(runningHistoryEntity: RunningHistoryEntity): Result<RunningHistory>
}
