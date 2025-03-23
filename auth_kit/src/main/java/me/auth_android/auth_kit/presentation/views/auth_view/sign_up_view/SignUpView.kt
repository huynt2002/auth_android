package me.auth_android.auth_kit.presentation.views.auth_view.sign_up_view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.auth_android.auth_kit.R
import me.auth_android.auth_kit.presentation.components.EmailInputField
import me.auth_android.auth_kit.presentation.components.HyperlinkText
import me.auth_android.auth_kit.presentation.components.NameInputField
import me.auth_android.auth_kit.presentation.components.PasswordTextField
import me.auth_android.auth_kit.presentation.components.PopUpView

@Composable
internal fun SignUpView(
    navigateBack: () -> Unit,
    email: String = "",
    viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>(),
) {
    LaunchedEffect(true) { viewModel.onEmailChange(email) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.navigateEvent.collect {
            when (it) {
                SignUpViewNavigateEvent.ToSignIn -> navigateBack()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    "",
                    modifier = Modifier.padding(10.dp).size(24.dp),
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.size(4.dp))
            Text(
                stringResource(R.string.sign_up_text),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.size(32.dp))
            NameInputField(
                name = uiState.userName,
                onValueChange = viewModel::onUserNameChange,
                keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            )
            Spacer(Modifier.size(8.dp))
            EmailInputField(
                email = uiState.email,
                onValueChange = viewModel::onEmailChange,
                keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            )
            Spacer(Modifier.size(8.dp))
            PasswordTextField(
                password = uiState.password,
                onPasswordChange = viewModel::onPasswordChange,
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
            )
            Spacer(Modifier.size(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                //            val termAndPolicyRes = viewModel.termAndPolicy
                //            Text(
                //                style = MaterialTheme.typography.bodyLarge,
                //                text =
                //                    AnnotatedString.fromHtml(
                //                        htmlString = stringResource(termAndPolicyRes),
                //                        linkStyles =
                //                            TextLinkStyles(
                //                                style = SpanStyle(color =
                // MaterialTheme.colorScheme.primary)
                //                            ),
                //                    ),
                //                textAlign = TextAlign.Center,
                //            )
                HyperlinkText(
                    text = stringResource(R.string.agreement_text),
                    linkText =
                        listOf(stringResource(R.string.term), stringResource(R.string.policy)),
                    hyperlinks = viewModel.termAndPolicyLinks,
                    linkStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.size(12.dp))
            Button(
                onClick = { viewModel.onSignUpClick() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uiState.isEmailValid && uiState.isPasswordValid && uiState.isUserNameValid,
            ) {
                Text(
                    stringResource(R.string.sign_up_text),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            }
        }
    }
    PopUpView(uiState.popUpState)
}

@Preview
@Composable
private fun Preview() {
    SignUpView({})
}
