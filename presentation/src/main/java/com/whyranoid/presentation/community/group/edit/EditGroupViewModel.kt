package com.whyranoid.presentation.community.group.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.toRule
import com.whyranoid.domain.usecase.CheckIsDuplicatedGroupNameUseCase
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
    private val checkIsDuplicatedGroupNameUseCase: CheckIsDuplicatedGroupNameUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val initGroupInfo = requireNotNull(stateHandle.get<GroupInfoUiModel>("groupInfo"))

    val groupName = MutableStateFlow<String>(initGroupInfo.name)
    val groupIntroduce = MutableStateFlow<String>(initGroupInfo.introduce)
    val rules = MutableStateFlow<List<String>>(initGroupInfo.rules.map { it.toString() })

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val isNotDuplicate = MutableStateFlow(false)

    val isDoubleCheckButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(groupIntroduce) { name, introduce ->
            name.trim().isNotEmpty() && introduce.trim().isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val isGroupCreateButtonEnable: StateFlow<Boolean>
        get() = combine(
            isDoubleCheckButtonEnable,
            isNotDuplicate
        ) { isDoubleCheckButtonEnable, isNotDuplicate ->
            isDoubleCheckButtonEnable && isNotDuplicate
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun onDuplicateCheckButtonClicked() {
        viewModelScope.launch {
            val isDuplicatedGroupName = checkIsDuplicatedGroupNameUseCase(groupName.value)
            emitEvent(Event.DuplicateCheckButtonClick(isDuplicatedGroupName))
            isNotDuplicate.value = isDuplicatedGroupName.not()
        }
    }

    fun onAddRuleButtonClicked() {
        emitEvent(Event.AddRuleButtonClick)
    }

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.AddRuleButtonClick,
                Event.WarningButtonClick -> {
                    _eventFlow.emit(event)
                }
                is Event.DuplicateCheckButtonClick -> {
                    if (event.isDuplicatedGroupName) {
                        _eventFlow.emit(event.copy(isDuplicatedGroupName = true))
                    } else {
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
}
