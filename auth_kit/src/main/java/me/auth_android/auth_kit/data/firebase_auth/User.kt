package me.auth_android.auth_kit.data.firebase_auth

import com.google.firebase.auth.FirebaseUser
import java.util.Date
import kotlinx.coroutines.tasks.await
import me.auth_android.auth_kit.data.auth.AuthProvider
import me.auth_android.auth_kit.data.auth.AuthProviderLink
import me.auth_android.auth_kit.data.auth.AuthUser

internal data class User(
    override val userID: String,
    override val email: String? = null,
    override val isAnonymous: Boolean = false,
    override val displayName: String? = null,
    override val givenName: String? = null,
    override val familyName: String? = null,
    override val photoURL: String? = null,
    override val creationDate: Date? = null,
    override val authProviderLinks: List<AuthProviderLink>,
    override val userIDToken: suspend () -> String?,
) : AuthUser

internal fun FirebaseUser.toAuthUser(): AuthUser {
    // temp
    val list: MutableList<AuthProviderLink> = mutableListOf()
    if (!this.isAnonymous) {
        AuthProvider.allProviders.forEach {
            val provider =
                this.providerData.find { provider -> provider.providerId == it.identifier }
            val link =
                if (provider != null) {
                    AuthProviderLink(email = provider.email, provider = it, isLinked = true)
                } else {
                    AuthProviderLink(provider = it, isLinked = false)
                }
            list.add(link)
        }
    }

    val ggMethod = this.providerData.find { it.providerId == AuthProvider.Google.identifier }
    val email = ggMethod?.email ?: this.email
    val displayName =
        ggMethod?.displayName ?: if (this.displayName.isNullOrEmpty()) null else this.displayName
    val photoURL = ggMethod?.photoUrl ?: this.photoUrl
    // need use nameFormatComponent
    val nameComponents = displayName?.split(" ") ?: emptyList()
    val given = if (nameComponents.isNotEmpty()) nameComponents[0] else null
    val familyName = if (nameComponents.size > 1) nameComponents[1] else null

    val creationDate = this.metadata?.creationTimestamp?.let { Date(it) }

    return User(
        userID = this.uid,
        email = email,
        isAnonymous = this.isAnonymous,
        displayName = displayName,
        photoURL = photoURL.toString(),
        givenName = given,
        familyName = familyName,
        creationDate = creationDate,
        authProviderLinks = list,
        userIDToken = { this.getIdToken(true).await().token },
    )
}
