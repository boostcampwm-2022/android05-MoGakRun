package com.whyranoid.data.account

import kotlinx.coroutines.flow.Flow

interface AccountDataSource {
    fun getUserNickName(): Flow<String>
    fun getUserProfileImgUri(): Flow<String>
    suspend fun updateUserNickName(newNickName: String): Result<String>
}
