package com.whyranoid.data.post

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.whyranoid.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {

    fun getAllPostFlow(): Flow<List<Post>>

    fun getMyPostFlow(uid: String): Flow<List<Post>>

    suspend fun getCurrentPagingPost(key: QuerySnapshot?): QuerySnapshot

    suspend fun getNextPagingPost(lastDocumentSnapshot: DocumentSnapshot): QuerySnapshot

    suspend fun convertPostType(document: QueryDocumentSnapshot): Post?

    suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean

    suspend fun createRunningPost(
        authorUid: String,
        runningHistoryId: String,
        content: String
    ): Result<Boolean>

    suspend fun deletePost(postId: String): Boolean
}
