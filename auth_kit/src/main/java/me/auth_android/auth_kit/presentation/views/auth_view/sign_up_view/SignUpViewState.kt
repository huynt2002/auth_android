package me.auth_android.auth_kit.presentation.views.auth_view.sign_up_view

import androidx.compose.runtime.Immutable
import me.auth_android.auth_kit.presentation.components.PopUpState
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.presentation.utils.getPasswordValid

@Immutable
data class SignUpViewState(
    val userName: String = "",
    val isUserNameValid: Boolean = userName.isNotBlank(),
    val email: String = "",
    val isEmailValid: Boolean = email.getEmailValid(),
    val password: String = "",
    val isPasswordValid: Boolean = password.getPasswordValid(),
    val popUpState: PopUpState = PopUpState.None,
)

enum class SignUpViewNavigateEvent {
    ToSignIn
}
