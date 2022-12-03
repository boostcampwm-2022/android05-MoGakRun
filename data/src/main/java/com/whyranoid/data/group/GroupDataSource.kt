package com.whyranoid.data.group

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.Rule
import kotlinx.coroutines.flow.Flow

interface GroupDataSource {

    suspend fun updateGroupInfo(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean

    suspend fun joinGroup(uid: String, groupId: String): Boolean

    suspend fun exitGroup(uid: String, groupId: String): Boolean

    suspend fun createGroup(
        groupName: String,
        introduce: String,
        rules: List<String>,
        uid: String
    ): Boolean

    fun getGroupInfoFlow(uid: String, groupId: String): Flow<GroupInfo>

    suspend fun isDuplicatedGroupName(groupName: String): Boolean
}
