package com.debduttapanda.j3lib

class NotificationService(private val notificationService: _NotificationService){
    fun notify(id: Any, arg: Any? = null){
        notificationService.callback(id, arg)
    }
}