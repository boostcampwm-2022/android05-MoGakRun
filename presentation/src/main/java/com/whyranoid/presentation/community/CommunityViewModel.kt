package com.whyranoid.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.DeletePostUseCase
import com.whyranoid.domain.usecase.GetMyGroupListUseCase
import com.whyranoid.domain.usecase.GetMyPagingPostsUseCase
import com.whyranoid.domain.usecase.GetPagingPostsUseCase
import com.whyranoid.domain.usecase.JoinGroupUseCase
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
class CommunityViewModel @Inject constructor(
    getMyGroupListUseCase: GetMyGroupListUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    getPagingPostsUseCase: GetPagingPostsUseCase,
    val getMyPagingPostsUseCase: GetMyPagingPostsUseCase
) : ViewModel() {

    private val _myGroupList = MutableStateFlow<List<GroupInfoUiModel>>(emptyList())
    val myGroupList: StateFlow<List<GroupInfoUiModel>>
        get() = _myGroupList.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow: SharedFlow<Event>
        get() = _eventFlow.asSharedFlow()

    val pagingPost = getPagingPostsUseCase()

    fun onGroupItemClicked(groupInfo: GroupInfoUiModel) {
        emitEvent(Event.GroupItemClick(groupInfo))
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.GroupItemClick -> {
                    _eventFlow.emit(event)
                }
                is Event.JoinGroup -> {
                    if (event.isSuccess) {
                        _eventFlow.emit(event)
                    } else {
                        _eventFlow.emit(event.copy(isSuccess = false))
                    }
                }
                is Event.DeletePost -> {
                    if (event.isSuccess) {
                        _eventFlow.emit(event)
                    } else {
                        _eventFlow.emit(event.copy(isSuccess = false))
                    }
                }
            }
        }
    }

    fun onGroupJoinButtonClicked(groupId: String) {
        viewModelScope.launch {
            emitEvent(Event.JoinGroup(joinGroupUseCase(groupId)))
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            emitEvent(Event.DeletePost(deletePostUseCase(postId)))
        }
    }

    init {
        viewModelScope.launch {
            getMyGroupListUseCase(this).onEach { groupInfoList ->
                _myGroupList.value = groupInfoList.map { groupInfo ->
                    groupInfo.toGroupInfoUiModel()
                }
            }.launchIn(this)
        }
    }
}
