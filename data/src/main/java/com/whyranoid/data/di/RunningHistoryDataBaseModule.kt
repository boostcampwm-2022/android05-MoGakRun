package com.whyranoid.data.di

import android.content.Context
import androidx.room.Room
import com.whyranoid.data.account.RunningHistoryDao
import com.whyranoid.data.account.RunningHistoryLocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RunningHistoryDataBaseModule {
    @Singleton
    @Provides
    fun provideRoomDataBase(
        @ApplicationContext appContext: Context
    ): RunningHistoryLocalDataBase = Room.databaseBuilder(
        appContext,
        RunningHistoryLocalDataBase::class.java,
        "mogakrun_running_history.db"
    )
        .build()

    @Singleton
    @Provides
    fun provideRunningHistoryDao(runningHistoryLocalDataBase: RunningHistoryLocalDataBase): RunningHistoryDao =
        runningHistoryLocalDataBase.runningHistoryDao()
}
