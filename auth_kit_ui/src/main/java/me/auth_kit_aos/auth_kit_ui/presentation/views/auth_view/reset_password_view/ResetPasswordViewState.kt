package me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.reset_password_view

import androidx.compose.runtime.Immutable
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpState
import me.auth_kit_aos.auth_kit_ui.utils.getEmailValid

@Immutable
data class ResetPasswordViewState(
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val popUpState: PopUpState = PopUpState.None,
)

enum class ResetPasswordViewNavigateEvent {
    NavigateBack
}
