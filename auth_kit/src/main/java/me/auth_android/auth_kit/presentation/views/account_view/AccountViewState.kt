package me.auth_android.auth_kit.presentation.views.account_view

import androidx.compose.runtime.Immutable
import me.auth_android.auth_kit.presentation.model.AuthUserUI
import me.auth_android.auth_kit.presentation.components.PopUpState

@Immutable
data class AccountViewState(
    val user: AuthUserUI?,
    val email: String = "",
    val password: String = "",
    val isLinkEmail: Boolean = false,
    val popUpState: PopUpState = PopUpState.None,
)

enum class AccountViewNavigateEvent {
    BackToAppView,
    ToReAuthView,
    ToSignInView,
}
