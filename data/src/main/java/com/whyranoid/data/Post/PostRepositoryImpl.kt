package com.whyranoid.data.Post

import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.User
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

    override suspend fun createPost(
        user: User,
        postContent: String,
        runningHistory: RunningHistory,
        updatedAt: Long
    ): Boolean {
        TODO("Not yet implemented")
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
