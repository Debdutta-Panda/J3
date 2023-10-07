package com.debduttapanda.j3lib

import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

class RestrictedController(
    private val _resolver: Resolver,
    private val _notifier: NotificationService
){
    fun resolver() = _resolver
    fun notifier() = _notifier
    fun floatState(key: Any): State<Float> {
        return _resolver.get(key)
    }

    fun doubleState(key: Any): State<Double> {
        return _resolver.get(key)
    }

    fun stringState(key: Any): State<String> {
        return _resolver.get(key)
    }

    fun stringResourceState(key: Any): State<StringResource> {
        return _resolver.get(key)
    }

    fun boolState(key: Any): State<Boolean> {
        return _resolver.get(key)
    }

    fun safeBoolState(key: Any): State<Boolean>? {
        return _resolver.get(key)
    }

    fun intState(key: Any): State<Int> {
        return _resolver.get(key)
    }

    fun <T>listState(key: Any): SnapshotStateList<T> {
        return _resolver.get(key)
    }

    fun <T>safeListState(key: Any): SnapshotStateList<T>? {
        return _resolver.get(key)
    }

    fun <T, E>mapState(key: Any): SnapshotStateMap<T, E> {
        return _resolver.get(key)
    }

    fun <T, E>safeMapState(key: Any): SnapshotStateMap<T, E>? {
        return _resolver.get(key)
    }

    fun <T>t(key: Any): T {
        return _resolver.get(key)
    }

    fun <T>tState(key: Any): State<T> {
        return _resolver.get(key)
    }

    fun <T>safeTState(key: Any): State<T>? {
        return _resolver.get(key)
    }
}