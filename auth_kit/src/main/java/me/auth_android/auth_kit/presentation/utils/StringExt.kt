package me.auth_android.auth_kit.presentation.utils

import android.util.Patterns
import androidx.annotation.StringRes
import me.auth_android.auth_kit.R

internal fun String.getEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.isNotBlank()
}

private const val MIN_PASS_LENGTH = 6

internal fun String.getPasswordValid(): Boolean {
    return this.getPasswordError() == null
}

internal fun String.getPasswordError(): PasswordError? {
    if (this.length < MIN_PASS_LENGTH) {
        return PasswordError.SHORT
    }
    return null
}

internal enum class PasswordError(@StringRes val message: Int) {
    SHORT(R.string.password_short_error)
}

internal fun PasswordError.getMessage(): UiText {
    return when (this) {
        PasswordError.SHORT -> UiText.StringResource(this.message)
    }
}
