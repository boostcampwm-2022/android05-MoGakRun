package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunningRepository
import javax.inject.Inject

class StartRunningUseCase @Inject constructor(
    private val runningRepository: RunningRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Boolean {
        accountRepository.getUid().onSuccess { uid ->
            return runningRepository.startRunning(uid)
        }.onFailure {
            println("UID 또 이상해")
        }
        return false
    }
}
