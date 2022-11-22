package com.whyranoid.presentation.di

import android.content.Context
import com.whyranoid.presentation.util.networkconnection.NetworkConnectionStateHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkConnectionModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionStateHolder(@ApplicationContext context: Context): NetworkConnectionStateHolder {
        return NetworkConnectionStateHolder(context)
    }
}
