package me.auth_kit_aos.auth_kit_ui.presentation.views.on_app_auth_view.account_view

import androidx.compose.runtime.Immutable
import me.auth_kit_aos.auth_kit_ui.model.AuthUserUI
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpState

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
