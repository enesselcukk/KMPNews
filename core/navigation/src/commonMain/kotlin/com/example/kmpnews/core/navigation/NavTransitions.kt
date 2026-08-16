package com.example.kmpnews.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

private const val NAV_ANIMATION_DURATION_MS = 450

fun <T : Any> kmpNewsForwardTransitionSpec():
    AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(NAV_ANIMATION_DURATION_MS),
    ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS)) togetherWith
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth / 3 },
            animationSpec = tween(NAV_ANIMATION_DURATION_MS),
        ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
}

fun <T : Any> kmpNewsPopTransitionSpec():
    AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / 3 },
        animationSpec = tween(NAV_ANIMATION_DURATION_MS),
    ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS)) togetherWith
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(NAV_ANIMATION_DURATION_MS),
        ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
}
