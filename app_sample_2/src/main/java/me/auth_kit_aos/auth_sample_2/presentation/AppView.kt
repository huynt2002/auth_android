package me.auth_kit_aos.auth_sample_2.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AppView(onAccountView: () -> Unit, appViewModel: AppViewModel = hiltViewModel()) {
    val userName = appViewModel.user.collectAsStateWithLifecycle().value
    Column {
        Text("Hello $userName")
        Button(onClick = onAccountView) { Text("Account Setting") }
    }
}

@Preview
@Composable
private fun Preview() {
    AppView({})
}
