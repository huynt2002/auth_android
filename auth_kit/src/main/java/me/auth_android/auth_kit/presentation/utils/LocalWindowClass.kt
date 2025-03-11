package me.auth_android.auth_kit.presentation.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

class LocalWindowClass {
    enum class WindowClass {
        COMPACT,
        MEDIUM,
        EXPANDED,
    }

    companion object Local {
        @Composable
        fun getLocalWindowClass(): WindowClass {
            val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            val windowWidthClass = windowSizeClass.windowWidthSizeClass
            val windowHeightClass = windowSizeClass.windowHeightSizeClass
            if (
                windowWidthClass == WindowWidthSizeClass.COMPACT ||
                    windowHeightClass == WindowHeightSizeClass.COMPACT
            ) {
                return WindowClass.COMPACT
            }
            if (windowWidthClass == WindowWidthSizeClass.EXPANDED) {
                return WindowClass.EXPANDED
            }
            return WindowClass.MEDIUM
        }
    }
}
