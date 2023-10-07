package com.debduttapanda.j3lib

data class _NotificationService(
    var callback: (Any, Any?) -> Unit = {_,_->}
){
    fun notify(id: Any, arg: Any? = null){
        callback(id, arg)
    }
}