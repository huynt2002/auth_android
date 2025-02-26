package me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.sign_up_view

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
import me.auth_kit_aos.auth_kit.auth.defines.Authenticating
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpState
import me.auth_kit_aos.auth_kit_ui.presentation.components.SuccessType
import me.auth_kit_aos.auth_kit_ui.utils.asUiText
import me.auth_kit_aos.auth_kit_ui.utils.getEmailValid
import me.auth_kit_aos.auth_kit_ui.utils.getPasswordValid
import me.auth_kit_aos.utils.Failure
import me.auth_kit_aos.utils.Success

@HiltViewModel
class SignUpViewModel
@Inject
constructor(private val accountService: Authenticating, val termAndPolicyLinks: List<String>) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SignUpViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigateEvent = Channel<SignUpViewNavigateEvent>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = email.getEmailValid()) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, isPasswordValid = password.getPasswordValid())
        }
    }

    fun onUserNameChange(userName: String) {
        _uiState.update { it.copy(userName = userName, isUserNameValid = userName.isNotBlank()) }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            val result =
                accountService.signUp(
                    _uiState.value.email,
                    _uiState.value.password,
                    _uiState.value.userName,
                )
            when (result) {
                is Failure ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                                PopUpState.Error(
                                    result.reason.exception.asUiText(),
                                    { _uiState.update { it.copy(popUpState = PopUpState.None) } },
                                )
                        )
                    }

                is Success ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                                PopUpState.Success(
                                    SuccessType.SignUp,
                                    {
                                        viewModelScope.launch {
                                            _uiState.update {
                                                it.copy(popUpState = PopUpState.Loading)
                                            }
                                            delay(1000)
                                            _navigateEvent.send(SignUpViewNavigateEvent.ToSignIn)
                                        }
                                    },
                                )
                        )
                    }
            }
        }
    }
}
