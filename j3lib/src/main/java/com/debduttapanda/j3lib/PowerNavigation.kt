package com.debduttapanda.j3lib

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.debduttapanda.j3lib.models.ActivityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotFragmentActivityException :
    Exception("The activity must be an instance of androidx.fragment.app.FragmentActivity")

typealias UINavigationScope = suspend Bundle.(NavHostController?, LifecycleOwner, ActivityService?) -> Unit

internal fun MutableState<UINavigationScope?>.scope(block: UINavigationScope?) {
    this@scope.value = { navHostController, lifecycleOwner, toaster ->
        block?.invoke(
            navHostController?.currentBackStackEntry?.arguments ?: Bundle(),
            navHostController,
            lifecycleOwner,
            toaster
        )
        this@scope.value = null
    }
}

internal suspend fun MutableState<UINavigationScope?>.forward(
    navHostController: NavHostController,
    lifecycleOwner: LifecycleOwner,
    activityService: ActivityService? = null
) {
    this.value?.invoke(
        navHostController.currentBackStackEntry?.arguments ?: Bundle(),
        navHostController,
        lifecycleOwner,
        activityService
    )
}

internal suspend fun MutableState<UINavigationScope?>.forward(
    lifecycleOwner: LifecycleOwner,
    activityService: ActivityService? = null
) {
    this.value?.invoke(
        Bundle(),
        null,
        lifecycleOwner,
        activityService
    )
}

internal fun Navigation() = mutableStateOf<UINavigationScope?>(null)

fun NavHostController.setBack(
    route: String,
    key: String,
    value: Any?,
    last: Boolean = false
) {
    (if (last) {
        currentBackStack.value
            .lastOrNull { it.destination.route == route }
    } else {
        currentBackStack.value
            .firstOrNull { it.destination.route == route }
    })?.savedStateHandle?.set(key, value)
}

fun NavHostController.setBack(
    route: String,
    map: Map<String, Any?>,
    last: Boolean = false
) {
    (if (last) {
        currentBackStack.value
            .lastOrNull { it.destination.route == route }
    } else {
        currentBackStack.value
            .firstOrNull { it.destination.route == route }
    })?.savedStateHandle?.apply {
        map.forEach {
            set(it.key, it.value)
        }
    }
}

fun NavHostController?.arguments() = this?.currentBackStackEntry?.arguments

fun <T> NavHostController.getBack(key: String): T? {
    return currentBackStackEntry
        ?.savedStateHandle
        ?.get<T>(key)
}