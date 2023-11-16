package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.debduttapanda.j3lib.models.StringResource

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
fun <T> listState(key: Any): SnapshotStateList<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T> safeListState(key: Any): SnapshotStateList<T>? {
    return LocalResolver.current.get(key)
}

@Composable
fun <T, E> mapState(key: Any): SnapshotStateMap<T, E> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T, E> safeMapState(key: Any): SnapshotStateMap<T, E>? {
    return LocalResolver.current.get(key)
}

@Composable
fun <T> t(key: Any): T {
    return LocalResolver.current.get(key)
}

@Composable
fun <T> tState(key: Any): State<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun <T> safeTState(key: Any): State<T>? {
    return LocalResolver.current.get(key)
}