package com.debduttapanda.j3lib

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@OptIn(ExperimentalAnimationApi::class)
inline fun <reified T : ViewModel> NavGraphBuilder.MyScreen(
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable () -> Unit
){
    myComposable(
        route,
        arguments = arguments,
        deepLinks = deepLinks
    ){
        MyPage(
            navController,
            suffix = route,
            wvm = getInterface<T>() ?: return@myComposable
        ) {
            content()
        }
    }
}

@Composable
inline fun <reified T : ViewModel>getInterface(): WirelessViewModelInterface? {
    return hiltViewModel<T>() as? WirelessViewModelInterface
}
