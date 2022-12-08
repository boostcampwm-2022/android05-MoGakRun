package com.whyranoid

import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val signInDataSource: SignInDataSource) :
    SignInRepository {

    override suspend fun saveLogInUserInfo(userInfo: SignInUserInfo): Boolean {
        return signInDataSource.saveLogInUserInfo(userInfo)
    }

    override suspend fun restoreRunningHistoryData(uid: String): Result<Boolean> {
        return signInDataSource.restoreRunningHistoryData(uid)
    }
}
