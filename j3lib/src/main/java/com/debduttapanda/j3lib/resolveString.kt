package com.debduttapanda.j3lib

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class StringResource(
    @StringRes val id: Int,
    var formatArgs: List<Any> = emptyList(),
)

@Composable
fun Any.resolveString(): String {
    return when (this) {
        is String -> this
        is StringResource -> {
            stringResource(id = this.id, *(this.formatArgs.toTypedArray()))
        }
        else -> "$this"
    }
}