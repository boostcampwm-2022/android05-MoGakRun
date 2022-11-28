package com.whyranoid.presentation.community.group.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.toRule
import com.whyranoid.domain.usecase.UpdateGroupInfoUseCase
import com.whyranoid.presentation.model.GroupInfoUiModel
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
class EditGroupViewModel @Inject constructor(
    private val updateGroupInfoUseCase: UpdateGroupInfoUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val initGroupInfo = stateHandle.get<GroupInfoUiModel>("groupInfo")!!

    val groupName = MutableStateFlow<String>(initGroupInfo.name)
    val groupIntroduce = MutableStateFlow<String>(initGroupInfo.introduce)
    val rules = MutableStateFlow<List<String>>(initGroupInfo.rules.map { it.toString() })

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(groupIntroduce) { name, introduce ->
            name.trim().isNotEmpty() && introduce.trim().isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    // TODO : 중복확인 로직
    fun onDuplicateCheckButtonClicked() {
    }

    fun onAddRuleButtonClicked() {
        emitEvent(Event.AddRuleButtonClick)
    }

    fun emitEvent(event: Event) {
        when (event) {
            is Event.AddRuleButtonClick,
            Event.WarningButtonClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(event)
                }
            }
            // TODO : 성공 여부에 따른 분기처리
            is Event.EditGroupButtonClick -> {
                viewModelScope.launch {
                    val isSuccess = updateGroupInfoUseCase(
                        groupId = initGroupInfo.groupId,
                        groupName = groupName.value,
                        groupIntroduce = groupIntroduce.value,
                        rules = rules.value.map {
                            it.toRule()
                        }
                    )
                    if (isSuccess) {
                        _eventFlow.emit(event)
                    } else {
                        _eventFlow.emit(Event.EditGroupButtonClick(false))
                    }
                }
            }
        }
    }
}
