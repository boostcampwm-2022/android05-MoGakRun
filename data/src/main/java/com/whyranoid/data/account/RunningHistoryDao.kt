package com.whyranoid.data.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.whyranoid.data.model.RunningHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRunningHistory(runningHistory: RunningHistoryEntity)

    @Query("SELECT * FROM running_history ORDER BY started_at DESC")
    fun getRunningHistory(): Flow<List<RunningHistoryEntity>>
}
