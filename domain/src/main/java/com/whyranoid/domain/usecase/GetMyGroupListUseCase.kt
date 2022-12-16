package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyGroupListUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Flow<Result<List<GroupInfo>>> {
        val uid = accountRepository.getUid()
        return groupRepository.getMyGroupListFlow(uid)
    }
}
