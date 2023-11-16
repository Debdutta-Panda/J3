package com.debduttapanda.j3lib.models

data class _NotificationService(
    var callback: (Any, Any?) -> Unit = { _, _ -> }
) {
    fun notify(id: Any, arg: Any? = null) {
        callback(id, arg)
    }
}