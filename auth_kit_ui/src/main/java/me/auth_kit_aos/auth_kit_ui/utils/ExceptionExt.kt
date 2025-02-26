package me.auth_kit_aos.auth_kit_ui.utils

import me.auth_kit_aos.auth_kit.auth.defines.AuthErrorException
import me.auth_kit_aos.auth_kit_ui.R

fun Exception.asUiText(): UiText {
    return when (this) {
        AuthErrorException.UserNotFound -> UiText.StringResource(R.string.user_not_found_error)

        AuthErrorException.NeedReauth -> UiText.StringResource(R.string.require_reauth_error)

        AuthErrorException.UnSupportedCredential ->
            UiText.StringResource(R.string.un_supported_credential_error)

        AuthErrorException.InvalidProvider -> UiText.StringResource(R.string.invalid_provider_error)

        else -> UiText.DynamicString(this.localizedMessage ?: "")
    }
}
