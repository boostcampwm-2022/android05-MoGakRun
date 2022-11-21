package com.whyranoid

interface SignInRepository {
    fun saveLogInUserInfo(uid: String, nickName: String, profileImgUrl: String)
}
