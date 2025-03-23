package me.auth_android.auth_kit.presentation.views.auth_view.reset_password_view

import androidx.compose.runtime.Immutable
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.utils.getEmailValid

@Immutable
internal data class ResetPasswordViewState(
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val popUpState: PopUpState = PopUpState.None,
)

internal enum class ResetPasswordViewNavigateEvent {
    NavigateBack
}
