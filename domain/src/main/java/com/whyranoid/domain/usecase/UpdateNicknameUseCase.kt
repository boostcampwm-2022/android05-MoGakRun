package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(newNickname: String): Result<String> {
        return accountRepository.updateNickname(newNickname)
    }
}
