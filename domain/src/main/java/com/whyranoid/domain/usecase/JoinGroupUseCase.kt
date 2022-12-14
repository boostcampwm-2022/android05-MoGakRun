package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(groupId: String): Boolean {
        groupRepository.joinGroup(accountRepository.getUid(), groupId)
        return true
    }
}
