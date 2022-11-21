package com.whyranoid.data.user

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId.GROUPS_COLLECTION
import com.whyranoid.data.constant.CollectionId.USERS_COLLECTION
import com.whyranoid.data.constant.Exceptions.NO_GROUP_EXCEPTION
import com.whyranoid.data.constant.Exceptions.NO_JOINED_GROUP_EXCEPTION
import com.whyranoid.data.constant.Exceptions.NO_USER_EXCEPTION
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toRule
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.GroupInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>> {
        val myGroupInfoList = mutableListOf<GroupInfo>()

        return runCatching {
            val joinedGroupList = db.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()
                .toObject(UserResponse::class.java)
                ?.joinedGroupList ?: throw NO_USER_EXCEPTION

            joinedGroupList.forEach { groupId ->

                val groupInfoResponse = db.collection(GROUPS_COLLECTION)
                    .document(groupId)
                    .get()
                    .await()
                    .toObject(GroupInfoResponse::class.java)
                    ?: throw NO_GROUP_EXCEPTION

                val leader = db.collection(USERS_COLLECTION)
                    .document(uid)
                    .get()
                    .await()
                    .toObject(UserResponse::class.java)
                    ?.toUser() ?: throw NO_USER_EXCEPTION

                val groupInfo = groupInfoResponse.toGroupInfo(
                    leader = leader,
                    rules = groupInfoResponse.rules.map {
                        it.toRule()
                    }
                )

                myGroupInfoList.add(groupInfo)
            }
            if (myGroupInfoList.size == 0) throw NO_JOINED_GROUP_EXCEPTION else myGroupInfoList
        }
    }

    // TODO: 콜백을 suspend로 변경
    // TODO: 예외처리
    fun getMyGroupListFlow(uid: String): Flow<List<GroupInfo>> = callbackFlow {
        db.collection(USERS_COLLECTION)
            .document(uid)
            .addSnapshotListener { documentSnapshot, _ ->
                val myGroupInfoList = mutableListOf<GroupInfo>()
                val joinedGroupList =
                    documentSnapshot?.toObject(UserResponse::class.java)?.joinedGroupList

                joinedGroupList?.forEach { groupId ->

                    db.collection(GROUPS_COLLECTION)
                        .document(groupId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->

                            val groupInfoResponse =
                                documentSnapshot.toObject(GroupInfoResponse::class.java)

                            groupInfoResponse?.let {
                                db.collection(USERS_COLLECTION)
                                    .document(it.leaderId)
                                    .get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        val leader =
                                            documentSnapshot.toObject(UserResponse::class.java)
                                                ?.toUser()

                                        leader?.let {
                                            val groupInfo = groupInfoResponse.toGroupInfo(
                                                leader = leader,
                                                rules = groupInfoResponse.rules.map {
                                                    it.toRule()
                                                }
                                            )
                                            myGroupInfoList.add(groupInfo)
                                            trySend(myGroupInfoList)
                                        }
                                    }
                            }
                        }
                }
            }

        awaitClose()
    }
}
