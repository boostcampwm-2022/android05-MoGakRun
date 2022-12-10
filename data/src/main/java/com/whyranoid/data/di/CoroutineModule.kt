package com.whyranoid.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Provides
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}
