package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.GroupInfo
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class GetMyGroupListUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(uid: String): Result<List<GroupInfo>> {
        return groupRepository.getMyGroupList(uid)
    }
}
