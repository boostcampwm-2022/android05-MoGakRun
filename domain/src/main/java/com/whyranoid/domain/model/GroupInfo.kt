package com.whyranoid.domain.model

import java.io.Serializable

data class GroupInfo(
    val name: String,
    val groupId: String,
    val introduce: String,
    val rules: List<Rule>,
    val headCount: Int,
    val leader: User
) : Serializable
