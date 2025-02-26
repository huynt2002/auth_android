package me.auth_kit_aos.auth_sample_2.utils

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable object MainApp : Route

    @Serializable object Auth : Route

    @Serializable object OnAppAuthView : Route
}
