package com.whyranoid.data.post

import com.whyranoid.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {

    fun getAllPostFlow(): Flow<List<Post>>

    fun getMyPostFlow(uid: String): Flow<List<Post>>

    suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean

    suspend fun createRunningPost(
        authorUid: String,
        runningHistoryId: String,
        content: String
    ): Result<Boolean>
}
