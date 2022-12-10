package com.whyranoid.data.user

import com.google.firebase.firestore.FirebaseFirestore
import com.whyranoid.data.constant.CollectionId.GROUPS_COLLECTION
import com.whyranoid.data.constant.CollectionId.USERS_COLLECTION
import com.whyranoid.data.constant.Exceptions.NO_GROUP_EXCEPTION
import com.whyranoid.data.constant.Exceptions.NO_JOINED_GROUP_EXCEPTION
import com.whyranoid.data.constant.Exceptions.NO_USER_EXCEPTION
import com.whyranoid.data.di.IODispatcher
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.User
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : UserDataSource {

    override suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>> {
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

    // TODO: 예외처리
    override fun getMyGroupListFlow(
        uid: String
    ): Flow<List<GroupInfo>> = callbackFlow {
        val coroutineScope = CoroutineScope(dispatcher)
        db.collection(USERS_COLLECTION)
            .document(uid)
            .addSnapshotListener { documentSnapshot, _ ->
                val myGroupInfoList = mutableListOf<GroupInfo>()
                val joinedGroupList =
                    documentSnapshot?.toObject(UserResponse::class.java)?.joinedGroupList

                coroutineScope.launch {
                    joinedGroupList?.forEach { groupId ->
                        val groupInfo = getGroupInfo(groupId)
                        myGroupInfoList.add(groupInfo)
                        trySend(myGroupInfoList)
                    }
                }
            }

        awaitClose {
            coroutineScope.cancel()
        }
    }

    // TODO : 예외 처리
    private suspend fun getGroupInfo(groupId: String): GroupInfo {
        val groupInfoResponse = requireNotNull(
            db.collection(GROUPS_COLLECTION)
                .document(groupId)
                .get()
                .await()
                .toObject(GroupInfoResponse::class.java)
        )

        val leader = getLeaderInfo(groupInfoResponse.leaderId)

        return groupInfoResponse.toGroupInfo(
            leader = leader,
            rules = groupInfoResponse.rules.map {
                it.toRule()
            }
        )
    }

    // TODO : 예외 처리
    private suspend fun getLeaderInfo(leaderId: String): User {
        return requireNotNull(
            db.collection(USERS_COLLECTION)
                .document(leaderId)
                .get()
                .await()
                .toObject(UserResponse::class.java)?.toUser()
        )
    }
}
