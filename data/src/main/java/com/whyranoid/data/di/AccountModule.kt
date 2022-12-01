package com.whyranoid.data.di

import com.whyranoid.data.account.AccountDataSource
import com.whyranoid.data.account.AccountDataSourceImpl
import com.whyranoid.data.account.AccountRepositoryImpl
import com.whyranoid.data.account.RunningHistoryLocalDataSource
import com.whyranoid.data.account.RunningHistoryLocalDataSourceImpl
import com.whyranoid.data.account.RunningHistoryRemoteDataSource
import com.whyranoid.data.account.RunningHistoryRemoteDataSourceImpl
import com.whyranoid.data.account.RunningHistoryRepositoryImpl
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunningHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountModule {

    @Binds
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindAccountDataSource(accountDataSourceImpl: AccountDataSourceImpl): AccountDataSource

    @Binds
    abstract fun provideRunningHistoryRepository(runningHistoryRepositoryImpl: RunningHistoryRepositoryImpl): RunningHistoryRepository

    @Binds
    abstract fun provideRunningHistoryDataSource(runningHistoryLocalDataSourceImpl: RunningHistoryLocalDataSourceImpl): RunningHistoryLocalDataSource

    @Binds
    abstract fun bindRunningHistoryRemoteDataSource(runningHistoryRemoteDataSourceImpl: RunningHistoryRemoteDataSourceImpl): RunningHistoryRemoteDataSource
}
