package com.debduttapanda.j3lib

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi


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
fun notifier(): NotificationService {
    return LocalNotificationService.current
}

interface WirelessViewModelInterface{

    fun retrieveResolver(): Resolver{
        addResolverData(__resolver)
        return __resolver
    }

    fun addResolverData(resolver: Resolver)

    val __softInputMode: MutableState<Int>
    val __resolver: Resolver
    val __notifier: NotificationService
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

abstract class WirelessViewModel: WirelessViewModelInterface, ViewModel(){
    val controller by lazy { Controller(__resolver, __notifier) }
    private val __statusBarColor = mutableStateOf<StatusBarColor?>(null)
    override val __softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val __resolver = Resolver()
    override val __notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.back -> {
                onBack()
            }
            else->onNotification(id,arg)
        }
    }

    fun setSoftInputMode(mode: Int){
        __softInputMode.value = mode
    }
    abstract fun onBack()
    abstract fun onStart()
    abstract fun onNotification(id: Any?, arg: Any?)
    override val __navigation = Navigation()
    override val __permissionHandler = PermissionHandler()
    override val __resultingActivityHandler = ResultingActivityHandler()
    init {
        __resolver.addAll(DataIds.statusBarColor to __statusBarColor)
        onStart()
    }


    fun setStatusBarColor(color: Color, darkIcon: Boolean){
        __statusBarColor.value = StatusBarColor(
            color = color,
            darkIcons = darkIcon
        )
    }

    fun navigate(block: NavHostController.()->Unit){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            block(navHostController)
        }
    }

    fun popBackStack(){
        navigate {
            popBackStack()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun String.permitted() = __permissionHandler.check(this)

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun List<String>.permitted() = __permissionHandler.check(*toTypedArray())

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun String.requestPermission() = __permissionHandler.request(this)
    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun List<String>.requestPermission() = __permissionHandler.request(*this.toTypedArray())

    fun goToAppSettings(){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.myAppSettingsPage()
        }
    }

    suspend fun <I,O>requestForResult(
        contract: ActivityResultContract<I, O>,
        maxTry: Int = 10,
        millis: Long = 200,
        launcher: (ManagedActivityResultLauncher<I, O>) -> Unit
    ): O?{
        return __resultingActivityHandler.request(
            contract,
            maxTry,
            millis,
            launcher
        )
    }

    suspend fun takePicturePreview() = __resultingActivityHandler.takePicturePreview()
    suspend fun getContent(type: String) = __resultingActivityHandler.getContent(type)

    fun newController(notificationService: NotificationService) = Controller(Resolver(), notificationService)
}

data class Controller(
    val resolver: Resolver,
    val notificationService: NotificationService
)