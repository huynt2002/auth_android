package me.auth_android.auth_kit.presentation.views.on_app_auth_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.auth_android.auth_kit.presentation.components.CompactView
import me.auth_android.auth_kit.presentation.components.MediumOrExpandView
import me.auth_android.auth_kit.presentation.views.auth_view.reset_password_view.ResetPasswordView
import me.auth_android.auth_kit.presentation.views.on_app_auth_view.account_view.AccountView
import me.auth_android.auth_kit.presentation.views.on_app_auth_view.reauth_view.ReAuthView
import me.auth_android.auth_kit.presentation.utils.LocalWindowClass
import me.auth_android.auth_kit.presentation.utils.OnAppAuthRoute
import me.auth_android.auth_kit.presentation.utils.getCurrentRoute
import me.rolingo.core.ui.animation.BackDropEffect
import me.rolingo.core.ui.animation.enterPop
import me.rolingo.core.ui.animation.enterPush
import me.rolingo.core.ui.animation.exitPop
import me.rolingo.core.ui.animation.exitPush

@Composable
fun OnAppAuthView(toSignInView: () -> Unit, onNavigateBack: () -> Unit) {
    val navHostController = rememberNavController()
    val currentRoute = navHostController.getCurrentRoute()
    var lastRoute by remember { mutableStateOf(OnAppAuthRoute.AccountView.toString()) }
    val content: @Composable () -> Unit = {
        NavHost(startDestination = OnAppAuthRoute.AccountView, navController = navHostController) {
            composable<OnAppAuthRoute.AccountView>() {
                BackDropEffect(
                    currentNavigationRoute = currentRoute,
                    effectedRoute = OnAppAuthRoute.AccountView,
                ) {
                    AccountView(
                        toSignInView = toSignInView,
                        onNavigateBack = onNavigateBack,
                        toReAuth = {
                            navHostController.navigate(OnAppAuthRoute.ReAuthView)
                            lastRoute = toString()
                        },
                    )
                }
            }
            composable<OnAppAuthRoute.ReAuthView>(
                enterTransition = {
                    if (lastRoute == toString()) enterPop() else null
                },
                exitTransition = {
                    if (
                        navHostController.currentDestination
                            ?.route
                            ?.contains(
                                OnAppAuthRoute.AccountView::class.qualifiedName.toString()
                            ) == true
                    )
                        null
                    else exitPop()
                },
            ) {
                BackDropEffect(currentRoute, OnAppAuthRoute.ReAuthView) {
                    ReAuthView(
                        {
                            navHostController.navigateUp()
                            lastRoute = toString()
                        },
                        { email ->
                            navHostController.navigate(OnAppAuthRoute.ResetPassword(email))
                            lastRoute = toString()
                        },
                    )
                }
            }

            composable<OnAppAuthRoute.ResetPassword>(
                enterTransition = { enterPush() },
                exitTransition = { exitPush() },
            ) { entry ->
                val email = entry.toRoute<OnAppAuthRoute.ResetPassword>().email
                ResetPasswordView(
                    {
                        navHostController.navigateUp()
                        lastRoute = toString()
                    },
                    email,
                )
            }
        }
    }
    val windowClass = LocalWindowClass.getLocalWindowClass()
    if (windowClass == LocalWindowClass.WindowClass.COMPACT) {
        CompactView(content)
    } else {
        MediumOrExpandView(content)
    }
}
