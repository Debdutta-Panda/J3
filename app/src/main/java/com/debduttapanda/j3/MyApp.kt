package com.debduttapanda.j3

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.debduttapanda.j3lib.MyScreen

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Routes.splash.full
    ) {
        MyScreen(
            navController = navController,
            route = Routes.splash.full,
            { viewModel<SplashViewModel>() }
        ) {
            SplashPage()
        }
        MyScreen(
            navController = navController,
            route = Routes.home.full,
            { viewModel<HomeViewModel>() }
        ) {
            HomePage()
        }
    }
}