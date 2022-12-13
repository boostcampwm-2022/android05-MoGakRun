package com.whyranoid.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.DeletePostUseCase
import com.whyranoid.domain.usecase.GetMyGroupListUseCase
import com.whyranoid.domain.usecase.GetMyPagingPostsUseCase
import com.whyranoid.domain.usecase.GetPagingPostsUseCase
import com.whyranoid.domain.usecase.JoinGroupUseCase
import com.whyranoid.presentation.model.GroupInfoUiModel
import com.whyranoid.presentation.model.UiState
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

    private val _myGroupListState =
        MutableStateFlow<UiState<List<GroupInfoUiModel>>>(UiState.UnInitialized)
    val myGroupListState: StateFlow<UiState<List<GroupInfoUiModel>>>
        get() = _myGroupListState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow: SharedFlow<Event>
        get() = _eventFlow.asSharedFlow()

    val pagingPost = getPagingPostsUseCase(viewModelScope)

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

    fun deletePost(postId: String, block: () -> Unit) {
        viewModelScope.launch {
            emitEvent(Event.DeletePost(deletePostUseCase(postId), block))
        }
    }

    init {
        viewModelScope.launch {
            getMyGroupListUseCase().onEach { groupInfoListResult ->

                _myGroupListState.value = UiState.Loading
                groupInfoListResult.onSuccess { groupInfoList ->
                    _myGroupListState.value = UiState.Success(
                        groupInfoList.map { groupInfo ->
                            groupInfo.toGroupInfoUiModel()
                        }
                    )
                }.onFailure { throwable ->
                    _myGroupListState.value = UiState.Failure(throwable)
                }
            }.launchIn(this)
        }
    }
}
