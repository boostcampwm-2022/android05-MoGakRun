package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunningRepository
import javax.inject.Inject

class FinishRunningUseCase @Inject constructor(
    private val runningRepository: RunningRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Boolean {
        accountRepository.getUid().onSuccess { uid ->
            return runningRepository.finishRunning(uid)
        }.onFailure {
            println("uid 이상해 희희")
        }
        return false
    }
}
