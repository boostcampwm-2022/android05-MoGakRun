package com.whyranoid.data.post

import com.whyranoid.domain.model.Post
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
) : PostRepository {

    // TODO : 페이징처리하기
    override fun getPagingPosts(): Flow<List<Post>> {
        TODO("Not yet implemented")
    }

    override fun getAllPostFlow(): Flow<List<Post>> {
        return postDataSource.getAllPostFlow()
    }

    override fun getMyPostFlow(uid: String): Flow<List<Post>> {
        return postDataSource.getMyPostFlow(uid)
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
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(postId: String, postContent: String, updatedAt: Long): Boolean {
        TODO("Not yet implemented")
    }
}
