package com.whyranoid.data.account

import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunningHistoryLocalDataSourceImpl @Inject constructor(
    private val roomDb: RunningHistoryLocalDataBase
) : RunningHistoryLocalDataSource {

    override fun getRunningHistory(): Flow<List<RunningHistory>> {
        return roomDb.runningHistoryDao().getRunningHistory().map { runningHistoryList ->
            runningHistoryList.map { runningHistoryEntity ->
                runningHistoryEntity.toRunningHistory()
            }
        }
    }
}
