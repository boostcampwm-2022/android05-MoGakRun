package com.whyranoid.presentation.myrun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.usecase.GetEmailUseCase
import com.whyranoid.domain.usecase.SignOutUseCase
import com.whyranoid.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getEmailUseCase: GetEmailUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    init {
        getEmail()
    }

    private val _emailState = MutableStateFlow<UiState<String>>(UiState.UnInitialized)
    val emailState: StateFlow<UiState<String>>
        get() = _emailState.asStateFlow()

    private fun getEmail() {
        viewModelScope.launch {
            _emailState.value = UiState.Loading

            getEmailUseCase().collect { emailResult ->
                emailResult.onSuccess { email ->
                    _emailState.value = UiState.Success(email)
                }.onFailure { throwable ->
                    _emailState.value = UiState.Failure(throwable)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}
