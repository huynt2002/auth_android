package me.auth_kit_aos.auth_kit_ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable object SignIn : Route

    @Serializable data class SignUp(val email: String = "") : Route

    @Serializable data class ResetPassword(val email: String = "") : Route
}

@Serializable
sealed interface OnAppAuthRoute : Route {
    @Serializable object AccountView : OnAppAuthRoute

    @Serializable object ReAuthView : OnAppAuthRoute

    @Serializable data class ResetPassword(val email: String) : OnAppAuthRoute
}

@Composable
fun NavHostController.getCurrentRoute(): String? {
    return this.currentBackStackEntryAsState().value?.destination?.route?.split(".")?.last()
}
