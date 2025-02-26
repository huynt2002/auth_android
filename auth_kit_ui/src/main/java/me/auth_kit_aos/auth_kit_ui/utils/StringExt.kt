package me.auth_kit_aos.auth_kit_ui.utils

import android.util.Patterns
import androidx.annotation.StringRes
import me.auth_kit_aos.auth_kit_ui.R

fun String.getEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.isNotBlank()
}

private const val MIN_PASS_LENGTH = 6

fun String.getPasswordValid(): Boolean {
    return this.getPasswordError() == null
}

fun String.getPasswordError(): PasswordError? {
    if (this.length < MIN_PASS_LENGTH) {
        return PasswordError.SHORT
    }
    return null
}

enum class PasswordError(@StringRes val message: Int) {
    SHORT(R.string.password_short_error)
}

fun PasswordError.getMessage(): UiText {
    return when (this) {
        PasswordError.SHORT -> UiText.StringResource(this.message)
    }
}
