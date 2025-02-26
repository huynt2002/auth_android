package me.auth_kit_aos.auth_kit_ui.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import me.auth_kit_aos.auth_kit_ui.R
import me.auth_kit_aos.auth_kit_ui.utils.UiText

sealed interface PopUpState {
    object None : PopUpState

    object Loading : PopUpState

    data class Error(val messageUiText: UiText, val onClick: () -> Unit = {}) : PopUpState

    data class Success(val type: SuccessType, val onClick: () -> Unit = {}) : PopUpState

    data class Question(
        val type: QuestionType,
        val onConfirm: () -> Unit = {},
        val onCancel: () -> Unit = {},
    ) : PopUpState
}

enum class SuccessType(@StringRes val message: Int) {
    SignIn(R.string.sign_in_success),
    SignUp(R.string.sign_up_success),
    ResetPassword(R.string.reset_password_success),
    UnLink(R.string.un_link_success),
    Link(R.string.link_success),
    DeleteData(R.string.delete_data_success),
}

enum class QuestionType(@StringRes val message: Int) {
    DeleteData(R.string.delete_data_confirm),
    DeleteAccount(R.string.delete_account_confirm),
    UnLink(R.string.un_link_confirm),
    SignOut(R.string.sign_out_confirm),
    ReAuth(R.string.require_reauth_error),
}

@Composable
fun PopUpView(popUpState: PopUpState) {
    when (popUpState) {
        is PopUpState.None -> {}
        is PopUpState.Loading -> LoadingView()
        is PopUpState.Error -> ErrorView(popUpState)
        is PopUpState.Success -> SuccessView(popUpState)
        is PopUpState.Question -> QuestionView(popUpState)
    }
}

@Composable
private fun QuestionView(popUpState: PopUpState.Question) {
    val dialogProperties =
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    val confirmText =
        when (popUpState.type) {
            QuestionType.UnLink -> stringResource(R.string.un_link_label)
            QuestionType.SignOut -> stringResource(R.string.sign_out_label)
            QuestionType.DeleteData -> stringResource(R.string.delete_label)
            QuestionType.DeleteAccount -> stringResource(R.string.delete_label)
            else -> stringResource(R.string.ok_button)
        }
    AlertDialog(
        onDismissRequest = popUpState.onCancel,
        confirmButton = {
            TextButton(onClick = { popUpState.onConfirm() }) {
                Text(confirmText, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = { popUpState.onCancel() }) {
                Text(
                    stringResource(R.string.cancel_button),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        properties = dialogProperties,
        icon = {
            Icon(
                painterResource(R.drawable.baseline_question_mark),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        title = { Text(stringResource(R.string.confirm_label)) },
        text = { Text(text = stringResource(popUpState.type.message)) },
    )
}

@Composable
private fun SuccessView(popUpState: PopUpState.Success) {
    val dialogProperties =
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    AlertDialog(
        onDismissRequest = popUpState.onClick,
        confirmButton = {
            TextButton(onClick = { popUpState.onClick() }) {
                Text(stringResource(R.string.ok_button))
            }
        },
        dismissButton = {
            TextButton(onClick = { popUpState.onClick() }) {
                Text(stringResource(R.string.dismiss_button))
            }
        },
        properties = dialogProperties,
        icon = {
            Icon(
                painterResource(R.drawable.success_icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        text = { Text(text = stringResource(popUpState.type.message)) },
        title = { Text(text = stringResource(R.string.success_label)) },
    )
}

@Composable
private fun ErrorView(popUpState: PopUpState.Error) {
    val dialogProperties =
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    AlertDialog(
        onDismissRequest = popUpState.onClick,
        confirmButton = {
            TextButton(onClick = { popUpState.onClick() }) {
                Text(stringResource(R.string.ok_button))
            }
        },
        dismissButton = {
            //            TextButton(onClick = { popUpState.onClick() }) {
            //                Text(stringResource(R.string.cancel_button))
            //            }
        },
        properties = dialogProperties,
        icon = {
            Icon(
                painterResource(R.drawable.error_icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(text = stringResource(R.string.error_label)) },
        text = { Text(text = popUpState.messageUiText.asString()) },
    )
}

@Composable
private fun LoadingView() {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun Error_Preview() {
    ErrorView(PopUpState.Error(UiText.DynamicString("This is a error")))
}

@Preview
@Composable
private fun Question_Preview() {
    QuestionView(PopUpState.Question(QuestionType.UnLink))
}

@Preview
@Composable
private fun Success_Preview() {
    SuccessView(PopUpState.Success(SuccessType.ResetPassword))
}
