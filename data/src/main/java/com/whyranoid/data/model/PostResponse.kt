package com.whyranoid.data.model

import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.toRule

sealed interface PostResponse {
    val postId: String
    val author: UserResponse
    val updatedAt: Long
}

data class RecruitPostResponse(
    override val postId: String = "",
    override val author: UserResponse = UserResponse(),
    override val updatedAt: Long = 0L,
    val groupInfo: GroupInfoResponse = GroupInfoResponse()
) : PostResponse

fun RecruitPostResponse.toRecruitPost(): RecruitPost {
    val leader = this.author.toUser()
    return RecruitPost(
        postId = this.postId,
        author = leader,
        updatedAt = this.updatedAt,
        groupInfo = this.groupInfo.toGroupInfo(
            leader,
            this.groupInfo.rules.map {
                it.toRule()
            }
        )
    )
}
