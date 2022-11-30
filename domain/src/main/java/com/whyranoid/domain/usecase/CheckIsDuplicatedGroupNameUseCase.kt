package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class CheckIsDuplicatedGroupNameUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {

    suspend operator fun invoke(groupName: String): Boolean {
        return groupRepository.isDuplicatedGroupName(groupName)
    }
}
