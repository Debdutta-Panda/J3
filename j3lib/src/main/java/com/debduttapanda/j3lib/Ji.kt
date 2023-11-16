package com.debduttapanda.j3lib

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.jvm.jvmErasure

object ji {
    private var _app: Application? = null
    val app: Application
        get() = _app!!
    val _resolved = mutableMapOf<Class<*>, Any>()
    val _map = mutableMapOf<Class<*>, ji.() -> Any>()
    inline fun <reified T> set(noinline r: ji.() -> Any): ji {
        val type = T::class.java
        _map[type] = r
        return this
    }

    fun app(application: Application): ji {
        _app = application
        set<Context> { app }
        return this
    }

    fun <T> get(type: Class<T>): T {
        try {
            if (_resolved.containsKey(type)) {
                return _resolved[type] as T
            }
            val r = _map[type]
            val value = r!!(ji)
            var rvalue = if (value is Function<*>) {
                ((value as (() -> Any))()) as T
            } else {
                value as T
            }
            if (value !is Function<*>) {
                _map.remove(type)
                _resolved[type] = rvalue!!
            }
            return rvalue
        } catch (e: Exception) {
            throw Exception("$type not mapped")
        }
    }

    inline fun <reified T> get(): T {
        val type = T::class.java
        try {
            if (_resolved.containsKey(type)) {
                return _resolved[type] as T
            }
            val r = _map[type]
            val value = r!!(ji)
            var rvalue = if (value is Function<*>) {
                ((value as (() -> Any))()) as T
            } else {
                value as T
            }
            if (value !is Function<*>) {
                _map.remove(type)
                _resolved[type] = rvalue!!
            }
            return rvalue
        } catch (e: Exception) {
            throw Exception("$type not mapped")
        }
    }
}


inline fun <reified VM : ViewModel> createViewModel(): ViewModel {
    val klass = VM::class
    val constructors = klass.constructors
    val c1 = constructors.first()
    return c1.call(*c1.parameters.map {
        ji.get(it.type.jvmErasure.java)
    }.toTypedArray())
}

fun <VM : ViewModel> myViewModelFactory(initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer() as T
        }
    }
}

@Suppress("MissingJvmstatic")
@Composable
inline fun <reified VM : WirelessViewModel> wvm(): WirelessViewModel = viewModel(
    factory = myViewModelFactory {
        createViewModel<VM>()
    }
)
