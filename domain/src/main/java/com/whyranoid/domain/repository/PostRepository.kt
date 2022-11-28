package com.whyranoid.domain.repository

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.User
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    // TODO : 페이징 처리
    // 글(홍보 / 인증) 페이징으로 가져오기 - 리모트
    fun getPagingPosts(): Flow<List<Post>>

    // 글 작성하기 - 리모트
    suspend fun createPost(
        user: User,
        postContent: String,
        runningHistory: RunningHistory,
        updatedAt: Long
    ): Boolean

    suspend fun createRecruitPost(
        author: User,
        updatedAt: Long = 0L,
        groupInfo: GroupInfo
    ): Boolean

    // 글 삭제하기 - 리모트
    suspend fun deletePost(postId: String): Boolean

    // 글 수정하기 - 리모트
    suspend fun updatePost(postId: String, postContent: String, updatedAt: Long): Boolean
}
