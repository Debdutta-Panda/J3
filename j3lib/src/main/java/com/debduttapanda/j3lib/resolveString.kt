package com.debduttapanda.j3lib

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.debduttapanda.j3lib.models.StringResource


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