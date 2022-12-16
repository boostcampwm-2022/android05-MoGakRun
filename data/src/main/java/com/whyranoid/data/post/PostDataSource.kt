package com.whyranoid.data.post

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.whyranoid.domain.model.Post

interface PostDataSource {

    suspend fun getCurrentPagingPost(key: QuerySnapshot?): QuerySnapshot

    suspend fun getNextPagingPost(lastDocumentSnapshot: DocumentSnapshot): QuerySnapshot

    suspend fun getMyCurrentPagingPost(key: QuerySnapshot?, uid: String): QuerySnapshot

    suspend fun getMyNextPagingPost(
        lastDocumentSnapshot: DocumentSnapshot,
        uid: String
    ): QuerySnapshot

    suspend fun convertPostType(document: QueryDocumentSnapshot): Result<Post>

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
