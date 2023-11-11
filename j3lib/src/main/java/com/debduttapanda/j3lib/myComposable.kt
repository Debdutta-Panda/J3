package com.debduttapanda.j3lib

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object DefaultNavigationAnimation{
    private const val animationTime = 300
    val enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        fadeIn(
            animationSpec = tween(
                animationTime, easing = LinearEasing
            )
        ) + slideIntoContainer(
            animationSpec = tween(animationTime, easing = EaseIn),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
    }
    val exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        fadeOut(
            animationSpec = tween(
                animationTime, easing = LinearEasing
            )
        ) + slideOutOfContainer(
            animationSpec = tween(animationTime, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
    }
    val popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        fadeIn(
            animationSpec = tween(
                animationTime, easing = LinearEasing
            )
        ) + slideIntoContainer(
            animationSpec = tween(animationTime, easing = EaseIn),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        )
    }
    val popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        fadeOut(
            animationSpec = tween(
                animationTime, easing = LinearEasing
            )
        ) + slideOutOfContainer(
            animationSpec = tween(animationTime, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        )
    }
}

fun NavGraphBuilder.myComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enableDefaultTransition: Boolean = true,
    enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
){
    composable(
        route,
        arguments,
        deepLinks,
        content = content,
        enterTransition = enterTransition?: if(enableDefaultTransition) DefaultNavigationAnimation.enterTransition else null,
        exitTransition = exitTransition?: if(enableDefaultTransition) DefaultNavigationAnimation.exitTransition else null,
        popEnterTransition = popEnterTransition?: if(enableDefaultTransition) DefaultNavigationAnimation.popEnterTransition else null,
        popExitTransition = popExitTransition?: if(enableDefaultTransition) DefaultNavigationAnimation.popExitTransition else null
    )
}
