package com.whyranoid

import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val signInDataSource: SignInDataSource) :
    SignInRepository {

    override suspend fun saveLogInUserInfo(userInfo: User): Boolean {
        return signInDataSource.saveLogInUserInfo(userInfo)
    }
}
