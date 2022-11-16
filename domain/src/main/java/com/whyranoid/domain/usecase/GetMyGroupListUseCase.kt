package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class GetMyGroupListUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Result<List<GroupInfo>> {
        accountRepository.getUid().onSuccess { uid ->
            return groupRepository.getMyGroupList(uid)
        }.onFailure {
            println("UID 이상해 희희")
        }
        return Result.failure(Exception("UID 이상해 희희"))
    }
}
