package me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.sign_in_view

import androidx.compose.runtime.Immutable
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpState
import me.auth_kit_aos.auth_kit_ui.utils.getEmailValid
import me.auth_kit_aos.auth_kit_ui.utils.getPasswordValid

@Immutable
data class SignInViewState(
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val continueClicked: Boolean = false,
    val password: String = "",
    val isPasswordValid: Boolean = password.getPasswordValid(),
    val popUpState: PopUpState = PopUpState.None,
)

enum class SignInViewNavigateEvent {
    ToAppView,
    ToResetPassword,
    ToSignUp,
}
