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
import me.auth_android.auth_kit.presentation.components.AdaptiveView
import me.auth_android.auth_kit.presentation.utils.OnAppAuthRoute
import me.auth_android.auth_kit.presentation.utils.getCurrentRoute
import me.auth_android.auth_kit.presentation.views.auth_view.reset_password_view.ResetPasswordView
import me.auth_android.auth_kit.presentation.views.on_app_auth_view.reauth_view.ReAuthView
import me.rolingo.core.ui.animation.BackDropEffect
import me.rolingo.core.ui.animation.enterPop
import me.rolingo.core.ui.animation.enterPush
import me.rolingo.core.ui.animation.exitPop
import me.rolingo.core.ui.animation.exitPush

@Composable
fun AppReAuthView(onNavigateBack: () -> Unit) {
    val navHostController = rememberNavController()
    var lastRoute by remember { mutableStateOf(OnAppAuthRoute.ReAuthView::class.java.simpleName) }
    AdaptiveView {
        NavHost(startDestination = OnAppAuthRoute.ReAuthView, navController = navHostController) {
            composable<OnAppAuthRoute.ReAuthView>(
                enterTransition = {
                    if (lastRoute == OnAppAuthRoute.ResetPassword::class.java.simpleName) enterPop()
                    else null
                },
                exitTransition = { exitPop() },
            ) {
                val currentRoute = navHostController.getCurrentRoute()
                BackDropEffect(
                    currentRoute,
                    OnAppAuthRoute.ReAuthView::class.simpleName.toString(),
                ) {
                    ReAuthView(
                        onBack = onNavigateBack,
                        toResetPassword = { email ->
                            navHostController.navigate(OnAppAuthRoute.ResetPassword(email))
                            lastRoute = OnAppAuthRoute.ReAuthView::class.java.simpleName
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
                        lastRoute = OnAppAuthRoute.ResetPassword::class.java.simpleName
                    },
                    email,
                )
            }
        }
    }
}
