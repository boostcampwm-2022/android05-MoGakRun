package com.whyranoid.data.running

import com.whyranoid.domain.repository.RunnerRepository
import kotlinx.coroutines.flow.Flow

class RunnerRepositoryImpl(private val runnerDataSource: RunnerDataSource) : RunnerRepository {
    override fun getCurrentRunnerCount(): Flow<Int> {
        return runnerDataSource.getCurrentRunnerCount()
    }

    override suspend fun startRunning(uid: String): Boolean {
        return runnerDataSource.startRunning(uid)
    }

    override suspend fun finishRunning(uid: String): Boolean {
        return runnerDataSource.finishRunning(uid)
    }
}
