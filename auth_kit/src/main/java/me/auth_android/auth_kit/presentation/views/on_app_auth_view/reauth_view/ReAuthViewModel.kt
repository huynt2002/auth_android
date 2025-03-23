package me.auth_android.auth_kit.presentation.views.on_app_auth_view.reauth_view

import android.content.Context
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
import me.auth_android.auth_kit.data.auth.BasicSignInProvider
import me.auth_android.auth_kit.data.auth.OAuthSignInProvider
import me.auth_android.auth_kit.data.auth.defines.Authenticating
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.utils.asUiText
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.presentation.utils.getPasswordValid
import me.auth_android.auth_kit.utils.Failure
import me.auth_android.auth_kit.utils.Success

@HiltViewModel
internal class ReAuthViewModel @Inject constructor(private val accountService: Authenticating) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ReAuthViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigateEvent = Channel<ReAuthViewNavigateEvent>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onResetPasswordClick() {
        viewModelScope.launch { _navigateEvent.send(ReAuthViewNavigateEvent.ToResetPasswordView) }
    }

    fun onCloseViewClick() {
        viewModelScope.launch { _navigateEvent.send(ReAuthViewNavigateEvent.NavigateBack) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = email.getEmailValid()) }
    }

    fun onContinueClick() {
        _uiState.update { it.copy(continueClicked = true) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, isPasswordValid = password.getPasswordValid())
        }
    }

    fun onReAuthClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }

            val authResult =
                accountService.reAuthenticate(
                    BasicSignInProvider.EmailAndPassword(
                        _uiState.value.email,
                        _uiState.value.password,
                    )
                )

            when (authResult) {
                is Success -> {
                    resetState()
                    _navigateEvent.send(ReAuthViewNavigateEvent.NavigateBack)
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            popUpState =
                                PopUpState.Error(
                                    authResult.reason.exception.asUiText(),
                                    { _uiState.update { it.copy(popUpState = PopUpState.None) } },
                                )
                        )
                    }
                }
            }
        }
    }

    fun onGoogleReAuth(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            val authResult = accountService.reAuthenticate(OAuthSignInProvider.Google, context)
            when (authResult) {
                is Success -> {
                    resetState()
                    _navigateEvent.send(ReAuthViewNavigateEvent.NavigateBack)
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            popUpState =
                                PopUpState.Error(
                                    authResult.reason.exception.asUiText(),
                                    { _uiState.update { it.copy(popUpState = PopUpState.None) } },
                                )
                        )
                    }
                }
            }
        }
    }

    private fun resetState() {
        _uiState.update { ReAuthViewState() }
    }
}
