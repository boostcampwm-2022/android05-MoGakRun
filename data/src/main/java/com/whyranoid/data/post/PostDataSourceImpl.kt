package com.whyranoid.data.post

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.constant.FieldId.AUTHOR_ID
import com.whyranoid.data.constant.FieldId.GROUP_ID
import com.whyranoid.data.constant.FieldId.RUNNING_HISTORY_ID
import com.whyranoid.data.constant.FieldId.UPDATED_AT
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.RecruitPostResponse
import com.whyranoid.data.model.RunningPostResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.MoGakRunException
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.RunningPost
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class PostDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : PostDataSource {

    override suspend fun getCurrentPagingPost(key: QuerySnapshot?): QuerySnapshot {
        return key ?: db.collection(CollectionId.POST_COLLECTION)
            .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
            .limit(DATA_COUNT_PER_PAGE)
            .get()
            .await()
    }

    override suspend fun getNextPagingPost(lastDocumentSnapshot: DocumentSnapshot): QuerySnapshot {
        return db.collection(CollectionId.POST_COLLECTION)
            .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
            .limit(DATA_COUNT_PER_PAGE).startAfter(lastDocumentSnapshot)
            .get()
            .await()
    }

    override suspend fun getMyCurrentPagingPost(key: QuerySnapshot?, uid: String): QuerySnapshot {
        return key ?: db.collection(CollectionId.POST_COLLECTION)
            .whereEqualTo(AUTHOR_ID, uid)
            .limit(DATA_COUNT_PER_PAGE)
            .get()
            .await()
    }

    override suspend fun getMyNextPagingPost(
        lastDocumentSnapshot: DocumentSnapshot,
        uid: String
    ): QuerySnapshot {
        return db.collection(CollectionId.POST_COLLECTION)
            .whereEqualTo(AUTHOR_ID, uid)
            .limit(DATA_COUNT_PER_PAGE)
            .startAfter(lastDocumentSnapshot)
            .get()
            .await()
    }

    override suspend fun convertPostType(document: QueryDocumentSnapshot): Result<Post> {
        return if (document[RUNNING_HISTORY_ID] != null) {
            document.toObject(RunningPostResponse::class.java).let { postResponse ->

                val authorResponseResult = getUserResponse(postResponse.authorId)
                authorResponseResult.mapCatching { authorResponse ->

                    val runningHistoryResult = getRunningHistory(postResponse.runningHistoryId)
                    runningHistoryResult.mapCatching { runningHistory ->

                        RunningPost(
                            postId = postResponse.postId,
                            author = authorResponse.toUser(),
                            updatedAt = postResponse.updatedAt,
                            runningHistory = runningHistory,
                            likeCount = 0,
                            content = postResponse.content
                        )
                    }.getOrThrow()
                }
            }

        } else {
            document.toObject(RecruitPostResponse::class.java).let { postResponse ->
                val authorResponseResult = getUserResponse(postResponse.authorId)
                authorResponseResult.mapCatching { authorResponse ->

                    val groupInfoResponseResult = getGroupInfoResponse(postResponse.groupId)
                    groupInfoResponseResult.mapCatching { groupInfoResponse ->

                        val author = authorResponse.toUser()
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
                    }.getOrThrow()
                }
            }
        }
    }

    override suspend fun createRecruitPost(
        authorUid: String,
        groupUid: String
    ): Boolean {
        val postId = UUID.randomUUID().toString()

        val previousRecruitPost = db.collection(CollectionId.POST_COLLECTION)
            .whereEqualTo(GROUP_ID, groupUid)
            .get()
            .await()

        return if (previousRecruitPost.isEmpty) {
            suspendCancellableCoroutine { cancellableContinuation ->

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
        } else {
            suspendCancellableCoroutine { cancellableContinuation ->

                val previousRecruitPostId =
                    previousRecruitPost.toObjects(RecruitPostResponse::class.java).first().postId

                db.collection(CollectionId.POST_COLLECTION)
                    .document(previousRecruitPostId)
                    .update(
                        mapOf(
                            UPDATED_AT to System.currentTimeMillis()
                        )
                    ).addOnSuccessListener {
                        cancellableContinuation.resume(true)
                    }.addOnFailureListener {
                        cancellableContinuation.resume(false)
                    }
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

    override suspend fun deletePost(postId: String): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            db.collection(CollectionId.POST_COLLECTION)
                .document(postId)
                .delete()
                .addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    private suspend fun getUserResponse(userId: String): Result<UserResponse> {
        return runCatching {
            db.collection(CollectionId.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(UserResponse::class.java)
                ?: throw MoGakRunException.FileNotFoundedException
        }
    }

    private suspend fun getRunningHistory(runningHistoryId: String): Result<RunningHistory> {
        return runCatching {
            db.collection(CollectionId.RUNNING_HISTORY_COLLECTION)
                .document(runningHistoryId)
                .get()
                .await()
                .toObject(RunningHistory::class.java)
                ?: throw MoGakRunException.FileNotFoundedException
        }
    }

    private suspend fun getGroupInfoResponse(groupId: String): Result<GroupInfoResponse> {
        return runCatching {
            db.collection(CollectionId.GROUPS_COLLECTION)
                .document(groupId)
                .get()
                .await()
                .toObject(GroupInfoResponse::class.java)
                ?: throw MoGakRunException.FileNotFoundedException
        }
    }

    companion object {
        private const val DATA_COUNT_PER_PAGE = 10L
    }
}
