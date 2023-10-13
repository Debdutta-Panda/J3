package com.debduttapanda.j3lib

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi

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
    val eventBusId: String? = null
    val eventBusTopics: (MutableList<String>.()->Unit)? = null
    val eventBusAction: ((pattern: String, topic: String, value: Any?) -> Unit)? = null
    init {
        if(
            eventBusId != null
            && eventBusTopics != null
            && eventBusAction != null
        ){
            EventBus.instance.register(eventBusId,eventBusAction,eventBusTopics)
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
    suspend fun String.check() = __permissionHandler.check(this)

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun List<String>.check() = __permissionHandler.check(*toTypedArray())

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun String.request() = __permissionHandler.request(this)
    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun List<String>.request() = __permissionHandler.request(*this.toTypedArray())

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

    fun newController(notificationService: _NotificationService) = Controller(_Resolver(), notificationService)

    override fun onCleared() {
        if(
            eventBusId != null
            && eventBusTopics != null
            && eventBusAction != null
        ){
            EventBus.instance.unregister(eventBusId)
        }
        super.onCleared()
    }

    fun eventBusNotify(topic: String, value: Any? = null){
        EventBus.instance.notify(topic,value)
    }
}