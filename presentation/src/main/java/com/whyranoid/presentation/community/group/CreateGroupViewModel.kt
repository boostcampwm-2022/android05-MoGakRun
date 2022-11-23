package com.whyranoid.presentation.community.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.CreateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    val groupName = MutableStateFlow<String?>(null)
    val groupIntroduce = MutableStateFlow<String?>(null)

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(groupIntroduce) { name, introduce ->
            name?.trim()?.isNotEmpty() ?: false && introduce?.trim()?.isNotEmpty() ?: false
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    // TODO 그룹명 중복확인 로직
    fun onButtonClicked() {
    }

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.CreateGroupButtonClick -> {
                    val isCreateGroupSuccess = createGroupUseCase(
                        groupName.value ?: "",
                        groupIntroduce.value ?: ""
                    )
                    if (isCreateGroupSuccess) {
                        _eventFlow.emit(event)
                    } else {
                        _eventFlow.emit(event.copy(isCompleted = false))
                    }
                }
                is Event.WarningButtonClick -> {
                    _eventFlow.emit(event)
                }
            }
        }
    }
}
