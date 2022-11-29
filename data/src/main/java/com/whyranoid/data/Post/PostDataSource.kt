package com.whyranoid.data.Post

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.RecruitPostResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class PostDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    //  TODO : 타입을 확인하고 캐스팅하는 부분 필요
    fun getAllPostFlow(): Flow<List<Post>> =
        callbackFlow {
            db.collection(CollectionId.POST_COLLECTION)
                .addSnapshotListener { snapshot, _ ->
                    val recruitPostList = mutableListOf<Post>()
                    snapshot?.forEach { docuemnt ->
                        docuemnt.toObject(RecruitPostResponse::class.java).let { postResponse ->

                            db.collection(CollectionId.USERS_COLLECTION)
                                .document(postResponse.authorId)
                                .get()
                                .addOnSuccessListener { authorDocument ->
                                    val authorResponse =
                                        authorDocument?.toObject(UserResponse::class.java)

                                    authorResponse?.let {
                                        db.collection(CollectionId.GROUPS_COLLECTION)
                                            .document(postResponse.groupId)
                                            .get()
                                            .addOnSuccessListener { groupDocument ->
                                                val groupInfoResponse =
                                                    groupDocument.toObject(GroupInfoResponse::class.java)

                                                groupInfoResponse?.let { groupInfoResponse ->
                                                    val author = authorResponse.toUser()
                                                    recruitPostList.add(
                                                        RecruitPost(
                                                            postId = postResponse.postId,
                                                            author = author,
                                                            updatedAt = postResponse.updatedAt,
                                                            groupInfo = groupInfoResponse
                                                                .toGroupInfo(
                                                                    author,
                                                                    rules = groupInfoResponse.rules.map {
                                                                        it.toRule()
                                                                    }
                                                                )
                                                        )
                                                    )
                                                    trySend(recruitPostList)
                                                }
                                            }
                                    }
                                }
                        }
                    }
                }

            awaitClose()
        }

    suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean {
        val postId = UUID.randomUUID().toString()

        return suspendCancellableCoroutine { cancellableContinuation ->

            db.collection(CollectionId.POST_COLLECTION)
                .document(postId)
                .set(
                    RecruitPostResponse(
                        postId = postId,
                        authorId = authorUid,
                        updatedAt = System.currentTimeMillis(),
                        groupId = groupUid
                    )
                ).addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }
}
