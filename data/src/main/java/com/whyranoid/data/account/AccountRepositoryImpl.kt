package com.whyranoid.data.account

import com.whyranoid.domain.model.User
import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

// TODO AccountRepositoryImpl 재구현 필요!! 현재는 Fake 상태
class AccountRepositoryImpl @Inject constructor() : AccountRepository {
    override suspend fun getUser(): Result<User> {
        return Result.success(User("byeonghee-uid", "병희", "github.com/bngsh"))
    }

    override suspend fun loginUser(): Boolean {
        return true
    }

    override suspend fun getUid(): Result<String> {
        return Result.success("byeonghee-uid")
    }

    override suspend fun getNickname(): Result<String> {
        return Result.success("병희")
    }

    override suspend fun updateNickname(newNickname: String): Boolean {
        return true
    }

    override suspend fun getProfileUrl(): Result<String> {
        return Result.success("github.com/bngsh")
    }

    override suspend fun updateProfileUrl(newProfileUrl: String): Boolean {
        return true
    }
}
