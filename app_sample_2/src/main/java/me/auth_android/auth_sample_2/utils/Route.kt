package me.auth_android.auth_sample_2.utils

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable object MainApp : Route

    @Serializable object Auth : Route

    @Serializable object Account : Route

    @Serializable object ReAuth : Route
}
