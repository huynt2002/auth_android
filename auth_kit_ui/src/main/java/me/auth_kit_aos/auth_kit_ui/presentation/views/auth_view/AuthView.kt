package me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.auth_kit_aos.auth_kit_ui.R
import me.auth_kit_aos.auth_kit_ui.presentation.components.CompactView
import me.auth_kit_aos.auth_kit_ui.presentation.components.MediumOrExpandView
import me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.reset_password_view.ResetPasswordView
import me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.sign_in_view.SignInView
import me.auth_kit_aos.auth_kit_ui.presentation.views.auth_view.sign_up_view.SignUpView
import me.auth_kit_aos.auth_kit_ui.utils.LocalWindowClass
import me.auth_kit_aos.auth_kit_ui.utils.Route
import me.auth_kit_aos.auth_kit_ui.utils.getCurrentRoute
import me.rolingo.core.ui.animation.BackDropEffect
import me.rolingo.core.ui.animation.enterPop
import me.rolingo.core.ui.animation.enterPush
import me.rolingo.core.ui.animation.exitPop
import me.rolingo.core.ui.animation.exitPush

@Composable
fun AuthView(appName: String, appImage: Painter, onAuthorized: () -> Unit) {
    val content: @Composable () -> Unit = {
        val navController = rememberNavController()
        val toSignUpView: (String) -> Unit = { email ->
            navController.navigate(Route.SignUp(email))
        }
        val toResetPasswordView: (String) -> Unit = { email ->
            navController.navigate(Route.ResetPassword(email))
        }
        val navigateBack: () -> Unit = { navController.navigateUp() }
        NavHost(navController = navController, startDestination = Route.SignIn) {
            composable<Route.SignIn>(
                enterTransition = { enterPop() },
                exitTransition = { exitPop() },
                popExitTransition = { exitPop() },
                popEnterTransition = { enterPop() },
            ) {
                val currentNavigationRoute = navController.getCurrentRoute()

                BackDropEffect(
                    currentNavigationRoute,
                    Route.SignIn,
                    {
                        SignInView(
                            appName,
                            appImage,
                            onAuthorized,
                            toSignUpView,
                            toResetPasswordView,
                        )
                    },
                )
            }

            composable<Route.SignUp>(
                enterTransition = { enterPush() },
                exitTransition = { exitPush() },
                popEnterTransition = { enterPush() },
                popExitTransition = { exitPush() },
            ) { entry ->
                val email = entry.toRoute<Route.SignUp>().email
                SignUpView(navigateBack, email)
            }

            composable<Route.ResetPassword>(
                enterTransition = { enterPush() },
                exitTransition = { exitPush() },
                popEnterTransition = { enterPush() },
                popExitTransition = { exitPush() },
            ) { entry ->
                val email = entry.toRoute<Route.ResetPassword>().email
                ResetPasswordView(navigateBack, email)
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

@Preview
@Composable
private fun AuthView_Preview() {
    val appName = "Rolingo"
    val appImage = painterResource(R.drawable.ic_launcher_foreground)
    AuthView(appName, appImage, {})
}
