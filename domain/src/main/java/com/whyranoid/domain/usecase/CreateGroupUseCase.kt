package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    // TODO AccountRepository에서 uid를 가져와야함.
    suspend operator fun invoke(groupName: String, introduce: String, uid: String): Boolean {
        return groupRepository.createGroup(groupName, introduce, uid)
    }
}
