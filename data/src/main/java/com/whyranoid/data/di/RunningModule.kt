package com.whyranoid.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.running.RunnerDataSource
import com.whyranoid.data.running.RunnerDataSourceImpl
import com.whyranoid.data.running.RunnerRepositoryImpl
import com.whyranoid.domain.repository.RunnerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RunningModule {

    @Provides
    @Singleton
    fun provideRunningDataSource(db: FirebaseFirestore): RunnerDataSource {
        return RunnerDataSourceImpl(db)
    }

    @Provides
    @Singleton
    fun provideRunningRepository(runnerDataSource: RunnerDataSource): RunnerRepository {
        return RunnerRepositoryImpl(runnerDataSource)
    }
}
