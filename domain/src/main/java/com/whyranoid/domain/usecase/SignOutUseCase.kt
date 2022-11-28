package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(): Result<Boolean> {
        return accountRepository.signOut()
    }
}
