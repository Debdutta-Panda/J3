package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@NonRestartableComposable
inline fun NavGraphBuilder.MyScreen(
    navController: NavHostController,
    route: Route,
    crossinline wirelessViewModel: ( @Composable ()->WirelessViewModel? ) = {null},
    crossinline content: @Composable () -> Unit
){
    myComposable(
        route.full,
        arguments = route.arguments,
        deepLinks = route.deepLinks
    ){
        MyNavigationPage(
            navController,
            suffix = route.name,
            wvm = wirelessViewModel()
        ) {
            content()
        }
    }
}
