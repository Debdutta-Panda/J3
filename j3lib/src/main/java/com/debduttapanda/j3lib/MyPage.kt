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

@Composable
fun MyPage(
    navController: NavHostController,
    suffix: String,
    wvm: WirelessViewModelInterface,
    content: @Composable () -> Unit
) {
    InitializeMetrics()
    wvm.permissionHandler.handlePermission()
    wvm.resultingActivityHandler.handle()
    LaunchedEffect(key1 = Unit){
        wvm.notifier.notify(WirelessViewModelInterface.startupNotification, null)
    }
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(key1 = wvm.navigation.value){
        wvm.navigation.forward(navController, owner, ActivityService(context))
    }
    // /////////
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = wvm.softInputMode.value) {
        activity.window.setSoftInputMode(wvm.softInputMode.value)
    }
    // /////////
    CompositionLocalProvider(
        LocalResolver provides wvm.resolver,
        LocalNotificationService provides wvm.notifier
    ) {
        OnLifecycleEvent{owner, event ->
            wvm.notifier.notify(WirelessViewModelInterface.lifecycleEvent, event)
        }
        HandleKeyboardVisibility(wvm)
        StatusBarColorControl()
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            content()
            LoaderUI(
                WirelessViewModelInterface.loaderState.value,

            )
        }
        BackHandle(suffix)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HandleKeyboardVisibility(wvm: WirelessViewModelInterface) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = wvm.keyboarder.value){
        keyboardController?.let {
            wvm.keyboarder.forward(it)
        }
    }
}

enum class LoaderState{
    None,
    Loading,
    Success,
    Fail
}

@Composable
fun BoxScope.Indeterminate(){
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
fun BoxScope.Success(){
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
fun BoxScope.Fail(){
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
fun LoaderUI(
    state: LoaderState
){
    if(state!= LoaderState.None){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {  },
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f))
            ){

            }
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500))  with
                            fadeOut(animationSpec = tween(500))
                }
            ) {
                when(it){
                    LoaderState.Loading -> Indeterminate()
                    LoaderState.Success -> Success()
                    LoaderState.Fail -> Fail()
                    else->{}
                }
            }
        }
    }
}

