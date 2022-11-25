package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(uid: String, newNickname: String): Result<String> {
        return accountRepository.updateNickname(uid, newNickname)
    }
}
