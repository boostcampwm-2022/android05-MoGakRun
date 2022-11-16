package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.User
import com.whyranoid.domain.repository.AccountRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(): Result<User> {
        return accountRepository.getUser()
    }
}
