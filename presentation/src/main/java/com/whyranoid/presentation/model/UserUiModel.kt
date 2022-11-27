package com.whyranoid.presentation.model

import android.os.Parcelable
import com.whyranoid.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUiModel(
    val uid: String,
    val name: String?,
    val profileUrl: String?
) : Parcelable

fun UserUiModel.toUser() =
    User(
        uid = this.uid,
        name = this.name,
        profileUrl = this.profileUrl
    )

fun User.toUserUiModel() =
    UserUiModel(
        uid = this.uid,
        name = this.name,
        profileUrl = this.profileUrl
    )
