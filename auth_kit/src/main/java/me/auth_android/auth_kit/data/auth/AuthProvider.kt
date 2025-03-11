package me.auth_android.auth_kit.data.auth

sealed class AuthProvider {
    object Email : AuthProvider()

    object Google : AuthProvider()

    val identifier: String
        get() =
            when (this) {
                is Email -> "password"
                is Google -> "google.com"
            }

    companion object {
        val allProviders: List<AuthProvider> by lazy { listOf(Email, Google) }
    }
}

sealed class OAuthSignInProvider {
    object Google : OAuthSignInProvider()
    //
    //    override fun toString(): String {
    //        return when (this) {
    //            is Google -> "google"
    //        }
    //    }
}

sealed interface BasicSignInProvider {
    object Anonymous : BasicSignInProvider

    data class EmailAndPassword(val email: String, val password: String) : BasicSignInProvider

    // data class EmailLink(val email: String, val link: String) : BasicSignInProvider
}
