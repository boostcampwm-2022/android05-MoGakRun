package com.whyranoid

interface SignInRepository {
    suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean
    suspend fun restoreRunningHistoryData(uid: String): Result<Boolean>
}
