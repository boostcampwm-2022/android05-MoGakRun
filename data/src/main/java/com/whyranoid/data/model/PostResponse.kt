package com.whyranoid.data.model

sealed interface PostResponse {
    val postId: String
    val authorId: String
    val updatedAt: Long
}

data class RecruitPostResponse(
    override val authorId: String = "",
    val groupId: String = "",
    override val postId: String = "",
    override val updatedAt: Long = 0L
) : PostResponse

data class RunningPostResponse(
    override val postId: String = "",
    override val authorId: String = "",
    override val updatedAt: Long = 0L,
    val runningHistoryId: String = "",
    val content: String = ""
) : PostResponse
