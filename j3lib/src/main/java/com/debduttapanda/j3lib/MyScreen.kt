package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@NonRestartableComposable
inline fun NavGraphBuilder.MyScreen(
    navController: NavHostController,
    route: String,
    crossinline wirelessViewModel: ( @Composable ()->WirelessViewModel? ) = {null},
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
            wvm = wirelessViewModel()
        ) {
            content()
        }
    }
}
