package me.auth_kit_aos.auth_kit.auth.defines

import android.content.Context
import me.auth_kit_aos.auth_kit.auth.AuthUser
import me.auth_kit_aos.auth_kit.auth.BasicSignInProvider
import me.auth_kit_aos.auth_kit.auth.LinkMethod
import me.auth_kit_aos.auth_kit.auth.OAuthSignInProvider
import me.auth_kit_aos.utils.Result

interface Authenticating {
    val currentUser: AuthUser?

    suspend fun signIn(provider: BasicSignInProvider): Result<AuthResult, AuthError>

    suspend fun signUp(
        email: String,
        password: String,
        userName: String,
    ): Result<AuthResult, AuthError>

    suspend fun signOut(context: Context): Result<Unit, AuthError>

    suspend fun deleteUser(): Result<Unit, AuthError>

    suspend fun resetPassword(email: String): Result<Unit, AuthError>

    suspend fun sendEmailVerification()

    suspend fun signIn(
        provider: OAuthSignInProvider,
        context: Context,
    ): Result<AuthResult, AuthError>

    suspend fun unlink(providerID: String): Result<Unit, AuthError>

    suspend fun link(method: LinkMethod): Result<Unit, AuthError>

    suspend fun reAuthenticate(provider: BasicSignInProvider): Result<Unit, AuthError>

    suspend fun reAuthenticate(
        provider: OAuthSignInProvider,
        context: Context,
    ): Result<Unit, AuthError>
}
