package me.auth_android.auth_kit.presentation.views.auth_view.sign_in_view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
import me.auth_android.auth_kit.utils.Success
import me.auth_android.auth_kit.utils.Failure


@HiltViewModel
class SignInViewModel @Inject constructor(private val accountService: Authenticating) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SignInViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigateEvent = Channel<SignInViewNavigateEvent>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun init() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            accountService.currentUser.let { user ->
                if (user != null) {
                    delay(1000)
                    _navigateEvent.send(SignInViewNavigateEvent.ToAppView)
                }
            }
            _uiState.update { it.copy(popUpState = PopUpState.None) }
        }
    }

    fun onResetPasswordClick() {
        viewModelScope.launch { _navigateEvent.send(SignInViewNavigateEvent.ToResetPassword) }
    }

    fun onSignUpClick() {
        viewModelScope.launch { _navigateEvent.send(SignInViewNavigateEvent.ToSignUp) }
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

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }

            val authResult =
                accountService.signIn(
                    BasicSignInProvider.EmailAndPassword(
                        _uiState.value.email,
                        _uiState.value.password,
                    )
                )
            when (authResult) {
                is Success -> {
                    resetState()
                    _navigateEvent.send(SignInViewNavigateEvent.ToAppView)
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

    fun onSignInAsGuest() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            when (val authResult = accountService.signIn(BasicSignInProvider.Anonymous)) {
                is Success -> {
                    resetState()
                    _navigateEvent.send(SignInViewNavigateEvent.ToAppView)
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

    fun onGoogleSignIn(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            when (val authResult = accountService.signIn(OAuthSignInProvider.Google, context)) {
                is Success -> {
                    resetState()
                    _navigateEvent.send(SignInViewNavigateEvent.ToAppView)
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
        _uiState.update { SignInViewState() }
    }
}
