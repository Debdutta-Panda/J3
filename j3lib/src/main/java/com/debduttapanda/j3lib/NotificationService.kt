package com.debduttapanda.j3lib

import com.debduttapanda.j3lib.models._NotificationService

class NotificationService(private val notificationService: _NotificationService) {
    fun notify(id: Any, arg: Any? = null) {
        notificationService.callback(id, arg)
    }
}