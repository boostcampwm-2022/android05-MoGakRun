package com.whyranoid.data.model

import com.whyranoid.domain.model.DayOfWeek
import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.model.User

data class UserResponse(
    val uid: String = "",
    val name: String = "",
    val profileUrl: String = "",
    val joinedGroupList: List<String> = emptyList()
)

fun UserResponse.toUser() =
    User(
        uid = this.uid,
        name = this.name,
        profileUrl = this.profileUrl
    )

fun String.toRule(): Rule {
    val ruleString = this.split("-")
    return Rule(
        dayOfWeek = DayOfWeek.values().find { it.dayResId == ruleString[0] } ?: DayOfWeek.SUN,
        hour = ruleString[1].toInt(),
        minute = ruleString[2].toInt()
    )
}
