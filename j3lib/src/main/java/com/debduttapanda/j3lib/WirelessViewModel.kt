package com.debduttapanda.j3lib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.debduttapanda.j3lib.df.Df
import com.debduttapanda.j3lib.models.EventBusDescription
import com.debduttapanda.j3lib.models.Route
import com.debduttapanda.j3lib.models.StatusBarColor
import com.debduttapanda.j3lib.models._NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


abstract class WirelessViewModel : ViewModel() {
    // properties
    val controller by lazy { createController() }
    val loader
        get() = object : LoaderInterface {
            override fun clear() {
                loaderState.value = LoaderState.None
            }

            override fun indeterminate() {
                loaderState.value = LoaderState.Loading
            }

            override suspend fun success() {
                loaderState.value = LoaderState.Success
            }

            override suspend fun fail() {
                loaderState.value = LoaderState.Fail
            }
        }

    // private properties
    private val uiMessage = mutableStateOf<Event<Any?>?>(null)
    private val uiScopes = mutableListOf<UINavigationScope>()
    private val statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private var eventBusRegistered = false
    private var eventBusDescription: EventBusDescription? = null
    private val interComSuffix = "_internal_j3_not_for_external"
    private var lastBusyUiScopeBusyTime = 0L
    private var uiScopeBusy = false

    // internal properties
    internal val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    internal val keyboarder: MutableState<KeyboardScope?> = Keyboarder()
    internal val navigation = Navigation()
    internal val permissionHandler = PermissionHandler()
    internal val resultingActivityHandler = ResultingActivityHandler()

    // private methods
    private fun clearEventBusRegistration() {
        EventBus.instance.unregister(interComIdentity())
        if (eventBusRegistered && eventBusDescription?.eventBusId != null) {
            EventBus.instance.unregister(eventBusDescription?.eventBusId)
        }
    }

    private fun onForwardStarted() {
        uiScopeBusy = true
        lastBusyUiScopeBusyTime = System.currentTimeMillis()
    }

    private fun interComIdentity() = this.simpleName() + interComSuffix

    // internal methods
    internal fun onForwarded() {
        uiScopeBusy = false
        lastBusyUiScopeBusyTime = 0
        if (uiScopes.isNotEmpty()) {
            try {
                navigation.scope(uiScopes.removeFirst())
            } catch (e: Exception) {

            }
        }
    }

