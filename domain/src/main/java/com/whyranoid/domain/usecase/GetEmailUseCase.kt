package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmailUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    operator fun invoke(): Flow<Result<String>> = accountRepository.getEmail()
}
