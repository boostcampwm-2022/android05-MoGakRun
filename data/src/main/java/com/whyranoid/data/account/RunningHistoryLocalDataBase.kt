package com.whyranoid.data.account

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whyranoid.data.model.RunningHistoryEntity

@Database(
    entities = [RunningHistoryEntity::class],
    version = 1
)
abstract class RunningHistoryLocalDataBase : RoomDatabase() {
    abstract fun runningHistoryDao(): RunningHistoryDao
}
