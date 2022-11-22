package com.whyranoid

interface SignInDataSource {
    suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean
}
