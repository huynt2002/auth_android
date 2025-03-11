package me.auth_android.auth_kit.data.auth.defines

import android.content.Context
import me.auth_android.auth_kit.data.auth.AuthUser
import me.auth_android.auth_kit.data.auth.BasicSignInProvider
import me.auth_android.auth_kit.data.auth.LinkMethod
import me.auth_android.auth_kit.data.auth.OAuthSignInProvider
import me.auth_android.auth_kit.utils.Result

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
