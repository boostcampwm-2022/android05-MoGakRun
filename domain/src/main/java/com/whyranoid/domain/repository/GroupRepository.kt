package com.whyranoid.domain.repository

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.RunningHistory
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    suspend fun getMyGroupList(uid: String): Result<List<GroupInfo>>

    fun getMyGroupListFlow(uid: String): Flow<List<GroupInfo>>

    // 그룹 정보 수정, 홍보 글 수정
    suspend fun updateGroupInfo(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean

    // 서버에는 해당 그룹에 내 정보 넘겨주기
    suspend fun joinGroup(uid: String, groupId: String): Boolean

    // 그룹 나가기 / 그룹에서 먼저 나간 후 성공하면 User 에 반영하기
    suspend fun exitGroup(uid: String, groupId: String): Boolean

    // 그룹의 정보를 Flow로 가져오기
    fun getGroupInfoFlow(uid: String, groupId: String): Flow<GroupInfo>

    // 혹은 그룹 채팅을 가져오기 + 글 작성하기
    fun getGroupNotifications(groupId: String): Flow<List<GroupNotification>>

    suspend fun notifyRunningStart(uid: String, groupIdList: List<String>)

    suspend fun notifyRunningFinish(
        uid: String,
        runningHistory: RunningHistory,
        groupIdList: List<String>
    )

    // 그룹 생성하기
    suspend fun createGroup(
        groupName: String,
        introduce: String,
        rules: List<String>,
        uid: String
    ): Boolean

    suspend fun isDuplicatedGroupName(groupName: String): Boolean
}
