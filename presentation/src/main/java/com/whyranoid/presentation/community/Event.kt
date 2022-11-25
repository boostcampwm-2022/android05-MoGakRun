package com.whyranoid.presentation.community

import com.whyranoid.presentation.model.GroupInfoUiModel

sealed class Event {
    data class CategoryItemClick(val groupInfo: GroupInfoUiModel) : Event()
}
