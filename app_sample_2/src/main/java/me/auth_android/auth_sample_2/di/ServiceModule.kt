package me.auth_android.auth_sample_2.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.auth_android.auth_kit.data.auth.defines.Authenticating
import me.auth_android.auth_kit.domain.firebase_auth.FirebaseAuthenticatingImp
import me.auth_android.auth_sample_2.R

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: FirebaseAuthenticatingImp): Authenticating
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun webClientId(@ApplicationContext context: Context): String =
        context.getString(R.string.web_client_id)

    @Provides
    fun termAndPolicyLinks(@ApplicationContext context: Context): List<String> =
        listOf(context.getString(R.string.term_link), context.getString(R.string.policy_link))
}
