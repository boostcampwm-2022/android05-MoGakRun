package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import com.whyranoid.domain.repository.RunnerRepository
import javax.inject.Inject

class StartRunningUseCase @Inject constructor(
    private val runnerRepository: RunnerRepository,
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): Boolean {
        runnerRepository.startRunning(accountRepository.getUid())
        groupRepository.getMyGroupList(accountRepository.getUid()).onSuccess { groupInfos ->
            groupRepository.notifyRunningStart(
                accountRepository.getUid(),
                groupInfos.map { it.groupId }
            )
        }
        return false
    }
}
