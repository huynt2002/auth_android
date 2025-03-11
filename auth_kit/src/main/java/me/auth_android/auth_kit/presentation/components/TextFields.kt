package me.auth_android.auth_kit.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import me.auth_android.auth_kit.R
import me.auth_android.auth_kit.presentation.utils.PasswordError
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.presentation.utils.getMessage
import me.auth_android.auth_kit.presentation.utils.getPasswordError

@Composable
fun NameInputField(
    name: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusChange: (Boolean) -> Unit = {},
) {
    var isError by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = name,
        onValueChange = {
            val userName = it.trim()
            onValueChange(userName)
            isError = userName.isEmpty()
        },
        placeholder = { Text(stringResource(R.string.name_input_label)) },
        singleLine = true,
        trailingIcon = {
            if (isError) {
                Icon(
                    painterResource(R.drawable.error_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardActions = keyboardActions,
        isError = isError,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().onFocusChanged { onFocusChange(it.isFocused) },
    )
}

@Composable
fun EmailInputField(
    email: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions? = null,
    onFocusChange: (Boolean) -> Unit = {},
) {
    var isError by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = email,
        onValueChange = {
            onValueChange(it)
            isError = !it.getEmailValid()
        },
        placeholder = { Text(stringResource(R.string.email_label)) },
        singleLine = true,
        trailingIcon = {
            if (isError) {
                Icon(
                    painterResource(R.drawable.error_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        keyboardActions = keyboardActions ?: KeyboardActions { focusManager.clearFocus() },
        isError = isError,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().onFocusChanged { onFocusChange(it.isFocused) },
    )
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions? = null,
    onFocusChange: (Boolean) -> Unit = {},
) {
    var showPassword by remember { mutableStateOf(false) }
    var passwordError: PasswordError? by rememberSaveable { mutableStateOf(null) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = password,
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = {
                onPasswordChange(it)
                passwordError = it.getPasswordError()
            },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter =
                                if (showPassword) {
                                    painterResource(R.drawable.un_visibility_icon)
                                } else {
                                    painterResource(R.drawable.visibility_icon)
                                },
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            },
            placeholder = { Text(stringResource(R.string.password_label)) },
            visualTransformation =
                if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = keyboardActions ?: KeyboardActions { focusManager.clearFocus() },
            singleLine = true,
            isError = passwordError != null,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth().onFocusChanged { onFocusChange(it.isFocused) },
        )
        AnimatedVisibility(visible = passwordError != null) {
            Column {
                Spacer(Modifier.size(4.dp))
                val text = passwordError?.getMessage()?.asString() ?: ""
                Text(
                    text,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.error
                        ),
                    modifier = Modifier.align(Alignment.Start),
                )
            }
        }
    }
}

@Composable
fun GoogleSignInButton(onGoogleSignIn: () -> Unit) {
    OutlinedButton(
        onClick = onGoogleSignIn,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().height(50.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(painter = painterResource(R.drawable.google_icon), "", Modifier.size(24.dp))
            Spacer(Modifier.size(8.dp))
            Text(
                stringResource(R.string.google_sign_in_button),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    text: String,
    linkText: List<String>,
    hyperlinks: List<String>,
    linkStyle: SpanStyle = SpanStyle(),
    textStyle: TextStyle = TextStyle(),
    textAlign: TextAlign? = null,
) {
    // https://gist.github.com/stevdza-san/ff9dbec0e072d8090e1e6d16e6b73c91
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        linkText.forEachIndexed { index, link ->
            val startIndex = text.indexOf(link, lastIndex)
            val endIndex = startIndex + link.length

            if (startIndex > lastIndex) append(text.substring(lastIndex, startIndex))

            val linkUrL =
                LinkAnnotation.Url(hyperlinks[index], TextLinkStyles(linkStyle)) {
                    val url = (it as LinkAnnotation.Url).url
                    uriHandler.openUri(url)
                }
            withLink(linkUrL) { append(link) }
            append(" ")
            lastIndex = endIndex + 1
        }
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
    Text(text = annotatedString, modifier = modifier, style = textStyle, textAlign = textAlign)
}
