package com.whyranoid.data.account

import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow

interface RunningHistoryLocalDataSource {
    fun getRunningHistory(): Flow<Result<List<RunningHistory>>>
}
