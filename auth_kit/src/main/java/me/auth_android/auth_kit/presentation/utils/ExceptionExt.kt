package me.auth_android.auth_kit.presentation.utils

import me.auth_android.auth_kit.R
import me.auth_android.auth_kit.data.auth.defines.AuthErrorException

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
