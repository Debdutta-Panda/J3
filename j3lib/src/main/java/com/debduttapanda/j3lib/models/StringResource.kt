package com.debduttapanda.j3lib.models

import androidx.annotation.StringRes

data class StringResource(
    @StringRes val id: Int,
    var formatArgs: List<Any> = emptyList(),
)