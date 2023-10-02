package com.debduttapanda.j3lib

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.SoftwareKeyboardController

@OptIn(ExperimentalComposeUiApi::class)
typealias KeyboardScope = suspend SoftwareKeyboardController.() -> Unit

@OptIn(ExperimentalComposeUiApi::class)
operator fun MutableState<KeyboardScope?>.invoke(block: KeyboardScope?){
    this.value = {
        block?.invoke(this)
        this@invoke.value = null
    }
}

@OptIn(ExperimentalComposeUiApi::class)
suspend fun MutableState<KeyboardScope?>.forward(
    copyManager: SoftwareKeyboardController
){
    this.value?.invoke(copyManager)
}
@OptIn(ExperimentalComposeUiApi::class)
fun Keyboarder() = mutableStateOf<KeyboardScope?>(null)