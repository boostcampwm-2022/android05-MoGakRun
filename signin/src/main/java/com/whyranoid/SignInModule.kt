package com.whyranoid

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInModule {

    @Binds
    @Singleton
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl): SignInRepository

    @Binds
    @Singleton
    abstract fun bindSignInDataSource(signInDataSourceImpl: SignInDataSourceImpl): SignInDataSource
}
