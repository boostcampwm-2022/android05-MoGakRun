package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import com.whyranoid.domain.repository.RunningRepository
import javax.inject.Inject

class StartRunningUseCase @Inject constructor(
    private val runningRepository: RunningRepository,
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): Boolean {
        accountRepository.getUid().collect { uid ->
            runningRepository.startRunning(uid)
            groupRepository.getMyGroupList(uid).onSuccess { groupInfos ->
                groupRepository.notifyRunningStart(uid, groupInfos.map { it.groupId })
            }
        }
        return false
    }
}
