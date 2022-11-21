package com.whyranoid.presentation.community

import com.whyranoid.domain.model.GroupInfo

sealed class Event {
    data class CategoryItemClick(val groupInfo: GroupInfo) : Event()
}
