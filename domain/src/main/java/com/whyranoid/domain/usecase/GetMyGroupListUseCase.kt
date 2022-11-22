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
    // TODO accountRepository에서 uid를 받아온 후 동작하도록 수정
    operator fun invoke(): Flow<List<GroupInfo>> {
        return groupRepository.getMyGroupListFlow("hsjeon")
    }
}
