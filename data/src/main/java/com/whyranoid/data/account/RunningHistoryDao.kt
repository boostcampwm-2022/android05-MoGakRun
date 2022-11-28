package com.whyranoid.data.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningHistoryDao {
    // TODO 일단 쓰기, 읽기만 해놨습니다~
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addRunningHistory(runningHistory: RunningHistoryEntity)

    @Query("SELECT * FROM running_history ORDER BY started_at ASC")
    fun getRunningHistory(): Flow<List<RunningHistoryEntity>>
}
