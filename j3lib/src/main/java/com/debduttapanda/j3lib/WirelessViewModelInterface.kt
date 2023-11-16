package com.debduttapanda.j3lib

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface WirelessViewModelInterface{
    val controller: Controller

    val __softInputMode: MutableState<Int>

    val __navigation: MutableState<UINavigationScope?>
    val __permissionHandler: PermissionHandler
    val __resultingActivityHandler: ResultingActivityHandler
    fun onStartUp(route: Route? = null, arguments: Bundle? = null)
    fun _toast(message: Any,duration: Int = Toast.LENGTH_SHORT){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            CoroutineScope(Dispatchers.Main).launch {
                activityService?.toast(str(activityService,message),duration)
            }
        }
    }
    fun onBack()
    fun onForwarded()
    fun onForwardStarted()

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
        const val lifecycleEvent = -9999
        internal val loaderState = mutableStateOf(LoaderState.None)
    }
}