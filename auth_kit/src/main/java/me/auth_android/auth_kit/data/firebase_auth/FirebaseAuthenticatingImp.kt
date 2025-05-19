package me.auth_android.auth_kit.data.firebase_auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import me.auth_android.auth_kit.data.auth.AuthUser
import me.auth_android.auth_kit.data.auth.BasicSignInProvider
import me.auth_android.auth_kit.data.auth.LinkMethod
import me.auth_android.auth_kit.data.auth.OAuthSignInProvider
import me.auth_android.auth_kit.data.auth.defines.AuthError
import me.auth_android.auth_kit.data.auth.defines.AuthErrorException
import me.auth_android.auth_kit.data.auth.defines.AuthResult
import me.auth_android.auth_kit.data.auth.defines.Authenticating
import me.auth_android.auth_kit.data.sessionService.SessionService
import me.auth_android.auth_kit.utils.Failure
import me.auth_android.auth_kit.utils.Result
import me.auth_android.auth_kit.utils.Success

class FirebaseAuthenticatingImp
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val webClientId: String,
    private val sessionService: SessionService,
) : Authenticating {
    override val currentUser: AuthUser?
        get() = auth.currentUser?.toAuthUser()

    override suspend fun signIn(provider: BasicSignInProvider): Result<AuthResult, AuthError> {
        return try {
            val result =
                when (provider) {
                    is BasicSignInProvider.EmailAndPassword -> {
                        val rs =
                            auth
                                .signInWithEmailAndPassword(provider.email, provider.password)
                                .await()
                                .user
                        AuthResult(rs?.toAuthUser())
                    }

                    is BasicSignInProvider.Anonymous -> {
                        AuthResult(auth.signInAnonymously().await().user?.toAuthUser())
                    }
                }
            sessionService.updateTime()
            Success(result)
        } catch (e: FirebaseException) {
            Failure(AuthError(e))
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit, AuthError> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Success(Unit)
        } catch (e: FirebaseException) {
            Failure(AuthError(e))
        }
    }

    override suspend fun sendEmailVerification() {
        try {
            auth.currentUser?.sendEmailVerification()?.await()
        } catch (e: Exception) {
            print(e.message)
        }
    }

    override suspend fun signIn(
        provider: OAuthSignInProvider,
        context: Context,
    ): Result<AuthResult, AuthError> {
        try {
            when (val credential = getGoogleCredential(context)) {
                is Success -> {
                    val user = auth.signInWithCredential(credential.value).await().user
                    sessionService.updateTime()
                    return Success(AuthResult(user?.toAuthUser()))
                }

                is Failure -> return credential
            }
        } catch (e: Exception) {
            return Failure(AuthError(e))
        }
    }

    override suspend fun unlink(providerID: String): Result<Unit, AuthError> {
        return try {
            if (needReAuth()) {
                throw AuthErrorException.NeedReauth
            }
            auth.currentUser?.unlink(providerID)?.await()
            Success(Unit)
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String,
    ): Result<AuthResult, AuthError> {
        return try {
            val rs = auth.createUserWithEmailAndPassword(email, password).await()?.user
            sessionService.updateTime()
            val profile = userProfileChangeRequest {
                UserProfileChangeRequest.Builder().displayName = userName
            }
            auth.currentUser?.updateProfile(profile)?.await()
            Success(AuthResult(rs?.toAuthUser()))
        } catch (e: FirebaseException) {
            Failure(AuthError(e))
        }
    }

    override suspend fun link(method: LinkMethod): Result<Unit, AuthError> {
        try {
            val credential =
                when (method) {
                    is LinkMethod.WithEmailPassword ->
                        EmailAuthProvider.getCredential(method.email, method.password)

                    is LinkMethod.WithGoogle -> {
                        when (val credential = getGoogleCredential(method.context)) {
                            is Success -> credential.value
                            is Failure -> return credential
                        }
                    }
                }

            auth.currentUser?.linkWithCredential(credential)?.await()
            return Success(Unit)
        } catch (e: Exception) {
            return Failure(AuthError(e))
        }
    }

    override suspend fun reAuthenticate(provider: BasicSignInProvider): Result<Unit, AuthError> {
        val credential =
            when (provider) {
                is BasicSignInProvider.Anonymous -> throw AuthErrorException.InvalidProvider
                is BasicSignInProvider.EmailAndPassword ->
                    EmailAuthProvider.getCredential(provider.email, provider.password)
            }
        return try {
            auth.currentUser?.reauthenticate(credential)?.await()
            sessionService.updateTime()
            Success(Unit)
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }

    override suspend fun reAuthenticate(
        provider: OAuthSignInProvider,
        context: Context,
    ): Result<Unit, AuthError> {
        return try {
            when (val credential = getGoogleCredential(context)) {
                is Success -> {
                    auth.currentUser?.reauthenticate(credential.value)?.await()
                    sessionService.updateTime()
                    Success(Unit)
                }

                is Failure -> credential
            }
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }

    override suspend fun signOut(context: Context): Result<Unit, AuthError> {
        return try {
            auth.signOut()
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest(
                    ClearCredentialStateRequest.TYPE_CLEAR_RESTORE_CREDENTIAL
                )
            )
            Success(Unit)
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }

    override suspend fun deleteUser(): Result<Unit, AuthError> {
        return try {
            if (needReAuth()) {
                throw AuthErrorException.NeedReauth
            }
            if (auth.currentUser == null) {
                throw AuthErrorException.UserNotFound
            }
            auth.currentUser?.delete()?.await()
            Success(Unit)
        } catch (e: Exception) {
            Failure(AuthError(e))
        }
    }

    private suspend fun getGoogleCredential(context: Context): Result<AuthCredential, AuthError> {
        val credentialManager = CredentialManager.create(context)
        val request =
            GetCredentialRequest.Builder().addCredentialOption(getSignInGoogleOption()).build()
        try {
            val result = credentialManager.getCredential(context, request)
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(result.credential.data)
                        val googleTokenId = googleIdTokenCredential.idToken
                        return Success(GoogleAuthProvider.getCredential(googleTokenId, null))
                    } else {
                        throw AuthErrorException.UnSupportedCredential
                    }
                }

                else -> {
                    throw AuthErrorException.UnSupportedCredential
                }
            }
        } catch (e: Exception) {
            return Failure(AuthError(e))
        }
    }

    // use for google accounts that have been store in the device
    private fun getGoogleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setNonce(createNonce())
            .build()
    }

    private fun getSignInGoogleOption(): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(serverClientId = webClientId)
            .setNonce(createNonce())
            .build()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun needReAuth(): Boolean {
        return sessionService.isExpired() && currentUser?.isAnonymous == false
    }
}
