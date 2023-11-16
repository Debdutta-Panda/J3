package com.debduttapanda.j3lib

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController

internal typealias KeyboardScope = suspend SoftwareKeyboardController.() -> Unit

internal operator fun MutableState<KeyboardScope?>.invoke(block: KeyboardScope?) {
    this.value = {
        block?.invoke(this)
        this@invoke.value = null
    }
}

internal suspend fun MutableState<KeyboardScope?>.forward(
    copyManager: SoftwareKeyboardController
) {
    this.value?.invoke(copyManager)
}

internal fun Keyboarder() = mutableStateOf<KeyboardScope?>(null)