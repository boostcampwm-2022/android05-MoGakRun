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
