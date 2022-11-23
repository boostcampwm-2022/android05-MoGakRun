package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUriUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(): Flow<String> {
        return accountRepository.getProfileUri()
    }
}
