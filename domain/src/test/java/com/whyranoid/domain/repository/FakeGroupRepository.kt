package com.whyranoid.domain.repository

import com.whyranoid.domain.model.FakeGroupInfo
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val fakeGroupRepository =
    object : GroupRepository {

        override fun getGroupInfoFlow(uid: String, groupId: String): Flow<GroupInfo> =
            flow {
                if (groupId == "fake") emit(FakeGroupInfo.getInstance())
            }

        override suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>> {
            TODO("Not yet implemented")
        }

        override fun getMyGroupListFlow(uid: String): Flow<List<GroupInfo>> {
            TODO("Not yet implemented")
        }

        override suspend fun updateGroupInfo(
            groupId: String,
            groupName: String,
            groupIntroduce: String,
            rules: List<Rule>
        ): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun joinGroup(uid: String, groupId: String): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun exitGroup(uid: String, groupId: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>> {
            TODO("Not yet implemented")
        }

        override suspend fun notifyRunningStart(uid: String, groupIdList: List<String>) {
            TODO("Not yet implemented")
        }

        override suspend fun notifyRunningFinish(
            uid: String,
            runningHistory: RunningHistory,
            groupIdList: List<String>
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun createGroup(
            groupName: String,
            introduce: String,
            rules: List<String>,
            uid: String
        ): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun deleteGroup(uid: String, groupId: String): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun isDuplicatedGroupName(groupName: String): Boolean {
            TODO("Not yet implemented")
        }
    }
