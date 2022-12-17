package com.whyranoid.presentation.community.group.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.toRule
import com.whyranoid.domain.usecase.UpdateGroupInfoUseCase
import com.whyranoid.presentation.model.GroupInfoUiModel
import com.whyranoid.presentation.util.MutableEventFlow
import com.whyranoid.presentation.util.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(
    private val updateGroupInfoUseCase: UpdateGroupInfoUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val initGroupInfo = requireNotNull(stateHandle.get<GroupInfoUiModel>("groupInfo"))

    val groupName = MutableStateFlow<String>(initGroupInfo.name)
    val groupIntroduce = MutableStateFlow<String>(initGroupInfo.introduce)
    val rules = MutableStateFlow<List<String>>(initGroupInfo.rules.map { it.toString() })

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    fun onAddRuleButtonClicked() {
        emitEvent(Event.AddRuleButtonClick)
    }

    fun removeRule(rule: String) {
        rules.value = rules.value.filter { it != rule }
    }

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.AddRuleButtonClick -> {
                    _eventFlow.emit(event)
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
