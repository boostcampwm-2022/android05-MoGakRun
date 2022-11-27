package com.whyranoid.data.account

import kotlinx.coroutines.flow.Flow

interface AccountDataSource {
    fun getUserNickName(): Flow<String>
    fun getUserProfileImgUri(): Flow<String>
    fun getUserUid(): Flow<String>
    fun getEmail(): Flow<Result<String>>
    suspend fun updateUserNickName(uid: String, newNickName: String): Result<String>
    suspend fun signOut(): Result<Boolean>
    suspend fun withDrawal(): Result<Boolean>
}
