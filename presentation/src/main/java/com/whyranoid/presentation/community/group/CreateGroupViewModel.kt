package com.whyranoid.presentation.community.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CreateGroupViewModel : ViewModel() {

    val groupName = MutableStateFlow<String?>(null)
    val groupIntroduce = MutableStateFlow<String?>(null)

    val isButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(groupIntroduce) { name, introduce ->
            name?.isNotEmpty() ?: false && introduce?.isNotEmpty() ?: false
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )
}
