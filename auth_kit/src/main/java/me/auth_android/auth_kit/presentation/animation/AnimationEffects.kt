package me.rolingo.core.ui.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BackDropEffect(
    currentNavigationRoute: String?,
    effectedRouteClassSimpleName: String,
    content: @Composable () -> Unit,
) {
    var currentNavigated =
        isCurrentNavigatedRoute(currentNavigationRoute, effectedRouteClassSimpleName)
    LaunchedEffect(true) {
        if (!currentNavigated) {
            currentNavigated = true
        }
    }

    val color by
        animateColorAsState(
            targetValue =
                if (currentNavigated) Color.Transparent else Color.Black.copy(alpha = 0.3f),
            animationSpec = tween(300),
            label = "",
        )

    content()

    Box(modifier = Modifier.fillMaxSize().background(color))
}

private fun isCurrentNavigatedRoute(
    currentNavigationRouteName: String?,
    effectedRouteClassSimpleName: String,
): Boolean {
    return currentNavigationRouteName?.contains(effectedRouteClassSimpleName) == true
}
