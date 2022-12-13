package com.whyranoid.domain.repository

import androidx.paging.PagingData
import com.whyranoid.domain.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    // 글(홍보 / 인증) 페이징으로 가져오기 - 리모트
    fun getPagingPosts(coroutineScope: CoroutineScope): Flow<PagingData<Post>>

    fun getMyPagingPosts(uid: String, coroutineScope: CoroutineScope): Flow<PagingData<Post>>

    // 인증 글 작성
    suspend fun createRunningPost(
        authorUid: String,
        runningHistoryId: String,
        content: String
    ): Result<Boolean>

    // 홍보 글 작성
    suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean

    // 글 삭제하기 - 리모트
    suspend fun deletePost(postId: String): Boolean

    // 글 수정하기 - 리모트
    suspend fun updatePost(postId: String, postContent: String, updatedAt: Long): Boolean
}
