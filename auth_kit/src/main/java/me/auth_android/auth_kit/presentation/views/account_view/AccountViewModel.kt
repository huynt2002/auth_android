package me.auth_android.auth_kit.presentation.views.account_view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.auth_android.auth_kit.data.auth.AuthProvider
import me.auth_android.auth_kit.data.auth.LinkMethod
import me.auth_android.auth_kit.data.auth.defines.AuthError
import me.auth_android.auth_kit.data.auth.defines.AuthErrorException
import me.auth_android.auth_kit.data.auth.defines.Authenticating
import me.auth_android.auth_kit.presentation.model.toUserUI
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.components.QuestionType
import me.auth_android.auth_kit.presentation.components.SuccessType
import me.auth_android.auth_kit.presentation.utils.asUiText
import me.auth_android.auth_kit.utils.Failure
import me.auth_android.auth_kit.utils.Success
import me.auth_android.auth_kit.utils.Result

@HiltViewModel
class AccountViewModel @Inject constructor(private val authenticating: Authenticating) :
    ViewModel() {
    private val _uiState =
        MutableStateFlow(AccountViewState(authenticating.currentUser?.toUserUI()))
    val uiState = _uiState.asStateFlow()

    private val _navigateEvent = Channel<AccountViewNavigateEvent>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onCloseViewClick() {
        viewModelScope.launch { _navigateEvent.send(AccountViewNavigateEvent.BackToAppView) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLinkEmailClick() {
        _uiState.update { it.copy(isLinkEmail = true) }
    }

    fun onDismissEmailLink() {
        _uiState.update { it.copy(isLinkEmail = false, password = "", email = "") }
    }

    fun onSignOutClick(context: Context) {
        _uiState.update {
            it.copy(
                popUpState =
                PopUpState.Question(
                    QuestionType.SignOut,
                    onConfirm = {
                        viewModelScope.launch {
                            when (val rs = authenticating.signOut(context)) {
                                is Success -> {
                                    _uiState.update { it.copy(popUpState = PopUpState.Loading) }
                                    delay(1000)
                                    _navigateEvent.send(AccountViewNavigateEvent.ToSignInView)
                                }

                                is Failure ->
                                    _uiState.update {
                                        it.copy(
                                            popUpState =
                                            PopUpState.Error(
                                                rs.reason.exception.asUiText(),
                                                { onCancelPopUp() },
                                            )
                                        )
                                    }
                            }
                        }
                    },
                    onCancel = { onCancelPopUp() },
                )
            )
        }
    }

    fun onUnLinkEmail() {
        _uiState.update {
            it.copy(
                popUpState =
                PopUpState.Question(
                    QuestionType.UnLink,
                    onConfirm = {
                        unLink(
                            AuthProvider.Email.identifier,
                            {
                                navigate {
                                    _navigateEvent.send(AccountViewNavigateEvent.ToReAuthView)
                                }
                            },
                        )
                    },
                    onCancel = { onCancelPopUp() },
                )
            )
        }
    }

    fun onUnLinkGoogle() {
        _uiState.update {
            it.copy(
                popUpState =
                PopUpState.Question(
                    QuestionType.UnLink,
                    onConfirm = {
                        unLink(
                            AuthProvider.Google.identifier,
                            {
                                navigate {
                                    _navigateEvent.send(AccountViewNavigateEvent.ToReAuthView)
                                }
                            },
                        )
                    },
                    onCancel = { onCancelPopUp() },
                )
            )
        }
    }

    private fun unLink(providerId: String, navigateEvent: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            when (val rs = authenticating.unlink(providerId)) {
                is Failure ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                            if (rs.reason.exception is AuthErrorException.NeedReauth)
                                PopUpState.Question(
                                    QuestionType.ReAuth,
                                    onConfirm = {
                                        onCancelPopUp()
                                        navigateEvent()
                                    },
                                    onCancel = { onCancelPopUp() },
                                )
                            else
                                PopUpState.Error(
                                    rs.reason.exception.asUiText(),
                                    { onCancelPopUp() },
                                )
                        )
                    }

                is Success ->
                    _uiState.update {
                        it.copy(
                            popUpState = PopUpState.Success(SuccessType.UnLink, { resetState() })
                        )
                    }
            }
        }
    }

    fun onLinkWithGoogleClick(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            when (val rs = authenticating.link(LinkMethod.WithGoogle(context))) {
                is Failure ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                            PopUpState.Error(
                                rs.reason.exception.asUiText(),
                                { onCancelPopUp() },
                            )
                        )
                    }

                is Success ->
                    _uiState.update {
                        it.copy(popUpState = PopUpState.Success(SuccessType.Link, { resetState() }))
                    }
            }
        }
    }

    fun linkWithEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading, isLinkEmail = false) }
            val rs =
                authenticating.link(
                    LinkMethod.WithEmailPassword(
                        email = _uiState.value.email,
                        _uiState.value.password,
                    )
                )
            when (rs) {
                is Failure ->
                    _uiState.update {
                        it.copy(
                            popUpState =
                            PopUpState.Error(
                                rs.reason.exception.asUiText(),
                                {
                                    _uiState.update {
                                        it.copy(
                                            popUpState = PopUpState.None,
                                            isLinkEmail = true,
                                        )
                                    }
                                },
                            )
                        )
                    }

                is Success ->
                    _uiState.update {
                        it.copy(popUpState = PopUpState.Success(SuccessType.Link, { resetState() }))
                    }
            }
        }
    }

    fun onDeleteDataClick() {
        _uiState.update {
            it.copy(
                popUpState =
                PopUpState.Question(
                    QuestionType.DeleteData,
                    onConfirm = {
                        viewModelScope.launch {
                            when (val rs = deleteAccountData()) {
                                is Success -> {
                                    _uiState.update {
                                        it.copy(
                                            popUpState =
                                            PopUpState.Success(
                                                SuccessType.DeleteData,
                                                onClick = { resetState() },
                                            )
                                        )
                                    }
                                }

                                is Failure -> {
                                    _uiState.update {
                                        it.copy(
                                            popUpState =
                                            if (
                                                rs.reason.exception
                                                        is AuthErrorException.NeedReauth
                                            )
                                                PopUpState.Question(
                                                    QuestionType.ReAuth,
                                                    onConfirm = {
                                                        navigate {
                                                            _navigateEvent.send(
                                                                AccountViewNavigateEvent
                                                                    .ToReAuthView
                                                            )
                                                        }
                                                    },
                                                    onCancel = { onCancelPopUp() },
                                                )
                                            else
                                                PopUpState.Error(
                                                    rs.reason.exception.asUiText(),
                                                    { onCancelPopUp() },
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onCancel = { onCancelPopUp() },
                )
            )
        }
    }

    fun onDeleteAccountClick() {
        _uiState.update {
            it.copy(
                popUpState =
                PopUpState.Question(
                    QuestionType.DeleteAccount,
                    onConfirm = {
                        viewModelScope.launch {
                            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
                            val rs = authenticating.deleteUser()
                            when (rs) {
                                is Success -> {
                                    _navigateEvent.send(AccountViewNavigateEvent.ToSignInView)
                                }

                                is Failure -> {
                                    _uiState.update {
                                        it.copy(
                                            popUpState =
                                            if (
                                                rs.reason.exception
                                                        is AuthErrorException.NeedReauth
                                            )
                                                PopUpState.Question(
                                                    QuestionType.ReAuth,
                                                    onConfirm = {
                                                        navigate {
                                                            _navigateEvent.send(
                                                                AccountViewNavigateEvent
                                                                    .ToReAuthView
                                                            )
                                                        }
                                                    },
                                                    onCancel = { onCancelPopUp() },
                                                )
                                            else
                                                PopUpState.Error(
                                                    rs.reason.exception.asUiText(),
                                                    { onCancelPopUp() },
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onCancel = { onCancelPopUp() },
                )
            )
        }
    }

    private fun onCancelPopUp() {
        _uiState.update { it.copy(popUpState = PopUpState.None) }
    }

    private fun resetState() {
        _uiState.update { AccountViewState(authenticating.currentUser?.toUserUI()) }
    }

    private fun navigate(navigateEvent: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(popUpState = PopUpState.Loading) }
            delay(1000)
            onCancelPopUp()
            delay(500)
            navigateEvent()
        }
    }

    // temp
    private fun deleteAccountData(): Result<Unit, AuthError> {
        val rand = Random.nextInt(1, 10)
        return try {
            if (rand % 2 == 0) {
                throw AuthErrorException.NeedReauth
            }
            Success(Unit)
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }
}
