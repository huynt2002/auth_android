package me.auth_android.auth_kit.presentation.model

import java.util.Date
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import me.auth_android.auth_kit.data.auth.AuthProviderLink
import me.auth_android.auth_kit.data.auth.AuthUser

data class AuthUserUI(
    val userID: String,
    val email: String? = null,
    val isAnonymous: Boolean = false,
    val displayName: String? = null,
    val givenName: String? = null,
    val familyName: String? = null,
    val photoURL: String? = null,
    val creationDate: Date? = null,
    val authProviderLinks: ImmutableList<AuthProviderLink>,
)

fun AuthUser.toUserUI(): AuthUserUI {
    return AuthUserUI(
        userID = this.userID,
        email = this.email,
        isAnonymous = this.isAnonymous,
        displayName = this.displayName,
        givenName = this.givenName,
        familyName = this.familyName,
        photoURL = this.photoURL,
        creationDate = this.creationDate,
        authProviderLinks = this.authProviderLinks.toPersistentList(),
    )
}
