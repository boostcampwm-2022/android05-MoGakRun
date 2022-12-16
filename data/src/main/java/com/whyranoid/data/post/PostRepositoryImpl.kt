package com.whyranoid.data.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
) : PostRepository {

    // TODO : 캐싱하기
    override fun getPagingPosts(coroutineScope: CoroutineScope): Flow<PagingData<Post>> {
        return Pager(
            PagingConfig(pageSize = 5)
        ) {
            PostPagingDataSource(postDataSource = postDataSource)
        }.flow.cachedIn(coroutineScope)
    }

    override fun getMyPagingPosts(uid: String, coroutineScope: CoroutineScope): Flow<PagingData<Post>> {
        return Pager(
            PagingConfig(pageSize = 5)
        ) {
            PostPagingDataSource(myUid = uid, postDataSource)
        }.flow.cachedIn(coroutineScope)
    }

    override suspend fun createRunningPost(
        authorUid: String,
        runningHistoryId: String,
        content: String
    ): Result<Boolean> {
        return postDataSource.createRunningPost(authorUid, runningHistoryId, content)
    }

    override suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean {
        return postDataSource.createRecruitPost(authorUid, groupUid)
    }

    override suspend fun deletePost(postId: String): Boolean {
        return postDataSource.deletePost(postId)
    }

    override suspend fun updatePost(postId: String, postContent: String, updatedAt: Long): Boolean {
        TODO("Not yet implemented")
    }
}
