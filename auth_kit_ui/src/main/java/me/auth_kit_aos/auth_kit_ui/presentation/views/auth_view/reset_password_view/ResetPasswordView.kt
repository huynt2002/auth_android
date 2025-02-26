package me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.reset_password_view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.auth_kit_aos.auth_kit_ui.R
import me.auth_kit_aos.auth_kit_ui.presentation.components.EmailInputField
import me.auth_kit_aos.auth_kit_ui.presentation.components.PopUpView

@Composable
fun ResetPasswordView(
    navigateBack: () -> Unit,
    email: String = "",
    viewModel: ResetPasswordViewModel = hiltViewModel<ResetPasswordViewModel>(),
) {
    LaunchedEffect(true) { viewModel.onEmailChange(email) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.navigateEvent.collect {
            when (it) {
                ResetPasswordViewNavigateEvent.NavigateBack -> navigateBack()
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
                stringResource(R.string.forget_password_label),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.size(32.dp))
            EmailInputField(
                uiState.email,
                viewModel::onEmailChange,
                keyboardActions = KeyboardActions({ focusManager.clearFocus() }),
            )
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = { viewModel.onResetClick() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uiState.isEmailValid,
            ) {
                Text(
                    stringResource(R.string.reset_button),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            }
        }
    }
    PopUpView(uiState.popUpState)
}
