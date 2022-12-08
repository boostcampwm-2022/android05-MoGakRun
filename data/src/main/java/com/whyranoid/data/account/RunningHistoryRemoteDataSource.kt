package com.whyranoid.data.account

import com.whyranoid.domain.model.RunningHistory

interface RunningHistoryRemoteDataSource {

    suspend fun uploadRunningHistory(uid: String, runningHistory: RunningHistory): Result<Boolean>
}
