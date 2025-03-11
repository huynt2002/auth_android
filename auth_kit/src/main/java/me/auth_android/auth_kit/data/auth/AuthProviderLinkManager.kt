package me.auth_android.auth_kit.data.auth

import android.content.Context

data class AuthProviderLink(
    var email: String? = null,
    var isLinked: Boolean = false,
    var provider: AuthProvider,
)

sealed interface LinkMethod {
    data class WithEmailPassword(val email: String, val password: String) : LinkMethod

    // data class WithEmailLink(val email: String, val link: String) : LinkMethod

    data class WithGoogle(val context: Context) : LinkMethod
}

// object AuthProviderLinkManager {
//    fun getAuthProviderLinks(
//        user: AuthUser,
//        allowedProviders: List<AuthProviderLink>,
//    ): List<AuthProviderLink> {
//        // Process user provider data
//        val links = user.authProviderLinks
//        // Map allowedProviders to either the existing link or a default
//        return allowedProviders.map { link ->
//            links.firstOrNull { it.provider == link.provider } ?: link
//        }
//    }
// }
