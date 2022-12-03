package com.whyranoid.data.di

import com.whyranoid.data.group.GroupDataSource
import com.whyranoid.data.group.GroupDataSourceImpl
import com.whyranoid.data.group.GroupRepositoryImpl
import com.whyranoid.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupModule {

    @Binds
    abstract fun bindGroupDataSource(groupDataSourceImpl: GroupDataSourceImpl): GroupDataSource

    @Binds
    abstract fun bindGroupRepository(groupRepositoryImpl: GroupRepositoryImpl): GroupRepository
}
