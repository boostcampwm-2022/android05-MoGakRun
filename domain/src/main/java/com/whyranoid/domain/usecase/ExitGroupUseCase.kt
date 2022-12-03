package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class ExitGroupUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: String): Boolean {
        return groupRepository.exitGroup(accountRepository.getUid(), groupId)
    }
}
