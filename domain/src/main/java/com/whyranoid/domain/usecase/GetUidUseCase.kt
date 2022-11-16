package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class GetUidUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(): Result<String> {
        return accountRepository.getUid()
    }
}
