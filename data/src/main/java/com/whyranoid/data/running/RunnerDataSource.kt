package com.whyranoid.data.running

import kotlinx.coroutines.flow.Flow

interface RunnerDataSource {

    fun getCurrentRunnerCount(): Flow<Result<Int>>

    suspend fun startRunning(uid: String): Boolean

    suspend fun finishRunning(uid: String): Boolean
}
