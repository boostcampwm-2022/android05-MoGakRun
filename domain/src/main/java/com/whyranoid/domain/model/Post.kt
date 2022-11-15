package com.whyranoid.domain.model

sealed interface Post {
    val postId: String
    val author: User
    val updatedAt: Long
}

data class RecruitPost(
    override val postId: String,
    override val author: User,
    override val updatedAt: Long,
    val groupInfo: GroupInfo
) : Post

data class RunningPost(
    override val postId: String,
    override val author: User,
    override val updatedAt: Long,
    val runningHistory: RunningHistory,
    val likeCount: Int,
    val content: String
) : Post
