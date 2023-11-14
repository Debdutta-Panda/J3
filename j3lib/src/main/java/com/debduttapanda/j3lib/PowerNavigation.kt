package com.debduttapanda.j3lib

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import com.debduttapanda.j3lib.df.Df
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ActivityService(
    private val context: Context
){
    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(context, message, duration).show()
    }

    fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String{
        return context.getString(id,formatArgs)
    }

    fun myAppSettingsPage(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun letConsumeContext(contextConsumer: ContextConsumer){
        contextConsumer.consume(context)
    }

    internal suspend fun <T>showDf(df: Df<T>, tag: String,block: ((df: Df<T>,topic: Any, value: Any?)->Unit)? = null): T{
        if(context !is FragmentActivity){
            throw NotFragmentActivityException()
        }
        val r = df.start(context.supportFragmentManager,tag, block)
        return r
    }

    fun contentResolver(): ContentResolver = context.contentResolver
    //fun context(): Context = context
}

class NotFragmentActivityException: Exception("The activity must be an instance of androidx.fragment.app.FragmentActivity")

typealias UINavigationScope = suspend Bundle.(NavHostController?, LifecycleOwner, ActivityService?) -> Unit

fun MutableState<UINavigationScope?>.scope(block: UINavigationScope?){
    this.value = {navHostController, lifecycleOwner, toaster ->
        withContext(Dispatchers.Main){
            block?.invoke(
                navHostController?.currentBackStackEntry?.arguments ?: Bundle(),
                navHostController,
                lifecycleOwner,
                toaster
            )
        }
        this@scope.value = null
    }
}

suspend fun MutableState<UINavigationScope?>.forward(
    navHostController: NavHostController,
    lifecycleOwner: LifecycleOwner,
    activityService: ActivityService? = null
){
    this.value?.invoke(
        navHostController.currentBackStackEntry?.arguments ?: Bundle(),
        navHostController,
        lifecycleOwner,
        activityService
    )
}

suspend fun MutableState<UINavigationScope?>.forward(
    lifecycleOwner: LifecycleOwner,
    activityService: ActivityService? = null
){
    this.value?.invoke(
        Bundle(),
        null,
        lifecycleOwner,
        activityService
    )
}

fun Navigation() = mutableStateOf<UINavigationScope?>(null)

fun NavHostController.setBack(
    route: String,
    key: String,
    value: Any?,
    last: Boolean = false
){
    (if (last){
        currentBackStack.value
            .lastOrNull{ it.destination.route == route }
    } else {
        currentBackStack.value
            .firstOrNull{ it.destination.route == route }
    })?.savedStateHandle?.set(key, value)
}

fun NavHostController.setBack(
    route: String,
    map: Map<String,Any?>,
    last: Boolean = false
){
    (if (last){
        currentBackStack.value
            .lastOrNull{ it.destination.route == route }
    } else {
        currentBackStack.value
            .firstOrNull{ it.destination.route == route }
    })?.savedStateHandle?.apply {
        map.forEach{
            set(it.key,it.value)
        }
    }
}

fun NavHostController?.arguments() = this?.currentBackStackEntry?.arguments

fun <T> NavHostController.getBack(key: String): T? {
    return currentBackStackEntry
        ?.savedStateHandle
        ?.get<T>(key)
}