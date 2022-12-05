package com.whyranoid.presentation.community.group.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.FinishNotification
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.StartNotification
import com.whyranoid.domain.usecase.CreateRecruitPostUseCase
import com.whyranoid.domain.usecase.ExitGroupUseCase
import com.whyranoid.domain.usecase.GetGroupInfoUseCase
import com.whyranoid.domain.usecase.GetGroupNotificationsUseCase
import com.whyranoid.domain.usecase.GetUidUseCase
import com.whyranoid.presentation.model.GroupInfoUiModel
import com.whyranoid.presentation.model.toGroupInfoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    getGroupInfoUseCase: GetGroupInfoUseCase,
    getGroupNotificationsUseCase: GetGroupNotificationsUseCase,
    private val createRecruitPostUseCase: CreateRecruitPostUseCase,
    private val exitGroupUseCase: ExitGroupUseCase,
    val getUidUseCase: GetUidUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId = requireNotNull(stateHandle.get<GroupInfoUiModel>("groupInfo")).groupId
    val leaderId =
        requireNotNull(stateHandle.get<GroupInfoUiModel>("groupInfo")?.leader?.uid)

    private var _groupInfo =
        MutableStateFlow(requireNotNull(stateHandle.get<GroupInfoUiModel>("groupInfo")))
    val groupInfo: StateFlow<GroupInfoUiModel>
        get() = _groupInfo.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow: SharedFlow<Event>
        get() = _eventFlow.asSharedFlow()

    private val startNotification = MutableStateFlow<List<GroupNotification>>(emptyList())
    private val finishNotification = MutableStateFlow<List<GroupNotification>>(emptyList())
    val mergedNotifications = MutableStateFlow<List<GroupNotification>>(emptyList())

    init {

        viewModelScope.launch {

            getGroupInfoUseCase(leaderId, groupId).onEach { groupInfo ->
                _groupInfo.value = groupInfo.toGroupInfoUiModel()
            }.launchIn(this)
        }

        getGroupNotificationsUseCase(groupId).onEach { notifications ->

            if (notifications.isNotEmpty() && notifications.first() is StartNotification) {
                startNotification.value = notifications
            } else {
                finishNotification.value = notifications
            }

            mergedNotifications.value =
                (startNotification.value + finishNotification.value)
                    .sortedBy { notification ->
                        when (notification) {
                            is StartNotification -> notification.startedAt
                            is FinishNotification -> notification.runningHistory.startedAt
                        }
                    }
        }.launchIn(viewModelScope)
    }

    fun onRecruitButtonClicked() {
        emitEvent(Event.RecruitButtonClick)
    }

    fun onExitGroupButtonClicked() {
        emitEvent(Event.ExitGroupButtonClick)
    }

    fun onRecruitSnackBarButtonClick() {
        viewModelScope.launch {
            val isCreateRecruitPostSuccess = createRecruitPostUseCase(groupId)
            if (isCreateRecruitPostSuccess) {
                emitEvent(Event.RecruitSnackBarButtonClick())
            } else {
                emitEvent(Event.RecruitSnackBarButtonClick(false))
            }
        }
    }

    fun onExitGroupSnackBarButtonClick() {
        viewModelScope.launch {
            val isExitGroupSuccess = exitGroupUseCase(groupId)
            if (isExitGroupSuccess) {
                emitEvent(Event.ExitGroupSnackBarButtonClick())
            } else {
                emitEvent(Event.ExitGroupSnackBarButtonClick(false))
            }
        }
    }

    private fun emitEvent(event: Event) {
        when (event) {
            Event.RecruitButtonClick,
            Event.ExitGroupButtonClick,
            is Event.RecruitSnackBarButtonClick,
            is Event.ExitGroupSnackBarButtonClick
            -> {
                viewModelScope.launch {
                    _eventFlow.emit(event)
                }
            }
        }
    }
}
