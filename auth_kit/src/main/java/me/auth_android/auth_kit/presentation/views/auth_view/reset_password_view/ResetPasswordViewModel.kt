package me.auth_android.auth_kit.presentation.views.auth_view.reset_password_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.auth_android.auth_kit.data.auth.defines.Authenticating
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.components.SuccessType
import me.auth_android.auth_kit.presentation.utils.asUiText
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.utils.Failure
import me.auth_android.auth_kit.utils.Success

@HiltViewModel
internal class ResetPasswordViewModel @Inject constructor(private val accountService: Authenticating) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigateEvent = Channel<ResetPasswordViewNavigateEvent>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = email.getEmailValid()) }
    }

    fun onResetClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            when (val authResult = accountService.resetPassword(email = _uiState.value.email)) {
                is Failure ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                            PopUpState.Error(
                                authResult.reason.exception.asUiText(),
                                { _uiState.update { it.copy(popUpState = PopUpState.None) } },
                            )
                        )
                    }

                is Success ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                            PopUpState.Success(
                                SuccessType.ResetPassword,
                                { _uiState.update { it.copy(popUpState = PopUpState.None) } },
                            )
                        )
                    }
            }
        }
    }
}
