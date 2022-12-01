package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import com.whyranoid.domain.repository.RunnerRepository
import com.whyranoid.domain.repository.RunningHistoryRepository
import javax.inject.Inject

class FinishRunningUseCase @Inject constructor(
    private val runnerRepository: RunnerRepository,
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository,
    private val runningHistoryRepository: RunningHistoryRepository
) {
    suspend operator fun invoke(runningHistory: RunningHistory? = null): Boolean {
        val uid = accountRepository.getUid()

        runnerRepository.finishRunning(uid)

        if (runningHistory != null) {
            val uploadResult = runningHistoryRepository.uploadRunningHistory(runningHistory)

            if (uploadResult.isFailure) {
                return false
            }

            groupRepository.getMyGroupList(uid).onSuccess { groupInfos ->
                groupRepository.notifyRunningFinish(
                    uid = uid,
                    runningHistory = runningHistory,
                    groupIdList = groupInfos.map { it.groupId }
                )
            }
        }
        return true
    }
}
