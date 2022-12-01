package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupName: String, introduce: String, rules: List<String>): Boolean {
        val uid = accountRepository.getUid()
        return groupRepository.createGroup(groupName, introduce, rules = rules, uid = uid)
    }
}
