package me.auth_android.auth_sample_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.auth_android.auth_kit.presentation.views.account_view.AccountView
import me.auth_android.auth_kit.presentation.views.auth_view.AuthView
import me.auth_android.auth_kit.presentation.views.on_app_auth_view.AppReAuthView
import me.auth_android.auth_sample_2.presentation.AppView
import me.auth_android.auth_sample_2.ui.theme.Auth_sampleTheme
import me.auth_android.auth_sample_2.utils.Route

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Auth_sampleTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    Box(Modifier.fillMaxSize().padding(innerPadding)) {
                        NavHost(navController, startDestination = Route.Auth) {
                            composable<Route.Auth> {
                                AuthView(
                                    "ExmaApp",
                                    painterResource(R.drawable.ic_launcher_foreground),
                                    {
                                        navController.navigate(Route.MainApp) {
                                            popUpTo(Route.Auth) { inclusive = true }
                                        }
                                    },
                                )
                            }

                            composable<Route.MainApp> {
                                AppView(onAccountView = { navController.navigate(Route.Account) })
                            }

                            //temp
                            composable<Route.Account> {
                                AccountView(
                                    toSignInView = {
                                        navController.navigate(Route.Auth) {
                                            popUpTo(Route.MainApp) { inclusive = true }
                                        }
                                    },
                                    onNavigateBack = { navController.navigateUp() },
                                    toReAuth = { navController.navigate(Route.ReAuth) },
                                )
                            }

                            composable<Route.ReAuth> {
                                AppReAuthView(onNavigateBack = { navController.navigateUp() })
                            }
                        }
                    }
                }
            }
        }
    }
}
