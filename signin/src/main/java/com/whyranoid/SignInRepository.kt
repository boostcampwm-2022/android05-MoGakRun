package com.whyranoid

interface SignInRepository {
    suspend fun saveLogInUserInfo(userInfo: User): Boolean
}
