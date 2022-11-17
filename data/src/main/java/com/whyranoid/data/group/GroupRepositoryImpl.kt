package com.whyranoid.data.group

import com.whyranoid.data.user.UserDataSource
import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val groupDataSource: GroupDataSource
) : GroupRepository {

    override suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>> {
        return userDataSource.getMyGroupList(uid)
    }

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

    override fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>> {
        TODO("Not yet implemented")
    }
}