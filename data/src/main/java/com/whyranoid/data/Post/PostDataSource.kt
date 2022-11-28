package com.whyranoid.data.Post

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class PostDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun createRecruitPost(
        author: User,
        updatedAt: Long,
        groupInfo: GroupInfo
    ): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val postId = UUID.randomUUID().toString()
            db.collection(CollectionId.POST_COLLECTION)
                .document(postId)
                .set(
                    RecruitPost(
                        postId = postId,
                        author = author,
                        updatedAt = updatedAt,
                        groupInfo = groupInfo
                    )
                ).addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }
}
