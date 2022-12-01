package com.whyranoid.data.group

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId.GROUPS_COLLECTION
import com.whyranoid.data.constant.CollectionId.USERS_COLLECTION
import com.whyranoid.data.constant.FieldId.GROUP_INTRODUCE
import com.whyranoid.data.constant.FieldId.GROUP_MEMBERS_ID
import com.whyranoid.data.constant.FieldId.GROUP_NAME
import com.whyranoid.data.constant.FieldId.JOINED_GROUP_LIST
import com.whyranoid.data.constant.FieldId.RULES
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.MoGakRunException
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

class GroupDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun updateGroupInfo(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    mapOf(
                        GROUP_NAME to groupName,
                        GROUP_INTRODUCE to groupIntroduce,
                        RULES to rules.map { rule ->
                            rule.toString()
                        }
                    )
                )
                .addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    suspend fun joinGroup(uid: String, groupId: String): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    GROUP_MEMBERS_ID,
                    FieldValue.arrayUnion(uid)
                ).addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    suspend fun exitGroup(uid: String, groupId: String): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    mapOf(
                        GROUP_MEMBERS_ID to FieldValue.arrayRemove(uid)
                    )
                ).addOnSuccessListener {
                    db.collection(USERS_COLLECTION)
                        .document(uid)
                        .update(
                            mapOf(
                                JOINED_GROUP_LIST to FieldValue.arrayRemove(groupId)
                            )
                        )
                        .addOnSuccessListener {
                            cancellableContinuation.resume(true)
                        }
                        .addOnFailureListener {
                            cancellableContinuation.resume(false)
                        }
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    suspend fun createGroup(
        groupName: String,
        introduce: String,
        rules: List<String>,
        uid: String
    ): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val newGroupId = UUID.randomUUID().toString()
            db.collection(GROUPS_COLLECTION)
                .document(newGroupId)
                .set(
                    GroupInfoResponse(
                        groupId = newGroupId,
                        groupName = groupName,
                        introduce = introduce,
                        leaderId = uid,
                        membersId = listOf(uid),
                        rules = rules
                    )
                ).addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    fun getGroupInfoFlow(uid: String, groupId: String): Flow<GroupInfo> {
        return callbackFlow {
            val groupInfoResponse = getGroupInfoResponse(groupId)
            val userResponse = getUserInfoResponse(uid)
            val groupInfo = groupInfoResponse.toGroupInfo(
                leader = userResponse.toUser(),
                rules = groupInfoResponse.rules.map { it.toRule() }
            )
            trySend(groupInfo)
            awaitClose()
        }
    }

    private suspend fun getGroupInfoResponse(groupId: String): GroupInfoResponse {
        return suspendCancellableCoroutine { continuation ->
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .addSnapshotListener { documentSnapshot, _ ->
                    continuation.resume(
                        documentSnapshot?.toObject(GroupInfoResponse::class.java)
                            ?: throw MoGakRunException.FileNotFoundedException

                    )
                }
        }
    }

    private suspend fun getUserInfoResponse(uid: String): UserResponse {
        return suspendCancellableCoroutine { continuation ->
            db.collection(USERS_COLLECTION)
                .document(uid)
                .addSnapshotListener { documentSnapshot, _ ->
                    continuation.resume(
                        documentSnapshot?.toObject(UserResponse::class.java)
                            ?: throw MoGakRunException.FileNotFoundedException
                    )
                }
        }
    }

    suspend fun isDuplicatedGroupName(groupName: String): Boolean {
        return db.collection(GROUPS_COLLECTION)
            .whereEqualTo(GROUP_NAME, groupName)
            .get()
            .await()
            .isEmpty
            .not()
    }
}
