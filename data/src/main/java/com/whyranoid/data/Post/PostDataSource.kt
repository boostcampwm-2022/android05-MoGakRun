package com.whyranoid.data.Post

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class PostDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun createRecruitPost(
        authorUid: String,
        updatedAt: Long,
        groupUid: String
    ): Boolean {
        val postId = UUID.randomUUID().toString()

        val author = db.collection(CollectionId.USERS_COLLECTION)
            .document(authorUid)
            .get()
            .await()
            .toObject(UserResponse::class.java)
            ?.toUser()

        author?.let {
            val groupInfo = db.collection(CollectionId.GROUPS_COLLECTION)
                .document(groupUid)
                .get()
                .await()
                .toObject(GroupInfoResponse::class.java)

            groupInfo?.let {
                return suspendCancellableCoroutine { cancellableContinuation ->

                    db.collection(CollectionId.POST_COLLECTION)
                        .document(postId)
                        .set(
                            RecruitPost(
                                postId = postId,
                                author = author,
                                updatedAt = updatedAt,
                                groupInfo = groupInfo.toGroupInfo(
                                    leader = author,
                                    rules = groupInfo.rules.map { it.toRule() }
                                )
                            )
                        ).addOnSuccessListener {
                            cancellableContinuation.resume(true)
                        }.addOnFailureListener {
                            cancellableContinuation.resume(false)
                        }
                }
            }
        }
        return false
    }
}
