package com.whyranoid

interface SignInDataSource {
    suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean
    suspend fun restoreRunningHistoryData(uid: String): Result<Boolean>
}
