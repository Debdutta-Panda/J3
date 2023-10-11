package com.debduttapanda.j3lib

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

val LocalResolver = compositionLocalOf { _Resolver() }
val LocalController = compositionLocalOf { Controller(_Resolver(),_NotificationService{ _, _->}).restricted() }
val LocalNotificationService = compositionLocalOf { _NotificationService{ _, _ -> } }
val LocalSuffix = compositionLocalOf { "" }

@Composable
fun suffix(): String{
    return LocalSuffix.current
}
@Composable
fun controller(): RestrictedController{
    return LocalController.current
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
fun controller(key: Any): Controller {
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
fun notifier(): _NotificationService {
    return LocalNotificationService.current
}






@Composable
fun rememberStringState(id: Any): State<String>{
    val controller = LocalController.current
    return remember(controller) {
        controller.stringState(id)
    }
}
@Composable
fun rememberBoolState(id: Any): State<Boolean>{
    val controller = LocalController.current
    return remember(controller) {
        controller.boolState(id)
    }
}
@Composable
fun rememberNotifier(): NotificationService{
    val controller = LocalController.current
    return remember(controller) { controller.notifier() }
}

@Composable
fun ControlledBy(
    controller: RestrictedController,
    content: @Composable () -> Unit,
){
    CompositionLocalProvider(
        LocalController provides controller
    ){
        content()
    }
}

@Composable
fun ControlledById(
    id: Any,
    content: @Composable () -> Unit,
){
    val controller = LocalController.current.resolver().get<RestrictedController>(id)
    CompositionLocalProvider(
        LocalController provides controller
    ){
        content()
    }
}

@Composable
fun SafeControlledById(
    id: Any,
    content: @Composable () -> Unit,
){
    val controller = LocalController.current.resolver().getOrNull<RestrictedController>(id)
    if(controller!=null){
        CompositionLocalProvider(
            LocalController provides controller
        ){
            content()
        }
    }
}