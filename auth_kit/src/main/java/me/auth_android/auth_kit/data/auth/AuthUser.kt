package me.auth_android.auth_kit.data.auth

import java.util.Date

interface AuthUser {
    val userID: String
    val email: String?
    val isAnonymous: Boolean
    val displayName: String?
    val givenName: String?
    val familyName: String?
    val photoURL: String?
    val creationDate: Date?
    val authProviderLinks: List<AuthProviderLink>
    val userIDToken: suspend () -> String?
}
