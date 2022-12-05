package com.whyranoid.data.di

import com.whyranoid.data.post.PostDataSource
import com.whyranoid.data.post.PostDataSourceImpl
import com.whyranoid.data.post.PostRepositoryImpl
import com.whyranoid.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    @Binds
    abstract fun bindPostDataSource(postDataSourceImpl: PostDataSourceImpl): PostDataSource

    @Binds
    abstract fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository
}
