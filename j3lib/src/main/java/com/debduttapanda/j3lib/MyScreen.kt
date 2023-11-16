package com.debduttapanda.j3lib

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.debduttapanda.j3lib.models.Route

@NonRestartableComposable
inline fun NavGraphBuilder.MyScreen(
    navController: NavHostController,
    route: Route,
    crossinline wirelessViewModel: (@Composable () -> WirelessViewModel?) = { null },
    enableDefaultTransition: Boolean = true,
    noinline enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    noinline exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    noinline popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    noinline popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,

    crossinline content: @Composable () -> Unit
) {
    myComposable(
        route.full,
        arguments = route.arguments,
        deepLinks = route.deepLinks,
        enableDefaultTransition = enableDefaultTransition,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) {
        MyNavigationPage(
            navController,
            route = route,
            wvm = wirelessViewModel()
        ) {
            content()
        }
    }
}
