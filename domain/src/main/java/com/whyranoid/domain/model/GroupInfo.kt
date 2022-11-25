package com.whyranoid.domain.model

data class GroupInfo(
    val name: String,
    val groupId: String,
    val introduce: String,
    val rules: List<Rule>,
    val headCount: Int,
    val leader: User
)
