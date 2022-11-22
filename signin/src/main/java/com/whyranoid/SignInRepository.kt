package com.whyranoid

interface SignInRepository {
    suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean
}
