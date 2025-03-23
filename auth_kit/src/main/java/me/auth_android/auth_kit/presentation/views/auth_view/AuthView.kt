package me.auth_android.auth_kit.presentation.views.auth_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.auth_android.auth_kit.R
import me.auth_android.auth_kit.presentation.components.AdaptiveView
import me.auth_android.auth_kit.presentation.utils.Route
import me.auth_android.auth_kit.presentation.utils.getCurrentRoute
import me.auth_android.auth_kit.presentation.views.auth_view.reset_password_view.ResetPasswordView
import me.auth_android.auth_kit.presentation.views.auth_view.sign_in_view.SignInView
import me.auth_android.auth_kit.presentation.views.auth_view.sign_up_view.SignUpView
import me.rolingo.core.ui.animation.BackDropEffect
import me.rolingo.core.ui.animation.enterPop
import me.rolingo.core.ui.animation.enterPush
import me.rolingo.core.ui.animation.exitPop
import me.rolingo.core.ui.animation.exitPush

@Composable
fun AuthView(appName: String, appImage: Painter, onAuthorized: () -> Unit) {
    AdaptiveView {
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
                    Route.SignIn::class.simpleName.toString(),
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
}

@Preview
@Composable
private fun AuthView_Preview() {
    val appName = "Rolingo"
    val appImage = painterResource(R.drawable.ic_launcher_foreground)
    AuthView(appName, appImage, {})
}
