package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(uid: String, groupId: String): Boolean {
        return groupRepository.joinGroup(uid, groupId)
    }
}
