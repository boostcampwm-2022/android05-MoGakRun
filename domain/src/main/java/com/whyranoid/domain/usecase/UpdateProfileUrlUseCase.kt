package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateProfileUrlUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(newProfileUrl: String): Boolean {
        return accountRepository.updateProfileUrl(newProfileUrl)
    }
}
