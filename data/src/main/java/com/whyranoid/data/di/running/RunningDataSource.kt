package com.whyranoid.data.di.running

import kotlinx.coroutines.flow.Flow

interface RunningDataSource {

    fun getCurrentRunnerCount(): Flow<Int>

    suspend fun startRunning(uid: String): Boolean

    suspend fun finishRunning(uid: String): Boolean
}
