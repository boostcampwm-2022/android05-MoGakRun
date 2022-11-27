package com.whyranoid.presentation.model

import android.os.Parcelable
import com.whyranoid.domain.model.GroupInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupInfoUiModel(
    val name: String,
    val groupId: String,
    val introduce: String,
    val rules: List<RuleUiModel>,
    val headCount: Int,
    val leader: UserUiModel
) : Parcelable

fun GroupInfoUiModel.toGroupInfo() =
    GroupInfo(
        name = this.name,
        groupId = this.groupId,
        introduce = this.introduce,
        rules = this.rules.map { rule ->
            rule.toRule()
        },
        headCount = this.headCount,
        leader = this.leader.toUser()
    )

fun GroupInfo.toGroupInfoUiModel() =
    GroupInfoUiModel(
        name = this.name,
        groupId = this.groupId,
        introduce = this.introduce,
        rules = this.rules.map { rule ->
            rule.toRuleUiModel()
        },
        headCount = this.headCount,
        leader = this.leader.toUserUiModel()
    )
