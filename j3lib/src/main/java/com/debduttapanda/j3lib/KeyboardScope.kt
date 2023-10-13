package com.debduttapanda.j3lib

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.SoftwareKeyboardController

typealias KeyboardScope = suspend SoftwareKeyboardController.() -> Unit

operator fun MutableState<KeyboardScope?>.invoke(block: KeyboardScope?){
    this.value = {
        block?.invoke(this)
        this@invoke.value = null
    }
}

suspend fun MutableState<KeyboardScope?>.forward(
    copyManager: SoftwareKeyboardController
){
    this.value?.invoke(copyManager)
}
fun Keyboarder() = mutableStateOf<KeyboardScope?>(null)