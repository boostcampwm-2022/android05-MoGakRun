package com.whyranoid.data.account

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RunningHistoryEntity::class],
    version = 1
)
abstract class RunningHistoryLocalDataBase : RoomDatabase() {
    abstract fun runningHistoryDao(): RunningHistoryDao
}
