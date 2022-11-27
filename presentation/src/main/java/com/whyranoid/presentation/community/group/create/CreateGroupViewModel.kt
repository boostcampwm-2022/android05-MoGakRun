package com.whyranoid.presentation.community.group.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.CreateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val rules = MutableStateFlow<List<String>>(emptyList())

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    fun onDialogConfirm(date: String, hour: String, minute: String) {
        rules.value = rules.value + listOf("$date-$hour-$minute")
        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    val isButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(groupIntroduce) { name, introduce ->
            name?.trim()?.isNotEmpty() ?: false && introduce?.trim()?.isNotEmpty() ?: false
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    // TODO 그룹명 중복확인 로직
    fun onDuplicateCheckButtonClicked() {
    }

    fun onAddRuleButtonClicked() {
        emitEvent(Event.AddRuleButtonClick)
    }

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.CreateGroupButtonClick -> {
                    val isCreateGroupSuccess = createGroupUseCase(
                        groupName.value ?: "",
                        groupIntroduce.value ?: "",
                        rules.value
                    )
                    if (isCreateGroupSuccess) {
                        _eventFlow.emit(event)
                    } else {
                        _eventFlow.emit(event.copy(isSuccess = false))
                    }
                }
                is Event.WarningButtonClick -> {
                    _eventFlow.emit(event)
                }
                is Event.AddRuleButtonClick -> {
                    _eventFlow.emit(event)
                }
            }
        }
    }
}