    // methods
    fun goToAppSettings() {
        navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.myAppSettingsPage()
        }
    }

    fun consumeContext(contextConsumer: ContextConsumer) {
        navigation.scope { navHostController, lifecycleOwner, activityService ->
            activityService?.letConsumeContext(contextConsumer)
        }
    }

    fun newController(notificationService: _NotificationService) =
        Controller(_Resolver(), notificationService)

    fun eventBusNotify(topic: String, value: Any? = null) {
        EventBus.instance.notify(topic, value)
    }

    fun <T> Df<T>.startScoped(
        tag: String = "",
        block: ((df: Df<T>, topic: Any, value: Any?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            start(tag, block)
        }
    }

    fun hideKeyboard() {
        keyboarder {
            hide()
        }
    }

    fun showKeyboard() {
        keyboarder {
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

    fun navigation(block: NavHostController.() -> Unit) {
        val now = System.currentTimeMillis()
        if (uiScopeBusy && lastBusyUiScopeBusyTime != 0L && (now - lastBusyUiScopeBusyTime) < 4000) {
            uiScopes.add { controller, _, _ ->
                controller?.let { block(controller) }
            }
        } else {
            onForwardStarted()
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                block(navHostController ?: return@scope)
            }
        }
    }

    fun popBackStack() {
        navigation {
            popBackStack()
        }
    }

    fun toast(message: Any, duration: Int = Toast.LENGTH_SHORT) {
        val now = System.currentTimeMillis()
        if (uiScopeBusy && lastBusyUiScopeBusyTime != 0L && (now - lastBusyUiScopeBusyTime) < 4000) {
            uiScopes.add { navHostController, lifecycleOwner, activityService ->
                activityService?.toast(str(activityService, message), duration)
            }
        } else {
            onForwardStarted()
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                activityService?.toast(str(activityService, message), duration)
            }
        }
    }

    fun setStatusBarColor(color: Color, darkIcon: Boolean) {
        statusBarColor.value = StatusBarColor(
            color = color, darkIcons = darkIcon
        )
    }


    fun setStatusBarColor(statusBarColo: StatusBarColor) {
        statusBarColor.value = StatusBarColor(
            color = statusBarColo.color, darkIcons = statusBarColo.darkIcons
        )
    }

    inline fun <reified T : WirelessViewModel> interCom(data: Any?) {
        EventBus.instance.notify(
            "${T::class.java.simpleName}_internal_j3_not_for_external",
            InterCom(this.simpleName(), data)
        )
    }

    fun Bundle.toMap(route: Route): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        route.arguments.forEach {
            val name = it.name
            when (it.argument.type) {
                NavType.StringType -> {
                    map[name] = getString(name)
                }

                NavType.IntType -> {
                    map[name] = getInt(name)
                }

                NavType.IntArrayType -> {
                    map[name] = getIntArray(name)
                }

                NavType.LongType -> {
                    map[name] = getLong(name)
                }

                NavType.LongArrayType -> {
                    map[name] = getLongArray(name)
                }

                NavType.BoolType -> {
                    map[name] = getBoolean(name)
                }

                NavType.BoolArrayType -> {
                    map[name] = getBooleanArray(name)
                }

                NavType.StringArrayType -> {
                    map[name] = getStringArray(name)
                }

                NavType.FloatType -> {
                    map[name] = getFloat(name)
                }

                NavType.FloatArrayType -> {
                    map[name] = getFloatArray(name)
                }
            }
        }
        return map
    }

    fun setSoftInputMode(mode: Int) {
        softInputMode.value = mode
    }
    fun uiNotify(data: Any?, id: Any? = null){
        uiMessage.value = Event(data, id)
    }

    suspend fun <I, O> requestForResult(
        contract: ActivityResultContract<I, O>,
        maxTry: Int = 10,
        millis: Long = 200,
        launcher: (ManagedActivityResultLauncher<I, O>) -> Unit
    ): O? {
        return resultingActivityHandler.request(
            contract, maxTry, millis, launcher
        )
    }

    suspend fun takePicturePreview() = resultingActivityHandler.takePicturePreview()
    suspend fun getContent(type: String) = resultingActivityHandler.getContent(type)
    suspend fun intentResult(intent: Intent) = resultingActivityHandler.intentContract(intent)
    suspend fun pickVisualMedia(pickVisualMediaRequest: PickVisualMediaRequest) =
        resultingActivityHandler.pickVisualMedia(pickVisualMediaRequest)

    suspend fun pickMultipleVisualMedia(
        pickVisualMediaRequest: PickVisualMediaRequest,
        maxItems: Int = Int.MAX_VALUE
    ) = resultingActivityHandler.pickMultipleVisualMedia(pickVisualMediaRequest, maxItems)

    suspend fun takePicture(uri: Uri) = resultingActivityHandler.takePicture(uri)
    suspend fun takeVideo(uri: Uri) = resultingActivityHandler.takeVideo(uri)
    suspend fun captureVideo(uri: Uri) = resultingActivityHandler.captureVideo(uri)
    suspend fun getMultipleContents(type: String) =
        resultingActivityHandler.getMultipleContents(type)

    suspend fun openDocument(types: Array<String>) = resultingActivityHandler.openDocument(types)
    suspend fun openMultipleDocuments(types: Array<String>) =
        resultingActivityHandler.openMultipleDocuments(types)

    suspend fun createDocument(type: String) = resultingActivityHandler.createDocument(type)
    suspend fun openDocumentTree(uri: Uri?) = resultingActivityHandler.openDocumentTree(uri)
    suspend fun <I, O> requestResult(
        createIntent: (context: Context, input: I) -> Intent,
        parseResult: (resultCode: Int, intent: Intent?) -> O,
        launch: ManagedActivityResultLauncher<I, O>.() -> Unit,
        maxTry: Int = 10,
        millis: Long = 200
    ) = resultingActivityHandler.requestResult(
        createIntent,
        parseResult,
        launch,
        maxTry, millis
    )


    suspend fun <T> dfer(
        df: Df<T>,
        tag: String,
        block: ((df: Df<T>, topic: Any, value: Any?) -> Unit)? = null
    ): T = suspendCancellableCoroutine { continuation ->
        navigation.scope { navHostController, lifecycleOwner, activityService ->
            CoroutineScope(Dispatchers.Main).launch {
                val r = activityService!!.showDf(df, tag, block)
                continuation.resume(r)
                continuation.cancel()
            }
        }
    }

    suspend fun <T> Df<T>.start(
        tag: String = "",
        block: ((df: Df<T>, topic: Any, value: Any?) -> Unit)? = null
    ) = dfer(this, tag, block)


    suspend fun String.checkPermission() = permissionHandler.check(this)

    suspend fun List<String>.checkPermission() = permissionHandler.check(*toTypedArray())

    suspend fun String.requestPermission() = permissionHandler.request(this)

    suspend fun List<String>.requestPermission() = permissionHandler.request(*this.toTypedArray())


    // abstract methods
    abstract fun onBack()
    abstract fun interCom(message: InterCom)
    abstract fun eventBusDescription(): EventBusDescription?
    abstract fun onNotification(id: Any?, arg: Any?)
    abstract fun onStartUp(route: Route? = null, arguments: Bundle? = null)

    // extensible methods
    open fun createController() = Controller(_Resolver(), _NotificationService(::onNotification))


    init {
        controller.resolver.addAll(
            DataIds.UiMessage to uiMessage
        )
        val id = interComIdentity()
        EventBus.instance.register(id, { p, t, v ->
            interCom(v as InterCom)
        }) {
            add(id)
        }
        val ed = eventBusDescription()
        if (ed?.eventBusId != null && ed.eventBusTopics != null && ed.eventBusAction != null) {
            EventBus.instance.register(ed.eventBusId, ed.eventBusAction, ed.eventBusTopics)
            eventBusDescription = ed
            eventBusRegistered = true
        }

        controller.resolver.addAll(DataIds.statusBarColor to statusBarColor)
    }


    interface LoaderInterface {
        fun clear()
        fun indeterminate()
        suspend fun success()
        suspend fun fail()
    }

    companion object {
        const val lifecycleEvent = -9999
        internal val loaderState = mutableStateOf(LoaderState.None)
    }


    override fun onCleared() {
        clearEventBusRegistration()
        super.onCleared()
    }
}

