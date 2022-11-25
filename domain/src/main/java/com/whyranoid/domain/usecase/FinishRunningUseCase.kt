package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunningRepository
import javax.inject.Inject

class FinishRunningUseCase @Inject constructor(
    private val runningRepository: RunningRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Boolean {
        accountRepository.getUid().collect { uid ->
            runningRepository.finishRunning(uid)
        }
        return true
    }
}
