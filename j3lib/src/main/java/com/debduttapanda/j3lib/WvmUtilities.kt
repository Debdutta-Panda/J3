package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.debduttapanda.j3lib.models._NotificationService

@Composable
fun suffix(): String {
    return LocalSuffix.current
}

@Composable
fun controller(): RestrictedController {
    return LocalController.current
}

@Composable
fun suffix(
    suffix: String,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        values = arrayOf(LocalSuffix provides "${LocalSuffix.current}$suffix"),
        content
    )
}


@Composable
fun notifier(): _NotificationService {
    return LocalNotificationService.current
}


@Composable
fun rememberNotifier(): NotificationService {
    val controller = LocalController.current
    return remember(controller) { controller.notifier() }
}

@Composable
fun ControlledBy(
    controller: RestrictedController,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalController provides controller
    ) {
        content()
    }
}

@Composable
fun ControlledById(
    id: Any,
    content: @Composable () -> Unit,
) {
    val controller = LocalController.current.resolver().get<RestrictedController>(id)
    CompositionLocalProvider(
        LocalController provides controller
    ) {
        content()
    }
}

@Composable
fun SafeControlledById(
    id: Any,
    content: @Composable () -> Unit,
) {
    val controller = LocalController.current.resolver().getOrNull<RestrictedController>(id)
    if (controller != null) {
        CompositionLocalProvider(
            LocalController provides controller
        ) {
            content()
        }
    }
}