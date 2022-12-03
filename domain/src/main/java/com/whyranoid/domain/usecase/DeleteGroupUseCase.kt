package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

// TODO : 그룹원들은 어떻게 그룹을 나가게 하지?
class DeleteGroupUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: String): Boolean {
        return groupRepository.deleteGroup(accountRepository.getUid(), groupId)
    }
}
