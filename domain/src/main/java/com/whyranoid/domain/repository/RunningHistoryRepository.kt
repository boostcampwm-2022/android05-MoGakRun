package com.whyranoid.domain.repository

import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow

interface RunningHistoryRepository {

    // 운동 내역 가져오기 - 로컬
    fun getRunningHistory(): Flow<Result<List<RunningHistory>>>

    // 글 안쓴 운동 내역 가져오기 - 로컬
    suspend fun getUnpostedRunningHistory(): List<RunningHistory>

    // 운동한 기록 저장하기 - 로컬
    suspend fun saveRunningHistory(
        historyId: String,
        startedAt: Long,
        finishedAt: Long,
        totalRunningTime: Int,
        pace: Double,
        totalDistance: Double
    ): Result<RunningHistory>

    suspend fun uploadRunningHistory(uid: String, runningHistory: RunningHistory): Result<Boolean>
}
