package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.Rule
import com.whyranoid.domain.repository.GroupRepository
import javax.inject.Inject

class UpdateGroupInfoUseCase @Inject constructor(private val groupRepository: GroupRepository) {

    suspend operator fun invoke(
        groupId: String,
        groupName: String,
        groupIntroduce: String,
        rules: List<Rule>
    ): Boolean {
        return groupRepository.updateGroupInfo(groupId, groupName, groupIntroduce, rules)
    }
}
