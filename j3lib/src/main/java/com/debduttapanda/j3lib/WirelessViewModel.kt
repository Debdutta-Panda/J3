package com.debduttapanda.j3lib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.debduttapanda.j3lib.df.Df
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class EventBusDescription(
    val eventBusId: String,
    val eventBusTopics: (EventBus.TopicsBuilder.()->Unit)? = null,
    val eventBusAction: ((pattern: String, topic: String, value: Any?) -> Unit)? = null
)
abstract class WirelessViewModel: WirelessViewModelInterface, ViewModel(){
    private val uiScopes = mutableListOf<UINavigationScope>()
    fun Bundle.toMap(route: Route): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        route.arguments.forEach {
            val name = it.name
            when(it.argument.type){
                NavType.StringType->{map[name] = getString(name)}
                NavType.IntType->{map[name] = getInt(name)}
                NavType.IntArrayType->{map[name] = getIntArray(name)}
                NavType.LongType->{map[name] = getLong(name)}
                NavType.LongArrayType->{map[name] = getLongArray(name)}
                NavType.BoolType->{map[name] = getBoolean(name)}
                NavType.BoolArrayType->{map[name] = getBooleanArray(name)}
                NavType.StringArrayType->{map[name] = getStringArray(name)}
                NavType.FloatType->{map[name] = getFloat(name)}
                NavType.FloatArrayType->{map[name] = getFloatArray(name)}
            }
        }
        return map
    }

    override val controller by lazy { createController() }

    open fun createController() = Controller(_Resolver(), _NotificationService(::onNotification))

    private val __statusBarColor = mutableStateOf<StatusBarColor?>(null)
    override val __softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val __keyboarder: MutableState<KeyboardScope?> = Keyboarder()

    fun setSoftInputMode(mode: Int){
        __softInputMode.value = mode
    }


    abstract fun onNotification(id: Any?, arg: Any?)
    override val __navigation = Navigation()
    override val __permissionHandler = PermissionHandler()
    override val __resultingActivityHandler = ResultingActivityHandler()
    abstract fun eventBusDescription(): EventBusDescription?
    private var eventBusRegistered = false
    private var eventBusDescription: EventBusDescription? = null

    data class InterCom(
        val sender: String,
        val data: Any?
    )
    abstract fun interCom(message: InterCom)
    inline fun <reified T:WirelessViewModel>interCom(data: Any?){
        EventBus.instance.notify("${T::class.java.simpleName}_internal_j3_not_for_external",InterCom(this.simpleName(),data))
    }
    private val inter_com_suffix = "_internal_j3_not_for_external"
    private fun interComIdentity() = this.simpleName()+inter_com_suffix
    init {
        val id = interComIdentity()
        EventBus.instance.register(
            id,
            {p,t,v->
                interCom(v as InterCom)
            }
        ){
            add(id)
        }
        storeArgs()
        val ed = eventBusDescription()
        if(
            ed?.eventBusId != null
            && ed.eventBusTopics != null
            && ed.eventBusAction != null
        ){
            EventBus.instance.register(ed.eventBusId,ed.eventBusAction,ed.eventBusTopics)
            eventBusDescription = ed
            eventBusRegistered = true
        }

        controller.resolver.addAll(DataIds.statusBarColor to __statusBarColor)
    }

    private fun storeArgs() {
        navigation {
            var args = arguments()
            args?.apply {

            }
        }
    }


    fun setStatusBarColor(color: Color, darkIcon: Boolean){
        __statusBarColor.value = StatusBarColor(
            color = color,
            darkIcons = darkIcon
        )
    }


    fun setStatusBarColor(statusBarColo: StatusBarColor){
        __statusBarColor.value = StatusBarColor(
            color = statusBarColo.color,
            darkIcons = statusBarColo.darkIcons
        )
    }

    private var lastBusyTime = 0L
    private var uiScopeBusy = false

    override fun onForwardStarted() {
        uiScopeBusy = true
        lastBusyTime = System.currentTimeMillis()
    }

    override fun onForwarded() {
        uiScopeBusy = false
        lastBusyTime = 0
        if(uiScopes.isNotEmpty()){
            try {
                __navigation.scope(uiScopes.removeFirst())
            } catch (e: Exception) {

            }
        }
    }
    fun navigation(block: NavHostController.()->Unit){
        val now = System.currentTimeMillis()
        if(uiScopeBusy && lastBusyTime != 0L && (now-lastBusyTime)<4000){
            uiScopes.add { controller, _, _ ->
                controller?.let { block(controller) }
            }
        }
        else{
            onForwardStarted()
            __navigation.scope { navHostController, lifecycleOwner, activityService ->
                block(navHostController?:return@scope)
            }
        }
    }

    fun popBackStack(){
        navigation {
            popBackStack()
        }
    }

    suspend fun String.checkPermission() = __permissionHandler.check(this)

    suspend fun List<String>.checkPermission() = __permissionHandler.check(*toTypedArray())

    suspend fun String.requestPermission() = __permissionHandler.request(this)

    suspend fun List<String>.requestPermission() = __permissionHandler.request(*this.toTypedArray())

    fun goToAppSettings(){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.myAppSettingsPage()
        }
    }

    fun consumeContext(contextConsumer: ContextConsumer){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.letConsumeContext(contextConsumer)
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
    suspend fun intentResult(intent: Intent) = __resultingActivityHandler.intentContract(intent)

    fun newController(notificationService: _NotificationService) = Controller(_Resolver(), notificationService)

    override fun onCleared() {
        clearEventBusRegistration()
        super.onCleared()
    }

    private fun clearEventBusRegistration() {
        EventBus.instance.unregister(interComIdentity())
        if(eventBusRegistered && eventBusDescription?.eventBusId != null){
            EventBus.instance.unregister(eventBusDescription?.eventBusId)
        }
    }

    fun eventBusNotify(topic: String, value: Any? = null){
        EventBus.instance.notify(topic,value)
    }

    fun toast(message: Any,duration: Int = Toast.LENGTH_SHORT){
        val now = System.currentTimeMillis()
        if(uiScopeBusy && lastBusyTime != 0L && (now-lastBusyTime)<4000){
            uiScopes.add { navHostController, lifecycleOwner, activityService ->
                activityService?.toast(str(activityService,message),duration)
            }
        }
        else{
            onForwardStarted()
            __navigation.scope { navHostController, lifecycleOwner, activityService ->
                activityService?.toast(str(activityService,message),duration)
            }
        }
    }

    suspend fun <T>dfer(df: Df<T>, tag: String, block: ((df: Df<T>,topic: Any, value: Any?)->Unit)? = null): T = suspendCancellableCoroutine { continuation ->
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            CoroutineScope(Dispatchers.Main).launch{
                val r = activityService!!.showDf(df, tag, block)
                continuation.resume(r)
                continuation.cancel()
            }
        }
    }

    suspend fun <T>Df<T>.start(tag: String = "", block: ((df: Df<T>,topic: Any, value: Any?)->Unit)? = null) = dfer(this,tag, block)
    fun <T>Df<T>.startScoped(tag: String = "", block: ((df: Df<T>,topic: Any, value: Any?)->Unit)? = null){
        viewModelScope.launch {
            start(tag,block)
        }
    }

    fun hideKeyboard(){
        __keyboarder{
            hide()
        }
    }

    fun showKeyboard(){
        __keyboarder{
            show()
        }
    }


    fun ViewModel.ioScope(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            block(this)
        }
    }

    fun ViewModel.mainScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            block(this)
        }
    }

}

interface ContextConsumer{
    fun consume(context: Context)
}