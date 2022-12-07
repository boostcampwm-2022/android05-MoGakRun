package com.whyranoid.presentation.community.runningpost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.CreateRunningPostUseCase
import com.whyranoid.presentation.model.RunningHistoryUiModel
import com.whyranoid.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRunningPostViewModel @Inject constructor(
    private val createRunningPostUseCase: CreateRunningPostUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    val selectedRunningHistory =
        savedState.get<RunningHistoryUiModel>(RUNNING_HISTORY_SAFE_ARGS_KEY)
    val runningPostContent = MutableStateFlow<String?>(null)

    val createPostButtonEnableState: StateFlow<Boolean>
        get() = runningPostContent.map { content ->
            content.isNullOrBlank().not()
        }.stateIn(
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5000),
            scope = viewModelScope
        )

    private val _createPostState = MutableStateFlow<UiState<Boolean>>(UiState.UnInitialized)
    val createPostState: StateFlow<UiState<Boolean>>
        get() = _createPostState.asStateFlow()

    fun createRunningPost() {
        viewModelScope.launch {
            _createPostState.value = UiState.Loading

            selectedRunningHistory?.let { runningHistory ->
                createRunningPostUseCase(
                    runningPostContent.value.toString(),
                    runningHistory.historyId
                ).onSuccess { createPostResult ->
                    _createPostState.value = UiState.Success(createPostResult)
                }.onFailure { throwable ->
                    _createPostState.value = UiState.Failure(throwable)
                }
            }
        }
    }

    companion object {
        private const val RUNNING_HISTORY_SAFE_ARGS_KEY = "selectedRunningHistory"
    }
}
