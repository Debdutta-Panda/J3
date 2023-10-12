package com.debduttapanda.j3lib

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi

interface WirelessViewModelInterface{
    val controller: Controller

    val __softInputMode: MutableState<Int>

    val __navigation: MutableState<UIScope?>
    val __permissionHandler: PermissionHandler
    val __resultingActivityHandler: ResultingActivityHandler

    fun toast(message: Any){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.toast(str(activityService,message))
        }
    }
    @OptIn(ExperimentalComposeUiApi::class)
    val __keyboarder: MutableState<KeyboardScope?> get() = Keyboarder()
    interface LoaderInterface{
        fun clear()
        fun indeterminate()
        suspend fun success()
        suspend fun fail()
    }
    private val __loader get() = object: LoaderInterface {
        override fun clear(){
            loaderState.value = LoaderState.None
        }
        override fun indeterminate(){
            loaderState.value = LoaderState.Loading
        }
        override suspend fun success(){
            loaderState.value = LoaderState.Success
        }
        override suspend fun fail(){
            loaderState.value = LoaderState.Fail
        }
    }
    companion object{
        const val startupNotification = -10000
        const val lifecycleEvent = -9999
        internal val loaderState = mutableStateOf(LoaderState.None)
    }
}