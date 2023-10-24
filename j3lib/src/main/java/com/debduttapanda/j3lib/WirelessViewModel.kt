package com.debduttapanda.j3lib

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.debduttapanda.j3lib.df.Df
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

data class EventBusDescription(
    val eventBusId: String? = null,
    val eventBusTopics: (MutableList<String>.()->Unit)? = null,
    val eventBusAction: ((pattern: String, topic: String, value: Any?) -> Unit)? = null
)

abstract class WirelessViewModel: WirelessViewModelInterface, ViewModel(){
    override val controller by lazy { createController() }

    open fun createController() = Controller(_Resolver(), _NotificationService(::onNotification))

    private val __statusBarColor = mutableStateOf<StatusBarColor?>(null)
    override val __softInputMode = mutableStateOf(SoftInputMode.adjustNothing)

    fun setSoftInputMode(mode: Int){
        __softInputMode.value = mode
    }
    abstract fun onBack()
    abstract fun onStart()
    abstract fun onNotification(id: Any?, arg: Any?)
    override val __navigation = Navigation()
    override val __permissionHandler = PermissionHandler()
    override val __resultingActivityHandler = ResultingActivityHandler()
    abstract fun eventBusDescription(): EventBusDescription?
    private var eventBusRegistered = false
    private var eventBusDescription: EventBusDescription? = null
    init {
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
        onStart()
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

    fun navigation(block: NavHostController.()->Unit){
        __navigation.scope { navHostController, lifecycleOwner, activityService ->
            block(navHostController!!)
        }
    }

    fun popBackStack(){
        navigation {
            popBackStack()
        }
    }

    suspend fun String.check() = __permissionHandler.check(this)

    suspend fun List<String>.check() = __permissionHandler.check(*toTypedArray())

    suspend fun String.request() = __permissionHandler.request(this)

    suspend fun List<String>.request() = __permissionHandler.request(*this.toTypedArray())

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

    fun newController(notificationService: _NotificationService) = Controller(_Resolver(), notificationService)

    override fun onCleared() {
        if(eventBusRegistered && eventBusDescription?.eventBusId != null){
            EventBus.instance.unregister(eventBusDescription?.eventBusId)
        }
        super.onCleared()
    }

    fun eventBusNotify(topic: String, value: Any? = null){
        EventBus.instance.notify(topic,value)
    }

    fun toast(message: Any,duration: Int = Toast.LENGTH_SHORT){
        viewModelScope.launch(Dispatchers.Main) {
            _toast(message,duration)
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

    suspend fun <T>Df<T>.start(tag: String, block: ((df: Df<T>,topic: Any, value: Any?)->Unit)? = null) = dfer(this,tag, block)

}

interface ContextConsumer{
    fun consume(context: Context)
}