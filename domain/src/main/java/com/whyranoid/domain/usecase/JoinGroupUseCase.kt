package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(groupId: String): Boolean {
        accountRepository.getUid().onSuccess { uid ->
            return groupRepository.joinGroup(uid, groupId)
        }.onFailure {
            println("UID 이상해 병희희")
        }
        return false
    }
}
