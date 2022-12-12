package com.whyranoid.data.account

import com.whyranoid.data.model.RunningHistoryEntity
import com.whyranoid.data.model.toRunningHistory
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunningHistoryLocalDataSourceImpl @Inject constructor(
    private val runningHistoryDao: RunningHistoryDao
) : RunningHistoryLocalDataSource {

    override fun getRunningHistory(): Flow<Result<List<RunningHistory>>> {
        return runningHistoryDao.getRunningHistory().map { runningHistoryList ->
            runCatching {
                runningHistoryList.map { runningHistoryEntity ->
                    runningHistoryEntity.toRunningHistory()
                }
            }
        }
    }

    override suspend fun saveRunningHistory(runningHistoryEntity: RunningHistoryEntity): Result<RunningHistory> {
        return runCatching {
            runningHistoryDao.addRunningHistory(runningHistoryEntity)
            runningHistoryEntity.toRunningHistory()
        }
    }
}
