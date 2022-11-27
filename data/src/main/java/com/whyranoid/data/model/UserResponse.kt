package com.whyranoid.data.model

import com.whyranoid.domain.model.User

data class UserResponse(
    val uid: String = "",
    val name: String? = "",
    val profileUrl: String? = "",
    val joinedGroupList: List<String> = emptyList()
)

fun UserResponse.toUser() =
    User(
        uid = this.uid,
        name = this.name,
        profileUrl = this.profileUrl
    )
