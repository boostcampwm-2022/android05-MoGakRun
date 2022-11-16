package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(newNickname: String): Boolean {
        return accountRepository.updateNickname(newNickname)
    }
}
