package com.whyranoid.data.account

import com.whyranoid.domain.model.User
import com.whyranoid.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO AccountRepositoryImpl 재구현 필요!! 현재는 Fake 상태
class AccountRepositoryImpl @Inject constructor(
    private val accountDataSource: AccountDataSource
) : AccountRepository {
    override suspend fun getUser(): Result<User> {
        return Result.success(User("byeonghee-uid", "병희", "github.com/bngsh"))
    }

    override suspend fun loginUser(): Boolean {
        return true
    }

    override suspend fun getUid(): Result<String> {
        return Result.success("byeonghee-uid")
    }

    override suspend fun getNickname(): Flow<String> {
        return accountDataSource.getUserNickName()
    }

    override suspend fun updateNickname(newNickName: String): Result<String> {
        return accountDataSource.updateUserNickName(newNickName)
    }

    override suspend fun getProfileUri(): Flow<String> {
        return accountDataSource.getUserProfileImgUri()
    }

    override suspend fun updateProfileUrl(newProfileUrl: String): Boolean {
        return true
    }
}
