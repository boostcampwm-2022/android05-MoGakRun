package com.whyranoid.data.model

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.User

data class GroupInfoResponse(
    val groupId: String = "",
    val groupName: String = "",
    val introduce: String = "",
    val leaderId: String = "",
    val membersId: List<String> = emptyList(),
    val rules: List<String> = emptyList()
)

fun GroupInfoResponse.toGroupInfo(leader: User, rules: List<Rule>) =
    GroupInfo(
        name = this.groupName,
        groupId = this.groupId,
        introduce = this.introduce,
        rules = rules,
        headCount = this.membersId.size,
        leader = leader
    )
