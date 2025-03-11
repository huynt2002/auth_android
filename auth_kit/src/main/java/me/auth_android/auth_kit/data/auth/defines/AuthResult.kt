package me.auth_android.auth_kit.data.auth.defines

import me.auth_android.auth_kit.data.auth.AuthUser

// interface PersonalInfo {
//    val fullName: String?
//    val givenName: String?
//    val familyName: String?
//    val profileImageUrl: String?
// }
//
// data class UserInfo(
//    override var fullName: String? = null,
//    override var givenName: String? = null,
//    override var familyName: String? = null,
//    override var profileImageUrl: String? = null,
// ) : PersonalInfo
//
// data class SocialSignInUser(
//    val email: String,
//    override val fullName: String? = null,
//    override val givenName: String? = null,
//    override val familyName: String? = null,
//    override val profileImageUrl: String? = null,
// ) : PersonalInfo
//
// sealed class ProviderLoginInfo {
//    data class Google(val googleUser: SocialSignInUser?) : ProviderLoginInfo()
// }

data class AuthResult(
    val user: AuthUser? = null
    //    var additionalInfo: ProviderLoginInfo? = null
)

// {
//        val userInfo: UserInfo? by lazy {
//            val fallbackPhoto = user?.photoURL
//            val fallback =
//                UserInfo(
//                    fullName = user?.displayName,
//                    givenName = user?.givenName,
//                    familyName = user?.familyName,
//                    profileImageUrl = fallbackPhoto,
//                )
//
//            when (val info = additionalInfo) {
//                is ProviderLoginInfo.Google -> {
//                    val googleUser = info.googleUser
//                    googleUser?.let {
//                        UserInfo(
//                            fullName = it.fullName,
//                            givenName = it.givenName,
//                            familyName = it.familyName,
//                            profileImageUrl = it.profileImageUrl ?: fallbackPhoto,
//                        )
//                    } ?: fallback
//                }
//
//                else -> fallback
//            }
//        }
// }

data class AuthError(val exception: Exception)

sealed class AuthErrorException : Exception() {
    object UserNotFound : AuthErrorException()

    object NeedReauth : AuthErrorException()

    object InvalidProvider : AuthErrorException()

    object UnSupportedCredential : AuthErrorException()
}
