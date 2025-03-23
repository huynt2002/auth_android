package me.auth_android.auth_kit.presentation.views.auth_view.sign_in_view

import androidx.compose.runtime.Immutable
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.presentation.utils.getPasswordValid

@Immutable
internal data class SignInViewState(
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val continueClicked: Boolean = false,
    val password: String = "",
    val isPasswordValid: Boolean = password.getPasswordValid(),
    val popUpState: PopUpState = PopUpState.None,
)

internal enum class SignInViewNavigateEvent {
    ToAppView,
    ToResetPassword,
    ToSignUp,
}
