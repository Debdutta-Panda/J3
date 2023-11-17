package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.debduttapanda.j3lib.models.StringResource

@Composable
fun rememberFloatState(id: Any): State<Float> {
    val controller = LocalController.current
    return remember(controller) {
        controller.floatState(id)
    }
}

@Composable
fun rememberUiMessage(): State<Event<Any?>?> {
    val controller = LocalController.current
    return remember(controller) {
        controller.tState(DataIds.UiMessage)
    }
}

@Composable
fun rememberDoubleState(id: Any): State<Double> {
    val controller = LocalController.current
    return remember(controller) {
        controller.doubleState(id)
    }
}

@Composable
fun rememberStringState(id: Any): State<String> {
    val controller = LocalController.current
    return remember(controller) {
        controller.stringState(id)
    }
}


@Composable
fun rememberStringResourceState(id: Any): State<StringResource> {
    val controller = LocalController.current
    return remember(controller) {
        controller.stringResourceState(id)
    }
}

@Composable
fun rememberBoolState(id: Any): State<Boolean> {
    val controller = LocalController.current
    return remember(controller) {
        controller.boolState(id)
    }
}

@Composable
fun rememberSafeBoolState(id: Any): State<Boolean>? {
    val controller = LocalController.current
    return remember(controller) {
        controller.safeBoolState(id)
    }
}

@Composable
fun rememberIntState(id: Any): State<Int> {
    val controller = LocalController.current
    return remember(controller) {
        controller.intState(id)
    }
}

@Composable
fun <T> rememberListState(id: Any): SnapshotStateList<T> {
    val controller = LocalController.current
    return remember(controller) {
        controller.listState(id)
    }
}

@Composable
fun <T> rememberSafeListState(id: Any): SnapshotStateList<T>? {
    val controller = LocalController.current
    return remember(controller) {
        controller.safeListState(id)
    }
}

@Composable
fun <T, E> rememberMapState(id: Any): SnapshotStateMap<T, E> {
    val controller = LocalController.current
    return remember(controller) {
        controller.mapState(id)
    }
}

@Composable
fun <T, E> rememberSafeMapState(id: Any): SnapshotStateMap<T, E>? {
    val controller = LocalController.current
    return remember(controller) {
        controller.safeMapState(id)
    }
}

@Composable
fun <T> rememberT(id: Any): T {
    val controller = LocalController.current
    return remember(controller) {
        controller.t(id)
    }
}

@Composable
fun <T> rememberTState(id: Any): State<T> {
    val controller = LocalController.current
    return remember(controller) {
        controller.tState(id)
    }
}

@Composable
fun <T> rememberSafeTState(id: Any): State<T>? {
    val controller = LocalController.current
    return remember(controller) {
        controller.safeTState(id)
    }
}