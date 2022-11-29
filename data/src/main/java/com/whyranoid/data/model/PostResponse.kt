package com.whyranoid.data.model

sealed interface PostResponse {
    val postId: String
    val author: String
    val updatedAt: Long
}

data class RecruitPostResponse(
    override val postId: String = "",
    override val author: String = "",
    override val updatedAt: Long = 0L,
    val groupUid: String = ""
) : PostResponse
