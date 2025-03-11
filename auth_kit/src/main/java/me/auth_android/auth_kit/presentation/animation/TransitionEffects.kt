package me.rolingo.core.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

val enterPush: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        animationSpec = tween(300, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
    )
}
val exitPush: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        animationSpec = tween(400, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
    )
}
val enterPop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        animationSpec = tween(400, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        initialOffset = { it / 3 },
    )
}
val exitPop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(tween(300, 100, easing = LinearEasing), targetAlpha = 0.5f) +
        slideOutOfContainer(
            animationSpec = tween(1000, easing = LinearEasing),
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
        )
}
