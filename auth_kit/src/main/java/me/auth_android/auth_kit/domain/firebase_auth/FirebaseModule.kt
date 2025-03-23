package me.auth_android.auth_kit.domain.firebase_auth

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.auth_android.auth_kit.data.sessionService.SessionService

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseModule {
    @Provides fun auth(): FirebaseAuth = Firebase.auth

    @Provides fun context(@ApplicationContext context: Context): Context = context

    @Provides @Singleton fun sessionService(): SessionService = SessionService()
}
