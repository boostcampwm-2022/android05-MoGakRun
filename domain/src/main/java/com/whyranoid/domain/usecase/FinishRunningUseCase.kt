package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunnerRepository
import javax.inject.Inject

class FinishRunningUseCase @Inject constructor(
    private val runnerRepository: RunnerRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Boolean {
        accountRepository.getUid().collect { uid ->
            runnerRepository.finishRunning(uid)
        }
        return true
    }
}
