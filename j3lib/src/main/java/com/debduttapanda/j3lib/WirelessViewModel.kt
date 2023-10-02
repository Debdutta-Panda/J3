package com.debduttapanda.j3lib

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class Resolver(){
    private val _map: MutableMap<Any, Any?> = mutableMapOf()
    fun <T>get(key: Any): T{
        return _map[key] as T
    }

    operator fun set(key: Any, value: Any?){
        _map[key] = value
    }

    fun addAll(map: Map<Any, Any?>){
        _map.putAll(map)
    }

    fun addAll(vararg pairs: Pair<Any, Any?>){
        _map.putAll(pairs)
    }
}

data class NotificationService(
    val callback: (Any, Any?) -> Unit
){
    fun notify(id: Any, arg: Any? = null){
        callback(id, arg)
    }
}

val LocalResolver = compositionLocalOf { Resolver() }
val LocalNotificationService = compositionLocalOf { NotificationService{ _, _ -> } }
val LocalSuffix = compositionLocalOf { "" }

@Composable
fun suffix(): String{
    return LocalSuffix.current
}

@Composable
fun suffix(
    suffix: String,
    content: @Composable () -> Unit
){
    CompositionLocalProvider(
        values = arrayOf(LocalSuffix provides "${LocalSuffix.current}$suffix"),
        content
    )
}

@Composable
fun floatState(key: Any): State<Float> {
    return LocalResolver.current.get(key)
}

@Composable
fun doubleState(key: Any): State<Double> {
    return LocalResolver.current.get(key)
}

@Composable
fun stringState(key: Any): State<String> {
    return LocalResolver.current.get(key)
}

@Composable
fun stringResourceState(key: Any): State<StringResource> {
    return LocalResolver.current.get(key)
}

@Composable
fun boolState(key: Any): State<Boolean> {
    return LocalResolver.current.get(key)
}

@Composable
fun safeBoolState(key: Any): State<Boolean>? {
    return LocalResolver.current.get(key)
}

@Composable
fun intState(key: Any): State<Int> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T>listState(key: Any): SnapshotStateList<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T>safeListState(key: Any): SnapshotStateList<T>? {
    return LocalResolver.current.get(key)
}

@Composable
fun <T, E>mapState(key: Any): SnapshotStateMap<T, E> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T, E>safeMapState(key: Any): SnapshotStateMap<T, E>? {
    return LocalResolver.current.get(key)
}

@Composable
fun <T>t(key: Any): T {
    return LocalResolver.current.get(key)
}

@Composable
fun <T>tState(key: Any): State<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T>safeTState(key: Any): State<T>? {
    return LocalResolver.current.get(key)
}

@Composable
fun notifier(): NotificationService {
    return LocalNotificationService.current
}

interface WirelessViewModelInterface{

    val softInputMode: MutableState<Int>
    val resolver: Resolver
    val notifier: NotificationService
    val navigation: MutableState<UIScope?>
    val permissionHandler: PermissionHandler
    val resultingActivityHandler: ResultingActivityHandler

    fun toast(message: Any){
        navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.toast(str(activityService,message))
        }
    }
    @OptIn(ExperimentalComposeUiApi::class)
    val keyboarder: MutableState<KeyboardScope?> get() = Keyboarder()
    interface LoaderInterface{
        fun clear()
        fun indeterminate()
        suspend fun success()
        suspend fun fail()
    }
    val loader get() = object: LoaderInterface {
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
