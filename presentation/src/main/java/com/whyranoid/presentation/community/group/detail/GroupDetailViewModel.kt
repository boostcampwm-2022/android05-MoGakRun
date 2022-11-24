package com.whyranoid.presentation.community.group.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.FinishNotification
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.StartNotification
import com.whyranoid.domain.usecase.GetGroupNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    getGroupNotificationsUseCase: GetGroupNotificationsUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow: SharedFlow<Event>
        get() = _eventFlow.asSharedFlow()

    private val startNotification = MutableStateFlow<List<GroupNotification>>(emptyList())
    private val finishNotification = MutableStateFlow<List<GroupNotification>>(emptyList())
    val mergedNotifications = MutableStateFlow<List<GroupNotification>>(emptyList())

    init {
        // TODO : 그룹 아이디를 프레그먼트에서 받아오도록 변경
        getGroupNotificationsUseCase("수피치 그룹1").onEach { notifications ->

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

    private fun emitEvent(event: Event) {
        when (event) {
            Event.RecruitButtonClick,
            Event.ExitGroupButtonClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(event)
                }
            }
        }
    }
}
