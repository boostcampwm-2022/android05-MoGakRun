package com.whyranoid.data.group

import com.whyranoid.data.groupnotification.GroupNotificationDataSource
import com.whyranoid.data.user.UserDataSource
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val groupDataSource: GroupDataSource,
    private val groupNotificationDataSource: GroupNotificationDataSource
) : GroupRepository {

    override suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>> {
        return userDataSource.getMyGroupList(uid)
    }

    override fun getMyGroupListFlow(uid: String) = userDataSource.getMyGroupListFlow(uid)

    override suspend fun updateGroupInfo(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean {
        return groupDataSource.updateGroupInfo(groupId, groupName, groupIntroduce, rules)
    }

    override suspend fun joinGroup(uid: String, groupId: String): Boolean {
        return groupDataSource.joinGroup(uid, groupId)
    }

    override suspend fun exitGroup(uid: String, groupId: String): Boolean {
        return groupDataSource.exitGroup(uid, groupId)
    }

    override fun getGroupInfoFlow(uid: String, groupId: String): Flow<GroupInfo> {
        return groupDataSource.getGroupInfoFlow(uid, groupId)
    }

    override fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>> {
        return groupNotificationDataSource.getGroupNotifications(groupId)
    }

    override suspend fun notifyRunningStart(uid: String, groupIdList: List<String>) {
        groupNotificationDataSource.notifyRunningStart(uid, groupIdList)
    }

    override suspend fun notifyRunningFinish(
        uid: String,
        runningHistory: RunningHistory,
        groupIdList: List<String>
    ) {
        groupNotificationDataSource.notifyRunningFinish(uid, runningHistory, groupIdList)
    }

    override suspend fun createGroup(
        groupName: String,
        introduce: String,
        rules: List<String>,
        uid: String
    ): Boolean {
        return groupDataSource.createGroup(groupName, introduce, rules, uid)
    }

    override suspend fun deleteGroup(uid: String, groupId: String): Boolean {
        return groupDataSource.deleteGroup(uid, groupId)
    }

    override suspend fun isDuplicatedGroupName(groupName: String): Boolean {
        return groupDataSource.isDuplicatedGroupName(groupName)
    }
}
