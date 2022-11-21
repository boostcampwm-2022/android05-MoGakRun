package com.whyranoid

interface SignInDataSource {
    suspend fun saveLogInUserInfo(userInfo: User): Boolean
}
