package com.debduttapanda.j3lib.models

internal data class EventBusCallback(
    val topics: MutableList<String>,
    val action: (String, String, Any?) -> Unit
)