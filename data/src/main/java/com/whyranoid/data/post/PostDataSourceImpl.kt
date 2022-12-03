package com.whyranoid.data.post

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.constant.FieldId.AUTHOR_ID
import com.whyranoid.data.constant.FieldId.RUNNING_HISTORY_ID
import com.whyranoid.data.constant.FieldId.UPDATED_AT
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.RecruitPostResponse
import com.whyranoid.data.model.RunningPostResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.RunningPost
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class PostDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : PostDataSource {

    //  TODO : 조금 더 간결하게 처리 필요.
    override fun getAllPostFlow(): Flow<List<Post>> =
        callbackFlow {
            db.collection(CollectionId.POST_COLLECTION)
                .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    val postList = mutableListOf<Post>()
                    snapshot?.forEach { docuemnt ->

                        if (docuemnt[RUNNING_HISTORY_ID] != null) {
                            docuemnt.toObject(RunningPostResponse::class.java).let { postResponse ->
                                db.collection(CollectionId.USERS_COLLECTION)
                                    .document(postResponse.authorId)
                                    .get()
                                    .addOnSuccessListener { authorDocument ->
                                        val authorResponse =
                                            authorDocument?.toObject(UserResponse::class.java)

                                        authorResponse?.let {
                                            db.collection(CollectionId.RUNNING_HISTORY_COLLECTION)
                                                .document(postResponse.runningHistoryId)
                                                .get()
                                                .addOnSuccessListener { runningHistoryDocument ->
                                                    val runningHistoryResponse =
                                                        runningHistoryDocument.toObject(
                                                            RunningHistory::class.java
                                                        )

                                                    runningHistoryResponse?.let {
                                                        val author = authorResponse.toUser()

                                                        postList.add(
                                                            RunningPost(
                                                                postId = postResponse.postId,
                                                                author = author,
                                                                updatedAt = postResponse.updatedAt,
                                                                runningHistory = it,
                                                                likeCount = 0,
                                                                content = postResponse.content
                                                            )
                                                        )
                                                    }
                                                }
                                        }
                                    }
                            }
                        } else {
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
                                                        postList.add(
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
                                                        trySend(postList)
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                    }
                }

            awaitClose()
        }

    override fun getMyPostFlow(uid: String): Flow<List<Post>> =
        callbackFlow {
            db.collection(CollectionId.POST_COLLECTION)
                .whereEqualTo(AUTHOR_ID, uid)
                .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    val postList = mutableListOf<Post>()
                    snapshot?.forEach { docuemnt ->

                        if (docuemnt[RUNNING_HISTORY_ID] != null) {
                            docuemnt.toObject(RunningPostResponse::class.java).let { postResponse ->
                                db.collection(CollectionId.USERS_COLLECTION)
                                    .document(postResponse.authorId)
                                    .get()
                                    .addOnSuccessListener { authorDocument ->
                                        val authorResponse =
                                            authorDocument?.toObject(UserResponse::class.java)

                                        authorResponse?.let {
                                            db.collection(CollectionId.RUNNING_HISTORY_COLLECTION)
                                                .document(postResponse.runningHistoryId)
                                                .get()
                                                .addOnSuccessListener { runningHistoryDocument ->
                                                    val runningHistoryResponse =
                                                        runningHistoryDocument.toObject(
                                                            RunningHistory::class.java
                                                        )

                                                    runningHistoryResponse?.let {
                                                        val author = authorResponse.toUser()

                                                        postList.add(
                                                            RunningPost(
                                                                postId = postResponse.postId,
                                                                author = author,
                                                                updatedAt = postResponse.updatedAt,
                                                                runningHistory = it,
                                                                likeCount = 0,
                                                                content = postResponse.content
                                                            )
                                                        )
                                                    }
                                                }
                                        }
                                    }
                            }
                        } else {
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
                                                        postList.add(
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
                                                        trySend(postList)
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                    }
                }

            awaitClose()
        }

    override suspend fun createRecruitPost(
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

    override suspend fun createRunningPost(
        authorUid: String,
        runningHistoryId: String,
        content: String
    ): Result<Boolean> {
        val postId = UUID.randomUUID().toString()

        return runCatching {
            suspendCancellableCoroutine { cancellableContinuation ->

                db.collection(CollectionId.POST_COLLECTION)
                    .document(postId)
                    .set(
                        RunningPostResponse(
                            postId = postId,
                            authorId = authorUid,
                            updatedAt = System.currentTimeMillis(),
                            runningHistoryId = runningHistoryId,
                            content = content
                        )
                    ).addOnSuccessListener {
                        cancellableContinuation.resume(true)
                    }.addOnFailureListener {
                        cancellableContinuation.resume(false)
                    }
            }
        }
    }
}
