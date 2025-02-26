package me.auth_kit_aos.auth_kit_ui.presentation.views.on_app_auth_view.reauth_view

import androidx.compose.runtime.Immutable
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpState
import me.auth_kit_aos.auth_kit_ui.utils.getEmailValid
import me.auth_kit_aos.auth_kit_ui.utils.getPasswordValid

@Immutable
data class ReAuthViewState(
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val continueClicked: Boolean = false,
    val password: String = "",
    val isPasswordValid: Boolean = password.getPasswordValid(),
    val popUpState: PopUpState = PopUpState.None,
)

enum class ReAuthViewNavigateEvent {
    NavigateBack,
    ToResetPasswordView,
}
