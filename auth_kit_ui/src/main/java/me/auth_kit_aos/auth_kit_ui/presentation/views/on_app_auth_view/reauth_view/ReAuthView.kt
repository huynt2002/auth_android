package me.auth_kit_aos.auth_kit_ui.presentation.views.on_app_auth_view.reauth_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.auth_kit_aos.auth_kit_ui.R
import me.auth_kit_aos.auth_kit_ui.presentation.components.EmailInputField
import me.auth_kit_aos.auth_kit_ui.presentation.components.GoogleSignInButton
import me.auth_kit_aos.auth_kit_ui.presentation.components.PasswordTextField
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpView

@Composable
fun ReAuthView(
    onBack: () -> Unit,
    toResetPassword: (String) -> Unit,
    viewModel: ReAuthViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isFocus by remember { mutableStateOf(false) }

    LaunchedEffect(context, viewModel) {
        viewModel.navigateEvent.collect {
            when (it) {
                ReAuthViewNavigateEvent.NavigateBack -> onBack()
                ReAuthViewNavigateEvent.ToResetPasswordView -> toResetPassword(uiState.email)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = viewModel::onCloseViewClick) {
                Icon(
                    painter = painterResource(R.drawable.close_icon),
                    "",
                    modifier = Modifier.padding(10.dp),
                )
            }
        }
        Spacer(Modifier.size(4.dp))
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        ) {
            Text(
                stringResource(R.string.reauth_label),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(48.dp))

            AnimatedVisibility(visible = !isFocus) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    GoogleSignInButton({ viewModel.onGoogleReAuth(context) })
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        stringResource(R.string.or_text),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            EmailInputField(
                email = uiState.email,
                onValueChange = viewModel::onEmailChange,
                keyboardActions =
                    KeyboardActions({
                        if (uiState.continueClicked) {
                            focusManager.moveFocus(FocusDirection.Down)
                        } else {
                            focusManager.clearFocus()
                        }
                    }),
                onFocusChange = { isFocus = it },
            )

            AnimatedVisibility(visible = uiState.continueClicked) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                    PasswordTextField(
                        password = uiState.password,
                        onPasswordChange = viewModel::onPasswordChange,
                        onFocusChange = { isFocus = it },
                        keyboardActions = KeyboardActions({ focusManager.clearFocus() }),
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Button(
                onClick = {
                    if (uiState.continueClicked) {
                        viewModel.onReAuthClick()
                    } else {
                        viewModel.onContinueClick()
                    }
                    focusManager.clearFocus()
                },
                shape = MaterialTheme.shapes.small,
                enabled =
                    if (uiState.continueClicked) {
                        uiState.isEmailValid && uiState.isPasswordValid
                    } else {
                        uiState.isEmailValid
                    },
                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text(
                    if (uiState.continueClicked) {
                        stringResource(R.string.sign_in_button)
                    } else {
                        stringResource(R.string.continue_button)
                    },
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            }

            Spacer(Modifier.size(8.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodyLarge,
                text =
                    AnnotatedString.fromHtml(
                        htmlString = stringResource(R.string.forget_password),
                        linkStyles =
                            TextLinkStyles(
                                style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                            ),
                        linkInteractionListener = { viewModel.onResetPasswordClick() },
                    ),
                textAlign = TextAlign.Center,
            )
        }
    }
    PopUpView(uiState.popUpState)
}
