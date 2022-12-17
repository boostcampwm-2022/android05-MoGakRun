package com.whyranoid.presentation.community.group.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.CheckIsDuplicatedGroupNameUseCase
import com.whyranoid.domain.usecase.CreateGroupUseCase
import com.whyranoid.presentation.util.MutableEventFlow
import com.whyranoid.presentation.util.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase,
    private val checkIsDuplicatedGroupNameUseCase: CheckIsDuplicatedGroupNameUseCase
) : ViewModel() {

    val groupName = MutableStateFlow<String?>(null)
    val groupIntroduce = MutableStateFlow<String?>(null)
    val rules = MutableStateFlow<List<String>>(emptyList())

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    private val isNotDuplicate = MutableStateFlow(false)

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    fun onDialogConfirm(date: String, hour: String, minute: String) {
        if (date.isNotEmpty() && hour.isNotEmpty() && minute.isNotEmpty()) {
            rules.value = rules.value + listOf("$date-$hour-$minute")
        }

        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun removeRule(rule: String) {
        rules.value = rules.value.filter { it != rule }
    }

    val isDoubleCheckButtonEnable: StateFlow<Boolean>
        get() = groupName.combine(isNotDuplicate) { name, isNotDuplicated ->
            name?.trim()?.isNotEmpty() ?: false && isNotDuplicated.not()
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val isGroupCreateButtonEnable: StateFlow<Boolean>
        get() = combine(
            groupIntroduce,
            isNotDuplicate
        ) { groupIntroduce, isNotDuplicate ->
            groupIntroduce?.isNotEmpty() ?: false && isNotDuplicate
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun onDuplicateCheckButtonClicked() {
        viewModelScope.launch {
            val isDuplicatedGroupName =
                checkIsDuplicatedGroupNameUseCase(groupName.value ?: "")
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
                is Event.DuplicateCheckButtonClick -> {
                    if (event.isDuplicatedGroupName) {
                        _eventFlow.emit(event.copy(isDuplicatedGroupName = true))
                    } else {
                        _eventFlow.emit(event)
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
