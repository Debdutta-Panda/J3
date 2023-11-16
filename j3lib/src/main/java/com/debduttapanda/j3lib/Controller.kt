package com.debduttapanda.j3lib

import com.debduttapanda.j3lib.models._NotificationService


class Controller(
    val resolver: _Resolver = _Resolver(),
    val notificationService: _NotificationService = _NotificationService()
) {
    fun restricted(): RestrictedController = RestrictedController(
        Resolver(resolver),
        NotificationService(notificationService)
    )
}