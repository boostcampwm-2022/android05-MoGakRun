package com.whyranoid.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.running.RunningDataSource
import com.whyranoid.data.running.RunningDataSourceImpl
import com.whyranoid.data.running.RunningRepositoryImpl
import com.whyranoid.domain.repository.RunningRepository
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
    fun provideRunningDataSource(db: FirebaseFirestore): RunningDataSource {
        return RunningDataSourceImpl(db)
    }

    @Provides
    @Singleton
    fun provideRunningRepository(runningDataSource: RunningDataSource): RunningRepository {
        return RunningRepositoryImpl(runningDataSource)
    }
}
