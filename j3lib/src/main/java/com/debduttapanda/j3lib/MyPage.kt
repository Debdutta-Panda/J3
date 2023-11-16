package com.debduttapanda.j3lib

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.debduttapanda.j3lib.models.ActivityService
import com.debduttapanda.j3lib.models.Route
import com.debduttapanda.j3lib.models._NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MyNavigationPage(
    navController: NavHostController,
    route: Route,
    wvm: WirelessViewModel?,
    content: @Composable () -> Unit
) {
    InitializeMetrics()
    wvm?.permissionHandler?.handlePermission()
    wvm?.resultingActivityHandler?.handle()
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.Main) {
            val arguments = navController.currentBackStackEntry?.arguments
            wvm?.onStartUp(route, arguments)
        }
    }
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(key1 = wvm?.navigation?.value) {
        withContext(Dispatchers.Main) {
            wvm?.navigation?.forward(navController, owner, ActivityService(context))
            wvm?.onForwarded()
        }
    }
    // /////////
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = wvm?.softInputMode?.value) {
        activity.window.setSoftInputMode(wvm?.softInputMode?.value ?: return@LaunchedEffect)
    }
    // /////////
    CompositionLocalProvider(
        LocalResolver provides (wvm?.controller?.resolver ?: _Resolver()),
        LocalNotificationService provides (wvm?.controller?.notificationService
            ?: _NotificationService { _, _ -> }),
        LocalController provides (
                wvm
                    ?.controller?.restricted()
                    ?: Controller(
                        wvm
                            ?.controller
                            ?.resolver
                            ?: _Resolver(),
                        wvm
                            ?.controller
                            ?.notificationService
                            ?: _NotificationService { _, _ -> }
                    ).restricted()
                ),
    ) {
        OnLifecycleEvent { owner, event ->
            wvm?.controller?.notificationService?.notify(WirelessViewModel.lifecycleEvent, event)
        }
        HandleKeyboardVisibility(wvm)
        StatusBarColorControl()
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
            LoaderUI(
                WirelessViewModel.loaderState.value
            )
        }
        BackHandle(wvm)
    }
}

@Composable
fun MyPage(
    modifier: Modifier = Modifier,
    wvm: WirelessViewModel?,
    content: @Composable () -> Unit
) {
    wvm?.permissionHandler?.handlePermission()
    wvm?.resultingActivityHandler?.handle()
    LaunchedEffect(key1 = Unit) {
        wvm?.onStartUp()
    }
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(key1 = wvm?.navigation?.value) {
        wvm?.navigation?.forward(owner, ActivityService(context))
    }
    // /////////
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = wvm?.softInputMode?.value) {
        activity.window.setSoftInputMode(wvm?.softInputMode?.value ?: return@LaunchedEffect)
    }
    // /////////
    CompositionLocalProvider(
        LocalResolver provides (wvm?.controller?.resolver ?: _Resolver()),
        LocalNotificationService provides (wvm?.controller?.notificationService
            ?: _NotificationService { _, _ -> }),
        LocalController provides (
                wvm
                    ?.controller?.restricted()
                    ?: Controller(
                        wvm
                            ?.controller
                            ?.resolver
                            ?: _Resolver(),
                        wvm
                            ?.controller
                            ?.notificationService
                            ?: _NotificationService { _, _ -> }
                    ).restricted()
                ),
    ) {
        OnLifecycleEvent { owner, event ->
            wvm?.controller?.notificationService?.notify(WirelessViewModel.lifecycleEvent, event)
        }
        HandleKeyboardVisibility(wvm)
        StatusBarColorControl()
        Box(
            modifier = modifier
        ) {
            content()
            LoaderUI(
                WirelessViewModel.loaderState.value
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun HandleKeyboardVisibility(wvm: WirelessViewModel?) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = wvm?.keyboarder?.value) {
        keyboardController?.let {
            wvm?.keyboarder?.forward(it)
        }
    }
}

internal enum class LoaderState {
    None,
    Loading,
    Success,
    Fail
}

@Composable
internal fun BoxScope.Indeterminate() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loader),
    )
    LottieAnimation(
        composition = composition,
        isPlaying = true,
        iterations = Int.MAX_VALUE,
        modifier = Modifier
            .align(Alignment.Center)
            .size(200.dep)
    )
}

@Composable
internal fun BoxScope.Success() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.success),
    )
    LottieAnimation(
        composition = composition,
        isPlaying = true,
        iterations = 1,
        modifier = Modifier
            .size(200.dep)
            .padding(30.dep)
    )
}

@Composable
internal fun BoxScope.Fail() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.failed),
    )
    LottieAnimation(
        composition = composition,
        isPlaying = true,
        iterations = 1,
        modifier = Modifier
            .size(200.dep)
            .padding(44.dep)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun LoaderUI(
    state: LoaderState
) {
    if (state != LoaderState.None) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f))
            ) {

            }
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) with
                            fadeOut(animationSpec = tween(500))
                }, label = ""
            ) {
                when (it) {
                    LoaderState.Loading -> Indeterminate()
                    LoaderState.Success -> Success()
                    LoaderState.Fail -> Fail()
                    else -> {}
                }
            }
        }
    }
}

